package com.togetherseatech.whaleshark;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.togetherseatech.whaleshark.Db.History.TrainingDao;
import com.togetherseatech.whaleshark.Db.History.TrainingInfo;
import com.togetherseatech.whaleshark.Db.Member.MemberInfo;
import com.togetherseatech.whaleshark.Db.Problem.ProblemInfo;
import com.togetherseatech.whaleshark.util.CommonUtil;
import com.togetherseatech.whaleshark.util.SelectItems;
import com.togetherseatech.whaleshark.util.XmlSAXParser2;
import com.togetherseatech.whaleshark.util.XmlSAXParser3;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by bluepsh on 2017-10-19.
 */

public class AdminUpgradeAct extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private static String Tag = "AdminUpgradeAct";
    private Intent intent;
    private String LOGIN_MODE;
    private Button Admin_LogOut_Bt, Admin_ShipStaffs_Bt, Admin_Matrix_Bt, Admin_Training_Bt, Admin_Update_Bt,
            Admin_Upgrade_Bt, Admin_Dashboard_Bt, Admin_Help_Bt, Admin_All_Download_Bt;
    private ImageView Admin_Language_Iv;
    private TextView Admin_Name_Tv;
    // 액티비티에 보여지는 리스트뷰
    public static ExpandableListView Admin_ExpandableListView;
    // 리스트뷰에 사용할 어댑터
    public static UpgradeExpandableAdapter Admin_UpgradeExpandableAdapter;

    private String LOGIN_LANGUAGE, VSL;
    private int LOGIN_CARRIER;
    private SharedPreferences pref;
    private static Activity Act;
    private static SelectItems Si;
    public static XmlSAXParser3 xml;
    public static ArrayList<String> Admin_GroupList;
    public static ArrayList<ArrayList<UpgradeInfo>> Admin_ChildList;
    public static ArrayList<UpgradeInfo> Admin_ChildListContentQ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_upgrade_act);
        Log.e("test","AdminUpgradeAct OnCreate");
        Act = this;
        pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
        LOGIN_LANGUAGE = pref.getString("LOGIN_LANGUAGE", "");
        LOGIN_CARRIER = pref.getInt("LOGIN_CARRIER", 0);
        VSL = pref.getString("VSL", "");
        Si = new SelectItems();
        //permissionCheck();
        //checkPermission(Manifest.permission.MANAGE_EXTERNAL_STORAGE,1);
        initSetting();
        initEventListener();
        new AdminUpgrade(Act).execute();


    }

