package database;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import com.google.gson.Gson;
import database.entity.EndUser;
import database.entity.Office;
import database.entity.OrderTaxi;
import database.entity.Taxi;
import database.entity.Taxi_Rating;
import utility.HttpHelper;
import utility.SampleData;

public class AppRepositoryService
{

    private static AppRepositoryService ourInstance;

    public List<EndUser> mEndUser;
    public List<Office> mOffice;
    public List<OrderTaxi> mOrderTaxi;
    public List<Taxi> mTaxi;
    public List<Taxi_Rating> mTaxi_Rating;
    private AppDatabase mDb;
    private boolean networkOk;
    private static final String taxi_offine_json_url =  "http://syriataxi.000webhostapp.com/all_office.php";

    private Executor executor = Executors.newSingleThreadExecutor();



    public static AppRepositoryService getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new AppRepositoryService(context);
        }
        return ourInstance;
    }

    private AppRepositoryService(Context context)
    {


        mEndUser = SampleData.getEndUsers();
        mOffice=SampleData.getOffices();
        mOrderTaxi=SampleData.getOrderTaxis();
        mTaxi=SampleData.getTaxis();
        mTaxi_Rating =SampleData.getTaxi_Ratings();
        mDb = AppDatabase.getInstance(context);
    }

    public void addSampleData() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.endUserDao().insertAll(SampleData.getEndUsers());
                mDb.officeDao().insertAll(SampleData.getOffices());
                mDb.orderTaxiDao().insertAll(SampleData.getOrderTaxis());
                mDb.taxi_RatingDao().insertAll(SampleData.getTaxi_Ratings());
                mDb.taxiDao().insertAll(SampleData.getTaxis());
            }
        });
    }


}


