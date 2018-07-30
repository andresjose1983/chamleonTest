package com.trofiventures.testchameleonframework.viewmodel;

import android.content.Context;
import android.util.Log;

import com.trofiventures.chameleon.ChameleonViewModel;
import com.trofiventures.chameleon.SkinNotifier;

/**
 * Create activity viewmodel in order to notify this one about to change the theme and extends the ChameleonViewmodel
 * and implements SkinNotifier to get the default skin or beacon skin
 */
public class TestViewModel extends ChameleonViewModel implements SkinNotifier {

    private Context context;
    //private BeaconScan beaconScan;

    public void onResume() {
        //  beaconScan = new BeaconScan(context, this);
        //  beaconScan.onResume();
        super.getSkin(context, "0220320711487488002", "");
    }

    public void onPause() {
        //beaconScan.onPause();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void beaconSkin(String s) {
        Log.d("chameleon", s);
    }

    @Override
    public void defaultSkin() {

    }
}
