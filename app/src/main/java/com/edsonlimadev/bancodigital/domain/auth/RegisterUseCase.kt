package com.edsonlimadev.bancodigital.domain.auth

import com.edsonlimadev.bancodigital.data.model.User
import com.edsonlimadev.bancodigital.data.repository.auth.AuthFirebaseDataSourceImpl
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthFirebaseDataSourceImpl
) {
    suspend operator fun invoke(email: String, password: String) {
        return authRepository.register(email, password)
    }

}