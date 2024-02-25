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
import com.intern.calculator.goods.ui.item.Details.ItemDetailsDestination
import com.intern.calculator.goods.ui.item.Details.ItemDetailsScreen
import com.intern.calculator.goods.ui.item.Edit.ItemEditDestination
import com.intern.calculator.goods.ui.item.Edit.ItemEditScreen
import com.intern.calculator.goods.ui.item.Entry.ItemEntryDestination
import com.intern.calculator.goods.ui.item.Entry.ItemEntryScreen
import com.intern.calculator.goods.ui.settings.AboutDestination
import com.intern.calculator.goods.ui.settings.AboutScreen
import com.intern.calculator.goods.ui.settings.SettingsDestination
import com.intern.calculator.goods.ui.settings.SettingsScreen

@Composable
fun GoodsNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController, startDestination = HomeDestination.route, modifier = modifier
    ) {
        composable(
            route = HomeDestination.route,
        ) {
            HomeScreen(
                navigateToItemEntry = {
                    navController.navigate("${ItemEntryDestination.route}/${it}")
                },
                navigateToItemUpdate = {
                    navController.navigate("${ItemDetailsDestination.route}/${it}")
                },
                navigateToSettings = { navController.navigate(SettingsDestination.route) },
            )
        }

        composable(
            route = ItemEntryDestination.routeWithArgs,
            arguments = listOf(navArgument(ItemEntryDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            ItemEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
                receivedVariable = it.arguments?.getInt("itemId") ?: 1,
                buttonText = R.string.item_entry_save_button_text,
            )
        }
        composable(
            route = ItemDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(ItemDetailsDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) { navBackStackEntry ->
            ItemDetailsScreen(
                navigateToEditItem = { itemId ->
                    navController.navigate("${ItemEditDestination.route}/$itemId")
                },
                navigateBack = { navController.navigateUp() },
            )
        }
        composable(
            route = ItemEditDestination.routeWithArgs,
            arguments = listOf(navArgument(ItemEditDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) { navBackStackEntry ->
            ItemEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
            )
        }
        composable(route = SettingsDestination.route) {
            SettingsScreen(
                navigateUp = { navController.navigateUp() },
                navigateToAbout = { navController.navigate(AboutDestination.route) },
            )
        }
        composable(route = AboutDestination.route) {
            AboutScreen(
                navigateUp = { navController.navigateUp() },
            )
        }
    }
}
