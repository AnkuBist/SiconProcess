package com.hgil.siconprocess.activity.fragments.vanClose;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.hgil.siconprocess.R;
import com.hgil.siconprocess.syncPOJO.vanCloseModel.ItemStockCheck;
import com.hgil.siconprocess.utils.Utility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mohan.giri on 23-05-2017.
 */

public class VanCloseItemStockCheckAdapter extends RecyclerView.Adapter<VanCloseItemStockCheckAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<ItemStockCheck> mDataset;

    // Provide a suitable constructor (depends on the kind of dataset)
    public VanCloseItemStockCheckAdapter(Context mContext, ArrayList<ItemStockCheck> myDataset) {
        this.mContext = mContext;
        this.mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public VanCloseItemStockCheckAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_route_van_close_sku_stock, parent, false);
        VanCloseItemStockCheckAdapter.ViewHolder vh = new VanCloseItemStockCheckAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final VanCloseItemStockCheckAdapter.ViewHolder holder, int position) {
        final ItemStockCheck itemStockCheck = mDataset.get(position);
        holder.tvItemName.setText(itemStockCheck.getItem_name());
        if (itemStockCheck.getFresh_rejection() > 0)
            holder.etFRej.setText(String.valueOf(itemStockCheck.getFresh_rejection()));

        holder.etFRej.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    itemStockCheck.setFresh_rejection(Utility.getInteger(holder.etFRej.getText().toString()));
                } else {
                    itemStockCheck.setFresh_rejection(0);
                }
            }
        });

        if (itemStockCheck.getMarket_rejection() > 0)
            holder.etMRej.setText(String.valueOf(itemStockCheck.getMarket_rejection()));

        holder.etMRej.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    itemStockCheck.setMarket_rejection(Utility.getInteger(holder.etMRej.getText().toString()));
                } else {
                    itemStockCheck.setMarket_rejection(0);
                }
            }
        });

        if (itemStockCheck.getPhysical_leftover() > 0)
            holder.etLeft.setText(String.valueOf(itemStockCheck.getPhysical_leftover()));

        holder.etLeft.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    itemStockCheck.setPhysical_leftover(Utility.getInteger(holder.etLeft.getText().toString()));
                    itemStockCheck.setItem_variance(itemStockCheck.getActual_leftover() - itemStockCheck.getPhysical_leftover());
                } else {
                    itemStockCheck.setPhysical_leftover(0);
                    itemStockCheck.setItem_variance(itemStockCheck.getActual_leftover());
                }
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
        @BindView(R.id.etFRej)
        public EditText etFRej;
        @BindView(R.id.etMRej)
        public EditText etMRej;
        @BindView(R.id.etLeft)
        public EditText etLeft;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, itemView);
        }
    }
}