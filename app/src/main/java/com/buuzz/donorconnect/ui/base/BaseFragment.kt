package com.buuzz.donorconnect.ui.base

import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.buuzz.donorconnect.ui.custom.CustomYesNoDialog
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar


open class BaseFragment : Fragment() {

    private var errorDialog: CustomYesNoDialog? = CustomYesNoDialog

    fun showErrorDialog(
        message: String,
        isCancelable: Boolean = true,
        btn_text_yes: String = "Okay",
        btn_text_no: String? = "Cancel",
        yesBtnClicked: (() -> Unit)? = null,
        noBtnClicked: (() -> Unit)? = null,
    ) {
        errorDialog?.dismissYesNoDialog()
        errorDialog?.showYesNoDialog(
            requireContext(),
            message,
            isCancelable,
            btn_text_yes,
            btn_text_no,
            yesBtnClicked,
            noBtnClicked
        )
    }

    fun dismissErrorDialog() {
        errorDialog?.dismissYesNoDialog()
    }

    fun showTopSnackBar(view: View, message: String) {
        val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        try {
            val params = snackBarView.layoutParams as FrameLayout.LayoutParams
            params.gravity = Gravity.TOP
            snackBarView.layoutParams = params
        } catch (e: Exception) {
            val params =
                snackBarView.layoutParams as androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams
            snackBarView.layoutParams = params
        }
        snackBar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
        snackBar.show()
    }


}