package com.buuzz.donorconnect.ui.registerlogin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.buuzz.donorconnect.R
import com.buuzz.donorconnect.databinding.FragmentRegisterBinding
import com.buuzz.donorconnect.ui.base.BaseFragment
import com.buuzz.donorconnect.utils.helpers.onTextChanged
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : BaseFragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val sharedViewModel: LoginRegisterViewModel by activityViewModels()
    private var isLoading = false
    private var state: SignUpFormState? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        setButtonClicks()
        observeValidateEvents()
        return binding.root
    }

    private fun observeValidateEvents() {
        lifecycleScope.launchWhenStarted {
            sharedViewModel.validationEvent.collect {
                when (it) {
                    is SignUpValidationEvent.Failure -> {
                        isLoading = false
                        showErrorDialog(message = it.errorMessage, btn_text_no = null)
                        updateView()
                    }

                    is SignUpValidationEvent.Loading -> {
                        isLoading = true
                        updateView()
                    }

                    is SignUpValidationEvent.Success -> {
                        isLoading = false
                        updateView()
                        findNavController().popBackStack()
                    }

                    SignUpValidationEvent.UpdateData -> {
                        state = sharedViewModel.signUpState
                        isLoading = false
                        Log.e("STATE_DATA", "$state")
                        updateView()
                        displayError()
                    }

                    SignUpValidationEvent.ShowValidationError -> {
                        state = sharedViewModel.signUpState
                        Log.e("STATE_DATA", "error $state")
                        displayError()
                    }
                }
            }
        }
    }

    private fun updateView() {
        lifecycleScope.launch(Dispatchers.Main) {
            binding.apply {
                progressBar.isVisible = isLoading
                signUpButton.isEnabled = !isLoading
                firstNameTVET.isEnabled = !isLoading
                lastNameTVET.isEnabled = !isLoading
                emailTVET.isEnabled = !isLoading
            }
        }
    }

    private fun displayError() {
        state?.let { state ->
            binding.apply {
                setErrorIfTrue(
                    firstNameTVTxtInputLyt,

                    state.isFirstNameValid, state.isFirstNameValidErrMsg
                )
                setErrorIfTrue(
                    lastNameTVTxtInputLyt, state.isLastNameValid, state.isLastNameValidErrMsg
                )
                setErrorIfTrue(
                    emailTVTxtInputLyt, state.isEmailValid, state.isEmailValidErrMsg
                )
                setErrorIfTrue(
                    phoneTVTxtInputLyt, state.isPhoneNumberValid, state.isPhoneNumberValidErrMsg
                )
                setErrorIfTrue(
                    passwordTVTxtInputLyt, state.isPasswordValid, state.isPasswordValidErrMsg
                )
                if (state.isPasswordValid) {
                    setErrorIfTrue(
                        confirmPwdTVTxtInputLyt,
                        state.isConfirmPasswordValid,
                        state.isConfirmPasswordValidErrMsg
                    )
                }
            }

        }
    }

    private fun setButtonClicks() {
        binding.signUpButton.setOnClickListener {
            sharedViewModel.onEvent(SignUpFormEvent.Submit)
        }

        binding.firstNameTVET.onTextChanged {
            sharedViewModel.onEvent(SignUpFormEvent.FirstNameChanged(it))
            setErrorIfTrue(
                binding.firstNameTVTxtInputLyt,
                state?.isFirstNameValid, state?.isFirstNameValidErrMsg
            )
        }
        binding.lastNameTVET.onTextChanged {
            sharedViewModel.onEvent(SignUpFormEvent.LastNameChanged(it))
            setErrorIfTrue(
                binding.lastNameTVTxtInputLyt,
                state?.isLastNameValid, state?.isLastNameValidErrMsg
            )
        }


        binding.emailTVET.onTextChanged {
            sharedViewModel.onEvent(SignUpFormEvent.EmailChanged(it))
            setErrorIfTrue(
                binding.emailTVTxtInputLyt,
                state?.isEmailValid, state?.isEmailValidErrMsg
            )
        }
        binding.phoneTVET.onTextChanged {
            sharedViewModel.onEvent(SignUpFormEvent.PhoneNoChanged(it))
            setErrorIfTrue(
                binding.phoneTVTxtInputLyt,
                state?.isPhoneNumberValid, state?.isPhoneNumberValidErrMsg
            )
        }

        binding.passwordTVET.onTextChanged {
            binding.passwordTVTxtInputLyt.isErrorEnabled = false
            sharedViewModel.onEvent(SignUpFormEvent.PasswordChanged(it))
            sharedViewModel.onEvent(
                SignUpFormEvent.ConfirmPasswordChanged(
                    it, sharedViewModel.signUpState.confirmPassword
                )
            )
            setErrorIfTrue(
                binding.passwordTVTxtInputLyt,
                state?.isPasswordValid, state?.isPasswordValidErrMsg
            )
        }

        binding.confirmPwdTVET.onTextChanged {
            binding.confirmPwdTVTxtInputLyt.isErrorEnabled = false
            sharedViewModel.onEvent(
                SignUpFormEvent.ConfirmPasswordChanged(
                    sharedViewModel.signUpState.password, it
                )
            )
            setErrorIfTrue(
                binding.confirmPwdTVTxtInputLyt,
                state?.isConfirmPasswordValid, state?.isConfirmPasswordValidErrMsg
            )
        }

        binding.loginTV.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()

        }
    }

    private fun setErrorIfTrue(
        textInputLayout: TextInputLayout, isValid: Boolean?, errMessage: String?
    ) {
        isValid?.let {
            textInputLayout.isErrorEnabled = !isValid
            if (!isValid) {
                textInputLayout.error = errMessage
                textInputLayout.boxStrokeWidth = 2
            } else {
                textInputLayout.boxStrokeWidth = 1
                textInputLayout.error = ""
            }
        }
    }

}