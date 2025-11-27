package com.togetherseatech.whaleshark;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;

import com.togetherseatech.whaleshark.Db.History.TrainingDao;
import com.togetherseatech.whaleshark.Db.History.TrainingInfo;
import com.togetherseatech.whaleshark.Db.Training.TrainingsDao;
import com.togetherseatech.whaleshark.Db.Training.TrainingsInfo;
import com.togetherseatech.whaleshark.Db.Member.MemberDao;
import com.togetherseatech.whaleshark.Db.Member.MemberInfo;
import com.togetherseatech.whaleshark.util.CommonUtil;
import com.togetherseatech.whaleshark.util.SelectItems;
import com.togetherseatech.whaleshark.util.TextFontUtil;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * Created by bluepsh on 2017-10-19.
 */

public class AdminTrainingStatusAct extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private Intent intent;
    private String LOGIN_MODE;
    int gP = 0, rP = 0;
    private Button TS_LogOut_Bt, TS_ShipStaffs_Bt, TS_Matrix_Bt, TS_Training_Bt, TS_Update_Bt, TS_Upgrade_Bt, TS_Help_Bt, Ts_Dashboard_Bt;
    private Button TS_P_Bt, TS_G_Bt, TS_NR_Bt, TS_Pop_X_Bt;
    private ImageView TS_Language_Iv, TS_Pop_Ring_Iv;
    private TextView TS_Name_Tv, TS_Avg1_Tv, TS_Avg2_Tv, TS_Avg3_Tv, TS_Crew_Name_Tv, TS_Pop_Crew_Name_Tv, TS_Pop_Name_Tv, TS_Pop_Subject_Tv;
    private TextView TS_Score_Tv, TS_Date_Tv, TS_Score1_Tv, TS_Date1_Tv, TS_Score2_Tv, TS_Date2_Tv, TS_Score3_Tv, TS_Date3_Tv;
    private RelativeLayout TS_Pop_Rl;
    private LinearLayout TS_Pop_Ll;
    // 액티비티에 보여지는 리스트뷰
    private ListView TS_ListView;
    // 리스트뷰에 사용할 어댑터
    private listView_Status_Adapter mAdapter;
    // 어댑터 및 리스트뷰에 사용할 데이터 리스트
    private List<MemberInfo> MbList;
    private MemberInfo Mi;
    private ArrayList<String> TS_Pdata;
    private ArrayList<ArrayList<String>> TS_GTdata;
    private ArrayList<TrainingInfo> TS_SubjectPList;
    private ArrayList<TrainingsInfo> TS_SubjectGTList;
    private ArrayList<SelectItems> TS_RankInfo;

    private String LOGIN_LANGUAGE, VSL, rank;
    private int LOGIN_CARRIER;
    private SharedPreferences pref;
    private TrainingDao Tdao;
    private TrainingsDao Tsdao;
    private SelectItems Si;
    private TextFontUtil Tf;
    public static Activity Act;
    private PieChartView pieChartView1, pieChartView2, pieChartView3, pieChartView;
    private PieChartData pieChartData1, pieChartData2, pieChartData3, pieChartData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.training_status_act);

        Act = this;
        pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
        Si = new SelectItems();
        Tf = new TextFontUtil();
        Tdao = new TrainingDao(this);
        Tsdao = new TrainingsDao(this);
        LOGIN_LANGUAGE = pref.getString("LOGIN_LANGUAGE", "");
        LOGIN_CARRIER = pref.getInt("LOGIN_CARRIER", 0);
        VSL = pref.getString("VSL", "");
        initSetting();
        initEventListener();
//        Tdao.getStatusTest(Si.getYear(), Si.getQuarter(), LOGIN_CARRIER);

        String Prate = Tdao.getStatusPRate(Si.getYear(), Si.getQuarter(), LOGIN_CARRIER);
        ArrayList<ArrayList<String>> GTrate = Tsdao.getStatusGTRate(LOGIN_CARRIER);
        /**같은 DB테이블 사용해서 구분하기위해 만든 구분자.*/
        if("G".equals(GTrate.get(0).get(0))) {
            gP = 0;
            rP = 1;
        } else {
            gP = 1;
            rP = 0;
        }

        TS_Avg1_Tv.setText(Prate+"%");
        TS_Avg2_Tv.setText(GTrate.get(gP).get(1)+"%");
        TS_Avg3_Tv.setText(GTrate.get(rP).get(1)+"%");

        MbList = new ArrayList<MemberInfo>();

        MemberDao Mdao = new MemberDao(this);
        MbList = Mdao.getMembers(Vars.DEL_N +  LOGIN_CARRIER + Vars.SIGNOFF_N);


        // 커스텀 어댑터를 생성한다.
        mAdapter = new listView_Status_Adapter(this, R.layout.training_status_row, MbList);

        // 리스트뷰에 어댑터를 세팅한다.
        TS_ListView.setAdapter(mAdapter);

        List pieData = new ArrayList<>();
