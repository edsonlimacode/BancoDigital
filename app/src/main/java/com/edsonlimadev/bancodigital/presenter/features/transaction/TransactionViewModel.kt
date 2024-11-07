package com.edsonlimadev.bancodigital.presenter.features.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.edsonlimadev.bancodigital.data.model.Transaction
import com.edsonlimadev.bancodigital.domain.transaction.CreateTransactionUseCase
import com.edsonlimadev.bancodigital.domain.transaction.GetTransactionsUseCase
import com.edsonlimadev.bancodigital.utils.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val createTransactionUseCase: CreateTransactionUseCase,
    private val getTransactionsUseCase: GetTransactionsUseCase
) : ViewModel() {

    fun create(transaction: Transaction) = liveData(Dispatchers.IO) {
        try {

            emit(StateView.Loading())

            createTransactionUseCase(transaction)

            emit(StateView.Sucess(Unit))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }

    fun getTransactions() = liveData(Dispatchers.IO) {
        try {

            emit(StateView.Loading())

           val transactions = getTransactionsUseCase()

            emit(StateView.Sucess(transactions))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }

}