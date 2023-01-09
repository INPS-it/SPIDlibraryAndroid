//
// SPDX-FileCopyrightText: 2023 Istituto Nazionale Previdenza Sociale
//
// SPDX-License-Identifier: BSD-3-Clause

package it.inps.spid.utils

enum class SpidEvent {
    GENERIC_ERROR,
    NETWORK_ERROR,
    SESSION_TIMEOUT,
    SPID_CONFIG_ERROR,
    SUCCESS,
    USER_CANCELLED
}