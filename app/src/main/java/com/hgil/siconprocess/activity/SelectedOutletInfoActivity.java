package com.hgil.siconprocess.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.hgil.siconprocess.R;
import com.hgil.siconprocess.base.BaseToolbarActivity;
import com.hgil.siconprocess.database.masterTables.CustomerRouteMappingView;
import com.hgil.siconprocess.retrofit.loginResponse.dbModels.CustomerRouteMapModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectedOutletInfoActivity extends BaseToolbarActivity {

    @Nullable
    @BindView(R.id.etDealerName)
    TextView etDealerName;
    @Nullable
    @BindView(R.id.etDealerCode)
    TextView etDealerCode;
    @Nullable
    @BindView(R.id.etRouteName)
    TextView etRouteName;
    @Nullable
    @BindView(R.id.etDealerType)
    TextView etDealerType;
/*    @Nullable
    @BindView(R.id.etDealerAcntNum)
    TextView etDealerAcntNum;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_outlet_info);

        ButterKnife.bind(this);
        setNavTitle("Outlet Info");
        hideSaveBtn();

        if (getIntent() != null) {
            customer_id = getIntent().getStringExtra(CUSTOMER_ID);
        }

        // get customer details here only
        CustomerRouteMappingView customerRouteMappingView = new CustomerRouteMappingView(this);
        CustomerRouteMapModel customerRouteMapModel = customerRouteMappingView.getCustomerRouteMapByCustomer(customer_id);

        //display customer information
        etDealerName.setText(customerRouteMapModel.getCustomerName());
        etDealerCode.setText(customerRouteMapModel.getCustomerId());
        etRouteName.setText(customerRouteMapModel.getRouteName());
        etDealerType.setText(customerRouteMapModel.getCType());
        //etDealerAcntNum.setText(customerRouteMapModel.getACCOUNTNUM());


    }
}
