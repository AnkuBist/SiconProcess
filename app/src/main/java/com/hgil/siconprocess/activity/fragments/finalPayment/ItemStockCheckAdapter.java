package com.hgil.siconprocess.activity.fragments.finalPayment;

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
import com.hgil.siconprocess.syncPOJO.supervisorSyncModel.ItemStockCheck;
import com.hgil.siconprocess.utils.Utility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mohan.giri on 23-05-2017.
 */

public class ItemStockCheckAdapter extends RecyclerView.Adapter<ItemStockCheckAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<ItemStockCheck> mDataset;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ItemStockCheckAdapter(Context mContext, ArrayList<ItemStockCheck> myDataset) {
        this.mContext = mContext;
        this.mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ItemStockCheckAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_next_day_order, parent, false);
        ItemStockCheckAdapter.ViewHolder vh = new ItemStockCheckAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ItemStockCheckAdapter.ViewHolder holder, int position) {
        final ItemStockCheck itemStockCheck = mDataset.get(position);
        holder.tvItemName.setText(itemStockCheck.getItem_name());
        holder.etQuantity.setText(String.valueOf(itemStockCheck.getPhysical_leftover()));

        holder.etQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    itemStockCheck.setPhysical_leftover(Utility.getInteger(holder.etQuantity.getText().toString()));
                    itemStockCheck.setItem_variance(itemStockCheck.getActual_leftover() - itemStockCheck.getPhysical_leftover());
                } else {
                    itemStockCheck.setPhysical_leftover(0);
                    itemStockCheck.setItem_variance(0);
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
        @BindView(R.id.etQuantity)
        public EditText etQuantity;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, itemView);
        }
    }
}