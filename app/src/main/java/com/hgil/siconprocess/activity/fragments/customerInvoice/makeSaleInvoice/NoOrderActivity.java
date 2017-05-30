package com.hgil.siconprocess.activity.fragments.customerInvoice.makeSaleInvoice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hgil.siconprocess.R;
import com.hgil.siconprocess.base.BaseToolbarActivity;
import com.hgil.siconprocess.database.masterTables.RcReasonTable;
import com.hgil.siconprocess.retrofit.loginResponse.dbModels.RcReason;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mohan.giri on 26-05-2017.
 */

public class NoOrderActivity extends BaseToolbarActivity {

    @Nullable
    @BindView(R.id.tvCustomerName)
    TextView tvCustomerName;
    @Nullable
    @BindView(R.id.rvRcReason)
    RecyclerView rvRcReason;
    @Nullable
    @BindView(R.id.tvEmpty)
    TextView tvEmpty;

    public static String customerId = "";

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
            this.customerId = customer_id;
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
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, resultIntent);
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, resultIntent);
                finish(); // close this activity and return to preview activity (if there is any)
                overridePendingTransition(R.anim.anim_slide_out_right, R.anim.anim_slide_in_right);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}