package com.edsonlimadev.bancodigital.presenter.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.edsonlimadev.bancodigital.data.model.User
import com.edsonlimadev.bancodigital.domain.profile.GetAllProfileUseCase
import com.edsonlimadev.bancodigital.domain.profile.GetProfileUseCase
import com.edsonlimadev.bancodigital.domain.profile.SaveProfileUseCase
import com.edsonlimadev.bancodigital.domain.profile.UpdateImageProfileUseCase
import com.edsonlimadev.bancodigital.utils.FirebaseHelper
import com.edsonlimadev.bancodigital.utils.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileUseCase: SaveProfileUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val getAllProfileUseCase: GetAllProfileUseCase,
    private val updateImageProfileUseCase: UpdateImageProfileUseCase
) : ViewModel() {

    fun saveProfile(user: User) = liveData(Dispatchers.IO) {
        try {

            emit(StateView.Loading())

            profileUseCase.invoke(user)

            emit(StateView.Sucess(null))


        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }

    fun getProfile(id: String) = liveData(Dispatchers.IO) {
        try {

            emit(StateView.Loading())

            val user = getProfileUseCase(id)

            emit(StateView.Sucess(user))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }

    fun getAllProfile() = liveData(Dispatchers.IO) {
        try {

            emit(StateView.Loading())

            val user = getAllProfileUseCase()

            emit(StateView.Sucess(user))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }

    fun updateImageProfile(uri: Uri) = liveData(Dispatchers.IO) {
        try {

            emit(StateView.Loading())

            val result = updateImageProfileUseCase(uri)

            emit(StateView.Sucess(result))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }
}