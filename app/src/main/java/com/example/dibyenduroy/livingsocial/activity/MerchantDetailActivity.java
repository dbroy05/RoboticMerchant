package com.example.dibyenduroy.livingsocial.activity;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.dibyenduroy.livingsocial.R;
import com.example.dibyenduroy.livingsocial.framework.MainApplication;
import com.example.dibyenduroy.livingsocial.helper.PicassoHelper;
import com.example.dibyenduroy.livingsocial.model.Merchant;
import com.example.dibyenduroy.livingsocial.service.MerchantManager;

/**
 * Created by dibyenduroy on 2/23/15.
 */
public class MerchantDetailActivity extends ActionBarActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Merchant Details");
        setContentView(R.layout.merchant_detail);
        int position = getIntent().getIntExtra(MainActivity.SELECTED_POSITION,0);
        Merchant merchant = getMerchantManager().getItemByPosition(position);
        ((TextView)findViewById(R.id.merchant_name)).setText(merchant.merchant_name);
        ((TextView)findViewById(R.id.merchant_title)).setText(merchant.title);
        ((TextView)findViewById(R.id.merchant_description)).setText(merchant.description);
        ((TextView)findViewById(R.id.price)).setText("$"+merchant.price);
        new PicassoHelper.PicassoBuilder(this)
                .setUrl(merchant.image_url)
                .setImageView((android.widget.ImageView) findViewById(R.id.merchant_product_image))
                .build();


    }

    private MerchantManager getMerchantManager() {
        MainApplication application = (MainApplication) getApplication();
        return application.getMerchantManager();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
