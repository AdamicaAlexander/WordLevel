package com.uniza.wordlevel

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GameDao {
    @Query("SELECT * FROM gamedata")
    suspend fun getGameData(): List<GameData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameData(gameData: GameData)
}

@Dao
interface SettingsDao {
    @Query("SELECT * FROM settings WHERE id = 0")
    fun getSettings(): LiveData<SettingsData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSettings(settings: SettingsData)
}
