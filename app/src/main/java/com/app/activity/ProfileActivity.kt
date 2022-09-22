package com.app.activity

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.appcompat.widget.Toolbar
import com.app.ngn.R
import com.app.viewmodel.Auth
import com.google.firebase.auth.UserProfileChangeRequest
import com.squareup.picasso.Picasso

class ProfileActivity : AppCompatActivity() {
    private val firebase : Auth by viewModels()
    private lateinit var displayName : TextView
    private lateinit var photoUrl : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_profile)

        val toolbar = (findViewById<LinearLayoutCompat>(R.id.toolbar_holder)).findViewById<Toolbar>(R.id.toolbar)
        val title = toolbar.findViewById<TextView>(R.id.title)
        title.text = "Profile"

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        displayName = findViewById(R.id.display_name)
        photoUrl = findViewById(R.id.photo_uri)

        firebase.currentAuth.currentUser!!.apply {
            findViewById<TextView>(R.id.email).text = email
            if(displayName!=null){
                findViewById<EditText>(R.id.display_name).setText(displayName)
            }
            if(photoUrl!=null){
                findViewById<EditText>(R.id.photo_uri).setText(photoUrl.toString())
                Picasso.get().load(photoUrl).into(findViewById<ImageView>(R.id.photo))
            }

        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        when(item.itemId){
            android.R.id.home->{
                if(displayName.text == "" || photoUrl.text == ""){
                    //Display Toast

                }else{
                    if(checkChange()){
                        val builder = AlertDialog.Builder(this)
                            .setCancelable(false)
                            .setMessage("Do you want to save changes?")
                            .setPositiveButton("Yes"
                            ) { di, _ ->
                                updateProfile(displayName.text.toString(), photoUrl.text.toString(),object : Listener{
                                    override fun onSuccess() {
                                        di.dismiss()
                                        onBackPressed()
                                    }

                                    override fun onFailure() {
                                        Toast.makeText(this@ProfileActivity, "Update failed", Toast.LENGTH_SHORT).show()
                                    }
                                })
                            }
                            .setNegativeButton("No"){
                                    di, _ ->
                                run {
                                    di.dismiss()
                                    onBackPressed()
                                }
                            }
                        builder.show()
                    }else{
                        onBackPressed()
                    }
                }
            }
            else->{

            }
        }

        return true
    }

    private fun updateProfile(displayName : String, photoUrl : String, listener : Listener) {
        firebase.currentAuth.currentUser!!.apply {
            val update = UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .setPhotoUri(Uri.parse(photoUrl))
                .build()
            this.updateProfile(update)
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        listener.onSuccess()
                    }
                }.addOnFailureListener {
                    listener.onFailure()
                }
        }
    }

    private fun checkChange() : Boolean{
        firebase.currentAuth.currentUser!!.apply {
            if(this.displayName!=this@ProfileActivity.displayName.text || this.photoUrl.toString()!=this@ProfileActivity.photoUrl.text){
                return true
            }
            return false
        }
    }

    interface Listener{
        fun onSuccess()
        fun onFailure()
    }
}