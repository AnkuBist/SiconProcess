package com.hgil.siconprocess.activity.fragments.vanClose;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hgil.siconprocess.R;
import com.hgil.siconprocess.base.BaseFragment;
import com.hgil.siconprocess.database.dbModels.PaymentModel;
import com.hgil.siconprocess.database.masterTables.CustomerItemPriceTable;
import com.hgil.siconprocess.database.masterTables.CustomerRouteMappingView;
import com.hgil.siconprocess.database.masterTables.DepotInvoiceView;
import com.hgil.siconprocess.database.masterTables.ProductView;
import com.hgil.siconprocess.database.masterTables.RouteView;
import com.hgil.siconprocess.database.tables.CustomerRejectionTable;
import com.hgil.siconprocess.database.tables.InvoiceOutTable;
import com.hgil.siconprocess.database.tables.PaymentTable;
import com.hgil.siconprocess.retrofit.RetrofitService;
import com.hgil.siconprocess.retrofit.RetrofitUtil;
import com.hgil.siconprocess.retrofit.loginResponse.syncResponse;
import com.hgil.siconprocess.syncPOJO.FinalPaymentModel;
import com.hgil.siconprocess.syncPOJO.SRouteModel;
import com.hgil.siconprocess.syncPOJO.invoiceSyncModel.SyncInvoiceDetailModel;
import com.hgil.siconprocess.syncPOJO.vanCloseModel.CrateStockCheck;
import com.hgil.siconprocess.syncPOJO.vanCloseModel.ItemStockCheck;
import com.hgil.siconprocess.syncPOJO.vanCloseModel.SyncVanClose;
import com.hgil.siconprocess.utils.Constant;
import com.hgil.siconprocess.utils.UtilBillNo;
import com.hgil.siconprocess.utils.Utility;
import com.hgil.siconprocess.utils.ui.SampleDialog;

