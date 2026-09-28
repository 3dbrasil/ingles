import { createServerFn } from "@tanstack/react-start";
import { generateText, type LanguageModel } from "ai";
import { createOpenAICompatible } from "@ai-sdk/openai-compatible";
import { z } from "zod";

const TranslationInput = z.object({
  lines: z.array(z.object({
    id: z.string().trim().min(1).max(160),
    text: z.string().trim().min(1).max(800),
  })).min(1).max(40),
});

function parseTranslations(raw: string): Record<string, string> {
  const cleaned = raw.trim().replace(/^```(?:json)?/i, "").replace(/```$/i, "").trim();
  const jsonMatch = cleaned.match(/\{[\s\S]*\}/);
  if (!jsonMatch) return {};
  try {
    const parsed = JSON.parse(jsonMatch[0]);
    if (!parsed || typeof parsed !== "object" || Array.isArray(parsed)) return {};
    return Object.fromEntries(
      Object.entries(parsed)
        .filter(([, value]) => typeof value === "string" && value.trim())
        .map(([key, value]) => [key, String(value).trim()]),
    );
  } catch (_) {
    return {};
  }
}

// Public translation endpoint — no auth required.
export const translateLessonLines = createServerFn({ method: "POST" })
  .inputValidator((input) => TranslationInput.parse(input))
  .handler(async ({ data }) => {
    const payload = data.lines.map((line) => ({ id: line.id, text: line.text }));

    const providers: { name: string; getModel: () => LanguageModel }[] = [];

    const geminiKey = process.env.GEMINI_API_KEY;
    if (geminiKey) {
      providers.push({
        name: "Gemini",
        getModel: () =>
          createOpenAICompatible({
            name: "gemini",
            baseURL: "https://generativelanguage.googleapis.com/v1beta/openai/",
            headers: { Authorization: `Bearer ${geminiKey}` },
          })("gemini-3.5-flash"),
      });
    }

    const groqKey = process.env.GROQ_API_KEY;
    if (groqKey) {
      providers.push({
        name: "Groq",
        getModel: () =>
          createOpenAICompatible({
            name: "groq",
            baseURL: "https://api.groq.com/openai/v1",
            headers: { Authorization: `Bearer ${groqKey}` },
          })("llama-3.3-70b-versatile"),
      });
    }

    const nvidiaKey = process.env.NVIDIA_API_KEY;
    if (nvidiaKey) {
      providers.push({
        name: "NVIDIA",
        getModel: () =>
          createOpenAICompatible({
            name: "nvidia",
            baseURL: "https://integrate.api.nvidia.com/v1",
            headers: { Authorization: `Bearer ${nvidiaKey}` },
          })("meta/llama-3.3-70b-instruct"),
      });
    }

    if (!providers.length) throw new Error("Configure uma chave de IA (Gemini, Groq ou NVIDIA) para tradução.");
    let lastError: unknown;
    for (const provider of providers) {
      try {
        const result = await generateText({
          model: provider.getModel(),
          temperature: 0.1,
          system: "You translate English lesson dialogue lines into natural Brazilian Portuguese. Return only valid JSON.",
          prompt: `Translate each text to Brazilian Portuguese, keeping the same meaning and tone. Return exactly one JSON object where each key is the id and each value is the Portuguese translation. Do not include markdown.\n\n${JSON.stringify(payload)}`,
        });
        const translations = parseTranslations(result.text);
        if (Object.keys(translations).length) return translations;
        lastError = new Error(`${provider.name}: resposta vazia`);
      } catch (error) { lastError = error; }
    }
    throw lastError ?? new Error("Tradução indisponível no momento.");
  });
