package com.edsonlimadev.bancodigital.domain.profile

import android.net.Uri
import com.edsonlimadev.bancodigital.data.model.User
import com.edsonlimadev.bancodigital.data.repository.profile.ProfileRepository
import javax.inject.Inject

class UpdateImageProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {

    suspend operator fun invoke(uri: Uri): String {
        return profileRepository.saveImageProfile(uri)
    }

}