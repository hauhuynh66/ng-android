package com.app.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.ngn.R
import java.util.*

class LoginActivity: AppCompatActivity() {
    private lateinit var password:EditText;
    private lateinit var email:EditText;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_login)
        password = findViewById(R.id.password)
        email = findViewById(R.id.email)
        val loginBtn = findViewById<Button>(R.id.loginBtn)
        val clearBtn = findViewById<Button>(R.id.clearBtn)
        clearBtn.setOnClickListener{
            email.text.clear()
            password.text.clear()
        }
        loginBtn.setOnClickListener {
            val success = authenticate(email.text.toString(), password.text.toString())
            if(success){
                val intent = Intent(this, NavigationActivity::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(this, "Bad Credentials", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun authenticate(email: String, password:String): Boolean
    {
        // TODO
        if(email == "test@gmail.com" && password.uppercase(Locale.getDefault()) == "TEST"){
            return true;
        }
        return false
        //TODO
    }
}