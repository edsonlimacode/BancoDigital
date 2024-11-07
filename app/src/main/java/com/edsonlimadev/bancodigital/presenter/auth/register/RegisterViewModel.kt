package com.edsonlimadev.bancodigital.presenter.auth.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.edsonlimadev.bancodigital.data.model.User
import com.edsonlimadev.bancodigital.domain.auth.RegisterUseCase
import com.edsonlimadev.bancodigital.utils.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    fun register(email:String, password:String) = liveData(Dispatchers.IO) {
        try {

            emit(StateView.Loading())

            registerUseCase.invoke(email, password)

            emit(StateView.Sucess(null))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }

}