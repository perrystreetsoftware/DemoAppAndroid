package com.example.traveladvisories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.domainmodels.CountryDetails
import com.example.uicomponents.countrydetails.CountryDetailsPage
import com.example.uicomponents.countrydetails.CountryDetailsUIState
import com.example.viewmodels.CountryDetailsViewModel
import org.koin.android.ext.android.get
import org.koin.core.parameter.parametersOf

class CountryDetailsFragment : Fragment() {
    private val args: CountryDetailsFragmentArgs by navArgs()

    private val viewModel: CountryDetailsViewModel by lazy {
        get { parametersOf(args.country) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                val viewModelState by viewModel.state.subscribeAsState(CountryDetailsViewModel.State.Initial)
                val details by viewModel.details.subscribeAsState(initial = CountryDetails.EMPTY)

                CountryDetailsPage(
                    detailsUIState = CountryDetailsUIState(
                        details = details,
                        viewModelState = viewModelState
                    )
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onPageLoaded()
    }
}
