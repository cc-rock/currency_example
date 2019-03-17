package com.example.currencies.common.fixer

import com.example.currencies.common.entities.Currency
import com.example.currencies.common.entities.CurrencyRate
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class FixerResponseConverter @Inject constructor() {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun convert(jsonResponse: FixerJsonResponse): List<CurrencyRate> = jsonResponse.rates.map {
        CurrencyRate(
            stringToCurrency(jsonResponse.base),
            stringToCurrency(it.key),
            it.value,
            stringToDate(jsonResponse.date)
        )
    }

    private fun stringToCurrency(code: String) =
        Currency.values().firstOrNull { it.code == code } ?: throw Exception("Unknown currency")

    private fun stringToDate(formattedDate: String) = dateFormat.parse(formattedDate)

}