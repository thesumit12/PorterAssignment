package com.example.porterassignment.di

import android.content.Context
import com.example.porterassignment.BuildConfig
import com.example.porterassignment.common.util.Constant
import com.example.porterassignment.network.IApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val CACHE_SIZE = 10 * 1024 * 1024 // 10 MB
private const val BASE_URL = "https://assignment-mobileapi.porter.in/"


/**
 * File that initialized the Network stuff using Koin.
 * @author slakra
 * @date 09/02/2019
 */
val networkModule = module {
    single { createOkHttpClient(androidContext()) }
    single { createGsonConverter() }
    single { createRetrofitBuilder(get()) }

    single { createApiService(get(), BASE_URL) }

}

/**
 * Method used to create okHttpClient
 * @author slakra
 * @date 09/02/2019
 * @param [OkHttpClient]
 * */
fun createOkHttpClient(context: Context): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)
    val clientBuilder = OkHttpClient.Builder()
    clientBuilder.connectTimeout(Constant.TIME_OUT, TimeUnit.SECONDS)
    clientBuilder.readTimeout(Constant.TIME_OUT, TimeUnit.SECONDS)
    clientBuilder.writeTimeout(Constant.TIME_OUT, TimeUnit.SECONDS)

    // Deciding cache size and where to keep it
    val cache = Cache(context.cacheDir, CACHE_SIZE.toLong())
    clientBuilder.cache(cache)
    if (BuildConfig.DEBUG) {
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        clientBuilder.addInterceptor(httpLoggingInterceptor)
    }
    return clientBuilder.build()
}

/**
 * Method used to create GsonConverter
 * @author slakra
 * @date 09/02/2019
 * @return [Gson]
 * */
fun createGsonConverter(): Gson = GsonBuilder().setLenient().create()

/**
 * Method used to create Retrofit
 * @author slakra
 * @date 09/02/2019
 * @param okHttpClient
 * @param gson
 * @return [Retrofit]
 * */
fun createRetrofitBuilder(okHttpClient: OkHttpClient): Retrofit.Builder = Retrofit.Builder()
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())

/**
 * Method used to create Network Service to download file from server
 * @author slakra
 * @date 09/02/2019
 * @param retrofit
 * @return [IFileNService]
 * */
fun createApiService(builder: Retrofit.Builder, baseUrl: String): IApiService =
        builder.baseUrl(baseUrl).build().create(IApiService::class.java)
