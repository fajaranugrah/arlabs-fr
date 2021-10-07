package com.example.baseproject.presenter.recognize

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.baseproject.data.Resource
import com.example.baseproject.domain.model.CheckIn
import com.example.baseproject.domain.model.CheckOut
import com.example.baseproject.domain.model.Thermal
import com.example.baseproject.domain.model.User
import com.example.baseproject.domain.usecase.AppUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecognizeViewModel @Inject constructor(private val useCase: AppUseCase) : ViewModel() {

    private val _thermal = MutableLiveData<Thermal?>()
    val thermal = Transformations.map(_thermal) { it }

    private val _identifyCheckInLiveData = MutableLiveData<Resource<CheckIn>>()
    val identifyCheckInLiveData = Transformations.map(_identifyCheckInLiveData) { it }

    private val _identifyCheckOutLiveData = MutableLiveData<Resource<CheckOut>>()
    val identifyCheckOutLiveData = Transformations.map(_identifyCheckOutLiveData) { it }

    var isOnProgress = false

    fun setThermal(temperature: String, isUnusual: Boolean) {
        _thermal.postValue(Thermal(
            temperature = temperature,
            isUnusual = isUnusual,
        ))
    }

    fun identifyCheckIn(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.identifyCheckIn(user).collectLatest {
                _identifyCheckInLiveData.postValue(it)
            }
        }
    }

    fun identifyCheckOut(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.identifyCheckOut(user).collectLatest {
                _identifyCheckOutLiveData.postValue(it)
            }
        }
    }
}