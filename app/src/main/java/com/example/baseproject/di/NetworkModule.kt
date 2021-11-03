package com.example.baseproject.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.baseproject.BuildConfig
import com.example.baseproject.data.source.remote.network.FRApiService
import com.example.baseproject.data.source.remote.network.PLApiService
import com.example.baseproject.data.source.remote.network.ScanLogsApiService
import com.example.baseproject.data.source.remote.network.TenantApiService
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
class NetworkModule {

    @Provides
    fun provideOkHttpClient(context: Context): OkHttpClient {
        val chainInterceptor = { chain: Interceptor.Chain ->
            chain.proceed(
                chain.request().newBuilder()
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .build()
            )
        }
        val chuckerInterceptorBuilder = ChuckerInterceptor.Builder(context)
        val httpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(chainInterceptor)
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            httpClient
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(chuckerInterceptorBuilder.build())
        }

        return httpClient.build()
    }

    @Provides
    fun provideTenantApiService(client: OkHttpClient): TenantApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL_TENANT)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(TenantApiService::class.java)
    }

    @Provides
    fun provideFRApiService(client: OkHttpClient): FRApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL_FR)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(FRApiService::class.java)
    }

    @Provides
    fun providePLApiService(client: OkHttpClient): PLApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL_PL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(PLApiService::class.java)
    }

    @Provides
    fun provideLoketApiService(client: OkHttpClient): ScanLogsApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://139.162.21.55/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ScanLogsApiService::class.java)
    }
}