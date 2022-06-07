package com.app.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.app.ngn.R
import java.util.*
import org.json.JSONObject




class LoginActivity: AppCompatActivity() {
    private lateinit var password:EditText;
    private lateinit var email:EditText;
    private lateinit var requestQueue: RequestQueue
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_login)
        requestQueue = Volley.newRequestQueue(this)
        password = findViewById(R.id.password)
        email = findViewById(R.id.email)
        val loginBtn = findViewById<Button>(R.id.loginBtn)
        val clearBtn = findViewById<Button>(R.id.clearBtn)
        clearBtn.setOnClickListener{
            email.text.clear()
            password.text.clear()
        }
        loginBtn.setOnClickListener {
            val body = JSONObject()
            body.put("username", this.email.text.toString())
            body.put("password", this.password.text.toString())
            val success = true
            if(success){
                val intent = Intent(this, NavigationActivity::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(this, "Bad Credentials", Toast.LENGTH_SHORT).show()
            }
        }
    }
}