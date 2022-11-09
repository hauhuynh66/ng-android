package com.app.fragment

import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.app.adapter.PageAdapter
import com.app.data.PageData
import com.app.ngn.R
import com.app.util.Generator
import com.app.util.ViewUtils

class AboutFragment : Fragment() {
    private lateinit var indicator : DotAdapter
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
            PageData(Generator.generateString(40), R.drawable.image_background_1),
            PageData(Generator.generateString(40), R.drawable.image_background_2),
            PageData(Generator.generateString(40), R.drawable.image_background_3)
        )

        indicator = DotAdapter(data.size)

        val viewpager = view.findViewById<ViewPager>(R.id.pager)
        viewpager.adapter = PageAdapter(data)

        view.findViewById<RecyclerView>(R.id.dot_list).apply {
            adapter = indicator
            layoutManager = ViewUtils.getFixedHorizontalLayoutManager(requireContext())
        }

        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                indicator.highlightItem(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    class DotAdapter(private val n : Int, iniPosition : Int = 0) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        private val data : MutableList<Boolean> = mutableListOf()

        init {
            for (i in 0 until n){
                data.add(false)
            }
            data[iniPosition] = true
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return DotHolder(LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false))
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as DotHolder).bind(data[position])
        }

        override fun getItemCount(): Int {
            return n
        }

        fun highlightItem(position : Int){
            data.forEachIndexed { i, value ->
                if(value){
                    data[i] = false
                    notifyItemChanged(i)
                }
            }

            data[position] = true
            notifyItemChanged(position)
        }

        /**
         * Indicator Holder
         * v : indicator view
         * option : indicator options
         */
        class DotHolder(v : View, private val option : IndicatorOption = IndicatorOption()) : RecyclerView.ViewHolder(v){
            fun bind(isHighLight : Boolean){
                itemView.findViewById<TextView>(android.R.id.text1).apply {
                    gravity = Gravity.CENTER
                    setPadding(0, paddingTop, 0, paddingBottom)
                    setTextSize(TypedValue.COMPLEX_UNIT_PT, option.size)

                    if(isHighLight){
                        setTextColor(option.selectColor)
                    }else{
                        setTextColor(option.defaultColor)
                    }
                    text = Html.fromHtml("&#8226", Html.FROM_HTML_MODE_LEGACY)
                }
            }
        }

        /**
         * Indicator Dot Option
         * selectColor : color of current display item
         * defaultColor : color of all non-selected item
         * size : dot size
         */
        data class IndicatorOption(
            val selectColor : Int = Color.RED,
            val defaultColor : Int = Color.BLACK,
            val size : Float = 20f
        )
    }

}