package com.hgil.siconprocess.adapter.customerOrder;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hgil.siconprocess.R;
import com.hgil.siconprocess.activity.NavBaseActivity;
import com.hgil.siconprocess.activity.fragments.customerTomorrowOrder.PrepareCustomerTomorrowOrderFragment;
import com.hgil.siconprocess.adapter.routeMap.RouteCustomerModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mohan.giri on 29-03-2017.
 */

public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.ViewHolder> {
    public ArrayList<RouteCustomerModel> mDataset;
    private Context mContext;

    public CustomerListAdapter(Context mContext, ArrayList<RouteCustomerModel> myDataset) {
        this.mContext = mContext;
        this.mDataset = myDataset;
    }

    @Override
    public CustomerListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_outlet, null, false);
        CustomerListAdapter.ViewHolder vh = new CustomerListAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final CustomerListAdapter.ViewHolder holder, int position) {
        final RouteCustomerModel routeCustomerModel = mDataset.get(position);
        holder.tvOutletName.setText(routeCustomerModel.getCustomerName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrepareCustomerTomorrowOrderFragment fragment = PrepareCustomerTomorrowOrderFragment.newInstance(routeCustomerModel.getCustomerId(), routeCustomerModel.getCustomerName());
                String fragClassName = fragment.getClass().getName();
                FragmentManager fragmentManager = ((NavBaseActivity) mContext).getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left, R.anim.anim_slide_out_right, R.anim.anim_slide_in_right)
                        .replace(R.id.flContent, fragment)
                        .addToBackStack(fragClassName)
                        .setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left, R.anim.anim_slide_out_right, R.anim.anim_slide_in_right)
                        .commit();

             /*   Intent intent = new Intent(mContext, SelectedOutletInfoActivity.class);
                intent.putExtra("customer_id", routeCustomerModel.getCustomerId());
                mContext.startActivity(intent);
                ((NavBaseActivity) mContext).overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
*/
            }
        });

        holder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvOutletName)
        public TextView tvOutletName;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}