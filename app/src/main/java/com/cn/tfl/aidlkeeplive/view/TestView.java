package com.cn.tfl.aidlkeeplive.view;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cn.tfl.aidlkeeplive.func1.Helpers;
import com.cn.tfl.aidlkeeplive.fun2_3_4.LocalService;
import com.cn.tfl.aidlkeeplive.R;
import com.cn.tfl.aidlkeeplive.fun2_3_4.RemoteService;
import com.cn.tfl.aidlkeeplive.fun2_3_4.JobHandleService3;
import com.cn.tfl.aidlkeeplive.func5.JobSchedulerManager;

import com.cn.tfl.aidlkeeplive.fun6.JobWakeUpService;
import com.cn.tfl.aidlkeeplive.fun7.MyNotificationListenerService;


public class TestView extends LinearLayout implements View.OnClickListener {
    private Button btnHideTest, btn_test_1_1,btn_test_1_2,btn_test_1_3,btn_test_1_4,
            btn_test_2,btn_test_3,btn_test_4,
            btn_test_5_1,btn_test_5_2,btn_test_5_3,
            btn_test_6,btn_test_7;
    private Context context;
    JobSchedulerManager jobSchedulerInstance = null ;

    public TestView(Context context) {
        this(context, null);
    }

    public TestView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_test, this);
        this.context = context;
        initView();
        initData();
    }

    private void initView() {
        btnHideTest = (Button) findViewById(R.id.btn_hide_test);
        btnHideTest.setOnClickListener(this);

        //方法一：开启一个线程，杀死后不再起来
        btn_test_1_1 = (Button) findViewById(R.id.btn_test_1_1);
        btn_test_1_1.setOnClickListener(this);
        btn_test_1_2 = (Button) findViewById(R.id.btn_test_1_2);
        btn_test_1_2.setOnClickListener(this);
        btn_test_1_3 = (Button) findViewById(R.id.btn_test_1_3);
        btn_test_1_3.setOnClickListener(this);
        btn_test_1_4 = (Button) findViewById(R.id.btn_test_1_4);
        btn_test_1_4.setOnClickListener(this);

        //方法二：
        btn_test_2 = (Button) findViewById(R.id.btn_test_2);
        btn_test_2.setOnClickListener(this);

        //方法三：
        btn_test_3 = (Button) findViewById(R.id.btn_test_3);
        btn_test_3.setOnClickListener(this);

        //方法四：
        btn_test_4 = (Button) findViewById(R.id.btn_test_4);
        btn_test_4.setOnClickListener(this);

        //方法五：
        btn_test_5_1 = (Button) findViewById(R.id.btn_test_5_1);
        btn_test_5_1.setOnClickListener(this);
        btn_test_5_2 = (Button) findViewById(R.id.btn_test_5_2);
        btn_test_5_2.setOnClickListener(this);
        btn_test_5_3 = (Button) findViewById(R.id.btn_test_5_3);
        btn_test_5_3.setOnClickListener(this);

        //方法六：
        btn_test_6 = (Button) findViewById(R.id.btn_test_6);
        btn_test_6.setOnClickListener(this);

        //方法七：
        btn_test_7 = (Button) findViewById(R.id.btn_test_7);
        btn_test_7.setOnClickListener(this);
    }

    private void initData() {
//        testXiaoMi(); //
//        initDataTest1(context);
//        initDataTest2(context);
//        initDataTest3(context);
//        initDataTest4(context);

    }

    private void initDataTest7(Context context) {
        context.startService(new Intent(context, MyNotificationListenerService.class));
    }

    private void initDataTest6(Context context) {
        context.startService(new Intent(context, LocalService.class));
        context.startService(new Intent(context, RemoteService.class));
        System.out.println("这是一个新世界==SDK_INT==" +Build.VERSION.SDK_INT);//我的华为测试机：SDK_INT=API=26；
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.LOLLIPOP) {
            //版本必须大于5.0
            context.startService(new Intent(context, JobWakeUpService.class));
        }
    }

    private void initDataTest5(Context context) {
        jobSchedulerInstance = JobSchedulerManager.getJobSchedulerInstance(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initDataTest4(Context context) {
        JobScheduler scheduler= (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo jobInfo=new JobInfo.Builder(1,new ComponentName(context,JobHandleService3.class))
                .setPeriodic(1000)
                /*.setOverrideDeadline(2000)*/
                .setPersisted(true)
                .build();
        scheduler.schedule(jobInfo);
        for(JobInfo info:scheduler.getAllPendingJobs()){
            if(jobInfo.getId()==1){
                Toast.makeText(context,"initDataTest4",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initDataTest3(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            context.startService(new Intent(context, JobHandleService3.class));
        } else {
            context.startService(new Intent(context, LocalService.class));
            context.startService(new Intent(context, RemoteService.class));
        }
    }

    private void initDataTest2(Context context) {
        context.startService(new Intent(context, LocalService.class));
        context.startService(new Intent(context, RemoteService.class));
    }

    private void initDataTest1(Context context) {
        Helpers.schedule( context );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_hide_test:
                setButtonVisible(btnHideTest.getText().toString().contains("显示"));
                break;

            case R.id.btn_test_1_1:
                initDataTest1(context);
//                Helpers.schedule(context);
                break;
            case R.id.btn_test_1_2:
                Helpers.jobFinished(context);
                break;
            case R.id.btn_test_1_3:
                Helpers.cancelJob(context);
                break;
            case R.id.btn_test_1_4:
                Helpers.enqueueJob();
                break;

            case R.id.btn_test_2:
                initDataTest2(context);
                break;
            case R.id.btn_test_3:
                initDataTest3(context);
                break;
            case R.id.btn_test_4:
                initDataTest4(context);
                break;
            case R.id.btn_test_5_1:
                initDataTest5(context);
                break;
            case R.id.btn_test_5_2:
                jobSchedulerInstance.startJobScheduler();
                break;
            case R.id.btn_test_5_3:
                jobSchedulerInstance.stopJobScheduler();
                break;
            case R.id.btn_test_6:
                initDataTest6(context);
                break;
            case R.id.btn_test_7:
                initDataTest7(context);
                break;
            default:
                break;
        }
    }


    private void setButtonVisible(boolean visible) {
        if (visible) {
            btn_test_1_1.setVisibility(VISIBLE);
            btn_test_1_2.setVisibility(VISIBLE);
            btn_test_1_3.setVisibility(VISIBLE);
            btn_test_1_4.setVisibility(VISIBLE);

            btn_test_2.setVisibility(VISIBLE);
            btn_test_3.setVisibility(VISIBLE);
            btn_test_4.setVisibility(VISIBLE);

            btn_test_5_1.setVisibility(VISIBLE);
            btn_test_5_2.setVisibility(VISIBLE);
            btn_test_5_3.setVisibility(VISIBLE);

            btn_test_6.setVisibility(VISIBLE);
            btn_test_7.setVisibility(VISIBLE);
            btnHideTest.setText("隐藏测试按钮");
        } else {
            btn_test_1_1.setVisibility(GONE);
            btn_test_1_2.setVisibility(GONE);
            btn_test_1_3.setVisibility(GONE);
            btn_test_1_4.setVisibility(GONE);

            btn_test_2.setVisibility(GONE);
            btn_test_3.setVisibility(GONE);
            btn_test_4.setVisibility(GONE);

            btn_test_5_1.setVisibility(GONE);
            btn_test_5_2.setVisibility(GONE);
            btn_test_5_3.setVisibility(GONE);

            btn_test_6.setVisibility(GONE);
            btn_test_7.setVisibility(GONE);
            btnHideTest.setText("显示测试按钮");
        }
    }

    private void testXiaoMi() {
        if (Build.MANUFACTURER.equals("Xiaomi")) {
            Intent intent = new Intent();
            intent.setAction("miui.intent.action.OP_AUTO_START");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            context.startActivity(intent);
        }
    }
}
