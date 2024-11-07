package com.edsonlimadev.bancodigital.domain.recharge

import com.edsonlimadev.bancodigital.data.model.Recharge
import com.edsonlimadev.bancodigital.data.repository.recharge.RechargeRepository
import javax.inject.Inject

class CreateRechargeUseCase @Inject constructor(
    private val rechargeRepository: RechargeRepository
) {

    suspend operator fun invoke(recharge: Recharge): Recharge {
        return rechargeRepository.save(recharge)
    }
}