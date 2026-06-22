package com.myhindlab.abkat.abha.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.myhindlab.abkat.R
import com.myhindlab.abkat.abha.activities.ViewQueuePatientActivity
import com.myhindlab.abkat.abha.models.patient_queue.Output
import com.myhindlab.abkat.utilities.Utilities
import org.json.JSONArray
import org.json.JSONObject

class PatientQueueAdapter(private val patientQueueList: List<Output>) :
    RecyclerView.Adapter<PatientQueueAdapter.PatientQueueAdapterViewHolder>() {

    lateinit var context: Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PatientQueueAdapterViewHolder {
        context = parent.context
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.patient_queue_list_item, parent, false)
        return PatientQueueAdapterViewHolder(v)
    }

    override fun getItemCount(): Int {
        return patientQueueList.size
    }

    override fun onBindViewHolder(holder: PatientQueueAdapterViewHolder, position: Int) {
        val pos: Int = holder.absoluteAdapterPosition
        val item = patientQueueList[pos]
        holder.patName.text = item.name
        holder.tvTokenNo.text = item.identityID.toString()
        holder.tvView.setOnClickListener {
            try {


                val responseJson = JSONObject(item.response);
                val profileJSON = responseJson.getJSONObject("profile")
                val token: String = item.authtoken.toString()
                val name = item.name
                val abhaNumber = item.healthIdNumber
                val abhaAddress = item.healthId
                val gender = item.gender
                val dob = "${item.yearOfBirth}/${item.monthOfBirth}/${item.dayOfBirth}"
                val ageInYears = Utilities.getAge(
                    item.yearOfBirth!!.toInt(),
                    item.monthOfBirth!!.toInt(),
                    item.dayOfBirth!!.toInt(),
                )

//            val identifires: JSONArray = JSONArray(item.identifiers)
//            var mobileNum = ""
//            for (i in 0 until identifires.length()) {
//                val identifier = identifires.getJSONObject(i)
////                println("${book.get("book_name")} by ${book.get("author")}")
//                if (identifier.getString("type").equals("MOBILE")) {
//                    mobileNum = identifier.getString("value")
//                }
//            }
//            val mobile = mobileNum
                val patientJSON = profileJSON.getJSONObject("patient")
                val addressJSON = patientJSON.getJSONObject("address")
                val addressLine = addressJSON.getString("line")

                context.startActivity(
                    Intent(
                        context,
                        ViewQueuePatientActivity::class.java
                    )
                        .putExtra("name", name)
                        .putExtra("abhaNumber", abhaNumber)
                        .putExtra("abhaAddress", abhaAddress)
                        .putExtra("gender", gender)
                        .putExtra("dob", dob)
                        .putExtra("ageInYears", ageInYears.toString())
                        .putExtra("mobileNum", item.MobileNo.toString())
                        .putExtra("addressLine", addressLine)
                        .putExtra("token", token)
                        .putExtra("identityID", item.identityID)
                        .putExtra("response", item.response)
                )

            } catch (e: Exception) {
                e.printStackTrace()
            }
            (context as Activity).finish()

        }
    }

    class PatientQueueAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val patName = itemView.findViewById<TextView>(R.id.tvPatientName)
        val tvView = itemView.findViewById<TextView>(R.id.tvViewQueuePatient)
        val tvTokenNo = itemView.findViewById<TextView>(R.id.tvTokenNo)

    }

}