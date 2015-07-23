package com.example.dibyenduroy.livingsocial;

import android.app.Activity;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

import com.example.dibyenduroy.livingsocial.model.Merchant;
import com.example.dibyenduroy.livingsocial.model.MerchantsResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.robotium.solo.Solo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BaseActivityTest<T extends Activity> extends ActivityInstrumentationTestCase2<T> {
    protected Solo solo;
    protected static Gson gson;
    protected final static String STUB_PATH = "stub/";

    public BaseActivityTest(Class<T> activityClass) {
        super(activityClass);
    }

    @Override
    public void setUp(){
        solo = new Solo(this.getInstrumentation(), this.getActivity());
        solo.unlockScreen();

    }

    @Override
    public void tearDown(){
        solo.finishOpenedActivities();
    }

    public Gson getGson(){
        if (gson == null) {
            gson = new GsonBuilder()
                    .create();
        }
        return gson;
    }

    protected Merchant[] getMockResposne(){
        String respsponse = openFileFromAssets(STUB_PATH + "merchantlist");
        return getGson().fromJson(respsponse, Merchant[].class);
    }

    public String openFileFromAssets(String fileName){

        Context c = this.getInstrumentation().getContext();
        InputStream is = null;
        try {
            is = c.getAssets().open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(is);
        //Read stub data from assets
        String fullData = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while((line = reader.readLine()) != null){
                sb.append(line);
            }
            is.close();
            reader.close();
            fullData = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fullData;
    }
}