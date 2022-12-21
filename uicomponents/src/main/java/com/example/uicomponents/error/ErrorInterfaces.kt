package com.example.uicomponents.error

interface CustomErrorStateFactory<Error, DisplayState> {
    fun getErrorDisplayState(error: Error): DisplayState
}
