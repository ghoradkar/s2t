package com.myhindlab.abkat.utilities.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.myhindlab.abkat.R

class GenericAdapter<T>(
    private var itemList: List<T>,
    private val displayText: (T) -> String,
    private val onItemClick: (T) -> Unit
) : RecyclerView.Adapter<GenericAdapter<T>.ViewHolder>(), Filterable {

    private var filteredList: List<T> = itemList

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.txtItemName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.generic_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = filteredList[position]
        holder.textView.text = displayText(item)
        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    override fun getItemCount(): Int = filteredList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(query: CharSequence?): FilterResults {
                val searchText = query.toString().lowercase()
                filteredList = if (searchText.isEmpty()) {
                    itemList
                } else {
                    itemList.filter { displayText(it).lowercase().contains(searchText) }
                }
                return FilterResults().apply { values = filteredList }
            }

            override fun publishResults(query: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as List<T>
                notifyDataSetChanged()
            }
        }
    }
}
