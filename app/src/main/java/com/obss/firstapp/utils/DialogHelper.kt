package com.obss.firstapp.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import com.obss.firstapp.R

object DialogHelper {
    fun showCustomAlertDialog(
        context: Context,
        message: String,
    ) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.error_dialog, null)

        val alertDialogBuilder =
            AlertDialog
                .Builder(context)
                .setView(dialogView)

        val alertDialog = alertDialogBuilder.show()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(0))

        val messageTextView: TextView = dialogView.findViewById(R.id.tv_error_dialog_desc)
        val closeButton: Button = dialogView.findViewById(R.id.btn_error_close)

        messageTextView.text = message

        closeButton.setOnClickListener {
            alertDialog.dismiss()
        }
    }
}
