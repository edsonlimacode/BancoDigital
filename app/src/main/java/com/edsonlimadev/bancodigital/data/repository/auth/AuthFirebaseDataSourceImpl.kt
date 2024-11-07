package com.edsonlimadev.bancodigital.data.repository.auth

import com.edsonlimadev.bancodigital.data.model.User
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class AuthFirebaseDataSourceImpl @Inject constructor(
    val auth: FirebaseAuth
) : AuthFirebaseDataSource {
    override suspend fun login(email: String, password: String) {
        return suspendCoroutine { continuation ->
            auth.signInWithEmailAndPassword(
                email,
                password
            ).addOnCompleteListener { authResult ->

                if (authResult.isSuccessful) {
                    continuation.resumeWith(Result.success(Unit))
                } else {
                    authResult.exception?.let {
                        continuation.resumeWith(Result.failure(it))
                    }
                }
            }
        }
    }

    override suspend fun register(
       email: String, password: String
    ) {

        return suspendCoroutine { continuation ->
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { authResult ->

                    if (authResult.isSuccessful) {
                        continuation.resumeWith(Result.success(Unit))
                    } else {

                        authResult.exception?.let {
                            continuation.resumeWith(Result.failure(it))
                        }

                    }
                }
        }

    }

    override suspend fun recover(email: String) {
        return suspendCoroutine { continuation ->
            auth.sendPasswordResetEmail(
                email
            ).addOnCompleteListener { authResult ->
                if (authResult.isSuccessful) {
                    continuation.resumeWith(Result.success(Unit))
                } else {
                    authResult.exception?.let {
                        continuation.resumeWith(Result.failure(it))
                    }
                }
            }
        }
    }
}