package com.example.baseproject.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Place(
    val crowd: Int = 0,
    val maxCapacity: Int = 0,
    val name: String? = null
) : Parcelable
