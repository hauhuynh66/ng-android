package com.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.helper.widget.Carousel
import androidx.fragment.app.Fragment
import com.app.ngn.R

class MainFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fg_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val images = arrayListOf(
            R.drawable.image_background_1,
            R.drawable.heh,
            R.drawable.goldengate,
            R.drawable.mt_rushmore,
            R.drawable.bryce_canyon
        )
        val carousel = view.findViewById<Carousel>(R.id.carousel)
        println(carousel.currentIndex)
        carousel.setAdapter(object : Carousel.Adapter{
            override fun count(): Int {
                return images.size
            }

            override fun populate(view: View?, index: Int) {
                println(index)
                println(carousel.currentIndex)
                (view as ImageView).setImageResource(images[index])
            }

            override fun onNewItem(index: Int) {

            }
        })
    }
}