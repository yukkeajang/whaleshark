package com.togetherseatech.whaleshark;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.togetherseatech.whaleshark.Db.Member.MemberInfo;
import com.togetherseatech.whaleshark.util.CommonUtil;
import com.togetherseatech.whaleshark.util.TextFontUtil;

/**
 * AlertDialog 엑티비티
 *
 */
public class AgreementPopUpDialog extends Dialog implements View.OnClickListener {
	
	private Button AgreementPopUp_Bt, AgreementPopUp_No_Bt;
	private TextView AgreementPopUp_Tv;
	private Activity Act;
	private Intent intent;
	private SharedPreferences pref;
	private String LOGIN_LANGUAGE;
	private TextFontUtil tf;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
		lpWindow.screenBrightness = (float) 125/255;
		lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND|WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		getWindow().setAttributes(lpWindow);
		setContentView(R.layout.agreement_popup);

		initSetting();
		initEventListener();

		if("KR".equals(LOGIN_LANGUAGE)){
			AgreementPopUp_Tv.setText(R.string.agreement_kr);
			AgreementPopUp_Bt.setBackgroundResource(R.drawable.agreement_kr_bt);
			AgreementPopUp_No_Bt.setBackgroundResource(R.drawable.popup_ucancel_kr_bt);

		}else{
			AgreementPopUp_Tv.setText(R.string.agreement_eng);
			AgreementPopUp_Tv.setLineSpacing(1.0f, 0.70f);
			AgreementPopUp_Bt.setBackgroundResource(R.drawable.agreement_eng_bt);
			AgreementPopUp_No_Bt.setBackgroundResource(R.drawable.popup_ucancel_eng_bt);
		}
	}

	public AgreementPopUpDialog(Activity act) {
		super(act , android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		this.Act = act;
		pref = act.getSharedPreferences("pref", Context.MODE_PRIVATE);
		LOGIN_LANGUAGE = pref.getString("LOGIN_LANGUAGE", "");
		tf = new TextFontUtil();
	}

	private void initSetting() {
		AgreementPopUp_Tv = (TextView)findViewById(R.id.agreement_con);
		tf.setNanumSquareL(Act, AgreementPopUp_Tv);
		AgreementPopUp_Bt = (Button)findViewById(R.id.popup_ok_bt);
		AgreementPopUp_No_Bt = (Button)findViewById(R.id.popup_no_bt);
	}
	
	private void initEventListener() {
		AgreementPopUp_Bt.setOnClickListener(this);
		AgreementPopUp_No_Bt.setOnClickListener(this);
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.popup_ok_bt:
//				intent = new Intent(Act, DashBoardMainAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
				intent = new Intent(Act, CrewMemberAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				Act.startActivity(intent);//  go Intent
				Act.finish();
				Act.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);

				dismiss();
				break;
			case R.id.popup_no_bt:

				dismiss();
				break;

		}
		 if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
			new CommonUtil().setHideNavigationBar(Act.getWindow().getDecorView());
		}
	}
}
