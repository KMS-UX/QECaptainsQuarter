# Quantum Effect Design System

**Quantum Effect** is a sci-fi open-world RPG (2D/2.5D pixel art) set in a dark dystopian future: *"The First Earth, 2077. Quantum energy reshaped humanity — but also awakened greed, ambition, and the Original Sins. Factions rise. Chaos spreads. You are the last Quantum Baby — the key to change, or the end."* Open-ended, choice-driven play; tagline: **THE QUANTUM IS POWER. POWER CORRUPTS. CHOICE DEFINES.**

## Sources
All context came from 37 uploaded reference sheets in `uploads/` (no codebase, Figma, or font binaries were provided):
- `Asset20CompleteArtDirectionBible.png` — pillars, palettes, typography, motifs (primary source of truth)
- `Asset15HUDandUIKit.png` — HUD layouts, buttons, bars, dialogs, minimaps, platform adaptation
- `Asset16InventoryandIconBible.png` — item icons, rarity system, slot frames, badges
- `Asset19Quest&MissionVisualLanguage.png` — quest tiers, cards, states, markers, rewards
- `Asset14DialogueExpression.png` — character portraits (Aurin, Lyra, Quark, Kai, Lord Vorax)
- `QuantumEffectOverviewSheet.png`, `QuantumEffectOverview.png` — concept overview + pixel key art
- Factions, characters, creatures, environments, FX, animation, map & dungeon sheets (Asset1–13, 17–18, QuantumEffect*)

## World & product
One product: **the game UI** (PC/console 16:9, adapts to mobile). Key entities used in sample content:
- Player: **Aurin** (Lv 23, Chronomancer archetype); the **Quantum Baby** is the mythic player origin
- Companions: **Lyra** (resonance adept), **Kai** (ally), **QUARK** (android)
- Antagonist: **Lord Vorax / Chronarch Prime**
- Factions: **Aurelian Order** (gold, "Knowledge. Discipline. Unity."), **Emberpact** (ember orange, "Strength. Survival. Freedom."), **Void Seekers** (purple, "The truth lies beyond."), **Ironward** (industrial green, "Industry. Progress. Security.")
- Stats: HP / EP(MP) / XP; ATK DEF MAG RES SPD LCK; currencies: gold coins + quantum shards

## CONTENT FUNDAMENTALS
- **Voice:** terse, declarative, imperative. Short lines, often sentence fragments. "Find the Core Relay." "Reach the Quantum Spire."
- **Labels are ALL CAPS** in the display font with wide tracking: `CURRENT OBJECTIVE`, `TARGET LOCK`, `FACTION PERKS`.
- **Second person** for instructions and quest text ("Your actions affect the balance of power."). Characters speak in first person, gravely: "The Core Relay is our last line of defense. Get it online, and keep it safe. Quantum depends on you."
- **Epigrams** close major surfaces: "No path is purely light. No choice is without consequence. The frontier remembers."
- **Numbers are content**: damage, timers (`02:45:18`), levels (`Lv. 18+`), counts (`3/5`) are shown raw in mono type.
- **No emoji, ever.** Status is conveyed with the game's own pixel iconography and color.
- Faction/system flavor uses triads: "Knowledge. Discipline. Unity."
- Vibe: dark wonder — dangerous but heroic; never jokey, never corporate.

