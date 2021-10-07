package com.example.baseproject.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class CheckPeduliLindungiResponse(

    @SerializedName("success")
    val success: Boolean?,

    @SerializedName("code")
    val code: Int,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: CheckPeduliLingdungiDataResponse?,
)

data class CheckPeduliLingdungiDataResponse(

    @SerializedName("userStatus")
    val userStatus: String?,
)