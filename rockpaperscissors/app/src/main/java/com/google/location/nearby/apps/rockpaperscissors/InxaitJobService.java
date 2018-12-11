package com.google.location.nearby.apps.rockpaperscissors;

/**
 * Created by jamorc on 31/01/2018.
 */

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.Strategy;

/* JobService to be scheduled by com.example.inxait.Scheduler.
 * start Inxait service
 */
//implements  GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
public class InxaitJobService extends JobService {

    private static final String TAG = "RCALVOR";

    private static final int TTL_IN_SECONDS = 3 * 60; // Three minutes.

    private static final int REQUEST_READ_PHONE_STATE = 0;

    JobParameters params;


    private Message mPubMessage;


    private static final Strategy PUB_STRATEGY = new Strategy.Builder().setTtlSeconds(TTL_IN_SECONDS).setDistanceType(Strategy.DISTANCE_TYPE_EARSHOT).build();
    private GoogleApiClient mGoogleApiClient;


    public boolean onStartJob(JobParameters p) {
        Intent service = new Intent(this, ServiceC.class);
        getApplicationContext().startService(service);
        return false;
    }


    @Override
    public boolean onStopJob(JobParameters params) {

        return false;
    }



}
