package com.example.dibyenduroy.livingsocial;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.dibyenduroy.livingsocial.activity.MainActivity;
import com.example.dibyenduroy.livingsocial.model.Merchant;

/**
 * Created by dibyenduroy on 2/24/15.
 */
public class MerchantListActivityTest extends BaseActivityTest<MainActivity> {

    private ListView mlistView;
    private ListAdapter mMerchantData;

    public MerchantListActivityTest(Class<MainActivity> activityClass) {
        super(activityClass);
    }

    @Override
    public void setUp(){
        super.setUp();

        mlistView =
                (ListView) solo.getView(
                        R.id.merchant_list
                );

        mMerchantData = mlistView.getAdapter();

    }

    public void testPreConditions() {
        assertTrue(mlistView.getOnItemSelectedListener() != null);
        assertTrue(mMerchantData != null);
    }

    public void testMerchantListSize(){
        Merchant[] merchants = getMockResposne();
        assertTrue(merchants.length>0);
    }
}
