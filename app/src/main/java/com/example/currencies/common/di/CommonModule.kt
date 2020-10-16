package com.example.currencies.common.di

import com.example.currencies.common.CurrencyRateRepository
import com.example.currencies.common.fixer.BASE_URL
import com.example.currencies.common.fixer.FixerApi
import com.example.currencies.common.fixer.FixerRetrofitCurrencyRateRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named

@Module
abstract class CommonModule {

    companion object {

        const val MAIN_DISPATCHER = "MAIN_DISPATCHER"
        const val IO_DISPATCHER = "IO_DISPATCHER"

        @Provides
        @Named(MAIN_DISPATCHER)
        fun provideMainThreadCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.Main

        @Provides
        @Named(IO_DISPATCHER)
        fun provideIoCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.Main

        @Provides
        fun provideFixerApi(): FixerApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build().create(FixerApi::class.java)
        }

    }

    @Binds
    abstract fun bindCurrencyRatesRepository(fixerRetrofitRepo: FixerRetrofitCurrencyRateRepository): CurrencyRateRepository

}