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

    private static final String DATABASE_NAME = "Sicon_route";
    private static final String TABLE_NAME = "V_SD_Route_Master";

    private static final String REC_ID = "Rec_Id";
    private static final String SUB_COMPANY_ID = "Sub_Company_id";
    private static final String DEPOT_ID = "Depot_id";
    private static final String SUBDEPOT_ID = "SubDepot_id";
    private static final String ROUTE_ID = "Route_Id";
    private static final String ROUTE_NAME = "Route_Name";
    private static final String ROUTE_DESCRIPTION = "Route_Description";
    private static final String SALE_DATE_PARAMETER = "Sale_Date_Parameter";
    private static final String LOADING_TYPE = "Loading_Type";
    private static final String TCC = "TCC";
    private static final String MAINDEPOT = "MainDepot";
    private static final String FLAG = "Flag";
    // private static final String LAST_INVOICE_NO = "last_invoice_no";
    private static final String LAST_BILL_NO = "last_bill_no";

    public RouteView(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + REC_ID + " INTEGER NULL, "
                + SUB_COMPANY_ID + " TEXT NULL, " + DEPOT_ID + " TEXT NULL, " + SUBDEPOT_ID + " TEXT NULL, "
                + ROUTE_ID + " TEXT NULL, " + ROUTE_NAME + " TEXT NULL, " + ROUTE_DESCRIPTION + " TEXT NULL, "
                + SALE_DATE_PARAMETER + " TEXT NULL, " + LOADING_TYPE + " TEXT NULL, " + TCC + " INTEGER NULL, "
                + MAINDEPOT + " TEXT NULL, " + FLAG + " INTEGER NULL, "
                //+ LAST_INVOICE_NO + " TEXT NULL, "
                + LAST_BILL_NO + " TEXT NULL)");
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
        final int subCompanyIdColumn = ih.getColumnIndex(SUB_COMPANY_ID);
        final int depotIdColumn = ih.getColumnIndex(DEPOT_ID);
        final int subDepotIdColumn = ih.getColumnIndex(SUBDEPOT_ID);
        final int routeIdColumn = ih.getColumnIndex(ROUTE_ID);
        final int routeNameColumn = ih.getColumnIndex(ROUTE_NAME);
        final int routeDescColumn = ih.getColumnIndex(ROUTE_DESCRIPTION);
        final int saleDateParameterColumn = ih.getColumnIndex(SALE_DATE_PARAMETER);
        final int loadingTypeColumn = ih.getColumnIndex(LOADING_TYPE);
        final int tccColumn = ih.getColumnIndex(TCC);
        final int flagColumn = ih.getColumnIndex(FLAG);
        final int lastBillNoColumn = ih.getColumnIndex(LAST_BILL_NO);

        try {
            db.beginTransaction();
            ih.prepareForInsert();
            ih.bind(recIdColumn, routeModel.getRecId());
            ih.bind(subCompanyIdColumn, routeModel.getSubCompanyId());
            ih.bind(depotIdColumn, routeModel.getDepotId());
            ih.bind(subDepotIdColumn, routeModel.getSubDepotId());
            ih.bind(routeIdColumn, routeModel.getRouteId());
            ih.bind(routeNameColumn, routeModel.getRouteName());
            ih.bind(routeDescColumn, routeModel.getRouteDescription());
            ih.bind(saleDateParameterColumn, routeModel.getSaleDateParameter());
            ih.bind(loadingTypeColumn, routeModel.getLoadingType());
            ih.bind(tccColumn, routeModel.getTCC());
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
            routeModel.setSubCompanyId(res.getString(res.getColumnIndex(SUB_COMPANY_ID)));
            routeModel.setDepotId(res.getString(res.getColumnIndex(DEPOT_ID)));
            routeModel.setSubDepotId(res.getString(res.getColumnIndex(SUBDEPOT_ID)));
            routeModel.setRouteId(res.getString(res.getColumnIndex(ROUTE_ID)));
            routeModel.setRouteName(res.getString(res.getColumnIndex(ROUTE_NAME)));
            routeModel.setRouteDescription(res.getString(res.getColumnIndex(ROUTE_DESCRIPTION)));
            routeModel.setSaleDateParameter(res.getString(res.getColumnIndex(SALE_DATE_PARAMETER)));
            routeModel.setLoadingType(res.getString(res.getColumnIndex(LOADING_TYPE)));
            routeModel.setTCC(res.getInt(res.getColumnIndex(TCC)));
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

    /*
        contentValues.put(IP, userRoleMapModel.getIp());
        contentValues.put(U_TS, userRoleMapModel.getU_ts());
        db.update(TABLE_NAME, contentValues, USER_ROLE_ID + "= ? ", new String[]{Integer.toString(userRoleMapModel.getUser_role_id())});
        db.close();
        return true;
    }*/

 /*   public Integer deleteUserRoleMapById(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, USER_ROLE_ID + "= ? ", new String[]{Integer.toString(id)});
    }*/

    public RouteModel getRoute() {
        RouteModel routeModel = new RouteModel();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (res.moveToFirst()) {
            //while (res.isAfterLast() == false) {
            routeModel.setRecId(res.getInt(res.getColumnIndex(REC_ID)));
            routeModel.setSubCompanyId(res.getString(res.getColumnIndex(SUB_COMPANY_ID)));
            routeModel.setDepotId(res.getString(res.getColumnIndex(DEPOT_ID)));
            routeModel.setSubDepotId(res.getString(res.getColumnIndex(SUBDEPOT_ID)));
            routeModel.setRouteId(res.getString(res.getColumnIndex(ROUTE_ID)));
            routeModel.setRouteName(res.getString(res.getColumnIndex(ROUTE_NAME)));
            routeModel.setRouteDescription(res.getString(res.getColumnIndex(ROUTE_DESCRIPTION)));
            routeModel.setSaleDateParameter(res.getString(res.getColumnIndex(SALE_DATE_PARAMETER)));
            routeModel.setLoadingType(res.getString(res.getColumnIndex(LOADING_TYPE)));
            routeModel.setTCC(res.getInt(res.getColumnIndex(TCC)));
            //routeModel.setMainDepot(res.getString(res.getColumnIndex(MAINDEPOT)));
            routeModel.setFlag(res.getInt(res.getColumnIndex(FLAG)));
            routeModel.setExpectedLastBillNo(res.getString(res.getColumnIndex(LAST_BILL_NO)));
            //}
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
                routeModel.setSubCompanyId(res.getString(res.getColumnIndex(SUB_COMPANY_ID)));
                routeModel.setDepotId(res.getString(res.getColumnIndex(DEPOT_ID)));
                routeModel.setSubDepotId(res.getString(res.getColumnIndex(SUBDEPOT_ID)));
                routeModel.setRouteId(res.getString(res.getColumnIndex(ROUTE_ID)));
                routeModel.setRouteName(res.getString(res.getColumnIndex(ROUTE_NAME)));
                routeModel.setRouteDescription(res.getString(res.getColumnIndex(ROUTE_DESCRIPTION)));
                routeModel.setSaleDateParameter(res.getString(res.getColumnIndex(SALE_DATE_PARAMETER)));
                routeModel.setLoadingType(res.getString(res.getColumnIndex(LOADING_TYPE)));
                routeModel.setTCC(res.getInt(res.getColumnIndex(TCC)));
                //routeModel.setMainDepot(res.getString(res.getColumnIndex(MAINDEPOT)));
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
}
