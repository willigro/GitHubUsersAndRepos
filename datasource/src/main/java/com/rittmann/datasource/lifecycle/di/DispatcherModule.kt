package com.rittmann.datasource.lifecycle.di

import com.rittmann.datasource.lifecycle.DefaultDispatcherProvider
import com.rittmann.datasource.lifecycle.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DispatcherModule {

    @Singleton
    @Provides
    fun providesDispatcher(): DispatcherProvider = DefaultDispatcherProvider()
}