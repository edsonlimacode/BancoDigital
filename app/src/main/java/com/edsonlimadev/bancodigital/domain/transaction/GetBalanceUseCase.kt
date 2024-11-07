package com.edsonlimadev.bancodigital.domain.transaction

import androidx.core.math.MathUtils
import com.edsonlimadev.bancodigital.data.enum.Type
import com.edsonlimadev.bancodigital.data.repository.transaction.TransactionRepository
import javax.inject.Inject

class GetBalanceUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {

    suspend operator fun invoke(): Float {

        var cashIn = 0f
        var cashOut = 0f

        transactionRepository.getTransactions().forEach {

            if (it.type == Type.CASH_IN) {
                cashIn += it.value
            } else {
                cashOut += it.value
            }
        }

        return cashIn - cashOut
    }

}