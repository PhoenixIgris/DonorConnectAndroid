package com.buuzz.donorconnect.ui.postmap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.buuzz.donorconnect.R
import com.buuzz.donorconnect.databinding.ActivityPostMapBinding

class PostMapActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostMapBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostMapBinding.inflate(layoutInflater)

        binding.map.settings.javaScriptEnabled = true

        binding.map.loadUrl("http://192.168.1.4:8000/api/map/showMap")
        setContentView(binding.root)
    }
}