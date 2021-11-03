package com.example.baseproject.data.source.remote

import com.example.baseproject.data.source.remote.network.*
import com.example.baseproject.data.source.remote.request.*
import com.example.baseproject.data.source.remote.response.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    private val tenantApiService: TenantApiService,
    private val frApiService: FRApiService,
    private val plApiService: PLApiService,
    private val scanLogsApiService: ScanLogsApiService,
) {

    suspend fun loginTenant(
        clientId: String?,
        password: String?,
    ): Flow<ApiResponse<LoginTenantResponse>> {
        return flow {
            try {
                val response = tenantApiService.loginTenant(
                    clientId = clientId,
                    password = password,
                )

                if (response.statusCode == null) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Error(response.statusMessage.toString()))
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun registerUser(
        accessToken: String?,
        request: EnrollFaceRequest,
    ): Flow<ApiResponse<EnrollFaceResponse>> {
        return flow {
            try {
                val response = frApiService.enrollFace(
                    accessToken = accessToken,
                    request = request
                )

                if (response.status == "200") {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Error(response.statusMessage.toString()))
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun registerFaceGalleryId(
        accessToken: String?,
        request: CreateFaceGalleryId,
    ): Flow<ApiResponse<CreateFaceGalleryResponse>> {
        return flow {
            try {
                val response = frApiService.createFaceGallery(
                    accessToken = accessToken,
                    request = request,
                )

                if (response.status == "200") {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Error(response.statusMessage.toString()))
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun recognizeUser(
        accessToken: String?,
        request: RecognizeFaceRequest,
    ): Flow<ApiResponse<RecognizeFaceResponse>> {
        return flow {
            try {
                val response = frApiService.recognizeFace(
                    accessToken = accessToken,
                    request = request,
                )

                if (response.status == "200") {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Error(response.statusMessage.toString()))
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun verifyCheckinCheckout(
        accessToken: String?,
        request: VerifyCheckinCheckoutRequest,
    ): Flow<ApiResponse<VerifyCheckinCheckoutResponse>> {
        return flow {
            try {
                val response = frApiService.verifyCheckinCheckout(
                    accessToken = accessToken,
                    request = request,
                )

                if (response.status == "200") {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Error(response.statusMessage.toString()))
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun identifyCheckinCheckout(
        accessToken: String?,
        request: IdentifyCheckinCheckoutRequest,
    ): Flow<ApiResponse<IdentifyCheckinCheckoutResponse>> {
        return flow {
            try {
                val response = frApiService.identifyCheckinCheckout(
                    accessToken = accessToken,
                    request = request,
                )

                if (response.status == "200") {
                    emit(ApiResponse.Success(response))
                } else {
                    if (response.plData?.message?.contains("checkin", true) == true) {
                        emit(ApiResponse.Error(response.plData.message))
                    } else {
                        emit(ApiResponse.Error(response.statusMessage.toString()))
                    }
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun scanLogs(
        authorization: String?,
        request: ScanLogsRequest,
    ): Flow<ApiResponse<ScanLogsResponse>> {
        return flow {
            try {
                val response = scanLogsApiService.scanLogs(
                    authorization = authorization,
                    request = request,
                )

                if (response.status == "200") {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Error(response.statusMessage.toString()))
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }
}