import java.util.ArrayList;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class VanCloseItemCheckFragment extends BaseFragment {

    /*crate check*/
    @BindView(R.id.etCrateOpening)
    EditText etCrateOpening;
    @BindView(R.id.etCrateIssued)
    EditText etCrateIssued;
    @BindView(R.id.etCrateReceived)
    EditText etCrateReceived;
    @BindView(R.id.etCrateBalance)
    EditText etCrateBalance;

    @BindView(R.id.rvItemStockCheck)
    RecyclerView rvItemStockCheck;
    @BindView(R.id.tvEmpty)
    TextView tvEmpty;

    @BindView(R.id.btnVanClose)
    Button btnVanClose;

    private Handler updateBarHandler;

    private CrateStockCheck crateStockCheck;

    private RouteView routeView;

    private VanCloseItemStockCheckAdapter vanCloseItemStockCheckAdapter;
    private ProductView productView;
    private ArrayList<ItemStockCheck> arrItemStockCheck = new ArrayList<>();

    public VanCloseItemCheckFragment() {
        // Required empty public constructor
    }

    public static VanCloseItemCheckFragment newInstance() {
        VanCloseItemCheckFragment fragment = new VanCloseItemCheckFragment();
        return fragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_van_close_item_stock;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hideSaveButton();
        setTitle("Van Closing");
        updateBarHandler = new Handler();

        routeView = new RouteView(getContext());

        /*crate stock validations*/
        // crate stock verifying
        crateStockCheck = new PaymentTable(getContext()).syncCrateStock(getRouteId(), getRouteModel().getCrateLoading());

        etCrateOpening.setText(String.valueOf(crateStockCheck.getOpening()));
        etCrateIssued.setText(String.valueOf(crateStockCheck.getIssued()));
        if (crateStockCheck.getReceived() > 0)
            etCrateReceived.setText(String.valueOf(crateStockCheck.getReceived()));
        etCrateBalance.setText(String.valueOf(crateStockCheck.getBalance()));

        etCrateReceived.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    crateStockCheck.setReceived(Integer.parseInt(etCrateReceived.getText().toString()));
                    etCrateBalance.setText(String.valueOf(crateStockCheck.getOpening() - crateStockCheck.getIssued() + crateStockCheck.getReceived()));
                } else {
                    etCrateBalance.setText(String.valueOf(crateStockCheck.getOpening() - crateStockCheck.getIssued()));
                }
            }
        });

        /*item stock validation*/
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvItemStockCheck.setLayoutManager(linearLayoutManager);

        productView = new ProductView(getContext());
        arrItemStockCheck.addAll(productView.checkItemStock());
        vanCloseItemStockCheckAdapter = new VanCloseItemStockCheckAdapter(getContext(), arrItemStockCheck);
        rvItemStockCheck.setAdapter(vanCloseItemStockCheckAdapter);

        // final submit process
        btnVanClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBarHandler.post(new Runnable() {
                    public void run() {
                        RetrofitUtil.showDialog(getContext(), getString(R.string.str_van_close));
                    }
                });

                // call van close here
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        initializeVanClose();
                    }
                }).start();
            }
        });
    }

    private void initializeVanClose() {
        SyncVanClose syncVanClose = new SyncVanClose();

        /*crate check*/
        crateStockCheck.setReceived(Utility.getInteger(etCrateReceived.getText().toString()));
        crateStockCheck.setBalance(Utility.getInteger(etCrateBalance.getText().toString()));

        // crate stock verifying
        syncVanClose.setCrateStockCheck(crateStockCheck);

        /*item stock*/
        syncVanClose.setArrItemStock(arrItemStockCheck);

        // route retail sale
        /*make retail sale and payment to local data here only*/
        syncVanClose.setArrRetailSale(varianceRetailSale(arrItemStockCheck));

        /*update here route close status and details*/
        vanCloseFinalPayment(arrItemStockCheck);

        //sync the above syncVanClose to server.
        String imei_number = Utility.readPreference(getContext(), Utility.DEVICE_IMEI);
        String supervisor_paycode = "android";      //TODO

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
        String vanCloseData = new Gson().toJson(syncVanClose);

        vanClose(routeDetails, vanCloseData);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (arrItemStockCheck.size() == 0) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvItemStockCheck.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvItemStockCheck.setVisibility(View.VISIBLE);
        }
    }

    private String getBill_no() {
        InvoiceOutTable invoiceOutTable = new InvoiceOutTable(getContext());
        CustomerRejectionTable rejectionTable = new CustomerRejectionTable(getContext());

        String tempBill = null;
        String expectedLastBillNo = null;
        double max_bill_1 = 0, max_bill_2 = 0;

        String last_max_bill_1 = invoiceOutTable.returnMaxBillNo();
        String last_max_bill_2 = rejectionTable.returnMaxBillNo();

        // case to find the last max bill no
        if (last_max_bill_1 != null && !last_max_bill_1.isEmpty() && last_max_bill_1.length() == 14)
            max_bill_1 = Double.valueOf(last_max_bill_1);
        if (last_max_bill_2 != null && !last_max_bill_2.isEmpty() && last_max_bill_2.length() == 14)
            max_bill_2 = Double.valueOf(last_max_bill_2);

        if (max_bill_1 == max_bill_2)
            expectedLastBillNo = last_max_bill_1;
        else if (max_bill_1 > max_bill_2)
            expectedLastBillNo = last_max_bill_1;
        else if (max_bill_2 > max_bill_1)
            expectedLastBillNo = last_max_bill_2;
        else
            expectedLastBillNo = getRouteModel().getExpectedLastBillNo();

        tempBill = UtilBillNo.generateBillNo(getRouteModel().getRecId(), expectedLastBillNo);

        return tempBill;
    }

    // generate temporary invoice for the retails customer for variance items
    private ArrayList<SyncInvoiceDetailModel> varianceRetailSale(ArrayList<ItemStockCheck> arrItemStockCheck) {
        CustomerItemPriceTable customerItemPriceTable = new CustomerItemPriceTable(getContext());
        CustomerRouteMappingView customerRouteMappingView = new CustomerRouteMappingView(getContext());
        String routeManagementId = getRouteModel().getRouteManagementId();
        String invoiceNumber = new DepotInvoiceView(getContext()).commonInvoiceNumber();
        String invoiceDate = Utility.getCurDate();
        String billNo = getBill_no();
        String cashierCode = getRouteModel().getCashierCode();
        String retailCustomerId = customerRouteMappingView.retailCustomer();

        double retailSaleAmount = 0;

        ArrayList<SyncInvoiceDetailModel> arrVarianceInvoice = new ArrayList<>();
        for (ItemStockCheck itemStockCheck : arrItemStockCheck) {
            int variance = itemStockCheck.getItem_variance();
            if (variance > 0) {
                int saleCount = variance;
                SyncInvoiceDetailModel syncModel = new SyncInvoiceDetailModel();
                String item_id = itemStockCheck.getItem_id();

                double item_price = customerItemPriceTable.itemPrice(item_id);
                double discount_price = 0;
                double discount_percentage = 0;
                String discount_type = "";
                double discounted_price = item_price;
                int sample = 0;

                syncModel.setItem_id(item_id);
                syncModel.setCustomer_id(retailCustomerId);

                syncModel.setItem_price(item_price);
                syncModel.setDisc_price(discount_price);
                syncModel.setDisc_percentage(discount_percentage);
                syncModel.setDisc_type(discount_type);
                syncModel.setDiscounted_price(discounted_price);
                syncModel.setSample(sample);

                syncModel.setRoute_management_id(routeManagementId);
                syncModel.setBill_no(billNo);
                syncModel.setInvoice_no(invoiceNumber);
                syncModel.setInvoice_date(invoiceDate);
                syncModel.setRoute_id(getRouteId());
                syncModel.setCashier_code(cashierCode);          // CASHIER_CODE
                syncModel.setSale_count(saleCount);

                int fresh_rej = 0;
                int market_rej = 0;

                // final details
                int actual_sale_count = saleCount - market_rej;
                syncModel.setActual_sale_count(actual_sale_count);

                //can be calculated here only
                syncModel.setTotal_sale_amount(actual_sale_count * discounted_price);
                syncModel.setTotal_disc_amount((item_price - discounted_price) * actual_sale_count);
                syncModel.setF_rej_amount(discounted_price * fresh_rej);
                syncModel.setM_rej_amount(discounted_price * market_rej);

                retailSaleAmount += syncModel.getTotal_sale_amount();

                arrVarianceInvoice.add(syncModel);
            }
        }

        /*update this retail sale to local and also make payment for this*/
        InvoiceOutTable invoiceOutTable = new InvoiceOutTable(getContext());
        invoiceOutTable.invoiceVarianceRetailSale(arrVarianceInvoice);
        invoiceOutTable.updateCustInvStatus(customer_id, Constant.STATUS_COMPLETE);
        PaymentTable paymentTable = new PaymentTable(getContext());

        PaymentModel paymentModel = new PaymentModel();
        paymentModel.setCustomerId(retailCustomerId);
        paymentModel.setCustomerName(customerRouteMappingView.customerName(retailCustomerId));
        paymentModel.setSaleAmount(retailSaleAmount);
        paymentModel.setCashPaid(retailSaleAmount);
        paymentModel.setTotalPaidAmount(retailSaleAmount);
        paymentModel.setImei_no("");
        paymentModel.setLat_lng("");
        paymentModel.setLogin_id(getLoginId());

        paymentTable.varianceInvoicePayment(paymentModel);

        return arrVarianceInvoice;
    }

    //update van close final payment details
    private void vanCloseFinalPayment(ArrayList<ItemStockCheck> arrItemStockCheck) {
        PaymentTable paymentTable = new PaymentTable(getContext());

        double m_rej_amount = 0, f_rej_amount = 0, sample_amount = 0, leftover_amount = 0;  //supervisor leftover amount

        // final payment information
        double net_total_amount = paymentTable.getRouteSale();       //net sale amount

        // get sample and rejection amount from last table values
        for (ItemStockCheck itemStockCheck : arrItemStockCheck) {
            m_rej_amount += itemStockCheck.getItem_price() * itemStockCheck.getMarket_rejection();
            f_rej_amount += itemStockCheck.getItem_price() * itemStockCheck.getFresh_rejection();
            sample_amount += itemStockCheck.getItem_price() * itemStockCheck.getSample();
            leftover_amount += itemStockCheck.getItem_price() * itemStockCheck.getPhysical_leftover();
        }

        double cashier_receive_amount = paymentTable.routeCashPaidAmount();      // cashier collected amount upi+cash....supervisor entered figure

        FinalPaymentModel finalPaymentModel = new FinalPaymentModel();

        finalPaymentModel.setRoute_id(getRouteId());
        finalPaymentModel.setTotal_amount(net_total_amount);
        finalPaymentModel.setF_rej_amount(f_rej_amount);
        finalPaymentModel.setM_rej_amount(m_rej_amount);
        finalPaymentModel.setLeftover_amount(leftover_amount);
        finalPaymentModel.setSample_amount(sample_amount);
        finalPaymentModel.setCashier_receive_amount(cashier_receive_amount);

        routeView.updateRouteFinalPayment(finalPaymentModel);
    }

    // sync process retrofit call
    public void vanClose(String routeDetails, String vanCloseData) {
        updateBarHandler.post(new Runnable() {
            public void run() {
                RetrofitUtil.updateDialogTitle(getString(R.string.str_van_close));
            }
        });
        RetrofitService service = RetrofitUtil.retrofitClient();
        Call<syncResponse> apiCall = service.syncRouteVanClose(routeDetails, vanCloseData);
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
                    //van close status
                    routeView.updateVanClose(getRouteId());

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
