package com.example.uicomponents.library

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.example.uicomponents.dialog.PssDialog
import com.example.uicomponents.models.FloatingAlert

@Composable
fun FloatingAlertNotifier(floatingAlert: FloatingAlert?, errorDismissing: () -> Unit) {
    if (floatingAlert == null) {
        return
    }
    when(floatingAlert){
        is FloatingAlert.Dialog -> {
            PssDialog(dialogUiState = floatingAlert.state, errorDismissing)
        }
        is FloatingAlert.Toast -> {
            val context = LocalContext.current
            LaunchedEffect(Unit){
                Toast.makeText(context, floatingAlert.state.message, Toast.LENGTH_SHORT).show()
                errorDismissing.invoke()
            }
        }
    }
}