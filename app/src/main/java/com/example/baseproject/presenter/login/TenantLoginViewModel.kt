package com.example.baseproject.presenter.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.baseproject.data.Resource
import com.example.baseproject.domain.model.Tenant
import com.example.baseproject.domain.usecase.AppUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

class TenantLoginViewModel @Inject constructor(private val useCase: AppUseCase) : ViewModel() {

    private val _loginTenantLiveData = MutableLiveData<Resource<Tenant>>()
    val loginTenantLiveData = Transformations.map(_loginTenantLiveData) { it }

    fun loginTenant(clientId: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.loginTenant(
                clientId = clientId,
                password = password,
            ).collectLatest {
                _loginTenantLiveData.postValue(it)
            }
        }
    }

    fun saveLoggedTenant(tenant: Tenant) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.saveLoggedTenant(tenant).firstOrNull()
        }
    }
}