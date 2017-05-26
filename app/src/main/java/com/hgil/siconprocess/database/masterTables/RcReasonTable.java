package com.hgil.siconprocess.database.masterTables;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hgil.siconprocess.retrofit.loginResponse.dbModels.RcReason;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohan.giri on 26-05-2017.
 */

public class RcReasonTable extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "Sicon_Route_Close";
    private static final String TABLE_NAME = "V_SD_Route_Close_Master";

    private static final String REC_ID = "Rec_Id";
    private static final String REASON = "reason";

    private Context mContext;

    public RcReasonTable(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + REC_ID + " INTEGER NULL, "
                + REASON + " TEXT NULL)");
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
    public boolean insertReason(List<RcReason> arrRcReason) {
        SQLiteDatabase db = this.getWritableDatabase();
        DatabaseUtils.InsertHelper ih = new DatabaseUtils.InsertHelper(db, TABLE_NAME);

        // Get the numeric indexes for each of the columns that we're updating
        final int recIdColumn = ih.getColumnIndex(REC_ID);
        final int reasonColumn = ih.getColumnIndex(REASON);

        try {
            db.beginTransaction();
            for (RcReason rcReason : arrRcReason) {
                ih.prepareForInsert();
                ih.bind(recIdColumn, rcReason.getReasonId());
                ih.bind(reasonColumn, rcReason.getReason());
                ih.execute();
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        db.close();
        return true;
    }

    public RcReason getReason(String reason_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + REC_ID + "=?", new String[]{reason_id});

        RcReason rcReason = new RcReason();
        if (res.moveToFirst()) {
            rcReason.setReasonId(res.getInt(res.getColumnIndex(REC_ID)));
            rcReason.setReason(res.getString(res.getColumnIndex(REASON)));
        }
        res.close();
        db.close();
        return rcReason;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        db.close();
        return numRows;
    }

    public RcReason getRoute() {
        RcReason rcReason = new RcReason();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (res.moveToFirst()) {
            rcReason.setReasonId(res.getInt(res.getColumnIndex(REC_ID)));
            rcReason.setReason(res.getString(res.getColumnIndex(REASON)));
        }
        res.close();
        db.close();
        return rcReason;
    }

    public ArrayList<RcReason> getAllReasons(String customer_id) {
        ArrayList<RcReason> array_list = new ArrayList<RcReason>();

        RcReason custReason = new CustomerRouteMappingView(mContext).custNoOrderReason(customer_id);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (res.moveToFirst()) {
            while (res.isAfterLast() == false) {
                RcReason rcReason = new RcReason();
                int recId = res.getInt(res.getColumnIndex(REC_ID));
                rcReason.setReasonId(res.getInt(res.getColumnIndex(REC_ID)));
                rcReason.setReason(res.getString(res.getColumnIndex(REASON)));
                if (recId == custReason.getReasonId())
                    rcReason.setStatus(true);
                else
                    rcReason.setStatus(false);

                array_list.add(rcReason);
                res.moveToNext();
            }
        }
        res.close();
        db.close();
        return array_list;
    }
}
