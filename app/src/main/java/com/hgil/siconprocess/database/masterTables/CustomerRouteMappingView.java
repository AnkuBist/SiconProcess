package com.hgil.siconprocess.database.masterTables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hgil.siconprocess.adapter.routeMap.RouteCustomerModel;
import com.hgil.siconprocess.database.tables.InvoiceOutTable;
import com.hgil.siconprocess.database.tables.PaymentTable;
import com.hgil.siconprocess.retrofit.loginResponse.dbModels.CustomerRouteMapModel;
import com.hgil.siconprocess.retrofit.loginResponse.dbModels.RcReason;
import com.hgil.siconprocess.syncPOJO.invoiceSyncModel.OutletCloseDetail;
import com.hgil.siconprocess.utils.Constant;
import com.hgil.siconprocess.utils.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohan.giri on 24-01-2017.
 */

public class CustomerRouteMappingView extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;

    private static final String DATABASE_NAME = "Sicon_route_map";
    private static final String TABLE_NAME = "V_SD_Customer_Route_Mapping";

    private static final String ROUTE_ID = "Route_id";
    private static final String ROUTE_NAME = "Route_Name";
    private static final String CUSTOMER_ID = "Customer_id";
    private static final String CUSTOMER_NAME = "Customer_Name";
    private static final String PRICEGROUP = "PRICEGROUP";
    private static final String LINEDISC = "LINEDISC";
    private static final String C_TYPE = "C_Type";
    private static final String CRATE_LOADING = "crate_loading";
    private static final String CRATE_CREDIT = "crate_credit";
    private static final String AMOUNT_CREDIT = "amount_credit";
    private static final String CUSTCLASSIFICATIONID = "CUSTCLASSIFICATIONID";

    // customer status
    private static final String INVOICE_NUMBER = "invoice_number";
    private static final String BILL_NO = "bill_no";
    private static final String CUST_STATUS = "Customer_status";
    private static final String CUST_SALE_AMT = "cust_sale_amt";
    private static final String CUST_RECEIVED_AMT = "received_amt";
    private static final String REASON_ID = "reason_id";
    private static final String REASON_STATUS = "reason_status";
    private static final String UPDATE_TIME = "update_time";

    private Context mContext;

    public CustomerRouteMappingView(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + ROUTE_ID + " TEXT NULL, "
                + ROUTE_NAME + " TEXT NULL, "
                + CUSTOMER_ID + " TEXT NULL, " + CUSTOMER_NAME + " TEXT NOT NULL, " + PRICEGROUP + " TEXT NOT NULL, "
                + LINEDISC + " TEXT NOT NULL, " + C_TYPE + " TEXT NOT NULL, " + CUSTCLASSIFICATIONID + " TEXT NULL, "
                + CRATE_LOADING + " INTEGER NULL, " + CRATE_CREDIT + " INTEGER NULL, " + AMOUNT_CREDIT + " REAL NULL, "
                + INVOICE_NUMBER + " TEXT NULL, " + BILL_NO + " TEXT NULL, " + CUST_SALE_AMT + " REAL NULL, "
                + CUST_RECEIVED_AMT + " REAL NULL, " + CUST_STATUS + " TEXT NULL, " + REASON_ID + " INTEGER NULL, "
                + REASON_STATUS + " TEXT NULL, " + UPDATE_TIME + " TEXT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void eraseTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME); //delete all rows in a table
        db.close();
    }

    //update customer no order reason
    public void updateOrderReason(String customer_id, int reason_id, String reason) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(REASON_ID, reason_id);
        contentValues.put(REASON_STATUS, reason);
        db.update(TABLE_NAME, contentValues, CUSTOMER_ID + "=?", new String[]{customer_id});
        db.close();
    }

    //update route customer status and update time
    public void updateCustomerStatus(String customer_id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CUST_STATUS, status);
        contentValues.put(UPDATE_TIME, Utility.timeStamp());
        db.update(TABLE_NAME, contentValues, CUSTOMER_ID + "=?", new String[]{customer_id});
        db.close();
    }

    // multiple insert
    public boolean insertCustomerRouteMap(List<CustomerRouteMapModel> arrList) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < arrList.size(); i++) {
            CustomerRouteMapModel customerRouteMapModel = arrList.get(i);
            ContentValues contentValues = new ContentValues();
            contentValues.put(ROUTE_ID, customerRouteMapModel.getRouteId());
            contentValues.put(ROUTE_NAME, customerRouteMapModel.getRouteName());
            contentValues.put(CUSTOMER_ID, customerRouteMapModel.getCustomerId());
            contentValues.put(CUSTOMER_NAME, customerRouteMapModel.getCustomerName());
            contentValues.put(PRICEGROUP, customerRouteMapModel.getPRICEGROUP());
            contentValues.put(LINEDISC, customerRouteMapModel.getLINEDISC());
            contentValues.put(C_TYPE, customerRouteMapModel.getCType());
            contentValues.put(CUSTCLASSIFICATIONID, customerRouteMapModel.getCUSTCLASSIFICATIONID());
            contentValues.put(CRATE_LOADING, customerRouteMapModel.getCrateLoading());
            contentValues.put(CRATE_CREDIT, customerRouteMapModel.getCrateCredit());
            contentValues.put(AMOUNT_CREDIT, customerRouteMapModel.getAmountCredit());
            contentValues.put(CUST_STATUS, customerRouteMapModel.getCustStatus());
            db.insert(TABLE_NAME, null, contentValues);
        }
        db.close();
        return true;
    }

    public RcReason custNoOrderReason(String customer_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT " + REASON_ID + ", " + REASON_STATUS + " FROM "
                + TABLE_NAME + " WHERE " + CUSTOMER_ID + "=?", new String[]{customer_id});

        RcReason rcReason = new RcReason();
        if (res.moveToFirst()) {
            rcReason.setReasonId(res.getInt(res.getColumnIndex(REASON_ID)));
            rcReason.setReason(res.getString(res.getColumnIndex(REASON_STATUS)));
        }
        res.close();
        db.close();
        return rcReason;
    }

    public CustomerRouteMapModel getCustomerRouteMapByCustomer(String customer_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + CUSTOMER_ID + "=?", new String[]{customer_id});

        CustomerRouteMapModel customerRouteMapModel = new CustomerRouteMapModel();
        if (res.moveToFirst()) {
            customerRouteMapModel.setRouteId(res.getString(res.getColumnIndex(ROUTE_ID)));
            customerRouteMapModel.setRouteName(res.getString(res.getColumnIndex(ROUTE_NAME)));
            customerRouteMapModel.setCustomerId(res.getString(res.getColumnIndex(CUSTOMER_ID)));
            customerRouteMapModel.setCustomerName(res.getString(res.getColumnIndex(CUSTOMER_NAME)));
            customerRouteMapModel.setPRICEGROUP(res.getString(res.getColumnIndex(PRICEGROUP)));
            customerRouteMapModel.setLINEDISC(res.getString(res.getColumnIndex(LINEDISC)));
            customerRouteMapModel.setCType(res.getString(res.getColumnIndex(C_TYPE)));
            customerRouteMapModel.setCUSTCLASSIFICATIONID(res.getString(res.getColumnIndex(CUSTCLASSIFICATIONID)));
            customerRouteMapModel.setCrateLoading(res.getInt(res.getColumnIndex(CRATE_LOADING)));
            customerRouteMapModel.setCrateCredit(res.getInt(res.getColumnIndex(CRATE_CREDIT)));
            customerRouteMapModel.setAmountCredit(res.getDouble(res.getColumnIndex(AMOUNT_CREDIT)));
            customerRouteMapModel.setCustStatus(res.getString(res.getColumnIndex(CUST_STATUS)));
        }
        res.close();
        db.close();
        return customerRouteMapModel;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        db.close();
        return numRows;
    }

    public ArrayList<RouteCustomerModel> getRouteCustomers() {
        ArrayList<RouteCustomerModel> array_list = new ArrayList<RouteCustomerModel>();

        SQLiteDatabase db = this.getReadableDatabase();
        InvoiceOutTable invoiceOutTable = new InvoiceOutTable(mContext);
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (res.moveToFirst()) {
            while (res.isAfterLast() == false) {
                RouteCustomerModel routeCustomerModel = new RouteCustomerModel();
                String customer_id = res.getString(res.getColumnIndex(CUSTOMER_ID));
                routeCustomerModel.setRouteId(res.getString(res.getColumnIndex(ROUTE_ID)));
                routeCustomerModel.setRouteName(res.getString(res.getColumnIndex(ROUTE_NAME)));
                routeCustomerModel.setCustomerId(customer_id);
                routeCustomerModel.setCustomerName(res.getString(res.getColumnIndex(CUSTOMER_NAME)));
                routeCustomerModel.setCustStatus(res.getString(res.getColumnIndex(CUST_STATUS)));
                routeCustomerModel.setSaleAmount(invoiceOutTable.custInvoiceTotalAmount(customer_id));

                array_list.add(routeCustomerModel);
                res.moveToNext();
            }
        }
        res.close();
        db.close();
        return array_list;
    }

    /*route pending customers list*/
    public ArrayList<RouteCustomerModel> getRoutePendingCustomers() {
        ArrayList<RouteCustomerModel> array_list = new ArrayList<RouteCustomerModel>();

        SQLiteDatabase db = this.getReadableDatabase();
        InvoiceOutTable invoiceOutTable = new InvoiceOutTable(mContext);
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + CUST_STATUS + "=?", new String[]{"PENDING"});
        if (res.moveToFirst()) {
            while (res.isAfterLast() == false) {
                RouteCustomerModel routeCustomerModel = new RouteCustomerModel();
                routeCustomerModel.setRouteId(res.getString(res.getColumnIndex(ROUTE_ID)));
                routeCustomerModel.setRouteName(res.getString(res.getColumnIndex(ROUTE_NAME)));
                routeCustomerModel.setCustomerId(res.getString(res.getColumnIndex(CUSTOMER_ID)));
                routeCustomerModel.setCustomerName(res.getString(res.getColumnIndex(CUSTOMER_NAME)));
                routeCustomerModel.setCustStatus(res.getString(res.getColumnIndex(CUST_STATUS)));
                routeCustomerModel.setSaleAmount(invoiceOutTable.custInvoiceTotalAmount(routeCustomerModel.getCustomerId()));

                array_list.add(routeCustomerModel);
                res.moveToNext();
            }
        }
        res.close();
        db.close();
        return array_list;
    }

    /*route completed customers list*/
    public ArrayList<RouteCustomerModel> getRouteCompletedCustomers() {
        ArrayList<RouteCustomerModel> array_list = new ArrayList<RouteCustomerModel>();

        SQLiteDatabase db = this.getReadableDatabase();
        InvoiceOutTable invoiceOutTable = new InvoiceOutTable(mContext);
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + CUST_STATUS + "=?", new String[]{"COMPLETED"});
        if (res.moveToFirst()) {
            while (res.isAfterLast() == false) {
                RouteCustomerModel routeCustomerModel = new RouteCustomerModel();
                routeCustomerModel.setRouteId(res.getString(res.getColumnIndex(ROUTE_ID)));
                routeCustomerModel.setRouteName(res.getString(res.getColumnIndex(ROUTE_NAME)));
                routeCustomerModel.setCustomerId(res.getString(res.getColumnIndex(CUSTOMER_ID)));
                routeCustomerModel.setCustomerName(res.getString(res.getColumnIndex(CUSTOMER_NAME)));
                routeCustomerModel.setCustStatus(res.getString(res.getColumnIndex(CUST_STATUS)));
                routeCustomerModel.setSaleAmount(invoiceOutTable.custInvoiceTotalAmount(routeCustomerModel.getCustomerId()));

                array_list.add(routeCustomerModel);
                res.moveToNext();
            }
        }
        res.close();
        db.close();
        return array_list;
    }

    /*get customer classification id*/
    public String getCustomerClassification(String customer_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT " + CUSTCLASSIFICATIONID + " FROM " + TABLE_NAME + " WHERE " + CUSTOMER_ID + "=?", new String[]{customer_id});

        String classification_id = "";
        if (res.moveToFirst()) {
            classification_id = res.getString(res.getColumnIndex(CUSTCLASSIFICATIONID));
        }
        res.close();
        db.close();
        return classification_id;
    }

    /*get van crate total*/
    public int vanTotalCrate() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select sum(" + CRATE_LOADING + ") as total_crates " +
                "from " + TABLE_NAME;
        Cursor res = db.rawQuery(query, null);

        int total_crates = 0;
        if (res.moveToFirst()) {
            total_crates = res.getInt(res.getColumnIndex("total_crates"));
        }
        res.close();
        db.close();
        return total_crates;
    }

    /*van credit/opening crates*/
    public int vanOpeningCrates() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select sum(" + CRATE_CREDIT + ") as total_crates " +
                "from " + TABLE_NAME;
        Cursor res = db.rawQuery(query, null);

        int total_crates = 0;
        if (res.moveToFirst()) {
            total_crates = res.getInt(res.getColumnIndex("total_crates"));
        }
        res.close();
        db.close();
        return total_crates;
    }

    // get crate opening by user id
    public int custCreditCrates(String customer_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select " + CRATE_CREDIT + " from " + TABLE_NAME + " where " + CUSTOMER_ID + "=?";
        Cursor res = db.rawQuery(query, new String[]{customer_id});

        int crate = 0;
        if (res.moveToFirst()) {
            crate = res.getInt(res.getColumnIndex(CRATE_CREDIT));
        }
        res.close();
        db.close();
        return crate;
    }

    // get crate loading by user id
    public int custLoadingCrates(String customer_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select " + CRATE_LOADING + " from " + TABLE_NAME + " where " + CUSTOMER_ID + "=?";
        Cursor res = db.rawQuery(query, new String[]{customer_id});

        int crate = 0;
        if (res.moveToFirst()) {
            crate = res.getInt(res.getColumnIndex(CRATE_LOADING));
        }
        res.close();
        db.close();
        return crate;
    }

    // customer credit balance
    public double custCreditAmount(String customer_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select " + AMOUNT_CREDIT + " from " + TABLE_NAME + " where " + CUSTOMER_ID + "=?";
        Cursor res = db.rawQuery(query, new String[]{customer_id});

        double amount = 0;
        if (res.moveToFirst()) {
            amount = res.getDouble(res.getColumnIndex(AMOUNT_CREDIT));
        }
        res.close();
        db.close();
        return Utility.roundTwoDecimals(amount);
    }


    /*closed customer details*/
    /*route completed customers list*/
    public ArrayList<OutletCloseDetail> closedOutletDetails() {
        ArrayList<OutletCloseDetail> array_list = new ArrayList<OutletCloseDetail>();

        SQLiteDatabase db = this.getReadableDatabase();
        InvoiceOutTable invoiceOutTable = new InvoiceOutTable(mContext);
        PaymentTable paymentTable = new PaymentTable(mContext);
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + CUST_STATUS + "=?", new String[]{"COMPLETED"});
        if (res.moveToFirst()) {
            while (res.isAfterLast() == false) {
                OutletCloseDetail routeCustomerModel = new OutletCloseDetail();
                String customer_id = res.getString(res.getColumnIndex(CUSTOMER_ID));

                routeCustomerModel.setRoute_id(res.getString(res.getColumnIndex(ROUTE_ID)));
                routeCustomerModel.setRoute_name(res.getString(res.getColumnIndex(ROUTE_NAME)));
                routeCustomerModel.setCustomer_id(customer_id);
                routeCustomerModel.setCustomer_name(res.getString(res.getColumnIndex(CUSTOMER_NAME)));
                routeCustomerModel.setSale_amount(invoiceOutTable.custInvoiceTotalAmount(customer_id));
                routeCustomerModel.setReceived_amount(paymentTable.custCashPaidAmt(customer_id));
                routeCustomerModel.setReason_id(res.getString(res.getColumnIndex(REASON_ID)));
                routeCustomerModel.setReason(res.getString(res.getColumnIndex(REASON_STATUS)));
                routeCustomerModel.setClose_time(res.getString(res.getColumnIndex(UPDATE_TIME)));

                array_list.add(routeCustomerModel);
                res.moveToNext();
            }
        }
        res.close();
        db.close();
        return array_list;
    }

    public int numberPendingCustomers() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME, CUST_STATUS + "<>?",
                new String[]{Constant.STATUS_COMPLETE});
        db.close();
        return numRows;
    }

}