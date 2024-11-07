package com.edsonlimadev.bancodigital.presenter.features.deposit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.edsonlimadev.bancodigital.data.model.Deposit
import com.edsonlimadev.bancodigital.domain.deposit.CreateDepositUseCase
import com.edsonlimadev.bancodigital.domain.deposit.GetDepositUseCase
import com.edsonlimadev.bancodigital.utils.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class DepositViewModel @Inject constructor(
    private val createDepositUseCase: CreateDepositUseCase,
    private val getDepositUseCase:  GetDepositUseCase
) : ViewModel() {

    fun create(deposit: Deposit) = liveData(Dispatchers.IO) {
        try {

            emit(StateView.Loading())

            val result = createDepositUseCase(deposit)

            emit(StateView.Sucess(result))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }

    fun getDepositById(id: String) = liveData(Dispatchers.IO) {
        try {

            emit(StateView.Loading())

            val deposit = getDepositUseCase(id)

            emit(StateView.Sucess(deposit))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }
}