package com.itacademy.onlineshop.ui.base

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.itacademy.onlineshop.ui.utils.isNetworkAvailable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(context: Application) : AndroidViewModel(context), CoroutineScope {
    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> = _error
    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean> = _progress

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun showProgress() {
        if (isNetworkAvailable) {
            _progress.postValue(true)
        }
    }

    val context: Context
        get() = getApplication()

    val isNetworkAvailable: Boolean
        get() = context.isNetworkAvailable()

    fun hideProgress() {
        _progress.postValue(false)
    }
}