package com.togetherseatech.whaleshark;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.togetherseatech.whaleshark.Db.History.TrainingDao;
import com.togetherseatech.whaleshark.Db.History.TrainingInfo;
import com.togetherseatech.whaleshark.Db.Training.TrainingsDao;
import com.togetherseatech.whaleshark.Db.Training.TrainingsInfo;
import com.togetherseatech.whaleshark.util.CommonUtil;
import com.togetherseatech.whaleshark.util.SelectItems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by bluepsh on 2017-10-19.
 */

public class AdminUpdateAct extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private Intent intent;
    private Button Admin_LogOut_Bt, Admin_ShipStaffs_Bt, Admin_Matrix_Bt, Admin_Training_Bt, Admin_Update_Bt,
            Admin_Upgrade_Bt, Admin_Dashboard_Bt, Admin_Help_Bt, Admin_Download_Bt, Admin_Submit_Bt;
    private ImageView Admin_Language_Iv;
    private TextView Admin_Name_Tv;
    // 액티비티에 보여지는 리스트뷰
    private ListView MemberListView;
    // 리스트뷰에 사용할 어댑터
    public static listView_Update_Adapter mAdapter;
    // 어댑터 및 리스트뷰에 사용할 데이터 리스트
    public static List<TrainingInfo> TiList;
    public static List<TrainingsInfo> TsiList;
    private SharedPreferences pref;
    private String LOGIN_LANGUAGE, VSL;
    private int LOGIN_CARRIER;
    private TrainingDao Tdao;
    private TrainingsDao Tsdao;
    private SelectItems Si;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_update_act);

        pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
        LOGIN_LANGUAGE = pref.getString("LOGIN_LANGUAGE", "");
        LOGIN_CARRIER = pref.getInt("LOGIN_CARRIER", 0);
        VSL = pref.getString("VSL", "");
        Si = new SelectItems();
        initSetting();
        initEventListener();
        TiList = new ArrayList<>();
        TsiList = new ArrayList<>();

        Tdao = new TrainingDao(this);
        Tsdao = new TrainingsDao(this);

        TiList = Tdao.getUpdateData(LOGIN_CARRIER);
        Log.e("test","TiListSize : " + TiList.size());

        /*if(TiList.size() == 0) {
            TiList.add(null);
            MemberListView.setEnabled(false);
        }*/
//        mAdapter.notifyDataSetChanged();
        // 커스텀 어댑터를 생성한다.
        mAdapter = new listView_Update_Adapter(this, R.layout.admin_update_row, TiList);

        // 리스트뷰에 어댑터를 세팅한다.
        MemberListView.setAdapter(mAdapter);
    }

    private void initSetting() {
        Admin_LogOut_Bt = (Button)findViewById(R.id.admin_logout_bt);
        Admin_ShipStaffs_Bt = (Button)findViewById(R.id.admin_shipstaffs_bt);
        Admin_Matrix_Bt = (Button)findViewById(R.id.admin_matrix_bt);
        Admin_Training_Bt = (Button)findViewById(R.id.admin_training_bt);
        Admin_Update_Bt = (Button)findViewById(R.id.admin_update_bt);
        Admin_Update_Bt.setBackgroundResource(R.mipmap.update_on);
        Admin_Upgrade_Bt = (Button)findViewById(R.id.admin_upgrade_bt);
        Admin_Dashboard_Bt = (Button)findViewById(R.id.admin_dashboard_bt);
        Admin_Name_Tv = (TextView) findViewById(R.id.admin_name_tv);
        Admin_Name_Tv.setText(VSL);
        Admin_Language_Iv = (ImageView)findViewById(R.id.admin_language_iv);
        if("KR".equals(LOGIN_LANGUAGE))
            Admin_Language_Iv.setImageResource(R.mipmap.language_kr);
        else if("ENG".equals(LOGIN_LANGUAGE))
            Admin_Language_Iv.setImageResource(R.mipmap.language_eng);
        Admin_Help_Bt = (Button)findViewById(R.id.admin_help_bt);
        MemberListView = (ListView)findViewById(R.id.admin_lv);
        Admin_Download_Bt = (Button)findViewById(R.id.admin_download_bt);
        Admin_Submit_Bt = (Button)findViewById(R.id.admin_submit_bt);
    }

	private void initEventListener() {
        Admin_LogOut_Bt.setOnClickListener(this);
        Admin_ShipStaffs_Bt.setOnClickListener(this);
        Admin_Matrix_Bt.setOnClickListener(this);
        Admin_Training_Bt.setOnClickListener(this);
        Admin_Upgrade_Bt.setOnClickListener(this);
        Admin_Dashboard_Bt.setOnClickListener(this);
        Admin_Help_Bt.setOnClickListener(this);
//        MemberListView.setOnItemClickListener(this);
        Admin_Download_Bt.setOnClickListener(this);
        Admin_Submit_Bt.setOnClickListener(this);

	}

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "onItemClick", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.admin_logout_bt:
                new CommonUtil().setHideDialog(new TwoButtonPopUpDialog(this,"LOGOUT"));
                break;
            case R.id.admin_shipstaffs_bt:
                intent = new Intent(AdminUpdateAct.this, AdminAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;
            case R.id.admin_matrix_bt:
                intent = new Intent(AdminUpdateAct.this, AdminMatrixAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;
            case R.id.admin_training_bt:
                intent = new Intent(AdminUpdateAct.this, AdminTrainingStatusAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;
            case R.id.admin_upgrade_bt:
                intent = new Intent(AdminUpdateAct.this, AdminUpgradeAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                break;
            case R.id.admin_dashboard_bt:
                new CommonUtil().setHideDialog(new TwoButtonPopUpDialog(this,"GODB"));
                break;
            case R.id.admin_help_bt:
                intent = new Intent(AdminUpdateAct.this, GuideAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);//  go Intent
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_in);
                break;
            case R.id.admin_download_bt:
                if(TiList.size() > 0)
                    new CommonUtil().setHideDialog(new TwoButtonPopUpDialog(this, "DOWNLOAD"));
                else
                    Si.getMassage("UPDATE_SAVE", LOGIN_LANGUAGE, this, Toast.LENGTH_SHORT);
                break;
            case R.id.admin_submit_bt:
                if(TiList.size() > 0)
                    new CommonUtil().setHideDialog(new TwoButtonPopUpDialog(this, "UPDATE"));
                else
                    Si.getMassage("UPDATE_NO", LOGIN_LANGUAGE, this, Toast.LENGTH_SHORT);
                break;
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
        Log.e("AdminUpdateAct", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("AdminUpdateAct", "onResume");
         if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
            new CommonUtil().setHideNavigationBar(getWindow().getDecorView());
        }
    }

    @Override
    protected void onUserLeaveHint(){
        super.onUserLeaveHint();
        Log.e("AdminUpdateAct", "onUserLeaveHint");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.e("AdminUpdateAct", "onPause");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("AdminUpdateAct", "onStop");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.e("AdminUpdateAct", "onDestroy");
    }

}
