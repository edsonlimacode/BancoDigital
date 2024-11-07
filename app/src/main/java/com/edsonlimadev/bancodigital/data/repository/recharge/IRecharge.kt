package com.edsonlimadev.bancodigital.data.repository.recharge

import com.edsonlimadev.bancodigital.data.model.Recharge

interface IRecharge {

    suspend fun save(recharge: Recharge): Recharge
    suspend fun getRecharge(id: String): Recharge

}