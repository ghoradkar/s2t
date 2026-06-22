package com.myhindlab.abkat.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.myhindlab.abkat.R;
import com.myhindlab.abkat.activities.PatientAttendance_Activity;
import com.myhindlab.abkat.adapters.SiteDetailsAdapter;
import com.myhindlab.abkat.models.ConstructionSitesList_Model;
import com.myhindlab.abkat.pojos.ConstructionSitesList_Pojo;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ConstantData;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.RecyclerItemClickListener;
import com.myhindlab.abkat.utilities.UserSessionManager;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class PatientAttendance_Fragment extends Fragment {

//    private Context context;
//    private UserSessionManager session;
//    private MaterialEditText edt_selectdistrict, edt_selectsite, edt_siteaddress, edt_district, edt_taluka, edt_city, edt_pincode;
//    private Button btn_next;
//    private ProgressDialog pd;
//    private String DISTLGDCODE, SiteDetailId;

    private Context context;
    private ProgressDialog pd;
    private UserSessionManager session;
    private ConstantData constantData;

    private LatLng latLng = null;
    private Address address = null;

    private LinearLayout ll_currentlayout;
    private EditText edt_locationaddress, edt_searchorselectdistrict;
    private LinearLayout ll_sitedetailslist;
    private SearchView searchview_pincode, searchview_sitename;
    private RecyclerView recycler_view_sitesurveyList;

    private ArrayList<ConstructionSitesList_Model> siteList;
    private ArrayList<ConstructionSitesList_Model> searchSiteList;
    private String userID = "", searchDTLGDCODE = "";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_patient_attendance, container, false);
        context = getActivity();

        init(rootView);
        setDefault();
        setEventHandlers();
        return rootView;
    }

    private void init(View rootView) {
        pd = new ProgressDialog(context);
        session = new UserSessionManager(context);

//        edt_selectdistrict = rootView.rootView.findViewById(R.id.edt_selectdistrict);
//        edt_selectsite = rootView.findViewById(R.id.edt_selectsite);
//        edt_siteaddress = rootView.findViewById(R.id.edt_siteaddress);
//        edt_district = rootView.findViewById(R.id.edt_district);
//        edt_taluka = rootView.findViewById(R.id.edt_taluka);
//        edt_city = rootView.findViewById(R.id.edt_city);
//        edt_pincode = rootView.findViewById(R.id.edt_pincode);
//
//        btn_next = rootView.findViewById(R.id.btn_next);

        ll_currentlayout = rootView.findViewById(R.id.ll_currentlayout);
        edt_locationaddress = rootView.findViewById(R.id.edt_locationaddress);
        searchview_pincode = rootView.findViewById(R.id.searchview_pincode);

        ll_sitedetailslist = rootView.findViewById(R.id.ll_sitedetailslist);
        searchview_sitename = rootView.findViewById(R.id.searchview_sitename);
        recycler_view_sitesurveyList = rootView.findViewById(R.id.recycler_view_sitesurveyList);
        searchSiteList = new ArrayList<>();
    }

    public void setDefault() {
        context = getContext();
        constantData = ConstantData.getInstance();

        searchview_pincode.clearFocus();
        searchview_sitename.clearFocus();

        ll_sitedetailslist.setVisibility(View.GONE);

        try {
            JSONArray user_info = new JSONArray(session.getUserDetails().get(
                    ApplicationConstants.KEY_LOGIN_INFO));
            for (int j = 0; j < user_info.length(); j++) {
                JSONObject json = user_info.getJSONObject(j);
                userID = json.getString("EmpCode");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setEventHandlers() {
//        edt_selectdistrict.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Utilities.isNetworkAvailable(context)) {
//                    new GetDistrictList().execute();
//                } else {
//                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
//                }
//            }
//        });
//
//        edt_selectsite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Utilities.isNetworkAvailable(context)) {
//                    new GetListOfConstructionSite().execute(DISTLGDCODE);
//                } else {
//                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
//                }
//            }
//        });
//
//        btn_next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (edt_selectdistrict.getText().toString().trim().isEmpty()) {
//                    edt_selectdistrict.setError("Please select district");
//                    return;
//                }
//
//                if (edt_selectsite.getText().toString().trim().isEmpty()) {
//                    edt_selectsite.setError("Please select site");
//                    return;
//                }
//
//                startActivity(new Intent(context, PatientAttendance_Activity.class)
//                        .putExtra("SiteDetailId", SiteDetailId));
//            }
//        });
        searchview_pincode.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.trim().length() == 6) {
                    if (!Utilities.isNetworkAvailable(context)) {
                        Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                        return true;
                    }

                    new GetListOfConstructionSite().execute(query);

                } else {
                    Utilities.showToastMessage("Invalid PIN Code", context, false);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.trim().length() == 6) {
                    if (!Utilities.isNetworkAvailable(context)) {
                        Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                        return true;
                    }

                    new GetListOfConstructionSite().execute(newText);
                }
                return true;
            }
        });

        searchview_sitename.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchSiteList = new ArrayList<>();

                if (siteList != null) {
                    if (siteList.size() > 0) {
                        for (ConstructionSitesList_Model pojo : siteList) {
                            String siteDetails = pojo.getSiteName() + pojo.getPinCode();
                            if (siteDetails != null && siteDetails.toLowerCase().contains(query.toLowerCase())) {
                                searchSiteList.add(pojo);
                            }
                        }

                        if (searchSiteList.size() == 0) {
                            Utilities.showAlertDialog(context, "Alert", "No record found.", false);
                            searchSiteList.addAll(siteList);
                            showSiteListDialog(searchSiteList);
                        } else {
                            showSiteListDialog(searchSiteList);
                        }
                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchSiteList = new ArrayList<>();

                if (siteList != null) {
                    if (newText.equals("")) {
                        searchSiteList.addAll(siteList);
                        showSiteListDialog(searchSiteList);
                    } else {
                        if (siteList.size() > 0) {
                            for (ConstructionSitesList_Model pojo : siteList) {
                                String siteDetails = pojo.getSiteName() + pojo.getPinCode();
                                if (siteDetails != null && siteDetails.toLowerCase().contains(newText.toLowerCase())) {
                                    searchSiteList.add(pojo);
                                }
                            }

                            if (searchSiteList.size() == 0) {
                                searchSiteList.addAll(siteList);
                                showSiteListDialog(searchSiteList);
                            } else {
                                showSiteListDialog(searchSiteList);
                            }
                        }
                    }
                }
                return true;
            }
        });

        recycler_view_sitesurveyList.addOnItemTouchListener(
                new RecyclerItemClickListener(context,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, final int position) {
                                ConstructionSitesList_Model siteDetails = new ConstructionSitesList_Model();
                                siteDetails = searchSiteList.get(position);
                                siteDetails.setUserID(userID);

                                startActivity(new Intent(context, PatientAttendance_Activity.class)
                                        .putExtra("SiteDetailId", siteDetails.getSiteDetailId()));
                            }
                        }));
    }

    @Override
    public void onResume() {
        super.onResume();
        searchview_pincode.clearFocus();
        searchview_sitename.clearFocus();

        address = constantData.getAddress();

        if (searchSiteList.size() == 0) {
            if (address != null) {

                ll_currentlayout.setVisibility(View.VISIBLE);

                String addr = address.getAddressLine(0);
                String pincode = address.getPostalCode();

                edt_locationaddress.setText(addr);
                searchview_pincode.setQuery(pincode, false);

                if (Utilities.isNetworkAvailable(context)) {
                    String PINCode = searchview_pincode.getQuery().toString().trim();
                    if (PINCode.length() == 6) {
                        new GetListOfConstructionSite().execute(PINCode);
                    }
                } else {
                    Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
                }
            }
        }
    }

    //    public class GetDistrictList extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pd.setMessage("Please wait . . . ");
