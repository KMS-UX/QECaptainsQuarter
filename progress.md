# Progress Tracker — Quantum Effect: Captain's Quarters

Living document for tracking the ship-immersion redesign initiative. See
`Quantum Effect Captain's Quarters Build Bible.txt` for the full design vision;
this file tracks *implementation* status against it.

Last updated: 2026-07-31 (session 6)

## Current initiative: "Dynamic wallpaper" starship

**Goal (from the user):** replace the static, icon/menu-driven interface with an
interactive, panoramic ship the captain swipes to look around and walks up to
interact with — starting with the Captain's Quarters ("home"), then extending
the same treatment to the rest of the starship ("base").

**Core mechanic** (already existed in `CabinView`, `app/src/main/java/com/example/ui/GameScreens.kt`,
confirmed working before this initiative started):
- Each deck renders inside a `1380.dp`-wide `Box` with `graphicsLayer` scale/pivot,
  itself inside a `horizontalScroll` — this is the "swipe to look around."
- Tapping a hotspot sets `zoomedNode`, which animates `scale` from `1.0f` to
  `3.5f` toward a pivot point, then on `finishedListener` calls
  `viewModel.setNode(...)` and opens the matching detail panel
  (`InteractivePanelContainer`). This is the "approach and interact" beat.

What this initiative adds on top: replacing generic icon-in-a-box hotspots
with hand-drawn physical objects, and replacing thin/centered deck layouts
with real spatial compositions (corridors, lounges) that reward swiping.

### Done

