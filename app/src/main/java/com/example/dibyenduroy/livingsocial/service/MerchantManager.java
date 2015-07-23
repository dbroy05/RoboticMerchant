package com.example.dibyenduroy.livingsocial.service;

import com.example.dibyenduroy.livingsocial.listeners.MerchantResponseListener;
import com.example.dibyenduroy.livingsocial.model.Merchant;
import com.example.dibyenduroy.livingsocial.model.MerchantsResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dibyenduroy on 2/23/15.
 */
public class MerchantManager {
    private static MerchantsResponse merchantsResponse;

    public void getAllMerchantList(final MerchantResponseListener listener){
        if(merchantsResponse!=null){
            listener.onSuccessResult(merchantsResponse);
            return;
        }

        new BaseServiceTask(new MerchantResponseListener() {

            @Override
            public void onSuccessResult(MerchantsResponse resp) {
                merchantsResponse = resp;
                listener.onSuccessResult(resp);
            }

            @Override
            public void onError(String faultMessage) {

            }
        }).execute();
    }

    public List<Merchant> getMerchantsByTitle(String title){
        List<Merchant> filteredMerchants = new ArrayList<Merchant>();
        for(Merchant merchant: merchantsResponse.merchants){
            if(merchant.title.toLowerCase().contains(title))//Just to make search easy
                filteredMerchants.add(merchant);
        }

        return filteredMerchants;
    }

    public Merchant getItemByPosition(int position){
        return merchantsResponse.merchants[position];
    }
}
