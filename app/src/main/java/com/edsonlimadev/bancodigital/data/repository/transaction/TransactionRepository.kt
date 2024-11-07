package com.edsonlimadev.bancodigital.data.repository.transaction

import com.edsonlimadev.bancodigital.data.enum.Type
import com.edsonlimadev.bancodigital.data.model.Transaction
import com.edsonlimadev.bancodigital.utils.FirebaseHelper
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class TransactionRepository @Inject constructor(
    private val database: FirebaseFirestore
) : ITransaction {


    override suspend fun save(transaction: Transaction) {

        return suspendCoroutine { continuation ->

            database.collection("transaction")
                .document(FirebaseHelper.getUserId().toString())
                .collection("transactions")
                .document(transaction.id)
                .set(transaction)
                .addOnCompleteListener { taskResult ->
                    if (taskResult.isSuccessful) {
                        continuation.resumeWith(Result.success(Unit))
                    } else {
                        taskResult.exception?.let {
                            continuation.resumeWith(Result.failure(it))
                        }
                    }
                }
        }
    }

    override suspend fun getTransactions(): List<Transaction> {

        return suspendCoroutine { continuation ->

            database.collection("transaction")
                .document(FirebaseHelper.getUserId().toString())
                .collection("transactions")
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener { querySnapshot ->

                    val documentSnapshot = querySnapshot.result.documents

                    val transactions = mutableListOf<Transaction>()

                    documentSnapshot.forEach { documentSnapshot ->

                        val transaction = documentSnapshot.toObject(Transaction::class.java)

                        transaction?.let {
                            transactions.add(transaction)
                        }
                    }

                    continuation.resumeWith(Result.success(transactions))

                }.addOnFailureListener {
                    continuation.resumeWith(Result.failure(it))
                }
        }
    }
}