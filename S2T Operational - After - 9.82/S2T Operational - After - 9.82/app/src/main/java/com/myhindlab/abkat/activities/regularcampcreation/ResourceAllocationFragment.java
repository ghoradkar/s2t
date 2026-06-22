package com.myhindlab.abkat.activities.regularcampcreation;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.regularcampcreation.adapter.TestListForResourceMappingAdapter;
import com.myhindlab.abkat.activities.regularcampcreation.model.ConsumableListModel;
import com.myhindlab.abkat.activities.regularcampcreation.model.GetLabOnDistrictListModel;
import com.myhindlab.abkat.activities.regularcampcreation.model.ResourceListForMappingModel;
import com.myhindlab.abkat.activities.regularcampcreation.model.TestListForResourceMappingModel;
import com.myhindlab.abkat.models.UserDetailsModel;
import com.myhindlab.abkat.rest.ApiClient;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.rest.MediProcAPI;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResourceAllocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResourceAllocationFragment extends Fragment implements TestListForResourceMappingAdapter.TestListEvent {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String CampId;
    private String SkipFlag;
    private TextView tvError;
    private Context context;
    private RecyclerView rvDesignation;
    private Button btnSubmit;
    private ProgressBar progressBar;
    static FragmentChange fragmentChange;
    private UserSessionManager userSessionManager;
    private Context mContext;
    private int distLgdCode;
    private final String TAG = DeviceAllocationFragment.class.getSimpleName();
    private TestListForResourceMappingModel testListForResourceMappingModel;
    private ResourceListForMappingModel resourceListModel;
    private ApiInterface apiService;
    private TestListForResourceMappingAdapter designationListAdapter;
    private List<ResourceListForMappingModel.Output> allSelResourceModel;
    private MediProcAPI mediProcAPI;
    private int labCode = 0;


    public ResourceAllocationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ResourceAllocationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ResourceAllocationFragment newInstance(String param1, String param2) {
        ResourceAllocationFragment fragment = new ResourceAllocationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_resource_allocation, container, false);

        initView(rootView);
        getSessionDetails();
        clickEvents();

        return rootView;
    }

    private void initView(View view) {
        mContext = requireContext();
        userSessionManager = new UserSessionManager(mContext);
        apiService = ApiClient.getD2DClient().create(ApiInterface.class);
        allSelResourceModel = new ArrayList<ResourceListForMappingModel.Output>();
        mediProcAPI = ApiClient.getMediProcClient().create(MediProcAPI.class);


        rvDesignation = view.findViewById(R.id.rvResourceList);
        tvError = view.findViewById(R.id.tvError);
        progressBar = view.findViewById(R.id.progress_circular);
        btnSubmit = view.findViewById(R.id.btnSubmit);

        rvDesignation.setHasFixedSize(true);
        rvDesignation.setLayoutManager(new LinearLayoutManager(mContext));

      //  getTestList();
    }

    private void getSessionDetails() {
        UserDetailsModel.Output userResponseModel = userSessionManager.getUserDetailsJson();
//        distLgdCode = CampCreationActivityV4.campCreationModel.getCampDetails().getDistLgdCode();
        CampId =CampCreationActivityV4.campCreationModel.getCampDetails().getCampId();
        SkipFlag = CampCreationActivityV4.campCreationModel.getCampDetails().getSkipFlag();



        getTestList();
    }

    void clickEvents() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isNetworkAvailable(mContext)) {
                    for (int i = 0; i < testListForResourceMappingModel.getOutput().size(); i++) {

                        TestListForResourceMappingAdapter.ResourceListViewHolder myViewHolder = (TestListForResourceMappingAdapter.ResourceListViewHolder) rvDesignation.findViewHolderForAdapterPosition(i);

                        if (myViewHolder != null) {
                            try {

                                if (myViewHolder.edtSelResource.getText().toString().isEmpty()) {
                                    myViewHolder.edtSelResource.setError("Select at least 1 resource");
                                    return;
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    if (allSelResourceModel.size() >= testListForResourceMappingModel.getOutput().size()) {


                        if (CampCreationActivityV4.campCreationModel.getISD2DCamp() == 0) {
                            insertChannelPartnerCampNew();
                        } else {

//                                    InserCampCreationD2DWithoutApprovalForPartner();

                        }
//                        Utilities.showAlertDialog(mContext, "Confirmation", "You have selected " + allSelResourceModel.size() + " Resources for this camp", true, "Okay", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//
//
//                            }
//                        });
                    } else {
                        Utilities.showToastMessage("Please select resources for camp", mContext, false);
                    }
                } else if (CampCreationActivityV4.campCreationModel.getISD2DCamp() == 0) {
                    for (int i = 0; i < testListForResourceMappingModel.getOutput().size(); i++) {

                        TestListForResourceMappingAdapter.ResourceListViewHolder myViewHolder = (TestListForResourceMappingAdapter.ResourceListViewHolder) rvDesignation.findViewHolderForAdapterPosition(i);

                        try {
                            assert myViewHolder != null;
                            if (myViewHolder.edtSelResource.getText().toString().isEmpty()) {
                                myViewHolder.edtSelResource.setError("Select at least 1 resource");
                                return;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (allSelResourceModel.size() >= testListForResourceMappingModel.getOutput().size()) {
                        Utilities.showAlertDialog(mContext, "Confirmation", "You have selected " + allSelResourceModel.size() + " Resources for this camp", true, "Create Camp", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if (CampCreationActivityV4.campCreationModel.getCampDetails().getSelectedPartnerId() != 0) {
                                    insertRegularCampForChannelPartner();
                                } else {
                                    insertRegularCamp();
                                }
                            }
                        });
                    } else {
                        Utilities.showToastMessage("Please select resources for camp", mContext, false);
                    }
                } else {

                    for (int i = 0; i < testListForResourceMappingModel.getOutput().size(); i++) {

                        TestListForResourceMappingAdapter.ResourceListViewHolder myViewHolder = (TestListForResourceMappingAdapter.ResourceListViewHolder) rvDesignation.findViewHolderForAdapterPosition(i);

                        try {
                            assert myViewHolder != null;
                            if (myViewHolder.edtSelResource.getText().toString().isEmpty()) {
                                myViewHolder.edtSelResource.setError("Select at least 1 resource");
                                return;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


//                    if (allSelResourceModel.size() >= testListForResourceMappingModel.getOutput().size()) {
//                        Utilities.showAlertDialog(mContext, "Confirmation", "You have selected " + allSelResourceModel.size() + " Resources for this camp", true, "Create Camp", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                                if (CampCreationActivityV4.campCreationModel.getCampDetails().getSelectedPartnerId() != 0) {
//                                    insertD2dCampForPartner();
//                                } else
////                                    insertD2dCamp();
//
//
//                            }
//                        });
//                    }
                }

            }
        });
    }

    void insertRegularCamp() {
        CampCreationActivityV4.campCreationModel.setResources(allSelResourceModel);


        Gson gson = new GsonBuilder().addSerializationExclusionStrategy(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        if (f.getName().equalsIgnoreCase("desgid") || f.getName().equalsIgnoreCase("AVAILABELSTOCK") || f.getName().equalsIgnoreCase("Expectedbeneficiarycount") || f.getName().equalsIgnoreCase("selectedPartnerId")) {
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
//                                    .excludeFieldsWithoutExposeAnnotation()
                .serializeNulls()
                .create();

        AlertDialog alertDialog = Utilities.ProgressDialog(mContext, "Creating Camp, Please wait").create();
        alertDialog.show();
        Log.i(TAG, "onClick Submit: " + gson.toJson(CampCreationActivityV4.campCreationModel));


        String SiteMappingJSON = gson.toJson(CampCreationActivityV4.campCreationModel.getSiteList());
        String CampDetailsJson = gson.toJson(CampCreationActivityV4.campCreationModel.getCampDetails());
        String DeviceMappingJson = gson.toJson(CampCreationActivityV4.campCreationModel.getSelectedDevice());
        String ConsumableMappingJson = gson.toJson(CampCreationActivityV4.campCreationModel.getConsumables());
        String ResourceMappingJson = gson.toJson(CampCreationActivityV4.campCreationModel.getResources());

        apiService.insertCampCreationWithoutApproval(String.valueOf(userSessionManager.getUserDetailsJson().getEmpCode()), SiteMappingJSON, CampDetailsJson, DeviceMappingJson, ConsumableMappingJson, ResourceMappingJson).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                alertDialog.dismiss();

                Log.d(TAG, "onResponse Error: " + response.errorBody());
                Log.d(TAG, "onResponse: " + response.body());
                Log.d(TAG, "onResponse: " + response.code());

                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("success")) {
                            String msg = jsonObject.getString("message");

                            apiService.getLabOnDistrict(distLgdCode).enqueue(new Callback<GetLabOnDistrictListModel>() {
                                @Override
                                public void onResponse(Call<GetLabOnDistrictListModel> call, Response<GetLabOnDistrictListModel> response) {
                                    Log.d(TAG, "onResponse: " + response.body().getStatus());

                                    try {
                                        if (response.isSuccessful()) {
                                            if (response.body().getOutput() != null && response.body().getOutput().size() > 0) {
                                                labCode = response.body().getOutput().get(0).getLabCode();

                                                JsonArray jsonArray = new JsonArray();
                                                JsonObject consumableAllocation = new JsonObject();
                                                consumableAllocation.add("CampAllocatedProduct", jsonArray);
                                                for (ConsumableListModel.Output con :
                                                        CampCreationActivityV4.campCreationModel.getConsumables()
                                                ) {
                                                    if (con.getAvailabelstock() != null) {
                                                        JsonObject jsonObject1 = new JsonObject();
                                                        jsonObject1.addProperty("CampID", msg);
                                                        jsonObject1.addProperty("LabCode", labCode);
                                                        jsonObject1.addProperty("ProductID", con.getProductid());
                                                        jsonObject1.addProperty("AllotQauntity", con.getExpectedQuantity());
                                                        jsonObject1.addProperty("CampStatus", 1);
                                                        jsonObject1.addProperty("CampDate", CampCreationActivityV4.campCreationModel.getCampDetails().getCampDate());
                                                        jsonArray.add(jsonObject1);
                                                    }

                                                }

                                                Log.d(TAG, "consumableAllocation: " + consumableAllocation);

                                                mediProcAPI.allocatedProductListForCamp(consumableAllocation.toString(), String.valueOf(userSessionManager.getUserDetailsJson().getEmpCode()), "11").enqueue(new Callback<ResponseBody>() {
                                                    @Override
                                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                        if (response.isSuccessful()) {
                                                            try {
                                                                JSONObject jsonObject1 = new JSONObject(response.body().string());
                                                                String status = jsonObject1.getString("status");
                                                                if (status.equalsIgnoreCase("success")) {
                                                                    Utilities.showAlertDialog(mContext, status, "Camp Created Successfully..", true, "Okay", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                                            CampCreationActivityV4.campCreationModel = null;
                                                                            ((CampCreationActivityV4) mContext).finish();
                                                                        }
                                                                    });

                                                                } else {
                                                                    Utilities.showAlertDialog(mContext, "Success", "Camp Created Successfully.. but Stock not updated in inventory", true, "Okay", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                                            CampCreationActivityV4.campCreationModel = null;
                                                                            ((CampCreationActivityV4) mContext).finish();
                                                                        }
                                                                    });

                                                                }
                                                            } catch (
                                                                    JSONException | IOException e) {
                                                                e.printStackTrace();
                                                            }

                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                                        Log.e(TAG, "allocatedProductListForCamp fail: " + t.getMessage());
                                                    }
                                                });

                                            } else {
//                                                Utilities.showAlertDialog(mContext, "Lab not found", "Lab not mapped for this district", false, "Okay", new DialogInterface.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                                        ((CampApprovalActivity) mContext).finish();
//                                                        dialogInterface.dismiss();
//                                                    }
//                                                });
                                            }
                                        } else {
                                            Utilities.showToastMessage("Lab Not Found", mContext, false);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }

                                @Override
                                public void onFailure(Call<GetLabOnDistrictListModel> call, Throwable t) {
                                    Log.e(TAG, "onFailure: " + t.getMessage());
                                    Utilities.showToastMessage("Lab Not Found " + t.getMessage(), mContext, false);

                                }
                            });


                        } else {
                            String msg = jsonObject.getString("ExceptionValue");
                            Utilities.showAlertDialog(mContext, status, msg, false);

                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                alertDialog.dismiss();


            }
        });

    }

    void insertRegularCampForChannelPartner() {
        CampCreationActivityV4.campCreationModel.setResources(allSelResourceModel);


        Gson gson = new GsonBuilder().addSerializationExclusionStrategy(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        if (f.getName().equalsIgnoreCase("desgid") || f.getName().equalsIgnoreCase("AVAILABELSTOCK") || f.getName().equalsIgnoreCase("Expectedbeneficiarycount") || f.getName().equalsIgnoreCase("selectedPartnerId")) {
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
//                                    .excludeFieldsWithoutExposeAnnotation()
                .serializeNulls()
                .create();

        AlertDialog alertDialog = Utilities.ProgressDialog(mContext, "Creating Camp, Please wait").create();
        alertDialog.show();
        Log.i(TAG, "onClick Submit: " + gson.toJson(CampCreationActivityV4.campCreationModel));


        String SiteMappingJSON = gson.toJson(CampCreationActivityV4.campCreationModel.getSiteList());
        String CampDetailsJson = gson.toJson(CampCreationActivityV4.campCreationModel.getCampDetails());
        String DeviceMappingJson = gson.toJson(CampCreationActivityV4.campCreationModel.getSelectedDevice());
        String ConsumableMappingJson = gson.toJson(CampCreationActivityV4.campCreationModel.getConsumables());
        String ResourceMappingJson = gson.toJson(CampCreationActivityV4.campCreationModel.getResources());

        apiService.inserCampCreationWithoutApprovalNew(String.valueOf(userSessionManager.getUserDetailsJson().getEmpCode()), SiteMappingJSON, CampDetailsJson, DeviceMappingJson, ConsumableMappingJson, ResourceMappingJson, CampCreationActivityV4.campCreationModel.getCampDetails().getSelectedPartnerId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                alertDialog.dismiss();

                Log.d(TAG, "onResponse Error: " + response.errorBody());
                Log.d(TAG, "onResponse: " + response.body());
                Log.d(TAG, "onResponse: " + response.code());

                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("success")) {
                            String msg = jsonObject.getString("message");

                            apiService.getLabOnDistrict(distLgdCode).enqueue(new Callback<GetLabOnDistrictListModel>() {
                                @Override
                                public void onResponse(Call<GetLabOnDistrictListModel> call, Response<GetLabOnDistrictListModel> response) {
                                    Log.d(TAG, "onResponse: " + response.body().getStatus());

                                    try {
                                        if (response.isSuccessful()) {
                                            if (response.body().getOutput() != null && response.body().getOutput().size() > 0) {
                                                labCode = response.body().getOutput().get(0).getLabCode();

                                                JsonArray jsonArray = new JsonArray();
                                                JsonObject consumableAllocation = new JsonObject();
                                                consumableAllocation.add("CampAllocatedProduct", jsonArray);
                                                for (ConsumableListModel.Output con :
                                                        CampCreationActivityV4.campCreationModel.getConsumables()
                                                ) {
                                                    if (con.getAvailabelstock() != null) {
                                                        JsonObject jsonObject1 = new JsonObject();
                                                        jsonObject1.addProperty("CampID", msg);
                                                        jsonObject1.addProperty("LabCode", labCode);
                                                        jsonObject1.addProperty("ProductID", con.getProductid());
                                                        jsonObject1.addProperty("AllotQauntity", con.getExpectedQuantity());
                                                        jsonObject1.addProperty("CampStatus", 1);
                                                        jsonObject1.addProperty("CampDate", CampCreationActivityV4.campCreationModel.getCampDetails().getCampDate());
                                                        jsonArray.add(jsonObject1);
                                                    }

                                                }

                                                Log.d(TAG, "consumableAllocation: " + consumableAllocation);

                                                mediProcAPI.allocatedProductListForCamp(consumableAllocation.toString(), String.valueOf(userSessionManager.getUserDetailsJson().getEmpCode()), "11").enqueue(new Callback<ResponseBody>() {
                                                    @Override
                                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                        if (response.isSuccessful()) {
                                                            try {
                                                                JSONObject jsonObject1 = new JSONObject(response.body().string());
                                                                String status = jsonObject1.getString("status");
                                                                if (status.equalsIgnoreCase("success")) {
                                                                    Utilities.showAlertDialog(mContext, status, "Camp Created Successfully..", true, "Okay", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                                            CampCreationActivityV4.campCreationModel = null;
                                                                            ((CampCreationActivityV4) mContext).finish();
                                                                        }
                                                                    });

                                                                } else {
                                                                    Utilities.showAlertDialog(mContext, "Success", "Camp Created Successfully.. but Stock not updated in inventory", true, "Okay", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                                            CampCreationActivityV4.campCreationModel = null;
                                                                            ((CampCreationActivityV4) mContext).finish();
                                                                        }
                                                                    });

                                                                }
                                                            } catch (
                                                                    JSONException | IOException e) {
                                                                e.printStackTrace();
                                                            }

                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                                        Log.e(TAG, "allocatedProductListForCamp fail: " + t.getMessage());
                                                    }
                                                });

                                            } else {
                                                Utilities.showAlertDialog(mContext, "Lab not found", "Lab not mapped for this district", false, "Okay", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                                        ((CampApprovalActivity) mContext).finish();
                                                        dialogInterface.dismiss();
                                                    }
                                                });
                                            }
                                        } else {
                                            Utilities.showToastMessage("Lab Not Found", mContext, false);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }

                                @Override
                                public void onFailure(Call<GetLabOnDistrictListModel> call, Throwable t) {
                                    Log.e(TAG, "onFailure: " + t.getMessage());
                                    Utilities.showToastMessage("Lab Not Found " + t.getMessage(), mContext, false);

                                }
                            });


                        } else {
                            String msg = jsonObject.getString("ExceptionValue");
                            Utilities.showAlertDialog(mContext, status, msg, false);

                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                alertDialog.dismiss();


            }
        });

    }

    void insertChannelPartnerCamp() {
        ResourceListForMappingModel resourceListForMappingModel = new ResourceListForMappingModel();
        allSelResourceModel.add(resourceListForMappingModel.new Output(0, "", 34, 3, true));
        CampCreationActivityV4.campCreationModel.setResources(allSelResourceModel);


        Gson gson = new GsonBuilder().addSerializationExclusionStrategy(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        if (f.getName().equalsIgnoreCase("desgid") || f.getName().equalsIgnoreCase("AVAILABELSTOCK") || f.getName().equalsIgnoreCase("Expectedbeneficiarycount") || f.getName().equalsIgnoreCase("selectedPartnerId")) {
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
//                                    .excludeFieldsWithoutExposeAnnotation()
                .serializeNulls()
                .create();

        AlertDialog alertDialog = Utilities.ProgressDialog(mContext, "Creating Camp, Please wait").create();
        alertDialog.show();
        Log.i(TAG, "onClick Submit: " + gson.toJson(CampCreationActivityV4.campCreationModel));


        String SiteMappingJSON = gson.toJson(CampCreationActivityV4.campCreationModel.getSiteList());
        String CampDetailsJson = gson.toJson(CampCreationActivityV4.campCreationModel.getCampDetails());
        String DeviceMappingJson = gson.toJson(CampCreationActivityV4.campCreationModel.getSelectedDevice());
        String ConsumableMappingJson = gson.toJson(CampCreationActivityV4.campCreationModel.getConsumables());
        String ResourceMappingJson = gson.toJson(CampCreationActivityV4.campCreationModel.getResources());


        apiService.InserCampCreationWithoutApprovalForPartner(String.valueOf(userSessionManager.getUserDetailsJson().getEmpCode()), SiteMappingJSON, CampDetailsJson, DeviceMappingJson, ConsumableMappingJson, ResourceMappingJson).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                alertDialog.dismiss();

                Log.d(TAG, "onResponse Error: " + response.errorBody());
                Log.d(TAG, "onResponse: " + response.body());
                Log.d(TAG, "onResponse: " + response.code());

                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("success")) {
                            String msg = jsonObject.getString("message");

                            apiService.getLabOnDistrict(distLgdCode).enqueue(new Callback<GetLabOnDistrictListModel>() {
                                @Override
                                public void onResponse(Call<GetLabOnDistrictListModel> call, Response<GetLabOnDistrictListModel> response) {
                                    Log.d(TAG, "onResponse: " + response.body().getStatus());

                                    try {
                                        if (response.isSuccessful()) {
                                            if (response.body().getOutput() != null && response.body().getOutput().size() > 0) {
                                                labCode = response.body().getOutput().get(0).getLabCode();

                                                JsonArray jsonArray = new JsonArray();
                                                JsonObject consumableAllocation = new JsonObject();
                                                consumableAllocation.add("CampAllocatedProduct", jsonArray);
                                                for (ConsumableListModel.Output con :
                                                        CampCreationActivityV4.campCreationModel.getConsumables()
                                                ) {
                                                    if (con.getAvailabelstock() != null) {
                                                        JsonObject jsonObject1 = new JsonObject();
                                                        jsonObject1.addProperty("CampID", msg);
                                                        jsonObject1.addProperty("LabCode", labCode);
                                                        jsonObject1.addProperty("ProductID", con.getProductid());
                                                        jsonObject1.addProperty("AllotQauntity", con.getExpectedQuantity());
                                                        jsonObject1.addProperty("CampStatus", 1);
                                                        jsonObject1.addProperty("CampDate", CampCreationActivityV4.campCreationModel.getCampDetails().getCampDate());
                                                        jsonArray.add(jsonObject1);
                                                    }
                                                }

                                                Log.d(TAG, "consumableAllocation: " + consumableAllocation);

                                                mediProcAPI.allocatedProductListForCamp(consumableAllocation.toString(), String.valueOf(userSessionManager.getUserDetailsJson().getEmpCode()), "11").enqueue(new Callback<ResponseBody>() {
                                                    @Override
                                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                        if (response.isSuccessful()) {
                                                            try {
                                                                JSONObject jsonObject1 = new JSONObject(response.body().string());
                                                                String status = jsonObject1.getString("status");
                                                                if (status.equalsIgnoreCase("success")) {
                                                                    Utilities.showAlertDialog(mContext, status, "Camp Created Successfully..", true, "Okay", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                                            CampCreationActivityV4.campCreationModel = null;
                                                                            ((CampCreationActivityV4) mContext).finish();
                                                                        }
                                                                    });

                                                                } else {
                                                                    Utilities.showAlertDialog(mContext, "Success", "Camp Created Successfully.. but Stock not updated in inventory", true, "Okay", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                                            CampCreationActivityV4.campCreationModel = null;
                                                                            ((CampCreationActivityV4) mContext).finish();
                                                                        }
                                                                    });

                                                                }
                                                            } catch (
                                                                    JSONException e) {
                                                                e.printStackTrace();
                                                            } catch (
                                                                    IOException e) {
                                                                e.printStackTrace();
                                                            }

                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                                        Log.e(TAG, "allocatedProductListForCamp fail: " + t.getMessage());
                                                    }
                                                });

                                            } else {
                                                Utilities.showAlertDialog(mContext, "Lab not found", "Lab not mapped for this district", false, "Okay", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                                        ((CampApprovalActivity) mContext).finish();
                                                        dialogInterface.dismiss();
                                                    }
                                                });
                                            }
                                        } else {
                                            Utilities.showToastMessage("Lab Not Found", mContext, false);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }

                                @Override
                                public void onFailure(Call<GetLabOnDistrictListModel> call, Throwable t) {
                                    Log.e(TAG, "onFailure: " + t.getMessage());
                                    Utilities.showToastMessage("Lab Not Found " + t.getMessage(), mContext, false);

                                }
                            });


                        } else {
                            String msg = jsonObject.getString("ExceptionValue");
                            Utilities.showAlertDialog(mContext, status, msg, false);

                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                alertDialog.dismiss();

            }
        });

    }
    void insertChannelPartnerCampNew() {
        ResourceListForMappingModel resourceListForMappingModel = new ResourceListForMappingModel();
        allSelResourceModel.add(resourceListForMappingModel.new Output(0, "", 34, 3, true));
        CampCreationActivityV4.campCreationModel.setResources(allSelResourceModel);


        Gson gson = new GsonBuilder().addSerializationExclusionStrategy(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        if (f.getName().equalsIgnoreCase("desgid") || f.getName().equalsIgnoreCase("AVAILABELSTOCK") || f.getName().equalsIgnoreCase("Expectedbeneficiarycount") || f.getName().equalsIgnoreCase("selectedPartnerId")) {
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
//                                    .excludeFieldsWithoutExposeAnnotation()
                .serializeNulls()
                .create();

        AlertDialog alertDialog = Utilities.ProgressDialog(mContext, "Do You Want To Proceed").create();
        alertDialog.show();
        Log.i(TAG, "onClick Submit: " + gson.toJson(CampCreationActivityV4.campCreationModel));


        String SiteMappingJSON = gson.toJson(CampCreationActivityV4.campCreationModel.getSiteList());
        String CampDetailsJson = gson.toJson(CampCreationActivityV4.campCreationModel.getCampDetails());
        String ConsumableMappingJson = gson.toJson(CampCreationActivityV4.campCreationModel.getConsumables());
        String DeviceMappingJson = gson.toJson(CampCreationActivityV4.campCreationModel.getSelectedDevice());
        String ResourceMappingJson = gson.toJson(CampCreationActivityV4.campCreationModel.getResources());




        if (SkipFlag.equalsIgnoreCase("1")){
            String ResourceMappingJsonNew = gson.toJson(CampCreationActivityV4.campCreationModel.getResources());
            String DeviceMappingJsonNew =   "[{\"DeviceCompName\":\"BP-Bavdhan\",\"DeviceModel\":\"\",\"DeviceSerial\":\"BP-789\",\"DevicesId\":0,\"ISActive\":null,\"isChecked\":true,\"SubDevicesId\":0},{\"DeviceCompName\":\"DA-Bavdhan\",\"DeviceModel\":\"\",\"DeviceSerial\":\"DA-987\",\"DevicesId\":0,\"ISActive\":null,\"isChecked\":true,\"SubDevicesId\":0},{\"DeviceCompName\":\"SC-Bavdhan\",\"DeviceModel\":\"\",\"DeviceSerial\":\"SC-654\",\"DevicesId\":0,\"ISActive\":null,\"isChecked\":true,\"SubDevicesId\":0},{\"DeviceCompName\":\"SP-Pune\",\"DeviceModel\":\"\",\"DeviceSerial\":\"SP123\",\"DevicesId\":0,\"ISActive\":null,\"isChecked\":true,\"SubDevicesId\":0},{\"DeviceCompName\":\"WM-Bavdhan\",\"DeviceModel\":\"\",\"DeviceSerial\":\"WM-456\",\"DevicesId\":0,\"ISActive\":null,\"isChecked\":true,\"SubDevicesId\":0}]";
            apiService.inserCampCreationD2DWithoutApproval(  DeviceMappingJsonNew, ResourceMappingJsonNew,CampCreationActivityV4.campCreationModel.getCampDetails().getEmpCode(),CampId).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    alertDialog.dismiss();

                    Log.d(TAG, "onResponse Error: " + response.errorBody());
                    Log.d(TAG, "onResponse: " + response.body());
                    Log.d(TAG, "onResponse: " + response.code());

                    if (response.isSuccessful()) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase("success")) {
                                String msg = jsonObject.getString("message");

//                            apiService.getLabOnDistrict(distLgdCode).enqueue(new Callback<GetLabOnDistrictListModel>() {
//                                @Override
//                                public void onResponse(Call<GetLabOnDistrictListModel> call, Response<GetLabOnDistrictListModel> response) {
//                                    Log.d(TAG, "onResponse: " + response.body().getStatus());
//
//                                    try {
//                                        if (response.isSuccessful()) {
//                                            if (response.body().getOutput() != null && response.body().getOutput().size() > 0) {
//                                                labCode = response.body().getOutput().get(0).getLabCode();
//
//                                                JsonArray jsonArray = new JsonArray();
//                                                JsonObject consumableAllocation = new JsonObject();
//                                                consumableAllocation.add("CampAllocatedProduct", jsonArray);
//                                                for (ConsumableListModel.Output con :
//                                                        CampCreationActivityV4.campCreationModel.getConsumables()
//                                                ) {
//                                                    if (con.getAvailabelstock() != null) {
//                                                        JsonObject jsonObject1 = new JsonObject();
//                                                        jsonObject1.addProperty("CampID", msg);
//                                                        jsonObject1.addProperty("LabCode", labCode);
//                                                        jsonObject1.addProperty("ProductID", con.getProductid());
//                                                        jsonObject1.addProperty("AllotQauntity", con.getExpectedQuantity());
//                                                        jsonObject1.addProperty("CampStatus", 1);
//                                                        jsonObject1.addProperty("CampDate", CampCreationActivityV4.campCreationModel.getCampDetails().getCampDate());
//                                                        jsonArray.add(jsonObject1);
//                                                    }
//                                                }
//
//                                                Log.d(TAG, "consumableAllocation: " + consumableAllocation);
//
//                                                mediProcAPI.allocatedProductListForCamp(consumableAllocation.toString(), String.valueOf(userSessionManager.getUserDetailsJson().getEmpCode()), "11").enqueue(new Callback<ResponseBody>() {
//                                                    @Override
//                                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                                                        if (response.isSuccessful()) {
//                                                            try {
//                                                                JSONObject jsonObject1 = new JSONObject(response.body().string());
//                                                                String status = jsonObject1.getString("status");
//                                                                if (status.equalsIgnoreCase("success")) {
//                                                                    Utilities.showAlertDialog(mContext, status, "Camp Created Successfully..", true, "Okay", new DialogInterface.OnClickListener() {
//                                                                        @Override
//                                                                        public void onClick(DialogInterface dialogInterface, int i) {
//                                                                            CampCreationActivityV4.campCreationModel = null;
//                                                                            ((CampCreationActivityV4) mContext).finish();
//                                                                        }
//                                                                    });
//
//                                                                } else {
                                Utilities.showAlertDialog(mContext, "Success", "या कॅम्पसाठी Resource Mapping यशस्वीरीत्या पूर्ण झाले आहे. कृपया लक्षात घ्या की एकदा patient registration झाल्यानंतर या कॅम्पमध्ये कोणताही नवीन phlebotomist/doctor जोडता किंवा हटवता येणार नाही.", true, "Okay", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        CampCreationActivityV4.campCreationModel = null;
                                        ((CampCreationActivityV4) mContext).finish();
                                    }
                                });
//
//                                                                }
//                                                            } catch (
//                                                                    JSONException e) {
//                                                                e.printStackTrace();
//                                                            } catch (
//                                                                    IOException e) {
//                                                                e.printStackTrace();
//                                                            }
//
//                                                        }
//                                                    }
//
//                                                    @Override
//                                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                                                        Log.e(TAG, "allocatedProductListForCamp fail: " + t.getMessage());
//                                                    }
//                                                });
//
//                                            } else {
//                                                Utilities.showAlertDialog(mContext, "Lab not found", "Lab not mapped for this district", false, "Okay", new DialogInterface.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(DialogInterface dialogInterface, int i) {
////                                                        ((CampApprovalActivity) mContext).finish();
//                                                        dialogInterface.dismiss();
//                                                    }
//                                                });
//                                            }
//                                        } else {
//                                            Utilities.showToastMessage("Lab Not Found", mContext, false);
//                                        }
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//
//                                }
//
//                                @Override
//                                public void onFailure(Call<GetLabOnDistrictListModel> call, Throwable t) {
//                                    Log.e(TAG, "onFailure: " + t.getMessage());
//                                    Utilities.showToastMessage("Lab Not Found " + t.getMessage(), mContext, false);
//
//                                }
//                            });


                            } else {
                                String msg = jsonObject.getString("ExceptionValue");
                                Utilities.showAlertDialog(mContext, status, msg, false);

                            }
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    alertDialog.dismiss();


                }
            });

        }else if (SkipFlag.equalsIgnoreCase("0")){
            apiService.inserCampCreationD2DWithoutApproval(  DeviceMappingJson, ResourceMappingJson,CampCreationActivityV4.campCreationModel.getCampDetails().getEmpCode(),CampId).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    alertDialog.dismiss();

                    Log.d(TAG, "onResponse Error: " + response.errorBody());
                    Log.d(TAG, "onResponse: " + response.body());
                    Log.d(TAG, "onResponse: " + response.code());

                    if (response.isSuccessful()) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase("success")) {
                                String msg = jsonObject.getString("message");

//                            apiService.getLabOnDistrict(distLgdCode).enqueue(new Callback<GetLabOnDistrictListModel>() {
//                                @Override
//                                public void onResponse(Call<GetLabOnDistrictListModel> call, Response<GetLabOnDistrictListModel> response) {
//                                    Log.d(TAG, "onResponse: " + response.body().getStatus());
//
//                                    try {
//                                        if (response.isSuccessful()) {
//                                            if (response.body().getOutput() != null && response.body().getOutput().size() > 0) {
//                                                labCode = response.body().getOutput().get(0).getLabCode();
//
//                                                JsonArray jsonArray = new JsonArray();
//                                                JsonObject consumableAllocation = new JsonObject();
//                                                consumableAllocation.add("CampAllocatedProduct", jsonArray);
//                                                for (ConsumableListModel.Output con :
//                                                        CampCreationActivityV4.campCreationModel.getConsumables()
//                                                ) {
//                                                    if (con.getAvailabelstock() != null) {
//                                                        JsonObject jsonObject1 = new JsonObject();
//                                                        jsonObject1.addProperty("CampID", msg);
//                                                        jsonObject1.addProperty("LabCode", labCode);
//                                                        jsonObject1.addProperty("ProductID", con.getProductid());
//                                                        jsonObject1.addProperty("AllotQauntity", con.getExpectedQuantity());
//                                                        jsonObject1.addProperty("CampStatus", 1);
//                                                        jsonObject1.addProperty("CampDate", CampCreationActivityV4.campCreationModel.getCampDetails().getCampDate());
//                                                        jsonArray.add(jsonObject1);
//                                                    }
//                                                }
//
//                                                Log.d(TAG, "consumableAllocation: " + consumableAllocation);
//
//                                                mediProcAPI.allocatedProductListForCamp(consumableAllocation.toString(), String.valueOf(userSessionManager.getUserDetailsJson().getEmpCode()), "11").enqueue(new Callback<ResponseBody>() {
//                                                    @Override
//                                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                                                        if (response.isSuccessful()) {
//                                                            try {
//                                                                JSONObject jsonObject1 = new JSONObject(response.body().string());
//                                                                String status = jsonObject1.getString("status");
//                                                                if (status.equalsIgnoreCase("success")) {
//                                                                    Utilities.showAlertDialog(mContext, status, "Camp Created Successfully..", true, "Okay", new DialogInterface.OnClickListener() {
//                                                                        @Override
//                                                                        public void onClick(DialogInterface dialogInterface, int i) {
//                                                                            CampCreationActivityV4.campCreationModel = null;
//                                                                            ((CampCreationActivityV4) mContext).finish();
//                                                                        }
//                                                                    });
//
//                                                                } else {
                                Utilities.showAlertDialog(mContext, "Success", "Data Inserted successfully", true, "Okay", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        CampCreationActivityV4.campCreationModel = null;
                                        ((CampCreationActivityV4) mContext).finish();
                                    }
                                });
