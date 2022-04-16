package com.itacademy.onlineshop.data.model

data class User(
    val id: String?,
    val userName: String?,
    val email: String?,
    val profileImage: String? = null,
    val account: Int?=3000
)