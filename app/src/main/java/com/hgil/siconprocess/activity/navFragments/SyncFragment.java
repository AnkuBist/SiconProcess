package com.hgil.siconprocess.activity.navFragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hgil.siconprocess.R;
import com.hgil.siconprocess.base.BaseFragment;
import com.hgil.siconprocess.database.masterTables.CustomerItemPriceTable;
import com.hgil.siconprocess.database.tables.CustomerRejectionTable;
import com.hgil.siconprocess.database.tables.InvoiceOutTable;
import com.hgil.siconprocess.database.tables.MarketProductTable;
import com.hgil.siconprocess.database.tables.PaymentTable;
import com.hgil.siconprocess.retrofit.RetrofitService;
import com.hgil.siconprocess.retrofit.RetrofitUtil;
import com.hgil.siconprocess.retrofit.loginResponse.syncResponse;
import com.hgil.siconprocess.syncPOJO.invoiceSyncModel.SyncData;
import com.hgil.siconprocess.utils.Utility;
import com.hgil.siconprocess.utils.ui.SampleDialog;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncFragment extends BaseFragment {

    @BindView(R.id.tvRouteName)
    TextView tvRouteName;
    @BindView(R.id.btnSyncData)
    Button btnSyncData;
    private String TAG = this.getClass().getName();
    private InvoiceOutTable invoiceOutTable;
    private CustomerRejectionTable rejectionTable;
    private PaymentTable paymentTable;
    private MarketProductTable marketProductTable;
    private Handler updateBarHandler;

    public SyncFragment() {
        // Required empty public constructor
    }

    public static SyncFragment newInstance() {
        SyncFragment fragment = new SyncFragment();
        return fragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_sync;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle("Sync");
        hideSaveButton();
        updateBarHandler = new Handler();

        if (getRouteName() != null)
            tvRouteName.setText(getRouteName());

        initializeTableObjects();
        btnSyncData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBarHandler.post(new Runnable() {
                    public void run() {
                        RetrofitUtil.showDialog(getContext(), getString(R.string.str_synchronizing_data));
                    }
                });
                // call sync data here
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        initiateDataSync();
                    }
                }).start();
            }
        });
    }

    public void initializeTableObjects() {
        invoiceOutTable = new InvoiceOutTable(getContext());
        rejectionTable = new CustomerRejectionTable(getContext());
        paymentTable = new PaymentTable(getContext());
        marketProductTable = new MarketProductTable(getContext());
    }

    /*preparing data to sync whole day process*/
    private void initiateDataSync() {
        // finally convert all object and array data into jsonObject and send as object data to server side api;
        SyncData syncData = new SyncData();
        /*invoice data preparation*/
        // invoice sync
        syncData.setSyncInvoice(invoiceOutTable.syncCompletedInvoice());
        syncData.setSyncInvoiceRejection(rejectionTable.syncCompletedRejection(getRouteId()));

        syncData.setSyncRejDetails(rejectionTable.syncCompletedRejectionDetails(getRouteId()));
        syncData.setChequeCollection(paymentTable.syncCompletedChequeDetail(routeId));
        //syncData.setArrMarketProductsSummary(marketProductTable.routeCompletedMarketProductDetails());
        syncData.setCrateCollection(paymentTable.syncCompletedCrateDetail());

        /*actual database synchronisation*/
        syncData.setSyncInvoiceSaleRej(new CustomerItemPriceTable(getContext()).syncInvoiceSaleRej(getRouteId()));
        syncData.setCashCollection(paymentTable.syncCompletedCashDetail());

        String json = new Gson().toJson(syncData);
        JSONObject jObj = null;
        try {
            jObj = new JSONObject(json);
            Log.e(TAG, json);
            Log.e(TAG + "1", jObj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String imei_number = Utility.readPreference(getContext(), Utility.DEVICE_IMEI);

        // make retrofit request call
        syncRouteInvoice(getRouteModel().getDepotId(), getRouteId(), getRouteModel().getRouteManagementId(),
                getRouteModel().getCashierCode(), jObj, imei_number);
    }

    // sync process retrofit call
    /*retrofit call test example*/
    public void syncRouteInvoice(String depot_id, final String route_id, String route_management_id,
                                 String cashier_paycode, JSONObject route_data, String imei_number) {
        updateBarHandler.post(new Runnable() {
            public void run() {
                RetrofitUtil.updateDialogTitle(getString(R.string.str_synchronizing_data));
            }
        });
        RetrofitService service = RetrofitUtil.retrofitClient();
        Call<syncResponse> apiCall = service.syncRouteData(depot_id, route_id, route_management_id, cashier_paycode,
                route_data.toString(), imei_number);
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
                    //RetrofitUtil.showToast(getContext(), syncResponse.getStrMessage());

                    new SampleDialog("", syncResponse.getStrMessage(), true, getContext());

                    //erase all masterTables data
                    //eraseAllSyncTableData();

                    //after saving all values to database start new activity
                    //startActivity(new Intent(LoginActivity.this, NavBaseActivity.class));
                    // finish();
                    //overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                } else {
                    // RetrofitUtil.showToast(getContext(), syncResponse.getStrMessage());
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

    //TODO
    // erase all local tables made to sync data
    public void eraseAllSyncTables() {
        rejectionTable.eraseTable();
        invoiceOutTable.eraseTable();
        paymentTable.eraseTable();
        marketProductTable.eraseTable();
    }

}
