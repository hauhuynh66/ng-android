package com.app.fragment.mc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.helper.widget.Carousel
import androidx.fragment.app.Fragment
import com.app.data.Device
import com.app.ngn.R

class DeviceListFragment : Fragment() {
    private lateinit var devices : ArrayList<Device>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fg_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val images = arrayListOf<Int>(
            R.drawable.image_background_1,
            R.drawable.image_background_2,
            R.drawable.image_background_3,
            R.drawable.goldengate,
            R.drawable.bryce_canyon
        )
        val carousel = view.findViewById<Carousel>(R.id.carousel)
        carousel.setAdapter(object : Carousel.Adapter{
            override fun count(): Int {
                return images.size
            }

            override fun populate(view: View?, index: Int) {
                (view!! as ImageView).setImageResource(images[index])
            }

            override fun onNewItem(index: Int) {

            }
        })
    }
}