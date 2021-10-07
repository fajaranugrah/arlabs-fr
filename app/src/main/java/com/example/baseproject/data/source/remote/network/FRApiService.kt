package com.example.baseproject.data.source.remote.network

import com.example.baseproject.data.source.remote.request.EnrollFaceRequest
import com.example.baseproject.data.source.remote.request.IdentifyCheckinCheckoutRequest
import com.example.baseproject.data.source.remote.request.RecognizeFaceRequest
import com.example.baseproject.data.source.remote.request.VerifyCheckinCheckoutRequest
import com.example.baseproject.data.source.remote.response.EnrollFaceResponse
import com.example.baseproject.data.source.remote.response.IdentifyCheckinCheckoutResponse
import com.example.baseproject.data.source.remote.response.RecognizeFaceResponse
import com.example.baseproject.data.source.remote.response.VerifyCheckinCheckoutResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface FRApiService {

    @POST("pedulilindungi/enroll-face")
    suspend fun enrollFace(
        @Header("Accesstoken") accessToken: String?,
        @Body request: EnrollFaceRequest?,
    ): EnrollFaceResponse

    @POST("facegallery/recognize-face")
    suspend fun recognizeFace(
        @Header("Accesstoken") accessToken: String?,
        @Body request: RecognizeFaceRequest?,
    ): RecognizeFaceResponse

    @POST("pedulilindungi/verify-checkin-checkout")
    suspend fun verifyCheckinCheckout(
        @Header("Accesstoken") accessToken: String?,
        @Body request: VerifyCheckinCheckoutRequest?,
    ): VerifyCheckinCheckoutResponse

    @POST("pedulilindungi/identify-checkin-checkout")
    suspend fun identifyCheckinCheckout(
        @Header("Accesstoken") accessToken: String?,
        @Body request: IdentifyCheckinCheckoutRequest?,
    ): IdentifyCheckinCheckoutResponse
}