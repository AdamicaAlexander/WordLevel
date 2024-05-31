package com.uniza.wordlevel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.io.BufferedReader
import java.io.InputStreamReader

class ViewModels(context: Context) {
    val settingsViewModel = SettingsViewModel()
    val levelWords = loadWordsFromAssets(context, "level_words.txt")
    val viableWords = loadWordsFromAssets(context, "viable_words.txt")
}

class SettingsViewModel : ViewModel() {
    val isDarkModeEnabled = mutableStateOf(false)
}

private fun loadWordsFromAssets(context: Context, fileName: String): List<String> {
    val inputStream = context.assets.open(fileName)
    val reader = BufferedReader(InputStreamReader(inputStream))
    return reader.readLines()
}