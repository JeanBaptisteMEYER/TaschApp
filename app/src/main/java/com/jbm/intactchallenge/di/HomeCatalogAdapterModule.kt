package com.jbm.intactchallenge.di

import android.content.Context
import com.jbm.intactchallenge.adapter.HomeCatalogAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HomeCatalogAdapterModule {
    //This will provide the MyRepository instance for hilt injections
    @Provides
    @Singleton
    fun provideCatalogAdapter(@ApplicationContext context: Context): HomeCatalogAdapter = HomeCatalogAdapter(context)
}