package com.hgil.siconprocess.adapter.nextDayOrder;

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
import com.hgil.siconprocess.database.dbModels.NextDayOrderModel;
import com.hgil.siconprocess.utils.Utility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mohan.giri on 09-02-2017.
 */

public class NextDayOrderAdapter extends RecyclerView.Adapter<NextDayOrderAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<NextDayOrderModel> mDataset;

    // Provide a suitable constructor (depends on the kind of dataset)
    public NextDayOrderAdapter(Context mContext, ArrayList<NextDayOrderModel> myDataset) {
        this.mContext = mContext;
        this.mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NextDayOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_next_day_order, parent, false);
        NextDayOrderAdapter.ViewHolder vh = new NextDayOrderAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final NextDayOrderAdapter.ViewHolder holder, int position) {
        final NextDayOrderModel nextDayOrderModel = mDataset.get(position);
        holder.tvItemName.setText(nextDayOrderModel.getItemName());
        if (nextDayOrderModel.getQuantity() > 0)
            holder.etQuantity.setText(String.valueOf(nextDayOrderModel.getQuantity()));

        holder.etQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                nextDayOrderModel.setQuantity(Utility.getInteger(holder.etQuantity.getText().toString()));
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