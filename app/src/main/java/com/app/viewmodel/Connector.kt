package com.app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConnectorModel<T> : ViewModel(){
    val data : MutableLiveData<T> = MutableLiveData(null)
}

class ListHolderModel<T> : ViewModel(){
    val data : MutableList<T> = mutableListOf()
}