package com.hgil.siconprocess.adapter.invoiceRejection;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.hgil.siconprocess.R;
import com.hgil.siconprocess.activity.HomeInvoiceActivity;
import com.hgil.siconprocess.activity.fragments.customerInvoice.CustomerRejectionFragment;
import com.hgil.siconprocess.activity.fragments.customerInvoice.rejActivities.FreshRejectionActivity;
import com.hgil.siconprocess.activity.fragments.customerInvoice.rejActivities.MarketRejectionActivity;
import com.hgil.siconprocess.utils.Utility;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mohan.giri on 04-02-2017.
 */

public class InvoiceRejectionAdapter extends RecyclerView.Adapter<InvoiceRejectionAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<CRejectionModel> mDataset;

    // Provide a suitable constructor (depends on the kind of dataset)
    public InvoiceRejectionAdapter(Context mContext, ArrayList<CRejectionModel> myDataset) {
        this.mContext = mContext;
        this.mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public InvoiceRejectionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rejection, parent, false);
        InvoiceRejectionAdapter.ViewHolder vh = new InvoiceRejectionAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(InvoiceRejectionAdapter.ViewHolder holder, final int position) {
        final CRejectionModel cRejectionModel = mDataset.get(position);
        holder.tvItemName.setText(cRejectionModel.getItem_name());

        double price = cRejectionModel.getPrice();
        holder.etMarketRetPrice.setText(holder.strRupee + String.valueOf(cRejectionModel.getPrice()));

        final MarketRejectionModel marketRejectionModel = cRejectionModel.getMarketRejection();
        final FreshRejectionModel freshRejectionModel = cRejectionModel.getFreshRejection();
        if (marketRejectionModel != null) {
            int total = marketRejectionModel.getTotal();
            double totalAmount = total * price;
            holder.etMarketRetQty.setText(String.valueOf(marketRejectionModel.getTotal()));

            holder.etMarketRetAmount.setText(holder.strRupee + Utility.roundTwoDecimals(totalAmount));
        } else {
            holder.etMarketRetQty.setText("0");
            holder.etMarketRetAmount.setText(holder.strRupee + "0.00");
        }

        if (freshRejectionModel != null) {
            holder.etFreshRetQty.setText(String.valueOf(freshRejectionModel.getTotal()));
        } else {
            holder.etFreshRetQty.setText("0");
        }

        holder.btnMarketRejection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MarketRejectionActivity.class);
                CustomerRejectionFragment.setMarketRejectionId(position);
                CustomerRejectionFragment.setFreshRejectionId(-1);
                intent.putExtra("customerName", CustomerRejectionFragment.getCustomerName());
                intent.putExtra("marketRejection", marketRejectionModel);
                ((HomeInvoiceActivity) mContext).startActivityForResult(intent, CustomerRejectionFragment.getMarketRejectionId());
                ((HomeInvoiceActivity) mContext).overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
            }
        });
        holder.btnFreshRejection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FreshRejectionActivity.class);
                CustomerRejectionFragment.setFreshRejectionId(position);
                CustomerRejectionFragment.setMarketRejectionId(-1);
                intent.putExtra("customerName", CustomerRejectionFragment.getCustomerName());
                intent.putExtra("freshRejection", freshRejectionModel);
                ((HomeInvoiceActivity) mContext).startActivityForResult(intent, CustomerRejectionFragment.getFreshRejectionId());
                ((HomeInvoiceActivity) mContext).overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
            }
        });

        holder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvItemName)
        public TextView tvItemName;
        @BindView(R.id.btnMarketRejection)
        public TextView btnMarketRejection;
        @BindView(R.id.btnFreshRejection)
        public TextView btnFreshRejection;

        @BindView(R.id.etMarketRetQty)
        public EditText etMarketRetQty;
        @BindView(R.id.etMarketRetPrice)
        public EditText etMarketRetPrice;
        @BindView(R.id.etMarketRetAmount)
        public EditText etMarketRetAmount;
        @BindView(R.id.etFreshRetQty)
        public EditText etFreshRetQty;

        @BindString(R.string.strRupee)
        public String strRupee;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, itemView);
        }
    }
}