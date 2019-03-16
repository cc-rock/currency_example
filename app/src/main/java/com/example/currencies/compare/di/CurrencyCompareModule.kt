package com.example.currencies.compare.di

import com.example.currencies.compare.view.CurrencyCompareFragment
import com.example.currencies.compare.CurrencyComparePresenter
import com.example.currencies.compare.presenter.CurrencyComparePresenterImpl
import com.example.currencies.compare.CurrencyCompareView
import dagger.Binds
import dagger.Module

@Module
abstract class CurrencyCompareModule {

    @Binds
    abstract fun bindView(currencyCompareFragment: CurrencyCompareFragment): CurrencyCompareView

    @Binds
    abstract fun bindPresenter(currencyComparePresenterImpl: CurrencyComparePresenterImpl): CurrencyComparePresenter

}