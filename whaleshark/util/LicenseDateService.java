package com.togetherseatech.whaleshark.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.togetherseatech.whaleshark.TRGBgService;

/**
 * Created by seonghak on 2017. 12. 8..
 */

public class LicenseDateService extends BroadcastReceiver {

    String ACTION_CLOSELICENSE_SERVICE = "CloseLicense";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("RestartService","onReceive");

        if(intent.getAction().equals(ACTION_CLOSELICENSE_SERVICE)) {
            intent = new Intent(context, TRGBgService.class);
            context.startService(intent);
        }
    }
}
