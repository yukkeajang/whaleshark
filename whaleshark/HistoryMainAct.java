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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.togetherseatech.whaleshark.Db.History.TrainingDao;
import com.togetherseatech.whaleshark.Db.History.TrainingInfo;
import com.togetherseatech.whaleshark.Db.Member.MemberInfo;
import com.togetherseatech.whaleshark.util.CommonUtil;
import com.togetherseatech.whaleshark.util.SelectItems;
import com.togetherseatech.whaleshark.util.TextFontUtil;

import java.util.ArrayList;

/**
 * Created by bluepsh on 2017-10-19.
 */

public class HistoryMainAct extends Activity implements View.OnClickListener {

    private Button DashBoard_Bt, Training_Bt, History_Bt, Help_Bt, Admin_LogOut_Bt, Admin_ShipStaffs_Bt, Admin_Training_Bt, Admin_Update_Bt, Admin_Upgrade_Bt;
    private TextView HistoryMain_Name_tv;
    private ImageView HistoryMain_Language_Iv;

    private String Type;
    private Intent intent;
    private TrainingDao Tdao;
    private SharedPreferences pref;
    private TextFontUtil tf;
    public static MemberInfo Mi;
    private SelectItems Si;
    private String LOGIN_LANGUAGE;
    public static Activity Act;
    public static int Position = -1;
    // 액티비티에 보여지는 리스트뷰
    private ListView HistoryListView;
    // 리스트뷰에 사용할 어댑터
    private listView_TestHistory_Adapter mAdapter;
    // 어댑터 및 리스트뷰에 사용할 데이터 리스트
    public static ArrayList<TrainingInfo> TiList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Act = this;
        pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
        LOGIN_LANGUAGE = pref.getString("LOGIN_LANGUAGE", "");
        /**MemberInfo 수정**/
        Bundle M = getIntent().getBundleExtra("MemberInfo");
        Mi = M.getParcelable("MemberInfo");
        Log.v("test","Mi : " + Mi);
//        Mi = (MemberInfo)getIntent().getExtras().get("MemberInfo");
        Type = getIntent().getExtras().get("Type").toString();
        tf = new TextFontUtil();
        Tdao = new TrainingDao(this);
        int Xml = 0;
        if("G".equals(Type))
            Xml = R.layout.historymain_act;
        else
            Xml = R.layout.history_admin_act;

        setContentView(Xml);

//        Log.e("HistoryMainAct", Mi.getIdx()+"");
        initSetting();
        initEventListener();

        TiList = new ArrayList<>();
        TiList = Tdao.getHistoryData(Mi.getIdx());
        // 커스텀 어댑터를 생성한다.
        mAdapter = new listView_TestHistory_Adapter(this, R.layout.history_row, TiList);

        // 리스트뷰에 어댑터를 세팅한다.
        HistoryListView.setAdapter(mAdapter);
    }

    private void initSetting() {
        if("G".equals(Type)){
            DashBoard_Bt = (Button)findViewById(R.id.history_dashboard_bt);
            Training_Bt = (Button)findViewById(R.id.history_test_bt);
            History_Bt = (Button)findViewById(R.id.history_history_bt);
            History_Bt.setPressed(true);
//            History_Bt.setBackgroundResource(R.mipmap.history_on);
        }else{
            Admin_LogOut_Bt = (Button)findViewById(R.id.history_logout_bt);
            Admin_ShipStaffs_Bt = (Button)findViewById(R.id.history_shipstaffs_bt);
            Admin_Training_Bt = (Button)findViewById(R.id.history_training_bt);
            Admin_Training_Bt.setBackgroundResource(R.mipmap.training_status_on);
            Admin_Update_Bt = (Button)findViewById(R.id.history_update_bt);
            Admin_Upgrade_Bt = (Button)findViewById(R.id.history_upgrade_bt);
        }

        Help_Bt = (Button)findViewById(R.id.history_help_bt);

        HistoryMain_Name_tv = (TextView)findViewById(R.id.history_name_tv);
        HistoryMain_Name_tv.setText(Mi.getSurName() + " " + Mi.getFirstName());
        tf.setNanumSquareL(this, HistoryMain_Name_tv);

        HistoryMain_Language_Iv = (ImageView)findViewById(R.id.history_language_iv);

        if("KR".equals(LOGIN_LANGUAGE))
            HistoryMain_Language_Iv.setImageResource(R.mipmap.language_kr);
        else if("ENG".equals(LOGIN_LANGUAGE))
            HistoryMain_Language_Iv.setImageResource(R.mipmap.language_eng);

        HistoryListView = (ListView)findViewById(R.id.history_lv);

    }

	private void initEventListener() {
        Help_Bt.setOnClickListener(this);

        if("G".equals(Type)){
            DashBoard_Bt.setOnClickListener(this);
            Training_Bt.setOnClickListener(this);
        }else{
            Admin_LogOut_Bt.setOnClickListener(this);
            Admin_ShipStaffs_Bt.setOnClickListener(this);
            Admin_Training_Bt.setOnClickListener(this);
            Admin_Update_Bt.setOnClickListener(this);
            Admin_Upgrade_Bt.setOnClickListener(this);
        }
	}

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.history_dashboard_bt:
                /**MemberInfo 수정 **/
                Bundle history_dashboard_m = new Bundle();
                history_dashboard_m.putParcelable("MemberInfo",Mi);
                intent = new Intent(this, DashBoardMainAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("MemberInfo", history_dashboard_m);
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

                /*intent = new Intent(this, DashBoardMainAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("MemberInfo", Mi);
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);*/
                break;
            case R.id.history_test_bt:
                Bundle history_test_m = new Bundle();
                history_test_m.putParcelable("MemberInfo",Mi);
                intent = new Intent(this, CrewEvaluationAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("MemberInfo", history_test_m);
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                /*intent = new Intent(this, CrewEvaluationAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("MemberInfo", Mi);
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);*/
                break;
            case R.id.history_logout_bt:
                new TwoButtonPopUpDialog(this,"LOGOUT").show();
                break;
            case R.id.history_shipstaffs_bt:
                intent = new Intent(this, AdminAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;
            case R.id.history_training_bt:
                intent = new Intent(this, AdminTrainingStatusAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;
            case R.id.history_update_bt:
                intent = new Intent(this, AdminUpdateAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                break;
            case R.id.history_upgrade_bt:
                intent = new Intent(this, AdminUpgradeAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                break;
            case R.id.history_help_bt:
                intent = new Intent(HistoryMainAct.this, GuideAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);//  go Intent
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_in);
                break;

        }

    }

    public static void getCapture(String position) {
        int Position = Integer.valueOf(position);
        Log.v("test","getCapture  Pos : " + Position);
//        Bundle m = new Bundle();
//        m.putParcelable("MemberInfo",Mi);
        Intent intent = new Intent(Act, CaptureAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("MemberInfo", Mi);
        intent.putExtra("TrainingInfo", TiList.get(Position));
        Act.startActivity(intent);//  go Intent

    }

    /*private void refresh( String $inputValue ) {
        _arrAdapter.add( $inputValue ) ;
        _arrAdapter.notifyDataSetChanged() ;
    }*/

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
        Log.e("HistoryMainAct", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("HistoryMainAct", "onResume");
         if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
            new CommonUtil().setHideNavigationBar(getWindow().getDecorView());
        }
    }

    @Override
    protected void onUserLeaveHint(){
        super.onUserLeaveHint();
        Log.e("HistoryMainAct", "onUserLeaveHint");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.e("HistoryMainAct", "onPause");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("HistoryMainAct", "onStop");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.e("HistoryMainAct", "onDestroy");
    }


}
