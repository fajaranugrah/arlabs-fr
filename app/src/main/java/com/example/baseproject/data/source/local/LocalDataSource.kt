package com.example.baseproject.data.source.local

import com.example.baseproject.data.source.local.room.dao.TenantDao
import com.example.baseproject.data.source.local.room.entity.TenantEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(
    private val tenantDao: TenantDao,
) {

    fun getLoggedTenant() = tenantDao.getLoggedTenant()

    fun saveLoggedTenant(tenant: TenantEntity) = tenantDao.insertLoggedTenant(tenant)

    fun deleteLoggedTenant() = tenantDao.deleteAllTenants()
}