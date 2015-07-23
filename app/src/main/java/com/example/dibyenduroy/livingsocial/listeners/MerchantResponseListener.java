package com.example.dibyenduroy.livingsocial.listeners;

import com.example.dibyenduroy.livingsocial.model.MerchantsResponse;

/**
 * Created by dibyenduroy on 2/23/15.
 */
public interface MerchantResponseListener {

    public void onSuccessResult(MerchantsResponse resp);
    public void onError(String faultMessage);

}
