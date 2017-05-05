package com.hgil.siconprocess.activity.fragments.invoice.makeSaleInvoice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hgil.siconprocess.R;
import com.hgil.siconprocess.activity.fragments.invoice.CustomerRejectionFragment;
import com.hgil.siconprocess.adapter.invoice.InvoiceModel;
import com.hgil.siconprocess.adapter.invoice.invoiceOut.CustomerInvoiceOutAdapter;
import com.hgil.siconprocess.base.BaseFragment;
import com.hgil.siconprocess.database.masterTables.DemandTargetTable;
import com.hgil.siconprocess.database.tables.InvoiceOutTable;
import com.hgil.siconprocess.utils.Utility;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class InvoiceOutFragment extends BaseFragment {
    private static final String INVOICE_LIST = "invoice_list";
    //@BindView(R.id.tvCustomerTotal)
    public static TextView tvCustomerTotal;
    public static double grandTotal = 0;
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
    private CustomerInvoiceOutAdapter invoiceAdapter;
    private InvoiceOutTable invoiceOutTable;
    private ArrayList<InvoiceModel> arrInvoiceItems;

    public InvoiceOutFragment() {
        // Required empty public constructor
    }

    public static InvoiceOutFragment newInstance(String customer_id, String customer_name, ArrayList<InvoiceModel> arrayList) {
        InvoiceOutFragment fragment = new InvoiceOutFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CUSTOMER_ID, customer_id);
        bundle.putString(CUSTOMER_NAME, customer_name);
        bundle.putSerializable(INVOICE_LIST, arrayList);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        grandTotal = 0;
        if (getArguments() != null) {
            customer_id = getArguments().getString(CUSTOMER_ID);
            customer_name = getArguments().getString(CUSTOMER_NAME);
            arrInvoiceItems = (ArrayList<InvoiceModel>) getArguments().getSerializable(INVOICE_LIST);
        }

        for (int i = 0; i < arrInvoiceItems.size(); i++) {
            double itemOrderAmount = arrInvoiceItems.get(i).getOrderAmount();
            grandTotal += itemOrderAmount;
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_invoice_out;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (customer_name != null)
            tvCustomerName.setText(customer_name);

        tvCustomerTotal = (TextView) view.findViewById(R.id.tvInvoiceTotal);

        DemandTargetTable demandTargetTable = new DemandTargetTable(getContext());

        //total of demand target sale
        double demandTargetSale = demandTargetTable.customerTargetSale(customer_id);

        /*target sale and average sale amount*/
        tvTargetSale.setText("Target Sale\n" + strRupee + Utility.roundTwoDecimals(demandTargetSale));
        tvAvgSale.setText("Avg Sale\n" + strRupee + "0.00");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCustomerInvoice.setLayoutManager(linearLayoutManager);

        invoiceOutTable = new InvoiceOutTable(getContext());

        invoiceAdapter = new CustomerInvoiceOutAdapter(getActivity(), arrInvoiceItems);
        rvCustomerInvoice.setAdapter(invoiceAdapter);

        setTitle("Today's Sale Summary");
        showSaveButton();
        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // move to next fragment to review user order with the items ordered
                invoiceOutTable.insertInvoiceOut(arrInvoiceItems, customer_id);

                // show snackbar message
                showSnackbar(getView(), "Invoice order saved successfully.");

                // start rejection fragment
                CustomerRejectionFragment fragment = CustomerRejectionFragment.newInstance(customer_id, customer_name);
                launchInvoiceFragment(fragment);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updateSaveIcon();
        tvCustomerTotal.setText("Total\n"+strRupee + Utility.roundTwoDecimals(grandTotal));
        if (arrInvoiceItems.size() == 0) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvCustomerInvoice.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvCustomerInvoice.setVisibility(View.VISIBLE);
        }
    }

   /* public void setInvoiceTotal(String invoiceAmount) {
        tvCustomerTotal.setText(invoiceAmount);
    }*/

}
