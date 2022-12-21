package com.example.feature.countrylist.componenets

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.example.uicomponents.dialog.PssDialog
import com.example.uicomponents.models.FloatingAlert
import com.example.viewmodels.ErrorDismissing

@Composable
fun AlertNotifier(floatingAlert: FloatingAlert?, errorDismissing: ErrorDismissing){
    if (floatingAlert == null) {
        return
    }
    when(floatingAlert){
        is FloatingAlert.Dialog -> {
            PssDialog(dialogConfig = floatingAlert.state, onDismissRequest = {
                errorDismissing.dismissError()
            })
        }
        is FloatingAlert.Toast -> {
            val context = LocalContext.current
            LaunchedEffect(Unit){
                Toast.makeText(context, floatingAlert.state.message, Toast.LENGTH_SHORT).show()
                errorDismissing.dismissError()
            }
        }
    }
}