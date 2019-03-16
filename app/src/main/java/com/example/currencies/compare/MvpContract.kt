package com.example.currencies.compare

interface CurrencyCompareView {

    fun displayAmount(amountAndCurrency: String)

    fun displayTable(header: HeaderRow, rows: List<Row>)

    class HeaderRow(val dateHeader: String, val firstAmountHeader: String, val secondAmountHeader: String)
    class Row(val date: String, val firstAmount: String, val secondAmount: String)

}

interface CurrencyComparePresenter {

    fun onViewShown()

}