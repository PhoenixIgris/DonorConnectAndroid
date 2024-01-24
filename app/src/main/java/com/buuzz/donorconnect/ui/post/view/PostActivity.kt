package com.buuzz.donorconnect.ui.post.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.buuzz.donorconnect.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
    }
}