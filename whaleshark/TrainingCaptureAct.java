package com.togetherseatech.whaleshark;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.togetherseatech.whaleshark.Db.Member.MemberInfo;
import com.togetherseatech.whaleshark.Db.Training.TrainingsDao;
import com.togetherseatech.whaleshark.Db.Training.TrainingsInfo;
import com.togetherseatech.whaleshark.util.CommonUtil;
import com.togetherseatech.whaleshark.util.SelectItems;
import com.togetherseatech.whaleshark.util.TextFontUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by bluepsh on 2017-10-19.
 */

public class TrainingCaptureAct extends Activity implements View.OnClickListener {

    private Button TCapture_Save_Bt, TCapture_Back_Bt;
    private RelativeLayout TCapture_Main_rl;
    private TextView TCapture_Rank_Tv, TCapture_Name_Tv, TCapture_Vsl_Name_Tv, TCapture_Vsl_Type_Tv, TCapture_Date_Tv,
            TCapture_Chapter_Tv, TCapture_Score_tv;

    private TextFontUtil tf;
    private TrainingsInfo Tsi;
    private String Type;
    private SelectItems Si;
    private TrainingsDao TsDao;
    private List<TrainingsInfo> TsiList;
    private String titleName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.training_capture_act);

        Tsi = (TrainingsInfo)getIntent().getExtras().get("TrainingsInfo");
        Type = getIntent().getStringExtra("Type");
        tf = new TextFontUtil();
        Si = new SelectItems();
        TsDao = new TrainingsDao(this);

//        Log.e("TCaptureAct", "Tsi.getTraining_course() "+Tsi.getTraining_course()+"/"+Tsi.getTraining_course2());
        initSetting();
        initEventListener();


        TsiList = TsDao.getHistoryMember(Tsi.getIdx());
//        TiList = TDao.getAllProblem();
        String rank = "";
        String name = "";
        for(int i=0; i < TsiList.size();i++){

            rank += Si.getMemberRank(TsiList.get(i).getRank(),"ENG");
            name += TsiList.get(i).getSurName() + " " + TsiList.get(i).getFirstName();

            if(i != TsiList.size()-1) {
                rank += "\n";
                name += "\n";
            }
//            Log.e("ReportCardAct",i+" : "+ required);
        }

        if("R".equals(Type)){
            titleName = Si.getNewRegulation(Tsi.getTraining_course(),"ENG");
        }else{
            titleName = Si.getGeneralTraining(Tsi.getTraining_course(), Tsi.getTraining_course2(),"ENG");
        }

        TCapture_Chapter_Tv.setText(titleName);
//        TCapture_Vsl_Name_Tv.setText(Tsi.getVsl());
        TCapture_Vsl_Type_Tv.setText(Si.getSelectMemberVslType(Tsi.getVsl_type(),"ENG"));
        TCapture_Date_Tv.setText(Tsi.getDate());
        TCapture_Rank_Tv.setText(rank);
        TCapture_Name_Tv.setText(name);
        TCapture_Score_tv.setText(Tsi.getScore()+"");
    }

    private void initSetting() {
        TCapture_Back_Bt = (Button)findViewById(R.id.capture_back_bt);
        TCapture_Save_Bt = (Button)findViewById(R.id.capture_save_bt);

        TCapture_Main_rl = (RelativeLayout) findViewById(R.id.capture_main_rl);
        TCapture_Chapter_Tv = (TextView) findViewById(R.id.capture_chapter_tv);
//        TCapture_Vsl_Name_Tv = (TextView) findViewById(R.id.capture_vsl_name_tv);
        TCapture_Vsl_Type_Tv = (TextView) findViewById(R.id.capture_vsl_type_tv);
        TCapture_Date_Tv = (TextView) findViewById(R.id.capture_date_tv);
        TCapture_Rank_Tv = (TextView) findViewById(R.id.capture_rank_tv);
        TCapture_Name_Tv = (TextView) findViewById(R.id.capture_name_tv);
        TCapture_Score_tv = (TextView) findViewById(R.id.capture_score_tv);

//        tf.setOranienbaum(this, TCapture_Vsl_Name_Tv);
        tf.setOranienbaum(this, TCapture_Vsl_Type_Tv);
        tf.setOranienbaum(this, TCapture_Date_Tv);
        tf.setOranienbaum(this, TCapture_Chapter_Tv);
        tf.setPrestigeSignatureSerif(this, TCapture_Rank_Tv);
        tf.setPrestigeSignatureSerif(this, TCapture_Name_Tv);
        tf.setNanumGothicBold(this, TCapture_Score_tv);

    }

	private void initEventListener() {
        TCapture_Back_Bt.setOnClickListener(this);
        TCapture_Save_Bt.setOnClickListener(this);
	}
    @Override
//test 230427
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.capture_back_bt:
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;
            case R.id.capture_save_bt:
                View rootView = TCapture_Main_rl;
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
            String filename = titleName.toLowerCase() + dateFormat.format(date) + ".png";
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


    /** 230427 test previous below
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.capture_back_bt:
//                Toast.makeText(this, "back", Toast.LENGTH_LONG).show();
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;
            case R.id.capture_save_bt:
//                Toast.makeText(this, "save", Toast.LENGTH_LONG).show();

                View rootView = TCapture_Main_rl;

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

        String filename = titleName.toLowerCase() + dateFormat.format(date) + ".png";
        File file = new File(Environment.getExternalStorageDirectory()+"/SeaTech", filename);  //Pictures폴더 screenshot.png 파일
        FileOutputStream os = null;
        try{
            os = new FileOutputStream(file);
            screenBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);   //비트맵을 PNG파일로 변환
            os.close();
        }catch (IOException e){
            e.printStackTrace();
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
        Log.e("TCaptureAct", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("TCaptureAct", "onResume");
         if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
            new CommonUtil().setHideNavigationBar(getWindow().getDecorView());
        }
    }

    @Override
    protected void onUserLeaveHint(){
        super.onUserLeaveHint();
        Log.e("TCaptureAct", "onUserLeaveHint");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.e("TCaptureAct", "onPause");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("TCaptureAct", "onStop");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.e("TCaptureAct", "onDestroy");
    }


}
