package com.togetherseatech.whaleshark;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import com.togetherseatech.whaleshark.Db.History.TrainingDao;
import com.togetherseatech.whaleshark.Db.History.TrainingInfo;
import com.togetherseatech.whaleshark.Db.Member.MemberDao;
import com.togetherseatech.whaleshark.Db.Member.MemberInfo;
import com.togetherseatech.whaleshark.Db.Problem.ProblemDao;
import com.togetherseatech.whaleshark.Db.Training.TrainingsDao;
import com.togetherseatech.whaleshark.Db.Training.TrainingsInfo;
import com.togetherseatech.whaleshark.util.AllUpdate;
import com.togetherseatech.whaleshark.util.CommonUtil;
import com.togetherseatech.whaleshark.util.SelectItems;
import com.togetherseatech.whaleshark.util.TextFontUtil;
import com.togetherseatech.whaleshark.util.XmlSAXParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 * Created by bluepsh on 2017-10-19.
 */

public class MainAct extends Activity implements View.OnClickListener {

    private Button Main_Admin_Bt, Main_General_Bt;//, All_Download_Bt;
    private TextView Main_Version_Tv, Main_License_Tv;
    private Intent intent;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private int MY_PERMISSION_REQUEST_STORAGE = 1;
    private MemberDao Mdao;
    private String VslName;
    private int ALLDOWNLOAD;
    private SelectItems Si;
    private CommonUtil Cu;
    private boolean PatchFile = true;
    private Context con;
    private boolean LTE, LTE_Con;
    private WifiManager wifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_act);
//        Log.e("MainAct", "!!!!!!!!");
//        Log.e("MainAct", "MODEL = " + Build.MODEL); //모델명

        con = this;
        checkPermission();
        Si = new SelectItems();
        Mdao = new MemberDao(this);
        pref = getSharedPreferences("pref", Context.MODE_PRIVATE);

        /*editor = pref.edit();
        editor.putInt("ALLDOWNLOAD", 1);
        editor.commit();*/

        ALLDOWNLOAD = pref.getInt("ALLDOWNLOAD",0);
        Log.e("MainAct", "ALLDOWNLOAD : " + ALLDOWNLOAD);
        initSetting();
		initEventListener();

        PackageManager pm = getPackageManager();
        String packageName = getPackageName();
        int flags = PackageManager.GET_PERMISSIONS;
        PackageInfo packageInfo = null;
        String versionName = "";
        try {
            packageInfo = pm.getPackageInfo(packageName, flags);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
//            Log.e("MainAct", e.getMessage());
        }

        MemberInfo Mi = Mdao.getcloseLicense(Si.getDateTime2());

        String licenseDate = "";
        if(Mi.getStart_date() != null && Mi.getEnd_date() != null)
            licenseDate = Mi.getStart_date().replace("-",".") + " - " + Mi.getEnd_date().replace("-",".");
        else
            licenseDate = "";

        Main_Version_Tv.setText("Ver "+versionName);
//        Log.e("MainAct"," getScore : " + Math.round((float)8 / (float)15 * 100));
        pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
        Main_License_Tv.setText(licenseDate);
//        Main_License_Tv.setText("");
//        AutoUpdate aua = new AutoUpdate(getApplicationContext());	// <-- don't forget to instantiate
//        aua.addObserver(this);	// see the remark below, next to update() method

        TrainingsDao Tsdao = new TrainingsDao(this);
        Tsdao.updateGt("5");
        Tsdao.updateGt("6");
        Tsdao.updateGt("12");


