package com.togetherseatech.whaleshark;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.togetherseatech.whaleshark.Db.History.TrainingDao;
import com.togetherseatech.whaleshark.Db.History.TrainingInfo;
import com.togetherseatech.whaleshark.Db.Member.MemberDao;
import com.togetherseatech.whaleshark.Db.Member.MemberInfo;
import com.togetherseatech.whaleshark.util.CommonUtil;
import com.togetherseatech.whaleshark.util.KeyboardUtils;
import com.togetherseatech.whaleshark.util.SelectItems;
import com.togetherseatech.whaleshark.util.SoftKeyboard;
import com.togetherseatech.whaleshark.util.TextFontUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * AlertDialog 엑티비티
 *
 */
public class LicensePopUpDialog extends Dialog implements View.OnClickListener {

	private RelativeLayout LicensePopUpLayout;
	private TextView PopUpLicense_Title_Tv;
	private EditText PopUpLicense_Et;
	private Button PopUpOk_Bt;
	private int signup;
	private String Language;
	private SharedPreferences pref;
	private SharedPreferences.Editor editor;
	private SelectItems Si;
	private Activity con;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
		lpWindow.screenBrightness = (float) 125/255;
		lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND|WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		getWindow().setAttributes(lpWindow);
		setContentView(R.layout.license_popup);

		initSetting();
		initEventListener();

		if("KR".equals(Language)){
			signup = R.drawable.popup_signup_kr_bt;
			PopUpLicense_Title_Tv.setText(R.string.license_title_kr);
		} else if("ENG".equals(Language)) {
			signup = R.drawable.popup_signup_eng_bt;
			PopUpLicense_Title_Tv.setText(R.string.license_title_eng);
			PopUpLicense_Title_Tv.setTextScaleX(0.8f);
		}

		PopUpOk_Bt.setBackgroundResource(signup);

		KeyboardUtils.addKeyboardToggleListener(con, new KeyboardUtils.SoftKeyboardToggleListener() {
			@Override
			public void onToggleSoftKeyboard(boolean isVisible)
			{
				Log.e("keyboard", "keyboard visible: "+isVisible);
				if(!isVisible) {
					 if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
						new CommonUtil().setHideNavigationBar(getWindow().getDecorView());
					}
				}
			}
		});
	}

	public LicensePopUpDialog(Activity act) {
		super(act , android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		this.con = act;
		this.pref = act.getSharedPreferences("pref", Context.MODE_PRIVATE);
		this.Language = pref.getString("LOGIN_LANGUAGE", "");
		this.Si = new SelectItems();

	}

	private void initSetting() {
		LicensePopUpLayout = (RelativeLayout)findViewById(R.id.license_popup_main);
		PopUpLicense_Title_Tv = (TextView)findViewById(R.id.license_popup_title_tv);
		PopUpLicense_Et = (EditText)findViewById(R.id.license_popup_et);
		PopUpLicense_Et.setFilters(new InputFilter[]{new InputFilter.AllCaps()}); // 소문자로 입력된 값을 대문자로 바꿔줌.
		PopUpOk_Bt = (Button)findViewById(R.id.license_popup_ok_bt);
	}
	
	private void initEventListener() {
		PopUpOk_Bt.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.license_popup_ok_bt:
				if (PopUpLicense_Et.getText().toString().trim().length() < 1) {
					Si.getMassage("LICENSE_NO", Language, con, Toast.LENGTH_SHORT);
				}else{
					MemberDao Mdio = new MemberDao(con);

					int cnt = Mdio.getKeyLicense(PopUpLicense_Et.getText().toString().trim());
					Log.e("LicensePopUpDialog", "cnt : " + cnt);
					Log.e("LicensePopUpDialog ", "자르면 : " + PopUpLicense_Et.getText().toString().trim());
					if (cnt > 0) {
						MemberInfo Date = new MemberInfo();

						if(cnt == 1) {
							Log.e("test","Date getTime ::: " + Si.getDateTime());
							Log.e("test","Date getTime2 ::: " + Si.getDateTime2());
							Date = Si.getDateTime(Si.getDateTime2());
							Date.setKey(PopUpLicense_Et.getText().toString().trim());
						} else {
							Log.e("test","Date1 : " + Date);
							Log.e("test","Date1222222 : " + Mdio.getLicenseSDate(cnt - 1));
							Date = Si.getDateTime(Mdio.getLicenseSDate(cnt - 1));
							Date.setKey(PopUpLicense_Et.getText().toString().trim());
						}

						int result = Mdio.updateKeyLicense(Date);
						if (result > 0) {
//							Log.e("LicensePopUpDialog", "result : " + result);
							Si.getMassage("LICENSE_OK", Language, con, Toast.LENGTH_SHORT);
							LogInAct.CLOSELICENSE = Si.CloseLicense(con);
						}else{
							Si.getMassage("LICENSE_NO", Language, con, Toast.LENGTH_SHORT);
						}
						dismiss();
						Mdio.getKeyLicense();
					} else {
						Si.getMassage("LICENSE_NO", Language, con, Toast.LENGTH_SHORT);
					}
				}
				break;
		}
		 if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
			new CommonUtil().setHideNavigationBar(con.getWindow().getDecorView());
		}
	}
}
