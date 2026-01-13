package com.aitextcleaner.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aitextcleaner.ui.screens.HomeScreen
import com.aitextcleaner.ui.screens.ResultScreen
import com.aitextcleaner.ui.screens.SettingsScreen
import com.aitextcleaner.viewmodel.SettingsViewModel
import com.aitextcleaner.viewmodel.TextCleanerViewModel

sealed class AppDestination(val route: String) {
    data object Home : AppDestination("home")
    data object Result : AppDestination("result")
    data object Settings : AppDestination("settings")
}

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val textCleanerViewModel: TextCleanerViewModel = viewModel()
    val settingsViewModel: SettingsViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = AppDestination.Home.route,
        modifier = modifier
    ) {
        composable(AppDestination.Home.route) {
            HomeScreen(
                viewModel = textCleanerViewModel,
                onNavigateToResult = { navController.navigate(AppDestination.Result.route) },
                onNavigateToSettings = { navController.navigate(AppDestination.Settings.route) }
            )
        }
        composable(AppDestination.Result.route) {
            ResultScreen(
                viewModel = textCleanerViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(AppDestination.Settings.route) {
            SettingsScreen(
                viewModel = settingsViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
