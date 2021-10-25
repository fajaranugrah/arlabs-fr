package com.example.baseproject.data.source

import android.util.Base64
import android.util.Base64OutputStream
import com.example.baseproject.data.Resource
import com.example.baseproject.data.source.local.LocalDataSource
import com.example.baseproject.data.source.remote.RemoteDataSource
import com.example.baseproject.data.source.remote.network.ApiResponse
import com.example.baseproject.data.source.remote.network.ConstNetwork
import com.example.baseproject.data.source.remote.request.*
import com.example.baseproject.domain.model.*
import com.example.baseproject.domain.repository.AppRepository
import com.example.baseproject.util.DataMapper
import com.example.baseproject.util.DataMapper.mapFaceGalleryIdResponseToFaceGalleryIdEntity
import com.example.baseproject.util.DataMapper.mapTenantEntityToTenant
import com.example.baseproject.util.DataMapper.mapTenantToTenantEntity
import com.example.baseproject.util.ext.formatDate
import com.example.baseproject.util.ext.toTimeZone
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.io.ByteArrayOutputStream
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AppRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
) : AppRepository {

    override fun loginTenant(
        clientId: String,
        password: String?,
    ): Flow<Resource<Tenant>> {
        return flow {
            emit(Resource.Loading())
            emitAll(remoteDataSource.loginTenant(
                clientId = clientId,
                password = password,
            ).map {
                when (it) {
                    is ApiResponse.Success -> {
                        Resource.Success(
                            DataMapper.mapLoginTenantResponseToTenant(
                                tenantId = clientId,
                                input = it.data,
                            )
                        )
                    }
                    is ApiResponse.Error -> {
                        Resource.Error(it.errorMessage)
                    }
                }
            })
        }
    }

    override fun getLoggedTenant(): Flow<Resource<Tenant>> {
        return flow {
            localDataSource.getLoggedTenant()?.let {
                emit(Resource.Success(mapTenantEntityToTenant(it)))
            }
        }
    }

    override fun saveLoggedTenant(tenant: Tenant): Flow<Resource<Any>> {
        return flow {
            localDataSource.saveLoggedTenant(mapTenantToTenantEntity(tenant))
        }
    }

    override fun registerUser(user: User): Flow<Resource<String>> {
        val imageBase64 = ByteArrayOutputStream().use { outputStream ->
            Base64OutputStream(outputStream, Base64.DEFAULT).use { base64FilterStream ->
                user.image?.file?.inputStream().use { inputStream ->
                    inputStream?.copyTo(base64FilterStream)
                }
            }

            return@use outputStream.toString()
        }

        val request = EnrollFaceRequest(
            nik = user.id,
            userName = user.name,
            faceGalleryId = ConstNetwork.FACE_GALLERY_ID,
            //faceGalleryId = localDataSource.getLoggedFaceGalleryId()?.facegalleryId,
            image = imageBase64,
            timestamp = Calendar.getInstance().time.toString(),
            trxId = "appenroll${Calendar.getInstance().timeInMillis}",
        )

        return flow {
            emit(Resource.Loading())
            emitAll(remoteDataSource.registerUser(
                accessToken = localDataSource.getLoggedTenant()?.accessToken,
                request = request,
            ).map {
                when (it) {
                    is ApiResponse.Success -> {
                        Resource.Success(it.data.statusMessage.toString())
                    }
                    is ApiResponse.Error -> {
                        Resource.Error(it.errorMessage)
                    }
                }
            })
        }
    }

    override fun registerFaceGalleryId(): Flow<Any> {
        val request = CreateFaceGalleryId(
            facegalleryId = ConstNetwork.FACE_GALLERY_ID,
            trxId = ConstNetwork.TRX_ID,
        )

        return flow {
            emitAll(remoteDataSource.registerFaceGalleryId(
                accessToken = localDataSource.getLoggedTenant()?.accessToken,
                request = request,
            ).map {
                when (it) {
                    is ApiResponse.Success -> {
                        localDataSource.saveLoggedFaceGalleryId(
                            mapFaceGalleryIdResponseToFaceGalleryIdEntity(
                                input = it.data
                            )
                        )
                    }
                }
            })
        }
    }

    override fun recognizeUser(user: User): Flow<Resource<List<User>>> {
        val imageBase64 = ByteArrayOutputStream().use { outputStream ->
            Base64OutputStream(outputStream, Base64.DEFAULT).use { base64FilterStream ->
                user.image?.file?.inputStream().use { inputStream ->
                    inputStream?.copyTo(base64FilterStream)
                }
            }

            return@use outputStream.toString()
        }
        val request = RecognizeFaceRequest(
            faceGalleryId = ConstNetwork.FACE_GALLERY_ID,
            //faceGalleryId = localDataSource.getLoggedFaceGalleryId()?.facegalleryId,
            image = imageBase64,
            trxId = "appenrecognizeface${Calendar.getInstance().timeInMillis}",
        )

        return flow {
            emit(Resource.Loading())
            emitAll(remoteDataSource.recognizeUser(
                accessToken = localDataSource.getLoggedTenant()?.accessToken,
                request = request,
            ).map {
                when (it) {
                    is ApiResponse.Success -> {
                        Resource.Success(
                            DataMapper.mapRecognizeFaceResponseToListUser(
                                imageBase64 = imageBase64,
                                input = it.data
                            )
                        )
                    }
                    is ApiResponse.Error -> {
                        Resource.Error(it.errorMessage)
                    }
                }
            })
        }
    }

    override fun verifyCheckinCheckout(type: String, user: User): Flow<Resource<User>> {
        val request = VerifyCheckinCheckoutRequest(
            qrCode = "$type:613ecab858aa7a008db7aafa",
            latitude = -6.884797196794496,
            longitude = 107.64813410425357,
            nik = user.id,
            faceGalleryId = ConstNetwork.FACE_GALLERY_ID,
            //faceGalleryId = localDataSource.getLoggedFaceGalleryId()?.facegalleryId,
            image = user.image?.base64,
            trxId = "appverify${Calendar.getInstance().timeInMillis}",
        )

        return flow {
            emit(Resource.Loading())
            emitAll(remoteDataSource.verifyCheckinCheckout(
                accessToken = localDataSource.getLoggedTenant()?.accessToken,
                request = request,
            ).map {
                when (it) {
                    is ApiResponse.Success -> {
                        Resource.Success(
                            DataMapper.mapVerifyCheckinCheckoutResponseToUser(
                                user = user,
                                input = it.data,
                            )
                        )
                    }
                    is ApiResponse.Error -> {
                        Resource.Error(it.errorMessage)
                    }
                }
            })
        }
    }

    override fun identifyCheckIn(user: User): Flow<Resource<CheckIn>> {
        val imageBase64 = ByteArrayOutputStream().use { outputStream ->
            Base64OutputStream(outputStream, Base64.DEFAULT).use { base64FilterStream ->
                user.image?.file?.inputStream().use { inputStream ->
                    inputStream?.copyTo(base64FilterStream)
                }
            }

            return@use outputStream.toString()
        }

        val request = IdentifyCheckinCheckoutRequest(
            qrCode = ConstNetwork.QR_CODE_CHECK_IN,
            latitude = ConstNetwork.LATITUDE,
            longitude = ConstNetwork.LONGITUDE,
            faceGalleryId = ConstNetwork.FACE_GALLERY_ID,
            //faceGalleryId = localDataSource.getLoggedFaceGalleryId()?.facegalleryId,
            image = imageBase64,
            trxId = "appverify${Calendar.getInstance().timeInMillis}",
        )

        return flow {
            emit(Resource.Loading())
            emitAll(remoteDataSource.identifyCheckinCheckout(
                accessToken = localDataSource.getLoggedTenant()?.accessToken,
                request = request,
            ).map {
                when (it) {
                    is ApiResponse.Success -> {
                        Resource.Success(
                            CheckIn(
                                date = it.data.plData?.checkInTime?.formatDate(
                                    outputFormat = "dd MMM yyyy"
                                ),
                                time = "${it.data.plData?.checkInTime?.formatDate(outputFormat = "HH:mm")} ${
                                    it.data.plData?.checkInTime?.takeLast(5)?.toTimeZone()
                                }",
                                user = DataMapper.mapIdentifyCheckinCheckoutResponseToUser(it.data),
                                place = DataMapper.mapIdentifyCheckinCheckoutResponseToPlace(it.data),
                            )
                        )
                    }
                    is ApiResponse.Error -> {
                        Resource.Error(it.errorMessage)
                    }
                }
            })
        }
    }

    override fun identifyCheckOut(user: User): Flow<Resource<CheckOut>> {
        val imageBase64 = ByteArrayOutputStream().use { outputStream ->
            Base64OutputStream(outputStream, Base64.DEFAULT).use { base64FilterStream ->
                user.image?.file?.inputStream().use { inputStream ->
                    inputStream?.copyTo(base64FilterStream)
                }
            }

            return@use outputStream.toString()
        }

        val request = IdentifyCheckinCheckoutRequest(
            qrCode = ConstNetwork.QR_CODE_CHECK_OUT,
            latitude = ConstNetwork.LATITUDE,
            longitude = ConstNetwork.LONGITUDE,
            faceGalleryId = ConstNetwork.FACE_GALLERY_ID,
            //faceGalleryId = localDataSource.getLoggedFaceGalleryId()?.facegalleryId,
            image = imageBase64,
            trxId = "appverify${Calendar.getInstance().timeInMillis}",
        )

        return flow {
            emit(Resource.Loading())
            emitAll(remoteDataSource.identifyCheckinCheckout(
                accessToken = localDataSource.getLoggedTenant()?.accessToken,
                request = request,
            ).map {
                when (it) {
                    is ApiResponse.Success -> {
                        Resource.Success(
                            CheckOut(
                                date = it.data.plData?.checkOutTime?.formatDate(
                                    outputFormat = "dd MMM yyyy"
                                ),
                                time = "${it.data.plData?.checkOutTime?.formatDate(outputFormat = "HH:mm")} ${
                                    it.data.plData?.checkOutTime?.takeLast(5)?.toTimeZone()
                                }",
                                user = DataMapper.mapIdentifyCheckinCheckoutResponseToUser(it.data),
                                place = DataMapper.mapIdentifyCheckinCheckoutResponseToPlace(it.data)
                            )
                        )
                    }
                    is ApiResponse.Error -> {
                        Resource.Error(it.errorMessage)
                    }
                }
            })
        }
    }
}