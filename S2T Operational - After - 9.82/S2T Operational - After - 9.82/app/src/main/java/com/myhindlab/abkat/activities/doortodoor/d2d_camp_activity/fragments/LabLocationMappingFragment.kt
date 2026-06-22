package com.myhindlab.abkat.activities.doortodoor.d2d_camp_activity.fragments

import android.Manifest
import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*
import com.myhindlab.abkat.databinding.FragmentLabLocationMappingBinding
import com.myhindlab.abkat.databinding.FragmentReadinessBinding
import com.myhindlab.abkat.models.UserAttendanceModel
import com.myhindlab.abkat.models.UserAttendanceModel.Output
import com.myhindlab.abkat.models.doortodoor.D2DCampDetails
import com.myhindlab.abkat.models.doortodoor.d2d_camp_activity.GetLocationDetailsByLabCodeResponseModel
import com.myhindlab.abkat.models.doortodoor.d2d_camp_activity.TeamDetailsByCampIDResponseModel
import com.myhindlab.abkat.rest.ApiClient
import com.myhindlab.abkat.rest.ApiInterface
import com.myhindlab.abkat.utilities.UserSessionManager
import com.myhindlab.abkat.utilities.Utilities
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LabLocationMappingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LabLocationMappingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var userAttendanceModel: UserAttendanceModel? = null
    private var campDetails: D2DCampDetails.Output? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var locationCallback: LocationCallback
    lateinit var currentLocationRequest: CurrentLocationRequest
    lateinit var crrLocation: Location

    lateinit var sessionManager: UserSessionManager
    lateinit var apiInterface: ApiInterface
    lateinit var progressDialog: ProgressDialog
    val TAG: String = LabLocationMappingFragment::class.java.simpleName


    lateinit var getLocationDetailsByLabCodeResponseModel: GetLocationDetailsByLabCodeResponseModel

    lateinit var binding: FragmentLabLocationMappingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                campDetails = it.getSerializable(ARG_PARAM2, D2DCampDetails.Output::class.java)
                userAttendanceModel =
                    it.getSerializable(ARG_PARAM1, UserAttendanceModel::class.java)
            } else {
                campDetails = it.getSerializable(ARG_PARAM2) as D2DCampDetails.Output
                userAttendanceModel = it.getSerializable(ARG_PARAM1) as UserAttendanceModel

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLabLocationMappingBinding.inflate(layoutInflater, container, false)
        init()
        eventHandler()
        return binding.root
    }

    fun init() {
        apiInterface = ApiClient.getD2DClient().create(ApiInterface::class.java)
        progressDialog = ProgressDialog(context)
        sessionManager = UserSessionManager(context)
        if (userAttendanceModel != null) {
            if (userAttendanceModel!!.output[0].readinessFlag != 0) {
//                binding.btnReadyForCamp.isEnabled = false
            }
        }
        getTeamDetailsBytCampId()
        getCurrentLocation()


    }

    fun eventHandler() {
        binding.rgLocation.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {

            override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
                if (binding.rbUsingPincode.isChecked) {
                    binding.llUsingGPS.visibility = View.GONE
                    binding.llUsingPincode.visibility = View.VISIBLE
                } else if (binding.rbUsingGPS.isChecked) {
                    binding.llUsingGPS.visibility = View.VISIBLE
                    binding.llUsingPincode.visibility = View.GONE
                }
            }

        })
    }

    fun getLabForLocation() {

        progressDialog.setMessage("Comparing labs")
        apiInterface.getLocationDetailsByLabCode(0, 0, 0).enqueue(object :
            Callback<GetLocationDetailsByLabCodeResponseModel> {
            override fun onResponse(
                call: Call<GetLocationDetailsByLabCodeResponseModel>,
                response: Response<GetLocationDetailsByLabCodeResponseModel>
            ) {
                progressDialog.dismiss()
                if (response.isSuccessful) {
                    getLocationDetailsByLabCodeResponseModel = response.body()!!

                    val nearestLab = getLocationDetailsByLabCodeResponseModel.output[0]
                    val labLocation = Location("Lab")
                    labLocation.latitude = nearestLab.latitude
                    labLocation.longitude = nearestLab.longitude
                    var distanceFromLab = crrLocation.distanceTo(labLocation)
                    for (i in getLocationDetailsByLabCodeResponseModel.output) {
                        labLocation.latitude = i.latitude
                        labLocation.longitude = i.longitude
                        distanceFromLab = crrLocation.distanceTo(labLocation)
                        i.distanceFrom = distanceFromLab / 1000
                    }
                    getLocationDetailsByLabCodeResponseModel.output.sortBy {
                        it.distanceFrom
                    }

                    binding.edtNearestLab.setText(getLocationDetailsByLabCodeResponseModel.output[0].labName + " (" + getLocationDetailsByLabCodeResponseModel.output[0].distanceFrom + "K.M.)")


                } else {
                    Utilities.showToastMessage(response.body()!!.message, requireContext(), false)

                }
            }

            override fun onFailure(
                call: Call<GetLocationDetailsByLabCodeResponseModel>,
                t: Throwable
            ) {
                progressDialog.dismiss()

                Utilities.showToastMessage("Unable to get labs", requireContext(), false)
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

                            binding.tvCampId.text = ":" + response.body()!!.output.get(0).campNo
                            binding.tvDistrict.text = ":" + response.body()!!.output.get(0).distname
                            binding.tvTeamMember.text =
                                ":" + response.body()!!.output.get(0).teamName
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
         * @return A new instance of fragment LabLocationMappingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: UserAttendanceModel, param2: D2DCampDetails.Output?) =
            LabLocationMappingFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                    putSerializable(ARG_PARAM2, param2)
                }
            }
    }

    fun getCurrentLocation() {
        progressDialog.setMessage("Getting location")
        progressDialog.setCancelable(false)
        progressDialog.show()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

//        currentLocationRequest =
//            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, (30 * 1000).toLong())
//        currentLocationRequest = currentLocationRequest.build()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result ?: return
                for (location in result.locations) {
                    crrLocation = location
                }
            }
        }


        var currentLocationResult =
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            } else {
                fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)


            }

        currentLocationResult.addOnCompleteListener {
            if (it.isSuccessful) {
                crrLocation = it.result
                binding.tvCrrAddress.setText(getAddress(crrLocation))
                getLabForLocation()

            } else {
                progressDialog.dismiss()

            }
        }

    }

    fun getAddress(location: Location): String {
        val geocoder = Geocoder(requireContext())
        val address = geocoder.getFromLocation(location.latitude, location.longitude, 1)!!.get(0)
            .getAddressLine(0)

        return address
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}