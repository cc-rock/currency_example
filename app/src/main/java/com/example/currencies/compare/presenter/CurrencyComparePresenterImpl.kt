package com.example.currencies.compare.presenter

import android.content.res.Resources
import com.example.currencies.R
import com.example.currencies.common.di.CommonModule.Companion.IO_DISPATCHER
import com.example.currencies.common.di.CommonModule.Companion.MAIN_DISPATCHER
import com.example.currencies.common.entities.Amount
import com.example.currencies.common.entities.Currency
import com.example.currencies.compare.CurrencyComparePresenter
import com.example.currencies.compare.CurrencyCompareView
import com.example.currencies.compare.usecase.ConvertAmountWithHistoricalRates
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

class CurrencyComparePresenterImpl @Inject constructor(private val resources: Resources,
                                                       private val calendar: Calendar,
                                                       private val convertAmountWithHistoricalRates: ConvertAmountWithHistoricalRates,
                                                       @param:Named(IO_DISPATCHER) private val ioDispatcher: CoroutineDispatcher,
                                                       @param:Named(MAIN_DISPATCHER) private val mainThreadDispatcher: CoroutineDispatcher):
    CurrencyComparePresenter, CoroutineScope {

    companion object {
        private const val NUMBER_OF_DAYS = 5
    }

    // coroutine scope implementation
    private lateinit var mainJob: Job
    override val coroutineContext: CoroutineContext
        get() = mainThreadDispatcher + mainJob

    private var view: CurrencyCompareView? = null

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun viewCreated(newView: CurrencyCompareView) {
        view = newView
        mainJob = Job()
    }

    override fun loadData(amountToConvert: Amount, firstCurrency: Currency, secondCurrency: Currency) {
        launch {
            view?.showAmount(
                resources.getString(R.string.amount_and_currency, amountToConvert.value, amountToConvert.currency.code)
            )
            view?.showLoading()
            val dates = (0..(NUMBER_OF_DAYS - 1)).map {
                calendar.time = Date()
                calendar.add(Calendar.DATE, -it)
                calendar.time
            }
            val historicalAmounts = try {
                withContext(ioDispatcher) {
                    convertAmountWithHistoricalRates.execute(this, amountToConvert, listOf(firstCurrency, secondCurrency), dates)
                }
            } catch(e: Throwable) {
                if (e !is CancellationException) {
                    view?.showError()
                    view?.hideLoading()
                }
                return@launch
            }
            view?.hideLoading()
            view?.showTable(
                CurrencyCompareView.HeaderRow(
                    resources.getString(R.string.date),
                    firstCurrency.code,
                    secondCurrency.code
                ),
                historicalAmounts.map {
                    CurrencyCompareView.Row(
                        dateFormat.format(it.date),
                        findAmountForCurrency(it.amounts, firstCurrency)?.value.toString(),
                        findAmountForCurrency(it.amounts, secondCurrency)?.value.toString()
                    )
                }
            )
        }
    }

    private fun findAmountForCurrency(amounts: List<Amount>, currency: Currency): Amount? {
        return amounts.firstOrNull { it.currency == currency }
    }

    override fun viewDestroyed() {
        mainJob.cancel()
        view = null
    }

}