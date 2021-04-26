// 
// SPDX-FileCopyrightText: 2021 Istituto Nazionale Previdenza Sociale
//
// SPDX-License-Identifier: BSD-3-Clause

package it.inps.spid.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.DividerItemDecoration
import it.inps.spid.R
import it.inps.spid.adapter.IdentityProvidersAdapter
import it.inps.spid.databinding.ActivityProviderSelectorBinding
import it.inps.spid.fragment.SpidDialogFragment
import it.inps.spid.model.IdentityProvider
import it.inps.spid.model.SpidParams
import it.inps.spid.model.SpidResponse
import it.inps.spid.utils.*

class IdentityProviderSelectorActivity : AppCompatActivity(), SpidDialogFragment.SpidCallback {

    private lateinit var binding: ActivityProviderSelectorBinding
    private val spidConfig by lazy {
        intent.extras?.getSerializable(EXTRA_SPID_CONFIG) as SpidParams.Config
    }
    private val idpList by lazy {
        intent.extras?.getSerializable(EXTRA_IDP_LIST) as List<IdentityProvider>
    }

    companion object {
        const val EXTRA_SPID_CONFIG = "EXTRA_SPID_CONFIG"
        const val EXTRA_IDP_LIST = "EXTRA_IDP_LIST"
        const val EXTRA_SPID_RESPONSE = "EXTRA_SPID_RESPONSE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProviderSelectorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnMoreInformation.setOnClickListener {
            openBrowser(spidConfig.spidPageInfoUrl)
        }
        binding.btnNoSpid.setOnClickListener {
            openBrowser(spidConfig.requestSpidPageUrl)
        }

        if (resources.getBoolean(R.bool.show_items_divider)) {
            binding.recyclerviewProviders.addItemDecoration(
                    DividerItemDecoration(
                            this,
                            DividerItemDecoration.VERTICAL
                    )
            )
        }
        binding.recyclerviewProviders.adapter = IdentityProvidersAdapter(idpList.shuffled()) {
            if (isNetworkAvailable()) {
                if (spidConfig.isSpidConfigValid()) {
                    SpidDialogFragment.newInstance(
                            it,
                            spidConfig
                    ).show(
                            supportFragmentManager,
                            SpidDialogFragment::class.java.simpleName
                    )
                } else {
                    setResult(SPID_CONFIG_ERROR)
                    finish()
                }
            } else {
                setResult(NETWORK_ERROR)
                finish()
            }
        }
    }

    private fun openBrowser(@NonNull url: String) =
            CustomTabsIntent.Builder().build().launchUrl(this, Uri.parse(url))

    override fun onBackPressed() {
        setResult(USER_CANCELLED)
        finish()
    }

    override fun onSpidSuccess(spidResponse: SpidResponse) {
        Intent().apply {
            putExtra(EXTRA_SPID_RESPONSE, spidResponse)
        }.also {
            setResult(SUCCESS, it)
            finish()
        }
    }

    override fun onSpidFailure(spidEvent: SpidEvent) {
        when (spidEvent) {
            SpidEvent.GENERIC_ERROR -> {
                setResult(GENERIC_ERROR)
            }
            SpidEvent.NETWORK_ERROR -> {
                setResult(NETWORK_ERROR)
            }
            SpidEvent.SESSION_TIMEOUT -> {
                setResult(SESSION_TIMEOUT)
            }
            SpidEvent.SPID_CONFIG_ERROR -> {
                setResult(SPID_CONFIG_ERROR)
            }
            SpidEvent.USER_CANCELLED -> {
                setResult(USER_CANCELLED)
            }
        }
        finish()
    }
}