package com.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.app.ngn.R

class LoginOptionFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fg_lg_options, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fb = view.findViewById<Button>(R.id.lg_fb).apply {
            setOnClickListener {

            }
        }
        val gg = view.findViewById<Button>(R.id.lg_gg).apply {
            setOnClickListener {

            }
        }
        val gh = view.findViewById<Button>(R.id.lg_gh).apply {

        }
    }
}