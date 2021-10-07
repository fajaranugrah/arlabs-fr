package com.example.baseproject.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CheckIn(
    val date: String?,
    val time: String?,
    val user: User?,
    val place: Place?,
) : Parcelable
