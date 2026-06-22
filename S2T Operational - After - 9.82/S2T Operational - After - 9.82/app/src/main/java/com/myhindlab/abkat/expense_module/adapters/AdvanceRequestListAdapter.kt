package com.myhindlab.abkat.expense_module.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.myhindlab.abkat.R
import com.myhindlab.abkat.expense_module.activities.SeeRequestedAdvanceActivity
import com.myhindlab.abkat.expense_module.activities.SubmitBillDetailsActivity
import com.myhindlab.abkat.expense_module.models.advance_detail_response.Output
import com.myhindlab.abkat.utilities.UserSessionManager
import com.myhindlab.abkat.utilities.Utilities

class AdvanceRequestListAdapter(
    val advanceRequestList: ArrayList<Output>,
    val fromDate: String,
    val toDate: String
) :
    RecyclerView.Adapter<AdvanceRequestListAdapter.AdvanceRequestViewHolder>() {

    var context: Context? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvanceRequestViewHolder {

        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.advance_request_list_item, parent, false)
        context = parent.context

        return AdvanceRequestViewHolder(v)
    }

    override fun getItemCount(): Int {
        return advanceRequestList.size
    }

    override fun onBindViewHolder(holder: AdvanceRequestViewHolder, position: Int) {
        val item = advanceRequestList[holder.absoluteAdapterPosition]
        holder.tvSrNo.text = "${(holder.absoluteAdapterPosition + 1)}"
        holder.tvCampId.text = "${item.campid}"
        holder.tvDistrict.text = "${item.Distname}"
        holder.tvCampDate.text = "${item.campdate}"
        holder.tvExpectedBeneficiary.text =
            "${item.Registeredbeneficiarycount}/${item.Expectedbeneficiarycount}"
        holder.tvAdvanceFundReqStatus.text = "${item.AdvanceFundStatus}"
        holder.tvAdvanceFundReceived.text = "${item.ExpenseAmount}"
        holder.tvApprovedDate.text = "${item.ApprovedDate}"
        holder.tvActualExpenseApproveStatus.text = "${item.ActualExpenseStatus}"
        holder.tvActualExpenseOnBillSubmit.text = "${item.ActualBillSubmit}"
        holder.tvFinalSettlement.text = "${item.FinalSettelment}"
        holder.tvActualExpenseReceived.text = "${item.ActualAmountApporved}"
        holder.tvExpenseSaved.text = "${item.ExpenseSaved}"
        holder.tvBillUploaded.text = "${item.IsBillUploaded}"

//        if (item.ActualBillSubmit != null && item.ActualBillSubmit!! > 0) {
//            holder.btnSeeBillDetails.visibility = View.VISIBLE
//            holder.btnSeeBillDetails.setOnClickListener(View.OnClickListener {
//                context!!.startActivity(
//                    Intent(context, SeeRequestedAdvanceActivity::class.java).putExtra(
//                        "campId",
//                        item.campid.toString()
//                    ).putExtra("screenType", 2)
//                )
//            })
//        } else {
//            holder.btnSeeBillDetails.setVisibility(View.GONE)
//        }
        if (item.ExpenseSaved.equals("Yes")) {
            holder.btnSeeBillDetails.visibility = View.VISIBLE
            holder.btnSeeBillDetails.setOnClickListener(View.OnClickListener {
                context!!.startActivity(
                    Intent(context, SeeRequestedAdvanceActivity::class.java).putExtra(
                        "campId",
                        item.campid.toString()
                    ).putExtra("screenType", 2)
                )
            })
        } else {
            holder.btnSeeBillDetails.setVisibility(View.GONE)
        }

        holder.cvMain.setOnClickListener {

            if (UserSessionManager(context).userDetailsJson.desgid != 102) {
                if (item.AdvRaisedbyUserid != null && (item.AdvRaisedbyUserid != UserSessionManager(
                        context
                    ).userDetailsJson.empCode)
                ) {
                    Utilities.showAlertDialog(
                        context,
                        "Warning",
                        "This advance not requested by you.",
                        false
                    )
                    return@setOnClickListener
                }
            }

            if (item.ActualExpenseStatus != null && item.ActualExpenseStatus.equals(
                    "Approved",
                    ignoreCase = true
                )
            ) {
                Utilities.showAlertDialog(
                    context,
                    "Warning",
                    "This bill already approved",
                    false
                )

            } else {
                context!!.startActivity(
                    Intent(
                        context,
                        SubmitBillDetailsActivity::class.java
                    ).putExtra(
                        "advanceDetails",
                        item
                    ).putExtra("fromDate", fromDate).putExtra("toDate", toDate)
                )

            }

        }

    }


    class AdvanceRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvSrNo: TextView = itemView.findViewById(R.id.tvSrNo)
        val tvCampId: TextView = itemView.findViewById(R.id.tvCampId)
        val tvCampDate: TextView = itemView.findViewById(R.id.tvCampDate)
        val tvDistrict: TextView = itemView.findViewById(R.id.tvDistrict)
        val tvExpectedBeneficiary: TextView = itemView.findViewById(R.id.tvExpectedBeneficiary)
        val tvAdvanceFundReqStatus: TextView = itemView.findViewById(R.id.tvAdvanceFundReqStatus)
        val tvActualExpenseOnBillSubmit: TextView =
            itemView.findViewById(R.id.tvActualExpenseOnBillSubmit)
        val tvApprovedDate: TextView = itemView.findViewById(R.id.tvApprovedDate)
        val tvExpenseSaved: TextView = itemView.findViewById(R.id.tvExpenseSaved)
        val tvBillUploaded: TextView = itemView.findViewById(R.id.tvBillUploaded)
        val tvActualExpenseReceived: TextView = itemView.findViewById(R.id.tvActualExpenseReceived)
        val tvActualExpenseApproveStatus: TextView =
            itemView.findViewById(R.id.tvActualExpenseApproveStatus)
        val tvAdvanceFundReceived: TextView = itemView.findViewById(R.id.tvAdvanceFundReceived)
        val tvFinalSettlement: TextView = itemView.findViewById(R.id.tvFinalSettlement)
        val cvMain: MaterialCardView = itemView.findViewById(R.id.cvMain)
        val btnSeeBillDetails: Button = itemView.findViewById(R.id.btnSeeBillDetails)


    }


}