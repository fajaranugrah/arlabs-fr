package com.example.baseproject.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ScanLogsResponse (

    @SerializedName("status")
    val status: String?,

    @SerializedName("status_message")
    val statusMessage: String?,

    @SerializedName("data")
    val data: Data?,
)

data class Data(

    @SerializedName("name")
    val name: String?,

    @SerializedName("owner")
    val owner: String?,

    @SerializedName("creation")
    val creation: String?,

    @SerializedName("modified")
    val modified: String?,

    @SerializedName("modified_by")
    val modifiedBy: String?,

    @SerializedName("parent")
    val parent: String?,

    @SerializedName("parentfield")
    val parentfield: String?,

    @SerializedName("parenttype")
    val parenttype: String?,

    @SerializedName("idx")
    val idx: String?,

    @SerializedName("docstatus")
    val docstatus: String?,

    @SerializedName("id")
    val id: String?,

    @SerializedName("time")
    val time: String?,

    @SerializedName("doctype")
    val doctype: String?,
)
