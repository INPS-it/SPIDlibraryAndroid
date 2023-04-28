// 
// SPDX-FileCopyrightText: 2023 Istituto Nazionale Previdenza Sociale
//
// SPDX-License-Identifier: BSD-3-Clause

package it.inps.spid.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.inps.spid.databinding.ItemProviderBinding
import it.inps.spid.model.IdentityProvider

class IdentityProvidersAdapter(
    private var identityProvidersList: List<IdentityProvider>,
    private val callback: (String) -> Unit
) :
    RecyclerView.Adapter<IdentityProvidersAdapter.IdentityProvidersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IdentityProvidersViewHolder {
        return IdentityProvidersViewHolder(
            ItemProviderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return identityProvidersList.size
    }

    override fun onBindViewHolder(holder: IdentityProvidersViewHolder, position: Int) {
        holder.bind(
            identityProvidersList[position].accessibilityName,
            identityProvidersList[position].logo,
            identityProvidersList[position].idpParameter,
            callback
        )
    }

    class IdentityProvidersViewHolder(private val binding: ItemProviderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            idpName: String?,
            resId: Int,
            idpParameter: String,
            callback: (String) -> Unit
        ) {
            binding.ivProvider.apply {
                setImageResource(resId)
                contentDescription = idpName
                setOnClickListener { callback(idpParameter) }
            }
        }
    }
}