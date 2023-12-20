package com.buuzz.donorconnect.data.local

interface OnDataCallback {
    fun onData(data: String?){}
    fun onError(errorMessage: String?){}
}