package com.example.currencies.di

import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.currencies.MainActivity
import com.example.currencies.R
import com.example.currencies.conversion.view.CurrencyConversionFragment
import com.example.currencies.conversion.di.CurrencyConversionModule
import com.example.currencies.compare.view.CurrencyCompareFragment
import com.example.currencies.compare.di.CurrencyCompareModule
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {

    @Module
    companion object {

        @Provides
        @ActivityScope
        @JvmStatic
        fun provideNavController(activity: MainActivity): NavController = Navigation.findNavController(
            activity, R.id.nav_host_fragment
        )

    }

    // fragment injectors

    @FragmentScope
    @ContributesAndroidInjector(modules = [CurrencyConversionModule::class])
    abstract fun createCurrencyConversionFragmentInjector(): CurrencyConversionFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [CurrencyCompareModule::class])
    abstract fun createCurrencyCompareFragmentInjector(): CurrencyCompareFragment

}