package database.entity;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import utility.HttpHelper;

public class EndUserJsonFetcherService extends IntentService {

    public static final String TAG = "EndUserJsonService";

    /////////////////////// ADD_NEW_OFFICE/////////////////////
    public static final String ADD_NEW_EndUse_MESSAGE = "ADD_NEW_EndUseMessage";
    public static final String ADD_NEW_EndUse_PAYLOAD = "ADD_NEW_EndUsePayload";
    public static final String ADD_NEW_EndUse_JSON_URL = "http://syriataxi.000webhostapp.com/add_new_enduser.php/?";// enduser_name=ahmad&lang=44.5555&lat=55.6666&user_name=ali&pass=aassdd&phone=0998888

    public EndUserJsonFetcherService() {
            super("EndUserJsonFetcherService");
        }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(     intent.getAction()!=null &&     intent.getAction().equals(ADD_NEW_EndUse_MESSAGE))
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


            Intent messageIntent = new Intent(ADD_NEW_EndUse_MESSAGE);
            messageIntent.putExtra(ADD_NEW_EndUse_PAYLOAD, response);
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

