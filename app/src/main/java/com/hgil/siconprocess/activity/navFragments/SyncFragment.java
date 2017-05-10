package com.hgil.siconprocess.activity.navFragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hgil.siconprocess.R;
import com.hgil.siconprocess.activity.fragments.invoiceSyncModel.CashCheck;
import com.hgil.siconprocess.activity.fragments.invoiceSyncModel.CollectionCashModel;
import com.hgil.siconprocess.activity.fragments.invoiceSyncModel.CollectionCrateModel;
import com.hgil.siconprocess.activity.fragments.invoiceSyncModel.CrateCheck;
import com.hgil.siconprocess.activity.fragments.invoiceSyncModel.CrateStockCheck;
import com.hgil.siconprocess.activity.fragments.invoiceSyncModel.RejectionDetailModel;
import com.hgil.siconprocess.activity.fragments.invoiceSyncModel.SyncData;
import com.hgil.siconprocess.activity.fragments.invoiceSyncModel.SyncInvoiceDetailModel;
import com.hgil.siconprocess.activity.fragments.invoiceSyncModel.VanStockCheck;
import com.hgil.siconprocess.base.BaseFragment;
import com.hgil.siconprocess.database.masterTables.CustomerRouteMappingView;
import com.hgil.siconprocess.database.masterTables.DepotInvoiceView;
import com.hgil.siconprocess.database.tables.CustomerRejectionTable;
import com.hgil.siconprocess.database.tables.InvoiceOutTable;
import com.hgil.siconprocess.database.tables.MarketProductTable;
import com.hgil.siconprocess.database.tables.NextDayOrderTable;
import com.hgil.siconprocess.database.tables.PaymentTable;
import com.hgil.siconprocess.retrofit.RetrofitService;
import com.hgil.siconprocess.retrofit.RetrofitUtil;
import com.hgil.siconprocess.retrofit.loginResponse.syncResponse;
import com.hgil.siconprocess.utils.ui.SampleDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
    private CustomerRouteMappingView routeCustomerView;
    private DepotInvoiceView depot_invoice;
    private InvoiceOutTable invoiceOutTable;
    private CustomerRejectionTable rejectionTable;
    private PaymentTable paymentTable;
    private NextDayOrderTable nextDayOrderTable;
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
                initiateDataSync();
            }
        });
    }

    public void initializeTableObjects() {
        routeCustomerView = new CustomerRouteMappingView(getContext());
        depot_invoice = new DepotInvoiceView(getContext());
        invoiceOutTable = new InvoiceOutTable(getContext());
        rejectionTable = new CustomerRejectionTable(getContext());
        paymentTable = new PaymentTable(getContext());
        nextDayOrderTable = new NextDayOrderTable(getContext());
        marketProductTable = new MarketProductTable(getContext());
    }

    /*preparing data to sync whole day process*/
    private void initiateDataSync() {
        /*invoice data preparation*/

        // invoice sync
        //TODO
        ArrayList<SyncInvoiceDetailModel> syncInvoice = invoiceOutTable.syncInvoice();

        // get rejections details
        ArrayList<SyncInvoiceDetailModel> syncInvoiceRejection = rejectionTable.syncRejection(getRouteId());
        ArrayList<RejectionDetailModel> syncRejectDetails = rejectionTable.syncRejectionDetails(getRouteId());

        ArrayList<CollectionCashModel> cashCollection = paymentTable.syncCashDetail();
        ArrayList<CollectionCrateModel> crateCollection = paymentTable.syncCrateDetail();
        CrateStockCheck crateStock = paymentTable.syncCrateStock(getRouteId());

        // below commented values are not mendatory to validate here
        // these values are validated by the head cashier only.
        // cashier total verification
        CashCheck cashCheck = paymentTable.routeTotalAmountCollection();
        
        /*get items loaded and received stock*/
        int crates_loaded_in_van = routeCustomerView.vanTotalCrate();
        // int crates_delivered_by_cashier = 0; // ui task

        CrateCheck crateCheck = new CrateCheck();
        crateCheck.setCrates_loaded(crates_loaded_in_van);
        //crateCheck.setCrate_delivered(crates_delivered_by_cashier);

        // cross check items left and rejections
        int items_loaded = depot_invoice.totalItemCount();
        int items_sold = invoiceOutTable.soldItemCount();
        int items_leftover = items_loaded - items_sold;
        //int items_delivered = 0; //ui
        int items_fresh_rejection = rejectionTable.routeFreshRejection();
        // int items_fresh_rej_received = 0;   //ui
        int items_market_rejection = rejectionTable.routeMarketRejection();
        // int items_market_rej_received = 0;  //ui

        VanStockCheck vanStockCheck = new VanStockCheck();
        vanStockCheck.setItems_loaded(items_loaded);
        vanStockCheck.setItems_sold(items_sold);
        vanStockCheck.setItems_leftover(items_leftover);
        // vanStockCheck.setItem_delivered(items_delivered);
        vanStockCheck.setFresh_rejections(items_fresh_rejection);
        //  vanStockCheck.setFresh_rejections_delivered(items_fresh_rej_received);
        vanStockCheck.setMarket_rejection(items_market_rejection);
        // vanStockCheck.setMarket_rejection_delivered(items_market_rej_received);

        // get van actual stock
        /*
        * 1. items loaded
        * 2. items sold
        * 3. rejection
        * 4. item_left over
        * 5. total item in van(loaded-sold+rejection)
        * */

        // finally convert all object and array data into jsonObject and send as object data to server side api;
        SyncData syncData = new SyncData();

        syncData.setSyncInvoice(syncInvoice);
        syncData.setSyncInvoiceRejection(syncInvoiceRejection);
        syncData.setSyncRejectDetails(syncRejectDetails);
        syncData.setCashCollection(cashCollection);
        syncData.setCrateCollection(crateCollection);
        syncData.setCrateStock(crateStock);
        syncData.setCashCheck(cashCheck);
        syncData.setCrateCheck(crateCheck);
        syncData.setVanStockCheck(vanStockCheck);
        syncData.setChequeCollection(paymentTable.syncChequeDetail(routeId));
        syncData.setArrNextDayOrder(nextDayOrderTable.getRouteOrder());
        syncData.setArrMaketProductsSummary(marketProductTable.getRouteOrder());

        String json = new Gson().toJson(syncData);
        JSONObject jObj = null;
        try {
            jObj = new JSONObject(json);
            Log.e(TAG, json);
            Log.e(TAG + "1", jObj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // make retrofit request call
        syncRouteInvoice(getRouteId(), jObj);
    }

    // sync process retrofit call
    /*retrofit call test example*/
    public void syncRouteInvoice(final String route_id, JSONObject route_data) {
        updateBarHandler.post(new Runnable() {
            public void run() {
                RetrofitUtil.showDialog(getContext(), getString(R.string.str_synchronizing_data));
            }
        });
        RetrofitService service = RetrofitUtil.retrofitClient();
        Call<syncResponse> apiCall = service.syncRouteData(route_id, route_data.toString());
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
        // nextDayOrderTable.eraseTable();
        marketProductTable.eraseTable();
    }

}
