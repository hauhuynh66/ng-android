package com.app.fragment.weather

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.app.activity.weather.WeatherActivity
import com.app.adapter.CustomListAdapter
import com.app.adapter.ListManager
import com.app.adapter.TextManager
import com.app.model.AppDatabase
import com.app.ngn.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class LocationListFragment : Fragment() {
    private lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fg_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        db = Room.databaseBuilder(requireContext(), AppDatabase::class.java, "db").fallbackToDestructiveMigration().build()
        val arr = runBlocking {
            var arr = arrayListOf<String>()
            withContext(Dispatchers.IO){
                val listData = db.locationRepository().getAll()
                arr = listData.map { it.name }.toCollection(ArrayList())
            }
            return@runBlocking arr
        }

        val list = view.findViewById<RecyclerView>(R.id.item_list)
        val textManager = TextManager(arr)
        textManager.onItemClickListener = object : ListManager.OnItemClickListener<String>{
            override fun execute(item: String) {
                runBlocking {
                    withContext(Dispatchers.IO){
                        db.settingRepository().update("current_city", item)
                    }
                }
                val intent = Intent(requireActivity(), WeatherActivity::class.java)
                startActivity(intent)
            }
        }

        /*val onLongClick = object : ListAdapter.OnItemLongClickListener{
            override fun execute() {
                /*runBlocking {
                    withContext(Dispatchers.IO){
                        val count = db.locationRepository().count()
                        if(count>1){
                            val setting = db.settingRepository().getProperty("current_city")
                            if(setting==data){
                                db.settingRepository().update("current_city", db.locationRepository().getFirst()!!.name)
                            }
                            db.locationRepository().delete(data)
                        }
                    }
                }
                val pos = adapter.data.indexOf(data)
                adapter.data.remove(data)
                adapter.notifyItemRemoved(pos)*/
            }
        }*/

        val adapter = CustomListAdapter(textManager)
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }
}