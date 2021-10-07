package com.example.baseproject.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class LoginTenantResponse(

    @SerializedName("status_code")
    val statusCode: Int?,

    @SerializedName("status_message")
    val statusMessage: String?,

    @SerializedName("access_token")
    val accessToken: String?,
)
