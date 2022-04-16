package com.itacademy.onlineshop.ui.activities.login

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast

import com.itacademy.onlineshop.R
import com.itacademy.onlineshop.databinding.ActivityLoginoBinding
import com.itacademy.onlineshop.ui.activities.registration.Registration
import com.itacademy.onlineshop.ui.base.BaseActivityNew
import com.itacademy.onlineshop.ui.utils.showToast

class LoginActivity : BaseActivityNew<ActivityLoginoBinding>(ActivityLoginoBinding::inflate) {

    private lateinit var loginViewModel: LoginViewModel

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun setupView() {
        pb = binding.progressBar

        val email = binding.email
        val password = binding.password
        val login = binding.continueButton
        val signup = binding.signup

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both email / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                email.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        signup.setOnClickListener {
            startActivity(Registration.newInstance(this@LoginActivity))
        }

        loginViewModel.progress.observe(this@LoginActivity) {
            setProgressVisible(it)
        }

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            if (loginResult.error != null) {
                applicationContext.showToast(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }
            //Complete and destroy login activity once successful
        })

        email.afterTextChanged {
            loginViewModel.loginDataChanged(
                email.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    email.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            email.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
                loginViewModel.login(email.text.toString(), password.text.toString())
            }
        }
    }

    override fun bindViewModel() {
        loginViewModel =
            ViewModelProvider(
                this,
                LoginViewModelFactory(context = application)
            )[LoginViewModel::class.java]
    }
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}