package database.entity;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import utility.HttpHelper;

public class TaxiJsonFetcherService extends IntentService {

    public static final String TAG = "TaxiJsonFetcherService";
    ////////////////////////   ADD_NEW_TAXI //////////////////////////
    public static  final String ADD_NEW_TAXI_JSON_URL="http://syriataxi.000webhostapp.com/add_new_taxi.php/?";
    public static final String ADD_NEW_TAXI_MESSAGE = "ADD_NEW_TAXI_Message";
    public static final String ADD_NEW_TAXI_PAYLOAD = "ADD_NEW_TAXI_Payload";
    ////////////////////////   SHOW_ALL_TAXIS //////////////////////////
    public static  final String SHOW_ALL_TAXIS_JSON_URL="http://syriataxi.000webhostapp.com/sel_taxi_4_office.php/?";
    public static final String SHOW_ALL_TAXIS_MESSAGE = "SHOW_ALL_TAXIS_Message";
    public static final String SHOW_ALL_TAXIS_PAYLOAD = "SHOW_ALL_TAXIS_Payload";

    public TaxiJsonFetcherService() {
        super("TaxiJsonFetcherServer");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(     intent.getAction()!=null &&     intent.getAction().equals(ADD_NEW_TAXI_MESSAGE))
        {
            Uri uri = intent.getData();
            Log.i(TAG, "onHandleIntent: " + uri.toString());

            String response;
            try {
                response = HttpHelper.downloadUrl(uri.toString());
            } catch (IOException e) {
                e.printStackTrace();
                response=null;
                // return;
            }
            Log.i(TAG, "response: " +response);


            Intent messageIntent = new Intent(ADD_NEW_TAXI_MESSAGE);
            messageIntent.putExtra(ADD_NEW_TAXI_PAYLOAD, response);
            LocalBroadcastManager manager =
                    LocalBroadcastManager.getInstance(getApplicationContext());
            manager.sendBroadcast(messageIntent);
        }
        else if( intent.getAction()!=null &&  intent.getAction().equals(SHOW_ALL_TAXIS_MESSAGE))
        {
            Uri uri = intent.getData();
            Log.i(TAG, "onHandleIntent: " + uri.toString());

            String response;
            try {
                response = HttpHelper.downloadUrl(uri.toString());
            } catch (IOException e) {
                e.printStackTrace();
                response=null;
            }
            Log.i(TAG, "response: " +response);
            Gson gson = new Gson();
            Taxi[] dataItems = gson.fromJson(response, Taxi[].class);

            Log.i(TAG, "dataItems:.length " +dataItems.length+" dataItems = "+dataItems.toString());
            Intent messageIntent = new Intent(SHOW_ALL_TAXIS_MESSAGE);
            messageIntent.putExtra(SHOW_ALL_TAXIS_PAYLOAD, dataItems);
            LocalBroadcastManager manager =
                    LocalBroadcastManager.getInstance(getApplicationContext());
            manager.sendBroadcast(messageIntent);
        }
        //////////////////////
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }
}