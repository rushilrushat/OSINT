package com.example.testapp.ui.Activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.KeyEvent
import android.webkit.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.testapp.R
import kotlinx.android.synthetic.main.activity_web.*


class WebActivity : AppCompatActivity() {

    companion object {
        const val PAGE_URL = "pageUrl"
        const val MAX_PROGRESS = 100

        fun newIntent(context: Context, pageUrl: String): Intent {
            val intent = Intent(context, WebActivity::class.java)
            intent.putExtra(PAGE_URL, pageUrl)
            return intent
        }
    }

    private lateinit var pageUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        // get pageUrl from String
        pageUrl = intent.getStringExtra(PAGE_URL)
            ?: throw IllegalStateException("field $PAGE_URL missing in Intent")

        initWebView()
        setWebClient()
        handlePullToRefresh()
        loadUrl(pageUrl)
    }

    private fun handlePullToRefresh() {

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.domStorageEnabled = true

        webView.settings.setSupportZoom(true)
        webView.settings.builtInZoomControls=true
        webView.settings.displayZoomControls=true

        webView.setDownloadListener({ url, userAgent, contentDisposition, mimeType, contentLength ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED
                ) {
                    Log.d(
                        "permission",
                        "permission denied to WRITE_EXTERNAL_STORAGE - requesting it"
                    )
                    val permissions =
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permissions, 1)
                }
            }
            val request = DownloadManager.Request(Uri.parse(url))
            request.setMimeType(mimeType)
            request.addRequestHeader("cookie", CookieManager.getInstance().getCookie(url))
            request.addRequestHeader("User-Agent", userAgent)
            request.setDescription("Downloading file...")
            request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType))
            request.allowScanningByMediaScanner()
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, ".png")
            val dm = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            dm.enqueue(request)
            Toast.makeText(applicationContext, "Downloading File", Toast.LENGTH_LONG).show()
        })

        webView.webViewClient = object : WebViewClient() {
            override
            fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                handler?.proceed()
            }
        }

    }

    private fun setWebClient() {
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(
                view: WebView,
                newProgress: Int
            ) {
                super.onProgressChanged(view, newProgress)
                progressBar.progress = newProgress
                if (newProgress < MAX_PROGRESS && progressBar.visibility == ProgressBar.GONE) {
                    progressBar.visibility = ProgressBar.VISIBLE
                }

                if (newProgress == MAX_PROGRESS) {
                    progressBar.visibility = ProgressBar.GONE
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // Check if the key event was the Back button and if there's history
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack()
            return true
        }
        // If it wasn't the Back key or there's no web page history, exit the activity)
        return super.onKeyDown(keyCode, event)
    }

    private fun loadUrl(pageUrl: String) {
        webView.loadUrl(pageUrl)
    }
}
