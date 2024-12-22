package com.rodriguesalex.droidpokedex.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DroidHomeViewModel @Inject constructor(

)  : ViewModel() {

    val testEvent = MutableLiveData<String>()

    init {
        testEvent.value = "Test event"
    }
}