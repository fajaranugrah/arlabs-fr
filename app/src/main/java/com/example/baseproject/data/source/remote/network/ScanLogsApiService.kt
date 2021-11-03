package com.example.baseproject.data.source.remote.network

import com.example.baseproject.data.source.remote.request.ScanLogsRequest
import com.example.baseproject.data.source.remote.response.ScanLogsResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ScanLogsApiService {

    @POST("resource/Scan Logs")
    suspend fun scanLogs(
        @Header("Authorization") authorization: String?,
        @Body request: ScanLogsRequest?,
    ): ScanLogsResponse
}
