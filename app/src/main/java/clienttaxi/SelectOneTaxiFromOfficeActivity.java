package clienttaxi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidbootstrap.BootstrapAlert;
import androidbootstrap.BootstrapProgressBar;
import database.entity.Taxi;
import database.entity.TaxiJsonFetcherService;
import database.entity.TaxisCallAdapter;
import utility.NetworkHelper;
import clienttaxi.R;
public class SelectOneTaxiFromOfficeActivity extends AppCompatActivity {
    private static final String TAG = ShowAllTaxis_for_OfficeActivity.class.getSimpleName();
    RecyclerView all_taxi_recyclerView;
    int number_trying= 1;
    private Random random;
    private List<Taxi> allTaxi=new ArrayList<>();
    private TaxisCallAdapter taxisCallAdapter;
    private boolean networkOk;
    BootstrapProgressBar show_all_taxis_sizeExample;
    BootstrapAlert show_all_signup_success_alert;
    BootstrapAlert show_all_network_not_available_alert;
    BootstrapAlert show_all_some_faild_empty_alert,notaxiforthisoffice;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            String message =
//                    intent.getStringExtra(MyService.MY_SERVICE_PAYLOAD);
            notaxiforthisoffice.dismiss(true);
            if(intent.getAction().equals(TaxiJsonFetcherService.SHOW_ALL_TAXIS_MESSAGE))
            {
                Log.v(TAG,"number_trying = "+number_trying);
                if(TaxiJsonFetcherService.SHOW_ALL_TAXIS_PAYLOAD==null && number_trying<10)
                {
                    Log.v(TAG,"try again");
                    show_all_taxi_my_office() ;
                    number_trying++;
                }
                else {

                    Log.v(TAG,"try finish");
                    countDownTimer.cancel();
                    show_all_taxis_sizeExample.setVisibility(View.INVISIBLE);
                    Taxi[] dataItems = (Taxi[]) intent
                            .getParcelableArrayExtra(TaxiJsonFetcherService.SHOW_ALL_TAXIS_PAYLOAD);
                    Log.d(TAG,"Taxi : dataItems = "+dataItems);
                    if(dataItems==null)
                    {
                        show_all_signup_success_alert.dismiss(true);
                        show_all_network_not_available_alert.show(true);
                        notaxiforthisoffice.dismiss(true);
                    }
                    else if(dataItems.length==0)
                    {
                        notaxiforthisoffice.show(true);
                        show_all_signup_success_alert.dismiss(true);
                        show_all_network_not_available_alert.dismiss(true);
                    }
                    else
                    {
                        notaxiforthisoffice.dismiss(true);
                        show_all_signup_success_alert.show(true);
                        show_all_network_not_available_alert.dismiss(true);
                        for(int i=0;i<dataItems.length;i++){
                            allTaxi.add(new Taxi(dataItems[i].getDescrip(), dataItems[i].getDriver(), dataItems[i].getFk_office_id(), dataItems[i].getIs_busy(), dataItems[i].getLang(), dataItems[i].getLat(), dataItems[i].getTaxi_id()));
                        }

                        initRecylceView();


                    }


                    number_trying=0;
                }

            }


        }
    };

    private void show_all_taxi_my_office() {

        //  office_name=lattakia&lang=22.33&lat=33.44&office_username=ali&office_pass=lat2468
        networkOk = NetworkHelper.hasNetworkAccess(this);

        if (networkOk)
        {
            show_all_network_not_available_alert.dismiss(true);
            show_all_taxis_sizeExample.setVisibility(View.VISIBLE);
            countDownTimer.start();
            LocalBroadcastManager.getInstance(getApplicationContext())
                    .registerReceiver(mBroadcastReceiver,
                            new IntentFilter(TaxiJsonFetcherService.SHOW_ALL_TAXIS_MESSAGE));






            String office_idss=office_idStr;
            office_idStr= Uri.encode(office_idss, "UTF-8");
            String urltosend= TaxiJsonFetcherService. SHOW_ALL_TAXIS_JSON_URL+"office_id="+office_idStr;
            Log.v(TAG,"urltosend = "+urltosend);
            Intent intent = new Intent(this, TaxiJsonFetcherService.class);
            intent.setAction(TaxiJsonFetcherService.SHOW_ALL_TAXIS_MESSAGE);
            intent.setData(Uri.parse(urltosend));
            number_trying=0;
            startService(intent);

        } else {
            // Toast.makeText(this, "Network not available!", Toast.LENGTH_SHORT).show();
            show_all_network_not_available_alert.show(true);
        }

    }
    String office_idStr=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent= getIntent();

        office_idStr = intent.getStringExtra("office_id");
        Log.d(TAG," office_idStr : "+office_idStr);

        setContentView(R.layout.activity_select_one_taxi_from_office);
        Toolbar toolbar = findViewById(R.id.toolbar);

        all_taxi_recyclerView=findViewById(R.id.all_taxi_recycler_view);
        show_all_network_not_available_alert=findViewById(R.id.show_all_network_not_available_alert);
        show_all_network_not_available_alert.dismiss(true);
        show_all_signup_success_alert=findViewById(R.id.show_all_signup_success_alert);

        show_all_signup_success_alert.dismiss(true);
        show_all_some_faild_empty_alert=findViewById(R.id.show_all_some_faild_empty_alert);
        show_all_some_faild_empty_alert.dismiss(true);
        setSupportActionBar(toolbar);
        show_all_taxis_sizeExample=findViewById(R.id.show_all_taxis_progress_default);
        show_all_taxis_sizeExample.setVisibility(View.VISIBLE);
        notaxiforthisoffice=findViewById(R.id.notaxiforthisoffice);
        notaxiforthisoffice.setVisibility(View.VISIBLE);

        show_all_taxi_my_office();
    }

    private void initRecylceView()
    {
        all_taxi_recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        all_taxi_recyclerView.setLayoutManager(layoutManager);
        taxisCallAdapter=new TaxisCallAdapter(allTaxi,this);
        all_taxi_recyclerView.setAdapter(taxisCallAdapter);
        DividerItemDecoration divier=new DividerItemDecoration(
                all_taxi_recyclerView.getContext(),layoutManager.getOrientation()
        );
        all_taxi_recyclerView.addItemDecoration(divier);
    }
    CountDownTimer countDownTimer = new CountDownTimer(60 * 1000, 1000) {
        public void onTick(long millisUntilFinished) {
            if(show_all_taxis_sizeExample.getProgress()<200-50) {
                Log.d(TAG,"show_all_taxis_sizeExample.getProgress() : "+show_all_taxis_sizeExample.getProgress()+"/"+show_all_taxis_sizeExample.getMaxProgress());
                show_all_taxis_sizeExample.setProgress(Math.round(show_all_taxis_sizeExample.getProgress() + 50));
            }
        }
        public void onFinish() {
            show_all_taxis_sizeExample.setProgress( 200);

        }
    };
    private int randomProgress(int currentProgress, int maxProgress)
    {

        if (random == null) {
            random = new Random();
        }

        int prog = currentProgress + random.nextInt(20);

        if (prog > maxProgress) {
            prog -= maxProgress;
        }

        return prog;
    }

    public static class MainActivity extends AppCompatActivity
    {
        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);



        }

        public void show_office_apponclick(View view)
        {
            startActivity(new Intent(MainActivity.this, RealLocationActivity.class));
        }



        public void show_client_apponlcick(View view) {
            startActivity(new Intent(MainActivity.this, ClientActivity.class));
        }
    }
}
