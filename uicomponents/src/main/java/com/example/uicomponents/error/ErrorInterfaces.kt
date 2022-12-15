package com.example.uicomponents.error

import com.example.uicomponents.models.DialogState

interface UiErrorMapper<LogicError, UiError> {
    fun toUiError(error: LogicError): UiError?
}

interface ErrorDialogFactory<Error> {
    fun getDialogState(error: Error): DialogState
}

interface ErrorToastFactory<Error> {
    fun getToastMessage(error: Error): Int
}

interface ErrorBannerFactory<Error> {
    fun getTitleAndMessage(error: Error): Pair<String, String>
}





