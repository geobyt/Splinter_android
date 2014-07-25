package com.splinter.app;

import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.splinter.app.Database.Coordinate;
import com.splinter.app.Database.DBAdapter;
import com.splinter.app.Service.JsonParser;
import com.splinter.app.Service.WebServiceListener;
import com.splinter.app.Service.WebServiceTask;
import com.google.android.gms.location.LocationListener;

import android.widget.Toast;

import java.util.List;

public class MainActivity
        extends ActionBarActivity
        implements WebServiceListener,
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        LocationListener {

    static final int REQUEST_CODE_RECOVER_PLAY_SERVICES = 1001;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    // Milliseconds per second
    private static final int MILLISECONDS_PER_SECOND = 1000;
    // Update frequency in seconds
    public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
    // Update frequency in milliseconds
    private static final long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    // The fastest update frequency, in seconds
    private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
    // A fast frequency ceiling in milliseconds
    private static final long FASTEST_INTERVAL = MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;

    private DBAdapter mDbHelper;
    WebServiceTask webServiceTask = new WebServiceTask();
    private GoogleMap mMap;

    Location mCurrentLocation; //location of the client
    LocationClient mLocationClient;
    LocationRequest mLocationRequest;
    boolean mUpdatesRequested;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.LoadCoordinates();

        webServiceTask.delegate = this;

        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create();
        // Use high accuracy
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Set the update interval to 5 seconds
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        // Set the fastest update interval to 1 second
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        mLocationClient = new LocationClient(this, this, this);

        mUpdatesRequested = true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        mLocationClient.connect();
    }

    @Override
    protected void onStop() {
        if (mLocationClient.isConnected()) {
            mLocationClient.removeLocationUpdates(this);
        }
        mLocationClient.disconnect();
        super.onStop();
    }

    @Override
    public void onLocationChanged(Location location) {
        // Report to the UI that the location was updated
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

        this.MoveToCoordinate(new LatLng(location.getLatitude(), location.getLongitude()));
        this.DrawLocationOnMap(new LatLng(location.getLatitude(), location.getLongitude()),
                BitmapDescriptorFactory.fromResource(R.drawable.map_marker_red),
                "my location");

        //get current location only once
        if (mLocationClient.isConnected()) {
            mLocationClient.removeLocationUpdates(this);
        }
    }

    @Override
    public void onConnected(Bundle dataBundle) {
        // Display the connection status
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        // If already requested, start periodic updates
        if (mUpdatesRequested) {
            mLocationClient.requestLocationUpdates(mLocationRequest, this);
        }
    }

    @Override
    public void onDisconnected() {
        // Display the connection status
        Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void LoadCoordinates(){
        //open sql database
        try {
            mDbHelper = new DBAdapter(this);
            mDbHelper.open();
        }
        catch (Exception ex){
            Log.w("MainActivity", ex.getMessage());
            return;
        }

        //retrieve last edited quote
        List<Coordinate> coordinates = mDbHelper.fetchAllCoordinates();

        if (coordinates.size() > 0){
            //display coordinates on the map
            //TODO: check for freshness, and if necessary poll service and update

        }
        else{
            //poll service for coordinates
            this.webServiceTask.execute("http://lyraserver.azurewebsites.net/locations");
        }
    }

    public void response(String result){
        //TODO: actually update database here
        if (!result.isEmpty()) {
            List<Coordinate> coordinates = JsonParser.ParseJson(result);

            if (coordinates != null){
                for (Coordinate c : coordinates){
                    this.DrawLocationOnMap(new LatLng(Double.parseDouble(c.getLatitude()), Double.parseDouble(c.getLongitude())),
                            BitmapDescriptorFactory.fromResource(R.drawable.map_marker_blue),
                            c.getDescription());
                }
            }
        }
    }

    void MoveToCoordinate(LatLng location){
        CameraPosition myCoordinate =
                new CameraPosition.Builder().
                        target(location).
                        zoom(15.5f).
                        build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(myCoordinate));
    }

    void DrawLocationOnMap(LatLng location, BitmapDescriptor image, String title){
        /*CircleOptions circleOptions = new CircleOptions()
                .center(location)
                .fillColor(color)
                .radius(size); // In meters

        Circle circle = mMap.addCircle(circleOptions);*/

        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(location)
                .title(title)
                .icon(image));
    }
}