| # | PR | What |
|---|----|------|
| 1 | [#1](https://github.com/KMS-UX/QECaptainsQuarter/pull/1) | **Captain's Quarters**: redesigned all 6 hotspots (porthole window, AI hologram core, desk+slate, coffee mug, bookshelf, elevator doors) as Canvas-drawn physical props instead of icon-in-bordered-box. Added an approach vignette during zoom-in and disabled panorama swipe mid-zoom. Also redesigned the equivalent objects on the other 3 decks for consistency (greenhouse pods/console, aquarium feeder/jukebox pre-lounge-redesign, crew doors/pet incubator). |
| 2 | [#2](https://github.com/KMS-UX/QECaptainsQuarter/pull/2) | **Crew Habitation deck**: was showing 2 hardcoded fake doors ("JAX", "LYRA") with invented roles/colors that didn't match the real roster. Now generates one door per real companion (`state.companions`: Lyra, Nova, Elara, Quark) from `GameViewModel`, laid out down a proper corridor (`CrewCorridorStructure`: floor perspective lines, recessed door alcoves) instead of a centered `Row`. Tapping a door routes straight into *that* companion's detail screen via `viewModel.selectCompanionChat(id)` instead of a generic tabbed menu. Added `zoomPivotOverride` so the zoom-in animation targets the specific door tapped (previously all crew-node hotspots shared one fixed pivot). |
| 3 | [#3](https://github.com/KMS-UX/QECaptainsQuarter/pull/3) | **Aquarium Lounge deck**: was one compact centered block (tank + feeder + jukebox) in the middle of the 1380dp panorama — swiping just revealed empty background either side. Spread it across the full width: tank is now a bigger (640dp) centerpiece in a recessed viewing-bay frame (`AquariumLoungeStructure`), added decorative bench seating (`LoungeSeatingProp`) for a "watch the fish" lounge feel, feeder/jukebox got their own alcove recesses further down the room. |

| 4 | (unmerged, this branch) | **Greenhouse / Plantation deck**: replaced the centered `Row(SpaceEvenly)` layout with the same corridor/lounge recipe as PRs #2/#3. Added `GreenhouseStructure` (floor perspective + recessed growing-bay niches behind each prop) and `GreenhouseFoliageProp` (decorative swaying wall-planter, purely ambient) to `GameScreens.kt`. `GreenhousePodObject`/`GreenhouseClimateConsole` got a `modifier: Modifier = Modifier` first param (existing recipe step 3) so they can be placed with absolute offsets. Hydro-Pod Alpha/Beta, the Climate Console, and the elevator are now spread across x-fractions `[0.08, 0.28, 0.52, 0.88]` of the 1380dp panorama, with two foliage props filling the gaps. Also added a per-hotspot `zoomPivotOverride` for the pods/console (same fix PR #2 applied to Crew doors) since they no longer share one on-screen position, so the old single fixed `CabinetNode.GREENHOUSE` pivot would have zoomed toward the wrong spot depending which prop was tapped. |

| 5 | (unmerged, this branch) | **Real painted pixel-art backgrounds for the 3 non-Captain's-Quarters decks.** The user uploaded a `VisualAssets/` folder to `main` (merged into this branch) containing ~19 reference-sheet PNGs from an art pass done outside this session. Three of them — `QE_BKG_GREENHOUSE.png`, `QE_BKG_AQUARIUM.png`, `QE_BKG_CREWBAY.png` — are full painted scenes matching the style/quality of the existing `img_cozy_cabin.jpg` Captain's Quarters wallpaper. Converted to JPEG (`img_greenhouse_bay.jpg`, `img_aquarium_lounge.jpg`, `img_crew_habitation.jpg` in `app/src/main/res/drawable/`) and wired into `BiomechanicalGreenhouseBackground()`, `AquariumLoungeBackground()`, `CrewHabitationBackground()` using the exact same `Image(..., contentScale = ContentScale.Crop)` pattern the Captain's Quarters wallpaper already uses, replacing the old flat procedural gradients. Kept a translucent scrim + the existing low-alpha scanline/bulkhead Canvas overlays on top for hotspot-label legibility and continuity with Captain's Quarters' own weather-tint treatment. All 4 decks now have a real painted backdrop, not just Captain's Quarters. |

| 6 | (unmerged, this branch) | **Weather overlay art + plant growth-stage sprites + companion rooms + detail-panel polish**, all in one branch (`claude/weather-plant-visuals-18k7ac`). Four independent changes: (1) `WeatherOverlayEffect`/`getWeatherColor` now layer the painted nebula/ice/EMI textures cropped from `WeatherOverlay.png` behind the existing animated particle Canvas for Dense Nebula, Ice Comet Shower, and EMI (Quantum Storm keeps its procedural-only look — no rain-analog asset exists in this batch), plus 3 new named colors (`NebulaViolet`/`IceCometCyan`/`EMIPlasma`) sampled from the art. (2) `PlantVisualStem` swaps its Canvas-drawn stem/leaf-dot placeholder for the sapling→mature sprite sequence cropped from `LivingUniverse_Progression.png` (`QE_DEC_013/014/015`), with a species-tinted glow halo kept behind the fully-bloomed stage. (3) `CompanionDetailScreen` now renders a `CompanionQuartersDecor` Canvas backdrop keyed off `companion.id`/`colorHex` — Lyra gets a sniper's-nest scope reticle + tactical stripes + ammo crate, Nova gets falling hacker data-streams + a terminal frame, Elara gets climbing bio-sanctuary vines/leaves, Quark gets an android circuit-trace grid with glowing nodes — since no painted per-companion room art exists yet (see inventory below), this is procedural decor per the Architecture notes' documented approach. (4) Captain's Desk / AI Terminal / Bookshelf / Coffee Corner detail panels get icon treatment seeded from `QE_FUR_CON.png` (CaptainDesk, AICoreConsole, Bookshelf icons) and `CoffeeBrewingCycle.png` (the lit "ready" brewer frame): `InteractivePanelContainer` takes an optional `iconRes` shown in the shared panel header, Bookshelf/Coffee Corner (previously text-only) get a new `PanelIconBanner` intro card, Captain's Desk gets the desk icon inline with its header, AI Terminal gets the AI Core Console icon as a trailing badge next to the existing L.I.L.A. avatar. |

All three original PRs confirmed green on the `Build Android APK` GitHub
Actions workflow after merge. PR #4, PR #5 (asset integration), and PR #6
(this weather/plant/companion-rooms/panel-polish work) not yet
opened/merged — see below. As with PR #4/#5, no local Android SDK was
available to run `gradle assembleDebug` in this sandbox (confirmed again
this session — `com.android.application` plugin doesn't resolve through
the local proxy); verification was manual review plus a brace/paren/bracket
balance script, matching the process documented below. Watch the `Build
Android APK` GitHub Actions workflow after this branch merges.

| 7 | (unmerged, this branch) | **Display Shelf** (Build Bible: "Player achievements. Alien artifacts. Models. Photographs. Crew gifts."), the one item from session 4's "suggested next session" list that had no prior implementation. Session start also confirmed PR #4/#5/#6 had *already* been merged directly to `main` outside the PR flow (the PRs themselves show `merged:false`/`closed` on GitHub, but `main`'s HEAD matches this branch's pre-session commit exactly) — that suggested-next-session item is done, nothing to merge. New work this session: added `CabinetNode.DISPLAY_SHELF`, a Canvas-drawn `DisplayCabinetObject` physical hotspot (glass case, glowing medals/curio silhouette — same vector-prop treatment as every other Captain's Quarters hotspot, not a raster sprite) placed at x=1140dp between the Bookshelf (1020dp) and Elevator (now 1250dp, shifted right from 1200dp to make room — checked the shift keeps the elevator's 110dp-wide column within the 1380dp panorama, since the old 1200dp start already left only 180dp of margin), and a `DisplayShelfPanel` with 3 tabs. Rather than inventing a fake achievements database, **Mementos** are 8 milestones computed live from state that already exists and only grows (companion affinity, plant growth, completed research, placed decorations, log count, calendar day) — no new persisted schema. **Gifts** scans each companion's existing `chatHistory` for "Replicated and presented gift:" messages (already logged by `replicateCompanionGift`, just never surfaced anywhere) and shows what's been given per companion, with an honest "nothing presented yet" empty state if none have. **Curios** (alien artifacts/models/photographs) has no underlying collection mechanic in the game at all, so it's an honest "ARTIFACT BAY: EMPTY" placeholder rather than fabricated content — a real feature would need its own next session. Also cropped `QE_FUR_010_DisplayCabinet` from `QE_FUR_CON.png` into `img_icon_display_shelf.png` (same chroma-key-by-brightness/saturation approach as session 4's icon crops) for the panel header banner, following the `PanelIconBanner` pattern from Bookshelf/Coffee Corner. Along the way, confirmed `DecorationState`/`purchaseDecoration`/`toggleDecoration` (the Build Bible's separate "Personal Decorations" bullet, right after Display Shelf) already exists and is already surfaced — it's tab index 2 ("IV. COZY QUARTERS ORNAMENTS") inside `LivingShipEcosystemTab`, reached from the Captain's Desk panel — so it needed no new work, just confirmation it wasn't being duplicated. |

| 8 | (unmerged, this branch) | **Artwork brush-up pass**, prompted by the user after new visual assets landed in the repo (`VisualAssets/AdditionalArts/`, `VisualAssets/QE_AMB.png`, `Uploads/QuantumEffectMap/`). Investigated all three; only `QE_AMB.png` turned out usable this session — see the new "AdditionalArts / Uploads — investigated, not used" note below for why the other two were set aside rather than force-fit. Two concrete swaps, both sourced from this game's own in-house `QE_`-batch art (not the sister-game dumps), both replacing a Canvas-drawn "simple graphic" with real painted art in an *existing, already-reachable* feature: (1) `GalaxyRadarSection` (the RADAR tab inside the Captain's Desk panel — real `SectorState` data, previously flagged as "premature" in session 4 because a search for a literal "star map screen" didn't turn it up under that name) now has a painted circuit-hex starmap backdrop (`img_starmap_hex.png`, cropped from `QE_AMB.png`'s `QE_ANI_006_Star_Map_Procedural_Hex`) behind the existing Canvas radar sweep/rings, and each of the 6 sectors' radar nodes now renders a themed painted planet sprite (`img_sector_planet_{home,tech,sanctuary,anomaly,nebula,plasma}.png`, cropped from `GalaxyExploration.png`'s `QE_ANI_006_Dynamic_Planet_Sprite` row, matched to each sector's lore via `getSectorPlanetIcon(sectorId)`) instead of the old flat color dot — color-coded rings are kept for the alignment cue. (2) Two more panels got a `getPanelIcon` entry (the header-badge mechanism from session 4, previously only DESK/AI/COFFEE/BOOKSHELF/DISPLAY_SHELF): `GREENHOUSE` gets `img_icon_holographic_planter.png` (`QE_DEC_017_HolographicPlanter`, `QE_EXPAN_SEASONAL.png`) and `PET_SANCTUARY` gets `img_icon_pet_dock.png` (`QE_DEC_019_RobotPetDock`, same sheet) — both panels had no header icon at all before, so this was a pure addition, no risk of clashing with an existing treatment. Deliberately did *not* touch any panorama hotspot's physical prop rendering (`DisplayCabinetObject`, `CoffeeBrewingObject`, etc. all stay Canvas-vector) — that stays a deliberate house-style choice per the Architecture notes, not something this pass second-guessed. |

### AdditionalArts / Uploads — investigated, not used

Session 6 also surveyed the two new asset dumps beyond `QE_AMB.png` and is
recording *why* they weren't used, so a future session doesn't have to
re-open every sheet to re-derive the same conclusion:
- **`VisualAssets/AdditionalArts/`** is a complete, self-contained design
  system bundle (its own `SKILL.md`/`readme.md`, React components, HTML
  style guides, CSS tokens) for a *different, related game* also called
  "Quantum Effect" — a top-down 2D pixel-art RPG, not this game. Its own
  docs call the character/prop sprites "prototype-grade... good for
  greyboxing, not final." Checked its `Characters/sheets/companion-reference.png`
  (a 5-companion roster: Atom/Lumen/Nia/Rex/Echo) and
  `Uploads/QuantumEffectMap/QuantumBabyCompanions.png` (a *different* 6-companion
  roster: Lyra-the-Scout/Drox/Nix/Vayn/Elsi/Grum) against this game's actual
  4 companions (Lyra/Nova/Elara/Quark) — neither matches, and the two sheets
  don't even agree with each other, confirming this is unfinished pitch
  material for the sister game, not a canon reference for ours. One
  genuine 1:1 hit: `Characters/sheets/dialogue-expression.png` has a
  "COMPANION – QUARK (ANDROID)" portrait row (round android head, glowing
  cyan face) that matches this game's Quark (`role = "Android Companion"`,
  `colorHex = "00F0FF"` cyan) almost exactly. Left it unused anyway — every
  companion currently gets identical treatment (a `colorHex` dot +
  Canvas-drawn `CompanionQuartersDecor`, no portraits for anyone), and giving
  only Quark a real portrait would read as an inconsistency, not an
  upgrade. If a portrait pass is ever wanted, it needs art for all 4
  companions (or a decision to add portraits only where they exist and
  accept the asymmetry) — a product decision, not a quick swap.
  `Environment/sheets/prop-library.png` (500+ pixel props) was also
  checked and set aside for a style reason: it's true dithered pixel-art
  furniture in warm wood tones, visibly different rendering fidelity and
  palette from this game's clean cyan/teal isometric-icon furniture
  (`QE_FUR_CON.png`) — mixing the two in the same panel header slot would
  look like two different games.
- **`Uploads/QuantumEffectMap/`** is a Godot project's asset import (`.import`
  sidecar files) for a top-down 2D prototype — `QuantumEffectEnemy1-4.png`,
  `QuantumEffectFactions1/2.png`, `QuantumEffectStarships.png`, etc. Same
  sister-game/prototype status as `AdditionalArts/`; nothing here has a
  companion/faction/prop that matches this game's established roster or
  visual bible, so none of it was used.
