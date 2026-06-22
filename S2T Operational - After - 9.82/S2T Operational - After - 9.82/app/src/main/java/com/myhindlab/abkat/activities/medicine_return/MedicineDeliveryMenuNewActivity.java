package com.myhindlab.abkat.activities.medicine_return;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myhindlab.abkat.BuildConfig;
import com.myhindlab.abkat.activities.AcceptMedicinePacketActivity;
import com.myhindlab.abkat.activities.Availability_Activity;
import com.myhindlab.abkat.activities.CampAwarenessCampList_Activity;
import com.myhindlab.abkat.activities.CampCalendar_Activity;
import com.myhindlab.abkat.activities.CampDetails_Activity;
import com.myhindlab.abkat.activities.CampRequestActivity;
import com.myhindlab.abkat.activities.CampSelectForAcknowledgement_Activity;
import com.myhindlab.abkat.activities.CampSelectionForBreastScreeing_Activity;
import com.myhindlab.abkat.activities.CampSelectionForCampClosingList_Activity;
import com.myhindlab.abkat.activities.CampSelectionForHealthScreeing_Activity;
import com.myhindlab.abkat.activities.CampSelectionForInstantPatientRegistration_Activity;
import com.myhindlab.abkat.activities.CreateCamp_Activity_v3;
import com.myhindlab.abkat.activities.DashboardAvailabilityReportDistrict_Activty;
import com.myhindlab.abkat.activities.DashboardInvoice_Activity;
import com.myhindlab.abkat.activities.DashboardPatientApp;
import com.myhindlab.abkat.activities.ELearning;
import com.myhindlab.abkat.activities.MedicineDeliveryActivity;
import com.myhindlab.abkat.activities.PacketCollectionActivity;
import com.myhindlab.abkat.activities.PacketReceiveActivity;
import com.myhindlab.abkat.activities.PayoutDetails;
import com.myhindlab.abkat.activities.PostCampCounts_Activity;
import com.myhindlab.abkat.activities.PowerBIDashboard;
import com.myhindlab.abkat.activities.ReportDeliveryActivity;
import com.myhindlab.abkat.activities.ResourceList_Activity;
import com.myhindlab.abkat.activities.SampleProcessingStatus_Activity;
import com.myhindlab.abkat.activities.SetPostCampTarget_Activity;
import com.myhindlab.abkat.activities.SiteSurveyRequestList_Actvity;
import com.myhindlab.abkat.activities.SiteSurvey_BuildingConstDetails_Activity;
import com.myhindlab.abkat.activities.SiteSurvey_Menu_Activity;
import com.myhindlab.abkat.activities.SiteSurvey_RERASiteList_Activity;
import com.myhindlab.abkat.activities.TeamCampMappingActivity;
import com.myhindlab.abkat.activities.UnregWorkerSiteForWorkerNo_Activity;
import com.myhindlab.abkat.activities.UnregisteredWorkerSiteSelection_Activity;
import com.myhindlab.abkat.activities.UpdateProcessLabActivity;
import com.myhindlab.abkat.activities.UploadFingerPrint_Activity;
import com.myhindlab.abkat.activities.WorkerSiteMappingSiteSelection_Activity;
import com.myhindlab.abkat.activities.campApproval.CampSelectionForCampUpdate_Activity;
import com.myhindlab.abkat.activities.campredinessnew.CampredinessNewActivity;
import com.myhindlab.abkat.activities.confirmatoryTest.CTAndedicineCommonBeneficiaryListActivity;
import com.myhindlab.abkat.activities.confirmatoryTest.ConfirmatoryTestActivity;
import com.myhindlab.abkat.activities.confirmatoryTest.ConfirmatoryTestAssignTeamActivity;
import com.myhindlab.abkat.activities.couriermodule.AcceptSamplesInLabActivity;
import com.myhindlab.abkat.activities.couriermodule.CentriFugeConfirmationActivity;
import com.myhindlab.abkat.activities.couriermodule.Courier_Activity;
import com.myhindlab.abkat.activities.doortodoor.CallToDoctorRequestActivity;
import com.myhindlab.abkat.activities.doortodoor.D2DAvailabilityActivity;
import com.myhindlab.abkat.activities.doortodoor.D2DSelectCampActivity;
import com.myhindlab.abkat.activities.doortodoor.D2dPhysicalExaminationDetailsActivity;
import com.myhindlab.abkat.activities.doortodoor.D2dTeamsActivity;
import com.myhindlab.abkat.activities.doortodoor.D2dTeamsNotWorkingTeamActivity;
import com.myhindlab.abkat.activities.doortodoor.ExpectectedBeneficiaryForTeamsActivity;
import com.myhindlab.abkat.activities.doortodoor.d2d_camp_activity.CampSelectionForD2DActivity_Activity;
import com.myhindlab.abkat.activities.liver_scanning_dashboard.DashboardPatientAppNew;
import com.myhindlab.abkat.activities.payout.PayoutInvoiceActivity;
import com.myhindlab.abkat.activities.payout.PayoutInvoiceForDoctorActivity;
import com.myhindlab.abkat.activities.re_registration.DailyWorkDashboardActivity;
import com.myhindlab.abkat.activities.re_registration.DailyWorkDashboardForAdminActivity;
import com.myhindlab.abkat.activities.reallocation.PacketReallocationActivity;
import com.myhindlab.abkat.activities.regularcampcreation.CampCreationActivityV4;
import com.myhindlab.abkat.adapters.MenuListAdapter;
import com.myhindlab.abkat.appointment_confirmation.Expected_BeneficiariesActivity;
import com.myhindlab.abkat.databinding.ActivityMedicineDeliveryMenuNewBinding;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.expense_module.activities.ExpenseClaimDashboardActivity;
import com.myhindlab.abkat.pojos.MenuListPojo;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ConstantData;
import com.myhindlab.abkat.utilities.RecyclerItemClickListener;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class MedicineDeliveryMenuNewActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMedicineDeliveryMenuNewBinding binding;

    private Context context;
    private ConstantData constantData;

    private ArrayList<MenuListPojo> menuList;

    int DESGID;
    private String name = "", Designation = "", DISTLGDCODE, TodayDate, mobileNo, TodayDateNew, allowMenu = "0", userId;

    private UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMedicineDeliveryMenuNewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        initView();
        setDefaults();
        setEventHandler();
        setUpToolbar();

