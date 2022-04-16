package com.itacademy.onlineshop.ui.activities.login

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.itacademy.onlineshop.R
import com.itacademy.onlineshop.data.Failure
import com.itacademy.onlineshop.data.Success
import com.itacademy.onlineshop.data.model.LoginFormState
import com.itacademy.onlineshop.data.model.LoginResult
import com.itacademy.onlineshop.ui.utils.showToast
import com.itacademy.onlineshop.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(private val loginRepository: LoginRepository, context: Application) :
    BaseViewModel(context) {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(email: String, password: String) {
        showProgress()

        launch {
            if (isNetworkAvailable) {
                val result =
                    withContext(Dispatchers.IO) { loginRepository.login(email, password) }

                hideProgress()

                when (result) {
                    is Success -> _loginResult.value =
                        LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
                    is Failure -> _loginResult.value = LoginResult(error = result.error.message)
                }
            } else context.showToast(context.getString(R.string.no_internet))
        }
    }

    fun register(email:String,password: String,userName:String){
        showProgress()

        launch {
            if (isNetworkAvailable) {
                val result =
                    withContext(Dispatchers.IO) { loginRepository.login(email, password) }

                hideProgress()

                when (result) {
                    is Success -> _loginResult.value =
                        LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
                    is Failure -> _loginResult.value = LoginResult(error = result.error.message)
                }
            } else context.showToast(context.getString(R.string.no_internet))
        }
    }

    fun loginDataChanged(email: String, password: String) {
        if (!isEmailValid(email)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_email)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isEmailValid(email: String): Boolean {
        return if (email.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            email.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}