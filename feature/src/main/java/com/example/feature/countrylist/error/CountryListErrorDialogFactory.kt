package com.example.feature.countrylist.error

import com.example.errors.CountryListError
import com.example.features.R
import com.example.uicomponents.error.ErrorDialogFactory
import com.example.uicomponents.models.DialogActions
import com.example.uicomponents.models.DialogState
import com.example.uicomponents.models.DialogTexts

class CountryListErrorDialogFactory(private val goToRandomAction: () -> Unit) : ErrorDialogFactory<CountryListError> {

    override fun getDialogState(error: CountryListError): DialogState? {
        if (error.titleAndMessage() == null) {
            return null
        }

        val (titleKey, messageKeys) = error.titleAndMessage()!!
        val config = DialogState(DialogTexts(titleKey = titleKey, messageKeys = messageKeys))

        return when (error) {
            is CountryListError.BlockedCountry -> {
                val actions = DialogActions(
                    positiveTextKey = R.string.country_list_blocked_error_positive_button,
                    negativeTextKey = R.string.cancel_button_title,
                    onPositive = goToRandomAction
                )
                config.copy(dialogActions = actions)
            }
            else -> config
        }
    }

    private fun CountryListError.titleAndMessage(): Pair<Int, List<Int>>? = when (this) {
        CountryListError.ConnectionError -> {
            R.string.connection_error_title to
                listOf(R.string.connection_error_message1, R.string.connection_error_message2)
        }
        CountryListError.Forbidden -> R.string.forbidden_error_title to listOf(R.string.forbidden_error_message)
        is CountryListError.BlockedCountry -> R.string.country_list_blocked_error_title to listOf(R.string.country_list_blocked_error_message)
        CountryListError.Other -> R.string.generic_error_title to listOf(R.string.generic_error_message)
        else -> null
    }
}


fun CountryListError.asDialogState(goToRandomAction: () -> Unit) =
    CountryListErrorDialogFactory(goToRandomAction).getDialogState(this)