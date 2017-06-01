package com.hgil.siconprocess.database.masterTables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hgil.siconprocess.retrofit.loginResponse.dbModels.RouteModel;
import com.hgil.siconprocess.syncPOJO.FinalPaymentModel;

import java.util.ArrayList;

/**
 * Created by mohan.giri on 24-01-2017.
 */

public class RouteView extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "Sicon_route";
    private static final String TABLE_NAME = "V_SD_Route_Master";

    private static final String REC_ID = "Rec_Id";
    private static final String DEPOT_ID = "Depot_id";
    private static final String SUB_COMPANY_ID = "subCompanyId";
    private static final String ROUTE_ID = "Route_Id";
    private static final String ROUTE_NAME = "Route_Name";
    private static final String ROUTE_MANAGEMENT_ID = "route_management_id";
    private static final String ROUTE_CASHIER_NAME = "cashier_name";
    private static final String ROUTE_CRATE_LOADING = "crateLoading";
    private static final String FLAG = "Flag";
    private static final String LAST_BILL_NO = "last_bill_no";
    private static final String CUSTOMER_CARE_NO = "customer_care_no";

    private static final String VAN_CLOSE_STATUS = "van_close_status";
    private static final String FINAL_PAYMENT_STATUS = "final_payment_status";

    /*route close details*/
    //update details at van close time
    private static final String NET_TOTAL_SALE = "net_total_amount";
    private static final String M_REJ_AMOUNT = "m_rej_amount";
    private static final String F_REJ_AMOUNT = "f_rej_amount";
    private static final String SAMPLE_AMOUNT = "sample_amount";
    private static final String LEFTOVER_AMOUNT = "leftover_amount";
    private static final String CASHIER_RECEIVED_AMOUNT = "cashier_receive_amount";

    /*route close*/
    private static final String ROUTE_CLOSE = "route_close";

    public RouteView(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + REC_ID + " INTEGER NULL, "
                + DEPOT_ID + " TEXT NULL, " + SUB_COMPANY_ID + " TEXT NULL, " + ROUTE_ID + " TEXT NULL, "
                + ROUTE_NAME + " TEXT NULL, " + ROUTE_MANAGEMENT_ID + " TEXT NULL, "
                + ROUTE_CASHIER_NAME + " TEXT NULL, " + ROUTE_CRATE_LOADING + " INTEGER NULL, "
                + FLAG + " INTEGER NULL, " + VAN_CLOSE_STATUS + " INTEGER NULL, " + FINAL_PAYMENT_STATUS + " INTEGER NULL, "
                + NET_TOTAL_SALE + " REAL NULL, " + M_REJ_AMOUNT + " REAL NULL, " + F_REJ_AMOUNT + " REAL NULL, "
                + SAMPLE_AMOUNT + " REAL NULL, " + LEFTOVER_AMOUNT + " REAL NULL, " + CASHIER_RECEIVED_AMOUNT + " REAL NULL, "
                + ROUTE_CLOSE + " INTEGER NULL, " + CUSTOMER_CARE_NO + " TEXT NULL, " + LAST_BILL_NO + " TEXT NULL)");
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

    //insert single
    public boolean insertRoute(RouteModel routeModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        DatabaseUtils.InsertHelper ih = new DatabaseUtils.InsertHelper(db, TABLE_NAME);

        // Get the numeric indexes for each of the columns that we're updating
        final int recIdColumn = ih.getColumnIndex(REC_ID);
        final int depotIdColumn = ih.getColumnIndex(DEPOT_ID);
        final int subCompanyIdColumn = ih.getColumnIndex(SUB_COMPANY_ID);
        final int routeIdColumn = ih.getColumnIndex(ROUTE_ID);
        final int routeNameColumn = ih.getColumnIndex(ROUTE_NAME);
        final int routeManagementIdColumn = ih.getColumnIndex(ROUTE_MANAGEMENT_ID);
        final int routeCashierNameColumn = ih.getColumnIndex(ROUTE_CASHIER_NAME);
        final int routeCrateLoadingColumn = ih.getColumnIndex(ROUTE_CRATE_LOADING);
        final int flagColumn = ih.getColumnIndex(FLAG);
        final int lastBillNoColumn = ih.getColumnIndex(LAST_BILL_NO);
        final int customerCareNoColumn = ih.getColumnIndex(CUSTOMER_CARE_NO);

        try {
            db.beginTransaction();
            ih.prepareForInsert();
            ih.bind(recIdColumn, routeModel.getRecId());
            ih.bind(depotIdColumn, routeModel.getDepotId());
            ih.bind(subCompanyIdColumn, routeModel.getSubCompanyId());
            ih.bind(routeIdColumn, routeModel.getRouteId());
            ih.bind(routeNameColumn, routeModel.getRouteName());
            ih.bind(routeManagementIdColumn, routeModel.getRouteManagementId());
            ih.bind(routeCashierNameColumn, routeModel.getCashierCode());
            ih.bind(routeCrateLoadingColumn, routeModel.getCrateLoading());
            ih.bind(flagColumn, routeModel.getFlag());
            ih.bind(customerCareNoColumn, routeModel.getCustomerCareNo());
            ih.bind(lastBillNoColumn, routeModel.getExpectedLastBillNo());
            ih.execute();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        db.close();
        return true;
    }

    public FinalPaymentModel routeFinalPayment(String route_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + ROUTE_ID + "=?", new String[]{route_id});

        FinalPaymentModel paymentModel = new FinalPaymentModel();
        if (res.moveToFirst()) {
            paymentModel.setRoute_id(res.getString(res.getColumnIndex(ROUTE_ID)));
            paymentModel.setTotal_amount(res.getDouble(res.getColumnIndex(NET_TOTAL_SALE)));
            paymentModel.setF_rej_amount(res.getDouble(res.getColumnIndex(F_REJ_AMOUNT)));
            paymentModel.setM_rej_amount(res.getDouble(res.getColumnIndex(M_REJ_AMOUNT)));
            paymentModel.setLeftover_amount(res.getDouble(res.getColumnIndex(LEFTOVER_AMOUNT)));
            paymentModel.setSample_amount(res.getDouble(res.getColumnIndex(SAMPLE_AMOUNT)));
            paymentModel.setCashier_receive_amount(res.getDouble(res.getColumnIndex(CASHIER_RECEIVED_AMOUNT)));
        }
        res.close();
        db.close();
        return paymentModel;
    }

    /*update route final payment model*/
    public void updateRouteFinalPayment(FinalPaymentModel paymentModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NET_TOTAL_SALE, paymentModel.getTotal_amount());
        contentValues.put(F_REJ_AMOUNT, paymentModel.getF_rej_amount());
        contentValues.put(M_REJ_AMOUNT, paymentModel.getM_rej_amount());
        contentValues.put(LEFTOVER_AMOUNT, paymentModel.getLeftover_amount());
        contentValues.put(SAMPLE_AMOUNT, paymentModel.getSample_amount());
        contentValues.put(CASHIER_RECEIVED_AMOUNT, paymentModel.getCashier_receive_amount());
        db.update(TABLE_NAME, contentValues, ROUTE_ID + "=?", new String[]{paymentModel.getRoute_id()});
        db.close();
    }

    //get van close status
    public int vanCloseStatus(String route_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select " + VAN_CLOSE_STATUS + " from " + TABLE_NAME + " WHERE " + ROUTE_ID + "=?";
        Cursor res = db.rawQuery(query, new String[]{route_id});

        int status = 0;
        if (res.moveToFirst()) {
            status = res.getInt(res.getColumnIndex(VAN_CLOSE_STATUS));
        }
        res.close();
        db.close();
        return status;
    }

    //update van close status
    public void updateVanClose(String route_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(VAN_CLOSE_STATUS, 1);
        db.update(TABLE_NAME, contentValues, ROUTE_ID + "=?", new String[]{route_id});
        db.close();
    }

    //get route close status
    public int finalPaymentStatus(String route_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select " + FINAL_PAYMENT_STATUS + " from " + TABLE_NAME + " WHERE " + ROUTE_ID + "=?";
        Cursor res = db.rawQuery(query, new String[]{route_id});

        int status = 0;
        if (res.moveToFirst()) {
            status = res.getInt(res.getColumnIndex(FINAL_PAYMENT_STATUS));
        }
        res.close();
        db.close();
        return status;
    }

    //update route close status
    public void updateFinalPaymentStatus(String route_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FINAL_PAYMENT_STATUS, 1);
        db.update(TABLE_NAME, contentValues, ROUTE_ID + "=?", new String[]{route_id});
        db.close();
    }

    public RouteModel geRouteInfoByRouteId(String route_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + ROUTE_ID + "='" + route_id + "'", null);

        RouteModel routeModel = new RouteModel();
        if (res.moveToFirst()) {
            routeModel.setRecId(res.getInt(res.getColumnIndex(REC_ID)));
            routeModel.setDepotId(res.getString(res.getColumnIndex(DEPOT_ID)));
            routeModel.setSubCompanyId(res.getString(res.getColumnIndex(SUB_COMPANY_ID)));
            routeModel.setRouteId(res.getString(res.getColumnIndex(ROUTE_ID)));
            routeModel.setRouteName(res.getString(res.getColumnIndex(ROUTE_NAME)));
            routeModel.setRouteManagementId(res.getString(res.getColumnIndex(ROUTE_MANAGEMENT_ID)));
            routeModel.setCashierCode(res.getString(res.getColumnIndex(ROUTE_CASHIER_NAME)));
            routeModel.setCrateLoading(res.getInt(res.getColumnIndex(ROUTE_CRATE_LOADING)));
            routeModel.setFlag(res.getInt(res.getColumnIndex(FLAG)));
            routeModel.setCustomerCareNo(res.getString(res.getColumnIndex(CUSTOMER_CARE_NO)));
            routeModel.setExpectedLastBillNo(res.getString(res.getColumnIndex(LAST_BILL_NO)));
        }
        res.close();
        db.close();
        return routeModel;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        db.close();
        return numRows;
    }

    public RouteModel getRoute() {
        RouteModel routeModel = new RouteModel();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (res.moveToFirst()) {
            routeModel.setRecId(res.getInt(res.getColumnIndex(REC_ID)));
            routeModel.setDepotId(res.getString(res.getColumnIndex(DEPOT_ID)));
            routeModel.setSubCompanyId(res.getString(res.getColumnIndex(SUB_COMPANY_ID)));
            routeModel.setRouteId(res.getString(res.getColumnIndex(ROUTE_ID)));
            routeModel.setRouteName(res.getString(res.getColumnIndex(ROUTE_NAME)));
            routeModel.setRouteManagementId(res.getString(res.getColumnIndex(ROUTE_MANAGEMENT_ID)));
            routeModel.setCashierCode(res.getString(res.getColumnIndex(ROUTE_CASHIER_NAME)));
            routeModel.setCrateLoading(res.getInt(res.getColumnIndex(ROUTE_CRATE_LOADING)));
            routeModel.setFlag(res.getInt(res.getColumnIndex(FLAG)));
            routeModel.setCustomerCareNo(res.getString(res.getColumnIndex(CUSTOMER_CARE_NO)));
            routeModel.setExpectedLastBillNo(res.getString(res.getColumnIndex(LAST_BILL_NO)));
        }
        res.close();
        db.close();
        return routeModel;
    }

    public ArrayList<RouteModel> getAllRoutes() {
        ArrayList<RouteModel> array_list = new ArrayList<RouteModel>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (res.moveToFirst()) {
            while (res.isAfterLast() == false) {
                RouteModel routeModel = new RouteModel();
                routeModel.setRecId(res.getInt(res.getColumnIndex(REC_ID)));
                routeModel.setDepotId(res.getString(res.getColumnIndex(DEPOT_ID)));
                routeModel.setSubCompanyId(res.getString(res.getColumnIndex(SUB_COMPANY_ID)));
                routeModel.setRouteId(res.getString(res.getColumnIndex(ROUTE_ID)));
                routeModel.setRouteName(res.getString(res.getColumnIndex(ROUTE_NAME)));
                routeModel.setRouteManagementId(res.getString(res.getColumnIndex(ROUTE_MANAGEMENT_ID)));
                routeModel.setCashierCode(res.getString(res.getColumnIndex(ROUTE_CASHIER_NAME)));
                routeModel.setCrateLoading(res.getInt(res.getColumnIndex(ROUTE_CRATE_LOADING)));
                routeModel.setFlag(res.getInt(res.getColumnIndex(FLAG)));
                routeModel.setCustomerCareNo(res.getString(res.getColumnIndex(CUSTOMER_CARE_NO)));
                routeModel.setExpectedLastBillNo(res.getString(res.getColumnIndex(LAST_BILL_NO)));

                array_list.add(routeModel);
                res.moveToNext();
            }
        }
        res.close();
        db.close();
        return array_list;
    }

    /*public int vanTotalCrate() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select " + ROUTE_CRATE_LOADING +
                " from " + TABLE_NAME;
        Cursor res = db.rawQuery(query, null);

        int total_crates = 0;
        if (res.moveToFirst()) {
            total_crates = res.getInt(res.getColumnIndex(ROUTE_CRATE_LOADING));
        }
        res.close();
        db.close();
        return total_crates;
    }*/
}
