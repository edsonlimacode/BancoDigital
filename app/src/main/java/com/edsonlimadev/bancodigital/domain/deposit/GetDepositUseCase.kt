package com.edsonlimadev.bancodigital.domain.deposit

import com.edsonlimadev.bancodigital.data.model.Deposit
import com.edsonlimadev.bancodigital.data.repository.deposit.DepositRepository
import javax.inject.Inject

class GetDepositUseCase @Inject constructor(
    private val depositRepository: DepositRepository
) {

    suspend operator fun invoke(id: String): Deposit {
        return depositRepository.getDepositById(id)
    }

}