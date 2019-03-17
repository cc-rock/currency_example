package com.example.currencies.conversion.usecase

import com.example.currencies.common.CurrencyRateRepository
import com.example.currencies.common.entities.Amount
import com.example.currencies.common.entities.Currency
import javax.inject.Inject

/**
 * Gets latest rates for the given base and target currecies, and uses them to convert the given amount
 */
class ConvertAmountWithLatestRates @Inject constructor(private val ratesRepository: CurrencyRateRepository) {

    suspend fun execute(amountToConvert: Amount, targetCurrencies: List<Currency>): List<Amount> {
        return ratesRepository.getLatestCurrencyRates(amountToConvert.currency, targetCurrencies).map {
            Amount(it.toCurrency, amountToConvert.value * it.rate)
        }
    }

}