package com.example.currencies.conversion.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.currencies.R
import com.example.currencies.conversion.CurrencyConversionPresenter
import com.example.currencies.conversion.CurrencyConversionView
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_currency_conversion.*
import javax.inject.Inject

class CurrencyConversionFragment : DaggerFragment(), CurrencyConversionView {

    @Inject
    lateinit var presenter: CurrencyConversionPresenter

    @Inject
    lateinit var adapter: CurrencyConversionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_currency_conversion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        currencies_table.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        currencies_table.adapter = adapter
        savedInstanceState?.let {
            adapter.restoreState(it)
        }
        presenter.viewCreated(this, savedInstanceState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        // set listeners after the view has been restored, to avoid triggering unwanted events
        amount_field.addTextChangedListener(
            object: TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    presenter.onAmountTextChanged(s?.toString() ?: "")
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

            }
        )
        compare_button.setOnClickListener {
            amount_field.clearFocus()
            presenter.onCompareClicked()
        }
        adapter.onCurrencyClicked = presenter::onRowClicked
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        presenter.onSaveState(outState)
        adapter.saveState(outState)
    }

    override fun onDestroyView() {
        presenter.viewDestroyed()
        super.onDestroyView()
    }

    override fun showInputHintText(hint: String) {
        amount_layout.hint = hint
    }

    override fun showRows(rows: List<CurrencyConversionView.Row>) {
        adapter.updateItems(rows)
    }

    override fun showError() {
        Toast.makeText(context, R.string.error_message, LENGTH_LONG).show()
    }

    override fun showSelection(selection: CurrencyConversionView.Selection) {
        adapter.updateSelection(selection)
    }

    override fun enableCompareButton(enabled: Boolean) {
        compare_button.isEnabled = enabled
    }


}
