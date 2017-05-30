package com.hgil.siconprocess.activity.fragments.customerInvoice.makeSaleInvoice;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hgil.siconprocess.R;
import com.hgil.siconprocess.activity.HomeInvoiceActivity;
import com.hgil.siconprocess.activity.fragments.customerInvoice.CustomerPaymentFragment;
import com.hgil.siconprocess.adapter.invoice.InvoiceModel;
import com.hgil.siconprocess.adapter.invoice.invoiceSale.CustomerInvoiceAdapter;
import com.hgil.siconprocess.base.BaseFragment;
import com.hgil.siconprocess.database.masterTables.CustomerItemPriceTable;
import com.hgil.siconprocess.database.masterTables.CustomerRouteMappingView;
import com.hgil.siconprocess.database.masterTables.DepotInvoiceView;
import com.hgil.siconprocess.database.tables.CustomerRejectionTable;
import com.hgil.siconprocess.database.tables.InvoiceOutTable;
import com.hgil.siconprocess.retrofit.loginResponse.dbModels.RcReason;
import com.hgil.siconprocess.utils.UtilBillNo;
import com.hgil.siconprocess.utils.UtilNetworkLocation;
import com.hgil.siconprocess.utils.Utility;
import com.hgil.siconprocess.utils.ui.SampleDialog;
import com.hgil.siconprocess.utils.utilPermission.UtilIMEI;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerInvoiceFragment extends BaseFragment {
    //@BindView(R.id.tvCustomerTotal)
    public static TextView tvInvoiceTotal;
    public static double grandTotal = 0;
    public static final int NO_ORDER = 113;

    @BindView(R.id.tvCustomerName)
    TextView tvCustomerName;
    @BindView(R.id.tvTargetSale)
    TextView tvTargetSale;
    @BindView(R.id.tvAvgSale)
    TextView tvAvgSale;

    @BindView(R.id.rvCustomerInvoice)
    RecyclerView rvCustomerInvoice;
    @BindView(R.id.tvEmpty)
    TextView tvEmpty;

    @BindView(R.id.btnNoOrder)
    Button btnNoOrder;

    private boolean NO_ORDER_STATUS = false;

    String TAG = getClass().getName();
    private String bill_no;
    private CustomerInvoiceAdapter invoiceAdapter;
    private DepotInvoiceView customerInvoice;
    private CustomerRouteMappingView customerRouteMappingView;
    private CustomerRejectionTable rejectionTable;
    private InvoiceOutTable invoiceOutTable;
    private ArrayList<InvoiceModel> arrInvoiceItems = new ArrayList<>();

    public CustomerInvoiceFragment() {
        // Required empty public constructor
    }

    public static CustomerInvoiceFragment newInstance(String customer_id, String customer_name) {
        CustomerInvoiceFragment fragment = new CustomerInvoiceFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CUSTOMER_ID, customer_id);
        bundle.putString(CUSTOMER_NAME, customer_name);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        grandTotal = 0;
        if (getArguments() != null) {
            customer_id = getArguments().getString(CUSTOMER_ID);
            customer_name = getArguments().getString(CUSTOMER_NAME);
        }

        // initialise the values at first time
        customerRouteMappingView = new CustomerRouteMappingView(getContext());
        customerInvoice = new DepotInvoiceView(getContext());
        invoiceOutTable = new InvoiceOutTable(getContext());
        rejectionTable = new CustomerRejectionTable(getContext());

        invoiceAdapter = new CustomerInvoiceAdapter(getActivity(), arrInvoiceItems);

        if (arrInvoiceItems != null)
            arrInvoiceItems.clear();
        arrInvoiceItems.addAll(customerInvoice.getCustomerInvoice(customer_id));

        // if there is no invoice data exists for the user then get all available stock to the user
        if (arrInvoiceItems.size() == 0) {
            arrInvoiceItems.addAll(customerInvoice.getCustomerInvoiceOff(customer_id));
        }

        for (int i = 0; i < arrInvoiceItems.size(); i++) {
            double itemOrderAmount = arrInvoiceItems.get(i).getOrderAmount();
            grandTotal += itemOrderAmount;
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_customer_invoice;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // generate bill no
        bill_no = getBill_no();

        CustomerItemPriceTable itemPriceTable = new CustomerItemPriceTable(getContext());

        //total of demand target sale
        double demandTargetSale = itemPriceTable.customerTargetSale(customer_id);

        /*target sale and average sale amount*/
        tvTargetSale.setText("Target Sale\n" + strRupee + Utility.roundTwoDecimals(demandTargetSale));
        tvAvgSale.setText("Avg Sale\n" + strRupee + "0.00");

        // Do this code only first time, not after rotation or reuse fragment from backstack
        tvInvoiceTotal = (TextView) view.findViewById(R.id.tvInvoiceTotal);
        onFragmentStart();

        btnNoOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NoOrderActivity.class);
                intent.putExtra("customer_id", customer_id);
                intent.putExtra("customer_name", customer_name);
                startActivityForResult(intent, NO_ORDER);
                ((HomeInvoiceActivity) getContext()).overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);

            }
        });
    }

    private void onFragmentStart() {
        if (customer_name != null)
            tvCustomerName.setText(customer_name);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCustomerInvoice.setLayoutManager(linearLayoutManager);

        rvCustomerInvoice.setAdapter(invoiceAdapter);

        setTitle("Today's Sale");
        showSaveButton();
        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NO_ORDER_STATUS) {
                    // erase sale and rejection for this same customer
                    new InvoiceOutTable(getContext()).eraseInvoiceUser(NoOrderActivity.customerId);
                    new CustomerRejectionTable(getContext()).eraseCustRejections(NoOrderActivity.customerId);

                    CustomerPaymentFragment fragment = CustomerPaymentFragment.newInstance(customer_id, customer_name);
                    launchInvoiceFragment(fragment);
                } else {
                    ArrayList<InvoiceModel> reviewOrderData = new ArrayList<InvoiceModel>();
                    for (int i = 0; i < arrInvoiceItems.size(); i++) {
                        InvoiceModel invoiceModel = arrInvoiceItems.get(i);
                        if (invoiceModel.getOrderAmount() > 0 && invoiceModel.getInvQtyPs() > 0) {
                            // update bill_no, device imei_no, location and login_id
                            // time_stamp will be updated automatically;
                            invoiceModel.setBill_no(bill_no);
                            invoiceModel.setImei_no(UtilIMEI.getIMEINumber(getContext()));
                            invoiceModel.setLat_lng(UtilNetworkLocation.getLatLng(UtilNetworkLocation.getLocation(getContext())));
                            invoiceModel.setLogin_id(getLoginId());

                            reviewOrderData.add(invoiceModel);
                        }
                    }

                    if (reviewOrderData.size() == 0) {
                        new SampleDialog(getString(R.string.str_no_order_warning), getContext());
                    } else {
                        // here simply forward the collected array data to the next fragmen to let the user choose whether to save invoice or not
                        InvoiceOutFragment fragment = InvoiceOutFragment.newInstance(customer_id, customer_name, reviewOrderData);
                        launchInvoiceFragment(fragment);
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        tvInvoiceTotal.setText("Total\n" + strRupee + Utility.roundTwoDecimals(grandTotal));

        RcReason rcReason = customerRouteMappingView.custNoOrderReason(customer_id);
        if (rcReason.getReasonId() == 0) {
            NO_ORDER_STATUS = false;
            if (arrInvoiceItems.size() == 0) {
                tvEmpty.setVisibility(View.VISIBLE);
                rvCustomerInvoice.setVisibility(View.GONE);
            } else {
                tvEmpty.setVisibility(View.GONE);
                rvCustomerInvoice.setVisibility(View.VISIBLE);
            }
        } else {
            NO_ORDER_STATUS = true;
            rvCustomerInvoice.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
            tvEmpty.setText(rcReason.getReason());
        }
    }

    private String getBill_no() {
        String tempBill = null;
        String expectedLastBillNo = null;
        double max_bill_1 = 0, max_bill_2 = 0;
        String tempBill1 = invoiceOutTable.returnCustomerBillNo(customer_id);
        String tempBill2 = rejectionTable.returnCustomerBillNo(customer_id);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == NO_ORDER) {
                // refresh button color if any status is saved.
                onResume();
            }
        }
    }
}
