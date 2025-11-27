package com.togetherseatech.whaleshark;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
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
import com.togetherseatech.whaleshark.util.TextFontUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * AlertDialog 엑티비티
 *
 */
public class AdminMemberPopUpDialog extends Dialog implements View.OnClickListener , View.OnFocusChangeListener {

	private EditText PopUpFirstName_Tv, PopUpSurName_Tv;//, PopUpVslName_Tv;
	private Button PopUpCanCel_Bt, PopUpOk_Bt, PopUpBirth_Bt, PopUpSignOn_Bt, PopUpSignOff_Bt;
	private Spinner PopUpRank_Sp, PopUpNational_Sp;//,  PopUpVslType_Sp;
	private ImageView PopUpBg_Iv;
	private MemberInfo Mi;
	private Activity Act;
	private Intent intent;
	private Context pContext, sContext;

	private ArrayList<TrainingInfo> TiList;
	private ArrayList<SelectItems> areaList;
	private SpinnerArrayAdapter Aa_Rank;
	private SpinnerArrayAdapter Aa_National;
	private ArrayAdapter Aa_VslType;

	private SharedPreferences pref;
	private String Language;
	private SelectItems Si;
	private TextFontUtil tf;
	private TrainingDao Tdao;
	private Calendar c;
	private int year, month, day;
	private Pattern ps;
	private Matcher ms, ms2;
	private DatePickerDialog startDatePicker;
	private int LOGIN_CARRIER;
	private String VSL;
	private int Idx, bg, cancel, signup;
	private int MSTER_IDX, VSL_TYPE;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
		lpWindow.screenBrightness = (float) 125/255;
		lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND|WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		getWindow().setAttributes(lpWindow);
		setContentView(R.layout.admin_member_popup);

		Si = new SelectItems();
		tf = new TextFontUtil();

		initSetting();
		initEventListener();

		 if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
			avoidSpinnerDropdownFocus(PopUpRank_Sp);
			avoidSpinnerDropdownFocus(PopUpNational_Sp);
			KeyboardUtils.addKeyboardToggleListener(Act, new KeyboardUtils.SoftKeyboardToggleListener() {
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

		c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DATE);
		ps = Pattern.compile("^[A-Z]*$");

		areaList = new ArrayList<SelectItems>();
		areaList = Si.getSelectMemberRank();
		Aa_Rank = new SpinnerArrayAdapter(Act, R.layout.spinner_item, areaList);
		Aa_Rank.setDropDownViewResource(R.layout.spinner_dropdown_item);
		PopUpRank_Sp.setAdapter(Aa_Rank);

		areaList = Si.getSelectMemberNational();
		Aa_National = new SpinnerArrayAdapter(Act, R.layout.spinner_item, areaList);
		Aa_National.setDropDownViewResource(R.layout.spinner_dropdown_item);
		PopUpNational_Sp.setAdapter(Aa_National);

//		ArrayList<String> areaList2 = new ArrayList<String>();
//		areaList2 = Si.getSelectMemberVslType2();
//		areaList = Si.getSelectMemberVslType();
//		Aa_VslType = new SpinnerArrayAdapter(sContext, R.layout.spinner_item, areaList );
//		Aa_VslType.setDropDownViewResource(R.layout.spinner_dropdown_item);
//		PopUpVslType_Sp.setAdapter(Aa_VslType);
//		Aa_VslType = new ArrayAdapter<String>(sContext, R.layout.spinner_item, areaList2 );
//		Aa_VslType.setDropDownViewResource(R.layout.spinner_dropdown_item);
//		PopUpVslType_Sp.setAdapter(Aa_VslType);

		if(Mi != null){
//			Log.e("AdminMemberPopUpDialog"," IDX : "+Mi.getIdx());
			PopUpSurName_Tv.setText(Mi.getSurName());
			PopUpSurName_Tv.setFocusableInTouchMode(false);
			PopUpFirstName_Tv.setText(Mi.getFirstName());
			PopUpFirstName_Tv.setFocusableInTouchMode(false);
			PopUpNational_Sp.setSelection(Mi.getNational());
			PopUpRank_Sp.setSelection(Mi.getRank());
			PopUpRank_Sp.setEnabled(false);
			PopUpRank_Sp.setBackgroundColor(Color.parseColor("#00000000"));
			PopUpBirth_Bt.setText(Mi.getBirth());
//			PopUpVslName_Tv.setText(Mi.getVsl());
//			tf.setNanumSquareL(sContext, PopUpVslName_Tv);
//			PopUpVslType_Sp.setSelection(Mi.getVsl_type());
			PopUpSignOn_Bt.setText(Mi.getSign_on());
			PopUpSignOff_Bt.setText(Mi.getSign_off());
		}else{
			PopUpRank_Sp.setBackgroundResource(R.mipmap.admin_popup_sp_bg);
			PopUpSurName_Tv.setFocusableInTouchMode(true);
			PopUpFirstName_Tv.setFocusableInTouchMode(true);
//			Log.e("AdminMemberPopUpDialog"," IDX : "+Mi);
//			PopUpVslType_Sp.setSelection(LOGIN_CARRIER);
//			PopUpVslName_Tv.setText(VSL);
//			tf.setNanumSquareL(sContext, PopUpVslName_Tv);
		}

		if("KR".equals(Language)){
			bg = R.mipmap.admin_member_popup_kr_bg;
			cancel = R.drawable.popup_mcancel_kr_bt;
			signup = R.drawable.popup_signup_kr_bt;
		} else if("ENG".equals(Language)) {
			bg = R.mipmap.admin_member_popup_eng_bg;
			cancel = R.drawable.popup_mcancel_eng_bt;
			signup = R.drawable.popup_signup_eng_bt;
		}

		PopUpBg_Iv.setBackgroundResource(bg);
		PopUpCanCel_Bt.setBackgroundResource(cancel);
		PopUpOk_Bt.setBackgroundResource(signup);
	}