- `QE_AMB.png` (the one asset from this batch that *was* used, see row 8) is
  from this game's own art pipeline (same `QE_ANI_XXX` naming and reference-
  sheet template as `WeatherOverlay.png`/`GalaxyExploration.png`), which is
  why it slotted in cleanly with no style mismatch.

### VisualAssets inventory — what's usable vs. reference-only

The full `VisualAssets/` folder (19 PNGs + a placeholder note) is now on
`main`. The 3 deck backgrounds (session 3) plus weather/plant/panel-icon
crops (session 4, this branch) are wired in — everything else is either a
labeled *reference sheet* (multiple small icons on a plain grey background,
meant to be cropped) or drawn in a different visual language than the
current game and needs a deliberate decision before use:

- **Wired in this session (session 4):**
  - `WeatherOverlay.png` — cropped the `QE_ANI_005_NebulaShift`,
    `QE_FX_003_WeatherEffect_Ice`, and `QE_FX_004_WeatherEffect_EMI` panels
    (chroma-keyed transparent where they're drawn as angled window panes) into
    `img_weather_nebula/ice/emi.png`, layered into `WeatherOverlayEffect`.
  - `LivingUniverse_Progression.png` — cropped `QE_DEC_013/014/015` into
    `img_plant_seed/sprout/mature.png`, swapped into `PlantVisualStem`.
  - `QE_FUR_CON.png` — cropped `QE_FUR_001_CaptainDesk`, `QE_CON_004_AICoreConsole`,
    `QE_FUR_011_Bookshelf` into `img_icon_captain_desk/ai_core/bookshelf.png`,
    and (session 5) `QE_FUR_010_DisplayCabinet` into `img_icon_display_shelf.png`,
    used as panel-header icons/banners. `QE_CON_010_HolographicDisplay` and
    `QE_CON_014_StellarArchive` are still cropped-quality-verified but unused —
    good candidates for a Bookshelf sub-tab, or the Curios tab once it has a
    real collection mechanic (see Suggested next session).
  - `CoffeeBrewingCycle.png` — used only the final "ready" frame (glowing cyan
    outline, no visible brew animation) as a static header icon
    (`img_icon_coffee_brewer.png`) for the Coffee Corner *detail panel*, not
    the panorama hotspot. This is a different context than the concern noted
    below: the panorama scene's prop layer is still 100% Canvas-vector
    (`CoffeeBrewingObject` untouched); this icon only appears inside the 2D
    panel UI, which already mixes raster imagery (e.g. `img_ai_avatar`,
    `img_cinematic_space`) with vector Canvas chrome. The other 9 frames
    (the brew-in-progress animation) remain unused — still a good fit if the
    panorama hotspot layer ever moves to sprite art wholesale.
- **Reference/mockup only, not directly droppable:**
  - `DesktopProp_CaptainLogJournal.png`, `UIFrame_AIWelcomePrompt.png` — concept
    mockups with baked-in UI text/labels overlaid on a screenshot-like
    background; would need the text/background removed before the prop art
    (an open journal, a dialogue frame) could be extracted cleanly. No image
    editing tool available in this sandbox beyond crop/resize (Pillow), so
    isolating just the prop without the baked text wasn't attempted.
  - `CrewSystem.png`, `QE_CREW_INTER.png` — generic crewman/crewwoman walk
    cycles and a "First Officer" portrait (teal-haired, white/red uniform).
    Doesn't match any of the 4 real companions' `colorHex`/role (Lyra, Nova,
    Elara, Quark) — using it would mean either inventing a 5th companion or
    mismatching an existing one's established look. Needs a product decision,
    not a code change.
  - `QE_CON_DEC_EXT_MISC.png`, `QE_ENG_DEC.png` — Phase 5/6 icon sheets (trade
    routes, engineering props) for systems that still don't exist in
    `GameViewModel` (no trade mechanic). Premature to wire in before the
    underlying feature exists.
  - **Correction (session 6):** `GalaxyExploration.png` and
    `QE_EXPAN_SEASONAL.png` were wrongly filed here in session 4 — the
    "star map" feature they were held back for already existed
    (`GalaxyRadarSection`, the RADAR tab inside the Captain's Desk panel,
    driven by real `SectorState` data) and now uses them (see below). Always
    grep the actual UI for a feature name before calling an asset premature.
  - `QE_INF1.png`, `QE_INF2.png`, `QE_EXT1.png` — a full isometric
    wall/floor/ceiling/door tile-kit and exterior hull modules. Drawn in a
    true isometric-tile style, which is a different rendering approach than
    this game's current flat side-view panorama (`CabinView`'s
    `horizontalScroll` + `graphicsLayer` zoom). Using these would mean
    building an isometric room renderer, not a drop-in art swap — a much
    bigger architectural decision.

