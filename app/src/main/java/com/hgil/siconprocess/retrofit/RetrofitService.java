package com.hgil.siconprocess.retrofit;

import com.hgil.siconprocess.retrofit.loginResponse.loginResponse;
import com.hgil.siconprocess.retrofit.loginResponse.syncResponse;
import com.hgil.siconprocess.utils.API;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by mohan.giri on 04-01-2017.
 */

public interface RetrofitService {
  /*  @GET(API.LOGIN_URL)
    Call<loginResponse> getUserLogin(@Path("username") String username, @Path("password") String password);*/

    @FormUrlEncoded
    @POST(API.LOGIN_URL)
    Call<loginResponse> postUserLogin(@Field("route_id") String username, @Field("password") String password,
                                      @Field("imei_number") String imei_number);

    /*@FormUrlEncoded
    @POST(API.LOGIN_URL)
    Call<ResponseBody> testLogin(@Field("username") String username, @Field("password") String password);*/

    @FormUrlEncoded
    @POST(API.SYNC_URL)
    Call<syncResponse> syncRouteData(@Field("depot_id") String depot_id,
                                     @Field("route_id") String username,
                                     @Field("route_management_id") String routeManagementId,
                                     @Field("cashier_paycode") String cashier_paycode,
                                     @Field("route_data") String route_data,
                                     @Field("imei_number") String imei_number);

    @FormUrlEncoded
    @POST(API.VAN_CLOSE_URL)
    Call<syncResponse> syncRouteVanClose(@Field("depot_id") String depot_id,
                                             @Field("route_id") String username,
                                             @Field("routeManagementId") String routeManagementId,
                                             @Field("cashier_paycode") String cashier_paycode,
                                             @Field("supervisor_paycode") String supervisor_paycode,
                                             @Field("van_close_data") String cashier_data,
                                             @Field("imei_number") String imei_number);

    @FormUrlEncoded
    @POST(API.FINAL_PAYMENT_URL)
    Call<syncResponse> syncFinalPayment(@Field("depot_id") String depot_id,
                                             @Field("route_id") String username,
                                             @Field("routeManagementId") String routeManagementId,
                                             @Field("cashier_paycode") String cashier_paycode,
                                             @Field("supervisor_paycode") String supervisor_paycode,
                                             @Field("payment_data") String cashier_data,
                                             @Field("imei_number") String imei_number);


}
