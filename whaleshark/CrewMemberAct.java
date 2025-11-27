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
import android.widget.Toast;

import com.togetherseatech.whaleshark.Db.Member.MemberDao;
import com.togetherseatech.whaleshark.Db.Member.MemberInfo;
import com.togetherseatech.whaleshark.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bluepsh on 2017-10-19.
 */

public class CrewMemberAct extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private Intent intent;
    private Activity Act;
    private Button Crew_Member_Help_Bt, Crew_Member_Back_Bt;
    private ImageView Crew_Member_Language;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    // 액티비티에 보여지는 리스트뷰
    private ListView Crew_Member_ListView;
    // 리스트뷰에 사용할 어댑터
    private listView_CrewMember_Adapter mAdapter;
    // 어댑터 및 리스트뷰에 사용할 데이터 리스트
    private List<MemberInfo> MbList;
    private int LOGIN_CARRIER;
    private String LOGIN_LANGUAGE, TYPE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crewmember_act);
        Act = this;
        pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
        LOGIN_CARRIER = pref.getInt("LOGIN_CARRIER", 0);
        LOGIN_LANGUAGE = pref.getString("LOGIN_LANGUAGE", "");
        TYPE = getIntent().getStringExtra("Type");
        Log.e("test","TYPE : " + TYPE);

        initSetting();
        initEventListener();

        MbList = new ArrayList<MemberInfo>();

        MemberDao Mdao = new MemberDao(this);
        Log.e("test","Mdao 데이터 :" + Mdao.getMembers(Vars.DEL_N +  LOGIN_CARRIER + Vars.SIGNOFF_N));
        MbList = Mdao.getMembers(Vars.DEL_N +  LOGIN_CARRIER + Vars.SIGNOFF_N);


        // 커스텀 어댑터를 생성한다.
        mAdapter = new listView_CrewMember_Adapter(this, R.layout.crewmember_row, MbList);

        // 리스트뷰에 어댑터를 세팅한다.
        Crew_Member_ListView.setAdapter(mAdapter);

    }

    private void initSetting() {
        Crew_Member_Back_Bt  = (Button)findViewById(R.id.crew_member_back_bt);
        Crew_Member_Language = (ImageView)findViewById(R.id.crew_member_language_iv);
        Crew_Member_Help_Bt = (Button)findViewById(R.id.crew_member_help_bt);
        Crew_Member_ListView = (ListView)findViewById(R.id.crew_member_lv);

        if("KR".equals(LOGIN_LANGUAGE))
            Crew_Member_Language.setImageResource(R.mipmap.language_kr);
        else if("ENG".equals(LOGIN_LANGUAGE))
            Crew_Member_Language.setImageResource(R.mipmap.language_eng);
    }

	private void initEventListener() {
        Crew_Member_Back_Bt.setOnClickListener(this);
        Crew_Member_Help_Bt.setOnClickListener(this);
        Crew_Member_ListView.setOnItemClickListener(this);
	}

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MemberInfo mi = MbList.get(position);

        /*if("Training".equals(TYPE))
            intent = new Intent(this, PersonalTrainingAct.class);
        else if("Test".equals(TYPE))
            intent = new Intent(this, PersonalTestAct.class);
        else {
            intent = new Intent(this, TrainingHistoryAct.class);
            intent.putExtra("Type", TYPE);
        }*/
        /** Bundle로 받는 부분 classException 에러 수정후 코드 2021.09.16**/
        Bundle m = new Bundle();
        m.putParcelable("MemberInfo",mi);
        intent = new Intent(this, DashBoardMainAct.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("MemberInfo",m);
        startActivity(intent);//  go Intent
        finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);

        /** Bundle로 받는 부분 classException 에러 수정전 코드 2021.09.16**/
        /*intent = new Intent(this, DashBoardMainAct.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("MemberInfo",mi);
        startActivity(intent);//  go Intent
        finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);*/
        /** Bundle로 받는 부분 classException 에러 수정전 코드 2021.09.16**/

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.crew_member_back_bt:
//                intent = new Intent(CrewMemberAct.this, DashBoardMainAct.class);
                intent = new Intent(CrewMemberAct.this, LogInAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;
            case R.id.crew_member_help_bt:
                intent = new Intent(CrewMemberAct.this, GuideAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);//  go Intent
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_in);
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Log.e("LogInAct", "onResume");
         if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
            new CommonUtil().setHideNavigationBar(getWindow().getDecorView());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                break;
        }
        return true;
    }
}
