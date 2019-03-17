package com.example.currencies.conversion.usecase

import com.example.currencies.common.CurrencyRateRepository
import com.example.currencies.common.entities.Amount
import com.example.currencies.common.entities.Currency
import com.example.currencies.common.entities.CurrencyRate
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import java.util.*
import kotlin.test.assertEquals

class ConvertAmountWithLatestRatesTest {

    @get:Rule
    val rule : MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var repository: CurrencyRateRepository

    lateinit var useCase: ConvertAmountWithLatestRates

    @Before
    fun setup() {
        useCase = ConvertAmountWithLatestRates(repository)
    }

    @Test
    fun `when executing the use case, the rates are retrieved from the repository and the amount is converted correctly`() {
        runBlocking {
            whenever(repository.getLatestCurrencyRates(Currency.EUR, listOf(Currency.AUD, Currency.JPY, Currency.CHF))).thenReturn(
                listOf(
                    CurrencyRate(Currency.EUR, Currency.AUD, 0.5f, Date()),
                    CurrencyRate(Currency.EUR, Currency.JPY, 2.0f, Date()),
                    CurrencyRate(Currency.EUR, Currency.CHF, 4.0f, Date())
                )
            )
            val result = useCase.execute(Amount(Currency.EUR, 10.4f), listOf(Currency.AUD, Currency.JPY, Currency.CHF))
            assertEquals(
                listOf(
                    Amount(Currency.AUD, 5.2f),
                    Amount(Currency.JPY, 20.8f),
                    Amount(Currency.CHF, 41.6f)
                ),
                result
            )
        }
    }

}