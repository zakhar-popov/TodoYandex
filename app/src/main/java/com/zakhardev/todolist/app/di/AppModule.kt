package com.zakhardev.todolist.app.di

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.zakhardev.todolist.todos_list.data.api.AuthInterceptor
import com.zakhardev.todolist.todos_list.data.api.TodosApi
import com.zakhardev.todolist.todos_list.data.datasourse.RemoteTodosBackend
import com.zakhardev.todolist.todos_list.data.datasourse.FileStorage
import com.zakhardev.todolist.todos_list.data.repository.TodosRepository
import com.zakhardev.todolist.todos_list.domain.repository.TodosBackend
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "https://hive.mrdekk.ru/todo/"
    private const val TOKEN = "dfd8dee1-208c-4bd5-ab97-bc59c2e022e4"

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(TOKEN))
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        json: Json,
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideTodosApi(retrofit: Retrofit): TodosApi = retrofit.create(TodosApi::class.java)

    @Provides
    @Singleton
    fun provideTodosBackend(
        api: TodosApi
    ): TodosBackend = RemoteTodosBackend(api = api)

    @Provides
    @Singleton
    fun provideFileStorage(
        @ApplicationContext context: Context
    ): FileStorage = FileStorage(context = context)

    @Provides
    @Singleton
    fun provideTodosRepository(
        storage: FileStorage,
        backend: TodosBackend
    ): TodosRepository = TodosRepository(
        storage = storage,
        backend = backend
    )
}