//            pd.setCancelable(false);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            String res = "";
//            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
//
//            res = WebServiceCall.APICall(ApplicationConstants.GetDistrictList, param);
//            return res;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            pd.dismiss();
//            String type = "", message = "";
//            try {
//                if (!result.equals("")) {
//                    ArrayList<DistrictList_Model> districtList = new ArrayList<>();
//                    DistrictList_Pojo pojoDetails = new Gson().fromJson(result, DistrictList_Pojo.class);
//                    type = pojoDetails.getStatus();
//                    message = pojoDetails.getMessage();
//                    if (type.equalsIgnoreCase("success")) {
//                        districtList = pojoDetails.getOutput();
//                        if (districtList.size() > 0) {
//                            showDistrictListDialog(districtList);
//                        } else {
//                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
//                        }
//                    } else {
//                        Utilities.showAlertDialog(context, "Fail", message, false);
//
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
//            }
//        }
//    }
//
//    private void showDistrictListDialog(final ArrayList<DistrictList_Model> districtList) {
//        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
//        builderSingle.setTitle("Select District");
//        builderSingle.setCancelable(false);
//
//        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);
//
//        for (int i = 0; i < districtList.size(); i++) {
//            arrayAdapter.add(String.valueOf(districtList.get(i).getDISTNAME()));
//        }
//
//        builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//
//        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                edt_selectdistrict.setText(districtList.get(which).getDISTNAME());
//                DISTLGDCODE = districtList.get(which).getDISTLGDCODE();
//
//                edt_selectsite.setText("");
//                edt_siteaddress.setText("");
//                edt_district.setText("");
//                edt_taluka.setText("");
//                edt_city.setText("");
//                edt_pincode.setText("");
//                SiteDetailId = "";
//            }
//        });
//        builderSingle.show();
//    }

    public class GetListOfConstructionSite extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait . . . ");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String res = "";
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("DISTLGDCODE", "0"));
            param.add(new ParamsPojo("PinCode", params[0]));
            res = WebServiceCall.APICall(ApplicationConstants.GetListOfConstructionSite, ApplicationConstants.webservice, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            pd.dismiss();
//            String type = "", message = "";
//            try {
//                if (!result.equals("")) {
//                    ArrayList<ConstructionSitesList_Model> siteList = new ArrayList<>();
//                    ConstructionSitesList_Pojo pojoDetails = new Gson().fromJson(result, ConstructionSitesList_Pojo.class);
//                    type = pojoDetails.getStatus();
//                    message = pojoDetails.getMessage();
//                    if (type.equalsIgnoreCase("success")) {
//                        siteList = pojoDetails.getOutput();
//                        if (siteList.size() > 0) {
//                            showSiteListDialog(siteList);
//                        } else {
//                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
//                        }
//                    } else {
//                        Utilities.showAlertDialog(context, "Fail", message, false);
//
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
//            }
//        }
//    }
            pd.dismiss();
            String type = "", message = "";
            try {
                if (!result.equals("")) {

                    ConstructionSitesList_Pojo pojoDetails = new Gson().fromJson(result, ConstructionSitesList_Pojo.class);
                    type = pojoDetails.getStatus();
                    message = pojoDetails.getMessage();
                    if (type.equalsIgnoreCase("success")) {
                        searchview_pincode.clearFocus();

                        siteList = new ArrayList<>();
                        siteList = pojoDetails.getOutput();

                        searchSiteList = new ArrayList<>();
                        searchSiteList.addAll(siteList);

                        if (siteList.size() > 0) {
                            showSiteListDialog(searchSiteList);
                        } else {
                            ll_sitedetailslist.setVisibility(View.GONE);
                            Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                        }
                    } else {
                        ll_sitedetailslist.setVisibility(View.GONE);
                        Utilities.showAlertDialog(context, "Fail", message, false);
                    }
                } else {
                    ll_sitedetailslist.setVisibility(View.GONE);
                    Utilities.showToastMessage(R.string.msgt_emptylist, context, false);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ll_sitedetailslist.setVisibility(View.GONE);
                Utilities.showAlertDialog(context, "Please Try Again", "Server Not Responding", false);
            }
        }
    }

    //    private void showSiteListDialog(final ArrayList<ConstructionSitesList_Model> siteList) {
