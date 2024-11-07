package com.edsonlimadev.bancodigital.data.repository.auth

import com.edsonlimadev.bancodigital.data.model.User
import com.google.firebase.auth.FirebaseUser

interface AuthFirebaseDataSource {
    suspend fun login(email: String, password: String)
    suspend fun register(email: String, password: String)
    suspend fun recover(email: String)
}