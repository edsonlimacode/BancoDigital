package com.edsonlimadev.bancodigital.utils

import com.edsonlimadev.bancodigital.R
import com.google.firebase.auth.FirebaseAuth

abstract class FirebaseHelper {

    companion object {

        fun getUserId() = FirebaseAuth.getInstance().currentUser?.uid

        fun isAuthenticated(): Boolean = FirebaseAuth.getInstance().currentUser != null

        fun validError(error: String): Int {
            return when {
                error.contains("The email address is badly formatted") -> {
                    R.string.invalid_email_register_fragment
                }

                error.contains("The supplied auth credential is incorrect, malformed or has expired") -> {
                    R.string.invalid_credentials
                }

                error.contains("The email address is already in use by another account") -> {
                    R.string.email_in_use_register_fragment
                }

                error.contains("The given password is invalid. [ Password should be at least 6 characters ]") -> {
                    R.string.strong_password_register_fragment
                }

                else -> R.string.generic_error
            }
        }
    }

}