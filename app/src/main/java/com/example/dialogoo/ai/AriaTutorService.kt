package com.example.dialogoo.ai

import com.example.dialogoo.model.Dialogue

data class ChatMessage(
    val id: String,
    val sender: String,
    val text: String,
    val translation: String = "",
    val correction: String = "",
    val suggestedReplies: List<String> = emptyList(),
    val timestamp: Long = System.currentTimeMillis()
) {
    val isStudent: Boolean get() = sender == "You"
}

class AriaTutorService {

    fun generateInitialGreeting(dialogue: Dialogue): ChatMessage {
        return ChatMessage(
            id = "aria-intro",
            sender = "Aria AI",
            text = "Hi there! I'm Aria, your English conversation coach. We just practiced '${dialogue.title}'. Let's chat freely in this context! How would you start this conversation?",
            translation = "Olá! Sou a Aria, sua treinadora de conversação em inglês. Acabamos de praticar '${dialogue.title}'. Vamos conversar livremente nesse contexto! Como você começaria essa conversa?",
            suggestedReplies = listOf(
                "Could you help me practice ordering food?",
                "I want to practice asking for directions.",
                "Let's simulate a real work meeting!"
            )
        )
    }

    fun respondToStudent(dialogue: Dialogue, studentInput: String): ChatMessage {
        val lower = studentInput.lowercase().trim()

        val (reply, translation, correction, suggestions) = when {
            lower.contains("coffee") || lower.contains("latte") || lower.contains("order") -> {
                Quadruple(
                    "Great choice! Would you like that iced or hot, and do you prefer oat milk or regular dairy?",
                    "Ótima escolha! Você gostaria gelado ou quente, e prefere leite de aveia ou leite tradicional?",
                    if (lower.contains("i want")) "💡 Tip: 'I would like' or 'Could I get' sounds much more polite than 'I want' in American coffee shops!" else "",
                    listOf(
                        "I'd like an iced latte with oat milk, please.",
                        "Hot with regular milk and no sugar, thank you.",
                        "Can you add a pump of hazelnut syrup?"
                    )
                )
            }
            lower.contains("direction") || lower.contains("subway") || lower.contains("station") || lower.contains("where") -> {
                Quadruple(
                    "You're in luck! Walk two blocks straight down 5th Avenue and make a quick left. The entrance is right by the pharmacy.",
                    "Você está com sorte! Caminhe dois quarteirões reto na 5ª Avenida e vire à esquerda. A entrada fica ao lado da farmácia.",
                    if (lower.contains("where is") && !lower.contains("excuse me")) "💡 Polite tip: Preceding your question with 'Excuse me' makes native speakers much more receptive!" else "",
                    listOf(
                        "Thank you so much! Is it within walking distance?",
                        "Do I need to buy a transit metro card beforehand?",
                        "Which line goes toward Central Park?"
                    )
                )
            }
            lower.contains("job") || lower.contains("interview") || lower.contains("experience") || lower.contains("developer") -> {
                Quadruple(
                    "That's impressive technical background. How do you handle disagreements on software architectural choices with other engineers?",
                    "Essa é uma bagagem técnica impressionante. Como você lida com discordâncias sobre escolhas arquiteturais com outros engenheiros?",
                    "",
                    listOf(
                        "I rely on data, prototypes, and open team benchmarks.",
                        "I listen actively and seek consensus aligned with business goals.",
                        "We run A/B testing and review code constructively."
                    )
                )
            }
            lower.contains("hello") || lower.contains("hi") || lower.contains("good morning") -> {
                Quadruple(
                    "Hello! It is wonderful to practice with you. What specific phrase from this lesson would you like to master today?",
                    "Olá! É maravilhoso praticar com você. Qual frase específica desta lição você gostaria de dominar hoje?",
                    "",
                    listOf(
                        "I want to practice my pronunciation on the key lines.",
                        "Can you test my vocabulary with a quick quiz?",
                        "Let's roleplay the situation from the beginning."
                    )
                )
            }
            else -> {
                Quadruple(
                    "Nicely phrased! That sounds very natural. Let's keep exploring this topic: what would you say next if the other person agreed with you?",
                    "Muito bem formulado! Soa bem natural. Vamos continuar explorando: o que você diria a seguir se a outra pessoa concordasse com você?",
                    "",
                    listOf(
                        "I would confirm the meeting time and thank them.",
                        "I would ask for the receipt and pay by card.",
                        "I'd ask them to send the details over email."
                    )
                )
            }
        }

        return ChatMessage(
            id = "aria-${System.currentTimeMillis()}",
            sender = "Aria AI",
            text = reply,
            translation = translation,
            correction = correction,
            suggestedReplies = suggestions
        )
    }

    private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
}
