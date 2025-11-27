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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.togetherseatech.whaleshark.Db.History.TrainingDao;
import com.togetherseatech.whaleshark.Db.History.TrainingInfo;
import com.togetherseatech.whaleshark.Db.Member.MemberInfo;
import com.togetherseatech.whaleshark.util.CommonUtil;
import com.togetherseatech.whaleshark.util.SelectItems;
import com.togetherseatech.whaleshark.util.TextFontUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by seonghak on 2017. 11. 3..
 */

public class CrewEvaluationAct extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener{

    private Button DashBoard_Bt, Test_Bt, History_Bt, Help_Bt, PT_Start_Bt;
    private ImageView PT_Language_Iv, PT_Square_Iv,PT_Graph_Iv ;
    private TextView PT_Name_Tv, PT_Title_Tv, PT_Score_Tv, PT_Status_Tv, PT_Contents_Tv;

    private Intent intent;
    private TrainingDao Tdao;
    private SharedPreferences pref;
    private TextFontUtil tf;
    private MemberInfo Mi;
    private SelectItems Si;
    private TrainingInfo Ti, TiTemp;
    private String LOGIN_LANGUAGE;
    // 액티비티에 보여지는 리스트뷰
    private ListView PT_Lv;
    // 리스트뷰에 사용할 어댑터
    private listView_Chapter_Adapter mAdapter;
    // 어댑터 및 리스트뷰에 사용할 데이터 리스트
    private List<SelectItems> MbList;
    private List<TrainingInfo> TiList;
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crew_evaluation_act);

        Log.e("test","crewevaluationAct onCreate");

        pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
        LOGIN_LANGUAGE = pref.getString("LOGIN_LANGUAGE", "");
        /*** Bundle로 받는 부분 classException 에러 수정후 코드 **/
        Bundle M = getIntent().getBundleExtra("MemberInfo");
        Mi = M.getParcelable("MemberInfo");
        /*** Bundle로 받는 부분 classException 에러 수정후 코드 **/
        /*** Bundle로 받는 부분 classException 에러 수정전 코드 **/
//        Mi = (MemberInfo)getIntent().getExtras().get("MemberInfo");
        /*** Bundle로 받는 부분 classException 에러 수정전 코드 **/
        Log.e("test","MemberInfor intent = " + Mi);

        Si = new SelectItems();
        tf = new TextFontUtil();
        Tdao = new TrainingDao(this);
        MbList = new ArrayList<SelectItems>();

        initSetting();
        initEventListener();
//        Tdao.test2(Mi.getIdx(),0,2020,1,"2020.01.28");
        MbList = Si.getRankChapter(Mi.getRank(),Mi.getVsl_type());
        TiList = Tdao.getHistorys(Mi.getIdx(), Si.getYear(), Si.getQuarter());

        Log.e("test","Mi.getIdx : " + Mi.getIdx());
        Log.e("test","Mi.getYear : " + Si.getYear());
        Log.e("test","Si.getqu : " + Si.getQuarter());
        Log.e("test","TList : " + TiList.size());
//        Log.e("PersonnelTrainingsAct","TiList.size() : " + " / " + TiList.size());
        for(int i = 0; i < TiList.size(); i++) {
//            Log.e("PersonnelTrainingsAct","TiList : " + " / " + TiList.get(i).getTraining_course());
            for(int j = 0; j < MbList.size(); j++) {
                Log.e("test","MbList === TiList :: " + MbList.get(j).getIdx() + " |+| " + TiList.get(i).getTraining_course());
                if(MbList.get(j).getIdx() == TiList.get(i).getTraining_course()) {
                    if(TiList.get(i).getDate() != null)
                        MbList.get(j).setChecked(true);
//                    Log.e("PersonnelTrainingsAct","setChecked : " + MbList.get(j).getIdx() + " / " + TiList.get(i).getTraining_course());
//                    Log.e("PersonnelTrainingsAct","Checked : " + MbList.get(j).getChecked());
                }
            }
        }
        Log.e("test","TList1111 : " + TiList.size());
        if(TiList.size() > 0 && TiList.get(0).getTraining_course() == 0) {
            TiTemp = TiList.get(0);
            PT_Square_Iv.setImageResource(R.mipmap.trainings_check_on);
            PT_Score_Tv.setText(TiList.get(0).getScore() == 0 ? "00" : TiList.get(0).getScore()+"");
            PT_Score_Tv.setTextColor(Color.parseColor("#0F9383"));
            PT_Graph_Iv.setImageResource(Si.getScoreGraph(TiList.get(0).getScore()));
            PT_Start_Bt.setTag(R.mipmap.improvements_required);
            PT_Start_Bt.setBackgroundResource(R.mipmap.improvements_required);
            PT_Status_Tv.setText("Complete!");
            PT_Status_Tv.setTextColor(Color.parseColor("#0F9383"));
            if("KR".equals(LOGIN_LANGUAGE))
                PT_Contents_Tv.setText(R.string.test_yes_kr);
            else
                PT_Contents_Tv.setText(R.string.test_yes_eng);
        }else{
            if("KR".equals(LOGIN_LANGUAGE))
                PT_Contents_Tv.setText(R.string.test_no_kr);
            else
                PT_Contents_Tv.setText(R.string.test_no_eng);
            PT_Square_Iv.setImageResource(R.mipmap.trainings_check_off);
            PT_Score_Tv.setText("00");
            PT_Graph_Iv.setImageResource(Si.getScoreGraph(0));
            PT_Start_Bt.setTag(R.mipmap.start_learning);
            PT_Start_Bt.setBackgroundResource(R.mipmap.start_learning);
        }
        // 커스텀 어댑터를 생성한다.
        mAdapter = new listView_Chapter_Adapter(this, R.layout.crew_evaluation_row, MbList, position);
        // 리스트뷰에 어댑터를 세팅한다.
        PT_Lv.setAdapter(mAdapter);

        if("KR".equals(LOGIN_LANGUAGE)) {
            PT_Language_Iv.setImageResource(R.mipmap.language_kr);
            PT_Title_Tv.setText(MbList.get(0).getKr());
        }else if("ENG".equals(LOGIN_LANGUAGE)) {
            PT_Language_Iv.setImageResource(R.mipmap.language_eng);
            PT_Title_Tv.setText(MbList.get(0).getEng());
        }
