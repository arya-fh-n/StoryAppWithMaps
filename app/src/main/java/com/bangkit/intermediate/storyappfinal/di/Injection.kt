package com.bangkit.intermediate.storyappfinal.di

import com.bangkit.intermediate.storyappfinal.BuildConfig
import com.bangkit.intermediate.storyappfinal.core.feature.auth.data.preferences.UserPreference
import com.bangkit.intermediate.storyappfinal.core.feature.auth.data.repository.AuthRepository
import com.bangkit.intermediate.storyappfinal.core.feature.auth.data.repository.UserRepository
import com.bangkit.intermediate.storyappfinal.core.feature.auth.data.service.AuthApiService
import com.bangkit.intermediate.storyappfinal.core.feature.auth.domain.service.AuthRepositoryService
import com.bangkit.intermediate.storyappfinal.core.feature.auth.domain.service.UserRepositoryService
import com.bangkit.intermediate.storyappfinal.core.feature.story.data.repository.StoryRepository
import com.bangkit.intermediate.storyappfinal.core.feature.story.data.service.StoryApiService
import com.bangkit.intermediate.storyappfinal.core.feature.story.domain.service.StoryRepositoryService
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
object Injection {

    @Provides
    @Singleton
    fun provideStoryApi(): StoryApiService {
        val loggingInterceptor =
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(StoryApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideStoryRepository(api: StoryApiService): StoryRepositoryService {
        return StoryRepository(api)
    }


    @Provides
    @Singleton
    fun provideAuthApi(): AuthApiService {
        val loggingInterceptor =
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(api: AuthApiService): AuthRepositoryService {
        return AuthRepository(api)
    }

    @Provides
    @Singleton
    fun provideUserRepository(api: UserPreference): UserRepositoryService {
        return UserRepository(api)
    }
}