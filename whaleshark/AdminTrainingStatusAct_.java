package com.togetherseatech.whaleshark;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.togetherseatech.whaleshark.Db.History.TrainingDao;
import com.togetherseatech.whaleshark.Db.History.TrainingInfo;
import com.togetherseatech.whaleshark.Db.Member.MemberInfo;
import com.togetherseatech.whaleshark.util.SelectItems;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bluepsh on 2017-10-19.
 */

public class AdminTrainingStatusAct_ extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private Intent intent;
    private String LOGIN_MODE;
    private Button TS_LogOut_Bt, TS_ShipStaffs_Bt, TS_Training_Bt, TS_Update_Bt, TS_Upgrade_Bt, TS_Help_Bt, Ts_Dashboard_Bt;
    private ImageView TS_Language_Iv;
    private TextView TS_Name_Tv;
    // 어댑터 및 리스트뷰에 사용할 데이터 리스트
    public static ArrayList<TrainingInfo> TS_GroupList;
    private ArrayList<TrainingInfo> TS_ChildList;
    private ArrayList<ArrayList<TrainingInfo>> TS_ChildList_Left;
    private ArrayList<ArrayList<String>> TS_ChildList_Right;
    private ArrayList<TrainingInfo> TS_ChildListContent_Left;
    private ArrayList<String> TS_ChildListContent_Right;
    // 액티비티에 보여지는 리스트뷰
    private ExpandableListView TS_ExpandableListView;
    // 리스트뷰에 사용할 어댑터
    private BaseExpandableAdapter TS_BaseExpandableAdapter;

    private String LOGIN_LANGUAGE, VSL;
    private int LOGIN_CARRIER;
    private SharedPreferences pref;
    private TrainingDao Tdao;
    private SelectItems Si;
    public static Activity Act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.training_status_act_);
        Act = this;
        pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
        Si = new SelectItems();
        Tdao = new TrainingDao(this);
        LOGIN_LANGUAGE = pref.getString("LOGIN_LANGUAGE", "");
        LOGIN_CARRIER = pref.getInt("LOGIN_CARRIER", 0);
        VSL = pref.getString("VSL", "");
        initSetting();
        initEventListener();
        TS_GroupList = new ArrayList<TrainingInfo>();
        TS_ChildList = new ArrayList<TrainingInfo>();
        TS_ChildList_Left = new ArrayList<ArrayList<TrainingInfo>>();
        TS_ChildList_Right = new ArrayList<ArrayList<String>>();

        TS_GroupList = Tdao.getStatusDatas(Si.getYear(), Si.getQuarter(), LOGIN_CARRIER);

        for(int j=0; j < TS_GroupList.size(); j++) {
            TS_ChildListContent_Left = new ArrayList<TrainingInfo>();
            TS_ChildListContent_Right = new ArrayList<String>();
            TS_ChildList = Tdao.getHistorys(TS_GroupList.get(j).getIdx(), Si.getYear(), Si.getQuarter());
//            Log.e("TrainingStatusAct","TS_ChildList : "+ TS_ChildList.size());
            for (int k = 0; k < TS_ChildList.size(); k++) {
//                Log.e("TrainingStatusAct","TS_ChildList Chapter : "+ Si.getChapter(TS_ChildList.get(k).getTraining_course(), LOGIN_LANGUAGE));
//                Log.e("TrainingStatusAct","TS_ChildList getDate : "+ TS_ChildList.get(k).getDate());
                    if(TS_ChildList.get(k).getDate() != null) {
                        TS_ChildListContent_Left.add(TS_ChildList.get(k));
                    }else {
                        TS_ChildListContent_Right.add(Si.getChapter(TS_ChildList.get(k).getTraining_course(), LOGIN_LANGUAGE));
                    }
            }

            int count = Math.abs( TS_ChildListContent_Left.size() - TS_ChildListContent_Right.size());
//                Log.e("TrainingStatusAct","count : "+j+" = "+ count);
                for(int i=0; i < count; i++) {
                if(TS_ChildListContent_Left.size() > TS_ChildListContent_Right.size()) {
                    TS_ChildListContent_Right.add("");
                }else{
                    TS_ChildListContent_Left.add(new TrainingInfo(-1,""));
                }
            }

            TS_ChildList_Left.add(TS_ChildListContent_Left);
            TS_ChildList_Right.add(TS_ChildListContent_Right);
        }

        // 커스텀 어댑터를 생성한다.
        TS_BaseExpandableAdapter = new BaseExpandableAdapter(this, TS_GroupList, TS_ChildList_Left, TS_ChildList_Right);

        // 리스트뷰에 어댑터를 세팅한다.
        TS_ExpandableListView.setAdapter(TS_BaseExpandableAdapter);
    }

    private void initSetting() {
        TS_LogOut_Bt = (Button)findViewById(R.id.training_status_logout_bt);
        TS_ShipStaffs_Bt = (Button)findViewById(R.id.training_status_shipstaffs_bt);
        TS_Training_Bt = (Button)findViewById(R.id.training_status_training_bt);
        TS_Training_Bt.setBackgroundResource(R.mipmap.training_status_on);
        TS_Update_Bt = (Button)findViewById(R.id.training_status_update_bt);
        TS_Upgrade_Bt = (Button)findViewById(R.id.training_status_upgrade_bt);
        Ts_Dashboard_Bt = (Button)findViewById(R.id.training_status_dashboard_bt);
        TS_Name_Tv = (TextView) findViewById(R.id.training_status_name_tv);
        TS_Name_Tv.setText(VSL);
        TS_Language_Iv = (ImageView)findViewById(R.id.training_status_language_iv);

        if("KR".equals(LOGIN_LANGUAGE))
            TS_Language_Iv.setImageResource(R.mipmap.language_kr);
        else if("ENG".equals(LOGIN_LANGUAGE))
            TS_Language_Iv.setImageResource(R.mipmap.language_eng);
        TS_Help_Bt = (Button)findViewById(R.id.training_status_help_bt);

        TS_ExpandableListView = (ExpandableListView)findViewById(R.id.training_status_exlv);
    }

	private void initEventListener() {
        TS_LogOut_Bt.setOnClickListener(this);
        TS_ShipStaffs_Bt.setOnClickListener(this);
        TS_Training_Bt.setOnClickListener(this);
        TS_Update_Bt.setOnClickListener(this);
        TS_Upgrade_Bt.setOnClickListener(this);
        Ts_Dashboard_Bt.setOnClickListener(this);
        TS_Help_Bt.setOnClickListener(this);
        TS_ExpandableListView.setOnGroupClickListener(setOnGroupClickEvent);
	}

    private ExpandableListView.OnGroupClickListener setOnGroupClickEvent = new ExpandableListView.OnGroupClickListener() {
        int lastClickedPosition = 0;

        @Override
        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
            // 선택 한 groupPosition의 펼침/닫힘 상태 체크
            Boolean isExpand = (!TS_ExpandableListView.isGroupExpanded(groupPosition));

            // 이 전에 열려있던 group 닫기
            TS_ExpandableListView.collapseGroup(lastClickedPosition);

            if(isExpand){
                TS_ExpandableListView.expandGroup(groupPosition);
            }
            lastClickedPosition = groupPosition;
            return true;
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(this, "onItemClick", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.training_status_logout_bt:
                new TwoButtonPopUpDialog(this,"LOGOUT").show();
                break;
            case R.id.training_status_shipstaffs_bt:
                intent = new Intent(AdminTrainingStatusAct_.this, AdminAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;
            case R.id.training_status_update_bt:
                intent = new Intent(AdminTrainingStatusAct_.this, AdminUpdateAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                break;
            case R.id.training_status_upgrade_bt:
                intent = new Intent(AdminTrainingStatusAct_.this, AdminUpgradeAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                break;
            case R.id.training_status_help_bt:
                intent = new Intent(AdminTrainingStatusAct_.this, GuideAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);//  go Intent
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_in);
                break;
            case R.id.training_status_dashboard_bt:
                new TwoButtonPopUpDialog(this, this,"GODB").show();
                break;

        }

    }

    public static void goHistory(String position){
        int Position = Integer.valueOf(position);

        MemberInfo Mi = new MemberInfo();
        Mi.setIdx(TS_GroupList.get(Position).getIdx());
        Mi.setFirstName(TS_GroupList.get(Position).getFirstName());
        Mi.setSurName(TS_GroupList.get(Position).getSurName());
        /**수정추가**/
        Bundle m = new Bundle();
        m.putParcelable("MemberInfo",Mi);
        Intent intent = new Intent(Act, HistoryMainAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("MemberInfo", m);
        intent.putExtra("TrainingInfo", TS_GroupList.get(Position));
        intent.putExtra("Type", "A");
        Act.startActivity(intent);//  go Intent
        Act.finish();
        Act.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
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
        Log.e("TrainingStatusAct", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("TrainingStatusAct", "onResume");
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
