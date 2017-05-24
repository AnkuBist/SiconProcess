package com.hgil.siconprocess.activity.fragments.finalPayment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.hgil.siconprocess.R;
import com.hgil.siconprocess.syncPOJO.supervisorSyncModel.CashierSyncModel;
import com.hgil.siconprocess.syncPOJO.supervisorSyncModel.CashCheckModel;
import com.hgil.siconprocess.syncPOJO.supervisorSyncModel.ItemStockCheck;
import com.hgil.siconprocess.base.BaseFragment;
import com.hgil.siconprocess.database.tables.PaymentTable;
import com.hgil.siconprocess.retrofit.RetrofitService;
import com.hgil.siconprocess.retrofit.RetrofitUtil;
import com.hgil.siconprocess.retrofit.loginResponse.syncResponse;
import com.hgil.siconprocess.utils.Utility;
import com.hgil.siconprocess.utils.ui.SampleDialog;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CashCheckFragment extends BaseFragment {

    private static final String SYNC_OBJECT = "sync_object";
    @BindView(R.id.etNetSale)
    EditText etNetSale;
    @BindView(R.id.etCashCollection)
    EditText etCashCollection;
    @BindView(R.id.etCashReceived)
    EditText etCashReceived;

    private double net_total_amount;
    private double m_rej_amount = 0;
    private double f_rej_amount = 0;
    private double leftover_amount = 0;     //supervisor leftover amount..,...not mandatory
    private double sample_amount = 0;       //  not manadatory
    private double cashier_receive_amount;
    private double supervisor_received_amount;
    private double short_excess_amount;      //cashier short_Excess
    private double discount_amount;         //not calculated


    private CashierSyncModel cashierSyncModel;
    private Handler updateBarHandler;

    public CashCheckFragment() {
        // Required empty public constructor
    }

    public static CashCheckFragment newInstance(CashierSyncModel cashierSyncModel) {
        CashCheckFragment fragment = new CashCheckFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SYNC_OBJECT, cashierSyncModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_cash_check;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cashierSyncModel = (CashierSyncModel) getArguments().getSerializable(SYNC_OBJECT);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showSaveButton();
        updateBarHandler = new Handler();

        PaymentTable paymentTable = new PaymentTable(getContext());

        // final payment information
        net_total_amount = paymentTable.getRouteSale();       //net sale amount

        // get sample and rejection amount from last table values
        for (ItemStockCheck itemStockCheck : cashierSyncModel.getArrItemStock()) {
            m_rej_amount += itemStockCheck.getItem_price() * itemStockCheck.getMarket_rejection();
            f_rej_amount += itemStockCheck.getItem_price() * itemStockCheck.getFresh_rejection();
            sample_amount += itemStockCheck.getItem_price() * itemStockCheck.getSample();
            leftover_amount += itemStockCheck.getItem_price() * itemStockCheck.getPhysical_leftover();
        }

        cashier_receive_amount = paymentTable.routeCashPaidAmount();      // cashier collected amount upi+cash....supervisor entered figure

        //editText values set
        etNetSale.setText(String.valueOf(net_total_amount));
        etCashCollection.setText(String.valueOf(cashier_receive_amount));

        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                supervisor_received_amount = Utility.getDouble(etCashReceived.getText().toString());
                short_excess_amount = cashier_receive_amount - supervisor_received_amount;

                CashCheckModel cashCheckModel = new CashCheckModel();
                cashCheckModel.setRoute_id(getRouteId());
                cashCheckModel.setTotal_amount(net_total_amount);
                cashCheckModel.setF_rej_amount(f_rej_amount);
                cashCheckModel.setM_rej_amount(m_rej_amount);
                cashCheckModel.setLeftover_amount(leftover_amount);
                cashCheckModel.setSample_amount(sample_amount);
                cashCheckModel.setCashier_receive_amount(cashier_receive_amount);
                cashCheckModel.setSupervisor_received_amount(supervisor_received_amount);
                cashCheckModel.setShort_excess_amount(short_excess_amount);
                cashCheckModel.setDiscount_amount(discount_amount);

                cashierSyncModel.setCashCheckModel(cashCheckModel);

                //sync the above cashierSyncModel to server.
                String json = new Gson().toJson(cashierSyncModel);
                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String imei_number = Utility.readPreference(getContext(), Utility.DEVICE_IMEI);
                String supervisor_paycode = "android";
                syncRouteByCashier(getRouteModel().getDepotId(), getRouteId(), getRouteModel().getRouteManagementId(), getRouteModel().getCashierCode(),
                        supervisor_paycode, jObj, imei_number);
            }
        });
    }

    // sync process retrofit call
    /*retrofit call test example*/
    public void syncRouteByCashier(String depotId, final String route_id, String routeManagementId, String cashierPaycode, String supervisor_paycode,
                                   JSONObject cashier_data, String imei_no) {
        updateBarHandler.post(new Runnable() {
            public void run() {
                RetrofitUtil.showDialog(getContext(), getString(R.string.str_synchronizing_data));
            }
        });
        RetrofitService service = RetrofitUtil.retrofitClient();
        Call<syncResponse> apiCall = service.syncRouteCashierCheck(depotId, route_id, routeManagementId, cashierPaycode, supervisor_paycode,
                cashier_data.toString(), imei_no);
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
