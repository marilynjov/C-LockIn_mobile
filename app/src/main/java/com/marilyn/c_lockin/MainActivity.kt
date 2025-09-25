package com.marilyn.c_lockin

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.marilyn.c_lockin.ui.screens.*
import com.marilyn.c_lockin.ui.theme.CLockInTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CLockInTheme {
                CLockInApp()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CLockInApp() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFFFFFFF),
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("splash") {
                SplashScreen(onNavigateToLogin = {
                    navController.navigate("login")
                })
            }
            composable("login") {
                LoginScreen(
                    onNavigateToRegister = { navController.navigate("register") },
                    onNavigateToMain = { navController.navigate("main") },
                    onNavigateToEmailConfirmation = { navController.navigate("email_confirmation")}
                )
            }
            composable("register") {
                RegisterScreen(
                    onNavigateToLogin = { navController.navigate("login") },
                    onNavigateToEmailConfirmation = { navController.navigate("email_confirmation") }
                )
            }
            composable("email_confirmation") {
                EmailConfirmationScreen(
                    onNavigateToLogin = { navController.navigate("login") },
                    onNavigateToRegister = { navController.navigate("register")}
                )
            }
            composable("main") {
                MainScreen(
                    onNavigateToCreateAlarm = { navController.navigate("create_alarm") },
                    onNavigateToSettings = { navController.navigate("profile") },
                    onNavigateToNotifications = { navController.navigate("notifications") }
                )
            }
            composable("create_alarm") {
                CreateAlarmScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable("profile") {
                EditAccountScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onSaveAccount = { _, _, _, _ -> navController.popBackStack() },
                    onLogout = { navController.navigate("login")}
                )
            }

            composable("notifications") {
                NotificationsScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}