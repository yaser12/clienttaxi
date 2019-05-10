package clienttaxi;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import butterknife.BindView;
import database.entity.OfficeJsonFetcherService;
import utility.NetworkHelper;
import viewmodel.AddNewOfficeViewModel;

public class AddNewOfficeActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener,GoogleMap.OnMapClickListener
{
    private Random random;
    private static final int ACCESS_FINE_LOCATION_CODE = 120;
    private static final String TAG = AddNewOfficeActivity.class.getSimpleName();
    public static final String  SHARED_NAME = "AddNewOfficeActivitysharedpreference";

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
    EditText name,office_username,office_pass,latitude,longitude;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            String message =
//                    intent.getStringExtra(MyService.MY_SERVICE_PAYLOAD);
            if(intent.getAction().equals(OfficeJsonFetcherService.ADD_NEW_OFFICE_MESSAGE))
            {
                Log.v(TAG,"number_trying = "+number_trying);
                    if(OfficeJsonFetcherService.ADD_NEW_OFFICE_PAYLOAD==null && number_trying<10)
                    {
                        Log.v(TAG,"try again");
                        add_me_as_taxi_office() ;
                        number_trying++;
                    }
                    else {
                        Log.v(TAG,"try finish");
                        countDownTimer.cancel();
                        sizeExample.setVisibility(View.INVISIBLE);
                        if(
                          intent.getStringExtra(OfficeJsonFetcherService.ADD_NEW_OFFICE_PAYLOAD)==null
                                ||
                                  NetworkHelper.getValidint(intent.getStringExtra(OfficeJsonFetcherService.ADD_NEW_OFFICE_PAYLOAD))<0
                                  ||
                                intent.getStringExtra(OfficeJsonFetcherService.ADD_NEW_OFFICE_PAYLOAD).equals("")
                        )
                        {
                            signup_success_alert.dismiss(true);
                            network_not_available_alert.show(true);

                        }
                        else
                        {
                            signup_success_alert.show(true);
                            network_not_available_alert.dismiss(true);

                            SharedPreferences.Editor editor=getSharedPreferences(SHARED_NAME,MODE_PRIVATE).edit();
                            editor.putString("office_username",office_username.getText().toString());
                            editor.putString("office_pass",office_pass.getText().toString());
                            editor.putString("office_id",intent.getStringExtra(OfficeJsonFetcherService.ADD_NEW_OFFICE_PAYLOAD));
                            editor.putString("office_name",name.getText().toString());
                            editor.putString("latitude",latitude.getText().toString());
                            editor.putString("longitude",latitude.getText().toString());
                            editor.apply();
                            SharedPreferences pref=getSharedPreferences(SHARED_NAME,MODE_PRIVATE);
                            String office_idss=pref.getString("office_id","");
                            output.setText(context.getString(R.string.youroffinenumber_is)+ office_idss + "\n");
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
        setContentView(R.layout.activity_add_new_office);
        output = (TextView) findViewById(R.id.contact_form_title);
        name = (EditText) findViewById(R.id.name);

        office_username = (EditText) findViewById(R.id.office_username);
        signup_success_alert=findViewById(R.id.signup_success_alert);
        signup_success_alert.dismiss(true);
        sizeExample=findViewById(R.id.example_progress_default);
        sizeExample.setVisibility(View.INVISIBLE);
        DefaultBootstrapSize size = DefaultBootstrapSize.SM;

        size = DefaultBootstrapSize.MD;

        size = DefaultBootstrapSize.LG;
        size = DefaultBootstrapSize.XS;
        size = DefaultBootstrapSize.XL;


        sizeExample.setBootstrapSize(size);
        sizeExample.setProgress(randomProgress(10, 1000));
        some_faild_empty_alert=findViewById(R.id.some_faild_empty_alert);
        some_faild_empty_alert.dismiss(true);
        office_pass= (EditText) findViewById(R.id.office_pass);
        network_not_available_alert=findViewById(R.id.network_not_available_alert);
        latitude=findViewById(R.id.latitude);
        longitude=findViewById(R.id.longitude);
        network_not_available_alert.dismiss(true);

        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(OfficeJsonFetcherService.ADD_NEW_OFFICE_MESSAGE));
        networkOk = NetworkHelper.hasNetworkAccess(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.addnewofficemap);

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
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(this,""+marker.getTitle(),Toast.LENGTH_SHORT).show();
        return false;

    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this)
                .get(AddNewOfficeViewModel.class);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        // Creating a marker
        MarkerOptions marker = new MarkerOptions().title("new office");
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.marker);
        marker.icon(bitmapDescriptor);
        // Setting the position for the marker
        marker.position(latLng);
        latitude.setText(latLng.latitude+"");
        longitude.setText(latLng.longitude+"");
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
private  void add_me_as_taxi_office()
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
                        new IntentFilter(OfficeJsonFetcherService.ADD_NEW_OFFICE_MESSAGE));
        String office_namestr=null;
        String langStr=null;
        String latStr=null;
        String office_passStr=null;
        String office_usernameStr=null;

            office_namestr= Uri.encode(name.getText().toString(), "UTF-8");
            langStr=Uri.encode(latitude.getText().toString(), "UTF-8");
            latStr=Uri.encode(longitude.getText().toString(), "UTF-8");
            office_passStr=Uri.encode(office_pass.getText().toString(), "UTF-8");
            office_usernameStr=Uri.encode(office_username.getText().toString(), "UTF-8");



        String urltosend=OfficeJsonFetcherService.ADD_NEW_OFFICE_JSON_URL+"office_name="+office_namestr+"&lang="+langStr+"&lat="+latStr+"&office_pass="+office_passStr+"&office_username="+office_usernameStr;
        Log.v(TAG,"urltosend = "+urltosend);
        Intent intent = new Intent(this, OfficeJsonFetcherService.class);
        intent.setAction(OfficeJsonFetcherService.ADD_NEW_OFFICE_MESSAGE);
        intent.setData(Uri.parse(urltosend));
        number_trying=0;
        startService(intent);

    } else {
       // Toast.makeText(this, "Network not available!", Toast.LENGTH_SHORT).show();
        network_not_available_alert.show(true);
    }
}

    public void add_me_as_taxi_office(View view)
    {
        if(check_empty_faild())
        {
            some_faild_empty_alert.dismiss(true);
            add_me_as_taxi_office();
        }
        else
        {

            some_faild_empty_alert.show(true);
        }

    }
    public boolean check_empty_faild()
    {
        Log.v(TAG," name.getText().toString() = "+ name.getText().toString().trim());
        if(name.getText()==null ||  name.getText().toString().trim().equals( "") ) return false;
        if(office_username.getText()==null ||  office_username.getText().toString().trim().equals( "")  ) return false;
        if(office_pass.getText()==null ||  office_pass.getText().toString().trim() .equals( "") ) return false;
        if(latitude.getText()==null ||  latitude.getText().toString().trim().equals( "")  ) return false;
        if(longitude.getText()==null ||  longitude.getText().toString().trim().equals( "") ) return false;
        return  true;
    }

}