//
//        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
//        builderSingle.setTitle("Select Construction Site");
//        builderSingle.setCancelable(false);
//
//        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.list_row);
//
//        for (int i = 0; i < siteList.size(); i++) {
//            arrayAdapter.add(String.valueOf(siteList.get(i).getSiteName()));
//        }
//
//        builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//
//        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                edt_selectsite.setText(siteList.get(which).getSiteName());
//                edt_siteaddress.setText(siteList.get(which).getSiteAddress());
//                edt_district.setText(siteList.get(which).getDISTNAME());
//                edt_taluka.setText(siteList.get(which).getTALNAME());
//                edt_city.setText(siteList.get(which).getCity());
//                edt_pincode.setText(siteList.get(which).getPinCode());
//
//                SiteDetailId = siteList.get(which).getSiteDetailId();
//            }
//        });
//        builderSingle.show();
//    }

    private void showSiteListDialog(ArrayList<ConstructionSitesList_Model> siteList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recycler_view_sitesurveyList.setLayoutManager(layoutManager);
        ll_sitedetailslist.setVisibility(View.VISIBLE);
        recycler_view_sitesurveyList.setAdapter(new SiteDetailsAdapter(siteList));
    }

//    private class GetAddress extends AsyncTask<String, String, Address> {
//        private ProgressDialog pDialog;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            if (context != null) {
//                pDialog = new ProgressDialog(context);
//                pDialog.setMessage("Please Wait . . .");
//                pDialog.setIndeterminate(false);
//                pDialog.setCancelable(true);
//                pDialog.show();
//            }
//        }
//
//        @Override
//        protected Address doInBackground(String... params) {
//            Address bestMatch = null;
//            try {
//                Geocoder geoCoder = new Geocoder(getContext());
//                List<Address> matches = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
//                bestMatch = (matches.isEmpty() ? null : matches.get(0));
//
//                if (bestMatch != null) {
//                    return bestMatch;
//                } else {
//                    return bestMatch;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                return bestMatch;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(Address result) {
//            // TODO Auto-generated method stub
//            super.onPostExecute(result);
//            if (context != null) {
//                pDialog.dismiss();
//
//                if (result != null) {
//                    ll_currentlayout.setVisibility(View.VISIBLE);
//
//                    String address = result.getAddressLine(0);
//                    String pincode = result.getPostalCode();
//
//                    edt_locationaddress.setText(address);
//                    searchview_pincode.setQuery(pincode, false);
//
//                    if (Utilities.isNetworkAvailable(context)) {
//                        new GetListOfConstructionSite().execute(pincode);
//                    } else {
//                        Utilities.showToastMessage(R.string.msgt_nointernetconnection, context, false);
//                    }
//                } else {
//                    ll_currentlayout.setVisibility(View.GONE);
//                }
//            }
//        }
//    }

}