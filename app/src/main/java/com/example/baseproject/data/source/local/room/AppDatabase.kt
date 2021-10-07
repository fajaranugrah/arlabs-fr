package com.example.baseproject.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.baseproject.data.source.local.room.dao.TenantDao
import com.example.baseproject.data.source.local.room.entity.TenantEntity

@Database(
    entities = [
        TenantEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "fr.db"
    }

    abstract fun tenantDao(): TenantDao
}