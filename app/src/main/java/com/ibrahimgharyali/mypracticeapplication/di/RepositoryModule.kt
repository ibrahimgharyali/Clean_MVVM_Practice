package com.ibrahimgharyali.mypracticeapplication.di;

import com.ibrahimgharyali.mypracticeapplication.data.UserRepositoryImpl
import com.ibrahimgharyali.mypracticeapplication.domain.repository.UserRepository
import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindsUserRepository(impl: UserRepositoryImpl): UserRepository
}

