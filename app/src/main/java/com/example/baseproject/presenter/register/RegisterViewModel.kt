package com.example.baseproject.presenter.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.baseproject.data.Resource
import com.example.baseproject.domain.model.User
import com.example.baseproject.domain.usecase.AppUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class RegisterViewModel @Inject constructor(private val useCase: AppUseCase) : ViewModel() {

    private val _registerUserLiveData = MutableLiveData<Resource<String>>()
    val registerUserLiveData = Transformations.map(_registerUserLiveData) { it }

    private val _userPhotoFileLiveData = MutableLiveData<File?>()
    val userPhotoFileLiveData = Transformations.map(_userPhotoFileLiveData) { it }

    private val _scanLogs = MutableLiveData<Any>()
    val scanLogs = Transformations.map(_scanLogs) { it }

    var user: User? = null

   fun registerUser(user: User) {
       viewModelScope.launch(Dispatchers.IO) {
           useCase.registerUser(user).collectLatest {
               _registerUserLiveData.postValue(it)
           }
       }
   }

    fun setUserPhotoFile(file: File?) {
        viewModelScope.launch(Dispatchers.IO) {
            _userPhotoFileLiveData.postValue(file)
        }
    }

    fun scanLogs(id: String?, date: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.scanLogs(id, date).collectLatest {
                _scanLogs.postValue(it)
            }
        }
    }
}