### Not started yet
- Nothing has been done yet on Phase 2+ systems from the Build Bible roadmap
  (Daily Briefing content depth, Quantum Resonance Forecast, crew
  schedules/relationships beyond the existing affinity system, trade/galaxy
  simulation). This initiative is purely the presentation/interaction layer
  for the existing feature set.

## Architecture notes for continuing this work

- **File**: everything lives in one file,
  `app/src/main/java/com/example/ui/GameScreens.kt` (~6900 lines). Search for
  `StarshipDeck` and `CabinetNode` to orient.
- **Decks**: `enum class StarshipDeck { CAPTAINS_QUARTERS, BIOMECHANICAL_GREENHOUSE, AQUARIUM_LOUNGE, CREW_HABITATION }`.
  `CabinView`'s `when (activeDeck)` block is where each deck's panorama content
  lives. All decks already render inside the shared 1380dp scroll/zoom
  container — a deck only *feels* flat if its content is centered/compact
  instead of using absolute `.align(Alignment.TopStart).offset(x = ..., y = ...)`
  placement across the full width.
- **Recipe for giving a deck spatial depth** (used for Crew Habitation and
  Aquarium Lounge, reusable for Greenhouse):
  1. Add a `<Deck>Structure` Canvas composable: floor perspective lines
     converging to a vanishing point, a ceiling/ambient light strip, and
     recessed alcove panels (`drawRoundRect` with alpha-black fill) positioned
     at the same x-fractions as the hotspots that will sit in front of them.
  2. Replace `Row(Arrangement.SpaceEvenly)` with individual hotspot calls using
     `Modifier.align(Alignment.TopStart).offset(x = 1380.dp * fraction, y = ...)`.
  3. Give any object composable that will be positioned this way a
     `modifier: Modifier = Modifier` first parameter (all call sites in this
     codebase use named args, so adding this is always safe) and thread it into
     the composable's outer `Column`/`Box`.
