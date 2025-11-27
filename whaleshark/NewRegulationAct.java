package com.togetherseatech.whaleshark;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.togetherseatech.whaleshark.Db.History.TrainingDao;
import com.togetherseatech.whaleshark.Db.Member.MemberInfo;
import com.togetherseatech.whaleshark.Db.Training.TrainingsDao;
import com.togetherseatech.whaleshark.Db.Training.TrainingsInfo;
import com.togetherseatech.whaleshark.util.CommonUtil;
import com.togetherseatech.whaleshark.util.SelectItems;
import com.togetherseatech.whaleshark.util.TextFontUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by seonghak on 2017. 11. 3..
 */

public class NewRegulationAct extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener{

    private Button DashBoard_Bt, Training_Bt, History_Bt, Help_Bt;
    private ImageView NR_Language_Iv;
    private TextView NR_Name_Tv;
    private Intent intent;
    private SharedPreferences pref;
    private SelectItems Si;
    private String LOGIN_LANGUAGE, FileName;
    public static Activity Act;
    // test 230420
    private int volume = 15;
    // 액티비티에 보여지는 리스트뷰
    private ListView NR_Lv;
    // 리스트뷰에 사용할 어댑터
    private listView_Regulation_Adapter mAdapter;
    // 어댑터 및 리스트뷰에 사용할 데이터 리스트
    private List<TrainingsInfo> TisList;
    private TrainingsInfo Tsi;
    private ArrayList<SelectItems> SiList;
    private MemberInfo Mi;
    private TrainingsDao Tsdao;
    private Integer[] intArray = new Integer[]{0,1,2,3,4,5,6,7};
    private int listpos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_regulation_act);
        Log.e("test","New Regulation Oncreate");
        Act = this;
        pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
        LOGIN_LANGUAGE = pref.getString("LOGIN_LANGUAGE", "");
        Si = new SelectItems();
        Tsdao = new TrainingsDao(this);
        TisList = new ArrayList<TrainingsInfo>();
        /*** Bundle로 받는 부분 classException 에러 수정후 코드 **/
        Bundle M = getIntent().getBundleExtra("MemberInfo");
        Mi = M.getParcelable("MemberInfo");
        /*** Bundle로 받는 부분 classException 에러 수정후 코드 **/
        /*** Bundle로 받는 부분 classException 에러 수정전 코드 **/
        //Mi = (MemberInfo)getIntent().getExtras().get("MemberInfo");
        Log.e("test","처음에 들어오는 Mi : " +Mi.getId());

        initSetting();
        initEventListener();
