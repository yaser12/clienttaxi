package clienttaxi;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import java.util.HashMap;

import database.entity.Office;
import database.entity.OfficeJsonFetcherService;
import utility.NetworkHelper;

public class SelectOffiecesActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener,GoogleMap.OnMapClickListener
{

    private static final int ACCESS_FINE_LOCATION_CODE = 120;
    private static final String TAG = MapsActivity.class.getSimpleName();
    private GoogleMap googleMap;


    private boolean networkOk;
    TextView output;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            String message =
//                    intent.getStringExtra(MyService.MY_SERVICE_PAYLOAD);
            if(intent.getAction()!=null && intent.getAction()== OfficeJsonFetcherService.GET_ALL_OFFICE_MESSAGE  )
            {

                if(OfficeJsonFetcherService.GET_ALL_OFFICE_PAYLOAD==null)
                {

                    Toast.makeText(SelectOffiecesActivity.this,"يبدو ان اتصال الانترنت ضعيف.. اعد المحاولة لاحقا",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Office[] dataItems = (Office[]) intent
                            .getParcelableArrayExtra(OfficeJsonFetcherService.GET_ALL_OFFICE_PAYLOAD);
                    if(dataItems==null)
                    {
                        Toast.makeText(SelectOffiecesActivity.this,"يبدو ان اتصال الانترنت ضعيف.. اعد المحاولة لاحقا",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        show_offices_on_map(dataItems);
                    }
                }
            }
        }
    };
    HashMap positoin_officeid=new HashMap();
    public void show_offices_on_map(Office[] dataItems ){
        googleMap.clear();
        // Creating a marker
        MarkerOptions marker=null;
        for (Office item : dataItems) {


            LatLng itemlatLng = new LatLng(  item.getLang(),item.getLat());
            marker = new MarkerOptions().position(itemlatLng).title(item.getOffice_name());
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.taximarker);
            marker.icon(bitmapDescriptor);
            positoin_officeid.put(marker.getPosition(),item.getOffice_id());
            // Setting the position for the marker



            // Setting the title for the marker.
            // This will be displayed on taping the marker
            // markerOptions.title(latLng.latitude + " : " + latLng.longitude);

            // Clears the previously touched position


            // Animating to the touched position
            // googleMap.animateCamera(CameraUpdateFactory.newLatLng(itemlatLng));

            // Placing a marker on the touched position
            googleMap.addMarker(marker);
        }
        float zoomLevel = (float) 7;
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(zoomLevel));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        output = (TextView) findViewById(R.id.output);
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(OfficeJsonFetcherService.GET_ALL_OFFICE_MESSAGE));

        networkOk = NetworkHelper.hasNetworkAccess(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
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

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case ACCESS_FINE_LOCATION_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "onRequestPermissionsResult fine location granted now...");
                }
            }
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        this.googleMap = googleMap;

//        drawSimpleLocation();
        drawMultipleLocation();
        googleMap.setOnMarkerClickListener(this);
        // Setting a click event handler for the map
        // googleMap.setOnMapClickListener(this);
    }

    /**
     * from google api
     */
    private void drawSimpleLocation() {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        LatLng sydney = new LatLng(-33.852, 151.211);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
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
                    // MarkerOptions marker = new MarkerOptions().position(data.location).title(data.title);
                    // marker.icon(data.bitmapDescriptor);
                    // googleMap.addMarker(marker);
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
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), durationMs, null);

                if (networkOk) {
                    Intent intent = new Intent(SelectOffiecesActivity.this, OfficeJsonFetcherService.class);
                    intent.setAction(OfficeJsonFetcherService.GET_ALL_OFFICE_MESSAGE);
                    intent.setData(Uri.parse(OfficeJsonFetcherService.GET_ALL_OFFICE_JSON_URL));
                    startService(intent);
                } else {
                    Toast.makeText(SelectOffiecesActivity.this, "Network not available!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void runClickHandler(View view) {

        if (networkOk) {
            Intent intent = new Intent(this, OfficeJsonFetcherService.class);
            intent.setAction(OfficeJsonFetcherService.GET_ALL_OFFICE_MESSAGE);
            intent.setData(Uri.parse(OfficeJsonFetcherService.GET_ALL_OFFICE_JSON_URL));
            startService(intent);
        } else {
            Toast.makeText(this, "Network not available!", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onMarkerClick(Marker marker)
    {
        Toast.makeText(this,""+marker.getTitle(),Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(SelectOffiecesActivity.this, SelectOneTaxiFromOfficeActivity.class);
        intent.putExtra("office_id", ""+ positoin_officeid.get(marker.getPosition()));

        startActivity(intent);
        return false;

    }

    @Override
    public void onMapClick(LatLng latLng) {
        // Creating a marker
        MarkerOptions marker = new MarkerOptions().title("new office");
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.marker);
        marker.icon(bitmapDescriptor);
        // Setting the position for the marker
        marker.position(latLng);

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
}
