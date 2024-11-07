package com.edsonlimadev.bancodigital.domain.recharge

import com.edsonlimadev.bancodigital.data.model.Recharge
import com.edsonlimadev.bancodigital.data.repository.recharge.RechargeRepository
import javax.inject.Inject

class GetRechargeUseCase @Inject constructor(
    private val rechargeRepository: RechargeRepository
) {

    suspend operator fun invoke(id: String): Recharge {
        return rechargeRepository.getRecharge(id)
    }
}