//        pieData1.add(new SliceValue(60, Color.parseColor("#00a99d"))/*.setLabel("Q1: $10")*/);
        pieData.add(new SliceValue(100, Color.GRAY)/*.setLabel("Q2: $4")*/);

        List pieData1 = new ArrayList<>();
//        pieData1.add(new SliceValue(60, Color.parseColor("#00a99d"))/*.setLabel("Q1: $10")*/);
        pieData1.add(new SliceValue(100, Color.GRAY)/*.setLabel("Q2: $4")*/);

        List pieData2 = new ArrayList<>();
//        pieData2.add(new SliceValue(60, Color.parseColor("#6577cc"))/*.setLabel("Q1: $10")*/);
        pieData2.add(new SliceValue(100, Color.GRAY)/*.setLabel("Q2: $4")*/);

        List pieData3 = new ArrayList<>();
//        pieData3.add(new SliceValue(60, Color.parseColor("#f79388"))/*.setLabel("Q1: $10")*/);
        pieData3.add(new SliceValue(100, Color.GRAY)/*.setLabel("Q2: $4")*/);

        pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(false).setValueLabelTextSize(14);
        pieChartData.setHasCenterCircle(true).setCenterText1("0.0%").setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#00a99d"));

        pieChartData1 = new PieChartData(pieData1);
        pieChartData1.setHasLabels(false).setValueLabelTextSize(14);
        pieChartData1.setHasCenterCircle(true).setCenterText1("0.0%").setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#00a99d"));

        pieChartView1.setPieChartData(pieChartData1);

        pieChartData2 = new PieChartData(pieData2);
        pieChartData2.setHasLabels(false).setValueLabelTextSize(14);
        pieChartData2.setHasCenterCircle(true).setCenterText1("0.0%").setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#6577cc"));
        pieChartView2.setPieChartData(pieChartData2);

        pieChartData3 = new PieChartData(pieData3);
        pieChartData3.setHasLabels(false).setValueLabelTextSize(14);
        pieChartData3.setHasCenterCircle(true).setCenterText1("0.0%").setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#f79388"));
        pieChartView3.setPieChartData(pieChartData3);
    }

    private void initSetting() {
        TS_LogOut_Bt = (Button)findViewById(R.id.training_status_logout_bt);
        TS_ShipStaffs_Bt = (Button)findViewById(R.id.training_status_shipstaffs_bt);
        TS_Matrix_Bt = (Button)findViewById(R.id.training_status_matrix_bt);
        TS_Training_Bt = (Button)findViewById(R.id.training_status_training_bt);
        TS_Training_Bt.setBackgroundResource(R.mipmap.training_status_on);
        TS_Update_Bt = (Button)findViewById(R.id.training_status_update_bt);
        TS_Upgrade_Bt = (Button)findViewById(R.id.training_status_upgrade_bt);
        Ts_Dashboard_Bt = (Button)findViewById(R.id.training_status_dashboard_bt);
        TS_Name_Tv = (TextView)findViewById(R.id.training_status_name_tv);
        TS_Name_Tv.setText(VSL);
        TS_Language_Iv = (ImageView)findViewById(R.id.training_status_language_iv);

        if("KR".equals(LOGIN_LANGUAGE))
            TS_Language_Iv.setImageResource(R.mipmap.language_kr);
        else if("ENG".equals(LOGIN_LANGUAGE))
            TS_Language_Iv.setImageResource(R.mipmap.language_eng);
        TS_Help_Bt = (Button)findViewById(R.id.training_status_help_bt);

        TS_Avg1_Tv = (TextView)findViewById(R.id.training_status_avg1_tv);
        TS_Avg2_Tv = (TextView)findViewById(R.id.training_status_avg2_tv);
        TS_Avg3_Tv = (TextView)findViewById(R.id.training_status_avg3_tv);
        Tf.setNanumSquareL(this, TS_Avg1_Tv);
        Tf.setNanumSquareL(this, TS_Avg2_Tv);
        Tf.setNanumSquareL(this, TS_Avg3_Tv);

        TS_ListView = (ListView)findViewById(R.id.training_status_lv);

        TS_Crew_Name_Tv = (TextView)findViewById(R.id.training_status_sel_crew_name_tv);
        Tf.setNanumSquareL(this, TS_Crew_Name_Tv);

        TS_Score1_Tv  = (TextView)findViewById(R.id.training_status_score1_tv);
        TS_Date1_Tv  = (TextView)findViewById(R.id.training_status_lastdate1_tv);
        Tf.setNanumSquareRoundR(this, TS_Score1_Tv);
        Tf.setNanumSquareRoundR(this, TS_Date1_Tv);
        TS_Score2_Tv  = (TextView)findViewById(R.id.training_status_score2_tv);
        TS_Date2_Tv  = (TextView)findViewById(R.id.training_status_lastdate2_tv);
        Tf.setNanumSquareRoundR(this, TS_Score2_Tv);
        Tf.setNanumSquareRoundR(this, TS_Date2_Tv);
        TS_Score3_Tv  = (TextView)findViewById(R.id.training_status_score3_tv);
        TS_Date3_Tv  = (TextView)findViewById(R.id.training_status_lastdate3_tv);
        Tf.setNanumSquareRoundR(this, TS_Score3_Tv);
        Tf.setNanumSquareRoundR(this, TS_Date3_Tv);

        pieChartView1 = (PieChartView)findViewById(R.id.training_status_chart1);
        pieChartView2 = (PieChartView)findViewById(R.id.training_status_chart2);
        pieChartView3 = (PieChartView)findViewById(R.id.training_status_chart3);

        TS_P_Bt = (Button)findViewById(R.id.training_status_p_bt);
        TS_G_Bt = (Button)findViewById(R.id.training_status_g_bt);
        TS_NR_Bt = (Button)findViewById(R.id.training_status_nr_bt);

        TS_Pop_Rl = (RelativeLayout) findViewById(R.id.training_status_pop_rl);
        TS_Pop_Ll = (LinearLayout) findViewById(R.id.training_status_pop_subject_ll);

        TS_Pop_X_Bt = (Button)findViewById(R.id.training_status_pop_x_bt);

        TS_Pop_Crew_Name_Tv = (TextView)findViewById(R.id.training_status_pop_crew_name_tv);
        Tf.setNanumSquareL(this, TS_Pop_Crew_Name_Tv);

        TS_Pop_Ring_Iv = (ImageView)findViewById(R.id.training_status_pop_ring_iv);
        pieChartView = (PieChartView)findViewById(R.id.training_status_pop_chart);
        TS_Score_Tv = (TextView)findViewById(R.id.training_status_pop_score_tv);
        TS_Date_Tv = (TextView)findViewById(R.id.training_status_pop_lastdate_tv);
        Tf.setNanumSquareRoundR(this, TS_Score_Tv);
        Tf.setNanumSquareRoundR(this, TS_Date_Tv);
        TS_Pop_Name_Tv = (TextView)findViewById(R.id.training_status_pop_name_tv);
        Tf.setNanumGothicBold(this, TS_Pop_Name_Tv);
//        TS_Pop_Subject_Tv = (TextView)findViewById(R.id.training_status_pop_subject_tv);
//        Tf.setNanumSquareRoundL(this, TS_Pop_Subject_Tv);

    }

	private void initEventListener() {
        TS_LogOut_Bt.setOnClickListener(this);
        TS_ShipStaffs_Bt.setOnClickListener(this);
        TS_Matrix_Bt.setOnClickListener(this);
        TS_Training_Bt.setOnClickListener(this);
        TS_Update_Bt.setOnClickListener(this);
        TS_Upgrade_Bt.setOnClickListener(this);
        Ts_Dashboard_Bt.setOnClickListener(this);
        TS_Help_Bt.setOnClickListener(this);
        TS_ListView.setOnItemClickListener(this);
        TS_P_Bt.setOnClickListener(this);
        TS_G_Bt.setOnClickListener(this);
        TS_NR_Bt.setOnClickListener(this);
        TS_Pop_X_Bt.setOnClickListener(this);
	}
    
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Log.e(page, "size : " + MbList.size() + "/" + position);
        Mi = (MemberInfo)parent.getItemAtPosition(position);
        MbList.get(position).setChecked(true);
        for(int i=0; i < MbList.size(); i++){
            if(i != position) {
                MbList.get(i).setChecked(false);
            }
        }
        mAdapter.notifyDataSetChanged();
        Setting(Mi);
    }

    private void Setting(MemberInfo Mi){
        
        TS_Pop_Rl.setVisibility(View.INVISIBLE);
        TS_P_Bt.setVisibility(View.VISIBLE);
        TS_G_Bt.setVisibility(View.VISIBLE);
        TS_NR_Bt.setVisibility(View.VISIBLE);

        rank = Si.getGTRankDept(Mi.getRank());
        List<Integer> GTCount = Tsdao.getMatrixCount(rank);
//        Log.e("AdminTrainingStatusAct","Setting : " + Mi.getIdx());
        TS_Pdata = Tdao.getStatusPData(Si.getYear(), Si.getQuarter(), LOGIN_CARRIER, Mi.getIdx());
//        TS_GTdata = Tsdao.getStatusGTData(Si.getGeneralTrainingSize()-1, Si.getNewRegulationRaw().size(), Mi.getIdx(), LOGIN_CARRIER);
        TS_GTdata = Tsdao.getStatusGTData(GTCount.get(0), GTCount.get(1), Mi.getIdx(), LOGIN_CARRIER);
        Float chartRate1 = Float.valueOf(TS_Pdata.get(0));
        Float chartRate2 = (float)100 - chartRate1;
        List pieData = new ArrayList<>();
        pieData.add(new SliceValue(chartRate1, Color.parseColor("#00a99d")));
        pieData.add(new SliceValue(chartRate2, Color.GRAY));
        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasCenterCircle(true).setCenterText1(chartRate1+"%").setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#00a99d"));
        pieChartView1.setPieChartData(pieChartData);
        pieChartView1.startDataAnimation();
        TS_Score1_Tv.setText(TS_Pdata.get(1));
        TS_Date1_Tv.setText(TS_Pdata.get(2));

        if("G".equals(TS_GTdata.get(0).get(0))) {
            gP = 0;
            rP = 1;
        } else {
            gP = 1;
            rP = 0;
        }

        chartRate1 = Float.valueOf(TS_GTdata.get(gP).get(1));
        chartRate2 = (float)100 - chartRate1;
        pieData = new ArrayList<>();
        pieData.add(new SliceValue(chartRate1, Color.parseColor("#6577cc")));
        pieData.add(new SliceValue(chartRate2, Color.GRAY));
        pieChartData = new PieChartData(pieData);
        pieChartData.setHasCenterCircle(true).setCenterText1(chartRate1+"%").setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#6577cc"));
        pieChartView2.setPieChartData(pieChartData);
        pieChartView2.startDataAnimation();
        TS_Score2_Tv.setText(TS_GTdata.get(gP).get(2));
        TS_Date2_Tv.setText(TS_GTdata.get(gP).get(3));

        chartRate1 = Float.valueOf(TS_GTdata.get(rP).get(1));
        chartRate2 = (float)100 - chartRate1;
        pieData = new ArrayList<>();
        pieData.add(new SliceValue(chartRate1, Color.parseColor("#f79388")));
        pieData.add(new SliceValue(chartRate2, Color.GRAY));
        pieChartData = new PieChartData(pieData);
        pieChartData.setHasCenterCircle(true).setCenterText1(chartRate1+"%").setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#f79388"));
        pieChartView3.setPieChartData(pieChartData);
        pieChartView3.startDataAnimation();
        TS_Score3_Tv.setText(TS_GTdata.get(rP).get(2));
        TS_Date3_Tv.setText(TS_GTdata.get(rP).get(3));

        TS_Crew_Name_Tv.setText(Mi.getSurName() + " " + Mi.getFirstName());
    }

    private void Setting2(String Type, MemberInfo Mi){
        if(Mi != null) {
            TS_Pop_Ll.removeAllViews();
            TS_Pop_Crew_Name_Tv.setText(Mi.getSurName() + " " + Mi.getFirstName());

            List pieData = new ArrayList<>();
            PieChartData pieChartData;
            Float chartRate1 = 0f;
            Float chartRate2 = 0f;
            String Subjec = "";
            TextView tv[] = null;

            if("P".equals(Type)) {
                chartRate1 = Float.valueOf(TS_Pdata.get(0));
                chartRate2 = (float)100 - chartRate1;
                pieData.add(new SliceValue(chartRate1, Color.parseColor("#00a99d")));
                pieData.add(new SliceValue(new SliceValue(chartRate2, Color.GRAY)));
                pieChartData = new PieChartData(pieData);
                pieChartData.setHasCenterCircle(true).setCenterText1(chartRate1+"%").setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#00a99d"));
                pieChartView.setPieChartData(pieChartData);
                pieChartView.startDataAnimation();
                TS_Score_Tv.setText(TS_Pdata.get(1));
                TS_Date_Tv.setText(TS_Pdata.get(2));

                TS_RankInfo = Si.getRankChapter(Mi.getRank(),LOGIN_CARRIER);
                TS_SubjectPList = Tdao.getHistorys(Mi.getIdx(), Si.getYear(), Si.getQuarter());

                for (int i = 0; i < TS_SubjectPList.size(); i++) {
                    for (int j = 0; j < TS_RankInfo.size(); j++) {
                        if (TS_SubjectPList.get(i).getTraining_course() == TS_RankInfo.get(j).getIdx())
                            if(TS_SubjectPList.get(i).getDate() != null)
                                TS_RankInfo.remove(j);
                    }
                }

                tv = new TextView[TS_RankInfo.size()];

                for (int i = 0; i < TS_RankInfo.size(); i++) {
                    /*Subjec += Si.getChapter(TS_SubjectPList.get(i).getTraining_course(),"ENG");
                    if(TS_SubjectPList.size()-1 != i)
                        Subjec += "\r\n";*/
                    tv[i] = new TextView(this);
                    tv[i].setTextColor(Color.parseColor("#af8db2"));
                    tv[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    tv[i].setTextSize(12f);
                    tv[i].setText(Si.getChapter(TS_RankInfo.get(i).getIdx(),"ENG"));

                    Tf.setNanumSquareRoundL(this, tv[i]);
                    if(TS_SubjectPList.size()-1 != i) {
                        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        llp.setMargins(0, 0, 0, 15); // llp.setMargins(left, top, right, bottom);
                        tv[i].setLayoutParams(llp);
                    }
                    TS_Pop_Ll.addView(tv[i]);
                }
//                TS_Pop_Subject_Tv.setText(Subjec);
//                TS_Pop_Subject_Tv.setLineSpacing(1.0f, 1.2f);
            } else {//if("G".equals(Type)) {
                int color = 0;
                if("G".equals(Type)) {
                    color = Color.parseColor("#6577cc");
                    TS_Score_Tv.setText(TS_GTdata.get(gP).get(2));
                    TS_Date_Tv.setText(TS_GTdata.get(gP).get(3));

                    chartRate1 = Float.valueOf(TS_GTdata.get(gP).get(1));
                    chartRate2 = (float)100 - chartRate1;
                    pieData.add(new SliceValue(chartRate1, color));

                }else if("R".equals(Type)) {
                    color = Color.parseColor("#f79388");
                    TS_Score_Tv.setText(TS_GTdata.get(rP).get(2));
                    TS_Date_Tv.setText(TS_GTdata.get(rP).get(3));

                    chartRate1 = Float.valueOf(TS_GTdata.get(rP).get(1));
                    chartRate2 = (float)100 - chartRate1;
                    pieData.add(new SliceValue(chartRate1, color));

                }

                pieData.add(new SliceValue(new SliceValue(chartRate2, Color.GRAY)));
                pieChartData = new PieChartData(pieData);
                pieChartData.setHasCenterCircle(true).setCenterText1(chartRate1+"%").setCenterText1FontSize(20).setCenterText1Color(color);
                pieChartView.setPieChartData(pieChartData);
                pieChartView.startDataAnimation();
//                getMatrixs
                List<TrainingsInfo> count = Tsdao.getMatrixs(Type, rank);
//                List<String> count = Si.getGTRankDept(Type, Mi.getRank());
//                Log.e("AdminTrainingStatusAct","count : "+count.size());
                List<TrainingsInfo> subject =  Si.getGTsubject(count, Type);
                TS_SubjectGTList = Tsdao.getStatusHistory(subject, Type, LOGIN_CARRIER, Mi.getIdx());
//                Log.e("AdminTrainingStatusAct","TS_SubjectGTList : " + TS_SubjectGTList.size());
                tv = new TextView[TS_SubjectGTList.size()];
                for (int i = 0; i < TS_SubjectGTList.size(); i++) {
                    if (TS_SubjectGTList.get(i).getDate() == null) {
                        /*Subjec += TS_SubjectGTList.get(i).getTitle();
                        if(TS_SubjectGTList.size()-1 != i)
                            Subjec += "\r\n";*/
                        tv[i] = new TextView(this);
                        tv[i].setTextColor(Color.parseColor("#af8db2"));
                        tv[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        tv[i].setTextSize(12f);
                        tv[i].setText(TS_SubjectGTList.get(i).getTitle());
                        Tf.setNanumSquareRoundL(this, tv[i]);
                        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        llp.setMargins(0, 0, 0, 15); // llp.setMargins(left, top, right, bottom);
                        tv[i].setLayoutParams(llp);

                        TS_Pop_Ll.addView(tv[i]);
                    }
                }
//                TS_Pop_Subject_Tv.setText(Subjec);
//                TS_Pop_Subject_Tv.setLineSpacing(1.0f, 1.2f);
            } /*else if("R".equals(Type)) {
        }*/
            TS_P_Bt.setVisibility(View.INVISIBLE);
            TS_G_Bt.setVisibility(View.INVISIBLE);
            TS_NR_Bt.setVisibility(View.INVISIBLE);
            TS_Pop_Rl.setVisibility(View.VISIBLE);
        } else {
            Si.getToast(this,"NO DATA.", Toast.LENGTH_SHORT);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.training_status_logout_bt:
                new CommonUtil().setHideDialog(new TwoButtonPopUpDialog(this,"LOGOUT"));
                break;
            case R.id.training_status_shipstaffs_bt:
                intent = new Intent(AdminTrainingStatusAct.this, AdminAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;
            case R.id.training_status_matrix_bt:
                intent = new Intent(AdminTrainingStatusAct.this, AdminMatrixAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;
            case R.id.training_status_update_bt:
                intent = new Intent(AdminTrainingStatusAct.this, AdminUpdateAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                break;
            case R.id.training_status_upgrade_bt:
                intent = new Intent(AdminTrainingStatusAct.this, AdminUpgradeAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                break;
            case R.id.training_status_help_bt:
                intent = new Intent(AdminTrainingStatusAct.this, GuideAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);//  go Intent
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_in);
                break;
            case R.id.training_status_dashboard_bt:
                new CommonUtil().setHideDialog(new TwoButtonPopUpDialog(this, this,"GODB"));
                break;
            case  R.id.training_status_p_bt:
                TS_Pop_Ring_Iv.setBackground(getDrawable(R.mipmap.training_status_ring_bg1));
                TS_Pop_Name_Tv.setText("Crew Evaluation");
                Setting2("P", Mi);
                break;
            case  R.id.training_status_g_bt:
                TS_Pop_Ring_Iv.setBackground(getDrawable(R.mipmap.training_status_ring_bg2));
                TS_Pop_Name_Tv.setText("General");
                Setting2("G", Mi);
                break;
            case  R.id.training_status_nr_bt:
                TS_Pop_Ring_Iv.setBackground(getDrawable(R.mipmap.training_status_ring_bg3));
                TS_Pop_Name_Tv.setText("New Regulation");
                Setting2("R", Mi);
                break;
            case  R.id.training_status_pop_x_bt:
                TS_Pop_Rl.setVisibility(View.INVISIBLE);
                TS_P_Bt.setVisibility(View.VISIBLE);
                TS_G_Bt.setVisibility(View.VISIBLE);
                TS_NR_Bt.setVisibility(View.VISIBLE);
                break;
        }

    }

    /*public void goHistory(String position){
        int Position = Integer.valueOf(position);

        MemberInfo Mi = new MemberInfo();
        Mi.setIdx(TS_GroupList.get(Position).getIdx());
        Mi.setFirstName(TS_GroupList.get(Position).getFirstName());
        Mi.setSurName(TS_GroupList.get(Position).getSurName());

        Intent intent = new Intent(Act, HistoryMainAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("MemberInfo", Mi);
        intent.putExtra("TrainingInfo", TS_GroupList.get(Position));
        intent.putExtra("Type", "A");
        Act.startActivity(intent);//  go Intent
        Act.finish();
        Act.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
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

}
