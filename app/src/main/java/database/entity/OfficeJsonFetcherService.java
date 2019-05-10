package database.entity;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import utility.HttpHelper;

public class OfficeJsonFetcherService  extends IntentService {

    public static final String TAG = "OfficeJsonService";
    ///////GET_ALL_OFFICE///////////
    public static final String GET_ALL_OFFICE_MESSAGE = "GET_ALL_OFFICEMessage";
    public static final String GET_ALL_OFFICE_PAYLOAD = "GET_ALL_OFFICEPayload";
    public static final String GET_ALL_OFFICE_JSON_URL = "http://syriataxi.000webhostapp.com/sel_all_office.php";


    /////////////////////// ADD_NEW_OFFICE/////////////////////
    public static final String ADD_NEW_OFFICE_MESSAGE = "ADD_NEW_OFFICEMessage";
    public static final String ADD_NEW_OFFICE_PAYLOAD = "ADD_NEW_OFFICEPayload";
    public static final String ADD_NEW_OFFICE_JSON_URL = "http://syriataxi.000webhostapp.com/add_new_office.php/?";
    ///  office_name=lattakia&lang=22.33&lat=33.44&office_username=ali&office_pass=lat2468
    public OfficeJsonFetcherService() {
        super("OfficeJsonFetcherService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
            if(     intent.getAction()!=null &&     intent.getAction().equals(ADD_NEW_OFFICE_MESSAGE))
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


                Intent messageIntent = new Intent(ADD_NEW_OFFICE_MESSAGE);
                messageIntent.putExtra(ADD_NEW_OFFICE_PAYLOAD, response);
                LocalBroadcastManager manager =
                        LocalBroadcastManager.getInstance(getApplicationContext());
                manager.sendBroadcast(messageIntent);
            }
            else if( intent.getAction()!=null &&  intent.getAction().equals(GET_ALL_OFFICE_MESSAGE))
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
                Office[] dataItems = gson.fromJson(response, Office[].class);

                Intent messageIntent = new Intent(GET_ALL_OFFICE_MESSAGE);
                messageIntent.putExtra(GET_ALL_OFFICE_PAYLOAD, dataItems);
                LocalBroadcastManager manager =
                        LocalBroadcastManager.getInstance(getApplicationContext());
                manager.sendBroadcast(messageIntent);
            }


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