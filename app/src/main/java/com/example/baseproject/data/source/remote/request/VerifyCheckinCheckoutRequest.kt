package com.example.baseproject.data.source.remote.request

import com.google.gson.annotations.SerializedName

data class VerifyCheckinCheckoutRequest(

    @SerializedName("qr_code")
    val qrCode: String?,

    @SerializedName("latitude")
    val latitude: Double?,

    @SerializedName("longitude")
    val longitude: Double?,

    @SerializedName("nik")
    val nik: String?,

    @SerializedName("facegallery_id")
    val faceGalleryId: String?,

    @SerializedName("image")
    val image: String?,

    @SerializedName("trx_id")
    val trxId: String?,
)