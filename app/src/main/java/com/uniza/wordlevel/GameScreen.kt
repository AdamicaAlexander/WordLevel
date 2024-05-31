package com.uniza.wordlevel

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun GameScreen(viewmodels: ViewModels, navController: NavController, level: Int?) {
    val levelWords = viewmodels.levelWords
    val thisLevelWord = levelWords[level!! - 1]
    val viableWords = viewmodels.viableWords

    val rows = 6
    val columns = 5
    val gridState = remember { mutableStateListOf(*Array(rows) { mutableStateListOf(*Array(columns) { "" }) }) }
    val currentColumn = remember { mutableStateOf(0) }
    val currentRow = remember { mutableStateOf(0) }
    val settledRows = remember { mutableStateListOf<Boolean>().apply { addAll(List(rows) { false }) } }

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
                modifier = Modifier.align(Alignment.TopCenter).padding(top = 22.dp)
            )

            // Settings Button
            IconButton(
                onClick = { navController.navigate("settings") },
                modifier = Modifier.align(Alignment.TopStart).padding(16.dp)
            ) {
                Icon(
                    painterResource(id = R.drawable.settings_final),
                    contentDescription = "Settings"
                )
            }

            // How to Play Button
            IconButton(
                onClick = { navController.navigate("howToPlay") },
                modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
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
                Grid(rows, columns, gridState, settledRows, thisLevelWord)
            }

            // Custom Keyboard
            CustomKeyboard(
                onKeyPress = { key ->
                    if (currentColumn.value < columns) {
                        gridState[currentRow.value][currentColumn.value] = key
                        currentColumn.value++
                    }
                },
                onBackspace = {
                    if (currentColumn.value > 0 && !settledRows[rows - 1]) {
                        currentColumn.value--
                        gridState[currentRow.value][currentColumn.value] = ""
                    }
                },
                onEnter = {
                    val word = gridState[currentRow.value].joinToString("").lowercase()
                    if (viableWords.contains(word)) {
                        settledRows[currentRow.value] = true
                        if (currentRow.value < rows - 1) {
                            currentRow.value++
                            currentColumn.value = 0
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun Grid(
    rows: Int,
    columns: Int,
    gridState: List<MutableList<String>>,
    settledRows: List<Boolean>,
    thisLevelWord: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        repeat(rows) { rowIndex ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                repeat(columns) { columnIndex ->
                    val letter = gridState[rowIndex][columnIndex].lowercase()
                    var backgroundColor = MaterialTheme.colorScheme.background
                    if (settledRows[rowIndex]) {
                        backgroundColor = when {
                            letter == thisLevelWord[columnIndex].toString() -> MaterialTheme.colorScheme.primary
                            thisLevelWord.contains(letter) -> MaterialTheme.colorScheme.secondary
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
                        modifier = Modifier
                            .size(50.dp, 50.dp),
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
                        modifier = Modifier
                            .size(35.dp, 50.dp),
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
                        modifier = Modifier
                            .size(50.dp, 50.dp),
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
