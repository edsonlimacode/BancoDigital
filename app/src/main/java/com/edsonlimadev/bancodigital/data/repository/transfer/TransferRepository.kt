package com.edsonlimadev.bancodigital.data.repository.transfer

import com.edsonlimadev.bancodigital.data.enum.Operation
import com.edsonlimadev.bancodigital.data.enum.Type
import com.edsonlimadev.bancodigital.data.model.Transaction
import com.edsonlimadev.bancodigital.data.model.Transfer
import com.edsonlimadev.bancodigital.utils.FirebaseHelper
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class TransferRepository @Inject constructor(
    private val database: FirebaseFirestore
) : ITransfer {

    override suspend fun save(transfer: Transfer) {
        return suspendCoroutine { continuation ->
            database.collection("transfer")
                .document(transfer.userSentId)
                .collection("transfers")
                .document(transfer.id)
                .set(transfer)
                .addOnSuccessListener {
                    database.collection("transfer")
                        .document(transfer.userReceiverId)
                        .collection("transfers")
                        .document(transfer.id)
                        .set(transfer)
                        .addOnSuccessListener {
                            continuation.resumeWith(Result.success(Unit))
                        }.addOnFailureListener {
                            continuation.resumeWith(Result.failure(it))
                        }
                }.addOnFailureListener {
                    continuation.resumeWith(Result.failure(it))
                }
        }
    }

    override suspend fun getTransfer(id: String): Transfer {
        return suspendCoroutine { continuation ->
            database.collection("transfer")
                .document(FirebaseHelper.getUserId().toString())
                .collection("transfers")
                .document(id)
                .get()
                .addOnCompleteListener { documentSnapshot ->

                    val transfer = documentSnapshot.result.toObject(Transfer::class.java)

                    transfer?.let {
                        continuation.resumeWith(Result.success(transfer))
                    }


                }.addOnFailureListener {
                    continuation.resumeWith(Result.failure(it))
                }
        }
    }

    override suspend fun saveTransferTransaction(transfer: Transfer) {

        return suspendCoroutine { continuation ->

            val userSentTransaction = Transaction(
                id = transfer.id,
                operation = Operation.TRANSFER,
                type = Type.CASH_OUT,
                value = transfer.value
            )

            val userReceiverTransaction = Transaction(
                id = transfer.id,
                operation = Operation.TRANSFER,
                type = Type.CASH_IN,
                value = transfer.value
            )

            database.collection("transaction")
                .document(transfer.userSentId)
                .collection("transactions")
                .document(transfer.id)
                .set(userSentTransaction)
                .addOnCompleteListener { taskResult ->

                    database.collection("transaction")
                        .document(transfer.userReceiverId)
                        .collection("transactions")
                        .document(transfer.id)
                        .set(userReceiverTransaction)
                        .addOnSuccessListener {
                            continuation.resumeWith(Result.success(Unit))
                        }.addOnFailureListener {

                            continuation.resumeWith(Result.failure(it))
                        }
                }.addOnFailureListener {
                    continuation.resumeWith(Result.failure(it))
                }
        }
    }

}