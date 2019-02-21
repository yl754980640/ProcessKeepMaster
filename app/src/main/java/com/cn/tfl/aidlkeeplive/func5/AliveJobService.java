package com.cn.tfl.aidlkeeplive.func5;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.cn.tfl.aidlkeeplive.MainActivity;

import java.util.List;



@TargetApi(21)
public class AliveJobService extends JobService {
    private final static String TAG = "KeepAliveService";  
    // 告知编译器，这个变量不能被优化  
    private volatile static Service mKeepAliveService = null;
   
    public static boolean isJobServiceAlive(){  
        return mKeepAliveService != null;  
    }  
   
    private static final int MESSAGE_ID_TASK = 0x01;  
   
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override 
        public boolean handleMessage(Message msg) {
            // 具体任务逻辑  
            if(isAppaLive(getApplicationContext(), "com.cn.tfl.aidlkeeplive")){
                Toast.makeText(getApplicationContext(), "APP活着的", Toast.LENGTH_SHORT).show();
            }else{  
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
                startActivity(intent);  
                Toast.makeText(getApplicationContext(), "APP被杀死，重启...", Toast.LENGTH_SHORT)  
                        .show();  
            }  
            // 通知系统任务执行结束  
            jobFinished( (JobParameters) msg.obj, false );
            return true;  
        }  
    });

    public boolean isAppaLive(Context context , String PackageName) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        boolean isAppRunning = false;
        //String MY_PKG_NAME = "你的包名";
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(PackageName)//如果想要手动输入的话可以str换成<span style="font-family: Arial, Helvetica, sans-serif;">MY_PKG_NAME，下面相同</span>
                    || info.baseActivity.getPackageName().equals(PackageName)) {
                isAppRunning = true;
                Log.i(TAG, info.topActivity.getPackageName()
                        + " info.baseActivity.getPackageName()="
                        + info.baseActivity.getPackageName());
                break;
            }
        }
        return isAppRunning;
    }


    @Override 
    public boolean onStartJob(JobParameters params) {  

            Log.d(TAG,"KeepAliveService----->JobService服务被启动...");
        mKeepAliveService = this;  
        // 返回false，系统假设这个方法返回时任务已经执行完毕；  
        // 返回true，系统假定这个任务正要被执行  
        Message msg = Message.obtain(mHandler, MESSAGE_ID_TASK, params);  
        mHandler.sendMessage(msg);  
        return true;  
    }  
   
    @Override 
    public boolean onStopJob(JobParameters params) {  
        mHandler.removeMessages(MESSAGE_ID_TASK);
            Log.d(TAG,"KeepAliveService----->JobService服务被关闭");  
        return false;  
    }  
}