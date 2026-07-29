package com.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.LogEntity
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.BorderStroke
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun GameMainLayout(viewModel: GameViewModel) {
    val state by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedContent(
            targetState = state.screen,
            transitionSpec = {
                fadeIn(animationSpec = tween(1000)) togetherWith fadeOut(animationSpec = tween(800))
            },
            label = "ScreenTransition"
        ) { screen ->
            when (screen) {
                GameScreen.CINEMATIC -> CinematicIntroView(
                    onBegin = { captain, ship ->
                        viewModel.updateProfileNames(captain, ship)
                        viewModel.setScreen(GameScreen.ENTRY)
                    }
                )
                GameScreen.ENTRY -> FirstPersonCabinEntryView(
                    onStepInside = { viewModel.setScreen(GameScreen.CABIN) }
                )
                GameScreen.CABIN -> CabinView(
                    state = state,
                    viewModel = viewModel
                )
            }
        }

        // DEEP SLEEP / REST PROTOCOL OVERLAY
        AnimatedVisibility(
            visible = state.isResting,
            enter = fadeIn(animationSpec = tween(1000)),
            exit = fadeOut(animationSpec = tween(1000))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(CyberObsidian)
                    .clickable(enabled = false) {},
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(
                        color = CyberAmber,
                        modifier = Modifier.size(48.dp),
                        strokeWidth = 3.dp
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "DEEP SLEEP PROTOCOL ACTIVE",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = CyberAmber,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 3.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "REGENERATING BIOMETRICS // RE-POLARIZING CORE COILS",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = CyberCyanDim,
                            fontFamily = FontFamily.Monospace
                        )
                    )
                }
            }
        }
    }
}

// --- 1. CINEMATIC INTRO SCREEN ---
@Composable
fun CinematicIntroView(onBegin: (captain: String, ship: String) -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "stars")
    val starAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "starAlpha"
    )

    var showNamingState by remember { mutableStateOf(false) }
    var captainInput by remember { mutableStateOf("Kenmong") }
    var shipInput by remember { mutableStateOf("Aetherius-VII") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberObsidian)
    ) {
        // Space Background Image
        Image(
            painter = painterResource(id = R.drawable.img_cinematic_space),
            contentDescription = "Deep Space",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Twinkling Star Particles (Canvas overlay)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val random = java.util.Random(42)
            for (i in 0..60) {
                val x = random.nextFloat() * size.width
                val y = random.nextFloat() * size.height
                val radius = random.nextFloat() * 4f + 1f
                val individualAlpha = (random.nextFloat() * 0.5f + 0.5f) * starAlpha
                drawCircle(
                    color = CyberCyan.copy(alpha = individualAlpha),
                    radius = radius,
                    center = Offset(x, y)
                )
            }
        }

        // Dark Gradient Vignette for cozy cinematic aesthetic
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            CyberObsidian.copy(alpha = 0.4f),
                            CyberObsidian.copy(alpha = 0.95f)
                        )
                    )
                )
        )

        // Title and Narrative Column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .statusBarsPadding()
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Space Buffer
            Spacer(modifier = Modifier.height(20.dp))

            if (!showNamingState) {
                // Narrative intro text
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "A LONE STARSHIP DRIFTS THROUGH THE COLD NEON SPACE...",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = CyberCyanDim,
                            letterSpacing = 2.sp,
                            fontFamily = FontFamily.Monospace
                        ),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Holographic App Title
                    Text(
                        text = "QUANTUM EFFECT",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 6.sp,
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 32.sp
                        ),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "CAPTAIN'S QUARTERS",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            color = CyberCyan,
                            fontWeight = FontWeight.Light,
                            letterSpacing = 4.sp,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 18.sp
                        ),
                        textAlign = TextAlign.Center
                    )
                }

                // Begin Journey Button with custom sci-fi styling
                Button(
                    onClick = { showNamingState = true },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(56.dp)
                        .testTag("begin_journey_button")
                        .border(1.5.dp, CyberCyan, CutCornerShape(8.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CyberCyan.copy(alpha = 0.15f),
                        contentColor = CyberCyan
                    ),
                    shape = CutCornerShape(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Begin",
                            tint = CyberCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "INITIALIZE COZY LINK",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        )
                    }
                }
            } else {
                // BIOMETRIC ENROLLMENT CARD
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .border(1.5.dp, CyberCyan, CutCornerShape(8.dp)),
                    colors = CardDefaults.cardColors(containerColor = CyberPanel),
                    shape = CutCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "BIOMETRIC ENROLLMENT TERMINAL",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = CyberCyan,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        )

                        Text(
                            text = "Please establish your credentials for the ship's quantum logging core.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.LightGray,
                                fontFamily = FontFamily.SansSerif
                            ),
                            textAlign = TextAlign.Center
                        )

                        OutlinedTextField(
                            value = captainInput,
                            onValueChange = { captainInput = it },
                            label = { Text("CAPTAIN IDENTITY NAME", fontFamily = FontFamily.Monospace) },
                            textStyle = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace, color = Color.White),
                            modifier = Modifier.fillMaxWidth().testTag("captain_name_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyberCyan,
                                unfocusedBorderColor = CyberBorder,
                                focusedLabelColor = CyberCyan,
                                unfocusedLabelColor = CyberCyanDim
                            ),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = shipInput,
                            onValueChange = { shipInput = it },
                            label = { Text("STARSHIP MODEL ID", fontFamily = FontFamily.Monospace) },
                            textStyle = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace, color = Color.White),
                            modifier = Modifier.fillMaxWidth().testTag("ship_name_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyberCyan,
                                unfocusedBorderColor = CyberBorder,
                                focusedLabelColor = CyberCyan,
                                unfocusedLabelColor = CyberCyanDim
                            ),
                            singleLine = true
                        )

                        Button(
                            onClick = { onBegin(captainInput, shipInput) },
                            enabled = captainInput.isNotBlank() && shipInput.isNotBlank(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("commit_biometrics_button")
                                .border(1.dp, CyberCyan, CutCornerShape(4.dp)),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CyberCyan.copy(alpha = 0.2f),
                                contentColor = CyberCyan
                            ),
                            shape = CutCornerShape(4.dp)
                        ) {
                            Text(
                                text = "COMMIT REGISTRATION KEYS",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(1.dp))
            }
        }
    }
}

// --- 2. FIRST-PERSON ENTRY VIEW ---
@Composable
fun FirstPersonCabinEntryView(onStepInside: () -> Unit) {
    var doorOpened by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val doorScale by animateFloatAsState(
        targetValue = if (doorOpened) 1.5f else 1.0f,
        animationSpec = tween(1500, easing = FastOutSlowInEasing),
        label = "doorScale"
    )
    val doorAlpha by animateFloatAsState(
        targetValue = if (doorOpened) 0.0f else 1.0f,
        animationSpec = tween(1200, easing = LinearEasing),
        label = "doorAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberObsidian)
    ) {
        // Star background behind the sliding doors
        Image(
            painter = painterResource(id = R.drawable.img_cozy_cabin),
            contentDescription = "Cozy Cabin Preview",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Heavy Starship Blast Doors
        if (!doorOpened || doorAlpha > 0.01f) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(scaleX = doorScale, scaleY = doorScale, alpha = doorAlpha)
                    .background(CyberSteel)
                    .drawBehind {
                        // Drawing metal plates and sci-fi neon joints
                        drawLine(
                            color = CyberBorder,
                            start = Offset(0f, size.height / 2f),
                            end = Offset(size.width, size.height / 2f),
                            strokeWidth = 6f
                        )
                        drawLine(
                            color = CyberCyan,
                            start = Offset(size.width / 2f, 0f),
                            end = Offset(size.width / 2f, size.height),
                            strokeWidth = 4f
                        )
                    }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Warning Lock",
                        tint = CyberAmber,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "CAPTAIN'S CABIN [SECURE STATE]",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = CyberAmber,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 1.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "AUTHORIZED DECK BIOMETRICS DETECTED",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = CyberCyan,
                            fontFamily = FontFamily.Monospace
                        )
                    )
                    Spacer(modifier = Modifier.height(48.dp))

                    Button(
                        onClick = {
                            scope.launch {
                                doorOpened = true
                                delay(1500)
                                onStepInside()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(50.dp)
                            .border(1.dp, CyberCyan, RoundedCornerShape(24.dp)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CyberCyan.copy(alpha = 0.2f),
                            contentColor = CyberCyan
                        )
                    ) {
                        Text(
                            text = "CYCLE AIRLOCK",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        )
                    }
                }
            }
        }
    }
}

// --- 3. CABIN MAIN VIEW ---
enum class StarshipDeck {
    CAPTAINS_QUARTERS,
    BIOMECHANICAL_GREENHOUSE,
    AQUARIUM_LOUNGE,
    CREW_HABITATION
}

