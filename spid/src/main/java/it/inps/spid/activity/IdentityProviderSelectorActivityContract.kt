//
// SPDX-FileCopyrightText: 2023 Istituto Nazionale Previdenza Sociale
//
// SPDX-License-Identifier: BSD-3-Clause

package it.inps.spid.activity

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import it.inps.spid.model.SpidParams
import it.inps.spid.model.SpidResponse
import it.inps.spid.model.SpidResult
import it.inps.spid.utils.*

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
                SpidResult(
                    SpidEvent.SUCCESS,
                    intent?.getSerializableExtra(
                        IdentityProviderSelectorActivity.EXTRA_SPID_RESPONSE
                    ) as SpidResponse
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