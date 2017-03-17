package com.alpharun.jack.alpharun.Activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alpharun.jack.alpharun.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import static com.alpharun.jack.alpharun.Utilities.RunCalculations.distFrom;

public class RunActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    protected static String TAG = "RunActivity";




    //Variables for controlling how fast the location update intervals are
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;



    //TextViews for showing location
    protected TextView mLatitudeText;
    protected TextView mLongitudeText;
    protected TextView mDistanceText;
    protected Boolean mRequestingLocationUpdates;

    //Strings
    protected String mLatitudeLabel = "Latitude: ";
    protected String mLongitudeLabel = "Longtitude";
    protected String mLastUpdateTime;

    //Location variables
    protected Location mCurrentLocation;
    protected Location mLastLocation;
    protected ArrayList<Location> coordinates = new ArrayList<Location>();


    //The distance the user has run.
    protected double runDistance;

    //The time the run was started
    private long timeStart;

    //GoogleApiClient object to be used
    protected GoogleApiClient mGoogleApiClient;

    //LocationRequest used by FusedAPI in order to get coordinate updates at intervals
    protected LocationRequest mLocationRequest = new LocationRequest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);

        runDistance = 0;
        //Set the time the run was started
        timeStart = System.currentTimeMillis()/1000;
        Log.e("Run Start time: ", Long.toString(timeStart));

        //Get the textview objects to enter run details.
        mLatitudeText = (TextView) findViewById((R.id.latitude_text));
        mLongitudeText = (TextView) findViewById((R.id.longtitude_text));
        mDistanceText = (TextView) findViewById((R.id.distance_text));

        //true to automatically start recording data, false to wait for a callback (button press for example)
        mRequestingLocationUpdates = true;



        // Create an instance of GoogleAPIClient.
        buildGoogleApiClient();
    }

    //Builds the Google API client instance
    private void buildGoogleApiClient(){
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        createLocationRequest();
    }

    //When a location update occurs we need to update the textview (UI) fields with the information
    private void updateUI(){
        Log.e(TAG, "UpdateUI called");
        mLatitudeText.setText(String.format("%s: %f", mLatitudeLabel,
                mCurrentLocation.getLatitude()));
        mLongitudeText.setText(String.format("%s: %f", mLongitudeLabel,
                mCurrentLocation.getLongitude()));
        mDistanceText.setText(String.format("%s: %4.2f", "Distance: ",
                runDistance));
    }



    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        //Generally if we want to do something in the future once the API has connected we could do it here.
        //Right now we wait until the user presses the button before we actually get the lcoation so
        //it's not neccessary to do anything here.

        Log.i(TAG, "Connected to GoogleApiClient");

        // If the initial location was never previously requested, we use
        // FusedLocationApi.getLastLocation() to get it. If it was previously requested, we store
        // its value in the Bundle and check for it in onCreate(). We
        // do not request it again unless the user specifically requests location updates by pressing
        // the Start Updates button.
        //
        // Because we cache the value of the initial location in the Bundle, it means that if the
        // user launches the activity,
        // moves to a new location, and then changes the device orientation, the original location
        // is displayed as the activity is re-created.
//        if (mCurrentLocation == null) {
//            Log.e(TAG, "onConnected");
//            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//            updateUI();
//        }

        // If the user presses the Start Updates button before GoogleApiClient connects, we set
        // mRequestingLocationUpdates to true (see startUpdatesButtonHandler()). Here, we check
        // the value of mRequestingLocationUpdates and if it is true, we start location updates.
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    //This method is used to create the location request object which we will use to get updates at regular intervals
    //(Used for tracking during running)
    protected void createLocationRequest() {
        Log.e(TAG, "Creating Location Request...");
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    @Override
    public void onLocationChanged(Location location) {
        Log.e(TAG, "onlocationchanged");
        mLastLocation = mCurrentLocation;
        mCurrentLocation = location;

        //add the location to the list
        coordinates.add(location);
        if (mLastLocation != null){
            //Get the distance between the two points

            //TODO: (Improve Accuracy) In order to get the most accuracy, have to put some measure in that checks if the points are too far apart.
            double d = distFrom(mLastLocation, mCurrentLocation);
            Log.e(TAG, "Distance = " + d);
            Log.e(TAG, "Location Accuracy: " + location.getAccuracy());
            runDistance = runDistance + d;
            Log.e(TAG, "Run Distance = " + runDistance);
        }

        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

        Toast.makeText(this, "Location changed",
                Toast.LENGTH_SHORT).show();
        updateUI();
    }

    protected void startLocationUpdates() {
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        Log.e(TAG, "Starting Location Updates...");
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }




    //This method is called when the run is finished. Run should be saved to SQLite entry
    //and information passed to next intent
    public void stopRunButtonCallback(View view){
        //Stop the updates
        stopLocationUpdates();

        //TODO: Pass information to SQLite server

        //Pass information to the summary screen (could also get the summary screen to read last
        //entry to the database
        Intent intent = new Intent(this, RunSummaryActivity.class);
        intent.putExtra("RUN_DISTANCE", runDistance);
        intent.putExtra("COORD_LIST", coordinates);
        intent.putExtra("START_TIME", timeStart);
        intent.putExtra("END_TIME", System.currentTimeMillis()/1000);
        startActivity(intent);
        finish();
    }

    private void stopLocationUpdates(){
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

}
