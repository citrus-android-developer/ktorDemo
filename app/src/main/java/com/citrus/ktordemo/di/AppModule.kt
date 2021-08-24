package com.citrus.ktordemo.di


import android.content.Context
import com.citrus.ktordemo.data.remote.api.ApiService
import com.citrus.ktordemo.util.DispatcherProvider
import com.citrus.ktordemo.util.clientId
import com.citrus.ktordemo.util.dataStore
import com.google.gson.Gson
import com.skydoves.sandwich.coroutines.CoroutinesResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val DEFAULT_CONNECT_TIME = 10L
    private const val DEFAULT_WRITE_TIME = 30L
    private const val DEFAULT_READ_TIME = 30L

    @WSOKHttp
    @Singleton
    @Provides
    fun provideOkHttpClient(clientId: String): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val url = chain.request().url.newBuilder()
                    .addQueryParameter("client_id", clientId)
                    .build()
                val request = chain.request().newBuilder()
               //     .addHeader("X-Authorization", "R3YKZFKBVi")
                    .url(url)
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @ServiceOKHttp
    @Provides
    @Singleton
    fun okHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(DEFAULT_CONNECT_TIME, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_WRITE_TIME, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_READ_TIME, TimeUnit.SECONDS)
            .build()

    @Singleton
    @Provides
    fun provideClientId(@ApplicationContext context: Context): String {
        return runBlocking { context.dataStore.clientId() }
    }

    @Provides
    @Singleton
    fun provideRetrofit( @ServiceOKHttp okHttpClient:OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(ApiService.BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(CoroutinesResponseCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideLocalApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)


    @Singleton
    @Provides
    fun provideApplicationContext(
        @ApplicationContext context: Context
    ) = context

    @Singleton
    @Provides
    fun provideGsonInstance(): Gson {
        return Gson()
    }

    @Singleton
    @Provides
    fun provideDispatcherProvider(): DispatcherProvider {
        return object : DispatcherProvider {
            override val main: CoroutineDispatcher
                get() = Dispatchers.Main
            override val io: CoroutineDispatcher
                get() = Dispatchers.IO
            override val default: CoroutineDispatcher
                get() = Dispatchers.Default
        }
    }


}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class WSOKHttp

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ServiceOKHttp