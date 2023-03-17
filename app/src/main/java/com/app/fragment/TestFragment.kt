package com.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.app.ngn.R
import com.charts.Pie
import com.charts.data.DataSet
import java.util.*

/**
 * Test Fragment
 * Fragment for testing new components
 */
class TestFragment : Fragment() {
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
        val chart = view.findViewById<Pie>(R.id.chart)
        val addBtn = view.findViewById<Button>(R.id.add)
        val removeBtn = view.findViewById<Button>(R.id.remove)
        addBtn.setOnClickListener {
            chart.addDataSet(DataSet("Test", randomNum(4, 10, 200)))
        }

        removeBtn.setOnClickListener {
            chart.pop()
        }
    }

    private fun randomNum(length : Int, from : Int, to : Int) : List<Number>
    {
        val ret = mutableListOf<Number>()
        for(i in 0 until length)
        {
            ret.add(Random().nextInt(100) + 10)
        }
        return ret
    }
}