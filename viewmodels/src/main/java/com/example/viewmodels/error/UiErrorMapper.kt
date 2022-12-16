package com.example.viewmodels.error

interface UiErrorMapper<LogicError, UiError> {
    fun toUiError(error: LogicError): UiError?
}