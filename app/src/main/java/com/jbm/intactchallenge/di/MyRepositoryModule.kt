package com.jbm.intactchallenge.di

import android.content.Context
import com.jbm.intactchallenge.model.Catalog
import com.jbm.intactchallenge.model.MyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MyRepositoryModule {
    //This will provide the MyRepository instance for hilt injections
    @Provides
    @Singleton
    fun provideRepo(@ApplicationContext context: Context, catalog: Catalog):
            MyRepository = MyRepository(context, catalog)
}