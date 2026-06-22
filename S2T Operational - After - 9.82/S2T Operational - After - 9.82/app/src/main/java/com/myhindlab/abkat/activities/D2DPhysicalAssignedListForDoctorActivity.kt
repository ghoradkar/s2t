package com.myhindlab.abkat.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.myhindlab.abkat.R
import com.myhindlab.abkat.databinding.ActivityD2DphysicalAssignedListForDoctorBinding
import com.myhindlab.abkat.models.doortodoor.AssignedPhysicalExamModel
import com.myhindlab.abkat.rest.ApiClient
import com.myhindlab.abkat.rest.ApiInterface
import com.myhindlab.abkat.utilities.UserSessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class D2DPhysicalAssignedListForDoctorActivity : AppCompatActivity() {

    private val TAG:String=D2DPhysicalAssignedListForDoctorActivity::class.java.simpleName
    lateinit var binding: ActivityD2DphysicalAssignedListForDoctorBinding
    lateinit var apiInterface: ApiInterface
    lateinit var userSessionManager: UserSessionManager
    var campId: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_d2_dphysical_assigned_list_for_doctor)
    }

    fun init() {
        userSessionManager = UserSessionManager(this@D2DPhysicalAssignedListForDoctorActivity)
        apiInterface = ApiClient.getD2DClient().create(ApiInterface::class.java)

        getAssignedWorkerList()
    }

    fun getAssignedWorkerList() {

        campId = intent.getStringExtra("campId")
        apiInterface.getAssignedBeneficiaryAndDoctorList(
            campId,
            userSessionManager.userDetailsJson.empCode
        ).enqueue(object : Callback<AssignedPhysicalExamModel>{
            override fun onResponse(
                call: Call<AssignedPhysicalExamModel>,
                response: Response<AssignedPhysicalExamModel>
            ) {
               if(response.isSuccessful){

               }
            }

            override fun onFailure(call: Call<AssignedPhysicalExamModel>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}" )
            }

        })
    }
}