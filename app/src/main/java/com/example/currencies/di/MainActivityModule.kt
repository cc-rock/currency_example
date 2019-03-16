package com.example.currencies.di

import com.example.currencies.conversion.view.CurrencyConversionFragment
import com.example.currencies.conversion.di.CurrencyConversionModule
import com.example.currencies.compare.view.CurrencyCompareFragment
import com.example.currencies.compare.di.CurrencyCompareModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {

    // fragment injectors

    @FragmentScope
    @ContributesAndroidInjector(modules = [CurrencyConversionModule::class])
    abstract fun createCurrencyConversionFragmentInjector(): CurrencyConversionFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [CurrencyCompareModule::class])
    abstract fun createCurrencyCompareFragmentInjector(): CurrencyCompareFragment

}