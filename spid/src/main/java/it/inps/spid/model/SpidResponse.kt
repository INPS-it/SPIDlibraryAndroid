//
// SPDX-FileCopyrightText: 2021 Istituto Nazionale Previdenza Sociale
//
// SPDX-License-Identifier: BSD-3-Clause

package it.inps.spid.model

import java.io.Serializable

data class SpidResponse(
        var cookies: List<String>
) : Serializable
