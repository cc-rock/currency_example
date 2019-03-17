package com.example.currencies.conversion.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.currencies.R
import com.example.currencies.common.entities.Currency
import com.example.currencies.conversion.CurrencyConversionView
import java.util.ArrayList
import javax.inject.Inject

class CurrencyConversionAdapter @Inject constructor(): RecyclerView.Adapter<CurrencyConversionAdapter.ViewHolder>() {

    companion object {
        private const val KEY_ITEMS = "CurrencyConversionAdapter.KEY_CURRENT_ITEMS"
        private const val KEY_SELECTION = "CurrencyConversionAdapter.KEY_CURRENT_SELECTION"
    }

    private val items = emptyList<CurrencyConversionView.Row>().toMutableList()
    private var selectedCurrencies = CurrencyConversionView.Selection(null, null)

    var onCurrencyClicked: ((Currency) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyConversionAdapter.ViewHolder {
        val newViewHolder = ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.currency_conversion_list_item,
                parent,
                false)
        )
        newViewHolder.onRowClicked = {
            onCurrencyClicked?.invoke(it)
        }
        return newViewHolder
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: CurrencyConversionAdapter.ViewHolder, position: Int) {
        // do nothing, payload version is used
    }

    override fun onBindViewHolder(holder: CurrencyConversionAdapter.ViewHolder, position: Int, payloads: MutableList<Any>) {
        holder.bind(items[position], isCurrencySelected(items[position].currency))
    }

    private fun isCurrencySelected(currency: Currency) =
            selectedCurrencies.firstSelected == currency || selectedCurrencies.secondSelected == currency

    fun updateSelection(selection: CurrencyConversionView.Selection) {
        selectedCurrencies = selection
        items.forEachIndexed { index, _ ->
            notifyItemChanged(index, Object())
        }
    }

    fun updateItems(newItems: List<CurrencyConversionView.Row>) {
        val oldSize = items.size
        items.clear()
        newItems.forEachIndexed { index, row ->
            items.add(row)
            if (index < oldSize) {
                notifyItemChanged(index, Object())
            } else {
                notifyItemInserted(index)
            }
        }
    }

    fun saveState(outState: Bundle) {
        outState.putParcelableArrayList(KEY_ITEMS, ArrayList(items))
        outState.putParcelable(KEY_SELECTION, selectedCurrencies)
    }

    fun restoreState(savedState: Bundle) {
        items.clear()
        items.addAll(savedState.getParcelableArrayList(KEY_ITEMS) ?: emptyList())
        selectedCurrencies = savedState.getParcelable(KEY_SELECTION) ?: CurrencyConversionView.Selection(null, null)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val label: TextView = itemView.findViewById(R.id.currency_label)
        private val amount: TextView = itemView.findViewById(R.id.amount)

        private var currency: Currency? = null
        var onRowClicked: ((Currency) -> Unit)? = null

        init {
            itemView.setOnClickListener {
                currency?.let { onRowClicked?.invoke(it) }
            }
        }

        fun bind(rowData: CurrencyConversionView.Row, selected: Boolean) {
            label.text = rowData.currencyLabel
            amount.text = rowData.amount
            currency = rowData.currency
            itemView.setBackgroundColor(if (selected) {
                ContextCompat.getColor(itemView.context, R.color.selected_row)
            } else {
                ContextCompat.getColor(itemView.context, R.color.white)
            })
        }

    }

}