package com.myhindlab.abkat.adapters.doortodoor.d2d_camp_activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.myhindlab.abkat.R
import com.myhindlab.abkat.models.doortodoor.CheckListResponseModel

class CampCheckListAdapter(
    val list: List<CheckListResponseModel.Output>,
    val checkListEvent: CheckListEvent
) :
    Adapter<CampCheckListAdapter.CampCheckListViewHolder>() {

    interface CheckListEvent {
        fun onCheckChange(checkList: CheckListResponseModel.Output)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CampCheckListViewHolder {
        val context = parent.context
        val v = LayoutInflater.from(context).inflate(R.layout.readiness_list_item, parent, false)
        return CampCheckListViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CampCheckListViewHolder, position: Int) {
        val output = list.get(holder.absoluteAdapterPosition)
        holder.srNoTv.setText((holder.absoluteAdapterPosition + 1).toString())
        holder.listItemTv.setText(output.checkListDescription)
        holder.cbCheck.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {


            override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
                output.isChecked = isChecked
                checkListEvent.onCheckChange(output)
            }
        })
    }

    class CampCheckListViewHolder(itemView: View) : ViewHolder(itemView) {

        val listItemTv: TextView = itemView.findViewById(R.id.textView5)
        val cbCheck: CheckBox = itemView.findViewById(R.id.cbCheck)
        val srNoTv: TextView = itemView.findViewById(R.id.textView6)

    }

}