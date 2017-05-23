package com.hgil.siconprocess.activity.fragments.finalPayment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.hgil.siconprocess.R;
import com.hgil.siconprocess.activity.fragments.finalPayment.cashierSync.CashierSyncModel;
import com.hgil.siconprocess.activity.fragments.invoiceSyncModel.CashCheck;
import com.hgil.siconprocess.activity.fragments.invoiceSyncModel.cashierSync.CashCheckModel;
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
    @BindView(R.id.etCashCollected)
    EditText etCashCollected;
    @BindView(R.id.etCashReceived)
    EditText etCashReceived;
    @BindView(R.id.etChequeCollected)
    EditText etChequeCollected;
    @BindView(R.id.etChequeReceived)
    EditText etChequeReceived;
    private double cash_collected, cash_delivered_by_cashier, cheque_collected, cheque_amount_delivered;
    private CashierSyncModel cashierSyncModel;
    private CashCheck cashCheck;
    private Handler updateBarHandler;

    public CashCheckFragment() {
        // Required empty public constructor
    }

    public static CashCheckFragment newInstance(CashierSyncModel cashierSyncModel){
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

        // cashier total verification
        cashCheck = paymentTable.routeTotalAmountCollection();
        cash_collected = cashCheck.getCash_collected();
        cash_delivered_by_cashier = cashCheck.getCash_delivered();
        cheque_collected = cashCheck.getCheque_amount();
        cheque_amount_delivered = cashCheck.getCheque_amount_delivered();

        etCashCollected.setText(String.valueOf(cash_collected));
        etCashReceived.setText(String.valueOf(cash_delivered_by_cashier));
        etChequeCollected.setText(String.valueOf(cheque_collected));
        etChequeReceived.setText(String.valueOf(cheque_amount_delivered));

        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cash_delivered_by_cashier = Utility.getDouble(etCashReceived.getText().toString());
                cheque_amount_delivered = Utility.getDouble(etChequeReceived.getText().toString());

                CashCheck cashCheck = new CashCheck();
                cashCheck.setCash_collected(cash_collected);
                cashCheck.setCash_delivered(cash_delivered_by_cashier);
                cashCheck.setCheque_amount(cheque_collected);
                cashCheck.setCheque_amount_delivered(cheque_amount_delivered);

                //TODO cash collection verifying
                cashierSyncModel.setCashCheckModel(new CashCheckModel());

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
                String routeManagementId = "android";
                syncRouteByCashier(getRouteModel().getDepotId(), getRouteId(), routeManagementId, getRouteModel().getCashierCode(),
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
