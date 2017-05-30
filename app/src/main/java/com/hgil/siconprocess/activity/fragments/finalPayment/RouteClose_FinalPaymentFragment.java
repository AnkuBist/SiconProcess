package com.hgil.siconprocess.activity.fragments.finalPayment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.hgil.siconprocess.R;
import com.hgil.siconprocess.base.BaseFragment;
import com.hgil.siconprocess.database.masterTables.RouteView;
import com.hgil.siconprocess.retrofit.RetrofitService;
import com.hgil.siconprocess.retrofit.RetrofitUtil;
import com.hgil.siconprocess.retrofit.loginResponse.syncResponse;
import com.hgil.siconprocess.syncPOJO.FinalPaymentModel;
import com.hgil.siconprocess.syncPOJO.SRouteModel;
import com.hgil.siconprocess.utils.Utility;
import com.hgil.siconprocess.utils.ui.SampleDialog;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RouteClose_FinalPaymentFragment extends BaseFragment {

    @BindView(R.id.etNetSale)
    EditText etNetSale;
    @BindView(R.id.etCashCollection)
    EditText etCashCollection;
    @BindView(R.id.etCashReceived)
    EditText etCashReceived;
    @BindView(R.id.etShortExcess)
    EditText etShortExcess;

    @BindView(R.id.btnFinalPayment)
    Button btnFinalPayment;

    private double supervisor_received_amount;
    private double short_excess_amount;      //cashier short_Excess
    private double discount_amount;         //not calculated

    private Handler updateBarHandler;

    private RouteView routeView;
    private FinalPaymentModel finalPaymentModel;

    public RouteClose_FinalPaymentFragment() {
        // Required empty public constructor
    }

    public static RouteClose_FinalPaymentFragment newInstance() {
        RouteClose_FinalPaymentFragment fragment = new RouteClose_FinalPaymentFragment();
        return fragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_route_close_final_payment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hideSaveButton();
        updateBarHandler = new Handler();

        routeView = new RouteView(getContext());

        finalPaymentModel = routeView.routeFinalPayment(getRouteId());

        //editText values set
        etNetSale.setText(String.valueOf(finalPaymentModel.getTotal_amount()));
        etCashCollection.setText(String.valueOf(finalPaymentModel.getCashier_receive_amount()));
        etShortExcess.setText(String.valueOf(supervisor_received_amount - finalPaymentModel.getCashier_receive_amount()));

        etCashReceived.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    supervisor_received_amount = Utility.getDouble(etCashReceived.getText().toString());
                    etShortExcess.setText(String.valueOf(supervisor_received_amount - finalPaymentModel.getCashier_receive_amount()));
                } else {
                    supervisor_received_amount = 0;
                    etShortExcess.setText(String.valueOf(supervisor_received_amount - finalPaymentModel.getCashier_receive_amount()));
                }
            }
        });

        btnFinalPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supervisor_received_amount = Utility.getDouble(etCashReceived.getText().toString());
                short_excess_amount = supervisor_received_amount - finalPaymentModel.getCashier_receive_amount();
                finalPaymentModel.setSupervisor_received_amount(supervisor_received_amount);
                finalPaymentModel.setShort_excess_amount(short_excess_amount);
                finalPaymentModel.setDiscount_amount(discount_amount);

                //sync the above syncVanClose to server.
                String imei_number = Utility.readPreference(getContext(), Utility.DEVICE_IMEI);
                String supervisor_paycode = "android";

                SRouteModel routeModel = new SRouteModel();
                routeModel.setLoginId(getLoginId());
                routeModel.setRouteId(getRouteId());
                routeModel.setRouteName(getRouteName());
                routeModel.setDepotId(getRouteModel().getDepotId());
                routeModel.setRouteManagementId(getRouteModel().getRouteManagementId());
                routeModel.setCashierCode(getRouteModel().getCashierCode());
                routeModel.setSubCompanyId(getRouteModel().getSubCompanyId());
                routeModel.setSupervisorId(supervisor_paycode);
                routeModel.setImeiNo(imei_number);

                String routeDetails = new Gson().toJson(routeModel);
                String finalPayment = new Gson().toJson(finalPaymentModel);
                finalPayment(routeDetails, finalPayment);
            }
        });
    }

    // sync process retrofit call
    public void finalPayment(String routeDetails, String finalPayment) {
        updateBarHandler.post(new Runnable() {
            public void run() {
                RetrofitUtil.showDialog(getContext(), getString(R.string.str_final_payment));
            }
        });
        RetrofitService service = RetrofitUtil.retrofitClient();
        Call<syncResponse> apiCall = service.syncFinalPayment(routeDetails, finalPayment);
        apiCall.enqueue(new Callback<syncResponse>() {
            @Override
            public void onResponse(Call<syncResponse> call, Response<syncResponse> response) {
                updateBarHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        RetrofitUtil.hideDialog();
                    }
                }, 500);
                syncResponse syncResponse = response.body();
                // rest call to read data from api service
                if (syncResponse.getReturnCode()) {
                    //update final payment status
                    routeView.updateFinalPaymentStatus(getRouteId());

                    //check if call completed or not
                    new SampleDialog("", syncResponse.getStrMessage(), true, getContext());
                } else {
                    new SampleDialog("", syncResponse.getStrMessage(), getContext());
                }
            }

            @Override
            public void onFailure(Call<syncResponse> call, Throwable t) {
                updateBarHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        RetrofitUtil.hideDialog();
                    }
                }, 500);
                // show some error toast or message to display the api call issue
                new SampleDialog("", getString(R.string.str_retrofit_failure), getContext());
            }
        });
    }
}
