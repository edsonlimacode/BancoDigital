package com.edsonlimadev.bancodigital.domain.transfer

import com.edsonlimadev.bancodigital.data.model.Transfer
import com.edsonlimadev.bancodigital.data.repository.transfer.TransferRepository
import javax.inject.Inject

class SaveTransferTransactionUseCase @Inject constructor(
    private val transferRepository: TransferRepository
) {
    suspend operator fun invoke(transfer: Transfer) {
         transferRepository.saveTransferTransaction(transfer)
    }
}