package com.buuzz.donorconnect.utils.apihelper.safeapicall

interface ApiCallListener {
    fun onSuccess(response: String?)
    fun onError(errorMessage: String?)

}