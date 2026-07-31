# Progress Tracker — Quantum Effect: Captain's Quarters

Living document for tracking the ship-immersion redesign initiative. See
`Quantum Effect Captain's Quarters Build Bible.txt` for the full design vision;
this file tracks *implementation* status against it.

Last updated: 2026-07-29 (session 3)

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

All three original PRs confirmed green on the `Build Android APK` GitHub
Actions workflow after merge. PR #4 and this asset-integration work (PR #5)
not yet opened/merged — see below.

### VisualAssets inventory — what's usable vs. reference-only

The full `VisualAssets/` folder (19 PNGs + a placeholder note) is now on
`main`. Only the 3 backgrounds above were wired in this session — everything
else is either a labeled *reference sheet* (multiple small icons on a plain
grey background, meant to be cropped) or drawn in a different visual language
than the current game and needs a deliberate decision before use:

- **Directly usable, not yet wired in:**
  - `CoffeeBrewingCycle.png` — a clean 10-frame brewing-animation sprite
    sheet for an isometric coffee machine. Not used: the existing
    `CoffeeBrewingObject` (`GameScreens.kt`) is a hand-drawn Canvas-vector mug,
    matching every other Captain's Quarters hotspot (window, AI core, desk,
    bookshelf, elevator are all Canvas-vector too). Dropping in one raster
    sprite next to five vector props would break that established "painted
    background + vector foreground props" language — the same language the
    3 new deck backgrounds above deliberately preserve. Worth a proper look
    if/when the whole hotspot layer moves to sprite art at once, not
    piecemeal.
  - `WeatherOverlay.png` — nebula-shift, ice-comet-shower, and EMI-storm
    overlay textures that map almost exactly onto the Build Bible's weather
    translation table (fog→dense nebula, snow→ice comet shower,
    thunderstorm→EMI) and the existing `WeatherOverlayEffect`/
    `getWeatherColor()` functions. Strong next-session candidate.
  - `LivingUniverse_Progression.png` — includes 3 clean plant-growth-stage
    icons (`QE_DEC_013/014/015`, sapling → mature) that could replace/augment
    the Canvas-drawn `PlantVisualStem` in the greenhouse ecosystem tab.
  - `QE_FUR_CON.png` — a big furniture/console reference sheet including a
    `DisplayCabinet` and `Bookshelf` icon (matches the Build Bible's "Display
    Shelf" section) and an `AICoreConsole` icon.
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
  - `GalaxyExploration.png`, `QE_EXPAN_SEASONAL.png`, `QE_CON_DEC_EXT_MISC.png`,
    `QE_ENG_DEC.png` — Phase 4/5/6 icon sheets (star map, trade routes, planet
    sprites, decorations, engineering props) for systems that don't exist yet
    in `GameViewModel` (no star map screen, no trade mechanic). Premature to
    wire in before the underlying feature exists.
  - `QE_INF1.png`, `QE_INF2.png`, `QE_EXT1.png` — a full isometric
    wall/floor/ceiling/door tile-kit and exterior hull modules. Drawn in a
    true isometric-tile style, which is a different rendering approach than
    this game's current flat side-view panorama (`CabinView`'s
    `horizontalScroll` + `graphicsLayer` zoom). Using these would mean
    building an isometric room renderer, not a drop-in art swap — a much
    bigger architectural decision.

### Not started yet
- **Individual companion rooms (visual, not just routing)** — PR #2 made each
  crew door route to the *correct* companion, but all companions still land on
  the same `CompanionDetailScreen` UI. The Build Bible / user's stated goal is
  that companions "eventually have their own rooms" — i.e. visually distinct
  personal quarters (decor reflecting Lyra's sniper focus, Elara's
  medic/plant focus, Nova's hacker/infiltrator focus, Quark's android nature),
  not just the same detail screen pre-scrolled to the right person. Deferred
  because it needs either new per-companion background art or procedurally
  themed Canvas decor keyed off `companion.role`/`colorHex` — a bigger, more
  creative lift than the routing fix.
- **Captain's Desk / AI Terminal / Bookshelf / Coffee Corner panels** — these
  detail panels (opened from Captain's Quarters hotspots) are still fairly
  standard list/tab UI once you're inside them (mail, log, research, etc.).
  Out of scope so far; the user's ask has been about the *room* layer, not
  every sub-screen. Worth revisiting once all 4 decks have their spatial pass.
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
- **No local Android SDK in the sandbox this work was done in** — could not run
  `gradle assembleDebug` locally (AGP version doesn't resolve through the local
  proxy). Verification for each PR was: careful manual review of every changed
  composable against Compose `DrawScope`/`Path`/`Brush` API signatures, plus
  waiting for the `Build Android APK` GitHub Actions workflow to go green after
  merge to `main` (it has, every time so far).

## Suggested next session

All 4 decks now have both the spatial (corridor/lounge/bay) pass *and* a real
painted background. Good next candidates, roughly in order of how directly
they drop into existing code:
1. **Weather overlay art** (`WeatherOverlay.png`) — swap/augment
   `WeatherOverlayEffect` and `getWeatherColor()` with the nebula/ice/EMI
   textures from `VisualAssets/`, matching the Build Bible's weather
   translation table.
2. **Plant growth-stage sprites** (`LivingUniverse_Progression.png`) into
   `PlantVisualStem`.
3. Either the **individual companion room decor** pass (bigger, more
   creative — needs new art or a product decision per companion), or the
   **detail panel** polish (Captain's Desk / AI Terminal / Bookshelf / Coffee
   Corner).

No local Android SDK in this sandbox either — same verification method as
prior sessions: careful manual review of Compose API usage (brace/paren
balance checked with a script), Pillow available in-sandbox for any future
image cropping/resizing needs, plus watching the `Build Android APK` GitHub
Actions workflow after push/merge.
