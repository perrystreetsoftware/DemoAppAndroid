package com.example.uicomponents.models

sealed class FloatingAlert {
    data class Dialog(val state: DialogUiState): FloatingAlert()
    data class Toast(val state: ToastUiState): FloatingAlert()
}
