package com.example.hanut.retrofit;

import com.example.hanut.models.FCMBody;
import com.example.hanut.models.FCMResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMApi {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAA2UWsOp8:APA91bFcbE3G-deYRr0rXyMkS_4en_z-rKWJF9pkltPimLbj2uGf2vxosHvAjCvwt1RcEiCatZo9S3nU9dzZnW8SmthK8XjHlG_ZOFXUw6Pnkbnd9Va_gXs4GtSS5__5ONm1g2BQuhV1",
            ""
    })

    @POST("fcm/send")
    Call<FCMResponse> send(@Body FCMBody body);

}
