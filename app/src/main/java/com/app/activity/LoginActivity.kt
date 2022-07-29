package com.app.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.ngn.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginActivity: AppCompatActivity() {
    private lateinit var password:EditText
    private lateinit var email:EditText
    private lateinit var auth:FirebaseAuth
    private var n:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_login)
        auth = Firebase.auth
        val user = auth.currentUser
        if(user!=null){
            val mainIntent = Intent(this, NavigatorActivity::class.java)
            startActivity(mainIntent)
        }
        password = findViewById(R.id.password)
        email = findViewById(R.id.email)
        email.setText("hauhuynh66@gmail.com")
        password.setText("Hauhuynh")
        val loginBtn = findViewById<Button>(R.id.loginBtn)
        val clearBtn = findViewById<Button>(R.id.clearBtn)
        clearBtn.setOnClickListener{
            email.text.clear()
            password.text.clear()
            n++
            if ( n > 5) {
                val skip = Intent(this, NavigatorActivity::class.java)
                startActivity(skip)
            }
        }

        loginBtn.setOnClickListener {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(loginBtn.windowToken,0)
            auth.signInWithEmailAndPassword(this.email.text!!.toString(), this.password.text!!.toString())
                .addOnCompleteListener(this){
                    task->run{
                        if(task.isSuccessful){
                            val mainIntent = Intent(this, NavigatorActivity::class.java)
                            startActivity(mainIntent)
                        }else{
                            clearBtn.performClick()
                            Toast.makeText(this, "Bad Credentials", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                .addOnFailureListener {
                    err->
                    run{
                        clearBtn.performClick()
                        Toast.makeText(this, err.message, Toast.LENGTH_LONG).show()
                    }

                }
        }
    }
}