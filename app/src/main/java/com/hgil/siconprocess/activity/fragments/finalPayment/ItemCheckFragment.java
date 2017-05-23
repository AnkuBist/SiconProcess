package com.hgil.siconprocess.activity.fragments.finalPayment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;

import com.hgil.siconprocess.R;
import com.hgil.siconprocess.activity.fragments.finalPayment.cashierSync.CashierSyncModel;
import com.hgil.siconprocess.activity.fragments.invoiceSyncModel.VanStockCheck;
import com.hgil.siconprocess.base.BaseFragment;
import com.hgil.siconprocess.database.masterTables.DepotInvoiceView;
import com.hgil.siconprocess.database.tables.CustomerRejectionTable;
import com.hgil.siconprocess.database.tables.InvoiceOutTable;
import com.hgil.siconprocess.utils.Utility;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ItemCheckFragment extends BaseFragment {

    private static final String SYNC_OBJECT = "sync_object";
    @BindView(R.id.etItemsLoaded)
    EditText etItemsLoaded;
    @BindView(R.id.etItemsSold)
    EditText etItemsSold;
    @BindView(R.id.etItemsLeftover)
    EditText etItemsLeftover;
    @BindView(R.id.etItemsReceived)
    EditText etItemsReceived;
    @BindView(R.id.etItemsFreshRej)
    EditText etItemsFreshRej;
    @BindView(R.id.etItemsReceivedFreshRej)
    EditText etItemsReceivedFreshRej;
    @BindView(R.id.etItemsMarketRej)
    EditText etItemsMarketRej;
    @BindView(R.id.etItemsReceivedMarketRej)
    EditText etItemsReceivedMarketRej;
    private CashierSyncModel cashierSyncModel;
    private int items_loaded, items_sold, items_leftover, items_received, items_fresh_rejection,
            items_fresh_rej_received, items_market_rejection, items_market_rej_received;

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

        DepotInvoiceView depot_invoice = new DepotInvoiceView(getContext());
        CustomerRejectionTable rejectionTable = new CustomerRejectionTable(getContext());
        InvoiceOutTable invoiceOutTable = new InvoiceOutTable(getContext());

        // cross check items left and rejections
        items_loaded = depot_invoice.totalItemCount();
        items_sold = invoiceOutTable.soldItemCount();
        items_leftover = items_loaded - items_sold;
        items_fresh_rejection = rejectionTable.routeFreshRejection();
        items_market_rejection = rejectionTable.routeMarketRejection();

        etItemsLoaded.setText(String.valueOf(items_loaded));
        etItemsSold.setText(String.valueOf(items_sold));
        etItemsLeftover.setText(String.valueOf(items_leftover));
        etItemsReceived.setText(String.valueOf(items_received));
        etItemsFreshRej.setText(String.valueOf(items_fresh_rejection));
        etItemsReceivedFreshRej.setText(String.valueOf(items_fresh_rej_received));
        etItemsMarketRej.setText(String.valueOf(items_market_rejection));
        etItemsReceivedMarketRej.setText(String.valueOf(items_market_rej_received));

        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items_received = Utility.getInteger(etItemsReceived.getText().toString());
                items_fresh_rej_received = Utility.getInteger(etItemsReceivedFreshRej.getText().toString());
                items_market_rej_received = Utility.getInteger(etItemsReceivedMarketRej.getText().toString());

                // sync by supervisor completed now close or move the user to main screen
                VanStockCheck vanStockCheck = new VanStockCheck();
                vanStockCheck.setItems_loaded(items_loaded);
                vanStockCheck.setItems_sold(items_sold);
                vanStockCheck.setItems_leftover(items_leftover);
                vanStockCheck.setItem_delivered(items_received);
                vanStockCheck.setFresh_rejections(items_fresh_rejection);
                vanStockCheck.setFresh_rejections_delivered(items_fresh_rej_received);
                vanStockCheck.setMarket_rejection(items_market_rejection);
                vanStockCheck.setMarket_rejection_delivered(items_market_rej_received);

                CashCheckFragment fragment = CashCheckFragment.newInstance(cashierSyncModel);
                launchNavFragment(fragment);
            }
        });
    }

}
