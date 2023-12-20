package com.buuzz.donorconnect.ui.registerlogin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.buuzz.donorconnect.databinding.ActivityRegisterLoginBinding
import com.buuzz.donorconnect.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterLoginActivity : BaseActivity() {
    private lateinit var binding: ActivityRegisterLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}