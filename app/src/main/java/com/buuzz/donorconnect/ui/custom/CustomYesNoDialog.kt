package com.buuzz.donorconnect.ui.custom

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.buuzz.donorconnect.R

object CustomYesNoDialog {

    private var yesNoDialog: AlertDialog? = null

    @SuppressLint("InflateParams")
    fun showYesNoDialog(
        context: Context,
        message: String,
        isCancelable: Boolean = true,
        btn_text_yes: String = "Okay",
        btn_text_no: String? = "Cancel",
        yesBtnClicked: (() -> Unit)? = null,
        noBtnClicked: (() -> Unit)? = null,
    ) {
        yesNoDialog = AlertDialog.Builder(context, R.style.CustomDialogTheme).apply {
            setCancelable(isCancelable)
            setView(
                LayoutInflater.from(context).inflate(R.layout.custom_dialogbox_yesno, null).apply {
                    findViewById<TextView>(R.id.tv_dialog_message).text = message
                    findViewById<TextView>(R.id.btn_positive).text = btn_text_yes
                    if (!btn_text_no.isNullOrEmpty()) {
                        findViewById<TextView>(R.id.btn_no).isVisible = true
                        findViewById<TextView>(R.id.btn_negative).text = btn_text_no
                    } else {
                        findViewById<TextView>(R.id.btn_no).isVisible = false
                    }
                    findViewById<LinearLayout>(R.id.btn_yes).setOnClickListener {
                        dismissYesNoDialog()
                        if (yesBtnClicked != null) {
                            yesBtnClicked()
                        }
                    }
                    findViewById<LinearLayout>(R.id.btn_no).setOnClickListener {
                        if (noBtnClicked != null) {
                            noBtnClicked()
                        }
                        dismissYesNoDialog()
                    }
                })
        }.create()
        yesNoDialog?.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setGravity(Gravity.CENTER)
        }
        yesNoDialog?.show()
    }


    fun dismissYesNoDialog() {
        yesNoDialog?.dismiss()
    }

    fun showYesNoDialog() {
        yesNoDialog?.show()
    }
}