package com.example.baseproject.data.source.remote.network

import com.example.baseproject.data.source.remote.response.LoginTenantResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface TenantApiService {

    @POST("client/login")
    @FormUrlEncoded
    suspend fun loginTenant(
        @Field(value = "client_id") clientId: String?,
        @Field(value = "password") password: String?
    ): LoginTenantResponse
}