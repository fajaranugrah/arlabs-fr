package com.example.baseproject.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Thermal(
    val temperature: String?,
    val isUnusual: Boolean?,
) : Parcelable