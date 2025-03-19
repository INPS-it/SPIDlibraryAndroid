// 
// SPDX-FileCopyrightText: 2025 Istituto Nazionale Previdenza Sociale
//
// SPDX-License-Identifier: BSD-3-Clause

package it.inps.spid.model

import androidx.annotation.DrawableRes
import it.inps.spid.R
import java.io.Serializable

/**
 *
 * @param accessibilityName The identity provider name for accessibility
 * @param logo an @IntegerRes of a drawable resource
 * The library provides the following logos:
 * <ul>
 *     <li>ARUBA: R.drawable.ic_spid_idp_arubaid</li>
 *     <li>INFOCERT: R.drawable.ic_spid_idp_infocertid</li>
 *     <li>INFOCAMERE: R.drawable.ic_spid_idp_infocamereid</li>
 *     <li>ETNA: R.drawable.ic_spid_idp_etnaid</li>
 *     <li>LEPIDA: R.drawable.ic_spid_idp_lepidaid</li>
 *     <li>NAMIRIALIID: R.drawable.ic_spid_idp_namirialid</li>
 *     <li>POSTE: R.drawable.ic_spid_idp_posteid</li>
 *     <li>SIELTE: R.drawable.ic_spid_idp_sielteid</li>
 *     <li>SPID ITALIA: R.drawable.ic_spid_idp_spiditalia</li>
 *     <li>TIM: R.drawable.ic_spid_idp_timid</li>
 *     <li>TEAMSYSTEM: R.drawable.ic_spid_idp_teamsystemid</li>
 *     <li>INTESIGROUP: R.drawable.ic_spid_idp_intesigroup</li>
 * </ul>
 *
 */
data class IdentityProvider(
    val accessibilityName: String?,
    @DrawableRes val logo: Int,
    val idpParameter: String
) : Serializable {

    class Builder {
        private var idpList = ArrayList<IdentityProvider>()

        fun build(): ArrayList<IdentityProvider> {
            return idpList
        }

        fun addAruba(
            accessibilityName: String = "Aruba",
            idpParameter: String
        ): Builder {
            idpList.add(
                IdentityProvider(
                    accessibilityName,
                    R.drawable.ic_spid_idp_arubaid,
                    idpParameter
                )
            )
            return this
        }

        fun addInfocert(
            accessibilityName: String = "Infocert",
            idpParameter: String
        ): Builder {
            idpList.add(
                IdentityProvider(
                    accessibilityName,
                    R.drawable.ic_spid_idp_infocertid,
                    idpParameter
                )
            )
            return this
        }

        fun addInfoCamere(
            accessibilityName: String = "InfoCamere",
            idpParameter: String
        ): Builder {
            idpList.add(
                IdentityProvider(
                    accessibilityName,
                    R.drawable.ic_spid_idp_infocamereid,
                    idpParameter
                )
            )
            return this
        }

        fun addEtna(
            accessibilityName: String = "Etna",
            idpParameter: String
        ): Builder {
            idpList.add(
                IdentityProvider(
                    accessibilityName,
                    R.drawable.ic_spid_idp_etnaid,
                    idpParameter
                )
            )
            return this
        }

        fun addLepida(
            accessibilityName: String = "Lepida",
            idpParameter: String
        ): Builder {
            idpList.add(
                IdentityProvider(
                    accessibilityName,
                    R.drawable.ic_spid_idp_lepidaid,
                    idpParameter
                )
            )
            return this
        }

        fun addNamirial(
            accessibilityName: String = "Namirial",
            idpParameter: String
        ): Builder {
            idpList.add(
                IdentityProvider(
                    accessibilityName,
                    R.drawable.ic_spid_idp_namirialid,
                    idpParameter
                )
            )
            return this
        }

        fun addPoste(
            accessibilityName: String = "Poste Italiane",
            idpParameter: String
        ): Builder {
            idpList.add(
                IdentityProvider(
                    accessibilityName,
                    R.drawable.ic_spid_idp_posteid,
                    idpParameter
                )
            )
            return this
        }

        fun addSielte(
            accessibilityName: String = "Sielte",
            idpParameter: String
        ): Builder {
            idpList.add(
                IdentityProvider(
                    accessibilityName,
                    R.drawable.ic_spid_idp_sielteid,
                    idpParameter
                )
            )
            return this
        }

        fun addSpidItalia(
            accessibilityName: String = "SPID Italia",
            idpParameter: String
        ): Builder {
            idpList.add(
                IdentityProvider(
                    accessibilityName,
                    R.drawable.ic_spid_idp_spiditalia,
                    idpParameter
                )
            )
            return this
        }

        fun addTim(
            accessibilityName: String = "TIM",
            idpParameter: String
        ): Builder {
            idpList.add(
                IdentityProvider(
                    accessibilityName,
                    R.drawable.ic_spid_idp_timid,
                    idpParameter
                )
            )
            return this
        }

        fun addTeamSystem(
            accessibilityName: String = "TeamSystem",
            idpParameter: String
        ): Builder {
            idpList.add(
                IdentityProvider(
                    accessibilityName,
                    R.drawable.ic_spid_idp_teamsystemid,
                    idpParameter
                )
            )
            return this
        }

        fun addIntesiGroup(
            accessibilityName: String = "IntesiGroup",
            idpParameter: String
        ): Builder {
            idpList.add(
                IdentityProvider(
                    accessibilityName,
                    R.drawable.ic_spid_idp_intesigroup,
                    idpParameter
                )
            )
            return this
        }

        fun addCustomIdentityProvider(
            accessibilityName: String? = null,
            @DrawableRes logo: Int,
            idpParameter: String
        ): Builder {
            idpList.add(
                IdentityProvider(
                    accessibilityName,
                    logo,
                    idpParameter
                )
            )
            return this
        }
    }
}
