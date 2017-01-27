package appewtc.masterung.mylocation;

import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    private Button normalButton, satelliteButton, terrainButton, hybridButton;
    private LocationManager locationManager;
    private Criteria criteria;
    private double latADouble, lngADouble;
    private TextView latTextView, lngTextView;
    private boolean aBoolean = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_layout);

        //Setup
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);

        //Bind Widget
        normalButton = (Button) findViewById(R.id.button);
        satelliteButton = (Button) findViewById(R.id.button2);
        terrainButton = (Button) findViewById(R.id.button3);
        hybridButton = (Button) findViewById(R.id.button4);
        latTextView = (TextView) findViewById(R.id.textView2);
        lngTextView = (TextView) findViewById(R.id.textView3);

        //Button Controller
        normalButton.setOnClickListener(this);
        satelliteButton.setOnClickListener(this);
        terrainButton.setOnClickListener(this);
        hybridButton.setOnClickListener(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }   // Main Method

    private void myLoop() {

        myRunLocation();
        createMarker();


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (aBoolean) {
                    myLoop();
                }
            }
        }, 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        myRunLocation();
    }

    private void myRunLocation() {

        locationManager.removeUpdates(locationListener);


        Location networkLocation = myFindLocation(LocationManager.NETWORK_PROVIDER);
        if (networkLocation != null) {
            latADouble = networkLocation.getLatitude();
            lngADouble = networkLocation.getLongitude();
        }

        Location gpsLocation = myFindLocation(LocationManager.GPS_PROVIDER);
        if (gpsLocation != null) {
            latADouble = gpsLocation.getLatitude();
            lngADouble = gpsLocation.getLongitude();
        }

        try {

            latTextView.setText("Lat = " + Double.toString(latADouble));
            lngTextView.setText("Lng = " + Double.toString(lngADouble));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationListener);
    }

    public Location myFindLocation(String strProvider) {

        Location location = null;

        if (locationManager.isProviderEnabled(strProvider)) {

            locationManager.requestLocationUpdates(strProvider, 1000, 10, locationListener);
            location = locationManager.getLastKnownLocation(strProvider);

        }

        return location;
    }


    //Create Listener
    public LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            latADouble = location.getLatitude();
            lngADouble = location.getLongitude();

        }   // onLocationChanged

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        double douLatCenter = 13.672403;
        double douLngCenter = 100.607176;


        //Setup LatLng
        LatLng latLng = new LatLng(douLatCenter, douLngCenter);

        //Setup Center Map
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

        //Create Marker
        createMarker();

        myLoop();


        //Click OnMap
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                aBoolean = false;
                mMap.addMarker(new MarkerOptions().position(latLng));

            }
        });

        //Click Marker
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                aBoolean = false;
                marker.remove();

                return true;
            }
        });



    }   //  onMap

    private void createMarker() {

        // Marker #1-#4
        LatLng latLng1 = new LatLng(13.670693, 100.602841);
        LatLng latLng2 = new LatLng(13.678573, 100.605612);
        LatLng latLng3 = new LatLng(13.677168, 100.612428);
        LatLng latLng4 = new LatLng(13.668578, 100.609743);

        mMap.clear();

        try {

            mMap.addMarker(new MarkerOptions().position(new LatLng(latADouble, lngADouble))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.doremon48)));

        } catch (Exception e) {
            e.printStackTrace();
        }


        //Normal Marker
        mMap.addMarker(new MarkerOptions().position(latLng1));

        //Normal Marker Type Color
        mMap.addMarker(new MarkerOptions().position(latLng2)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));

        //Image Marker
        mMap.addMarker(new MarkerOptions().position(latLng3)
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.build1)));

        mMap.addMarker(new MarkerOptions().position(latLng4)
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.nobita48)));


        //Create PolyLine
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.add(latLng1)
                .add(latLng2)
                .add(latLng3)
                .add(latLng4)
                .add(latLng1)
                .width(5)
                .color(Color.MAGENTA);
        mMap.addPolyline(polylineOptions);

        //Create Polygon
        LatLng latLng5 = new LatLng(13.671360, 100.608368);
        LatLng latLng6 = new LatLng(13.673567, 100.602213);
        LatLng latLng7 = new LatLng(13.676778, 100.608947);

        PolygonOptions polygonOptions = new PolygonOptions();
        polygonOptions.add(latLng5)
                .add(latLng6)
                .add(latLng7)
                .add(latLng5)
                .strokeWidth(10)
                .strokeColor(Color.RED)
                .fillColor(Color.argb(50, 7, 250, 93));
        mMap.addPolygon(polygonOptions);



    }   // createMarker

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.button:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.button2:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.button3:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.button4:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            default:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
        }

    } // onClick

}   // Main Class
