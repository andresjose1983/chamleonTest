package com.trofiventures.testchameleonframework.service;

import com.trofiventures.testchameleonframework.model.SkinClient;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * only for test
 */
public interface ChameleonApi {

    @GET("skin/{skin_id}")
    Call<Object> getSkin(@Header("FINTV-Auth-Token") String token,
                         @Path("skin_id") String def);

    @POST("skin/client")
    Call<Object> getSkin(@Body SkinClient skinClient);

}
