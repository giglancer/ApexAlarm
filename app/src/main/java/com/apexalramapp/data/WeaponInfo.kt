package com.apexalramapp.data

data class WeaponInfo(
    val WeaponInfo: List<WeaponType>
)

data class WeaponType(
    val type: Int,
    val weapon: List<Weapon>,
    val icon: Int
)

data class Weapon(
    val name: Int,
    val icon: Int,
    val gunShot: Int
)
