package com.myhindlab.abkat.utilities

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myhindlab.abkat.abha.adapters.GenericListAdapter
import kotlin.apply

class Utility {

//    companion object {
//        fun <T> showRecyclerListDialog(
//            context: Context,
//            title: String,
//            items: List<T>,
//            getLabel: (T) -> String,
//            onItemSelected: (T) -> Unit
//        ) {
//            val recyclerView = RecyclerView(context).apply {
//                layoutManager = LinearLayoutManager(context)
//                adapter = GenericListAdapter(items, getLabel, onItemSelected)
//            }
//
//            AlertDialog.Builder(context)
//                .setTitle(title)
//                .setView(recyclerView)
//                .setNegativeButton("Cancel", null)
//                .show()
//        }
//    }

    companion object {
        fun <T> showRecyclerListDialog(
            context: Context,
            title: String,
            items: List<T>,
            getLabel: (T) -> String,
            onItemSelected: (T) -> Unit
        ) {
            val recyclerView = RecyclerView(context).apply {
                layoutManager = LinearLayoutManager(context)
            }

            val dialog = AlertDialog.Builder(context)
                .setTitle(title)
                .setView(recyclerView)
                .setNegativeButton("Cancel", null)
                .create()

            recyclerView.adapter = GenericListAdapter(items, getLabel) { selectedItem ->
                onItemSelected(selectedItem)
                dialog.dismiss()  // 👈 Dismiss after item selected
            }

            dialog.show()
        }
    }


}