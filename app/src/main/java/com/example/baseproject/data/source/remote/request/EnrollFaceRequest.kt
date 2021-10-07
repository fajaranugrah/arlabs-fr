package com.example.baseproject.data.source.remote.request

import com.google.gson.annotations.SerializedName

data class EnrollFaceRequest(

    @SerializedName("nik")
    val nik: String?,

    @SerializedName("user_name")
    val userName: String?,

    @SerializedName("facegallery_id")
    val faceGalleryId: String?,

    @SerializedName("image")
    val image: String?,

    @SerializedName("timestamp")
    val timestamp: String?,

    @SerializedName("trx_id")
    val trxId: String?,
)
