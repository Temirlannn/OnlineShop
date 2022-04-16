package com.itacademy.onlineshop.enums

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal enum class EventCode(val code: String) : Parcelable {
    USERS("USERS"),
    NONE("NONE");

    companion object {
        private val DEFAULT = NONE
        fun findByCode(code: String) = values().find { it.code.contentEquals(code) } ?: DEFAULT
    }
}