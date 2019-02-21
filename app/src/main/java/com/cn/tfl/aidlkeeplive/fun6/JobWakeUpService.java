package com.cn.tfl.aidlkeeplive.fun6;

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

import com.cn.tfl.aidlkeeplive.fun2_3_4.LocalService;
import com.cn.tfl.aidlkeeplive.fun2_3_4.RemoteService;

import java.util.List;



/**
 * 用于判断Service是否被杀死
 * Created by db on 2018/1/11.
 * https://blog.csdn.net/qq_38520096/article/details/79007228
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)//5.0以后可用
public class JobWakeUpService extends JobService {
    JobScheduler mJobScheduler = null;
    private int JobWakeUpId = 1;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        //开启轮寻
//        JobInfo.Builder mJobBulider = new JobInfo.Builder(JobWakeUpId, new ComponentName(this, JobWakeUpService.class));
//        //设置轮寻时间
//        mJobBulider.setPeriodic(5000);
//        JobScheduler mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
//        mJobScheduler.schedule(mJobBulider.build());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
            JobInfo.Builder builder = new JobInfo.Builder(startId++, new ComponentName(getPackageName(), JobWakeUpService.class.getName()));
            builder.setPeriodic(5000); //每隔5秒运行一次 builder.setRequiresCharging(true); builder.setPersisted(true); //设置设备重启后，是否重新执行任务 builder.setRequiresDeviceIdle(true);
            if (mJobScheduler.schedule(builder.build()) <= 0) { //If something goes wrong System.out.println("工作失败");
            } else {
                System.out.println("工作成功");
            }
        }

        return START_STICKY;

    }
 
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        //开启定时任务 定时轮寻 判断应用Service是否被杀死
        //如果被杀死则重启Service
//        boolean messageServiceAlive = serviceAlive(StepService.class.getName());
//        if(!messageServiceAlive){
//            startService(new Intent(this,StepService.class));
//        }
        return false;
    }
 
    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if (!isServiceRunning(this, "com.cn.tfl.aidlkeeplive.fun2_3_4.LocalService") || !isServiceRunning(this, "com.cn.tfl.aidlkeeplive.fun2_3_4.RemoteService")) {
            startService(new Intent(this, LocalService.class));
            startService(new Intent(this, RemoteService.class));
        }
        return false;
    }

    // 服务是否运行
    public boolean isServiceRunning(Context context, String serviceName) {
        boolean isRunning = false;
      ActivityManager am = (ActivityManager) this .getSystemService(Context.ACTIVITY_SERVICE);
    List<ActivityManager.RunningAppProcessInfo> lists = am.getRunningAppProcesses();
    for (ActivityManager.RunningAppProcessInfo info : lists) {// 获取运行服务再启动
        // System.out.println(info.processName);
        if (info.processName.equals(serviceName)) {
            isRunning = true;
        }
    }
    return isRunning;
}


 
    /**
     * 判断某个服务是否正在运行的方法
     * @param serviceName
     *            是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    private boolean serviceAlive(String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(100);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }
}
