package com.example.currencies.common.fixer

import com.example.currencies.common.CurrencyRateRepository
import com.example.currencies.common.entities.Currency
import com.example.currencies.common.entities.CurrencyRate
import java.util.*
import javax.inject.Inject

class FixerRetrofitCurrencyRateRepository @Inject constructor(private val fixerApi: FixerApi,
                                                              private val inputConverter: FixerInputConverter,
                                                              private val responseConverter: FixerResponseConverter): CurrencyRateRepository {

    companion object {
        private const val API_KEY = "b90617e77d89b12cfebeb90018c45b95"
    }

    override suspend fun getLatestCurrencyRates(
        baseCurrency: Currency,
        targetCurrencies: List<Currency>
    ): List<CurrencyRate> = responseConverter.convert(
        fixerApi.latestRates(
            API_KEY,
            inputConverter.currencyToString(baseCurrency),
            inputConverter.currenciesToString(targetCurrencies)
        )
    )

    override suspend fun getHistoricalCurrencyRates(
        baseCurrency: Currency,
        targetCurrencies: List<Currency>,
        date: Date
    ): List<CurrencyRate> = responseConverter.convert(
        fixerApi.historicalRates(
            API_KEY,
            inputConverter.currencyToString(baseCurrency),
            inputConverter.currenciesToString(targetCurrencies),
            inputConverter.dateToString(date)
        )
    )

}