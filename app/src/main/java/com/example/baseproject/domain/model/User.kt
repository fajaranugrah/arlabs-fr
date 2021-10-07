package com.example.baseproject.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String? = null,
    val name: String? = null,
    val status: String? = null,
    val confidenceLevel: String? = null,
    val image: Image? = null,
) : Parcelable
