package com.example.currencies.di

import android.app.Application
import android.content.Context
import android.content.res.Resources
import com.example.currencies.MainActivity
import com.example.currencies.common.di.CommonModule
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module(includes = [CommonModule::class])
abstract class AppModule {

    @Module
    companion object {

        @Provides
        @JvmStatic
        fun provideApplicationContext(application: Application): Context {
            return application.applicationContext
        }

        @Provides
        @JvmStatic
        fun provideResources(context: Context): Resources {
            return context.resources
        }

    }

    @ActivityScope
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun createMainActivityInjector(): MainActivity

}