package com.example.currencies.conversion

import android.os.Bundle
import android.os.Parcelable
import com.example.currencies.common.entities.Amount
import com.example.currencies.common.entities.Currency
import kotlinx.android.parcel.Parcelize

interface CurrencyConversionView {

    fun showInputHintText(hint: String)

    fun showRows(rows: List<Row>)

    fun showSelection(selection: Selection)

    fun enableCompareButton(enabled: Boolean)

    fun showError()

    @Parcelize
    data class Row(val currency: Currency, val currencyLabel: String, val amount: String): Parcelable

    @Parcelize
    data class Selection(val firstSelected: Currency?, val secondSelected: Currency?): Parcelable {
        fun contains(currency: Currency) =
                firstSelected == currency || secondSelected == currency
    }

}

interface CurrencyConversionPresenter {

    fun viewCreated(newView: CurrencyConversionView, savedState: Bundle?)

    fun onAmountTextChanged(amountText: String)

    fun onRowClicked(currency: Currency)

    fun onCompareClicked()

    fun onSaveState(outState: Bundle)

    fun viewDestroyed()
}

interface CurrencyConversionNavigator {
    fun goToCurrencyCompare(amount: Amount, firstCurrency: Currency, secondCurrency: Currency)
}