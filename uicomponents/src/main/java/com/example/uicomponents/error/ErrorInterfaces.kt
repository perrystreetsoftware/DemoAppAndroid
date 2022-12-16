package com.example.uicomponents.error

import com.example.uicomponents.models.DialogState

interface ErrorDialogFactory<Error> {
    fun getDialogState(error: Error): DialogState
}

interface ErrorToastFactory<Error> {
    fun getToastMessage(error: Error): Int
}

interface ErrorBannerFactory<Error> {
    fun getTitleAndMessage(error: Error): Pair<String, String>
}





