package com.edsonlimadev.bancodigital.domain.profile

import com.edsonlimadev.bancodigital.data.model.User
import com.edsonlimadev.bancodigital.data.repository.profile.ProfileRepository
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {

    suspend operator fun invoke(id: String): User {
        return profileRepository.getProfile(id)
    }

}