//        setMenuRecyclerView();

//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_medicine_delivery_menu_new);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

//        binding.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAnchorView(R.id.fab)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    private void setEventHandler() {

        binding.recyclerviewSitesurveymenu.addOnItemTouchListener(
                new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        MenuListPojo element = menuList.get(position);
                        if (element.getClassName() != null) {
                            if (session.isHllUser()) {
                                switch (element.getMenuName()) {
                                    case "ELearning":
                                        startActivity(new Intent(context, ELearning.class));
                                        break;
                                    case "Fingerprint Upload":
                                        startActivity(new Intent(context, UploadFingerPrint_Activity.class));
                                        break;
                                    case "User Attendance":
                                        startActivity(new Intent(context, Availability_Activity.class));
                                        break;

                                    case "Team-Camp Mapping":
                                        startActivity(new Intent(context, TeamCampMappingActivity.class));
                                        break;

                                    case "D2D Availability Screening":
                                        startActivity(new Intent(context, D2DAvailabilityActivity.class));
                                        break;

                                    case "Camp Calendar":
                                        startActivity(new Intent(context, CampCalendar_Activity.class));
                                        break;

                                    case "Target Tracking":
                                        startActivity(new Intent(context, SetPostCampTarget_Activity.class));
                                        break;

                                    case "Resource List":
                                        startActivity(new Intent(context, ResourceList_Activity.class));
                                        break;

                                    case "Report Delivery":
                                        startActivity(new Intent(context, ReportDeliveryActivity.class));
                                        break;

                                    case "Medicine Delivery":
                                        startActivity(new Intent(context, MedicineDeliveryActivity.class));
                                        break;


                                    case "Packet Allocation":
                                        startActivity(new Intent(context, element.getClassName()));
                                        break;

                                    case "Medicine Return":
                                        startActivity(new Intent(context, element.getClassName()));
                                        break;

                                    case "Payout Details":
                                        startActivity(new Intent(context, PayoutDetails.class));
                                        break;

                                    case "Packet Collection":
                                        startActivity(new Intent(context, PacketCollectionActivity.class));
                                        break;

                                    case "Post Camp":
                                        startActivity(new Intent(context, PostCampCounts_Activity.class));
                                        break;

                                    case "Sample Processing":
                                        startActivity(new Intent(context, SampleProcessingStatus_Activity.class));
                                        break;

                                    case "Expense/Claim":
                                        startActivity(new Intent(context, ExpenseClaimDashboardActivity.class));
                                        break;

                                    case "D2d Teams":
                                        startActivity(new Intent(context, D2dTeamsActivity.class));
                                        break;

                                    case "Acknowledgment":
                                        startActivity(new Intent(context, CampSelectForAcknowledgement_Activity.class));
                                        break;

                                    case "D2d Team":
                                        startActivity(new Intent(context, D2dTeamsNotWorkingTeamActivity.class));
                                        break;

                                    case "Accept Samples":
                                        startActivity(new Intent(context, AcceptSamplesInLabActivity.class));
                                        break;

                                    case "Packet Receive":
                                        startActivity(new Intent(context, PacketReceiveActivity.class));
                                        break;

                                    case "Medicine Delivery Menu":
                                        startActivity(new Intent(context, MedicineDeliveryMenuNewActivity.class));
                                        break;

                                    case "Pickup Medicine Packet":
                                        startActivity(new Intent(context, AcceptMedicinePacketActivity.class));
                                        break;

                                    case "Device & Resource Mapping":
                                        startActivity(new Intent(context, CampCreationActivityV4.class));
                                        break;

                                    case "Centrifuge Confirmation":
                                        startActivity(new Intent(context, CentriFugeConfirmationActivity.class));
                                        break;

                                    case "Courier Activity":
                                        startActivity(new Intent(context, Courier_Activity.class));
                                        break;

                                    case "CSC Camp Request":
                                        startActivity(new Intent(context, CampRequestActivity.class));
                                        break;


                                    case "Call To Doctor":
                                        startActivity(new Intent(context, CallToDoctorRequestActivity.class));
                                        break;

                                    case "Availability Dashboard":
                                        startActivity(new Intent(context, DashboardAvailabilityReportDistrict_Activty.class));
                                        break;

                                    case "Heath Screening Staus":
                                        startActivity(new Intent(context, CampDetails_Activity.class));
                                        break;

                                    case "Camp Creation":
                                        startActivity(new Intent(context, CreateCamp_Activity_v3.class));
                                        break;

                                    case "Update Process Lab":
                                        startActivity(new Intent(context, UpdateProcessLabActivity.class));
                                        break;

                                    case "Post Camp Readiness":
                                        startActivity(new Intent(context, DashboardInvoice_Activity.class));
                                        break;

                                    case "Check Beneficiary Details":
                                        startActivity(new Intent(context, element.getClassName()));
                                        break;

                                    case "Appointment Confirmed List":
                                        startActivity(new Intent(context, element.getClassName()));
                                        break;

                                    case "Common Beneficiary List":
                                        startActivity(new Intent(context, element.getClassName()));
                                        break;

                                    case "Payment & Invoice":
                                        startActivity(new Intent(context, element.getClassName()));
                                        break;

                                    case "Calling List":
                                        startActivity(new Intent(context, element.getClassName()));
                                        break;


                                    case "Patient Registration":
                                        startActivity(new Intent(context, element.getClassName()));
                                        break;

                                    case "Dashboard Patient app":
                                        startActivity(new Intent(context, element.getClassName()));
                                        break;


                                    case "D2d Physical Examination Details":
                                        startActivity(new Intent(context, element.getClassName()));
                                        break;

                                    case "Appointments & Sample Collection Of CT":
                                        startActivity(new Intent(context, element.getClassName()));
                                        break;

                                    case "Camp Readiness Form":
                                        startActivity(new Intent(context, element.getClassName()));
                                        break;

                                    case "CT Assignment":
                                        startActivity(new Intent(context, element.getClassName()));
                                        break;

//                                    case "Packet Allocation":
//                                        startActivity(new Intent(context, element.getClassName()));
//                                        break;
                                    case "Daily Work Dashboard":
                                        startActivity(new Intent(context, element.getClassName()));
                                        break;

                                    case "Pick-Up Medicine Packet":
                                        startActivity(new Intent(context, element.getClassName()));
                                        break;

                                    case "D2D Camp Activity":
                                        constantData = ConstantData.getInstance();
                                        constantData.setSetSitetypeid(String.valueOf(position + 1));
                                        constantData.setSetSitetypeName(element.getMenuName());
                                        constantData.setClassName(element.getClassName());
                                        startActivity(new Intent(context, element.getClassName()));
                                        break;

                                    default:
                                        constantData = ConstantData.getInstance();
                                        constantData.setSetSitetypeid(String.valueOf(position + 1));
                                        constantData.setSetSitetypeName(element.getMenuName());
                                        constantData.setClassName(element.getClassName());
                                        startActivity(new Intent(context, D2DSelectCampActivity.class));
                                        break;
                                }
                            }

//                            else if (element.getClassName().equals(ResourceList_Activity.class)) {
//                                Utilities.showToastMessage("Coming Soon...", context, false);
//
//                            }
                            else {
                                constantData = ConstantData.getInstance();
                                constantData.setSetSitetypeid(String.valueOf(position + 1));
                                constantData.setSetSitetypeName(element.getMenuName());
                                startActivity(new Intent(context, element.getClassName()));
                            }
                        } else {
                            Utilities.showToastMessage("Coming Soon...", context, false);
                        }
                    }
                })
        );

    }


    private void initView() {
        context = MedicineDeliveryMenuNewActivity.this;
        session = new UserSessionManager(context);

    }


    private void setDefaults() {

        PackageInfo pinfo = null;
        try {
            pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
//        versionName = pinfo.versionName;
//
//        Calendar cal = Calendar.getInstance();
//        mYear = cal.get(Calendar.YEAR);
//        mMonth = cal.get(Calendar.MONTH);
//        mDay = cal.get(Calendar.DAY_OF_MONTH);

        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                name = json.getString("name");
                Designation = json.getString("Designation");
                DESGID = json.getInt("DESGID");
                DISTLGDCODE = json.getString("DISTLGDCODE");
                userId = String.valueOf(json.getInt("EmpCode"));
                mobileNo = json.getString("BMobile");


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        setupMenu();
    }


    void setupMenu() {
        menuList = new ArrayList<MenuListPojo>();
        menuList.clear();

        if (session.isHllUser()) {
            switch (DESGID) {


                case 157:///Delivery Executive
                    menuList.add(new MenuListPojo("Pick-Up Medicine Packet", R.drawable.ic_packetaccept, AcceptMedicinePacketActivity.class));
                    menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.icon_medicinedelivery, MedicineDeliveryActivity.class));
//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, Availability_Activity.class));
                    break;


                case 131: //Report Delivery Executive
                    menuList.add(new MenuListPojo("Pick-Up Medicine Packet", R.drawable.ic_packetaccept, AcceptMedicinePacketActivity.class));
                    menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.icon_medicinedelivery, MedicineDeliveryActivity.class));
