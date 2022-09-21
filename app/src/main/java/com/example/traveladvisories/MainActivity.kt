package com.example.traveladvisories

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        val navController: NavController = Navigation.findNavController(this, R.id.fragment_container_view)
        val currentDestination = navController.currentDestination
        if (currentDestination == null || !navController.popBackStack()) {
            finish()
        }
    }
}
