package com.example.feature.countrylist.error

import com.example.features.R
import com.example.uicomponents.error.ErrorDialogFactory
import com.example.uicomponents.models.DialogActions
import com.example.uicomponents.models.DialogState
import com.example.uicomponents.models.DialogTexts

class CountryListErrorDialogFactory(private val goToRandomAction: () -> Unit) :
    ErrorDialogFactory<CountryListDialogError> {

    override fun getDialogState(error: CountryListDialogError): DialogState {
        val (titleKey, messageKeys) = error.titleAndMessage()
        val config = DialogState(DialogTexts(titleKey = titleKey, messageKeys = messageKeys))

        return when (error) {
            is CountryListDialogError.Blocked -> {
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

    private fun CountryListDialogError.titleAndMessage(): Pair<Int, List<Int>> = when (this) {
        CountryListDialogError.Connection -> {
            R.string.connection_error_title to
                listOf(R.string.connection_error_message1, R.string.connection_error_message2)
        }
        CountryListDialogError.Forbidden -> R.string.forbidden_error_title to listOf(R.string.forbidden_error_message)
        CountryListDialogError.Generic -> R.string.generic_error_title to listOf(R.string.generic_error_message)
        is CountryListDialogError.Blocked -> R.string.country_list_blocked_error_title to listOf(R.string.country_list_blocked_error_message)
    }
}


fun CountryListDialogError.dialogState(goToRandomAction: () -> Unit) =
    CountryListErrorDialogFactory(goToRandomAction).getDialogState(this)