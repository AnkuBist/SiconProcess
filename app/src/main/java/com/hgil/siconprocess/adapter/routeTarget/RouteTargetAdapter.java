package com.hgil.siconprocess.adapter.routeTarget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hgil.siconprocess.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mohan.giri on 06-03-2017.
 */

public class RouteTargetAdapter extends RecyclerView.Adapter<RouteTargetAdapter.ViewHolder> {
    public ArrayList<RouteTargetModel> mDataset;
    private Context mContext;

    public RouteTargetAdapter(Context mContext, ArrayList<RouteTargetModel> myDataset) {
        this.mContext = mContext;
        this.mDataset = myDataset;
    }

    @Override
    public RouteTargetAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_targets, null, false);
        RouteTargetAdapter.ViewHolder vh = new RouteTargetAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RouteTargetAdapter.ViewHolder holder, int position) {
        RouteTargetModel routeTargetModel = mDataset.get(position);
        holder.tvItemName.setText(routeTargetModel.getItem_name());
        holder.tvDemand.setText(String.valueOf(routeTargetModel.getDemand()));
        holder.tvTarget.setText(String.valueOf(routeTargetModel.getTarget()));
        holder.tvAchieved.setText(String.valueOf(routeTargetModel.getAchieved()));
        holder.tvVariance.setText(String.valueOf(routeTargetModel.getVariance()));
        holder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvItemName)
        TextView tvItemName;
        @BindView(R.id.tvDemand)
        TextView tvDemand;
        @BindView(R.id.tvTarget)
        TextView tvTarget;
        @BindView(R.id.tvAchieved)
        TextView tvAchieved;
        @BindView(R.id.tvVariance)
        TextView tvVariance;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}