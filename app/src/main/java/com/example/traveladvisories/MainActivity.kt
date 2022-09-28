package com.example.traveladvisories

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TravelAdvisoriesApp()
        }
    }
}

@Composable
fun TravelAdvisoriesApp() {
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) })
    }) {
        TravelAdvisoriesNavHost()
    }
}
