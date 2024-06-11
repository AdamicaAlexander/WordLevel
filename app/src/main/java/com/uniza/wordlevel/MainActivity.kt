package com.uniza.wordlevel

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.uniza.wordlevel.ui.theme.WordLevelTheme

class MainActivity : ComponentActivity() {
    private val viewModel: AppViewModel by viewModels()
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted -> }
    private val notificationHandler by lazy { NotificationHandler(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WordLevelTheme(viewModel.settingsData.observeAsState().value?.darkModeEnabled ?: false) {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "menu"
                ) {
                    composable("menu") {
                        MenuScreen(
                            onPlayClicked = { navController.navigate("levels") },
                            onSettingsClicked = { navController.navigate("settings") },
                            onHowToPlayClicked = { navController.navigate("howToPlay") }
                        )
                    }
                    composable("levels") {
                        LevelsScreen(navController)
                    }
                    composable(
                        "game/{level}",
                        arguments = listOf(navArgument("level") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val level = backStackEntry.arguments?.getInt("level")
                        GameScreen(viewModel, navController, level!!)
                    }
                    composable("settings") {
                        SettingsScreen(viewModel, notificationHandler)
                    }
                    composable("howToPlay") {
                        HowToPlayScreen()
                    }
                }
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationHandler.requestNotificationPermission(requestPermissionLauncher)
        }
    }
}

@Composable
fun MenuScreen(
    onPlayClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
    onHowToPlayClicked: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.wordlevel_final),
                contentDescription = "Logo",
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = (-150).dp),
                contentScale = ContentScale.Fit
            )

            // Play Button
            Button(
                onClick = { onPlayClicked() },
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = 200.dp)
                    .size(width = 300.dp, height = 100.dp)
                    .clip(RoundedCornerShape(20.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = Color.White
                ),
            ) {
                Text(
                    text = "Play",
                    fontFamily = FontFamily(Font(R.font.fredokaone_regular)),
                    fontSize = 28.sp
                )
            }

            // Settings Button
            IconButton(
                onClick = { onSettingsClicked() },
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
                onClick = { onHowToPlayClicked() },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    painterResource(id = R.drawable.questionmark_final),
                    contentDescription = "How to play"
                )
            }
        }
    }
}
