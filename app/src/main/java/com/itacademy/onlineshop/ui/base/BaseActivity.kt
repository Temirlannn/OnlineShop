package com.itacademy.onlineshop.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.itacademy.onlineshop.ui.utils.ConnectionStateMonitor
import com.itacademy.onlineshop.ui.utils.hide
import com.itacademy.onlineshop.ui.utils.show
import com.pieaksoft.event.consumer.android.utils.hideSystemUI
import com.pieaksoft.event.consumer.android.utils.isUIAvailable
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class BaseActivityNew<VB : ViewBinding>(val bindingFactory: (LayoutInflater) -> VB) :
    AppCompatActivity(), ConnectionStateMonitor.OnNetworkAvailableCallbacks, CoroutineScope {

    val binding: VB by lazy { bindingFactory(layoutInflater) }

    lateinit var pb: ProgressBar

    protected val connectionStateMonitor: ConnectionStateMonitor by lazy {
        ConnectionStateMonitor(this, this)
    }

//    protected val sharedPrefs: SharedPreferences by lazy {
//        getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)
//    }

    private val job by lazy { Job() }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    abstract fun setupView()

    abstract fun bindViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupView()
        bindViewModel()
    }

    override fun onPositive() {
        TODO("Not yet implemented")
    }

    override fun onNegative() {
        TODO("Not yet implemented")
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (hasFocus) {
            lifecycleScope.launch {
                delay(1000L)
                hideSystemUI()
            }
        }
        super.onWindowFocusChanged(hasFocus)
    }

    fun setProgressVisible(visible: Boolean) {
        launch {
            if (!isUIAvailable()) {
                return@launch
            }
            try {
                if (visible && !pb.isVisible) {
                    pb.show()
                } else if (!visible && pb.isVisible) {
                    pb.hide()
                }
            } catch (e: Exception) {
            }
        }
    }
}