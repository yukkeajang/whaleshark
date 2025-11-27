package com.togetherseatech.whaleshark;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.togetherseatech.whaleshark.Db.Member.MemberDao;
import com.togetherseatech.whaleshark.Db.Member.MemberInfo;
import com.togetherseatech.whaleshark.util.CommonUtil;
import com.togetherseatech.whaleshark.util.SelectItems;

import java.util.ArrayList;

/**
 * AlertDialog 엑티비티
 *
 */
public class InitSettingPopUpDialog extends Dialog implements View.OnClickListener, AdapterView.OnItemSelectedListener {

	private TextView PopUpInitSetting_Title_Tv;
	private EditText PopUpInitSetting_Et;
	private Spinner PopUpInitSetting_Sp;
	private Button PopUpOk_Bt;
	private int signup;
	private String Language, type = "";
	private SharedPreferences pref;
	private SharedPreferences.Editor editor;
	private SelectItems Si;
	private Context con;
	private Activity act;
	private ArrayList<String> areaList;
	private ArrayAdapter<String> Aa_Carrier;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
		lpWindow.screenBrightness = (float) 125/255;
		lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND|WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		getWindow().setAttributes(lpWindow);
		setContentView(R.layout.init_setting_popup);

		initSetting();
		initEventListener();

//		if("KR".equals(Language)){
//			signup = R.drawable.popup_signup_kr_bt;
//			PopUpInitSetting_Title_Tv.setText(R.string.license_title_kr);
//		} else if("ENG".equals(Language)) {
//			signup = R.drawable.popup_signup_eng_bt;
//			PopUpInitSetting_Title_Tv.setText(R.string.license_title_eng);
//			PopUpInitSetting_Title_Tv.setTextScaleX(0.8f);
//		}

		areaList = new ArrayList<String>();
		areaList = Si.getSelectMemberVslType3();

		Aa_Carrier = new ArrayAdapter<String>(con, R.layout.spinner_item, areaList );
		Aa_Carrier.setDropDownViewResource(R.layout.spinner_dropdown_item);
		PopUpInitSetting_Sp.setAdapter(Aa_Carrier);

//		PopUpOk_Bt.setBackgroundResource(signup);
	}

	public InitSettingPopUpDialog(Activity act) {
		super(act , android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		this.con = act;
		this.act = act;
		this.pref = con.getSharedPreferences("pref", Context.MODE_PRIVATE);
//		this.Language = pref.getString("LOGIN_LANGUAGE", "");
		this.Si = new SelectItems();

	}

	public InitSettingPopUpDialog(Activity act, String type) {
		super(act , android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		this.con = act;
		this.act = act;
		this.pref = con.getSharedPreferences("pref", Context.MODE_PRIVATE);
//		this.Language = pref.getString("LOGIN_LANGUAGE", "");
		this.Si = new SelectItems();
		this.type = type;

	}

	private void initSetting() {
		PopUpInitSetting_Title_Tv = (TextView)findViewById(R.id.init_title_tv);
		PopUpInitSetting_Et = (EditText)findViewById(R.id.init_vsl_et);
		PopUpInitSetting_Sp = (Spinner)findViewById(R.id.init_carrier_sp);
		PopUpOk_Bt = (Button)findViewById(R.id.init_ok_bt);
	}
	
	private void initEventListener() {
		PopUpOk_Bt.setOnClickListener(this);
		PopUpInitSetting_Sp.setOnItemSelectedListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.init_ok_bt:
				if (PopUpInitSetting_Et.getText().toString().trim().length() < 1) {
					Si.getMassage("VSL_NAME", "ENG", con, Toast.LENGTH_SHORT);
				}else if(PopUpInitSetting_Sp.getSelectedItemPosition() == 0){
					Si.getMassage("VSL_TYPE", "ENG", con, Toast.LENGTH_SHORT);
				}else{
					MemberDao Mdao = new MemberDao(con);
					Mdao.updateVslName(PopUpInitSetting_Et.getText().toString().trim(), PopUpInitSetting_Sp.getSelectedItemPosition());

					editor = pref.edit();
					editor.putString("VSL", PopUpInitSetting_Et.getText().toString().trim());
					editor.putInt("LOGIN_CARRIER", PopUpInitSetting_Sp.getSelectedItemPosition());
					editor.commit();
					dismiss();

					if(type.equals("9999")){
						Intent intent = new Intent(act, MainAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
						intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
						act.startActivity(intent);//  go Intent
						act.finish();
						act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
					}
				}
				break;
		}
		 if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
			new CommonUtil().setHideNavigationBar(act.getWindow().getDecorView());
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

}
