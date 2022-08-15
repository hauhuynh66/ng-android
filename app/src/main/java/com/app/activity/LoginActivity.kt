package com.app.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.app.fragment.lg.EmailPasswordLoginFragment
import com.app.fragment.lg.LoginOptionsFragment
import com.app.ngn.R
import com.app.viewmodel.Login


class LoginActivity: AppCompatActivity() {
    private val viewModel : Login by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_holder)
        if(viewModel.auth.currentUser!=null){
            val intent = Intent(this, NavigatorActivity::class.java)
            startActivity(intent)
        }
        supportFragmentManager.beginTransaction().replace(R.id.container, LoginOptionsFragment()).commit()
        viewModel.currentState.observe(this) {
            when (it) {
                0->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, LoginOptionsFragment()).commit()
                }
                1 -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, EmailPasswordLoginFragment()).commit()
                }
                2 -> {
                    viewModel.init(this)
                    viewModel.googleSignIn()
                }
                else -> {

                }
            }
        }
    }

    override fun onBackPressed() {
        viewModel.setState(0)
    }
}