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
import com.hgil.siconprocess.syncPOJO.invoiceSyncModel.SyncData;
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

    private DepotInvoiceView depotInvoiceView;
    private ProductView productView;
    private RouteView routeView;
    private CustomerRouteMappingView customerRouteMappingView;
    private CustomerItemPriceTable customerItemPriceTable;
    private InvoiceOutTable invoiceOutTable;
    private CustomerRejectionTable rejectionTable;
    private PaymentTable paymentTable;

    private VanCloseItemStockCheckAdapter vanCloseItemStockCheckAdapter;
    private ArrayList<ItemStockCheck> arrItemStockCheck = new ArrayList<>();

    public VanCloseItemCheckFragment() {
        // Required empty public constructor
    }

    public static VanCloseItemCheckFragment newInstance(String supervisorCode) {
        VanCloseItemCheckFragment fragment = new VanCloseItemCheckFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SUPERVISOR_CODE, supervisorCode);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            supervisor_code = getArguments().getString(SUPERVISOR_CODE);
        }
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

        initializeTableObjects();

        /*crate stock validations*/
        // crate stock verifying
        crateStockCheck = paymentTable.syncCrateStock(getRouteId(), getRouteModel().getCrateLoading());

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

        arrItemStockCheck.addAll(productView.checkItemStock());
        vanCloseItemStockCheckAdapter = new VanCloseItemStockCheckAdapter(getContext(), arrItemStockCheck);
        rvItemStockCheck.setAdapter(vanCloseItemStockCheckAdapter);

        // final submit process
        btnVanClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBarHandler.post(new Runnable() {
                    public void run() {
                        RetrofitUtil.showDialog(getContext(), getString(R.string.str_gathering_v_close_data));
                    }
                });

                final String retailCustomerId = customerRouteMappingView.retailCustomer();

                // call van close here
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String routeDetails = new Gson().toJson(routeModel());
                        String vanCloseData = new Gson().toJson(vanCloseData(retailCustomerId));
                        String syncRouteData = new Gson().toJson(syncRouteData());

                        routeVanClose(retailCustomerId, routeDetails, syncRouteData, vanCloseData);
                    }
                }).start();
            }
        });
    }

    //1. route model
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
        routeModel.setSupervisorId(supervisor_code);

        routeModel.setImeiNo(imei_number);
        return routeModel;
    }

    //2. van close data
    private SyncVanClose vanCloseData(String retailCustomerId) {
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
        varianceRetailSale(retailCustomerId, arrItemStockCheck);

        return syncVanClose;
    }

    //3. sync route data
    private SyncData syncRouteData() {
        // finally convert all object and array data into jsonObject and send as object data to server side api;
        SyncData syncData = new SyncData();
        /*invoice data preparation*/
        // invoice sync

        //TODO
        /*syncData.setSyncInvoice(invoiceOutTable.syncCompletedInvoice());
        syncData.setSyncInvoiceRejection(rejectionTable.syncCompletedRejection(getRouteId()));
        syncData.setSyncRejDetails(rejectionTable.syncCompletedRejectionDetails(getRouteId()));
        syncData.setChequeCollection(paymentTable.syncCompletedChequeDetail(routeId));*/
        //syncData.setArrMarketProductsSummary(marketProductTable.routeCompletedMarketProductDetails());

        /*actual database synchronisation*/
        syncData.setArrClosedCustDetails(customerRouteMappingView.closedOutletDetails());
        syncData.setSyncInvoiceSaleRej(customerItemPriceTable.syncInvoiceSaleRej(getRouteId()));
        syncData.setCashCollection(paymentTable.syncCompletedCashDetail());
        syncData.setChequeCollection(paymentTable.syncCompletedChequeDetail(getRouteId()));
        syncData.setCrateCollection(paymentTable.syncCompletedCrateDetail());

        return syncData;
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

    private String getBill_no(String retailCustomerId) {
        String tempBill = null;
        String expectedLastBillNo = null;
        double max_bill_1 = 0, max_bill_2 = 0;
        String tempBill1 = invoiceOutTable.returnCustomerBillNo(retailCustomerId);
        String tempBill2 = rejectionTable.returnCustomerBillNo(retailCustomerId);

        String last_max_bill_1 = invoiceOutTable.returnMaxBillNo();
        String last_max_bill_2 = rejectionTable.returnMaxBillNo();

        if (tempBill1 != null && !tempBill1.isEmpty() && tempBill1.length() == 14)
            return tempBill1;
        else if (tempBill2 != null && !tempBill2.isEmpty() && tempBill2.length() == 14)
            return tempBill2;

        // case to find the last max bill no
        if (last_max_bill_1 != null && !last_max_bill_1.isEmpty() && last_max_bill_1.length() == 14)
            max_bill_1 = Double.valueOf(last_max_bill_1);
        if (last_max_bill_2 != null && !last_max_bill_2.isEmpty() && last_max_bill_2.length() == 14)
            max_bill_2 = Double.valueOf(last_max_bill_2);

        if (max_bill_1 == max_bill_2 && max_bill_1 != 0 && max_bill_2 != 0)
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
    private void varianceRetailSale(String retailCustomerId, ArrayList<ItemStockCheck> arrItemStockCheck) {
        String routeManagementId = getRouteModel().getRouteManagementId();
        String invoiceNumber = depotInvoiceView.commonInvoiceNumber();
        String invoiceDate = Utility.getCurDate();
        String billNo = getBill_no(retailCustomerId);
        String cashierCode = getRouteModel().getCashierCode();

        double retailSaleAmount = 0;

        ArrayList<SyncInvoiceDetailModel> arrVarianceInvoice = new ArrayList<>();
        for (ItemStockCheck itemStockCheck : arrItemStockCheck) {
            int variance = itemStockCheck.getItem_variance();
            if (variance > 0) {
                int saleCount = variance;
                SyncInvoiceDetailModel syncModel = new SyncInvoiceDetailModel();
                String item_id = itemStockCheck.getItem_id();

                double item_price = customerItemPriceTable.itemPrice(item_id);
                double discounted_price = item_price;

                syncModel.setItem_id(item_id);
                syncModel.setCustomer_id(retailCustomerId);

                syncModel.setItem_price(item_price);
                syncModel.setDisc_price(0);
                syncModel.setDisc_percentage(0);
                syncModel.setDisc_type("");
                syncModel.setDiscounted_price(discounted_price);
                syncModel.setSample(0);

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

                // retail sale amount
                retailSaleAmount += syncModel.getTotal_sale_amount();

                arrVarianceInvoice.add(syncModel);
            }
        }

        /*update this retail sale to local and also make payment for this*/
        invoiceOutTable.invoiceVarianceRetailSale(retailCustomerId, billNo, arrVarianceInvoice);
        invoiceOutTable.updateCustInvStatus(retailCustomerId, Constant.STATUS_COMPLETE);

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
        paymentTable.updateCustPaymentStatus(retailCustomerId, Constant.STATUS_COMPLETE);
    }

    // retrofit van close data
    public void routeVanClose(final String retailCustomerId, String routeDetails, String routeData, String vanCloseData) {
        updateBarHandler.post(new Runnable() {
            public void run() {
                RetrofitUtil.updateDialogTitle(getString(R.string.str_van_close));
            }
        });
        RetrofitService service = RetrofitUtil.retrofitClient();
        Call<syncResponse> apiCall = service.syncRouteVanClose(routeDetails, routeData, vanCloseData);
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
                    /*update here route close status and details*/
                    updateVanCloseFinalRoutePayment(arrItemStockCheck);

                    //update customer status
                    customerRouteMappingView.updateCustomerStatus(retailCustomerId, Constant.STATUS_COMPLETE);

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

    //update van close final payment details
    private void updateVanCloseFinalRoutePayment(ArrayList<ItemStockCheck> arrItemStockCheck) {
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

    public void initializeTableObjects() {
        depotInvoiceView = new DepotInvoiceView(getContext());
        productView = new ProductView(getContext());
        routeView = new RouteView(getContext());
        customerRouteMappingView = new CustomerRouteMappingView(getContext());
        customerItemPriceTable = new CustomerItemPriceTable(getContext());
        invoiceOutTable = new InvoiceOutTable(getContext());
        rejectionTable = new CustomerRejectionTable(getContext());
        paymentTable = new PaymentTable(getContext());
    }

}
