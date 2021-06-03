package com.visualsearch.finder.notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService
{
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAXw0bUcE:APA91bGgIfUHAIf096MTRJQ3TgtydqUoo-H3dExq5Q9X_NrlOO_nxZuSob1S-bdVvLOw8jtfFo6RUD-Yjhq2j3D4WdVPolTSX6q7iGWOoQqKnDuefTz6o8wPOPd47A5YKBaLJgGEnA1Z"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
