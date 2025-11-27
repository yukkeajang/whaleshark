package com.togetherseatech.whaleshark;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.content.Intent;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.togetherseatech.whaleshark.Db.Member.MemberDao;
import com.togetherseatech.whaleshark.Db.Member.MemberInfo;
import com.togetherseatech.whaleshark.util.CommonUtil;
import com.togetherseatech.whaleshark.util.KeyboardUtils;
import com.togetherseatech.whaleshark.util.SelectItems;
import com.togetherseatech.whaleshark.util.SoftKeyboard;
import com.togetherseatech.whaleshark.util.TextFontUtil;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.widget.AdapterView.*;

/**
 * Created by bluepsh on 2017-10-19.
 */

public class LogInAct extends Activity implements OnClickListener, OnItemSelectedListener, CompoundButton.OnCheckedChangeListener, OnFocusChangeListener{

    private RelativeLayout LoginLayout;
    private TextView LogIn_Title, LogIn_Con;
    public static Spinner LogIn_Carrier;
    private EditText LogIn_Id, LogIn_Pw;
    private Button LogIn_Back, LogIn;
    private ToggleButton LogIn_Language;
    private Intent intent;
    private Activity Act;
    private ArrayList<String> areaList;
    private ArrayAdapter<String> Aa_Carrier;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String LOGIN_MODE;
    public static Boolean CLOSELICENSE;
    private SelectItems Si;
    private TextFontUtil tf;
    private MemberInfo VslMi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_act);


        // 날짜 & 시간 설정을 자동
        Settings.System.putInt(getContentResolver(), Settings.Global.AUTO_TIME, 1);

        Act = this;
        pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.putString("LOGIN_LANGUAGE", "KR");
        editor.putInt("LOGIN_CARRIER", 0);
        editor.commit();
        LOGIN_MODE = pref.getString("LOGIN_MODE","");
        Log.e("test","LOGIN_MODE : " + LOGIN_MODE);
//        CLOSELICENSE = pref.getBoolean("CLOSELICENSE", true);
        VslMi = new MemberInfo();
        tf = new TextFontUtil();
        initSetting();
        initEventListener();



        /*InputMethodManager controlManager = (InputMethodManager)getSystemService(Service.INPUT_METHOD_SERVICE);
        softKeyboard = new SoftKeyboard(LoginLayout, controlManager);
        softKeyboard.setSoftKeyboardCallback(new SoftKeyboard.SoftKeyboardChanged() {
            @Override
            public void onSoftKeyboardHide() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        //키보드 내려왔을때
                        Pattern ps = Pattern.compile("^[a-zA-Z ]+$");
                        Matcher ms = ps.matcher(LogIn_Id.getText().toString());
                        boolean bs = ms.matches();

                        if (bs == false) {
//                        Si.getMassage("ENG", pref.getString("LOGIN_LANGUAGE",""), this, Toast.LENGTH_SHORT);
                        }else{
                            MemberDao Mdao = new MemberDao(Act);
                            VslMi = Mdao.getVslMember(LogIn_Id.getText().toString().trim(), LOGIN_MODE);
                            if(VslMi != null)
                                LogIn_Carrier.setSelection(VslMi.getVsl_type());
                            else
                                LogIn_Carrier.setSelection(0);
                        }

                         if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL)) {
                            new CommonUtil().setHideNavigationBar(getWindow().getDecorView());
                        }
                    }
                });
            }

            @Override
            public void onSoftKeyboardShow() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        //키보드 올라왔을때
                    }
                });
            }


        });*/

        KeyboardUtils.addKeyboardToggleListener(this, new KeyboardUtils.SoftKeyboardToggleListener() {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible)
            {
//                Log.e("keyboard", "keyboard visible: "+isVisible);
                if(!isVisible) {
                    if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
                        new CommonUtil().setHideNavigationBar(getWindow().getDecorView());
                    }
                }
            }
        });

        Si = new SelectItems();

        areaList = new ArrayList<String>();
        areaList = Si.getSelectMemberVslType2();

        Aa_Carrier = new ArrayAdapter<String>(this, R.layout.spinner_item, areaList );
        Aa_Carrier.setDropDownViewResource(R.layout.spinner_dropdown_item);
        LogIn_Carrier.setAdapter(Aa_Carrier);

        CLOSELICENSE = Si.CloseLicense(this);
