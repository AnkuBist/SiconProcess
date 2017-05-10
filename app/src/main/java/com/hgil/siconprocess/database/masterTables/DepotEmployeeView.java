package com.hgil.siconprocess.database.masterTables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hgil.siconprocess.retrofit.loginResponse.dbModels.EmployeeModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohan.giri on 24-01-2017.
 */

public class DepotEmployeeView extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "Sicon_depot_employee";
    private static final String TABLE_NAME = "V_Depot_Employee_Master";

    private static final String PAYCODE = "Paycode";
    private static final String NAME = "Name";
    private static final String ETYPE = "Etype";
    private static final String DESIGNATION = "Designation";
    private static final String SUB_COMPANY_ID = "Sub_Company_id";
    private static final String DEPOT_ID = "Depot_id";
    private static final String NEWPAYCODE = "NewPaycode";
    private static final String FLAG = "Flag";

    public DepotEmployeeView(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + PAYCODE + " TEXT NOT NULL, "
                + NAME + " TEXT NULL NULL, " + ETYPE + " TEXT NULL, " + DESIGNATION + " TEXT NOT NULL, "
                + SUB_COMPANY_ID + " TEXT NULL, " + DEPOT_ID + " TEXT NOT NULL, " + NEWPAYCODE + " TEXT NOT NULL, "
                + FLAG + " TEXT NOT NULL)");
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
    public boolean insertDepotEmployee(EmployeeModel employeeModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PAYCODE, employeeModel.getPaycode());
        contentValues.put(NAME, employeeModel.getName());
        contentValues.put(ETYPE, employeeModel.getEtype());
        contentValues.put(DESIGNATION, employeeModel.getDesignation());
        contentValues.put(SUB_COMPANY_ID, employeeModel.getSubCompanyId());
        contentValues.put(DEPOT_ID, employeeModel.getDepotId());
        contentValues.put(NEWPAYCODE, employeeModel.getNewPaycode());
        contentValues.put(FLAG, employeeModel.getFlag());
        db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }

    // insert multiple
    public boolean insertDepotEmployee(List<EmployeeModel> arrList) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < arrList.size(); i++) {
            EmployeeModel employeeModel = arrList.get(i);
            ContentValues contentValues = new ContentValues();
            contentValues.put(PAYCODE, employeeModel.getPaycode());
            contentValues.put(NAME, employeeModel.getName());
            contentValues.put(ETYPE, employeeModel.getEtype());
            contentValues.put(DESIGNATION, employeeModel.getDesignation());
            contentValues.put(SUB_COMPANY_ID, employeeModel.getSubCompanyId());
            contentValues.put(DEPOT_ID, employeeModel.getDepotId());
            contentValues.put(NEWPAYCODE, employeeModel.getNewPaycode());
            contentValues.put(FLAG, employeeModel.getFlag());
            db.insert(TABLE_NAME, null, contentValues);
        }
        db.close();
        return true;
    }

    public EmployeeModel getDepotEmployeeByPaycode(String paycode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + PAYCODE + "='" + paycode + "'", null);

        EmployeeModel employeeModel = new EmployeeModel();
        if (res.moveToFirst()) {
            employeeModel.setPaycode(res.getString(res.getColumnIndex(PAYCODE)));
            employeeModel.setName(res.getString(res.getColumnIndex(NAME)));
            employeeModel.setEtype(res.getString(res.getColumnIndex(ETYPE)));
            employeeModel.setDesignation(res.getString(res.getColumnIndex(DESIGNATION)));
            employeeModel.setSubCompanyId(res.getString(res.getColumnIndex(SUB_COMPANY_ID)));
            employeeModel.setDepotId(res.getString(res.getColumnIndex(DEPOT_ID)));
            employeeModel.setNewPaycode(res.getString(res.getColumnIndex(NEWPAYCODE)));
            employeeModel.setFlag(res.getString(res.getColumnIndex(FLAG)));
        }
        res.close();
        db.close();
        return employeeModel;
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

    public ArrayList<EmployeeModel> getAllDepotEmployee() {
        ArrayList<EmployeeModel> array_list = new ArrayList<EmployeeModel>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (res.moveToFirst()) {
            while (res.isAfterLast() == false) {
                EmployeeModel employeeModel = new EmployeeModel();
                employeeModel.setPaycode(res.getString(res.getColumnIndex(PAYCODE)));
                employeeModel.setName(res.getString(res.getColumnIndex(NAME)));
                employeeModel.setEtype(res.getString(res.getColumnIndex(ETYPE)));
                employeeModel.setDesignation(res.getString(res.getColumnIndex(DESIGNATION)));
                employeeModel.setSubCompanyId(res.getString(res.getColumnIndex(SUB_COMPANY_ID)));
                employeeModel.setDepotId(res.getString(res.getColumnIndex(DEPOT_ID)));
                employeeModel.setNewPaycode(res.getString(res.getColumnIndex(NEWPAYCODE)));
                employeeModel.setFlag(res.getString(res.getColumnIndex(FLAG)));

                array_list.add(employeeModel);
                res.moveToNext();
            }
        }
        res.close();
        db.close();
        return array_list;
    }
}