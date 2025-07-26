package com.kosa.selp.features.gift.composable

import android.annotation.SuppressLint
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun GiftDetailWebView(
    url: String,
    onLoadingFinished: () -> Unit,
    modifier: Modifier = Modifier,
    alpha: Float = 1f
) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true

                webChromeClient = object : WebChromeClient() {
                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                        if (newProgress >= 99) {
                            onLoadingFinished()
                        }
                    }
                }

                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        val js = """
                            (function() {
                                const topUnder = document.querySelector('.product-detail-top.under');
                                if (topUnder) topUnder.remove();

                                const topOver = document.querySelector('.product-detail-top.over');
                                if (topOver) topOver.remove();

                                const detailBar = document.querySelector('#product-detail-tab');
                                if (detailBar) detailBar.remove();
                            })();
                        """.trimIndent()
                        view?.evaluateJavascript(js, null)
                    }
                }

                loadUrl(url)
            }
        },
        modifier = modifier.graphicsLayer { this.alpha = alpha }
    )
}
