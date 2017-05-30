package com.hgil.siconprocess.activity.fragments.customerInvoice.makeSaleInvoice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hgil.siconprocess.R;
import com.hgil.siconprocess.database.masterTables.CustomerRouteMappingView;
import com.hgil.siconprocess.retrofit.loginResponse.dbModels.RcReason;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mohan.giri on 26-05-2017.
 */

public class NoOrderAdapter extends RecyclerView.Adapter<NoOrderAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<RcReason> mDataset;

    // Provide a suitable constructor (depends on the kind of dataset)
    public NoOrderAdapter(Context mContext, ArrayList<RcReason> myDataset) {
        this.mContext = mContext;
        this.mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NoOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_no_order, parent, false);
        NoOrderAdapter.ViewHolder vh = new NoOrderAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(NoOrderAdapter.ViewHolder holder, int position) {
        final RcReason pSelectModel = mDataset.get(position);
        holder.tvStatus.setText(pSelectModel.getReason());

        if (pSelectModel.isStatus()) {
            holder.relativeHolder.setBackgroundColor(mContext.getResources().getColor(R.color.colorBackgroundGreen));
        } else {
            holder.relativeHolder.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
        }
        setClickListener(holder, position);

        holder.setIsRecyclable(false);
    }

    private void setClickListener(final ViewHolder holder, final int position) {
        holder.relativeHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save reason here only
                new CustomerRouteMappingView(mContext).updateOrderReason(NoOrderActivity.customerId, mDataset.get(position).getReasonId(), mDataset.get(position).getReason());
                Intent resultIntent = new Intent();
                ((NoOrderActivity) mContext).setResult(Activity.RESULT_OK, resultIntent);
                ((NoOrderActivity) mContext).finish();
                ((NoOrderActivity) mContext).overridePendingTransition(R.anim.anim_slide_out_right, R.anim.anim_slide_in_right);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.relativeHolder)
        RelativeLayout relativeHolder;
        @BindView(R.id.tvStatus)
        public TextView tvStatus;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, itemView);
        }
    }
}