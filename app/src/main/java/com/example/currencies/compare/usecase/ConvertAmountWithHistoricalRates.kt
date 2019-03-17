package com.example.currencies.compare.usecase

import com.example.currencies.common.CurrencyRateRepository
import com.example.currencies.common.entities.Amount
import com.example.currencies.common.entities.Currency
import com.example.currencies.common.entities.HistoricalAmounts
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import java.util.*
import javax.inject.Inject

/**
 * Retrieve the historical rate for the given dates and returns the list of amounts converted to the historical rates
 */
class ConvertAmountWithHistoricalRates @Inject constructor(private val repository: CurrencyRateRepository) {

    suspend fun execute(coroutineScope: CoroutineScope,
                        amountToConvert: Amount,
                        targetCurrencies: List<Currency>,
                        dates: List<Date>): List<HistoricalAmounts> {
        // start retrieval of the required rates in parallel
        val parallelJobs = dates.map {
            coroutineScope.async { repository.getHistoricalCurrencyRates(amountToConvert.currency, targetCurrencies, it) }
        }
        // await for each job and calculate amounts
        return parallelJobs.map { job ->
            val rates = job.await()
            HistoricalAmounts(
                rates.first().date,
                rates.map { Amount(it.toCurrency, amountToConvert.value * it.rate) }
            )
        }
    }

}