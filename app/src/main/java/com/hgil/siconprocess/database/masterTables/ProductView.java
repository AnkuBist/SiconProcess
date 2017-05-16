package com.hgil.siconprocess.database.masterTables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hgil.siconprocess.adapter.productSelection.ProductSelectModel;
import com.hgil.siconprocess.adapter.vanStock.VanStockModel;
import com.hgil.siconprocess.database.tables.CustomerRejectionTable;
import com.hgil.siconprocess.database.tables.InvoiceOutTable;
import com.hgil.siconprocess.retrofit.loginResponse.dbModels.ProductModel;
import com.hgil.siconprocess.utils.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohan.giri on 24-01-2017.
 */

public class ProductView extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;

    private static final String DATABASE_NAME = "Sicon_product";
    private static final String TABLE_NAME = "V_Item_Master";

    private static final String ITEMSEQUENCE = "ITEMSEQUENCE";
    private static final String ITEM_ID = "Item_id";
    private static final String ITEM_NAME = "Item_Name";
    private static final String TARGET_REJ = "Target_Rej";
    private static final String ITEMGROUPID = "ITEMGROUPID";

    private Context mContext;

    public ProductView(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + ITEMSEQUENCE + " INTEGER NOT NULL, "
                + ITEM_ID + " TEXT NULL, " + ITEM_NAME + " TEXT NULL, " + TARGET_REJ + " REAL NULL, "
                + ITEMGROUPID + " TEXT NULL)");
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

    //insert multiple rows
    public boolean insertProducts(List<ProductModel> arrayList) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (ProductModel productModel : arrayList) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ITEMSEQUENCE, productModel.getITEMSEQUENCE());
            contentValues.put(ITEM_ID, productModel.getItemId());
            contentValues.put(ITEM_NAME, productModel.getItemName());
            contentValues.put(TARGET_REJ, productModel.getTargetRej());
            contentValues.put(ITEMGROUPID, productModel.getITEMGROUPID());
            db.insert(TABLE_NAME, null, contentValues);
        }
        db.close();
        return true;
    }

    public ProductModel getProductById(String item_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + ITEM_ID + "=?", new String[]{item_id});

        ProductModel productModel = new ProductModel();
        if (res.moveToFirst()) {
            productModel.setITEMSEQUENCE(res.getInt(res.getColumnIndex(ITEMSEQUENCE)));
            productModel.setItemId(res.getString(res.getColumnIndex(ITEM_ID)));
            productModel.setItemName(res.getString(res.getColumnIndex(ITEM_NAME)));
            productModel.setITEMGROUPID(res.getString(res.getColumnIndex(ITEMGROUPID)));
        }
        res.close();
        db.close();
        return productModel;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        db.close();
        return numRows;
    }

    /*check product exists or not*/
    public boolean checkProduct(String item_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {ITEM_NAME};
        String selection = ITEM_ID + " =?";
        String[] selectionArgs = {item_id};
        String limit = "1";

        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null, limit);
        boolean exists = (cursor.getCount() > 0);

        return exists;
    }

    // get all products loaded in van
    public ArrayList<ProductModel> getAllProducts() {
        ArrayList<ProductModel> array_list = new ArrayList<ProductModel>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (res.moveToFirst()) {
            while (res.isAfterLast() == false) {
                ProductModel productModel = new ProductModel();
                productModel.setITEMSEQUENCE(res.getInt(res.getColumnIndex(ITEMSEQUENCE)));
                productModel.setItemId(res.getString(res.getColumnIndex(ITEM_ID)));
                productModel.setItemName(res.getString(res.getColumnIndex(ITEM_NAME)));
                productModel.setITEMGROUPID(res.getString(res.getColumnIndex(ITEMGROUPID)));

                array_list.add(productModel);
                res.moveToNext();
            }
        }
        res.close();
        db.close();
        return array_list;
    }

    // get all products loaded in van for customer to select for rejection....this has to be filtered with user rejection list if exists
    public ArrayList<ProductSelectModel> getAvailableProducts(String customer_id, ArrayList<String> alreadyExists) {
        ArrayList<ProductSelectModel> array_list = new ArrayList<ProductSelectModel>();

        SQLiteDatabase db = this.getReadableDatabase();
        CustomerItemPriceTable dbPriceTable = new CustomerItemPriceTable(mContext);

        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + ITEMSEQUENCE + " ASC", null);
        if (res.moveToFirst()) {
            while (res.isAfterLast() == false) {
                ProductSelectModel pSelectModel = new ProductSelectModel();
                pSelectModel.setItem_id(res.getString(res.getColumnIndex(ITEM_ID)));
                pSelectModel.setItem_name(res.getString(res.getColumnIndex(ITEM_NAME)));
                pSelectModel.setItem_price(dbPriceTable.getItemPriceById(pSelectModel.getItem_id(), customer_id));

                if (alreadyExists.contains(pSelectModel.getItem_id())) {
                    // do not add this product
                } else {
                    array_list.add(pSelectModel);
                }
                res.moveToNext();
            }
        }
        res.close();
        db.close();
        return array_list;
    }

    /*get product data for van stock*/
    public ArrayList<VanStockModel> getVanStock() {
        ArrayList<VanStockModel> array_list = new ArrayList<VanStockModel>();

        SQLiteDatabase db = this.getReadableDatabase();

        DepotInvoiceView depotInvoiceView = new DepotInvoiceView(mContext);
        CustomerItemPriceTable itemPriceTable = new CustomerItemPriceTable(mContext);
        InvoiceOutTable invoiceOutTable = new InvoiceOutTable(mContext);
        CustomerRejectionTable customerRejTable = new CustomerRejectionTable(mContext);

        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + ITEMSEQUENCE + " ASC", null);
        if (res.moveToFirst()) {
            while (res.isAfterLast() == false) {
                VanStockModel vanStockModel = new VanStockModel();
                String item_id = res.getString(res.getColumnIndex(ITEM_ID));
                vanStockModel.setItem_id(item_id);
                vanStockModel.setItem_name(res.getString(res.getColumnIndex(ITEM_NAME)));

                int loadingQty = depotInvoiceView.getLoadingCount(item_id);
                if (loadingQty > 0) {
                    int saleQty = invoiceOutTable.getItemOrderQty(item_id);
                    int sampleQty = itemPriceTable.getSampleCount(item_id);

                    // get product total stock in van
                    vanStockModel.setLoadQty(loadingQty);
                    // total product quantity sold
                    vanStockModel.setGross_sale(saleQty);
                    // van sample loads
                    vanStockModel.setSample(sampleQty);

                    // get product market and fresh rejection total
                    int marketRejection = customerRejTable.productMarketRejection(item_id);
                    int freshRejection = customerRejTable.productFreshRejection(item_id);

                    vanStockModel.setMarket_rejection(marketRejection);
                    vanStockModel.setFresh_rejection(freshRejection);

                    int leftOver = loadingQty - saleQty - sampleQty;
                    vanStockModel.setLeft_over(leftOver);

                    array_list.add(vanStockModel);
                }
                res.moveToNext();
            }
        }

        res.close();
        db.close();
        return array_list;
    }

    /*get item name using item id*/
    // get customer invoice total
    public String productName(String item_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select " + ITEM_NAME + " from " + TABLE_NAME + " where " + ITEM_ID + "=?";
        Cursor res = db.rawQuery(query, new String[]{item_id});

        String item_name = null;
        if (res.moveToFirst()) {
            item_name = res.getString(res.getColumnIndex(ITEM_NAME));
        }
        res.close();
        db.close();
        return Utility.getString(item_name);
    }


}