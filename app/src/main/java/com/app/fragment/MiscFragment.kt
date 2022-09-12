package com.app.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.activity.MessageActivity
import com.app.activity.ex.FileExplorerActivity
import com.app.activity.mp.MusicBrowserActivity
import com.app.activity.sport.FootballMainActivity
import com.app.activity.weather.WeatherActivity
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
        return inflater.inflate(R.layout.fg_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        data = arrayListOf()
        data.add(MiscData(R.drawable.chat, "Message", object : MiscData.Listener{
            override fun onClick() {
                val intent = Intent(requireActivity(), MessageActivity::class.java)
                startActivity(intent)
            }
        }))
        data.add(MiscData(R.drawable.dj, "Music Player", object : MiscData.Listener{
            override fun onClick() {
                val intent = Intent(requireActivity(), MusicBrowserActivity::class.java)
                startActivity(intent)
            }
        }))
        data.add(MiscData(R.drawable.folder, "File Explorer", object : MiscData.Listener{
            override fun onClick() {
                val intent = Intent(requireActivity(), FileExplorerActivity::class.java)
                startActivity(intent)
            }
        }))
        data.add(MiscData(R.drawable.cloudy, "Weather",object : MiscData.Listener{
            override fun onClick() {
                val intent = Intent(requireActivity(), WeatherActivity::class.java)
                startActivity(intent)
            }
        }))
        data.add(MiscData(R.drawable.vision, "OpenCV", object : MiscData.Listener{
            override fun onClick() {
                val listener = object : OptionBottomSheet.Listener{
                    override fun onClick(option: String?) {

                    }
                }
                val optionData = arrayListOf(
                    OptionBottomSheet.Data("Orb")
                )

                val bottomSheet = OptionBottomSheet(optionData, listener)
                bottomSheet.isCancelable = true
                bottomSheet.show(requireActivity().supportFragmentManager, "CV_OPTIONS")
            }
        }))

        data.add(MiscData(R.drawable.sports, "Sport", object : MiscData.Listener{
            override fun onClick() {
                val intent = Intent(requireActivity(), FootballMainActivity::class.java)
                startActivity(intent)
            }
        }))

        val list = view.findViewById<RecyclerView>(R.id.item_list)
        list.layoutManager = GridLayoutManager(requireContext(), 2)
        list.adapter = MiscAdapter(requireContext(), data, 1)
    }
}