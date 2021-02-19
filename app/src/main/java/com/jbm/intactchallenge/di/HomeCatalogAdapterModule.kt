package com.jbm.intactchallenge.di

import android.content.Context
import com.jbm.intactchallenge.adapter.HomeCatalogAdapter
import com.jbm.intactchallenge.model.Catalog
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
    fun provideCatalogAdapter(@ApplicationContext context: Context, catalog: Catalog):
            HomeCatalogAdapter = HomeCatalogAdapter(context, catalog)
}