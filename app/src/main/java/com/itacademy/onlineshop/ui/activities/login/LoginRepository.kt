package com.itacademy.onlineshop.ui.activities.login

import android.util.Log
import com.itacademy.onlineshop.data.Failure
import com.itacademy.onlineshop.data.Result
import com.itacademy.onlineshop.data.Success
import com.itacademy.onlineshop.data.model.LoggedInUser
import com.itacademy.onlineshop.data.model.User

class LoginRepository(val dataSource: LoginDataSource) {

    var user: LoggedInUser? = null
        private set

    init {
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    suspend fun login(email: String, password: String): Result<LoggedInUser, Exception> {

        return when (val result = dataSource.login(email, password)) {
            is Success -> {
                if (result.data.user != null) {
                    val user =
                        LoggedInUser(
                            result.data.user!!.uid,
                            result.data.user!!.displayName.toString()
                        )
                    Log.e("user:", user.toString())
                    getUserFromDB(user.userId)
                } else {
                    Failure(Exception("User is null"))
                }
            }
            is Failure -> Failure(result.error)
        }
    }

    private suspend fun getUserFromDB(userId: String): Result<LoggedInUser, Exception> {
        return when (val user = dataSource.getUserFromDB(userId)) {
            is Success -> {
                setLoggedInUser(LoggedInUser(userId, user.data.userName!!))
                Success(LoggedInUser(userId, user.data.userName))
            }
            is Failure -> {
                logout()
                Failure(Exception("Failed to sign in"))
            }

        }
    }

    suspend fun register(
        userName: String,
        email: String,
        password: String
    ): Result<LoggedInUser, Exception> {
        return when (val result = dataSource.register(email, password)) {
            is Success -> {
                if (result.data.user != null) {
                    val user =
                        LoggedInUser(
                            result.data.user!!.uid,
                            result.data.user!!.displayName.toString()
                        )
                    Log.e("user:", user.toString())

                    saveUserToDB(user.userId, userName, email)
                } else {
                    Failure(Exception("User is null"))
                }
            }
            is Failure -> Failure(result.error)
        }
    }

    private suspend fun saveUserToDB(
        id: String,
        userName: String,
        email: String
    ): Result<LoggedInUser, Exception> {
        val user = User(id, userName, email)
        return when (dataSource.saveUserToDB(user)) {
            is Success -> {
                setLoggedInUser(LoggedInUser(id, userName))
                Success(LoggedInUser(id, userName))
            }
            is Failure -> {
                deleteSignedUpUser()
                Failure(Exception("failed to save user to fire store"))
            }

        }
    }

    private suspend fun deleteSignedUpUser() {
        dataSource.deleteSignedUpUser()
    }


    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
    }
}