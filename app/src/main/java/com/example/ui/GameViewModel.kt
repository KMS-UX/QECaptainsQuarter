package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.GameRepository
import com.example.data.LogEntity
import com.example.data.ProfileEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class GameScreen {
    CINEMATIC,
    ENTRY,
    CABIN
}

enum class CabinetNode {
    NONE,
    WINDOW,
    DESK,
    AI,
    COFFEE,
    BOOKSHELF,
    CREW,
    GREENHOUSE,
    AQUARIUM,
    PET_SANCTUARY,
    ELEVATOR
}

data class ChatMessage(
    val sender: String, // "Captain" or "Core AI"
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class CrewResponseOption(
    val text: String,
    val costCredits: Int = 0,
    val affinityChange: Int = 0,
    val responseText: String,
    val reputationEnlightenersChange: Int = 0,
    val reputationTechnopunksChange: Int = 0
)

data class CrewMessage(
    val sender: String,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val responseOptions: List<CrewResponseOption> = emptyList()
)

data class CompanionState(
    val id: String,
    val name: String,
    val role: String,
    val weapon: String,
    val bio: String,
    val affinity: Int = 0, // 0 to 5
    val statusText: String,
    val colorHex: String,
    val giftType: String,
    val giftReaction: String,
    val chatHistory: List<CrewMessage> = emptyList()
)

data class PetState(
    val id: String,
    val name: String,
    val role: String,
    val ability: String,
    val description: String,
    val activeLevel: Int = 1,
    val upgradeCost: Int = 80,
    val reactionText: String
)

data class SectorState(
    val id: String,
    val name: String,
    val classification: String,
    val description: String,
    val coordX: Float,
    val coordY: Float,
    val hazardLevel: String = "LOW", // "LOW", "MEDIUM", "HIGH", "CRITICAL"
    val stability: Int = 100, // 0 to 100
    val cartographyProgress: Int = 0, // 0 to 100
    val isFullyCartographed: Boolean = false,
    val alignment: String = "Uncharted", // "Technopunk", "Enlightener", "Uncharted"
    val temperatureKelvin: Int = 120,
    val gravityGs: Double = 1.0,
    val targetFrequency: Float = 0.5f, // Target calibration frequency (0.0 to 1.0)
    val anomalySignatures: List<String> = emptyList(),
    val scannedResources: List<String> = emptyList()
)

data class MailMessage(
    val id: Int,
    val sender: String,
    val subject: String,
    val body: String,
    val date: String,
    val processed: Boolean = false,
    val enlightenersChange: Int = 0,
    val technopunksChange: Int = 0,
    val creditsChange: Int = 0,
    val actionText: String? = null
)

// --- LIVING SHIP SYSTEM STATE MODELS ---
data class PlantState(
    val id: String,
    val name: String,
    val species: String, // "Neon Lotus", "Vivid Fern", "Orchid", "Solar Bloom"
    val growthProgress: Int = 0, // 0 to 100
    val waterLevel: Int = 50, // 0 to 100
    val isWateredToday: Boolean = false,
    val harvestReady: Boolean = false,
    val description: String
)

data class FishState(
    val id: String,
    val name: String,
    val species: String, // "Cyber-Guppy", "Aether-Ray", "Singularity Angler"
    val isFedToday: Boolean = false,
    val generation: Int = 1,
    val growthStage: String = "Juvenile", // "Egg", "Juvenile", "Adult"
    val scaleFactor: Float = 1.0f,
    val colorHex: String = "#FF00FF"
)

data class ResearchState(
    val id: String,
    val title: String,
    val description: String,
    val progress: Int = 0, // 0 to 100
    val daysRequired: Int = 2,
    val daysSpent: Int = 0,
    val costCredits: Int = 100,
    val isStarted: Boolean = false,
    val isCompleted: Boolean = false,
    val rewardDescription: String
)

data class DecorationState(
    val id: String,
    val name: String,
    val description: String,
    val isUnlocked: Boolean = false,
    val isPlaced: Boolean = false,
    val placementSlot: String = "Shelf", // "Desk", "Window", "Shelf"
    val atmosphericBonus: String,
    val costCredits: Int = 150
)

data class GameUiState(
    val screen: GameScreen = GameScreen.CINEMATIC,
    val activeNode: CabinetNode = CabinetNode.NONE,
    val captainName: String = "Kenmong",
    val shipName: String = "Aetherius-VII",
    val credits: Int = 1200,
    val reputationEnlighteners: Int = 50,
    val reputationTechnopunks: Int = 50,
    val activeBuff: String = "None",
    val calendarDay: Int = 1,
    val selectedWeather: String = "Clear Stellar Lanes",
    val logs: List<LogEntity> = emptyList(),
    val logInputText: String = "",
    val logInputMood: String = "Serene",
    val logReflectionText: String? = null,
    val isSavingLog: Boolean = false,
    val aiChatHistory: List<ChatMessage> = emptyList(),
    val isAiLoading: Boolean = false,
    val coffeeSelection: String = "None",
    
    // Daily Rituals
    val coffeeBrewedToday: Boolean = false,
    val radarScannedToday: Boolean = false,
    val logRecordedToday: Boolean = false,
    val scanInProcess: Boolean = false,
    val scanResult: String? = null,
    val activeSubspaceMails: List<MailMessage> = emptyList(),
    val isResting: Boolean = false,
    
    // Companion & Pet Quarters
    val companions: List<CompanionState> = emptyList(),
    val recruitedPets: List<PetState> = emptyList(),
    val activeCompanionChatId: String? = null,
    val petReactionText: String? = null,

    // Galaxy Exploration & Subspace Cartography
    val sectors: List<SectorState> = emptyList(),
    val selectedSectorId: String = "home_port",
    val frequencyValue: Float = 0.5f,
    val cartographyMinigameActive: Boolean = false,
    val cartographyMinigameMessage: String? = null,

    // Living Ship Ecosystem & Simulation Engine
    val plants: List<PlantState> = emptyList(),
    val fish: List<FishState> = emptyList(),
    val research: List<ResearchState> = emptyList(),
    val decorations: List<DecorationState> = emptyList(),
    val ecosystemMessage: String? = null
)

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = GameRepository(application)

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.ensureProfileExists()
            
            // Observe Profile
            launch {
                repository.profile.collect { profile ->
                    profile?.let {
                        _uiState.value = _uiState.value.copy(
                            captainName = it.captainName,
                            shipName = it.shipName,
                            credits = it.credits,
                            reputationEnlighteners = it.reputationEnlighteners,
                            reputationTechnopunks = it.reputationTechnopunks,
                            activeBuff = it.activeBuff,
                            calendarDay = it.calendarDay,
                            selectedWeather = it.selectedWeather,
                            activeSubspaceMails = if (_uiState.value.activeSubspaceMails.isEmpty()) generateMailsForDay(it.calendarDay) else _uiState.value.activeSubspaceMails
                        )
                    }
                }
            }

            // Observe Logs
            launch {
                repository.allLogs.collect { logs ->
                    _uiState.value = _uiState.value.copy(logs = logs)
                }
            }
            
            // Initialize AI Chat
            _uiState.value = _uiState.value.copy(
                aiChatHistory = listOf(
                    ChatMessage(
                        sender = "Core AI",
                        text = "Good morning, Captain. Ship systems are operational. I have calculated the Quantum Resonance. coffee has been prepared in the corner. Would you like to check the ship log or observation window?"
                    )
                )
            )

            // Initialize Companions & Recruited Pets
            initializeCompanionsAndPets()

            // Initialize Galaxy Sectors for Exploration
            initializeSectors()

            // Initialize Ecosystem & Simulation Engine
            initializeEcosystem()
        }
    }

    fun setScreen(screen: GameScreen) {
        _uiState.value = _uiState.value.copy(screen = screen)
    }

    fun setNode(node: CabinetNode) {
        _uiState.value = _uiState.value.copy(activeNode = node)
    }

    fun setWeather(weather: String) {
        viewModelScope.launch {
            val currentProfile = repository.profile.firstOrNull() ?: ProfileEntity()
            repository.saveProfile(currentProfile.copy(selectedWeather = weather))
        }
    }

    // --- ONBOARDING: UPDATE NAMES ---
    fun updateProfileNames(captainName: String, shipName: String) {
        viewModelScope.launch {
            val currentProfile = repository.profile.firstOrNull() ?: ProfileEntity()
            repository.saveProfile(currentProfile.copy(
                captainName = captainName,
                shipName = shipName
            ))
            _uiState.value = _uiState.value.copy(
                captainName = captainName,
                shipName = shipName
            )
        }
    }

    // --- COFFEE BREWING METHOD ---
    fun brewCoffee(coffeeType: String, cost: Int, buffName: String, enlightenersDelta: Int, technopunksDelta: Int) {
        val currentState = _uiState.value
        if (currentState.credits >= cost) {
            viewModelScope.launch {
                val currentProfile = repository.profile.firstOrNull() ?: ProfileEntity()
                repository.saveProfile(
                    currentProfile.copy(
                        credits = currentProfile.credits - cost,
                        activeBuff = buffName,
                        reputationEnlighteners = (currentProfile.reputationEnlighteners + enlightenersDelta).coerceIn(0, 100),
                        reputationTechnopunks = (currentProfile.reputationTechnopunks + technopunksDelta).coerceIn(0, 100)
                    )
                )
                _uiState.value = _uiState.value.copy(coffeeBrewedToday = true)
            }
        }
    }

    // --- GALAXY RADAR SCAN METHOD ---
    fun dispatchRadarProbe(sectorId: String) {
        val currentState = _uiState.value
        val cost = if (currentState.credits >= 50) 50 else 0
        if (cost == 0) {
            _uiState.value = _uiState.value.copy(
                scanResult = "ERROR: Insufficient credits (50 required for tactical scanner probe)."
            )
            return
        }

        _uiState.value = _uiState.value.copy(
            scanInProcess = true,
            scanResult = null
        )
        viewModelScope.launch {
            kotlinx.coroutines.delay(2000)
            val rewards = listOf(
                Pair("Detected rich dark-matter pockets! Harvested raw elements worth 150 Credits. Cartography progress increased (+25%).", 150),
                Pair("Probe synchronized warp matrices with Technopunk relays. Gained +10% Technopunk morale. Cartography progress increased (+25%).", 0),
                Pair("Found a mysterious ancient quantum obelisk. Gained +10% Enlightener resonance. Cartography progress increased (+25%).", 0),
                Pair("Encountered solar storm interference. Probe shields drained some charge (-30 Credits). Cartography progress increased (+25%).", -30)
            )
            val selected = rewards.random()
            
            val currentProfile = repository.profile.firstOrNull() ?: ProfileEntity()
            val creditsDelta = selected.second
            var enlightenersDelta = 0
            var technopunksDelta = 0
            
            if (selected.first.contains("Technopunk")) {
                technopunksDelta = 10
            } else if (selected.first.contains("Enlightener")) {
                enlightenersDelta = 10
            }
            
            val updatedSectors = currentState.sectors.map { s ->
                if (s.id == sectorId) {
                    val newProgress = (s.cartographyProgress + 25).coerceIn(0, 100)
                    s.copy(
                        cartographyProgress = newProgress,
                        isFullyCartographed = newProgress >= 100,
                        stability = if (newProgress >= 100) 100 else s.stability
                    )
                } else s
            }
            
            val oldSector = currentState.sectors.find { s -> s.id == sectorId }
            var completionBonus = ""
            if (oldSector != null && !oldSector.isFullyCartographed && (oldSector.cartographyProgress + 25 >= 100)) {
                completionBonus = " // SUCCESS: Hyperlane fully mapped! Earned +200 credits bonus."
                repository.saveProfile(
                    currentProfile.copy(
                        credits = (currentProfile.credits - cost + creditsDelta + 200).coerceAtLeast(0),
                        reputationEnlighteners = (currentProfile.reputationEnlighteners + enlightenersDelta + 10).coerceIn(0, 100),
                        reputationTechnopunks = (currentProfile.reputationTechnopunks + technopunksDelta + 10).coerceIn(0, 100)
                    )
                )
            } else {
                repository.saveProfile(
                    currentProfile.copy(
                        credits = (currentProfile.credits - cost + creditsDelta).coerceAtLeast(0),
                        reputationEnlighteners = (currentProfile.reputationEnlighteners + enlightenersDelta).coerceIn(0, 100),
                        reputationTechnopunks = (currentProfile.reputationTechnopunks + technopunksDelta).coerceIn(0, 100)
                    )
                )
            }
            
            _uiState.value = _uiState.value.copy(
                scanInProcess = false,
                scanResult = selected.first + completionBonus,
                sectors = updatedSectors,
                radarScannedToday = true
            )
        }
    }

    // --- RESOLVE SUBSPACE MAIL ---
    fun resolveMail(mailId: Int) {
        val updatedList = _uiState.value.activeSubspaceMails.map { mail ->
            if (mail.id == mailId) {
                mail.copy(processed = true)
            } else {
                mail
            }
        }
        val mailToResolve = _uiState.value.activeSubspaceMails.find { it.id == mailId } ?: return
        
        _uiState.value = _uiState.value.copy(activeSubspaceMails = updatedList)
        
        viewModelScope.launch {
            val currentProfile = repository.profile.firstOrNull() ?: ProfileEntity()
            repository.saveProfile(
                currentProfile.copy(
                    credits = (currentProfile.credits + mailToResolve.creditsChange).coerceAtLeast(0),
                    reputationEnlighteners = (currentProfile.reputationEnlighteners + mailToResolve.enlightenersChange).coerceIn(0, 100),
                    reputationTechnopunks = (currentProfile.reputationTechnopunks + mailToResolve.technopunksChange).coerceIn(0, 100)
                )
            )
        }
    }

    // --- REST AND ADVANCE DAY (DAILY RITUAL DEEP SLEEP) ---
    fun restAndAdvanceDay() {
        _uiState.value = _uiState.value.copy(isResting = true)
        viewModelScope.launch {
            kotlinx.coroutines.delay(2000)
            val currentProfile = repository.profile.firstOrNull() ?: ProfileEntity()
            val nextDay = currentProfile.calendarDay + 1
            val weathers = listOf("Quantum Storm", "Ice Comet Shower", "Dense Nebula", "Electromagnetic Interference", "Clear Stellar Lanes")
            val nextWeather = weathers.random()
            
            // Calculate decoration bonus and advance state variables
            val (reportMessage, bonusCredits) = advanceSimulationDay(nextDay)
            
            repository.saveProfile(
                currentProfile.copy(
                    calendarDay = nextDay,
                    selectedWeather = nextWeather,
                    credits = currentProfile.credits + 150 + bonusCredits, // Daily allowance + decor bonus!
                    activeBuff = "None" // Reset buff overnight
                )
            )
            
            _uiState.value = _uiState.value.copy(
                isResting = false,
                coffeeBrewedToday = false,
                radarScannedToday = false,
                logRecordedToday = false,
                scanResult = null,
                activeSubspaceMails = generateMailsForDay(nextDay)
            )
        }
    }

    fun generateMailsForDay(day: Int): List<MailMessage> {
        return when (day) {
            1 -> listOf(
                MailMessage(
                    id = 1,
                    sender = "Arch-Priest Vaelen [Enlighteners]",
                    subject = "Quantum Resonance Influx Detected",
                    body = "Greetings Captain. Our sensory dishes detect a wonderful quantum alignment around your coordinates. Please keep your ship state tranquil. Do not let the Technopunks' biomechanical devices disrupt the cosmic flow. We send our blessing and some credits.",
                    date = "SD 8243.01",
                    creditsChange = 100,
                    enlightenersChange = 8,
                    technopunksChange = -2,
                    actionText = "ACKNOWLEDGE BLESSING (+100 ⚿, +8% Enlighteners)"
                ),
                MailMessage(
                    id = 2,
                    sender = "Chief Engineer Sparky [Technopunks]",
                    subject = "Overclocked Core Injector Deal",
                    body = "Yo Cap! The engines are humming, but we can make them roar. I've got some spare cybernetic power coils. Wire me 150 credits, and I'll rig your ship to overclock its singularity drives. Deal?",
                    date = "SD 8243.02",
                    creditsChange = -150,
                    enlightenersChange = -4,
                    technopunksChange = 15,
                    actionText = "PROCURE COILS (-150 ⚿, +15% Technopunks)"
                )
            )
            2 -> listOf(
                MailMessage(
                    id = 3,
                    sender = "Sector Fleet Admiral",
                    subject = "ALERT: Galactic Dust Storm Warning",
                    body = "Captain, high-energy electromagnetic anomalies are sweep-scanning the local cluster. All captains are ordered to polarize deflector grids and log biometric readings to monitor cognitive integrity. Do not stray off target.",
                    date = "SD 8243.10",
                    creditsChange = 50,
                    enlightenersChange = 4,
                    technopunksChange = 4,
                    actionText = "ACKNOWLEDGE ADVISORY (+50 ⚿)"
                ),
                MailMessage(
                    id = 4,
                    sender = "Cyber-Merchant Jax",
                    subject = "Black Market Nanite Inoculation",
                    body = "Psst... Captain. Want to bypass normal medical protocols? I have a container of illegal neuro-nano bots. They'll boost your memory logs, but the mystic Enlighteners might find them offensive. 100 credits.",
                    date = "SD 8243.11",
                    creditsChange = -100,
                    enlightenersChange = -10,
                    technopunksChange = 12,
                    actionText = "INJECT NANOBOTS (-100 ⚿, +12% Technopunks)"
                )
            )
            else -> listOf(
                MailMessage(
                    id = 5 + day,
                    sender = "Central Command Dispatch",
                    subject = "Routine Sector Patrol Stipend",
                    body = "Stardate cycle $day patrol duties completed successfully. Credits allocated to ship account. Keep maintaining space lane safety, Captain.",
                    date = "SD 8243.${day * 7}",
                    creditsChange = 100,
                    enlightenersChange = 2,
                    technopunksChange = 2,
                    actionText = "COLLECT STIPEND (+100 ⚿)"
                )
            )
        }
    }

    fun updateLogInputText(text: String) {
        _uiState.value = _uiState.value.copy(logInputText = text)
    }

    fun updateLogInputMood(mood: String) {
        _uiState.value = _uiState.value.copy(logInputMood = mood)
    }

    // --- SAVE CAPTAIN LOG WITH GEMINI REFLECTION (HIGH THINKING MODE) ---
    fun saveLog() {
        val text = _uiState.value.logInputText
        val mood = _uiState.value.logInputMood
        if (text.isBlank()) return

        _uiState.value = _uiState.value.copy(isSavingLog = true, logReflectionText = "AI Core is analyzing entry neural patterns...")

        viewModelScope.launch {
            val prompt = """
                The starship captain recorded the following log entry:
                Mood: $mood
                Log Text: "$text"
                
                Please generate a brief (1-2 sentences), highly immersive, sci-fi companion AI reflection or comment on this log. 
                Keep it highly technical but warm, fitting for a companion AI onboard. Do not use generic responses.
            """.trimIndent()

            val systemInstruction = """
                You are A.D.A.M., the tactical and domestic companion AI onboard the starship Aetherius-VII. 
                Your tone is highly advanced, scientific, respectful, and slightly poetic. 
                Keep replies very concise (maximum 40 words) and deeply immersive.
            """.trimIndent()

            val reflection = repository.queryShipAi(prompt, systemInstruction)

            val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
            val currentTime = formatter.format(Date())
            val stardate = getStardateString()

            val logEntity = LogEntity(
                stardate = stardate,
                realTime = currentTime,
                text = text,
                mood = mood,
                aiReflection = reflection
            )

            repository.insertLog(logEntity)

            // Save log and award credits!
            val currentProfile = repository.profile.firstOrNull() ?: ProfileEntity()
            repository.saveProfile(
                currentProfile.copy(
                    credits = currentProfile.credits + 100 // Reward the Captain with 100 credits per log!
                )
            )

            _uiState.value = _uiState.value.copy(
                isSavingLog = false,
                logInputText = "",
                logReflectionText = reflection,
                logRecordedToday = true
            )
        }
    }

    fun deleteLog(id: Int) {
        viewModelScope.launch {
            repository.deleteLog(id)
        }
    }

    // --- CHAT WITH AI TERMINAL (HIGH THINKING MODE) ---
    fun sendChatMessage(message: String) {
        if (message.isBlank()) return

        val userChat = ChatMessage(sender = "Captain", text = message)
        val updatedHistory = _uiState.value.aiChatHistory + userChat
        _uiState.value = _uiState.value.copy(
            aiChatHistory = updatedHistory,
            isAiLoading = true
        )

        viewModelScope.launch {
            val systemInstruction = """
                You are L.I.L.A., the central core AI of the Captain's starship. 
                Your personality is sophisticated, highly analytical, loyal, and cozy. 
                You analyze situations with extreme depth because your neural core is processing complex calculations (High thinking level).
                Respond to the Captain's message or queries in character. Keep responses immersive, creative, and relatively concise (under 75 words).
                Incorporate starship status (Weather: ${_uiState.value.selectedWeather}, Buffs: ${_uiState.value.activeBuff}) when relevant.
            """.trimIndent()

            val responseText = repository.queryShipAi(message, systemInstruction)

            val aiChat = ChatMessage(sender = "Core AI", text = responseText)
            _uiState.value = _uiState.value.copy(
                aiChatHistory = _uiState.value.aiChatHistory + aiChat,
                isAiLoading = false
            )
        }
    }

    fun clearChat() {
        _uiState.value = _uiState.value.copy(
            aiChatHistory = listOf(
                ChatMessage(
                    sender = "Core AI",
                    text = "Lila central neural node reset complete. System initialized."
                )
            )
        )
    }

    private fun getStardateString(): String {
        val sdf = SimpleDateFormat("DDD", Locale.getDefault())
        val dayOfYear = sdf.format(Date()).toDouble()
        val year = SimpleDateFormat("yyyy", Locale.getDefault()).format(Date()).toInt()
        val stardate = year + (dayOfYear / 365.0)
        return String.format(Locale.US, "SD %.2f", stardate * 4.3 + 1200.0)
    }

    // --- COMPANION & PETS INITIALIZATION & ACTIONS ---

    private fun initializeCompanionsAndPets() {
        val initialCompanions = listOf(
            CompanionState(
                id = "lyra",
                name = "Lyra",
                role = "Ranged Specialist / Sniper",
                weapon = "Sniper Rifle",
                bio = "A calm and calculating sniper from the Slums. Lyra joined you after you saved her from an Eclipse Syndicate hit. She trusts few, but she protects fiercely.",
                affinity = 2,
                statusText = "Calibrating sights",
                colorHex = "C084FC", // Purple
                giftType = "Vapor-polished Optics Calibration Liquid",
                giftReaction = "Precise optics are a shooter's lifeline. Thank you, Captain. This gesture... means a lot.",
                chatHistory = listOf(
                    CrewMessage(
                        sender = "Lyra",
                        text = "Calibration finished on the heavy hyper-spectral optics. The Eclipse Syndicate has been quiet lately... too quiet. Captain, are you taking enough rest? I've noticed you pacing around the cabin.",
                        responseOptions = listOf(
                            CrewResponseOption(
                                text = "I'm fine, Lyra. Your sniper lens is in perfect order?",
                                affinityChange = 1,
                                responseText = "Always, Captain. But remember: an un-calibrated eye is as useless as an un-charged blaster. Get some coffee."
                            ),
                            CrewResponseOption(
                                text = "Let's run a security sweep. Syndicate scouts could be tracking us.",
                                affinityChange = 2,
                                responseText = "Understood. Re-orienting sensory grids. I am watching your six.",
                                reputationTechnopunksChange = 5
                            )
                        )
                    )
                )
            ),
            CompanionState(
                id = "nova",
                name = "Nova",
                role = "Infiltrator / Hacker",
                weapon = "SMG",
                bio = "A brilliant netrunner and locksmith. Nova lives for information and the thrill of breaking impossible systems. Sarcastic, playful, but always three steps ahead.",
                affinity = 1,
                statusText = "Wiping data traces",
                colorHex = "E91E63", // Pink/Magenta
                giftType = "Quantum Decryption Override Shard",
                giftReaction = "Woah! A high-frequency matrix keyset? You really know how to make a hacker's heart skip! This is sweet!",
                chatHistory = listOf(
                    CrewMessage(
                        sender = "Nova",
                        text = "Captain, look at this! I tapped into an encrypted beacon near Sector-4. It's filled with juicy Enlightener historical records. I could wipe it for credits, or compile it into the ship's codex!",
                        responseOptions = listOf(
                            CrewResponseOption(
                                text = "Compile it. Knowledge is our best navigator.",
                                affinityChange = 2,
                                responseText = "Boooring... but alright, I uploaded it. The Enlighteners will probably think you're a sage now.",
                                reputationEnlightenersChange = 10
                            ),
                            CrewResponseOption(
                                text = "Wipe it and sell the data. We need credits.",
                                costCredits = -150, // Positive credits reward
                                affinityChange = 1,
                                responseText = "Heck yeah! Credits wired. Nice doing business with you, Captain.",
                                reputationTechnopunksChange = 10
                            )
                        )
                    )
                )
            ),
            CompanionState(
                id = "elara",
                name = "Elara",
                role = "Medic / Biotechnician",
                weapon = "Medi-Gauntlet",
                bio = "A kind-hearted healer who believes life can still flourish. Elara studies bio-mechanics and uses her tech to mend both bodies and hearts.",
                affinity = 3,
                statusText = "Cultivating flora seeds",
                colorHex = "00FF66", // Green
                giftType = "Rare Nebula Orchid Seed",
                giftReaction = "Oh my... a real flora seed from the nebula! I will nurture it in the biosphere. Your kindness heals, Captain.",
                chatHistory = listOf(
                    CrewMessage(
                        sender = "Elara",
                        text = "Captain, the ship's hydroponic garden is responding wonderfully to our quantum soil injections. I synthesized a rare bio-infusion. Should we administer it to boost our life-support buffers or save it?",
                        responseOptions = listOf(
                            CrewResponseOption(
                                text = "Administer it. Keep the crew in peak condition.",
                                affinityChange = 2,
                                responseText = "A wise decision. The air smells fresher already, and I feel much closer to this ship.",
                                reputationEnlightenersChange = 10
                            ),
                            CrewResponseOption(
                                text = "Let's store it. In space, emergencies are constant.",
                                affinityChange = 1,
                                responseText = "Sensible. I have put it in cold stasis inside the medical replicator.",
                                reputationTechnopunksChange = 5
                            )
                        )
                    )
                )
            ),
            CompanionState(
                id = "quark",
                name = "Quark",
                role = "Android Companion",
                weapon = "Energy Blade",
                bio = "An advanced android with a curious mind and hidden memories. Sees patterns others cannot. Loyal, logical, and quietly fierce.",
                affinity = 2,
                statusText = "Analyzing sector logs",
                colorHex = "00F0FF", // Cyan
                giftType = "Sub-Zero Superconductor Core",
                giftReaction = "Thermal efficiency increased by 18.4%. My neural networks process your gift as an expression of strong social bonding. Thank you, Captain.",
                chatHistory = listOf(
                    CrewMessage(
                        sender = "Quark",
                        text = "Query for Captain: My computational matrices indicate a 94.2% chance that you are experiencing elevated cognitive weariness. I have compiled a series of soothing sub-space resonance frequencies. Shall I play them?",
                        responseOptions = listOf(
                            CrewResponseOption(
                                text = "Please do, Quark. My brain needs a rest.",
                                affinityChange = 2,
                                responseText = "Initializing soundscape. Cognitive metrics stabilizing. You are an exceptional organic leader, Captain."
                            ),
                            CrewResponseOption(
                                text = "I prefer silence. Let's focus on ship diagnostics.",
                                affinityChange = 1,
                                responseText = "Understood. Soundscape deactivated. Proceeding with thermodynamic subsystem verification."
                            )
                        )
                    )
                )
            )
        )

        val initialPets = listOf(
            PetState(
                id = "byte",
                name = "Byte",
                role = "Combat Drone",
                ability = "Target Mark",
                description = "Scouts and highlights high-threat targets.",
                reactionText = "BEEP BOOP! *Sensory dome glows in green concentric ring patterns.*"
            ),
            PetState(
                id = "scrapy",
                name = "Scrapy",
                role = "Mech Dog",
                ability = "Threat Taunt",
                description = "Emits low-frequency pulse to draw enemy fire.",
                reactionText = "AWOOO-RUFF! *Tail wagging intensifies, gear joints humming in excitement.*"
            ),
            PetState(
                id = "pix",
                name = "Pix",
                role = "Quantum Cat",
                ability = "Item Finder",
                description = "Phase-shifts to locate hidden parts and credits.",
                reactionText = "MEOWWW. *Spins in a tight circle of gravity-warping purrs and nuzzles your boots.*"
            )
        )

        _uiState.value = _uiState.value.copy(
            companions = initialCompanions,
            recruitedPets = initialPets
        )
    }

    fun selectCompanionChat(companionId: String?) {
        _uiState.value = _uiState.value.copy(activeCompanionChatId = companionId)
    }

    fun sendCompanionResponse(companionId: String, responseOption: CrewResponseOption) {
        val currentState = _uiState.value
        val updatedCompanions = currentState.companions.map { comp ->
            if (comp.id == companionId) {
                val lastMsg = comp.chatHistory.lastOrNull()
                val newChatHistory = if (lastMsg != null) {
                    val messageWithoutOptions = lastMsg.copy(responseOptions = emptyList())
                    comp.chatHistory.dropLast(1) + messageWithoutOptions +
                        CrewMessage(sender = currentState.captainName, text = responseOption.text) +
                        CrewMessage(sender = comp.name, text = responseOption.responseText)
                } else {
                    comp.chatHistory +
                        CrewMessage(sender = currentState.captainName, text = responseOption.text) +
                        CrewMessage(sender = comp.name, text = responseOption.responseText)
                }
                comp.copy(
                    affinity = (comp.affinity + responseOption.affinityChange).coerceIn(0, 5),
                    chatHistory = newChatHistory
                )
            } else comp
        }

        _uiState.value = currentState.copy(companions = updatedCompanions)

        // Persist credits and reputation adjustments
        viewModelScope.launch {
            val currentProfile = repository.profile.firstOrNull() ?: ProfileEntity()
            // If responseOption.costCredits is negative, it's a reward (added to credits)
            val updatedCredits = (currentProfile.credits - responseOption.costCredits).coerceAtLeast(0)
            val updatedEnlighteners = (currentProfile.reputationEnlighteners + responseOption.reputationEnlightenersChange).coerceIn(0, 100)
            val updatedTechnopunks = (currentProfile.reputationTechnopunks + responseOption.reputationTechnopunksChange).coerceIn(0, 100)
            
            repository.saveProfile(
                currentProfile.copy(
                    credits = updatedCredits,
                    reputationEnlighteners = updatedEnlighteners,
                    reputationTechnopunks = updatedTechnopunks
                )
            )
        }
    }

    fun replicateCompanionGift(companionId: String) {
        val currentState = _uiState.value
        if (currentState.credits >= 150) {
            val updatedCompanions = currentState.companions.map { comp ->
                if (comp.id == companionId) {
                    val giftMsg = CrewMessage(sender = currentState.captainName, text = "Replicated and presented gift: ${comp.giftType}")
                    val reactionMsg = CrewMessage(sender = comp.name, text = comp.giftReaction)
                    comp.copy(
                        affinity = (comp.affinity + 1).coerceIn(0, 5),
                        chatHistory = comp.chatHistory + giftMsg + reactionMsg
                    )
                } else comp
            }

            _uiState.value = currentState.copy(companions = updatedCompanions)

            // Deduct Credits and save to DB
            viewModelScope.launch {
                val currentProfile = repository.profile.firstOrNull() ?: ProfileEntity()
                repository.saveProfile(
                    currentProfile.copy(
                        credits = (currentProfile.credits - 150).coerceAtLeast(0)
                    )
                )
            }
        }
    }

    fun interactWithPet(petId: String) {
        val currentState = _uiState.value
        val pet = currentState.recruitedPets.find { it.id == petId } ?: return
        val cost = pet.upgradeCost
        
        if (currentState.credits >= cost) {
            val updatedPets = currentState.recruitedPets.map { p ->
                if (p.id == petId) {
                    val newLevel = p.activeLevel + 1
                    val newCost = newLevel * 40 + 80
                    p.copy(
                        activeLevel = newLevel,
                        upgradeCost = newCost
                    )
                } else p
            }

            _uiState.value = currentState.copy(
                recruitedPets = updatedPets,
                petReactionText = "${pet.name} leveled up! Level: ${pet.activeLevel + 1} // Reaction: ${pet.reactionText}"
            )

            viewModelScope.launch {
                val currentProfile = repository.profile.firstOrNull() ?: ProfileEntity()
                repository.saveProfile(
                    currentProfile.copy(
                        credits = (currentProfile.credits - cost).coerceAtLeast(0)
                    )
                )
            }
        }
    }

    fun clearPetReaction() {
        _uiState.value = _uiState.value.copy(petReactionText = null)
    }

    // --- GALAXY EXPLORATION & SUBSPACE CARTOGRAPHY METHODS ---

    private fun initializeSectors() {
        val initialSectors = listOf(
            SectorState(
                id = "home_port",
                name = "Aetherius Home Port",
                classification = "Central Base Sector",
                description = "Our primary deep-space command terminal and hyperlane harbor. Fully charted, secured by fleet patrol forces, and operates as a safe zone.",
                coordX = 200f,
                coordY = 250f,
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
                coordX = 120f,
                coordY = 150f,
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
                coordY = 100f,
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
                coordX = 350f,
                coordY = 320f,
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
            ),
            SectorState(
                id = "dark_nebula",
                name = "Obsidian Dark Nebula",
                classification = "Gas Dust Veil",
                description = "An opaque expanse of cold gas and cosmic dust, completely shielding light. Ships often get lost here without precision cartography sweeps.",
                coordX = 60f,
                coordY = 320f,
                hazardLevel = "HIGH",
                stability = 48,
                cartographyProgress = 0,
                isFullyCartographed = false,
                alignment = "Uncharted",
                temperatureKelvin = 12,
                gravityGs = 0.05,
                targetFrequency = 0.67f,
                anomalySignatures = listOf("Darkness Field Distortion", "Sub-zero Static Flare"),
                scannedResources = listOf("Liquid Helium-3", "Heavy Elements")
            ),
            SectorState(
                id = "plasma_ridge",
                name = "Helios Plasma Ridge",
                classification = "Stellar Flare Field",
                description = "A scorching cosmic ridge near a hyper-giant star. Extreme thermal radiation. Perfect for hyper-charging experimental solar engines.",
                coordX = 380f,
                coordY = 80f,
                hazardLevel = "EXTREME",
                stability = 35,
                cartographyProgress = 0,
                isFullyCartographed = false,
                alignment = "Technopunk",
                temperatureKelvin = 1450,
                gravityGs = 2.4,
                targetFrequency = 0.94f,
                anomalySignatures = listOf("Solar Prominence Loop", "Magnetic Storm Pulse"),
                scannedResources = listOf("Plasma Fuel Cells", "Aurum Plates")
            )
        )
        _uiState.value = _uiState.value.copy(
            sectors = initialSectors,
            selectedSectorId = "home_port"
        )
    }

    fun selectSector(sectorId: String) {
        _uiState.value = _uiState.value.copy(
            selectedSectorId = sectorId,
            cartographyMinigameMessage = null
        )
    }

    fun calibrateQuantumSensor(frequency: Float) {
        _uiState.value = _uiState.value.copy(frequencyValue = frequency)
    }

    fun scanWithCalibratedFrequency() {
        val currentState = _uiState.value
        val selectedId = currentState.selectedSectorId
        val sector = currentState.sectors.find { it.id == selectedId } ?: return
        
        val currentFreq = currentState.frequencyValue
        val targetFreq = sector.targetFrequency
        val diff = Math.abs(currentFreq - targetFreq)
        
        // If difference is small, it's a critical alignment scan!
        val isCritical = diff <= 0.06f
        val progressIncrement = if (isCritical) 40 else 15
        
        val cost = if (currentState.credits >= 30) 30 else 0
        if (cost == 0) {
            _uiState.value = _uiState.value.copy(
                cartographyMinigameMessage = "ERROR: Insufficient credits (30 required for sensor sweep)."
            )
            return
        }
        
        _uiState.value = _uiState.value.copy(
            scanInProcess = true,
            cartographyMinigameMessage = null
        )
        
        viewModelScope.launch {
            kotlinx.coroutines.delay(1800)
            
            val updatedSectors = currentState.sectors.map { s ->
                if (s.id == selectedId) {
                    val newProgress = (s.cartographyProgress + progressIncrement).coerceIn(0, 100)
                    val fullyCartographed = newProgress >= 100
                    s.copy(
                        cartographyProgress = newProgress,
                        isFullyCartographed = fullyCartographed,
                        stability = if (fullyCartographed) 100 else s.stability
                    )
                } else s
            }
            
            val oldSector = currentState.sectors.find { it.id == selectedId }!!
            val newlyCompleted = !oldSector.isFullyCartographed && (oldSector.cartographyProgress + progressIncrement >= 100)
            
            var rewardMsg = ""
            var rewardCredits = 0
            var rewardEnlighteners = 0
            var rewardTechnopunks = 0
            
            if (isCritical) {
                rewardMsg = "CRITICAL RESONANCE DETECTED! Sensor perfectly tuned to ${String.format(Locale.US, "%.0f", targetFreq * 100)}% MHz. Subspace Cartography increased by +40%!"
                rewardCredits = 60
            } else {
                rewardMsg = "Sensor tuned to ${String.format(Locale.US, "%.0f", currentFreq * 100)}% MHz. Target: ${String.format(Locale.US, "%.0f", targetFreq * 100)}% MHz. Signal alignment weak, but charted +15% progress."
                rewardCredits = 10
            }
            
            if (newlyCompleted) {
                rewardMsg += " // SUCCESS: Hyperlane fully mapped! Earned +250 Credits and +15 Faction Reputation."
                rewardCredits += 250
                if (oldSector.alignment == "Technopunk") {
                    rewardTechnopunks = 15
                } else if (oldSector.alignment == "Enlightener") {
                    rewardEnlighteners = 15
                } else {
                    rewardTechnopunks = 8
                    rewardEnlighteners = 8
                }
            }
            
            val currentProfile = repository.profile.firstOrNull() ?: ProfileEntity()
            repository.saveProfile(
                currentProfile.copy(
                    credits = (currentProfile.credits - cost + rewardCredits).coerceAtLeast(0),
                    reputationEnlighteners = (currentProfile.reputationEnlighteners + rewardEnlighteners).coerceIn(0, 100),
                    reputationTechnopunks = (currentProfile.reputationTechnopunks + rewardTechnopunks).coerceIn(0, 100)
                )
            )
            
            _uiState.value = _uiState.value.copy(
                scanInProcess = false,
                sectors = updatedSectors,
                cartographyMinigameMessage = rewardMsg,
                radarScannedToday = true
            )
        }
    }

    // --- LIVING SHIP SIMULATION ENGINE METHODS ---

    private fun initializeEcosystem() {
        val initialPlants = listOf(
            PlantState(
                id = "plant_lotus",
                name = "Vibrant Neon Lotus",
                species = "Neon Lotus",
                growthProgress = 15,
                waterLevel = 60,
                isWateredToday = false,
                harvestReady = false,
                description = "An exotic bio-luminescent flower that absorbs stray radiation and shines neon cyan."
            ),
            PlantState(
                id = "plant_fern",
                name = "Aether Whisper Fern",
                species = "Vivid Fern",
                growthProgress = 40,
                waterLevel = 45,
                isWateredToday = false,
                harvestReady = false,
                description = "A gaseous frond from a dense gravity well that sighs softly when stroked."
            ),
            PlantState(
                id = "plant_orchid",
                name = "Subspace Chronal Orchid",
                species = "Orchid",
                growthProgress = 0,
                waterLevel = 75,
                isWateredToday = false,
                harvestReady = false,
                description = "A rare flora whose petals flicker in and out of phase with our current timeline."
            )
        )

        val initialFish = listOf(
            FishState(
                id = "fish_guppy_1",
                name = "Bubbles",
                species = "Cyber-Guppy",
                isFedToday = false,
                generation = 1,
                growthStage = "Adult",
                scaleFactor = 1.0f,
                colorHex = "#00FFFF"
            ),
            FishState(
                id = "fish_ray_1",
                name = "Starlight",
                species = "Aether-Ray",
                isFedToday = false,
                generation = 1,
                growthStage = "Adult",
                scaleFactor = 1.2f,
                colorHex = "#FF00FF"
            ),
            FishState(
                id = "fish_angler_1",
                name = "Voidy",
                species = "Singularity Angler",
                isFedToday = false,
                generation = 1,
                growthStage = "Juvenile",
                scaleFactor = 0.8f,
                colorHex = "#FFA500"
            )
        )

        val initialResearch = listOf(
            ResearchState(
                id = "res_auto_irrigation",
                title = "Hyper-conductive Automated Irrigation",
                description = "Synthesizes an auto-hydration mesh using cobalt nano-slabs. Prevents water depletion from dropping below 30% daily.",
                progress = 0,
                daysRequired = 2,
                daysSpent = 0,
                costCredits = 250,
                rewardDescription = "Unlocks Hydroponic Auto-Waterer (keeps plants hydrated!)"
            ),
            ResearchState(
                id = "res_gene_splice",
                title = "Quantum Bioluminescence Enhancement",
                description = "Alters fish genetic matrices using sub-atomic ion arrays to increase double-birth and cross-breeding rates by 50%.",
                progress = 0,
                daysRequired = 3,
                daysSpent = 0,
                costCredits = 400,
                rewardDescription = "Increases Fish Breeding chance and unlocks rare Golden scales."
            ),
            ResearchState(
                id = "res_warp_decor",
                title = "Subspace Gravity Well Replicator",
                description = "Modulates the molecular synthesizer to recreate beautiful anti-gravity ornaments to float elegantly around the quarters.",
                progress = 0,
                daysRequired = 1,
                daysSpent = 0,
                costCredits = 150,
                rewardDescription = "Unlocks the 'Floating Quantum Prism' decoration."
            )
        )

        val initialDecorations = listOf(
            DecorationState(
                id = "dec_lantern",
                name = "Warm Cyber-Lantern",
                description = "A retro-styled kerosene lantern refitted with a neon-amber cold-fusion filament.",
                isUnlocked = true,
                isPlaced = true,
                placementSlot = "Shelf",
                atmosphericBonus = "+30 Credits per Day",
                costCredits = 0
            ),
            DecorationState(
                id = "dec_bonsai",
                name = "Holographic Bonsai Tree",
                description = "A rotating neon-green hologram of a traditional juniper tree. Calms frayed nerves.",
                isUnlocked = false,
                isPlaced = false,
                placementSlot = "Desk",
                atmosphericBonus = "+10% Enlightener Rep gained from actions",
                costCredits = 150
            ),
            DecorationState(
                id = "dec_gramophone",
                name = "Vintage Warp Gramophone",
                description = "Plays old earth melodies encoded onto carbonized sound-plates. Jax's favorite cozy addition.",
                isUnlocked = false,
                isPlaced = false,
                placementSlot = "Shelf",
                atmosphericBonus = "+10% Technopunk Rep gained from actions",
                costCredits = 200
            ),
            DecorationState(
                id = "dec_toy",
                name = "Stuffed Alien Plushy",
                description = "An incredibly soft five-eyed plush companion found in Sector Sigma-9. Crew often snuggle it.",
                isUnlocked = false,
                isPlaced = false,
                placementSlot = "Window",
                atmosphericBonus = "+15% Pet and Drone upgrade efficiency",
                costCredits = 120
            ),
            DecorationState(
                id = "dec_prism",
                name = "Floating Quantum Prism",
                description = "A glittering amethyst gemstone floating inside a self-contained anti-gravity emitter box.",
                isUnlocked = false,
                isPlaced = false,
                placementSlot = "Desk",
                atmosphericBonus = "+50 daily Credits and ultra resonance glow",
                costCredits = 0
            )
        )

        _uiState.value = _uiState.value.copy(
            plants = initialPlants,
            fish = initialFish,
            research = initialResearch,
            decorations = initialDecorations
        )
    }

    private fun advanceSimulationDay(nextDay: Int): Pair<String, Int> {
        val current = _uiState.value
        val hasAutoIrrigation = current.research.find { it.id == "res_auto_irrigation" }?.isCompleted == true
        val hasGeneSplice = current.research.find { it.id == "res_gene_splice" }?.isCompleted == true
        
        val messageBuilder = StringBuilder()
        messageBuilder.append("SD 8243.$nextDay LIFE-SUPPORT & SIMULATION REPORT:\n\n")

        // 1. Plants growth tick
        val updatedPlants = current.plants.map { plant ->
            var water = plant.waterLevel
            if (hasAutoIrrigation && water < 30) {
                water = 65
            } else {
                water = (water - 25).coerceAtLeast(0)
            }
            
            val isGrown = plant.growthProgress >= 100
            val newProgress = if (water > 0 && !isGrown) {
                (plant.growthProgress + 35).coerceIn(0, 100)
            } else plant.growthProgress
            
            val ready = newProgress >= 100
            if (ready && !plant.harvestReady) {
                messageBuilder.append("🌱 [Hydroponics] ${plant.name} has fully matured and is ready to harvest!\n")
            }
            
            plant.copy(
                growthProgress = newProgress,
                waterLevel = water,
                isWateredToday = false,
                harvestReady = ready
            )
        }

        // 2. Fish breeding & growth tick
        val updatedFish = current.fish.toMutableList()
        val numFed = current.fish.count { it.isFedToday }
        
        if (numFed > 0 && current.fish.size < 10) {
            val breedChance = if (hasGeneSplice) 0.65f else 0.35f
            if (Math.random() < breedChance) {
                val speciesOptions = listOf("Cyber-Guppy", "Aether-Ray", "Singularity Angler")
                val randomSpecies = speciesOptions.random()
                val randomColor = listOf("#00FFFF", "#FF00FF", "#FFA500", "#FFD700", "#32CD32").random()
                val idSuffix = (100..999).random()
                val newBorn = FishState(
                    id = "fish_born_$idSuffix",
                    name = "Baby ${randomSpecies.substringAfter("-")}",
                    species = randomSpecies,
                    isFedToday = false,
                    generation = (current.fish.maxOfOrNull { it.generation } ?: 1) + 1,
                    growthStage = "Juvenile",
                    scaleFactor = 0.5f,
                    colorHex = randomColor
                )
                updatedFish.add(newBorn)
                messageBuilder.append("🐟 [Aquarium] A beautiful new generation ${newBorn.generation} $randomSpecies has hatched into the tank!\n")
            }
        }
        
        // Mature existing juvenile fish
        val finalFishList = updatedFish.map { f ->
            if (f.growthStage == "Juvenile" && !f.id.contains("born")) {
                f.copy(growthStage = "Adult", scaleFactor = 1.0f)
            } else {
                f
            }
        }

        // 3. Research completion tick
        val updatedResearch = current.research.map { res ->
            if (res.isStarted && !res.isCompleted) {
                val newSpent = res.daysSpent + 1
                val complete = newSpent >= res.daysRequired
                val progress = ((newSpent.toFloat() / res.daysRequired) * 100).toInt().coerceIn(0, 100)
                if (complete) {
                    messageBuilder.append("🔬 [Research] Project '${res.title}' is completed! ${res.rewardDescription}\n")
                }
                res.copy(
                    daysSpent = newSpent,
                    progress = progress,
                    isCompleted = complete
                )
            } else res
        }

        // If warp decor research is completed, we should unlock the Floating Prism decoration!
        val isWarpDecorResCompleted = updatedResearch.find { it.id == "res_warp_decor" }?.isCompleted == true
        val updatedDecorations = current.decorations.map { dec ->
            if (dec.id == "dec_prism" && isWarpDecorResCompleted && !dec.isUnlocked) {
                dec.copy(isUnlocked = true)
            } else dec
        }

        // 4. Decorations bonus calculation
        var decorationBonusCredits = 0
        current.decorations.forEach { dec ->
            if (dec.isPlaced) {
                if (dec.id == "dec_prism") decorationBonusCredits += 50
                else decorationBonusCredits += 30 // Flat +30 credits per placed item
            }
        }
        if (decorationBonusCredits > 0) {
            messageBuilder.append("🏺 [Decor] Placed cozy ornaments yielded +$decorationBonusCredits bonus Credits.\n")
        }

        // 5. Random Crew Activity Shift
        val crewStatusOptions = mapOf(
            "lyra" to listOf("Calibrating sniper optics", "Polishing heavy scopes", "Stargazing near the window ledge", "Relaxing with warm tea"),
            "jax" to listOf("Overclocking shield generators", "Refitting custom espresso nozzles", "Napping with combat drone", "Finetuning cybernetic elbow gears")
        )
        val updatedCompanions = current.companions.map { comp ->
            val options = crewStatusOptions[comp.id]
            if (options != null) {
                comp.copy(statusText = options.random())
            } else comp
        }

        // 6. Crew Cozy Surprise Event
        if (Math.random() < 0.40) {
            val surprises = listOf(
                "Lyra left a hand-drawn target decal on the bookshelf cupboard.",
                "Jax completed a custom copper cyber-wire weave sculpture and set it on your desk.",
                "L.I.L.A. projected a tiny warm-tinted spiral constellation above your bunk."
            )
            messageBuilder.append("💌 [Surprise] ${surprises.random()}\n")
        }

        if (messageBuilder.length < 50) {
            messageBuilder.append("The starship quarters environment remains peaceful and quiet. All life-support systems are operating nominally.")
        }

        _uiState.value = _uiState.value.copy(
            plants = updatedPlants,
            fish = finalFishList,
            research = updatedResearch,
            decorations = updatedDecorations,
            companions = updatedCompanions,
            ecosystemMessage = messageBuilder.toString()
        )

        return Pair(messageBuilder.toString(), decorationBonusCredits)
    }

    fun waterPlant(plantId: String) {
        val current = _uiState.value
        val hasAutoIrrigation = current.research.find { it.id == "res_auto_irrigation" }?.isCompleted == true
        val cost = if (hasAutoIrrigation) 0 else 10
        
        if (current.credits < cost) {
            _uiState.value = _uiState.value.copy(ecosystemMessage = "ERROR: Insufficient credits (10 required for hydration formula).")
            return
        }

        viewModelScope.launch {
            val currentProfile = repository.profile.firstOrNull() ?: ProfileEntity()
            repository.saveProfile(currentProfile.copy(credits = (currentProfile.credits - cost).coerceAtLeast(0)))
            
            val updatedPlants = current.plants.map { p ->
                if (p.id == plantId) {
                    p.copy(
                        waterLevel = (p.waterLevel + 40).coerceIn(0, 100),
                        isWateredToday = true
                    )
                } else p
            }
            _uiState.value = _uiState.value.copy(
                plants = updatedPlants,
                ecosystemMessage = "Hydroponic hydration formula injected. Water level increased (+40%)."
            )
        }
    }

    fun harvestPlant(plantId: String) {
        val current = _uiState.value
        val plant = current.plants.find { p -> p.id == plantId } ?: return
        if (!plant.harvestReady) return

        viewModelScope.launch {
            val currentProfile = repository.profile.firstOrNull() ?: ProfileEntity()
            val rewardCredits = 150
            repository.saveProfile(currentProfile.copy(credits = currentProfile.credits + rewardCredits))

            val updatedPlants = current.plants.map { p ->
                if (p.id == plantId) {
                    p.copy(
                        growthProgress = 0,
                        waterLevel = 40,
                        harvestReady = false,
                        isWateredToday = false
                    )
                } else p
            }

            _uiState.value = _uiState.value.copy(
                plants = updatedPlants,
                ecosystemMessage = "SUCCESS: Harvested ${plant.species}! Replicated ${rewardCredits} Credits and gained a temporary Greenhouse comfort buff.",
                activeBuff = "Greenhouse Cozy Aura"
            )
        }
    }

    fun feedFish(fishId: String) {
        val current = _uiState.value
        val cost = 15
        if (current.credits < cost) {
            _uiState.value = _uiState.value.copy(ecosystemMessage = "ERROR: Insufficient credits (15 required for molecular nutrients).")
            return
        }

        viewModelScope.launch {
            val currentProfile = repository.profile.firstOrNull() ?: ProfileEntity()
            repository.saveProfile(currentProfile.copy(credits = (currentProfile.credits - cost).coerceAtLeast(0)))

            val updatedFish = current.fish.map { f ->
                if (f.id == fishId) f.copy(isFedToday = true) else f
            }

            _uiState.value = _uiState.value.copy(
                fish = updatedFish,
                ecosystemMessage = "Nutrient formulas dispersed into the quantum aquarium tank. Fish are extremely active!"
            )
        }
    }

    fun purchaseFishEgg(species: String) {
        val current = _uiState.value
        val cost = 100
        if (current.credits < cost) {
            _uiState.value = _uiState.value.copy(ecosystemMessage = "ERROR: Insufficient credits (100 required for premium egg clone).")
            return
        }
        if (current.fish.size >= 10) {
            _uiState.value = _uiState.value.copy(ecosystemMessage = "ERROR: Quantum Aquarium is at maximum volume capacity (10 fish).")
            return
        }

        viewModelScope.launch {
            val currentProfile = repository.profile.firstOrNull() ?: ProfileEntity()
            repository.saveProfile(currentProfile.copy(credits = (currentProfile.credits - cost).coerceAtLeast(0)))

            val idSuffix = (100..999).random()
            val colorHex = listOf("#00FFFF", "#FF00FF", "#FFA500", "#FFD700", "#32CD32").random()
            val newFish = FishState(
                id = "fish_egg_$idSuffix",
                name = "Unnamed Hatchling",
                species = species,
                isFedToday = false,
                generation = 1,
                growthStage = "Juvenile",
                scaleFactor = 0.6f,
                colorHex = colorHex
            )

            _uiState.value = _uiState.value.copy(
                fish = current.fish + newFish,
                ecosystemMessage = "Cloned $species egg placed in the tank. It will mature after deep rest!"
            )
        }
    }

    fun startResearch(researchId: String) {
        val current = _uiState.value
        val res = current.research.find { it.id == researchId } ?: return
        if (res.isStarted || res.isCompleted) return

        if (current.credits < res.costCredits) {
            _uiState.value = _uiState.value.copy(ecosystemMessage = "ERROR: Insufficient credits to fund this research project.")
            return
        }

        val resetResearch = current.research.map { r ->
            if (r.isStarted && !r.isCompleted) {
                r.copy(isStarted = false)
            } else r
        }

        viewModelScope.launch {
            val currentProfile = repository.profile.firstOrNull() ?: ProfileEntity()
            repository.saveProfile(currentProfile.copy(credits = (currentProfile.credits - res.costCredits).coerceAtLeast(0)))

            val updatedResearch = resetResearch.map { r ->
                if (r.id == researchId) r.copy(isStarted = true) else r
            }

            _uiState.value = _uiState.value.copy(
                research = updatedResearch,
                ecosystemMessage = "RESEARCH DECK ACTIVE: Funded '${res.title}'. Progress will grow when the captain sleeps."
            )
        }
    }

    fun purchaseDecoration(decId: String) {
        val current = _uiState.value
        val dec = current.decorations.find { it.id == decId } ?: return
        if (dec.isUnlocked) return

        if (current.credits < dec.costCredits) {
            _uiState.value = _uiState.value.copy(ecosystemMessage = "ERROR: Insufficient credits to purchase this decoration.")
            return
        }

        viewModelScope.launch {
            val currentProfile = repository.profile.firstOrNull() ?: ProfileEntity()
            repository.saveProfile(currentProfile.copy(credits = (currentProfile.credits - dec.costCredits).coerceAtLeast(0)))

            val updatedDecorations = current.decorations.map { d ->
                if (d.id == decId) d.copy(isUnlocked = true) else d
            }

            _uiState.value = _uiState.value.copy(
                decorations = updatedDecorations,
                ecosystemMessage = "SUCCESS: Purchased and synthesized '${dec.name}'! You can place it in your quarters now."
            )
        }
    }

    fun toggleDecoration(decId: String) {
        val current = _uiState.value
        val dec = current.decorations.find { it.id == decId } ?: return
        if (!dec.isUnlocked) return

        val nextPlaced = !dec.isPlaced
        val updatedDecorations = current.decorations.map { d ->
            if (d.id == decId) d.copy(isPlaced = nextPlaced) else d
        }

        _uiState.value = _uiState.value.copy(
            decorations = updatedDecorations,
            ecosystemMessage = if (nextPlaced) "Placed '${dec.name}' in the quarters. Bonus Active: ${dec.atmosphericBonus}." else "Removed '${dec.name}' from the slot."
        )
    }
}
