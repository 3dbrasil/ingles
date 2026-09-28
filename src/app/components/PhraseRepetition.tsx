import { useState, useEffect, useMemo, useRef, useCallback } from 'react';
import {
  Brain, Volume2, ChevronRight, CheckCircle2, RotateCcw,
  Sparkles, Layers, VolumeX, Eye, HelpCircle, ThumbsUp, Flame
} from 'lucide-react';
import { Dialogue } from '../types';
import { speakAmericanEnglish } from '../utils/speech';
import {
  classifyDifficulty, getState, recordAnkiResult, markLearned,
  speakerAvatar, type AnkiRating, type SrsLevel
} from '../utils/srs';

interface Phrase {
  text: string;
  translation: string;
  pronunciationGuide?: string;
  title: string;
  level: string;
  speaker: string;
}

interface Props {
  dialogues: Dialogue[];
  completedDialogues: string[];
  onAddXp: (xp: number) => void;
}

type LevelFilter = 'all' | SrsLevel;

export default function PhraseRepetition({ dialogues, completedDialogues, onAddXp }: Props) {
  const [started, setStarted] = useState(false);
  const [preparing, setPreparing] = useState(false);
  const [phrases, setPhrases] = useState<Phrase[]>([]);
  const [idx, setIdx] = useState(0);
  const [revealed, setRevealed] = useState(false);
  const [spk, setSpk] = useState(false);
  const [rate, setRate] = useState(0.85);
  const [srsTick, setSrsTick] = useState(0);
  const [reviewed, setReviewed] = useState(0);
  const [filter, setFilter] = useState<LevelFilter>('all');
  const [autoPlayAudio, setAutoPlayAudio] = useState(true);
  const [sourceMode, setSourceMode] = useState<'learned' | 'all'>('learned');
  const playSeqRef = useRef(0);

  // Build flashcards queue
  useEffect(() => {
    if (!started) return;
    let cancelled = false;
    const yieldToBrowser = () => new Promise<void>((resolve) => setTimeout(resolve, 0));

    const buildQueue = async () => {
      setPreparing(true);
      const all: Phrase[] = [];
      const seen = new Set<string>();

      // Filter dialogues based on user preference
      let source = dialogues;
      if (sourceMode === 'learned') {
        const learned = dialogues.filter(d => completedDialogues.includes(d.id));
        source = learned.length > 0 ? learned : dialogues.filter(d => d.level === 'A1');
      }

      for (let i = 0; i < source.length; i++) {
        for (const l of source[i].lines) {
          if (!l.text || seen.has(l.text.trim())) continue;
          seen.add(l.text.trim());
          all.push({
            text: l.text.trim(),
            translation: l.translation || '',
            pronunciationGuide: l.pronunciationGuide,
            title: source[i].title,
            level: source[i].level,
            speaker: l.speaker,
          });
        }
        if (i % 12 === 0) await yieldToBrowser();
        if (cancelled) return;
      }

      const now = Date.now();
      const evaluated: { p: Phrase; s: ReturnType<typeof getState> }[] = [];
      for (let i = 0; i < all.length; i++) {
        const p = all[i];
        const s = getState(p.text);
        const level = s.level || classifyDifficulty(p.text);
        if (filter === 'all' || level === filter) {
          evaluated.push({ p, s });
        }
        if (i % 250 === 0) await yieldToBrowser();
        if (cancelled) return;
      }

      // Prioritize phrases that are due for review or not yet learned
      const due = evaluated
        .filter(x => x.s.nextReview <= now || !x.s.learned)
        .sort((a, b) => a.s.nextReview - b.s.nextReview);

      const queue = due.length ? due : evaluated;
      setPhrases(queue.map(x => x.p));
      setIdx(0);
      setRevealed(false);
      setPreparing(false);
    };

    buildQueue();
    return () => {
      cancelled = true;
      if ('speechSynthesis' in window) window.speechSynthesis.cancel();
    };
  }, [started, dialogues, completedDialogues, filter, sourceMode]);

  const cur = phrases[idx];
  const srs = useMemo(() => (cur ? getState(cur.text) : null), [cur, srsTick]);
  const avatar = cur ? speakerAvatar(cur.speaker) : { initial: '?', color: 'bg-slate-600' };

  // Play audio function with safety check
  const playAudio = useCallback(async (textToPlay?: string) => {
    const text = textToPlay || cur?.text;
    if (!text || spk) return;
    const currentSeq = ++playSeqRef.current;
    setSpk(true);
    try {
      await speakAmericanEnglish(text, undefined, rate);
    } catch (_) {
      // Ignored
    } finally {
      if (currentSeq === playSeqRef.current) {
        setSpk(false);
      }
    }
  }, [cur?.text, spk, rate]);

  // When card index changes, reset reveal state and automatically play audio FIRST (Anki sound-first)
  useEffect(() => {
    setRevealed(false);
    setSpk(false);
    if ('speechSynthesis' in window) window.speechSynthesis.cancel();

    if (started && cur && autoPlayAudio) {
      const timer = setTimeout(() => {
        playAudio(cur.text);
      }, 250);
      return () => clearTimeout(timer);
    }
  }, [idx, started, cur?.text, autoPlayAudio]); // eslint-disable-line react-hooks/exhaustive-deps

  const handleRate = (rating: AnkiRating) => {
    if (!cur) return;
    recordAnkiResult(cur.text, rating);
    setSrsTick(x => x + 1);
    setReviewed(r => r + 1);

    if (rating === 'easy') {
      onAddXp(6);
    } else if (rating === 'hard') {
      onAddXp(3);
    } else {
      onAddXp(1);
    }

    // Move to next card
    if (phrases.length > 0) {
      setIdx(p => (p + 1) % phrases.length);
    }
  };

  if (!started) {
    const learnedCount = dialogues.filter(d => completedDialogues.includes(d.id)).length;
    return (
      <div className="max-w-xl mx-auto space-y-6 animate-fade-in pb-16">
        <div className="text-center sm:text-left">
          <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-cyan-500/10 border border-cyan-500/20 text-[10px] font-bold text-cyan-400 uppercase tracking-widest mb-2">
            <Brain className="w-3.5 h-3.5" /> Método Anki Espaçado
          </div>
          <h1 className="text-3xl font-bold text-white tracking-tight" style={{ fontFamily: "'Playfair Display', serif" }}>
            Estudo das Frases
          </h1>
          <p className="text-sm text-slate-400 mt-1 leading-relaxed">
            Treine seu ouvido primeiro com som natural. Tente entender a frase e classifique seu aprendizado.
          </p>
        </div>

        {/* Source selector */}
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
          <button
            onClick={() => setSourceMode('learned')}
            className={`p-4 rounded-2xl border text-left transition-all ${
              sourceMode === 'learned'
                ? 'bg-gradient-to-br from-cyan-500/15 to-[#00D4A0]/15 border-cyan-400/40 text-white shadow-[0_0_20px_rgba(6,182,212,0.2)]'
                : 'bg-white/[0.02] border-white/5 text-slate-400 hover:border-white/15 hover:text-slate-200'
            }`}
          >
            <div className="flex items-center justify-between mb-1">
              <span className="text-xs font-bold uppercase tracking-wider">Apenas Lições Aprendidas</span>
              <CheckCircle2 className={`w-4 h-4 ${sourceMode === 'learned' ? 'text-[#00D4A0]' : 'text-slate-500'}`} />
            </div>
            <p className="text-xs text-slate-400 leading-relaxed">
              {learnedCount} liçõe{learnedCount === 1 ? 's' : 's'} marcada{learnedCount === 1 ? 's' : 's'} com check no Mapa.
            </p>
          </button>

          <button
            onClick={() => setSourceMode('all')}
            className={`p-4 rounded-2xl border text-left transition-all ${
              sourceMode === 'all'
                ? 'bg-gradient-to-br from-cyan-500/15 to-[#00D4A0]/15 border-cyan-400/40 text-white shadow-[0_0_20px_rgba(6,182,212,0.2)]'
                : 'bg-white/[0.02] border-white/5 text-slate-400 hover:border-white/15 hover:text-slate-200'
            }`}
          >
            <div className="flex items-center justify-between mb-1">
              <span className="text-xs font-bold uppercase tracking-wider">Todas as Frases</span>
              <Layers className={`w-4 h-4 ${sourceMode === 'all' ? 'text-cyan-400' : 'text-slate-500'}`} />
            </div>
            <p className="text-xs text-slate-400 leading-relaxed">
              Inclui frases de todos os diálogos cadastrados no app.
            </p>
          </button>
        </div>

        {/* How it works preview card */}
        <div className="bg-slate-900/60 rounded-3xl border border-white/10 p-6 sm:p-8 space-y-6 backdrop-blur-xl">
          <div className="flex items-center gap-4">
            <div className="w-12 h-12 rounded-2xl bg-cyan-500/15 border border-cyan-500/30 flex items-center justify-center text-cyan-400 shrink-0">
              <Volume2 className="w-6 h-6 animate-pulse" />
            </div>
            <div>
              <h2 className="text-base font-semibold text-white">Como funciona o treino:</h2>
              <p className="text-xs text-slate-400 mt-0.5">Dinâmica comprovada de repetição espaçada</p>
            </div>
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-3 gap-3 text-xs">
            <div className="p-3.5 rounded-2xl bg-white/[0.02] border border-white/5 space-y-1">
              <span className="text-[#2A7FFF] font-bold">1. Ouça primeiro</span>
              <p className="text-slate-400 text-[11px] leading-relaxed">A frase toca automaticamente sem texto na tela.</p>
            </div>
            <div className="p-3.5 rounded-2xl bg-white/[0.02] border border-white/5 space-y-1">
              <span className="text-[#00D4A0] font-bold">2. Revele a resposta</span>
              <p className="text-slate-400 text-[11px] leading-relaxed">Veja o texto em inglês e a tradução em português.</p>
            </div>
            <div className="p-3.5 rounded-2xl bg-white/[0.02] border border-white/5 space-y-1">
              <span className="text-amber-400 font-bold">3. Avalie seu nível</span>
              <p className="text-slate-400 text-[11px] leading-relaxed">Escolha Fácil, Difícil ou Não Entendi.</p>
            </div>
          </div>

          <button
            onClick={() => setStarted(true)}
            className="w-full py-4 rounded-2xl bg-gradient-to-r from-[#2A7FFF] to-[#00D4A0] text-slate-950 font-bold text-sm hover:scale-[1.02] active:scale-[0.98] transition-all flex items-center justify-center gap-2 shadow-[0_10px_35px_rgba(0,212,160,0.35)]"
          >
            <Volume2 className="w-5 h-5" /> Iniciar Estudo com Som
          </button>
        </div>
      </div>
    );
  }

  if (preparing) {
    return (
      <div className="max-w-xl mx-auto space-y-5 animate-fade-in text-center py-16">
        <Brain className="w-12 h-12 text-cyan-400 mx-auto animate-bounce" />
        <p className="text-sm font-semibold text-slate-200">Organizando cards no sistema Anki...</p>
      </div>
    );
  }

  if (!phrases.length) {
    return (
      <div className="text-center py-16 max-w-md mx-auto space-y-4 animate-fade-in">
        <Brain className="w-12 h-12 text-slate-500 mx-auto" />
        <h3 className="text-lg font-bold text-white">Nenhuma frase encontrada</h3>
        <p className="text-xs text-slate-400 leading-relaxed">
          {sourceMode === 'learned'
            ? 'Você ainda não marcou nenhuma lição como aprendida no Mapa. Marque o check no Mapa ou selecione "Todas as Frases".'
            : 'Nenhuma frase disponível para este filtro.'}
        </p>
        <button
          onClick={() => {
            setSourceMode('all');
            setStarted(false);
          }}
          className="px-5 py-2.5 rounded-full bg-cyan-500 text-slate-950 font-bold text-xs hover:bg-cyan-400 transition"
        >
          Voltar às opções
        </button>
      </div>
    );
  }

  return (
    <div className="max-w-xl mx-auto space-y-5 animate-fade-in pb-20">
      {/* Top status bar */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3">
        <div>
          <div className="flex items-center gap-2">
            <span className="text-xs font-bold uppercase tracking-wider text-cyan-400 flex items-center gap-1.5">
              <Brain className="w-3.5 h-3.5" /> Anki Flashcards
            </span>
            <span className="text-[10px] text-slate-500">·</span>
            <span className="text-[11px] font-semibold text-slate-300">
              Card {idx + 1} de {phrases.length}
            </span>
          </div>
          <p className="text-[11px] text-slate-400 mt-0.5">
            Revisados hoje: {reviewed} · {srs?.learned ? '⭐ Aprendida' : '🌱 Em estudo'}
          </p>
        </div>

        {/* Level filter & sound switch */}
        <div className="flex items-center gap-2">
          <button
            onClick={() => setAutoPlayAudio(!autoPlayAudio)}
            title={autoPlayAudio ? 'Áudio automático ativado' : 'Áudio automático desativado'}
            className={`flex items-center gap-1 px-2.5 py-1 rounded-lg text-[10px] font-semibold border transition ${
              autoPlayAudio
                ? 'bg-cyan-500/15 border-cyan-500/30 text-cyan-300'
                : 'bg-slate-900 border-white/10 text-slate-500'
            }`}
          >
            {autoPlayAudio ? <Volume2 className="w-3 h-3" /> : <VolumeX className="w-3 h-3" />}
            Auto-som
          </button>

          <button
            onClick={() => setStarted(false)}
            className="px-3 py-1 rounded-lg text-[10px] font-bold text-slate-400 hover:text-white bg-slate-900/60 border border-white/5"
          >
            Sair
          </button>
        </div>
      </div>

      {/* Main Flashcard */}
      {cur && (
        <div className="relative rounded-3xl bg-slate-900/70 border border-white/10 shadow-[0_15px_50px_rgba(0,0,0,0.5)] overflow-hidden backdrop-blur-xl">
          {/* Progress bar inside card */}
          <div className="h-1 bg-white/5">
            <div
              className="h-full bg-gradient-to-r from-[#2A7FFF] to-[#00D4A0] transition-all duration-300"
              style={{ width: `${((idx + 1) / phrases.length) * 100}%` }}
            />
          </div>

          <div className="p-6 sm:p-8 space-y-6">
            {/* Speaker context */}
            <div className="flex items-center justify-between gap-3">
              <div className="flex items-center gap-2.5 min-w-0">
                <div className={`w-8 h-8 rounded-full ${avatar.color} flex items-center justify-center text-white font-bold text-xs shadow-md`}>
                  {avatar.initial}
                </div>
                <div className="min-w-0">
                  <p className="text-xs font-semibold text-white truncate">{cur.speaker}</p>
                  <p className="text-[10px] text-slate-400 truncate">{cur.level} · {cur.title}</p>
                </div>
              </div>

              {/* Speed controls */}
              <div className="flex items-center bg-slate-950/80 rounded-lg p-0.5 border border-white/10">
                {[
                  { v: 0.65, l: '0.7x' },
                  { v: 0.85, l: '1x' },
                  { v: 1.05, l: '1.2x' },
                ].map(o => (
                  <button
                    key={o.v}
                    onClick={() => {
                      setRate(o.v);
                      playAudio();
                    }}
                    className={`text-[9px] font-bold px-2 py-0.5 rounded transition ${
                      rate === o.v ? 'bg-cyan-500 text-slate-950' : 'text-slate-400 hover:text-white'
                    }`}
                  >
                    {o.l}
                  </button>
                ))}
              </div>
            </div>

            {/* Sound Hero Button */}
            <button
              onClick={() => playAudio()}
              disabled={spk}
              className={`w-full py-8 sm:py-10 rounded-2xl border-2 transition-all flex flex-col items-center justify-center gap-3 ${
                spk
                  ? 'border-cyan-400 bg-cyan-500/15 shadow-[0_0_30px_rgba(6,182,212,0.35)]'
                  : 'border-white/10 hover:border-cyan-400/60 bg-white/[0.02] hover:bg-white/[0.04]'
              }`}
            >
              <div className={`w-16 h-16 rounded-full flex items-center justify-center transition-all ${
                spk ? 'bg-cyan-500 text-slate-950 scale-110' : 'bg-cyan-500/10 text-cyan-400 border border-cyan-500/20'
              }`}>
                <Volume2 className={`w-8 h-8 ${spk ? 'animate-pulse' : ''}`} />
              </div>
              <div className="text-center">
                <span className="text-xs font-bold text-white uppercase tracking-wider block">
                  {spk ? 'Ouvindo agora...' : 'Tocar áudio da frase'}
                </span>
                <span className="text-[10px] text-slate-400">
                  Ouça com atenção antes de virar o card
                </span>
              </div>
            </button>

            {/* Hidden state / Revealed state */}
            {!revealed ? (
              <button
                onClick={() => {
                  setRevealed(true);
                  playAudio();
                }}
                className="w-full py-4 rounded-2xl bg-white/[0.03] hover:bg-white/[0.06] border border-white/10 text-white font-semibold text-xs transition-all flex items-center justify-center gap-2 hover:border-cyan-400/40"
              >
                <Eye className="w-4 h-4 text-cyan-400" />
                <span>Mostrar resposta (Inglês & Português)</span>
              </button>
            ) : (
              <div className="space-y-4 animate-fade-in">
                {/* English card */}
                <div className="p-5 rounded-2xl bg-white/[0.03] border border-white/10 text-center space-y-2">
                  <span className="text-[10px] uppercase font-bold tracking-widest text-[#2A7FFF]">Inglês</span>
                  <p className="text-xl sm:text-2xl font-bold text-white leading-relaxed">
                    "{cur.text}"
                  </p>
                  {cur.pronunciationGuide && (
                    <p className="text-[11px] text-cyan-300/80 font-mono">
                      🔊 {cur.pronunciationGuide}
                    </p>
                  )}
                </div>

                {/* Translation card */}
                <div className="p-4 rounded-2xl bg-emerald-500/10 border border-emerald-500/20 text-center space-y-1">
                  <span className="text-[10px] uppercase font-bold tracking-widest text-[#00D4A0]">Tradução</span>
                  <p className="text-sm font-medium text-emerald-100">
                    {cur.translation || 'Sem tradução'}
                  </p>
                </div>

                {/* Anki 3-level evaluation buttons */}
                <div className="pt-2">
                  <p className="text-[11px] font-semibold text-slate-400 text-center mb-3">
                    Como foi para você?
                  </p>
                  <div className="grid grid-cols-3 gap-2">
                    {/* Não entendi */}
                    <button
                      onClick={() => handleRate('dont_know')}
                      className="py-3 px-2 rounded-2xl bg-red-500/15 hover:bg-red-500/25 border border-red-500/30 text-red-300 font-bold text-xs flex flex-col items-center justify-center gap-1 transition-all active:scale-95"
                    >
                      <HelpCircle className="w-4 h-4 text-red-400" />
                      <span>Não entendi</span>
                      <span className="text-[9px] text-red-400/80 font-normal">Rever logo</span>
                    </button>

                    {/* Difícil */}
                    <button
                      onClick={() => handleRate('hard')}
                      className="py-3 px-2 rounded-2xl bg-amber-500/15 hover:bg-amber-500/25 border border-amber-500/30 text-amber-300 font-bold text-xs flex flex-col items-center justify-center gap-1 transition-all active:scale-95"
                    >
                      <RotateCcw className="w-4 h-4 text-amber-400" />
                      <span>Difícil</span>
                      <span className="text-[9px] text-amber-400/80 font-normal">+3 XP</span>
                    </button>

                    {/* Fácil */}
                    <button
                      onClick={() => handleRate('easy')}
                      className="py-3 px-2 rounded-2xl bg-[#00D4A0]/15 hover:bg-[#00D4A0]/25 border border-[#00D4A0]/40 text-[#00D4A0] font-bold text-xs flex flex-col items-center justify-center gap-1 transition-all active:scale-95"
                    >
                      <ThumbsUp className="w-4 h-4 text-[#00D4A0]" />
                      <span>Fácil</span>
                      <span className="text-[9px] text-[#00D4A0]/80 font-normal">+6 XP</span>
                    </button>
                  </div>
                </div>
              </div>
            )}
          </div>

          {/* Footer bar */}
          <div className="px-6 py-3 bg-black/30 border-t border-white/5 flex items-center justify-between text-[11px] text-slate-400">
            <div className="flex items-center gap-2">
              <button
                onClick={() => {
                  if (cur) markLearned(cur.text, !srs?.learned);
                  setSrsTick(x => x + 1);
                }}
                className={`flex items-center gap-1 text-[11px] transition ${
                  srs?.learned ? 'text-[#00D4A0]' : 'text-slate-500 hover:text-slate-300'
                }`}
              >
                <CheckCircle2 className="w-3.5 h-3.5" />
                <span>{srs?.learned ? 'Marcada como aprendida' : 'Marcar como aprendida'}</span>
              </button>
            </div>

            <button
              onClick={() => setIdx(p => (p + 1) % phrases.length)}
              className="flex items-center gap-1 text-slate-400 hover:text-white transition"
            >
              <span>Pular card</span>
              <ChevronRight className="w-3.5 h-3.5" />
            </button>
          </div>
        </div>
      )}
    </div>
  );
}