	public AdminMemberPopUpDialog(Activity act, MemberInfo mi) {
		super(act, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		this.Mi = mi;
		this.Act = act;
		this.sContext = act;
		this.pContext = new ContextThemeWrapper(act, android.R.style.Theme_Holo_Light_Dialog);
//		Log.e("AdminMemberPopUpDialog", Build.VERSION.SDK_INT+" / "+ Build.VERSION_CODES.M);
		/*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			// API 24 이상일 경우 시스템 기본 테마 사용
			this.pContext = context;
		}*/

		this.pref = act.getSharedPreferences("pref", Context.MODE_PRIVATE);
		this.Language = pref.getString("LOGIN_LANGUAGE", "");
		this.LOGIN_CARRIER = pref.getInt("LOGIN_CARRIER", 0);
		this.MSTER_IDX = pref.getInt("MSTER_IDX", 0);
		this.VSL_TYPE = pref.getInt("VSL_TYPE", 0);

		this.VSL = pref.getString("VSL", "");
		this.Tdao = new TrainingDao(act);
	}

	public static void avoidSpinnerDropdownFocus(Spinner spinner) {
		try {
			Field listPopupField = Spinner.class.getDeclaredField("mPopup");
			listPopupField.setAccessible(true);
			Object listPopup = listPopupField.get(spinner);
			if (listPopup instanceof ListPopupWindow) {
				Field popupField = ListPopupWindow.class.getDeclaredField("mPopup");
				popupField.setAccessible(true);
				Object popup = popupField.get((ListPopupWindow) listPopup);
				if (popup instanceof PopupWindow) {
					((PopupWindow) popup).setFocusable(false);
					new CommonUtil().setHideNavigationBar(((PopupWindow) popup).getContentView());
				}
			}
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	private void initSetting() {
		PopUpBg_Iv = (ImageView) findViewById(R.id.admin_member_popup_bg_iv);
		PopUpSurName_Tv = (EditText)findViewById(R.id.admin_member_popup_name_tv);
		tf.setNanumSquareL(sContext, PopUpSurName_Tv);
		PopUpSurName_Tv.setFilters(new InputFilter[]{new InputFilter.AllCaps()}); // 소문자로 입력된 값을 대문자로 바꿔줌.
		PopUpFirstName_Tv = (EditText)findViewById(R.id.admin_member_popup_name2_tv);
		tf.setNanumSquareL(sContext, PopUpFirstName_Tv);
		PopUpFirstName_Tv.setFilters(new InputFilter[]{new InputFilter.AllCaps()}); // 소문자로 입력된 값을 대문자로 바꿔줌.
		PopUpNational_Sp = (Spinner)findViewById(R.id.admin_member_popup_national_sp);
		PopUpRank_Sp = (Spinner)findViewById(R.id.admin_member_popup_rank_sp);
		PopUpBirth_Bt = (Button)findViewById(R.id.admin_member_popup_birth_bt);
		tf.setNanumSquareL(sContext, PopUpBirth_Bt);
//		PopUpVslName_Tv = (EditText)findViewById(R.id.admin_member_popup_vslname_tv);
//		PopUpVslName_Tv.setEnabled(false);
//		PopUpVslType_Sp  = (Spinner)findViewById(R.id.admin_member_popup_vsltype_sp);
//		PopUpVslType_Sp.setEnabled(false);
		PopUpSignOn_Bt = (Button)findViewById(R.id.admin_member_popup_signon_bt);
		tf.setNanumSquareL(sContext, PopUpSignOn_Bt);
		PopUpSignOff_Bt = (Button)findViewById(R.id.admin_member_popup_signoff_bt);
		tf.setNanumSquareL(sContext, PopUpSignOff_Bt);
		PopUpCanCel_Bt = (Button)findViewById(R.id.admin_member_popup_cancel_bt);
		PopUpOk_Bt = (Button)findViewById(R.id.admin_member_popup_ok_bt);

	}
	
	private void initEventListener() {
		PopUpBirth_Bt.setOnClickListener(this);
		PopUpSignOn_Bt.setOnClickListener(this);
		PopUpSignOff_Bt.setOnClickListener(this);
		PopUpCanCel_Bt.setOnClickListener(this);
		PopUpOk_Bt.setOnClickListener(this);
//		PopUpSurName_Tv.setOnFocusChangeListener(this);
//		PopUpFirstName_Tv.setOnFocusChangeListener(this);
//		PopUpVslName_Tv.setOnFocusChangeListener(this);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
//		Log.e("onFocusChange","hasFocus : "+hasFocus);
		/*TextView tv = new TextView(v.getContext());
		switch (v.getId()) {
			case R.id.admin_member_popup_name_tv:
				tv = PopUpFirstName_Tv;
				break;

			case R.id.admin_member_popup_name2_tv:
				tv = PopUpSurName_Tv;
				break;
		}


		ms = ps.matcher(tv.getText().toString());
		boolean bs = ms.matches();
		switch (v.getId()) {
			case R.id.admin_member_popup_name_tv:
				if(!hasFocus) {
					if (bs == false) {
						Si.getMassage("ENG", pref.getString("LOGIN_LANGUAGE",""), this.getContext(), Toast.LENGTH_SHORT);
					}
				}
				break;
			case R.id.admin_member_popup_vslname_tv:
//                Toast.makeText(this, "login_pw_et", Toast.LENGTH_LONG).show();
				if (bs == false)
					tv.requestFocus();
				else
					tf.hideKeypad(this.getContext() , PopUpVslName_Tv);
				break;
		}*/
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.admin_member_popup_cancel_bt:
				dismiss();
				break;
			case R.id.admin_member_popup_ok_bt:
				Pattern ps = Pattern.compile("^[A-Z ]+$");
				ms = ps.matcher(PopUpFirstName_Tv.getText().toString());
				ms2 = ps.matcher(PopUpSurName_Tv.getText().toString());
				boolean bs = ms.matches();
				boolean bs2 = ms2.matches();
				if(PopUpFirstName_Tv.getText().toString().trim().length() < 1 && PopUpSurName_Tv.getText().toString().trim().length() < 1){
					Si.getMassage("NAME", Language, pContext, Toast.LENGTH_SHORT);
				}/*else if(PopUpSurName_Tv.getText().toString().trim().length() < 1){
					Si.getMassage("NAME", Language, pContext, Toast.LENGTH_SHORT);
				}*/else if(bs == false && bs2 == false){
					Si.getMassage("ENG", Language, pContext, Toast.LENGTH_SHORT);
				} else if(PopUpRank_Sp.getSelectedItemPosition() == 0){
					Si.getMassage("RANK", Language, pContext, Toast.LENGTH_SHORT);
				}else if(PopUpNational_Sp.getSelectedItemPosition() == 0){
					Si.getMassage("NATIONAL", Language, pContext, Toast.LENGTH_SHORT);
				}else if(PopUpBirth_Bt.getText().toString().trim().length() < 1){
					Si.getMassage("BIRTH", Language, pContext, Toast.LENGTH_SHORT);
//				}else if(PopUpVslName_Tv.getText().toString().trim().length() < 1){
//					Si.getMassage("VSL_NAME", Language, pContext, Toast.LENGTH_SHORT);
//				}else if(PopUpVslType_Sp.getSelectedItemPosition() == 0){
//					Si.getMassage("VSL_TYPE", Language, pContext, Toast.LENGTH_SHORT);
				}else if(PopUpSignOn_Bt.getText().toString().trim().length() < 1){
					Si.getMassage("SIGN_ON", Language, pContext, Toast.LENGTH_SHORT);
				}else {
					MemberDao mDao = new MemberDao(pContext);
//					Log.e("AdminMemberPopUpDialog"," PopUpSignOff : "+PopUpSignOff_Bt.getText()+"");
					if(Mi != null){
						/*Log.e("updateMember", "onClick : " + Mi.getIdx() + "/" + PopUpRank_Sp.getSelectedItemPosition()
								+ "/" + PopUpName_Tv.getText().toString().trim() + "/" + PopUpVslName_Tv.getText().toString().trim()
								+ "/" + PopUpVslType_Sp.getSelectedItemPosition() + "/" + PopUpBirth_Bt.getText().toString().trim()
								+ "/" + PopUpNational_Sp.getSelectedItemPosition() + "/" + PopUpSignOn_Bt.getText().toString().trim());*/
						mDao.updateMember(new MemberInfo(Mi.getIdx(), PopUpRank_Sp.getSelectedItemPosition(), PopUpFirstName_Tv.getText().toString().trim(), PopUpSurName_Tv.getText().toString().trim()
								, Mi.getVsl(), Mi.getVsl_type(), PopUpBirth_Bt.getText().toString().trim()
								, PopUpNational_Sp.getSelectedItemPosition(), PopUpSignOn_Bt.getText().toString().trim(), PopUpSignOff_Bt.getText().toString().trim()));
					} else {
						Idx = mDao.getMemberIdx();
						Log.e("test","MEMBER IDX === " + Idx);
						Log.e("test","MEMBER 값? === " + mDao.getMembers(Vars.DEL_N +  LOGIN_CARRIER + Vars.SIGNOFF_N));
//						Log.e("addMember", "onClick Idx : " + Idx);
						/*Log.e("addMember", "onClick : " + PopUpRank_Sp.getSelectedItemPosition()
								+ "/" + PopUpName_Tv.getText().toString().trim() + "/" + PopUpVslName_Tv.getText().toString().trim()
								+ "/" + PopUpVslType_Sp.getSelectedItemPosition() + "/" + PopUpBirth_Bt.getText().toString().trim()
								+ "/" + PopUpNational_Sp.getSelectedItemPosition() + "/" + PopUpSignOn_Bt.getText().toString().trim());*/
						mDao.addMember(new MemberInfo(Idx, MSTER_IDX, PopUpRank_Sp.getSelectedItemPosition(), PopUpFirstName_Tv.getText().toString().trim(), PopUpSurName_Tv.getText().toString().trim()
								, VSL, LOGIN_CARRIER, PopUpBirth_Bt.getText().toString().trim()
								, PopUpNational_Sp.getSelectedItemPosition(), PopUpSignOn_Bt.getText().toString().trim(), PopUpSignOff_Bt.getText().toString().trim(), "N" ));

						/*areaList = Si.getRankChapter(PopUpRank_Sp.getSelectedItemPosition(),0);
						TiList = new ArrayList<>();
//						Log.e("AdminMemberPopUpDialog" ,"MbList Size : "+ areaList.size());
						for(int i = 0; i < areaList.size(); i++){
							TrainingInfo Ti = new TrainingInfo();
							Ti.setMember_idx(Idx);
							Ti.setTraining_course(areaList.get(i).getIdx());
							Ti.setYear(Si.getYear());
							Ti.setQuarter(Si.getQuarter());
							Ti.setSubmit("N");
							TiList.add(Ti);
						}

						Tdao.addHistorys(TiList);*/

					}

					AdminAct.MbList = new ArrayList<>();
					AdminAct.MbList = mDao.getMembers(Vars.DEL_N + LOGIN_CARRIER);
					AdminAct.mAdapter.clear();
					AdminAct.mAdapter.addAll(AdminAct.MbList);
					AdminAct.mAdapter.notifyDataSetChanged();
					Log.e("test","AdminAct ==== " + AdminAct.MbList.size());

					dismiss();
				}

				break;
			case R.id.admin_member_popup_birth_bt:
				startDatePicker = new DatePickerDialog(pContext, BirthDateSetListener, year, month, day );
//				startDatePicker.setInverseBackgroundForced(true);
				startDatePicker.setOnCancelListener(new OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						PopUpBirth_Bt.setText(null);
					}
				});
				startDatePicker.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
				startDatePicker.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
						WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
				 if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
					new CommonUtil().setHideNavigationBar(startDatePicker.getWindow().getDecorView());
				}
				startDatePicker.show();
				startDatePicker.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

				break;
			case R.id.admin_member_popup_signon_bt:

				startDatePicker = new DatePickerDialog(pContext, signOnDateSetListener, year, month, day );
//				startDatePicker.setInverseBackgroundForced(true);
				startDatePicker.setOnCancelListener(new OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						PopUpSignOn_Bt.setText(null);
					}
				});
				startDatePicker.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
				startDatePicker.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
						WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
				 if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
					new CommonUtil().setHideNavigationBar(startDatePicker.getWindow().getDecorView());
				}
				startDatePicker.show();
				startDatePicker.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

				break;
			case R.id.admin_member_popup_signoff_bt:

				startDatePicker = new DatePickerDialog(pContext, signOffDateSetListener, year, month, day );