//        for(int i = 1; i <= 15; i++)
//            Log.e("PersonnelTrainingsAct","getDate : " + " / " + Math.round((float)i / (float)15 * 100));
    }
    @Override
    protected void onResume() {
        super.onResume();
        /*RelativeLayout rl = (RelativeLayout)PT_Lv.getChildAt(position).findViewById(R.id.trainings_row_rl);
        rl.setBackgroundResource(R.mipmap.personal_row_on);
        for(int i=0; i < PT_Lv.getChildCount(); i++){
            if(i != Integer.valueOf(rl.getTag().toString())) {
                RelativeLayout trl = (RelativeLayout)PT_Lv.getChildAt(i).findViewById(R.id.trainings_row_rl);
                trl.setBackgroundResource(R.mipmap.personal_row_off);
            }
        }*/
         if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
            new CommonUtil().setHideNavigationBar(getWindow().getDecorView());
        }
    }
    private void initSetting() {

        DashBoard_Bt = (Button)findViewById(R.id.test_dashboard_bt);

        Test_Bt = (Button)findViewById(R.id.test_bt);
        Test_Bt.setBackgroundResource(R.mipmap.evaluation_on);

        History_Bt = (Button)findViewById(R.id.test_history_bt);
        Help_Bt = (Button)findViewById(R.id.test_help_bt);
        PT_Language_Iv = (ImageView) findViewById(R.id.test_language_iv);
        PT_Title_Tv = (TextView) findViewById(R.id.test_title_tv);
        tf.setNanumSquareEB(this, PT_Title_Tv);
        PT_Name_Tv = (TextView)findViewById(R.id.test_name_tv);
        PT_Name_Tv.setText(Mi.getSurName() + " " + Mi.getFirstName());
        tf.setNanumSquareL(this, PT_Name_Tv);
        PT_Square_Iv = (ImageView) findViewById(R.id.test_square_iv);

        PT_Score_Tv = (TextView) findViewById(R.id.test_score_tv);
        tf.setNanumSquareEB(this, PT_Score_Tv);
        PT_Score_Tv.setTextColor(Color.parseColor("#592A82"));

        PT_Graph_Iv = (ImageView) findViewById(R.id.test_graph_iv);
        PT_Graph_Iv.setImageResource(R.mipmap.graph0);

        PT_Status_Tv = (TextView) findViewById(R.id.test_status_tv);
        tf.setNanumSquareL(this, PT_Status_Tv);
        PT_Status_Tv.setTextColor(Color.parseColor("#592A82"));

        PT_Contents_Tv = (TextView) findViewById(R.id.test_contents_tv);
        tf.setNanumSquareL(this, PT_Contents_Tv);
        PT_Lv = (ListView)findViewById(R.id.test_Lv);
        PT_Start_Bt = (Button)findViewById(R.id.test_start_tv);

    }

    private void initEventListener() {
        DashBoard_Bt.setOnClickListener(this);
//        Test_Bt.setOnClickListener(this);
        History_Bt.setOnClickListener(this);
        Help_Bt.setOnClickListener(this);
        PT_Lv.setOnItemClickListener(this);
        PT_Start_Bt.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.test_dashboard_bt :
                /**MemberInfo intent bundle로 수정**/
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
            case R.id.test_history_bt :
                /**MemberInfo intent bundle로 수정**/
                Bundle history_m = new Bundle();
                history_m.putParcelable("MemberInfo",Mi);
                Log.v("test","crew_history:" + Mi);
                intent = new Intent(this, HistoryMainAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("MemberInfo", history_m);
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
            case R.id.test_help_bt :
                intent = new Intent(CrewEvaluationAct.this, GuideAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);//  go Intent
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_in);
                break;
            case R.id.test_start_tv :
                if(PT_Start_Bt.getTag().equals(R.mipmap.start_learning)) {
                    Log.e("test","PT : " + PT_Start_Bt.getTag());
                    Ti = new TrainingInfo();
                    Ti.setMember_idx(Mi.getIdx());
                    Ti.setTraining_course(MbList.get(position).getIdx());
                    Ti.setDate("");
//                    Log.e("PersonnelTrainingsAct","TiTemp null: " + TiTemp == null ? "true" : "false");

                    if(TiTemp != null && TiTemp.getDate() == null) {

                        Ti.setIdx(TiList.get(position).getIdx());
                        Ti.setDate(null);
                    }
                    new CommonUtil().setHideDialog(new TwoButtonPopUpDialog(this, Mi, Ti, "START",this));
                } else if(PT_Start_Bt.getTag().equals(R.mipmap.improvements_required)) {
                    Bundle reportcard_m = new Bundle();
                    reportcard_m.putParcelable("MemberInfo",Mi);
                    intent = new Intent(this, ReportCardAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("MemberInfo", reportcard_m);
                    intent.putExtra("TrainingInfo", TiTemp);
                    intent.putExtra("Type", "PT");
                    startActivity(intent);//  go Intent
                    finish();
                    overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                    /*intent = new Intent(this, ReportCardAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("MemberInfo", Mi);
                    intent.putExtra("TrainingInfo", TiTemp);
                    intent.putExtra("Type", "PT");
                    startActivity(intent);//  go Intent
                    finish();
                    overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);*/
                }
                break;
        }
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.position = position;
        SelectItems si = (SelectItems)parent.getItemAtPosition(position);
        if("KR".equals(LOGIN_LANGUAGE)) {
            PT_Title_Tv.setText(si.getKr());
        }else if("ENG".equals(LOGIN_LANGUAGE)) {
            PT_Title_Tv.setText(si.getEng());
        }
        RelativeLayout rl = (RelativeLayout)parent.getChildAt(position).findViewById(R.id.trainings_row_rl);
        rl.setBackgroundResource(R.mipmap.personal_row_on);
        for(int i=0; i < parent.getChildCount(); i++){
            if(i != Integer.valueOf(rl.getTag().toString()))	{
                RelativeLayout trl = (RelativeLayout)parent.getChildAt(i).findViewById(R.id.trainings_row_rl);
                trl.setBackgroundResource(R.mipmap.personal_row_off);
            }
        }
        TiTemp = Tdao.getHistory(Mi.getIdx(), Si.getYear(), Si.getQuarter(), MbList.get(position).getIdx());
//        Log.e("PersonnelTrainingsAct","Ti : " + " / " + TiTemp.getDate());
        if(TiTemp != null && TiTemp.getDate() != null) {
            PT_Square_Iv.setImageResource(R.mipmap.trainings_check_on);
            PT_Score_Tv.setText(TiTemp.getScore() == 0 ? "00" : TiTemp.getScore()+"");
            PT_Score_Tv.setTextColor(Color.parseColor("#0F9383"));
            PT_Graph_Iv.setImageResource(Si.getScoreGraph(TiTemp.getScore()));
            PT_Start_Bt.setBackgroundResource(R.mipmap.improvements_required);
            PT_Start_Bt.setTag(R.mipmap.improvements_required);
            PT_Status_Tv.setText("Complete!");
            PT_Status_Tv.setTextColor(Color.parseColor("#0F9383"));
            if("KR".equals(LOGIN_LANGUAGE))
                PT_Contents_Tv.setText(R.string.test_yes_kr);
            else
                PT_Contents_Tv.setText(R.string.test_yes_eng);
        } else {
            PT_Square_Iv.setImageResource(R.mipmap.trainings_check_off);
            PT_Score_Tv.setText("00");
            PT_Score_Tv.setTextColor(Color.parseColor("#592A82"));
            PT_Graph_Iv.setImageResource(Si.getScoreGraph(0));
            PT_Start_Bt.setBackgroundResource(R.mipmap.start_learning);
            PT_Start_Bt.setTag(R.mipmap.start_learning);
            PT_Status_Tv.setText("Not Complete");
            PT_Status_Tv.setTextColor(Color.parseColor("#592A82"));
            if("KR".equals(LOGIN_LANGUAGE))
                PT_Contents_Tv.setText(R.string.test_no_kr);
            else
                PT_Contents_Tv.setText(R.string.test_no_eng);
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
