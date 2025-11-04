package com.yash.devkagitam

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yash.devkagitam.registries.AppRegistry
import com.yash.devkagitam.screens.viewModels.AppViewModel
import com.yash.devkagitam.screens.views.CurrentPaperScreen
import com.yash.devkagitam.screens.views.InstallScreen
import com.yash.devkagitam.screens.views.Navbar
import com.yash.devkagitam.screens.views.PaperScreen
import com.yash.devkagitam.screens.views.WidgetScreen
import kotlinx.coroutines.launch

@SuppressLint("ContextCastToActivity")
@Composable
fun App(){

    val vm: AppViewModel = viewModel(LocalContext.current as ComponentActivity)

    AppRegistry.setCurrentPlugin(null,null)
    val coroutineScope = rememberCoroutineScope ()

    val pagerState = rememberPagerState(
        initialPage = vm.currentPage.value,
        pageCount = { 3 }
    )

    LaunchedEffect(pagerState.currentPage) { vm.currentPage.value = pagerState.currentPage }

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
                vm.currentPage.value = it
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
        enterTransition = { fadeIn(tween(200)) },
        exitTransition = { fadeOut(tween(200)) },
        popEnterTransition = { fadeIn(tween(200)) },
        popExitTransition = { fadeOut(tween(200)) }

    ){
        composable("App"){
            App()
        }

        composable("paper"){
            CurrentPaperScreen()
        }

    }
}