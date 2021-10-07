package com.example.baseproject.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class IdentifyCheckinCheckoutResponse(

    @SerializedName("status")
    val status: String?,

    @SerializedName("status_message")
    val statusMessage: String?,

    @SerializedName("user_status")
    val userStatus: String?,

    @SerializedName("return")
    val data: List<IdentifyCheckinCheckoutDataResponse>?,

    @SerializedName("pl_data")
    val plData: IdentifyCheckinCheckoutPlDataResponse?,
)

data class IdentifyCheckinCheckoutDataResponse(

    @SerializedName("confidence_level")
    val confidenceLevel: String?,

    @SerializedName("masker")
    val masker: Boolean?,

    @SerializedName("user_id")
    val userId: String?,

    @SerializedName("user_name")
    val userName: String?,
)

data class IdentifyCheckinCheckoutPlDataResponse(

    @SerializedName("message")
    val message: String?,

    @SerializedName("user_id")
    val userId: String?,

    @SerializedName("checkInTime")
    val checkInTime: String?,

    @SerializedName("checkOutTime")
    val checkOutTime: String?,

    @SerializedName("crowd")
    val crowd: Int?,

    @SerializedName("place")
    val place: IdentifyCheckinCheckoutPlDataPlaceResponse?,
)

data class IdentifyCheckinCheckoutPlDataPlaceResponse(

    @SerializedName("maxCapacity")
    val maxCapacity: Int?,

    @SerializedName("name")
    val name: String?,
)