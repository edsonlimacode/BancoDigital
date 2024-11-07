package com.edsonlimadev.bancodigital.domain.deposit

import com.edsonlimadev.bancodigital.data.model.Deposit
import com.edsonlimadev.bancodigital.data.repository.deposit.DepositRepository
import javax.inject.Inject

class CreateDepositUseCase @Inject constructor(
    private val depositRepository: DepositRepository
) {

    suspend operator fun invoke(deposit: Deposit): Deposit {
        return depositRepository.save(deposit)
    }

}