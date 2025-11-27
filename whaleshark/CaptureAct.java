package com.togetherseatech.whaleshark;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.togetherseatech.whaleshark.Db.History.TrainingDao;
import com.togetherseatech.whaleshark.Db.History.TrainingInfo;
import com.togetherseatech.whaleshark.Db.Member.MemberInfo;
import com.togetherseatech.whaleshark.util.CommonUtil;
import com.togetherseatech.whaleshark.util.SelectItems;
import com.togetherseatech.whaleshark.util.TextFontUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by bluepsh on 2017-10-19.
 */

public class CaptureAct extends Activity implements View.OnClickListener {

    private Button Capture_Save_Bt, Capture_Back_Bt;
    private RelativeLayout Capture_Main_rl;
    private TextView Capture_Rank_Tv, Capture_Name_Tv, Capture_Vsl_Name_Tv, Capture_Vsl_Type_Tv, Capture_Date_Tv, Capture_Birth_Tv,
            Capture_National_Tv, Capture_Chapter_Tv, Capture_Required1_Tv, Capture_Required2_Tv, Capture_Score_tv;

    private Intent intent;
    private SharedPreferences pref;
    private TextFontUtil tf;
    private MemberInfo Mi;
    private TrainingInfo Ti;
    private SelectItems Si;
    private String LOGIN_LANGUAGE;
    private TrainingDao TDao;
    private List<TrainingInfo> TiList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capture_act);
        Log.v("test","인증서 액티비티?");

        pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
//        LOGIN_LANGUAGE = pref.getString("LOGIN_LANGUAGE", "");
        Mi = (MemberInfo)getIntent().getExtras().get("MemberInfo");
        Log.v("test","captureAct : " + Mi);
        Ti = (TrainingInfo)getIntent().getExtras().get("TrainingInfo");
        tf = new TextFontUtil();
        Si = new SelectItems();
        TDao = new TrainingDao(this);

        Log.e("CaptureAct", Ti.getFirstName() + " " + Ti.getSurName());
        initSetting();
        initEventListener();


        TiList = TDao.getProblems(Ti.getIdx());
//        TiList = TDao.getAllProblem();
        String required1= "";
        String required2= "";
        for(int i=0; i < TiList.size();i++){
//            Log.e("ReportCardAct","AnswerScoreList "+i+" : "+ TiList.get(i).getRelative_regulation());
            String requiredTemp[] = TiList.get(i).getRelative_regulation().trim().split("/");

//            Log.e("requiredTemp",i+" : "+ requiredTemp.length);
            if(TiList.get(i).getRelative_regulation().trim().length() > 0) {
                if(requiredTemp.length == 3){
                    required1 += requiredTemp[0].trim();
                    required2 += requiredTemp[1].trim();
                    required2 += " " + requiredTemp[2].trim();
                } else if(requiredTemp.length == 2){
                    required1 += requiredTemp[0].trim();
                    required2 += requiredTemp[1].trim();
                }else{
                    required1 += requiredTemp[0].trim();
                    required2 += "";
                }

                if(i != TiList.size()-1) {
                    required1 += "\n";
                    required2 += "\n";
                }
            }

//            Log.e("ReportCardAct",i+" : "+ required);
        }

        Capture_Rank_Tv.setText(Si.getMemberRank(Ti.getRank(),"ENG"));
        Capture_Name_Tv.setText(Mi.getSurName() + " " + Mi.getFirstName());