- **Zoom pivot**: `pivotX`/`pivotY` in `CabinView` are keyed by `CabinetNode`,
  which is too coarse when multiple hotspots on one deck share a node (e.g.
  all crew doors → `CabinetNode.CREW`). Use `zoomPivotOverride: Pair<Float,Float>?`
  — set it right before setting `zoomedNode` in a hotspot's `onClick`, matching
  x/y fractions to that hotspot's own position. It's cleared automatically in
  the zoom `finishedListener` alongside `zoomedNode`.
- **Companion data**: real roster lives in `GameViewModel`'s `initialCompanions`
  (ids: `lyra`, `nova`, `elara`, `quark`). `parseHexColor()` in `GameScreens.kt`
  already has a case for each of their `colorHex` values — if a 5th companion
  is ever added, extend `parseHexColor` too or it'll fall back to `CyberCyan`.
  As of session 4, also extend `CompanionQuartersDecor`'s `when (companionId)`
  and `companionQuartersLabel()` (both in `GameScreens.kt`, right before
  `CompanionDetailScreen`) or the new companion's quarters will render with no
  decor at all (the `when` has no `else` branch drawing anything).
- **Panel header icons**: `InteractivePanelContainer` takes an optional
  `iconRes: Int?` (session 4) shown in place of the plain color dot; wired via
  `getPanelIcon(CabinetNode): Int?` right next to `getPanelTitle`. Currently
  DESK/AI/COFFEE/BOOKSHELF/DISPLAY_SHELF have an icon — add a case there (plus
  crop a matching PNG into `res/drawable/`) to extend to other nodes.
