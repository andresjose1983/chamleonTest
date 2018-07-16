package com.trofiventures.testchameleonframework.viewmodel;

import android.content.Context;
import android.util.Log;

import com.trofiventures.chameleon.Chameleon;
import com.trofiventures.chameleon.ChameleonViewModel;
import com.trofiventures.chameleon.SkinNotifier;
import com.trofiventures.chameleon.beacon.BeaconScan;
import com.trofiventures.testchameleonframework.model.SkinClient;
import com.trofiventures.testchameleonframework.service.RetrofitClient;

import org.json.JSONObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Create activity viewmodel in order to notify this one about to change the theme and extends the ChameleonViewmodel
 * and implements SkinNotifier to get the default skin or beacon skin
 */
public class TestViewModel extends ChameleonViewModel implements SkinNotifier {

    private Context context;
    private BeaconScan beaconScan;
    private RetrofitClient retrofitClient;

    public void onResume() {
        retrofitClient = new RetrofitClient();
        beaconScan = new BeaconScan(context, this);
        beaconScan.onResume();
    }

    public void onPause() {
        beaconScan.onPause();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void beaconSkin(String s) {
        Log.d("chameleon", s);
        // call resource to get beacon skin
        SkinClient skinClient = new SkinClient();
        skinClient.setBeaconId(s);
        skinClient.setBundleId("com.sixg.android.sixgDigital.incircl");
        retrofitClient.get().getSkin(skinClient).enqueue(
                new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        Log.d("chameleon", "" + response.body());
                        //save skin
                        Chameleon.Companion.saveSkin(context, new JSONObject((Map<String, String>) response.body()));
                        //notify views to change color
                        change(context, true);
                        //
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        Log.d("chameleon", "error");

                    }
                }
        );
    }

    @Override
    public void defaultSkin() {
        Log.d("chameleon", "default");
        retrofitClient.get().getSkin("5b2aa33b59981820f4d2f2cf", "default").enqueue(
                new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        Log.d("chameleon default", "" + response.body());
                        //save skin
                        Chameleon.Companion.saveSkin(context, new JSONObject((Map<String, String>) response.body()));
                        //notify views to change color
                        change(context, true);
                        //
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        Log.d("chameleon", "error");

                    }
                }
        );
    }
}
