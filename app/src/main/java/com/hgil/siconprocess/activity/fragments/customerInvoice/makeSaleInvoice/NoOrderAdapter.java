package com.hgil.siconprocess.activity.fragments.customerInvoice.makeSaleInvoice;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hgil.siconprocess.R;
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
    SparseBooleanArray sparseBooleanArray;

    // Provide a suitable constructor (depends on the kind of dataset)
    public NoOrderAdapter(Context mContext, ArrayList<RcReason> myDataset) {
        this.mContext = mContext;
        this.mDataset = myDataset;
        this.sparseBooleanArray = new SparseBooleanArray();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NoOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_product, parent, false);
        NoOrderAdapter.ViewHolder vh = new NoOrderAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(NoOrderAdapter.ViewHolder holder, int position) {
        final RcReason pSelectModel = mDataset.get(position);
        holder.tvItemName.setText(pSelectModel.getReason());
        holder.cbItem.setVisibility(View.GONE);

        if (sparseBooleanArray.get(position, false)) {
            holder.cbItem.setChecked(true);
            NoOrderActivity.selected_ReasonId = pSelectModel.getReasonId();
            NoOrderActivity.selectedReason = pSelectModel.getReason();

            holder.relativeHolder.setBackgroundColor(mContext.getResources().getColor(R.color.colorBackgroundGreen));
        } else {
            holder.cbItem.setChecked(false);
            holder.relativeHolder.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
        }
        setClickListner(holder, position);

        /*if (pSelectModel.isStatus()) {
            holder.cbItem.setChecked(true);
            NoOrderActivity.selected_ReasonId = pSelectModel.getReasonId();
            NoOrderActivity.selectedReason = pSelectModel.getReason();

        } else {
            holder.cbItem.setChecked(false);
        }*/

       /* holder.cbItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // add item to list
                    pSelectModel.setStatus(true);


                } else {
                    // remove item from list
                    pSelectModel.setStatus(false);
                }
            }
        });*/

        holder.setIsRecyclable(false);
    }

    private void setClickListner(final ViewHolder holder, final int position) {
        holder.relativeHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sparseBooleanArray.clear();
                sparseBooleanArray.put(position, true);
                notifyDataSetChanged();
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
        @BindView(R.id.tvItemName)
        public TextView tvItemName;
        @BindView(R.id.cbItem)
        public CheckBox cbItem;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, itemView);
        }
    }
}