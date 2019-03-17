package com.example.currencies.compare.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.currencies.R
import com.example.currencies.compare.CurrencyCompareView
import javax.inject.Inject

class CurrencyCompareAdapter @Inject constructor(): RecyclerView.Adapter<CurrencyCompareAdapter.ViewHolder>() {

    private val items = emptyList<CurrencyCompareView.Row>().toMutableList()
    private var header: CurrencyCompareView.HeaderRow? = null

    private val hasHeader
        get() = (header != null)

    private val headerOffset
        get() = if (hasHeader) 1 else 0

    enum class ViewType {HEADER_VIEW_TYPE, ITEM_VIEW_TYPE}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutRes = when (viewType) {
            ViewType.ITEM_VIEW_TYPE.ordinal -> R.layout.currency_compare_list_item
            ViewType.HEADER_VIEW_TYPE.ordinal -> R.layout.currency_compare_header_item
            else -> throw IllegalArgumentException("Invalid view type")
        }
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                layoutRes,
                parent,
                false
            )
        )
    }

    override fun getItemViewType(position: Int) =
        if (hasHeader && position == 0) {
            ViewType.HEADER_VIEW_TYPE.ordinal
        } else {
            ViewType.ITEM_VIEW_TYPE.ordinal
        }

    override fun getItemCount() = items.size + headerOffset

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when {
            hasHeader && position == 0 -> header?.let { holder.bindHeader(it) }
            else -> holder.bindItem(items[position - headerOffset])
        }
    }
    
    fun updateItems(newHeader: CurrencyCompareView.HeaderRow, newItems: List<CurrencyCompareView.Row>) {
        header = newHeader
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val date: TextView = itemView.findViewById(R.id.date)
        private val firstAmount: TextView = itemView.findViewById(R.id.first_amount)
        private val secondAmount: TextView = itemView.findViewById(R.id.second_amount)

        fun bindItem(rowData: CurrencyCompareView.Row) {
            date.text = rowData.date
            firstAmount.text = rowData.firstAmount
            secondAmount.text = rowData.secondAmount
        }

        fun bindHeader(headerData: CurrencyCompareView.HeaderRow) {
            date.text = headerData.dateHeader
            firstAmount.text = headerData.firstAmountHeader
            secondAmount.text = headerData.secondAmountHeader
        }

    }

}