//
//                                                                }
//                                                            } catch (
//                                                                    JSONException e) {
//                                                                e.printStackTrace();
//                                                            } catch (
//                                                                    IOException e) {
//                                                                e.printStackTrace();
//                                                            }
//
//                                                        }
//                                                    }
//
//                                                    @Override
//                                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                                                        Log.e(TAG, "allocatedProductListForCamp fail: " + t.getMessage());
//                                                    }
//                                                });
//
//                                            } else {
//                                                Utilities.showAlertDialog(mContext, "Lab not found", "Lab not mapped for this district", false, "Okay", new DialogInterface.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(DialogInterface dialogInterface, int i) {
////                                                        ((CampApprovalActivity) mContext).finish();
//                                                        dialogInterface.dismiss();
//                                                    }
//                                                });
//                                            }
//                                        } else {
//                                            Utilities.showToastMessage("Lab Not Found", mContext, false);
//                                        }
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//
//                                }
//
//                                @Override
//                                public void onFailure(Call<GetLabOnDistrictListModel> call, Throwable t) {
//                                    Log.e(TAG, "onFailure: " + t.getMessage());
//                                    Utilities.showToastMessage("Lab Not Found " + t.getMessage(), mContext, false);
//
//                                }
//                            });


                            } else {
                                String msg = jsonObject.getString("ExceptionValue");
                                Utilities.showAlertDialog(mContext, status, msg, false);

                            }
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    alertDialog.dismiss();


                }
            });

        }


    }


    void insertD2dCampForPartner() {
        CampCreationActivityV4.campCreationModel.setResources(allSelResourceModel);


        Gson gson = new GsonBuilder().addSerializationExclusionStrategy(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        if (f.getName().equalsIgnoreCase("desgid") || f.getName().equalsIgnoreCase("AVAILABELSTOCK") || f.getName().equalsIgnoreCase("Expectedbeneficiarycount") || f.getName().equalsIgnoreCase("ISD2DCamp") || f.getName().equalsIgnoreCase("TALLGDCODE") || f.getName().equalsIgnoreCase("selectedPartnerId")) {
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
//                                    .excludeFieldsWithoutExposeAnnotation()
                .serializeNulls()
                .create();

        AlertDialog alertDialog = Utilities.ProgressDialog(mContext, "Creating Camp, Please wait").create();
        alertDialog.show();
        Log.i(TAG, "onClick Submit: " + gson.toJson(CampCreationActivityV4.campCreationModel));


        String SiteMappingJSON = gson.toJson(CampCreationActivityV4.campCreationModel.getSiteList());
        String CampDetailsJson = gson.toJson(CampCreationActivityV4.campCreationModel.getCampDetails());
        String DeviceMappingJson = gson.toJson(CampCreationActivityV4.campCreationModel.getSelectedDevice());
        String ConsumableMappingJson = gson.toJson(CampCreationActivityV4.campCreationModel.getConsumables());
        String ResourceMappingJson = gson.toJson(CampCreationActivityV4.campCreationModel.getResources());

        apiService.inserCampCreationD2DWithoutApprovalNew(String.valueOf(userSessionManager.getUserDetailsJson().getEmpCode()), String.valueOf(CampCreationActivityV4.campCreationModel.getISD2DCamp()), SiteMappingJSON, CampDetailsJson, DeviceMappingJson, ConsumableMappingJson, ResourceMappingJson, CampCreationActivityV4.campCreationModel.getCampDetails().getSelectedPartnerId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                alertDialog.dismiss();

                Log.d(TAG, "onResponse Error: " + response.errorBody());
                Log.d(TAG, "onResponse: " + response.body());
                Log.d(TAG, "onResponse: " + response.code());

                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("success")) {
                            String msg = jsonObject.getString("message");

                            apiService.getLabOnDistrict(distLgdCode).enqueue(new Callback<GetLabOnDistrictListModel>() {
                                @Override
                                public void onResponse(Call<GetLabOnDistrictListModel> call, Response<GetLabOnDistrictListModel> response) {
                                    Log.d(TAG, "onResponse: " + response.body().getStatus());

                                    try {
                                        if (response.isSuccessful()) {
                                            if (response.body().getOutput() != null && response.body().getOutput().size() > 0) {
                                                labCode = response.body().getOutput().get(0).getLabCode();

                                                JsonArray jsonArray = new JsonArray();
                                                JsonObject consumableAllocation = new JsonObject();
                                                consumableAllocation.add("CampAllocatedProduct", jsonArray);
                                                for (ConsumableListModel.Output con :
                                                        CampCreationActivityV4.campCreationModel.getConsumables()
                                                ) {
                                                    if (con.getAvailabelstock() != null) {
                                                        JsonObject jsonObject1 = new JsonObject();
                                                        jsonObject1.addProperty("CampID", msg);
                                                        jsonObject1.addProperty("LabCode", labCode);
                                                        jsonObject1.addProperty("ProductID", con.getProductid());
                                                        jsonObject1.addProperty("AllotQauntity", con.getExpectedQuantity());
                                                        jsonObject1.addProperty("CampStatus", 1);
                                                        jsonObject1.addProperty("CampDate", CampCreationActivityV4.campCreationModel.getCampDetails().getCampDate());
                                                        jsonArray.add(jsonObject1);
                                                    }

                                                }

                                                Log.d(TAG, "consumableAllocation: " + consumableAllocation);

                                                mediProcAPI.allocatedProductListForCamp(consumableAllocation.toString(), String.valueOf(userSessionManager.getUserDetailsJson().getEmpCode()), "11").enqueue(new Callback<ResponseBody>() {
                                                    @Override
                                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                        if (response.isSuccessful()) {
                                                            try {
                                                                JSONObject jsonObject1 = new JSONObject(response.body().string());
                                                                String status = jsonObject1.getString("status");
                                                                if (status.equalsIgnoreCase("success")) {
                                                                    Utilities.showAlertDialog(mContext, status, "Camp Created Successfully..", true, "Okay", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                                            CampCreationActivityV4.campCreationModel = null;
                                                                            ((CampCreationActivityV4) mContext).finish();
                                                                        }
                                                                    });

                                                                } else {
                                                                    Utilities.showAlertDialog(mContext, "Success", "Camp Created Successfully.. but Stock not updated in inventory", true, "Okay", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                                            CampCreationActivityV4.campCreationModel = null;
                                                                            ((CampCreationActivityV4) mContext).finish();
                                                                        }
                                                                    });

                                                                }
                                                            } catch (
                                                                    JSONException e) {
                                                                e.printStackTrace();
                                                            } catch (
                                                                    IOException e) {
                                                                e.printStackTrace();
                                                            }

                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                                        Log.e(TAG, "allocatedProductListForCamp fail: " + t.getMessage());
                                                    }
                                                });

                                            } else {
                                                Utilities.showAlertDialog(mContext, "Lab not found", "Lab not mapped for this district", false, "Okay", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                                        ((CampApprovalActivity) mContext).finish();
                                                        dialogInterface.dismiss();
                                                    }
                                                });
                                            }
                                        } else {
                                            Utilities.showToastMessage("Lab Not Found", mContext, false);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }

                                @Override
                                public void onFailure(Call<GetLabOnDistrictListModel> call, Throwable t) {
                                    Log.e(TAG, "onFailure: " + t.getMessage());
                                    Utilities.showToastMessage("Lab Not Found " + t.getMessage(), mContext, false);

                                }
                            });


                        } else {
                            String msg = jsonObject.getString("ExceptionValue");
                            Utilities.showAlertDialog(mContext, status, msg, false);

                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                alertDialog.dismiss();


            }
        });

    }

    void InserCampCreationD2DWithoutApprovalForPartner() {
        CampCreationActivityV4.campCreationModel.setResources(allSelResourceModel);


        Gson gson = new GsonBuilder().addSerializationExclusionStrategy(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        if (f.getName().equalsIgnoreCase("desgid") || f.getName().equalsIgnoreCase("AVAILABELSTOCK") || f.getName().equalsIgnoreCase("Expectedbeneficiarycount") || f.getName().equalsIgnoreCase("ISD2DCamp") || f.getName().equalsIgnoreCase("TALLGDCODE") || f.getName().equalsIgnoreCase("selectedPartnerId")) {
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
//                                    .excludeFieldsWithoutExposeAnnotation()
                .serializeNulls()
                .create();

        AlertDialog alertDialog = Utilities.ProgressDialog(mContext, "Creating Camp, Please wait").create();
        alertDialog.show();
        Log.i(TAG, "onClick Submit: " + gson.toJson(CampCreationActivityV4.campCreationModel));


        String SiteMappingJSON = gson.toJson(CampCreationActivityV4.campCreationModel.getSiteList());
        String CampDetailsJson = gson.toJson(CampCreationActivityV4.campCreationModel.getCampDetails());
        String DeviceMappingJson = gson.toJson(CampCreationActivityV4.campCreationModel.getSelectedDevice());
        String ConsumableMappingJson = gson.toJson(CampCreationActivityV4.campCreationModel.getConsumables());
        String ResourceMappingJson = gson.toJson(CampCreationActivityV4.campCreationModel.getResources());

        apiService.inserCampCreationD2DWithoutApprovalForPartner(String.valueOf(userSessionManager.getUserDetailsJson().getEmpCode()), String.valueOf(CampCreationActivityV4.campCreationModel.getISD2DCamp()), SiteMappingJSON, CampDetailsJson, /*DeviceMappingJson, ConsumableMappingJson,*/ ResourceMappingJson).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                alertDialog.dismiss();

                Log.d(TAG, "onResponse Error: " + response.errorBody());
                Log.d(TAG, "onResponse: " + response.body());
                Log.d(TAG, "onResponse: " + response.code());

                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("success")) {
                            String msg = jsonObject.getString("message");

                            apiService.getLabOnDistrict(distLgdCode).enqueue(new Callback<GetLabOnDistrictListModel>() {
                                @Override
                                public void onResponse(Call<GetLabOnDistrictListModel> call, Response<GetLabOnDistrictListModel> response) {
                                    Log.d(TAG, "onResponse: " + response.body().getStatus());

                                    try {
                                        if (response.isSuccessful()) {
                                            if (response.body().getOutput() != null && response.body().getOutput().size() > 0) {
                                                labCode = response.body().getOutput().get(0).getLabCode();

                                                JsonArray jsonArray = new JsonArray();
                                                JsonObject consumableAllocation = new JsonObject();
                                                consumableAllocation.add("CampAllocatedProduct", jsonArray);
                                                for (ConsumableListModel.Output con :
                                                        CampCreationActivityV4.campCreationModel.getConsumables()
                                                ) {
                                                    if (con.getAvailabelstock() != null) {
                                                        JsonObject jsonObject1 = new JsonObject();
                                                        jsonObject1.addProperty("CampID", msg);
                                                        jsonObject1.addProperty("LabCode", labCode);
                                                        jsonObject1.addProperty("ProductID", con.getProductid());
                                                        jsonObject1.addProperty("AllotQauntity", con.getExpectedQuantity());
                                                        jsonObject1.addProperty("CampStatus", 1);
                                                        jsonObject1.addProperty("CampDate", CampCreationActivityV4.campCreationModel.getCampDetails().getCampDate());
                                                        jsonArray.add(jsonObject1);
                                                    }

                                                }

                                                Log.d(TAG, "consumableAllocation: " + consumableAllocation);

                                                mediProcAPI.allocatedProductListForCamp(consumableAllocation.toString(), String.valueOf(userSessionManager.getUserDetailsJson().getEmpCode()), "11").enqueue(new Callback<ResponseBody>() {
                                                    @Override
                                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                        if (response.isSuccessful()) {
                                                            try {
                                                                JSONObject jsonObject1 = new JSONObject(response.body().string());
                                                                String status = jsonObject1.getString("status");
                                                                if (status.equalsIgnoreCase("success")) {
                                                                    Utilities.showAlertDialog(mContext, status, "Camp Created Successfully..", true, "Okay", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                                            CampCreationActivityV4.campCreationModel = null;
                                                                            ((CampCreationActivityV4) mContext).finish();
                                                                        }
                                                                    });

                                                                } else {
                                                                    Utilities.showAlertDialog(mContext, "Success", "Camp Created Successfully.. but Stock not updated in inventory", true, "Okay", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                                            CampCreationActivityV4.campCreationModel = null;
                                                                            ((CampCreationActivityV4) mContext).finish();
                                                                        }
                                                                    });

                                                                }
                                                            } catch (
                                                                    JSONException e) {
                                                                e.printStackTrace();
                                                            } catch (
                                                                    IOException e) {
                                                                e.printStackTrace();
                                                            }

                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                                        Log.e(TAG, "allocatedProductListForCamp fail: " + t.getMessage());
                                                    }
                                                });

                                            } else {
                                                Utilities.showAlertDialog(mContext, "Lab not found", "Lab not mapped for this district", false, "Okay", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                                        ((CampApprovalActivity) mContext).finish();
                                                        dialogInterface.dismiss();
                                                    }
                                                });
                                            }
                                        } else {
                                            Utilities.showToastMessage("Lab Not Found", mContext, false);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }

                                @Override
                                public void onFailure(Call<GetLabOnDistrictListModel> call, Throwable t) {
                                    Log.e(TAG, "onFailure: " + t.getMessage());
                                    Utilities.showToastMessage("Lab Not Found " + t.getMessage(), mContext, false);

                                }
                            });


                        } else {
                            String msg = jsonObject.getString("ExceptionValue");
                            Utilities.showAlertDialog(mContext, status, msg, false);

                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    Utilities.showAlertDialog(mContext, "Unable to create camp", "something went wrong", false);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                alertDialog.dismiss();


            }
        });

    }


    void getTestList() {
        progressBar.setVisibility(View.VISIBLE);
        rvDesignation.setVisibility(View.GONE);

        if (Utilities.isNetworkAvailable(mContext)) {
            apiService.getTestListForMapResourceDesgForD2d().enqueue(new Callback<TestListForResourceMappingModel>() {
                @Override
                public void onResponse(Call<TestListForResourceMappingModel> call, Response<TestListForResourceMappingModel> response) {
                    progressBar.setVisibility(View.GONE);
                    rvDesignation.setVisibility(View.VISIBLE);
                    Log.i(TAG, "onResponse: " + response);
                    if (response.isSuccessful()) {
                        testListForResourceMappingModel = response.body();
                        if (testListForResourceMappingModel.getStatus().equalsIgnoreCase("success")) {
                            if (testListForResourceMappingModel.getOutput().size() > 0) {
//                                Collections.sort(testListForResourceMappingModel.getOutput(), (o1, o2) -> o1.getTestName().compareTo(o2.getTestName()));
                                designationListAdapter = new TestListForResourceMappingAdapter(testListForResourceMappingModel.getOutput(), ResourceAllocationFragment.this);
                                rvDesignation.setAdapter(designationListAdapter);
                            } else {
                                rvDesignation.setVisibility(View.GONE);
                                tvError.setVisibility(View.VISIBLE);
                            }
                        } else {
                            rvDesignation.setVisibility(View.GONE);
                            tvError.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<TestListForResourceMappingModel> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    tvError.setVisibility(View.VISIBLE);

                    Log.e(TAG, "onFailure: " + t.getMessage());
                }
            });


        } else {


            apiService.getTestListForMapResourceDesg().enqueue(new Callback<TestListForResourceMappingModel>() {
                @Override
                public void onResponse(Call<TestListForResourceMappingModel> call, Response<TestListForResourceMappingModel> response) {
                    progressBar.setVisibility(View.GONE);
                    rvDesignation.setVisibility(View.VISIBLE);
                    Log.i(TAG, "onResponse: " + response);

                    if (response.isSuccessful()) {
                        testListForResourceMappingModel = response.body();
                        if (testListForResourceMappingModel.getStatus().equalsIgnoreCase("success")) {
                            if (testListForResourceMappingModel.getOutput().size() > 0) {
                                Collections.sort(testListForResourceMappingModel.getOutput(), (o1, o2) -> o1.getTestName().compareTo(o2.getTestName()));
                                designationListAdapter = new TestListForResourceMappingAdapter(testListForResourceMappingModel.getOutput(), ResourceAllocationFragment.this);
                                rvDesignation.setAdapter(designationListAdapter);
                            } else {
                                rvDesignation.setVisibility(View.GONE);
                                tvError.setVisibility(View.VISIBLE);
                            }
                        } else {
                            rvDesignation.setVisibility(View.GONE);
                            tvError.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<TestListForResourceMappingModel> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    tvError.setVisibility(View.VISIBLE);

                    Log.e(TAG, "onFailure: " + t.getMessage());
                }
            });

        }
    }


    void getResources(TestListForResourceMappingModel.Output designation) {
        if (designation.getResources() == null) {

            if (Utilities.isNetworkAvailable(mContext)) {
                apiService.getTestidFromResourcelistNewForPatnerResorces(designation.getTestId(), CampCreationActivityV4.campCreationModel.getCampDetails().getCampDate(), CampCreationActivityV4.campCreationModel.getCampDetails().getDistLgdCode(), 1, Integer.parseInt(CampCreationActivityV4.campCreationModel.getCampDetails().getLabCode())).enqueue(new Callback<ResourceListForMappingModel>() {
                    @Override
                    public void onResponse(Call<ResourceListForMappingModel> call, Response<ResourceListForMappingModel> response) {

                        Log.i(TAG, "onResponse: " + response);
                        if (response.isSuccessful()) {
                            resourceListModel = response.body();
                            if (resourceListModel.getStatus().equalsIgnoreCase("success")) {
                                if (resourceListModel.getOutput().size() > 0) {
                                    Collections.sort(resourceListModel.getOutput(), (o1, o2) -> o1.getResourceName().compareTo(o2.getResourceName()));
                                    showResourcesDialog(designation);
                                }
                            } else {
                                Utilities.showToastMessage("Resources not available", mContext, false);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResourceListForMappingModel> call, Throwable t) {

                        Log.e(TAG, "onFailure: " + t.getMessage());
                    }
                });

            } else {
                apiService.getTestidFromResourcelist(designation.getTestId(), CampCreationActivityV4.campCreationModel.getCampDetails().getCampDate(), distLgdCode).enqueue(new Callback<ResourceListForMappingModel>() {
                    @Override
                    public void onResponse(Call<ResourceListForMappingModel> call, Response<ResourceListForMappingModel> response) {

                        Log.i(TAG, "onResponse: " + response);
                        if (response.isSuccessful()) {
                            resourceListModel = response.body();
                            if (resourceListModel.getStatus().equalsIgnoreCase("success")) {
                                if (resourceListModel.getOutput().size() > 0) {
                                    Collections.sort(resourceListModel.getOutput(), (o1, o2) -> o1.getResourceName().compareTo(o2.getResourceName()));
                                    showResourcesDialog(designation);
                                }
                            } else {
                                Utilities.showToastMessage("Resources not available", mContext, false);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResourceListForMappingModel> call, Throwable t) {

                        Log.e(TAG, "onFailure: " + t.getMessage());
                    }
                });
            }
        } else {
            resourceListModel.setOutput(designation.getResources());
            showResourcesDialog(designation);

        }
    }


    private void showResourcesDialog(TestListForResourceMappingModel.Output test) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_check_list, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(view);
        builder.setTitle("Select Resources");
        builder.setCancelable(false);

        RecyclerView rv_checklist = view.findViewById(R.id.rv_checklist);
        rv_checklist.setLayoutManager(new LinearLayoutManager(mContext));
        rv_checklist.setAdapter(new ResourceListAdapter());

        builder.setPositiveButton("Select", (dialog, which) -> {


            int checkedTestCount = 0;
            StringBuilder selectedSubCategories = new StringBuilder();
            ArrayList<ResourceListForMappingModel.Output> selResourceListModel = new ArrayList<ResourceListForMappingModel.Output>();
            selResourceListModel.addAll(resourceListModel.getOutput());

            for (ResourceListForMappingModel.Output output : resourceListModel.getOutput()) {

                if (output.isChecked()) {
                    checkedTestCount = checkedTestCount + 1;
                    if (!allSelResourceModel.contains(output)) {
                        allSelResourceModel.add(output);
                    }
                } else {
                    if (allSelResourceModel.contains(output)) {
                        allSelResourceModel.remove(output);
                    }
                }
            }

            if (checkedTestCount == 0) {
                Utilities.showToastMessage("Please select at least one resource", mContext, false);
            }
            test.setResources(selResourceListModel);
            rvDesignation.setAdapter(designationListAdapter);

//            deviceListAdapter.notifyDataSetChanged();


        });

        builder.create().show();
    }


    @Override
    public void onTestSelected(TestListForResourceMappingModel.Output output) {
        getResources(output);
    }


    private class ResourceListAdapter extends RecyclerView.Adapter<ResourceListAdapter.MyViewHolder> {

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.list_row_checklist, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int pos) {
            final int position = holder.getAdapterPosition();

            holder.cb_select.setText(resourceListModel.getOutput().get(position).getResourceName());

            if (resourceListModel.getOutput().get(position).isChecked()) {
                holder.cb_select.setChecked(true);
            }

            holder.cb_select.setOnCheckedChangeListener((buttonView, isChecked) -> {
                resourceListModel.getOutput().get(position).setChecked(isChecked);
            });
        }

        @Override
        public int getItemCount() {
            return resourceListModel.getOutput().size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private CheckBox cb_select;

            public MyViewHolder(@NonNull View view) {
                super(view);
                cb_select = view.findViewById(R.id.cb_select);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }
}