@Composable
fun CabinView(state: GameUiState, viewModel: GameViewModel) {
    val scrollState = rememberScrollState()
    var activeDeck by remember { mutableStateOf(StarshipDeck.CAPTAINS_QUARTERS) }
    var zoomedNode by remember { mutableStateOf<CabinetNode?>(null) }

    // Elevator transit states
    var isElevatorTransiting by remember { mutableStateOf(false) }
    var transitTargetDeck by remember { mutableStateOf<StarshipDeck?>(null) }
    var transitStateText by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    // Interactive zoom state machine
    val scale by animateFloatAsState(
        targetValue = if (zoomedNode != null) 3.5f else 1.0f,
        animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
        finishedListener = {
            if (zoomedNode != null) {
                // When zoom animation completes, activate the corresponding panel and reset zoom
                viewModel.setNode(zoomedNode!!)
                zoomedNode = null
            }
        },
        label = "room_zoom"
    )

    val pivotX by animateFloatAsState(
        targetValue = when (zoomedNode) {
            CabinetNode.WINDOW -> 0.08f
            CabinetNode.AI -> 0.28f
            CabinetNode.DESK -> 0.48f
            CabinetNode.COFFEE -> 0.68f
            CabinetNode.BOOKSHELF -> 0.82f
            CabinetNode.CREW -> 0.94f
            CabinetNode.GREENHOUSE -> 0.35f
            CabinetNode.AQUARIUM -> 0.5f
            CabinetNode.PET_SANCTUARY -> 0.75f
            CabinetNode.ELEVATOR -> 0.5f
            else -> 0.5f
        },
        animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
        label = "zoom_pivot_x"
    )

    val pivotY by animateFloatAsState(
        targetValue = when (zoomedNode) {
            CabinetNode.WINDOW -> 0.22f
            CabinetNode.AI -> 0.38f
            CabinetNode.DESK -> 0.65f
            CabinetNode.COFFEE -> 0.62f
            CabinetNode.BOOKSHELF -> 0.48f
            CabinetNode.CREW -> 0.35f
            CabinetNode.GREENHOUSE -> 0.45f
            CabinetNode.AQUARIUM -> 0.5f
            CabinetNode.PET_SANCTUARY -> 0.45f
            CabinetNode.ELEVATOR -> 0.5f
            else -> 0.5f
        },
        animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
        label = "zoom_pivot_y"
    )

    // Reset scroll when deck changes
    LaunchedEffect(activeDeck) {
        scrollState.scrollTo(0)
    }

    val onElevatorDeckSelected: (StarshipDeck) -> Unit = { target ->
        if (target != activeDeck) {
            transitTargetDeck = target
            isElevatorTransiting = true
            transitStateText = "TRANSITING TO ${target.name.replace("_", " ")}"
            viewModel.setNode(CabinetNode.NONE)
            coroutineScope.launch {
                delay(1500)
                activeDeck = target
                isElevatorTransiting = false
                transitTargetDeck = null
            }
        } else {
            viewModel.setNode(CabinetNode.NONE)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberObsidian)
    ) {
        // --- THE PANORAMIC SCROLLABLE ROOM ---
        Box(
            modifier = Modifier
                .fillMaxSize()
                .horizontalScroll(scrollState)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1380.dp) // Generous horizontal workspace
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        transformOrigin = androidx.compose.ui.graphics.TransformOrigin(pivotX, pivotY)
                    }
            ) {
                when (activeDeck) {
                    StarshipDeck.CAPTAINS_QUARTERS -> {
                        // Wide cozy cabin background image
                        Image(
                            painter = painterResource(id = R.drawable.img_cozy_cabin),
                            contentDescription = "Captain's Quarters Cabin",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        // Dynamic Weather Particles Overlay inside the room
                        WeatherOverlayEffect(weather = state.selectedWeather)

                        // Ambient Tint based on real/chosen weather & buffs
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            getWeatherColor(state.selectedWeather).copy(alpha = 0.22f),
                                            Color.Transparent,
                                            CyberObsidian.copy(alpha = 0.80f)
                                        )
                                    )
                                )
                        )

                        // --- PANORAMIC INTERACTIVE OBJECTS (TACTILE PHYSICAL HOTSPOTS) ---
                        
                        // 1. STELLAR VIEWPORT (Observation Window)
                        StellarViewportObject(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .offset(x = 80.dp, y = 100.dp),
                            weather = state.selectedWeather,
                            onClick = { if (zoomedNode == null) zoomedNode = CabinetNode.WINDOW }
                        )

                        // 2. L.I.L.A. AI TERMINAL (Floating Hologram Core)
                        AiTerminalObject(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .offset(x = 340.dp, y = 140.dp),
                            onClick = { if (zoomedNode == null) zoomedNode = CabinetNode.AI }
                        )

                        // 3. CAPTAIN'S COMMAND DESK & INTERACTIVE SLATE
                        CommandDeskSlateObject(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .offset(x = 580.dp, y = 240.dp),
                            onClick = { if (zoomedNode == null) zoomedNode = CabinetNode.DESK }
                        )

                        // 4. COFFEE BREWING BAY (Steaming ceramic mug)
                        CoffeeBrewingObject(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .offset(x = 800.dp, y = 220.dp),
                            buffText = state.activeBuff,
                            onClick = { if (zoomedNode == null) zoomedNode = CabinetNode.COFFEE }
                        )

                        // 5. THE STELLAR CODEX (Holographic ancient bookshelf)
                        StellarCodexObject(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .offset(x = 1020.dp, y = 160.dp),
                            onClick = { if (zoomedNode == null) zoomedNode = CabinetNode.BOOKSHELF }
                        )

                        // 6. TRANSIT ELEVATOR PORTAL
                        StarshipElevatorHotspot(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .offset(x = 1200.dp, y = 100.dp),
                            onClick = { if (zoomedNode == null) zoomedNode = CabinetNode.ELEVATOR }
                        )
                    }
                    StarshipDeck.BIOMECHANICAL_GREENHOUSE -> {
                        BiomechanicalGreenhouseBackground()
                        GreenhouseBioluminescentParticles()

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 110.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            GreenhousePodObject(
                                title = "HYDRO-POD ALPHA",
                                plantName = state.plants.getOrNull(0)?.name ?: "Empty Plot",
                                growth = state.plants.getOrNull(0)?.growthProgress ?: 0,
                                species = state.plants.getOrNull(0)?.species ?: "Stellar Lily",
                                onClick = { if (zoomedNode == null) zoomedNode = CabinetNode.GREENHOUSE }
                            )

                            GreenhousePodObject(
                                title = "HYDRO-POD BETA",
                                plantName = state.plants.getOrNull(1)?.name ?: "Empty Plot",
                                growth = state.plants.getOrNull(1)?.growthProgress ?: 0,
                                species = state.plants.getOrNull(1)?.species ?: "Chronos Fern",
                                onClick = { if (zoomedNode == null) zoomedNode = CabinetNode.GREENHOUSE }
                            )

                            GreenhouseClimateConsole(
                                onClick = { if (zoomedNode == null) zoomedNode = CabinetNode.GREENHOUSE }
                            )

                            StarshipElevatorHotspot(
                                onClick = { if (zoomedNode == null) zoomedNode = CabinetNode.ELEVATOR }
                            )
                        }
                    }
                    StarshipDeck.AQUARIUM_LOUNGE -> {
                        AquariumLoungeBackground()
                        AquariumLoungeParticles()

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 60.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Text(
                                    text = "COMMUNAL AQUA-TANK SYSTEM",
                                    color = CyberCyan,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                )

                                Box(
                                    modifier = Modifier
                                        .size(width = 520.dp, height = 130.dp)
                                        .border(2.dp, CyberCyan, RoundedCornerShape(12.dp))
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable { if (zoomedNode == null) zoomedNode = CabinetNode.AQUARIUM }
                                ) {
                                    AquariumSimulationTank(fishList = state.fish)
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(
                                                Brush.radialGradient(
                                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.5f))
                                                )
                                            )
                                    )
                                }

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(40.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    NutrientFeederConsole(
                                        onClick = { if (zoomedNode == null) zoomedNode = CabinetNode.AQUARIUM }
                                    )

                                    LoungeJukeboxObject(
                                        onClick = { if (zoomedNode == null) zoomedNode = CabinetNode.AQUARIUM }
                                    )

                                    StarshipElevatorHotspot(
                                        onClick = { if (zoomedNode == null) zoomedNode = CabinetNode.ELEVATOR }
                                    )
                                }
                            }
                        }
                    }
                    StarshipDeck.CREW_HABITATION -> {
                        CrewHabitationBackground()
                        CrewHabitationParticles()

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 110.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CompanionHabitationRoomObject(
                                name = "JAX",
                                role = "BIO-MECHANIC ENGINEER",
                                color = Color(0xFFFFB300),
                                activeQuest = "DRONE GRID ACTIVE",
                                onClick = { if (zoomedNode == null) zoomedNode = CabinetNode.CREW }
                            )

                            CompanionHabitationRoomObject(
                                name = "LYRA",
                                role = "TACTICAL FOCUS OPERATIVE",
                                color = Color(0xFFC084FC),
                                activeQuest = "TACTICAL FOCUS STABLE",
                                onClick = { if (zoomedNode == null) zoomedNode = CabinetNode.CREW }
                            )

                            PetIncubatorSanctuaryObject(
                                petCount = state.recruitedPets.size,
                                onClick = { if (zoomedNode == null) zoomedNode = CabinetNode.PET_SANCTUARY }
                            )

                            StarshipElevatorHotspot(
                                onClick = { if (zoomedNode == null) zoomedNode = CabinetNode.ELEVATOR }
                            )
                        }
                    }
                }
            }
        }

        // --- SWIPE PROMPT HUD overlay (fades out when scrolled) ---
        AnimatedVisibility(
            visible = scrollState.value < 50 && zoomedNode == null,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.SwapHoriz,
                    contentDescription = "Swipe",
                    tint = CyberCyan,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "SWIPE TO OBSERVE ${activeDeck.name.replace("_", " ")}",
                    color = CyberCyanDim,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // --- FIXED HUD FOREGROUND DASHBOARD OVERLAYS ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // HUD TOP BAR (Fixed on screen)
            CabinTopHud(state = state)

            Spacer(modifier = Modifier.weight(1f))

            // HUD BOTTOM LOGO & STATUS BAR (Fixed on screen)
            CabinBottomHud(state = state)
        }

        // --- SLIDING/EXPANDED DETAILED PANELS OVERLAY ---
        AnimatedVisibility(
            visible = state.activeNode != CabinetNode.NONE,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            InteractivePanelContainer(
                title = getPanelTitle(state.activeNode),
                onClose = { viewModel.setNode(CabinetNode.NONE) }
            ) {
                when (state.activeNode) {
                    CabinetNode.WINDOW -> ObservationWindowPanel(state, viewModel)
                    CabinetNode.DESK -> CaptainDeskPanel(state, viewModel)
                    CabinetNode.AI -> AiTerminalPanel(state, viewModel)
                    CabinetNode.COFFEE -> CoffeeCornerPanel(state, viewModel)
                    CabinetNode.BOOKSHELF -> BookshelfPanel(state, viewModel)
                    CabinetNode.CREW -> CompanionQuartersPanel(state, viewModel)
                    CabinetNode.GREENHOUSE -> GreenhousePanel(state, viewModel)
                    CabinetNode.AQUARIUM -> AquariumPanel(state, viewModel)
                    CabinetNode.PET_SANCTUARY -> PetSanctuaryPanel(state, viewModel)
                    CabinetNode.ELEVATOR -> ElevatorPanel(activeDeck, onElevatorDeckSelected, state, viewModel)
                    else -> Spacer(modifier = Modifier.height(1.dp))
                }
            }
        }

        // --- IMMERSIVE ELEVATOR TRANSIT OVERLAY ---
        AnimatedVisibility(
            visible = isElevatorTransiting,
            enter = fadeIn(animationSpec = tween(400)),
            exit = fadeOut(animationSpec = tween(400))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.95f))
                    .clickable(enabled = false) {},
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "QUANTUM TRANSIT LIFT",
                        color = CyberCyan,
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )

                    // Blinking lift elevator indicator
                    val infiniteTransition = rememberInfiniteTransition(label = "transit_blink")
                    val transitAlpha by infiniteTransition.animateFloat(
                        initialValue = 0.2f,
                        targetValue = 1.0f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(400, easing = LinearEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "transit_blink"
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = "Lift moving",
                            tint = CyberCyan.copy(alpha = transitAlpha),
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = transitStateText,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Lift moving",
                            tint = CyberCyan.copy(alpha = transitAlpha),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Simulated deck indicator lights moving vertically
                    Box(
                        modifier = Modifier
                            .width(180.dp)
                            .height(10.dp)
                            .background(Color.DarkGray, RoundedCornerShape(4.dp))
                            .border(1.dp, CyberBorder, RoundedCornerShape(4.dp))
                    ) {
                        val progress by animateFloatAsState(
                            targetValue = 1.0f,
                            animationSpec = tween(1200, easing = LinearOutSlowInEasing),
                            label = "transit_bar"
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(progress)
                                .background(CyberCyan, RoundedCornerShape(4.dp))
                        )
                    }

                    Text(
                        text = "MAGNETIC DECK COUPLING ... STABLE",
                        color = MatrixGreen,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}

@Composable
fun StellarViewportObject(
    modifier: Modifier = Modifier,
    weather: String,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "viewport")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "viewport_glow"
    )

    Column(
        modifier = modifier
            .width(130.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(width = 120.dp, height = 75.dp)
                .background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                .border(2.dp, CyberCyan.copy(alpha = glowAlpha), RoundedCornerShape(4.dp))
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Draw a beautiful custom cybernetic overlay
                drawRect(
                    color = CyberCyan.copy(alpha = 0.15f),
                    size = size
                )
                // Reticle lines
                drawLine(
                    color = CyberCyan.copy(alpha = 0.4f),
                    start = Offset(0f, size.height / 2),
                    end = Offset(size.width, size.height / 2),
                    strokeWidth = 1f
                )
                // Draw blinking status indicator dot
                drawCircle(
                    color = CyberCyan,
                    radius = 3f,
                    center = Offset(size.width - 10f, 10f)
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Visibility,
                    contentDescription = "Viewport",
                    tint = CyberCyan,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "VIEWPORT ACTIVE",
                    color = CyberCyan,
                    fontSize = 8.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "OBSERVATION WINDOW",
            color = Color.White,
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(2.dp))
                .padding(horizontal = 4.dp, vertical = 2.dp)
        )
        Text(
            text = weather.uppercase(),
            color = CyberCyanDim,
            fontSize = 8.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun AiTerminalObject(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ai_terminal")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Column(
        modifier = modifier
            .width(110.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(50))
                .border(2.dp, MatrixGreen, RoundedCornerShape(50))
                .padding(6.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Floating concentric ring
                drawCircle(
                    color = MatrixGreen.copy(alpha = 0.2f),
                    radius = size.width / 2,
                    style = Stroke(width = 2f)
                )
            }
            Icon(
                imageVector = Icons.Default.DeveloperMode,
                contentDescription = "AI Core",
                tint = MatrixGreen,
                modifier = Modifier
                    .size(28.dp)
                    .graphicsLayer(rotationZ = rotation)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "L.I.L.A. CORE",
            color = Color.White,
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(2.dp))
                .padding(horizontal = 4.dp, vertical = 2.dp)
        )
        Text(
            text = "AI TERMINAL",
            color = MatrixGreen,
            fontSize = 8.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CommandDeskSlateObject(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "slate")
    val bounceY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounce"
    )

    Column(
        modifier = modifier
            .width(120.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .offset(y = bounceY.dp)
                .size(width = 90.dp, height = 65.dp)
                .background(CyberPanel, CutCornerShape(4.dp))
                .border(2.dp, CyberAmber, CutCornerShape(4.dp))
                .padding(6.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Computer,
                    contentDescription = "Slate",
                    tint = CyberAmber,
                    modifier = Modifier.size(22.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(4.dp).background(CyberAmber, RoundedCornerShape(50)))
                    Text(
                        text = "SLATE ONLINE",
                        color = CyberAmber,
                        fontSize = 8.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "COMMAND DESK",
            color = Color.White,
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(2.dp))
                .padding(horizontal = 4.dp, vertical = 2.dp)
        )
        Text(
            text = "LOGS & STELLAR SECTORS",
            color = CyberAmber.copy(alpha = 0.6f),
            fontSize = 7.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CoffeeBrewingObject(
    modifier: Modifier = Modifier,
    buffText: String,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "coffee")
    val steamAlpha by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "steam"
    )

    Column(
        modifier = modifier
            .width(110.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(65.dp)
                .background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                .border(1.5.dp, CyberMagenta, RoundedCornerShape(6.dp))
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Animated Steam lines
                Canvas(modifier = Modifier.size(width = 20.dp, height = 12.dp)) {
                    val path = Path()
                    path.moveTo(5f, size.height)
                    path.quadraticTo(2f, size.height * 0.5f, 5f, 0f)
                    path.moveTo(10f, size.height)
                    path.quadraticTo(8f, size.height * 0.5f, 10f, 0f)
                    path.moveTo(15f, size.height)
                    path.quadraticTo(12f, size.height * 0.5f, 15f, 0f)
                    drawPath(
                        path = path,
                        color = Color.White.copy(alpha = steamAlpha),
                        style = Stroke(width = 2f)
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Icon(
                    imageVector = Icons.Default.LocalCafe,
                    contentDescription = "Coffee Corner",
                    tint = CyberMagenta,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "BREWING BAY",
            color = Color.White,
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(2.dp))
                .padding(horizontal = 4.dp, vertical = 2.dp)
        )
        Text(
            text = if (buffText.lowercase().contains("none")) "MUG FULL // COZY" else "BUFF: $buffText",
            color = CyberMagenta,
            fontSize = 8.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun StellarCodexObject(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "codex")
    val floatY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float"
    )

    Column(
        modifier = modifier
            .width(110.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .offset(y = floatY.dp)
                .size(65.dp)
                .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                .border(1.5.dp, CyberCyanDim, RoundedCornerShape(8.dp))
                .padding(6.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Book,
                    contentDescription = "Codex",
                    tint = CyberCyanDim,
                    modifier = Modifier.size(26.dp)
                )
                Text(
                    text = "LIB-ACTIVE",
                    color = CyberCyanDim,
                    fontSize = 8.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "STELLAR CODEX",
            color = Color.White,
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(2.dp))
                .padding(horizontal = 4.dp, vertical = 2.dp)
        )
        Text(
            text = "ANCIENT RECORDS",
            color = Color.Gray,
            fontSize = 8.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CrewVaultDoorObject(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "vault_door")
    val keypadPulse by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Column(
        modifier = modifier
            .width(120.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(width = 80.dp, height = 80.dp)
                .background(CyberPanel, RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                .border(2.dp, Color(0xFFC084FC), RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                .padding(6.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Vertical vault slide line
                drawLine(
                    color = Color(0xFFC084FC).copy(alpha = 0.5f),
                    start = Offset(size.width / 2, 0f),
                    end = Offset(size.width / 2, size.height),
                    strokeWidth = 2f
                )
                // Electronic digital lock keypad glow
                drawCircle(
                    color = Color(0xFFC084FC).copy(alpha = keypadPulse),
                    radius = 4f,
                    center = Offset(size.width * 0.75f, size.height * 0.4f)
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.People,
                    contentDescription = "Crew Door",
                    tint = Color(0xFFC084FC),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "VAULT SECURE",
                    color = Color(0xFFC084FC),
                    fontSize = 8.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "CREW & ECO-CORE",
            color = Color.White,
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(2.dp))
                .padding(horizontal = 4.dp, vertical = 2.dp)
        )
        Text(
            text = "COMPANION ROOMS",
            color = Color(0xFFC084FC),
            fontSize = 8.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
    }
}

// --- 3A. Cabin HUD Top Bar ---
@Composable
fun CabinTopHud(state: GameUiState) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .border(1.dp, CyberBorder, CutCornerShape(4.dp)),
        colors = CardDefaults.cardColors(containerColor = CyberPanel),
        shape = CutCornerShape(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Stardate & Day
            Column {
                Text(
                    text = "STARDATE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = CyberCyanDim,
                        letterSpacing = 1.sp,
                        fontFamily = FontFamily.Monospace
                    )
                )
                Text(
                    text = "SD 8243.${state.calendarDay * 7 + 1}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                )
            }

            // Credits count
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "ENERGY CREDITS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = CyberCyanDim,
                        letterSpacing = 1.sp,
                        fontFamily = FontFamily.Monospace
                    )
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.GeneratingTokens,
                        contentDescription = "Credits Symbol",
                        tint = CyberAmber,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${state.credits} ⚿",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = CyberAmber,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    )
                }
            }

            // Active Buff Status
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "COGNITIVE STIMULANT",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = CyberCyanDim,
                        letterSpacing = 1.sp,
                        fontFamily = FontFamily.Monospace
                    )
                )
                Text(
                    text = state.activeBuff,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = if (state.activeBuff == "None") Color.Gray else CyberCyan,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                )
            }
        }
    }
}

// --- 3B. Cabin HUD Bottom Status Bar ---
@Composable
fun CabinBottomHud(state: GameUiState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CyberPanel)
            .then(TopStroke(1.dp, CyberBorder))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "SHIP: ${state.shipName} // COMM: STABLE",
            style = MaterialTheme.typography.labelSmall.copy(
                color = CyberCyanDim,
                fontFamily = FontFamily.Monospace
            )
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MatrixGreen)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "NEURAL LINK: ESTABLISHED",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MatrixGreen,
                    fontFamily = FontFamily.Monospace
                )
            )
        }
    }
}

