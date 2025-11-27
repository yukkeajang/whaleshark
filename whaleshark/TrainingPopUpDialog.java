package com.togetherseatech.whaleshark;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.togetherseatech.whaleshark.Db.Member.MemberInfo;
import com.togetherseatech.whaleshark.util.CommonUtil;
import com.togetherseatech.whaleshark.util.SelectItems;

/**
 * AlertDialog 엑티비티
 *
 */
public class TrainingPopUpDialog extends Dialog implements View.OnClickListener {

	private TextView PopUpTraining_Title_Tv, PopUpTraining_Con_Tv;
	private Button PopUpOk_Bt, PopUpNo_Bt;
	private String Language, type = "";
	private SharedPreferences pref;
	private SharedPreferences.Editor editor;
	private SelectItems Si;
	private Context con;
	private Activity act;
	private MemberInfo Mi;
	private int Position = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
		lpWindow.screenBrightness = (float) 125/255;
		lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND|WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		getWindow().setAttributes(lpWindow);
		setContentView(R.layout.training_popup);

		initSetting();
		initEventListener();

		if("KR".equals(Language)){
			PopUpTraining_Con_Tv.setText(R.string.training_con_kr);
		} else if("ENG".equals(Language)) {
			PopUpTraining_Con_Tv.setText(R.string.training_con_eng);
		}

	}

	public TrainingPopUpDialog(Activity act, MemberInfo Mi, int position) {
		super(act , android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		this.con = act;
		this.act = act;
		this.pref = con.getSharedPreferences("pref", Context.MODE_PRIVATE);
		this.Language = pref.getString("LOGIN_LANGUAGE", "");
		this.Si = new SelectItems();
		this.Mi = Mi;
		this.Position = position;
	}

	private void initSetting() {
		PopUpTraining_Title_Tv = (TextView)findViewById(R.id.training_title_tv);
		PopUpTraining_Con_Tv = (TextView)findViewById(R.id.training_con_tv);

		PopUpOk_Bt = (Button)findViewById(R.id.training_ok_bt);
		PopUpNo_Bt = (Button)findViewById(R.id.training_no_bt);
	}
	
	private void initEventListener() {
		PopUpOk_Bt.setOnClickListener(this);
		PopUpNo_Bt.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.training_ok_bt:
				dismiss();
				Bundle m = new Bundle();
				m.putParcelable("MemberInfo",Mi);
				Intent intent = new Intent(act, CrewEvaluationAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent.putExtra("MemberInfo", m);
				intent.putExtra("Position", Position);
				act.startActivity(intent);//  go Intent
				act.finish();
				act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
				break;
			case R.id.training_no_bt:
				dismiss();
				break;
		}
		 if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
			new CommonUtil().setHideNavigationBar(act.getWindow().getDecorView());
		}
	}
}
