package com.example.uicomponents.models

sealed class FloatingAlert {
    data class Dialog(val state: DialogConfig): FloatingAlert()
    data class Toast(val state: ToastConfig): FloatingAlert()
}