//                        menuList.add(new MenuListPojo("Report Delivery", R.drawable.icon_report_patient, ReportDeliveryActivity.class));
//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, Availability_Activity.class));
                    break;

                case 156:   //Service Delivery Coordinator

//                        menuList.add(new MenuListPojo("Packet Receive", R.drawable.packetreceive, PacketReceiveActivity.class));
                    menuList.add(new MenuListPojo("Packet Allocation", R.drawable.icon_medicinedelivery, PacketReallocationActivity.class));

//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, Availability_Activity.class));
                    break;

                case 115: //Lab Logistic Executive // Lab Logistic Coordinator
                case 190: //Logistic Executive
                case 85: //Logistic Executive
                    menuList.add(new MenuListPojo("Packet Collection", R.drawable.ic_packetcollect, PacketCollectionActivity.class));


//                        menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, Availability_Activity.class));
                    break;


                case 29:    // --- Camp Coordinator

                    menuList.add(new MenuListPojo("Packet Allocation", R.drawable.icon_medicinedelivery, PacketReallocationActivity.class));
                    menuList.add(new MenuListPojo("Packet Collection", R.drawable.ic_packetcollect, PacketCollectionActivity.class));
//                        menuList.add(new MenuListPojo("Packet Receive", R.drawable.packetreceive, PacketReceiveActivity.class));


                    break;


                case 58:    // --- LFT (Lung) Technician LungFunction
                case 41:    // --- AVT (Audio) Technician visual tech
                case 57:    // --- VST (Visual) Technician Audio Technician
                    // case 105:    // --- HLL Plhebo
                case 4:    // --- HLL Plhebo
                case 35:
                case 86: //Data Entry Operator
                case 146: //Flexi phebo


                    menuList.add(new MenuListPojo("Packet Collection", R.drawable.ic_packetcollect, PacketCollectionActivity.class));
                    menuList.add(new MenuListPojo("Pick-Up Medicine Packet", R.drawable.ic_packetaccept, AcceptMedicinePacketActivity.class));
                    menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.icon_medicinedelivery, MedicineDeliveryActivity.class));

                    break;


                case 138:///Mmu T2t Phlebotomist
                case 169:///Ert Mmu
                case 177:///Ert Mmu
                case 137:///Nurse
                case 31:///Nurse
                case 176:///Nurse


                    menuList.add(new MenuListPojo("Packet Collection", R.drawable.ic_packetcollect, PacketCollectionActivity.class));
                    menuList.add(new MenuListPojo("Pick-Up Medicine Packet", R.drawable.ic_packetaccept, AcceptMedicinePacketActivity.class));
                    menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.icon_medicinedelivery, MedicineDeliveryActivity.class));

                    break;


                case 172:
                case 173:
                case 186:
