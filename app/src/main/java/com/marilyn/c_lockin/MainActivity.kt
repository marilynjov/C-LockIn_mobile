package com.marilyn.c_lockin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.marilyn.c_lockin.ui.screens.*
import com.marilyn.c_lockin.ui.theme.CLockInTheme

class MainActivity : ComponentActivity() {
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

@Composable
fun CLockInApp() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize()
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
                    onNavigateToForgotPassword = { navController.navigate("forgot_password") }
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
                    onNavigateToLogin = { navController.navigate("login") }
                )
            }
            composable("main") {
                MainScreen(
                    onNavigateToCreateAlarm = { navController.navigate("create_alarm") },
                    onNavigateToSettings = { navController.navigate("settings") }
                )
            }
            composable("create_alarm") {
                CreateAlarmScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable("settings") {
                SettingsScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToEditAccount = { navController.navigate("edit_account") },
                    onNavigateToNotifications = { navController.navigate("notifications") }
                )
            }
            composable("edit_account") {
                EditAccountScreen(
                    onNavigateBack = { navController.popBackStack() }
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