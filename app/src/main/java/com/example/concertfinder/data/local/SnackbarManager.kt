package com.example.concertfinder.data.local

import android.content.Context
import com.example.concertfinder.presentation.app.AppViewModel
import java.lang.ref.WeakReference

object AppSnackbarManager {
    private var appViewModelRef: WeakReference<AppViewModel>? = null // Or use a direct flow

    fun init(appViewModel: AppViewModel) { // Call this from where AppViewModel is created
        appViewModelRef = WeakReference(appViewModel)
    }

    fun showSnackbar(message: String, actionLabel: String? = null, onAction: (() -> Unit)? = null) {
        appViewModelRef?.get()?.showSnackbar(message, actionLabel, onAction)
    }

    // If you need string resources from non-Composable contexts
    fun showSnackbar(context: Context, messageResId: Int, actionLabelResId: Int? = null, onAction: (() -> Unit)? = null) {
        val message = context.getString(messageResId)
        if (actionLabelResId != null) {
            val actionLabel = context.getString(actionLabelResId)
            showSnackbar(message, actionLabel, onAction)
        } else {
            showSnackbar(message)
        }
    }
}