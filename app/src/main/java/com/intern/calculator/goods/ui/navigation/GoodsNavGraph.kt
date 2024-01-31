package com.intern.calculator.goods.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.intern.calculator.goods.ui.home.HomeDestination
import com.intern.calculator.goods.ui.home.HomeScreen
import com.intern.calculator.goods.ui.item.ItemDetailsDestination
import com.intern.calculator.goods.ui.item.ItemDetailsScreen
import com.intern.calculator.goods.ui.item.ItemEditDestination
import com.intern.calculator.goods.ui.item.ItemEditScreen
import com.intern.calculator.goods.ui.item.ItemEntryDestination
import com.intern.calculator.goods.ui.item.ItemEntryScreen

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
            HomeScreen(navigateToItemEntry = { navController.navigate("${ItemEntryDestination.route}/${it}") },
                navigateToItemUpdate = {
                    navController.navigate("${ItemDetailsDestination.route}/${it}")
                })
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
                receivedVariable = it.arguments?.getInt("itemId") ?: 1
            )
        }
        composable(
            route = ItemDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(ItemDetailsDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            ItemDetailsScreen(
                navigateToEditItem =
                {
                    navController.navigate("${ItemEditDestination.route}/$it")
                },
                navigateBack = { navController.navigateUp() })
        }
        composable(
            route = ItemEditDestination.routeWithArgs,
            arguments = listOf(navArgument(ItemEditDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            ItemEditScreen(navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() })
        }
    }
}