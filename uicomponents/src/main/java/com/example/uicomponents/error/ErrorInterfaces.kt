package com.example.uicomponents.error

import com.example.uicomponents.models.BannerState
import com.example.uicomponents.models.DialogState
import com.example.uicomponents.models.ToastState

interface ErrorDialogFactory<Error> {
    fun getDialogState(error: Error): DialogState?
}

interface ErrorBannerFactory<Error> {
    fun getBannerState(error: Error): BannerState?
}

interface ErrorToastFactory<Error> {
    fun getToastState(error: Error): ToastState?
}

interface CustomErrorStateFactory<Error, DisplayState> {
    fun getErrorDisplayState(error: Error): DisplayState
}





