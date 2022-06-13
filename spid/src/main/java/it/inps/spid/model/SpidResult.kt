//
// SPDX-FileCopyrightText: 2021 Istituto Nazionale Previdenza Sociale
//
// SPDX-License-Identifier: BSD-3-Clause

package it.inps.spid.model

import it.inps.spid.utils.SpidEvent
import java.io.Serializable

data class SpidResult(
    var spidEvent: SpidEvent,
    var spidResponse: SpidResponse? = null
) : Serializable
