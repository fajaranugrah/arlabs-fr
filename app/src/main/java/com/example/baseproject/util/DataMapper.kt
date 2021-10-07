package com.example.baseproject.util

import com.example.baseproject.data.source.local.room.entity.TenantEntity
import com.example.baseproject.data.source.remote.response.*
import com.example.baseproject.domain.model.Image
import com.example.baseproject.domain.model.Place
import com.example.baseproject.domain.model.Tenant
import com.example.baseproject.domain.model.User

object DataMapper {

    fun mapTenantToTenantEntity(input: Tenant): TenantEntity {
        return TenantEntity(
            id = input.id.toString(),
            accessToken = input.accessToken,
        )
    }

    fun mapTenantEntityToTenant(input: TenantEntity?): Tenant {
        return Tenant(
            id = input?.id,
            accessToken = input?.accessToken,
        )
    }

    fun mapLoginTenantResponseToTenant(
        tenantId: String?,
        input: LoginTenantResponse,
    ): Tenant {
        return Tenant(
            id = tenantId,
            accessToken = input.accessToken,
        )
    }

    fun mapRecognizeFaceResponseToListUser(
        imageBase64: String?,
        input: RecognizeFaceResponse,
    ): List<User> {
        return input.result?.map { mapRecognizeFaceReturnResponseToUser(imageBase64, it) } ?: emptyList()
    }

    private fun mapRecognizeFaceReturnResponseToUser(
        imageBase64: String?,
        input: RecognizeFaceReturnResponse,
    ): User {
        return User(
            id = input.userId,
            name = input.userName,
            confidenceLevel = input.confidenceLevel,
            image = Image(
                base64 = imageBase64,
            ),
        )
    }

    fun mapVerifyCheckinCheckoutResponseToUser(
        user: User,
        input: VerifyCheckinCheckoutResponse,
    ): User {
        return User(
            id = user.id,
            name = user.name,
            status = input.userStatus,
            confidenceLevel = input.similarity.toString(),
            image = user.image,
        )
    }

    fun mapIdentifyCheckinCheckoutResponseToUser(
        input: IdentifyCheckinCheckoutResponse,
    ): User {
        return User(
            id = input.data?.firstOrNull()?.userId,
            name = input.data?.firstOrNull()?.userName,
            status = input.userStatus,
            confidenceLevel = input.data?.firstOrNull()?.confidenceLevel,
        )
    }

    fun mapIdentifyCheckinCheckoutResponseToPlace(
        input: IdentifyCheckinCheckoutResponse,
    ): Place {
        return Place(
            crowd = input.plData?.crowd ?: 0,
            maxCapacity = input.plData?.place?.maxCapacity ?: 0,
            name = input.plData?.place?.name
        )
    }
}