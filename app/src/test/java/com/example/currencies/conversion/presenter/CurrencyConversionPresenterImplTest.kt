package com.example.currencies.conversion.presenter

import android.content.res.Resources
import android.os.Bundle
import androidx.core.os.bundleOf
import com.example.currencies.R
import com.example.currencies.conversion.CurrencyConversionView
import com.example.currencies.common.CurrencyRateRepository
import com.example.currencies.common.entities.Amount
import com.example.currencies.common.entities.Currency
import com.example.currencies.common.entities.CurrencyRate
import com.example.currencies.conversion.CurrencyConversionNavigator
import com.example.currencies.conversion.usecase.ConvertAmountWithLatestRates
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.stubbing.OngoingStubbing
import java.lang.Exception
import java.util.*

class CurrencyConversionPresenterImplTest {

    companion object {
        private val BASE_CURRENCY = Currency.EUR
        private val TARGET_CURRENCIES = Currency.values().toList() - Currency.EUR

        // Bundle keys
        private const val KEY_LAST_QUERIED_AMOUNT = "CurrencyConversionPresenterImpl.KEY_LAST_QUERIED_AMOUNT"
        private const val KEY_CURRENT_SELECTION = "CurrencyConversionPresenterImpl.KEY_CURRENT_SELECTION"
    }

    @get:Rule
    val rule : MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var convertAmountWithLatestRates: ConvertAmountWithLatestRates

    @Mock
    lateinit var resources: Resources

    @Mock
    lateinit var navigator: CurrencyConversionNavigator

    @Mock
    lateinit var view: CurrencyConversionView

