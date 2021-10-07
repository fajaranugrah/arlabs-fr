package com.example.baseproject.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class RecognizeFaceResponse(

    @SerializedName("status")
    val status: String?,

    @SerializedName("status_message")
    val statusMessage: String?,

    @SerializedName("status_description")
    val statusDescription: String?,

    @SerializedName("return")
    val result: List<RecognizeFaceReturnResponse>?,
)

data class RecognizeFaceReturnResponse(

    @SerializedName("confidence_level")
    val confidenceLevel: String?,

    @SerializedName("mask")
    val mask: String?,

    @SerializedName("user_id")
    val userId: String?,

    @SerializedName("user_name")
    val userName: String?,
)