package com.itacademy.onlineshop.ui.activities.login

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.itacademy.onlineshop.data.Failure
import com.itacademy.onlineshop.data.Result
import com.itacademy.onlineshop.data.Success
import com.itacademy.onlineshop.data.model.User
import com.itacademy.onlineshop.enums.EventCode
import kotlinx.coroutines.tasks.await

class LoginDataSource(private val auth: FirebaseAuth, private val fireStore: FirebaseFirestore) {

    suspend fun login(email: String, password: String): Result<AuthResult, Exception> {
        return try {
            val data = auth
                .signInWithEmailAndPassword(email, password)
                .await()
            Success(data)
        } catch (e: Exception) {
            Failure(e)
        }
    }

    suspend fun register(email: String, password: String): Result<AuthResult, Exception> {
        return try {
            val data = auth
                .createUserWithEmailAndPassword(email, password)
                .await()
            Success(data)
        } catch (e: Exception) {
            Failure(e)
        }
    }

    suspend fun saveUserToDB(user: User): Result<Boolean, Exception> {
        return try {
            fireStore
                .collection(EventCode.USERS.code)
                .document(user.id.toString())
                .set(user)
                .await()
            Success(true)
        } catch (e: Exception) {
            Failure(e)
        }
    }

    suspend fun getUserFromDB(id: String): Result<User, Exception> {
        return try {
            val user =
                fireStore
                    .collection(EventCode.USERS.code)
                    .document(id)
                    .get()
                    .await()
                    .toObject(User::class.java)
            Success(user!!)
        } catch (e: Exception) {
            Failure(e)
        }
    }

    suspend fun deleteSignedUpUser() {
        auth.currentUser?.delete()?.await()
    }

    fun logout() {
        auth.signOut()
    }
}