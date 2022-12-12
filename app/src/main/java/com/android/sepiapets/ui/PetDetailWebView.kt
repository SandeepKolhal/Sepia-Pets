package com.android.sepiapets.ui

import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.android.sepiapets.databinding.ActivityPetDetailWebViewBinding
import com.android.sepiapets.models.petsData.Pet
import com.android.sepiapets.utils.Constants.PET_DATA

class PetDetailWebView : AppCompatActivity() {
    private lateinit var binding: ActivityPetDetailWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPetDetailWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val petData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(PET_DATA, Pet::class.java)
        } else {
            @Suppress("DEPRECATION") intent.getParcelableExtra(PET_DATA) as? Pet
        }

        binding.apply {
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    webProgressBar.visibility = View.GONE
                    webUrl.text = url
                    webTitle.text = view?.title
                    updateUI()
                }

                override fun shouldOverrideUrlLoading(
                    view: WebView?, request: WebResourceRequest?
                ): Boolean {
                    webProgressBar.visibility = View.VISIBLE
                    return super.shouldOverrideUrlLoading(view, request)
                }
            }

            backword.setOnClickListener {
                webView.goBack()
            }

            foreword.setOnClickListener {
                webView.goForward()
            }

            refreshWebSite.setOnClickListener {
                webView.reload()
                webProgressBar.visibility = View.VISIBLE
            }

            petData?.let {
                webView.loadUrl(it.contentUrl)
                webProgressBar.visibility = View.VISIBLE
                petName.text = it.title
                webUrl.text = it.contentUrl
            }

            closeWebView.setOnClickListener {
                finish()
            }
        }
    }

    private fun updateUI() {
        binding.webView.apply {
            binding.backword.isEnabled = canGoBack()
            binding.foreword.isEnabled = canGoForward()
        }
    }
}