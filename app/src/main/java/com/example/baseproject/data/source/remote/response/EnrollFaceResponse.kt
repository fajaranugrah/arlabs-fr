package com.example.baseproject.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class EnrollFaceResponse(

    @SerializedName("status")
    val status: String?,

    @SerializedName("status_message")
    val statusMessage: String?,

    @SerializedName("pl_data")
    val plData: PlDataResponse?,
)

data class PlDataResponse(

    @SerializedName("success")
    val success: Boolean?,

    @SerializedName("code")
    val code: Int?,

    @SerializedName("message")
    val message: String?,

    @SerializedName("apiVersion")
    val apiVersion: String?,
)