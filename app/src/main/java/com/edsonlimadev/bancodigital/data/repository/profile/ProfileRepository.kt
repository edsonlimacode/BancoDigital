package com.edsonlimadev.bancodigital.data.repository.profile

import android.net.Uri
import com.edsonlimadev.bancodigital.data.model.User
import com.edsonlimadev.bancodigital.utils.FirebaseHelper
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.Continuation
import kotlin.coroutines.suspendCoroutine

class ProfileRepository @Inject constructor(
    private val database: FirebaseFirestore,
    private val storage: FirebaseStorage
) : IProfile {

    override suspend fun save(user: User) {

        return suspendCoroutine { continuation ->
            database.collection("profile")
                .document(FirebaseHelper.getUserId().toString())
                .set(user)
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

    override suspend fun getProfile(id: String): User {

        return suspendCoroutine { continuation ->
            database.collection("profile")
                .document(id)
                .get()
                .addOnCompleteListener { documentSnapshot ->

                    val profile = documentSnapshot.result.toObject(User::class.java)

                    profile?.let {
                        continuation.resumeWith(Result.success(it))
                    }

                }.addOnFailureListener {
                    continuation.resumeWith(Result.failure(it))
                }

        }
    }

    override suspend fun getProfileList(): List<User> {
        return suspendCoroutine { continuation ->
            database.collection("profile")
                .get()
                .addOnCompleteListener { documentSnapshot ->

                    val documents = documentSnapshot.result.documents

                    val profileList = mutableListOf<User>()

                    documents.forEach {

                        val profile = it.toObject(User::class.java)

                        profile?.let {
                            profileList.add(it)
                        }
                    }

                    val filteredList = profileList.filter {
                        it.id != FirebaseHelper.getUserId()
                    }

                    continuation.resumeWith(Result.success(filteredList))

                }.addOnFailureListener {
                    continuation.resumeWith(Result.failure(it))
                }
        }
    }

    override suspend fun saveImageProfile(uri: Uri): String {
        return suspendCoroutine { continuation ->

            storage.getReference("profile")
                .child("${FirebaseHelper.getUserId()}.jpeg")
                .putFile(uri)
                .addOnSuccessListener { uploadResult ->

                    uploadResult.metadata?.reference?.downloadUrl?.addOnSuccessListener { uri ->

                        database.collection("profile")
                            .document(FirebaseHelper.getUserId().toString())
                            .update("image", uri.toString())
                            .addOnSuccessListener {
                                continuation.resumeWith(Result.success(uri.toString()))
                            }.addOnFailureListener {
                                continuation.resumeWith(Result.failure(it))
                            }
                    }
                }.addOnFailureListener {
                    continuation.resumeWith(Result.failure(it))
                }
        }
    }
}