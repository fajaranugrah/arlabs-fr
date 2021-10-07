package com.example.baseproject.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.baseproject.data.source.local.room.entity.TenantEntity

@Dao
interface TenantDao {

    /**
     * Select a tenant first.
     *
     * @return A [TenantEntity] of the selected tenant.
     */
    @Query("SELECT * FROM tenant LIMIT 1")
    fun getLoggedTenant(): TenantEntity?

    /**
     * Inserts a logged tenant into the table.
     *
     * @param tenantEntity A new logged tenant.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLoggedTenant(tenantEntity: TenantEntity?)

    /**
     * Clear all tenant.
     */
    @Query("DELETE FROM tenant")
    fun deleteAllTenants()
}