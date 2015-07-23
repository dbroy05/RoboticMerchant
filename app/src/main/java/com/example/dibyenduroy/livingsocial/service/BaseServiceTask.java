package com.example.dibyenduroy.livingsocial.service;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.example.dibyenduroy.livingsocial.listeners.MerchantResponseListener;
import com.example.dibyenduroy.livingsocial.model.Merchant;
import com.example.dibyenduroy.livingsocial.model.MerchantsResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.BasicHttpContext;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

/**
 * Created by dibyenduroy on 2/23/15.
 */
public class BaseServiceTask extends AsyncTask<String, String, String> {

    private static final int CONNECTION_TIMEOUT_MILLIS = 1000 * 60; //45 second timeout
    private String serviceEndpoint = "http://sheltered-bastion-2512.herokuapp.com/feed.json";
    private static final boolean DEBUG_NETWORK = true;
    private static final String TAG = "BaseTask";

    private static Gson sGson;
    private boolean mNetworkFailureOccurred;
    private int mServerResponseCode = 0;
    private MerchantResponseListener mListener;
    private MerchantsResponse mResponse;

    public BaseServiceTask(MerchantResponseListener listener){
        this.mListener = listener;
    }
    protected static Gson getGson() {
        if (sGson == null) {
            sGson = new GsonBuilder()
                    .create();
        }
        return sGson;
    }
    private static BasicHttpContext sUrlContext;
    private static DefaultHttpClient sUrlClient;
    private static DefaultHttpClient getUrlClient() {
        if (sUrlClient == null) {
            SchemeRegistry schemeRegistry = new SchemeRegistry();

            schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

            BasicHttpParams params = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIMEOUT_MILLIS);
            ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);

            sUrlContext = new BasicHttpContext();
            sUrlClient = new DefaultHttpClient(cm, params);
        }
        return sUrlClient;
    }

    private void setRequestHeaders(HttpRequestBase request) {
        //JSON
        request.setHeader("Accept", "application/json");

        //Other headers
        //request.setHeader("Accept-Language", "en-US,en;q=0.5");
        //request.setHeader("Accept-Encoding", "gzip, deflate");
    }

    @Override
    protected String doInBackground(String... params) {
        HttpRequestBase request = new HttpGet();
        request.setURI(URI.create(serviceEndpoint));
        setRequestHeaders(request);

        HttpResponse response;
        try {
            response = getUrlClient().execute(request, sUrlContext);
        } catch (Exception e) {
            System.out.println("WWWWWWWW: "+e);
            mNetworkFailureOccurred = true;
            return null;
        }

        StatusLine statusLine = response.getStatusLine();
        int statusCode = statusLine.getStatusCode();
        String statusReason = statusLine.getReasonPhrase();
        HttpEntity responseEntity = response.getEntity();
        String responseString = getStringFromHttpEntity(responseEntity);
        mServerResponseCode = statusCode;
        if (statusCode < 200) {
            //100-level code is informational
            if (DEBUG_NETWORK) Log.i(TAG, "Response: "+statusCode+" "+statusReason);
            if (DEBUG_NETWORK) Log.i(TAG, ""+responseString);
        } else if (statusCode > 199 && statusCode < 300) {
            //200-level code is success
            if (DEBUG_NETWORK) Log.i(TAG, "Response: "+statusCode+" "+statusReason);
            if (DEBUG_NETWORK) Log.i(TAG, ""+responseString);
        } else if (statusCode > 299 && statusCode < 400) {
            //300-level code is redirect - shouldn't get here as the framework should automatically follow the redirect
            if (DEBUG_NETWORK) Log.i(TAG, "Response: "+statusCode+" "+statusReason);
            if (DEBUG_NETWORK) Log.i(TAG, ""+responseString);
            return null;
        } else if (statusCode > 399 && statusCode < 500) {
            //400-level code is client error
            if (DEBUG_NETWORK) Log.w(TAG, "Response: "+statusCode+" "+statusReason);
            if (DEBUG_NETWORK) Log.w(TAG, ""+responseString);
            return null;
        } else if (statusCode > 499) {
            //500-level code is server error
            if (DEBUG_NETWORK) Log.w(TAG, "Response: "+statusCode+" "+statusReason);
            if (DEBUG_NETWORK) Log.w(TAG, ""+responseString);

            return null;
        }
        try {
            handleResponseString(responseString);
        } catch (Exception e) {
            mNetworkFailureOccurred = true;
        }

        return null;
    }

    protected void handleResponseString(String responseString) {
        Merchant[] merchants = getGson().fromJson(responseString, Merchant[].class);
        mResponse = new MerchantsResponse();
        mResponse.merchants = merchants;
    }

    protected void onPostExecute(String result) {
        if (mNetworkFailureOccurred) {
            if (mListener != null) {
                mListener.onError("Error in connecting to the server..");
            }
        } else {
            if(mResponse == null){
                if(mListener != null){
                    mListener.onError(null);
                    return;
                }
            }else {
                mListener.onSuccessResult(mResponse);
            }

        }
    }

    private static String getStringFromHttpEntity(HttpEntity entity) {
        final char[] buffer = new char[1024];
        final StringBuilder out = new StringBuilder();
        try {
            InputStream inputStream;
            if (entity.getContentEncoding() != null && entity.getContentEncoding().getValue().contains("gzip")) {
                Log.d(TAG, "Response Content Encoding: " + entity.getContentEncoding().getValue());
                inputStream = new GZIPInputStream(entity.getContent());
            } else {
                inputStream = entity.getContent();
            }
            final Reader in = new InputStreamReader(inputStream, "UTF-8");
            try {
                for (;;) {
                    int rsz = in.read(buffer, 0, buffer.length);
                    if (rsz < 0)
                        break;
                    out.append(buffer, 0, rsz);
                }
            }
            finally {
                in.close();
            }
        }
        catch (Exception ex) {
            Log.e(TAG, "exception", ex);
            return null;
        }
        return out.toString();
    }
}
