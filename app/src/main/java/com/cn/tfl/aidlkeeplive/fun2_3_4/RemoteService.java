package com.cn.tfl.aidlkeeplive.fun2_3_4;

import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.cn.tfl.aidlkeeplive.IProcessService;

public class RemoteService extends Service {

    private static final String TAG = "RemoteService";

    private RemoteBinder remoteBinder;

    private RemoteServiceConnection remoteServiceConnection;

    @Override
    public void onCreate() {
        remoteBinder = new RemoteBinder();
        if (remoteServiceConnection == null) {
            remoteServiceConnection = new RemoteServiceConnection();
        }
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return remoteBinder;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        bindService(new Intent(this, LocalService.class), remoteServiceConnection, Context.BIND_IMPORTANT);
        //设置为前台进程
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setVibrate(null);
        builder.setContentText("远程服务线程保护");
        builder.setContentTitle("远程服务正在运行。。。");
//        builder.setSmallIcon(R.mipmap.ic_launcher_round);
        builder.setContentInfo("info");
        builder.setWhen(System.currentTimeMillis());
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        startForeground(startId, builder.build());
        return START_STICKY;
    }

    class RemoteBinder extends IProcessService.Stub {
        @Override
        public String getServiceName() throws RemoteException {
            return "远程服务";
        }
    }


    class RemoteServiceConnection implements ServiceConnection {
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
            Log.d(TAG, "本地服务被干死了..");
            startService(new Intent(RemoteService.this, LocalService.class));
            bindService(new Intent(RemoteService.this, LocalService.class), remoteServiceConnection, Context.BIND_IMPORTANT);
        }
    }


    @Override
    public void onDestroy() {
        unbindService(remoteServiceConnection);
        super.onDestroy();
    }
}
