package com.app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.data.FootballResult
import java.text.SimpleDateFormat
import java.util.*

class Sport : ViewModel() {
    val state = MutableLiveData(0)
    val arg = MutableLiveData("")
    val selectedDate = MutableLiveData(format(Date()))
    //val season = MutableLiveData(Int)
    val results = MutableLiveData(arrayListOf<FootballResult>())
    val isDisplay = MutableLiveData(0)

    private fun format(date : Date) : String{
        val sf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return try {
            sf.format(date)
        }catch (e:Exception){
            ""
        }
    }
}