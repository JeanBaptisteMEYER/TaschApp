package com.jbm.intactchallenge.di

import com.jbm.intactchallenge.model.Catalog
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CatalogModule {
    @Provides
    @Singleton
    fun provideCatalog(): Catalog = Catalog()
}