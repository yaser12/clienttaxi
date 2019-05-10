package clienttaxi;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import clienttaxi.R;
/**
 * Created by dinakar.maurya on 23-01-2018.
 */

public class LocationsData {
    public LatLng location;
    public BitmapDescriptor bitmapDescriptor;
    public String title;

    /**
     * getting hard coded data, data source can be anything ie. network, db etc.
     *
     * @return
     */
    public static ArrayList<LocationsData> getData() {
        //hard coded data, can be change dynamically
        LatLng first = new LatLng(36.20124, 37.16117);
        LatLng second = new LatLng(36.5672, 37.3261);
        LatLng third = new LatLng(36.4649, 37.5113 );
        LatLng fourth = new LatLng(36.5665, 37.3406  );


        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.marker);
        BitmapDescriptor bitmapDescriptor2 = BitmapDescriptorFactory.fromResource(R.mipmap.taximarker);

        String title = "Fortis Hospital, Noida, Uttar Pradesh, India";
        String title2 = "The Great India Place Mall, Noida, Uttar Pradesh, India";
        String title3 = "Pari Chowk, NRI City, Greater Noida, Uttar Pradesh 201310, India";
        String title4 = "Arun Vihar, Sector 37, Noida, Uttar Pradesh 201303, India";

        ArrayList<LocationsData> datas = new ArrayList<>();

        LocationsData data = new LocationsData();
        data.location = first;
        data.bitmapDescriptor = bitmapDescriptor;
        data.title = title;
        datas.add(data);

        data = new LocationsData();
        data.location = second;
        data.bitmapDescriptor = bitmapDescriptor2;
        data.title = title2;
        datas.add(data);

        data = new LocationsData();
        data.location = third;
        data.bitmapDescriptor = bitmapDescriptor2;
        data.title = title3;
        datas.add(data);

        data = new LocationsData();
        data.location = fourth;
        data.bitmapDescriptor = bitmapDescriptor;
        data.title = title4;
        datas.add(data);

        return datas;
    }
}
