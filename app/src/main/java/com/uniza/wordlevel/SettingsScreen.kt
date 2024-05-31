package com.uniza.wordlevel

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsScreen(viewmodels: ViewModels) {
    val areNotificationsEnabled = remember { mutableStateOf(false) }

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Settings",
                    fontSize = 30.sp,
                    fontFamily = FontFamily(Font(R.font.fredokaone_regular))
                )
            }

            CheckboxWithLabel(
                label = "Dark Mode",
                checkedState = viewmodels.settingsViewModel.isDarkModeEnabled,
                modifier = Modifier.padding(top = 10.dp)
            )

            CheckboxWithLabel(
                label = "Notifications",
                checkedState = areNotificationsEnabled
            )
        }
    }
}

@Composable
fun CheckboxWithLabel(label: String, checkedState: androidx.compose.runtime.MutableState<Boolean>, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Checkbox(
            checked = checkedState.value,
            onCheckedChange = { checkedState.value = it },
            modifier = Modifier.scale(1.25f)
        )
        Text(
            text = label,
            fontSize = 18.sp,
            fontFamily = FontFamily(Font(R.font.fredokaone_regular)),
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}