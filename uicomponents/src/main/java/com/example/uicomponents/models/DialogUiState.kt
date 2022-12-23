package com.example.uicomponents.models

import android.content.Context
import androidx.annotation.StringRes
import com.example.uicomponents.R

data class DialogUiState(
    val dialogTexts: DialogTexts,
    val positiveAction: DialogAction = DialogAction(textKey = R.string.ok),
    val negativeAction: DialogAction? = null,
    val dismissOnButtonPress: Boolean = true,
    val dismissOnClickOutside: Boolean = true,
    val dismissOnBackPress: Boolean = true,
) {
    private val invalidNegativeButton =
        !dismissOnButtonPress
            && negativeAction?.onClick == null
            && negativeAction?.text == null
            && negativeAction?.textKey == null

    private val invalidPositiveButton =
        !dismissOnButtonPress
            && positiveAction.onClick == null
            && positiveAction.text == null
            && positiveAction.textKey == null

    init {
        require(!invalidPositiveButton && !invalidNegativeButton) {
            "Trying to create a dialog without button actions and [dismissOnButtonPress] set to false." +
                "This dialog wouldn't be dismissible"
        }
    }
}

data class DialogAction(
    val text: String? = null,
    @StringRes val textKey: Int? = null,
    val onClick: (() -> Unit)? = null,
) {
    init {
        require(text != null || textKey != null) {
            "You must provide a button text or key"
        }
    }

    fun getButtonText(context: Context): String =
        text ?: context.getString(textKey!!)
}

data class DialogTexts(
    val title: String? = null,
    @StringRes val titleKey: Int? = null,
    val message: String? = null,
    @StringRes val messageKeys: List<Int>? = null,
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
        require(isValid()) {
            "You must provide a title and a message to create a dialog state"
        }
    }
}
