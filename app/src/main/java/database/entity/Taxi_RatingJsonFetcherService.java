package database.entity;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import utility.HttpHelper;

public class Taxi_RatingJsonFetcherService extends IntentService {

    public static final String TAG = "Taxi_RatingJsonService";
    public static final String MY_SERVICE_MESSAGE = "Taxi_RatingJsonServiceMessage";
    public static final String MY_SERVICE_PAYLOAD = "Taxi_RatingJsonServicePayload";

    public Taxi_RatingJsonFetcherService() {
        super("Taxi_RatingJsonService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Uri uri = intent.getData();
        Log.i(TAG, "onHandleIntent: " + uri.toString());

        String response;
        try {
            response = HttpHelper.downloadUrl(uri.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Gson gson = new Gson();
        Taxi_Rating[] dataItems = gson.fromJson(response, Taxi_Rating[].class);

        Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);
        messageIntent.putExtra(MY_SERVICE_PAYLOAD, dataItems);
        LocalBroadcastManager manager =
                LocalBroadcastManager.getInstance(getApplicationContext());
        manager.sendBroadcast(messageIntent);
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