## VISUAL FOUNDATIONS
- **Backgrounds:** near-black blue-violet (`--void-0` #04060d → `--void-4`). Full-bleed pixel key art used behind heroes/menus, dimmed with `--overlay-dim`. Subtle scanline texture (`--scanline`) on panels; no light mode.
- **Color:** teal = technology/UI highlights; purple = quantum energy/mystery; gold = premium/important; orange/red = danger & heat alerts; green = nature/life/healing. Max one accent per element; color IS meaning (rarity scale, quest tiers, faction hues — see `tokens/colors.css`).
- **Type:** Orbitron (display; all-caps headers, titles, important data) + Share Tech Mono (body/UI — substitute for the unshipped "Quantum Sans"). Section headers are numbered and colored teal or gold: `05. IN-GAME HUD LAYOUTS`.
- **Borders & cards:** 1px borders (`--border-panel`), sharp corners (0–4px radius), corner tick accents on featured panels. Cards = dark panel + 1px border, colored 2px top strip for categorized cards (quest tiers), glow border for selected/legendary.
- **Shadows/glow:** outer glow in the accent color signals energy/selection (`--glow-*`); panels use deep drop shadow + inner dark inset (`--shadow-panel`, `--inset-panel`). Protection = dim overlay, not gradient capsules.
- **Hover:** brighter border + accent glow + text brightens. **Press:** background lightens a step, glow tightens (no shrink transforms). **Disabled:** desaturated `--fg-disabled`, no border glow.
- **Motion:** 0.15–0.3s UI transitions (`--ease-ui`), fades and glow pulses; no bounces. Bars animate width linearly.
- **Transparency/blur:** dim translucent overlays for modals; no glassmorphism blur.
- **Imagery:** rich cool-dark pixel art (purple/teal nights, ember-orange sunsets), high contrast, strong rim light, emissive sources. Never bright/pastel.
- **Layout:** dense HUD grids, fixed top-left player plate, top-right minimap, bottom-center skill bar; 44px minimum hit targets.

## ICONOGRAPHY
- The game uses a **bespoke pixel-art icon set** (16×16 base grid, 16/32/64px variants) — the "Quantum Core" reference sheet (`uploads/IconSprites1.png`) defines ~340 icons across 20 categories: weapons, armor, consumables, materials, currencies, quest/key items, skills, status buffs/debuffs, UI/system, elements, markers, interface actions, chests, rarity/portrait frames, NPC/faction emblems, map/location, achievements, emotes. **No icon font or SVG set exists** — icons are pixel PNGs.
- A curated **35-icon working set** is extracted (4×/3× nearest-neighbor upscaled) to `assets/icons/`: weapons (`weapon-sword`, `-rare`, `-legendary`, `weapon-spear/bow/staff`), consumables (`potion-health/stamina/mana/epic`, `crystal-essence`), currency (`coin-gold`, `gem-blue/green/purple`), skills (`skill-slash/nova/fire/frost`), elements (`elem-fire/ice/lightning/nature/void/quantum`), chests (`chest-wooden/golden/epic/legendary`), faction emblems (`faction-arclight/ironaccord/voidsentinels/mechanicum/outlaws/mystics`). Browse them in the **Icons** group on the Design System tab.
- Usage: pass a path to `ItemSlot image=…` (inventory, skill bar), or `<img style="image-rendering:pixelated">`. The `ItemSlot`'s own rarity frame wraps the art.
- Faction-emblem naming on the sheet (Arclight Order, Iron Accord, Void Sentinels, Mechanicum…) differs from the game's lore factions (Aurelian Order, Ironward, Void Seekers, Emberpact); the DS keeps the lore names and maps emblems by color/motif. **Confirm the intended emblem-to-faction mapping.**
- The **full sheet is now extracted** to `assets/icon-library/` (~340 icons across 20 categories, 2× native PNG, nearest-neighbor). Individually-sliced tiles live in per-category subfolders (`armor/`, `consumables/`, `materials/`, `skills/`, `buffs-debuffs/`, `quest-key-items/`, `ui-system/`, `interface-actions/`, `direction-markers/`, `inventory-categories/`, `rarity-frames/`, `portrait-frames/`, `emotes/`, plus the earlier `weapons/`, `currencies/`, `elements/`, `chests/`, `factions/`, `map/`, `achievements/`); the trimmed source sheets remain under `icon-library/sheets/`. See `icon-library/manifest.json`. The curated 35-icon HUD working set stays in `assets/icons/` for template use.
- Unicode glyphs (◆ ● ✦ ⚠ ×) remain as lightweight stand-ins where no sprite is wired. No emoji.
- Design motifs: atom/orbit mark, quantum diamond/rhombus, hex grid, glitch accents.

## Components (reusable primitives)
Namespace `QuantumEffectDesignSystem_2d830e`. Grouped by concern:
- **core/** — `Button` (primary/secondary/ghost/danger HUD button), `MenuList` (main-menu stack), `Tabs` (inventory-style filter tabs)
- **hud/** — `Panel` (corner-ticked scanline container), `StatBar` (HP/EP/XP/boss bars), `Notification` + `LootPopup` (corner toasts)
- **dialogue/** — `DialogueBox` (dialogue/system/warning/confirm message box with portrait)
- **inventory/** — `ItemSlot` (7-tier rarity frame, stacks, enhancement + state badges: new/favorite/equipped/locked) + `RarityTag`
- **quests/** — `QuestCard` (tier-coded quest card), `ObjectiveList` (objective checklist), `QuestTracker` + `QuestStatus` (tracked-quest HUD + 8 status-state icons), `QuestRewards` (XP/rep/currency/item/choice reward display) — from the Quest & Mission Visual Language sheet
- **system/** — `Minimap` (circular/square/fog-of-war radar with typed blips) + `LoadingScreen` (loading & transition screen with tips panel) — from the HUD & UI Kit sheet
- **equipment/** — `StatMeter` (labeled % attribute bar for augmentation/ship stats) + `ModChip` (rarity-framed insertable mod / augmentation row) — from the Modifications & Equipment sheet
- **quantum/** — `ElementCard` (7-element crystal card with rarity/source/usage) + `HazardBadge` (5-level quantum-field hazard badge) — from the Starships, Quantum Field & Elements sheet

## UI kit
- **ui_kits/game/** — interactive game-UI recreation: Main Menu → Exploration HUD → Quest Log → Inventory → Dialogue. Composes the components above over dimmed key art.

## Index (root manifest)
- `styles.css` — entry point; `@import`s everything below
- `tokens/` — `fonts.css`, `colors.css`, `typography.css`, `spacing.css`, `effects.css`
- `guidelines/` — 14 foundation specimen cards (Colors, Type, Spacing, Effects, Brand)
- `components/{core,hud,dialogue,inventory,quests}/` — 12 components (`.jsx` + `.d.ts` + `.prompt.md` + card HTML)
- `ui_kits/game/` — game UI kit
- `assets/` — logos, key art, portraits (real crops from source sheets); `assets/characters/sprites/` and `assets/tiles/` hold **prototype-grade** sliced + alpha-keyed sprites/props from the character & tileset montages (see their READMEs — good for greyboxing, not final; request raw atlases for production)
- `SKILL.md` — Agent Skills entry point
- `thumbnail.html` — homepage tile

## Intentional additions
- `MenuList` — main-menu button stack (seen on the HUD sheet's Menu Screens; generalized for reuse).

## Logo
Real logotypes were cropped from the sheets (gold logotype, distressed white logotype, pixel title card). No vector logo exists; do not redraw it — use the PNGs or set the name in Orbitron.
