package com.example.currencies.compare.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.example.currencies.R
import com.example.currencies.common.entities.Amount
import com.example.currencies.common.entities.Currency
import com.example.currencies.compare.CurrencyComparePresenter
import com.example.currencies.compare.CurrencyCompareView
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class CurrencyCompareFragment : DaggerFragment(), CurrencyCompareView {

    @Inject
    lateinit var presenter: CurrencyComparePresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_currency_compare, container, false)
    }

    override fun displayAmount(amountAndCurrency: String) {

    }

    override fun displayTable(header: CurrencyCompareView.HeaderRow, rows: List<CurrencyCompareView.Row>) {

    }

    companion object {

        private const val ARG_AMOUNT = "arg_amount"
        private const val ARG_FIRST_CURRENCY = "arg_first_currency"
        private const val ARG_SECOND_CURRENCY = "arg_second_currency"

        fun createArgsBundle(amount: Amount,
                             firstCurrency: Currency,
                             secondCurrency: Currency): Bundle {
            return bundleOf(
                ARG_AMOUNT to amount,
                ARG_FIRST_CURRENCY to firstCurrency,
                ARG_SECOND_CURRENCY to secondCurrency
            )
        }

    }

}
