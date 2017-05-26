package com.hgil.siconprocess.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.hgil.siconprocess.R;
import com.hgil.siconprocess.database.masterTables.CustomerItemPriceTable;
import com.hgil.siconprocess.database.masterTables.CustomerRouteMappingView;
import com.hgil.siconprocess.database.masterTables.DepotEmployeeView;
import com.hgil.siconprocess.database.masterTables.DepotInvoiceView;
import com.hgil.siconprocess.database.masterTables.ProductView;
import com.hgil.siconprocess.database.masterTables.RcReasonTable;
import com.hgil.siconprocess.database.masterTables.RouteView;
import com.hgil.siconprocess.database.tables.CustomerRejectionTable;
import com.hgil.siconprocess.database.tables.InvoiceOutTable;
import com.hgil.siconprocess.database.tables.MarketProductTable;
import com.hgil.siconprocess.database.tables.NextDayOrderTable;
import com.hgil.siconprocess.database.tables.PaymentTable;
import com.hgil.siconprocess.retrofit.RetrofitService;
import com.hgil.siconprocess.retrofit.RetrofitUtil;
import com.hgil.siconprocess.retrofit.loginResponse.ObjLoginResponse;
import com.hgil.siconprocess.retrofit.loginResponse.dbModels.RouteModel;
import com.hgil.siconprocess.retrofit.loginResponse.loginResponse;
import com.hgil.siconprocess.utils.Utility;
import com.hgil.siconprocess.utils.ui.SampleDialog;
import com.hgil.siconprocess.utils.utilPermission.UtilIMEI;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final int APP_PERMISSION = 105;
    @BindView(R.id.etUserId)
    EditText etUserId;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.cbSignIn)
    CheckBox cbSignIn;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.coordinateLayout)
    CoordinatorLayout coordinateLayout;

    private RouteView dbRouteView;
    private RcReasonTable dbRcReason;
    private CustomerRouteMappingView dbRouteMapView;
    private CustomerItemPriceTable dbCustomerItemPrice;
    private ProductView dbProductView;
    private DepotInvoiceView dbInvoice;
    private DepotEmployeeView dbEmployee;

    // sync table objects
    private InvoiceOutTable invoiceOutTable;
    private CustomerRejectionTable rejectionTable;
    private PaymentTable paymentTable;
    private NextDayOrderTable nextDayOrderTable;
    private MarketProductTable marketProductTable;
    private String existing_id = "", saved_id = "";
    private Handler updateBarHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        // read shared preference if any
        saved_id = Utility.readPreference(this, Utility.USER_ID);
        String savedPassword = Utility.readPreference(this, Utility.LAST_LOGIN_PASSWORD);
        existing_id = Utility.readPreference(this, Utility.LAST_LOGIN_ID);

        etUserId.setText(saved_id);
        etPassword.setText(savedPassword);

        /// initialise database objects
        initialiseDBObj();
        initializeSyncDbObj();

        updateBarHandler = new Handler();
        // ask all required permission at once only
        askAppPermission();
    }

    public void onSubmit(View view) {
        String username = etUserId.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String imeiNumber = UtilIMEI.getIMEINumber(this);

        if (username.isEmpty()) {
            etUserId.requestFocus();
            Toast.makeText(this, "Enter valid user ID", Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty()) {
            etPassword.requestFocus();
            Toast.makeText(this, "Enter a valid password", Toast.LENGTH_SHORT).show();
        } else if (imeiNumber == null) {
            UtilIMEI.checkAndroidVersionForPhoneState(this);
        } else {
            Utility.savePreference(this, Utility.DEVICE_IMEI, imeiNumber);
            // first check user login with the same existing id or not
            if (checkUserId(username)) {
                // if the user is logged in today but somehow logged out then check for the last logged in date
                // and local data in invoice if there exists any data then simply make user in
                if ((Utility.getCurDate()).matches(Utility.readPreference(LoginActivity.this, Utility.LAST_LOGIN_DATE))) {
                    //match here user saved username and password
                    String last_login_id = Utility.readPreference(LoginActivity.this, Utility.LAST_LOGIN_ID);
                    String last_login_password = Utility.readPreference(LoginActivity.this, Utility.LAST_LOGIN_PASSWORD);
                    if (username.matches(last_login_id) &&
                            password.matches(last_login_password)) {
                        startActivity(new Intent(LoginActivity.this, NavBaseActivity.class));
                        finish();
                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                    } else {
                        Snackbar.make(coordinateLayout, "Username and password combination wrong.", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    // check for login
                    getUserLogin(username, password, imeiNumber);
                }
            } else {
                //TODO--- this is a test code remove these lines and uncomment the below code after this
                // check for login
                //getUserLogin(username, password, imeiNumber);
                Snackbar.make(coordinateLayout, "Please erase app data before login with different user!", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private boolean checkUserId(String user_id) {
        if (existing_id.matches("")) {
            return true;
        } else if (!existing_id.matches("")) {
            if (existing_id.matches(user_id))
                return true;
            else
                return false;
        } else {
            return false;
        }
    }

    private void initialiseDBObj() {
        dbRouteView = new RouteView(this);
        dbRcReason = new RcReasonTable(this);
        dbRouteMapView = new CustomerRouteMappingView(this);
        dbCustomerItemPrice = new CustomerItemPriceTable(this);
        dbProductView = new ProductView(this);
        dbInvoice = new DepotInvoiceView(this);
        dbEmployee = new DepotEmployeeView(this);
    }

    private void eraseAllTableData() {
        dbRouteView.eraseTable();
        dbRcReason.eraseTable();
        dbRouteMapView.eraseTable();
        dbCustomerItemPrice.eraseTable();
        dbProductView.eraseTable();
        dbInvoice.eraseTable();
        dbEmployee.eraseTable();
    }

    public void initializeSyncDbObj() {
        invoiceOutTable = new InvoiceOutTable(this);
        rejectionTable = new CustomerRejectionTable(this);
        paymentTable = new PaymentTable(this);
        nextDayOrderTable = new NextDayOrderTable(this);
        marketProductTable = new MarketProductTable(this);
    }

    public void eraseAllSyncTables() {
        rejectionTable.eraseTable();
        invoiceOutTable.eraseTable();
        paymentTable.eraseTable();
        nextDayOrderTable.eraseTable();
        marketProductTable.eraseTable();
    }

    /*retrofit call test to fetch data from server*/
    public void getUserLogin(final String user_id, final String password, String imei_number) {
        updateBarHandler.post(new Runnable() {
            public void run() {
                RetrofitUtil.showDialog(LoginActivity.this, getString(R.string.str_login));
            }
        });
        RetrofitService service = RetrofitUtil.retrofitClient();
        Call<loginResponse> apiCall = service.postUserLogin(user_id, password, imei_number);
        apiCall.enqueue(new Callback<loginResponse>() {
            @Override
            public void onResponse(Call<loginResponse> call, Response<loginResponse> response) {
                updateBarHandler.post(new Runnable() {
                    public void run() {
                        RetrofitUtil.updateDialogTitle(getString(R.string.str_login_detail_fetch));
                    }
                });
                try {
                    final loginResponse loginResult = response.body();

                    // rest call to read data from api service
                    if (loginResult.getReturnCode()) {
                        if (cbSignIn.isChecked()) {
                            // save the password for the next login too
                            Utility.savePreference(LoginActivity.this, Utility.USER_ID, user_id);
                        }
                        // save user password for local login purpose
                        Utility.savePreference(LoginActivity.this, Utility.LAST_LOGIN_PASSWORD, password);

                        new loginSync(loginResult, user_id).execute();
                    } else {
                        updateBarHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                RetrofitUtil.hideDialog();
                            }
                        }, 500);
                        new SampleDialog("", loginResult.getStrMessage(), LoginActivity.this);
                    }
                } catch (Exception e) {
                    updateBarHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            RetrofitUtil.hideDialog();
                        }
                    }, 500);
                    new SampleDialog("", getString(R.string.str_error_login), LoginActivity.this);
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
                new SampleDialog("", getString(R.string.str_retrofit_failure), LoginActivity.this);
            }
        });
    }

    // AsyncTask copy one
    private class loginSync extends AsyncTask<Void, Void, Boolean> implements Serializable {

        loginResponse loginResponse;
        String user_id;

        public loginSync(loginResponse loginResponse, String user_id) {
            this.loginResponse = loginResponse;
            this.user_id = user_id;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // rest call to read data from api service
            if (loginResponse.getReturnCode()) {
                // erase all masterTables data
                eraseAllTableData();

                // erase table to sync
                //do not erase these sync tables if the last login by the user in the same date in case it will erase all data
                if (!Utility.getCurDate().matches(Utility.readPreference(LoginActivity.this, Utility.LAST_LOGIN_DATE)))
                    eraseAllSyncTables();

                ObjLoginResponse objResponse = loginResponse.getObjLoginResponse();

                // sync data to local table and views
                dbRouteView.insertRoute(objResponse.getRouteDetail());

                RouteModel routeData = objResponse.getRouteDetail();

                dbRcReason.insertReason(routeData.getArrRcReason());
                dbRouteMapView.insertCustomerRouteMap(routeData.getArrCustomerRouteMap());
                dbCustomerItemPrice.insertCustomerItemPrice(routeData.getArrItemDiscountPrice());
                dbProductView.insertProducts(routeData.getArrItemsMaster());
                dbInvoice.insertDepotInvoice(routeData.getArrInvoiceDetails());
                dbEmployee.insertDepotEmployee(routeData.getArrEmployees());

                Utility.saveLoginStatus(LoginActivity.this, Utility.LOGIN_STATUS, true);
                Utility.savePreference(LoginActivity.this, Utility.LAST_LOGIN_ID, user_id);
                Utility.savePreference(LoginActivity.this, Utility.LAST_LOGIN_DATE, Utility.getCurDate());
                return loginResponse.getReturnCode();
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean status) {
            updateBarHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    RetrofitUtil.hideDialog();
                }
            }, 500);
            if (status) {
                startActivity(new Intent(LoginActivity.this, NavBaseActivity.class));
                finish();
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
            } else {
                new SampleDialog("", getString(R.string.str_error_sync_data), LoginActivity.this);
            }
        }
    }

    private void askAppPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int check_COARSE_LOCATION = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
            int check_FINE_LOCATION = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            int check_READ_PHONE_STATE = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
            int check_SEND_SMS = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);

            if (check_COARSE_LOCATION != PackageManager.PERMISSION_GRANTED ||
                    check_FINE_LOCATION != PackageManager.PERMISSION_GRANTED ||
                    check_READ_PHONE_STATE != PackageManager.PERMISSION_GRANTED ||
                    check_SEND_SMS != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.SEND_SMS}, APP_PERMISSION);
                return;
            }
        }
    }

    // request permissions result
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case APP_PERMISSION:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
                            Toast.makeText(this, permissions[i] + " Permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
                return;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