//                        menuList.add(new MenuListPojo("CT Assignment", R.drawable.ic_group_outlined, ConfirmatoryTestAssignTeamActivity.class));
                    menuList.add(new MenuListPojo("Packet Collection", R.drawable.ic_packetcollect, PacketCollectionActivity.class));
//                        menuList.add(new MenuListPojo("Packet Receive", R.drawable.packetreceive, PacketReceiveActivity.class));
                    menuList.add(new MenuListPojo("Packet Allocation", R.drawable.icon_medicinedelivery, PacketReallocationActivity.class));
                    menuList.add(new MenuListPojo("Pick-Up Medicine Packet", R.drawable.ic_packetaccept, AcceptMedicinePacketActivity.class));
                    menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.icon_medicinedelivery, MedicineDeliveryActivity.class));
                    break;


                case 128:
                    menuList.add(new MenuListPojo("Pick-Up Medicine Packet", R.drawable.ic_packetaccept, AcceptMedicinePacketActivity.class));
                    menuList.add(new MenuListPojo("Medicine Delivery", R.drawable.icon_medicinedelivery, MedicineDeliveryActivity.class));
//                        menuList.add(new MenuListPojo("Report Delivery", R.drawable.icon_report_patient, ReportDeliveryActivity.class));

                    //   menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, Availability_Activity.class));
                    break;




                case 92: //---Dd Camp Coordinator
