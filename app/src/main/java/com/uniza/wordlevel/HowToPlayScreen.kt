package com.uniza.wordlevel

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HowToPlayScreen() {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopStart
        ) {
            Text(
                text = "How to Play",
                fontSize = 30.sp,
                fontFamily = FontFamily(Font(R.font.fredokaone_regular)),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp)
            )

            Text(
                text = "Guess the Wordle in 6 tries.",
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.fredokaone_regular)),
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        top = 78.dp
                    )
            )

            Text(
                text = "• Each guess must be a valid 5-letter word.\n" +
                        "• The color of the tiles will change to show how close your guess was to the word.",
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.fredokaone_regular)),
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        top = 114.dp
                    )
            )

            Text(
                text = "Examples",
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.fredokaone_regular)),
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        top = 194.dp
                    )
            )

            // Example 1
            Box(
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        top = 230.dp
                    )
            ) {
                Grid(1)
            }
            Text(
                text = "A is in the word and in the correct spot.",
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.fredokaone_regular)),
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        top = 298.dp
                    )
            )

            // Example 2
            Box(
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        top = 334.dp
                    )
            ) {
                Grid(2)
            }
            Text(
                text = "L is in the word but in the wrong spot.",
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.fredokaone_regular)),
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        top = 402.dp
                    )
            )

            // Example 3
            Box(
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        top = 438.dp
                    )
            ) {
                Grid(3)
            }
            Text(
                text = "E is not in the word in any spot.",
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.fredokaone_regular)),
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        top = 506.dp
                    )
            )
        }
    }
}

@Composable
fun Grid(example: Int) {
    Column {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            repeat(5) { columnIndex ->
                val word = "APPLE"
                val letter = word[columnIndex]
                val backgroundColor = when {
                    letter == 'A' && example == 1  -> MaterialTheme.colorScheme.primary
                    letter == 'L' && example == 2 -> MaterialTheme.colorScheme.secondary
                    letter == 'E' && example == 3 -> Color(0xFF6E6E6E)
                    else -> MaterialTheme.colorScheme.background
                }
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(backgroundColor)
                        .border(2.dp, Color(0xFF6E6E6E)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = letter.toString(),
                        fontSize = 30.sp,
                        fontFamily = FontFamily(Font(R.font.fredokaone_regular))
                    )
                }
            }
        }
    }
}