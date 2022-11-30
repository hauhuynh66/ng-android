package com.app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.data.media.Audio

class AudioListConnector : ViewModel(){
    val selectedAudio : MutableLiveData<Audio> = MutableLiveData(null)
}