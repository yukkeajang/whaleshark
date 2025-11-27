package com.togetherseatech.whaleshark;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.togetherseatech.whaleshark.util.CommonUtil;
import com.togetherseatech.whaleshark.util.SelectItems;
import com.togetherseatech.whaleshark.util.TextFontUtil;
import com.togetherseatech.whaleshark.util.XmlSAXParser3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bluepsh on 2017-10-19.
 */

public class GuideAct extends Activity implements View.OnClickListener {

    private String Tag = "GuideAct";
    private FrameLayout Guide_FrameLayout;
    private Fragment mFragment;
    private FragmentTransaction ft;

    private ImageView Guide_Close_iv;
    private ScrollView Guide_sv;
    private TextView Guide_tv, Guide_tv2;
    public static ImageView Guide_Iv;
    // 액티비티에 보여지는 리스트뷰
    private ExpandableListView Guide_ExpandableListView;
    // 리스트뷰에 사용할 어댑터
    private GuideExpandableAdapter Guide_ExpandableAdapter;

    private static Activity Act;
    private TextFontUtil tf;
    private ArrayList<Map<String,String>> Guide_GroupList;
    private ArrayList<Map<String,String>> Guide_ChildContentList;
    private ArrayList<ArrayList<Map<String,String>>> Guide_ChildList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_act);
        Act = this;
        tf = new TextFontUtil();
        initSetting();
        initEventListener();

        Guide_GroupList = new ArrayList<>();
        Guide_ChildList = new ArrayList<>();

        Map<String, String> Gmap = new HashMap<>();
        Map<String, String> Cmap = new HashMap<>();
        Gmap.put("num", "1.");
        Gmap.put("kr", "WhaleShark");
        Gmap.put("eng", "About WhaleShark");
        Gmap.put("check", "true");
        Guide_GroupList.add(Gmap);
        Guide_ChildList.add(new ArrayList<Map<String,String>>());

        Gmap = new HashMap<>();
        Gmap.put("num", "2.");
        Gmap.put("kr", "WhaleShark 기능");
        Gmap.put("eng", "WhaleShark’s function");
        Gmap.put("check", "false");
        Guide_GroupList.add(Gmap);
        Guide_ChildList.add(new ArrayList<Map<String,String>>());

        Gmap = new HashMap<>();
        Gmap.put("num", "3.");
        Gmap.put("kr", "라이선스 번호");
        Gmap.put("eng", "License number");
        Gmap.put("check", "false");
        Guide_GroupList.add(Gmap);
        Guide_ChildList.add(new ArrayList<Map<String,String>>());

        Gmap = new HashMap<>();
        Gmap.put("num", "4.");
        Gmap.put("kr", "관리자 모드");
        Gmap.put("eng", "Administration mode");
        Gmap.put("check", "false");
        Guide_GroupList.add(Gmap);
