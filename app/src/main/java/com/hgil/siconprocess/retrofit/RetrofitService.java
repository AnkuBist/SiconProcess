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
    Call<syncResponse> syncRouteData(@Field("route_details") String route_details,
                                     @Field("sync_route_data") String sync_route_data);

    @FormUrlEncoded
    @POST(API.VAN_CLOSE_URL)
    Call<syncResponse> syncRouteVanClose(@Field("route_details") String route_details,
                                         @Field("sync_route_data") String sync_route_data,
                                         @Field("sync_van_close_data") String sync_van_close_data);

    @FormUrlEncoded
    @POST(API.FINAL_PAYMENT_URL)
    Call<syncResponse> syncFinalPayment(@Field("route_details") String route_details,
                                        @Field("final_payment") String final_payment);


}
