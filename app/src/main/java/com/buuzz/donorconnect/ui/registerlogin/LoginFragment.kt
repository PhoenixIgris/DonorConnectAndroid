package com.buuzz.donorconnect.ui.registerlogin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.buuzz.donorconnect.MainActivity
import com.buuzz.donorconnect.R
import com.buuzz.donorconnect.databinding.FragmentLoginBinding
import com.buuzz.donorconnect.ui.base.BaseFragment
import com.buuzz.donorconnect.utils.apihelper.safeapicall.ApiCallListener
import com.buuzz.donorconnect.utils.helpers.onTextChanged
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    private lateinit var binding: FragmentLoginBinding
    private val sharedViewModel: LoginRegisterViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        setButtonClicks()
        return binding.root
    }

    private fun setButtonClicks() {
        binding.apply {
            signUpButton.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
            loginBtn.setOnClickListener {
                loginUser()
            }
            binding.emailTVET.onTextChanged {
                val isValidEmail = sharedViewModel.validateEmail(binding.emailTVET.text.toString())
                if (!isValidEmail.first) {
                    binding.emailInputLyt.isErrorEnabled = true
                    binding.emailInputLyt.error = isValidEmail.second
                } else {
                    binding.emailInputLyt.isErrorEnabled = false
                }
            }
            binding.passwordTVET.onTextChanged {
                val isValidEmail =
                    sharedViewModel.validatePassword(binding.passwordTVET.text.toString())
                if (!isValidEmail.first) {
                    binding.passwordInputLyt.isErrorEnabled = true
                    binding.passwordInputLyt.error = isValidEmail.second
                } else {
                    binding.passwordInputLyt.isErrorEnabled = false
                }
            }
        }
    }



    private fun loginUser() {
        binding.progressBar.isVisible = true
        sharedViewModel.login(
            binding.emailTVET.text.toString(),
            binding.passwordTVET.text.toString(),
            object : ApiCallListener {
                override fun onSuccess(response: String?) {
                    binding.progressBar.isVisible = false
                    Toast.makeText(requireContext(), response, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                }

                override fun onError(errorMessage: String?) {
                    binding.progressBar.isVisible = false
                    showErrorDialog(
                        message = errorMessage ?: "Error Logging In.",
                        btn_text_no = null
                    )
                }

            }
        )
    }


}