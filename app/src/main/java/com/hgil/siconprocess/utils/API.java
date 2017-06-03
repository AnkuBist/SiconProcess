package com.hgil.siconprocess.utils;

/**
 * Created by mohan.giri on 04-01-2017.
 */

public class API {

    //local address
    public static final String BASE_URL = "http://10.10.1.121:8084/SiconRestApi/LEON_REST/webService_v1/";

    //live address
    //public static final String BASE_URL = "http://123.63.89.83:8080/SiconRestApi/LEON_REST/webService_v1/";

    // other api's
    public static final String LOGIN_URL = "leon_loginUser";
    public static final String SUPERVISOR_LOGIN_URL = "leon_supervisorLogin";
    public static final String SYNC_INVOICE_URL = "leon_syncInvoice";
    public static final String SYNC_URL = "leon_syncRoute";
    public static final String VAN_CLOSE_URL = "leon_vanClose";
    public static final String FINAL_PAYMENT_URL = "leon_finalPayment";
}
