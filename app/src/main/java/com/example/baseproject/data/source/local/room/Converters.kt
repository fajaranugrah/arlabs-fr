package com.example.baseproject.data.source.local.room

import androidx.room.TypeConverter
import com.example.baseproject.data.source.local.room.entity.TenantEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromTenantObject(tenant: TenantEntity?): String {
        val type = object : TypeToken<TenantEntity>() {}.type
        return Gson().toJson(tenant, type)
    }

    @TypeConverter
    fun toTenantObject(tenant: String): TenantEntity? {
        val type = object : TypeToken<TenantEntity>() {}.type
        return Gson().fromJson(tenant, type)
    }
}