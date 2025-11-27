package com.togetherseatech.whaleshark;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.togetherseatech.whaleshark.Db.DBContactHelper;
import com.togetherseatech.whaleshark.Db.History.TrainingDao;
import com.togetherseatech.whaleshark.Db.Problem.ProblemDao;
import com.togetherseatech.whaleshark.Db.Training.TrainingsDao;
import com.togetherseatech.whaleshark.util.CommonUtil;
import com.togetherseatech.whaleshark.util.SelectItems;

import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by bluepsh on 2017-10-19.
 */
public class IntroAct extends Activity implements Observer {

//    private SelectItems Si;
    private SharedPreferences pref;
    private String isUpdate = "";
    public static Activity Act;
    public static Boolean APP = false;
    Handler mHandler = new Handler();
    Runnable r = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(IntroAct.this, UpdateAct.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
    };

    /*Runnable r2 = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(IntroAct.this, MainAct.class));
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_act);
        Log.e("IntroAct", "Build.MODEL : " + Build.MODEL);

        Act = this;
//        pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
        Log.e("Login", "Service is.... : "+getServiceTaskName());



        if(!getServiceTaskName())

//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//                getApplicationContext().startForegroundService(new Intent(getApplicationContext(),TRGBgService.class));
//
//            }else{
//                getApplicationContext().startService(new Intent(getApplicationContext(),TRGBgService.class));
//            }
            /** 수정전 **/
            startService(new Intent(getApplicationContext(), TRGBgService.class));
        /** -------------------------------------------------------------------------- **/
        //        Log.e("IntroAct Reading: ", "cnt = "+cnt);
//        if (cnt <= 0) {

//        } else {
//            mHandler.postDelayed(r2, 2000);
//        }
        /*TrainingDao Tdao = new TrainingDao(this);
        TrainingsDao Tsdao = new TrainingsDao(this);
        Tdao.test();
        Tsdao.test();*/

        AutoUpdate aua = new AutoUpdate(getApplicationContext());	// <-- don't forget to instantiate
        aua.addObserver(this);	// see the remark below, next to update() method
    }

    private boolean getServiceTaskName(){
        boolean checked = false;
        ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> info;
        info = am.getRunningServices(100);
        for(Iterator iterator = info.iterator(); iterator.hasNext();){
            ActivityManager.RunningServiceInfo runningTaskInfo = (ActivityManager.RunningServiceInfo) iterator.next();
    		Log.e("Login", "Service name : "+runningTaskInfo.service.getClassName());
            if(runningTaskInfo.service.getClassName().equals("com.togetherseatech.whaleshark.TRGBgService")) {
                checked = true;
//    			Log.e("Login", "Service is.... : "+true);
            }
        }
        return checked;
    }

    @Override
    public void update(Observable observable, Object data) {
        if( ((String)data).equalsIgnoreCase(AutoUpdate.UPDATE_GOT_UPDATE) ) {
//            Log.e("IntroAct", "Have just received update!");
        }else if( ((String)data).equalsIgnoreCase(AutoUpdate.UPDATE_HAVE_UPDATE) ) {
            Log.e("IntroAct", "There's an update available!");
            Bundle bun = new Bundle();
            bun.putStringArray("Update", AutoUpdate.update);
            Intent intent = new Intent(Act, PopupActivity.class);
            intent.putExtras(bun);
            startActivity(intent);
        }else if( ((String)data).equalsIgnoreCase(AutoUpdate.UPDATE_NO_UPDATE) ) {
//            Log.e("IntroAct", "There's no update available!");
            if(!APP) {
                mHandler.postDelayed(r, 1000);
                APP = true;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Log.e("LogInAct", "onResume");
//        Log.e("LogInAct", "Build.MODEL : " + Build.MODEL);
         if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
            new CommonUtil().setHideNavigationBar(getWindow().getDecorView());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                break;
        }
        return true;
    }
}