// Helper fun to draw a top stroke border
fun TopStroke(width: androidx.compose.ui.unit.Dp, color: Color) = Modifier.drawBehind {
    val strokeWidthPx = width.toPx()
    drawLine(
        color = color,
        start = Offset(0f, 0f),
        end = Offset(size.width, 0f),
        strokeWidth = strokeWidthPx
    )
}

// --- 4. INDIVIDUAL INTERACTIVE HOTSPOT COMPONENT ---
@Composable
fun HotspotNode(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scalePulse by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scalePulse"
    )

    Card(
        modifier = Modifier
            .width(170.dp)
            .clickable(onClick = onClick)
            .graphicsLayer(scaleX = scalePulse, scaleY = scalePulse)
            .border(1.dp, color.copy(alpha = 0.6f), CutCornerShape(8.dp)),
        colors = CardDefaults.cardColors(containerColor = CyberPanel.copy(alpha = 0.85f)),
        shape = CutCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
                    .border(1.dp, color, RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 1.sp,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = CyberCyanDim,
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

// --- 5. DETAILED SLIDING CONTAINER PANEL ---
@Composable
fun InteractivePanelContainer(
    title: String,
    onClose: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberObsidian.copy(alpha = 0.8f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClose() }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.85f)
                .border(2.dp, CyberCyan, CutCornerShape(12.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { /* Stop clicks from dismissing */ },
            colors = CardDefaults.cardColors(containerColor = CyberPanel),
            shape = CutCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // PANEL HEADER
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .then(BottomStroke(1.dp, CyberBorder)),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(CyberCyan)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = CyberCyan,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                letterSpacing = 2.sp
                            )
                        )
                    }

                    IconButton(
                        onClick = onClose,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = CyberMagenta
                        )
                    }
                }

                // PANEL SCROLLABLE CONTENT
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    content()
                }
            }
        }
    }
}

fun BottomStroke(width: androidx.compose.ui.unit.Dp, color: Color) = Modifier.drawBehind {
    val strokeWidthPx = width.toPx()
    drawLine(
        color = color,
        start = Offset(0f, size.height),
        end = Offset(size.width, size.height),
        strokeWidth = strokeWidthPx
    )
}

fun getPanelTitle(node: CabinetNode): String = when (node) {
    CabinetNode.WINDOW -> "COSMIC OBSERVERS DECK"
    CabinetNode.DESK -> "CAPTAIN'S WORKSPACE"
    CabinetNode.AI -> "CORE AI SYSTEM INTERFACE"
    CabinetNode.COFFEE -> "REPLICATOR & BREWING BAY"
    CabinetNode.BOOKSHELF -> "STELLAR ENCYCLOPEDIA CODEX"
    CabinetNode.CREW -> "COMPANION QUARTERS // CREW DECK"
    CabinetNode.GREENHOUSE -> "BIOMECHANICAL GREENHOUSE ARRAY"
    CabinetNode.AQUARIUM -> "QUANTUM BIO-AQUARIUM MANAGER"
    CabinetNode.PET_SANCTUARY -> "CYBER-PET FIRMWARE HARNESS"
    CabinetNode.ELEVATOR -> "QUANTUM TRANSIT LIFT"
    else -> "STARSHIP SYSTEM"
}

