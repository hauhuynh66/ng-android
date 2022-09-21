package com.app.fragment.cv

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.app.ngn.R
import com.app.util.ComputerVision.Companion.featureMatching

class ORBFragment : Fragment() {
    private lateinit var original1 : ImageView
    private lateinit var original2 : ImageView
    private lateinit var transformed : ImageView
    private var src1 : Bitmap? = null
    private var src2 : Bitmap? = null
    private lateinit var distance : SeekBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fg_orb, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        original1 = view.findViewById(R.id.ac_cv_camera_pic1)
        original2 = view.findViewById(R.id.ac_cv_camera_pic2)
        transformed = view.findViewById(R.id.ac_cv_pic_transformed)
        distance = view.findViewById(R.id.ac_cv_orb_distance)
        distance.isEnabled = false
        distance.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                p0!!.apply {
                    transformed.setImageBitmap(featureMatching(src1!!, src2!!, p0.progress.toDouble()/50))
                }
            }
        })
    }

    fun process(src : Bitmap){
        if (src1 == null) {
            src1 = src
        }else{
            if(src2 == null) {
                src2 = src
            }else{
                src1 = src2
                src2 = src
            }
            distance.isEnabled = true
            original2.setImageBitmap(src2)
            transformed.setImageBitmap(featureMatching(src1!!, src2!!, distance.progress.toDouble()/50))
        }
        original1.setImageBitmap(src1)
    }
}