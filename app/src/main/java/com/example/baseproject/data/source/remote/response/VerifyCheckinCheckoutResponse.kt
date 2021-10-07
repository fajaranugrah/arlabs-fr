package com.example.baseproject.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class VerifyCheckinCheckoutResponse(

    @SerializedName("status")
    val status: String?,

    @SerializedName("status_message")
    val statusMessage: String?,

    @SerializedName("similarity")
    val similarity: Double?,

    @SerializedName("masker")
    val masker: Boolean?,

    @SerializedName("verified")
    val verified: Boolean?,

    @SerializedName("user_status")
    val userStatus: String?,

    @SerializedName("pl_data")
    val plData: VerifyCheckinCheckoutPlDataResponse?,
)

data class VerifyCheckinCheckoutPlDataResponse(

    @SerializedName("user_id")
    val userId: String?,

    @SerializedName("user_name")
    val userName: String?,

    @SerializedName("confidence_level")
    val confidenceLevel: String?,

    @SerializedName("masker")
    val masker: String?,

    @SerializedName("crowd")
    val crowd: Int?,
)