    lateinit var presenter: CurrencyConversionPresenterImpl

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        presenter = CurrencyConversionPresenterImpl(
            convertAmountWithLatestRates, resources,
            navigator, Dispatchers.Unconfined, Dispatchers.Unconfined
        )
        whenever(resources.getString(R.string.hint_text, BASE_CURRENCY.code)).thenReturn("test hint")
    }

    @Test
    fun `the hint text is set when the view is created`() {
        presenter.viewCreated(view, null)
        verify(view).showInputHintText("test hint")
    }

    @Test
    fun `when the view is created without saved state, the button is disabled`() {
        presenter.viewCreated(view, null)
        verify(view).enableCompareButton(false)
    }

    @Test
    fun `when the view is created and the saved state has one currency selected, the button is disabled`() {
        val state: Bundle = mock()
        whenever(state.getParcelable<CurrencyConversionView.Selection>(KEY_CURRENT_SELECTION)).thenReturn(
            CurrencyConversionView.Selection(Currency.GBP, null)
        )
        presenter.viewCreated(
            view,
            state
        )
        verify(view).enableCompareButton(false)
    }

    @Test
    fun `when the view is created and the saved state has two currencies selected, the button is enabled`() {
        val state: Bundle = mock()
        whenever(state.getParcelable<CurrencyConversionView.Selection>(KEY_CURRENT_SELECTION)).thenReturn(
            CurrencyConversionView.Selection(Currency.GBP, Currency.AUD)
        )
        presenter.viewCreated(
            view,
            state
        )
        verify(view).enableCompareButton(true)
    }

    @Test
    fun `when an amount is entered, the conversion is calculated and the results are displayed`() {
        presenter.viewCreated(view, null)
        runBlocking {
            whenever(convertAmountWithLatestRates.execute(Amount(BASE_CURRENCY, 16.5f), TARGET_CURRENCIES)).thenReturn(
                listOf(
                    Amount(Currency.AUD, 18.4f),
                    Amount(Currency.JPY, 23.56f),
                    Amount(Currency.CHF, 6.12f)
                )
            )

            presenter.onAmountTextChanged("16.5")

            // todo find a way to skip delays when testing with coroutines
            // maybe wrap the delay function into a component that can be mocked in tests
            delay(400)

            with(inOrder(view, convertAmountWithLatestRates)) {
                verify(view, times(2)).enableCompareButton(false)
                verify(convertAmountWithLatestRates).execute(
                    Amount(BASE_CURRENCY, 16.5f), TARGET_CURRENCIES
                )
                verify(view).showRows(
                    listOf(
                        CurrencyConversionView.Row(Currency.AUD, "AUD", "18.4"),
                        CurrencyConversionView.Row(Currency.JPY, "JPY", "23.56"),
                        CurrencyConversionView.Row(Currency.CHF, "CHF", "6.12")
                    )
                )
                verify(view).enableCompareButton(false)
                verifyNoMoreInteractions()
            }
        }
    }

    @Test
    fun `when more than one amounts are entered in a very short time, only the latest is sent to the use case`() {
        presenter.viewCreated(view, null)
        runBlocking {
            whenever(convertAmountWithLatestRates.execute(any(), eq(TARGET_CURRENCIES))).thenReturn(
                listOf(
                    Amount(Currency.AUD, 18.4f),
                    Amount(Currency.JPY, 23.56f),
                    Amount(Currency.CHF, 6.12f)
                )
            )

            presenter.onAmountTextChanged("16.5")
            delay(50)
            presenter.onAmountTextChanged("16.54")
            delay(100)
            presenter.onAmountTextChanged("16.546")

            // todo find a way to skip delays when testing with coroutines
            // maybe wrap the delay function into a component that can be mocked in tests
            delay(400)

            verify(convertAmountWithLatestRates, times(0)).execute(Amount(BASE_CURRENCY, 16.5f), TARGET_CURRENCIES)
            verify(convertAmountWithLatestRates, times(0)).execute(Amount(BASE_CURRENCY, 16.54f), TARGET_CURRENCIES)
            verify(convertAmountWithLatestRates, times(1)).execute(Amount(BASE_CURRENCY, 16.546f), TARGET_CURRENCIES)
        }
    }

    @Test
    fun `when an exception is thrown while converting the amount, an error is shown by the view`() {
        presenter.viewCreated(view, null)
        runBlocking {
            whenever(convertAmountWithLatestRates.execute(any(), eq(TARGET_CURRENCIES))).thenThrow(
                RuntimeException("test exception")
            )

            presenter.onAmountTextChanged("16.5")

            // todo find a way to skip delays when testing with coroutines
            // maybe wrap the delay function into a component that can be mocked in tests
            delay(400)
            with(inOrder(view, convertAmountWithLatestRates)) {
                verify(view, times(2)).enableCompareButton(false)
                verify(convertAmountWithLatestRates).execute(
                    Amount(BASE_CURRENCY, 16.5f), TARGET_CURRENCIES
                )
                verify(view).showError()
                verifyNoMoreInteractions()
            }

        }
    }

    @Test
    fun `when the view is destroyed while the amounts are being converted, the job is cancelled and no more calls are made on the view`() {
        presenter.viewCreated(view, null)
        runBlocking {
            presenter.onAmountTextChanged("16.5")
            delay(10)
            presenter.viewDestroyed()

            delay(400)

            with(inOrder(view, convertAmountWithLatestRates)) {
                verify(view, times(2)).enableCompareButton(false)
                verifyNoMoreInteractions()
            }
        }
    }

    @Test
    fun `after 1 row is clicked, the selection is updated on the view and the button remains disabled`() {
        presenter.viewCreated(view, null)
        presenter.onRowClicked(Currency.GBP)
        with(inOrder(view)) {
            verify(view).enableCompareButton(false)
            verify(view).showSelection(CurrencyConversionView.Selection(Currency.GBP, null))
            verify(view).enableCompareButton(false)
            verifyNoMoreInteractions()
        }
    }

    @Test
    fun `after 2 rows are clicked, the selection is updated on the view and the button is enabled`() {
        presenter.viewCreated(view, null)
        presenter.onRowClicked(Currency.GBP)
        presenter.onRowClicked(Currency.AUD)
        with(inOrder(view)) {
            verify(view).enableCompareButton(false)
            verify(view).showSelection(CurrencyConversionView.Selection(Currency.GBP, null))
            verify(view).enableCompareButton(false)
            verify(view).showSelection(CurrencyConversionView.Selection(Currency.AUD, Currency.GBP))
            verify(view).enableCompareButton(true)
            verifyNoMoreInteractions()
        }
    }

    @Test
    fun `after 3 rows are clicked, only the last 2 remain selected, and the button is enabled`() {
        presenter.viewCreated(view, null)
        presenter.onRowClicked(Currency.GBP)
        presenter.onRowClicked(Currency.AUD)
        presenter.onRowClicked(Currency.USD)
        with(inOrder(view)) {
            verify(view).enableCompareButton(false)
            verify(view).showSelection(CurrencyConversionView.Selection(Currency.GBP, null))
            verify(view).enableCompareButton(false)
            verify(view).showSelection(CurrencyConversionView.Selection(Currency.AUD, Currency.GBP))
            verify(view).enableCompareButton(true)
            verify(view).showSelection(CurrencyConversionView.Selection(Currency.USD, Currency.AUD))
            verify(view).enableCompareButton(true)
            verifyNoMoreInteractions()
        }
    }

}