// --- 6. OBSERVATION WINDOW DETAILED PANEL ---
@Composable
fun ObservationWindowPanel(state: GameUiState, viewModel: GameViewModel) {
    val weatherTypes = listOf(
        "Clear Stellar Lanes",
        "Quantum Storm",
        "Ice Comet Shower",
        "Dense Nebula",
        "Electromagnetic Interference"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Celestial Visual Card Simulation
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .border(1.dp, CyberBorder, CutCornerShape(8.dp)),
            colors = CardDefaults.cardColors(containerColor = CyberObsidian),
            shape = CutCornerShape(8.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                // Interactive star map view
                Image(
                    painter = painterResource(id = R.drawable.img_cinematic_space),
                    contentDescription = "Cosmic View",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                WeatherOverlayEffect(weather = state.selectedWeather)

                // Calibration Ring Overlay
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = CyberCyan.copy(alpha = 0.3f),
                        radius = size.minDimension / 2.3f,
                        style = Stroke(width = 2f)
                    )
                    drawLine(
                        color = CyberCyan.copy(alpha = 0.2f),
                        start = Offset(0f, size.height / 2f),
                        end = Offset(size.width, size.height / 2f)
                    )
                    drawLine(
                        color = CyberCyan.copy(alpha = 0.2f),
                        start = Offset(size.width / 2f, 0f),
                        end = Offset(size.width / 2f, size.height)
                    )
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .background(CyberPanel.copy(alpha = 0.8f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .border(1.dp, CyberCyan, RoundedCornerShape(4.dp))
                ) {
                    Text(
                        text = "SENSOR FIELD: ${state.selectedWeather.uppercase()}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = CyberCyan,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Weather Selection / Simulation Actions
        Text(
            text = "CALIBRATE DEFLECTOR SHIELDS / ENVIRO-CONDITIONS",
            style = MaterialTheme.typography.bodySmall.copy(
                color = CyberCyanDim,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            weatherTypes.forEach { weather ->
                val isSelected = state.selectedWeather == weather
                Button(
                    onClick = { viewModel.setWeather(weather) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            if (isSelected) CyberCyan else CyberBorder,
                            CutCornerShape(4.dp)
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) CyberCyan.copy(alpha = 0.2f) else CyberSteel,
                        contentColor = if (isSelected) CyberCyan else Color.White
                    ),
                    shape = CutCornerShape(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = weather,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = FontFamily.Monospace,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        )
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Active",
                                tint = CyberCyan,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// --- 7. COMMAND DESK PANEL (LOGS & GALAXY MAP) ---
@Composable
fun CaptainDeskPanel(state: GameUiState, viewModel: GameViewModel) {
    var deskTab by remember { mutableStateOf(0) } // 0 = Logs, 1 = Star Map, 2 = Subspace Mail

    Column(modifier = Modifier.fillMaxSize()) {
        // --- A. DAILY BRIEFING & RITUAL CORE ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .border(1.dp, CyberBorder, CutCornerShape(8.dp)),
            colors = CardDefaults.cardColors(containerColor = CyberPanel),
            shape = CutCornerShape(8.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "DAILY BRIEFING TERMINAL // DAY ${state.calendarDay}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = CyberAmber,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 1.sp
                        )
                    )
                    Box(
                        modifier = Modifier
                            .background(CyberCyan.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                            .border(1.dp, CyberCyan, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "BUFF: ${state.activeBuff.uppercase()}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = CyberCyan,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "STATUS: Deflector shields stabilized under [${state.selectedWeather.uppercase()}]. A.D.A.M. system monitoring neural alignment. Standard duty protocol dictates completing the daily ritual loop below before initiating resting routines.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.LightGray,
                        fontFamily = FontFamily.SansSerif,
                        lineHeight = 15.sp
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                HorizontalDivider(color = CyberBorder, thickness = 1.dp)

                Spacer(modifier = Modifier.height(10.dp))

                // --- B. RITUAL CHECKLIST ---
                Text(
                    text = "DAILY RITUAL TRACKER PROTOCOL",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = CyberCyanDim,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 9.sp
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    RitualCheckItem(
                        title = "Synthesize & Consume Morning Coffee",
                        isChecked = state.coffeeBrewedToday
                    )
                    RitualCheckItem(
                        title = "Launch Deep Sector Radar Probe Scan",
                        isChecked = state.radarScannedToday
                    )
                    RitualCheckItem(
                        title = "Record Daily Log & Commit AI Reflection",
                        isChecked = state.logRecordedToday
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // --- C. REST / SLEEP MATRIX ACTUATOR ---
                val ritualsDone = state.coffeeBrewedToday && state.radarScannedToday && state.logRecordedToday
                Button(
                    onClick = { viewModel.restAndAdvanceDay() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            if (ritualsDone) CyberAmber else CyberBorder.copy(alpha = 0.5f),
                            CutCornerShape(4.dp)
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (ritualsDone) CyberAmber.copy(alpha = 0.2f) else CyberSteel.copy(alpha = 0.2f),
                        contentColor = if (ritualsDone) CyberAmber else Color.Gray
                    ),
                    shape = CutCornerShape(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Hotel,
                        contentDescription = "Rest",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (ritualsDone) "INITIATE SLEEP PROTOCOLS (+150 ⚿)" else "INITIATE SLEEP (RITUALS INCOMPLETE)",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // --- D. DESK SYSTEM TABS ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { deskTab = 0 },
                modifier = Modifier
                    .weight(1f)
                    .border(
                        1.dp,
                        if (deskTab == 0) CyberAmber else CyberBorder,
                        CutCornerShape(4.dp)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (deskTab == 0) CyberAmber.copy(alpha = 0.15f) else CyberSteel,
                    contentColor = if (deskTab == 0) CyberAmber else Color.White
                ),
                shape = CutCornerShape(4.dp)
            ) {
                Text(
                    text = "LOGS",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                )
            }

            Button(
                onClick = { deskTab = 1 },
                modifier = Modifier
                    .weight(1f)
                    .border(
                        1.dp,
                        if (deskTab == 1) CyberAmber else CyberBorder,
                        CutCornerShape(4.dp)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (deskTab == 1) CyberAmber.copy(alpha = 0.15f) else CyberSteel,
                    contentColor = if (deskTab == 1) CyberAmber else Color.White
                ),
                shape = CutCornerShape(4.dp)
            ) {
                Text(
                    text = "RADAR",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                )
            }

            Button(
                onClick = { deskTab = 2 },
                modifier = Modifier
                    .weight(1f)
                    .border(
                        1.dp,
                        if (deskTab == 2) CyberAmber else CyberBorder,
                        CutCornerShape(4.dp)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (deskTab == 2) CyberAmber.copy(alpha = 0.15f) else CyberSteel,
                    contentColor = if (deskTab == 2) CyberAmber else Color.White
                ),
                shape = CutCornerShape(4.dp)
            ) {
                Text(
                    text = "MAIL",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                )
            }
        }

        // TAB DISPLAY
        Box(modifier = Modifier.weight(1f)) {
            when (deskTab) {
                0 -> CaptainLogSection(state, viewModel)
                1 -> GalaxyRadarSection(state, viewModel)
                2 -> SubspaceMailSection(state, viewModel)
            }
        }
    }
}

@Composable
fun RitualCheckItem(title: String, isChecked: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (isChecked) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
            contentDescription = if (isChecked) "Completed" else "Incomplete",
            tint = if (isChecked) CyberCyan else Color.Gray,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall.copy(
                color = if (isChecked) Color.White else Color.Gray,
                fontFamily = FontFamily.Monospace
            )
        )
    }
}

@Composable
fun SubspaceMailSection(state: GameUiState, viewModel: GameViewModel) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (state.activeSubspaceMails.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = CyberSteel.copy(alpha = 0.2f))
                ) {
                    Text(
                        text = "Subspace mail lanes clear. No incoming transmissions.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.Gray,
                            fontFamily = FontFamily.Monospace
                        ),
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            items(state.activeSubspaceMails) { mail ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            if (mail.processed) CyberBorder else CyberCyan.copy(alpha = 0.8f),
                            RoundedCornerShape(8.dp)
                        ),
                    colors = CardDefaults.cardColors(containerColor = CyberSteel.copy(alpha = 0.4f)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = mail.sender,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (mail.processed) Color.Gray else CyberCyan,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            )
                            Text(
                                text = mail.date,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.Gray,
                                    fontFamily = FontFamily.Monospace
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = mail.subject,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = mail.body,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.LightGray,
                                fontFamily = FontFamily.SansSerif
                            )
                        )

                        if (!mail.processed && mail.actionText != null) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = { viewModel.resolveMail(mail.id) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CyberCyan.copy(alpha = 0.15f),
                                    contentColor = CyberCyan
                                ),
                                shape = CutCornerShape(4.dp)
                            ) {
                                Text(
                                    text = mail.actionText,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        } else if (mail.processed) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "TRANSMISSION SECURED & ARCHIVED",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = CyberBorder,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CaptainLogSection(state: GameUiState, viewModel: GameViewModel) {
    val moods = listOf("Serene", "Alert", "Pensive", "Excited", "Exhausted")

    Column(modifier = Modifier.fillMaxSize()) {
        // Form Layout
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .border(1.dp, CyberBorder, CutCornerShape(8.dp)),
            colors = CardDefaults.cardColors(containerColor = CyberSteel.copy(alpha = 0.5f)),
            shape = CutCornerShape(8.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "RECORD TODAY'S OBSERVATION",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = CyberAmber,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Log Text Field
                OutlinedTextField(
                    value = state.logInputText,
                    onValueChange = { viewModel.updateLogInputText(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.LightGray,
                        focusedBorderColor = CyberAmber,
                        unfocusedBorderColor = CyberBorder
                    ),
                    placeholder = {
                        Text(
                            text = "Write your thoughts, Captain...",
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Mood Buttons Flow
                Text(
                    text = "SELECT BIOMETRIC COGNITIVE MOOD",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = CyberCyanDim,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace
                    ),
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    moods.forEach { mood ->
                        val isSelected = state.logInputMood == mood
                        Box(
                            modifier = Modifier
                                .clickable { viewModel.updateLogInputMood(mood) }
                                .background(
                                    if (isSelected) CyberAmber.copy(alpha = 0.25f) else Color.Transparent,
                                    RoundedCornerShape(12.dp)
                                )
                                .border(
                                    1.dp,
                                    if (isSelected) CyberAmber else CyberBorder,
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = mood,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (isSelected) CyberAmber else Color.LightGray,
                                    fontFamily = FontFamily.Monospace
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Submit Button with loader
                Button(
                    onClick = { viewModel.saveLog() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, CyberAmber, CutCornerShape(4.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CyberAmber.copy(alpha = 0.15f),
                        contentColor = CyberAmber
                    ),
                    shape = CutCornerShape(4.dp),
                    enabled = !state.isSavingLog && state.logInputText.isNotBlank()
                ) {
                    if (state.isSavingLog) {
                        CircularProgressIndicator(
                            color = CyberAmber,
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "SAVE & COMMIT DIALECTIC REFLECTION",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Historical logs header
        Text(
            text = "LOG HISTORICAL DEPOSIT DATA",
            style = MaterialTheme.typography.labelSmall.copy(
                color = CyberCyanDim,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            ),
            modifier = Modifier.padding(bottom = 6.dp)
        )

        // Logs Scroll list
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (state.logs.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = CyberSteel.copy(alpha = 0.2f))
                    ) {
                        Text(
                            text = "No logs recorded yet. Write your first log above to begin.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.Gray,
                                fontFamily = FontFamily.Monospace
                            ),
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                items(state.logs) { log ->
                    LogItemCard(log, onDelete = { viewModel.deleteLog(log.id) })
                }
            }
        }
    }
}

@Composable
fun LogItemCard(log: LogEntity, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CyberBorder, RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(containerColor = CyberSteel.copy(alpha = 0.4f)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .background(CyberAmber.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = log.stardate,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = CyberAmber,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Mood: ${log.mood}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color.LightGray,
                            fontFamily = FontFamily.Monospace
                        )
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = CyberMagenta.copy(alpha = 0.8f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = log.text,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White,
                    fontFamily = FontFamily.SansSerif
                )
            )

            if (log.aiReflection.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CyberCyan.copy(alpha = 0.05f), RoundedCornerShape(4.dp))
                        .border(1.dp, CyberCyan.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                        .padding(8.dp)
                ) {
                    Column {
                        Text(
                            text = "AI reflection // A.D.A.M.",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = CyberCyanDim,
                                fontSize = 8.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = log.aiReflection,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = CyberCyan,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GalaxyRadarSection(state: GameUiState, viewModel: GameViewModel) {
    val density = androidx.compose.ui.platform.LocalDensity.current
    val scope = rememberCoroutineScope()

    // Dynamic sectors fallback if state is empty (safeguard)
    val sectorsList = state.sectors.ifEmpty {
        listOf(
            SectorState(
                id = "home_port",
                name = "Aetherius Home Port",
                classification = "Central Base Sector",
                description = "Our primary deep-space command terminal and hyperlane harbor. Fully charted, secured by fleet patrol forces, and operates as a safe zone.",
                coordX = 180f,
                coordY = 110f,
                hazardLevel = "LOW",
                stability = 100,
                cartographyProgress = 100,
                isFullyCartographed = true,
                alignment = "Neutral",
                temperatureKelvin = 293,
                gravityGs = 1.0,
                targetFrequency = 0.50f,
                anomalySignatures = listOf("Stable Warp Vortex", "Beacon Alpha Sync"),
                scannedResources = listOf("Pure Hydrazine", "Hyper-conductors")
            ),
            SectorState(
                id = "sigma_9",
                name = "Sector Sigma-9",
                classification = "Technopunk Hub",
                description = "A glittering neon megacity sector built onto a massive hollow asteroid. Run by Technopunks who refine hyper-matter. High-frequency signals constantly jam old sensors.",
                coordX = 60f,
                coordY = 70f,
                hazardLevel = "MEDIUM",
                stability = 74,
                cartographyProgress = 30,
                isFullyCartographed = false,
                alignment = "Technopunk",
                temperatureKelvin = 340,
                gravityGs = 0.65,
                targetFrequency = 0.82f,
                anomalySignatures = listOf("Hyper-matter Smog Flare", "Netrunner Relay Leak"),
                scannedResources = listOf("Neon Tritium", "Cobalt Slag")
            ),
            SectorState(
                id = "temple_core",
                name = "Enlightener Temple Core",
                classification = "Cosmic Sanctuary",
                description = "An ancient cosmic sanctuary built inside a dense gas cloud. The Enlighteners study gravitational relics here. Hyperlane pathways are heavily obscured by spiritual static.",
                coordX = 280f,
                coordY = 50f,
                hazardLevel = "LOW",
                stability = 95,
                cartographyProgress = 50,
                isFullyCartographed = false,
                alignment = "Enlightener",
                temperatureKelvin = 150,
                gravityGs = 0.12,
                targetFrequency = 0.28f,
                anomalySignatures = listOf("Gravity Well Distortion", "Aether Resonance Aura"),
                scannedResources = listOf("Void Crystals", "Nebula Condensate")
            ),
            SectorState(
                id = "crevice",
                name = "Uncharted Spatial Crevice",
                classification = "Gravitational Anomaly",
                description = "A torn rift in space-time filled with extreme grav-eddies. Unstable, volatile, and rich in exotic sub-atomic elements. High risk of sensor blackout.",
                coordX = 300f,
                coordY = 150f,
                hazardLevel = "CRITICAL",
                stability = 22,
                cartographyProgress = 10,
                isFullyCartographed = false,
                alignment = "Uncharted",
                temperatureKelvin = 42,
                gravityGs = 4.8,
                targetFrequency = 0.15f,
                anomalySignatures = listOf("Singularity Pulse", "Chronal Shift Leak"),
                scannedResources = listOf("Dark Matter Crystals", "Neutron Dust")
            )
        )
    }

    val selectedSector = sectorsList.find { it.id == state.selectedSectorId } ?: sectorsList.first()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // A. RADAR INTERACTIVE MAP CARD
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .border(1.dp, CyberBorder, CutCornerShape(8.dp)),
                colors = CardDefaults.cardColors(containerColor = CyberObsidian),
                shape = CutCornerShape(8.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Drawing dynamic radar rings, grid lines, and sweep sweep line
                    val infiniteTransition = rememberInfiniteTransition(label = "radar")
                    val angleSweep by infiniteTransition.animateFloat(
                        initialValue = 0f,
                        targetValue = 360f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(5000, easing = LinearEasing)
                        ),
                        label = "sweep"
                    )

                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val center = Offset(size.width / 2f, size.height / 2f)

                        // Draw radar circular grid rings
                        drawCircle(color = CyberBorder.copy(alpha = 0.15f), radius = size.minDimension / 6f, style = Stroke(width = 1f))
                        drawCircle(color = CyberBorder.copy(alpha = 0.25f), radius = size.minDimension / 3f, style = Stroke(width = 1.5f))
                        drawCircle(color = CyberBorder.copy(alpha = 0.35f), radius = size.minDimension / 2f, style = Stroke(width = 2f))
                        drawCircle(color = CyberBorder.copy(alpha = 0.10f), radius = size.minDimension / 1.5f, style = Stroke(width = 1f))

                        // Draw horizontal & vertical radar coordinate axes
                        drawLine(color = CyberBorder.copy(alpha = 0.2f), start = Offset(0f, center.y), end = Offset(size.width, center.y), strokeWidth = 1f)
                        drawLine(color = CyberBorder.copy(alpha = 0.2f), start = Offset(center.x, 0f), end = Offset(center.x, size.height), strokeWidth = 1f)

                        // Draw dynamic scanning sweep line
                        val radians = Math.toRadians(angleSweep.toDouble())
                        val endX = center.x + (size.width / 1.4f) * Math.cos(radians).toFloat()
                        val endY = center.y + (size.width / 1.4f) * Math.sin(radians).toFloat()
                        drawLine(
                            color = CyberCyan.copy(alpha = 0.5f),
                            start = center,
                            end = Offset(endX, endY),
                            strokeWidth = 2.5f
                        )

                        // Draw connection hyperlanes (Subspace Cartography networks)
                        sectorsList.forEach { s ->
                            if (s.id != "home_port") {
                                val startPx = Offset(with(density) { 180.dp.toPx() }, with(density) { 110.dp.toPx() })
                                val endPx = Offset(with(density) { s.coordX.dp.toPx() }, with(density) { s.coordY.dp.toPx() })
                                drawLine(
                                    color = if (s.isFullyCartographed) CyberCyan.copy(alpha = 0.6f) 
                                            else if (s.cartographyProgress > 0) CyberAmber.copy(alpha = 0.4f) 
                                            else Color.Gray.copy(alpha = 0.2f),
                                    start = startPx,
                                    end = endPx,
                                    strokeWidth = 3f,
                                    pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(12f, 10f), 0f)
                                )
                            }
                        }
                    }

                    // Radar Sector nodes overlay
                    sectorsList.forEach { sector ->
                        // Pulsing star node animation
                        val pulseTransition = rememberInfiniteTransition(label = sector.id)
                        val pulseScale by pulseTransition.animateFloat(
                            initialValue = 0.9f,
                            targetValue = 1.3f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(1200 + sector.id.hashCode() % 600, easing = EaseInOutSine),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "pulse"
                        )

                        val nodeColor = when (sector.alignment) {
                            "Technopunk" -> CyberMagenta
                            "Enlightener" -> CyberAmber
                            "Neutral" -> CyberCyan
                            else -> Color.Gray
                        }

                        val isSelected = sector.id == selectedSector.id

                        Box(
                            modifier = Modifier
                                .offset(sector.coordX.dp - 10.dp, sector.coordY.dp - 10.dp)
                                .size(20.dp)
                                .clickable { viewModel.selectSector(sector.id) },
                            contentAlignment = Alignment.Center
                        ) {
                            // Pulsing glowing ring
                            Box(
                                modifier = Modifier
                                    .size(16.dp * pulseScale)
                                    .clip(RoundedCornerShape(50))
                                    .background(
                                        if (isSelected) CyberAmber.copy(alpha = 0.35f) else nodeColor.copy(alpha = 0.2f)
                                    )
                                    .border(
                                        1.dp, 
                                        if (isSelected) CyberAmber else nodeColor.copy(alpha = 0.5f), 
                                        RoundedCornerShape(50)
                                    )
                            )

                            // Core solid node dot
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(if (isSelected) CyberAmber else nodeColor)
                            )
                        }

                        // Label overlay
                        Text(
                            text = if (sector.isFullyCartographed) "${sector.name} [SECURED]" else sector.name,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = if (isSelected) CyberAmber else Color.White.copy(alpha = 0.8f),
                                fontSize = 8.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            ),
                            modifier = Modifier
                                .offset(sector.coordX.dp + 12.dp, sector.coordY.dp - 5.dp)
                                .background(CyberObsidian.copy(alpha = 0.6f), RoundedCornerShape(2.dp))
                                .padding(horizontal = 2.dp)
                        )
                    }
                }
            }
        }

        // B. DETAILED SECTOR SPEC SHEET & CARTOGRAPHY INTERFACE
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CyberBorder, RoundedCornerShape(8.dp)),
                colors = CardDefaults.cardColors(containerColor = CyberSteel),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Header row: Title + alignment tag
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = selectedSector.name.uppercase(),
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = CyberAmber,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            )
                            Text(
                                text = "CLASSIFICATION: ${selectedSector.classification.uppercase()}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = CyberCyanDim,
                                    fontFamily = FontFamily.Monospace
                                )
                            )
                        }

                        // Alignment tag
                        val alignColor = when (selectedSector.alignment) {
                            "Technopunk" -> CyberMagenta
                            "Enlightener" -> CyberAmber
                            "Neutral" -> CyberCyan
                            else -> Color.Gray
                        }
                        Box(
                            modifier = Modifier
                                .border(1.dp, alignColor.copy(alpha = 0.6f), CutCornerShape(4.dp))
                                .background(alignColor.copy(alpha = 0.1f), CutCornerShape(4.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = selectedSector.alignment.uppercase(),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = alignColor,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 9.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Bio description
                    Text(
                        text = selectedSector.description,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.LightGray,
                            fontFamily = FontFamily.SansSerif,
                            lineHeight = 15.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Telemetry Grid (2x2 spec indicators)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Col 1: Hazard and Temperature
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            // Hazard
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = "Hazard Level",
                                    tint = if (selectedSector.hazardLevel == "CRITICAL" || selectedSector.hazardLevel == "EXTREME") Color.Red else CyberAmber,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "HAZARD: ${selectedSector.hazardLevel}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color.White,
                                        fontFamily = FontFamily.Monospace
                                    )
                                )
                            }
                            // Thermal
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.WbSunny,
                                    contentDescription = "Temperature",
                                    tint = CyberCyan,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "THERMAL: ${selectedSector.temperatureKelvin} K",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color.White,
                                        fontFamily = FontFamily.Monospace
                                    )
                                )
                            }
                        }

                        // Col 2: Gravity and Status
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            // Gravity
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Speed,
                                    contentDescription = "Gravity",
                                    tint = CyberCyan,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "GRAVITY: ${selectedSector.gravityGs} G",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color.White,
                                        fontFamily = FontFamily.Monospace
                                    )
                                )
                            }
                            // Stability
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Grid3x3,
                                    contentDescription = "Integrity",
                                    tint = MatrixGreen,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "STABILITY: ${selectedSector.stability}%",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color.White,
                                        fontFamily = FontFamily.Monospace
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Dynamic Subspace Cartography Progress Bar
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CyberObsidian.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                            .border(1.dp, CyberBorder.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "SUBSPACE CARTOGRAPHY RECORD",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = CyberCyanDim,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            )
                            Text(
                                text = "${selectedSector.cartographyProgress}%",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (selectedSector.isFullyCartographed) MatrixGreen else CyberAmber,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Progress track
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.Gray.copy(alpha = 0.2f))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(selectedSector.cartographyProgress / 100f)
                                    .background(
                                        if (selectedSector.isFullyCartographed) Brush.linearGradient(listOf(MatrixGreen, MatrixGreen))
                                        else Brush.horizontalGradient(listOf(CyberCyan, CyberAmber))
                                    )
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (selectedSector.isFullyCartographed) 
                                "WARP ROUTE ONLINE // HYPERLANE STABILIZED" 
                                else "UNMAPPED SECTOR // DISPATCH SCANNER PROBES OR CALIBRATE SENSORS TO SECURE HYPERLANE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (selectedSector.isFullyCartographed) MatrixGreen else Color.Gray,
                                fontSize = 8.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // C. SUBSPACE LORE TELEMETRIES (Resources & Anomalies)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .border(0.5.dp, CyberBorder.copy(alpha = 0.3f), RoundedCornerShape(4.dp)),
                            colors = CardDefaults.cardColors(containerColor = CyberObsidian.copy(alpha = 0.2f))
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(
                                    text = "ANOMALOUS SIGNATURES",
                                    fontSize = 8.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = CyberMagenta,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                selectedSector.anomalySignatures.forEach { sig ->
                                    Text(
                                        text = "• $sig",
                                        fontSize = 9.sp,
                                        fontFamily = FontFamily.Monospace,
                                        color = Color.LightGray
                                    )
                                }
                            }
                        }

                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .border(0.5.dp, CyberBorder.copy(alpha = 0.3f), RoundedCornerShape(4.dp)),
                            colors = CardDefaults.cardColors(containerColor = CyberObsidian.copy(alpha = 0.2f))
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(
                                    text = "RESOURCE DENSE MATTERS",
                                    fontSize = 8.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = CyberCyan,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                selectedSector.scannedResources.forEach { res ->
                                    Text(
                                        text = "⚡ $res",
                                        fontSize = 9.sp,
                                        fontFamily = FontFamily.Monospace,
                                        color = Color.LightGray
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // D. CARTOGRAPHY CONTROLS (Tuning Minigame & Probe Launching)
                    if (state.scanInProcess) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(
                                color = CyberCyan,
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "TRANSMITTING PROBE TELEMETRIES // SCANNING MULTI-DIMENSIONS",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = CyberCyan,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            // Probe dispatch (costs 50 credits)
                            Button(
                                onClick = { viewModel.dispatchRadarProbe(selectedSector.id) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, CyberCyan, CutCornerShape(4.dp)),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CyberCyan.copy(alpha = 0.08f),
                                    contentColor = CyberCyan
                                ),
                                shape = CutCornerShape(4.dp),
                                enabled = !selectedSector.isFullyCartographed
                            ) {
                                Icon(Icons.Default.Explore, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (selectedSector.isFullyCartographed) "HYPERLANE SECURED" else "LAUNCH TACTICAL SCAN PROBE (-50 ⚿)",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            // Minigame Divider
                            if (!selectedSector.isFullyCartographed) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                        .background(CyberBorder.copy(alpha = 0.3f))
                                )

                                Text(
                                    text = "QUANTUM RESONANCE TUNING DECK",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = CyberAmber,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    ),
                                    modifier = Modifier.padding(vertical = 2.dp)
                                )

                                Text(
                                    text = "Calibrate the quantum frequency spectrum to align with local spatial resonance for an efficient cartography sweep.",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color.Gray,
                                        fontFamily = FontFamily.SansSerif,
                                        fontSize = 10.sp
                                    )
                                )

                                // Current and Target values
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "CALIBRATED: ${String.format(java.util.Locale.US, "%.0f", state.frequencyValue * 100)} MHz",
                                        fontSize = 11.sp,
                                        fontFamily = FontFamily.Monospace,
                                        color = CyberCyan,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "TARGET OPTIMAL: ${String.format(java.util.Locale.US, "%.0f", selectedSector.targetFrequency * 100)} MHz",
                                        fontSize = 11.sp,
                                        fontFamily = FontFamily.Monospace,
                                        color = CyberAmber,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                // Interactive Wavelength Slider
                                Slider(
                                    value = state.frequencyValue,
                                    onValueChange = { viewModel.calibrateQuantumSensor(it) },
                                    colors = SliderDefaults.colors(
                                        thumbColor = CyberCyan,
                                        activeTrackColor = CyberCyan.copy(alpha = 0.7f),
                                        inactiveTrackColor = Color.Gray.copy(alpha = 0.3f)
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )

                                // Trigger scan
                                Button(
                                    onClick = { viewModel.scanWithCalibratedFrequency() },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(1.dp, CyberAmber, CutCornerShape(4.dp)),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = CyberAmber.copy(alpha = 0.08f),
                                        contentColor = CyberAmber
                                    ),
                                    shape = CutCornerShape(4.dp)
                                ) {
                                    Icon(Icons.Default.Wifi, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "SWEEP ALIGNED FREQUENCY (-30 ⚿)",
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    // E. SCAN READOUTS & FEEDBACK PANEL
                    val feedbackText = state.scanResult ?: state.cartographyMinigameMessage
                    if (feedbackText != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(CyberCyan.copy(alpha = 0.05f), RoundedCornerShape(4.dp))
                                .border(1.dp, CyberCyan.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                                .padding(10.dp)
                        ) {
                            Column {
                                Text(
                                    text = "LILI FEEDBACK INCOMING // TELEMETRY ANALYSIS",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = CyberCyanDim,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = feedbackText,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color.White,
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 11.sp,
                                        lineHeight = 14.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- 8. AI TERMINAL PANEL (COGNITIVE MODELING LILA WITH HIGH THINKING) ---
@Composable
fun AiTerminalPanel(state: GameUiState, viewModel: GameViewModel) {
    var userMessageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // Scroll chat to end when loaded or updated
    LaunchedEffect(state.aiChatHistory.size, state.isAiLoading) {
        if (state.aiChatHistory.isNotEmpty()) {
            listState.animateScrollToItem(state.aiChatHistory.size - 1)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Holographic AI Avatar Head details
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .border(1.dp, CyberCyan.copy(alpha = 0.5f), CutCornerShape(8.dp)),
            colors = CardDefaults.cardColors(containerColor = CyberObsidian),
            shape = CutCornerShape(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar Hologram Image
                Image(
                    painter = painterResource(id = R.drawable.img_ai_avatar),
                    contentDescription = "AI Lila Avatar",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .border(1.5.dp, CyberCyan, RoundedCornerShape(30.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "L.I.L.A. V4.2 [HYPERTHINK CORE]",
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = CyberCyan,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    )
                    Text(
                        text = "Resonance: 92% // Cognitive Depth: HIGH",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MatrixGreen,
                            fontFamily = FontFamily.Monospace
                        )
                    )
                }
            }
        }

        // DAILY QUANTUM RESONANCE STATS
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .border(1.dp, CyberBorder, RoundedCornerShape(4.dp)),
            colors = CardDefaults.cardColors(containerColor = CyberSteel.copy(alpha = 0.4f)),
            shape = RoundedCornerShape(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                ResonanceStat("FIELD STABILITY", "84%", CyberCyan)
                ResonanceStat("HARMONY", "EXCELLENT", MatrixGreen)
                ResonanceStat("TEMPORAL ECHOES", "LOW", CyberMagenta)
            }
        }

        // CHAT MESSAGE HISTORY
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(CyberSteel.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                .border(1.dp, CyberBorder, RoundedCornerShape(8.dp))
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.aiChatHistory) { msg ->
                val isCaptain = msg.sender == "Captain"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isCaptain) Arrangement.End else Arrangement.Start
                ) {
                    Card(
                        modifier = Modifier
                            .widthIn(max = 240.dp)
                            .border(
                                1.dp,
                                if (isCaptain) CyberAmber.copy(alpha = 0.5f) else CyberCyan.copy(alpha = 0.5f),
                                RoundedCornerShape(8.dp)
                            ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isCaptain) CyberPanel else CyberSteel
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = msg.sender.uppercase(),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (isCaptain) CyberAmber else CyberCyan,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 8.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = msg.text,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color.White,
                                    fontFamily = if (isCaptain) FontFamily.SansSerif else FontFamily.Monospace
                                )
                            )
                        }
                    }
                }
            }

            // Thinking processing indicator
            if (state.isAiLoading) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            color = CyberCyan,
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Core computing neural lanes (HIGH)...",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = CyberCyanDim,
                                fontFamily = FontFamily.Monospace
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // CHAT ENTRY ROW
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = userMessageText,
                onValueChange = { userMessageText = it },
                modifier = Modifier
                    .weight(1f)
                    .testTag("ai_chat_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.LightGray,
                    focusedBorderColor = CyberCyan,
                    unfocusedBorderColor = CyberBorder
                ),
                placeholder = {
                    Text(
                        text = "Transmit instructions to AI Core...",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    )
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = {
                    if (userMessageText.isNotBlank()) {
                        viewModel.sendChatMessage(userMessageText)
                        userMessageText = ""
                    }
                },
                modifier = Modifier
                    .size(48.dp)
                    .clip(CutCornerShape(4.dp))
                    .background(CyberCyan.copy(alpha = 0.15f))
                    .border(1.dp, CyberCyan, CutCornerShape(4.dp))
                    .testTag("send_ai_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = CyberCyan
                )
            }
        }
    }
}

@Composable
fun ResonanceStat(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = Color.Gray,
                fontSize = 8.sp,
                fontFamily = FontFamily.Monospace
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(
                color = color,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        )
    }
}

// --- 9. COFFEE CORNER BREWING PANEL ---
@Composable
fun CoffeeCornerPanel(state: GameUiState, viewModel: GameViewModel) {
    val coffees = listOf(
        Triple("COSMIC ESPRESSO", 50, "Overclocked Core (Energy +20%)"),
        Triple("NEBULA LATTE", 75, "Astral Focus (Intellect +15%)"),
        Triple("QUANTUM FLAT WHITE", 100, "Harmonic Resonance (Luck +30%)")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .border(1.dp, CyberBorder, CutCornerShape(8.dp)),
            colors = CardDefaults.cardColors(containerColor = CyberSteel.copy(alpha = 0.3f)),
            shape = CutCornerShape(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "REPLICATOR AUTOMATION PROTOCOL",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = CyberMagenta,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Synthesizing beverages triggers molecular alignment, granting brief neural buffs and altering companion factions resonance.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.LightGray,
                        fontFamily = FontFamily.SansSerif
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "SELECT REPLICATION FORMULA",
            style = MaterialTheme.typography.labelSmall.copy(
                color = CyberCyanDim,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            coffees.forEach { (name, cost, buff) ->
                val canAfford = state.credits >= cost
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, CyberBorder, RoundedCornerShape(8.dp)),
                    colors = CardDefaults.cardColors(containerColor = CyberSteel),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = name,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            )

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.GeneratingTokens,
                                    contentDescription = "Cost",
                                    tint = CyberAmber,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "$cost ⚿",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = CyberAmber,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Effect: $buff",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = CyberCyan,
                                fontFamily = FontFamily.Monospace
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                // Assign faction delta changes beautifully!
                                when (name) {
                                    "COSMIC ESPRESSO" -> viewModel.brewCoffee(name, cost, buff, 0, 8)
                                    "NEBULA LATTE" -> viewModel.brewCoffee(name, cost, buff, 8, 0)
                                    else -> viewModel.brewCoffee(name, cost, buff, 4, 4)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (canAfford) CyberMagenta.copy(alpha = 0.2f) else Color.DarkGray,
                                contentColor = if (canAfford) CyberMagenta else Color.LightGray
                            ),
                            enabled = canAfford
                        ) {
                            Text(
                                text = if (canAfford) "INITIATE MOLECULAR FUSION" else "CREDITS DEFICIENT",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

// --- 10. BOOKSHELF CODEX PANEL ---
@Composable
fun BookshelfPanel(state: GameUiState, viewModel: GameViewModel) {
    var selectedCategory by remember { mutableStateOf(0) } // 0 = Factions, 1 = Systems, 2 = anomalies

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CategoryTab("FACTIONS", selectedCategory == 0) { selectedCategory = 0 }
            CategoryTab("SYSTEMS", selectedCategory == 1) { selectedCategory = 1 }
            CategoryTab("ANOMALIES", selectedCategory == 2) { selectedCategory = 2 }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            when (selectedCategory) {
                0 -> Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    CodexEntry(
                        title = "The Enlighteners",
                        desc = "Philosophical cosmic mystics focused on the harmony of quantum resonance. They study ancient anomalies to find transcendence and believe the universe possesses consciousness.",
                        rep = "Resonance Morale: ${state.reputationEnlighteners}%"
                    )
                    CodexEntry(
                        title = "The Technopunks",
                        desc = "Rebellious biomechanical hackers and master engineers navigating star systems. They believe destiny lies in extreme technological evolution and cyborg customization.",
                        rep = "Resonance Morale: ${state.reputationTechnopunks}%"
                    )
                }
                1 -> Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    CodexEntry(
                        title = "Aetherius-VII Core Core Systems",
                        desc = "Equipped with a Class-IV Singularity Reactor and direct neural routing to L.I.L.A. The starship acts as a localized gravitational stabilizer in deep quantum void tunnels.",
                        rep = "Reactor Status: NOMINAL"
                    )
                    CodexEntry(
                        title = "Replication Bay Protocol",
                        desc = "A molecular synthesizer utilizing raw dark-matter plasma to form items. Safe for culinary replications like coffee and simple field nourishment.",
                        rep = "System Status: ON-LINE"
                    )
                }
                2 -> Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    CodexEntry(
                        title = "Quantum Storm",
                        desc = "Localized disruptions in dark space where quantum states fluctuate violently. Deflector shields must be polarized to avoid temporal feedback in bio-implants.",
                        rep = "Danger Index: HIGH"
                    )
                    CodexEntry(
                        title = "Stellar lanes (Clear)",
                        desc = "Calm inter-dimensional corridors optimized for FTL travel. Highly pleasant view from the observation window, optimal for cognitive relaxation.",
                        rep = "Danger Index: SAFE"
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryTab(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clickable(onClick = onClick)
            .border(
                1.dp,
                if (isSelected) CyberCyan else CyberBorder,
                CutCornerShape(4.dp)
            )
            .background(if (isSelected) CyberCyan.copy(alpha = 0.15f) else CyberSteel)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = if (isSelected) CyberCyan else Color.White,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        )
    }
}

@Composable
fun CodexEntry(title: String, desc: String, rep: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CyberBorder, RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(containerColor = CyberSteel),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = CyberCyan,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = desc,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.LightGray,
                    fontFamily = FontFamily.SansSerif
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = rep,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = CyberAmber,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

// --- 11. WEATHER GRAPHICS OVERLAYS (CANVAS EFFECTS) ---
@Composable
fun WeatherOverlayEffect(weather: String) {
    val infiniteTransition = rememberInfiniteTransition(label = "weather")

    when (weather) {
        "Quantum Storm" -> {
            val progress by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2500, easing = LinearEasing)
                ),
                label = "storm"
            )

            Canvas(modifier = Modifier.fillMaxSize()) {
                val rand = java.util.Random(99)
                for (i in 0..15) {
                    val x = rand.nextFloat() * size.width
                    val y = (rand.nextFloat() * size.height + progress * size.height) % size.height
                    val length = rand.nextFloat() * 80f + 20f
                    val angle = Math.toRadians(45.0 + rand.nextFloat() * 10f)
                    val endX = x + length * Math.cos(angle).toFloat()
                    val endY = y + length * Math.sin(angle).toFloat()

                    drawLine(
                        color = CyberMagenta.copy(alpha = 0.4f),
                        start = Offset(x, y),
                        end = Offset(endX, endY),
                        strokeWidth = 3f
                    )
                }
            }
        }
        "Ice Comet Shower" -> {
            val progress by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(4000, easing = LinearEasing)
                ),
                label = "comets"
            )

            Canvas(modifier = Modifier.fillMaxSize()) {
                val rand = java.util.Random(111)
                for (i in 0..8) {
                    val x = (rand.nextFloat() * size.width - progress * size.width)
                    val y = (rand.nextFloat() * size.height + progress * size.height) % size.height
                    val fixedX = if (x < 0) x + size.width else x

                    drawCircle(
                        color = CyberCyan.copy(alpha = 0.7f),
                        radius = 4f,
                        center = Offset(fixedX, y)
                    )

                    // tail
                    drawLine(
                        color = CyberCyan.copy(alpha = 0.15f),
                        start = Offset(fixedX, y),
                        end = Offset(fixedX + 40f, y - 40f),
                        strokeWidth = 2f
                    )
                }
            }
        }
        "Dense Nebula" -> {
            val pulse by infiniteTransition.animateFloat(
                initialValue = 0.3f,
                targetValue = 0.6f,
                animationSpec = infiniteRepeatable(
                    animation = tween(5000, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "nebula"
            )

            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            CyberCyanDim.copy(alpha = pulse),
                            CyberObsidian.copy(alpha = 0f)
                        ),
                        center = Offset(size.width * 0.7f, size.height * 0.3f),
                        radius = size.width * 0.8f
                    ),
                    radius = size.width * 0.8f,
                    center = Offset(size.width * 0.7f, size.height * 0.3f)
                )

                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            CyberMagenta.copy(alpha = pulse * 0.6f),
                            CyberObsidian.copy(alpha = 0f)
                        ),
                        center = Offset(size.width * 0.2f, size.height * 0.8f),
                        radius = size.width * 0.6f
                    ),
                    radius = size.width * 0.6f,
                    center = Offset(size.width * 0.2f, size.height * 0.8f)
                )
            }
        }
        "Electromagnetic Interference" -> {
            val trigger by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(200, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "interference"
            )

            Canvas(modifier = Modifier.fillMaxSize()) {
                if (trigger > 0.82f) {
                    val y = trigger * size.height
                    drawLine(
                        color = CyberCyan.copy(alpha = 0.25f),
                        start = Offset(0f, y),
                        end = Offset(size.width, y + Random.nextInt(-20, 20)),
                        strokeWidth = 2f
                    )
                    drawLine(
                        color = CyberAmber.copy(alpha = 0.2f),
                        start = Offset(0f, y - 50f),
                        end = Offset(size.width, y - 50f + Random.nextInt(-10, 10)),
                        strokeWidth = 1f
                    )
                }
            }
        }
        else -> {
            // Clear space - twinkling dots
        }
    }
}

fun getWeatherColor(weather: String): Color = when (weather) {
    "Quantum Storm" -> CyberMagenta
    "Ice Comet Shower" -> CyberCyan
    "Dense Nebula" -> CyberCyanDim
    "Electromagnetic Interference" -> CyberAmber
    else -> Color.Transparent
}

fun parseHexColor(hex: String): Color {
    return when (hex.lowercase()) {
        "c084fc" -> Color(0xFFC084FC) // Purple / Lyra
        "e91e63" -> Color(0xFFE91E63) // Pink / Nova
        "00ff66" -> Color(0xFF00FF66) // Green / Elara
        "00f0ff" -> Color(0xFF00F0FF) // Cyan / Quark
        else -> CyberCyan
    }
}

fun getAffinityTierName(affinity: Int): String = when (affinity) {
    0 -> "Stranger"
    1 -> "Acquaintance"
    2 -> "Ally"
    3 -> "Trusted"
    4 -> "Close"
    5 -> "Soulbound"
    else -> "Unknown"
}

// --- 8. COMPANION QUARTERS PANEL ---
@Composable
fun CompanionQuartersPanel(state: GameUiState, viewModel: GameViewModel) {
    var activeTab by remember { mutableStateOf(0) } // 0 = Crew Registry, 1 = Pet Sanctuary, 2 = Living Ship Ecosystem

    Column(modifier = Modifier.fillMaxSize()) {
        // TAB SELECTOR
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Button(
                onClick = { activeTab = 0 },
                modifier = Modifier
                    .weight(1f)
                    .border(
                        1.dp,
                        if (activeTab == 0) Color(0xFFC084FC) else CyberBorder,
                        CutCornerShape(4.dp)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (activeTab == 0) Color(0xFFC084FC).copy(alpha = 0.15f) else CyberSteel,
                    contentColor = if (activeTab == 0) Color(0xFFC084FC) else Color.White
                ),
                shape = CutCornerShape(4.dp),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.People,
                    contentDescription = "Crew",
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "CREW",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                )
            }

            Button(
                onClick = { activeTab = 1 },
                modifier = Modifier
                    .weight(1.5f)
                    .border(
                        1.dp,
                        if (activeTab == 1) CyberCyan else CyberBorder,
                        CutCornerShape(4.dp)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (activeTab == 1) CyberCyan.copy(alpha = 0.15f) else CyberSteel,
                    contentColor = if (activeTab == 1) CyberCyan else Color.White
                ),
                shape = CutCornerShape(4.dp),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Pets,
                    contentDescription = "Pets",
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "PET SANCTUARY",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                )
            }

            Button(
                onClick = { activeTab = 2 },
                modifier = Modifier
                    .weight(1.5f)
                    .border(
                        1.dp,
                        if (activeTab == 2) Color(0xFF10B981) else CyberBorder,
                        CutCornerShape(4.dp)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (activeTab == 2) Color(0xFF10B981).copy(alpha = 0.15f) else CyberSteel,
                    contentColor = if (activeTab == 2) Color(0xFF10B981) else Color.White
                ),
                shape = CutCornerShape(4.dp),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Spa,
                    contentDescription = "Ecosystem",
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "LIVING SHIP",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                )
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            when (activeTab) {
                0 -> CrewRegistryTab(state, viewModel)
                1 -> PetSanctuaryTab(state, viewModel)
                2 -> LivingShipEcosystemTab(state, viewModel)
            }
        }
    }
}

