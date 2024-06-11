package com.uniza.wordlevel

import android.app.Application
import androidx.lifecycle.LiveData
import java.io.BufferedReader
import java.io.InputStreamReader

class AppRepo(private val gameDao: GameDao, private val settingsDao: SettingsDao) {
    val settings: LiveData<SettingsData> = settingsDao.getSettings()

    suspend fun saveGameData(gameData: GameData) {
        gameDao.insertGameData(gameData)
    }

    suspend fun saveSettings(settings: SettingsData) {
        settingsDao.insertSettings(settings)
    }

    suspend fun getGameData(): List<GameData> {
        return gameDao.getGameData()
    }

    fun loadWords(application: Application): Pair<List<String>, List<String>> {
        val levelWords = loadWordsFromAssets(application, "level_words.txt")
        val viableWords = loadWordsFromAssets(application, "viable_words.txt")
        return Pair(levelWords, viableWords)
    }

    private fun loadWordsFromAssets(application: Application, fileName: String): List<String> {
        val inputStream = application.assets.open(fileName)
        val reader = BufferedReader(InputStreamReader(inputStream))
        return reader.readLines()
    }
}
