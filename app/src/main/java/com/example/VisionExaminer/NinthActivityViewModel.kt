package com.example.VisionExaminer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NinthActivityViewModel : ViewModel() {
    // LiveData to hold the selected midriatic agent
    var midriaticAgent: String = ""
    private val _selectedMidriaticAgent = MutableLiveData<String>()

    // Function to update the midriatic agent
    fun updateMidriaticAgent(agent: String) {
        midriaticAgent = agent
        _selectedMidriaticAgent.value = agent
    }
}
