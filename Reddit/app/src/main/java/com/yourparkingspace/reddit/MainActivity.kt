package com.yourparkingspace.reddit

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewTreeLifecycleOwner
import com.yourparkingspace.reddit.ui.nav.MainScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewTreeLifecycleOwner.set(window.decorView, this)
        setContent {
          //  window.statusBarColor = MaterialTheme.colors.background.toArgb()
           // window.navigationBarColor = MaterialTheme.colors.background.toArgb()
            MainScreen()
        }
    }
}