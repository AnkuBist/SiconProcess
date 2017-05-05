package com.hgil.siconprocess.database.masterTables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hgil.siconprocess.retrofit.loginResponse.dbModels.RejectionTargetModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohan.giri on 24-01-2017.
 */

public class RejectionTargetTable extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Sicon_rejection_target";
    private static final String TABLE_NAME = "SD_RejctionTarget_Master";

    private static final String REC_ID = "Rec_id";
    private static final String TARGET_FROM_DATE = "Target_From_Date";
    private static final String TARGET_TO_DATE = "Target_To_Date";
    private static final String DEPOT_ID = "Depot_ID";
    private static final String ROUTE_ID = "Route_ID";
    private static final String CUSTOMER_ID = "Customer_ID";
    private static final String PSM_ID = "PSM_ID";
    private static final String TARGET_TYPE = "Target_Type";
    private static final String DDATE = "DDate";
    private static final String ITEM_ID = "Item_id";
    private static final String TARGET_QTY = "Target_Qty";
    private static final String TARGET_REJ = "Target_Rej";
    private static final String TARGET_LEFTOVER = "Target_Leftover";
    private static final String REJ_FORMULA = "Rej_Formula";
    private static final String LEFTOVER_FORMULA = "Leftover_Formula";
    private static final String ACTIVE = "Active";
    private static final String UPDATEBY_PAYCODE = "updateby_paycode";
    private static final String UPDATEBY_DATE = "updateby_Date";
    private static final String UPDATED_IP = "updated_ip";
    private static final String ITEM_ACTIVE = "Item_Active";

    public RejectionTargetTable(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + REC_ID + " NUMERIC NOT NULL, "
                + TARGET_FROM_DATE + " TEXT NULL, " + TARGET_TO_DATE + " TEXT NULL, " + DEPOT_ID + " TEXT NULL, "
                + ROUTE_ID + " TEXT NULL, " + CUSTOMER_ID + " TEXT NULL, " + PSM_ID + " TEXT NULL, "
                + TARGET_TYPE + " TEXT NULL, " + DDATE + " TEXT NULL, " + ITEM_ID + " TEXT NULL, "
                + TARGET_QTY + " INTEGER NULL, " + TARGET_REJ + " REAL NULL, " + TARGET_LEFTOVER + " REAL NULL, "
                + REJ_FORMULA + " TEXT NULL, " + LEFTOVER_FORMULA + " TEXT NULL, " + ACTIVE + " TEXT NULL, "
                + UPDATEBY_PAYCODE + " TEXT NOT NULL, " + UPDATEBY_DATE + " TEXT NOT NULL, "
                + UPDATED_IP + " TEXT NOT NULL, " + ITEM_ACTIVE + " INTEGER NULL)");
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

    // insert multiple
    public boolean insertRejectionTarget(List<RejectionTargetModel> arrList) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < arrList.size(); i++) {
            RejectionTargetModel rejectionTargetModel = arrList.get(i);
            ContentValues contentValues = new ContentValues();
            contentValues.put(REC_ID, rejectionTargetModel.getRecId());
            contentValues.put(TARGET_FROM_DATE, rejectionTargetModel.getTargetFromDate());
            //contentValues.put(TARGET_TO_DATE, rejectionTargetModel.getTargetToDate());
            contentValues.put(DEPOT_ID, rejectionTargetModel.getDepotID());
            //contentValues.put(ROUTE_ID, rejectionTargetModel.getRouteID());
            //contentValues.put(CUSTOMER_ID, rejectionTargetModel.getCustomerID());
            //contentValues.put(PSM_ID, rejectionTargetModel.getPSMID());
            //contentValues.put(TARGET_TYPE, rejectionTargetModel.getTargetType());
            contentValues.put(DDATE, rejectionTargetModel.getDDate());
            contentValues.put(ITEM_ID, rejectionTargetModel.getItemId());
            contentValues.put(TARGET_QTY, rejectionTargetModel.getTargetQty());
            contentValues.put(TARGET_REJ, rejectionTargetModel.getTargetRej());
            contentValues.put(TARGET_LEFTOVER, rejectionTargetModel.getTargetLeftover());
            //contentValues.put(REJ_FORMULA, rejectionTargetModel.getRejFormula());
            //contentValues.put(LEFTOVER_FORMULA, rejectionTargetModel.getLeftoverFormula());
            contentValues.put(ACTIVE, rejectionTargetModel.getActive());
            contentValues.put(UPDATEBY_PAYCODE, rejectionTargetModel.getUpdatebyPaycode());
            contentValues.put(UPDATEBY_DATE, rejectionTargetModel.getUpdatebyDate());
            contentValues.put(UPDATED_IP, rejectionTargetModel.getUpdatedIp());
            contentValues.put(ITEM_ACTIVE, rejectionTargetModel.getItemActive());
            db.insert(TABLE_NAME, null, contentValues);
        }
        db.close();
        return true;
    }

    public RejectionTargetModel getRejectionTargetByDepot(String depot_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + DEPOT_ID + "='" + depot_id + "'", null);

        RejectionTargetModel rejectionTargetModel = new RejectionTargetModel();
        if (res.moveToFirst()) {
            rejectionTargetModel.setRecId(res.getLong(res.getColumnIndex(REC_ID)));
            rejectionTargetModel.setTargetFromDate(res.getString(res.getColumnIndex(TARGET_FROM_DATE)));
            //rejectionTargetModel.setTargetToDate(res.getString(res.getColumnIndex(TARGET_TO_DATE)));
            rejectionTargetModel.setDepotID(res.getString(res.getColumnIndex(DEPOT_ID)));
            //rejectionTargetModel.setRouteID(res.getString(res.getColumnIndex(ROUTE_ID)));
            //rejectionTargetModel.setCustomerID(res.getString(res.getColumnIndex(CUSTOMER_ID)));
            //rejectionTargetModel.setPSMID(res.getString(res.getColumnIndex(PSM_ID)));
            //rejectionTargetModel.setTargetType(res.getString(res.getColumnIndex(TARGET_TYPE)));
            rejectionTargetModel.setDDate(res.getString(res.getColumnIndex(DDATE)));
            rejectionTargetModel.setItemId(res.getString(res.getColumnIndex(ITEM_ID)));
            rejectionTargetModel.setTargetQty(res.getInt(res.getColumnIndex(TARGET_QTY)));
            rejectionTargetModel.setTargetRej(res.getFloat(res.getColumnIndex(TARGET_REJ)));
            rejectionTargetModel.setTargetLeftover(res.getFloat(res.getColumnIndex(TARGET_LEFTOVER)));
            //rejectionTargetModel.setRejFormula(res.getString(res.getColumnIndex(REJ_FORMULA)));
            //rejectionTargetModel.setLeftoverFormula(res.getString(res.getColumnIndex(LEFTOVER_FORMULA)));
            rejectionTargetModel.setActive(res.getString(res.getColumnIndex(ACTIVE)));
            rejectionTargetModel.setUpdatebyPaycode(res.getString(res.getColumnIndex(UPDATEBY_PAYCODE)));
            rejectionTargetModel.setUpdatebyDate(res.getString(res.getColumnIndex(UPDATEBY_DATE)));
            rejectionTargetModel.setUpdatedIp(res.getString(res.getColumnIndex(UPDATED_IP)));
            rejectionTargetModel.setItemActive(res.getInt(res.getColumnIndex(ITEM_ACTIVE)));
        }
        res.close();
        db.close();
        return rejectionTargetModel;
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

    public ArrayList<RejectionTargetModel> getAllRejectionTarget() {
        ArrayList<RejectionTargetModel> array_list = new ArrayList<RejectionTargetModel>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (res.moveToFirst()) {
            while (res.isAfterLast() == false) {
                RejectionTargetModel rejectionTargetModel = new RejectionTargetModel();
                rejectionTargetModel.setRecId(res.getLong(res.getColumnIndex(REC_ID)));
                rejectionTargetModel.setTargetFromDate(res.getString(res.getColumnIndex(TARGET_FROM_DATE)));
                //rejectionTargetModel.setTargetToDate(res.getString(res.getColumnIndex(TARGET_TO_DATE)));
                rejectionTargetModel.setDepotID(res.getString(res.getColumnIndex(DEPOT_ID)));
                //rejectionTargetModel.setRouteID(res.getString(res.getColumnIndex(ROUTE_ID)));
                //rejectionTargetModel.setCustomerID(res.getString(res.getColumnIndex(CUSTOMER_ID)));
                //rejectionTargetModel.setPSMID(res.getString(res.getColumnIndex(PSM_ID)));
                //rejectionTargetModel.setTargetType(res.getString(res.getColumnIndex(TARGET_TYPE)));
                rejectionTargetModel.setDDate(res.getString(res.getColumnIndex(DDATE)));
                rejectionTargetModel.setItemId(res.getString(res.getColumnIndex(ITEM_ID)));
                rejectionTargetModel.setTargetQty(res.getInt(res.getColumnIndex(TARGET_QTY)));
                rejectionTargetModel.setTargetRej(res.getFloat(res.getColumnIndex(TARGET_REJ)));
                rejectionTargetModel.setTargetLeftover(res.getFloat(res.getColumnIndex(TARGET_LEFTOVER)));
                //rejectionTargetModel.setRejFormula(res.getString(res.getColumnIndex(REJ_FORMULA)));
                //rejectionTargetModel.setLeftoverFormula(res.getString(res.getColumnIndex(LEFTOVER_FORMULA)));
                rejectionTargetModel.setActive(res.getString(res.getColumnIndex(ACTIVE)));
                rejectionTargetModel.setUpdatebyPaycode(res.getString(res.getColumnIndex(UPDATEBY_PAYCODE)));
                rejectionTargetModel.setUpdatebyDate(res.getString(res.getColumnIndex(UPDATEBY_DATE)));
                rejectionTargetModel.setUpdatedIp(res.getString(res.getColumnIndex(UPDATED_IP)));
                rejectionTargetModel.setItemActive(res.getInt(res.getColumnIndex(ITEM_ACTIVE)));

                array_list.add(rejectionTargetModel);
                res.moveToNext();
            }
        }
        res.close();
        db.close();
        return array_list;
    }

}