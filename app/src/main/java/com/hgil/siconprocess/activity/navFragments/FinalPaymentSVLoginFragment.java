package com.hgil.siconprocess.activity.navFragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hgil.siconprocess.R;
import com.hgil.siconprocess.activity.fragments.finalPayment.RouteClose_FinalPaymentFragment;
import com.hgil.siconprocess.base.BaseFragment;
import com.hgil.siconprocess.database.masterTables.RouteView;
import com.hgil.siconprocess.retrofit.RetrofitService;
import com.hgil.siconprocess.retrofit.RetrofitUtil;
import com.hgil.siconprocess.retrofit.loginResponse.loginResponse;
import com.hgil.siconprocess.utils.Utility;
import com.hgil.siconprocess.utils.ui.SampleDialog;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinalPaymentSVLoginFragment extends BaseFragment {
    @BindView(R.id.tvRouteName)
    TextView tvRouteName;
    @BindView(R.id.etSupervisorCode)
    EditText etSupervisorCode;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;

    private RouteView routeView;
    private Handler updateBarHandler;

    public FinalPaymentSVLoginFragment() {
        // Required empty public constructor
    }

    public static FinalPaymentSVLoginFragment newInstance() {
        FinalPaymentSVLoginFragment fragment = new FinalPaymentSVLoginFragment();
        return fragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_final_payment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // set route name to the route
        tvRouteName.setText(getRouteName());
        hideSaveButton();
        setTitle("Final Payment");

        updateBarHandler = new Handler();

        routeView = new RouteView(getContext());

        final int finalPaymentStatus = routeView.finalPaymentStatus(getRouteId());
        final int vanCloseStatus = routeView.vanCloseStatus(getRouteId());
        final String imei_number = Utility.readPreference(getContext(), Utility.DEVICE_IMEI);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String supervisor_code = etSupervisorCode.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (supervisor_code.isEmpty()) {
                    showSnackbar(getView(), "Please enter supervisor code");
                } else if (password.isEmpty()) {
                    showSnackbar(getView(), "Please enter password");
                } else {
                    if (vanCloseStatus != 1) {
                        new SampleDialog("Please Close Van before making final payment.", getContext());
                    } else if (finalPaymentStatus == 1) {
                        new SampleDialog("Route is already closed.", getContext());
                    } else {
                        supervisorLogin(getRouteModel().getDepotId(), supervisor_code, password, imei_number);
                    }
                }
            }
        });
    }

    /*supervisor login*/
    public void supervisorLogin(String depot_id, final String supervisor_code, final String password, String imei_number) {
        updateBarHandler.post(new Runnable() {
            public void run() {
                RetrofitUtil.showDialog(getContext(), getString(R.string.str_supervisor_login));
            }
        });
        RetrofitService service = RetrofitUtil.retrofitClient();
        Call<loginResponse> apiCall = service.supervisorLogin(depot_id, supervisor_code, password, imei_number);
        apiCall.enqueue(new Callback<loginResponse>() {
            @Override
            public void onResponse(Call<loginResponse> call, Response<loginResponse> response) {
                updateBarHandler.post(new Runnable() {
                    public void run() {
                        RetrofitUtil.updateDialogTitle(getString(R.string.str_supervisor_detail_fetch));
                    }
                });
                try {
                    final loginResponse loginResult = response.body();

                    if (loginResult.getReturnCode()) {
                        updateBarHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                RetrofitUtil.hideDialog();
                            }
                        }, 500);
                        showSnackbar(getView(), loginResult.getStrMessage());
                        RouteClose_FinalPaymentFragment fragment = RouteClose_FinalPaymentFragment.newInstance(supervisor_code);
                        launchNavFragment(fragment);
                    } else {
                        updateBarHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                RetrofitUtil.hideDialog();
                            }
                        }, 500);
                        new SampleDialog("", loginResult.getStrMessage(), getContext());
                    }
                } catch (Exception e) {
                    updateBarHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            RetrofitUtil.hideDialog();
                        }
                    }, 500);
                    new SampleDialog("", getString(R.string.str_error_supervisor_login), getContext());
                }
            }

            @Override
            public void onFailure(Call<loginResponse> call, Throwable t) {
                updateBarHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        RetrofitUtil.hideDialog();
                    }
                }, 500);
                // show some error toast or message to display the api call issue
                new SampleDialog("", getString(R.string.str_retrofit_failure), getContext());
            }
        });
    }
}
