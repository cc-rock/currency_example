package com.example.currencies.common.entities

import java.util.*

/**
 * Contains a list of amounts computed with historical currency rates
 */
data class HistoricalAmounts(val date: Date, val amounts: List<Amount>)