# Game UI Kit

Interactive recreation of the **Quantum Effect** in-game interface, composed entirely from this design system's components over dimmed key art.

## Flow
`Main Menu` → (Continue / New Game) → `Exploration HUD` → open `Quest Log`, `Inventory`, or trigger `Dialogue` → back to HUD.

## Screens
- `MainMenu.jsx` — logo + `MenuList` over hero key art
- `ExplorationHUD.jsx` — player plate (`Panel`+`StatBar`), minimap, objective tracker (`ObjectiveList`), notifications, skill bar (`ItemSlot`), action buttons
- `QuestLog.jsx` — `Tabs` + `QuestCard` grid + tracked-quest detail panel with rewards
- `Inventory.jsx` — rarity-framed `ItemSlot` grid + item detail panel (`RarityTag`, stat bars)
- `DialogueScene.jsx` — `DialogueBox` conversation with branching choice prompt
- `kit.jsx` — shared `Backdrop` / currency helpers (exported to `window`)

Entry: `index.html` (design size 1280×800). Real portraits/key art come from `../../assets/`. Item/skill glyphs are text stand-ins — swap in real pixel-icon sprites when available.
