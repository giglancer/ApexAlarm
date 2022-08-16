package com.apexalramapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.apexalramapp.data.WeaponType
import com.apexalramapp.databinding.WeaponTypeListRowBinding

abstract class WeaponTypeListAdapter : ListAdapter<WeaponType, WeaponTypeListAdapter.WeaponTypeListViewHolder>(DiffCallback) {
    abstract val clickListener: (weaponType: WeaponType) -> Unit

    class WeaponTypeListViewHolder(private var binding: WeaponTypeListRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(weaponTypeList: WeaponType) {
            binding.property = weaponTypeList
            binding.executePendingBindings()
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<WeaponType>() {
        override fun areItemsTheSame(oldItem: WeaponType, newItem: WeaponType): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: WeaponType, newItem: WeaponType): Boolean {
            return oldItem.type == newItem.type
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeaponTypeListViewHolder {
        return WeaponTypeListViewHolder(WeaponTypeListRowBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: WeaponTypeListViewHolder, position: Int) {
        val weaponTypeProperty = getItem(position)
        holder.itemView.setOnClickListener { clickListener.invoke(weaponTypeProperty) }
        holder.bind(weaponTypeProperty)
    }
}
