package com.buuzz.donorconnect.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buuzz.donorconnect.data.respository.UserRepository
import com.buuzz.donorconnect.utils.apihelper.safeapicall.ApiCallListener
import com.buuzz.donorconnect.utils.apihelper.safeapicall.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    fun logOut(callback: ApiCallListener) {
        viewModelScope.launch {
            when (val response = userRepository.logOut()) {
                is Resource.Failure -> {
                    callback.onError(response.errorMsg)
                }

                is Resource.Success -> {
                    userRepository.deleteAllData()
                    callback.onSuccess(response.value.message)
                }
            }
        }
    }
}