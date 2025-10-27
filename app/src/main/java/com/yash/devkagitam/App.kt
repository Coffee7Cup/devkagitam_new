package com.yash.devkagitam

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yash.devkagitam.registries.AppRegistry
import com.yash.devkagitam.screens.views.CurrentPaperScreen
import com.yash.devkagitam.screens.views.InstallScreen
import com.yash.devkagitam.screens.views.Navbar
import com.yash.devkagitam.screens.views.PaperScreen
import com.yash.devkagitam.screens.views.WidgetScreen
import kotlinx.coroutines.launch

@Composable
fun App(
    curr : Int = 0
){

    AppRegistry.setCurrentPlugin(null,null)
    val coroutineScope = rememberCoroutineScope ()

    val pagerState = rememberPagerState(
        initialPage = curr,
        pageCount = { 3 }
    )

    Box(Modifier.fillMaxSize()){
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = true
        ) { page ->
            when(page){
                0 -> WidgetScreen()
                1 -> PaperScreen()
                2 -> InstallScreen()
            }
        }
    }

    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ){
        Navbar(pagerState.currentPage) {
            coroutineScope.launch {
                pagerState.animateScrollToPage(it)

            }
        }
    }
}

@Composable
fun NavigationMain(){

    val navController = rememberNavController()

    LaunchedEffect(Unit){
        AppRegistry.setNavController(navController)
    }

    NavHost(
        navController = navController,
        startDestination = "App",
    ){
        composable("App"){
            App()
        }

        composable("plugin"){
            CurrentPaperScreen()
        }

    }
}