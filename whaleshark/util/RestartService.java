package com.togetherseatech.whaleshark.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.togetherseatech.whaleshark.TRGBgService;

/**
 * Created by seonghak on 2017. 12. 8..
 */

public class RestartService extends BroadcastReceiver {

    String ACTION_RESTART_TRGBGSERVICE = "restart";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("RestartService","onReceive : " + intent.getAction());

        if(intent.getAction().equals(ACTION_RESTART_TRGBGSERVICE)) {
            Intent i = new Intent(context, TRGBgService.class);
            context.startService(i);
        }
    }
}
