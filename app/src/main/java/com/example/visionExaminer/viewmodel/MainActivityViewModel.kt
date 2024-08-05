package com.example.visionExaminer.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.visionExaminer.R

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val _isNightMode = MutableLiveData(true)
    val isNightMode: LiveData<Boolean> get() = _isNightMode

    private val _videoUri = MutableLiveData<Uri>()
    val videoUri: LiveData<Uri> get() = _videoUri

    init {
        updateVideoUri()
    }

    fun toggleTheme() {
        _isNightMode.value = _isNightMode.value?.not()
        updateVideoUri()
    }

    private fun updateVideoUri() {
        val uri = if (_isNightMode.value == true) {
            Uri.parse("android.resource://" + getApplication<Application>().packageName + "/" + R.raw.wallpaper1_video)
        } else {
            Uri.parse("android.resource://" + getApplication<Application>().packageName + "/" + R.raw.wallpaper2_video)
        }
        _videoUri.value = uri
    }
}
