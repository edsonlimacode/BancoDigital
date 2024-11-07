package com.edsonlimadev.bancodigital.data.repository.transfer

import com.edsonlimadev.bancodigital.data.model.Transfer

interface ITransfer {
    suspend fun save(transfer: Transfer)

    suspend fun getTransfer(id: String): Transfer

    suspend fun saveTransferTransaction(transfer: Transfer)
}