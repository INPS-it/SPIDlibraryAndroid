//
// SPDX-FileCopyrightText: 2021 Istituto Nazionale Previdenza Sociale
//
// SPDX-License-Identifier: BSD-3-Clause

package it.inps.spid.fragment

import android.content.Context
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.NonNull
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import it.inps.spid.R
import it.inps.spid.activity.IdentityProviderSelectorActivity
import it.inps.spid.databinding.FragmentDialogSpidBinding
import it.inps.spid.model.SpidParams
import it.inps.spid.model.SpidResponse
import it.inps.spid.utils.SpidEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class SpidDialogFragment : DialogFragment() {

    private var _binding: FragmentDialogSpidBinding? = null
    private val binding get() = _binding!!
    private lateinit var spidCallback: SpidCallback

    private val spidConfig by lazy {
        arguments?.getSerializable(IdentityProviderSelectorActivity.EXTRA_SPID_CONFIG) as SpidParams.Config
    }
    private val cookiesHashMap = HashMap<String, String>()
    private var timer = Timer("sessionTimeout")

    companion object {
        const val EXTRA_POST_DATA_PROVIDER = "EXTRA_POST_DATA_PROVIDER"

        fun newInstance(postData: String, spidConfig: SpidParams.Config): SpidDialogFragment {
            return SpidDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_POST_DATA_PROVIDER, postData)
                    putSerializable(IdentityProviderSelectorActivity.EXTRA_SPID_CONFIG, spidConfig)
                }
            }
        }
    }

    interface SpidCallback {
        fun onSpidSuccess(spidResponse: SpidResponse)
        fun onSpidFailure(spidEvent: SpidEvent)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        spidCallback = try {
            context as SpidCallback
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString() + " must implement SpidCallback")
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDialogSpidBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startSessionTimeoutTask(false)
        val postData = arguments?.getString(EXTRA_POST_DATA_PROVIDER)
        if (postData.isNullOrEmpty()) {
            cancelSessionTimeoutTask()
            spidCallback.onSpidFailure(SpidEvent.GENERIC_ERROR)
            dismiss()
        }
        clearCookiesAndCache()
        binding.webviewSpid.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                addCookies(url)
                if (url.equals(spidConfig.callbackPageUrl, ignoreCase = true)) {
                    cancelSessionTimeoutTask()
                    spidCallback.onSpidSuccess(SpidResponse(getCookiesList()))
                    dismiss()
                } else {
                    startSessionTimeoutTask(true)
                }
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                if (resources.getBoolean(R.bool.ignore_ssl_errors)) {
                    handler?.proceed()
                } else {
                    super.onReceivedSslError(view, handler, error)
                }
            }
        }
        binding.webviewSpid.apply {
            settings.apply {
                javaScriptEnabled = true
                loadWithOverviewMode = true
                useWideViewPort = true
                domStorageEnabled = true
                setSupportZoom(true)
                builtInZoomControls = true
                displayZoomControls = false
            }
            scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
            isScrollbarFadingEnabled = false
        }

        CookieManager.getInstance().setAcceptCookie(true)
        binding.webviewSpid.loadUrl("${spidConfig.authPageUrl}$postData")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun clearCookiesAndCache() {
        CookieManager.getInstance().run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                removeAllCookies(null)
            } else {
                removeAllCookie()
            }
        }
    }

    private fun addCookies(@NonNull url: String) {
        CookieManager.getInstance().getCookie(url).let { cookies ->
            if (cookies != null) {
                val cookiesArray = cookies.split(";")
                for (cookie in cookiesArray) {
                    try {
                        if (cookie.split("=")[1].trim() != "") {
                            cookiesHashMap[cookie.split("=")[0]] = cookie.split("=")[1]
                        }
                    } catch (exception: Exception) {
                        exception.printStackTrace()
                    }
                }
            } else {
                cancelSessionTimeoutTask()
                spidCallback.onSpidFailure(SpidEvent.GENERIC_ERROR)
                dismiss()
            }
        }
    }

    private fun getCookiesList(): ArrayList<String> {
        return ArrayList<String>().apply {
            cookiesHashMap.keys.forEach { key ->
                add("$key=${cookiesHashMap[key]}")
            }
        }
    }

    private fun startSessionTimeoutTask(cancelCurrentTimer: Boolean) {
        if (cancelCurrentTimer) {
            cancelSessionTimeoutTask()
        }
        timer = Timer("sessionTimeout")
        timer.schedule(object : TimerTask() {
            override fun run() {
                lifecycleScope.launch(Dispatchers.Main) {
                    spidCallback.onSpidFailure(SpidEvent.SESSION_TIMEOUT)
                    dismissAllowingStateLoss()
                }
            }
        }, spidConfig.timeout.toLong() * 1000)
    }

    private fun cancelSessionTimeoutTask() {
        timer.cancel()
        timer.purge()
    }
}