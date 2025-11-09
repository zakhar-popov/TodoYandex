package com.zakhardev.todolist.app.di

import android.content.Context
import com.zakhardev.todolist.todos_list.data.FileStorage
import com.zakhardev.todolist.todos_list.data.TodosRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFileStorage(
        @ApplicationContext context: Context
    ): FileStorage = FileStorage(context = context)

    @Provides
    @Singleton
    fun provideTodosRepository(
        storage: FileStorage
    ): TodosRepository = TodosRepository(storage = storage)

}