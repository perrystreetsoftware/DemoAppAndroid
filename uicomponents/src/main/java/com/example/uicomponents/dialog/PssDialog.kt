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
import com.example.uicomponents.models.DialogState

@Composable
fun PssDialog(config: DialogState, onDismissRequest: () -> Unit) {
    val context = LocalContext.current
    val (title, message) = config.dialogTexts.getTitleAndText(context)
    val positiveButtonText = config.dialogActions.getPositiveButtonText(context)
    val negativeButtonText = config.dialogActions.getNegativeButtonText(context)

    fun handleDismissOnButtonPress() {
        if (config.dismissOnButtonPress) {
            onDismissRequest()
        }
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        buttons = {
            Box(Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 2.dp)) {
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically) {
                    negativeButtonText?.let {
                        DialogButton(text = negativeButtonText) {
                            config.dialogActions.onNegative?.invoke()
                            handleDismissOnButtonPress()
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                    DialogButton(text = positiveButtonText) {
                        config.dialogActions.onPositive?.invoke()
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
            dismissOnBackPress = config.dismissOnBackPress,
            dismissOnClickOutside = config.dismissOnClickOutside
        )
    )
}

@Composable
private fun DialogButton(text: String, onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(text = text)
    }
}