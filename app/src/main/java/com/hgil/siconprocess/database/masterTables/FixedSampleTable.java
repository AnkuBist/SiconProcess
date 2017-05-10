package com.hgil.siconprocess.database.masterTables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hgil.siconprocess.retrofit.loginResponse.dbModels.FixedSampleModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohan.giri on 24-01-2017.
 */

public class FixedSampleTable extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "Sicon_fixed_sample";
    private static final String TABLE_NAME = "SD_FixedSample_Master";

    private static final String ROUTE = "Route";
    private static final String CUSTOMER_ID = "Customer_id";
    private static final String ITEM_ID = "Item_id";
    private static final String SQTY = "SQty";

    public FixedSampleTable(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + ROUTE + " TEXT NOT NULL, " + CUSTOMER_ID + " TEXT NOT NULL, " + ITEM_ID + " TEXT NOT NULL, "
                + SQTY + " INTEGER NOT NULL)");
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
    public boolean insertFixedSample(List<FixedSampleModel> arrList) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < arrList.size(); i++) {
            FixedSampleModel fixedSampleModel = arrList.get(i);
            ContentValues contentValues = new ContentValues();
            contentValues.put(ROUTE, fixedSampleModel.getRoute());
            contentValues.put(CUSTOMER_ID, fixedSampleModel.getCustomerId());
            contentValues.put(ITEM_ID, fixedSampleModel.getItemId());
            contentValues.put(SQTY, fixedSampleModel.getSQty());
            db.insert(TABLE_NAME, null, contentValues);
        }
        db.close();
        return true;
    }

    public FixedSampleModel getFixedSampleItem(String item_id, String customer_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + ITEM_ID + "=? and " + CUSTOMER_ID + "=?", new String[]{item_id, customer_id});

        FixedSampleModel fixedSampleModel = new FixedSampleModel();
        if (res.moveToFirst()) {
            fixedSampleModel.setRoute(res.getString(res.getColumnIndex(ROUTE)));
            fixedSampleModel.setCustomerId(res.getString(res.getColumnIndex(CUSTOMER_ID)));
            fixedSampleModel.setItemId(res.getString(res.getColumnIndex(ITEM_ID)));
            fixedSampleModel.setSQty(res.getInt(res.getColumnIndex(SQTY)));
        }
        res.close();
        db.close();
        return fixedSampleModel;
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

    public ArrayList<FixedSampleModel> getAllFixedSample() {
        ArrayList<FixedSampleModel> array_list = new ArrayList<FixedSampleModel>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (res.moveToFirst()) {
            while (res.isAfterLast() == false) {
                FixedSampleModel fixedSampleModel = new FixedSampleModel();
                fixedSampleModel.setRoute(res.getString(res.getColumnIndex(ROUTE)));
                fixedSampleModel.setCustomerId(res.getString(res.getColumnIndex(CUSTOMER_ID)));
                fixedSampleModel.setItemId(res.getString(res.getColumnIndex(ITEM_ID)));
                fixedSampleModel.setSQty(res.getInt(res.getColumnIndex(SQTY)));

                array_list.add(fixedSampleModel);
                res.moveToNext();
            }
        }
        res.close();
        db.close();
        return array_list;
    }

    // get sample count
    public int getSampleCount(String item_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        /*String query = "select distinct " + ITEM_ID + ", " + ITEM_RATE + ", sum(" + INVQTY_PS + ") over (partition by " + ITEM_ID + ") as loading_qty " +
                "from " + TABLE_NAME + " where " + ITEM_ID + "=?";*/
        String query = "select distinct " + ITEM_ID + ", sum(" + SQTY + ") as loading_sample_qty " +
                "from " + TABLE_NAME + " where " + ITEM_ID + "=? group by " + ITEM_ID;
        Cursor res = db.rawQuery(query, new String[]{item_id});

        int total_item_count = 0;
        if (res.moveToFirst()) {
            total_item_count = res.getInt(res.getColumnIndex("loading_sample_qty"));
        }
        res.close();
        db.close();
        return total_item_count;

    }


}