package com.thinksurfmedia.utils

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.thinksurfmedia.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


var alertDialog: AlertDialog? = null

private fun createDialog(context: Context?, cancellable: Boolean) {
    alertDialog = AlertDialog.Builder(context, R.style.AlertDialog)
        .setCancelable(cancellable)
        .setView(R.layout.dialogs)
        .create()
}

fun displayProgressDialog(context: Context?) {
    createDialog(context, false)
    alertDialog!!.show()
}

private fun dismissDialog() {
    alertDialog?.dismiss()
}

fun successDialog(context: Context?, displayMessage: String) {

    dismissDialog()
    createDialog(context, true)

    val content: View = LayoutInflater.from(context).inflate(R.layout.dialogs, null)
    val progressBar: ProgressBar = content.findViewById(R.id.progressBar)
    val message: TextView = content.findViewById(R.id.message)
    val imageStatus: ImageView = content.findViewById(R.id.imageStatus)

    progressBar.visibility = View.GONE
    imageStatus.visibility = View.VISIBLE

    message.text = displayMessage
    Glide.with(imageStatus)
        .load(R.drawable.payment_success)
        .into(imageStatus)

    alertDialog!!.setView(content)
    alertDialog!!.show()

    CoroutineScope(Dispatchers.Unconfined).launch {
        delay(5000)
        dismissDialog()
    }
}

fun failedDialog(context: Context?, displayMessage: String) {

    dismissDialog()
    createDialog(context, true)

    val content: View = LayoutInflater.from(context).inflate(R.layout.dialogs, null)
    val progressBar: ProgressBar = content.findViewById(R.id.progressBar)
    val message: TextView = content.findViewById(R.id.message)
    val imageStatus: ImageView = content.findViewById(R.id.imageStatus)

    progressBar.visibility = View.GONE
    imageStatus.visibility = View.VISIBLE

    message.text = displayMessage
    Glide.with(imageStatus)
        .load(R.drawable.logo_small)
        .into(imageStatus)

    alertDialog!!.setView(content)
    alertDialog!!.show()
}

fun View.enabled() {
    isEnabled = true
    alpha = 1.0f
}

fun View.disabled() {
    isEnabled = false
    alpha = 0.5f
}

fun ImageView.loadImage(imageUrl: String) {
    Glide.with(this.context)
        .load(imageUrl)
        .centerCrop()
        .placeholder(R.drawable.logo_small)
        .error(R.drawable.logo_small)
        .into(this)
}