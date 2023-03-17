package com.app.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.activity.CVActivity
import com.app.activity.TestActivity
import com.app.activity.explorer.FileExplorerActivity
import com.app.activity.message.MessageActivity
import com.app.activity.player.BrowserActivity
import com.app.activity.score.MainActivity
import com.app.activity.weather.WeatherActivity
import com.app.adapter.ActionAdapter
import com.app.data.ActionData
import com.app.ngn.R

class ActionFragment : Fragment() {
    private lateinit var data: ArrayList<ActionData>
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
        data.add(ActionData(R.drawable.chat, "Message", object : ActionData.Callback{
            override fun onClick() {
                val intent = Intent(requireActivity(), MessageActivity::class.java)
                startActivity(intent)
            }
        }))

        data.add(ActionData(R.drawable.dj, "Music Player", object : ActionData.Callback{
            override fun onClick() {
                val intent = Intent(requireActivity(), BrowserActivity::class.java)
                startActivity(intent)
            }
        }))

        data.add(ActionData(R.drawable.folder, "File Explorer", object : ActionData.Callback{
            override fun onClick() {
                val intent = Intent(requireActivity(), FileExplorerActivity::class.java)
                startActivity(intent)
            }
        }))

        data.add(ActionData(R.drawable.cloudy, "Weather", object : ActionData.Callback{
            override fun onClick() {
                val intent = Intent(requireActivity(), WeatherActivity::class.java)
                startActivity(intent)
            }
        }))

        data.add(ActionData(R.drawable.vision, "CV", object : ActionData.Callback{
            override fun onClick() {
                val intent = Intent(requireActivity(), CVActivity::class.java)
                startActivity(intent)
            }
        }))

        data.add(ActionData(null, "Score", object : ActionData.Callback{
            override fun onClick() {
                val intent = Intent(requireActivity(), MainActivity::class.java)
                startActivity(intent)
            }
        }))

        data.add(ActionData(null, "Test", object : ActionData.Callback{
            override fun onClick() {
                val intent = Intent(requireActivity(), TestActivity::class.java)
                startActivity(intent)
            }
        }))

        val list = view.findViewById<RecyclerView>(R.id.item_list)
        list.layoutManager = GridLayoutManager(requireContext(), 2)
        list.adapter = ActionAdapter(data, 100)
    }
}