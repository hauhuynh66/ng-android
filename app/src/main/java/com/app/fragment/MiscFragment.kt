package com.app.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.activity.ComputerVision
import com.app.activity.FirebaseCloudMessaging
import com.app.activity.FileExplorer
import com.app.activity.Shopping
import com.app.adapter.MiscAdapter
import com.app.data.MiscData
import com.app.dialog.OptionBottomSheet
import com.app.ngn.R

class MiscFragment : Fragment() {
    private lateinit var data: ArrayList<MiscData>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fg_misc, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        data = arrayListOf()
        data.add(MiscData(null, object : MiscData.Listener{
            override fun onClick() {
                val intent = Intent(requireActivity(), Shopping::class.java)
                startActivity(intent)
            }
        }))
        data.add(MiscData(null, object : MiscData.Listener{
            override fun onClick() {
                val intent = Intent(requireActivity(), FirebaseCloudMessaging::class.java)
                startActivity(intent)
            }
        }))
        data.add(MiscData(null, object : MiscData.Listener{
            override fun onClick() {
                val intent = Intent(requireActivity(), FileExplorer::class.java)
                startActivity(intent)
            }
        }))
        data.add(MiscData(null, object : MiscData.Listener{
            override fun onClick() {
                val options = arrayListOf("Orb")
                val opts = arrayListOf<OptionBottomSheet.BottomSheetData>()
                val cbs = arrayListOf<OptionBottomSheet.Listener>()

                for(i in 0 until 1){
                    opts.add(OptionBottomSheet.BottomSheetData(options[i], true, options[i]))
                    val cb = object : OptionBottomSheet.Listener{
                        override fun onClick(option: String?) {
                            val intent = Intent(requireActivity(), ComputerVision::class.java)
                            intent.putExtra("mode", options[i])
                            startActivity(intent)
                        }
                    }
                    cbs.add(cb)
                }

                val bottomSheet = OptionBottomSheet(opts, cbs)
                bottomSheet.isCancelable = true
                bottomSheet.show(requireActivity().supportFragmentManager, "CV_OPTIONS")
            }
        }))

        val list = view.findViewById<RecyclerView>(R.id.fg_misc_list)
        list.layoutManager = GridLayoutManager(requireContext(), 2)
        list.adapter = MiscAdapter(requireContext(), data)
    }
}