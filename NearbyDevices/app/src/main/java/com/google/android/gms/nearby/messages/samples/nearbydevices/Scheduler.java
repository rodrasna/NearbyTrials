package com.google.android.gms.nearby.messages.samples.nearbydevices;

import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

/**
 * Created by jamorc on 31/01/2018.
 */

/**
 * Clase que implementa las funciones necesarias para implementar un scheduled job.
 */
public class Scheduler {
    private static final long MIN_INTERVAL = 1 * 60 * 1000;
    private static final String TAG = Scheduler.class.getName();
    private static final int JOB_ID=0;

    private long intervalSec;
    private Activity mcontext;

    public Scheduler(long intervalSec,
                      Activity mcontext) {
        this.intervalSec = intervalSec;
        this.mcontext = mcontext;
    }

    public void scheduleJob (){

        long interval = (long) (intervalSec * 60 * 1000);
        Log.d(TAG, "Executing Job Service Scheduler");
        ComponentName serviceComponent = new ComponentName(mcontext, InxaitJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, serviceComponent);
        builder.setPersisted(true); // Job Persist across device rebooting.
        if(interval >= MIN_INTERVAL){
            builder.setPeriodic(interval);
        } else {
            builder.setPeriodic(MIN_INTERVAL);
        }
        JobScheduler jobScheduler = (JobScheduler) mcontext.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (jobScheduler != null) {
            jobScheduler.schedule(builder.build());
        }
    }
    public static boolean isJobServiceOn(Activity mcontext) {
        JobScheduler scheduler = (JobScheduler) mcontext.getSystemService( Context.JOB_SCHEDULER_SERVICE ) ;
        boolean hasBeenScheduled = false ;
        for ( JobInfo jobInfo : scheduler.getAllPendingJobs() ) {
            if ( jobInfo.getId() == JOB_ID ) {
                hasBeenScheduled = true ;
                break ;
            }
        }
        return hasBeenScheduled ;
    }

    public static void stopJob(Activity mcontext){
        JobScheduler jobScheduler = (JobScheduler) mcontext.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.cancel(JOB_ID);
    }


}

