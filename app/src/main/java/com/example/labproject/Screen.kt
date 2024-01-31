package com.example.labproject

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object List : Screen("list")
    object Detail : Screen("detail")
    object Swipe : Screen("swipe")
}
