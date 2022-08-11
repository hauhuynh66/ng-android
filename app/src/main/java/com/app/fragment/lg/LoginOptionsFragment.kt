package com.app.fragment.lg

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.app.ngn.R
import com.app.viewmodel.Login

class LoginOptionsFragment : Fragment() {
    private val viewModel : Login by activityViewModels()
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

        view.findViewById<Button>(R.id.lg_ep).apply {
            setOnClickListener {
                viewModel.setState(1)
            }
        }
        view.findViewById<Button>(R.id.lg_gg).apply {
            setOnClickListener{
                viewModel.setState(2)
            }
        }
    }
}