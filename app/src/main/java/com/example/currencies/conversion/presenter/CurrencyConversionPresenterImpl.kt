package com.example.currencies.conversion.presenter

import android.content.res.Resources
import android.os.Bundle
import com.example.currencies.R
import com.example.currencies.conversion.CurrencyConversionPresenter
import com.example.currencies.conversion.CurrencyConversionView
import com.example.currencies.common.di.CommonModule.Companion.IO_DISPATCHER
import com.example.currencies.common.di.CommonModule.Companion.MAIN_DISPATCHER
import com.example.currencies.common.entities.Amount
import com.example.currencies.common.entities.Currency
import com.example.currencies.conversion.CurrencyConversionNavigator
import com.example.currencies.conversion.usecase.ConvertAmountWithLatestRates
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

class CurrencyConversionPresenterImpl @Inject constructor(private val convertAmountWithLatestRates: ConvertAmountWithLatestRates,
                                                          private val resources: Resources,
                                                          private val navigator: CurrencyConversionNavigator,
                                                          @param:Named(IO_DISPATCHER) private val ioDispatcher: CoroutineDispatcher,
                                                          @param:Named(MAIN_DISPATCHER) private val mainThreadDispatcher: CoroutineDispatcher): CurrencyConversionPresenter, CoroutineScope {

    companion object {
        private val BASE_CURRENCY = Currency.EUR
        private val TARGET_CURRENCIES = Currency.values().toList() - Currency.EUR

        // Bundle keys
        private const val KEY_LAST_QUERIED_AMOUNT = "CurrencyConversionPresenterImpl.KEY_LAST_QUERIED_AMOUNT"
        private const val KEY_CURRENT_SELECTION = "CurrencyConversionPresenterImpl.KEY_CURRENT_SELECTION"
    }

    // coroutine scope implementation
    private lateinit var mainJob: Job
    override val coroutineContext: CoroutineContext
        get() = mainThreadDispatcher + mainJob

    private var view: CurrencyConversionView? = null

    // current state
    private var currentSelection: CurrencyConversionView.Selection = CurrencyConversionView.Selection(null, null)
    private var lastQueriedAmount: Amount? = null

    override fun viewCreated(newView: CurrencyConversionView, savedState: Bundle?) {
        view = newView
        mainJob = Job()
        view?.showInputHintText(resources.getString(R.string.hint_text, BASE_CURRENCY.code))
        savedState?.let {
            lastQueriedAmount = it.getParcelable(KEY_LAST_QUERIED_AMOUNT)
            currentSelection = it.getParcelable(KEY_CURRENT_SELECTION) ?: CurrencyConversionView.Selection(null, null)
        }
        updateButton()
    }

    override fun onSaveState(outState: Bundle) {
        outState.putParcelable(KEY_LAST_QUERIED_AMOUNT, lastQueriedAmount)
        outState.putParcelable(KEY_CURRENT_SELECTION, currentSelection)
    }

    override fun onAmountTextChanged(amountText: String) {
        val amountToConvert = Amount(
            BASE_CURRENCY,
            amountText.toFloatOrNull() ?: 0f
        )
        retrieveAndShowData(amountToConvert)
    }

    private fun retrieveAndShowData(amountToConvert: Amount) {
        // launch coroutine in our scope (on main thread)
        launch {
            val convertedAmounts = try {
                // execute use case on worker thread
                withContext(ioDispatcher) {
                    convertAmountWithLatestRates.execute(amountToConvert, TARGET_CURRENCIES)
                }
            } catch (e: Throwable) {
                if (e !is CancellationException) {
                    view?.showError()
                }
                return@launch
            }
            view?.showRows(
                convertedAmounts.map {
                    CurrencyConversionView.Row(
                        it.currency,
                        it.currency.code,
                        it.value.toString()
                    )
                }
            )
            lastQueriedAmount = amountToConvert
        }
    }

    override fun onRowClicked(currency: Currency) {
        // ignore if already selected
        if (currentSelection.contains(currency)) return

        // rotate selection to keep the latest two
        currentSelection = CurrencyConversionView.Selection(
            currency,
            currentSelection.firstSelected
        )
        view?.showSelection(currentSelection)
        updateButton()
    }

    private fun updateButton() {
        view?.enableCompareButton(
            currentSelection.firstSelected != null && currentSelection.secondSelected != null
        )
    }

    override fun onCompareClicked() {
        if (lastQueriedAmount != null && currentSelection.firstSelected != null && currentSelection.secondSelected != null) {
            navigator.goToCurrencyCompare(
                lastQueriedAmount!!,
                currentSelection.firstSelected!!,
                currentSelection.secondSelected!!
            )
        }
    }

    override fun viewDestroyed() {
        view = null
        mainJob.cancel()
    }

}