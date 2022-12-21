package com.example.feature.countrylist.error

import com.example.errors.CountryListError
import com.example.features.R
import com.example.uicomponents.models.*
import com.example.viewmodels.CountryListViewModel

class CountryListErrorFloatingAlertFactory(
    private val viewModel: CountryListViewModel ,
    private val onAboutSelected: () -> Unit
    ) {
    fun asFloatingAlert(error: CountryListError): FloatingAlert {
        return when(error){
            is CountryListError.NotEnoughPermissionsError -> FloatingAlert.Toast(ToastConfig(R.string.country_list_unavailable_error))
            else -> getDialogState(error)
        }
    }

    private fun getDialogState(error: CountryListError): FloatingAlert.Dialog {
        val (titleKey, messageKeys) = error.titleAndMessage()
        val config = DialogConfig(DialogTexts(titleKey = titleKey, messageKeys = messageKeys))

        return FloatingAlert.Dialog(when (error) {
            is CountryListError.BlockedCountry -> {
                val actions = DialogActions(
                    positiveTextKey = R.string.country_list_blocked_error_positive_button,
                    onPositive = { viewModel.navigateToRandomCountry() },
                    negativeTextKey = R.string.cancel_button_title,
                )
                config.copy(dialogActions = actions)
            }
            is CountryListError.Other -> {
                val actions = DialogActions(
                    positiveTextKey = R.string.ok,
                    onPositive = { onAboutSelected.invoke() },
                    negativeTextKey = R.string.cancel_button_title,
                )

                config.copy(dialogActions = actions)
            }
            else -> config
        })
    }

    private fun CountryListError.titleAndMessage(): Pair<Int, List<Int>> = when (this) {
        CountryListError.ConnectionError -> {
            R.string.connection_error_title to
                listOf(R.string.connection_error_message1, R.string.connection_error_message2)
        }
        CountryListError.Forbidden -> R.string.forbidden_error_title to listOf(R.string.forbidden_error_message)
        is CountryListError.BlockedCountry -> R.string.country_list_blocked_error_title to listOf(R.string.country_list_blocked_error_message)
        CountryListError.Other -> R.string.other_error_title to listOf(R.string.other_error_message1, R.string.other_error_message2)
        else -> R.string.generic_error_title to listOf(R.string.generic_error_message)
    }
}


fun CountryListError.asFloatingAlert(viewModel: CountryListViewModel, onAboutSelected: () -> Unit) = CountryListErrorFloatingAlertFactory(viewModel, onAboutSelected).asFloatingAlert(this)