//        Log.e("LogInAct" , "CLOSELICENSE : "+CLOSELICENSE);
    }

    private void initSetting() {

        LoginLayout = (RelativeLayout)findViewById(R.id.loginLayout);
        LogIn_Title = (TextView)findViewById(R.id.login_title_tv);
        LogIn_Con = (TextView)findViewById(R.id.login_con_tv);
        if("General".equals(LOGIN_MODE)) {
            LoginLayout.setBackgroundResource(R.mipmap.rogmode_g);
            LogIn_Title.setText(R.string.general_title_kr);

        }else {
            LoginLayout.setBackgroundResource(R.mipmap.rogmode_a);
            LogIn_Title.setText(R.string.administration_title_kr);
        }
        LogIn_Con.setText(R.string.login_con_kr);
        LogIn_Back  = (Button)findViewById(R.id.login_back_bt);
        LogIn_Language = (ToggleButton)findViewById(R.id.login_language_tbt);
        LogIn_Id = (EditText)findViewById(R.id.login_id_et);
        LogIn_Id.setFilters(new InputFilter[]{new InputFilter.AllCaps()}); // 소문자로 입력된 값을 대문자로 바꿔줌.
        LogIn_Pw = (EditText)findViewById(R.id.login_pw_et);
        LogIn_Pw.setFilters(new InputFilter[]{new InputFilter.AllCaps()}); // 소문자로 입력된 값을 대문자로 바꿔줌.
        LogIn_Carrier = (Spinner)findViewById(R.id.login_carrier_sp);
        LogIn = (Button)findViewById(R.id.login_bt);
    }

    private void initEventListener() {

        LogIn_Back.setOnClickListener(this);
        LogIn_Language.setOnCheckedChangeListener(this);
        LogIn.setOnClickListener(this);
        LogIn_Id.setOnFocusChangeListener(this);
//        LogIn_Id.setCursorVisible(false);
        LogIn_Pw.setOnFocusChangeListener(this);
        LogIn_Carrier.setOnItemSelectedListener(this);
        LogIn_Carrier.setEnabled(false);
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
//        Log.e("LogInAct" , "onFocusChange : "+v.getId());
        Pattern ps = Pattern.compile("^[a-zA-Z ]+$");
        Matcher ms = ps.matcher(LogIn_Id.getText().toString());
        boolean bs = ms.matches();
        switch (v.getId()) {
            case R.id.login_id_et:
                if(!hasFocus) {
                    if (bs == false) {
//                        Si.getMassage("ENG", pref.getString("LOGIN_LANGUAGE",""), this, Toast.LENGTH_SHORT);
                    }else{
//                        Log.e("LogInAct" , "onFocusChange : "+hasFocus);
                        MemberDao Mdao = new MemberDao(this);
                        VslMi = Mdao.getVslMember(LogIn_Id.getText().toString().trim(), LOGIN_MODE);
                        /*if("GOLDEN AI HANAA".equals(VslMi.getVsl())) {
                            Mdao.updateVslName("GOLDEN AI HANA");
                        }*/
                        if(VslMi != null)
                            LogIn_Carrier.setSelection(VslMi.getVsl_type());
                        else
                            LogIn_Carrier.setSelection(0);
                    }
                }
                break;
            case R.id.login_pw_et:
//                if (bs == false)
//                    LogIn_Id.requestFocus();
                break;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        editor.putInt("LOGIN_CARRIER", parent.getSelectedItemPosition());
        editor.commit();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            editor.putString("LOGIN_LANGUAGE", "ENG");
            LogIn_Language.setBackgroundDrawable(ContextCompat.getDrawable(this, R.mipmap.mode_eng));
            Log.e("test","LOGIN_MODE : " + LOGIN_MODE);
            if("General".equals(LOGIN_MODE)) {
                LogIn_Title.setText(R.string.general_title_eng);
            }else{
                LogIn_Title.setText(R.string.administration_title_eng);
            }
            LogIn_Con.setText(R.string.login_con_eng);
        }else{
            LogIn_Language.setBackgroundDrawable(ContextCompat.getDrawable(this, R.mipmap.mode_kr));
            editor.putString("LOGIN_LANGUAGE", "KR");
            if("General".equals(LOGIN_MODE)) {
                LogIn_Title.setText(R.string.general_title_kr);
            }else{
                LogIn_Title.setText(R.string.administration_title_kr);
            }
            LogIn_Con.setText(R.string.login_con_kr);
        }
        editor.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_back_bt:
                intent = new Intent(LogInAct.this, MainAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                break;
            case R.id.login_bt:
//                Log.e("login","//"+CLOSELICENSE);
                if(!CLOSELICENSE) {
//                    Log.e("login","//"+LogIn_Pw.getText().toString().trim().length()+"/"+ LogIn_Pw.getText().toString());
                    if (LogIn_Id.getText().toString().trim().length() < 1) {
                        Si.getMassage("ID", pref.getString("LOGIN_LANGUAGE", ""), this, Toast.LENGTH_SHORT);
                    } else if (LogIn_Pw.getText().toString().trim().length() < 1) {

                        Si.getMassage("PASSWORD", pref.getString("LOGIN_LANGUAGE", ""), this, Toast.LENGTH_SHORT);
                    }/* else if(LogIn_Carrier.getSelectedItemPosition() == 0){
                    Si.getMassage("VSL_TYPE", pref.getString("LOGIN_LANGUAGE",""), this);
                }*/ else {
                        if("9999".equals(LogIn_Pw.getText().toString().trim())){
                            Log.e("test","login?");
                            new CommonUtil().setHideDialog(new InitSettingPopUpDialog(Act, "9999"));
                        }else{
                            if (VslMi.getPw() != null) {
                                if ("Administration".equals(LOGIN_MODE)) {
                                    if (VslMi.getId().equals(LogIn_Id.getText().toString().trim()) && VslMi.getPw().equals(LogIn_Pw.getText().toString().trim())) {
                                        editor.putString("VSL", VslMi.getVsl());
                                        editor.putInt("MSTER_IDX", VslMi.getMaster_idx());
                                        editor.commit();

                                        intent = new Intent(LogInAct.this, AdminAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(intent);//  go Intent
                                        finish();
                                        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                                    } else {
                                        Si.getMassage("CHECK", pref.getString("LOGIN_LANGUAGE", ""), this, Toast.LENGTH_SHORT);
                                    }
                                } else {
                                    if (VslMi.getId().equals(LogIn_Id.getText().toString().trim()) && VslMi.getPw().equals(LogIn_Pw.getText().toString().trim())) {
                                        editor.putString("VSL", VslMi.getVsl());
                                        editor.putInt("MSTER_IDX", VslMi.getMaster_idx());
                                        editor.commit();

                                        new CommonUtil().setHideDialog(new AgreementPopUpDialog(this));

                                    } else {
                                        Si.getMassage("CHECK", pref.getString("LOGIN_LANGUAGE", ""), this, Toast.LENGTH_SHORT);
                                    }
                                }
                            } else {
                                Si.getMassage("CHECK", pref.getString("LOGIN_LANGUAGE", ""), this, Toast.LENGTH_SHORT);
                            }
                        }

                    }
                }else{
                    new CommonUtil().setHideDialog(new LicensePopUpDialog(this));
                }
                break;
        }
    }

    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 처리 안함
        }
    };

    @Override
    protected void onStart(){
        super.onStart();
        Log.e("LogInAct", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Log.e("LogInAct", "onResume");
        if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
            new CommonUtil().setHideNavigationBar(getWindow().getDecorView());
        }
        /*IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        registerReceiver(mIntentReceiver, filter, null, null);*/
    }

    @Override
    protected void onUserLeaveHint(){
        super.onUserLeaveHint();
//        Log.e("LogInAct", "onUserLeaveHint");
    }

    @Override
    protected void onPause(){
        super.onPause();
//        Log.e("LogInAct", "onPause");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("LogInAct", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Log.e("LogInAct", "KEYCODE_BACK");
                break;
        }
        return true;
    }

}
