package com.hgil.siconprocess.activity.fragments.customerInvoice.rejActivities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hgil.siconprocess.R;
import com.hgil.siconprocess.adapter.invoiceRejection.MarketRejectionModel;
import com.hgil.siconprocess.base.BaseToolbarActivity;
import com.hgil.siconprocess.utils.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MarketRejectionActivity extends BaseToolbarActivity {
    @Nullable
    @BindView(R.id.tvCustomerName)
    TextView tvCustomerName;
    @Nullable
    @BindView(R.id.etDamaged)
    EditText etDamaged;
    @Nullable
    @BindView(R.id.etExpired)
    EditText etExpired;
    @Nullable
    @BindView(R.id.etRatEaten)
    EditText etRatEaten;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_rejection);

        ButterKnife.bind(this);
        tvCustomerName.setText(getRouteName());
        setNavTitle("Goods Return");
        showSaveBtn();

        if (getIntent().getExtras() != null) {
            String customerName = getIntent().getStringExtra("customerName");
            tvCustomerName.setText(customerName);

            MarketRejectionModel marketRejectionModel = (MarketRejectionModel) getIntent().getExtras().getSerializable("marketRejection");
            if (marketRejectionModel != null) {
                int damaged = marketRejectionModel.getDamaged();
                int expired = marketRejectionModel.getExpired();
                int ratEaten = marketRejectionModel.getRatEaten();

                etDamaged.setText(String.valueOf(damaged));
                etExpired.setText(String.valueOf(expired));
                etRatEaten.setText(String.valueOf(ratEaten));
            }
        }

        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                int damaged = Utility.getInteger(etDamaged.getText().toString());
                int expired = Utility.getInteger(etExpired.getText().toString());
                int ratEaten = Utility.getInteger(etRatEaten.getText().toString());
                int totalMarketRejection = damaged + expired + ratEaten;

                MarketRejectionModel marketRejection = new MarketRejectionModel();
                marketRejection.setDamaged(damaged);
                marketRejection.setExpired(expired);
                marketRejection.setRatEaten(ratEaten);
                marketRejection.setTotal(totalMarketRejection);

                Bundle bundle = new Bundle();
                bundle.putSerializable("marketRejection", marketRejection);
                resultIntent.putExtras(bundle);

                setResult(Activity.RESULT_OK, resultIntent);
                finish();
                overridePendingTransition(R.anim.anim_slide_out_right, R.anim.anim_slide_in_right);
            }
        });
    }
}
