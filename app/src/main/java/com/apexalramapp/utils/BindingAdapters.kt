package com.apexalramapp.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.apexalramapp.adapter.WeaponDetailListAdapter
import com.apexalramapp.adapter.WeaponTypeListAdapter
import com.apexalramapp.data.Weapon
import com.apexalramapp.data.WeaponType

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<WeaponType>?) {
    val adapter = recyclerView.adapter as WeaponTypeListAdapter
    adapter.submitList(data)
}

@BindingAdapter("weaponDetailListData")
fun weaponDetailListDataBindRecyclerView(recyclerView: RecyclerView, data: List<Weapon>?) {
    val adapter = recyclerView.adapter as WeaponDetailListAdapter
    adapter.submitList(data)
}

@BindingAdapter("setSrc")
fun ImageView.setImageResource(resourceId: Int) {
    setImageResource(resourceId)
}
