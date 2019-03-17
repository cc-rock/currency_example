package com.example.currencies.conversion.presenter

import android.content.res.Resources
import com.example.currencies.R
import com.example.currencies.conversion.CurrencyConversionView
import com.example.currencies.common.CurrencyRateRepository
import com.example.currencies.common.entities.Currency
import com.example.currencies.common.entities.CurrencyRate
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.stubbing.OngoingStubbing
import java.util.*

class CurrencyConversionPresenterImplTest {

    @get:Rule
    val rule : MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var repository: CurrencyRateRepository

    @Mock
    lateinit var resources: Resources

    @Mock
    lateinit var view: CurrencyConversionView

    lateinit var presenter: CurrencyConversionPresenterImpl

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        presenter = CurrencyConversionPresenterImpl(repository, resources, Dispatchers.Unconfined, Dispatchers.Unconfined)
        whenever(resources.getString(R.string.hint_text, "EUR")).thenReturn("ciao")
    }

    @Test
    fun `coroutine test`() {
        val theDate = Date()
        whenSuspend{repository.getLatestCurrencyRates(any(), any())}.thenReturn(listOf(CurrencyRate(Currency.EUR, Currency.GBP, 1.10f, theDate)))
        presenter.viewCreated(view)
        presenter.onAmountTextChanged("10")
        verify(view).showRows(listOf(CurrencyConversionView.Row(Currency.GBP, "GBP", "11.0", false)))
        presenter.viewDestroyed()
        presenter.onAmountTextChanged("10")
        presenter.viewCreated(view)
        presenter.onAmountTextChanged("11")
        verify(view, times(1)).showRows(listOf(CurrencyConversionView.Row(Currency.GBP, "GBP", "12.1", false)))
    }

    /**
     * Enables stubbing methods. Use it when you want the mock to return particular value when particular method is called.
     *
     * Alias for [Mockito.when].
     */
    inline fun <T> whenSuspend(crossinline block: suspend () -> T): OngoingStubbing<T> {
        var stubbing: OngoingStubbing<T>? = null
        runBlocking { stubbing = whenever(block()) }
        return stubbing!!
    }
}