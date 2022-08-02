package com.app.activity

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.app.data.Message
import com.app.fragment.MessageFragment
import com.app.ngn.R
import com.app.util.Generator.Companion.generateString
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class MessageActivity : AppCompatActivity() {
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
                val id = System.currentTimeMillis().toString()
                val ms = ref.child(id)
                val text = message.text.toString()
                ms.setValue(Message(displayUser, text))
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(this.windowToken,0)
                message.setText("")
            }
        }

        supportFragmentManager.beginTransaction().replace(R.id.container, MessageFragment(arrayListOf())).commit()

        ref.addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val fragment = supportFragmentManager.findFragmentById(R.id.container)
                val ms : Message? = snapshot.getValue(Message::class.java)
                if(fragment is MessageFragment && ms!=null){
                    fragment.add(ms)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val removed = snapshot.getValue(Message::class.java)
                val fragment = supportFragmentManager.findFragmentById(R.id.container)
                if(fragment is MessageFragment && removed!=null){
                    println(removed.content)
                    fragment.remove(removed)
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

}