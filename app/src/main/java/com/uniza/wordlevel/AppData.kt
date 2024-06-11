package com.uniza.wordlevel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gamedata")
data class GameData(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val guess1: String = "",
    val guess2: String = "",
    val guess3: String = "",
    val guess4: String = "",
    val guess5: String = "",
    val guess6: String = ""
)

@Entity(tableName = "settings")
data class SettingsData(
    @PrimaryKey
    val id: Int = 0,
    val darkModeEnabled: Boolean = false,
    val notificationsEnabled: Boolean = false
)
