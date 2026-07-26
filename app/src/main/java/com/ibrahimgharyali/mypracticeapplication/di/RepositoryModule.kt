package com.ibrahimgharyali.mypracticeapplication.di

import com.ibrahimgharyali.mypracticeapplication.data.TaskRepositoryImpl
import com.ibrahimgharyali.mypracticeapplication.domain.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindsTaskRepository(repository: TaskRepositoryImpl) : TaskRepository
}