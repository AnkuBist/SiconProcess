package com.hgil.siconprocess.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hgil.siconprocess.R;
import com.hgil.siconprocess.activity.fragments.customerInvoice.CratesManagementFragment;
import com.hgil.siconprocess.activity.fragments.customerInvoice.CustomerPaymentFragment;
import com.hgil.siconprocess.activity.fragments.customerInvoice.CustomerRejectionFragment;
import com.hgil.siconprocess.activity.fragments.customerInvoice.FinalInvoiceFragment;
import com.hgil.siconprocess.activity.fragments.customerInvoice.makeSaleInvoice.CustomerInvoiceFragment;
import com.hgil.siconprocess.base.BaseActivity;
import com.hgil.siconprocess.utils.Utility;

import butterknife.BindView;

public class HomeInvoiceActivity extends BaseActivity {

    @BindView(R.id.tvNavTitle)
    public TextView tvNavTitle;
    @BindView(R.id.tvNavDate)
    public TextView tvNavDate;
    @BindView(R.id.imgSave)
    public ImageView imgSave;

    protected String customer_id;
    protected String customer_name;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nvView)
    NavigationView nvDrawer;
    @BindView(R.id.flInvoiceContent)
    FrameLayout containerFrame;

    boolean doubleBackToExitPressedOnce = false;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        getLayoutInflater().inflate(layoutResID, containerFrame, true);
    }

    public void setup() {
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDefaultDisplayHomeAsUpEnabled(false);
        actionbar.setDisplayHomeAsUpEnabled(false);
        actionbar.setDisplayShowCustomEnabled(true);
        actionbar.setDisplayShowHomeEnabled(false);
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setDisplayUseLogoEnabled(false);
        actionbar.setHomeButtonEnabled(false);

        // drawer hamburger icons
        drawerToggle = setupDrawerToggle();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);

        // Setup drawer view
        setupDrawerContent(nvDrawer);

        //header setting
        View navHeaderView = nvDrawer.getHeaderView(0);
        ImageView imgNavIcon = (ImageView) navHeaderView.findViewById(R.id.imgNavIcon);
        TextView tvNavHeader = (TextView) navHeaderView.findViewById(R.id.tvNavHeader);

        imgNavIcon.setImageResource(R.mipmap.harvest_logo);
        if (getRouteName() != null) {
            //String output = getRouteName().substring(0, 1).toUpperCase() + getRouteName().substring(1).toLowerCase();
            tvNavHeader.setText(getRouteName());
        }

        MenuItem menuItem = nvDrawer.getMenu().findItem(R.id.nav_today_sale);

        CustomerInvoiceFragment fragment = CustomerInvoiceFragment.newInstance(customer_id, customer_name);
        getSupportFragmentManager().beginTransaction().replace(R.id.flInvoiceContent, fragment).commit();

        //menuItem.setChecked(true);

        tvNavTitle.setText(menuItem.getTitle());
        tvNavDate.setText(Utility.getDateMonth());
        // firstLaunch();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_home_invoice);

        if (getIntent() != null) {
            customer_id = getIntent().getStringExtra("customer_id");
            customer_name = getIntent().getStringExtra("customer_name");
        }

        setup();
    }

    // set default home nav item selected and launch this on first view
    private void firstLaunch() {
        nvDrawer.getMenu().performIdentifierAction(R.id.nav_today_sale, 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                //fragment = HomeFragment.newInstance();
                // start nav base activity here only
                Intent intent = new Intent(this, NavBaseActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                // from right to left
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                // from left to right
                //overridePendingTransition(R.anim.anim_slide_out_right, R.anim.anim_slide_in_right);
                break;
            case R.id.nav_today_sale:
                fragment = CustomerInvoiceFragment.newInstance(customer_id, customer_name);
                break;
            case R.id.nav_goods_return:
                fragment = CustomerRejectionFragment.newInstance(customer_id, customer_name);
                break;
            case R.id.nav_payment:
                fragment = CustomerPaymentFragment.newInstance(customer_id, customer_name);
                break;
            case R.id.nav_crate_management:
                fragment = CratesManagementFragment.newInstance(customer_id, customer_name);
                break;
           /* case R.id.nav_market_product:
                fragment = CustOtherMarketProductDetailsFragment.newInstance(customer_id, customer_name);
                break;*/
            case R.id.nav_final_invoice:
                fragment = FinalInvoiceFragment.newInstance(customer_id, customer_name);
                break;
            default:
                //fragment = HomeFragment.newInstance();
        }

        if (fragment != null) {
            String fragClassName = fragment.getClass().getName();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (menuItem.getItemId() != R.id.nav_home) {
                // Insert the fragment by replacing any existing fragment
                boolean fragmentPopped = fragmentManager.popBackStackImmediate(fragClassName, 0);
                if (!fragmentPopped) {
                    ft.replace(R.id.flInvoiceContent, fragment);
                    ft.addToBackStack(fragClassName);
                }
            } else {
                ft.replace(R.id.flInvoiceContent, fragment);
            }

            ft.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left, R.anim.anim_slide_out_right, R.anim.anim_slide_in_right);
            ft.commit();

            // Highlight the selected item has been done by NavigationView
            //menuItem.setChecked(true);

            // Set action bar title
            tvNavTitle.setText(menuItem.getTitle());
            imgSave.setVisibility(View.GONE);
        }
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                containerFrame.setTranslationX(slideOffset * drawerView.getWidth());
                toolbar.setTranslationX(slideOffset * drawerView.getWidth());
                mDrawer.bringChildToFront(drawerView);
                mDrawer.requestLayout();
                //below line used to remove shadow of drawer
                mDrawer.setScrimColor(Color.TRANSPARENT);
            }//this method helps you to aside menu drawer
        };
        return toggle;

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            //close drawer first
            mDrawer.closeDrawers();
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                super.onBackPressed();
                overridePendingTransition(R.anim.anim_slide_out_right, R.anim.anim_slide_in_right);
            } else {
                super.onBackPressed();
                overridePendingTransition(R.anim.anim_slide_out_right, R.anim.anim_slide_in_right);
                finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // get current fragment in container
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.flInvoiceContent);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    /*permission check redirect*/
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // get current fragment in container
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.flInvoiceContent);
        fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}