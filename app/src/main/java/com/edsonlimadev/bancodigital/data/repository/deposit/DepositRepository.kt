package com.edsonlimadev.bancodigital.data.repository.deposit

import com.edsonlimadev.bancodigital.data.model.Deposit
import com.edsonlimadev.bancodigital.utils.FirebaseHelper
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class DepositRepository @Inject constructor(
    private val database: FirebaseFirestore
) : IDeposit {

    override suspend fun save(deposit: Deposit): Deposit {

        return suspendCoroutine { continuation ->

            deposit.date = System.currentTimeMillis()

            database.collection("deposit")
                .document(FirebaseHelper.getUserId().toString())
                .collection("deposits")
                .document(deposit.id)
                .set(deposit)
                .addOnCompleteListener { taskResult ->
                    if (taskResult.isSuccessful) {

                        continuation.resumeWith(Result.success(deposit))

                    } else {
                        taskResult.exception?.let {
                            continuation.resumeWith(Result.failure(it))
                        }
                    }
                }
        }

    }

    override suspend fun getDepositById(id: String): Deposit {

        return suspendCoroutine { continuation ->

            database.collection("deposit")
                .document(FirebaseHelper.getUserId().toString())
                .collection("deposits")
                .document(id)
                .get()
                .addOnCompleteListener { documentSnapshot ->
                    if (documentSnapshot.isSuccessful) {

                        val deposit = documentSnapshot.result.toObject(Deposit::class.java)

                        deposit?.let {
                            continuation.resumeWith(Result.success(it))
                        }


                    } else {
                        documentSnapshot.exception?.let {
                            continuation.resumeWith(Result.failure(it))
                        }
                    }
                }
        }

    }

}