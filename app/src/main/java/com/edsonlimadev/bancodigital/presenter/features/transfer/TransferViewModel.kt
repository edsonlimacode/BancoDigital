package com.edsonlimadev.bancodigital.presenter.features.transfer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.edsonlimadev.bancodigital.data.model.Transaction
import com.edsonlimadev.bancodigital.data.model.Transfer
import com.edsonlimadev.bancodigital.domain.transaction.GetBalanceUseCase
import com.edsonlimadev.bancodigital.domain.transfer.GetTransferUseCase
import com.edsonlimadev.bancodigital.domain.transfer.SaveTransferTransactionUseCase
import com.edsonlimadev.bancodigital.domain.transfer.SaveTransferUseCase
import com.edsonlimadev.bancodigital.utils.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject


@HiltViewModel
class TransferViewModel @Inject constructor(
    private val getBalanceUseCase: GetBalanceUseCase,
    private val getTransferUseCase: GetTransferUseCase,
    private val saveTransferUseCase: SaveTransferUseCase,
    private val saveTransferTransactionUseCase: SaveTransferTransactionUseCase
) : ViewModel() {

    fun getBalance() = liveData(Dispatchers.IO) {
        try {

            emit(StateView.Loading())

            val result = getBalanceUseCase()

            emit(StateView.Sucess(result))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }

    fun saveTransfer(transfer: Transfer) = liveData(Dispatchers.IO) {
        try {

            emit(StateView.Loading())

            saveTransferUseCase(transfer)

            emit(StateView.Sucess(Unit))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }

    fun getTransfer(id: String) = liveData(Dispatchers.IO) {
        try {

            emit(StateView.Loading())

            val trasnfer = getTransferUseCase(id)

            emit(StateView.Sucess(trasnfer))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }

    fun saveTransferTransaction(transfer: Transfer) = liveData(Dispatchers.IO) {
        try {

            emit(StateView.Loading())

            saveTransferTransactionUseCase(transfer)

            emit(StateView.Sucess(Unit))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }

}