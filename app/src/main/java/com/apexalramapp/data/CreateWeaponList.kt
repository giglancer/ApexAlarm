package com.apexalramapp.data

import com.apexalramapp.R

object CreateWeaponList {
    fun weaponTypeList(): List<WeaponType> {
        return listOf(
            WeaponType(R.string.weapon_shotgun, shotgunList(), R.drawable.peace_keeper),
            WeaponType(R.string.weapon_pistol, pistolList(), R.drawable.p2020),
            WeaponType(R.string.weapon_sniper_rifle, sniperRifleList(), R.drawable.kraber),
            WeaponType(R.string.weapon_marksman, marksmanList(), R.drawable.mk30_30),
            WeaponType(R.string.weapon_lmg, lmgList(), R.drawable.spitfire),
            WeaponType(R.string.weapon_smg, smgList(), R.drawable.r_99),
            WeaponType(R.string.weapon_ar, arList(), R.drawable.r_301)
        )
    }

    fun shotgunList(): List<Weapon> {
        return listOf(
            Weapon(R.string.weapon_shotgun_peaceKeeper, R.drawable.peace_keeper, R.raw.peacekeeper),
            Weapon(R.string.weapon_shotgun_mozambique, R.drawable.mozambique, R.raw.mosam),
            Weapon(R.string.weapon_shotgun_mastiff, R.drawable.mastiff, R.raw.mastiff),
            Weapon(R.string.weapon_shotgun_eva_8_auto, R.drawable.eva_8_auto, R.raw.eva_8)
        )
    }

    fun pistolList(): List<Weapon> {
        return listOf(
            Weapon(R.string.weapon_pistol_p2020, R.drawable.p2020, R.raw.p2020),
            Weapon(R.string.weapon_pistol_re_45_auto, R.drawable.re_45_auto, R.raw.re45)
        )
    }

    fun sniperRifleList(): List<Weapon> {
        return listOf(
            Weapon(R.string.weapon_sniper_rifle_charge_rifle, R.drawable.charge_rifle, R.raw.charge),
            Weapon(R.string.weapon_sniper_rifle_longbow, R.drawable.longbow, R.raw.longbow),
            Weapon(R.string.weapon_sniper_rifle_kraber, R.drawable.kraber, R.raw.kraver),
            Weapon(R.string.weapon_sniper_rifle_sentinel, R.drawable.sentinel, R.raw.sentinel),
            Weapon(R.string.weapon_pistol_wingman, R.drawable.wingman, R.raw.wingman)
        )
    }

    fun marksmanList(): List<Weapon> {
        return listOf(
            Weapon(R.string.weapon_marksman_g7, R.drawable.g7, R.raw.g7),
            Weapon(R.string.weapon_marksman_triple_take, R.drawable.triple_take, R.raw.tripletake),
            Weapon(R.string.weapon_marksman_30, R.drawable.mk30_30, R.raw.mk3030),
            Weapon(R.string.weapon_marksman_bocek_compound_bow, R.drawable.bocek, R.raw.bocek)
        )
    }

    fun lmgList(): List<Weapon> {
        return listOf(
            Weapon(R.string.weapon_lmg_devotion, R.drawable.devotion, R.raw.devotion),
            Weapon(R.string.weapon_lmg_l_star, R.drawable.l_star, R.raw.lstar),
            Weapon(R.string.weapon_lmg_rampage, R.drawable.rampage, R.raw.rampage),
            Weapon(R.string.weapon_lmg_spitfire, R.drawable.spitfire, R.raw.spitfire)
        )
    }

    fun smgList(): List<Weapon> {
        return listOf(
            Weapon(R.string.weapon_smg_car, R.drawable.car, R.raw.car),
            Weapon(R.string.weapon_smg_alternator, R.drawable.alternator, R.raw.alternator),
            Weapon(R.string.weapon_smg_r_99, R.drawable.r_99, R.raw.r99),
            Weapon(R.string.weapon_smg_prowler, R.drawable.prowler, R.raw.prowler),
            Weapon(R.string.weapon_smg_volt, R.drawable.volt, R.raw.volt)
        )
    }

    fun arList(): List<Weapon> {
        return listOf(
            Weapon(R.string.weapon_ar_havoc, R.drawable.havoc, R.raw.havoc),
            Weapon(R.string.weapon_ar_flat_line, R.drawable.flat_line, R.raw.flatline),
            Weapon(R.string.weapon_ar_hemlok, R.drawable.hemlok, R.raw.hemlock),
            Weapon(R.string.weapon_ar_r_301_carbine, R.drawable.r_301, R.raw.r301)
        )
    }
}
