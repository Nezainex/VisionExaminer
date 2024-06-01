package com.example.testapp13

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NinthActivityViewModel : ViewModel() {
    // LiveData to hold the selected midriatic agent
    var midriaticAgent: String = ""
    private val _selectedMidriaticAgent = MutableLiveData<String>()
    val selectedMidriaticAgent: LiveData<String> get() = _selectedMidriaticAgent

    // Function to update the midriatic agent
    fun updateMidriaticAgent(agent: String) {
        midriaticAgent = agent
        _selectedMidriaticAgent.value = agent
    }
}
