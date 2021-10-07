package com.example.baseproject.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CheckOut(
    val date: String?,
    val time: String?,
    val user: User?,
    val place: Place?,
) : Parcelable

