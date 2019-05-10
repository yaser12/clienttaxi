package clienttaxi;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import clienttaxi.R;


import java.util.ArrayList;
import java.util.Random;

import androidbootstrap.BootstrapAlert;
import androidbootstrap.BootstrapProgressBar;
import androidbootstrap.api.defaults.DefaultBootstrapSize;
import database.entity.EndUserJsonFetcherService;
import utility.NetworkHelper;
import viewmodel.AddNewOfficeViewModel;


public class AddNewClientActivity extends AppCompatActivity  implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener,GoogleMap.OnMapClickListener {
    private static final String TAG = AddNewClientActivity.class.getSimpleName();
    public static final String  SHARED_NAME = "AddNewClientActivitysharedpreference";
    private Random random;
    private static final int ACCESS_FINE_LOCATION_CODE = 120;
    private GoogleMap googleMap;
    private AddNewOfficeViewModel mViewModel;
    BootstrapProgressBar sizeExample;
    BootstrapAlert signup_success_alert;
    BootstrapAlert network_not_available_alert;
    BootstrapAlert some_faild_empty_alert;
    // network_not_available_alert
    int number_trying= 1;
    private boolean networkOk;
    TextView output;
    EditText enduser_name,cleintuser_name,cleintpass,cleintlat,cleintlang,cleintphone;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            String message =
//                    intent.getStringExtra(MyService.MY_SERVICE_PAYLOAD);
            if(intent.getAction().equals(EndUserJsonFetcherService.ADD_NEW_EndUse_MESSAGE))
            {
                Log.v(TAG,"number_trying = "+number_trying);
                if(EndUserJsonFetcherService.ADD_NEW_EndUse_PAYLOAD==null && number_trying<10)
                {
                    Log.v(TAG,"try again");
                    add_me_as_cleint(); ;
                    number_trying++;
                }
                else {
                    Log.v(TAG,"try finish");
                    countDownTimer.cancel();
                    sizeExample.setVisibility(View.INVISIBLE);
                    if(
                            intent.getStringExtra(EndUserJsonFetcherService.ADD_NEW_EndUse_PAYLOAD)==null
                                    ||
                                    NetworkHelper.getValidint(intent.getStringExtra(EndUserJsonFetcherService.ADD_NEW_EndUse_PAYLOAD))<0
                                    ||
                                    intent.getStringExtra(EndUserJsonFetcherService.ADD_NEW_EndUse_PAYLOAD).equals("")
                    )
                    {
                        signup_success_alert.dismiss(true);
                        network_not_available_alert.show(true);

                    }
                    else
                    {
                        signup_success_alert.show(true);
                        network_not_available_alert.dismiss(true);
                        Log.d(TAG," EndUserJsonFetcherService.ADD_NEW_EndUse_PAYLOAD : "+intent.getStringExtra(EndUserJsonFetcherService.ADD_NEW_EndUse_PAYLOAD));
                        SharedPreferences.Editor editor=getSharedPreferences(SHARED_NAME,MODE_PRIVATE).edit();
                        editor.putString("cleintuser_name",cleintuser_name.getText().toString());
                        editor.putString("cleintpass",cleintpass.getText().toString());
                        editor.putString("EndUse_id",intent.getStringExtra(EndUserJsonFetcherService.ADD_NEW_EndUse_PAYLOAD));
                        editor.putString("cleintphone",cleintphone.getText().toString());

                        editor.putString("enduser_name",enduser_name.getText().toString());
                        editor.putString("cleintlat",cleintlat.getText().toString());
                        editor.putString("cleintlang",cleintlang.getText().toString());
                        editor.apply();
                        SharedPreferences pref=getSharedPreferences(SHARED_NAME,MODE_PRIVATE);
                        String EndUse_ids=pref.getString("EndUse_id","");
                        output.setText(context.getString((R.string.yourclientnumber))+ EndUse_ids + "\n");
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
        setContentView(R.layout.activity_add_new_client);
        output = (TextView) findViewById(R.id.contactclient_form_title);
        enduser_name = (EditText) findViewById(R.id.enduser_name);

        cleintuser_name = (EditText) findViewById(R.id.cleintuser_name);
        signup_success_alert=findViewById(R.id.signupcleint_success_alert);
        signup_success_alert.dismiss(true);
        sizeExample=findViewById(R.id.cleintexample_progress_default);
        sizeExample.setVisibility(View.INVISIBLE);
        DefaultBootstrapSize size = DefaultBootstrapSize.SM;

        size = DefaultBootstrapSize.MD;

        size = DefaultBootstrapSize.LG;
        size = DefaultBootstrapSize.XS;
        size = DefaultBootstrapSize.XL;


        sizeExample.setBootstrapSize(size);
        sizeExample.setProgress(randomProgress(10, 1000));
        some_faild_empty_alert=findViewById(R.id.someclient_faild_empty_alert);
        some_faild_empty_alert.dismiss(true);
        cleintpass= (EditText) findViewById(R.id.cleintpass);
        network_not_available_alert=findViewById(R.id.networkcleint_not_available_alert);
        cleintlat=findViewById(R.id.cleintlat);
        cleintlang=findViewById(R.id.cleintlang);
        cleintphone=findViewById(R.id.cleintphone);
        network_not_available_alert.dismiss(true);

        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(EndUserJsonFetcherService.ADD_NEW_EndUse_MESSAGE));
        networkOk = NetworkHelper.hasNetworkAccess(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.addnewclientmap);

        //  mapFragment.get
        mapFragment.getMapAsync(this);

        if (!Utils.isConnectedToInternet(this)) {
            Toast.makeText(this, "Please connect to internet!", Toast.LENGTH_LONG).show();
        }

        if (!Utils.isLocationServiceEnabled(this)) {
            Toast.makeText(this, "Please enable location services!", Toast.LENGTH_LONG).show();
        }
        boolean permissionGranted = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;

        if (permissionGranted) {
            Log.i(TAG, "permissionGranted fine location");
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_CODE);
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        ViewGroup.LayoutParams params = mapFragment.getView().getLayoutParams();
        params.height = (height*40/100);
        mapFragment.getView().setLayoutParams(params);
///////////////////////innerLinearLayout/////////////
        LinearLayout layoutinnerLinearLayout = findViewById(R.id.innerLinearLayout);
// Gets the layout params that will allow you to resize the layout
        ViewGroup.LayoutParams layoutinnerLinearLayoutparams = layoutinnerLinearLayout.getLayoutParams();
        layoutinnerLinearLayoutparams.height = (height*60/100);
        layoutinnerLinearLayout.setLayoutParams(layoutinnerLinearLayoutparams);
/////////////////////////////////NestedScrollView//////////////////////
        NestedScrollView innerNestedScrollView = findViewById(R.id.innerNestedScrollView);
// Gets the layout params that will allow you to resize the layout
        ViewGroup.LayoutParams layoutinnerNestedScrollView = innerNestedScrollView.getLayoutParams();
        layoutinnerNestedScrollView.height = (height*60/100);
        innerNestedScrollView.setLayoutParams(layoutinnerNestedScrollView);

        signup_success_alert.setVisibilityChangeListener(new BootstrapAlert.VisibilityChangeListener() {
            @Override
            public void onAlertDismissStarted(BootstrapAlert alert) {
                Log.d(TAG, "Started dismissing alert!");
            }

            @Override
            public void onAlertDismissCompletion(BootstrapAlert alert) {
                Log.d(TAG, "Finished dismissing alert!");
            }

            @Override
            public void onAlertAppearStarted(BootstrapAlert alert) {
                Log.d(TAG, "Started appearing alert!");
            }

            @Override
            public void onAlertAppearCompletion(BootstrapAlert alert) {
                Log.d(TAG, "Finished appearing alert!");
                // alert.dismiss(true);  alert.show(true);
            }
        });

        // countDownTimer.start();
        // countDownTimer.cancel();


    }

    public void add_me_as_cleint_office(View view)
    {

        if(check_empty_faild())
        {
            some_faild_empty_alert.dismiss(true);
            add_me_as_cleint();
        }
        else
        {
            some_faild_empty_alert.show(true);
        }
    }
    private  void add_me_as_cleint()
    {
        //  office_name=lattakia&lang=22.33&lat=33.44&office_username=ali&office_pass=lat2468
        networkOk = NetworkHelper.hasNetworkAccess(this);

        if (networkOk)
        {
            network_not_available_alert.dismiss(true);
            sizeExample.setVisibility(View.VISIBLE);
            countDownTimer.start();
            LocalBroadcastManager.getInstance(getApplicationContext())
                    .registerReceiver(mBroadcastReceiver,
                            new IntentFilter(EndUserJsonFetcherService.ADD_NEW_EndUse_MESSAGE));

            String enduser_namestr=null;
            String cleintlangStr=null;
            String cleintlatStr=null;
            String cleintpassStr=null;
            String cleintuser_nameStr=null;
            String cleintphoneStr=null;

            enduser_namestr= Uri.encode(enduser_name.getText().toString(), "UTF-8");
            cleintphoneStr= Uri.encode(cleintphone.getText().toString(), "UTF-8");
            cleintlangStr=Uri.encode(cleintlat.getText().toString(), "UTF-8");
            cleintlatStr=Uri.encode(cleintlang.getText().toString(), "UTF-8");
            cleintpassStr=Uri.encode(cleintpass.getText().toString(), "UTF-8");
            cleintuser_nameStr=Uri.encode(cleintuser_name.getText().toString(), "UTF-8");



            String urltosend=EndUserJsonFetcherService.ADD_NEW_EndUse_JSON_URL+"enduser_name="+enduser_namestr+"&lang="+cleintlangStr+"&lat="+cleintlatStr+"&user_name="+cleintuser_nameStr+"&pass="+cleintpassStr+"&phone="+cleintphoneStr;
            // enduser_name=ahmad&lang=44.5555&lat=55.6666&user_name=ali&pass=aassdd&phone=0998888
            Log.v(TAG,"urltosend = "+urltosend);
            Intent intent = new Intent(this, EndUserJsonFetcherService.class);
            intent.setAction(EndUserJsonFetcherService.ADD_NEW_EndUse_MESSAGE);
            intent.setData(Uri.parse(urltosend));
            number_trying=0;
            startService(intent);

        } else {
            // Toast.makeText(this, "Network not available!", Toast.LENGTH_SHORT).show();
            network_not_available_alert.show(true);
        }
    }
    public boolean check_empty_faild()
    {
        Log.v(TAG," name.getText().toString() = "+ enduser_name.getText().toString().trim());
        if(enduser_name.getText()==null ||  enduser_name.getText().toString().trim().equals( "") ) return false;
        if(cleintuser_name.getText()==null ||  cleintuser_name.getText().toString().trim().equals( "")  ) return false;
        if(cleintpass.getText()==null ||  cleintpass.getText().toString().trim() .equals( "") ) return false;
        if(cleintlat.getText()==null ||  cleintlat.getText().toString().trim().equals( "")  ) return false;
        if(cleintlang.getText()==null ||  cleintlang.getText().toString().trim().equals( "") ) return false;
        return  true;
    }
    CountDownTimer countDownTimer = new CountDownTimer(60 * 1000, 1000) {
        public void onTick(long millisUntilFinished) {
            if(sizeExample.getProgress()<200-50) {
                Log.d(TAG,"sizeExample.getProgress() : "+sizeExample.getProgress()+"/"+sizeExample.getMaxProgress());
                sizeExample.setProgress(Math.round(sizeExample.getProgress() + 50));
            }
        }
        public void onFinish() {
            sizeExample.setProgress( 200);

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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this. googleMap= googleMap;

//        drawSimpleLocation();

        googleMap.setOnMarkerClickListener(this);
        // Setting a click event handler for the map
        googleMap.setOnMapClickListener(this);

        drawMultipleLocation();

    }
    private void drawMultipleLocation() {

        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                // Create bounds that include all locations of the map
                LatLngBounds.Builder boundsBuilder = LatLngBounds.builder();
                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                ArrayList<LocationsData> locationsDatas = LocationsData.getData();
                for (LocationsData data : locationsDatas) {
                    //  MarkerOptions marker = new MarkerOptions().position(data.location).title(data.title);
                    // marker.icon(data.bitmapDescriptor);
                    //googleMap.addMarker(marker);
                    //
                    builder.include(data.location);
                }
                LatLngBounds bounds = builder.build();
                int padding = 150;
                //  Returns a CameraUpdate that transforms the camera such that the specified latitude/longitude
                // bounds are centered on screen at the greatest possible zoom level.
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                googleMap.moveCamera(cu);
                int durationMs = 3 * 1000;
                // Moves the map according to the update with an animation over a specified duration,
                // and calls an optional callback on completion
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(8), durationMs, null);
            }
        });

    }
    @Override
    public void onMapClick(LatLng latLng) {

        // Creating a marker
        MarkerOptions marker = new MarkerOptions().title("new office");
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.marker);
        marker.icon(bitmapDescriptor);
        // Setting the position for the marker

        marker.position(latLng);
        cleintlat.setText(latLng.latitude+"");
        cleintlang.setText(latLng.longitude+"");
        // Setting the title for the marker.
        // This will be displayed on taping the marker
        // markerOptions.title(latLng.latitude + " : " + latLng.longitude);

        // Clears the previously touched position
        googleMap.clear();

        // Animating to the touched position
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

        // Placing a marker on the touched position
        googleMap.addMarker(marker);

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
