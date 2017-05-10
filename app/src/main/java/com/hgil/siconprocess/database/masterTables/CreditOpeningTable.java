package com.hgil.siconprocess.database.masterTables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hgil.siconprocess.retrofit.loginResponse.dbModels.CreditOpeningModel;
import com.hgil.siconprocess.utils.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohan.giri on 24-01-2017.
 */

public class CreditOpeningTable extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "Sicon_credit_opening";
    private static final String TABLE_NAME = "SD_CreditOpening_Master";

    private static final String ROUTE_ID = "Route_id";
    private static final String CUSTOMER_ID = "Customer_id";
    private static final String DDATE = "DDate";
    private static final String OPENING = "Opening";
    private Context mContext;

    public CreditOpeningTable(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + ROUTE_ID + " TEXT NULL, "
                + CUSTOMER_ID + " TEXT NULL, " + DDATE + " TEXT NULL, " + OPENING + " REAL NULL)");
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
    public boolean insertCreditOpening(CreditOpeningModel creditOpeningModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ROUTE_ID, creditOpeningModel.getRouteId());
        contentValues.put(CUSTOMER_ID, creditOpeningModel.getCustomerId());
        contentValues.put(DDATE, creditOpeningModel.getDDate());
        contentValues.put(OPENING, creditOpeningModel.getOpening());
        db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }

    // insert multiple
    public boolean insertCreditOpening(List<CreditOpeningModel> arrList) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < arrList.size(); i++) {
            CreditOpeningModel creditOpeningModel = arrList.get(i);
            ContentValues contentValues = new ContentValues();
            contentValues.put(ROUTE_ID, creditOpeningModel.getRouteId());
            contentValues.put(CUSTOMER_ID, creditOpeningModel.getCustomerId());
            contentValues.put(DDATE, creditOpeningModel.getDDate());
            contentValues.put(OPENING, creditOpeningModel.getOpening());
            db.insert(TABLE_NAME, null, contentValues);
        }
        db.close();
        return true;
    }

    public CreditOpeningModel getCreditOpeningByRoute(String route_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + ROUTE_ID + "='" + route_id + "'", null);

        CreditOpeningModel creditOpeningModel = new CreditOpeningModel();
        if (res.moveToFirst()) {
            creditOpeningModel.setRouteId(res.getString(res.getColumnIndex(ROUTE_ID)));
            creditOpeningModel.setCustomerId(res.getString(res.getColumnIndex(CUSTOMER_ID)));
            creditOpeningModel.setDDate(res.getString(res.getColumnIndex(DDATE)));
            creditOpeningModel.setOpening(res.getFloat(res.getColumnIndex(OPENING)));
        }
        res.close();
        db.close();
        return creditOpeningModel;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        db.close();
        return numRows;
    }

    /*public boolean updateUserRoleMap(UserRoleMapModel userRoleMapModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EMAIL, userRoleMapModel.getEmail());
        contentValues.put(ROLE_ID, userRoleMapModel.getRole_id());
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

    public ArrayList<CreditOpeningModel> getAllCreditOpening() {
        ArrayList<CreditOpeningModel> array_list = new ArrayList<CreditOpeningModel>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (res.moveToFirst()) {
            while (res.isAfterLast() == false) {
                CreditOpeningModel creditOpeningModel = new CreditOpeningModel();
                creditOpeningModel.setRouteId(res.getString(res.getColumnIndex(ROUTE_ID)));
                creditOpeningModel.setCustomerId(res.getString(res.getColumnIndex(CUSTOMER_ID)));
                creditOpeningModel.setDDate(res.getString(res.getColumnIndex(DDATE)));
                creditOpeningModel.setOpening(res.getFloat(res.getColumnIndex(OPENING)));

                array_list.add(creditOpeningModel);
                res.moveToNext();
            }
        }
        res.close();
        db.close();
        return array_list;
    }


    // customer credit balance
    public double custCreditAmount(String customer_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select " + OPENING + " from " + TABLE_NAME + " where " + CUSTOMER_ID + "=?";
        Cursor res = db.rawQuery(query, new String[]{customer_id});

        double amount = 0;
        if (res.moveToFirst()) {
            amount = res.getDouble(res.getColumnIndex(OPENING));
        }
        res.close();
        db.close();
        return Utility.roundTwoDecimals(amount);
    }
}