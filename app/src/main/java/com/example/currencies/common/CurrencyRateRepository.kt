package com.example.currencies.common

import com.example.currencies.common.entities.Currency
import com.example.currencies.common.entities.CurrencyRate
import java.util.Date

interface CurrencyRateRepository {

    suspend fun getLatestCurrencyRates(baseCurrency: Currency, targetCurrencies: List<Currency>): List<CurrencyRate>

    suspend fun getHistoricalCurrencyRates(baseCurrency: Currency, targetCurrencies: List<Currency>, date: Date): List<CurrencyRate>

}