package com.hgil.siconprocess.base;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.hgil.siconprocess.R;
import com.hgil.siconprocess.activity.NavBaseActivity;
import com.hgil.siconprocess.utils.Utility;

import butterknife.BindString;
import butterknife.ButterKnife;

/**
 * Created by mohan.giri on 02-02-2017.
 */

public abstract class BaseFragment extends Fragment {
    protected static final String CUSTOMER_ID = "customer_id";
    protected static final String CUSTOMER_NAME = "customer_name";
    protected String customer_id, customer_name;

    protected TextView tvNavTitle, tvNavDate;
    protected ImageView imgSave;
    protected String routeId, routeName;

    @BindString(R.string.strRupee)
    protected String strRupee;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setRetainInstance(true);
        routeId = SiconApp.getInstance().getRouteId();
        routeName = SiconApp.getInstance().getRouteName();
    }

   /* @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        injectDependencies();
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(getFragmentLayout(), container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // setRetainInstance(true);
        //Utility.closeKeyboard(getActivity(), getView());
        bindViews(view);
        getToolbarView();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void getToolbarView() {
        tvNavTitle = (TextView) getActivity().findViewById(R.id.tvNavTitle);
        tvNavDate = (TextView) getActivity().findViewById(R.id.tvNavDate);
        imgSave = (ImageView) getActivity().findViewById(R.id.imgSave);
        updateOkIcon();
    }

    protected abstract int getFragmentLayout();

    /* private void injectDependencies() {
         ((BaseActivity) getActivity()).bind(this);
     }
 */
    private void bindViews(final View view) {
        ButterKnife.bind(this, view);
    }

    /*handling views using common method for fragments*/
    public void setTitle(String title) {
        tvNavTitle.setText(title);
    }

    public void showSaveButton() {
        imgSave.setVisibility(View.VISIBLE);
    }

    public void hideSaveButton() {
        imgSave.setVisibility(View.GONE);
    }

    /*public void onDestroyView(){
        // do nothing
    }*/

    public String getRouteId() {
        return routeId;
    }

    public String getRouteName() {
        return routeName;
    }

    // sample snackbar
    public static void showSnackbar(View view, String message) {
        // make snackbar
        Snackbar mSnackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        // get snackbar view
        View mView = mSnackbar.getView();
        // get textview inside snackbar view
        TextView mTextView = (TextView) mView.findViewById(android.support.design.R.id.snackbar_text);
        // set text to center
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        else
            mTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        // show the snackbar
        mSnackbar.show();
    }

    public void updateSaveIcon() {
        imgSave.setImageResource(R.mipmap.ic_nav_save);
    }

    public void updateOkIcon() {
        imgSave.setImageResource(R.mipmap.ic_ok);
    }

    protected void launchInvoiceFragment(Fragment fragment) {
        String fragClassName = fragment.getClass().getName();
        FragmentManager fragmentManager = (getActivity().getSupportFragmentManager());
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left, R.anim.anim_slide_out_right, R.anim.anim_slide_in_right)
                .replace(R.id.flInvoiceContent, fragment)
                .addToBackStack(fragClassName)
                .commit();
    }

    protected void launchNavFragment(Fragment fragment) {
        String fragClassName = fragment.getClass().getName();
        FragmentManager fragmentManager = ((NavBaseActivity) getActivity()).getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left, R.anim.anim_slide_out_right, R.anim.anim_slide_in_right)
                .replace(R.id.flContent, fragment)
                .addToBackStack(fragClassName)
                .commit();
    }

}