//				startDatePicker.setInverseBackgroundForced(true);
				startDatePicker.setOnCancelListener(new OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						PopUpSignOff_Bt.setText(null);
					}
				});
				startDatePicker.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
				startDatePicker.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
						WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
				 if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
					new CommonUtil().setHideNavigationBar(startDatePicker.getWindow().getDecorView());
				}
				startDatePicker.show();
				startDatePicker.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

				break;
		}

		 if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
			new CommonUtil().setHideNavigationBar(Act.getWindow().getDecorView());
		}

	}

	DatePickerDialog.OnDateSetListener BirthDateSetListener =
			new DatePickerDialog.OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					String Date = String.valueOf(year) + "." + ((monthOfYear + 1) <= 9 ? "0" + String.valueOf(monthOfYear + 1) : String.valueOf(monthOfYear + 1)) + "." + (dayOfMonth <= 9 ? "0" + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth));
//					Log.e("onDateSet", "view.getId() : " + view.getId() + "/" + R.id.admin_member_popup_birth_bt);
					PopUpBirth_Bt.setText(Date);
				}
			};

	DatePickerDialog.OnDateSetListener signOnDateSetListener =
			new DatePickerDialog.OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					String Date = String.valueOf(year) + "." + ((monthOfYear + 1) <= 9 ? "0" + String.valueOf(monthOfYear + 1) : String.valueOf(monthOfYear + 1)) + "." + (dayOfMonth <= 9 ? "0" + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth));
//					Log.e("onDateSet", "view.getId() : " + view.getId() + "/" + R.id.admin_member_popup_birth_bt);
					PopUpSignOn_Bt.setText(Date);
				}
			};

	DatePickerDialog.OnDateSetListener signOffDateSetListener =
			new DatePickerDialog.OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					String Date = String.valueOf(year) + "." + ((monthOfYear + 1) <= 9 ? "0" + String.valueOf(monthOfYear + 1) : String.valueOf(monthOfYear + 1)) + "." + (dayOfMonth <= 9 ? "0" + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth));
//					Log.e("onDateSet", "view.getId() : " + view.getId() + "/" + R.id.admin_member_popup_birth_bt);
					PopUpSignOff_Bt.setText(Date);
				}
			};


	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		Log.e("AdminMemberPopUpDialog", "onWindowFocusChanged : " + hasFocus);
		 if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
			new CommonUtil().setHideNavigationBar(getWindow().getDecorView());
		}
	}

}
