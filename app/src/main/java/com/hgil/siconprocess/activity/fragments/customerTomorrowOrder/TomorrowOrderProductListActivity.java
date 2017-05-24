package com.hgil.siconprocess.activity.fragments.customerTomorrowOrder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hgil.siconprocess.R;
import com.hgil.siconprocess.adapter.productSelection.ProductSelectAdapter;
import com.hgil.siconprocess.adapter.productSelection.ProductSelectModel;
import com.hgil.siconprocess.base.BaseToolbarActivity;
import com.hgil.siconprocess.database.masterTables.ProductView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TomorrowOrderProductListActivity extends BaseToolbarActivity implements View.OnClickListener {

    @Nullable
    @BindView(R.id.tvCustomerName)
    TextView tvCustomerName;
    @Nullable
    @BindView(R.id.rvProductList)
    RecyclerView rvProductList;
    @Nullable
    @BindView(R.id.tvEmpty)
    TextView tvEmpty;

    private ProductSelectAdapter itemListAdapter;
    private ProductView productView;
    private ArrayList<ProductSelectModel> arrProduct;
    private ArrayList<String> alreadyExisting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomorrow_order_product_list);

        ButterKnife.bind(this);
        tvCustomerName.setText(getRouteName());
        setNavTitle("Tomorrow's Order");
        showSaveBtn();

        if (getIntent() != null) {
            customer_id = getIntent().getStringExtra(CUSTOMER_ID);
            customer_name = getIntent().getStringExtra(CUSTOMER_NAME);
            alreadyExisting = getIntent().getStringArrayListExtra("existing_items");
        }

        if (customer_name != null)
            tvCustomerName.setText(customer_name);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvProductList.setLayoutManager(linearLayoutManager);

        productView = new ProductView(this);
        arrProduct = productView.getAvailableProducts(customer_id, alreadyExisting);

        itemListAdapter = new ProductSelectAdapter(this, arrProduct);
        rvProductList.setAdapter(itemListAdapter);

        imgSave.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (arrProduct.size() == 0) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvProductList.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvProductList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgSave:
                // send only selected product
                Bundle bundle = new Bundle();
                bundle.putSerializable("selected_products", arrProduct);

                Intent resultIntent = new Intent();
                resultIntent.putExtras(bundle);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
                overridePendingTransition(R.anim.anim_slide_out_right, R.anim.anim_slide_in_right);
                break;
            default:
                break;
        }
    }
}
