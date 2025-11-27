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
import android.widget.Toast;

import com.togetherseatech.whaleshark.Db.Member.MemberDao;
import com.togetherseatech.whaleshark.Db.Member.MemberInfo;
import com.togetherseatech.whaleshark.Db.Training.TrainingsDao;
import com.togetherseatech.whaleshark.Db.Training.TrainingsInfo;
import com.togetherseatech.whaleshark.util.CommonUtil;
import com.togetherseatech.whaleshark.util.SelectItems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by seonghak on 2018. 2. 21..
 */

public class AttendanceAct extends Activity implements View.OnClickListener {

    private Button Attendance_Help_Bt, Attendance_Back_Bt, Attendance_Test_Bt;
    private ImageView Attendance_Language;
    private SelectItems Si;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Intent intent;
    // 액티비티에 보여지는 리스트뷰
    private ListView AttendanceListView;
    // 리스트뷰에 사용할 어댑터
    public static listView_Attendance_Adapter mAdapter;
    // 어댑터 및 리스트뷰에 사용할 데이터 리스트
    private List<MemberInfo> MbList;
    private int LOGIN_CARRIER;
    private String LOGIN_LANGUAGE, VSL, Type;
    private TrainingsInfo Tsi;
    private MemberInfo Mi;
    public static ArrayList<String> list = new ArrayList<>();
    private Integer[] intArray = new Integer[]{0,1,2,3,4,5,6,7};
    private  int Rpos = 0;
    private List<TrainingsInfo> TisList;
    private Bundle m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_act);
        Log.e("test","intent get :" + Mi);
        pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
        LOGIN_CARRIER = pref.getInt("LOGIN_CARRIER", 0);
        LOGIN_LANGUAGE = pref.getString("LOGIN_LANGUAGE", "");
        VSL = pref.getString("VSL", "");
        Tsi = (TrainingsInfo)getIntent().getExtras().get("TrainingsInfo");
        Type = getIntent().getStringExtra("Type");
        Rpos = getIntent().getIntExtra("pos",Rpos);
        TisList = (List<TrainingsInfo>) getIntent().getSerializableExtra("TrainingsInfo");

        /*** Bundle로 받는 부분 classException 에러 수정후 코드 **/
        Bundle M = getIntent().getBundleExtra("MemberInfo");
        Mi = M.getParcelable("MemberInfo");
        /*** Bundle로 받는 부분 classException 에러 수정후 코드 **/
//        Mi = (MemberInfo)getIntent().getExtras().get("MemberInfo");
        Si = new SelectItems();

        initSetting();
        initEventListener();
        MbList = new ArrayList<MemberInfo>();

        MemberDao Mdao = new MemberDao(this);
        MbList = Mdao.getMembers(Vars.DEL_N +  LOGIN_CARRIER + Vars.SIGNOFF_N);

        // 커스텀 어댑터를 생성한다.
        mAdapter = new listView_Attendance_Adapter(this, R.layout.attendance_row, MbList);

        // 리스트뷰에 어댑터를 세팅한다.
        AttendanceListView.setAdapter(mAdapter);

    }

    private void initSetting() {
        Attendance_Language = (ImageView)findViewById(R.id.attendance_language_iv);
        Attendance_Help_Bt = (Button)findViewById(R.id.attendance_help_bt);
        AttendanceListView = (ListView)findViewById(R.id.attendance_lv);

        if("KR".equals(LOGIN_LANGUAGE))
            Attendance_Language.setImageResource(R.mipmap.language_kr);
        else if("ENG".equals(LOGIN_LANGUAGE))
            Attendance_Language.setImageResource(R.mipmap.language_eng);
        Attendance_Back_Bt = (Button)findViewById(R.id.attendance_back_bt);
        Attendance_Test_Bt = (Button)findViewById(R.id.attendance_test_bt);
    }

    private void initEventListener() {
        Attendance_Help_Bt.setOnClickListener(this);
        Attendance_Back_Bt.setOnClickListener(this);
        Attendance_Test_Bt.setOnClickListener(this);
//        MemberListView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.general_help_bt:
                intent = new Intent(AttendanceAct.this, GuideAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);//  go Intent
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_in);
                break;
            case R.id.attendance_back_bt:
                if("R".equals(Type)){
                    m = new Bundle();
                    m.putParcelable("MemberInfo",Mi);
                    intent = new Intent(this, NewRegulationAct.class);
                }else{
                    m = new Bundle();
                    m.putParcelable("MemberInfo",Mi);
                    intent = new Intent(this, GeneralTrainingAct.class);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("MemberInfo", m);
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;
            case R.id.attendance_test_bt:
                if(list.size() > 0){
                    List<Integer> intList = new ArrayList<>(Arrays.asList(intArray));
                    Log.e("test","getTraining_corse : " + Tsi.getTraining_course());
                    Log.e("test","Type : " + Type);
                    if(Type.equals("R")){

                        if(intList.contains(Rpos)){
                            Bundle m = new Bundle();
                            m.putParcelable("MemberInfo",Mi);
                            intent = new Intent(this, TestAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent.putExtra("Type",Type);
                            intent.putExtra("TrainingsInfo", Tsi);
                            intent.putExtra("MemberInfo", m);
                            intent.putStringArrayListExtra("list", list);

                            startActivity(intent);//  go Intent
                            finish();
                            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);

                        }else{

                            Log.e("test","==== Rpos === " + Rpos);
                            TrainingsDao TsDao = new TrainingsDao(this);
                            MemberDao mDao = new MemberDao(this);
                            List<TrainingsInfo> HistoryMember = new ArrayList<>();
                            int idx = TsDao.getHistoryIdx();
                            Tsi.setIdx(idx);
                            Tsi.setType(Type);
                            Tsi.setVsl(VSL);
                            Tsi.setVsl_type(LOGIN_CARRIER);
                            Tsi.setDate(Si.getDateTime());
                            Tsi.setScore(100);
                            Tsi.setSubmit("N");
                            TsDao.addHistory(Tsi);

                            for(int i=0; i < list.size();i++) {
                                MemberInfo Mbi = mDao.getMember(Integer.valueOf(list.get(i)));
                                TrainingsInfo Tsi = new TrainingsInfo();
                                Tsi.setHistroy_idx(idx);
                                Tsi.setMember_idx(Mbi.getIdx());

                                HistoryMember.add(Tsi);
                            }

                            TsDao.addHistoryMember(HistoryMember);
                            Bundle m = new Bundle();
                            m.putParcelable("MemberInfo",Mi);
                            Intent intent = new Intent(this, TestEndAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent.putExtra("MemberInfo", m);
                            intent.putExtra("Type", Type);
                            startActivity(intent);//  go Intent
                            finish();
                            overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                            Log.e("test","여기 안타나여");





                        }
                    }else if(Type.equals("G")){
                        Bundle m = new Bundle();
                        m.putParcelable("MemberInfo",Mi);
                        Log.e("test","s : " + Mi.getSurName() + "f : " + Mi.getFirstName());
                        intent = new Intent(this, TestAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.putExtra("Type",Type);
                        intent.putExtra("TrainingsInfo", Tsi);
                        intent.putExtra("MemberInfo", m);
                        intent.putStringArrayListExtra("list", list);

                        startActivity(intent);//  go Intent
                        finish();
                        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                    }

                }else{

                    Si.getMassage("ATTENDANCE", LOGIN_LANGUAGE, this, Toast.LENGTH_SHORT);
                }
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

    @Override
    protected void onDestroy(){
        super.onDestroy();
//        Log.e("AttendanceAct", "onDestroy");
        list.clear();
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
