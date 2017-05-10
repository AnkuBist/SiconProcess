package com.hgil.siconprocess.database.masterTables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hgil.siconprocess.retrofit.loginResponse.dbModels.CustomerItemPriceModel;

import java.util.List;

/**
 * Created by mohan.giri on 07-02-2017.
 */

public class CustomerItemPriceTable extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "Sicon_item_price_db";
    private static final String TABLE_NAME = "customer_item_price_table";

    private static final String ITEM_ID = "Item_Id";
    private static final String CUSTOMER_ID = "Customer_id";
    private static final String ITEM_PRICE = "item_price";
    private static final String DISCOUNT_PRICE = "discount_price";
    private static final String DISCOUNT_PERCENTAGE = "discount_percentage";
    private static final String DISCOUNT_TYPE = "discountType";
    private static final String DISCOUNTED_PRICE = "discounted_prc";

    private Context mContext;

    public CustomerItemPriceTable(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + ITEM_ID + " TEXT NOT NULL, "
                + CUSTOMER_ID + " TEXT NOT NULL, " + ITEM_PRICE + " REAL NOT NULL, "
                + DISCOUNT_PRICE + " REAL NOT NULL, " + DISCOUNT_PERCENTAGE + " REAL NOT NULL, "
                + DISCOUNT_TYPE + " TEXT NOT NULL, "
                + DISCOUNTED_PRICE + " REAL NOT NULL)");
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
    public boolean insertCustomerItemPrice(CustomerItemPriceModel customerItemPriceModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEM_ID, customerItemPriceModel.getItemId());
        contentValues.put(CUSTOMER_ID, customerItemPriceModel.getCustomerId());
        contentValues.put(ITEM_PRICE, customerItemPriceModel.getItemPrice());
        contentValues.put(DISCOUNT_PRICE, customerItemPriceModel.getDiscountPrice());
        contentValues.put(DISCOUNT_PERCENTAGE, customerItemPriceModel.getDiscountPercentage());
        contentValues.put(DISCOUNT_TYPE, customerItemPriceModel.getDiscountType());
        contentValues.put(DISCOUNTED_PRICE, customerItemPriceModel.getDiscountedPrice());

        db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }

    // insert multiple items price
    public boolean insertCustomerItemPrice(List<CustomerItemPriceModel> arrCustomerItemPrice) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < arrCustomerItemPrice.size(); i++) {
            CustomerItemPriceModel customerItemPriceModel = arrCustomerItemPrice.get(i);
            ContentValues contentValues = new ContentValues();
            contentValues.put(ITEM_ID, customerItemPriceModel.getItemId());
            contentValues.put(CUSTOMER_ID, customerItemPriceModel.getCustomerId());
            contentValues.put(ITEM_PRICE, customerItemPriceModel.getItemPrice());
            contentValues.put(DISCOUNT_PRICE, customerItemPriceModel.getDiscountPrice());
            contentValues.put(DISCOUNT_PERCENTAGE, customerItemPriceModel.getDiscountPercentage());
            contentValues.put(DISCOUNT_TYPE, customerItemPriceModel.getDiscountType());
            contentValues.put(DISCOUNTED_PRICE, customerItemPriceModel.getDiscountedPrice());
            db.insert(TABLE_NAME, null, contentValues);
        }
        db.close();
        return true;
    }

    public double getItemPriceById(String item_id, String customer_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT " + DISCOUNTED_PRICE + " FROM " + TABLE_NAME + " WHERE " + ITEM_ID + "=? and " + CUSTOMER_ID + "=?", new String[]{item_id, customer_id});

        double price = 0;
        if (res.moveToFirst()) {
            price = res.getDouble(res.getColumnIndex(DISCOUNTED_PRICE));
        }
        res.close();
        db.close();
        return price;
    }

    // get required details for the product for a customer on route
    public CustomerItemPriceModel getItemPriceDiscById(String customer_id, String item_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + CUSTOMER_ID + "=? AND " + ITEM_ID + "=?", new String[]{customer_id, item_id});

        CustomerItemPriceModel itemPriceModel = new CustomerItemPriceModel();
        if (res.moveToFirst()) {
            itemPriceModel.setItemPrice(res.getDouble(res.getColumnIndex(ITEM_PRICE)));
            itemPriceModel.setDiscountPrice(res.getDouble(res.getColumnIndex(DISCOUNT_PRICE)));
            itemPriceModel.setDiscountPercentage(res.getDouble(res.getColumnIndex(DISCOUNT_PERCENTAGE)));
            itemPriceModel.setDiscountType(res.getString(res.getColumnIndex(DISCOUNT_TYPE)));
            itemPriceModel.setDiscountedPrice(res.getDouble(res.getColumnIndex(DISCOUNTED_PRICE)));
        }
        res.close();
        db.close();
        return itemPriceModel;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        db.close();
        return numRows;
    }

}