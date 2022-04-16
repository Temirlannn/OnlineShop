package com.itacademy.onlineshop.data.model

import com.itacademy.onlineshop.ui.activities.login.LoggedInUserView

data class LoginResult(
    val success: LoggedInUserView? = null,
    val error: String? = null
)