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

public class ReportCardAct extends Activity implements View.OnClickListener{

    private Button DashBoard_Bt, Test_Bt, History_Bt, Help_Bt, Rc_Next_Bt;
    private ImageView Rc_Language_Iv;
    private TextView Rc_Name_Tv, Rc_Date_Tv, Rc_Chapter_Tv, Rc_Score_Tv, Rc_Required_Tv;

    private Intent intent;
    private SharedPreferences pref;
    private TextFontUtil tf;
    private MemberInfo Mi;
    private TrainingInfo Ti;
    private SelectItems Si;
    private String LOGIN_LANGUAGE;
    private String Type;
    private TrainingDao TDao;
    private List<TrainingInfo> TiList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_card_act);

        pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
        LOGIN_LANGUAGE = pref.getString("LOGIN_LANGUAGE", "");
        tf = new TextFontUtil();
        /**MemberInfo 수정 2021.09.17**/
        Bundle M = getIntent().getBundleExtra("MemberInfo");
        Mi = M.getParcelable("MemberInfo");
        //Mi = (MemberInfo)getIntent().getExtras().get("MemberInfo");
        Ti = (TrainingInfo)getIntent().getExtras().get("TrainingInfo");
        Type = (String)getIntent().getExtras().get("Type");

        Si = new SelectItems();
        TDao = new TrainingDao(this);

        initSetting();
        initEventListener();

        if("KR".equals(LOGIN_LANGUAGE)) {
            Rc_Language_Iv.setImageResource(R.mipmap.language_kr);
        }else if("ENG".equals(LOGIN_LANGUAGE)) {
            Rc_Language_Iv.setImageResource(R.mipmap.language_eng);
        }
        Log.e("ReportCardAct","Ti IDX : "+ Ti.getIdx());
        Log.e("ReportCardAct","Ti Member_idx : "+ Ti.getMember_idx());
        Log.e("ReportCardAct","Ti Training_course : "+ Ti.getTraining_course());
        Log.e("ReportCardAct","Ti Date : "+ Ti.getDate());
        Log.e("ReportCardAct","Ti Score : "+ Ti.getScore());

        Rc_Date_Tv.setText(Ti.getDate());
        Rc_Chapter_Tv.setText(Si.getChapter(Ti.getTraining_course(),LOGIN_LANGUAGE));
//        Rc_Score_Tv.setText(Ti.getScore()+" %");

        Rc_Score_Tv.setText(Ti.getScore()+" %");
        TiList = TDao.getProblems(Ti.getIdx());
