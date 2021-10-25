package com.example.baseproject.presenter.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.baseproject.data.Resource
import com.example.baseproject.domain.model.CheckIn
import com.example.baseproject.domain.model.FaceGalleryId
import com.example.baseproject.domain.model.Tenant
import com.example.baseproject.domain.usecase.AppUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashScreenViewModel @Inject constructor(private val useCase: AppUseCase) : ViewModel() {

    private val _isTenantLoggedIn = MutableLiveData<Boolean>()
    val isTenantLoggedIn = Transformations.map(_isTenantLoggedIn) { it }

    private val _faceGalleryIdLiveData = MutableLiveData<Any>()
    val faceGalleryIdLiveData = Transformations.map(_faceGalleryIdLiveData) { it }

    fun checkTenantHasLoggedIn() {
        viewModelScope.launch(Dispatchers.IO) {
            val accessToken =  useCase.getLoggedTenant().firstOrNull()?.data?.accessToken
            _isTenantLoggedIn.postValue(!accessToken.isNullOrEmpty())
        }
    }

    fun saveLoggedTenant(tenant: Tenant) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.saveLoggedTenant(tenant).firstOrNull()
        }
    }

    fun createFaceGalleryId() {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.registerFaceGalleryId().collectLatest {
                _faceGalleryIdLiveData.postValue(it)
            }
        }
    }
}