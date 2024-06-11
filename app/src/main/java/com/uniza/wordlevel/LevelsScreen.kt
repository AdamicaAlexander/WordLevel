package com.uniza.wordlevel

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.navigation.NavController

@Composable
fun LevelsScreen(navController: NavController) {
    val levels = (1..50).toList().chunked(5)
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Levels",
                    fontSize = 30.sp,
                    fontFamily = FontFamily(Font(R.font.fredokaone_regular)),
                    modifier = Modifier.padding(16.dp)
                )
                levels.forEach { rowLevels ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        rowLevels.forEach { level ->
                            val buttonColor = when (level) {
                                in 1..10 -> Color(0xFF008005)
                                in 11..20 -> Color(0xFF7BFF00)
                                in 21..30 -> Color(0xFFFFEB3B)
                                in 31..40 -> Color(0xFFFF1100)
                                in 41..50 -> Color(0xFF800900)
                                else -> Color.Black
                            }
                            Button(
                                onClick = { navController.navigate("game/$level") },
                                modifier = Modifier
                                    .size(65.dp)
                                    .border(3.dp, buttonColor, RoundedCornerShape(15.dp)),
                                shape = RoundedCornerShape(15.dp),
                                contentPadding = PaddingValues(0.dp),
                                colors = ButtonDefaults.buttonColors(
                                    contentColor = buttonColor,
                                    containerColor = MaterialTheme.colorScheme.background
                                )
                            ) {
                                Text(
                                    text = level.toString(),
                                    fontSize = 20.sp,
                                    fontFamily = FontFamily(Font(R.font.fredokaone_regular)),
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
