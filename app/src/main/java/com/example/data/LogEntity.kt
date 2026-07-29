package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "captains_logs")
data class LogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val stardate: String,
    val realTime: String,
    val text: String,
    val mood: String,
    val aiReflection: String,
    val timestamp: Long = System.currentTimeMillis()
)
