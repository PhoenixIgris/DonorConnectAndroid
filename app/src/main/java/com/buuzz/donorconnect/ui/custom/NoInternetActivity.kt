package com.buuzz.donorconnect.ui.custom

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.buuzz.donorconnect.databinding.NoInternetLytBinding
import com.buuzz.donorconnect.utils.apihelper.InternetConnectionChecker

class NoInternetActivity : AppCompatActivity() {
    private lateinit var binding: NoInternetLytBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NoInternetLytBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.swipeRefreshLayout.setOnRefreshListener {
            if (InternetConnectionChecker.isOnline(this)) {
                binding.swipeRefreshLayout.isRefreshing = false
                this.finish()
            } else {
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }
}