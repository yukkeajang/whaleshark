package com.togetherseatech.whaleshark;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.togetherseatech.whaleshark.util.CommonUtil;
import com.togetherseatech.whaleshark.util.SelectItems;
import com.togetherseatech.whaleshark.util.StartupCsvExporter;
import com.togetherseatech.whaleshark.util.StartupDbExporter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CbtApplication extends Application {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    @Override
    public void onCreate(){
        super.onCreate();
        Log.e("CbtApplication", "!!!!!!!!");
       /* SelectItems Si = new SelectItems();
//        Boolean Closelicense = Si.CloseLicense(getApplicationContext());
//        Log.e("CbtApplication", "Closelicense!!!!!!!! "+Closelicense);
        pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
        this.Language = pref.getBoolean("CLOSELICENSE", true);
        editor = pref.edit();
//        editor.putBoolean("CLOSELICENSE", Closelicense);
        editor.putBoolean("CLOSELICENSE", true);
        editor.commit();*/
        
        StartupDbExporter exporter = new StartupDbExporter(getApplicationContext());
        exporter.exportDatabase();

        StartupCsvExporter csvExporter = new StartupCsvExporter(getApplicationContext());
        csvExporter.exportPendingCsv();
    }
}
