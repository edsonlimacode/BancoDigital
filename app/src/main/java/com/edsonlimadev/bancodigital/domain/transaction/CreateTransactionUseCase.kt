package com.edsonlimadev.bancodigital.domain.transaction

import com.edsonlimadev.bancodigital.data.model.Transaction
import com.edsonlimadev.bancodigital.data.repository.transaction.TransactionRepository
import javax.inject.Inject

class CreateTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(trasaction: Transaction) {
        transactionRepository.save(trasaction)
    }
}