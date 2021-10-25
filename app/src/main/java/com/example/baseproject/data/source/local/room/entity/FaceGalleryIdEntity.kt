package com.example.baseproject.data.source.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "faceGalleryId")
data class FaceGalleryIdEntity(

    @PrimaryKey
    @ColumnInfo(name = "facegallery_id")
    val facegalleryId: String,
)