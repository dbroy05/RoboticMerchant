package com.example.dibyenduroy.livingsocial.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.dibyenduroy.livingsocial.R;
import com.example.dibyenduroy.livingsocial.adapters.MerchantListAdapter;
import com.example.dibyenduroy.livingsocial.framework.MainApplication;
import com.example.dibyenduroy.livingsocial.listeners.MerchantResponseListener;
import com.example.dibyenduroy.livingsocial.model.Merchant;
import com.example.dibyenduroy.livingsocial.model.MerchantsResponse;
import com.example.dibyenduroy.livingsocial.service.BaseServiceTask;
import com.example.dibyenduroy.livingsocial.service.MerchantManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    public static final String SELECTED_POSITION = "selected_pos";
    private MerchantListAdapter merchantListAdapter;
    private TextView errorMessage;
    private ListView merchantList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        merchantList = (ListView) findViewById(R.id.merchant_list);
        errorMessage = (TextView) findViewById(R.id.error_message);
        merchantListAdapter = new MerchantListAdapter(new WeakReference<Activity>(this),new ArrayList<Merchant>());
        merchantList.setAdapter(merchantListAdapter);
        merchantList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,MerchantDetailActivity.class);
                intent.putExtra(SELECTED_POSITION,position);
                startActivity(intent);
            }
        });
        //Fire off the task get the list
        getAllMerchantList();

    }

    private void getAllMerchantList() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();
        getMerchantManager().getAllMerchantList(new MerchantResponseListener() {
            @Override
            public void onSuccessResult(MerchantsResponse resp) {
                dialog.dismiss();
                showList();
                merchantListAdapter.clearAddAllItems(Arrays.asList(resp.merchants));
            }

            @Override
            public void onError(String faultMessage) {

            }
        });
    }

    private MerchantManager getMerchantManager() {
        MainApplication application = (MainApplication) getApplication();
        return application.getMerchantManager();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                    getAllMerchantList();

            }
        });


        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        // Special processing of the incoming intent only occurs if the if the action specified
        // by the intent is ACTION_SEARCH.
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // SearchManager.QUERY is the key that a SearchManager will use to send a query string
            // to an Activity.
            String query = intent.getStringExtra(SearchManager.QUERY);

            List<Merchant> filteredItems = getMerchantManager().getMerchantsByTitle(query);
            merchantListAdapter.clearAddAllItems(filteredItems);
            if(filteredItems.isEmpty()){
                merchantList.setVisibility(View.GONE);
                errorMessage.setVisibility(View.VISIBLE);
            }else {
                showList();
            }

        }
    }

    private void showList() {
        errorMessage.setVisibility(View.GONE);
        merchantList.setVisibility(View.VISIBLE);
    }

}
