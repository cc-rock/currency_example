package com.example.currencies.compare

import com.example.currencies.common.entities.Amount
import com.example.currencies.common.entities.Currency

interface CurrencyCompareView {

    fun showAmount(amountAndCurrency: String)

    fun showTable(header: HeaderRow, rows: List<Row>)

    fun showLoading()

    fun hideLoading()

    fun showError()

    class HeaderRow(val dateHeader: String, val firstAmountHeader: String, val secondAmountHeader: String)
    class Row(val date: String, val firstAmount: String, val secondAmount: String)

}

interface CurrencyComparePresenter {

    fun viewCreated(newView: CurrencyCompareView)

    fun viewDestroyed()

    fun loadData(amountToConvert: Amount, firstCurrency: Currency, secondCurrency: Currency)

}