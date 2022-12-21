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
import com.example.uicomponents.models.DialogConfig

@Composable
fun PssDialog(dialogConfig: DialogConfig, onDismissRequest: (() -> Unit)?) {
    val context = LocalContext.current
    val (title, message) = dialogConfig.dialogTexts.getTitleAndText(context)
    val positiveButtonText = dialogConfig.dialogActions.getPositiveButtonText(context)
    val negativeButtonText = dialogConfig.dialogActions.getNegativeButtonText(context)

    fun handleDismissOnButtonPress() {
        if (dialogConfig.dismissOnButtonPress) {
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
                    negativeButtonText?.let {
                        DialogButton(text = negativeButtonText) {
                            dialogConfig.dialogActions.onNegative?.invoke()
                            handleDismissOnButtonPress()
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                    DialogButton(text = positiveButtonText) {
                        dialogConfig.dialogActions.onPositive?.invoke()
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
            dismissOnBackPress = dialogConfig.dismissOnBackPress,
            dismissOnClickOutside = dialogConfig.dismissOnClickOutside
        )
    )
}

@Composable
private fun DialogButton(text: String, onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(text = text)
    }
}