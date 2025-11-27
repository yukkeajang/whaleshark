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

import com.togetherseatech.whaleshark.Db.Member.MemberDao;
import com.togetherseatech.whaleshark.Db.Member.MemberInfo;
import com.togetherseatech.whaleshark.util.CommonUtil;
import com.togetherseatech.whaleshark.util.SelectItems;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bluepsh on 2017-10-19.
 */

public class AdminAct extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private Intent intent;
    private String LOGIN_LANGUAGE, VSL;
    private int LOGIN_CARRIER;
    private Button Admin_LogOut_Bt, Admin_ShipStaffs_Bt, Admin_Matrix_Bt, Admin_Training_Bt,
            Admin_Upgrade_Bt, Admin_Help_Bt, Admin_Insert_Bt, Admin_Update_Bt, Admin_Dashboard_Bt;
    private ImageView Admin_Language_Iv;
    private TextView Admin_Name_Tv;
    // 액티비티에 보여지는 리스트뷰
    private ListView MemberListView;
    // 리스트뷰에 사용할 어댑터
    public static listView_Member_Adapter mAdapter;
    // 어댑터 및 리스트뷰에 사용할 데이터 리스트
    public static List<MemberInfo> MbList;
    public static Activity Act;
    private SharedPreferences pref;
    private MemberDao Mdao;
    private SelectItems Si;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_act);


        Act = this;
        pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
        LOGIN_LANGUAGE = pref.getString("LOGIN_LANGUAGE", "");
        LOGIN_CARRIER = pref.getInt("LOGIN_CARRIER", 0);
        VSL = pref.getString("VSL", "");
//        VSL = pref.getString("VSL", "") + "/" + pref.getInt("MSTER_IDX", 0);
        initSetting();
        initEventListener();

        MbList = new ArrayList<MemberInfo>();
        Log.e("test","Admin MBLIST = " + MbList.size());

        Mdao = new MemberDao(this);
        //Mdao.updateAll(); // reset 용
        Si = new SelectItems();
//        Mdao.test();
        MbList = Mdao.getMembers(Vars.DEL_N + LOGIN_CARRIER);
        Log.e("test","MBList Size : " + MbList.size());

        // 커스텀 어댑터를 생성한다.
        mAdapter = new listView_Member_Adapter(this, R.layout.amember_row, MbList);

        // 리스트뷰에 어댑터를 세팅한다.
        MemberListView.setAdapter(mAdapter);
    }

    private void initSetting() {
        Admin_LogOut_Bt = (Button)findViewById(R.id.admin_logout_bt);
        Admin_ShipStaffs_Bt = (Button)findViewById(R.id.admin_shipstaffs_bt);
        Admin_ShipStaffs_Bt.setBackgroundResource(R.mipmap.shipstaffs_on);
        Admin_Matrix_Bt = (Button)findViewById(R.id.admin_matrix_bt);
        Admin_Training_Bt = (Button)findViewById(R.id.admin_training_bt);
        Admin_Update_Bt = (Button)findViewById(R.id.admin_update_bt);
        Admin_Upgrade_Bt = (Button)findViewById(R.id.admin_upgrade_bt);
        Admin_Name_Tv = (TextView) findViewById(R.id.admin_name_tv);
        Admin_Name_Tv.setText(VSL);
        Admin_Language_Iv = (ImageView)findViewById(R.id.admin_language_iv);
        if("KR".equals(LOGIN_LANGUAGE))
            Admin_Language_Iv.setImageResource(R.mipmap.language_kr);
        else if("ENG".equals(LOGIN_LANGUAGE))
            Admin_Language_Iv.setImageResource(R.mipmap.language_eng);
        Admin_Help_Bt = (Button)findViewById(R.id.admin_help_bt);
        MemberListView = (ListView)findViewById(R.id.admin_lv);
        Admin_Insert_Bt = (Button)findViewById(R.id.admin_insert_bt);
        Admin_Dashboard_Bt = (Button)findViewById(R.id.admin_dashboard_bt);
    }

	private void initEventListener() {
        Admin_LogOut_Bt.setOnClickListener(this);
        Admin_Matrix_Bt.setOnClickListener(this);
        Admin_Training_Bt.setOnClickListener(this);
        Admin_Update_Bt.setOnClickListener(this);
        Admin_Upgrade_Bt.setOnClickListener(this);
        Admin_Help_Bt.setOnClickListener(this);
        MemberListView.setOnItemClickListener(this);
        Admin_Insert_Bt.setOnClickListener(this);
        Admin_Dashboard_Bt.setOnClickListener(this);
	}

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(this, "onItemClick", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.admin_logout_bt:
//                Toast.makeText(this, "admin_logout_bt", Toast.LENGTH_SHORT).show();
                new CommonUtil().setHideDialog(new TwoButtonPopUpDialog(this,"LOGOUT"));
                break;
            case R.id.admin_matrix_bt:
                intent = new Intent(AdminAct.this, AdminMatrixAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                break;
            case R.id.admin_training_bt:
                intent = new Intent(AdminAct.this, AdminTrainingStatusAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                break;
            case R.id.admin_update_bt:
                intent = new Intent(AdminAct.this, AdminUpdateAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                break;
            case R.id.admin_upgrade_bt:
                intent = new Intent(AdminAct.this, AdminUpgradeAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                break;
            case R.id.admin_help_bt:
                intent = new Intent(AdminAct.this, GuideAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);//  go Intent
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_in);
                break;
            case R.id.admin_insert_bt:
//                Toast.makeText(this, "insert", Toast.LENGTH_LONG).show();
                int Cnt = Mdao.getMembersCount(Vars.DEL_N + LOGIN_CARRIER);
//                Log.e("AdminAct","정원 : "+Cnt);
                if(Cnt >= 30){
                    Si.getMassage("ADD", pref.getString("LOGIN_LANGUAGE", ""), this, Toast.LENGTH_SHORT);
                }else {
                    new CommonUtil().setHideDialog(new AdminMemberPopUpDialog(this, null));
                }
                break;
            case R.id.admin_dashboard_bt:
                new CommonUtil().setHideDialog(new TwoButtonPopUpDialog(this,"GODB"));
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
        Log.e("AdminAct", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("AdminAct", "onResume");
//        Log.e("LogInAct", "onResume");
         if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
            new CommonUtil().setHideNavigationBar(getWindow().getDecorView());
        }
    }

    @Override
    protected void onUserLeaveHint(){
        super.onUserLeaveHint();
        Log.e("AdminAct", "onUserLeaveHint");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.e("AdminAct", "onPause");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("AdminAct", "onStop");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.e("AdminAct", "onDestroy");
    }

}
