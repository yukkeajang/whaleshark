package com.togetherseatech.whaleshark;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.togetherseatech.whaleshark.Db.History.TrainingDao;
import com.togetherseatech.whaleshark.Db.History.TrainingInfo;
import com.togetherseatech.whaleshark.Db.Member.MemberDao;
import com.togetherseatech.whaleshark.Db.Member.MemberInfo;
import com.togetherseatech.whaleshark.Db.Training.TrainingsDao;
import com.togetherseatech.whaleshark.Db.Training.TrainingsInfo;
import com.togetherseatech.whaleshark.util.CommonUtil;
import com.togetherseatech.whaleshark.util.SelectItems;
import com.togetherseatech.whaleshark.util.TextFontUtil;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * Created by bluepsh on 2019-11-06.
 */

public class AdminMatrixAct extends Activity implements View.OnClickListener {

    private Intent intent;
    private Button Admin_LogOut_Bt, Admin_ShipStaffs_Bt, Admin_Matrix_Bt, Admin_Training_Bt, Admin_Update_Bt, Admin_Upgrade_Bt, Admin_Help_Bt, Admin_Dashboard_Bt;
    private Button Admin_RI_Bt, Admin_G_Bt, Admin_NR_Bt, Admin_Edit_Bt, Admin_Save_Bt, Admin_Reset_Bt;
    private ImageView Admin_Language_Iv, Admin_Personal_Iv;//, Admin_Matrix_Sv_Iv;
//    private ScrollView Admin_Matrix_P_Sv;
    private TextView Admin_Name_Tv, Admin_Title_Tv;
    // 액티비티에 보여지는 리스트뷰
    private ListView Admin_ListView;
    // 리스트뷰에 사용할 어댑터
    private listView_Matrix_Adapter mAdapter;
    // 어댑터 및 리스트뷰에 사용할 데이터 리스트
    private List<TrainingsInfo> TsList;
//    private List<RelativeLayout> RlList;
//    private TrainingsInfo Tsi;
    private TrainingsDao Tsdao;

    private String LOGIN_LANGUAGE, VSL, Type = "P";
    private int LOGIN_CARRIER;
    private SharedPreferences pref;
    private SelectItems Si;
    private TextFontUtil Tf;
    public static Activity Act;
    public static Boolean editCheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_matrix_act);

        Act = this;
        pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
        Si = new SelectItems();
        Tf = new TextFontUtil();
        Tsdao = new TrainingsDao(this);
        LOGIN_LANGUAGE = pref.getString("LOGIN_LANGUAGE", "");
        LOGIN_CARRIER = pref.getInt("LOGIN_CARRIER", 0);
        VSL = pref.getString("VSL", "");
        initSetting();
        initEventListener();
        
        TsList = new ArrayList<>();
