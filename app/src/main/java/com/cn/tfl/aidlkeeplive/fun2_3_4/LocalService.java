package com.cn.tfl.aidlkeeplive.fun2_3_4;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.cn.tfl.aidlkeeplive.IProcessService;
import com.cn.tfl.aidlkeeplive.R;

//todo  
public class LocalService extends Service {

    private static final String TAG = "LocalService";

    private ServiceBinder serviceBinder;

    private MyServiceConnection myServiceConnection;


    @Override
    public void onCreate() {
        serviceBinder = new ServiceBinder();
        if (myServiceConnection == null) {
            myServiceConnection = new MyServiceConnection();
        }
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return serviceBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        bindService(new Intent(this, RemoteService.class), myServiceConnection, Context.BIND_IMPORTANT);
//        Intent intent1 = new Intent();
//        intent1.setClass(this,com.cn.tfl.aidlkeeplive.RemoteService.class);
//        intent1.setAction("com.cn.tfl.aidlkeeplive.RemoteService");
//        intent1.setPackage("com.cn.tfl.aidlkeeplive");
//        bindService(intent1, conn, BIND_AUTO_CREATE);

        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setTicker("双进程守护服务");
        Notification notification = builder.build();
        // 设置service为前台进程，避免手机休眠时系统自动杀掉该服务
        startForeground(startId, notification);
        return START_STICKY;
    }

    class ServiceBinder extends IProcessService.Stub {
        @Override
        public String getServiceName() throws RemoteException {
            return "本地服务";
        }
    }

    class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            IProcessService processService = IProcessService.Stub.asInterface(iBinder);
            try {
                Log.d(TAG, "连接" + processService.getServiceName() + "成功");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(TAG, "远程服务被干死了。。");
            startService(new Intent(LocalService.this, RemoteService.class));
            bindService(new Intent(LocalService.this, RemoteService.class), myServiceConnection, Context.BIND_IMPORTANT);
        }
    }

    @Override
    public void onDestroy() {
        unbindService(myServiceConnection);
        super.onDestroy();
    }
}
