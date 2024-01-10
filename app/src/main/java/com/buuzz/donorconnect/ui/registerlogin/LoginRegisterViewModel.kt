package com.buuzz.donorconnect.ui.registerlogin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buuzz.donorconnect.data.model.request.UserRegisterRequestModel
import com.buuzz.donorconnect.data.respository.UserRepository
import com.buuzz.donorconnect.utils.apihelper.safeapicall.ApiCallListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class LoginRegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    internal var signUpState by mutableStateOf(SignUpFormState())
    private val signUpValidationEventChannel = Channel<SignUpValidationEvent>()
    internal val validationEvent = signUpValidationEventChannel.receiveAsFlow()

    internal fun onEvent(event: SignUpFormEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            when (event) {
                is SignUpFormEvent.FirstNameChanged -> {
                    signUpState = signUpState.copy(firstName = event.name)
                    val isNameValid = userRepository.validateName(signUpState.firstName)
                    signUpState = signUpState.copy(
                        isFirstNameValid = isNameValid.first,
                        isFirstNameValidErrMsg = isNameValid.second
                    )
                    if (isNameValid.first) {
                        signUpState =
                            signUpState.copy(firstName = signUpState.firstName)
                    }
                    signUpValidationEventChannel.send(SignUpValidationEvent.UpdateData)
                }

                is SignUpFormEvent.LastNameChanged -> {
                    signUpState = signUpState.copy(lastName = event.name)
                    val isNameValid = userRepository.validateName(signUpState.lastName)
                    signUpState = signUpState.copy(
                        isLastNameValid = isNameValid.first,
                        isLastNameValidErrMsg = isNameValid.second
                    )
                    if (isNameValid.first) {
                        signUpState =
                            signUpState.copy(lastName = signUpState.lastName)
                    }
                    signUpValidationEventChannel.send(SignUpValidationEvent.UpdateData)
                }

                is SignUpFormEvent.EmailChanged -> {
                    val isEmailValid = userRepository.validateEmail(event.email?.trim())
                    signUpState = signUpState.copy(
                        email = event.email,
                        isEmailValid = isEmailValid.first,
                        isEmailValidErrMsg = isEmailValid.second
                    )
                    signUpValidationEventChannel.send(SignUpValidationEvent.UpdateData)
                }

                is SignUpFormEvent.PhoneNoChanged -> {
                    val isPhoneNumberValid = userRepository.validatePhoneNumber(event.phone)
                    signUpState = signUpState.copy(
                        phoneNo = event.phone,
                        isPhoneNumberValid = isPhoneNumberValid.first,
                        isPhoneNumberValidErrMsg = isPhoneNumberValid.second
                    )
                    signUpValidationEventChannel.send(SignUpValidationEvent.UpdateData)
                }

                is SignUpFormEvent.PasswordChanged -> {
                    val isPasswordValid = userRepository.validatePassword(event.password)
                    signUpState = signUpState.copy(
                        password = event.password,
                        isPasswordValid = isPasswordValid.first,
                        isPasswordValidErrMsg = isPasswordValid.second
                    )
                    signUpValidationEventChannel.send(SignUpValidationEvent.UpdateData)
                }

                is SignUpFormEvent.ConfirmPasswordChanged -> {
                    val isConfirmPasswordValid =
                        userRepository.validateConfirmPassword(
                            event.password,
                            event.confirmPassword
                        )
                    signUpState = signUpState.copy(
                        confirmPassword = event.confirmPassword,
                        isConfirmPasswordValid = isConfirmPasswordValid.first,
                        isConfirmPasswordValidErrMsg = isConfirmPasswordValid.second
                    )
                    signUpValidationEventChannel.send(SignUpValidationEvent.UpdateData)
                }

                is SignUpFormEvent.AddressChanged -> {
                    signUpState = signUpState.copy(
                        address = event.address
                    )
                    signUpValidationEventChannel.send(SignUpValidationEvent.UpdateData)
                }

                SignUpFormEvent.Submit -> {
                    signUpValidationEventChannel.send(SignUpValidationEvent.Loading("Checking Data"))
                    userRepository.validateData(signUpState) {
                        signUpState = signUpState.copy(isDataValid = it)
                    }
                    registerUser()
                }
            }
        }
    }

    private fun registerUser() {
        viewModelScope.launch(Dispatchers.IO) {
            if (signUpState.isDataValid) {
                userRepository.registerUser(
                    getUpdateDriverDataRequestBody(),
                    signUpValidationEventChannel
                )
            } else {
                signUpValidationEventChannel.send(SignUpValidationEvent.ShowValidationError)
                signUpValidationEventChannel.send(SignUpValidationEvent.UpdateData)
            }
        }
    }


    private suspend fun getUpdateDriverDataRequestBody(): UserRegisterRequestModel =
        withContext(Dispatchers.IO) {
            return@withContext UserRegisterRequestModel(
                first_name = "${signUpState.firstName}",
                last_name = "${signUpState.lastName}",
                email = if (signUpState.email.isNullOrEmpty()) null else "${signUpState.email}",
                password = "${signUpState.password}",
                phone_number = "+977${signUpState.phoneNo}",
                address = "${signUpState.address}",
                name = "${signUpState.firstName} ${signUpState.lastName}"
            )
        }


    internal fun validateEmail(email: String): Pair<Boolean, String> {
        return userRepository.validateEmail(email)
    }


    internal fun validatePassword(password: String?): Pair<Boolean, String> {
        return userRepository.validatePassword(password)
    }


    internal fun login(email: String?, password: String?, callback: ApiCallListener) {
        viewModelScope.launch {
            userRepository.loginUser(email, password, callback)
        }
    }

}


