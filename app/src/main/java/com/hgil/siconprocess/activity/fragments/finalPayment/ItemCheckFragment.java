package com.hgil.siconprocess.activity.fragments.finalPayment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hgil.siconprocess.R;
import com.hgil.siconprocess.base.BaseFragment;
import com.hgil.siconprocess.database.masterTables.CustomerItemPriceTable;
import com.hgil.siconprocess.database.masterTables.DepotInvoiceView;
import com.hgil.siconprocess.database.masterTables.ProductView;
import com.hgil.siconprocess.database.tables.CustomerRejectionTable;
import com.hgil.siconprocess.database.tables.InvoiceOutTable;
import com.hgil.siconprocess.syncPOJO.invoiceSyncModel.SyncInvoiceDetailModel;
import com.hgil.siconprocess.syncPOJO.supervisorSyncModel.CashierSyncModel;
import com.hgil.siconprocess.syncPOJO.supervisorSyncModel.ItemStockCheck;
import com.hgil.siconprocess.utils.UtilBillNo;
import com.hgil.siconprocess.utils.Utility;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ItemCheckFragment extends BaseFragment {

    private static final String SYNC_OBJECT = "sync_object";
    @BindView(R.id.rvItemStockCheck)
    RecyclerView rvItemStockCheck;
    @BindView(R.id.tvEmpty)
    TextView tvEmpty;
    private CashierSyncModel cashierSyncModel;

    private ItemStockCheckAdapter itemStockCheckAdapter;
    private ProductView productView;
    private ArrayList<ItemStockCheck> arrItemStockCheck = new ArrayList<>();

    public ItemCheckFragment() {
        // Required empty public constructor
    }

    public static ItemCheckFragment newInstance(CashierSyncModel cashierSyncModel) {
        ItemCheckFragment fragment = new ItemCheckFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SYNC_OBJECT, cashierSyncModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cashierSyncModel = (CashierSyncModel) getArguments().getSerializable(SYNC_OBJECT);
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_item_check;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showSaveButton();
        setTitle("Van Stock");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvItemStockCheck.setLayoutManager(linearLayoutManager);

        productView = new ProductView(getContext());
        arrItemStockCheck.addAll(productView.checkItemStock());
        itemStockCheckAdapter = new ItemStockCheckAdapter(getContext(), arrItemStockCheck);
        rvItemStockCheck.setAdapter(itemStockCheckAdapter);

        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cashierSyncModel.setArrItemStock(new ArrayList<ItemStockCheck>());
                cashierSyncModel.setArrItemStock(arrItemStockCheck);

                CustomerItemPriceTable customerItemPriceTable = new CustomerItemPriceTable(getContext());
                String routeManagementId = getRouteModel().getRouteManagementId();
                String invoiceNumber = new DepotInvoiceView(getContext()).commonInvoiceNumber();
                String invoiceDate = Utility.getCurDate();
                String billNo = getBill_no();
                String cashierCode = getRouteModel().getCashierCode();

                // generate temporary invoice for the retails customer for variance items
                ArrayList<SyncInvoiceDetailModel> arrVarianceInvoice = new ArrayList<>();
                for (ItemStockCheck itemStockCheck : arrItemStockCheck) {
                    int variance = itemStockCheck.getItem_variance();
                    if (variance > 0) {
                        int saleCount = variance;
                        SyncInvoiceDetailModel syncModel = new SyncInvoiceDetailModel();
                        String item_id = itemStockCheck.getItem_id();
                        String customer_id = "";        //TODO

                        double item_price = customerItemPriceTable.itemPrice(item_id);
                        double discount_price = 0;
                        double discount_percentage = 0;
                        String discount_type = "";
                        double discounted_price = item_price;
                        int sample = 0;

                        syncModel.setItem_id(item_id);
                        syncModel.setCustomer_id(customer_id);

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

                        arrVarianceInvoice.add(syncModel);
                    }
                }

                cashierSyncModel.setArrRetailSale(arrVarianceInvoice);
                CashCheckFragment fragment = CashCheckFragment.newInstance(cashierSyncModel);
                launchNavFragment(fragment);
            }
        });
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

}
