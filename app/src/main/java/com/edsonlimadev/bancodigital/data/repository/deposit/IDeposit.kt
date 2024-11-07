package com.edsonlimadev.bancodigital.data.repository.deposit

import com.edsonlimadev.bancodigital.data.model.Deposit

interface IDeposit {
    suspend fun save(deposit: Deposit): Deposit
    suspend fun getDepositById(id: String): Deposit
}