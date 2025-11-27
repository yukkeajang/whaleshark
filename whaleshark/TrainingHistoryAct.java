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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.togetherseatech.whaleshark.Db.Member.MemberInfo;
import com.togetherseatech.whaleshark.Db.Training.TrainingsDao;
import com.togetherseatech.whaleshark.Db.Training.TrainingsInfo;
import com.togetherseatech.whaleshark.util.CommonUtil;
import com.togetherseatech.whaleshark.util.SelectItems;
import com.togetherseatech.whaleshark.util.TextFontUtil;

import java.util.ArrayList;

/**
 * Created by bluepsh on 2017-10-19.
 */

public class TrainingHistoryAct extends Activity implements View.OnClickListener {

    private RelativeLayout TrainingHistory_Rl;
    private Button DashBoard_Bt, Training_Bt, History_Bt, Help_Bt;
    private ImageView HistoryMain_Language_Iv;
    private TextView HistoryMain_Name_Tv;
    private static MemberInfo Mi;
    private static String Type;
    private Intent intent;
    private TrainingsDao Tsdao;
    private SharedPreferences pref;
    private String LOGIN_LANGUAGE;
    private int LOGIN_CARRIER;
    public static Activity Act;
    public static int Position = -1;
    // 액티비티에 보여지는 리스트뷰
    private ListView HistoryListView;
    // 리스트뷰에 사용할 어댑터
    private listView_History_Adapter mAdapter;
    // 어댑터 및 리스트뷰에 사용할 데이터 리스트
    public static ArrayList<TrainingsInfo> TsiList;
    private Bundle m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.training_history_act);

        Act = this;
        pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
        LOGIN_LANGUAGE = pref.getString("LOGIN_LANGUAGE", "");
        LOGIN_CARRIER = pref.getInt("LOGIN_CARRIER", 0);
        Type = getIntent().getStringExtra("Type");
        /*** Bundle로 받는 부분 classException 에러 수정후 코드 **/
        Bundle M = getIntent().getBundleExtra("MemberInfo");
        Mi = M.getParcelable("MemberInfo");

        Log.e("test","Mi : " + Mi);

        /*** Bundle로 받는 부분 classException 에러 수정후 코드 **/
        //Mi = (MemberInfo)getIntent().getExtras().get("MemberInfo");

        Tsdao = new TrainingsDao(this);
//        Tsdao.updateGt2();

        initSetting();
        initEventListener();

        HistoryMain_Name_Tv.setText(Mi.getSurName() + " " + Mi.getFirstName());

        TsiList = Tsdao.getHistory(Type, LOGIN_CARRIER, Mi);
        // 커스텀 어댑터를 생성한다.
        mAdapter = new listView_History_Adapter(this, R.layout.training_history_row, TsiList, Type);

        // 리스트뷰에 어댑터를 세팅한다.
        HistoryListView.setAdapter(mAdapter);
    }

    private void initSetting() {
        TextFontUtil tf = new TextFontUtil();
        TrainingHistory_Rl = (RelativeLayout)findViewById(R.id.history_main_rl);
        DashBoard_Bt = (Button)findViewById(R.id.history_dashboard_bt);
        Training_Bt = (Button)findViewById(R.id.history_training_bt);
        History_Bt = (Button)findViewById(R.id.history_bt);
        History_Bt.setBackgroundResource(R.mipmap.history_on);

        if("R".equals(Type)){
            TrainingHistory_Rl.setBackgroundResource(R.mipmap.new_regulation_history_bg);
            Training_Bt.setBackgroundResource(R.drawable.new_regulation_bt);
        }else{
            TrainingHistory_Rl.setBackgroundResource(R.mipmap.general_history_bg);
            Training_Bt.setBackgroundResource(R.drawable.training_bt);
        }

        Help_Bt = (Button)findViewById(R.id.history_help_bt);
        HistoryMain_Name_Tv = (TextView)findViewById(R.id.history_name_tv);
        tf.setNanumSquareL(this, HistoryMain_Name_Tv);
        HistoryMain_Language_Iv = (ImageView)findViewById(R.id.history_language_iv);

        if("KR".equals(LOGIN_LANGUAGE))
            HistoryMain_Language_Iv.setImageResource(R.mipmap.language_kr);
        else if("ENG".equals(LOGIN_LANGUAGE))
            HistoryMain_Language_Iv.setImageResource(R.mipmap.language_eng);

        HistoryListView = (ListView)findViewById(R.id.history_lv);

    }

	private void initEventListener() {
        Help_Bt.setOnClickListener(this);
        DashBoard_Bt.setOnClickListener(this);
        Training_Bt.setOnClickListener(this);
	}

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.history_dashboard_bt:
                m = new Bundle();
                m.putParcelable("MemberInfo",Mi);
                intent = new Intent(this, DashBoardMainAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("MemberInfo", m);
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;
            case R.id.history_training_bt:
                if("R".equals(Type)){
                    m = new Bundle();
                    m.putParcelable("MemberInfo",Mi);
                    intent = new Intent(this, NewRegulationAct.class);
                }else if("G".equals(Type)){
                    m = new Bundle();
                    m.putParcelable("MemberInfo",Mi);
                    intent = new Intent(this, GeneralTrainingAct.class);
                }else{
                    m = new Bundle();
                    m.putParcelable("MemberInfo",Mi);
                    intent = new Intent(this,CrewEvaluationAct.class);
                }

                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("MemberInfo", m);
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;
            case R.id.history_help_bt:
                intent = new Intent(TrainingHistoryAct.this, GuideAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);//  go Intent
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_in);
                break;

        }

    }

    public static void getCapture(String position) {
        int Position = Integer.valueOf(position);
//        Log.e("getCapture",position);
//        Log.e("getCapture","TrainingsInfo "+TsiList.get(Position).getTraining_course());
        Intent intent = new Intent(Act, TrainingCaptureAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("Type", Type);
        intent.putExtra("TrainingsInfo", TsiList.get(Position));
        Act.startActivity(intent);//  go Intent

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
        Log.e("TrainingsHistoryAct", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("TrainingsHistoryAct", "onResume");
         if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
            new CommonUtil().setHideNavigationBar(getWindow().getDecorView());
        }
    }

    @Override
    protected void onUserLeaveHint(){
        super.onUserLeaveHint();
        Log.e("TrainingsHistoryAct", "onUserLeaveHint");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.e("TrainingsHistoryAct", "onPause");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("TrainingsHistoryAct", "onStop");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.e("TrainingsHistoryAct", "onDestroy");
    }


}
