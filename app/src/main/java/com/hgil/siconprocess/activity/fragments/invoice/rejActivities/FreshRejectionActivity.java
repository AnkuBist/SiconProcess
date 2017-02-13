package com.hgil.siconprocess.activity.fragments.invoice.rejActivities;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hgil.siconprocess.R;
import com.hgil.siconprocess.adapter.invoiceRejection.FreshRejectionModel;
import com.hgil.siconprocess.adapter.invoiceRejection.MarketRejectionModel;
import com.hgil.siconprocess.base.BaseToolbarActivity;
import com.hgil.siconprocess.base.SiconApp;
import com.hgil.siconprocess.utils.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FreshRejectionActivity extends BaseToolbarActivity {
    @Nullable
    @BindView(R.id.tvCustomerName)
    TextView tvCustomerName;
    @Nullable
    @BindView(R.id.etMShaped)
    EditText etMShaped;
    @Nullable
    @BindView(R.id.etTornPolly)
    EditText etTornPolly;
    @Nullable
    @BindView(R.id.etFungus)
    EditText etFungus;
    @Nullable
    @BindView(R.id.etWetBread)
    EditText etWetBread;
    @Nullable
    @BindView(R.id.etOthers)
    EditText etOthers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fresh_rejection);

        ButterKnife.bind(this);

        tvCustomerName.setText(SiconApp.getInstance().getRouteName());
        setNavTitle("Goods Return");
        showSaveBtn();

        if (getIntent().getExtras() != null) {
            String customerName = getIntent().getStringExtra("customerName");
            tvCustomerName.setText(customerName);

            FreshRejectionModel freshRejectionModel = (FreshRejectionModel) getIntent().getExtras().getSerializable("freshRejection");
            if (freshRejectionModel != null) {
                int mShaped = freshRejectionModel.getmShaped();
                int tornPolly = freshRejectionModel.getTornPolly();
                int fungus = freshRejectionModel.getFungus();
                int wetBread = freshRejectionModel.getWetBread();
                int others = freshRejectionModel.getOthers();

                etMShaped.setText(String.valueOf(mShaped));
                etTornPolly.setText(String.valueOf(tornPolly));
                etFungus.setText(String.valueOf(fungus));
                etWetBread.setText(String.valueOf(wetBread));
                etOthers.setText(String.valueOf(others));
            }
        }

        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                int mShaped = Utility.getInteger(etMShaped.getText().toString());
                int tornPolly = Utility.getInteger(etTornPolly.getText().toString());
                int fungus = Utility.getInteger(etFungus.getText().toString());
                int wetBread = Utility.getInteger(etWetBread.getText().toString());
                int others = Utility.getInteger(etOthers.getText().toString());
                int totalFreshRejection = mShaped + tornPolly + fungus + wetBread + others;

                FreshRejectionModel freshRejection = new FreshRejectionModel();
                freshRejection.setmShaped(mShaped);
                freshRejection.setTornPolly(tornPolly);
                freshRejection.setFungus(fungus);
                freshRejection.setWetBread(wetBread);
                freshRejection.setOthers(others);
                freshRejection.setTotal(totalFreshRejection);

                Bundle bundle = new Bundle();
                bundle.putSerializable("freshRejection", freshRejection);
                resultIntent.putExtras(bundle);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
                overridePendingTransition(R.anim.anim_slide_out_right, R.anim.anim_slide_in_right);
            }
        });
    }


}
