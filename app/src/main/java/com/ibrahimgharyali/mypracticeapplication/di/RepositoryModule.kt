package com.ibrahimgharyali.mypracticeapplication.di

import com.ibrahimgharyali.mypracticeapplication.data.ProductRepositoryImpl
import com.ibrahimgharyali.mypracticeapplication.domain.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindsProductRepository(repository: ProductRepositoryImpl) : ProductRepository
}