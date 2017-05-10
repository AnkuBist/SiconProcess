package com.hgil.siconprocess.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hgil.siconprocess.R;
import com.hgil.siconprocess.adapter.vanStock.VanStockAdapter;
import com.hgil.siconprocess.adapter.vanStock.VanStockModel;
import com.hgil.siconprocess.base.BaseToolbarActivity;
import com.hgil.siconprocess.database.masterTables.CustomerRouteMappingView;
import com.hgil.siconprocess.database.masterTables.ProductView;
import com.hgil.siconprocess.database.tables.PaymentTable;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewVanStockActivity extends BaseToolbarActivity {

    @Nullable
    @BindView(R.id.tvCrateLoaded)
    TextView tvCrateLoaded;
    @Nullable
    @BindView(R.id.tvCrateOs)
    TextView tvCrateOs;
    @Nullable
    @BindView(R.id.tvCrateIssued)
    TextView tvCrateIssued;
    @Nullable
    @BindView(R.id.tvCrateReturned)
    TextView tvCrateReturned;
    @Nullable
    @BindView(R.id.tvCrateLeftover)
    TextView tvCrateLeftover;

    @Nullable
    @BindView(R.id.rvVanStock)
    RecyclerView rvVanStock;
    @Nullable
    @BindView(R.id.tvEmpty)
    TextView tvEmpty;

    private VanStockAdapter vanStockAdapter;
    private ProductView productView;
    private ArrayList<VanStockModel> arrVanStock = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_van_stock);

        ButterKnife.bind(this);
        setNavTitle("View Van Stock");
        hideSaveBtn();

        // get crate info
        CustomerRouteMappingView routeCustomerView = new CustomerRouteMappingView(this);
        PaymentTable paymentTable = new PaymentTable(this);

        int crateLoaded = routeCustomerView.vanTotalCrate();
        int crateOs = 0;
        int crateIssued = paymentTable.routeIssuedCrate();
        int crateReturned = paymentTable.routeReceivedCrate();
        int crateLeftover = crateLoaded - crateIssued + crateReturned;

        // display these in UI
        tvCrateLoaded.setText(String.valueOf(crateLoaded));
        tvCrateOs.setText(String.valueOf(crateOs));
        tvCrateIssued.setText(String.valueOf(crateIssued));
        tvCrateReturned.setText(String.valueOf(crateReturned));
        tvCrateLeftover.setText(String.valueOf(crateLeftover));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvVanStock.setLayoutManager(linearLayoutManager);

        productView = new ProductView(this);
        arrVanStock.addAll(productView.getVanStock());
        vanStockAdapter = new VanStockAdapter(this, arrVanStock);
        rvVanStock.setAdapter(vanStockAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (arrVanStock.size() == 0) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvVanStock.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvVanStock.setVisibility(View.VISIBLE);
        }
    }
}
