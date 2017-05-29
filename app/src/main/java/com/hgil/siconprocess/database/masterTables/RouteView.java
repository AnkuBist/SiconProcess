package com.hgil.siconprocess.database.masterTables;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hgil.siconprocess.retrofit.loginResponse.dbModels.RouteModel;

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

    public RouteView(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + REC_ID + " INTEGER NULL, "
                + DEPOT_ID + " TEXT NULL, " + SUB_COMPANY_ID + " TEXT NULL, " + ROUTE_ID + " TEXT NULL, "
                + ROUTE_NAME + " TEXT NULL, " + ROUTE_MANAGEMENT_ID + " TEXT NULL, "
                + ROUTE_CASHIER_NAME + " TEXT NULL, " + ROUTE_CRATE_LOADING + " INTEGER NULL, "
                + FLAG + " INTEGER NULL, " + LAST_BILL_NO + " TEXT NULL)");
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
            ih.bind(lastBillNoColumn, routeModel.getExpectedLastBillNo());
            ih.execute();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        db.close();
        return true;
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
