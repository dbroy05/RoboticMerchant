package com.example.dibyenduroy.livingsocial.framework;

import android.app.Application;

import com.example.dibyenduroy.livingsocial.service.MerchantManager;

/**
 * Created by dibyenduroy on 2/23/15.
 */
public class MainApplication extends Application {

    private static MerchantManager merchantManager;
    public MerchantManager getMerchantManager() {
        if (merchantManager == null) {
            merchantManager = new MerchantManager();
        }
        return merchantManager;
    }
}
