package com.uniza.wordlevel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.io.BufferedReader
import java.io.InputStreamReader

class ViewModels(context: Context) : ViewModel() {
    val isDarkModeEnabled = mutableStateOf(false)
    val areNotificationsEnabled = mutableStateOf(false)
    val guesses = MutableList(50) { Guesses() }
    val levelWords = loadWordsFromAssets(context, "level_words.txt")
    val viableWords = loadWordsFromAssets(context, "viable_words.txt")
}

private fun loadWordsFromAssets(context: Context, fileName: String): List<String> {
    val inputStream = context.assets.open(fileName)
    val reader = BufferedReader(InputStreamReader(inputStream))
    return reader.readLines()
}

class Guesses(
    private var guess1: String = "",
    private var guess2: String = "",
    private var guess3: String = "",
    private var guess4: String = "",
    private var guess5: String = "",
    private var guess6: String = "",
) {
    fun getGuess(row: Int): String {
        return when (row) {
            0 -> guess1
            1 -> guess2
            2 -> guess3
            3 -> guess4
            4 -> guess5
            5 -> guess6
            else -> ""
        }
    }
    fun setGuess(row: Int, guess: String) {
        when (row) {
            0 -> guess1 = guess
            1 -> guess2 = guess
            2 -> guess3 = guess
            3 -> guess4 = guess
            4 -> guess5 = guess
            5 -> guess6 = guess
        }
    }
    fun isEmpty(row: Int): Boolean {
        return getGuess(row).isEmpty()
    }
}