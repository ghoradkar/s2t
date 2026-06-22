package com.myhindlab.abkat.activities.regularcampcreation;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SiteSelectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
//public class SiteSelectionFragment extends Fragment implements SiteListAdapter.SiteListEvent {
//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private ApiInterface apiService;
//
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//    static FragmentChange fragmentChange;
//    private UserSessionManager userSessionManager;
//    private Context mContext;
//    private int distLgdCode;
//    private RecyclerView rvSitesList;
//    private final String TAG = SiteSelectionFragment.class.getSimpleName();
//    private SearchView siteSearchView;
//    private SiteListModel siteListModel;
//    private ArrayList<SiteListModel.Output> searchList;
//    private Button btnNext;
//    private List<SiteListModel.Output> selSites;
//    private RadioGroup rgCampType;
//    private RadioButton rbRegular, rbD2d;
//    private ConstraintLayout clMain;
//
//    private ProgressDialog progressDialog;
//
//
//    public SiteSelectionFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1       Parameter 1.
//     * @param param2       Parameter 2.
//     * @param onFragChange
//     * @return A new instance of fragment SiteSelectionFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static SiteSelectionFragment newInstance(String param1, String param2, FragmentChange onFragChange) {
//        SiteSelectionFragment fragment = new SiteSelectionFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        fragmentChange = onFragChange;
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.fragment_site_selection, container, false);
//
//
//        init(rootView);
//        clickEvent();
//        return rootView;
//    }
//
//    void init(View view) {
//        mContext = requireContext();
//        userSessionManager = new UserSessionManager(mContext);
//        apiService = ApiClient.getClient().create(ApiInterface.class);
//        selSites = new ArrayList<>();
//        progressDialog = new ProgressDialog(mContext);
//
//        UserResponseModel.Output userResponseModel = userSessionManager.getUserDetailsJson();
//        distLgdCode = userResponseModel.getDistlgdcode();
//        siteSearchView = view.findViewById(R.id.siteSearchView);
//        rvSitesList = view.findViewById(R.id.rvSiteList);
//        rvSitesList.setHasFixedSize(true);
//        rvSitesList.setLayoutManager(new LinearLayoutManager(requireContext()));
//        btnNext = view.findViewById(R.id.btnNext);
//        rgCampType = view.findViewById(R.id.rgCampType);
//        rbRegular = view.findViewById(R.id.rbRegular);
//        rbD2d = view.findViewById(R.id.rbD2D);
//        clMain = view.findViewById(R.id.clMain);
//
//        CampCreationActivityV4.campCreationModel.setISD2DCamp(0);
//
//        getSites();
//
//        setUpSearch();
//
//
////        if (siteListModel != null && siteListModel.getOutput().size() > 0) {
////            siteSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
////                @Override
////                public boolean onQueryTextSubmit(String query) {
////                    searchList = new ArrayList<>();
////                    if (!query.isEmpty()) {
////                        for (SiteListModel.Output site :
////                                siteListModel.getOutput()) {
////                            if (site.getSiteName().toLowerCase().contains(query.toLowerCase())) {
////                                searchList.add(site);
////                            }
////                        }
////                        Log.i(TAG, "onQueryTextSubmit: " + searchList.size());
////
////                        if (searchList.size() > 0) {
////                            rvSitesList.setAdapter(new SiteListAdapter(searchList, SiteSelectionFragment.this::onSiteSelected));
////
////                        } else {
////                            rvSitesList.setAdapter(new SiteListAdapter(siteListModel.getOutput(), SiteSelectionFragment.this::onSiteSelected));
////
////                        }
////                    } else {
////                        rvSitesList.setAdapter(new SiteListAdapter(siteListModel.getOutput(), SiteSelectionFragment.this::onSiteSelected));
////
////                    }
////                    return true;
////
////                }
////
////                @Override
////                public boolean onQueryTextChange(String newText) {
////                    searchList = new ArrayList<>();
////                    if (!newText.isEmpty()) {
////                        for (SiteListModel.Output site :
////                                siteListModel.getOutput()) {
////                            if (site.getSiteName().toLowerCase().contains(newText.toLowerCase())) {
////                                searchList.add(site);
////                            }
////
////                        }
////                        Log.i(TAG, "onQueryTextChange: " + searchList.size());
////                        if (searchList.size() > 0) {
////                            rvSitesList.setAdapter(new SiteListAdapter(searchList, SiteSelectionFragment.this::onSiteSelected));
////
////                        } else {
////                            rvSitesList.setAdapter(new SiteListAdapter(siteListModel.getOutput(), SiteSelectionFragment.this::onSiteSelected));
////
////                        }
////                    } else {
////                        rvSitesList.setAdapter(new SiteListAdapter(siteListModel.getOutput(), SiteSelectionFragment.this::onSiteSelected));
////
////                    }
////
////                    return true;
////                }
////            });
////        }
//
//
//    }
//
//    private void clickEvent() {
//
//        rgCampType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                if (rbRegular.isChecked()) {
//                    clMain.setVisibility(View.VISIBLE);
//                    CampCreationActivityV4.campCreationModel.setISD2DCamp(0);
//                } else {
//                    clMain.setVisibility(View.GONE);
//                    CampCreationActivityV4.campCreationModel.setISD2DCamp(3);
//                }
//            }
//        });
//
//
//        btnNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
////                if (CampCreationActivityV4.campCreationModel.getSiteList() == null || CampCreationActivityV4.campCreationModel.getSiteList().size() == 0 || CampCreationActivityV4.campCreationModel.getSiteList().size() != selSites.size()) {
//                if (rbRegular.isChecked()) {
//                    if (siteListModel.getOutput() != null && siteListModel.getOutput().size() > 0) {
//
//
//                        for (SiteListModel.Output site : siteListModel.getOutput()
//                        ) {
//                            if (site.isChecked()) {
//                                if (!selSites.contains(site))
//                                    selSites.add(site);
//                            } else {
//                                if (selSites.contains(site))
//                                    selSites.remove(site);
//                            }
//
//                        }
//                        if (selSites.size() != 0) {
//                            Utilities.showAlertDialog(mContext, "Site", "You have selected " + selSites.size() + " Sites for this camp", true, "Next", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    CampCreationActivityV4.campCreationModel.setSiteList(selSites);
//                                    Log.i(TAG, "onClick: " + new Gson().toJson(CampCreationActivityV4.campCreationModel));
//                                    fragmentChange.onFragmentChange(1);
//                                }
//                            });
//                        } else {
//                            Utilities.showToastMessage("Please select sites for camp", mContext, false);
//                        }
//                    } else {
//                        Utilities.showToastMessage("Sites not available for camp", mContext, false);
//                    }
//                } else {
//                    SiteListModel siteListModel1 = new SiteListModel();
//                    selSites.add(siteListModel1.new Output(0, "04012023", "Bavdhan NDA Pashan Rd Lantana Gardens Bavdhan Pune Maharashtra 411021 India", "411028", "Adilabad", 18.5196546, 73.7786621, 501, "ADILABAD", 1, 50, 1, true));
//                    CampCreationActivityV4.campCreationModel.setSiteList(selSites);
//                    Log.i(TAG, "onClick: " + new Gson().toJson(CampCreationActivityV4.campCreationModel));
//                    fragmentChange.onFragmentChange(1);
//                }
////                }
////                else {
//////                    if (CampCreationActivityV4.campCreationModel.getSiteList().size() != selSites.size()) {
////                    Utilities.showAlertDialog(mContext, "Site", "You have selected " + CampCreationActivityV4.campCreationModel.getSiteList().size() + " Sites for this camp", true, "Next", new DialogInterface.OnClickListener() {
////                        @Override
////                        public void onClick(DialogInterface dialogInterface, int i) {
//////                                CampCreationActivityV4.campCreationModel.setSiteList(selSites);
////                            Log.i(TAG, "onClick: " + new Gson().toJson(CampCreationActivityV4.campCreationModel));
////                            fragmentChange.onFragmentChange(1);
////                        }
////                    });
//////                    } else {
//////                        Utilities.showToastMessage("Please select sites for camp", mContext, false);
//////                    }
////
////                }
//            }
//        });
//
//    }
//
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
////            if (CampCreationActivityV4.campCreationModel.getSiteList() != null) {
////                selSites = new ArrayList<>();
////                selSites.addAll(CampCreationActivityV4.campCreationModel.getSiteList());
////            }
//        }
//    }
//
//    // creates the comparator for comparing name
//    class NameComparator implements Comparator<SiteListModel.Output> {
//
//        // override the compare() method
//        public int compare(SiteListModel.Output s1, SiteListModel.Output s2) {
//            return s1.getSiteName().compareTo(s2.getSiteName());
//        }
//
//    }
//
//    void getSites() {
//        progressDialog.setMessage("Getting sites..");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
//        apiService.getSiteSurveyListFor(distLgdCode, "").enqueue(new Callback<SiteListModel>() {
//            @Override
//            public void onResponse(Call<SiteListModel> call, Response<SiteListModel> response) {
//
//                progressDialog.dismiss();
//                Log.i(TAG, "onResponse: " + response);
//                if (response.isSuccessful()) {
//                    siteListModel = response.body();
//                    if (siteListModel.getStatus().equalsIgnoreCase("success")) {
//                        if (!siteListModel.getOutput().isEmpty()) {
//                            Collections.sort(siteListModel.getOutput(), new NameComparator());
//                            if (CampCreationActivityV4.campCreationModel.getSiteList() != null) {
//                                for (SiteListModel.Output site :
//                                        CampCreationActivityV4.campCreationModel.getSiteList()) {
//                                    for (int i = 0; i < siteListModel.getOutput().size(); i++) {
//                                        if (Objects.equals(siteListModel.getOutput().get(i).getSiteDetailId(), site.getSiteDetailId())) {
//                                            siteListModel.getOutput().get(i).setChecked(true);
//                                            selSites.add(siteListModel.getOutput().get(i));
//                                        }
//                                    }
//                                }
//                                rvSitesList.setAdapter(new SiteListAdapter(siteListModel.getOutput(), SiteSelectionFragment.this::onSiteSelected));
//                            } else
//                                rvSitesList.setAdapter(new SiteListAdapter(siteListModel.getOutput(), SiteSelectionFragment.this::onSiteSelected));
//                        } else {
//                            Utilities.showToastMessage("No sites available", mContext, false);
//                        }
//                    } else {
//                        Utilities.showToastMessage("No sites available", mContext, false);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<SiteListModel> call, Throwable t) {
//                progressDialog.dismiss();
//
//                Log.e(TAG, "onFailure: " + t.getMessage());
//            }
//        });
//    }
//
//    void setUpSearch() {
//        siteSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                searchList = new ArrayList<>();
//                if (siteListModel != null)
//                    if (!query.isEmpty()) {
//                        for (SiteListModel.Output site :
//                                siteListModel.getOutput()) {
//                            if (site.getSiteName().toLowerCase().contains(query.toLowerCase())) {
//                                searchList.add(site);
//                            }
//                        }
//                        Log.i(TAG, "onQueryTextSubmit: " + searchList.size());
//
//                        if (searchList.size() > 0) {
//                            rvSitesList.setAdapter(new SiteListAdapter(searchList, SiteSelectionFragment.this::onSiteSelected));
//
//                        } else {
//                            rvSitesList.setAdapter(new SiteListAdapter(siteListModel.getOutput(), SiteSelectionFragment.this::onSiteSelected));
//
//                        }
//                    } else {
//                        if (siteListModel != null)
//                            rvSitesList.setAdapter(new SiteListAdapter(siteListModel.getOutput(), SiteSelectionFragment.this::onSiteSelected));
//
//                    }
//                return true;
//
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                searchList = new ArrayList<>();
//                if (siteListModel != null)
//                    if (!newText.isEmpty()) {
//                        for (SiteListModel.Output site :
//                                siteListModel.getOutput()) {
//                            if (site.getSiteName().toLowerCase().contains(newText.toLowerCase())) {
//                                searchList.add(site);
//                            }
//
//                        }
//                        Log.i(TAG, "onQueryTextChange: " + searchList.size());
//                        if (searchList.size() > 0) {
//                            rvSitesList.setAdapter(new SiteListAdapter(searchList, SiteSelectionFragment.this::onSiteSelected));
//
//                        } else {
//                            rvSitesList.setAdapter(new SiteListAdapter(siteListModel.getOutput(), SiteSelectionFragment.this::onSiteSelected));
//
//                        }
//                    } else {
//                        if (siteListModel != null)
//                            rvSitesList.setAdapter(new SiteListAdapter(siteListModel.getOutput(), SiteSelectionFragment.this::onSiteSelected));
//
//                    }
//
//                return true;
//            }
//        });
//
//    }
//
//
//    @Override
//    public void onSiteSelected(SiteListModel.Output output) {
//        if (output.isChecked()) {
//            if (!selSites.contains(output)) {
//                output.setSiteAddress(output.getSiteAddress().replaceAll(",", ""));
//                selSites.add(output);
//            }
//        } else {
//            if (selSites.contains(output)) {
//                selSites.remove(output);
//            }
//        }
//
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        rvSitesList.smoothScrollBy(0, 0);
//    }
//}