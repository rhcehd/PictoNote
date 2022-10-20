package com.lhs94.pictonote.ui.widget.toast

import android.content.Context
import android.widget.Toast

object Toast {
    private var contextRequester: (() -> Context)? = null
    fun init(context: Context) {
        contextRequester = { context }
    }
    private fun getContext(): Context? {
        return try {
            val context = contextRequester?.invoke() ?: throw ToastException()
            context
        } catch (e: ToastException) {
            e.printStackTrace()
            null
        }
    }
    fun showDebugToast(msg: String) {
        val context = getContext() ?: return
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}

private class ToastException: Exception("context is null..")