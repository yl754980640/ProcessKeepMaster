package com.cn.tfl.aidlkeeplive.func1;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class Helpers {
  private static JobService mJob;
  private static JobParameters mJobParams;
  public static final int JOB_ID = 0;
  public static final int JOB_OVERDIDE_DEADLINE = 1000;
  @TargetApi(Build.VERSION_CODES.M)
  public static void schedule(Context context) {
  Log.w(TAG, "Helpers schedule()");
  final JobScheduler scheduler = context.getSystemService(JobScheduler.class);
  final JobInfo.Builder builder = new JobInfo.Builder(  JOB_ID, new ComponentName(context, JobHandlerService.class));
  builder.setOverrideDeadline(JOB_OVERDIDE_DEADLINE);
  scheduler.schedule(builder.build());
  }
 
  @TargetApi(Build.VERSION_CODES.M)
  public static void cancelJob(Context context) {
  Log.w(TAG, "Helpers cancelJob()");
  final JobScheduler scheduler = context.getSystemService(JobScheduler.class);
  scheduler.cancel(JOB_ID);
  }
 
  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public static void jobFinished(Context context) {
  Log.w(TAG, "Helpers jobFinished()");
    if (mJob != null) {
      mJob.jobFinished(mJobParams, false);
    } else {
      Log.e(TAG, "这是一个新世界==jobFinished");
      Activity activity = (Activity) context;
      activity.finish();
    }
  }
 
  public static void enqueueJob() {
  Log.w(TAG, "Helpers enqueueJob()");
  }
 
  public static void doHardWork(JobService job, JobParameters params) {
  Log.w(TAG, "Helpers doHardWork()");
  mJob = job;
  mJobParams = params;
  }
}
