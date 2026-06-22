package com.myhindlab.abkat.activities.regularcampcreation;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.regularcampcreation.adapter.ConsumableListAdapter;
import com.myhindlab.abkat.activities.regularcampcreation.model.ConsumableListModel;
import com.myhindlab.abkat.activities.regularcampcreation.model.GetLabOnDistrictListModel;
import com.myhindlab.abkat.activities.regularcampcreation.model.ProductStockListModel;
import com.myhindlab.abkat.models.UserDetailsModel;
import com.myhindlab.abkat.rest.ApiClient;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.rest.MediProcAPI;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConsumableAllocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsumableAllocationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final String TAG = ConsumableAllocationFragment.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView tvError, tvMsg;
    private RecyclerView rvConsumableList;
    private Button btnNext,btnSkip;
    private ProgressBar progressBar;
    static FragmentChange fragmentChange;
    private UserSessionManager userSessionManager;
    private Context mContext;
    private ApiInterface apiService;
    private MediProcAPI mediProcAPI;
    private int distLgdCode;
    private ConsumableListModel consumableListModel;
    private ProductStockListModel productStockListModel;
    private ConsumableListAdapter consumableListAdapter;


    public ConsumableAllocationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1     Parameter 1.
     * @param param2     Parameter 2.
     * @param fragChange
     * @return A new instance of fragment ConsumableAllocationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConsumableAllocationFragment newInstance(String param1, String param2, FragmentChange fragChange) {
        ConsumableAllocationFragment fragment = new ConsumableAllocationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        fragmentChange = fragChange;
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
        View rootView = inflater.inflate(R.layout.fragment_consumable_allocation, container, false);

        initView(rootView);
        getSessionDetails();
        clickEvents();
        return rootView;
    }

    private void initView(View view) {
        mContext = requireContext();
        userSessionManager = new UserSessionManager(mContext);
        apiService = ApiClient.getD2DClient().create(ApiInterface.class);
        mediProcAPI = ApiClient.getD2DClient().create(MediProcAPI.class);

        rvConsumableList = view.findViewById(R.id.rvConsumable);
        tvError = view.findViewById(R.id.tvError);
        progressBar = view.findViewById(R.id.progressBar);
        btnNext = view.findViewById(R.id.btnConsumableNext);
        btnSkip = view.findViewById(R.id.btnSkip);
        tvMsg = view.findViewById(R.id.tvMsg);

        rvConsumableList.setHasFixedSize(true);
        rvConsumableList.setLayoutManager(new LinearLayoutManager(mContext));


//        if (Utilities.isNetworkAvailable(mContext)) {
//            getConsumables();
//        } else {
//            progressBar.setVisibility(View.GONE);
//            rvConsumableList.setVisibility(View.GONE);
//            tvError.setVisibility(View.VISIBLE);
//            tvError.setText("No need to map consumables");
//        }

    }

    private void getSessionDetails() {
        UserDetailsModel.Output userResponseModel = userSessionManager.getUserDetailsJson();
      //  distLgdCode = CampCreationActivityV4.campCreationModel.getCampDetails().getDistLgdCode();


        if (Utilities.isNetworkAvailable(mContext)) {
//            getConsumables();
        } else {
            progressBar.setVisibility(View.GONE);
            rvConsumableList.setVisibility(View.GONE);
            tvError.setVisibility(View.VISIBLE);
            tvError.setText("No need to map consumables");
        }
    }

    void clickEvents() {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isNetworkAvailable(mContext)) {

                    CampCreationActivityV4.campCreationModel.setConsumables(new ArrayList<>());
                    fragmentChange.onFragmentChange(2);
                    return;
                }
                if (consumableListModel != null) {
                    Utilities.showAlertDialog(mContext, "Confirmation", "Go to Resource allocation", true, "Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            CampCreationActivityV4.campCreationModel.setConsumables(consumableListModel.getOutput());
                            fragmentChange.onFragmentChange(2);
                        }
                    });
                } else {
                    Utilities.showToastMessage("Please select consumable list", mContext, false);
                }

            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isNetworkAvailable(mContext)) {

                    CampCreationActivityV4.campCreationModel.setConsumables(new ArrayList<>());
                    fragmentChange.onFragmentChange(2);
                    return;
                }
                if (consumableListModel != null) {
                    Utilities.showAlertDialog(mContext, "Confirmation", "Go to Resource allocation", true, "Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            CampCreationActivityV4.campCreationModel.setConsumables(consumableListModel.getOutput());
                            fragmentChange.onFragmentChange(2);
                        }
                    });
                } else {
                    Utilities.showToastMessage("Please select consumable list", mContext, false);
                }

            }
        });
    }


    void getConsumables() {
        progressBar.setVisibility(View.VISIBLE);
        rvConsumableList.setVisibility(View.GONE);

        apiService.getConsumableListForExpectedBeneficiary(CampCreationActivityV4.campCreationModel.getCampDetails().getExpectedBeneficiary()!=null?CampCreationActivityV4.campCreationModel.getCampDetails().getExpectedBeneficiary():0).enqueue(new Callback<ConsumableListModel>() {
            @Override
            public void onResponse(Call<ConsumableListModel> call, Response<ConsumableListModel> response) {
                progressBar.setVisibility(View.GONE);
                rvConsumableList.setVisibility(View.VISIBLE);
                Log.i(TAG, "onResponse: " + response);
                if (response.isSuccessful()) {
                    consumableListModel = response.body();
                    if (consumableListModel.getStatus().equalsIgnoreCase("success")) {
                        if (consumableListModel.getOutput().size() > 0) {
//                            Collections.sort(consumableListModel.getOutput(), (o1, o2) -> o1.getProductName().compareTo(o2.getProductName()));
//                            consumableListAdapter = new ConsumableListAdapter(response.body().getOutput());
//                            rvConsumableList.setAdapter(consumableListAdapter);

//                            getLabOnDistrict();
//                            getConsumablesDetailsFromMediProcs("5001");
                        } else {
                            rvConsumableList.setVisibility(View.GONE);
                            tvError.setVisibility(View.VISIBLE);
                        }
                    } else {
                        rvConsumableList.setVisibility(View.GONE);
                        tvError.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ConsumableListModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                tvError.setVisibility(View.VISIBLE);

                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    void getLabOnDistrict() {

        apiService.getLabOnDistrict(CampCreationActivityV4.campCreationModel.getCampDetails().getDistLgdCode()).enqueue(new Callback<GetLabOnDistrictListModel>() {
            @Override
            public void onResponse(Call<GetLabOnDistrictListModel> call, Response<GetLabOnDistrictListModel> response) {
                Log.d(TAG, "onResponse: " + response.body().getStatus());

                try {
                    if (response.isSuccessful()) {
                        if (response.body().getOutput() != null && response.body().getOutput().size() > 0) {
                            getConsumablesDetailsFromMediProcs(CampCreationActivityV4.campCreationModel.getCampDetails().getLabCode());
                        } else {
                            Utilities.showAlertDialog(mContext, "Lab not found", "Lab not mapped for this district", false, "Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    ((CampApprovalActivity) mContext).finish();
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

    }

    void getConsumablesDetailsFromMediProcs(String labcode) {
        progressBar.setVisibility(View.VISIBLE);
        rvConsumableList.setVisibility(View.GONE);
        apiService.getLabProductStockListByLab(CampCreationActivityV4.campCreationModel.getCampDetails().getLabCode()).enqueue(new Callback<ProductStockListModel>() {
            @Override
            public void onResponse(Call<ProductStockListModel> call, Response<ProductStockListModel> response) {
                progressBar.setVisibility(View.GONE);
                rvConsumableList.setVisibility(View.VISIBLE);
                Log.i(TAG, "onResponse: " + response);
                if (response.isSuccessful()) {
                    productStockListModel = response.body();
                    if (productStockListModel.getStatus().equalsIgnoreCase("success")) {
                        if (consumableListModel.getOutput().size() > 0) {
                            for (ConsumableListModel.Output c :
                                    consumableListModel.getOutput()) {
                                for (ProductStockListModel.Output p :
                                        productStockListModel.getOutput()) {
                                    if (Objects.equals(c.getProductid(), p.getProductId())) {
                                        c.setAvailabelstock(p.getAvailabelstock());
                                    }
                                }
                            }
                            Collections.sort(consumableListModel.getOutput(), (o1, o2) -> o1.getProductName().compareTo(o2.getProductName()));
                            consumableListAdapter = new ConsumableListAdapter(consumableListModel.getOutput());
                            rvConsumableList.setAdapter(consumableListAdapter);
                        } else {
                            rvConsumableList.setVisibility(View.GONE);
                            tvError.setVisibility(View.VISIBLE);
                        }
                    } else {
                        rvConsumableList.setVisibility(View.GONE);
                        tvError.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductStockListModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                tvError.setVisibility(View.VISIBLE);

                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (Utilities.isNetworkAvailable(mContext)) {
                getConsumables();

//                getLabOnDistrict();
            } else {
                tvMsg.setText("*Quantity has been Calculated based on the beneficiary count(" + CampCreationActivityV4.campCreationModel.getCampDetails().getExpectedBeneficiary() + ")");
                tvMsg.setVisibility(View.GONE);

            }
        }
    }
}