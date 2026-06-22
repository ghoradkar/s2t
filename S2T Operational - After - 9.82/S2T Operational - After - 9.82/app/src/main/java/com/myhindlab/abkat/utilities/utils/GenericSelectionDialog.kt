package com.myhindlab.abkat.utilities.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myhindlab.abkat.R

class GenericSelectionDialog<T>(
    private val title: String,
    private val itemList: List<T>,
    private val displayText: (T) -> String, // Function to display item
    private val onItemSelected: (T) -> Unit
) : DialogFragment() {

    private lateinit var adapter: GenericAdapter<T>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view= inflater.inflate(R.layout.generic_dialog_layout, container, false)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.generic_dialog_background)
        return  view;
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val searchView = view.findViewById<SearchView>(R.id.searchView)
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)
        val btnOkay = view.findViewById<Button>(R.id.btnOkay)

        if (title.equals("Select Remark")) {
            searchView.visibility = View.GONE
        }

        btnOkay.setOnClickListener { this.dismiss() }
        btnCancel.setOnClickListener { this.dismiss() }

        tvTitle.setText(title)

        adapter = GenericAdapter(itemList, displayText) { selectedItem ->
            onItemSelected(selectedItem)
            dismiss() // Close dialog after selection
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })
    }
}
