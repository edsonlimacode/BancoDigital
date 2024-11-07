package com.edsonlimadev.bancodigital.data.repository.transaction

import com.edsonlimadev.bancodigital.data.model.Transaction

interface ITransaction {

    suspend fun save(transaction: Transaction)
    suspend fun getTransactions(): List<Transaction>
}