package com.myhindlab.abkat.activities.couriermodule;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import com.myhindlab.abkat.R;
import com.myhindlab.abkat.models.couriermodule.BarcodeViewListModel;
import com.myhindlab.abkat.models.couriermodule.CourierReceivedDetailsModel;
import com.myhindlab.abkat.utilities.ApplicationConstants;
import com.myhindlab.abkat.utilities.ParamsPojo;
import com.myhindlab.abkat.utilities.Utilities;
import com.myhindlab.abkat.utilities.WebServiceCall;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CourierAccept2_Activity extends Activity {

    private Context context;
    private TextView tv_registration_lab, tv_to_lab, tv_processing_lab, tv_courier_category, tv_date, tv_time,
            tv_select_mode_of_transport, tv_select_runner_boy, tv_docket_no, tv_company_name, tv_bus_no, tv_mobile_no,
            tv_amount, tv_sample_box, tv_expected_arrival_date, tv_expected_arrival_time, tvBarcode;
    private LinearLayout ll_runner_boy, ll_courier, ll_bus_vehicle;
    AlertDialog labDialog;
    private ProgressDialog pd;
    private List<BarcodeViewListModel.Output> barcodeList;
    private CourierReceivedDetailsModel.OutputBean selectedCourierDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier_accept2);

        init();
        setDefaults();
        setUpToolBar();
    }

    private void init() {
        context = CourierAccept2_Activity.this;
        pd = new ProgressDialog(context);
        tv_registration_lab = findViewById(R.id.tv_registration_lab);
        tv_to_lab = findViewById(R.id.tv_to_lab);
        tv_processing_lab = findViewById(R.id.tv_processing_lab);
        tv_courier_category = findViewById(R.id.tv_courier_category);
        tv_date = findViewById(R.id.tv_date);
        tv_time = findViewById(R.id.tv_time);
        tv_select_mode_of_transport = findViewById(R.id.tv_select_mode_of_transport);
        tv_select_runner_boy = findViewById(R.id.tv_select_runner_boy);
        tv_docket_no = findViewById(R.id.tv_docket_no);
        tv_company_name = findViewById(R.id.tv_company_name);
        tv_bus_no = findViewById(R.id.tv_bus_no);
        tv_mobile_no = findViewById(R.id.tv_mobile_no);
        tv_amount = findViewById(R.id.tv_amount);
        tv_sample_box = findViewById(R.id.tv_sample_box);
        tv_expected_arrival_date = findViewById(R.id.tv_expected_arrival_date);
        tv_expected_arrival_time = findViewById(R.id.tv_expected_arrival_time);
        ll_runner_boy = findViewById(R.id.ll_runner_boy);
        ll_courier = findViewById(R.id.ll_courier);
        ll_bus_vehicle = findViewById(R.id.ll_bus_vehicle);
        tvBarcode = findViewById(R.id.tvBarcode);

    }

    private void setDefaults() {
        selectedCourierDetails = (CourierReceivedDetailsModel.OutputBean) getIntent().getSerializableExtra("selectedCourierDetails");

        tv_registration_lab.setText(selectedCourierDetails.getRegistrationLab());
        tv_to_lab.setText(selectedCourierDetails.getCourierToLab());
        tv_processing_lab.setText(selectedCourierDetails.getProcessLab());
        tv_courier_category.setText(selectedCourierDetails.getCourierType());
        tv_date.setText(selectedCourierDetails.getCourierDate());
        tv_time.setText(selectedCourierDetails.getCourierTime());
        tv_select_mode_of_transport.setText(selectedCourierDetails.getModeOfTransport());
        tv_select_runner_boy.setText(selectedCourierDetails.getRunnerBoySendName());
        tv_docket_no.setText(selectedCourierDetails.getDocketNo());
        tv_company_name.setText(selectedCourierDetails.getCompanyName());
        tv_bus_no.setText(selectedCourierDetails.getBusVehicleNumber());
        tv_mobile_no.setText(selectedCourierDetails.getContactNo());
        tv_amount.setText(selectedCourierDetails.getAmountPaidbySender());
        tv_sample_box.setText(selectedCourierDetails.getNoOfSamples());
        tv_expected_arrival_date.setText(selectedCourierDetails.getExpArrivalDate());
        tv_expected_arrival_time.setText(selectedCourierDetails.getExpArrivalTime());

        switch (selectedCourierDetails.getModeId()) {
            case "1":
                ll_courier.setVisibility(View.VISIBLE);
                ll_runner_boy.setVisibility(View.GONE);
                ll_bus_vehicle.setVisibility(View.GONE);
                ll_bus_vehicle.setVisibility(View.GONE);
                break;
            case "2":
            case "4":
                ll_courier.setVisibility(View.GONE);
                ll_runner_boy.setVisibility(View.GONE);
                ll_bus_vehicle.setVisibility(View.VISIBLE);
                break;
            case "3":
                ll_courier.setVisibility(View.GONE);
                ll_runner_boy.setVisibility(View.VISIBLE);
                ll_bus_vehicle.setVisibility(View.GONE);
                break;
            default:
                ll_courier.setVisibility(View.GONE);
                ll_runner_boy.setVisibility(View.GONE);
                ll_bus_vehicle.setVisibility(View.GONE);
                break;
        }

        tvBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isNetworkAvailable(context)) {
                    new GetBarcodeList().execute(selectedCourierDetails.getCourierID());
                } else {
                    Utilities.showMessage(R.string.msgt_nointernetconnection, context);
                }
            }
        });


    }

    private void setUpToolBar() {
        ImageButton back_btn = findViewById(R.id.tool_leftbtn);
        TextView Title = findViewById(R.id.tool_titile);

        back_btn.setBackgroundResource(R.drawable.icon_back);
        Title.setText("Accept Courier");

        back_btn.setOnClickListener(view -> finish());
    }

    private class GetBarcodeList extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String res = "[]";
            Log.d("GetBarcodelist Params", Arrays.toString(strings));
            List<ParamsPojo> param = new ArrayList<ParamsPojo>();
            param.add(new ParamsPojo("CourierID", strings[0]));
