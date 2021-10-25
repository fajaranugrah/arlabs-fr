package com.example.baseproject.data.source.local

import com.example.baseproject.data.source.local.room.dao.FaceGalleryIdDao
import com.example.baseproject.data.source.local.room.dao.TenantDao
import com.example.baseproject.data.source.local.room.entity.FaceGalleryIdEntity
import com.example.baseproject.data.source.local.room.entity.TenantEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(
    private val tenantDao: TenantDao,
    private val faceGalleryIdDao: FaceGalleryIdDao,
) {

    fun getLoggedTenant() = tenantDao.getLoggedTenant()

    fun saveLoggedTenant(tenant: TenantEntity) = tenantDao.insertLoggedTenant(tenant)

    fun deleteLoggedTenant() = tenantDao.deleteAllTenants()

    fun getLoggedFaceGalleryId() = faceGalleryIdDao.getLoggedFaceGalleryId()

    fun saveLoggedFaceGalleryId(faceGalleryIdEntity: FaceGalleryIdEntity) = faceGalleryIdDao.insertLoggedFaceGalleryId(faceGalleryIdEntity)

    fun deleteLoggedFaceGalleryId() = faceGalleryIdDao.deleteAllFaceGalleryIds()
}