package com.example.traveladvisories

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.compose.ui.platform.ComposeView
import com.example.uicomponents.countryselecting.CountrySelectingAdapter

import com.example.uicomponents.countryselecting.CountrySelectingPage
import com.example.viewmodels.CountrySelectingViewModel
import org.koin.java.KoinJavaComponent.inject

class MainActivity : AppCompatActivity() {
    private val viewModel: CountrySelectingViewModel by inject(CountrySelectingViewModel::class.java)
    private var didAppearOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val greeting = findViewById<ComposeView>(R.id.greeting)
        greeting.setContent {
            CountrySelectingAdapter(viewModel = viewModel).Content()
        }
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        if (didAppearOnce == false) {
            didAppearOnce = true
            viewModel.onPageLoaded()
        }
        return super.onCreateView(name, context, attrs)
    }
}