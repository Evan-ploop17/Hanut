package com.example.hanut.providers;

import com.example.hanut.models.FCMBody;
import com.example.hanut.models.FCMResponse;
import com.example.hanut.retrofit.IFCMApi;
import com.example.hanut.retrofit.RetrofitClient;

import retrofit2.Call;

public class NotificationProvider {

    private String url = "https://fcm.googleapis.com";


    public NotificationProvider(){}

    public Call<FCMResponse> sendNotification(FCMBody body){
        return RetrofitClient.getClient(url).create(IFCMApi.class).send(body);
    }

}
