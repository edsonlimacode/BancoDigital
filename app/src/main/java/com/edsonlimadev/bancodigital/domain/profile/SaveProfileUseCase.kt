package com.edsonlimadev.bancodigital.domain.profile

import com.edsonlimadev.bancodigital.data.model.User
import com.edsonlimadev.bancodigital.data.repository.profile.ProfileRepository
import javax.inject.Inject

class SaveProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {

    suspend operator fun invoke(user: User){
        return profileRepository.save(user)
    }

}