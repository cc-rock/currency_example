package com.example.currencies.common.fixer

import com.example.currencies.common.entities.Currency
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class FixerInputConverter @Inject constructor() {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun currencyToString(currency: Currency) = currency.code

    fun currenciesToString(currencies: List<Currency>) = currencies.joinToString(separator = ",") { currencyToString(it) }

    fun dateToString(date: Date) = dateFormat.format(date)

}