// 
// SPDX-FileCopyrightText: 2021 Istituto Nazionale Previdenza Sociale
//
// SPDX-License-Identifier: BSD-3-Clause

package it.inps.spid.model

import java.io.Serializable

data class SpidParams(
        val config: Config,
        val idpList: ArrayList<IdentityProvider>
) : Serializable {
    data class Config(
            val authPageUrl: String, // URL service provider page login
            val callbackPageUrl: String, // Redirect URL called by the identity provider after logging in
            val timeout: Int = 30, // Request timeout login
            val spidPageInfoUrl: String = "https://www.spid.gov.it", // Information page about SPID
            val requestSpidPageUrl: String = "https://www.spid.gov.it/richiedi-spid" // URL Page to request SPID
    ) : Serializable {

        fun isSpidConfigValid(): Boolean {
            return when {
                authPageUrl.isEmpty() -> false
                callbackPageUrl.isEmpty() -> false
                timeout <= 0 -> false
                spidPageInfoUrl.isEmpty() -> false
                requestSpidPageUrl.isEmpty() -> false
                authPageUrl.isNotEmpty() && !isValidHttpsUrl(authPageUrl) -> false
                callbackPageUrl.isNotEmpty() && !isValidHttpsUrl(callbackPageUrl) -> false
                spidPageInfoUrl.isNotEmpty() && !isValidHttpsUrl(spidPageInfoUrl) -> false
                requestSpidPageUrl.isNotEmpty() && !isValidHttpsUrl(requestSpidPageUrl) -> false
                else -> true
            }
        }

        private fun isValidHttpsUrl(url: String): Boolean {
            return url.startsWith("https://", ignoreCase = true)
        }
    }
}