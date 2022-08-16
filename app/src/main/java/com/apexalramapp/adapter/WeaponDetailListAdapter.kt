package com.apexalramapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.apexalramapp.data.Weapon
import com.apexalramapp.databinding.WeaponDetailListRowBinding
import kotlinx.android.synthetic.main.weapon_detail_list_row.view.*
import kotlinx.android.synthetic.main.weapon_type_list_row.view.*

abstract class WeaponDetailListAdapter : ListAdapter<Weapon, WeaponDetailListAdapter.WeaponDetailListViewHolder>(DiffCallback) {
    abstract val weaponDetailClickListener: (weapon: Weapon) -> Unit

    class WeaponDetailListViewHolder(private var binding: WeaponDetailListRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(weaponList: Weapon) {
            binding.properties = weaponList
            binding.executePendingBindings()
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<Weapon>() {
        override fun areItemsTheSame(oldItem: Weapon, newItem: Weapon): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Weapon, newItem: Weapon): Boolean {
            return oldItem.name == newItem.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeaponDetailListViewHolder {
        return WeaponDetailListViewHolder(WeaponDetailListRowBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: WeaponDetailListViewHolder, position: Int) {
        val weaponDetailProperty = getItem(position)
        holder.itemView.weapon_detail_img.setOnClickListener { weaponDetailClickListener.invoke(weaponDetailProperty) }
        holder.bind(weaponDetailProperty)
    }
}
