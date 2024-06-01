package org.emunix.nullpointer.history.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HistoryViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is history screen"
    }
    val text: LiveData<String> = _text
}