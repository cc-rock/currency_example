package com.example.currencies.conversion.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.currencies.R
import com.example.currencies.conversion.CurrencyConversionView
import javax.inject.Inject

class CurrencyConversionAdapter @Inject constructor(): RecyclerView.Adapter<CurrencyConversionAdapter.ViewHolder>() {

    private val items = emptyList<CurrencyConversionView.Row>().toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyConversionAdapter.ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.currency_conversion_list_item,
            parent,
            false)
    )

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: CurrencyConversionAdapter.ViewHolder, position: Int) {
        // do nothing, payload version is used
    }

    override fun onBindViewHolder(holder: CurrencyConversionAdapter.ViewHolder, position: Int, payloads: MutableList<Any>) {
        holder.bind(items[position])
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

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val label: TextView = itemView.findViewById(R.id.currency_label)
        private val amount: TextView = itemView.findViewById(R.id.amount)

        fun bind(rowData: CurrencyConversionView.Row) {
            label.text = rowData.currencyLabel
            amount.text = rowData.amount
        }

    }

}