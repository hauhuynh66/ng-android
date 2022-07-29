package com.app.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.app.ngn.R
import com.app.util.Generator.Companion.generateString
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class TestActivity : AppCompatActivity() {
    private val database = FirebaseDatabase.getInstance("https://ng-fb-966ff-default-rtdb.asia-southeast1.firebasedatabase.app")
    private val ref : DatabaseReference = database.getReference("message")
    private val auth = Firebase.auth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_test)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.apply {
            title = "Message"
        }

        val displayUser = if(auth.currentUser!=null){
            auth.currentUser!!.email!!
        }else{
            generateString(10)
        }

        val message : EditText = findViewById(R.id.message_text)

        findViewById<Button>(R.id.btn_send).apply {
            setOnClickListener {
                ref.child(System.currentTimeMillis().toString()).apply {
                    val text = message.text.toString()
                    if(text!=""){
                        child("from").setValue(displayUser)
                        child("content").setValue(text)
                    }
                }
            }
        }
        ref.addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }
}