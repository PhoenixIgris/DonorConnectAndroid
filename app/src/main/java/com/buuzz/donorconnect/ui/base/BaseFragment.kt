package com.buuzz.donorconnect.ui.base

import androidx.fragment.app.Fragment
import com.buuzz.donorconnect.ui.custom.CustomYesNoDialog


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

}