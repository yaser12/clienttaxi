package clienttaxi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import clienttaxi.R;

import java.util.Random;

import androidbootstrap.BootstrapAlert;
import androidbootstrap.BootstrapProgressBar;
import androidbootstrap.api.defaults.DefaultBootstrapSize;
import database.entity.TaxiJsonFetcherService;
import utility.NetworkHelper;


public class AddNewTaxiActivity extends AppCompatActivity {
    private static final String TAG = AddNewTaxiActivity.class.getSimpleName();
    int number_trying= 1;
    private Random random;
    private boolean networkOk;
     
    BootstrapAlert add_taxi_signup_success_alert;
    BootstrapAlert add_taxi_network_not_available_alert;
    BootstrapAlert add_taxi_some_faild_empty_alert;
    BootstrapProgressBar add_taxi_sizeExample;
    TextView output;
    EditText input_driver,input_decription;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            String message =
//                    intent.getStringExtra(MyService.MY_SERVICE_PAYLOAD);
            if(intent.getAction().equals(TaxiJsonFetcherService.ADD_NEW_TAXI_MESSAGE))
            {
                Log.v(TAG,"number_trying = "+number_trying);
                if(TaxiJsonFetcherService.ADD_NEW_TAXI_PAYLOAD==null && number_trying<10)
                {
                    Log.v(TAG,"try again");
                    add_me_as_taxi_office() ;
                    number_trying++;
                }
                else {
                    Log.v(TAG,"try finish");
                    countDownTimer.cancel();
                    add_taxi_sizeExample.setVisibility(View.INVISIBLE);
                    if(
                            intent.getStringExtra(TaxiJsonFetcherService.ADD_NEW_TAXI_PAYLOAD)==null
                                    ||
                                    NetworkHelper.getValidint(intent.getStringExtra(TaxiJsonFetcherService.ADD_NEW_TAXI_PAYLOAD))<0
                                    ||
                                    intent.getStringExtra(TaxiJsonFetcherService.ADD_NEW_TAXI_PAYLOAD).equals("")
                    )
                    {
                        add_taxi_signup_success_alert.dismiss(true);
                        add_taxi_network_not_available_alert.show(true);

                    }
                    else
                    {
                        add_taxi_signup_success_alert.show(true);
                        add_taxi_network_not_available_alert.dismiss(true);

                        output.setText(context.getString(R.string.your_number_is)+ intent.getStringExtra(TaxiJsonFetcherService.ADD_NEW_TAXI_PAYLOAD) + "\n");
                        /*

                         */
                    }

                    number_trying=0;
                }

            }


        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_taxi);
        add_taxi_some_faild_empty_alert=findViewById(R.id.add_taxi_some_faild_empty_alert);
        add_taxi_some_faild_empty_alert.dismiss(true);
        add_taxi_signup_success_alert=findViewById(R.id.add_taxi_signup_success_alert);
        add_taxi_signup_success_alert.dismiss(true);
        add_taxi_network_not_available_alert=findViewById(R.id.add_taxi_network_not_available_alert);
        add_taxi_network_not_available_alert.dismiss(true);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        input_decription=findViewById(R.id.input_decription);
        input_driver=findViewById(R.id.input_driver);

        add_taxi_sizeExample=findViewById(R.id.add_taxi_progress_default);
        add_taxi_sizeExample.setVisibility(View.INVISIBLE);
        DefaultBootstrapSize size = DefaultBootstrapSize.SM;

        size = DefaultBootstrapSize.MD;

        size = DefaultBootstrapSize.LG;
        size = DefaultBootstrapSize.XS;
        size = DefaultBootstrapSize.XL;


        add_taxi_sizeExample.setBootstrapSize(size);
        add_taxi_sizeExample.setProgress(randomProgress(10, 1000));

        output = (TextView) findViewById(R.id.add_taxi_result);
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(TaxiJsonFetcherService.ADD_NEW_TAXI_MESSAGE));
        networkOk = NetworkHelper.hasNetworkAccess(this);
    }
    CountDownTimer countDownTimer = new CountDownTimer(60 * 1000, 1000) {
        public void onTick(long millisUntilFinished) {

            if(add_taxi_sizeExample.getProgress()<200-50) {
                Log.d(TAG,"add_taxi_sizeExample.getProgress() : "+add_taxi_sizeExample.getProgress()+"/"+add_taxi_sizeExample.getMaxProgress());
                add_taxi_sizeExample.setProgress(Math.round(add_taxi_sizeExample.getProgress() + 50));
            }
        }
        public void onFinish() {

            add_taxi_sizeExample.setProgress( 200);

        }
    };
    private int randomProgress(int currentProgress, int maxProgress) {
        if (random == null) {
            random = new Random();
        }

        int prog = currentProgress + random.nextInt(20);

        if (prog > maxProgress) {
            prog -= maxProgress;
        }

        return prog;
    }
    private  void add_me_as_taxi_office()
    {
        //  office_name=lattakia&lang=22.33&lat=33.44&office_username=ali&office_pass=lat2468
        networkOk = NetworkHelper.hasNetworkAccess(this);

        if (networkOk)
        {
            add_taxi_network_not_available_alert.dismiss(true);
            add_taxi_sizeExample.setVisibility(View.VISIBLE);
            countDownTimer.start();
            LocalBroadcastManager.getInstance(getApplicationContext())
                    .registerReceiver(mBroadcastReceiver,
                            new IntentFilter(TaxiJsonFetcherService.ADD_NEW_TAXI_MESSAGE));
            String input_driverstr=null;
            String input_decriptionStr=null;
            String latStr=null;
            String office_idStr=null;
            String office_usernameStr=null;

            input_driverstr= Uri.encode(input_driver.getText().toString(), "UTF-8");
            input_decriptionStr=Uri.encode(input_decription.getText().toString(), "UTF-8");
            SharedPreferences pref=getSharedPreferences(AddNewOfficeActivity.SHARED_NAME,MODE_PRIVATE);
            String office_idss=pref.getString("office_id","");
            office_idStr=Uri.encode(office_idss, "UTF-8");



            String urltosend= TaxiJsonFetcherService. ADD_NEW_TAXI_JSON_URL+"officeid="+office_idStr+"&driver="+input_driverstr+"&descrip="+input_decriptionStr+"&lang=55.6666&lat=66.7777&is_busy=1";
            Log.v(TAG,"urltosend = "+urltosend);
            Intent intent = new Intent(this, TaxiJsonFetcherService.class);
            intent.setAction(TaxiJsonFetcherService.ADD_NEW_TAXI_MESSAGE);
            intent.setData(Uri.parse(urltosend));
            number_trying=0;
            startService(intent);

        } else {
            // Toast.makeText(this, "Network not available!", Toast.LENGTH_SHORT).show();
            add_taxi_network_not_available_alert.show(true);
        }
    }

    public boolean check_empty_faild()
    {


        if(input_driver.getText()==null ||  input_driver.getText().toString().trim().equals( "") ) return false;
        if(input_decription.getText()==null ||  input_decription.getText().toString().trim().equals( "")  ) return false;

        return  true;
    }



    public void add_new_taxi_info_to_my_officebtn(View view) {
        if(check_empty_faild())
        {
            add_taxi_some_faild_empty_alert.dismiss(true);
            add_me_as_taxi_office();
        }
        else
        {

            add_taxi_some_faild_empty_alert.show(true);
        }
    }
}
