package com.example.baseproject.data.source.remote.request

import com.google.gson.annotations.SerializedName

data class RecognizeFaceRequest(

    @SerializedName("facegallery_id")
    val faceGalleryId: String?,

    @SerializedName("image")
    val image: String?,

    @SerializedName("trx_id")
    val trxId: String?,
)