//        Guide_ChildList.add(new ArrayList<Map<String,String>>());


        Guide_ChildContentList = new ArrayList<>();
        Cmap.put("num", "4.1");
        Cmap.put("kr", "사용자 등록하기");
        Cmap.put("eng", "User registration");
        Gmap.put("check", "false");
        Guide_ChildContentList.add(Cmap);
        Cmap = new HashMap<>();
        Cmap.put("num", "4.2");
        Cmap.put("kr", "사용자 삭제하기");
        Cmap.put("eng", "Delete the user");
        Gmap.put("check", "false");
        Guide_ChildContentList.add(Cmap);
        Cmap = new HashMap<>();
        Cmap.put("num", "4.3");
        Cmap.put("kr", "매트릭스");
        Cmap.put("eng", "Matrix");
        Gmap.put("check", "false");
        Guide_ChildContentList.add(Cmap);
        Cmap = new HashMap<>();
        Cmap.put("num", "4.4");
        Cmap.put("kr", "모니터링");
        Cmap.put("eng", "Monitoring");
        Gmap.put("check", "false");
        Guide_ChildContentList.add(Cmap);
        Cmap = new HashMap<>();
        Cmap.put("num", "4.5");
        Cmap.put("kr", "인터넷 조건에서 사용자 Histroy 전송하기");
        Cmap.put("eng", "Sending the user histories under Internet condition");
        Gmap.put("check", "false");
        Guide_ChildContentList.add(Cmap);
        Cmap = new HashMap<>();
        Cmap.put("num", "4.6");
        Cmap.put("kr", "E-mail으로 사용자 History 전송하기");
        Cmap.put("eng", "Sending the user histories via E-mail");
        Gmap.put("check", "false");
        Guide_ChildContentList.add(Cmap);
        Cmap = new HashMap<>();
        Cmap.put("num", "4.7");
        Cmap.put("kr", "인터넷 조건에서 교육 자료 다운로드 하기");
        Cmap.put("eng", "Downloading training materials under Internet condition");
        Gmap.put("check", "false");
        Guide_ChildContentList.add(Cmap);
        Cmap = new HashMap<>();
        Cmap.put("num", "4.8");
        Cmap.put("kr", "USB 메모리 카드를 통한 교육 자료 다운로드 하기");
        Cmap.put("eng", "Downloading training materials via USB memory card");
        Gmap.put("check", "false");
        Guide_ChildContentList.add(Cmap);
        Guide_ChildList.add(Guide_ChildContentList);


        Gmap = new HashMap<>();
        Gmap.put("num", "5.");
        Gmap.put("kr", "일반 모드");
        Gmap.put("eng", "General mode");
        Gmap.put("check", "false");
        Guide_GroupList.add(Gmap);
//        Guide_ChildList.add(new ArrayList<Map<String,String>>());
        Guide_ChildContentList = new ArrayList<>();
        Cmap = new HashMap<>();
        Cmap.put("num", "5.1");
        Cmap.put("kr", "Crew Evaluation 이용하기");
        Cmap.put("eng", "Using Crew Evaluation");
        Gmap.put("check", "false");
        Guide_ChildContentList.add(Cmap);
        Cmap = new HashMap<>();
        Cmap.put("num", "5.2");
        Cmap.put("kr", "General Training 이용하기");
        Cmap.put("eng", "Use the General Training");
        Gmap.put("check", "false");
        Guide_ChildContentList.add(Cmap);
        Cmap = new HashMap<>();
        Cmap.put("num", "5.3");
        Cmap.put("kr", "New Regulation 이용하기");
        Cmap.put("eng", "Use the New Regulation");
        Gmap.put("check", "false");
        Guide_ChildContentList.add(Cmap);
        Guide_ChildList.add(Guide_ChildContentList);
        Gmap = new HashMap<>();
        Gmap.put("num", "6.");
        Gmap.put("kr", "주의사항");
        Gmap.put("eng", "Precautions");
        Gmap.put("check", "false");
        Guide_GroupList.add(Gmap);
        Guide_ChildList.add(new ArrayList<Map<String,String>>());

        Gmap = new HashMap<>();
        Gmap.put("num", "7.");
        Gmap.put("kr", "액세서리 (옵션 품목)");
        Gmap.put("eng", "Accessory (Option item)");
        Gmap.put("check", "false");
        Guide_GroupList.add(Gmap);
        Guide_ChildList.add(new ArrayList<Map<String,String>>());

        Gmap = new HashMap<>();
        Gmap.put("num", "8.");
        Gmap.put("kr", "문제해결");
        Gmap.put("eng", "Troubleshooting");
        Gmap.put("check", "false");
        Guide_GroupList.add(Gmap);
        Guide_ChildList.add(new ArrayList<Map<String,String>>());


        /*for(int i=0; i < Guide_ChildList.size();i++){
            if(Guide_ChildList.get(i) != null) {
                for (int j = 0; j < Guide_ChildList.get(i).size(); j++) {
                    Log.e(Tag, Guide_ChildList.get(i).get(j) + "/" + i + "/" + j);
                }
            }
        }*/
        // 커스텀 어댑터를 생성한다.
        Guide_ExpandableAdapter = new GuideExpandableAdapter(Act, Guide_GroupList, Guide_ChildList);
        Guide_ExpandableListView.setItemChecked(0,true);
        // 리스트뷰에 어댑터를 세팅한다.
        Guide_ExpandableListView.setAdapter(Guide_ExpandableAdapter);
        if(Guide_GroupList.size() > 0) {
            for(int i = 0; i < Guide_GroupList.size(); i++) {
                if(Guide_ChildList.get(i) != null)
                    Guide_ExpandableListView.expandGroup(i, true);
            }
        }
        Guide_ExpandableListView.smoothScrollToPosition(0);