@Composable
fun CrewRegistryTab(state: GameUiState, viewModel: GameViewModel) {
    val activeCompanionId = state.activeCompanionChatId

    if (activeCompanionId != null) {
        val companion = state.companions.find { it.id == activeCompanionId }
        if (companion != null) {
            CompanionDetailScreen(companion = companion, state = state, viewModel = viewModel)
        } else {
            viewModel.selectCompanionChat(null)
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(state.companions) { companion ->
                val compColor = parseHexColor(companion.colorHex)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, compColor.copy(alpha = 0.5f), CutCornerShape(8.dp)),
                    colors = CardDefaults.cardColors(containerColor = CyberPanel.copy(alpha = 0.9f)),
                    shape = CutCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = companion.name.uppercase(),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = compColor,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    letterSpacing = 1.sp
                                )
                            )
                            
                            Box(
                                modifier = Modifier
                                    .background(compColor.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                                    .border(1.dp, compColor.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = companion.statusText.uppercase(),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = compColor,
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "${companion.role} // Weapon: ${companion.weapon}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.Gray,
                                fontFamily = FontFamily.Monospace
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = companion.bio,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.LightGray,
                                lineHeight = 14.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        HorizontalDivider(color = CyberBorder, thickness = 1.dp)

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "BOND: ",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = CyberCyanDim,
                                        fontFamily = FontFamily.Monospace
                                    )
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                RepeatHearts(count = companion.affinity, color = compColor)
                            }

                            Button(
                                onClick = { viewModel.selectCompanionChat(companion.id) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = compColor.copy(alpha = 0.15f),
                                    contentColor = compColor
                                ),
                                shape = CutCornerShape(4.dp),
                                modifier = Modifier.height(32.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ChatBubble,
                                    contentDescription = "Link",
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "SECURE LINK",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CompanionDetailScreen(companion: CompanionState, state: GameUiState, viewModel: GameViewModel) {
    val compColor = parseHexColor(companion.colorHex)
    val chatState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // Auto-scroll chat to the bottom when history changes
    LaunchedEffect(companion.chatHistory.size) {
        if (companion.chatHistory.isNotEmpty()) {
            chatState.animateScrollToItem(companion.chatHistory.size - 1)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.selectCompanionChat(null) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.LightGray
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Column {
                Text(
                    text = "${companion.name.uppercase()} // ACTIVE CONNECTION",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = compColor,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                )
                Text(
                    text = "${getAffinityTierName(companion.affinity).uppercase()} // AFFINITY LEVEL ${companion.affinity}/5",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color.Gray,
                        fontFamily = FontFamily.Monospace
                    )
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(CyberSteel.copy(alpha = 0.3f), CutCornerShape(8.dp))
                .border(1.dp, CyberBorder, CutCornerShape(8.dp))
                .padding(10.dp),
            state = chatState,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Bio/Summary card inside chat
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = CyberPanel.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = companion.bio,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.LightGray,
                                fontFamily = FontFamily.SansSerif
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "EQUIPPED: ${companion.weapon}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = compColor,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 9.sp
                            )
                        )
                    }
                }
            }

            // Real responsive chat messages
            items(companion.chatHistory) { msg ->
                val isCaptain = msg.sender == state.captainName
                val alignment = if (isCaptain) Alignment.End else Alignment.Start
                val bubbleColor = if (isCaptain) CyberCyan.copy(alpha = 0.1f) else compColor.copy(alpha = 0.1f)
                val borderCol = if (isCaptain) CyberCyan.copy(alpha = 0.3f) else compColor.copy(alpha = 0.3f)
                val textCol = if (isCaptain) Color.White else Color.White

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = alignment
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .background(bubbleColor, RoundedCornerShape(8.dp))
                            .border(1.dp, borderCol, RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Column {
                            Text(
                                text = msg.sender.uppercase(),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (isCaptain) CyberCyan else compColor,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 8.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = msg.text,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = textCol,
                                    lineHeight = 14.sp
                                )
                            )
                        }
                    }
                }
            }

            // Interactive response choices
            val lastMsg = companion.chatHistory.lastOrNull()
            if (lastMsg != null && lastMsg.responseOptions.isNotEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "ESTABLISH DECISION MATRIX PROTOCOL",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = CyberAmber,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 9.sp
                            )
                        )
                        lastMsg.responseOptions.forEach { option ->
                            Button(
                                onClick = { viewModel.sendCompanionResponse(companion.id, option) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, compColor, CutCornerShape(4.dp)),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = compColor.copy(alpha = 0.08f),
                                    contentColor = compColor
                                ),
                                shape = CutCornerShape(4.dp),
                                contentPadding = PaddingValues(10.dp)
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = option.text,
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    )
                                    if (option.costCredits != 0) {
                                        Text(
                                            text = if (option.costCredits < 0) "AWARD: +${-option.costCredits} CREDITS" else "COST: ${option.costCredits} CREDITS",
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 8.sp,
                                            color = if (option.costCredits < 0) MatrixGreen else CyberMagenta
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Gift Replicator Interface
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, CyberBorder, CutCornerShape(6.dp)),
            colors = CardDefaults.cardColors(containerColor = CyberPanel),
            shape = CutCornerShape(6.dp)
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = "GIFT REPLICATOR CORE // ENHANCE ALIGNMENT",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = CyberAmber,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 8.sp
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Synthesize specific item of interest: ${companion.giftType}",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray, fontSize = 10.sp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { viewModel.replicateCompanionGift(companion.id) },
                    enabled = state.credits >= 150 && companion.affinity < 5,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CyberAmber.copy(alpha = 0.15f),
                        contentColor = CyberAmber
                    ),
                    shape = CutCornerShape(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Redeem,
                        contentDescription = "Gift",
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (companion.affinity >= 5) "ALIGNMENT MAXIMIZED" else "REPLICATE & PRESENT GIFT (-150 ⚿)",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun PetSanctuaryTab(state: GameUiState, viewModel: GameViewModel) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Flashing reaction notification
        if (state.petReactionText != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .border(1.dp, CyberCyan, RoundedCornerShape(8.dp)),
                colors = CardDefaults.cardColors(containerColor = CyberCyan.copy(alpha = 0.1f)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = state.petReactionText,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { viewModel.clearPetReaction() },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Text(
                            text = "X",
                            color = CyberCyan,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = CyberSteel.copy(alpha = 0.2f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "BIOMECHANICAL COZY LINK PET SANCTUARY",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = CyberCyan,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                letterSpacing = 1.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Provide regular hardware diagnostics and thermal updates to keep companion combat drones and auxiliary organisms operating at peak efficiency.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.Gray,
                                lineHeight = 14.sp
                            )
                        )
                    }
                }
            }

            items(state.recruitedPets) { pet ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, CyberBorder, CutCornerShape(8.dp)),
                    colors = CardDefaults.cardColors(containerColor = CyberPanel.copy(alpha = 0.85f)),
                    shape = CutCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = pet.name.uppercase(),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = CyberCyan,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    )
                                )
                                Text(
                                    text = "${pet.role} // LVL ${pet.activeLevel}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color.Gray,
                                        fontFamily = FontFamily.Monospace
                                    )
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .background(CyberCyan.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                                    .border(1.dp, CyberCyan.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "ABILITY: ${pet.ability.uppercase()}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = CyberCyan,
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = pet.description,
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.LightGray)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { viewModel.interactWithPet(pet.id) },
                            enabled = state.credits >= pet.upgradeCost,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CyberCyan.copy(alpha = 0.15f),
                                contentColor = CyberCyan
                            ),
                            shape = CutCornerShape(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Pets,
                                contentDescription = "Upgrade",
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "UPGRADE firmware & FEED TREAT (-${pet.upgradeCost} ⚿)",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RepeatHearts(count: Int, color: Color) {
    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        for (i in 1..5) {
            Icon(
                imageVector = if (i <= count) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Affinity Heart",
                tint = if (i <= count) color else Color.Gray.copy(alpha = 0.4f),
                modifier = Modifier.size(14.dp)
              )
          }
      }
}