/*    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("test","onActivity Result : ");

        if (requestCode == 1){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                if (Environment.isExternalStorageManager()){

                }else{
                    Toast.makeText(getApplicationContext(),"Allow permission for storage access!",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }*/

    private void initSetting() {
        Admin_LogOut_Bt = (Button)findViewById(R.id.admin_logout_bt);
        Admin_ShipStaffs_Bt = (Button)findViewById(R.id.admin_shipstaffs_bt);
        Admin_Matrix_Bt = (Button)findViewById(R.id.admin_matrix_bt);
        Admin_Training_Bt = (Button)findViewById(R.id.admin_training_bt);
        Admin_Update_Bt = (Button)findViewById(R.id.admin_update_bt);
        Admin_Upgrade_Bt = (Button)findViewById(R.id.admin_upgrade_bt);
        Admin_Upgrade_Bt.setBackgroundResource(R.mipmap.download_data_on);
        Admin_Dashboard_Bt = (Button)findViewById(R.id.admin_dashboard_bt);
        Admin_Name_Tv = (TextView) findViewById(R.id.admin_name_tv);
        Admin_Name_Tv.setText(VSL);
        Admin_Language_Iv = (ImageView)findViewById(R.id.admin_language_iv);

        if("KR".equals(LOGIN_LANGUAGE))
            Admin_Language_Iv.setImageResource(R.mipmap.language_kr);
        else if("ENG".equals(LOGIN_LANGUAGE))
            Admin_Language_Iv.setImageResource(R.mipmap.language_eng);
        Admin_Help_Bt = (Button)findViewById(R.id.admin_help_bt);
        Admin_ExpandableListView = (ExpandableListView)findViewById(R.id.admin_elv);
        Admin_All_Download_Bt = (Button)findViewById(R.id.admin_all_download_bt);
    }

    private void initEventListener() {
        Admin_LogOut_Bt.setOnClickListener(this);
        Admin_ShipStaffs_Bt.setOnClickListener(this);
        Admin_Matrix_Bt.setOnClickListener(this);
        Admin_Training_Bt.setOnClickListener(this);
        Admin_Update_Bt.setOnClickListener(this);
        Admin_Dashboard_Bt.setOnClickListener(this);
        Admin_Help_Bt.setOnClickListener(this);
        Admin_ExpandableListView.setOnGroupClickListener(setOnGroupClickEvent);
        Admin_All_Download_Bt.setOnClickListener(this);

    }

    private ExpandableListView.OnGroupClickListener setOnGroupClickEvent = new ExpandableListView.OnGroupClickListener() {
        int lastClickedPosition = 0;

        @Override
        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
            // 선택 한 groupPosition의 펼침/닫힘 상태 체크
//            Boolean isExpand = (!Admin_ExpandableListView.isGroupExpanded(groupPosition));

            // 이 전에 열려있던 group 닫기
//            Admin_ExpandableListView.collapseGroup(lastClickedPosition);

//            if(isExpand){
//                Admin_ExpandableListView.expandGroup(groupPosition);
//            }
//            lastClickedPosition = groupPosition;
            return true;
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(this, "onItemClick", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.admin_logout_bt:
                new CommonUtil().setHideDialog(new TwoButtonPopUpDialog(this,"LOGOUT"));
                break;
            case R.id.admin_shipstaffs_bt:
                intent = new Intent(AdminUpgradeAct.this, AdminAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;
            case R.id.admin_matrix_bt:
                intent = new Intent(AdminUpgradeAct.this, AdminMatrixAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;
            case R.id.admin_training_bt:
                intent = new Intent(AdminUpgradeAct.this, AdminTrainingStatusAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;
            case R.id.admin_update_bt:
                intent = new Intent(AdminUpgradeAct.this, AdminUpdateAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;
            case R.id.admin_dashboard_bt:
                new CommonUtil().setHideDialog(new TwoButtonPopUpDialog(this, this,"GODB"));
                break;
            case R.id.admin_all_download_bt:
//                Log.e(Tag,"ChildList size1 : " + Admin_ChildList.size());
                Log.e("test","childList size : " + Admin_ChildList.size());
                if(Admin_ChildList.size() > 0) {
                    for(int i = 0; i < Admin_ChildListContentQ.size(); i++){
                        Admin_ChildList.get(0).add(Admin_ChildListContentQ.get(i));
                    }
//                    Log.e(Tag,"ChildList size2 : " + Admin_ChildList.get(0).size());
                    new CommonUtil().setHideDialog(new TwoButtonPopUpDialog(Act, Admin_ChildList.get(0), "UPGRADE"));
                } else if(Admin_ChildListContentQ.size() > 0){
                    Admin_ChildList.add(new ArrayList<UpgradeInfo>());
                    for(int i = 0; i < Admin_ChildListContentQ.size(); i++){
                        Admin_ChildList.get(0).add(Admin_ChildListContentQ.get(i));
                    }
                    new CommonUtil().setHideDialog(new TwoButtonPopUpDialog(Act, Admin_ChildList.get(0), "UPGRADE"));
                } else
                    Si.getMassage("UPDATE_SAVE", pref.getString("LOGIN_LANGUAGE",""), this, Toast.LENGTH_SHORT);
                Log.e("test","??????? :" + "?????????");
                break;
            case R.id.admin_help_bt:
                intent = new Intent(AdminUpgradeAct.this, GuideAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);//  go Intent
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_in);
                break;

        }

    }


    public static class AdminUpgrade extends AsyncTask<String,Integer,Boolean> {

        private ProgressDialog mProgress;
        private Activity mAct;

        public AdminUpgrade(Activity act) {
            mAct = act;
        }

        protected void onPreExecute()
        {
            ;
            // show progress bar or something
            mProgress = new ProgressDialog(mAct);
            mProgress.setMessage("Loading...");
            mProgress.setIndeterminate(true);
            mProgress.setCancelable(true);
            mProgress.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
             if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
                new CommonUtil().setHideNavigationBar(mProgress.getWindow().getDecorView());
            }
            mProgress.show();
            mProgress.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        }

        @Override
        protected Boolean doInBackground(String... str) {

            xml = new XmlSAXParser3(mAct,"VersionFile");
            Admin_GroupList = xml.getGroupList();
            Admin_ChildList = xml.getChildList();
            Admin_ChildListContentQ = xml.getChildListQ();
//            Log.e(Tag,"Admin_ChildListContentQ : " + Admin_ChildListContentQ.size());
//        Admin_GroupList = Tdao.getStatusDatas(Si.getYear(), Si.getQuarter(), LOGIN_CARRIER);
            Log.e("test","dis : " + str);
            mProgress.dismiss();



            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // 커스텀 어댑터를 생성한다.
            Admin_UpgradeExpandableAdapter = new UpgradeExpandableAdapter(mAct, Admin_GroupList, Admin_ChildList, Admin_ChildListContentQ);

            // 리스트뷰에 어댑터를 세팅한다.
            Admin_ExpandableListView.setAdapter(Admin_UpgradeExpandableAdapter);
            if(Admin_GroupList.size() > 0)
                Admin_ExpandableListView.expandGroup(0,true);
        }
    }

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
        Log.e("TrainingStatusAct", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("TrainingStatusAct", "onResume");
         if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
            new CommonUtil().setHideNavigationBar(getWindow().getDecorView());
        }
    }

    @Override
    protected void onUserLeaveHint(){
        super.onUserLeaveHint();
        Log.e("TrainingStatusAct", "onUserLeaveHint");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.e("TrainingStatusAct", "onPause");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("TrainingStatusAct", "onStop");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.e("TrainingStatusAct", "onDestroy");
    }

    /** 11대응 퍼미션체크 메서드 **/
/*    private void checkPermission(String permission, int requestCode){
        Log.e("test","checkpermission ");
        if (ContextCompat.checkSelfPermission(Act,permission) == PackageManager.PERMISSION_DENIED){
            Log.e("test","거절시  :");
            Log.e("test","permission name : " + permission);
            ActivityCompat.requestPermissions(Act, new String[] {permission}, requestCode);
        }else{
            Log.e("test","권한 승인 완료");
        }
    }*/

    /**권한 허용 여부 true/flase*/
/*    private boolean permissionCheck(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            Log.e("test","En ture?flase???? :" + Environment.isExternalStorageManager());
            Log.e("test","isExternalStorgeManger? ");
            if (Environment.isExternalStorageManager() == false){
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package",getApplicationContext().getPackageName(), null);
                intent.setData(uri);
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }else{
                Log.e("test","true일때 여기");
                new AdminUpgrade(Act).execute();
            }

            return Environment.isExternalStorageManager();
        }else{
            Log.e("test","else?");
            int result = ContextCompat.checkSelfPermission(Act, Manifest.permission.READ_EXTERNAL_STORAGE);
            int result1 = ContextCompat.checkSelfPermission(Act, WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
        }
    }*/

    /**퍼미션요청*/
/*    private void requestPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s" , getApplicationContext().getPackageName())));
                startActivityForResult(intent,1);
            }catch (Exception e){
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent,1);
            }
        }else{
            ActivityCompat.requestPermissions(Act, new String[]{WRITE_EXTERNAL_STORAGE}, 1);
        }
    }*/


/*    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.e("test","request code : " + requestCode);

        if (requestCode == 1){
            Log.e("test","통과");
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("test", "권한 승인");
                new AdminUpgrade(Act).execute();
            }else{
                Log.e("test","권한 거절");
            }
        }
    }*/


}
