package com.example.currencies.conversion.di

import com.example.currencies.conversion.CurrencyConversionPresenter
import com.example.currencies.conversion.presenter.CurrencyConversionPresenterImpl
import dagger.Binds
import dagger.Module

@Module
abstract class CurrencyConversionModule {

    @Binds
    abstract fun bindPresenter(currencyConversionPresenterImpl: CurrencyConversionPresenterImpl): CurrencyConversionPresenter

}