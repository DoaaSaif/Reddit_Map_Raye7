package com.raye7.reddit.Screens;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.raye7.reddit.Parser.DirectionsJSONParser;
import com.raye7.reddit.R;
import com.raye7.reddit.Server.HttpConnection;

import org.json.JSONObject;

import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class TripActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener, GoogleMap.OnMapClickListener {
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private CoordinatorLayout mMap_coordinator;
    private GoogleMap mMap;
    private LatLng destination;
    private Location origin;
    private FloatingActionButton mMap_fab;
    private TextView mMap_from_tv, mDate_time_tv, mMap_to_tv;
    private List<Marker> markersList;
    private Snackbar distanceSnackbar;
    String distance=null, duration=null;
    Snackbar snackbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_trip);
        bindViews();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        markersList = new ArrayList<>();

    }

    private void bindViews() {

        mMap_coordinator = (CoordinatorLayout) findViewById(R.id.map_coordinator);
        mMap_from_tv = (TextView) findViewById(R.id.map_from_tv);
        mMap_to_tv = (TextView) findViewById(R.id.map_to_tv);
        mMap_fab = (FloatingActionButton) findViewById(R.id.map_fab);

        mDate_time_tv = (TextView) findViewById(R.id.date_time_tv);

        mMap_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DisplayDistance();
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());


        mDate_time_tv.setText(getString(R.string.date) +"  " +currentDateTimeString);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
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

    private void handleNewLocation(Location location) {
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        addMark("I am here!", latLng, R.drawable.current_location);
        mMap_from_tv.setText(getAddress(latLng));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
          }

    private void addMark(String title, LatLng location, int image) {
        MarkerOptions options;
        if (image != 0)

            options = new MarkerOptions()
                    .position(location)
                    .title(title).icon(BitmapDescriptorFactory.fromResource(image));

        else
            options = new MarkerOptions()
                    .position(location)
                    .title(title);
        Marker marker = mMap.addMarker(options);

        markersList.add(marker);
    }


    private String getDirectionsUrl(Location origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.getLatitude() + "," + origin.getLongitude();

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (android.os.Build.VERSION.SDK_INT < 23) {
            origin = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (origin == null) {

                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            } else {
                handleNewLocation(origin);

            }
        } else {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                origin = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (origin == null) {

                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                } else {
                    handleNewLocation(origin);
                }
                return;
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mMap_from_tv.setText(getAddress(new LatLng(location.getLatitude(), location.getLongitude())));
        ;
        handleNewLocation(location);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);

    }

    @Override
    public void onMapClick(LatLng latLng) {
        destination = latLng;

        mMap_to_tv.setText(getAddress(latLng));
        mMap.clear();
        markersList.clear();

        addMark("destenation", latLng ,0);

        handleNewLocation(origin);
        // String url = getMapsApiDirectionsUrl(latLng, new LatLng(destination.latitude, destination.longitude));
        String url = getDirectionsUrl(origin, new LatLng(destination.latitude, destination.longitude));
        ReadTask downloadTask = new ReadTask();
        downloadTask.execute(url);

    }


    private class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    private class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            //  final String distance ;
            //final String duration ;


            if (result.size() < 1) {
                Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }


            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0 || point.containsKey("distance")) {    // Get distance from the list
                        distance = (String) point.get("distance");
                        continue;
                    } else if (j == 1 || point.containsKey("duration")) { // Get duration from the list
                        duration = (String) point.get("duration");
                        continue;
                    }
                    if (point.containsKey("lat") && point.containsKey("lng")) {
                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(4);
                lineOptions.color(Color.BLUE);

            }

            // Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
            DisplayDistance();
        }

    }


    private String getAddress(LatLng location) {
        Geocoder geocoder;
        String loc_address = null, loc_city = null, loc_country = null;
        List<Address> addressValue;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addressValue = geocoder.getFromLocation(location.latitude, location.longitude, 1);


            if (addressValue.size() > 0) {
                loc_address = addressValue.get(0).getAddressLine(0);
                loc_city = addressValue.get(0).getAddressLine(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(loc_address);
    }

    private void DisplayDistance() {

         snackbar = Snackbar
                .make(mMap_coordinator, "Message is deleted", Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        if(duration!= null && distance != null)
        textView.setText(duration + "  (" + distance + ")");
        textView.setTextColor(Color.WHITE);
        snackbar.show();

    }
}

