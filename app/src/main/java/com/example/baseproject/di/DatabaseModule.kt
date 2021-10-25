package com.example.baseproject.di

import android.content.Context
import androidx.room.Room
import com.example.baseproject.data.source.local.room.AppDatabase
import com.example.baseproject.data.source.local.room.dao.FaceGalleryIdDao
import com.example.baseproject.data.source.local.room.dao.TenantDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java, AppDatabase.DATABASE_NAME,
        ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideTenantDao(database: AppDatabase): TenantDao = database.tenantDao()

    @Provides
    fun provideFaceGalleryIdDao(database: AppDatabase): FaceGalleryIdDao = database.faceGalleryId()
}