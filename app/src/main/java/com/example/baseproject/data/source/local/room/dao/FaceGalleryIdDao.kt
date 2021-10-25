package com.example.baseproject.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.baseproject.data.source.local.room.entity.FaceGalleryIdEntity

@Dao
interface FaceGalleryIdDao {

    /**
     * Select a facegallery_id first.
     *
     * @return A [FaceGalleryIdEntity] of the selected facegallery_id.
     */
    @Query("SELECT * FROM faceGalleryId LIMIT 1")
    fun getLoggedFaceGalleryId(): FaceGalleryIdEntity?

    /**
     * Inserts a logged facegallery_id into the table.
     *
     * @param faceGalleryIdEntity A new logged tenant.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLoggedFaceGalleryId(faceGalleryIdEntity: FaceGalleryIdEntity)

    /**
     * Clear all tenant.
     */
    @Query("DELETE FROM faceGalleryId")
    fun deleteAllFaceGalleryIds()
}