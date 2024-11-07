package com.edsonlimadev.bancodigital.data.repository.profile

import android.net.Uri
import com.edsonlimadev.bancodigital.data.model.User

interface IProfile {

    suspend fun save(user: User)
    suspend fun getProfile(id: String): User
    suspend fun getProfileList(): List<User>
    suspend fun saveImageProfile(uri: Uri): String
}