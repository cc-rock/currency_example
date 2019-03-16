package com.example.currencies.conversion

import com.example.currencies.common.entities.Currency

interface CurrencyConversionView {

    fun displayInputHintText(hint: String)

    fun updateTable(rows: List<Row>)

    data class Row(val currency: Currency, val currencyLabel: String, val amount: String, val selected: Boolean)

}

interface CurrencyConversionPresenter {

    fun viewCreated(newView: CurrencyConversionView)

    fun onAmountTextChanged(amountText: String)

    fun onRowClicked(currency: Currency)

    fun viewDestroyed()
}