package com.app.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.app.fragment.EmailPasswordLoginFragment
import com.app.fragment.LoginOptionsFragment
import com.app.ngn.R
import com.app.viewmodel.Authentication


class LoginActivity: AppCompatActivity() {
    private val viewModel : Authentication by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_login)

        if(viewModel.firebaseAuth.currentUser!=null){
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
                99 -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, EmailPasswordLoginFragment()).commit()
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