package com.edsonlimadev.bancodigital.di

import com.edsonlimadev.bancodigital.data.repository.auth.AuthFirebaseDataSource
import com.edsonlimadev.bancodigital.data.repository.auth.AuthFirebaseDataSourceImpl
import com.edsonlimadev.bancodigital.data.repository.profile.IProfile
import com.edsonlimadev.bancodigital.data.repository.profile.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class DomainModule {

   /* @Binds
    abstract fun bindsAuthRepository(
        authFirebaseDataSourceImpl: AuthFirebaseDataSourceImpl
    ): AuthFirebaseDataSource
*/
   /* @Binds
    abstract fun bindsProfileRepository(
        profileRepository: ProfileRepository
    ): IProfile*/

}