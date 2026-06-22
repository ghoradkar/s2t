package com.myhindlab.abkat.activities.campApproval;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.campApproval.adapter.ResourceListForApprovalAdapter;
import com.myhindlab.abkat.activities.campApproval.adapter.SiteListAdapter;
import com.myhindlab.abkat.activities.campApproval.models.AllocatedResourceListModel;
import com.myhindlab.abkat.activities.campApproval.models.CampDetailsModel;
import com.myhindlab.abkat.activities.campApproval.models.CampOrganizedListModel;
import com.myhindlab.abkat.activities.campApproval.models.ResourceDetailsForApprovalModel;
import com.myhindlab.abkat.activities.regularcampcreation.adapter.ConsumableListAdapter;
import com.myhindlab.abkat.activities.regularcampcreation.adapter.DeviceListAdapter;
import com.myhindlab.abkat.activities.regularcampcreation.model.AllocatedSubDeviceListModel;
import com.myhindlab.abkat.activities.regularcampcreation.model.ConsumableDetailsForApprovalModel;
import com.myhindlab.abkat.activities.regularcampcreation.model.DeviceDetailsForApprovalModel;
import com.myhindlab.abkat.activities.regularcampcreation.model.DeviceListModel;
import com.myhindlab.abkat.activities.regularcampcreation.model.GetLabOnDistrictListModel;
import com.myhindlab.abkat.activities.regularcampcreation.model.ProductStockListModel;
import com.myhindlab.abkat.activities.regularcampcreation.model.SiteListModel;
import com.myhindlab.abkat.activities.regularcampcreation.model.UserResponseModel;
import com.myhindlab.abkat.models.DoctorModel;
import com.myhindlab.abkat.models.UserDetailsModel;
import com.myhindlab.abkat.rest.ApiClient;
import com.myhindlab.abkat.rest.ApiInterface;
import com.myhindlab.abkat.rest.MediProcAPI;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.rengwuxian.materialedittext.MaterialEditText;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CampApprovalActivity extends AppCompatActivity implements SiteListAdapter.SiteListEvent, DeviceListAdapter.DeviceEvent, ResourceListForApprovalAdapter.ResourceListForApprovalEvent {

    //Site List
    private UserSessionManager userSessionManager;
    private Context mContext;
    private int distLgdCode;
    private RecyclerView rvSitesList;
    private final String TAG = CampApprovalActivity.class.getSimpleName();
    private SearchView siteSearchView;
    private SiteListModel siteListModel;
    private ArrayList<SiteListModel.Output> searchList;
    private Button btnNext;
    private List<SiteListModel.Output> selSites;
    private MaterialCardView cvSiteList;
    private boolean siteSectionVisibility = true;
    private ConstraintLayout clSiteList;
    private TextView tvSiteList, tvSiteError;

    //Camp Details
    private MaterialEditText edt_district, edt_camp_address, edt_camp_name, edt_camp_date,
            edt_post_camp_date, edt_lab, edt_hospital, edt_aarogyamitra_name, edt_aarogyamitra_mobile, edt_screening_tests,
            edt_lab_1, edt_tests_for_lab_1, edt_lab_2, edt_tests_for_lab_2, edt_lab_3, edt_tests_for_lab_3, edt_camp_organized_by, edt_expected_beneficiary, edt_sel_address;
    private ImageView imv_camp_approval_letter;
    private TextView tvExpectedBeneficiary;
    private Button btn_save;
    private Uri imageURI;
    private final int CAMERA_REQUEST = 100;
    private File patientPicsFolder;
    private String userId, labCode, labCodeOne, labCodeTwo, hospitalId, imagePath = "", labCodeThree;
    private int mYear, mMonth, mDay, campOrgId;
    private ArrayList<CampOrganizedListModel.Output> campOrganizedListModel;
    private CampDetailsModel.Output campDetailsModel;
    private MaterialCardView cvCampDetails;
    private LinearLayout llCampDetails;
    private TextView tvCampDetails;
    private boolean campDetailsSectionVisibility = false;


    //Device List
    private TextView tvDeviceError;
    private RecyclerView rvDeviceList;
    private ProgressBar progressBar;
    private DeviceDetailsForApprovalModel deviceListModel;
    private AllocatedSubDeviceListModel subDeviceListModel;
    private ApiInterface apiService;
    private DeviceListAdapter deviceListAdapter;
    private List<AllocatedSubDeviceListModel.Output> allSelSubDeviceListModel;
    private boolean deviceSectionVisibility = false;
    private MaterialCardView cvDeviceAllocation;
    private ConstraintLayout clDeviceAllocation, clDeviceHeader, clDeviceStatusButton;
    private TextView tvDeviceDetails;
    private String deviceRemark;
    private RadioButton btnDeviceReject, btnDeviceHold, btnDeviceApprove;
    private int isDeviceApprove = 2;
    private TextInputLayout textLayoutDevice;
    private TextInputEditText edtRemarkDevice;


    //Consumables
    private TextView tvError, tvConsumableMsg, tvConsumable;
    private RecyclerView rvConsumableList;
    private ProgressBar consumablesprogressBar;
    private ConsumableDetailsForApprovalModel consumableListModel;
    private ConsumableListAdapter consumableListAdapter;
    private ConstraintLayout clConsumableStatusButton, clConsumable;
    private MaterialCardView cvConsumable, cvConsumableHeader;
    private boolean consumableSectionVisibility = false;
    private RadioButton btnConsumableReject, btnConsumableHold, btnConsumableApprove;
    private String consumableRemark;
    private int isConsumableApprove = 2;
    private TextInputLayout textLayoutConsumable;
    private TextInputEditText edtRemarkConsumable;
    private ConsumableDetailsForApprovalModel consumableDetailsForApprovalModel;
    private MediProcAPI mediProcAPI;
    private ProductStockListModel productStockListModel;


    //Resource Allocation
    private TextView tvResourceError, tvResource;
    private RecyclerView rvDesignation;
    private Button btnSubmit;
    private ProgressBar resourceProgressBar;
    private ResourceDetailsForApprovalModel designationListModel;
    private AllocatedResourceListModel resourceListModel;
    private AllocatedResourceListModel allocatedResourceListModel;
    private ResourceListForApprovalAdapter resourceListAdapter;
    private List<AllocatedResourceListModel.Output> allSelResourceModel;
    private ConstraintLayout clResourceStatusButton, clResource;
    private MaterialCardView cvResource;
    private CardView cvResourceHeader;
    private boolean resourceSectionVisibility = false;
    private RadioButton btnResourceReject, btnResourceHold, btnResourceApprove;
    private String resourceRemark;
    private int isResourceApprove = 2;
    private TextInputLayout textLayoutResource;
    private TextInputEditText edtRemarkResource;
    private ResourceDetailsForApprovalModel resourceDetailsForApprovalModel;


    private int campId, campStatus;
    private String campDate;
    private AlertDialog progressDialog;
    private Button btnFinalApproval;

    private ConstraintLayout constraintLayout6;
    private String campType;
    private boolean isPartnerCamp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_approval);

        setUpToolbar();
        init();
        getSessionDetails();
        clickEvent();
    }

    void init() {
        mContext = CampApprovalActivity.this;
        userSessionManager = new UserSessionManager(mContext);
        apiService = ApiClient.getD2DClient().create(ApiInterface.class);
        mediProcAPI = ApiClient.getMediProcClient().create(MediProcAPI.class);
        allSelSubDeviceListModel = new ArrayList<AllocatedSubDeviceListModel.Output>();
        allSelResourceModel = new ArrayList<>();

        selSites = new ArrayList<>();
        progressDialog = Utilities.ProgressDialog(mContext, "Please Wait..").create();

        //Site List
        siteSearchView = findViewById(R.id.siteSearchView);
        rvSitesList = findViewById(R.id.rvSiteList);
        rvSitesList.setHasFixedSize(true);
        rvSitesList.setLayoutManager(new LinearLayoutManager(mContext));
        btnNext = findViewById(R.id.btnNext);
        cvSiteList = findViewById(R.id.cvSiteList);
        clSiteList = findViewById(R.id.clSiteList);
        tvSiteList = findViewById(R.id.tvSiteList);
        tvSiteError = findViewById(R.id.tvSiteError);
        constraintLayout6 = findViewById(R.id.constraintLayout6);

        //Camp Details
        llCampDetails = findViewById(R.id.llCampDetails);
        cvCampDetails = findViewById(R.id.cvCampDetails);
        tvCampDetails = findViewById(R.id.tvCampDetails);
        edt_district = findViewById(R.id.edt_district);
        edt_camp_address = findViewById(R.id.edt_camp_address);
        edt_camp_name = findViewById(R.id.edt_camp_name);
        edt_camp_date = findViewById(R.id.edt_camp_date);
        edt_post_camp_date = findViewById(R.id.edt_post_camp_date);
        edt_hospital = findViewById(R.id.edt_hospital);
        imv_camp_approval_letter = findViewById(R.id.imv_camp_approval_letter);
        btn_save = findViewById(R.id.btn_save);
        edt_expected_beneficiary = findViewById(R.id.edt_expected_beneficiary);
        edt_camp_organized_by = findViewById(R.id.edt_camp_organized_by);
        tvExpectedBeneficiary = findViewById(R.id.tvExpectedBeneficiary);
        edt_sel_address = findViewById(R.id.edt_sel_address);

        //Device Details
        rvDeviceList = findViewById(R.id.rvDeviceList);
        tvDeviceError = findViewById(R.id.tvDeviceError);
        progressBar = findViewById(R.id.progress_circular);
        rvDeviceList.setHasFixedSize(true);
        rvDeviceList.setLayoutManager(new LinearLayoutManager(mContext));
        cvDeviceAllocation = findViewById(R.id.cvDeviceAllocation);
        clDeviceAllocation = findViewById(R.id.clDeviceAllocation);
        tvDeviceDetails = findViewById(R.id.tvDeviceDetails);
        clDeviceHeader = findViewById(R.id.clDeviceHeader);
        clDeviceStatusButton = findViewById(R.id.clDeviceStatusButton);
        btnDeviceReject = findViewById(R.id.btnDeviceReject);
        btnDeviceHold = findViewById(R.id.btnDeviceHold);
        btnDeviceApprove = findViewById(R.id.btnDeviceApprove);
        textLayoutDevice = findViewById(R.id.textLayoutDevice);
        edtRemarkDevice = findViewById(R.id.edtRemarkDevice);

        //Consumable
        tvConsumable = findViewById(R.id.tvConsumable);
        cvConsumable = findViewById(R.id.cvConsumables);
        clConsumable = findViewById(R.id.clConsumable);
        cvConsumableHeader = findViewById(R.id.cvConsumableHeader);
        rvConsumableList = findViewById(R.id.rvConsumable);
        clConsumableStatusButton = findViewById(R.id.clConsumableStatusButton);
        tvError = findViewById(R.id.tvError);
        consumablesprogressBar = findViewById(R.id.progressBar);
        rvConsumableList.setHasFixedSize(true);
        rvConsumableList.setLayoutManager(new LinearLayoutManager(mContext));
        btnConsumableReject = findViewById(R.id.btnConsumableReject);
        btnConsumableHold = findViewById(R.id.btnConsumableHold);
        btnConsumableApprove = findViewById(R.id.btnConsumableApprove);
        textLayoutConsumable = findViewById(R.id.textLayoutConsumable);
        edtRemarkConsumable = findViewById(R.id.edtRemarkConsumable);


        //Resource
        rvDesignation = findViewById(R.id.rvResourceList);
        tvResourceError = findViewById(R.id.tvResourceError);
        resourceProgressBar = findViewById(R.id.resourceProgressBar);
        tvResource = findViewById(R.id.tvResource);
        cvResource = findViewById(R.id.cvResourceAllocation);
        clResource = findViewById(R.id.clResource);
        cvResourceHeader = findViewById(R.id.cvResourceHeader);
        clResourceStatusButton = findViewById(R.id.clResourceStatusButton);
        btnResourceReject = findViewById(R.id.btnResourceReject);
        btnResourceHold = findViewById(R.id.btnResourceHold);
        btnResourceApprove = findViewById(R.id.btnResourceApprove);
        textLayoutResource = findViewById(R.id.textLayoutResource);
        edtRemarkResource = findViewById(R.id.edtRemarkResource);


        rvDesignation.setHasFixedSize(true);
        rvDesignation.setLayoutManager(new LinearLayoutManager(mContext));

        btnFinalApproval = findViewById(R.id.btnFinalApprove);


    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Camp Approval");

        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getSessionDetails() {
        UserDetailsModel.Output userResponseModel = userSessionManager.getUserDetailsJson();
        if (getIntent() != null) {
            distLgdCode = getIntent().getIntExtra("campDistLgdCode", 0);
        }
        if (userResponseModel.getDesgid() == 84 || userResponseModel.getDesgid() == 100 || userResponseModel.getDesgid() == 102) {
            btnFinalApproval.setVisibility(View.VISIBLE);
        } else {
//            btnFinalApproval.setVisibility(View.GONE);
        }
        if (getIntent() != null) {
            campId = getIntent().getIntExtra("campId", 0);
            campType = getIntent().getStringExtra("campType");
            campDate = getIntent().getStringExtra("campDate");
            campStatus = getIntent().getIntExtra("campStatus", 0);
            isPartnerCamp = getIntent().getBooleanExtra("isPartnerCamp", false);
            if (campStatus == 1) {
                isDeviceApprove = campStatus;
                isConsumableApprove = campStatus;
                isResourceApprove = campStatus;

                btnConsumableApprove.setChecked(true);
                btnConsumableReject.setEnabled(false);
                btnConsumableHold.setEnabled(false);

                btnResourceApprove.setChecked(true);
                btnResourceReject.setEnabled(false);
                btnResourceHold.setEnabled(false);

                btnDeviceApprove.setChecked(true);
                btnDeviceReject.setEnabled(false);
                btnDeviceHold.setEnabled(false);

                edtRemarkDevice.setVisibility(View.GONE);
                edtRemarkConsumable.setVisibility(View.GONE);
                edtRemarkResource.setVisibility(View.GONE);

//                btnFinalApproval.setVisibility(View.GONE);
            }
            if (campType.equalsIgnoreCase("3")) {
                constraintLayout6.setVisibility(View.GONE);
            }
            getSites();

            setUpSearch();
        }


    }

    void toggleSiteSection() {
        if (!siteSectionVisibility) {
            getCampDetails();
        }

        siteSectionVisibility = !siteSectionVisibility;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                tvSiteList.setCompoundDrawablesWithIntrinsicBounds(null, null, siteSectionVisibility ? mContext.getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24) : mContext.getResources().getDrawable(R.drawable.icon_arrowdown), null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        clSiteList.setVisibility(siteSectionVisibility ? View.VISIBLE : View.GONE);
    }

    void toggleCampDetailsSection() {
        if (!campDetailsSectionVisibility) {
            getCampDetails();
        }

        campDetailsSectionVisibility = !campDetailsSectionVisibility;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                tvCampDetails.setCompoundDrawablesWithIntrinsicBounds(null, null, campDetailsSectionVisibility ? mContext.getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24) : mContext.getResources().getDrawable(R.drawable.icon_arrowdown), null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        llCampDetails.setVisibility(campDetailsSectionVisibility ? View.VISIBLE : View.GONE);

    }

    void toggleDeviceSection() {
        if (!deviceSectionVisibility) {
            getDevices();
        }

        deviceSectionVisibility = !deviceSectionVisibility;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                tvDeviceDetails.setCompoundDrawablesWithIntrinsicBounds(null, null, deviceSectionVisibility ? mContext.getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24) : mContext.getResources().getDrawable(R.drawable.icon_arrowdown), null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        clDeviceAllocation.setVisibility(deviceSectionVisibility ? View.VISIBLE : View.GONE);

    }

    void toggleConsumableSection() {
        if (!consumableSectionVisibility) {
            getConsumables();
        }

        consumableSectionVisibility = !consumableSectionVisibility;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                tvConsumable.setCompoundDrawablesWithIntrinsicBounds(null, null, consumableSectionVisibility ? mContext.getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24) : mContext.getResources().getDrawable(R.drawable.icon_arrowdown), null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        clConsumable.setVisibility(consumableSectionVisibility ? View.VISIBLE : View.GONE);

    }

    void toggleResourceSection() {
        if (!resourceSectionVisibility) {
            getResourcesForApproval();
        }

        resourceSectionVisibility = !resourceSectionVisibility;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                tvResource.setCompoundDrawablesWithIntrinsicBounds(null, null, resourceSectionVisibility ? mContext.getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24) : mContext.getResources().getDrawable(R.drawable.icon_arrowdown), null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        clResource.setVisibility(resourceSectionVisibility ? View.VISIBLE : View.GONE);

    }

    void clickEvent() {
        cvSiteList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSiteSection();
            }
        });
        cvCampDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleCampDetailsSection();
            }
        });
        cvDeviceAllocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleDeviceSection();
            }
        });
        cvConsumable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleConsumableSection();
            }
        });
        cvResource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleResourceSection();
            }
        });

        btnDeviceApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textLayoutDevice.setVisibility(View.GONE);
                isDeviceApprove = 1;

