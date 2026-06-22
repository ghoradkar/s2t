package com.myhindlab.abkat.abha.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.myhindlab.abkat.R

class GenericListAdapter<T>(
    private val items: List<T>,
    private val getLabel: (T) -> String,
    private val onClick: (T) -> Unit
) : RecyclerView.Adapter<GenericListAdapter<T>.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemText: TextView = view.findViewById(R.id.txtItemName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.generic_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.itemText.text = getLabel(item)
        holder.itemView.setOnClickListener { onClick(item) }
    }
}
