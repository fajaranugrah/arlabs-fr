package com.example.baseproject.domain.usecase

import com.example.baseproject.data.Resource
import com.example.baseproject.domain.model.*
import kotlinx.coroutines.flow.Flow

interface AppUseCase {

    fun loginTenant(clientId: String, password: String): Flow<Resource<Tenant>>

    fun getLoggedTenant(): Flow<Resource<Tenant>>

    fun saveLoggedTenant(tenant: Tenant): Flow<Resource<Any>>

    fun registerUser(user: User): Flow<Resource<String>>

    fun recognizeUser(user: User): Flow<Resource<List<User>>>

    fun verifyCheckinCheckout(type: String, user: User): Flow<Resource<User>>

    fun identifyCheckIn(user: User): Flow<Resource<CheckIn>>

    fun identifyCheckOut(user: User): Flow<Resource<CheckOut>>

    fun registerFaceGalleryId(): Flow<Any>

    fun scanLogs(id: String?, date: String?): Flow<Any>
}