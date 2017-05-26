package com.hgil.siconprocess.activity.fragments.customerInvoice.makeSaleInvoice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hgil.siconprocess.R;
import com.hgil.siconprocess.base.BaseToolbarActivity;
import com.hgil.siconprocess.database.masterTables.CustomerRouteMappingView;
import com.hgil.siconprocess.database.masterTables.RcReasonTable;
import com.hgil.siconprocess.retrofit.loginResponse.dbModels.RcReason;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mohan.giri on 26-05-2017.
 */

public class NoOrderActivity extends BaseToolbarActivity implements View.OnClickListener {

    @Nullable
    @BindView(R.id.tvCustomerName)
    TextView tvCustomerName;
    @Nullable
    @BindView(R.id.rvRcReason)
    RecyclerView rvRcReason;
    @Nullable
    @BindView(R.id.tvEmpty)
    TextView tvEmpty;
    @Nullable
    @BindView(R.id.btnCancel)
    Button btnCancel;
    @Nullable
    @BindView(R.id.btnSubmit)
    Button btnSubmit;

    public static int selected_ReasonId = 0;
    public static String selectedReason = null;

    private NoOrderAdapter noOrderAdapter;
    private RcReasonTable rcReasonTable;
    private ArrayList<RcReason> arrReason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_order_choice);

        ButterKnife.bind(this);
        tvCustomerName.setText(getRouteName());
        setNavTitle("No Order Reason");
        hideSaveBtn();

        if (getIntent() != null) {
            customer_id = getIntent().getStringExtra(CUSTOMER_ID);
            customer_name = getIntent().getStringExtra(CUSTOMER_NAME);
        }

        if (customer_name != null)
            tvCustomerName.setText(customer_name);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvRcReason.setLayoutManager(linearLayoutManager);

        rcReasonTable = new RcReasonTable(this);
        arrReason = rcReasonTable.getAllReasons(customer_id);

        noOrderAdapter = new NoOrderAdapter(this, arrReason);
        rvRcReason.setAdapter(noOrderAdapter);

        btnCancel.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (arrReason.size() == 0) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvRcReason.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvRcReason.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancel:
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, resultIntent);
                finish();
                overridePendingTransition(R.anim.anim_slide_out_right, R.anim.anim_slide_in_right);
                break;
            case R.id.btnSubmit:
                // save reason here only
                new CustomerRouteMappingView(this).updateOrderReason(customer_id, selected_ReasonId, selectedReason);
                Intent resultIntent1 = new Intent();
                setResult(Activity.RESULT_OK, resultIntent1);
                finish();
                overridePendingTransition(R.anim.anim_slide_out_right, R.anim.anim_slide_in_right);
                break;
            default:
                break;
        }
    }
}