//                Utilities.showAlertDialog(mContext, "Confirm?", "Are you sure, you want to approve Devices", true, "Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//
//                    }
//                }, "No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        isDeviceApprove = 1;
//                        dialogInterface.dismiss();
//
//
//                    }
//                });
            }
        });

        btnDeviceReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textLayoutDevice.setVisibility(View.VISIBLE);

//                remarkDialog(0, 2);
                isDeviceApprove = 2;

            }
        });
        btnDeviceHold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textLayoutDevice.setVisibility(View.VISIBLE);

//                remarkDialog(0, 3);
                isDeviceApprove = 3;
            }
        });


        btnConsumableApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textLayoutConsumable.setVisibility(View.GONE);
                isConsumableApprove = 1;

//                Utilities.showAlertDialog(mContext, "Confirm?", "Are you sure, you want to approve Consumables", true, "Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//
//                    }
//                }, "No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        isConsumableApprove = 1;
//                        dialogInterface.dismiss();
//
//
//                    }
//                });
            }
        });
        btnConsumableReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                remarkDialog(1, 2);
                textLayoutConsumable.setVisibility(View.VISIBLE);

                isConsumableApprove = 2;
            }
        });
        btnConsumableHold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                remarkDialog(1, 3);
                textLayoutConsumable.setVisibility(View.VISIBLE);

                isConsumableApprove = 3;
            }
        });

        btnResourceApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textLayoutResource.setVisibility(View.GONE);
                isResourceApprove = 1;

