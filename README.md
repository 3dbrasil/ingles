# Dialogoo — Speak Native Android Application

Dialogoo is a native Android application built with Kotlin, Jetpack Compose, and Material 3, designed for immersive English language acquisition through interactive dialogues, phonetics, spaced repetition, and real-time pronunciation practice.

## Core Features

- **CEFR Journey Trilha (Duolingo-style Map)**:
  - Levels from A1 (Iniciante) to C2 (Maestria) with structured dialogues.
  - Interactive progression tracking with lesson unlock mechanism, streak counts, and XP awards.
- **Dialogue Practice**:
  - Immersive chat-bubble UI with distinct speaker avatars and color palettes.
  - Line-by-line native American English Text-to-Speech (TTS) with configurable speed (0.6x, 1.0x, 1.3x).
  - Tap-to-reveal Portuguese translations and phonetic pronunciation guides.
  - Key vocabulary chips and automatic dialogue playback mode.
- **Cumulative Arena**:
  - Random real-world speech challenges (hotel check-in, job interview, tech support, airport boarding).
  - Speech recognition via microphone with word-by-word accuracy scoring (Levenshtein similarity) and actionable feedback.
- **Spaced Repetition System (SRS)**:
  - Smart review queue with self-grading (Easy, Medium, Hard).
  - Automatic interval scheduling and retention tracking.
- **Aria AI Conversational Tutor**:
  - Contextual dialogue roleplay with immediate polite corrections, natural replies, and suggested prompts.
- **Offline-First & Local Access**:
  - Local student/admin access loaded immediately on startup without blocking on remote auth.
  - Robust Room SQLite local database persistence for dialogues, user stats, badges, and SRS phrases.

## Architecture & Tech Stack

- **UI**: Jetpack Compose, Material 3, Edge-to-Edge (`enableEdgeToEdge()`).
- **Language**: Kotlin 2.0.21.
- **Local Database**: Room 2.6.1 (`DialogooDatabase`, `DialogueDao`, `UserStatsDao`, `SrsDao`).
- **Audio & Speech**: Android `TextToSpeech` and `SpeechRecognizer`.
- **Design System**: Oceanic Bento theme (`#020617` deep navy night, `#00D4A0` emerald/teal, `#2A7FFF` ocean blue).
