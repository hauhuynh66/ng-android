package com.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.data.FCMData
import com.app.ngn.R
import com.app.service.FCMService

class FCMFragment : Fragment() {
    private lateinit var fcmService: FCMService
    private lateinit var data:ArrayList<FCMData>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fg_fcm, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        data = arrayListOf()
        fcmService = FCMService(data)
        super.onViewCreated(view, savedInstanceState)
    }
}