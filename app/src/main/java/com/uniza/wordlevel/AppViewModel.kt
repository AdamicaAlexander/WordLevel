package com.uniza.wordlevel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: AppRepo
    val settingsData: LiveData<SettingsData>
    val gameData: HashMap<Int, MutableLiveData<GameData>> = HashMap()
    var levelWords: List<String> = emptyList()
    var viableWords: List<String> = emptyList()

    init {
        val appDatabase = AppDatabase.getInstance(application)
        repo = AppRepo(appDatabase.gameDao(), appDatabase.settingsDao())
        settingsData = repo.settings
        loadAllGameData()
        loadWords()
    }

    fun saveGameData(gameDataInput: GameData) {
        viewModelScope.launch {
            repo.saveGameData(gameDataInput)
            gameDataInput.id.let { id ->
                gameData[id]?.postValue(gameDataInput)
            }
        }
    }

    fun saveSettings(settings: SettingsData) {
        viewModelScope.launch {
            repo.saveSettings(settings)
        }
    }

    fun getGameData(level: Int): LiveData<GameData> {
        return gameData.getOrPut(level) { MutableLiveData() }
    }

    private fun loadAllGameData() {
        viewModelScope.launch {
            val allGameData = repo.getGameData()
            allGameData.forEach { gameDataFromList ->
                gameData[gameDataFromList.id] = MutableLiveData(gameDataFromList)
            }
        }
    }

    private fun loadWords() {
        viewModelScope.launch {
            val (loadedLevelWords, loadedViableWords) = repo.loadWords(getApplication())
            levelWords = loadedLevelWords
            viableWords = loadedViableWords
        }
    }
}
