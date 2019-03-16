package com.example.currencies.common.entities

import java.util.Date

class CurrencyRate(val baseCurrency: Currency,
                   val toCurrency: Currency,
                   val rate: Float,
                   val date: Date)