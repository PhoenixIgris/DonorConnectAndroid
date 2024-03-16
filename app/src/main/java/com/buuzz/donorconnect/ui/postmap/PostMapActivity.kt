package com.buuzz.donorconnect.ui.postmap

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.buuzz.donorconnect.R
import com.buuzz.donorconnect.databinding.ActivityPostMapBinding
import com.buuzz.donorconnect.ui.post.view.PostActivity
import com.buuzz.donorconnect.utils.helpers.IntentParams

private const val TAG = "PostMapActivity"
class PostMapActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostMapBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostMapBinding.inflate(layoutInflater)
        loadWebView("http://192.168.1.4:8000/api/map/showMap")
        setContentView(binding.root)
    }


    private fun loadWebView(url: String?) {
        binding.apply {
            webView.settings.javaScriptEnabled = true
            webView.settings.loadWithOverviewMode = true
            webView.settings.useWideViewPort = true
            webView.settings.builtInZoomControls = true
            webView.settings.userAgentString = "Android WebView"
            webView.settings.domStorageEnabled = true
            webView.addJavascriptInterface(
                WebAppInterface(
                    this@PostMapActivity,
                    this@PostMapActivity
                ), "Android"
            )
            webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    view.loadUrl(url)
                    //                view.addJavascriptInterface(WebAppInterface(getActivityContext().applicationContext), "Android")
                    return true
                }

                override fun onLoadResource(view: WebView, url: String) {
                }

                override fun onPageFinished(view: WebView, url: String) {
                }

                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    super.onReceivedError(view, request, error)

                }


            }
            if (url != null) {
                webView.loadUrl(url)
            }
        }
    }

    inner class WebAppInterface(val context: Context, val activity: Activity) {

        @JavascriptInterface
        fun onMarkerClick(data: String) {
            // Handle the received data from JavaScript
            // You can perform any actions here
            Log.e(TAG, "onMarkerClick: $data", )
            val intent = Intent(this@PostMapActivity, PostActivity::class.java)
            intent.putExtra(IntentParams.POST_DETAIL, data)
            startActivity(intent)
        }

    }
}