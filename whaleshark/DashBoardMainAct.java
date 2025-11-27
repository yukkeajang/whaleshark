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
import android.widget.TextView;

import com.togetherseatech.whaleshark.Db.Member.MemberInfo;
import com.togetherseatech.whaleshark.util.CommonUtil;
import com.togetherseatech.whaleshark.util.TextFontUtil;

/**
 * Created by seonghak on 2017. 11. 3..
 */

public class DashBoardMainAct extends Activity implements View.OnClickListener{

    private Button DashBoard_Back_Bt, DashBoard_Bt, Help_Bt, Personal_Test_Bt, General_Training_Bt, New_Regulation_Bt;//, Personal_Training_Bt
    private TextView DashBoard_Name_Tv;
    private ImageView DashBoard_Language_Iv;
    private Intent intent;
    private SharedPreferences pref;
    private MemberInfo Mi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboardmain_act);

        pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
        /*** Bundle로 받는 부분 classException 에러 수정후 코드 **/
        Bundle m = getIntent().getBundleExtra("MemberInfo");

        Mi = m.getParcelable("MemberInfo");
        /*** Bundle로 받는 부분 classException 에러 수정전 코드 **/
        //Mi = (MemberInfo)getIntent().getExtras().get("MemberInfo");
        initSetting();
        initEventListener();
    }

    private void initSetting() {
        TextFontUtil tf = new TextFontUtil();
        DashBoard_Back_Bt = (Button)findViewById(R.id.dashboard_back_bt);
        DashBoard_Bt = (Button)findViewById(R.id.dashboard_dashboard_bt);
        DashBoard_Bt.setBackgroundResource(R.mipmap.dashboard_on);
        Help_Bt = (Button)findViewById(R.id.dashboard_help_bt);
//        Personal_Training_Bt = (Button)findViewById(R.id.dashboard_personnel_training_bt);
        Personal_Test_Bt = (Button)findViewById(R.id.dashboard_personnel_test_bt);
        General_Training_Bt = (Button)findViewById(R.id.dashboard_general_training_bt);
        New_Regulation_Bt = (Button)findViewById(R.id.dashboard_new_regulation_bt);
        DashBoard_Name_Tv = (TextView)findViewById(R.id.dashboard_name_tv);
        DashBoard_Name_Tv.setText(Mi.getSurName() + " " + Mi.getFirstName());
        tf.setNanumSquareL(this, DashBoard_Name_Tv);
        DashBoard_Language_Iv = (ImageView) findViewById(R.id.dashboard_language_iv);

        String LOGIN_LANGUAGE = pref.getString("LOGIN_LANGUAGE", "");
        if("KR".equals(LOGIN_LANGUAGE))
            DashBoard_Language_Iv.setImageResource(R.mipmap.language_kr);
        else if("ENG".equals(LOGIN_LANGUAGE))
            DashBoard_Language_Iv.setImageResource(R.mipmap.language_eng);
    }

    private void initEventListener() {
        DashBoard_Back_Bt.setOnClickListener(this);
        Help_Bt.setOnClickListener(this);
//        Personal_Training_Bt.setOnClickListener(this);
        Personal_Test_Bt.setOnClickListener(this);
        General_Training_Bt.setOnClickListener(this);
        New_Regulation_Bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.dashboard_back_bt:
                new CommonUtil().setHideDialog(new TwoButtonPopUpDialog(this,"LOGOUT"));
                break;
            case R.id.dashboard_help_bt:
                intent = new Intent(DashBoardMainAct.this, GuideAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);//  go Intent
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_in);
                break;
            /*case R.id.dashboard_personnel_training_bt:
//                intent = new Intent(DashBoardMainAct.this, CrewMemberAct.class);
                intent = new Intent(DashBoardMainAct.this, PersonalTrainingAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("MemberInfo",Mi);
                intent.putExtra("Type","Training");
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                break;*/
            case R.id.dashboard_personnel_test_bt:
//                intent = new Intent(DashBoardMainAct.this, CrewMemberAct.class);
                /*** Bundle로 받는 부분 classException 에러 수정후 코드 2021.09.16**/
                Bundle member = new Bundle();
                member.putParcelable("MemberInfo",Mi);
                intent = new Intent(DashBoardMainAct.this, CrewEvaluationAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("MemberInfo",member);
                intent.putExtra("Type","Test");
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                /** Bundle로 받는 부분 classException 에러 수정전 코드 2021.09.16**/
                /*intent = new Intent(DashBoardMainAct.this, CrewEvaluationAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("MemberInfo",Mi);
                intent.putExtra("Type","Test");
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);*/

                break;
            case R.id.dashboard_general_training_bt:
                Bundle member1 = new Bundle();
                member1.putParcelable("MemberInfo",Mi);
                intent = new Intent(DashBoardMainAct.this, GeneralTrainingAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("MemberInfo",member1);
                startActivity(intent);//  go Intent
//                GeneralTrainingAct_.intent(this).flags(Intent.FLAG_ACTIVITY_SINGLE_TOP).start();
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                break;
            case R.id.dashboard_new_regulation_bt:
                Bundle member2 = new Bundle();
                member2.putParcelable("MemberInfo",Mi);
                intent = new Intent(DashBoardMainAct.this, NewRegulationAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("MemberInfo",member2);
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("DashBoardMainAct", "onResume");
         if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
            new CommonUtil().setHideNavigationBar(getWindow().getDecorView());
        }
    }

    // back키 동작 금지
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            return false;
        }
        return true;
    }

}
