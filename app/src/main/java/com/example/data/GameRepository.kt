package com.example.data

import android.content.Context
import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GameRepository(context: Context) {
    private val db = AppDatabase.getDatabase(context)
    private val logDao = db.logDao()
    private val profileDao = db.profileDao()

    val allLogs: Flow<List<LogEntity>> = logDao.getAllLogs()
    val profile: Flow<ProfileEntity?> = profileDao.getProfileFlow()

    suspend fun insertLog(log: LogEntity) = withContext(Dispatchers.IO) {
        logDao.insertLog(log)
    }

    suspend fun deleteLog(id: Int) = withContext(Dispatchers.IO) {
        logDao.deleteLogById(id)
    }

    suspend fun ensureProfileExists() = withContext(Dispatchers.IO) {
        val existing = profileDao.getProfileDirect()
        if (existing == null) {
            profileDao.saveProfile(ProfileEntity())
        }
    }

    suspend fun saveProfile(profileEntity: ProfileEntity) = withContext(Dispatchers.IO) {
        profileDao.saveProfile(profileEntity)
    }

    /**
     * Calls Gemini 3.1 Pro Preview with High thinking level to answer
     * questions from the captain of the Quantum starship.
     */
    suspend fun queryShipAi(userMessage: String, systemInstruction: String): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext "AI Core Offline. Please configure your GEMINI_API_KEY in the AI Studio Secrets panel."
        }

        val request = GenerateContentRequest(
            contents = listOf(
                Content(
                    parts = listOf(
                        Part(text = userMessage)
                    )
                )
            ),
            generationConfig = GenerationConfig(
                temperature = 1.0, // Thinking models prefer higher temperature
                thinkingConfig = ThinkingConfig(thinkingLevel = "high") // REQUIRED high thinking level
            ),
            systemInstruction = Content(
                parts = listOf(
                    Part(text = systemInstruction)
                )
            )
        )

        try {
            val response = RetrofitClient.service.generateContentPro(apiKey, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: "No signal received from Core AI. State: Silent."
        } catch (e: Exception) {
            Log.e("GameRepository", "Error calling Gemini API: ${e.message}", e)
            "AI Terminal Link Malfunction: ${e.localizedMessage ?: "Connection Timeout."}"
        }
    }
}
