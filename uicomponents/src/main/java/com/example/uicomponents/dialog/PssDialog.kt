package com.example.uicomponents.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.uicomponents.models.DialogUiState

@Composable
fun PssDialog(dialogUiState: DialogUiState, onDismissRequest: (() -> Unit)?) {
    val context = LocalContext.current
    val (title, message) = dialogUiState.dialogTexts.getTitleAndText(context)
    val positiveAction = dialogUiState.positiveAction
    val negativeAction = dialogUiState.negativeAction

    fun handleDismissOnButtonPress() {
        if (dialogUiState.dismissOnButtonPress) {
            if (onDismissRequest != null) {
                onDismissRequest()
            }
        }
    }

    AlertDialog(
        onDismissRequest = {
            if (onDismissRequest != null) {
                onDismissRequest()
            }
        },
        buttons = {
            Box(Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 2.dp)) {
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically) {
                    negativeAction?.let {
                        DialogButton(text = it.getButtonText(context)) {
                            it.onClick?.invoke()
                            handleDismissOnButtonPress()
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                    DialogButton(text = positiveAction.getButtonText(context)) {
                        positiveAction.onClick?.invoke()
                        handleDismissOnButtonPress()
                    }
                }
            }
        },
        title = { Text(text = title) },
        text = {
            Text(text = message)
        },
        properties = DialogProperties(
            dismissOnBackPress = dialogUiState.dismissOnBackPress,
            dismissOnClickOutside = dialogUiState.dismissOnClickOutside
        )
    )
}

@Composable
private fun DialogButton(text: String, onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(text = text)
    }
}