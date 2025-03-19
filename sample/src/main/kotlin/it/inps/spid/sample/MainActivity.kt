// 
// SPDX-FileCopyrightText: 2025 Istituto Nazionale Previdenza Sociale
//
// SPDX-License-Identifier: BSD-3-Clause

package it.inps.spid.sample

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import it.inps.spid.activity.IdentityProviderSelectorActivityContract
import it.inps.spid.model.IdentityProvider
import it.inps.spid.model.SpidParams
import it.inps.spid.sample.databinding.ActivityMainBinding
import it.inps.spid.utils.SpidEvent

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName

    private lateinit var binding: ActivityMainBinding

    private val startSpidFlow =
        registerForActivityResult(IdentityProviderSelectorActivityContract()) { spidResult ->
            when (spidResult.spidEvent) {
                SpidEvent.GENERIC_ERROR -> {
                    Toast.makeText(this@MainActivity, "GENERIC_ERROR", Toast.LENGTH_LONG).show()
                }
                SpidEvent.NETWORK_ERROR -> {
                    MaterialAlertDialogBuilder(this@MainActivity)
                        .setTitle(R.string.attention)
                        .setMessage(R.string.msg_no_connection)
                        .setPositiveButton(android.R.string.ok) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
                SpidEvent.SESSION_TIMEOUT -> {
                    Toast.makeText(this@MainActivity, "SESSION_TIMEOUT", Toast.LENGTH_LONG).show()
                }
                SpidEvent.SPID_CONFIG_ERROR -> {
                    Toast.makeText(this@MainActivity, "SPID_CONFIG_ERROR", Toast.LENGTH_LONG).show()
                }
                SpidEvent.SUCCESS -> {
                    Log.i(TAG, "cookies -> ${spidResult.spidResponse?.cookies}")
                    MaterialAlertDialogBuilder(this@MainActivity)
                        .setMessage(R.string.login_succesfull)
                        .setPositiveButton(android.R.string.ok) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
                SpidEvent.USER_CANCELLED -> {
                    Toast.makeText(this@MainActivity, "USER_CANCELLED", Toast.LENGTH_LONG).show()
                }
                else -> Toast.makeText(this@MainActivity, "UNKNOWN_ERROR", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnLoginSpid.setOnClickListener {
            val spidConfig = SpidParams.Config(
                "", // TODO
                "", // TODO
                60
            )
            val idpList = IdentityProvider.Builder()
                .addAruba(idpParameter = "")
                .addPoste(idpParameter = "")
                .addTim(idpParameter = "")
                .addTeamSystem(idpParameter = "")
                .addCustomIdentityProvider(
                    "CUSTOM IDENTITY PROVIDER",
                    R.drawable.ic_spid_idp_custom,
                    ""
                )
                // TODO
                .build()

            startSpidFlow.launch(SpidParams(spidConfig, idpList))
        }
    }
}