- **Captain's Quarters hotspot spacing is now tight.** All 7 physical props
  (window/AI/desk/coffee/bookshelf/display-shelf/elevator) share the same
  1380dp panorama width; gaps shrink from 260dp near the window down to ~10dp
  by the display shelf/elevator (session 5 added the display shelf between
  bookshelf and elevator, and narrowed its own footprint to 90dp to make the
  math work — see `DisplayCabinetObject`). Adding an 8th prop to this deck
  without widening the panorama or removing something else will very likely
  start actually overlapping objects, not just crowding them.
- **No local Android SDK in the sandbox this work was done in** — could not run
  `gradle assembleDebug` locally (AGP version doesn't resolve through the local
  proxy). Verification for each PR was: careful manual review of every changed
  composable against Compose `DrawScope`/`Path`/`Brush` API signatures, plus
  waiting for the `Build Android APK` GitHub Actions workflow to go green after
  merge to `main` (it has, every time so far).

## Suggested next session

All 4 decks now have the spatial pass, a real painted background, weather
overlay art, plant growth sprites, per-companion quarters decor, polished
detail panels, and a Display Shelf — the presentation/interaction-layer
initiative from this document's original goal is feature-complete for the 4
existing decks against everything in the Build Bible's "Captain's Quarters"
section. Good next candidates:
1. **Confirm `Build Android APK` is green on `main`** after this branch's
   Display Shelf work lands — check the workflow run for this branch's HEAD
   commit specifically (there's a backlog of runs from the bulk-upload
   commits at session start; several were still `in_progress`/queued when
   this session began).
