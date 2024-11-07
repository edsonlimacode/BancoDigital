package com.edsonlimadev.bancodigital.data.repository.recharge

import com.edsonlimadev.bancodigital.data.model.Recharge
import com.edsonlimadev.bancodigital.utils.FirebaseHelper
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class RechargeRepository @Inject constructor(
    private val database: FirebaseFirestore
) : IRecharge {

    override suspend fun save(recharge: Recharge): Recharge {

        return suspendCoroutine { continuation ->
            database.collection("recharge")
                .document(FirebaseHelper.getUserId().toString())
                .collection("recharges")
                .document(recharge.id)
                .set(recharge)
                .addOnSuccessListener {
                    continuation.resumeWith(Result.success(recharge))
                }.addOnFailureListener {
                    continuation.resumeWith(Result.failure(it))
                }
        }
    }

    override suspend fun getRecharge(id: String): Recharge {
        return suspendCoroutine { continuation ->
            database.collection("recharge")
                .document(FirebaseHelper.getUserId().toString())
                .collection("recharges")
                .document(id)
                .get()
                .addOnCompleteListener { documentSnapshot ->

                    if (documentSnapshot.isSuccessful) {

                        val recharge = documentSnapshot.result.toObject(Recharge::class.java)

                        recharge?.let {
                            continuation.resumeWith(Result.success(it))
                        }
                    }

                }.addOnFailureListener {
                    continuation.resumeWith(Result.failure(it))
                }
        }
    }
}