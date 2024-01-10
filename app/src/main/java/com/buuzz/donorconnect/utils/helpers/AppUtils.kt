package com.buuzz.donorconnect.utils.helpers

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText


fun TextInputEditText.onTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            afterTextChanged.invoke(s.toString())
        }

        override fun afterTextChanged(editable: Editable?) {
        }

    })
}