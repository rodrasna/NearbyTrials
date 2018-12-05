package com.google.android.gms.nearby.messages.samples.nearbydevices;

import android.Manifest;
import android.app.IntentService;
import android.app.job.JobParameters;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.PublishCallback;
import com.google.android.gms.nearby.messages.PublishOptions;
import com.google.android.gms.nearby.messages.Strategy;

import java.util.UUID;

public class PublishService extends IntentService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "RCALVOR";

    private static final int TTL_IN_SECONDS = 3 * 60; // Three minutes.

    private static final int REQUEST_READ_PHONE_STATE = 0;

    JobParameters params;


    private Message mPubMessage;


    private static final Strategy PUB_STRATEGY = new Strategy.Builder().setTtlSeconds(TTL_IN_SECONDS).setDistanceType(Strategy.DISTANCE_TYPE_EARSHOT).build();
    private GoogleApiClient mGoogleApiClient;

    public PublishService() {
        super("PublishService");
    }

    public PublishService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Log.i(TAG, "Lazando servicio");
        requestGoogleApi();

    }

    /**
     * Publishes a message to nearby devices and updates the UI if the publication either fails or
     * TTLs.
     */
    private void requestGoogleApi() {
        Log.i(TAG, "Requesting google 1");
        //int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE);
        //if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
        //  ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        //} else {
        // Log.i(TAG, "IMEI: " + m.getDeviceId());
        //}
        //Log.i(TAG, "UUID: " + getUUID());
        //Log.i(TAG,"mPubMessage: "+mPubMessage.getContent());
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Requesting google NO perms");
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        } else {
            Log.i(TAG, "Requesting google 2");
            buildGoogleApiClient();
        }


    }

    private void publish(TelephonyManager m) {
        Log.i(TAG, "Publishing");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }else {
            mPubMessage = DeviceMessage.newNearbyMessage(m.getDeviceId());
            PublishOptions options = new PublishOptions.Builder()
                    .setStrategy(PUB_STRATEGY)
                    .setCallback(new PublishCallback() {
                        @Override
                        public void onExpired() {
                            super.onExpired();
                            Log.i(TAG, "No longer publishing");
                        }
                    }).build();


            Nearby.Messages.publish(mGoogleApiClient, mPubMessage, options)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            if (status.isSuccess()) {
                                Log.i(TAG, "Published successfully.");
                            } else {
                                Log.i(TAG, "Published UNsuccessfully.Message: "+status.getStatusMessage());
                            }
                        }
                    });

        }

    }

    private void runOnUiThread(Runnable runnable) {

    }

    /**
     * Creates a UUID and saves it to {@link SharedPreferences}. The UUID is added to the published
     * message to avoid it being undelivered due to de-duplication. See {@link DeviceMessage} for
     * details.
     */
    public static String getUUID() {

        String uuid = UUID.randomUUID().toString();

        return uuid;
    }

    private void buildGoogleApiClient() {
        if (mGoogleApiClient != null) {
            Log.i(TAG, "Requesting google null");
            return;
        }
        Log.i(TAG, "Requesting google not null");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Nearby.MESSAGES_API)
                .addConnectionCallbacks(this)
                .build();
        mGoogleApiClient.connect();
        Log.i(TAG, "Requesting google not null 2");
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "onConnected");
        TelephonyManager m = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        publish(m);




    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "onConnectionFailed");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "onConnectionFailed");

    }


}
