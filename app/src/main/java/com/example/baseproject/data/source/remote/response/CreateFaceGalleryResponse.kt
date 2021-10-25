package com.example.baseproject.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class CreateFaceGalleryResponse(

    @SerializedName("status")
    val status: String?,

    @SerializedName("status_message")
    val statusMessage: String?,

    @SerializedName("facegallery_id")
    val facegalleryId: String,
)