//        Capture_Vsl_Name_Tv.setText(Mi.getVsl());
        Capture_Vsl_Type_Tv.setText(Si.getSelectMemberVslType(Mi.getVsl_type(),"ENG"));
        Capture_Date_Tv.setText(Ti.getDate());
        Capture_Birth_Tv.setText(Mi.getBirth());
        Capture_National_Tv.setText(Si.getMemberNational(Mi.getNational(),"ENG"));
        Capture_Chapter_Tv.setText(Si.getChapter(Ti.getTraining_course(),"ENG"));
        Capture_Required1_Tv.setText(required1);
        Capture_Required2_Tv.setText(required2);
        Capture_Score_tv.setText(Ti.getScore()+"");
    }

    private void initSetting() {
        Capture_Back_Bt = (Button)findViewById(R.id.capture_back_bt);
        Capture_Save_Bt = (Button)findViewById(R.id.capture_save_bt);

        Capture_Main_rl = (RelativeLayout) findViewById(R.id.capture_main_rl);
        Capture_Rank_Tv = (TextView) findViewById(R.id.capture_rank_tv);
        Capture_Name_Tv = (TextView) findViewById(R.id.capture_name_tv);
//        Capture_Vsl_Name_Tv = (TextView) findViewById(R.id.capture_vsl_name_tv);
        Capture_Vsl_Type_Tv = (TextView) findViewById(R.id.capture_vsl_type_tv);
        Capture_Date_Tv = (TextView) findViewById(R.id.capture_date_tv);
        Capture_Birth_Tv = (TextView) findViewById(R.id.capture_birth_tv);
        Capture_National_Tv = (TextView) findViewById(R.id.capture_national_tv);
        Capture_Chapter_Tv = (TextView) findViewById(R.id.capture_chapter_tv);
        Capture_Required1_Tv = (TextView) findViewById(R.id.capture_required1_tv);
        Capture_Required2_Tv = (TextView) findViewById(R.id.capture_required2_tv);
        Capture_Score_tv = (TextView) findViewById(R.id.capture_score_tv);

        tf.setOdstemplik(this, Capture_Rank_Tv);
        tf.setOranienbaum(this, Capture_Name_Tv);
//        tf.setOranienbaum(this, Capture_Vsl_Name_Tv);
        tf.setOranienbaum(this, Capture_Vsl_Type_Tv);
        tf.setOranienbaum(this, Capture_Date_Tv);
        tf.setOranienbaum(this, Capture_Birth_Tv);
        tf.setOranienbaum(this, Capture_National_Tv);
        tf.setPrestigeSignatureSerif(this, Capture_Chapter_Tv);
        tf.setPrestigeSignatureSerif(this, Capture_Required1_Tv);
        tf.setPrestigeSignatureSerif(this, Capture_Required2_Tv);
        tf.setNanumGothicBold(this, Capture_Score_tv);

    }

	private void initEventListener() {
        Capture_Back_Bt.setOnClickListener(this);
        Capture_Save_Bt.setOnClickListener(this);
	}

    @Override

    // test 230427
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.capture_back_bt:
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;
            case R.id.capture_save_bt:
                View rootView = Capture_Main_rl;
                File screenShot = ScreenShot(rootView);
                if (screenShot != null) {
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(screenShot)));
                    Toast.makeText(this, "Screenshot saved successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to save the screenshot", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public File ScreenShot(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap screenBitmap = view.getDrawingCache();
        File folder = new File(Environment.getExternalStorageDirectory() + "/SeaTech");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }
        if (success) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
            Date date = new Date();
            String chapter = Si.getChapter(Ti.getTraining_course(), "ENG");

            String filename = chapter.toLowerCase() + dateFormat.format(date) + ".png";
            File file = new File(Environment.getExternalStorageDirectory() + "/SeaTech", filename);
            FileOutputStream os;
            try {
                os = new FileOutputStream(file);
                screenBitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                os.close();
                view.setDrawingCacheEnabled(false);
                return file;
            } catch (IOException e) {
                e.printStackTrace();
                view.setDrawingCacheEnabled(false);
                return null;
            }
        } else {
            view.setDrawingCacheEnabled(false);
            return null;
        }
    }

    /** 0427 test 아래가 수정전
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.capture_back_bt:
//                Toast.makeText(this, "back", Toast.LENGTH_LONG).show();
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;
            case R.id.capture_save_bt:
//                Toast.makeText(this, "save", Toast.LENGTH_LONG).show();

                View rootView = Capture_Main_rl;

                File screenShot = ScreenShot(rootView);
                if(screenShot!= null){
                    //갤러리에 추가
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(screenShot)));
                }
                break;
        }

    }
    //화면 캡쳐하기
    public File ScreenShot(View view){
        view.setDrawingCacheEnabled(true);  //화면에 뿌릴때 캐시를 사용하게 한다
        Bitmap screenBitmap = view.getDrawingCache();   //캐시를 비트맵으로 변환
        File folder = new File(Environment.getExternalStorageDirectory() + "/SeaTech");
        boolean success = true;
        if (!folder.exists()) {
            //Toast.makeText(MainActivity.this, "Directory Does Not Exist, Create It", Toast.LENGTH_SHORT).show();
            success = folder.mkdir();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyyMMdd", Locale.getDefault());
        Date date = new Date();
        String chapter = Si.getChapter(Ti.getTraining_course(), "ENG");

        String filename = chapter.toLowerCase() + dateFormat.format(date) + "(" + Ti.getFirstName().trim().replaceAll(" ","") + Ti.getSurName().trim().replaceAll(" ","") + ").png";
        File file = new File(Environment.getExternalStorageDirectory()+"/SeaTech", filename);  //Pictures폴더 screenshot.png 파일
        FileOutputStream os = null;
        try{
            os = new FileOutputStream(file);
            screenBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);   //비트맵을 PNG파일로 변환
            os.close();
        }catch (IOException e){
            e.printStackTrace();
            //test 230426
            Toast.makeText(CaptureAct.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
            //test 200426
        }catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(CaptureAct.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
        view.setDrawingCacheEnabled(false);
        return file;

    }
**/
    // back키 동작 금지
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            event.startTracking();
            return false;
        }
        return true;
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.e("CaptureAct", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("CaptureAct", "onResume");
         if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
            new CommonUtil().setHideNavigationBar(getWindow().getDecorView());
        }
    }

    @Override
    protected void onUserLeaveHint(){
        super.onUserLeaveHint();
        Log.e("CaptureAct", "onUserLeaveHint");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.e("CaptureAct", "onPause");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("CaptureAct", "onStop");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.e("CaptureAct", "onDestroy");
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                View rootView = Capture_Main_rl;
                File screenShot = ScreenShot(rootView);
                if (screenShot != null) {
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(screenShot)));
                }
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
