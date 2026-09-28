//#region node_modules/.nitro/vite/services/ssr/assets/srs-DIXlKmqi.js
var STORAGE_KEY = "srs_state_v1";
var HOUR = 36e5;
var DAY = 24 * HOUR;
var INTERVALS = {
	easy: [
		4 * HOUR,
		1 * DAY,
		3 * DAY,
		7 * DAY,
		14 * DAY
	],
	medium: [
		2 * HOUR,
		12 * HOUR,
		2 * DAY,
		5 * DAY,
		10 * DAY
	],
	hard: [
		1 * HOUR,
		6 * HOUR,
		1 * DAY,
		3 * DAY,
		7 * DAY
	]
};
4 * HOUR, 2 * HOUR, 1 * HOUR;
var stateCache = null;
function isSrsLevel(value) {
	return value === "easy" || value === "medium" || value === "hard";
}
var TOP_500 = new Set("the be to of and a in that have i it for not on with he as you do at this but his by from they we say her she or an will my one all would there their what so up out if about who get which go me when make can like time no just him know take people into year your good some could them see other than then now look only come its over think also back after use two how our work first well way even new want because any these give day most us hello hi how are good morning night thanks please yes ok".split(" "));
function classifyDifficulty(text) {
	const t = text.toLowerCase().trim();
	const words = t.split(/\s+/).filter(Boolean);
	const len = words.length;
	if (/\b(had|have|has)\s+\w+(ed|en)\b/.test(t)) return "hard";
	if (/\b(would|could|should|might)\s+have\b/.test(t)) return "hard";
	if (/\bif\b.+\b(would|could)\b/.test(t)) return "hard";
	if (len >= 9) return "hard";
	if (/\bwill\b|\bgoing to\b/.test(t)) return "medium";
	if (/\b\w+ed\b/.test(t) && len > 4) return "medium";
	if (len >= 5) return "medium";
	const known = words.filter((w) => TOP_500.has(w.replace(/[^a-z]/g, ""))).length;
	if (len <= 4 && known / len >= .5) return "easy";
	return "easy";
}
function hashId(text) {
	let h = 0;
	for (let i = 0; i < text.length; i++) h = h * 31 + text.charCodeAt(i) | 0;
	return "p" + (h >>> 0).toString(36);
}
function loadAll() {
	if (stateCache) return stateCache;
	if (typeof window === "undefined") return {};
	try {
		const parsed = JSON.parse(localStorage.getItem(STORAGE_KEY) || "{}");
		const safe = {};
		Object.entries(parsed && typeof parsed === "object" ? parsed : {}).forEach(([key, value]) => {
			if (!value || typeof value !== "object") return;
			const raw = value;
			const id = typeof raw.id === "string" && raw.id ? raw.id : key;
			const level = isSrsLevel(raw.level) ? raw.level : "easy";
			safe[id] = {
				id,
				level,
				history: Array.isArray(raw.history) ? raw.history.filter((item) => typeof item === "boolean").slice(-10) : [],
				consecutiveHits: typeof raw.consecutiveHits === "number" && Number.isFinite(raw.consecutiveHits) ? Math.max(0, raw.consecutiveHits) : 0,
				nextReview: typeof raw.nextReview === "number" && Number.isFinite(raw.nextReview) ? raw.nextReview : Date.now(),
				learned: raw.learned === true
			};
		});
		stateCache = safe;
		return safe;
	} catch {
		stateCache = {};
		return stateCache;
	}
}
function saveAll(map) {
	stateCache = map;
	try {
		localStorage.setItem(STORAGE_KEY, JSON.stringify(map));
	} catch {}
}
function createState(text) {
	return {
		id: hashId(text),
		level: classifyDifficulty(text),
		history: [],
		consecutiveHits: 0,
		nextReview: Date.now(),
		learned: false
	};
}
function getState(text) {
	const id = hashId(text);
	const all = loadAll();
	if (all[id]) return all[id];
	return createState(text);
}
function recordAnkiResult(text, rating) {
	const all = loadAll();
	const id = hashId(text);
	const s = all[id] || createState(text);
	const now = Date.now();
	if (rating === "easy") {
		s.history = [...s.history.slice(-9), true];
		s.consecutiveHits += 2;
		const idx = Math.min(Math.max(s.consecutiveHits - 1, 1), INTERVALS.easy.length - 1);
		s.nextReview = now + INTERVALS.easy[idx];
		s.level = "easy";
		if (s.consecutiveHits >= 4) {
			s.learned = true;
			s.nextReview = now + 30 * DAY;
		}
	} else if (rating === "hard") {
		s.history = [...s.history.slice(-9), true];
		s.consecutiveHits = Math.max(1, s.consecutiveHits + 1);
		s.level = "hard";
		s.nextReview = now + 6 * HOUR;
	} else {
		s.history = [...s.history.slice(-9), false];
		s.consecutiveHits = 0;
		s.learned = false;
		s.level = "hard";
		s.nextReview = now + 9e5;
	}
	all[id] = s;
	saveAll(all);
	return s;
}
function markLearned(text, learned = true) {
	const all = loadAll();
	const id = hashId(text);
	const s = all[id] || createState(text);
	s.learned = learned;
	if (learned) {
		s.consecutiveHits = Math.max(s.consecutiveHits, 5);
		s.nextReview = Date.now() + 30 * DAY;
	}
	all[id] = s;
	saveAll(all);
	return s;
}
function setLevel(text, level) {
	const all = loadAll();
	const id = hashId(text);
	const s = all[id] || createState(text);
	s.level = level;
	all[id] = s;
	saveAll(all);
	return s;
}
function speakerAvatar(speaker) {
	const s = (speaker || "?").trim();
	const initial = s.charAt(0).toUpperCase() || "?";
	const palette = [
		"bg-purple-500",
		"bg-emerald-500",
		"bg-sky-500",
		"bg-amber-500",
		"bg-pink-500",
		"bg-indigo-500",
		"bg-teal-500",
		"bg-rose-500"
	];
	let h = 0;
	for (let i = 0; i < s.length; i++) h = h * 31 + s.charCodeAt(i) >>> 0;
	return {
		initial,
		color: palette[h % palette.length]
	};
}
//#endregion
export { setLevel as a, recordAnkiResult as i, getState as n, speakerAvatar as o, markLearned as r, classifyDifficulty as t };
