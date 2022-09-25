package com.example.traveladvisories

import android.R
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.extensions.asUIError
import com.example.libs.UIError
import com.example.uicomponents.countryselecting.CountrySelectingPage
import com.example.uicomponents.countryselecting.CountrySelectingUIState
import com.example.utils.filterIsInstance
import com.example.viewmodels.CountrySelectingViewModel
import com.example.viewmodels.CountrySelectingViewModel.Event.Navigate
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.java.KoinJavaComponent


class CountrySelectingFragment : Fragment() {
    private val disposables = CompositeDisposable()
    private val viewModel: CountrySelectingViewModel by KoinJavaComponent.inject(CountrySelectingViewModel::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val state by viewModel.state.subscribeAsState(CountrySelectingViewModel.State.Initial)
                val continents by viewModel.continents.subscribeAsState(initial = emptyList())

                CountrySelectingPage(
                    state = CountrySelectingUIState(
                        continents = continents,
                        viewModelState = state
                    ),
                    onClick = { country -> viewModel.onCountrySelected(country) },
                    onButtonClick = { viewModel.onButtonTapped() }
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.events
            .filterIsInstance<Navigate>().thenNavigate()
            .subscribe().also(disposables::add)

        viewModel.events
            .filterIsInstance<CountrySelectingViewModel.Event.Error>()
            .observeOn(AndroidSchedulers.mainThread())
            .thenShowError()
            .subscribe().also(disposables::add)

        viewModel.onPageLoaded()
    }

    private fun Observable<CountrySelectingViewModel.Event.Error>.thenShowError() = doOnNext { errorEvent ->
        showUIError(errorEvent.error.asUIError())
    }

    private fun Observable<Navigate>.thenNavigate() = doOnNext { event ->
        findNavController().navigate(CountrySelectingFragmentDirections.navigateToDetails(event.domainModel))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposables.clear()
    }

    private fun showUIError(uiError: UIError) {
        AlertDialog.Builder(context)
            .setTitle(uiError.titleKey)
            .setMessage(uiError.messageKeys.joinToString {
                getString(it)
            })
            .setNegativeButton(R.string.cancel, null)
            .setIcon(R.drawable.ic_dialog_alert)
            .show()

    }
}
