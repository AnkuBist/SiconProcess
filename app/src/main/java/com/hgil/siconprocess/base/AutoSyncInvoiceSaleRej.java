package com.hgil.siconprocess.base;

import android.os.Handler;

import com.google.gson.Gson;
import com.hgil.siconprocess.R;
import com.hgil.siconprocess.database.tables.CustomerRejectionTable;
import com.hgil.siconprocess.database.tables.InvoiceOutTable;
import com.hgil.siconprocess.retrofit.RetrofitService;
import com.hgil.siconprocess.retrofit.RetrofitUtil;
import com.hgil.siconprocess.retrofit.loginResponse.syncResponse;
import com.hgil.siconprocess.syncPOJO.SRouteModel;
import com.hgil.siconprocess.syncPOJO.autoSaleUpdate.SyncInvoice;
import com.hgil.siconprocess.utils.Utility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mohan.giri on 25-04-2017.
 */

public class AutoSyncInvoiceSaleRej extends BaseFragment {

    /*sync process*/
    private Handler updateBarHandler;

    public AutoSyncInvoiceSaleRej() {
    }

    public void startSyncData() {
        updateBarHandler = new Handler();
       /* updateBarHandler.post(new Runnable() {
            public void run() {
                RetrofitUtil.showDialog(getContext(), getString(R.string.str_synchronizing_data));
            }
        });*/

        // call sync data here
        new Thread(new Runnable() {
            @Override
            public void run() {
                SyncInvoice syncInvoice = new SyncInvoice();
                syncInvoice.setArrInvSale(new InvoiceOutTable(getContext()).autoSyncInvoiceSale());
                syncInvoice.setArrInvRej(new CustomerRejectionTable(getContext()).autoSyncInvoiceRejection());

                String routeDetails = new Gson().toJson(routeModel());
                String invoice_sync = new Gson().toJson(syncInvoice);
                synchronizedDataResponse(routeDetails, invoice_sync);
            }
        }).start();
    }

    private SRouteModel routeModel() {
        //sync the above syncVanClose to server.
        String imei_number = Utility.readPreference(getContext(), Utility.DEVICE_IMEI);

        SRouteModel routeModel = new SRouteModel();
        routeModel.setLoginId(getLoginId());
        routeModel.setRouteId(getRouteId());
        routeModel.setRouteName(getRouteName());
        routeModel.setDepotId(getRouteModel().getDepotId());
        routeModel.setRouteManagementId(getRouteModel().getRouteManagementId());
        routeModel.setCashierCode(getRouteModel().getCashierCode());
        routeModel.setSubCompanyId(getRouteModel().getSubCompanyId());

        routeModel.setImeiNo(imei_number);
        return routeModel;
    }

    /*retrofit call test to fetch data from server*/
    public void synchronizedDataResponse(String routeDetails, String invoiceSync) {
        /*updateBarHandler.post(new Runnable() {
            public void run() {
                RetrofitUtil.updateDialogTitle(getString(R.string.str_synchronizing_data));
            }
        });*/
        RetrofitService service = RetrofitUtil.retrofitClient();
        Call<syncResponse> apiCall = service.syncInvoiceSaleRej(routeDetails, invoiceSync);
        apiCall.enqueue(new Callback<syncResponse>() {
            @Override
            public void onResponse(Call<syncResponse> call, Response<syncResponse> response) {
                /*updateBarHandler.post(new Runnable() {
                    public void run() {
                        RetrofitUtil.updateDialogTitle(getString(R.string.str_synchronizing_data_started));
                    }
                });*/
                try {
                    final syncResponse syncResult = response.body();

                    // rest call to read data from api service
                    if (syncResult.getReturnCode()) {
                        //async process
                        RetrofitUtil.showToast(getContext(), syncResult.getStrMessage());
                    } else {
                       /* updateBarHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                RetrofitUtil.hideDialog();
                            }
                        }, 500);*/
                        RetrofitUtil.showToast(getContext(), syncResult.getStrMessage());

                    }
                } catch (Exception e) {
                  /*  updateBarHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            RetrofitUtil.hideDialog();
                        }
                    }, 500);*/
                    RetrofitUtil.showToast(getContext(), getString(R.string.str_error_sync_data));

                }
            }

            @Override
            public void onFailure(Call<syncResponse> call, Throwable t) {
              /*  updateBarHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        RetrofitUtil.hideDialog();
                    }
                }, 500);*/
                // show some error toast or message to display the api call issue
                RetrofitUtil.showToast(getContext(), getString(R.string.str_retrofit_failure));

            }
        });
    }

    @Override
    protected int getFragmentLayout() {
        return 0;
    }
}
