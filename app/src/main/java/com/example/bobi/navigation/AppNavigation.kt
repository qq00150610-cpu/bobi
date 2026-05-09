package com.example.bobi.navigation

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bobi.ui.screens.*
import com.example.bobi.viewmodel.MainViewModel

sealed class Screen(val route: String, val label: String) {
    object Chat : Screen("chat", "对话")
    object Brain : Screen("brain", "脑图")
    object Memories : Screen("memories", "记忆")
    object Status : Screen("status", "状态")
    object Settings : Screen("settings", "设置")
}

@Composable
fun AppNavigation(viewModel: MainViewModel = viewModel()) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Chat.route) {
        composable(Screen.Chat.route) {
            ChatScreen(
                viewModel = viewModel,
                onNavigateToBrain = { navController.navigate(Screen.Brain.route) },
                onNavigateToMemories = { navController.navigate(Screen.Memories.route) },
                onNavigateToStatus = { navController.navigate(Screen.Status.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
            )
        }

        composable(Screen.Brain.route) {
            BrainScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Memories.route) {
            MemoriesScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Status.route) {
            StatusScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
