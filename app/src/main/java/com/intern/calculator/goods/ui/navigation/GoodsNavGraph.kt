package com.intern.calculator.goods.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.intern.calculator.goods.R
import com.intern.calculator.goods.ui.home.HomeDestination
import com.intern.calculator.goods.ui.home.HomeScreen
import com.intern.calculator.goods.ui.item.details.ItemDetailsDestination
import com.intern.calculator.goods.ui.item.details.ItemDetailsScreen
import com.intern.calculator.goods.ui.item.edit.ItemEditDestination
import com.intern.calculator.goods.ui.item.edit.ItemEditScreen
import com.intern.calculator.goods.ui.item.entry.ItemEntryDestination
import com.intern.calculator.goods.ui.item.entry.ItemEntryScreen
import com.intern.calculator.goods.ui.settings.AboutDestination
import com.intern.calculator.goods.ui.settings.AboutScreen
import com.intern.calculator.goods.ui.settings.SettingsDestination
import com.intern.calculator.goods.ui.settings.SettingsScreen

// Composable function to define the navigation host for the app
@Composable
fun GoodsNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    // Define the navigation graph using NavHost composable
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        // Define individual destinations with associated composable screens
        composable(
            route = HomeDestination.route,
        ) {
            HomeScreen(
                navigateToItemEntry = {
                    // Navigate to ItemEntryDestination with itemId as argument
                    navController.navigate("${ItemEntryDestination.route}/${it}")
                },
                navigateToItemUpdate = {
                    // Navigate to ItemDetailsDestination with itemId as argument
                    navController.navigate("${ItemDetailsDestination.route}/${it}")
                },
                navigateToSettings = {
                    // Navigate to SettingsDestination
                    navController.navigate(SettingsDestination.route)
                },
            )
        }
        // ItemEntryDestination with itemId as argument
        composable(
            route = ItemEntryDestination.routeWithArgs,
            arguments = listOf(navArgument(ItemEntryDestination.itemIdArg) {
                type = NavType.IntType // Define argument type
            })
        ) {
            ItemEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
                receivedVariable = it.arguments?.getInt("itemId") ?: 1,
                buttonText = R.string.item_entry_save_button_text,
            )
        }
        // ItemDetailsDestination with itemId as argument
        composable(
            route = ItemDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(ItemDetailsDestination.itemIdArg) {
                type = NavType.IntType // Define argument type
            })
        ) {
            ItemDetailsScreen(
                navigateToEditItem = { itemId ->
                    navController.navigate("${ItemEditDestination.route}/$itemId")
                },
                navigateBack = { navController.navigateUp() },
            )
        }
        // ItemEditDestination with itemId as argument
        composable(
            route = ItemEditDestination.routeWithArgs,
            arguments = listOf(navArgument(ItemEditDestination.itemIdArg) {
                type = NavType.IntType // Define argument type
            })
        ) {
            ItemEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
            )
        }
        // SettingsDestination
        composable(route = SettingsDestination.route) {
            SettingsScreen(
                navigateUp = { navController.navigateUp() },
                navigateToAbout = { navController.navigate(AboutDestination.route) },
            )
        }
        // AboutDestination
        composable(route = AboutDestination.route) {
            AboutScreen(
                navigateUp = { navController.navigateUp() },
            )
        }
    }
}
