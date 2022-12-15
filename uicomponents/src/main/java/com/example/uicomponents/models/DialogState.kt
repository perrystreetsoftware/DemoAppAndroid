package com.example.uicomponents.models

import android.content.Context
import com.example.uicomponents.R

data class DialogState(
    val dialogTexts: DialogTexts,
    val dialogActions: DialogActions = DialogActions(),
    val dismissOnButtonPress: Boolean = true,
    val dismissOnClickOutside: Boolean = true,
    val dismissOnBackPress: Boolean = true,
) {
    private val invalidNegativeButton =
        !dismissOnButtonPress
            && dialogActions.onNegative == null
            && dialogActions.negativeText == null
            && dialogActions.negativeTextKey == null

    private val invalidPositiveButton =
        !dismissOnButtonPress
            && dialogActions.onPositive == null
            && dialogActions.positiveText == null
            && dialogActions.positiveTextKey == null

    init {
        if (invalidPositiveButton || invalidNegativeButton) {
            throw IllegalStateException("Trying to create a dialog without button actions and [dismissOnButtonPress] set to false." +
                "This dialog wouldn't be dismissible")
        }
    }
}

data class DialogActions(
    val positiveText: String? = null,
    val positiveTextKey: Int? = R.string.ok,
    val negativeText: String? = null,
    val negativeTextKey: Int? = null,
    val onPositive: (() -> Unit)? = null,
    val onNegative: (() -> Unit)? = null,
    val onDismiss: (() -> Unit)? = null,
) {
    private val hasPositiveButton = positiveText != null || positiveTextKey != null

    init {
        if (!hasPositiveButton) {
            throw IllegalStateException("You must provide a positive button text or key")
        }
    }

    fun getPositiveButtonText(context: Context): String =
        positiveText ?: context.getString(positiveTextKey!!)

    fun getNegativeButtonText(context: Context): String? =
        negativeText ?: negativeTextKey?.let { context.getString(it) }
}

data class DialogTexts(
    val title: String? = null,
    val titleKey: Int? = null,
    val message: String? = null,
    val messageKeys: List<Int>? = null,
) {

    private val hasTitle = title != null || titleKey != null
    private val hasMessage = message != null || messageKeys != null

    private fun isValid() = hasTitle && hasMessage

    fun getTitleAndText(context: Context): Pair<String, String> {
        val title = title ?: context.getString(titleKey!!)
        val text = message ?: messageKeys!!.joinToString(separator = " ") { context.getString(it) }

        return title to text
    }

    init {
        if (!isValid()) {
            throw IllegalStateException("You must provide a title and a message to create a dialog state")
        }
    }
}
