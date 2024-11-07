package com.edsonlimadev.bancodigital.domain.transaction

import com.edsonlimadev.bancodigital.data.model.Transaction
import com.edsonlimadev.bancodigital.data.repository.transaction.TransactionRepository
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {

    suspend operator fun invoke(): List<Transaction> {
        return transactionRepository.getTransactions()
    }

}