package com.myhindlab.abkat.abha.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.myhindlab.abkat.R

class PHRSuggestionsListAdapter(private val phrAddressList: ArrayList<String>) :
    RecyclerView.Adapter<PHRSuggestionsListAdapter.PHRSuggestionsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PHRSuggestionsViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.phr_suggestions_list_item, parent, false
        )

        return PHRSuggestionsViewHolder(v)
    }

    override fun getItemCount(): Int {
        return phrAddressList.size
    }

    override fun onBindViewHolder(holder: PHRSuggestionsViewHolder, position: Int) {
        val pos = holder.adapterPosition
        val phrAddresses = phrAddressList[pos]
        holder.tvPHRAddress.text = phrAddresses
    }

    inner class PHRSuggestionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvPHRAddress: TextView = itemView.findViewById(R.id.tvPHRAddress)
    }

}