//                        menuList.add(new MenuListPojo("Report Delivery", R.drawable.icon_report_patient, ReportDeliveryActivity.class));
                    menuList.add(new MenuListPojo("Packet Collection", R.drawable.ic_packetcollect, PacketCollectionActivity.class));
                    menuList.add(new MenuListPojo("Medicine Return", R.drawable.icon_medicinedelivery, MedicineReturnActivity.class));
                    menuList.add(new MenuListPojo("Packet Allocation", R.drawable.icon_medicinedelivery, PacketReallocationActivity.class));
//                        menuList.add(new MenuListPojo("Packet Receive", R.drawable.packetreceive, PacketReceiveActivity.class));

                    break;


                case 160: //Maha Arogya Saarathi
                case 108: //Lab Coordinator
                case 136: //Mmu T2t Camp Coordinator
                case 139: //Mmu T2t Arogasathi
//                        menuList.add(new MenuListPojo("Report Delivery", R.drawable.icon_report_patient, ReportDeliveryActivity.class));
                    menuList.add(new MenuListPojo("Packet Collection", R.drawable.ic_packetcollect, PacketCollectionActivity.class));
                    menuList.add(new MenuListPojo("Packet Allocation", R.drawable.icon_medicinedelivery, PacketReallocationActivity.class));
                    if (DESGID == 160) {
                        menuList.add(new MenuListPojo("Medicine Return", R.drawable.icon_medicinedelivery, MedicineReturnActivity.class));

                    }
//                        menuList.add(new MenuListPojo("Packet Receive", R.drawable.packetreceive, PacketReceiveActivity.class));
                    break;

                default:
//                        Utilities.showAlertDialog(context, "Alert",
//                                "You are not authorised user to use this application, Please contact to support team.",
//                                false, "Logout", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        session.logoutUser();
//                                        finish();
//                                    }
//                                });
                    //  menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, Availability_Activity.class));

                    break;
            }
            // menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, Availability_Activity.class));

        }

        if (DESGID == 51 || DESGID == 166 || DESGID == 173 || DESGID == 182 || DESGID == 183 || DESGID == 196 || DESGID == 198) {

        } else {
//                menuList.add(new MenuListPojo("User Attendance", R.drawable.icon_userattendance, Availability_Activity.class));
        }


        if (menuList != null) {
            setMenuRecyclerView();
        }

    }


    private void setUpToolbar() {
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);


        if (getSupportActionBar() != null) {

            getSupportActionBar().setTitle("Medicine Delivery");


        }
        toolbar.setNavigationIcon(R.drawable.icon_arrowback);
        toolbar.setNavigationOnClickListener(v -> finish());


    }


    private void setMenuRecyclerView() {
        binding.recyclerviewSitesurveymenu.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 3);
        binding.recyclerviewSitesurveymenu.setLayoutManager(layoutManager);
        binding.recyclerviewSitesurveymenu.setAdapter(new MenuListAdapter(menuList));
    }
//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_medicine_delivery_menu_new);
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
}