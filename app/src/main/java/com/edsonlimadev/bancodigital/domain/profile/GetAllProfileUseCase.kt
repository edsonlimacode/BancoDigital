package com.edsonlimadev.bancodigital.domain.profile

import com.edsonlimadev.bancodigital.data.model.User
import com.edsonlimadev.bancodigital.data.repository.profile.ProfileRepository
import javax.inject.Inject

class GetAllProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {

    suspend operator fun invoke(): List<User> {
        return profileRepository.getProfileList()
    }

}