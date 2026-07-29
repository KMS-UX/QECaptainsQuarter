# Progress Tracker — Quantum Effect: Captain's Quarters

Living document for tracking the ship-immersion redesign initiative. See
`Quantum Effect Captain's Quarters Build Bible.txt` for the full design vision;
this file tracks *implementation* status against it.

Last updated: 2026-07-29

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

All three merged to `main` via squash-merge PRs; each confirmed green on the
`Build Android APK` GitHub Actions workflow after merge.

### Not started yet

- **Greenhouse / Plantation deck** — still uses the pre-initiative centered
  `Row` layout (`StarshipDeck.BIOMECHANICAL_GREENHOUSE` in `CabinView`). Objects
  (`GreenhousePodObject`, `GreenhouseClimateConsole`) already got the physical-prop
  visual treatment in PR #1, but the deck composition itself needs the same
  corridor/lounge spatial treatment as Crew Habitation and Aquarium Lounge got.
  Natural next step, same recipe: add a `GreenhouseStructure`-style background
  composable, spread pods/console/elevator across absolute offsets instead of
  `Row.SpaceEvenly`.
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

Pick up with the **Greenhouse/Plantation deck** spatial redesign (same recipe
as above — lowest-risk, most mechanical of the remaining items), then decide
whether to do the **individual companion room decor** pass next or return to
it later once all decks have their shell.
