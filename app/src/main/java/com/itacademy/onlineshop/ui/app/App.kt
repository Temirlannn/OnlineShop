package com.itacademy.onlineshop.ui.app

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.e("here","Im here")
        FirebaseApp.initializeApp(applicationContext)
    }
}