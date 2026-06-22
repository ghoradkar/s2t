package com.myhindlab.abkat.activities.doortodoor

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.myhindlab.abkat.R
import com.myhindlab.abkat.databinding.ActivityReadinessBinding
import com.myhindlab.abkat.models.doortodoor.CheckListResponseModel
import com.myhindlab.abkat.rest.ApiClient
import com.myhindlab.abkat.rest.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReadinessActivity : AppCompatActivity() {

    lateinit var apiInterface: ApiInterface
    lateinit var progressDialog: ProgressDialog
    lateinit var binding: ActivityReadinessBinding
    val TAG: String = ReadinessActivity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadinessBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun init() {
        apiInterface = ApiClient.getD2DClient().create(ApiInterface::class.java)
        progressDialog = ProgressDialog(this@ReadinessActivity)

    }

    fun getCheckList() {

        apiInterface.bindD2DCheckListData().enqueue(object : Callback<CheckListResponseModel> {
            override fun onResponse(
                call: Call<CheckListResponseModel>,
                response: Response<CheckListResponseModel>
            ) {

                if(response.isSuccessful){

                }
            }

            override fun onFailure(call: Call<CheckListResponseModel>, t: Throwable) {
                Log.e(TAG, "onFailure: ")
            }

        })
    }
}