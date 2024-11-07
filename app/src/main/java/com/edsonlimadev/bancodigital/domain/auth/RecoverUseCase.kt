package com.edsonlimadev.bancodigital.domain.auth

import com.edsonlimadev.bancodigital.data.repository.auth.AuthFirebaseDataSourceImpl
import javax.inject.Inject

class RecoverUseCase @Inject constructor(
    private val authRepository: AuthFirebaseDataSourceImpl
) {
    suspend operator fun invoke(email: String){
      return authRepository.recover(email)
    }

}