package com.intern.calculator.goods

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.intern.calculator.goods.ui.navigation.GoodsNavHost

/**
 * Top level composable that represents screens for the application.
 */
@Composable
fun GoodsApp(navController: NavHostController = rememberNavController()) {
    GoodsNavHost(navController = navController)
}