//        TiList = TDao.getAllProblem();
        String required= "";

        for(int i=0; i < TiList.size();i++){
//            Log.e("ReportCardAct","AnswerScoreList "+i+" : "+ TiList.get(i).getRelative_regulation());
            if(TiList.get(i).getRelative_regulation().trim() != null && !"".equals(TiList.get(i).getRelative_regulation().trim())) {
                required += " + " + TiList.get(i).getRelative_regulation().trim();

                if (i != TiList.size() - 1)
                    required += "\n";
//            Log.e("ReportCardAct",i+" : "+ required);
            }
        }

        Rc_Required_Tv.setText(required);

        /*if("PT".equals(Type))
            Rc_Next_Bt.setVisibility(View.INVISIBLE);
        else
            Rc_Next_Bt.setVisibility(View.VISIBLE);*/
        if("PT".equals(Type)){
            Rc_Next_Bt.setBackgroundResource(R.drawable.report_card_back_bt);
            Rc_Next_Bt.setTag(R.drawable.report_card_back_bt);
        }else{
            Rc_Next_Bt.setTag(R.mipmap.report_card_next);
        }
    }

    private void initSetting() {

        DashBoard_Bt = (Button)findViewById(R.id.report_dashboard_bt);
        Test_Bt = (Button)findViewById(R.id.report_test_bt);
        Test_Bt.setBackgroundResource(R.mipmap.evaluation_on);
        History_Bt = (Button)findViewById(R.id.report_history_bt);
        Help_Bt = (Button)findViewById(R.id.report_help_bt);
        Rc_Language_Iv = (ImageView) findViewById(R.id.report_language_iv);
        Rc_Name_Tv = (TextView)findViewById(R.id.report_name_tv);
        Rc_Name_Tv.setText(Mi.getSurName() + " " + Mi.getFirstName());
        tf.setNanumSquareL(this, Rc_Name_Tv);

        Rc_Date_Tv = (TextView)findViewById(R.id.report_date_tv);
        tf.setNanumSquareL(this, Rc_Date_Tv);

        Rc_Chapter_Tv = (TextView) findViewById(R.id.report_chapter_tv);
        tf.setNanumSquareL(this, Rc_Chapter_Tv);

        Rc_Score_Tv = (TextView) findViewById(R.id.report_score_tv);
        tf.setNanumSquareL(this, Rc_Score_Tv);

        Rc_Required_Tv = (TextView) findViewById(R.id.report_required_tv);
        tf.setNanumSquareL(this, Rc_Required_Tv);


        Rc_Next_Bt = (Button)findViewById(R.id.report_next_bt);
//        Rc_TestEnd_Iv = (ImageView) findViewById(R.id.report_testend_iv);
    }

    private void initEventListener() {
        Help_Bt.setOnClickListener(this);
        if("PT".equals(Type)) {
            DashBoard_Bt.setOnClickListener(this);
            History_Bt.setOnClickListener(this);
        }
        Rc_Next_Bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.report_dashboard_bt :
                /**MemberInfo 수정 **/
                Bundle dashboard_m = new Bundle();
                dashboard_m.putParcelable("MemberInfo",Mi);
                intent = new Intent(this, DashBoardMainAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("MemberInfo", dashboard_m);
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
            case R.id.report_test_bt :
                Bundle reporttest_m = new Bundle();
                reporttest_m.putParcelable("MemberInfo",Mi);
                intent = new Intent(this, CrewEvaluationAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                intent.putExtra("MemberInfo", reporttest_m);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

                /*intent = new Intent(this, CrewEvaluationAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                intent.putExtra("MemberInfo", Mi);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);*/
                break;
            case R.id.report_history_bt :
                Bundle report_history_m = new Bundle();
                report_history_m.putParcelable("MemberInfo",Mi);
                intent = new Intent(this, HistoryMainAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("MemberInfo", report_history_m);
                intent.putExtra("Type", "G");
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);

                /*intent = new Intent(this, HistoryMainAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("MemberInfo", Mi);
                intent.putExtra("Type", "G");
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);*/
                break;
            case R.id.report_help_bt :
                intent = new Intent(ReportCardAct.this, GuideAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);//  go Intent
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_in);
                break;
            case R.id.report_next_bt :
                switch (Integer.valueOf(v.getTag().toString())){
                    case R.drawable.report_card_back_bt :
                        Bundle report_card_back_m = new Bundle();
                        report_card_back_m.putParcelable("MemberInfo",Mi);
                        intent = new Intent(this, CrewEvaluationAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                        intent.putExtra("MemberInfo", report_card_back_m);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);//  go Intent
                        finish();
                        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);

                        /*intent = new Intent(this, CrewEvaluationAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                        intent.putExtra("MemberInfo", Mi);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);//  go Intent
                        finish();
                        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);*/
                        break;
                    case R.mipmap.report_card_next :
//                        Toast.makeText(this, "start", Toast.LENGTH_SHORT).show();
//                        Rc_TestEnd_Iv.setVisibility(View.VISIBLE);
//                        DashBoard_Bt.setOnClickListener(this);
//                        Test_Bt.setOnClickListener(this);
//                        History_Bt.setOnClickListener(this);
                        Bundle report_card_next_m = new Bundle();
                        report_card_next_m.putParcelable("MemberInfo",Mi);
                        intent = new Intent(this, TestEndAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.putExtra("Type", "T");
                        intent.putExtra("MemberInfo", report_card_next_m);
                        startActivity(intent);//  go Intent
                        finish();
                        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                        /*intent = new Intent(this, TestEndAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.putExtra("Type", "T");
                        intent.putExtra("MemberInfo", Mi);
                        startActivity(intent);//  go Intent
                        finish();
                        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);*/
                        break;
                }

                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
         if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
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
