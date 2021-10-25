package com.example.baseproject.data.source.remote.request

import com.google.gson.annotations.SerializedName

data class CreateFaceGalleryId (

    @SerializedName("facegallery_id")
    val facegalleryId: String?,

    @SerializedName("trx_id")
    val trxId: String?,
)