package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "captain_profile")
data class ProfileEntity(
    @PrimaryKey val id: Int = 1,
    val captainName: String = "Kenmong",
    val shipName: String = "Aetherius-VII",
    val credits: Int = 1200,
    val reputationEnlighteners: Int = 50,
    val reputationTechnopunks: Int = 50,
    val activeBuff: String = "None",
    val activeBuffExpiry: Long = 0,
    val calendarDay: Int = 1,
    val selectedWeather: String = "Clear Stellar Lanes"
)