//        Tis = new TrainingsInfo();
        SiList = Si.getNewRegulationRaw();
        List<TrainingsInfo> mList = Tsdao.getMatrixs("R", Si.getGTRankDept(Mi.getRank()));

        if("KR".equals(LOGIN_LANGUAGE)) {
            //TisList.add(new TrainingsInfo("2018-03-01", Si.getNewRegulation(0,LOGIN_LANGUAGE), "Res.MEPC.276(70)", SiList.get(0).getKr(),0,false));
            TisList.add(new TrainingsInfo("2018-03-01", Si.getNewRegulation(0,LOGIN_LANGUAGE), "Res.MEPC.277(70)", SiList.get(0).getKr(),0,false));
            TisList.add(new TrainingsInfo("2018-03-01", Si.getNewRegulation(1,LOGIN_LANGUAGE), "Res.MEPC.276(70)", SiList.get(1).getKr(),1,false));
            TisList.add(new TrainingsInfo("2014-07-01", Si.getNewRegulation(2,LOGIN_LANGUAGE), "Res.MSC.338(91)", SiList.get(2).getKr(),2,false));
            TisList.add(new TrainingsInfo("2019-01-01", Si.getNewRegulation(3,LOGIN_LANGUAGE), "Res.MEPC.286(71)", SiList.get(3).getKr(),3,false));
            TisList.add(new TrainingsInfo("2019-01-01", Si.getNewRegulation(4,LOGIN_LANGUAGE), "Cap.311AB", SiList.get(4).getKr(),4,false));
            TisList.add(new TrainingsInfo("2019-01-01", Si.getNewRegulation(5,LOGIN_LANGUAGE), "Official letter of China MCA", SiList.get(5).getKr(),5,false));
            TisList.add(new TrainingsInfo("2019-01-01", Si.getNewRegulation(6,LOGIN_LANGUAGE), "Official letter of Taiwan MOTC", SiList.get(6).getKr(),6,false));
            TisList.add(new TrainingsInfo("2020-01-01", Si.getNewRegulation(7,LOGIN_LANGUAGE), "2020 NEW Regulation : The first half", SiList.get(7).getKr(),7,false));
            TisList.add(new TrainingsInfo("2021-01-01", Si.getNewRegulation(8,LOGIN_LANGUAGE), " - ", SiList.get(8).getKr(),8,false));
            TisList.add(new TrainingsInfo("2021-01-01", Si.getNewRegulation(9,LOGIN_LANGUAGE), "MEPC.315(74) MARPOL ANNEX II Ch.5", SiList.get(9).getKr(),9,false));
            TisList.add(new TrainingsInfo("2021-01-01", Si.getNewRegulation(10,LOGIN_LANGUAGE), "MEPC.318(74) IBC code", SiList.get(10).getKr(),10,false));
            TisList.add(new TrainingsInfo("2021-01-01", Si.getNewRegulation(11,LOGIN_LANGUAGE), "MEPC.319(74) BCH code", SiList.get(11).getKr(),11,false));
            TisList.add(new TrainingsInfo("2021-01-01", Si.getNewRegulation(12,LOGIN_LANGUAGE), "MSC.428(98) ISM code", SiList.get(12).getKr(),12,false));
            TisList.add(new TrainingsInfo("2021-01-01", Si.getNewRegulation(13,LOGIN_LANGUAGE), "MSC.460(101) IBC Code", SiList.get(13).getKr(),13,false));
            TisList.add(new TrainingsInfo("2021-01-01", Si.getNewRegulation(14,LOGIN_LANGUAGE), "MSC.463(101) BCH code", SiList.get(14).getKr(),14,false));

            //test
            TisList.add(new TrainingsInfo("2023-01-01", Si.getNewRegulation(15,LOGIN_LANGUAGE), "Accident Report 1", SiList.get(15).getKr(),15,false));


        }else {
            //TisList.add(new TrainingsInfo("3/1/2018", Si.getNewRegulation(0,LOGIN_LANGUAGE), "Res.MEPC.276(70)", SiList.get(0).getEng(),0,false));
            TisList.add(new TrainingsInfo("3/1/2018", Si.getNewRegulation(0,LOGIN_LANGUAGE), "Res.MEPC.277(70)", SiList.get(0).getEng(),0,false));
            TisList.add(new TrainingsInfo("3/1/2018", Si.getNewRegulation(1,LOGIN_LANGUAGE), "Res.MEPC.276(70)", SiList.get(1).getEng(),1,false));
            TisList.add(new TrainingsInfo("7/1/2014", Si.getNewRegulation(2,LOGIN_LANGUAGE), "Res.MSC.338(91)", SiList.get(2).getEng(),2,false));
            TisList.add(new TrainingsInfo("1/1/2019", Si.getNewRegulation(3,LOGIN_LANGUAGE), "Res.MEPC.286(71)", SiList.get(3).getEng(),3,false));
            TisList.add(new TrainingsInfo("1/1/2019", Si.getNewRegulation(4,LOGIN_LANGUAGE), "Cap.311AB", SiList.get(4).getEng(),4,false));
            TisList.add(new TrainingsInfo("1/1/2019", Si.getNewRegulation(5,LOGIN_LANGUAGE), "Official letter of China MCA", SiList.get(5).getEng(),5,false));
            TisList.add(new TrainingsInfo("1/1/2019", Si.getNewRegulation(6,LOGIN_LANGUAGE), "Official letter of Taiwan MOTC", SiList.get(6).getEng(),6,false));
            TisList.add(new TrainingsInfo("1/1/2020", Si.getNewRegulation(7,LOGIN_LANGUAGE), "2020 NEW Regulation : The first half", SiList.get(7).getEng(),7,false));
            TisList.add(new TrainingsInfo("1/1/2021", Si.getNewRegulation(8,LOGIN_LANGUAGE), " - ", SiList.get(8).getEng(),8,false));
            TisList.add(new TrainingsInfo("1/1/2021", Si.getNewRegulation(9,LOGIN_LANGUAGE), "MEPC.315(74) MARPOL ANNEX II Ch.5", SiList.get(9).getEng(),9,false));
            TisList.add(new TrainingsInfo("1/1/2021", Si.getNewRegulation(10,LOGIN_LANGUAGE), "MEPC.318(74) IBC code", SiList.get(10).getEng(),10,false));
            TisList.add(new TrainingsInfo("1/1/2021", Si.getNewRegulation(11,LOGIN_LANGUAGE), "MEPC.319(74) BCH code", SiList.get(11).getEng(),11,false));
            TisList.add(new TrainingsInfo("1/1/2021", Si.getNewRegulation(12,LOGIN_LANGUAGE), "MSC.428(98) ISM code", SiList.get(12).getEng(),12,false));
            TisList.add(new TrainingsInfo("1/1/2021", Si.getNewRegulation(13,LOGIN_LANGUAGE), "MSC.460(101) IBC Code", SiList.get(13).getEng(),13,false));
            TisList.add(new TrainingsInfo("1/1/2021", Si.getNewRegulation(14,LOGIN_LANGUAGE), "MSC.463(101) BCH code", SiList.get(14).getEng(),14,false));

            //test
            TisList.add(new TrainingsInfo("1/1/2023", Si.getNewRegulation(15,LOGIN_LANGUAGE), "Accident Report 1", SiList.get(15).getEng(),15,false));

        }

        for(int i = 0; i < TisList.size(); i++) {
            for(TrainingsInfo tsi : mList) {
//                Log.e("NewRegulationAct" , i +" : " + tsi.getTraining_course());
                if(TisList.get(i).getTraining_course() == tsi.getTraining_course() - 1) {
//                    Log.e("NewRegulationAct" , i +" setMChecked : " + tsi.getTraining_course());
                    TisList.get(i).setMChecked(true);
                }
            }
        }
        // 커스텀 어댑터를 생성한다.
        mAdapter = new listView_Regulation_Adapter(this, R.layout.new_regulation_row, TisList);
        // 리스트뷰에 어댑터를 세팅한다.
        NR_Lv.setAdapter(mAdapter);

        NR_Name_Tv.setText(Mi.getSurName() + " " + Mi.getFirstName());
        if("KR".equals(LOGIN_LANGUAGE)) {
            NR_Language_Iv.setImageResource(R.mipmap.language_kr);
        }else if("ENG".equals(LOGIN_LANGUAGE)) {
            NR_Language_Iv.setImageResource(R.mipmap.language_eng);
        }
    }

    private void initSetting() {
        TextFontUtil tf = new TextFontUtil();
        DashBoard_Bt = (Button)findViewById(R.id.regulation_dashboard_bt);
        Training_Bt = (Button)findViewById(R.id.regulation_training_bt);
        Training_Bt.setBackgroundResource(R.mipmap.new_regulation_bt_on);
        History_Bt = (Button)findViewById(R.id.regulation_history_bt);
        Help_Bt = (Button)findViewById(R.id.regulation_help_bt);
        NR_Name_Tv = (TextView) findViewById(R.id.regulation_name_tv);
        tf.setNanumSquareL(this, NR_Name_Tv);
        NR_Language_Iv = (ImageView) findViewById(R.id.regulation_language_iv);
        NR_Lv = (ListView)findViewById(R.id.regulation_lv);
    }

    private void initEventListener() {
        DashBoard_Bt.setOnClickListener(this);
//        Training_Bt.setOnClickListener(this);
        History_Bt.setOnClickListener(this);
        Help_Bt.setOnClickListener(this);
        NR_Lv.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.regulation_dashboard_bt :
                Bundle m = new Bundle();
                m.putParcelable("MemberInfo",Mi);
                intent = new Intent(this, DashBoardMainAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("MemberInfo", m);
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;
            case R.id.regulation_history_bt :
//                intent = new Intent(this, CrewMemberAct.class);
                Bundle m1 = new Bundle();
                m1.putParcelable("MemberInfo",Mi);
                intent = new Intent(this, TrainingHistoryAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("MemberInfo", m1);
                intent.putExtra("Type", "R");
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                break;
            case R.id.regulation_help_bt :
                intent = new Intent(NewRegulationAct.this, GuideAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);//  go Intent
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_in);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Si.deleteExternalStoragePrivateFileAll(Act);
        /**2021.01.18 이상한 code 수정 intList 수정전 position != 8**/
        List<Integer> intList = new ArrayList<>(Arrays.asList(intArray));

        if(intList.contains(position)) {
            Log.e("test","video pos :" + intList.contains(position));
            Bundle m = new Bundle();
            m.putParcelable("MemberInfo",Mi);
            Intent intent = new Intent(getApplicationContext(), FullScreenExoPlayerAct.class);
            intent.putExtra("MemberInfo", m);
            intent.putExtra("Flash_video", TisList.get(position).getRaw());
            intent.putExtra("TrainingsInfo",TisList.get(position));
            intent.putExtra("pos",position);
            startActivity(intent);
            finish();
        } else {
            Log.e("test","pdf pos :" + position);
            listpos = position;
            FileName = TisList.get(position).getRaw();
            Tsi = TisList.get(position);
            String PackageName = getApplicationContext().getPackageName();
            File root = Environment.getExternalStorageDirectory();

            File expPath = new File(root.toString() + "/Android/obb/" + PackageName + File.separator + FileName +".pdf");

            Intent fileLinkIntent = new Intent(Intent.ACTION_VIEW);
            fileLinkIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), PackageName + ".fileprovider", expPath);
            fileLinkIntent.setDataAndType(contentUri, "application/pdf");
            fileLinkIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileLinkIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            try {
//                startActivity(fileLinkIntent);
                startActivityForResult(fileLinkIntent, 1);
            } catch(ActivityNotFoundException e) {
                Toast.makeText(getApplicationContext(), expPath.getPath() + "을 실항할 수 있는 어플리케이션이 없습니다.\n파일을 열 수 없습니다.",
                        Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        Log.e("test","requestCode :" + requestCode);
        Log.e("test","resultCode :" + resultCode);
        Log.e("test","data :" + data);
        if (requestCode == 1) {
        // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user pressed ok
               Log.e("test","pressed ok : " + resultCode);
            }else{
                // The user pressed cancel
              Log.e("test","pressed cancel : " + resultCode);
                Log.e("test","ActivityResult Mi : " +Mi.getId());
                Log.e("test","NewPos : " + listpos);
                Si.deleteExternalStoragePrivateFile(this,FileName+".mp4");
                NewRegulationAct.Act.finish();
                Bundle m = new Bundle();
                m.putParcelable("MemberInfo",Mi);
                Intent intent = new Intent(this, AttendanceAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("MemberInfo", m);
                Log.e("test","intent put :" + Mi);
                intent.putExtra("TrainingsInfo", Tsi);
                intent.putExtra("Type", "R");
                intent.putExtra("pos",listpos);
                intent.putExtra("TrainingsInfo",TisList.get(listpos));
                Log.e("test","test : " + TisList.get(listpos));

                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
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