//        RlList = new ArrayList<>();
        // 커스텀 어댑터를 생성한다.
        Log.e("test","TsListSize : " + TsList.size());
        mAdapter = new listView_Matrix_Adapter(this, R.layout.matrix_trainings_row, TsList);

        // 리스트뷰에 어댑터를 세팅한다.
        Admin_ListView.setAdapter(mAdapter);

    }

    private void initSetting() {
        Admin_LogOut_Bt = (Button)findViewById(R.id.admin_logout_bt);
        Admin_ShipStaffs_Bt = (Button)findViewById(R.id.admin_shipstaffs_bt);
        Admin_Matrix_Bt = (Button)findViewById(R.id.admin_matrix_bt);
        Admin_Matrix_Bt.setBackgroundResource(R.mipmap.matrix_on);
        Admin_Training_Bt = (Button)findViewById(R.id.admin_training_bt);
        Admin_Update_Bt = (Button)findViewById(R.id.admin_update_bt);
        Admin_Upgrade_Bt = (Button)findViewById(R.id.admin_upgrade_bt);
        Admin_Dashboard_Bt = (Button)findViewById(R.id.admin_dashboard_bt);
        Admin_Name_Tv = (TextView)findViewById(R.id.admin_name_tv);
        Admin_Name_Tv.setText(VSL);
        Admin_Language_Iv = (ImageView)findViewById(R.id.admin_language_iv);
        Admin_Personal_Iv = (ImageView)findViewById(R.id.admin_matrix_p_iv);

        if("KR".equals(LOGIN_LANGUAGE)) {
            Admin_Language_Iv.setImageResource(R.mipmap.language_kr);
            Admin_Personal_Iv.setImageResource(R.mipmap.matrix_training_guide_k);
        } else if("ENG".equals(LOGIN_LANGUAGE)) {
            Admin_Language_Iv.setImageResource(R.mipmap.language_eng);
            Admin_Personal_Iv.setImageResource(R.mipmap.matrix_training_guide_e);
        }
        Admin_Help_Bt = (Button)findViewById(R.id.admin_help_bt);
//        Admin_Matrix_P_Sv = (ScrollView)findViewById(R.id.admin_matrix_p_sv);
//        Admin_Matrix_Sv_Iv = (ImageView)findViewById(R.id.admin_matrix_sv_iv);
        Admin_Title_Tv = (TextView)findViewById(R.id.admin_matrix_title_tv);
        Tf.setNanumSquareRoundR(this,Admin_Title_Tv);
        Admin_ListView = (ListView)findViewById(R.id.admin_matrix_lv);
//        Admin_ListView.setEnabled(false);
        Admin_RI_Bt = (Button)findViewById(R.id.admin_matrix_ri_bt);
        Admin_G_Bt = (Button)findViewById(R.id.admin_matrix_g_bt);
        Admin_NR_Bt = (Button)findViewById(R.id.admin_matrix_nr_bt);
        Admin_Edit_Bt  = (Button)findViewById(R.id.admin_matrix_edit_bt);
        Admin_Save_Bt = (Button)findViewById(R.id.admin_matrix_save_bt);
        Admin_Reset_Bt = (Button)findViewById(R.id.admin_matrix_reset_bt);
    }

	private void initEventListener() {
        Admin_LogOut_Bt.setOnClickListener(this);
        Admin_ShipStaffs_Bt.setOnClickListener(this);
        Admin_Training_Bt.setOnClickListener(this);
        Admin_Update_Bt.setOnClickListener(this);
        Admin_Upgrade_Bt.setOnClickListener(this);
        Admin_Dashboard_Bt.setOnClickListener(this);
        Admin_Help_Bt.setOnClickListener(this);
//        Admin_ListView.setOnItemClickListener(this);
        Admin_RI_Bt.setOnClickListener(this);
        Admin_G_Bt.setOnClickListener(this);
        Admin_NR_Bt.setOnClickListener(this);
        Admin_Edit_Bt.setOnClickListener(this);
        Admin_Save_Bt.setOnClickListener(this);
        Admin_Reset_Bt.setOnClickListener(this);
	}

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.admin_logout_bt:
                new CommonUtil().setHideDialog(new TwoButtonPopUpDialog(this,"LOGOUT"));
                break;
            case R.id.admin_shipstaffs_bt:
                intent = new Intent(AdminMatrixAct.this, AdminAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;
            case R.id.admin_training_bt:
                intent = new Intent(AdminMatrixAct.this, AdminTrainingStatusAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                break;
            case R.id.admin_update_bt:
                intent = new Intent(AdminMatrixAct.this, AdminUpdateAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                break;
            case R.id.admin_upgrade_bt:
                intent = new Intent(AdminMatrixAct.this, AdminUpgradeAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                break;
            case R.id.admin_help_bt:
                intent = new Intent(AdminMatrixAct.this, GuideAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);//  go Intent
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_in);
                break;
            case R.id.admin_dashboard_bt:
                new CommonUtil().setHideDialog(new TwoButtonPopUpDialog(this, this,"GODB"));
                break;
            case  R.id.admin_matrix_ri_bt:
                if("KR".equals(LOGIN_LANGUAGE)) {
                    Admin_Personal_Iv.setImageResource(R.mipmap.matrix_training_guide_k);
                } else if("ENG".equals(LOGIN_LANGUAGE)) {
                    Admin_Personal_Iv.setImageResource(R.mipmap.matrix_training_guide_e);
                }

                Admin_Edit_Bt.setVisibility(View.INVISIBLE);
                Admin_Save_Bt.setVisibility(View.INVISIBLE);
                Admin_Reset_Bt.setVisibility(View.INVISIBLE);
                Admin_Title_Tv.setVisibility(View.INVISIBLE);
                Admin_ListView.setVisibility(View.INVISIBLE);

                Admin_Title_Tv.setText("Personal Training");
                break;
            case  R.id.admin_matrix_g_bt:
                Type = "G";
                editCheck = false;
                Admin_Personal_Iv.setImageResource(R.mipmap.matrix_trainings_list_bg);
//                Admin_Matrix_P_Sv.setVisibility(View.INVISIBLE);
                Admin_Edit_Bt.setVisibility(View.VISIBLE);
                Admin_Save_Bt.setVisibility(View.INVISIBLE);
                Admin_Reset_Bt.setVisibility(View.INVISIBLE);
                Admin_Title_Tv.setVisibility(View.VISIBLE);
                Admin_ListView.setVisibility(View.VISIBLE);

                Admin_Title_Tv.setText("General Training");
                TsList = Tsdao.getMatrixs(Type,"");
                Log.e("test","Matrix : " + Tsdao.getMatrixs(Type,""));
//                Log.e("test","TsListTT : " + Tsdao.getMatrixs(Type,"").toString());
                mAdapter.setList(TsList);
                mAdapter.notifyDataSetChanged();
                break;
            case  R.id.admin_matrix_nr_bt:
                Type = "R";
                editCheck = false;
                Admin_Personal_Iv.setImageResource(R.mipmap.matrix_trainings_list_bg);
//                Admin_Matrix_P_Sv.setVisibility(View.INVISIBLE);
                Admin_Edit_Bt.setVisibility(View.VISIBLE);
                Admin_Save_Bt.setVisibility(View.INVISIBLE);
                Admin_Reset_Bt.setVisibility(View.INVISIBLE);
                Admin_Title_Tv.setVisibility(View.VISIBLE);
                Admin_ListView.setVisibility(View.VISIBLE);

                Admin_Title_Tv.setText("New Regulation Training");
                TsList = Tsdao.getMatrixs(Type,"");
                mAdapter.setList(TsList);
                mAdapter.notifyDataSetChanged();
                break;
            case  R.id.admin_matrix_edit_bt:
                editCheck = true;
                Admin_Edit_Bt.setVisibility(View.INVISIBLE);
                Admin_Save_Bt.setVisibility(View.VISIBLE);
                Admin_Reset_Bt.setVisibility(View.VISIBLE);

                break;
            case  R.id.admin_matrix_save_bt:
                TsList = mAdapter.getList();
                for(TrainingsInfo tsi : TsList) {
//                    Log.e("AdminMatrixAct", "SAVE : " + tsi.getNavigation_senior() + "/" + tsi.getNavigation_junior() + "/" + tsi.getNavigation_rating()
//                            + "/" + tsi.getEngine_senior() + "/" + tsi.getEngine_junior() + "/" + tsi.getEngine_rating() + "/" + tsi.getOthers_rating());
                    Tsdao.updateMatrix(tsi);
                }
                editCheck = false;
                Admin_Edit_Bt.setVisibility(View.VISIBLE);
                Admin_Save_Bt.setVisibility(View.INVISIBLE);
                Admin_Reset_Bt.setVisibility(View.INVISIBLE);
                break;
            case  R.id.admin_matrix_reset_bt:
                TsList = Si.getGTMatrix(Type);
                mAdapter.setList(TsList);
                mAdapter.notifyDataSetChanged();
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
        Log.e("AdminMatrixAct", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("AdminMatrixAct", "onResume");
        CommonUtil Cu = new CommonUtil();
         if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
            Cu.setHideNavigationBar(getWindow().getDecorView());
        }
    }

    @Override
    protected void onUserLeaveHint(){
        super.onUserLeaveHint();
        Log.e("AdminMatrixAct", "onUserLeaveHint");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.e("AdminMatrixAct", "onPause");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("AdminMatrixAct", "onStop");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.e("AdminMatrixAct", "onDestroy");
    }

}
