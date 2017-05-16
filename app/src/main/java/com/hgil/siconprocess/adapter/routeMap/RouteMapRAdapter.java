package com.hgil.siconprocess.adapter.routeMap;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hgil.siconprocess.R;
import com.hgil.siconprocess.activity.HomeInvoiceActivity;
import com.hgil.siconprocess.activity.NavBaseActivity;
import com.hgil.siconprocess.utils.ui.SnackbarUtil;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mohan.giri on 25-01-2017.
 */

public class RouteMapRAdapter extends RecyclerView.Adapter<RouteMapRAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<RouteCustomerModel> mDataset;

    // Provide a suitable constructor (depends on the kind of dataset)
    public RouteMapRAdapter(Context mContext, ArrayList<RouteCustomerModel> myDataset) {
        this.mContext = mContext;
        this.mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RouteMapRAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_route_map, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final RouteCustomerModel routeCustomerModel = mDataset.get(position);
        holder.tvCustomerName.setText(routeCustomerModel.getCustomerName());
        holder.tvTotalSaleAmt.setText("G.Sale: " + holder.strRupee + routeCustomerModel.getSaleAmount());

        //text color change on status
        String status = routeCustomerModel.getCustStatus();
        if (status == null || status.matches("") || status.matches("PENDING")) {
            String colored_status = "Status:" + " <font color='" + mContext.getResources().getColor(R.color.colorTextRed) + "'>" + status
                    + "</font>";
            holder.tvStatus.setText(Html.fromHtml(colored_status));
            holder.customer_item.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
        } else if (status.matches("COMPLETED")) {
            String colored_status = "Status:" + " <font color='" + mContext.getResources().getColor(R.color.colorTextGreen) + "'>" + status
                    + "</font>";
            holder.tvStatus.setText(Html.fromHtml(colored_status));
            holder.customer_item.setBackgroundColor(mContext.getResources().getColor(R.color.colorBackgroundGreen));
        }

        holder.customer_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (routeCustomerModel.getCustStatus().matches("COMPLETED")) {
                    // do nothing
                    SnackbarUtil.showSnackbar(v, "Customer Invoice is already completed you cannot access/edit.");
                } else {
                    //launch activity with updated nav bar
                    Intent intent = new Intent(mContext, HomeInvoiceActivity.class);
                    intent.putExtra("customer_id", routeCustomerModel.getCustomerId());
                    intent.putExtra("customer_name", routeCustomerModel.getCustomerName());
                    mContext.startActivity(intent);
                    ((NavBaseActivity) mContext).overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                }
            }
        });

        holder.setIsRecyclable(false);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        @BindView(R.id.tvCustomerName)
        public TextView tvCustomerName;
        @BindView(R.id.tvStatus)
        public TextView tvStatus;
        @BindView(R.id.tvTotalSaleAmt)
        public TextView tvTotalSaleAmt;
        @BindView(R.id.customer_item)
        public LinearLayout customer_item;
        @BindString(R.string.strRupee)
        public String strRupee;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, itemView);
        }
    }
}