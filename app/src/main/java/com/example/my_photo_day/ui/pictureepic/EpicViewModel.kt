package com.example.my_photo_day.ui.pictureepic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my_photo_day.domain.repository.Error
import com.example.my_photo_day.domain.repository.Success
import com.example.my_photo_day.domain.repository.nasa.INasaRepository
import com.example.my_photo_day.domain.model.nasa.Epic

class EpicViewModel(private val nasaRepository: INasaRepository) : ViewModel() {

    private val _loadingLiveData = MutableLiveData(false)
    private val _errorLiveData = MutableLiveData<String?>()
    private val _earthLiveData = MutableLiveData<ArrayList<Epic>>()

    val loadingLiveData: LiveData<Boolean> = _loadingLiveData
    val errorLiveData: LiveData<String?> = _errorLiveData
    val epicLiveData: LiveData<ArrayList<Epic>> = _earthLiveData

    fun getPhoto() {
        _loadingLiveData.value = true
        nasaRepository.getEarthPictureDay() {
            when (it) {
                is Success -> {
                    _earthLiveData.value = it.value ?: arrayListOf()
                    _errorLiveData.value = null
                    _loadingLiveData.value = false
                }
                is Error -> {
                    _errorLiveData.value = it.value.message.toString()
                    _loadingLiveData.value = false
                }
            }
        }
    }
}