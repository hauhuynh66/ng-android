package com.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.app.adapter.PageAdapter
import com.app.data.PageData
import com.app.ngn.R
import com.app.util.Generator

class AboutFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fg_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = listOf(
            PageData(Generator.generateString(40), R.drawable.goldengate),
            PageData(Generator.generateString(40), R.drawable.bryce_canyon),
            PageData(Generator.generateString(40), R.drawable.mt_rushmore)
        )

        view.findViewById<ViewPager>(R.id.pager).apply {
            adapter = PageAdapter(data)
        }
    }

}