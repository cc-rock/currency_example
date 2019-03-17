package com.example.currencies.compare.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.currencies.R
import com.example.currencies.common.entities.Amount
import com.example.currencies.common.entities.Currency
import com.example.currencies.compare.CurrencyComparePresenter
import com.example.currencies.compare.CurrencyCompareView
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_currency_compare.*
import javax.inject.Inject

class CurrencyCompareFragment : DaggerFragment(), CurrencyCompareView {

    @Inject
    lateinit var presenter: CurrencyComparePresenter

    @Inject
    lateinit var adapter: CurrencyCompareAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_currency_compare, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        currencies_table.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        currencies_table.addItemDecoration(
            DividerItemDecoration(context, RecyclerView.VERTICAL)
        )
        currencies_table.adapter = adapter
        presenter.viewCreated(this)
        arguments?.let {
            presenter.loadData(
                it.getParcelable(ARG_AMOUNT) ?: return,
                it.getSerializable(ARG_FIRST_CURRENCY) as? Currency ?: return,
                it.getSerializable(ARG_SECOND_CURRENCY) as? Currency ?: return
            )
        }
    }

    override fun showAmount(amountAndCurrency: String) {
        chosen_amount.text = amountAndCurrency
    }

    override fun showTable(header: CurrencyCompareView.HeaderRow, rows: List<CurrencyCompareView.Row>) {
        adapter.updateItems(header, rows)
    }

    override fun showLoading() {
        loading_view.visibility = View.VISIBLE
        currencies_table.visibility = View.GONE
    }

    override fun hideLoading() {
        currencies_table.visibility = View.VISIBLE
        loading_view.visibility = View.GONE
    }

    override fun showError() {
        Toast.makeText(context, R.string.error_message, Toast.LENGTH_LONG).show()
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
