package com.hgil.siconprocess.database.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hgil.siconprocess.activity.fragments.dashboard.DaySummaryAmountCollectionModel;
import com.hgil.siconprocess.activity.fragments.invoiceSyncModel.CashCheck;
import com.hgil.siconprocess.activity.fragments.invoiceSyncModel.CollectionCashModel;
import com.hgil.siconprocess.activity.fragments.invoiceSyncModel.CollectionChequeModel;
import com.hgil.siconprocess.activity.fragments.invoiceSyncModel.CollectionCrateModel;
import com.hgil.siconprocess.activity.fragments.invoiceSyncModel.CrateStockCheck;
import com.hgil.siconprocess.database.dbModels.ChequeDetailsModel;
import com.hgil.siconprocess.database.dbModels.CrateDetailModel;
import com.hgil.siconprocess.database.dbModels.PaymentModel;
import com.hgil.siconprocess.database.dbModels.UpiPaymentModel;
import com.hgil.siconprocess.database.masterTables.CustomerRouteMappingView;
import com.hgil.siconprocess.utils.Constant;
import com.hgil.siconprocess.utils.Utility;

import java.util.ArrayList;

/**
 * Created by mohan.giri on 08-02-2017.
 */

public class PaymentTable extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "payment_db";
    private static final String TABLE_NAME = "payment_table";

    private static final String CUSTOMER_ID = "customer_id";
    private static final String CUSTOMER_NAME = "customer_name";
    private static final String SALE_AMOUNT = "sale_amount";
    private static final String CASH_PAID = "cash_paid";
    private static final String TOTAL_PAID_AMOUNT = "total_paid_amount";

    // upi payment details
    private static final String UPI_REFERENCE_ID = "upi_payment_id";
    private static final String UPI_AMOUNT = "upi_amount";

    // cheque details
    private static final String CHEQUE_NUMBER = "chequeNumber";
    private static final String CHEQUE_DATE = "chequeDate";
    private static final String CHEQUE_AMOUNT = "chequeAmount";
    private static final String BANK_NAME = "bankName";
    private static final String BANK_BRANCH = "bankBranch";
    private static final String INVOICE_ID = "invoiceId";

    // crate details
    private static final String ISSUED_CRATES = "issuedCrates";
    private static final String RECEIVED_CRATES = "receivedCrates";

    private static final String IMEI_NO = "imei_no";
    private static final String LAT_LNG = "lat_lng";
    private static final String CURTIME = "cur_time";
    private static final String LOGIN_ID = "login_id";
    private static final String DATE = "date";

    private static final String INV_STATUS = "inv_status";

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + CUSTOMER_ID + " TEXT NOT NULL, "
            + CUSTOMER_NAME + " TEXT NOT NULL, " + SALE_AMOUNT + " REAL NULL, "
            + CASH_PAID + " REAL NULL, " + TOTAL_PAID_AMOUNT + " REAL NULL, "
            + UPI_REFERENCE_ID + " TEXT NULL, " + UPI_AMOUNT + " REAL NULL, "
            + CHEQUE_NUMBER + " TEXT NULL, " + CHEQUE_DATE + " TEXT NULL, " + CHEQUE_AMOUNT + " REAL NULL, "
            + BANK_NAME + " TEXT NULL, " + BANK_BRANCH + " TEXT NULL, " + INVOICE_ID + " TEXT NULL, "
            + ISSUED_CRATES + " INTEGER NULL, " + RECEIVED_CRATES + " INTEGER NULL, "
            + IMEI_NO + " TEXT NULL, " + LAT_LNG + " TEXT NULL, " + CURTIME + " TEXT NULL, " + LOGIN_ID + " TEXT NULL, "
            + DATE + " DATE NULL, " + INV_STATUS + " TEXT NULL)";
    private Context mContext;

    public PaymentTable(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void eraseTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.close();
    }

    /*update customer payment status*/
    public void updateCustPaymentStatus(String customer_id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(INV_STATUS, status);
        db.update(TABLE_NAME, contentValues, CUSTOMER_ID + "=?", new String[]{customer_id});
        db.close();
    }

    // insert customer payment information
    public void insertCustPayment(PaymentModel paymentModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CUSTOMER_ID, paymentModel.getCustomerId());
        contentValues.put(CUSTOMER_NAME, paymentModel.getCustomerName());
        contentValues.put(SALE_AMOUNT, paymentModel.getSaleAmount());
        contentValues.put(CASH_PAID, paymentModel.getCashPaid());
        contentValues.put(TOTAL_PAID_AMOUNT, paymentModel.getTotalPaidAmount());

        UpiPaymentModel upiModel = paymentModel.getUpiDetail();
        if (upiModel != null) {
            contentValues.put(UPI_REFERENCE_ID, upiModel.getPaymentReferenceId());
            contentValues.put(UPI_AMOUNT, upiModel.getPaidAmount());
        }

        ChequeDetailsModel chequeDetailsModel = paymentModel.getChequeDetail();

        if (chequeDetailsModel != null && paymentModel.getCashPaid() != paymentModel.getTotalPaidAmount()) {
            contentValues.put(CHEQUE_NUMBER, chequeDetailsModel.getChequeNumber());
            contentValues.put(CHEQUE_DATE, chequeDetailsModel.getChequeDate());
            contentValues.put(CHEQUE_AMOUNT, chequeDetailsModel.getChequeAmount());
            contentValues.put(BANK_NAME, chequeDetailsModel.getBankName());
            contentValues.put(BANK_BRANCH, chequeDetailsModel.getBankBranch());
            contentValues.put(INVOICE_ID, chequeDetailsModel.getInvoiceId());
        }

        contentValues.put(IMEI_NO, paymentModel.getImei_no());
        contentValues.put(LAT_LNG, paymentModel.getLat_lng());
        contentValues.put(CURTIME, Utility.timeStamp());
        contentValues.put(LOGIN_ID, paymentModel.getLogin_id());

        contentValues.put(DATE, Utility.getCurDate());

        // check if row exists for the same user or not
        // if yes then update the same or simply insert data
        if (Exists(db, paymentModel.getCustomerId()))
            db.update(TABLE_NAME, contentValues, CUSTOMER_ID + "=?", new String[]{paymentModel.getCustomerId()});
        else
            db.insert(TABLE_NAME, null, contentValues);
        db.close();
    }

    public void insertCustomerCrates(CrateDetailModel crateDetailModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CUSTOMER_ID, crateDetailModel.getCustomer_id());
        contentValues.put(ISSUED_CRATES, crateDetailModel.getIssuedCrates());
        contentValues.put(RECEIVED_CRATES, crateDetailModel.getReceivedCrates());
        contentValues.put(IMEI_NO, crateDetailModel.getImei_no());
        contentValues.put(LAT_LNG, crateDetailModel.getLat_lng());
        contentValues.put(CURTIME, Utility.timeStamp());
        contentValues.put(LOGIN_ID, crateDetailModel.getLogin_id());
        contentValues.put(DATE, Utility.getCurDate());

        //db.insert(TABLE_NAME, null, contentValues);
        db.update(TABLE_NAME, contentValues, CUSTOMER_ID + "=?", new String[]{crateDetailModel.getCustomer_id()});
        db.close();
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME, DATE + "<?", new String[]{Utility.getCurDate()});
        db.close();
        return numRows;
    }

    // erase customer rejections
    public void eraseCustPayment(SQLiteDatabase db, String customer_id) {
        db.delete(TABLE_NAME, CUSTOMER_ID + "=?", new String[]{customer_id});
    }

    //here need to implement the update process for the existing user.
    public boolean Exists(SQLiteDatabase db, String customer_id) {

        String[] columns = {CUSTOMER_ID};
        String selection = CUSTOMER_ID + " =?";
        String[] selectionArgs = {customer_id};
        String limit = "1";

        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null, limit);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    // get user all rejections by date
    // till now user has not restriction to make rejections.
    public PaymentModel getCustomerPaymentInfo(String customer_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        PaymentModel paymentModel = new PaymentModel();

        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + CUSTOMER_ID + "='" + customer_id + "'", null);
        if (res.moveToFirst()) {
            paymentModel.setCustomerId(res.getString(res.getColumnIndex(CUSTOMER_ID)));
            paymentModel.setCustomerName(res.getString(res.getColumnIndex(CUSTOMER_NAME)));
            paymentModel.setSaleAmount(res.getDouble(res.getColumnIndex(SALE_AMOUNT)));
            paymentModel.setCashPaid(res.getDouble(res.getColumnIndex(CASH_PAID)));
            paymentModel.setTotalPaidAmount(res.getDouble(res.getColumnIndex(TOTAL_PAID_AMOUNT)));
            paymentModel.setImei_no(res.getString(res.getColumnIndex(IMEI_NO)));
            paymentModel.setLat_lng(res.getString(res.getColumnIndex(LAT_LNG)));
            paymentModel.setTime_stamp(res.getString(res.getColumnIndex(CURTIME)));
            paymentModel.setLogin_id(res.getString(res.getColumnIndex(LOGIN_ID)));

            // upi payment details
            UpiPaymentModel upiModel = new UpiPaymentModel();
            upiModel.setPaymentReferenceId(res.getString(res.getColumnIndex(UPI_REFERENCE_ID)));
            upiModel.setPaidAmount(res.getDouble(res.getColumnIndex(UPI_AMOUNT)));
            paymentModel.setUpiDetail(upiModel);

            // cheque details
            ChequeDetailsModel chequeDetailsModel = new ChequeDetailsModel();
            chequeDetailsModel.setChequeNumber(res.getString(res.getColumnIndex(CHEQUE_NUMBER)));
            chequeDetailsModel.setChequeDate(res.getString(res.getColumnIndex(CHEQUE_DATE)));
            chequeDetailsModel.setChequeAmount(res.getDouble(res.getColumnIndex(CHEQUE_AMOUNT)));
            chequeDetailsModel.setBankName(res.getString(res.getColumnIndex(BANK_NAME)));
            chequeDetailsModel.setBankBranch(res.getString(res.getColumnIndex(BANK_BRANCH)));
            chequeDetailsModel.setInvoiceId(res.getString(res.getColumnIndex(INVOICE_ID)));

            paymentModel.setChequeDetail(chequeDetailsModel);
        }
        res.close();
        db.close();
        return paymentModel;
    }

    // get crate info
    public CrateDetailModel getCustomerCrateInfo(String customer_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        CrateDetailModel crateDetailModel = new CrateDetailModel();

        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + CUSTOMER_ID + "='" + customer_id + "'", null);
        if (res.moveToFirst()) {
            crateDetailModel.setCustomer_id(res.getString(res.getColumnIndex(CUSTOMER_ID)));
            crateDetailModel.setIssuedCrates(res.getInt(res.getColumnIndex(ISSUED_CRATES)));
            crateDetailModel.setReceivedCrates(res.getInt(res.getColumnIndex(RECEIVED_CRATES)));
            crateDetailModel.setImei_no(res.getString(res.getColumnIndex(IMEI_NO)));
            crateDetailModel.setLat_lng(res.getString(res.getColumnIndex(LAT_LNG)));
            crateDetailModel.setTime_stamp(res.getString(res.getColumnIndex(CURTIME)));
            crateDetailModel.setLogin_id(res.getString(res.getColumnIndex(LOGIN_ID)));
        }
        res.close();
        db.close();
        return crateDetailModel;
    }

  /*  // check if the product is in customer rejection list exists or not
    public int custRejExists(String customer_id, String item_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME, CUSTOMER_ID + "=? AND " + ITEM_ID + "=?", new String[]{customer_id, item_id});
        db.close();
        return numRows;
    }*/

  /*  // get grand total for customer rejections
    public double custRejTotalAmount(String customer_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select sum(" + GRAND_TOTAL + ") as total " +
                "from " + TABLE_NAME + " where " + CUSTOMER_ID + "=?";
        Cursor res = db.rawQuery(query, new String[]{customer_id});

        double amount = 0;
        if (res.moveToFirst()) {
            amount = res.getInt(res.getColumnIndex("total"));
        }
        res.close();
        db.close();
        return amount;
    }*/

    // get total issued crates for the route
    public int routeIssuedCrate() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select sum(" + ISSUED_CRATES + ") as total " +
                "from " + TABLE_NAME;
        Cursor res = db.rawQuery(query, null);

        int total = 0;
        if (res.moveToFirst()) {
            total = res.getInt(res.getColumnIndex("total"));
        }
        res.close();
        db.close();
        return total;
    }

    // get total issued crates for the route
    public int routeReceivedCrate() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select sum(" + RECEIVED_CRATES + ") as total " +
                "from " + TABLE_NAME;
        Cursor res = db.rawQuery(query, null);

        int total = 0;
        if (res.moveToFirst()) {
            total = res.getInt(res.getColumnIndex("total"));
        }
        res.close();
        db.close();
        return total;
    }

    // get total of cash and cheque amount collected by the cashier on certain root
    public CashCheck routeTotalAmountCollection() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select sum(" + CASH_PAID + ") as cash_total, sum(" + CHEQUE_AMOUNT + ") as cheque_total" +
                " from " + TABLE_NAME;
        Cursor res = db.rawQuery(query, null);

        CashCheck cashCheck = new CashCheck();

        if (res.moveToFirst()) {
            cashCheck.setCash_collected(res.getDouble(res.getColumnIndex("cash_total")));
            cashCheck.setCheque_amount(res.getDouble(res.getColumnIndex("cheque_total")));
        }
        res.close();
        db.close();
        return cashCheck;
    }

    // customer cash collection details
    public ArrayList<CollectionCashModel> syncCompletedCashDetail() {
        ArrayList<CollectionCashModel> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        InvoiceOutTable invoiceOutTable = new InvoiceOutTable(mContext);
        CustomerRouteMappingView routeCustomerView = new CustomerRouteMappingView(mContext);
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " where " + INV_STATUS + "=?",
                new String[]{Constant.STATUS_COMPLETE});
        if (res.moveToFirst()) {
            while (res.isAfterLast() == false) {
                CollectionCashModel cashModel = new CollectionCashModel();
                String customer_id = res.getString(res.getColumnIndex(CUSTOMER_ID));

                cashModel.setInvoice_no(invoiceOutTable.returnCustomerInvoiceNo(customer_id));
                cashModel.setBill_no(invoiceOutTable.returnCustomerBillNo(customer_id));
                cashModel.setCustomer_id(customer_id);
                cashModel.setOpening(routeCustomerView.custCreditAmount(cashModel.getCustomer_id()));
                cashModel.setSale(res.getDouble(res.getColumnIndex(SALE_AMOUNT)));
                cashModel.setReceive(res.getDouble(res.getColumnIndex(CASH_PAID)));

                cashModel.setUpi_reference_id(res.getString(res.getColumnIndex(UPI_REFERENCE_ID)));
                cashModel.setUpi_amount(res.getDouble(res.getColumnIndex(UPI_AMOUNT)));

                cashModel.setBalance(cashModel.getOpening() + cashModel.getSale() - cashModel.getReceive() - cashModel.getUpi_amount());

                cashModel.setImei_no(res.getString(res.getColumnIndex(IMEI_NO)));
                cashModel.setLat_lng(res.getString(res.getColumnIndex(LAT_LNG)));
                cashModel.setTime_stamp(res.getString(res.getColumnIndex(CURTIME)));
                cashModel.setLogin_id(res.getString(res.getColumnIndex(LOGIN_ID)));
                cashModel.setDate(res.getString(res.getColumnIndex(DATE)));
                array_list.add(cashModel);
                res.moveToNext();
            }
        }
        res.close();
        db.close();
        return array_list;
    }

    /*route cheque collection details*/
    public ArrayList<CollectionChequeModel> syncCompletedChequeDetail(String route_id) {
        ArrayList<CollectionChequeModel> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        InvoiceOutTable invoiceOutTable = new InvoiceOutTable(mContext);

        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " where " + CHEQUE_AMOUNT + ">0 and " + INV_STATUS + "=?",
                new String[]{Constant.STATUS_COMPLETE});
        if (res.moveToFirst()) {
            while (res.isAfterLast() == false) {
                CollectionChequeModel chequeModel = new CollectionChequeModel();
                String customer_id = res.getString(res.getColumnIndex(CUSTOMER_ID));

                chequeModel.setRoute_id(route_id);
                chequeModel.setInvoice_no(invoiceOutTable.returnCustomerInvoiceNo(customer_id));
                chequeModel.setBill_no(invoiceOutTable.returnCustomerBillNo(customer_id));
                chequeModel.setCustomer_id(customer_id);
                chequeModel.setCheque_no(res.getString(res.getColumnIndex(CHEQUE_NUMBER)));
                chequeModel.setCheque_date(res.getString(res.getColumnIndex(CHEQUE_DATE)));
                chequeModel.setCheque_amount(res.getDouble(res.getColumnIndex(CHEQUE_AMOUNT)));
                chequeModel.setBank_name(res.getString(res.getColumnIndex(BANK_NAME)));
                chequeModel.setBranch_name(res.getString(res.getColumnIndex(BANK_BRANCH)));
                chequeModel.setImei_no(res.getString(res.getColumnIndex(IMEI_NO)));
                chequeModel.setLat_lng(res.getString(res.getColumnIndex(LAT_LNG)));
                chequeModel.setTime_stamp(res.getString(res.getColumnIndex(CURTIME)));
                chequeModel.setLogin_id(res.getString(res.getColumnIndex(LOGIN_ID)));
                chequeModel.setDate(res.getString(res.getColumnIndex(DATE)));

                array_list.add(chequeModel);
                res.moveToNext();
            }
        }
        res.close();
        db.close();
        return array_list;
    }

    // customer crate collection details
    public ArrayList<CollectionCrateModel> syncCompletedCrateDetail() {
        ArrayList<CollectionCrateModel> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        InvoiceOutTable invoiceOutTable = new InvoiceOutTable(mContext);
        CustomerRouteMappingView routeCustomerView = new CustomerRouteMappingView(mContext);
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " where " + INV_STATUS + "=?",
                new String[]{Constant.STATUS_COMPLETE});
        if (res.moveToFirst()) {
            while (res.isAfterLast() == false) {
                CollectionCrateModel crateModel = new CollectionCrateModel();
                String customer_id = res.getString(res.getColumnIndex(CUSTOMER_ID));

                crateModel.setInvoice_no(invoiceOutTable.returnCustomerInvoiceNo(customer_id));
                crateModel.setBill_no(invoiceOutTable.returnCustomerBillNo(customer_id));
                crateModel.setCustomer_id(customer_id);
                crateModel.setOpening(routeCustomerView.custCreditCrates(customer_id));
                crateModel.setIssued(res.getInt(res.getColumnIndex(ISSUED_CRATES)));
                crateModel.setReceive(res.getInt(res.getColumnIndex(RECEIVED_CRATES)));
                crateModel.setBalance(crateModel.getOpening() + crateModel.getIssued() - crateModel.getReceive());
                crateModel.setImei_no(res.getString(res.getColumnIndex(IMEI_NO)));
                crateModel.setLat_lng(res.getString(res.getColumnIndex(LAT_LNG)));
                crateModel.setTime_stamp(res.getString(res.getColumnIndex(CURTIME)));
                crateModel.setLogin_id(res.getString(res.getColumnIndex(LOGIN_ID)));
                crateModel.setDdate(res.getString(res.getColumnIndex(DATE)));

                array_list.add(crateModel);
                res.moveToNext();
            }
        }
        res.close();
        db.close();
        return array_list;
    }

    // get total crate info
    public CrateStockCheck syncCrateStock(String route_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        CustomerRouteMappingView routeCustomerView = new CustomerRouteMappingView(mContext);
        CrateStockCheck crateStock = new CrateStockCheck();

        Cursor res = db.rawQuery("SELECT sum(" + ISSUED_CRATES + ") as issued, sum(" + RECEIVED_CRATES + ") as received  FROM " + TABLE_NAME, null);
        if (res.moveToFirst()) {
            crateStock.setRouteId(route_id);
            crateStock.setOpening(routeCustomerView.vanTotalCrate());
            crateStock.setIssued(res.getInt(res.getColumnIndex("issued")));
            crateStock.setReceived(res.getInt(res.getColumnIndex("received")));
            crateStock.setBalance(crateStock.getOpening() + crateStock.getIssued() - crateStock.getReceived());
        }
        res.close();
        db.close();
        return crateStock;
    }

    // day summary requirements

    // total row counts

    public int dataCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME, null, null);
        db.close();
        return numRows;
    }

    // total sale amount sum
    public double getRouteSale() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("SELECT sum(" + SALE_AMOUNT + ") as route_sale FROM " + TABLE_NAME, null);

        double route_sale = 0;
        if (res.moveToFirst()) {
            route_sale = res.getDouble(res.getColumnIndex("route_sale"));
        }
        res.close();
        db.close();
        return route_sale;
    }

    // cancel customer prepared invoice
    public void cancelInvoice(String customer_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, CUSTOMER_ID + "=?", new String[]{customer_id});
        db.close();
    }

    /*amounts for day summary*/
    public DaySummaryAmountCollectionModel routeCollectionDetail() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("SELECT sum(" + CASH_PAID + ") as cash_paid, sum(" + UPI_AMOUNT + ") as upi_amount, "
                + "sum(" + CHEQUE_AMOUNT + ") as cheque_amount FROM " + TABLE_NAME, null);

        DaySummaryAmountCollectionModel cModel = new DaySummaryAmountCollectionModel();
        if (res.moveToFirst()) {
            cModel.setCashCollected(res.getDouble(res.getColumnIndex("cash_paid")));
            cModel.setUPIAmount(res.getDouble(res.getColumnIndex("upi_amount")));
            cModel.setChequeAmount(res.getDouble(res.getColumnIndex("cheque_amount")));
            cModel.setTotalCollection(cModel.getCashCollected() + cModel.getUPIAmount());
        }
        res.close();
        db.close();
        return cModel;
    }
}