package com.example.currencies.conversion.di

import com.example.currencies.conversion.CurrencyConversionNavigator
import com.example.currencies.conversion.CurrencyConversionPresenter
import com.example.currencies.conversion.navigation.CurrencyConversionNavigatorImpl
import com.example.currencies.conversion.presenter.CurrencyConversionPresenterImpl
import com.example.currencies.di.FragmentScope
import dagger.Binds
import dagger.Module

@Module
abstract class CurrencyConversionModule {

    @Binds
    @FragmentScope
    abstract fun bindPresenter(currencyConversionPresenterImpl: CurrencyConversionPresenterImpl): CurrencyConversionPresenter

    @Binds
    @FragmentScope
    abstract fun bindNavigator(currencyConversionNavigatorImpl: CurrencyConversionNavigatorImpl): CurrencyConversionNavigator

}