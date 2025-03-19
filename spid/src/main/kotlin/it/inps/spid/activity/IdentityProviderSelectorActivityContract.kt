//
// SPDX-FileCopyrightText: 2025 Istituto Nazionale Previdenza Sociale
//
// SPDX-License-Identifier: BSD-3-Clause

package it.inps.spid.activity

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import it.inps.spid.model.SpidParams
import it.inps.spid.model.SpidResponse
import it.inps.spid.model.SpidResult
import it.inps.spid.utils.GENERIC_ERROR
import it.inps.spid.utils.NETWORK_ERROR
import it.inps.spid.utils.SESSION_TIMEOUT
import it.inps.spid.utils.SPID_CONFIG_ERROR
import it.inps.spid.utils.SUCCESS
import it.inps.spid.utils.SpidEvent
import it.inps.spid.utils.USER_CANCELLED

class IdentityProviderSelectorActivityContract : ActivityResultContract<SpidParams, SpidResult>() {

    override fun createIntent(context: Context, spidParams: SpidParams): Intent {
        return Intent(context, IdentityProviderSelectorActivity::class.java).apply {
            putExtra(IdentityProviderSelectorActivity.EXTRA_SPID_CONFIG, spidParams.config)
            putExtra(IdentityProviderSelectorActivity.EXTRA_IDP_LIST, spidParams.idpList)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): SpidResult {
        return when (resultCode) {
            SUCCESS -> {
                val spidResponseSerializable = intent?.getSerializableExtra(
                    IdentityProviderSelectorActivity.EXTRA_SPID_RESPONSE
                )
                SpidResult(
                    SpidEvent.SUCCESS,
                    if (spidResponseSerializable is SpidResponse) {
                        intent.getSerializableExtra(
                            IdentityProviderSelectorActivity.EXTRA_SPID_RESPONSE
                        ) as SpidResponse
                    } else {
                        null
                    }
                )
            }

            GENERIC_ERROR -> SpidResult(SpidEvent.GENERIC_ERROR)
            NETWORK_ERROR -> SpidResult(SpidEvent.NETWORK_ERROR)
            SESSION_TIMEOUT -> SpidResult(SpidEvent.SESSION_TIMEOUT)
            SPID_CONFIG_ERROR -> SpidResult(SpidEvent.SPID_CONFIG_ERROR)
            USER_CANCELLED -> SpidResult(SpidEvent.USER_CANCELLED)
            else -> SpidResult(SpidEvent.GENERIC_ERROR)
        }
    }
}