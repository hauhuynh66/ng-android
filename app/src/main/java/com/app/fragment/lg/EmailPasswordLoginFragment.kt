package com.app.fragment.lg

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.app.activity.NavigatorActivity
import com.app.ngn.R
import com.app.util.Animation
import com.app.viewmodel.Login

class EmailPasswordLoginFragment : Fragment() {
    private val viewModel : Login by activityViewModels()
    private lateinit var progressView : ProgressBar
    private lateinit var contentView : ConstraintLayout
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fg_email_password_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressView = view.findViewById(R.id.progress)
        contentView = view.findViewById(R.id.content)
        val password = view.findViewById<EditText>(R.id.password)
        val email = view.findViewById<EditText>(R.id.email)
        email.setText("hauhuynh66@gmail.com")
        password.setText("Hauhuynh")
        val loginBtn = view.findViewById<Button>(R.id.loginBtn)
        val clearBtn = view.findViewById<Button>(R.id.clearBtn)
        clearBtn.setOnClickListener{
            email.text.clear()
            password.text.clear()
        }

        loginBtn.setOnClickListener {
            if (email.text == null || password.text == null) {
                //
            } else {
                Animation.crossfade(arrayListOf(progressView), arrayListOf(contentView), 1000)
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(loginBtn.windowToken,0)
                viewModel.auth.signInWithEmailAndPassword(email.text!!.toString(), password.text!!.toString())
                    .addOnCompleteListener{
                            task->run{
                                if(task.isSuccessful){
                                    val mainIntent = Intent(requireContext(), NavigatorActivity::class.java)
                                    startActivity(mainIntent)
                                }
                            }
                    }
                    .addOnFailureListener { err ->
                        run {
                            Animation.crossfade(arrayListOf(contentView), arrayListOf(progressView), 1000)
                            Toast.makeText(requireContext(), err.message, Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }
}