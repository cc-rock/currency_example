package com.example.currencies.conversion.presenter

import android.content.res.Resources
import com.example.currencies.R
import com.example.currencies.conversion.CurrencyConversionPresenter
import com.example.currencies.conversion.CurrencyConversionView
import com.example.currencies.common.CurrencyRateRepository
import com.example.currencies.common.di.CommonModule.Companion.IO_DISPATCHER
import com.example.currencies.common.di.CommonModule.Companion.MAIN_DISPATCHER
import com.example.currencies.common.entities.Currency
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

class CurrencyConversionPresenterImpl @Inject constructor(private val currencyRateRepository: CurrencyRateRepository,
                                                          private val resources: Resources,
                                                          @param:Named(IO_DISPATCHER) private val ioDispatcher: CoroutineDispatcher,
                                                          @param:Named(MAIN_DISPATCHER) private val mainThreadDispatcher: CoroutineDispatcher): CurrencyConversionPresenter, CoroutineScope {

    companion object {
        private val BASE_CURRENCY = Currency.EUR
        private val TARGET_CURRENCIES = Currency.values().toList()
    }

    // coroutine scope implementation with cancellable parent job
    private lateinit var parentJob: Job
    override val coroutineContext: CoroutineContext
        get() = ioDispatcher + parentJob

    private var view: CurrencyConversionView? = null

    override fun viewCreated(newView: CurrencyConversionView) {
        view = newView
        parentJob = Job()
        view?.displayInputHintText(resources.getString(R.string.hint_text, BASE_CURRENCY.code))
    }

    override fun onAmountTextChanged(amountText: String) {
        val baseAmount = amountText.toFloat()

        // launch background coroutine with io dispatcher
        launch {
            val rows = currencyRateRepository.getLatestCurrencyRates(
                BASE_CURRENCY,
                TARGET_CURRENCIES
            ).map {
                CurrencyConversionView.Row(
                    it.toCurrency,
                    it.toCurrency.code,
                    (it.rate * baseAmount).toString(),
                    false
                )
            }

            // nested coroutine to update view on main thread
            launch(mainThreadDispatcher) {
                view?.updateTable(rows)
            }

        }
    }

    override fun onRowClicked(currency: Currency) {
    }

    override fun viewDestroyed() {
        view = null
        parentJob.cancel()
    }

}