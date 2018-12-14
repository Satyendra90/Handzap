package com.handzapassignment.utils;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Places;

public class AppLocationManager implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private final String TAG = AppLocationManager.class.getSimpleName();
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingRequest;
    private GoogleApiClient mGoogleApiClient;
    private boolean mRequestingLocationUpdates;
    private boolean mDeviceLocationChecked;
    private Context mContext;
    private Location mLastLocation;
    private AppLocationChangeListener mLocationChangeListener;
    public static final int REQUEST_CHECK_SETTINGS = 101;

    public interface AppLocationChangeListener{
        void onLocationChanged(Location location);
        void onGpsConnected(Location location);
    }

    public AppLocationManager(Context context){
        this.mContext = context;
    }

    public void init(){
        buildGoogleApiClient();
        createLocationRequest();
        buildLocationSettingsRequest();
    }

    public void setLocationChangeListener(AppLocationChangeListener locationChangeListener){
        mLocationChangeListener = locationChangeListener;
    }

    public LocationRequest getLocationRequest() {
        return mLocationRequest;
    }

    public LocationSettingsRequest getLocationSettingRequest() {
        return mLocationSettingRequest;
    }

    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    public boolean isRequestingLocationUpdates() {
        return mRequestingLocationUpdates;
    }

    public boolean isDeviceLocationChecked() {
        return mDeviceLocationChecked;
    }

    private synchronized void buildGoogleApiClient() {
        Log.d(TAG, "Inside buildGoogleApiClient() method");
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .build();
        Log.d(TAG,  "Outside buildGoogleApiClient() method");
    }

    private void createLocationRequest(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient
        mLocationSettingRequest = builder.build();
    }

    public void checkLocationSettings() {

        Log.d(TAG,  "Inside checkLocationSettings() method");
        mDeviceLocationChecked = true;
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, mLocationSettingRequest);
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        Log.d(TAG,  "Location is enabled.");
                        if (mGoogleApiClient.isConnected() && !mRequestingLocationUpdates) {
                            startLocationUpdates();
                        }
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        Log.d(TAG,  "Location is not enabled, need to show alert dialog.");
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult((Activity) mContext, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
        Log.d(TAG,  "Outside checkLocationSettings() method");
    }

    public void onStart(){
        if(mGoogleApiClient != null && !mGoogleApiClient.isConnected()) mGoogleApiClient.connect();
    }

    public void onStop(){
        stopLocationUpdates();
        if(mGoogleApiClient != null && mGoogleApiClient.isConnected()) mGoogleApiClient.disconnect();
    }

    public void onResume(){
        if(mGoogleApiClient != null && mGoogleApiClient.isConnected()) startLocationUpdates();
    }

    public void startLocationUpdates() {
        Log.d(TAG,  "Inside startLocationUpdates() method");
        try{
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(Status status) {
                    mRequestingLocationUpdates = true;
                }
            });
        }
        catch(SecurityException ex){
            ex.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Log.d(TAG,  "Outside startLocationUpdates() method");
    }

    public void stopLocationUpdates() {
        Log.d(TAG,  "Inside stopLocationUpdates() method");
        try{
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(Status status) {
                    mRequestingLocationUpdates = false;
                }
            });
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        Log.d(TAG,  "Outside stopLocationUpdates() method");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG,  "Inside onConnected() method");
        try{
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }
        catch (SecurityException ex){
            ex.printStackTrace();
        }
        if(mLastLocation != null){
            mLocationChangeListener.onGpsConnected(mLastLocation);
            Log.i(TAG,  "Latitude : " + String.valueOf(mLastLocation.getLatitude()) + " Longitude : " + String.valueOf(mLastLocation.getLongitude()));
        }
        else{
            Log.d(TAG,  "Last location is null");
        }
        Log.d(TAG,  "Outside onConnected() method");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if(mLocationChangeListener != null){
            mLocationChangeListener.onLocationChanged(location);
        }
    }
}
