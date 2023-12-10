package com.srhan.bostaandroidtask.presentation.di

import com.srhan.bostaandroidtask.data.remote.ApiService
import com.srhan.bostaandroidtask.data.repository.RepositoryImp
import com.srhan.bostaandroidtask.domain.repository.Repository
import com.srhan.bostaandroidtask.domain.usecase.GetAlbumsUseCase
import com.srhan.bostaandroidtask.domain.usecase.GetPhotosUseCase
import com.srhan.bostaandroidtask.domain.usecase.GetUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {

        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideBaseUrl(): String = "https://jsonplaceholder.typicode.com"


    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(provideBaseUrl())
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)


    @Provides
    @Singleton
    fun provideRepo(apiService: ApiService): Repository =
        RepositoryImp(apiService)


    @Singleton
    @Provides
    fun provideGetUserUseCase(repository: Repository): GetUserUseCase =
        GetUserUseCase(repository)


    @Singleton
    @Provides
    fun provideGetPhotosUseCase(repository: Repository): GetPhotosUseCase =
        GetPhotosUseCase(repository)


    @Singleton
    @Provides
    fun provideGetAlbumsUseCase(repository: Repository): GetAlbumsUseCase =
        GetAlbumsUseCase(repository)


}