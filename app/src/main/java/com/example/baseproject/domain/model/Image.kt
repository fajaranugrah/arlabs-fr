package com.example.baseproject.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
data class Image(
    val fileName: String? = null,
    val extension: String? = null,
    val file: File? = null,
    val base64: String? = null,
    val url: String? = null,
) : Parcelable