//            param.add(new ParamsPojo("Barcode", strings[1]));
//            param.add(new ParamsPojo("FromClient", selectedCourierDetails.getFromClient()));
//            param.add(new ParamsPojo("ToClient", selectedCourierDetails.getToClient()));
           // res = WebServiceCall.HLLAPICall(ApplicationConstants.GetCourierBarcodeServicedetail, param);
            res = WebServiceCall.APICall(ApplicationConstants.GetCourierBarcodeServicedetail , ApplicationConstants.webservice_d2d, param);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String status = "", message = "";
            Log.d("GetBarcodelist Resp", result);

            try {
                pd.dismiss();
                if (!result.equals("")) {
                    BarcodeViewListModel barcodeListModel = new Gson().fromJson(result, BarcodeViewListModel.class);
                    if (barcodeListModel.getStatus().equalsIgnoreCase("success")) {
//                        Collections.sort(clientListModel.getOutput(), (o1, o2) -> o1.getClientName().compareTo(o2.getClientName()));
                        if (barcodeListModel.getOutput().size() > 0) {
                            barcodeList = new ArrayList<>();

                            barcodeList = barcodeListModel.getOutput();
                            tvBarcode.setText("Total Barcodes " + barcodeList.size());
                            BarcodeListDialog(barcodeList);
                        } else {
                            Utilities.showAlertDialog(context, "No Barcodes Available", "", false);

                        }
                    } else
                        Utilities.showAlertDialog(context, status, message, false);

                } else {
                    Utilities.showAlertDialog(context, "Fail", "Empty response", false);

                }
            } catch (Exception e) {
                Utilities.showAlertDialog(context, "Fail",
                        "Server Not Responding", false);
                e.printStackTrace();
            }
        }
    }

    private void BarcodeListDialog(List<BarcodeViewListModel.Output> mainlist) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_search_test, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        builder.setTitle("Barcode Included");
        builder.setCancelable(false);

        final RecyclerView rv_testlist = view.findViewById(R.id.rv_testlist);
        EditText edt_search = view.findViewById(R.id.edt_search);
        rv_testlist.setLayoutManager(new LinearLayoutManager(context));
        rv_testlist.setAdapter(new BarcodeListAdapter(mainlist));

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {

                if (query.toString().isEmpty()) {
                    rv_testlist.setAdapter(new BarcodeListAdapter(mainlist));
                    return;
                }

                if (mainlist.size() == 0) {
                    rv_testlist.setVisibility(View.GONE);
                    return;
                }

                if (!query.toString().equals("")) {
                    ArrayList<BarcodeViewListModel.Output> searchedTestList = new ArrayList<BarcodeViewListModel.Output>();
                    for (BarcodeViewListModel.Output clientDetails : mainlist) {

                        String countryToBeSearched = clientDetails.getBarcode().toLowerCase();

                        if (countryToBeSearched.contains(query.toString().toLowerCase())) {
                            searchedTestList.add(clientDetails);
                        }
                    }
                    rv_testlist.setAdapter(new BarcodeListAdapter(searchedTestList));
                } else {
                    rv_testlist.setAdapter(new BarcodeListAdapter(mainlist));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        builder.setNeutralButton("cancel", (dialog, which) -> {
//
//        });
        builder.setPositiveButton("Okay", (dialog, which) -> {
            dialog.dismiss();


        });


        labDialog = builder.create();
        labDialog.show();
    }

    private class BarcodeListAdapter extends RecyclerView.Adapter<BarcodeListAdapter.MyViewHolder> {

        private List<BarcodeViewListModel.Output> barcodeList;

        public BarcodeListAdapter(List<BarcodeViewListModel.Output> barcodeList) {
            this.barcodeList = barcodeList;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.barcode_list_item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, final int pos) {
            final int position = holder.getAdapterPosition();

            holder.cbBarcode.setVisibility(View.GONE);
            holder.tv_name.setVisibility(View.VISIBLE);
//            if (barcodeList.get(position).isChecked()) {
//                holder.cbBarcode.setChecked(true);
//            } else {
//                holder.cbBarcode.setChecked(false);
//
//            }
          //  holder.tv_name.setText(barcodeList.get(position).getSampleBarcode() + " (" + barcodeList.get(position).getServiceName() + ")");
            holder.tv_name.setText(barcodeList.get(position).getSampleBarcode());

//            holder.tv_name.setOnClickListener(v -> {
//                tv_select_runner_boy.setText("");
//                runnerBoyId = "0";
//
//                tvDestClientList.setText(barcodeList.get(position).getBarcode());
////                destClientCode = String.valueOf(labList.get(position).getClientCode());
//
//                labDialog.dismiss();
//            });

        }

        @Override
        public int getItemCount() {
            return barcodeList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView tv_name;
            private CheckBox cbBarcode;

            public MyViewHolder(@NonNull View view) {
                super(view);
                tv_name = view.findViewById(R.id.tv_name);
                cbBarcode = view.findViewById(R.id.cbBarcode);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }

}