//        Tsdao.getHistory("G", 2, Mi);
        Tsdao.createMatrixtable();
        int cnt = Tsdao.getMatrixCount("",0,0);
        Log.e("test","cnt값 : "+ cnt);
        List<TrainingsInfo> tsList = new ArrayList<>();
        if (cnt <= 0) {
            tsList = Si.getGTMatrix("G");
            Log.e("test","제너럴 메트릭스 사이즈 :" + tsList.size());

            for(TrainingsInfo tsi : tsList) {
                Tsdao.addMatrix(tsi);
            }
            tsList = Si.getGTMatrix("R");
            for(TrainingsInfo tsi : tsList) {
                Tsdao.addMatrix(tsi);
            }
        } else {
            Log.e("test","cnt 아니면?" + cnt);
//            Tsdao.deleteMatrix("R",9,0);
//            tsList = Tsdao.getMatrixs("R","");
//            Log.e("MainAct","getTraining_course : " + tsList.get(tsList.size() - 1).getTraining_course());
//            if(tsList.get(tsList.size() - 1).getTraining_course() != 9) {
//            }
            cnt = Tsdao.getMatrixCount("G",41,0);
//            Log.e("MainAct","getMatrixCount : " + cnt);
            if(cnt <= 0) {
                Tsdao.allDeleteMatrix();
                tsList = Si.getGTMatrix("G");
                for(TrainingsInfo tsi : tsList) {
                    Tsdao.addMatrix(tsi);
                }
                tsList = Si.getGTMatrix("R");
                for(TrainingsInfo tsi : tsList) {
                    Tsdao.addMatrix(tsi);

                }
            }
        }

        /*ProblemDao Pdao = new ProblemDao(this);
        Pdao.deleteAllProblem();
        int pcnt = Pdao.getProblemCount();
        Log.e("MainAct", "pcnt = " + pcnt);
        if(pcnt <= 0)
            new XmlSAXParser(this, "Problem");*/

        /*ProblemDao Pdao = new ProblemDao(this);
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        list = Pdao.getAllProblemXml();
        String result = "";
        try {
            result = CommonUtil.string2Xml(list);

            File file = new File(Environment.getExternalStorageDirectory(), "Question.xml");
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(result);
            myOutWriter.close();
            fOut.flush();
            fOut.close();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
//        Mdao.getKeyLicense();
        if(ALLDOWNLOAD == 0)
            Wifi();
    }

    private void Wifi() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        Log.e("MainAct","Wifi : " + mWifi.isConnected());
        if (mWifi.isConnected()) {
            MemberDao Mdao = new MemberDao(this);
            TrainingDao Tdao = new TrainingDao(this);
            MemberInfo VslMi = Mdao.getVslMember("ADMIN", "Administration");
            List<TrainingInfo> TiList = Tdao.getAllUpdateData(VslMi.getVsl_type());
            if(TiList.size() > 0)
                new AllUpdate(this);
            else {
                editor = pref.edit();
                editor.putInt("ALLDOWNLOAD", 1);
                editor.commit();
                Si.getToast(this, "최적화가 완료되었습니다.", Toast.LENGTH_SHORT);
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("프로그램 최적화").setMessage("촤적화를 위해 인터넷 연결이 필요합니다.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    IntroAct.APP = false;
                    finish();
                }
            });

            /*builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int id)
                {
                    Toast.makeText(getApplicationContext(), "Cancel Click", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNeutralButton("Neutral", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int id)
                {
                    Toast.makeText(getApplicationContext(), "Neutral Click", Toast.LENGTH_SHORT).show();
                }
            });*/

            AlertDialog alertDialog = builder.create();
            alertDialog.setCancelable(false);
            alertDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

             if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
                new CommonUtil().setHideNavigationBar(alertDialog.getWindow().getDecorView());
            }
            alertDialog.show();
        }
    }

    private void initSetting() {
        TextFontUtil tf = new TextFontUtil();
        Main_Admin_Bt = (Button)findViewById(R.id.admin_bt);
        tf.setNanumGothicBold(this, Main_Admin_Bt);
        Main_General_Bt = (Button)findViewById(R.id.general_bt);
        tf.setNanumGothicBold(this, Main_General_Bt);
        Main_Version_Tv = (TextView)findViewById(R.id.version_tv);
        tf.setNanumGothicExtraBold(this, Main_Version_Tv);
        Main_License_Tv = (TextView)findViewById(R.id.license_tv);
        tf.setNanumGothicExtraBold(this, Main_License_Tv);
        /*All_Download_Bt = (Button)findViewById(R.id.all_download_bt2);
        if(ALLDOWNLOAD == 1)
            All_Download_Bt.setVisibility(View.GONE);
        else
            All_Download_Bt.setVisibility(View.VISIBLE);*/
    }

	private void initEventListener() {
        Main_Admin_Bt.setOnClickListener(this);
        Main_General_Bt.setOnClickListener(this);
//        All_Download_Bt.setOnClickListener(this);
	}

    @Override
    public void onClick(View v) {

        VslName = Mdao.getVslName();

        if(!"".equals(VslName) & VslName != null){
            editor = pref.edit();
            switch (v.getId()) {
                case R.id.admin_bt:
//                Toast.makeText(this, "Administration", Toast.LENGTH_LONG).show();
                    editor.putString("LOGIN_MODE", "Administration");
                    break;
                case R.id.general_bt:
//                Toast.makeText(this, "General", Toast.LENGTH_LONG).show();
                    editor.putString("LOGIN_MODE", "General");
                    break;
            }

//            if(v.getId() == R.id.all_download_bt2) {
//                new CommonUtil().setHideDialog(new TwoButtonPopUpDialog(this,All_Download_Bt, "ALLDOWNLOAD"));
//            } else {
                editor.commit();
                intent = new Intent(MainAct.this, LogInAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
//            }
        } else {
//            Log.e("MainAct", "VslName : "+VslName);
            new InitSettingPopUpDialog(this).show();
        }
    }

    /**
     * Permission check.
     */
    private void checkPermission() {
        Log.e("test","checkPermission : " +" checkPermission 시작");
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.WRITE_SETTINGS)
                != PackageManager.PERMISSION_GRANTED) {
//            Log.e("CaptureAct", "1");
            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to write the permission.
                Toast.makeText(this, "Read/Write external storage", Toast.LENGTH_SHORT).show();
            }

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_SETTINGS},
                    MY_PERMISSION_REQUEST_STORAGE);



/*            if(Build.VERSION.SDK_INT>=30){
                Log.e("test","버전 30 일때 : " +"퍼");
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_SETTINGS},
                        MY_PERMISSION_REQUEST_STORAGE);

            }*/

            // MY_PERMISSION_REQUEST_STORAGE is an
            // app-defined int constant

        } else {
            // 다음 부분은 항상 허용일 경우에 해당이 됩니다.
//            Log.e("checkPermission", "2");

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                new TwoButtonPopUpDialog(this,"EXIT" ).show();
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Log.e("LogInAct", "onResume");
         if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
            new CommonUtil().setHideNavigationBar(getWindow().getDecorView());
        }
    }
}