// --- 8B. LIVING SHIP SIMULATION TAB ---
@Composable
fun LivingShipEcosystemTab(state: GameUiState, viewModel: GameViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- 1. SYSTEM ECOSYSTEM UPDATE LOGS PANEL ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFF10B981), RoundedCornerShape(8.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF022C22).copy(alpha = 0.8f)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Spa,
                        contentDescription = "Sprout",
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "LIVING CO-GROWTH GRID // ECO-CORE REPORT",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xFF10B981),
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = state.ecosystemMessage ?: "SYSTEM ACTIVE // All biomechanical greenhouses, aquarium water filters, and replication slots are reporting normal life support parameters. Tap REST to advance day-growth ticks.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFFA7F3D0),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                )
            }
        }

        // --- 2. HYDROPONICS BAY SECTION ---
        Text(
            text = "I. GREENHOUSE HYDROPONICS BAY",
            style = MaterialTheme.typography.labelSmall.copy(
                color = Color(0xFF10B981),
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 1.sp
            )
        )

        state.plants.forEach { plant ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CyberBorder, CutCornerShape(6.dp)),
                colors = CardDefaults.cardColors(containerColor = CyberPanel.copy(alpha = 0.85f)),
                shape = CutCornerShape(6.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Growing Canvas stem
                    PlantVisualStem(progress = plant.growthProgress, species = plant.species)

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = plant.name.uppercase(),
                            style = MaterialTheme.typography.titleSmall.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        )
                        Text(
                            text = "${plant.species} // ${plant.description}",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray, fontSize = 10.sp),
                            modifier = Modifier.padding(vertical = 2.dp)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Progress Bars
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "GROWTH: ${plant.growthProgress}%",
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = if (plant.harvestReady) Color(0xFF10B981) else Color.LightGray
                                )
                                LinearProgressIndicator(
                                    progress = { plant.growthProgress / 100f },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(4.dp),
                                    color = Color(0xFF10B981),
                                    trackColor = Color.DarkGray
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "WATER: ${plant.waterLevel}%",
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = if (plant.waterLevel < 35) CyberMagenta else CyberCyan
                                )
                                LinearProgressIndicator(
                                    progress = { plant.waterLevel / 100f },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(4.dp),
                                    color = if (plant.waterLevel < 35) CyberMagenta else CyberCyan,
                                    trackColor = Color.DarkGray
                                )
                            }
                        }
                    }

                    // Water / Harvest Button
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (plant.harvestReady) {
                            Button(
                                onClick = { viewModel.harvestPlant(plant.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF047857), contentColor = Color.White),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text("HARVEST", fontSize = 9.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            val hasAutoWater = state.research.find { it.id == "res_auto_irrigation" }?.isCompleted == true
                            Button(
                                onClick = { viewModel.waterPlant(plant.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF065F46), contentColor = Color.White),
                                enabled = hasAutoWater || state.credits >= 10,
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = if (hasAutoWater) "AUTO ACTIVE" else "WATER (-10⚿)",
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- 3. QUANTUM BIO-AQUARIUM SECTION ---
        Text(
            text = "II. QUANTUM BIO-AQUARIUM TANK",
            style = MaterialTheme.typography.labelSmall.copy(
                color = CyberCyan,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 1.sp
            )
        )

        // Simulated aquarium rendering
        AquariumSimulationTank(fishList = state.fish)

        // Aquarium control card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, CyberBorder, CutCornerShape(6.dp)),
            colors = CardDefaults.cardColors(containerColor = CyberPanel),
            shape = CutCornerShape(6.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "AQUARIUM MANAGEMENT MODULE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = CyberCyanDim,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { 
                            // Feed all fish
                            state.fish.firstOrNull()?.let { viewModel.feedFish(it.id) }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan.copy(alpha = 0.15f), contentColor = CyberCyan),
                        enabled = state.credits >= 15 && state.fish.any { !it.isFedToday },
                        shape = CutCornerShape(4.dp)
                    ) {
                        Text("FEED FISH (-15 ⚿)", fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { 
                            val speciesOptions = listOf("Cyber-Guppy", "Aether-Ray", "Singularity Angler")
                            viewModel.purchaseFishEgg(speciesOptions.random())
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC084FC).copy(alpha = 0.15f), contentColor = Color(0xFFC084FC)),
                        enabled = state.credits >= 100 && state.fish.size < 10,
                        shape = CutCornerShape(4.dp)
                    ) {
                        Text("CLONE EGG (-100 ⚿)", fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "FISH SPECIES REGISTRY:",
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )

                state.fish.forEach { f ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(parseHexColor(f.colorHex), RoundedCornerShape(50))
                            )
                            Text(
                                text = "${f.name} [GEN ${f.generation} ${f.species}]",
                                fontSize = 11.sp,
                                color = Color.White,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        Box(
                            modifier = Modifier
                                .background(
                                    if (f.isFedToday) Color(0xFF065F46) else Color(0xFF991B1B),
                                    RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (f.isFedToday) "WELL-FED" else "HUNGRY",
                                fontSize = 8.sp,
                                color = Color.White,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // --- 4. SUBSPACE RESEARCH LABORATORY SECTION ---
        Text(
            text = "III. SUBSPACE RESEARCH LABORATORY",
            style = MaterialTheme.typography.labelSmall.copy(
                color = CyberAmber,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 1.sp
            )
        )

        state.research.forEach { res ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, if (res.isCompleted) Color(0xFF10B981).copy(alpha = 0.5f) else if (res.isStarted) CyberAmber.copy(alpha = 0.5f) else CyberBorder, CutCornerShape(6.dp)),
                colors = CardDefaults.cardColors(containerColor = CyberPanel.copy(alpha = 0.9f)),
                shape = CutCornerShape(6.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = res.title.uppercase(),
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = if (res.isCompleted) Color(0xFF10B981) else if (res.isStarted) CyberAmber else Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            )
                            Text(
                                text = res.description,
                                style = MaterialTheme.typography.bodySmall.copy(color = Color.LightGray, fontSize = 10.sp),
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    if (res.isCompleted) {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFF065F46), RoundedCornerShape(4.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "🔬 RESEARCH COMPLETED // REWARD ACTIVE: ${res.rewardDescription}",
                                fontSize = 10.sp,
                                color = Color(0xFFA7F3D0),
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f).padding(end = 16.dp)) {
                                Text(
                                    text = "PROGRESS: ${res.progress}% (DAYS: ${res.daysSpent}/${res.daysRequired})",
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color.Gray
                                )
                                LinearProgressIndicator(
                                    progress = { res.progress / 100f },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(4.dp),
                                    color = CyberAmber,
                                    trackColor = Color.DarkGray
                                )
                            }

                            Button(
                                onClick = { viewModel.startResearch(res.id) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (res.isStarted) Color.DarkGray else CyberAmber.copy(alpha = 0.15f),
                                    contentColor = if (res.isStarted) Color.LightGray else CyberAmber
                                ),
                                enabled = !res.isStarted && state.credits >= res.costCredits,
                                shape = CutCornerShape(4.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = if (res.isStarted) "FUNDED" else "FUND (-${res.costCredits}⚿)",
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- 5. COZY DECORATIVE SLOTS SECTION ---
        Text(
            text = "IV. COZY QUARTERS ORNAMENTS",
            style = MaterialTheme.typography.labelSmall.copy(
                color = Color(0xFFC084FC),
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 1.sp
            )
        )

        state.decorations.forEach { dec ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        if (dec.isPlaced) Color(0xFFC084FC).copy(alpha = 0.6f) else CyberBorder,
                        CutCornerShape(6.dp)
                    ),
                colors = CardDefaults.cardColors(containerColor = CyberPanel),
                shape = CutCornerShape(6.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(if (dec.isPlaced) Color(0xFFC084FC) else Color.Gray, RoundedCornerShape(50))
                            )
                            Text(
                                text = dec.name.uppercase(),
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = if (dec.isPlaced) Color(0xFFC084FC) else Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            )
                        }
                        Text(
                            text = "${dec.placementSlot} slot // ${dec.description}",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.LightGray, fontSize = 10.sp),
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                        Text(
                            text = "AURA BONUS: ${dec.atmosphericBonus}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFFE9D5FF),
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    if (dec.isUnlocked) {
                        Button(
                            onClick = { viewModel.toggleDecoration(dec.id) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (dec.isPlaced) Color(0xFFC084FC).copy(alpha = 0.15f) else Color.DarkGray,
                                contentColor = if (dec.isPlaced) Color(0xFFC084FC) else Color.White
                            ),
                            shape = RoundedCornerShape(4.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (dec.isPlaced) "REMOVE" else "PLACE",
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Button(
                            onClick = { viewModel.purchaseDecoration(dec.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF047857), contentColor = Color.White),
                            enabled = state.credits >= dec.costCredits,
                            shape = RoundedCornerShape(4.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "SYNTH (-${dec.costCredits}⚿)",
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlantVisualStem(progress: Int, species: String) {
    val progressFloat = progress / 100f
    val color = when (species) {
        "Neon Lotus" -> CyberCyan
        "Vivid Fern" -> Color(0xFF10B981)
        "Orchid" -> Color(0xFFC084FC)
        else -> CyberAmber
    }

    Box(
        modifier = Modifier
            .size(50.dp)
            .background(CyberObsidian, RoundedCornerShape(4.dp))
            .border(1.dp, CyberBorder, RoundedCornerShape(4.dp)),
        contentAlignment = Alignment.BottomCenter
    ) {
        Canvas(modifier = Modifier.fillMaxSize().padding(4.dp)) {
            val stemHeight = size.height * progressFloat * 0.8f
            
            // Draw Stem
            drawLine(
                color = Color(0xFF047857),
                start = Offset(size.width / 2, size.height),
                end = Offset(size.width / 2, size.height - stemHeight),
                strokeWidth = 4f
            )

            // Draw Leaves if grown
            if (progress >= 30) {
                drawCircle(
                    color = Color(0xFF059669),
                    radius = 4f,
                    center = Offset(size.width / 2 - 6f, size.height - stemHeight * 0.5f)
                )
                drawCircle(
                    color = Color(0xFF059669),
                    radius = 4f,
                    center = Offset(size.width / 2 + 6f, size.height - stemHeight * 0.7f)
                )
            }

            if (progress >= 100) {
                // Fully grown flower head!
                drawCircle(
                    color = color,
                    radius = 8f,
                    center = Offset(size.width / 2, size.height - stemHeight)
                )
                // Draw glow halo
                drawCircle(
                    color = color.copy(alpha = 0.3f),
                    radius = 14f,
                    center = Offset(size.width / 2, size.height - stemHeight)
                )
            }
        }
    }
}

@Composable
fun AquariumSimulationTank(fishList: List<FishState>) {
    val infiniteTransition = rememberInfiniteTransition(label = "aquarium")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .border(1.dp, Color(0xFF10B981).copy(alpha = 0.5f), CutCornerShape(8.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF022C22)),
        shape = CutCornerShape(8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Draw background grid or water lines
            Canvas(modifier = Modifier.fillMaxSize()) {
                val path = Path()
                path.moveTo(0f, size.height * 0.4f + kotlin.math.sin(Math.toRadians(waveOffset.toDouble())).toFloat() * 10f)
                for (x in 0..size.width.toInt() step 20) {
                    val y = size.height * 0.4f + kotlin.math.sin(Math.toRadians((waveOffset + x).toDouble())).toFloat() * 10f
                    path.lineTo(x.toFloat(), y)
                }
                path.lineTo(size.width, size.height)
                path.lineTo(0f, size.height)
                path.close()
                drawPath(path, color = Color(0xFF0F766E).copy(alpha = 0.15f))
            }

            // Draw each fish
            fishList.forEachIndexed { idx, fish ->
                val scale = if (fish.growthStage == "Juvenile") 0.6f else 1.0f
                val color = parseHexColor(fish.colorHex)
                
                // Calculate floating animation offset unique to each fish index
                val xAnimOffset by infiniteTransition.animateFloat(
                    initialValue = -50f,
                    targetValue = 250f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(3000 + idx * 1000, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "fish_x_$idx"
                )
                val yAnimOffset by infiniteTransition.animateFloat(
                    initialValue = 20f,
                    targetValue = 100f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(2000 + idx * 800, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "fish_y_$idx"
                )

                // Place fish with custom color and tail
                Box(
                    modifier = Modifier
                        .offset(x = (xAnimOffset + (idx * 25) % 150).dp, y = (yAnimOffset + (idx * 15) % 60).dp)
                        .size((16 * scale).dp)
                        .background(color, RoundedCornerShape(50))
                ) {
                    // Small glowing dot for tail
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .offset(x = (-4 * scale).dp)
                            .size((6 * scale).dp)
                            .background(color.copy(alpha = 0.7f), RoundedCornerShape(50))
                    )
                }
            }

            // Overlay tank title
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .background(Color(0xFF064E3B), RoundedCornerShape(4.dp))
                    .border(1.dp, Color(0xFF10B981), RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "AQUARIUM: ${fishList.size} ORGANISMS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color(0xFF10B981),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

// --- NEW DECK ACCESSORIES & OBJECT HOTSPOTS ---

@Composable
fun StarshipElevatorHotspot(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "elevator_glow")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Column(
        modifier = modifier
            .width(110.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                .border(1.5.dp, CyberCyan.copy(alpha = pulse), RoundedCornerShape(4.dp))
                .padding(6.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Vertical lift schematic
                drawLine(
                    color = CyberCyan.copy(alpha = 0.3f),
                    start = Offset(size.width / 2, 0f),
                    end = Offset(size.width / 2, size.height),
                    strokeWidth = 2f
                )
                // Lift cabin dot
                drawCircle(
                    color = CyberCyan,
                    radius = 5f,
                    center = Offset(size.width / 2, size.height * 0.4f)
                )
            }
            Icon(
                imageVector = Icons.Default.DirectionsTransit,
                contentDescription = "Transit Lift",
                tint = CyberCyan,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "TRANSIT LIFT",
            color = Color.White,
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(2.dp))
                .padding(horizontal = 4.dp, vertical = 2.dp)
        )
        Text(
            text = "ALL DECK ACCESS",
            color = CyberCyanDim,
            fontSize = 8.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun GreenhousePodObject(
    title: String,
    plantName: String,
    growth: Int,
    species: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(130.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(width = 110.dp, height = 80.dp)
                .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                .border(2.dp, Color(0xFF10B981), RoundedCornerShape(8.dp))
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Mini plant stem drawing
                PlantVisualStem(progress = growth, species = species)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$growth%",
                    color = Color(0xFF10B981),
                    fontSize = 8.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            color = Color.White,
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(2.dp))
                .padding(horizontal = 4.dp, vertical = 2.dp)
        )
        Text(
            text = plantName.uppercase(),
            color = Color(0xFF10B981).copy(alpha = 0.8f),
            fontSize = 8.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun GreenhouseClimateConsole(
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "climate")
    val scalePulse by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Column(
        modifier = Modifier
            .width(110.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .graphicsLayer(scaleX = scalePulse, scaleY = scalePulse)
                .background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                .border(1.5.dp, Color(0xFF10B981), RoundedCornerShape(6.dp))
                .padding(6.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.SettingsInputComponent,
                    contentDescription = "Climate Settings",
                    tint = Color(0xFF10B981),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "BIOS-LINK",
                    color = Color(0xFF10B981),
                    fontSize = 8.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "BIOS-CONSOLE",
            color = Color.White,
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(2.dp))
                .padding(horizontal = 4.dp, vertical = 2.dp)
        )
        Text(
            text = "CLIMATE & SOIL",
            color = Color.Gray,
            fontSize = 8.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun NutrientFeederConsole(
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(110.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(50))
                .border(1.5.dp, CyberCyan, RoundedCornerShape(50))
                .padding(6.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Waves,
                    contentDescription = "Dispenser",
                    tint = CyberCyan,
                    modifier = Modifier.size(22.dp)
                )
                Text(
                    text = "FEED-BAY",
                    color = CyberCyan,
                    fontSize = 8.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "FEED DISPENSER",
            color = Color.White,
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(2.dp))
                .padding(horizontal = 4.dp, vertical = 2.dp)
        )
        Text(
            text = "NUTRIENT FEEDER",
            color = CyberCyanDim,
            fontSize = 8.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun LoungeJukeboxObject(
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "jukebox")
    val waveHeight by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wave"
    )

    Column(
        modifier = Modifier
            .width(110.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                .border(1.5.dp, CyberMagenta, RoundedCornerShape(8.dp))
                .padding(6.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.height(20.dp)
                ) {
                    Box(modifier = Modifier.width(3.dp).fillMaxHeight(waveHeight * 0.4f).background(CyberMagenta))
                    Box(modifier = Modifier.width(3.dp).fillMaxHeight(waveHeight * 1.0f).background(CyberMagenta))
                    Box(modifier = Modifier.width(3.dp).fillMaxHeight(waveHeight * 0.7f).background(CyberMagenta))
                    Box(modifier = Modifier.width(3.dp).fillMaxHeight(waveHeight * 0.5f).background(CyberMagenta))
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "JUKEBOX",
                    color = CyberMagenta,
                    fontSize = 8.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "AMBIENT SYNTH",
            color = Color.White,
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(2.dp))
                .padding(horizontal = 4.dp, vertical = 2.dp)
        )
        Text(
            text = "SYSTEM SYNTHESIZER",
            color = Color.Gray,
            fontSize = 8.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CompanionHabitationRoomObject(
    name: String,
    role: String,
    color: Color,
    activeQuest: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(130.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(width = 110.dp, height = 80.dp)
                .background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                .border(2.dp, color, RoundedCornerShape(8.dp))
                .padding(6.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Companion Profile",
                    tint = color,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = name,
                    color = Color.White,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "${name}'S HAB-BAY",
            color = Color.White,
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(2.dp))
                .padding(horizontal = 4.dp, vertical = 2.dp)
        )
        Text(
            text = role,
            color = color.copy(alpha = 0.8f),
            fontSize = 7.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun PetIncubatorSanctuaryObject(
    petCount: Int,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pet")
    val orbitRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "orbit"
    )

    Column(
        modifier = Modifier
            .width(120.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(75.dp)
                .background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(50))
                .border(1.5.dp, Color(0xFFC084FC), RoundedCornerShape(50))
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Draw a small revolving orbit
                drawCircle(
                    color = Color(0xFFC084FC).copy(alpha = 0.2f),
                    radius = size.width / 2.3f,
                    style = Stroke(width = 1f)
                )
            }
            Icon(
                imageVector = Icons.Default.Pets,
                contentDescription = "Pet Playpen",
                tint = Color(0xFFC084FC),
                modifier = Modifier
                    .size(24.dp)
                    .graphicsLayer(rotationZ = orbitRotation)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "PET PLAYPEN",
            color = Color.White,
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(2.dp))
                .padding(horizontal = 4.dp, vertical = 2.dp)
        )
        Text(
            text = "SECURED PETS: $petCount",
            color = Color(0xFFC084FC),
            fontSize = 8.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
    }
}

// --- NEW DECK BACKGROUNDS ---

@Composable
fun BiomechanicalGreenhouseBackground() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        // Deep rich forest green to obsidian gradient
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF012110),
                    Color(0xFF02170D),
                    Color(0xFF030A06)
                )
            )
        )
        
        // Cybernetic grid lines and biomechanical curves
        // Horizontal scanline lines
        for (y in 0..size.height.toInt() step 80) {
            drawLine(
                color = Color(0xFF10B981).copy(alpha = 0.08f),
                start = Offset(0f, y.toFloat()),
                end = Offset(size.width, y.toFloat()),
                strokeWidth = 1f
            )
        }
        // Vertical arch structures representing greenhouse domes
        for (x in 0..size.width.toInt() step 160) {
            drawLine(
                color = Color(0xFF10B981).copy(alpha = 0.08f),
                start = Offset(x.toFloat(), 0f),
                end = Offset(x.toFloat(), size.height),
                strokeWidth = 1f
            )
        }
    }
}

@Composable
fun GreenhouseBioluminescentParticles() {
    val infiniteTransition = rememberInfiniteTransition(label = "greenhouse_pollen")
    val driftY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -30f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "driftY"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        // Draw floating bioluminescent specs
        val randomXOffsets = listOf(0.15f, 0.28f, 0.42f, 0.55f, 0.72f, 0.88f)
        val randomYOffsets = listOf(0.35f, 0.65f, 0.22f, 0.75f, 0.45f, 0.58f)
        randomXOffsets.forEachIndexed { i, rx ->
            val cx = size.width * rx
            val cy = (size.height * randomYOffsets[i]) + driftY
            drawCircle(
                color = Color(0xFF10B981).copy(alpha = 0.4f),
                radius = 4f + (i % 3) * 2f,
                center = Offset(cx, cy)
            )
            // Bloom glow
            drawCircle(
                color = Color(0xFF10B981).copy(alpha = 0.15f),
                radius = 12f + (i % 3) * 4f,
                center = Offset(cx, cy)
            )
        }
    }
}

@Composable
fun AquariumLoungeBackground() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        // Deep aquatic blue gradient
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF011424),
                    Color(0xFF010C1A),
                    Color(0xFF01050D)
                )
            )
        )
        // Draw subtle underwater light rays
        val path = Path()
        path.moveTo(0f, 0f)
        path.lineTo(size.width * 0.3f, 0f)
        path.lineTo(size.width * 0.45f, size.height)
        path.lineTo(size.width * 0.1f, size.height)
        path.close()
        
        drawPath(
            path = path,
            color = CyberCyan.copy(alpha = 0.05f)
        )
    }
}

@Composable
fun AquariumLoungeParticles() {
    val infiniteTransition = rememberInfiniteTransition(label = "aquarium_bubbles")
    val riseY by infiniteTransition.animateFloat(
        initialValue = 1.1f,
        targetValue = -0.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "bubbles"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val columnsX = listOf(0.12f, 0.32f, 0.55f, 0.78f, 0.92f)
        columnsX.forEachIndexed { i, cx ->
            val xPos = size.width * cx
            val yPos = size.height * ((riseY + (i * 0.2f)) % 1.0f)
            drawCircle(
                color = CyberCyan.copy(alpha = 0.3f),
                radius = 3f + (i % 2) * 2f,
                center = Offset(xPos, yPos),
                style = Stroke(width = 1f)
            )
        }
    }
}

@Composable
fun CrewHabitationBackground() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        // Deep amethyst and obsidian gradient
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF140520),
                    Color(0xFF0C0215),
                    Color(0xFF05010A)
                )
            )
        )
        // Tech bulkhead lines
        for (y in 0..size.height.toInt() step 120) {
            drawLine(
                color = Color(0xFFC084FC).copy(alpha = 0.06f),
                start = Offset(0f, y.toFloat()),
                end = Offset(size.width, y.toFloat()),
                strokeWidth = 2f
            )
        }
        for (x in 0..size.width.toInt() step 200) {
            drawLine(
                color = Color(0xFFC084FC).copy(alpha = 0.06f),
                start = Offset(x.toFloat(), 0f),
                end = Offset(x.toFloat(), size.height),
                strokeWidth = 1f
            )
        }
    }
}

@Composable
fun CrewHabitationParticles() {
    val infiniteTransition = rememberInfiniteTransition(label = "stardust")
    val twinkleAlpha by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "twinkle"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val coords = listOf(
            Offset(size.width * 0.18f, size.height * 0.3f),
            Offset(size.width * 0.42f, size.height * 0.7f),
            Offset(size.width * 0.65f, size.height * 0.25f),
            Offset(size.width * 0.85f, size.height * 0.62f)
        )
        coords.forEach { coord ->
            drawCircle(
                color = Color(0xFFC084FC).copy(alpha = twinkleAlpha),
                radius = 3f,
                center = coord
            )
        }
    }
}

// --- NEW DETAILED OVERLAY PANELS ---

@Composable
fun GreenhousePanel(state: GameUiState, viewModel: GameViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFF10B981), RoundedCornerShape(8.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF022C22).copy(alpha = 0.8f)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Spa,
                        contentDescription = "Ecosystem",
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "ECO-CENTRAL BIOMASS GRID",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xFF10B981),
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = state.ecosystemMessage ?: "PHOTOSYNTHESIS STABLE // Optimal nitrogen level calibration. Auto-watering triggers at moisture drops below 30%.",
                    color = Color(0xFFA7F3D0),
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        state.plants.forEach { plant ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CyberBorder, CutCornerShape(6.dp)),
                colors = CardDefaults.cardColors(containerColor = CyberPanel),
                shape = CutCornerShape(6.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PlantVisualStem(progress = plant.growthProgress, species = plant.species)
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = plant.name.uppercase(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "${plant.species} // ${plant.description}",
                            color = Color.Gray,
                            fontSize = 10.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("GROWTH: ${plant.growthProgress}%", color = Color(0xFF10B981), fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                                LinearProgressIndicator(progress = { plant.growthProgress / 100f }, color = Color(0xFF10B981), trackColor = Color.DarkGray)
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text("WATER: ${plant.waterLevel}%", color = CyberCyan, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                                LinearProgressIndicator(progress = { plant.waterLevel / 100f }, color = CyberCyan, trackColor = Color.DarkGray)
                            }
                        }
                    }
                    if (plant.harvestReady) {
                        Button(
                            onClick = { viewModel.harvestPlant(plant.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                        ) {
                            Text("HARVEST", fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Button(
                            onClick = { viewModel.waterPlant(plant.id) },
                            enabled = state.credits >= 10,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF065F46))
                        ) {
                            Text("WATER (-10⚿)", fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AquariumPanel(state: GameUiState, viewModel: GameViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, CyberCyan, RoundedCornerShape(8.dp)),
            colors = CardDefaults.cardColors(containerColor = CyberPanel),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "AQUATIC SPECIES SUMMARY // ${state.fish.size} INHABITANTS",
                    color = CyberCyan,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Feeding increases bio-density and yields additional replication credits. Rest to advance day cycles.",
                    color = Color.LightGray,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .border(2.dp, CyberCyan, CutCornerShape(8.dp))
                .clip(CutCornerShape(8.dp))
        ) {
            AquariumSimulationTank(fishList = state.fish)
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, CyberBorder, CutCornerShape(6.dp)),
            colors = CardDefaults.cardColors(containerColor = CyberPanel),
            shape = CutCornerShape(6.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "AQUACULTURE TERMINAL COMMANDS",
                    color = CyberCyanDim,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { state.fish.firstOrNull()?.let { viewModel.feedFish(it.id) } },
                        modifier = Modifier.weight(1f),
                        enabled = state.credits >= 15 && state.fish.any { !it.isFedToday },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan.copy(alpha = 0.15f), contentColor = CyberCyan)
                    ) {
                        Text("FEED FISH (-15 ⚿)", fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                    }

                    Button(
                        onClick = { viewModel.purchaseFishEgg("Cyber-Guppy") },
                        modifier = Modifier.weight(1f),
                        enabled = state.credits >= 120,
                        colors = ButtonDefaults.buttonColors(containerColor = CyberMagenta.copy(alpha = 0.15f), contentColor = CyberMagenta)
                    ) {
                        Text("SPAWN EGG (-120 ⚿)", fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                    }
                }
            }
        }
    }
}

@Composable
fun PetSanctuaryPanel(state: GameUiState, viewModel: GameViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (state.recruitedPets.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("NO PETS ACTIVE // RECRUIT THEM IN THE COMPANION REGISTRY", color = Color.Gray, fontFamily = FontFamily.Monospace)
            }
        } else {
            state.recruitedPets.forEach { pet ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFC084FC), CutCornerShape(8.dp)),
                    colors = CardDefaults.cardColors(containerColor = CyberPanel),
                    shape = CutCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(pet.name.uppercase(), color = Color.White, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFC084FC).copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("LVL ${pet.activeLevel}", color = Color(0xFFC084FC), fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                            }
                        }
                        Text("Ability: ${pet.ability} // ${pet.description}", color = Color.LightGray, fontSize = 10.sp, modifier = Modifier.padding(vertical = 4.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { viewModel.interactWithPet(pet.id) },
                            enabled = state.credits >= pet.upgradeCost,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC084FC).copy(alpha = 0.2f), contentColor = Color(0xFFC084FC))
                        ) {
                            Text("UPGRADE FIRMWARE (-${pet.upgradeCost} ⚿)", fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ElevatorPanel(
    currentDeck: StarshipDeck,
    onDeckSelected: (StarshipDeck) -> Unit,
    state: GameUiState,
    viewModel: GameViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "CHOOSE STARSHIP DECK LEVEL",
            color = CyberCyan,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        val deckItems = listOf(
            Triple(StarshipDeck.CAPTAINS_QUARTERS, "DECK 01 // BRIDGE & QUARTERS", "Captain's quarters, stellar viewport & Core AI holographic terminal"),
            Triple(StarshipDeck.BIOMECHANICAL_GREENHOUSE, "DECK 02 // HYDRO-GREENHOUSE BAY", "Organic greenhouse plots, rapid hydration & soil biometrics"),
            Triple(StarshipDeck.AQUARIUM_LOUNGE, "DECK 03 // BIO-AQUARIUM LOUNGE", "Holographic bio-aquarium simulation tank and ambient synthesizer"),
            Triple(StarshipDeck.CREW_HABITATION, "DECK 04 // CREW HABITATION BAY", "Companions private quarters quarters and pet incubator playpen")
        )

        deckItems.forEach { (deck, title, desc) ->
            val isCurrent = currentDeck == deck
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDeckSelected(deck) }
                    .border(
                        width = if (isCurrent) 2.dp else 1.dp,
                        color = if (isCurrent) CyberCyan else CyberBorder,
                        shape = CutCornerShape(8.dp)
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = if (isCurrent) CyberCyan.copy(alpha = 0.12f) else CyberPanel
                ),
                shape = CutCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .border(1.dp, if (isCurrent) CyberCyan else Color.Gray, RoundedCornerShape(50))
                            .padding(3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCurrent) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(CyberCyan, RoundedCornerShape(50))
                            )
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = title,
                            color = if (isCurrent) Color.White else Color.LightGray,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp
                        )
                        Text(
                            text = desc,
                            color = Color.Gray,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    if (isCurrent) {
                        Text(
                            text = "ACTIVE",
                            color = MatrixGreen,
                            fontSize = 8.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Text(
                            text = "READY",
                            color = CyberCyanDim,
                            fontSize = 8.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
