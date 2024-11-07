package com.edsonlimadev.bancodigital.presenter.auth.recover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.edsonlimadev.bancodigital.data.model.User
import com.edsonlimadev.bancodigital.domain.auth.RecoverUseCase
import com.edsonlimadev.bancodigital.domain.auth.RegisterUseCase
import com.edsonlimadev.bancodigital.utils.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class RecoverViewModel @Inject constructor(
    private val recoverUseCase: RecoverUseCase
) : ViewModel() {

    fun recover(email:String) = liveData(Dispatchers.IO) {
        try {

            emit(StateView.Loading())

            recoverUseCase.invoke(email)

            emit(StateView.Sucess(email))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }

}