//                Utilities.showAlertDialog(mContext, "Confirm?", "Are you sure, you want to approve Resources", true, "Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                }, "No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        isResourceApprove = 1;
//                        dialogInterface.dismiss();
//
//
//                    }
//                });
            }
        });
        btnResourceReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                remarkDialog(2, 2);
                textLayoutResource.setVisibility(View.VISIBLE);
                isResourceApprove = 2;

            }
        });
        btnResourceHold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                remarkDialog(2, 3);
                textLayoutResource.setVisibility(View.VISIBLE);
                isResourceApprove = 3;
            }
        });

        btnFinalApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (isDeviceApprove == 1 && isConsumableApprove == 1 && isResourceApprove == 1) {
//                    for (DeviceDetailsForApprovalModel.Output o :
//                            deviceListModel.getOutput()) {
//                        o.setRemark(edtRemarkDevice.getText().toString());
//                        o.setApproveStatus(isDeviceApprove);
//                        o.setCampId(campId);
//                        o.setModifiedBy(userSessionManager.getUserDetailsJson().getEmpCode());
//                    }
//
//                    for (ConsumableDetailsForApprovalModel.Output o :
//                            consumableDetailsForApprovalModel.getOutput()) {
//                        o.setRemark(edtRemarkConsumable.getText().toString());
//                        o.setApproveStatus(isConsumableApprove);
//                        o.setCampId(campId);
//                        o.setModifiedBy(userSessionManager.getUserDetailsJson().getEmpCode());
//                    }
//                    for (ResourceDetailsForApprovalModel.Output o :
//                            resourceDetailsForApprovalModel.getOutput()) {
//                        o.setRemark(edtRemarkResource.getText().toString());
//                        o.setApproveStatus(isResourceApprove);
//                        o.setCampId(campId);
//                        o.setModifiedBy(userSessionManager.getUserDetailsJson().getEmpCode());
//                    }
//
//
//                }


//                isDeviceApprove = 1;
                isConsumableApprove = 1;
                if (isResourceApprove == 1) {

                    for (int i = 0; i < designationListModel.getOutput().size(); i++) {

                        ResourceListForApprovalAdapter.DesignationListViewHolder myViewHolder = (ResourceListForApprovalAdapter.DesignationListViewHolder) rvDesignation.findViewHolderForAdapterPosition(i);

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

                    if (!campType.equalsIgnoreCase("3")) {


                        if (allSelResourceModel.size() != 0) {

                            for (ResourceDetailsForApprovalModel.Output o :
                                    resourceDetailsForApprovalModel.getOutput()) {
                                o.setRemark(edtRemarkResource.getText().toString());
                                o.setApproveStatus(isResourceApprove);
                                o.setCampId(campId);
                                o.setTestId(o.getTestId());
                                for (AllocatedResourceListModel.Output o1 : allSelResourceModel
                                ) {
                                    if (o1.getTestId() == o.getTestId()) {
                                        o.setResourceUserId(o1.getUserid());
                                    }

                                }

                                o.setModifiedBy(userSessionManager.getUserDetailsJson().getEmpCode());
                            }

                        } else {
                            Utilities.showToastMessage("Please select resources for camp", mContext, false);

                            return;
                        }
                    } else {
                        for (ResourceDetailsForApprovalModel.Output o :
                                resourceDetailsForApprovalModel.getOutput()) {
                            o.setRemark(edtRemarkResource.getText().toString());
                            o.setApproveStatus(isResourceApprove);
                            o.setCampId(campId);
                            o.setTestId(o.getTestId());
                            for (AllocatedResourceListModel.Output o1 : allSelResourceModel
                            ) {
                                if (o1.getTestId() == o.getTestId()) {
                                    o.setResourceUserId(o1.getUserid());
                                }

                            }

                            o.setModifiedBy(userSessionManager.getUserDetailsJson().getEmpCode());
                        }

                    }


                }


                if (isResourceApprove == 2 || isResourceApprove == 3) {
                    if (edtRemarkResource.getText().toString().isEmpty()) {
                        if (!resourceSectionVisibility) {
                            toggleResourceSection();
                        }

                        textLayoutResource.setErrorEnabled(true);
                        textLayoutResource.setError("Please enter remark");
                        return;
                    } else {
                        textLayoutResource.setErrorEnabled(false);
                        if (resourceDetailsForApprovalModel != null) {
                            for (ResourceDetailsForApprovalModel.Output o :
                                    resourceDetailsForApprovalModel.getOutput()) {
                                o.setRemark(edtRemarkResource.getText().toString());
                                o.setApproveStatus(isResourceApprove);
                                o.setCampId(campId);
                                o.setModifiedBy(userSessionManager.getUserDetailsJson().getEmpCode());

                            }
                        } else {
                            return;
                        }
                    }


                } else {


                        if (isDeviceApprove == 1) {
                            for (DeviceDetailsForApprovalModel.Output o :
                                    deviceListModel.getOutput()) {
                                o.setRemark(edtRemarkDevice.getText().toString());
                                o.setApproveStatus(isDeviceApprove);
                                o.setCampId(campId);
                                o.setModifiedBy(userSessionManager.getUserDetailsJson().getEmpCode());
                            }
                        }



//                    if (isConsumableApprove == 1) {
//                        for (ConsumableDetailsForApprovalModel.Output o :
//                                consumableDetailsForApprovalModel.getOutput()) {
//                            o.setRemark(edtRemarkConsumable.getText().toString());
//                            o.setApproveStatus(isConsumableApprove);
//                            o.setCampId(campId);
//                            o.setModifiedBy(userSessionManager.getUserDetailsJson().getEmpCode());
//                        }
//
//                    }

//                    if (isResourceApprove == 1) {
//
//
//                        for (ResourceDetailsForApprovalModel.Output o :
//                                resourceDetailsForApprovalModel.getOutput()) {
//                            o.setRemark(edtRemarkResource.getText().toString());
//                            o.setApproveStatus(isResourceApprove);
//                            o.setCampId(campId);
//                            o.setModifiedBy(userSessionManager.getUserDetailsJson().getEmpCode());
//                        }
//
//
//                    }


                   if (deviceListModel.getOutput()!=null){
                       if (isDeviceApprove == 2 || isDeviceApprove == 3  ) {
                           if (edtRemarkDevice.getText().toString().isEmpty()) {
                               if (!deviceSectionVisibility) {
                                   toggleDeviceSection();
                               }
                               textLayoutDevice.setErrorEnabled(true);
                               textLayoutDevice.setError("Please enter remark");
                               return;
                           } else {
                               textLayoutDevice.setErrorEnabled(false);
                               if (deviceListModel != null) {
                                   for (DeviceDetailsForApprovalModel.Output o :
                                           deviceListModel.getOutput()) {
                                       o.setRemark(edtRemarkDevice.getText().toString());
                                       o.setApproveStatus(isDeviceApprove);
                                       o.setCampId(campId);
                                       o.setModifiedBy(userSessionManager.getUserDetailsJson().getEmpCode());
                                   }
                               } else {
                                   return;
                               }

                           }
                       }

                   }
//                    if (isConsumableApprove == 2 || isConsumableApprove == 3) {
//                        if (edtRemarkConsumable.getText().toString().isEmpty()) {
//                            if (!consumableSectionVisibility) {
//                                toggleConsumableSection();
//                            }
//
//                            textLayoutConsumable.setErrorEnabled(true);
//                            textLayoutConsumable.setError("Please enter remark");
//                            return;
//                        } else {
//                            textLayoutConsumable.setErrorEnabled(false);
//                            if (consumableDetailsForApprovalModel != null) {
//                                for (ConsumableDetailsForApprovalModel.Output o :
//                                        consumableDetailsForApprovalModel.getOutput()) {
//                                    o.setRemark(edtRemarkConsumable.getText().toString());
//                                    o.setApproveStatus(isConsumableApprove);
//                                    o.setCampId(campId);
//                                    o.setModifiedBy(userSessionManager.getUserDetailsJson().getEmpCode());
//
//
//                                }
//                            } else {
//                                return;
//                            }
//                        }
//                    }
                    if (isResourceApprove == 2 || isResourceApprove == 3) {
                        if (edtRemarkResource.getText().toString().isEmpty()) {
                            if (!resourceSectionVisibility) {
                                toggleResourceSection();
                            }

                            textLayoutResource.setErrorEnabled(true);
                            textLayoutResource.setError("Please enter remark");
                            return;
                        } else {
                            textLayoutResource.setErrorEnabled(false);
                            if (resourceDetailsForApprovalModel != null) {
                                for (ResourceDetailsForApprovalModel.Output o :
                                        resourceDetailsForApprovalModel.getOutput()) {
                                    o.setRemark(edtRemarkResource.getText().toString());
                                    o.setApproveStatus(isResourceApprove);
                                    o.setCampId(campId);
                                    o.setModifiedBy(userSessionManager.getUserDetailsJson().getEmpCode());

                                }
                            } else {
                                return;
                            }
                        }
                    }
                }

                if (isDeviceApprove == 1 && isResourceApprove == 1) {
                    Utilities.showAlertDialog(mContext, "Confirm", "You are Approving this camp", true, "Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (!isPartnerCamp) {
                                submitData();
                            } else {
                                submitChannelPartnerData();
                            }
                            dialogInterface.dismiss();

                        }
                    }, "No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                } else if (isDeviceApprove == 2 || isConsumableApprove == 2 || isResourceApprove == 2) {
                    Utilities.showAlertDialog(mContext, "Confirm", "You are Rejecting this camp", false, "Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (!isPartnerCamp) {
                                submitData();
                            } else {
                                submitChannelPartnerData();
                            }
                            dialogInterface.dismiss();

                        }
                    }, "No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                } else {
                    Utilities.showAlertDialog(mContext, "Confirm", "You are putting this camp on Hold", false, "Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (!isPartnerCamp) {
                                submitData();
                            } else {
                                submitChannelPartnerData();
                            }
                            dialogInterface.dismiss();

                        }
                    }, "No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                }


            }
        });


    }

    void validateInput() {

    }

    void submitData() {

        Gson gson = new GsonBuilder().serializeNulls().addSerializationExclusionStrategy(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {

                Log.i(TAG, "shouldSkipField: " + f.getName());
                if (f.getName().equalsIgnoreCase("deviceName") || f.getName().equalsIgnoreCase("deviceSerialNumbers")
                        || f.getName().equalsIgnoreCase("devicecount") || f.getName().equalsIgnoreCase("") ||
                        f.getName().equalsIgnoreCase("expectedQuantity") || f.getName().equalsIgnoreCase("productName")
                        || f.getName().equalsIgnoreCase("requiredQuantity") || f.getName().equalsIgnoreCase("stockAsPerPhelbo")
                        || f.getName().equalsIgnoreCase("desgName") || f.getName().equalsIgnoreCase("AVAILABELSTOCK") ||
                        f.getName().equalsIgnoreCase("resourceName") || f.getName().equalsIgnoreCase("testId") || f.getName().equalsIgnoreCase("testName") || f.getName().equalsIgnoreCase("RequiredDevice") || f.getName().equalsIgnoreCase("resources") || f.getName().equalsIgnoreCase("subDevices")

                ) {
                    return true;

                } else
                    return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        }).create();


//        JsonArray teamUserJsonArray = new JsonArray();
//        for (AllocatedResourceListModel.Output t :
//                allSelResourceModel) {
//            if (t.isChecked()) {
//                JsonObject jsonObject = new JsonObject();
//                jsonObject.addProperty("ApproveStatus", isResourceApprove);
//                jsonObject.addProperty("CampId", campId);
//                jsonObject.addProperty("ModifiedBy",userSessionManager.getUserDetailsJson().getEmpCode());
//                jsonObject.addProperty("Remark", edtRemarkResource.getText().toString());
//                jsonObject.addProperty("ResourceUserId", t.getUserid());
//                teamUserJsonArray.add(jsonObject);
//
//            }
//        }

        Log.i(TAG, "Device: " + gson.toJson(deviceListModel.getOutput()));
//        Log.i(TAG, "Consumable: " + gson.toJson(consumableDetailsForApprovalModel.getOutput()));
        Log.i(TAG, "Resource: " + gson.toJson(resourceDetailsForApprovalModel.getOutput()));
//        String consumableJson = gson.toJson(consumableDetailsForApprovalModel.getOutput());


        String deviceJson = gson.toJson(deviceListModel.getOutput());
        String resourceJson = gson.toJson(resourceDetailsForApprovalModel.getOutput());

        String deviceJsonNew = gson.toJson(allSelSubDeviceListModel);

        String resourceJsonNew = gson.toJson(allSelResourceModel);


        progressDialog.show();
        apiService.insertInternalCampApproval(deviceJson, resourceJson).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();

                try {
//                    Log.d(TAG, "onResponse: " + response.body().string());

                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        JSONObject jsonObject = new JSONObject(res);
                        String status = jsonObject.getString("status");
//                        {"status":"Success","message":"1"}
                        if (status.equalsIgnoreCase("Success")) {
                            Utilities.showToastMessage("Status submitted successfully", mContext, true);
                            setResult(1);
                            finish();
//                            Gson gson = new GsonBuilder().addSerializationExclusionStrategy(new ExclusionStrategy() {
//                                        @Override
//                                        public boolean shouldSkipField(FieldAttributes f) {
//                                            if (f.getName().equalsIgnoreCase("desgid") || f.getName().equalsIgnoreCase("AVAILABELSTOCK") || f.getName().equalsIgnoreCase("Expectedbeneficiarycount")) {
//                                                return true;
//                                            }
//                                            return false;
//                                        }
//
//                                        @Override
//                                        public boolean shouldSkipClass(Class<?> clazz) {
//                                            return false;
//                                        }
//                                    })
////                                    .excludeFieldsWithoutExposeAnnotation()
//                                    .serializeNulls()
//                                    .create();

                            String consumableMappingJson = gson.toJson(consumableDetailsForApprovalModel.getOutput());
                            String deviceMappingJson = gson.toJson(deviceListModel.getOutput());
                            String resourceMappingJson = gson.toJson(resourceDetailsForApprovalModel.getOutput());

//                            apiService.updateCampCreationResourceDeviceConsumptionDetails(deviceMappingJson, consumableMappingJson, resourceMappingJson, userId, String.valueOf(campId)).enqueue(new Callback<ResponseBody>() {
//                                @Override
//                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                                    try {
//                                        Log.d(TAG, "onResponse: " + response.body().string());
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                    if (response.isSuccessful()) {
//                                        setResult(1);
//                                        finish();
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                                    Log.e(TAG, "updateCampCreation fail: " + t.getMessage());
//                                }
//                            });


                        } else {
                            String msg = null;
                            try {
                                msg = jsonObject.getString("ExceptionValue");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Utilities.showToastMessage(msg, mContext, false);

                        }


                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, "onFailure: " + t.getMessage());

            }
        });
    }

    void submitChannelPartnerData() {

        Gson gson = new GsonBuilder().serializeNulls().addSerializationExclusionStrategy(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {

                Log.i(TAG, "shouldSkipField: " + f.getName());
                if (f.getName().equalsIgnoreCase("deviceName") || f.getName().equalsIgnoreCase("deviceSerialNumbers")
                        || f.getName().equalsIgnoreCase("devicecount") || f.getName().equalsIgnoreCase("") ||
                        f.getName().equalsIgnoreCase("expectedQuantity") || f.getName().equalsIgnoreCase("productName")
                        || f.getName().equalsIgnoreCase("requiredQuantity") || f.getName().equalsIgnoreCase("stockAsPerPhelbo")
                        || f.getName().equalsIgnoreCase("desgName") || f.getName().equalsIgnoreCase("AVAILABELSTOCK") ||
                        f.getName().equalsIgnoreCase("resourceName") || f.getName().equalsIgnoreCase("testName") || f.getName().equalsIgnoreCase("RequiredDevice") || f.getName().equalsIgnoreCase("resources") || f.getName().equalsIgnoreCase("subDevices")

                ) {
                    return true;

                } else
                    return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        }).create();

//        Log.i(TAG, "Device: " + gson.toJson(deviceListModel.getOutput()));
//        Log.i(TAG, "Consumable: " + gson.toJson(consumableDetailsForApprovalModel.getOutput()));
        Log.i(TAG, "Resource: " + gson.toJson(resourceDetailsForApprovalModel.getOutput()));
//        String deviceJson = gson.toJson(deviceListModel.getOutput());
//        String consumableJson = gson.toJson(consumableDetailsForApprovalModel.getOutput());
        String resourceJson = gson.toJson(resourceDetailsForApprovalModel.getOutput());
        progressDialog.show();
        apiService.insertInternalCampApprovalForPartner("[]", "[]", resourceJson).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();

                try {
//                    Log.d(TAG, "onResponse: " + response.body().string());

                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        JSONObject jsonObject = new JSONObject(res);
                        String status = jsonObject.getString("status");
//                        {"status":"Success","message":"1"}
                        if (status.equalsIgnoreCase("Success")) {
                            Utilities.showToastMessage("Status submitted successfully", mContext, true);
                            setResult(1);
                            finish();
//                            Gson gson = new GsonBuilder().addSerializationExclusionStrategy(new ExclusionStrategy() {
//                                        @Override
//                                        public boolean shouldSkipField(FieldAttributes f) {
//                                            if (f.getName().equalsIgnoreCase("desgid") || f.getName().equalsIgnoreCase("AVAILABELSTOCK") || f.getName().equalsIgnoreCase("Expectedbeneficiarycount")) {
//                                                return true;
//                                            }
//                                            return false;
//                                        }
//
//                                        @Override
//                                        public boolean shouldSkipClass(Class<?> clazz) {
//                                            return false;
//                                        }
//                                    })
////                                    .excludeFieldsWithoutExposeAnnotation()
//                                    .serializeNulls()
//                                    .create();

//                            String consumableMappingJson = gson.toJson(consumableDetailsForApprovalModel.getOutput());
//                            String deviceMappingJson = gson.toJson(deviceListModel.getOutput());
//                            String resourceMappingJson = gson.toJson(resourceDetailsForApprovalModel.getOutput());

//                            apiService.updateCampCreationResourceDeviceConsumptionDetails(deviceMappingJson, consumableMappingJson, resourceMappingJson, userId, String.valueOf(campId)).enqueue(new Callback<ResponseBody>() {
//                                @Override
//                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                                    try {
//                                        Log.d(TAG, "onResponse: " + response.body().string());
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                    if (response.isSuccessful()) {
//                                        setResult(1);
//                                        finish();
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                                    Log.e(TAG, "updateCampCreation fail: " + t.getMessage());
//                                }
//                            });


                        } else {
                            String msg = null;
                            try {
                                msg = jsonObject.getString("ExceptionValue");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Utilities.showToastMessage(msg, mContext, false);

                        }


                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    Utilities.showToastMessage("Unable to update camp status", mContext, false);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, "onFailure: " + t.getMessage());
                Utilities.showToastMessage("Unable to update camp status", mContext, false);


            }
        });
    }


    void getSites() {
        apiService.getCampSiteDetails_ForAppForIntApproval(campId).enqueue(new Callback<SiteListModel>() {
            @Override
            public void onResponse(Call<SiteListModel> call, Response<SiteListModel> response) {

                Log.i(TAG, "onResponse: " + response);
                if (response.isSuccessful()) {
                    siteListModel = response.body();
                    if (siteListModel.getStatus().equalsIgnoreCase("success")) {
                        tvSiteError.setVisibility(View.GONE);
                        if (!siteListModel.getOutput().isEmpty()) {
                            Collections.sort(siteListModel.getOutput(), (o1, o2) -> o1.getSiteName().toLowerCase().compareTo(o2.getSiteName().toLowerCase()));
                            rvSitesList.setAdapter(new SiteListAdapter(siteListModel.getOutput(), CampApprovalActivity.this::onSiteSelected, 1));

                        } else {
                            Utilities.showToastMessage("No sites available", mContext, false);
                            tvSiteError.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Utilities.showToastMessage("No sites available", mContext, false);
                        tvSiteError.setVisibility(View.VISIBLE);

                    }
                }
            }

            @Override
            public void onFailure(Call<SiteListModel> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                tvSiteError.setVisibility(View.VISIBLE);

            }
        });
    }

    void setUpSearch() {
        siteSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchList = new ArrayList<>();
                if (!query.isEmpty()) {
                    for (SiteListModel.Output site :
                            siteListModel.getOutput()) {
                        if (site.getSiteName().toLowerCase().contains(query.toLowerCase())) {
                            searchList.add(site);
                        }
                    }
                    Log.i(TAG, "onQueryTextSubmit: " + searchList.size());

                    if (searchList.size() > 0) {
                        rvSitesList.setAdapter(new SiteListAdapter(searchList, CampApprovalActivity.this::onSiteSelected, 1));

                    } else {
                        rvSitesList.setAdapter(new SiteListAdapter(siteListModel.getOutput(), CampApprovalActivity.this::onSiteSelected, 1));

                    }
                } else {
                    if (siteListModel != null)
                        rvSitesList.setAdapter(new SiteListAdapter(siteListModel.getOutput(), CampApprovalActivity.this::onSiteSelected, 1));

                }
                return true;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList = new ArrayList<>();
                if (!newText.isEmpty()) {
                    for (SiteListModel.Output site :
                            siteListModel.getOutput()) {
                        if (site.getSiteName().toLowerCase().contains(newText.toLowerCase())) {
                            searchList.add(site);
                        }

                    }
                    Log.i(TAG, "onQueryTextChange: " + searchList.size());
                    if (searchList.size() > 0) {
                        rvSitesList.setAdapter(new SiteListAdapter(searchList, CampApprovalActivity.this::onSiteSelected, 1));

                    } else {
                        rvSitesList.setAdapter(new SiteListAdapter(siteListModel.getOutput(), CampApprovalActivity.this::onSiteSelected, 1));

                    }
                } else {
                    if (siteListModel != null)
                        rvSitesList.setAdapter(new SiteListAdapter(siteListModel.getOutput(), CampApprovalActivity.this::onSiteSelected, 1));

                }

                return true;
            }
        });

    }

    //Camp Details
    void getCampDetails() {
        progressDialog.show();
        apiService.getCampDetails_ForAppForIntApproval(campId).enqueue(new Callback<CampDetailsModel>() {
            @Override
            public void onResponse(Call<CampDetailsModel> call, Response<CampDetailsModel> response) {
                progressDialog.dismiss();

                if (response.isSuccessful()) {
                    if (response.body().getStatus().equalsIgnoreCase("success")) {

                        campDetailsModel = response.body().getOutput().get(0);
                        edt_district.setText(campDetailsModel.getDistrict());
                        edt_camp_name.setText(campDetailsModel.getCampName());
                        edt_camp_address.setText(campDetailsModel.getCampLocation());
                        Calendar c = Calendar.getInstance();
                        try {
                            c.setTime(Utilities.dfDate.parse(campDetailsModel.getCampDate()));
                            c.add(Calendar.DAY_OF_MONTH, 7);
                            edt_camp_date.setText(Utilities.dfDate2.format(Utilities.dfDate.parse(campDetailsModel.getCampDate())));
                            edt_post_camp_date.setText(Utilities.dfDate2.format(c.getTimeInMillis()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        tvExpectedBeneficiary.setText(String.valueOf("Expected Beneficiary (" + campDetailsModel.getExpectedbeneficiarycount() + ")"));
                    } else {
                        toggleCampDetailsSection();
                        Utilities.showToastMessage("Camp Details not found", mContext, false);
                    }
                }
            }

            @Override
            public void onFailure(Call<CampDetailsModel> call, Throwable t) {
                toggleCampDetailsSection();

                progressDialog.dismiss();
                Utilities.showToastMessage("Server not responding", mContext, false);

            }
        });
    }

    //Device Details
    void getDevices() {
        progressBar.setVisibility(View.VISIBLE);
        rvDeviceList.setVisibility(View.GONE);
        clDeviceHeader.setVisibility(View.GONE);
        clDeviceStatusButton.setVisibility(View.GONE);

        apiService.getCampDeviceDetails_ForAppForIntApproval(campId).enqueue(new Callback<DeviceDetailsForApprovalModel>() {
            @Override
            public void onResponse(Call<DeviceDetailsForApprovalModel> call, Response<DeviceDetailsForApprovalModel> response) {
                progressBar.setVisibility(View.GONE);
                Log.i(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    deviceListModel = response.body();
                    if (deviceListModel.getStatus().equalsIgnoreCase("success")) {

                        if (deviceListModel.getOutput().size() > 0) {
                            Collections.sort(deviceListModel.getOutput(), (o1, o2) -> o1.getDeviceName().compareTo(o2.getDeviceName()));
                            deviceListAdapter = new DeviceListAdapter(deviceListModel.getOutput(), CampApprovalActivity.this, 1);
                            rvDeviceList.setAdapter(deviceListAdapter);
                            rvDeviceList.setVisibility(View.VISIBLE);
                            clDeviceHeader.setVisibility(View.VISIBLE);
                            clDeviceStatusButton.setVisibility(View.VISIBLE);


                        } else {
                            tvDeviceError.setVisibility(View.VISIBLE);
                        }
                    } else {
                        tvDeviceError.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<DeviceDetailsForApprovalModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                tvDeviceError.setVisibility(View.VISIBLE);

                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    //Consumable
    void getConsumables() {
        consumablesprogressBar.setVisibility(View.VISIBLE);
        rvConsumableList.setVisibility(View.GONE);
        cvConsumableHeader.setVisibility(View.GONE);
        clConsumableStatusButton.setVisibility(View.GONE);
        apiService.getCampConsumptionDetails_ForAppForIntApproval(campId).enqueue(new Callback<ConsumableDetailsForApprovalModel>() {
            @Override
            public void onResponse(Call<ConsumableDetailsForApprovalModel> call, Response<ConsumableDetailsForApprovalModel> response) {
                consumablesprogressBar.setVisibility(View.GONE);
                consumableDetailsForApprovalModel = response.body();
                if (response.isSuccessful()) {
                    consumableListModel = response.body();
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        if (response.body().getOutput().size() > 0) {
//                            rvConsumableList.setAdapter(new ConsumableListAdapter(response.body().getOutput(), 1));
                            rvConsumableList.setVisibility(View.VISIBLE);
                            cvConsumableHeader.setVisibility(View.VISIBLE);
                            clConsumableStatusButton.setVisibility(View.VISIBLE);
                            tvError.setVisibility(View.GONE);

                            getLabOnDistrict(distLgdCode);

                        }

                    } else {
                        tvError.setText("Consumables not found");
                        tvError.setVisibility(View.VISIBLE);


                    }
                }
            }

            @Override
            public void onFailure(Call<ConsumableDetailsForApprovalModel> call, Throwable t) {
                consumablesprogressBar.setVisibility(View.GONE);
                tvError.setText("Server not responding");
                tvError.setVisibility(View.VISIBLE);


            }
        });
    }

    void getLabOnDistrict(int distLgdCode) {

        apiService.getLabOnDistrict(distLgdCode).enqueue(new Callback<GetLabOnDistrictListModel>() {
            @Override
            public void onResponse(Call<GetLabOnDistrictListModel> call, Response<GetLabOnDistrictListModel> response) {
                Log.d(TAG, "onResponse: " + response.body().getStatus());

                try {
                    if (response.isSuccessful()) {
                        if (response.body().getOutput() != null && response.body().getOutput().size() > 0) {
                            getConsumablesDetailsFromMediProcs("5001");
                        } else {
                            Utilities.showAlertDialog(mContext, "Lab not found", "Lab not mapped for this district", false, "Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ((CampApprovalActivity) mContext).finish();
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
        mediProcAPI.getLabProductStockListByLab(labcode).enqueue(new Callback<ProductStockListModel>() {
            @Override
            public void onResponse(Call<ProductStockListModel> call, Response<ProductStockListModel> response) {
                progressBar.setVisibility(View.GONE);
                rvConsumableList.setVisibility(View.VISIBLE);
                Log.i(TAG, "onResponse: " + response);
                if (response.isSuccessful()) {
                    productStockListModel = response.body();
                    if (productStockListModel.getStatus().equalsIgnoreCase("success")) {
                        if (consumableListModel.getOutput().size() > 0) {
                            for (ConsumableDetailsForApprovalModel.Output c :
                                    consumableListModel.getOutput()) {
                                for (ProductStockListModel.Output p :
                                        productStockListModel.getOutput()) {
                                    if (Objects.equals(c.getProductid(), p.getProductId())) {
                                        c.setAvailabelstock(p.getAvailabelstock());
                                    }
                                }
                            }
                            Collections.sort(consumableListModel.getOutput(), (o1, o2) -> o1.getProductName().compareTo(o2.getProductName()));
                            consumableListAdapter = new ConsumableListAdapter(consumableListModel.getOutput(), 1);
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


    void getResourcesForApproval() {
        resourceProgressBar.setVisibility(View.VISIBLE);
        rvDesignation.setVisibility(View.GONE);
        cvResourceHeader.setVisibility(View.GONE);
        clResourceStatusButton.setVisibility(View.GONE);
        apiService.getCampResourceDetails_ForAppForIntApproval(campId).enqueue(new Callback<ResourceDetailsForApprovalModel>() {
            @Override
            public void onResponse(Call<ResourceDetailsForApprovalModel> call, Response<ResourceDetailsForApprovalModel> response) {
                resourceProgressBar.setVisibility(View.GONE);
                resourceDetailsForApprovalModel = response.body();
                Log.i(TAG, "onResponse: " + response);
                if (response.isSuccessful()) {
                    designationListModel = response.body();
                    if (designationListModel.getStatus().equalsIgnoreCase("success")) {
                        if (designationListModel.getOutput().size() > 0) {
                            rvDesignation.setVisibility(View.VISIBLE);
                            cvResourceHeader.setVisibility(View.VISIBLE);
                            clResourceStatusButton.setVisibility(View.VISIBLE);
                            tvResourceError.setVisibility(View.GONE);
                            if (isPartnerCamp && campStatus != 1 && !campType.equalsIgnoreCase("3")) {
                                ResourceDetailsForApprovalModel resourceDetailsForApprovalModel1 = new ResourceDetailsForApprovalModel();
                                designationListModel.getOutput().add(designationListModel.getOutput().size() - 1, resourceDetailsForApprovalModel1.new Output(designationListModel.getOutput().get(0).getCampId(), 3, designationListModel.getOutput().get(0).getDesgName(), 0, "", "", 0, 0, "Physical Examination", new ArrayList<>()));
                            }
                            Collections.sort(designationListModel.getOutput(), (o1, o2) -> o1.getResourceName().compareTo(o2.getResourceName()));
                            resourceListAdapter = new ResourceListForApprovalAdapter(designationListModel.getOutput(), CampApprovalActivity.this);
                            rvDesignation.setAdapter(resourceListAdapter);

                        } else {
                            tvResourceError.setVisibility(View.VISIBLE);
                        }
                    } else {
                        tvResourceError.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResourceDetailsForApprovalModel> call, Throwable t) {
                resourceProgressBar.setVisibility(View.GONE);
                tvResourceError.setVisibility(View.VISIBLE);

                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    /**
     * @param type   0=Device,1=Consumable
     * @param status Approve=1,Reject=2,Hold=3
     */
    void remarkDialog(int type, int status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_with_edittext, null, false);
        builder.setTitle("Remark");
        builder.setCancelable(false);
        builder.setView(dialogView);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnSubmit = dialogView.findViewById(R.id.btnSubmit);
        TextInputEditText edtRemark = dialogView.findViewById(R.id.edtReason);
        TextInputLayout remarkLayout = dialogView.findViewById(R.id.textInputLayout);

        AlertDialog alertDialog = builder.create();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!edtRemark.getText().toString().isEmpty()) {
                    remarkLayout.setErrorEnabled(false);
                    if (type == 0) {
                        deviceRemark = edtRemark.getText().toString();
                        isDeviceApprove = status;
                    } else if (type == 1) {
                        isConsumableApprove = status;
                        consumableRemark = edtRemark.getText().toString();
                    } else {
                        isResourceApprove = status;
                        resourceRemark = edtRemark.getText().toString();
                    }
                } else {
                    remarkLayout.setErrorEnabled(true);
                    remarkLayout.setError("Please enter remark");
                }

                alertDialog.dismiss();

            }
        });

        alertDialog.show();

    }


    @Override
    public void onSiteSelected(SiteListModel.Output output) {

    }

    @Override
    public void onDeviceSelected(DeviceListModel.Output output) {
    }

    @Override
    public void onApproveDeviceSelected(DeviceDetailsForApprovalModel.Output output) {
        getSubDevices(output);
    }

    void getSubDevices(DeviceDetailsForApprovalModel.Output device) {
        if (device.getSubDevices() == null) {

            apiService.getApproveSubDeviceListForCampAllo(device.getDevicesId(), campDate, String.valueOf(campId)).enqueue(new Callback<AllocatedSubDeviceListModel>() {
                @Override
                public void onResponse(Call<AllocatedSubDeviceListModel> call, Response<AllocatedSubDeviceListModel> response) {

                    Log.i(TAG, "onResponse: " + response);
                    if (response.isSuccessful()) {
                        subDeviceListModel = response.body();
                        if (subDeviceListModel.getStatus().equalsIgnoreCase("success")) {
                            if (subDeviceListModel.getOutput().size() > 0) {
                                Collections.sort(subDeviceListModel.getOutput(), (o1, o2) -> o1.getDeviceCompName().compareTo(o2.getDeviceCompName()));
                                showSubDeviceDialog(device);
                            }
                        } else {
                            Utilities.showToastMessage("Devices not available", mContext, false);
                        }
                    }
                }

                @Override
                public void onFailure(Call<AllocatedSubDeviceListModel> call, Throwable t) {

                    Log.e(TAG, "onFailure: " + t.getMessage());
                }
            });
        } else {
            subDeviceListModel.setOutput(device.getSubDevices());
            showSubDeviceDialog(device);

        }
    }


    private void showSubDeviceDialog(DeviceDetailsForApprovalModel.Output device) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_check_list, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(view);
        builder.setTitle("Select Devices");
        builder.setCancelable(false);

        RecyclerView rv_checklist = view.findViewById(R.id.rv_checklist);
        rv_checklist.setLayoutManager(new LinearLayoutManager(mContext));
        rv_checklist.setAdapter(new SubDeviceListAdapter());


        AlertDialog alertDialog = builder.create();
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Select", (dialog, which) -> {


            int checkedTestCount = 0;
            StringBuilder selectedSubCategories = new StringBuilder();
            ArrayList<AllocatedSubDeviceListModel.Output> selSubDeviceListModel = new ArrayList<>();
            selSubDeviceListModel.addAll(subDeviceListModel.getOutput());

            for (AllocatedSubDeviceListModel.Output output : subDeviceListModel.getOutput()) {

                if (output.isChecked() || output.getDeviceStatus().equalsIgnoreCase("Selected")) {
                    checkedTestCount = checkedTestCount + 1;
                    if (!allSelSubDeviceListModel.contains(output)) {
                        allSelSubDeviceListModel.add(output);
                    }
                } else {
                    if (allSelSubDeviceListModel.contains(output)) {
                        allSelSubDeviceListModel.remove(output);
                    }
                }
            }


            if (checkedTestCount < device.getDevicecount()) {
                Utilities.showToastMessage("Please select " + device.getDevicecount() + " for this camp", mContext, false);
            }
            device.setSubDevices(selSubDeviceListModel);
            rvDeviceList.setAdapter(deviceListAdapter);
//            deviceListAdapter.notifyDataSetChanged();


        });

        alertDialog.show();

    }

    private class SubDeviceListAdapter extends RecyclerView.Adapter<SubDeviceListAdapter.MyViewHolder> {

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.sub_device_list_item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int pos) {
            final int position = holder.getAdapterPosition();

            holder.cb_select.setText(subDeviceListModel.getOutput().get(position).getDeviceCompName());
            holder.tvSerialNumber.setText("Serial Number:- " + subDeviceListModel.getOutput().get(position).getDeviceSerial());

            if (subDeviceListModel.getOutput().get(position).isChecked() || subDeviceListModel.getOutput().get(position).getDeviceStatus().equalsIgnoreCase("Selected")) {
                holder.cb_select.setChecked(true);
            }

            holder.cb_select.setOnCheckedChangeListener((buttonView, isChecked) -> {
                subDeviceListModel.getOutput().get(position).setChecked(isChecked);
                subDeviceListModel.getOutput().get(position).setDeviceStatus(isChecked ? "Selected" : "Not Selected");
            });
        }

        @Override
        public int getItemCount() {
            return subDeviceListModel.getOutput().size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private CheckBox cb_select;
            private TextView tvSerialNumber;

            public MyViewHolder(@NonNull View view) {
                super(view);
                cb_select = view.findViewById(R.id.cb_select);
                tvSerialNumber = view.findViewById(R.id.tvSerialNumber);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }


    /**
     * For Resources
     */
    void getResources(ResourceDetailsForApprovalModel.Output designation) {
        if (designation.getResources() == null || designation.getResources().size() == 0) {
            apiService.getApproveResourcelist(designation.getTestId(), campDate, String.valueOf(campId), distLgdCode).enqueue(new Callback<AllocatedResourceListModel>() {
                @Override
                public void onResponse(Call<AllocatedResourceListModel> call, Response<AllocatedResourceListModel> response) {

                    Log.i(TAG, "onResponse: " + response);
                    if (response.isSuccessful()) {
                        allocatedResourceListModel = response.body();
                        if (allocatedResourceListModel.getStatus().equalsIgnoreCase("success")) {
                            if (allocatedResourceListModel.getOutput().size() > 0) {
                                Collections.sort(allocatedResourceListModel.getOutput(), (o1, o2) -> o1.getResourceName().compareTo(o2.getResourceName()));
                                showResourcesDialog(designation);
                            }
                        } else {
                            Utilities.showToastMessage("Resources not available", mContext, false);
                        }
                    }
                }

                @Override
                public void onFailure(Call<AllocatedResourceListModel> call, Throwable t) {

                    Log.e(TAG, "onFailure: " + t.getMessage());
                }
            });
        } else {
            allocatedResourceListModel.setOutput(designation.getResources());
            showResourcesDialog(designation);

        }
    }


    private void showResourcesDialog(ResourceDetailsForApprovalModel.Output test) {
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
            ArrayList<AllocatedResourceListModel.Output> selResourceListModel = new ArrayList<>();
            selResourceListModel.addAll(allocatedResourceListModel.getOutput());

            for (AllocatedResourceListModel.Output output : allocatedResourceListModel.getOutput()) {

                if (output.isChecked() || output.getResStatus().equalsIgnoreCase("Selected")) {
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
            rvDesignation.setAdapter(resourceListAdapter);

//            deviceListAdapter.notifyDataSetChanged();


        });

        builder.create().show();
    }


    @Override
    public void onDesignationSelected(ResourceDetailsForApprovalModel.Output output) {
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

            holder.cb_select.setText(allocatedResourceListModel.getOutput().get(position).getResourceName());

            if (allocatedResourceListModel.getOutput().get(position).isChecked() || allocatedResourceListModel.getOutput().get(position).getResStatus().equalsIgnoreCase("Selected")) {
                holder.cb_select.setChecked(true);
            } else {
                holder.cb_select.setChecked(false);

            }

            holder.cb_select.setOnCheckedChangeListener((buttonView, isChecked) -> {
                allocatedResourceListModel.getOutput().get(position).setChecked(isChecked);
                allocatedResourceListModel.getOutput().get(position).setResStatus(isChecked ? "Selected" : "Not Selected");
            });
        }

        @Override
        public int getItemCount() {
            return allocatedResourceListModel.getOutput().size();
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