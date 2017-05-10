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
    public static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "Sicon_rejection_target";
    private static final String TABLE_NAME = "SD_RejctionTarget_Master";

    private static final String ITEM_ID = "Item_id";
    private static final String TARGET_REJ = "Target_Rej";

    public RejectionTargetTable(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + ITEM_ID + " TEXT NULL, "
                + TARGET_REJ + " REAL NULL)");
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
            contentValues.put(ITEM_ID, rejectionTargetModel.getItemId());
            contentValues.put(TARGET_REJ, rejectionTargetModel.getTargetRej());
            db.insert(TABLE_NAME, null, contentValues);
        }
        db.close();
        return true;
    }

    public RejectionTargetModel getRejectionTargetByDepot() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        RejectionTargetModel rejectionTargetModel = new RejectionTargetModel();
        if (res.moveToFirst()) {
            rejectionTargetModel.setItemId(res.getString(res.getColumnIndex(ITEM_ID)));
            rejectionTargetModel.setTargetRej(res.getFloat(res.getColumnIndex(TARGET_REJ)));
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
                rejectionTargetModel.setItemId(res.getString(res.getColumnIndex(ITEM_ID)));
                rejectionTargetModel.setTargetRej(res.getFloat(res.getColumnIndex(TARGET_REJ)));

                array_list.add(rejectionTargetModel);
                res.moveToNext();
            }
        }
        res.close();
        db.close();
        return array_list;
    }

}