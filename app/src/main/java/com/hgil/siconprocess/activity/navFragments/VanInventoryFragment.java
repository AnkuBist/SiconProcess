package com.hgil.siconprocess.activity.navFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.hgil.siconprocess.R;
import com.hgil.siconprocess.activity.NavBaseActivity;
import com.hgil.siconprocess.activity.ViewVanStockActivity;
import com.hgil.siconprocess.activity.fragments.vanClose.VanCloseFragment;
import com.hgil.siconprocess.base.BaseFragment;

import butterknife.BindView;

public class VanInventoryFragment extends BaseFragment {
    @BindView(R.id.tvRouteName)
    TextView tvRouteName;
    @BindView(R.id.tvViewVanStock)
    TextView tvViewVanStock;
    @BindView(R.id.tvVanClosing)
    TextView tvVanClosing;

    public VanInventoryFragment() {
        // Required empty public constructor
    }

    public static VanInventoryFragment newInstance() {
        VanInventoryFragment fragment = new VanInventoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_van_inventory;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // set route name to the route
        String routeName = getRouteName();
        if (routeName != null && !routeName.isEmpty())
            tvRouteName.setText(routeName);

        setTitle("Van Inventory");
        hideSaveButton();

        tvViewVanStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ViewVanStockActivity.class));
                ((NavBaseActivity) getContext()).overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
            }
        });

        tvVanClosing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VanCloseFragment fragment = VanCloseFragment.newInstance();
                String fragClassName = fragment.getClass().getName();
                FragmentManager fragmentManager = ((NavBaseActivity) getActivity()).getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                boolean fragmentPopped = fragmentManager.popBackStackImmediate(fragClassName, 0);
                if (!fragmentPopped) {
                    ft.replace(R.id.flContent, fragment);
                    ft.addToBackStack(fragClassName);
                }
                ft.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left, R.anim.anim_slide_out_right, R.anim.anim_slide_in_right);
                ft.commit();
            }
        });

    }
}
