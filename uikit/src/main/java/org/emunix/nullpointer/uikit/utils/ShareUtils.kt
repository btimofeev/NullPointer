package org.emunix.nullpointer.uikit.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent

fun Context?.copyToClipboard(text: String) {
    this?.let { ctx ->
        val clipboard: ClipboardManager? = ctx.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        val clip = ClipData.newPlainText(text, text)
        clipboard?.setPrimaryClip(clip)
    }
}

fun Context?.shareText(text: String) {
    this?.let { ctx ->
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        ctx.startActivity(shareIntent)
    }
}