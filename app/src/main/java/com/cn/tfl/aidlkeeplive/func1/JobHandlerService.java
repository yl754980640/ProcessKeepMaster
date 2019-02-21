package com.cn.tfl.aidlkeeplive.func1;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

//todo 与JobHandleService3相比就onStartJob()不同
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class JobHandlerService extends JobService {
    private static final String TAG = "JobHandlerService";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "这是一个新世界==onStartCommand==111" );
        scheduleJob(getJobInfo());
        return START_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.e(TAG, "这是一个新世界==onStartJob==111" );
        Toast.makeText(this, "保活服务启动", Toast.LENGTH_SHORT).show();
       boolean isServiceRunning =  isServiceRunning("com.cn.tfl.aidlkeeplive.func1.MyService");
        Log.e(TAG, "这是一个新世界==isServiceRunning=" +isServiceRunning);
        if (!isServiceRunning("com.cn.tfl.aidlkeeplive.func1.MyService")) {
            startService(new Intent(this, MyService.class));
        }
        return true;
    }

    //将任务作业发送到作业调度中去
    public void scheduleJob(JobInfo t) {
        JobScheduler tm =
                (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        tm.schedule(t);
    }

    public JobInfo getJobInfo() {
        JobInfo.Builder builder = new JobInfo.Builder(1, new ComponentName(this, JobHandlerService.class));
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setPersisted(true);
        builder.setRequiresCharging(false);
        builder.setRequiresDeviceIdle(false);
        //间隔1000毫秒
        builder.setPeriodic(1000);
        return builder.build();
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        scheduleJob(getJobInfo());
        return true;
    }

    // 服务是否运行
    public boolean isServiceRunning(String serviceName) {
        boolean isRunning = false;
        ActivityManager am = (ActivityManager) this
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> lists = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : lists) {// 获取运行服务再启动
            System.out.println(info.processName);
            if (info.processName.equals(serviceName)) {
                isRunning = true;
            }
        }
        return isRunning;
    }
}
