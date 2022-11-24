package com.app.fragment.explorer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.activity.explorer.FileListActivity
import com.app.adapter.ActionAdapter
import com.app.data.ActionData
import com.app.ngn.R

class EXGroupFragment : Fragment() {
    private lateinit var data : ArrayList<ActionData>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fg_ex_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        data = arrayListOf()
        for( i in 0 until 4){
            val callback = object : ActionData.Callback{
                override fun onClick() {
                    val intent = Intent(requireContext(), FileListActivity::class.java)
                    intent.putExtra("display", i)
                    startActivity(intent)
                }
            }
            data.add(ActionData(null, i.toString(), callback))
        }

        val iconList = view.findViewById<RecyclerView>(R.id.fg_ex_group1_list)
        iconList.layoutManager = object : LinearLayoutManager(requireContext(), HORIZONTAL, false) {
            override fun generateLayoutParams(
                c: Context?,
                attrs: AttributeSet?
            ): RecyclerView.LayoutParams {
                return spanLayoutSize(super.generateLayoutParams(c, attrs))
            }

            private fun spanLayoutSize(layoutParams: RecyclerView.LayoutParams): RecyclerView.LayoutParams {
                layoutParams.width = (width - paddingLeft - paddingRight) / itemCount
                return layoutParams
            }
            override fun canScrollHorizontally(): Boolean {
                return false
            }
        }

        iconList.adapter = ActionAdapter(data, 60)
    }
}