//        Guide_ExpandableListView.setItemChecked(1,true);
        Guide_ExpandableListView.collapseGroup(0);
        ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.guide_fl, new GuideFrag(Act, 0));
        ft.commit();
        Guide_ExpandableListView.setSelectedGroup(0);
    }

    private void initSetting() {
        Guide_tv = (TextView)findViewById(R.id.guide_tv);
        tf.setNanumSquareB(this, Guide_tv);
//        Guide_tv2 = (TextView)findViewById(R.id.guide_tv2);
//        tf.setNanumSquareB(this, Guide_tv2);
        Guide_ExpandableListView = (ExpandableListView)findViewById(R.id.guide_elv);
//        Guide_Iv = (ImageView) findViewById(R.id.guide_iv);
//        Guide_Iv.setBackgroundResource(R.mipmap.guid_1);
        Guide_sv = (ScrollView)findViewById(R.id.guide_sv);
        Guide_Close_iv = (ImageView) findViewById(R.id.guide_close_iv);
        Guide_FrameLayout = (FrameLayout)findViewById(R.id.guide_fl);
    }

    private void initEventListener() {
        Guide_ExpandableListView.setOnGroupClickListener(setOnGroupClickEvent);
        Guide_ExpandableListView.setOnChildClickListener(setOnChildClickEvent);
        Guide_Close_iv.setOnClickListener(this);
    }

    private ExpandableListView.OnGroupClickListener setOnGroupClickEvent = new ExpandableListView.OnGroupClickListener() {
        int lastClickedPosition = 0;

        @Override
        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//             선택 한 groupPosition의 펼침/닫힘 상태 체크
//            Boolean isExpand = (!Guide_ExpandableListView.isGroupExpanded(groupPosition));

//             이 전에 열려있던 group 닫기
//            Guide_ExpandableListView.collapseGroup(lastClickedPosition);

//            if(isExpand){
//                Guide_ExpandableListView.expandGroup(groupPosition);
//            }
//            lastClickedPosition = groupPosition;

            for(int i=0; i < Guide_ChildList.size(); i++){
                if(i != groupPosition) {
                    Guide_GroupList.get(i).put("check", "false");
                }
                for(int j=0; j < Guide_ChildList.get(i).size(); j++) {
                    Guide_ChildList.get(i).get(j).put("check", "false");
                }
            }

            if(groupPosition != 3 && groupPosition != 4)
                Guide_GroupList.get(groupPosition).put("check", "true");
            else {
                Guide_GroupList.get(groupPosition).put("check", "false");
                Guide_ChildList.get(groupPosition).get(0).put("check", "true");
            }

            Guide_ExpandableAdapter.notifyDataSetChanged();

            Guide_sv.smoothScrollTo(0,0);
            if(Guide_ChildList != null){
                /*if(groupPosition == 0)
                    Guide_Iv.setBackgroundResource(R.mipmap.guid_1);
                else if(groupPosition == 1)
                    Guide_Iv.setBackgroundResource(R.mipmap.guid_2);
                else if(groupPosition == 2)
                    Guide_Iv.setBackgroundResource(R.mipmap.guid_3);
                *//*else if(groupPosition == 3)
                    Guide_Iv.setBackgroundResource(R.mipmap.guid_4_1);
                else if(groupPosition == 4)
                    Guide_Iv.setBackgroundResource(R.mipmap.guid_5_1);*//*
                else if(groupPosition == 5)
                    Guide_Iv.setBackgroundResource(R.mipmap.guid_6);
                else if(groupPosition == 6)
                    Guide_Iv.setBackgroundResource(R.mipmap.guid_7);
                else if(groupPosition == 7)
                    Guide_Iv.setBackgroundResource(R.mipmap.guid_8);*/
                if(groupPosition != 3 && groupPosition != 4)
                    mFragment = new GuideFrag(Act, groupPosition);
                else
                    mFragment = new GuideFrag(Act, groupPosition , 0);
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.guide_fl, mFragment);
                ft.commit();
            }
            return true;
        }
    };

    private ExpandableListView.OnChildClickListener setOnChildClickEvent = new ExpandableListView.OnChildClickListener() {

        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            Log.e(Tag, "setOnChildClickEvent id : " + id);
            Log.e(Tag, "setOnChildClickEvent groupPosition : " + groupPosition);
            Log.e(Tag, "setOnChildClickEvent childPosition : " + childPosition);

            Guide_ChildList.get(groupPosition).get(childPosition).put("check", "true");
            for(int i=0; i < Guide_ChildList.size(); i++){
                Guide_GroupList.get(i).put("check", "false");
                for(int j=0; j < Guide_ChildList.get(i).size(); j++) {
                    if (!(i == groupPosition && j == childPosition)){
                        Guide_ChildList.get(i).get(j).put("check", "false");
                    }
                }
            }
            Guide_ExpandableAdapter.notifyDataSetChanged();

            Guide_sv.smoothScrollTo(0,0);
            /*if(Integer.valueOf(v.getId()) == 30)
                GuideAct.Guide_Iv.setBackgroundResource(R.mipmap.guid_4_1);
            else if(Integer.valueOf(v.getId()) == 31)
                GuideAct.Guide_Iv.setBackgroundResource(R.mipmap.guid_4_2);
            else if(Integer.valueOf(v.getId()) == 32)
                GuideAct.Guide_Iv.setBackgroundResource(R.mipmap.guid_4_3);
            else if(Integer.valueOf(v.getId()) == 33)
                GuideAct.Guide_Iv.setBackgroundResource(R.mipmap.guid_4_4);
            else if(Integer.valueOf(v.getId()) == 34)
                GuideAct.Guide_Iv.setBackgroundResource(R.mipmap.guid_4_5);
            else if(Integer.valueOf(v.getId()) == 35)
                GuideAct.Guide_Iv.setBackgroundResource(R.mipmap.guid_4_6);
            else if(Integer.valueOf(v.getId()) == 36)
                GuideAct.Guide_Iv.setBackgroundResource(R.mipmap.guid_4_7);
            else if(Integer.valueOf(v.getId()) == 40)
                GuideAct.Guide_Iv.setBackgroundResource(R.mipmap.guid_5_1);
            else if(Integer.valueOf(v.getId()) == 41)
                GuideAct.Guide_Iv.setBackgroundResource(R.mipmap.guid_5_2);
            else if(Integer.valueOf(v.getId()) == 42)
                GuideAct.Guide_Iv.setBackgroundResource(R.mipmap.guid_5_3);
            else if(Integer.valueOf(v.getId()) == 43)
                GuideAct.Guide_Iv.setBackgroundResource(R.mipmap.guid_5_4);*/
            mFragment = new GuideFrag(Act, groupPosition, childPosition);
            ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.guide_fl, mFragment);
            ft.commit();

            return false;
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.guide_close_iv:
                finish();
                overridePendingTransition(0, android.R.anim.fade_out);

                break;

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

    @Override
    protected void onStart(){
        super.onStart();
        Log.e(Tag, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(Tag, "onResume");
         if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
            new CommonUtil().setHideNavigationBar(getWindow().getDecorView());
        }
    }

    @Override
    protected void onUserLeaveHint(){
        super.onUserLeaveHint();
        Log.e(Tag, "onUserLeaveHint");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.e(Tag, "onPause");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(Tag, "onStop");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.e(Tag, "onDestroy");
    }

}
