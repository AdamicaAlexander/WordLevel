package com.uniza.wordlevel

import android.content.pm.ActivityInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun GameScreen(viewModel: AppViewModel, navController: NavController, level: Int) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    val gameData by viewModel.getGameData(level).observeAsState(GameData(level))
    val currentLevelWord = viewModel.levelWords[level - 1]
    val currentLevelGuesses = mutableListOf(
        gameData.guess1,
        gameData.guess2,
        gameData.guess3,
        gameData.guess4,
        gameData.guess5,
        gameData.guess6
    )

    val rows = 6
    val columns = 5
    var currentRow = 0
    var currentColumn = 0
    var gameEnd = false
    val gameWin = remember { mutableStateOf(false) }

    val gridState =
        remember { mutableStateListOf(*Array(rows) { mutableStateListOf(*Array(columns) { "" }) }) }

    gridState.forEachIndexed { rowIndex, row ->
        if (currentLevelGuesses[rowIndex].isEmpty()) {
            return@forEachIndexed
        }
        row.forEachIndexed { columnIndex, column ->
            gridState[rowIndex][columnIndex] = currentLevelGuesses[rowIndex][columnIndex].toString()
            currentColumn++
        }
        if (currentRow < rows - 1 && currentLevelGuesses[rowIndex] != currentLevelWord) {
            currentRow++
            currentColumn = 0
        }
    }

    val updateDummy = remember { mutableStateOf(false) }

    val showWrongEntryPopup = remember { mutableStateOf(false) }
    val showGameFinishedDialog = remember { mutableStateOf(false) }

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            // Title
            Text(
                text = "WordLevel",
                fontSize = 30.sp,
                fontFamily = FontFamily(Font(R.font.fredokaone_regular)),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 22.dp)
            )

            // Settings Button
            IconButton(
                onClick = { navController.navigate("settings") },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            ) {
                Icon(
                    painterResource(id = R.drawable.settings_final),
                    contentDescription = "Settings"
                )
            }

            // How to Play Button
            IconButton(
                onClick = { navController.navigate("howToPlay") },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    painterResource(id = R.drawable.questionmark_final),
                    contentDescription = "How to play"
                )
            }

            // Grid
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 80.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Grid(
                    rows,
                    columns,
                    gridState,
                    currentLevelGuesses,
                    currentLevelWord,
                    updateDummy.value
                )
            }

            // Custom Keyboard
            CustomKeyboard(
                onKeyPress = { key ->
                    if (currentColumn < columns) {
                        gridState[currentRow][currentColumn] = key
                        currentColumn++
                    }
                },
                onBackspace = {
                    if (currentColumn > 0 && !gameEnd) {
                        currentColumn--
                        gridState[currentRow][currentColumn] = ""
                    }
                },
                onEnter = {
                    val word = gridState[currentRow].joinToString("").lowercase()
                    if (viewModel.viableWords.contains(word)) {
                        currentLevelGuesses[currentRow] = word
                        val updatedGameData = when (currentRow) {
                            0 -> gameData.copy(guess1 = word)
                            1 -> gameData.copy(guess2 = word)
                            2 -> gameData.copy(guess3 = word)
                            3 -> gameData.copy(guess4 = word)
                            4 -> gameData.copy(guess5 = word)
                            5 -> gameData.copy(guess6 = word)
                            else -> gameData
                        }
                        viewModel.saveGameData(updatedGameData)

                        if (currentRow < rows - 1 && word != currentLevelWord) {
                            currentRow++
                            currentColumn = 0
                        } else {
                            gameEnd = true
                            if (word == currentLevelWord.lowercase()) {
                                gameWin.value = true
                            }
                            showGameFinishedDialog.value = true
                        }
                        updateDummy.value = !updateDummy.value
                    } else {
                        showWrongEntryPopup.value = true
                    }
                }
            )
        }
    }
    WrongEntryPopup(showWrongEntryPopup)
    GameFinishedDialog(showGameFinishedDialog, level, gameWin.value)
}

@Composable
fun Grid(
    rows: Int,
    columns: Int,
    gridState: List<MutableList<String>>,
    currentLevelGuesses: List<String>,
    currentLevelWord: String,
    updateDummy: Boolean
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        repeat(rows) { rowIndex ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                repeat(columns) { columnIndex ->
                    val letter = gridState[rowIndex][columnIndex].lowercase()
                    var backgroundColor = MaterialTheme.colorScheme.background
                    if (currentLevelGuesses[rowIndex].isNotEmpty()) {
                        backgroundColor = when {
                            letter == currentLevelWord[columnIndex].toString() -> MaterialTheme.colorScheme.primary
                            currentLevelWord.contains(letter) -> MaterialTheme.colorScheme.secondary
                            else -> Color(0xFF6E6E6E)
                        }
                    }
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(backgroundColor) // Set the background color
                            .border(2.dp, Color(0xFF6E6E6E)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = letter.uppercase(), // Set the text
                            fontSize = 30.sp,
                            fontFamily = FontFamily(Font(R.font.fredokaone_regular))
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CustomKeyboard(
    onKeyPress: (String) -> Unit,
    onBackspace: () -> Unit,
    onEnter: () -> Unit
) {
    val rows = listOf(
        "QWERTYUIOP",
        "ASDFGHJKL",
        "ZXCVBNM"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        rows.forEachIndexed { index, row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                if (index == rows.lastIndex) {
                    Button(
                        onClick = { onEnter() },
                        modifier = Modifier.size(50.dp, 50.dp),
                        shape = RoundedCornerShape(5.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF8C8C8C),
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            painterResource(id = R.drawable.enter_final),
                            contentDescription = "Enter",
                            modifier = Modifier.size(30.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                }

                row.forEach { key ->
                    Button(
                        onClick = { onKeyPress(key.toString()) },
                        modifier = Modifier.size(35.dp, 50.dp),
                        shape = RoundedCornerShape(5.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF8C8C8C),
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = key.toString(),
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.fredokaone_regular))
                        )
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                }

                if (index == rows.lastIndex) {
                    Button(
                        onClick = { onBackspace() },
                        modifier = Modifier.size(50.dp, 50.dp),
                        shape = RoundedCornerShape(5.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF8C8C8C),
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            painterResource(id = R.drawable.backspace_final),
                            contentDescription = "Backspace",
                            modifier = Modifier.size(30.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}

@Composable
fun GameFinishedDialog(showDialog: MutableState<Boolean>, level: Int, isWin: Boolean) {
    if (showDialog.value) {
        val text =
            if (isWin) "Congratulations!\nLevel $level is a WIN" else "Dang!\nLevel $level is a LOSS\nBetter luck next time."
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = {
                Text(
                    text = "Game Finished",
                    fontSize = 30.sp,
                    fontFamily = FontFamily(Font(R.font.fredokaone_regular))
                )
            },
            text = {
                Text(
                    text = text,
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.fredokaone_regular))
                )
            },
            confirmButton = {
                Button(
                    onClick = { showDialog.value = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "OK",
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.fredokaone_regular))
                    )
                }
            }
        )
    }

}

@Composable
fun WrongEntryPopup(showPopup: MutableState<Boolean>) {
    if (showPopup.value) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(150.dp, 75.dp)
                    .background(Color(0xFF8C8C8C), RoundedCornerShape(15.dp))
            ) {
                Text(
                    text = "Wrong entry",
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.fredokaone_regular)),
                    color = Color.White
                )
            }
            LaunchedEffect(key1 = showPopup.value) {
                delay(1000L)
                showPopup.value = false
            }
        }
    }
}
