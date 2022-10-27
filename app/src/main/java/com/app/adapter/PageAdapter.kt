package com.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.PagerAdapter
import com.app.data.PageData
import com.app.ngn.R


class PageAdapter(val data : List<PageData>) : PagerAdapter() {
    override fun getCount(): Int {
        return data.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val page = data[position]
        val view = LayoutInflater.from(container.context).inflate(R.layout.com_page, container, false)

        view.findViewById<TextView>(R.id.text).text = page.text
        view.findViewById<ImageView>(R.id.icon).setImageDrawable(ContextCompat.getDrawable(container.context, page.resource))
        view.findViewById<ConstraintLayout>(R.id.page_holder).setBackgroundColor(page.background)

        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}