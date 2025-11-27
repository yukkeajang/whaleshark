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
import android.widget.Toast;

import com.togetherseatech.whaleshark.Db.History.TrainingDao;
import com.togetherseatech.whaleshark.Db.History.TrainingInfo;
import com.togetherseatech.whaleshark.Db.Member.MemberInfo;
import com.togetherseatech.whaleshark.util.CommonUtil;
import com.togetherseatech.whaleshark.util.SelectItems;
import com.togetherseatech.whaleshark.util.TextFontUtil;

import java.util.List;

/**
 * Created by seonghak on 2017. 11. 3..
 */

public class TestEndAct extends Activity implements View.OnClickListener{

    private Button DashBoard_Bt, Test_Bt, History_Bt, Help_Bt;
    private ImageView Te_Language_Iv;

    private Intent intent;
    private SharedPreferences pref;
    private String LOGIN_LANGUAGE;
    private String Type;
    private MemberInfo Mi;
    private Bundle M;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_end_act);

        pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
        LOGIN_LANGUAGE = pref.getString("LOGIN_LANGUAGE", "");
        Type = (String)getIntent().getExtras().get("Type");
        /*** Bundle로 받는 부분 classException 에러 수정후 코드 **/
        M = getIntent().getBundleExtra("MemberInfo");
        Mi = M.getParcelable("MemberInfo");
        Log.e("test","surName : " + Mi.getSurName() + "FirstName : " + Mi.getFirstName());
        /*** Bundle로 받는 부분 classException 에러 수정후 코드 **/
        //Mi = (MemberInfo)getIntent().getExtras().get("MemberInfo");

        initSetting();
        initEventListener();

        if("KR".equals(LOGIN_LANGUAGE)) {
            Te_Language_Iv.setImageResource(R.mipmap.language_kr);
        }else if("ENG".equals(LOGIN_LANGUAGE)) {
            Te_Language_Iv.setImageResource(R.mipmap.language_eng);
        }
    }

    private void initSetting() {

        DashBoard_Bt = (Button)findViewById(R.id.end_test_dashboard_bt);
        Test_Bt = (Button)findViewById(R.id.end_test_bt);

        if("T".equals(Type))
            Test_Bt.setBackgroundResource(R.mipmap.evaluation_on);
        else if("R".equals(Type))
            Test_Bt.setBackgroundResource(R.mipmap.new_regulation_bt_on);
        else
            Test_Bt.setBackgroundResource(R.mipmap.training_on);

        History_Bt = (Button)findViewById(R.id.end_test_history_bt);
        Help_Bt = (Button)findViewById(R.id.end_test_help_bt);
        Te_Language_Iv = (ImageView) findViewById(R.id.end_test_language_iv);
    }

    private void initEventListener() {
        Help_Bt.setOnClickListener(this);
        DashBoard_Bt.setOnClickListener(this);
        History_Bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.end_test_dashboard_bt :
                Bundle m = new Bundle();
                m.putParcelable("MemberInfo",Mi);
                intent = new Intent(this, DashBoardMainAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("MemberInfo", m);
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;
            case R.id.end_test_history_bt :
                if("T".equals(Type)){

                    intent = new Intent(this, HistoryMainAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                    intent.putExtra("Type", "G");
                }else{

                    intent = new Intent(this, TrainingHistoryAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                    intent.putExtra("Type", Type);
                }

                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("MemberInfo", M);
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                break;
            case R.id.report_help_bt :
                intent = new Intent(TestEndAct.this, GuideAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);//  go Intent
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_in);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
         if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
            new CommonUtil().setHideNavigationBar(getWindow().getDecorView());
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
}
