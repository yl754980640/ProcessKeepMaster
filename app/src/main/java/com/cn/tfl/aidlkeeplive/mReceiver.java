package com.cn.tfl.aidlkeeplive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.cn.tfl.aidlkeeplive.fun2_3_4.LocalService;

/**
 * 开机完成广播
 */
 
public class mReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        Intent mIntent = new Intent(context,LocalService.class);
        context.startService(mIntent);
    }
}