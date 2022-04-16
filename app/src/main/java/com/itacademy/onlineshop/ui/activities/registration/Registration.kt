package com.itacademy.onlineshop.ui.activities.registration

import android.content.Context
import android.content.Intent
import com.itacademy.onlineshop.databinding.ActivitySignupBinding
import com.itacademy.onlineshop.ui.activities.login.LoginViewModel
import com.itacademy.onlineshop.ui.base.BaseActivityNew
import com.pieaksoft.event.consumer.android.utils.newIntent

class Registration : BaseActivityNew<ActivitySignupBinding>(ActivitySignupBinding::inflate) {
    private lateinit var loginViewModel: LoginViewModel

    override fun setupView() {
        TODO("Not yet implemented")
    }

    override fun bindViewModel() {
        TODO("Not yet implemented")
    }

    companion object{
        fun newInstance(context: Context): Intent {
            return newIntent<Registration>(context).apply {

            }
        }
    }
}