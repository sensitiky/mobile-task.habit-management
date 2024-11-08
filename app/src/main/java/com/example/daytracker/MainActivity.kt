package com.example.daytracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.daytracker.ui.screens.AppScreen
import com.example.daytracker.ui.theme.DayTrackerTheme
import com.google.android.gms.ads.MobileAds

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //r isAppReady = false
        //lashScreen.setKeepOnScreenCondition { !isAppReady}
        MobileAds.initialize(this) {}
        setContent {
            DayTrackerTheme {
                AppScreen()
            }
        }
    }
}

