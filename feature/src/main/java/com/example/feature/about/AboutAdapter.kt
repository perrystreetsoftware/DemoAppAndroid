package com.example.feature.about

import androidx.compose.runtime.Composable
import com.example.viewmodels.AboutViewModel
import org.koin.androidx.compose.getViewModel


@Composable
fun AboutAdapter(
    viewModel: AboutViewModel = getViewModel(),
) {
    AboutPage()
}