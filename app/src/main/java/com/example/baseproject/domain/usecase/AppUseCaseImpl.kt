package com.example.baseproject.domain.usecase

import com.example.baseproject.domain.model.Tenant
import com.example.baseproject.domain.model.User
import com.example.baseproject.domain.repository.AppRepository
import javax.inject.Inject

class AppUseCaseImpl @Inject constructor(private val repository: AppRepository) : AppUseCase {

    override fun loginTenant(
        clientId: String,
        password: String,
    ) = repository.loginTenant(
        clientId = clientId,
        password = password,
    )

    override fun getLoggedTenant() = repository.getLoggedTenant()

    override fun saveLoggedTenant(tenant: Tenant) = repository.saveLoggedTenant(tenant)

    override fun registerUser(user: User) = repository.registerUser(user)

    override fun recognizeUser(user: User) = repository.recognizeUser(user)

    override fun verifyCheckinCheckout(type: String, user: User) =
        repository.verifyCheckinCheckout(type, user)

    override fun identifyCheckIn(user: User) = repository.identifyCheckIn(user)

    override fun identifyCheckOut(user: User) = repository.identifyCheckOut(user)
}