package com.edsonlimadev.bancodigital.presenter.features.recharge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.edsonlimadev.bancodigital.data.model.Recharge
import com.edsonlimadev.bancodigital.domain.recharge.CreateRechargeUseCase
import com.edsonlimadev.bancodigital.domain.recharge.GetRechargeUseCase
import com.edsonlimadev.bancodigital.utils.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class RechargeViewModel @Inject constructor(
    private val createRechargeUseCase: CreateRechargeUseCase,
    private val getRechargeUseCase: GetRechargeUseCase
) : ViewModel() {

    fun save(recharge: Recharge) = liveData(Dispatchers.IO) {

        try {
            emit(StateView.Loading())

            val result = createRechargeUseCase(recharge)

            emit(StateView.Sucess(result))
        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }


    }

    fun getRecharge(id: String) = liveData(Dispatchers.IO) {

        try {
            emit(StateView.Loading())

            val result = getRechargeUseCase(id)

            emit(StateView.Sucess(result))
        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }

}