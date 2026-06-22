package com.myhindlab.abkat.activities.doortodoor.d2d_camp_activity.fragments

import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.GsonBuilder
import com.myhindlab.abkat.adapters.doortodoor.d2d_camp_activity.CampCheckListAdapter
import com.myhindlab.abkat.databinding.FragmentReadinessBinding
import com.myhindlab.abkat.models.UserAttendanceModel
import com.myhindlab.abkat.models.doortodoor.CheckListResponseModel
import com.myhindlab.abkat.models.doortodoor.D2DCampDetails
import com.myhindlab.abkat.models.doortodoor.D2DCampDetails.Output
import com.myhindlab.abkat.models.doortodoor.d2d_camp_activity.TeamDetailsByCampIDResponseModel
import com.myhindlab.abkat.rest.ApiClient
import com.myhindlab.abkat.rest.ApiInterface
import com.myhindlab.abkat.utilities.UserSessionManager
import com.myhindlab.abkat.utilities.Utilities
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReadinessFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReadinessFragment : Fragment(), CampCheckListAdapter.CheckListEvent {
    // TODO: Rename and change types of parameters
    private var userAttendanceModel: UserAttendanceModel? = null
    private var campDetails: D2DCampDetails.Output? = null

    lateinit var binding: FragmentReadinessBinding
    lateinit var apiInterface: ApiInterface
    lateinit var progressDialog: ProgressDialog
    val TAG: String = ReadinessFragment::class.java.simpleName
    lateinit var checkListResponseModel: ArrayList<CheckListResponseModel.Output>

    lateinit var sessionManager: UserSessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                campDetails = it.getSerializable(ARG_PARAM2, D2DCampDetails.Output::class.java)
                userAttendanceModel =
                    it.getSerializable(ARG_PARAM1, UserAttendanceModel::class.java)

            } else {
                campDetails = it.getSerializable(ARG_PARAM2) as Output
                userAttendanceModel = it.getSerializable(ARG_PARAM1) as UserAttendanceModel


            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentReadinessBinding.inflate(layoutInflater, container, false)


        init()
        clickEvent()
        return binding.root
    }

    fun init() {
        apiInterface = ApiClient.getD2DClient().create(ApiInterface::class.java)
        progressDialog = ProgressDialog(context)
        sessionManager = UserSessionManager(context)

        getTeamDetailsBytCampId()
        getCheckList()

        if (userAttendanceModel != null) {
            if (userAttendanceModel!!.output[0].readinessFlag != 0) {
                binding.btnReadyForCamp.isEnabled = false
            }
        }

    }

    fun clickEvent() {
        binding.btnReadyForCamp.setOnClickListener {

            var count = 0

            for (i in checkListResponseModel) {

                if (i.isChecked) {
                    count += 1;
                }
            }

            if (count != checkListResponseModel.size) {
                Utilities.showAlertDialog(context, "Warning", "Please select all checklist", false)

                return@setOnClickListener
            }

            val gson = GsonBuilder().addSerializationExclusionStrategy(object : ExclusionStrategy {
                override fun shouldSkipField(f: FieldAttributes): Boolean {
                    return f.name.equals(
                        "isChecked",
                        ignoreCase = true
                    )
                }

                override fun shouldSkipClass(clazz: Class<*>?): Boolean {
                    return false
                }
            })
                .serializeNulls()
                .create()

            submitData(gson.toJson(checkListResponseModel))

        }
    }

    fun getCheckList() {

        apiInterface.bindD2DCheckListData().enqueue(object : Callback<CheckListResponseModel> {
            override fun onResponse(
                call: Call<CheckListResponseModel>,
                response: Response<CheckListResponseModel>
            ) {

                if (response.isSuccessful) {
                    if (response.body()!!.status.equals("success", true)) {
                        checkListResponseModel = ArrayList()
                        checkListResponseModel.addAll(response.body()!!.output)
                        binding.rvCheckList.layoutManager = LinearLayoutManager(context)
                        binding.rvCheckList.hasFixedSize()
                        binding.rvCheckList.adapter =
                            CampCheckListAdapter(
                                checkListResponseModel,
                                this@ReadinessFragment
                            )
                    } else {
                        Utilities.showToastMessage("Check list not found", context, false);
                    }

                }
            }

            override fun onFailure(call: Call<CheckListResponseModel>, t: Throwable) {
                Log.e(TAG, "onFailure: ")
                Utilities.showToastMessage("Check list not found", context, false);

            }

        })
    }

    fun submitData(json: String) {

        progressDialog.setMessage("Please wait...")
        progressDialog.setCancelable(false)
        progressDialog.show()
        apiInterface.insert_D2D_CampReadiness(
            json,
            sessionManager.userDetailsJson.empCode,
            campDetails!!.campId
        ).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {

                progressDialog.dismiss()
                if (response.isSuccessful) {
                    try {

                        val res = JSONObject(response.body()!!.string())
                        val status = res.getString("status")
                        val msg = res.getString("message")
                        if (status.equals("success", true)) {
                            Utilities.showAlertDialog(context, status, msg, true)
                        } else {
                            Utilities.showToastMessage("Check list not found", context, false);
                        }
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }

                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                progressDialog.dismiss()

                Log.e(TAG, "onFailure: ")
                Utilities.showToastMessage("Check list not found", context, false);

            }

        })
    }

    fun getTeamDetailsBytCampId() {

        apiInterface.getTeamDetailsByCampID(campDetails!!.campId)
            .enqueue(object : Callback<TeamDetailsByCampIDResponseModel> {
                override fun onResponse(
                    call: Call<TeamDetailsByCampIDResponseModel>,
                    response: Response<TeamDetailsByCampIDResponseModel>
                ) {

                    if (response.isSuccessful) {
                        if (response.body()!!.status.equals("success", true)) {
//                        binding.rvCheckList.adapter = CampCheckListAdapter(response.body()!!.output)

                            binding.tvCampId.text = ":${response.body()!!.output.get(0).campNo}"
                            binding.tvDistrict.text = ":${response.body()!!.output.get(0).distname}"
                            binding.tvTeamMember.text =
                                ":${response.body()!!.output.get(0).teamName}"
                        } else {
                            Utilities.showToastMessage("Check list not found", context, false);
                        }

                    }
                }

                override fun onFailure(call: Call<TeamDetailsByCampIDResponseModel>, t: Throwable) {
                    Log.e(TAG, "onFailure: ")
                    Utilities.showToastMessage("Check list not found", context, false);

                }

            })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ReadinessFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: UserAttendanceModel, param2: Output?) =
            ReadinessFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                    putSerializable(ARG_PARAM2, param2)
                }
            }
    }

    override fun onCheckChange(checkList: CheckListResponseModel.Output) {

    }


}