2. **Curios tab has no data behind it.** `DisplayShelfPanel`'s CURIOS tab
   (alien artifacts/models/photographs) is an honest empty-state placeholder
   — there's no collection mechanic anywhere in `GameViewModel` for the
   player to find/earn artifacts. Building that out for real would mean: (a)
   a new persisted list (id/name/description/imageRes, mirroring
   `DecorationState`'s shape), and (b) *some* in-fiction way to earn entries
   (an exploration/sector-scan reward off `SectorState`? a rare research
   payout? a companion affinity milestone gift?) — needs a product decision
   on the earning mechanic before writing code, not just a UI pass.
   `QE_INF_018_TrophyCase` (`LivingUniverse_Progression.png`, not yet
   cropped) and the still-unused `QE_CON_010_HolographicDisplay`/
   `QE_CON_014_StellarArchive` crops (`QE_FUR_CON.png`, cropped/
   quality-checked in session 4 — see VisualAssets inventory) are ready-made
   art seeds once the mechanic is decided.
3. **Phase 2+ Build Bible systems** — nothing done yet on Daily Briefing
   content depth, Quantum Resonance Forecast, crew schedules/relationships
   beyond the existing affinity system, or the trade/galaxy simulation. These
   are gameplay/data-model work, not presentation — a different kind of
   session than the last 5.
4. If more painted companion-room art ever gets added (see `CrewSystem.png`/
   `QE_CREW_INTER.png` note above — needs a product decision on a 5th
   companion or a re-skin first), swap it in for session 4's procedural
   `CompanionQuartersDecor` Canvas backdrops.
5. **More `getPanelIcon` gaps remain** (session 6 filled GREENHOUSE and
   PET_SANCTUARY; WINDOW, CREW, and AQUARIUM still fall through to `else ->
   null`, i.e. the plain color-dot header). No good in-house art was
   identified for these three this session — check future `QE_`-batch
   uploads for a bookshelf-adjacent console, a crew-corridor prop, or an
   aquarium/tank icon before reaching for the sister-game dumps.
6. **The 5 `DecorationState` entries have no icons at all** (`dec_lantern`,
   `dec_bonsai`, `dec_gramophone`, `dec_toy`, `dec_prism` — the "IV. COZY
   QUARTERS ORNAMENTS" list inside `LivingShipEcosystemTab`, just a colored
   dot per row today). `QE_DEC_017_HolographicPlanter` (now used for the
   Greenhouse panel icon, see row 8) is a strong visual match for
   `dec_bonsai`'s "rotating hologram" description, and
   `QE_DEC_018_DataCrystalCluster` (`QE_EXPAN_SEASONAL.png`, not yet
   cropped) is a good match for `dec_prism`'s "glittering gemstone."
   Deliberately left alone this session: giving 2 of 5 decorations a real
   icon and leaving the other 3 as dots would read as unfinished, not
   polished — this needs either art for all 5 or a decision to skip it.

## Correction for future sessions: PR merge state is unreliable

Session 5 discovered that GitHub's PR list for this repo shows PRs #4/#5/#6
as `merged: false` / `state: closed` — but `main`'s actual HEAD commit
already contained all of that work byte-for-byte (confirmed via
`get_file_contents`/`list_commits` against `refs/heads/main`, not the local
`origin/main` tracking ref, which was stale until an explicit
`git fetch origin +refs/heads/main:refs/remotes/origin/main` — a plain
`git fetch origin main` silently no-opped in this sandbox). **Don't trust
this progress.md's "not yet opened/merged" notes about PR state at face
value** — always check `main`'s real HEAD content/commit log directly before
assuming something needs merging.

No local Android SDK in this sandbox either — same verification method as
prior sessions: careful manual review of Compose API usage (brace/paren
balance checked with a script), Pillow available in-sandbox for any future
image cropping/resizing needs, plus watching the `Build Android APK` GitHub
Actions workflow after push/merge.
