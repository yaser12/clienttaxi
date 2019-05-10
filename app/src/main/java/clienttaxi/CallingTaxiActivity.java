package clienttaxi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import clienttaxi.R;
import database.entity.EndUserJsonFetcherService;
import database.entity.Office;
import database.entity.OfficeJsonFetcherService;
import database.entity.OrderTaxiJsonFetcherService;
import database.entity.TaxiJsonFetcherService;

public class CallingTaxiActivity extends AppCompatActivity {
TextView textView2;
    public static final String  SHARED_NAME = "CallingTaxiActivity";
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            String message =
//                    intent.getStringExtra(MyService.MY_SERVICE_PAYLOAD);
            if(intent.getAction()!=null && intent.getAction()== OrderTaxiJsonFetcherService.ADD_NEW_ORDER_TAXI_MESSAGE  )
            {

                if(intent.getStringExtra(OrderTaxiJsonFetcherService.ADD_NEW_ORDER_TAXI_PAYLOAD)==null)
                {

                    Toast.makeText(CallingTaxiActivity.this,"يبدو ان اتصال الانترنت ضعيف.. اعد المحاولة لاحقا",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    textView2.setText("****تم ارسال التكسي من قبل مكتب التكسي***"+ "\n");
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling_taxi);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent= getIntent();

        textView2=findViewById(R.id.orderoutput);
      String  taxi_idStr = intent.getStringExtra("taxi_id");
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(OrderTaxiJsonFetcherService.ADD_NEW_ORDER_TAXI_MESSAGE));
        //////////////////////////////
        SharedPreferences pref=getSharedPreferences(AddNewClientActivity.SHARED_NAME,MODE_PRIVATE);
        String EndUse_idStr=pref.getString("EndUse_id","");
        textView2.setText("الرجاء الانتظار قليلا . ريثما يتم طلب التكسي");
        Intent intentorder = new Intent(this, OrderTaxiJsonFetcherService.class);
        intentorder.setAction(OrderTaxiJsonFetcherService.ADD_NEW_ORDER_TAXI_MESSAGE);
        String urltosend=OrderTaxiJsonFetcherService.ADD_NEW_ORDER_TAXI_JSON_URL+"fk_enduser_id="+EndUse_idStr+"&fk_taxi_id="+taxi_idStr;
        intentorder.setData(Uri.parse(urltosend));

        startService(intentorder);



    }

}
