package com.example.baseproject.data.source.remote.request

import com.google.gson.annotations.SerializedName

data class ScanLogsRequest (

    @SerializedName("id")
    val id: String?,

    @SerializedName("date")
    val date: String?,
)