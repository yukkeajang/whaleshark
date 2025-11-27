package com.togetherseatech.whaleshark;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.togetherseatech.whaleshark.Db.History.TrainingDao;
import com.togetherseatech.whaleshark.Db.History.TrainingInfo;
import com.togetherseatech.whaleshark.Db.Member.MemberDao;
import com.togetherseatech.whaleshark.Db.Member.MemberInfo;
import com.togetherseatech.whaleshark.Db.Problem.ProblemInfo;
import com.togetherseatech.whaleshark.Db.Training.TrainingsDao;
import com.togetherseatech.whaleshark.Db.Training.TrainingsInfo;
import com.togetherseatech.whaleshark.util.CommonUtil;
import com.togetherseatech.whaleshark.util.SaveCSV;
import com.togetherseatech.whaleshark.util.SelectItems;
import com.togetherseatech.whaleshark.util.TextFontUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * AlertDialog 엑티비티
 */
public class TwoButtonPopUpDialog extends Dialog implements View.OnClickListener {

    private Button PopUpCanCel_Bt, PopUpOk_Bt;
    private TextView PopUpTitle_tv, PopUpCon_tv;
    private Context pContext;
    private String Type, Menu;
    private MemberInfo Mi;
    private TrainingInfo Ti;
    private Object obj;
    private int position = 0;
    private int view;
    private Activity Act;
    private Intent intent;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String LOGIN_LANGUAGE;
    private ArrayList<UpgradeInfo> ArrayFname;
    private int LOGIN_CARRIER;
    private TextFontUtil tf;
    private SelectItems Si;
    private Map item2;
    private long mLastClickTime = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.screenBrightness = (float) 125 / 255;
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        getWindow().setAttributes(lpWindow);

        if ("LOGOUT".equals(Type))
            view = Vars.LOGOUT;
        else if ("DEL".equals(Type))
            view = Vars.DEL;
        else if ("UPDATE".equals(Type) || "DOWNLOAD".equals(Type) || "UPGRADE".equals(Type) || "ALLDOWNLOAD".equals(Type))
            view = Vars.UPDATE;
        else if ("START".equals(Type))
            view = Vars.START;
        else if ("NEXT".equals(Type))
            view = Vars.NEXT;
        else if ("END".equals(Type) || "EXIT".equals(Type))
            view = Vars.END;
        else if ("BACKDB".equals(Type) || "GODB".equals(Type))
            view = Vars.BACKDB;

        setContentView(view);

        pref = pContext.getSharedPreferences("pref", Context.MODE_PRIVATE);
        LOGIN_LANGUAGE = pref.getString("LOGIN_LANGUAGE", "");
        LOGIN_CARRIER = pref.getInt("LOGIN_CARRIER", 0);
        tf = new TextFontUtil();
        Si = new SelectItems();

        initSetting();
        initEventListener();

        if ("LOGOUT".equals(Type)) {
            if ("KR".equals(LOGIN_LANGUAGE)) {
                PopUpTitle_tv.setText(R.string.logout_title_kr);
                PopUpTitle_tv.setTextScaleX(1f);
                PopUpCon_tv.setText(R.string.logout_con_kr);
                PopUpCanCel_Bt.setBackgroundResource(R.drawable.popup_lcancel_kr_bt);
                PopUpOk_Bt.setBackgroundResource(R.drawable.popup_logout_kr_bt);
            } else {
                PopUpTitle_tv.setText(R.string.logout_title_eng);
                PopUpTitle_tv.setTextScaleX(0.9f);
                PopUpCon_tv.setText(R.string.logout_con_eng);
                PopUpCanCel_Bt.setBackgroundResource(R.drawable.popup_lcancel_eng_bt);
                PopUpOk_Bt.setBackgroundResource(R.drawable.popup_logout_eng_bt);
            }
        } else if ("DEL".equals(Type)) {
            if ("KR".equals(LOGIN_LANGUAGE)) {
                PopUpTitle_tv.setText(R.string.delete_title_kr);
                PopUpTitle_tv.setTextScaleX(1f);
                PopUpCon_tv.setText(R.string.delete_con_kr);
                PopUpCanCel_Bt.setBackgroundResource(R.drawable.popup_cancel_kr_bt);
                PopUpOk_Bt.setBackgroundResource(R.drawable.popup_delete_kr_bt);
            } else {
                PopUpTitle_tv.setText(R.string.delete_title_eng);
                PopUpTitle_tv.setTextScaleX(0.7f);
                PopUpCon_tv.setText(R.string.delete_con_eng);
                PopUpCanCel_Bt.setBackgroundResource(R.drawable.popup_cancel_eng_bt);
                PopUpOk_Bt.setBackgroundResource(R.drawable.popup_delete_eng_bt);
            }
        } else if ("DOWNLOAD".equals(Type) || "ALLDOWNLOAD".equals(Type)) {
            if ("KR".equals(LOGIN_LANGUAGE)) {
                PopUpTitle_tv.setText(R.string.download_title_kr);
                PopUpTitle_tv.setTextScaleX(1f);
                PopUpCon_tv.setText(R.string.download_con_kr);
                PopUpCanCel_Bt.setBackgroundResource(R.drawable.popup_ucancel_kr_bt);
                PopUpOk_Bt.setBackgroundResource(R.drawable.popup_usend_kr_bt);
            } else {
                PopUpTitle_tv.setText(R.string.download_title_eng);
                PopUpTitle_tv.setTextScaleX(1f);
                PopUpCon_tv.setText(R.string.download_con_eng);
                PopUpCanCel_Bt.setBackgroundResource(R.drawable.popup_ucancel_eng_bt);
                PopUpOk_Bt.setBackgroundResource(R.drawable.popup_usend_eng_bt);
            }
        } else if ("UPDATE".equals(Type)) {
            if ("KR".equals(LOGIN_LANGUAGE)) {
                PopUpTitle_tv.setText(R.string.update_title_kr);
                PopUpTitle_tv.setTextScaleX(1f);
                PopUpCon_tv.setText(R.string.update_con_kr);
                PopUpCanCel_Bt.setBackgroundResource(R.drawable.popup_ucancel_kr_bt);
                PopUpOk_Bt.setBackgroundResource(R.drawable.popup_usend_kr_bt);
            } else {
                PopUpTitle_tv.setText(R.string.update_title_eng);
                PopUpTitle_tv.setTextScaleX(0.8f);
                PopUpCon_tv.setText(R.string.update_con_eng);
                PopUpCanCel_Bt.setBackgroundResource(R.drawable.popup_ucancel_eng_bt);
                PopUpOk_Bt.setBackgroundResource(R.drawable.popup_usend_eng_bt);
            }
        } else if ("UPGRADE".equals(Type)) {
            if ("KR".equals(LOGIN_LANGUAGE)) {
                PopUpTitle_tv.setText(R.string.upgrade_title_kr);
                PopUpTitle_tv.setTextScaleX(1f);
                PopUpCon_tv.setText(R.string.upgrade_con_kr);
                PopUpCanCel_Bt.setBackgroundResource(R.drawable.popup_ucancel_kr_bt);
                PopUpOk_Bt.setBackgroundResource(R.drawable.popup_usend_kr_bt);
            } else {
                PopUpTitle_tv.setText(R.string.upgrade_title_eng);
                PopUpTitle_tv.setTextScaleX(0.7f);
                PopUpCon_tv.setText(R.string.upgrade_con_eng);
                PopUpCanCel_Bt.setBackgroundResource(R.drawable.popup_ucancel_eng_bt);
                PopUpOk_Bt.setBackgroundResource(R.drawable.popup_usend_eng_bt);
            }
        } else if ("START".equals(Type)) {
//			PopUpCanCel_Bt.setVisibility(View.INVISIBLE);
            if ("KR".equals(LOGIN_LANGUAGE)) {
                PopUpCon_tv.setText(R.string.guidance_kr);
                PopUpCon_tv.setLineSpacing(1.0f, 0.8f);
                PopUpOk_Bt.setBackgroundResource(R.drawable.agreement_kr_bt);
                PopUpCanCel_Bt.setBackgroundResource(R.drawable.popup_ucancel_kr_bt);
            } else {
                PopUpCon_tv.setText(R.string.guidance_eng);
                PopUpCon_tv.setLineSpacing(1.0f, 0.73f);
                PopUpOk_Bt.setBackgroundResource(R.drawable.agreement_eng_bt);
                PopUpCanCel_Bt.setBackgroundResource(R.drawable.popup_ucancel_eng_bt);
            }
        } else if ("NEXT".equals(Type)) {
            if ("KR".equals(LOGIN_LANGUAGE)) {
                PopUpTitle_tv.setText(R.string.next_title_kr);
                PopUpTitle_tv.setTextScaleX(1f);
                PopUpCon_tv.setText(R.string.next_con_kr);
            } else {
                PopUpTitle_tv.setText(R.string.next_title_eng);
                PopUpTitle_tv.setTextScaleX(0.6f);
                PopUpCon_tv.setText(R.string.next_con_eng);
            }
        } else if ("END".equals(Type)) {
            if ("KR".equals(LOGIN_LANGUAGE)) {
                PopUpTitle_tv.setText(R.string.end_title_kr);
                PopUpTitle_tv.setTextScaleX(1f);
                PopUpCon_tv.setText(R.string.end_con_kr);
                PopUpCanCel_Bt.setBackgroundResource(R.drawable.popup_go_kr_bt);
                PopUpOk_Bt.setBackgroundResource(R.drawable.popup_end_kr_bt);
            } else {
                PopUpTitle_tv.setText(R.string.end_title_eng);
                PopUpTitle_tv.setTextScaleX(0.8f);
                PopUpCon_tv.setText(R.string.end_con_eng);
                PopUpCanCel_Bt.setBackgroundResource(R.drawable.popup_go_eng_bt);
                PopUpOk_Bt.setBackgroundResource(R.drawable.popup_end_eng_bt);
            }
        } else if ("EXIT".equals(Type)) {
            if ("KR".equals(LOGIN_LANGUAGE)) {
                PopUpTitle_tv.setText(R.string.exit_title_kr);
                PopUpTitle_tv.setTextScaleX(1f);
                PopUpCon_tv.setText(R.string.exit_con_kr);
                PopUpCanCel_Bt.setBackgroundResource(R.drawable.popup_go_kr_bt);
                PopUpOk_Bt.setBackgroundResource(R.drawable.popup_end_kr_bt);
            } else {
                PopUpTitle_tv.setText(R.string.exit_title_eng);
                PopUpTitle_tv.setTextScaleX(1f);
                PopUpCon_tv.setText(R.string.exit_con_eng);
                PopUpCanCel_Bt.setBackgroundResource(R.drawable.popup_go_eng_bt);
                PopUpOk_Bt.setBackgroundResource(R.drawable.popup_end_eng_bt);
            }
        } else if ("BACKDB".equals((Type))) {
            if ("KR".equals(LOGIN_LANGUAGE)) {
                PopUpTitle_tv.setText(R.string.back_db_title_kr);
                PopUpTitle_tv.setTextScaleX(1f);
            } else {
                PopUpTitle_tv.setText(R.string.back_db_title_eng);
                PopUpTitle_tv.setTextScaleX(0.8f);
            }
            PopUpCanCel_Bt.setBackgroundResource(R.drawable.popup_training_no_bt);
            PopUpOk_Bt.setBackgroundResource(R.drawable.popup_training_yes_bt);
        } else if ("GODB".equals(Type)) {
            if ("KR".equals(LOGIN_LANGUAGE)) {
                PopUpTitle_tv.setText(R.string.go_db_title_kr);
                PopUpTitle_tv.setTextScaleX(1f);

            } else {
                PopUpTitle_tv.setText(R.string.go_db_title_eng);
                PopUpTitle_tv.setTextScaleX(1f);
            }
            PopUpCanCel_Bt.setBackgroundResource(R.drawable.popup_training_no_bt);
            PopUpOk_Bt.setBackgroundResource(R.drawable.popup_training_yes_bt);
        }
    }

    public TwoButtonPopUpDialog(Activity act, String type) {
        super(act, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        this.pContext = act;
        this.Act = act;
        this.Type = type;
    }

    public TwoButtonPopUpDialog(Activity act, Object obj, String type) {
        super(act, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        this.pContext = act;
        this.Act = act;
        this.obj = obj;
        this.Type = type;
    }

    public TwoButtonPopUpDialog(Context context, Activity act, String type, String menu, Object obj) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        this.pContext = context;
        this.Act = act;
        this.Type = type;
        this.Menu = menu;
        this.obj = obj;
    }

    public TwoButtonPopUpDialog(Context context, Activity act, String type, String menu) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        this.pContext = context;
        this.Act = act;
        this.Type = type;
        this.Menu = menu;
    }

    public TwoButtonPopUpDialog(Activity act, ArrayList<UpgradeInfo> ArrayFname, String type) {
        super(act, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        this.pContext = act;
        this.Act = act;
        this.ArrayFname = ArrayFname;
        this.Type = type;
//		Log.e("UPGRADE","FILE size : " + ArrayFname.size());
    }

    public TwoButtonPopUpDialog(Context context, Object obj, String type, Activity act) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        this.pContext = context;
        this.obj = obj;
        this.Type = type;
        this.Act = act;
    }

    public TwoButtonPopUpDialog(Context context, MemberInfo mi, TrainingInfo ti, String type, Activity act) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        this.pContext = context;
        this.Mi = mi;
        this.Ti = ti;
        this.Type = type;
        this.Act = act;
    }
	/*public TwoButtonPopUpDialog(Context context, Activity act, String type) {
		super(context , android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		this.pContext = context;
		this.Act = act;
		this.Type = type;
	}*/


    private void initSetting() {
        if (!"START".equals(Type)) {
            PopUpTitle_tv = (TextView) findViewById(R.id.title_tv);
            tf.setNanumSquareB(pContext, PopUpTitle_tv);
        }

        if (!"BACKDB".equals(Type) && !"GODB".equals(Type)) {
            PopUpCon_tv = (TextView) findViewById(R.id.con_tv);
            tf.setNanumSquareL(pContext, PopUpCon_tv);
        }

        PopUpCanCel_Bt = (Button) findViewById(R.id.admin_member_popup_cancel_bt);
        PopUpCanCel_Bt.setVisibility(View.VISIBLE);
        PopUpOk_Bt = (Button) findViewById(R.id.admin_member_popup_ok_bt);
    }

    private void initEventListener() {
        PopUpCanCel_Bt.setOnClickListener(this);
        PopUpOk_Bt.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.admin_member_popup_cancel_bt:
                dismiss();
                break;
            case R.id.admin_member_popup_ok_bt:
                // mis-clicking prevention, using threshold of 1000 ms
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

//				Log.e("TwoButtonPopUpDialog", Type);
                if ("LOGOUT".equals(Type)) {
                    intent = new Intent(Act, LogInAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    Act.startActivity(intent);//  go Intent
                    Act.finish();
                    Act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                } else if ("DEL".equals(Type)) {
                    MemberInfo Mi = (MemberInfo) obj;
                    MemberDao mDao = new MemberDao(pContext);
                    mDao.deleteMember(Mi.getIdx());
                    AdminAct.MbList = new ArrayList<MemberInfo>();
                    AdminAct.MbList = mDao.getMembers(Vars.DEL_N + LOGIN_CARRIER);

					/*if(AdminAct.MbList.size() == 0){
						AdminAct.MbList.add(null);
					}*/

                    AdminAct.mAdapter.clear();
                    AdminAct.mAdapter.addAll(AdminAct.MbList);
                    AdminAct.mAdapter.notifyDataSetChanged();
                } else if ("START".equals(Type)) {
                    /**수정 추가**/
                    Bundle start_m = new Bundle();
                    start_m.putParcelable("MemberInfo", Mi);
                    /**수정추가**/
                    intent = new Intent(Act, TestRegulationGuidanceAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("MemberInfo", start_m);
                    intent.putExtra("TrainingInfo", Ti);
                    Act.startActivity(intent);//  go Intent
                    Act.finish();
                    Act.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                } else if ("NEXT".equals(Type)) {
//					Log.e("TwoButtonPopUpDialog", "Answer : " + TestRegulationGuidanceAct.Answer);
//					Log.e("TwoButtonPopUpDialog", "NEXT Answer : " + TestRegulationGuidanceAct.PiList.get(TestRegulationGuidanceAct.problemCount).getAnswer());

                    if (TestRegulationGuidanceAct.Answer.equals(TestRegulationGuidanceAct.PiList.get(TestRegulationGuidanceAct.problemCount).getAnswer()))
                        TestRegulationGuidanceAct.AnswerScoreList.add("Y");
                    else
                        TestRegulationGuidanceAct.AnswerScoreList.add("N");

                    TestRegulationGuidanceAct.AnswerTimeList.add(90 - TestRegulationGuidanceAct.count);

                    TestRegulationGuidanceAct.problemCount += 1;
//					Log.e("TwoButtonPopUpDialog","problemCount : "+TestRegulationGuidanceAct.problemCount);
                    if (!TestRegulationGuidanceAct.play)
                        TestRegulationGuidanceAct.mediaPlayer.stop();
                    TestRegulationGuidanceAct.setPageData();

                } else if ("END".equals(Type)) {
                    TestRegulationGuidanceAct.T.cancel();
                    TestRegulationGuidanceAct.AnswerScoreList.clear();
                    TestRegulationGuidanceAct.AnswerTimeList.clear();
                    new TestRegulationGuidanceAct.setDataPro().execute();
                } else if ("EXIT".equals(Type)) {
                    IntroAct.APP = false;
                    Act.finish();
                } else if ("UPDATE".equals(Type)) {
                    TrainingDao Tdao = new TrainingDao(getContext());
                    ArrayList<Map<String, Object>> updateArray = new ArrayList<>();
                    ArrayList<Map<String, Object>> updateArray2 = new ArrayList<>();
                    ArrayList<Map<String, Object>> updateArray3 = new ArrayList<>();
                    ArrayList<Map<String, Object>> updateArray4 = new ArrayList<>();
                    ArrayList<TrainingInfo> Ti = Tdao.getDownloadData(LOGIN_CARRIER);

                    if (Ti.size() > 0) {
                        for (int i = 0; i < Ti.size(); i++) {
                            Map item = new HashMap();
                            item.put(Vars.KEY_IDX, Ti.get(i).getIdx());
                            item.put(Vars.KEY_MASTER_IDX, Ti.get(i).getMaster_idx());
                            item.put(Vars.KEY_RANK, Ti.get(i).getRank());
                            item.put(Vars.KEY_FNAME, Ti.get(i).getFirstName());
                            item.put(Vars.KEY_SNAME, Ti.get(i).getSurName());
                            item.put(Vars.KEY_VSL_NAME, Ti.get(i).getVsl());
                            item.put(Vars.KEY_VSL_TYPE, Ti.get(i).getVsl_type());
                            item.put(Vars.KEY_BIRTH, Ti.get(i).getBirth());
                            item.put(Vars.KEY_NATIONAL, Ti.get(i).getNational());
                            item.put(Vars.KEY_SIGN_ON, Ti.get(i).getSign_on());
                            item.put(Vars.KEY_SIGN_OFF, Ti.get(i).getSign_off());
                            item.put(Vars.KEY_TRAINING_COURSE, (Ti.get(i).getTraining_course() + 1));
                            item.put(Vars.KEY_YEAR, Ti.get(i).getYear());
                            item.put(Vars.KEY_QUARTER, Ti.get(i).getQuarter());
                            item.put(Vars.KEY_DATE, Ti.get(i).getDate());
                            item.put(Vars.KEY_TIME, Ti.get(i).getTime());
                            item.put(Vars.KEY_SCORE, Ti.get(i).getScore());
//						Log.e("UPDATE", "IDX : " + Ti.get(i).getIdx());
                            updateArray.add(item);
                            List<TrainingInfo> TiRr = Tdao.getProblems(Ti.get(i).getIdx());

                            if (TiRr.size() > 0) {
                                for (int j = 0; j < TiRr.size(); j++) {
                                    if (TiRr.get(j).getRelative_regulation().trim().length() > 0) {
                                        item2 = new HashMap();
//							Log.e("UPDATE", i+" : "+TiRr.get(j).getRelative_regulation());
                                        item2.put(Vars.KEY_HISTORY_IDX, TiRr.get(j).getHistroy_idx());
//                                        item2.put(Vars.KEY_MASTER_IDX, Ti.get(i).getMaster_idx());
//                                        item2.put(Vars.KEY_YEAR, Ti.get(i).getYear());
//                                        item2.put(Vars.KEY_QUARTER, Ti.get(i).getQuarter());
//                                        item2.put(Vars.KEY_RANK, Ti.get(i).getRank());
//                                        item2.put(Vars.KEY_FNAME, Ti.get(i).getFirstName());
//                                        item2.put(Vars.KEY_SNAME, Ti.get(i).getSurName());
//                                        item2.put(Vars.KEY_VSL_NAME, Ti.get(i).getVsl());
//                                        item2.put(Vars.KEY_VSL_TYPE, Ti.get(i).getVsl_type());
//                                        item2.put(Vars.KEY_BIRTH, Ti.get(i).getBirth());
//                                        item2.put(Vars.KEY_NATIONAL, Ti.get(i).getNational());
//                                        item2.put(Vars.KEY_TRAINING_COURSE, (Ti.get(i).getTraining_course() + 1));
//                                        item2.put(Vars.KEY_DATE, Ti.get(i).getDate());
                                        item2.put(Vars.KEY_RELATIVE_REGULATION, TiRr.get(j).getRelative_regulation().trim());

                                        updateArray2.add(item2);
                                    }
                                }
                            } else {
//								Log.e("UPDATE", i+" : "+item.get(Vars.KEY_IDX));
                                item2 = new HashMap();
                                item2.put(Vars.KEY_HISTORY_IDX, item.get(Vars.KEY_IDX));
//								item2.put(Vars.KEY_MASTER_IDX, Ti.get(i).getMaster_idx());
//								item2.put(Vars.KEY_YEAR, Ti.get(i).getYear());
//								item2.put(Vars.KEY_QUARTER, Ti.get(i).getQuarter());
//								item2.put(Vars.KEY_RANK, Ti.get(i).getRank());
//								item2.put(Vars.KEY_FNAME, Ti.get(i).getFirstName());
//								item2.put(Vars.KEY_SNAME, Ti.get(i).getSurName());
//								item2.put(Vars.KEY_VSL_NAME, Ti.get(i).getVsl());
//								item2.put(Vars.KEY_VSL_TYPE, Ti.get(i).getVsl_type());
//								item2.put(Vars.KEY_BIRTH, Ti.get(i).getBirth());
//								item2.put(Vars.KEY_NATIONAL, Ti.get(i).getNational());
//								item2.put(Vars.KEY_TRAINING_COURSE, (Ti.get(i).getTraining_course()+1));
//								item2.put(Vars.KEY_DATE, Ti.get(i).getDate());
                                item2.put(Vars.KEY_RELATIVE_REGULATION, " ");
                                updateArray2.add(item2);
                            }
                        }
                    }

                    TrainingsDao Tsdao = new TrainingsDao(getContext());
                    ArrayList<TrainingsInfo> Tsi = Tsdao.getDownloadData(LOGIN_CARRIER);

                    if (Tsi.size() > 0) {
                        for (int i = 0; i < Tsi.size(); i++) {

                            List<TrainingsInfo> TsiRr = Tsdao.getHistoryMember(Tsi.get(i).getIdx());

                            for (int j = 0; j < TsiRr.size(); j++) {
                                item2 = new HashMap();
//							Log.e("UPDATE", i+" : "+TiRr.get(j).getRelative_regulation());
                                item2.put(Vars.KEY_HISTORY_IDX, TsiRr.get(j).getHistroy_idx());
//								item2.put(Vars.KEY_MASTER_IDX, TsiRr.get(j).getMaster_idx());
                                item2.put(Vars.KEY_TYPE, Tsi.get(i).getType());
//								item2.put(Vars.KEY_VSL_NAME, Tsi.get(i).getVsl());
//								item2.put(Vars.KEY_VSL_TYPE, Tsi.get(i).getVsl_type());
//								if(Tsi.get(i).getType().equals("R"))
//									item2.put(Vars.KEY_TRAINING_COURSE, (Si.getNewRegulation(Tsi.get(i).getTraining_course(), LOGIN_LANGUAGE)));
//								else if(Tsi.get(i).getType().equals("G"))
//									item2.put(Vars.KEY_TRAINING_COURSE, (Si.getGeneralTraining(Tsi.get(i).getTraining_course(), LOGIN_LANGUAGE)));
//								item2.put(Vars.KEY_DATE, Tsi.get(i).getDate());
                                item2.put(Vars.KEY_RANK, TsiRr.get(j).getRank());
                                item2.put(Vars.KEY_SNAME, TsiRr.get(j).getSurName());
                                item2.put(Vars.KEY_FNAME, TsiRr.get(j).getFirstName());
                                updateArray4.add(item2);
                            }

                            Map item = new HashMap();
                            item.put(Vars.KEY_IDX, Tsi.get(i).getIdx());
                            item.put(Vars.KEY_MASTER_IDX, TsiRr.get(0).getMaster_idx());
                            item.put(Vars.KEY_TYPE, Tsi.get(i).getType());
                            item.put(Vars.KEY_VSL_NAME, Tsi.get(i).getVsl());
                            item.put(Vars.KEY_VSL_TYPE, Tsi.get(i).getVsl_type());
                            if (Tsi.get(i).getType().equals("R"))
                                item.put(Vars.KEY_TRAINING_COURSE, (Si.getNewRegulation(Tsi.get(i).getTraining_course(), "ENG")));
                            else if (Tsi.get(i).getType().equals("G"))
                                item.put(Vars.KEY_TRAINING_COURSE, (Si.getGeneralTraining(Tsi.get(i).getTraining_course(), Tsi.get(i).getTraining_course2(), "ENG")));
                            item.put(Vars.KEY_DATE, Tsi.get(i).getDate());
                            item.put(Vars.KEY_SCORE, Tsi.get(i).getScore());
//						Log.e("UPDATE", "IDX : " + Ti.get(i).getIdx());
                            updateArray3.add(item);
                        }
                    }
					/*for(int i = 0; i < updateArray2.size();i++){
						item2 = updateArray2.get(i);
						for(int j = 0; j < item2.size();j++){
							Log.e("UPDATE", j+" : "+item2.get(Vars.KEY_RELATIVE_REGULATION+j));
						}

					}*/

                    new AdminUpdatePro(getContext()).execute(updateArray, updateArray2, updateArray3, updateArray4);

                } else if ("DOWNLOAD".equals(Type)) {
                    Boolean msg = false;
                    int result = 0;
                    String filename = "";
                    SaveCSV sCSV = null;
                    TrainingDao Tdao = new TrainingDao(getContext());
                    TrainingsDao Tsdao = new TrainingsDao(getContext());

                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "yyyyMMddhhmmss", Locale.getDefault());
                    Date date = new Date();
                    ArrayList<TrainingInfo> TiList = Tdao.getDownloadData(LOGIN_CARRIER);

//					Log.e("TwoButtonPopUpDialog","DOWNLOAD TiList : "+ TiList.size());
                    if (TiList.size() > 0) {
                        filename = dateFormat.format(date) + "DownloadData.csv";
                        sCSV = new SaveCSV(getContext(), filename);
                        ArrayList<String[]> downArray = sCSV.getDownload(TiList);
//					Log.e("TwoButtonPopUpDialog","DOWNLOAD downArray : "+ downArray.size());
                        result = sCSV.save(downArray);
//					Log.e("TwoButtonPopUpDialog","DOWNLOAD result : "+ result);
                        if (result > 0) {
                            filename = dateFormat.format(date) + "DownloadData2.csv";
                            sCSV = new SaveCSV(getContext(), filename);
                            ArrayList<TrainingInfo> TiList2 = new ArrayList<>();

                            for (int i = 0; i < TiList.size(); i++) {
                                List<TrainingInfo> TiRr = Tdao.getProblems(TiList.get(i).getIdx());
                                if (TiRr.size() > 0) {
                                    for (int j = 0; j < TiRr.size(); j++) {
                                        if (TiRr.get(j).getRelative_regulation().trim().length() > 0) {
                                            TrainingInfo Ti = new TrainingInfo();
//									Log.e("TwoButtonPopUpDialog","DOWNLOAD Relative_regulation1 : "+ TiRr.get(j).getRelative_regulation());
                                            Ti.setHistroy_idx(TiRr.get(j).getHistroy_idx());
//                                            Ti.setMaster_idx(TiList.get(i).getMaster_idx());
//                                            Ti.setYear(TiList.get(i).getYear());
//                                            Ti.setQuarter(TiList.get(i).getQuarter());
//                                            Ti.setRank(TiList.get(i).getRank());
//                                            Ti.setFirstName(TiList.get(i).getFirstName());
//                                            Ti.setSurName(TiList.get(i).getSurName());
//                                            Ti.setVsl(TiList.get(i).getVsl());
//                                            Ti.setVsl_type(TiList.get(i).getVsl_type());
//                                            Ti.setBirth(TiList.get(i).getBirth());
//                                            Ti.setNational(TiList.get(i).getNational());
//                                            Ti.setTraining_course(TiList.get(i).getTraining_course());
//                                            Ti.setDate(TiList.get(i).getDate());
                                            Ti.setRelative_regulation(TiRr.get(j).getRelative_regulation());
                                            TiList2.add(Ti);
//									Log.e("TwoButtonPopUpDialog","DOWNLOAD Relative_regulation2 : "+ Ti.getRelative_regulation());
//									Log.e("TwoButtonPopUpDialog","DOWNLOAD Relative_regulation3 : "+ TiList2.get(j).getRelative_regulation());
                                        }
                                    }
                                } else {
                                    TrainingInfo Ti = new TrainingInfo();
                                    Ti.setHistroy_idx(TiList.get(i).getIdx());
//									Ti.setMaster_idx(TiList.get(i).getMaster_idx());
//									Ti.setYear(TiList.get(i).getYear());
//									Ti.setQuarter(TiList.get(i).getQuarter());
//									Ti.setRank(TiList.get(i).getRank());
//									Ti.setFirstName(TiList.get(i).getFirstName());
//									Ti.setSurName(TiList.get(i).getSurName());
//									Ti.setVsl(TiList.get(i).getVsl());
//									Ti.setVsl_type(TiList.get(i).getVsl_type());
//									Ti.setBirth(TiList.get(i).getBirth());
//									Ti.setNational(TiList.get(i).getNational());
//									Ti.setTraining_course(TiList.get(i).getTraining_course());
//									Ti.setDate(TiList.get(i).getDate());
                                    Ti.setRelative_regulation("");
                                    TiList2.add(Ti);
                                }
                            }

                            ArrayList<String[]> downArray2 = sCSV.getDownload2(TiList2);

                            result = sCSV.save(downArray2);

                            if (result > 0) {
                                msg = true;
                            } else {
                                msg = false;
                            }
                        } else
                            msg = false;
                    }

                    ArrayList<TrainingsInfo> TsiList3 = Tsdao.getDownloadData(LOGIN_CARRIER);
                    if (TsiList3.size() > 0) {
                        filename = dateFormat.format(date) + "DownloadData4.csv";
                        sCSV = new SaveCSV(getContext(), filename);
                        ArrayList<TrainingsInfo> TsiList4 = new ArrayList<>();

                        for (int i = 0; i < TsiList3.size(); i++) {
                            List<TrainingsInfo> TsiRr = Tsdao.getHistoryMember(TsiList3.get(i).getIdx());
                            for (int j = 0; j < TsiRr.size(); j++) {
                                TrainingsInfo Tsi = new TrainingsInfo();
//									Log.e("TwoButtonPopUpDialog","DOWNLOAD Relative_regulation1 : "+ TiRr.get(j).getRelative_regulation());
                                Tsi.setHistroy_idx(TsiRr.get(j).getHistroy_idx());
//									Tsi.setMaster_idx(TsiRr.get(j).getMaster_idx());
                                Tsi.setType(TsiList3.get(i).getType());
//									Tsi.setVsl(TsiList3.get(i).getVsl());
//									Tsi.setVsl_type(TsiList3.get(i).getVsl_type());
//									Tsi.setTraining_course(TsiList3.get(i).getTraining_course());
//									Tsi.setDate(TsiList3.get(i).getDate());
                                Tsi.setRank(TsiRr.get(j).getRank());
                                Tsi.setSurName(TsiRr.get(j).getSurName());
                                Tsi.setFirstName(TsiRr.get(j).getFirstName());
                                TsiList4.add(Tsi);
                            }
                            TsiList3.get(i).setMaster_idx(TsiRr.get(0).getMaster_idx());
                        }
                        ArrayList<String[]> downArray4 = sCSV.getDownload4(TsiList4);

                        result = sCSV.save(downArray4);
                        if (result > 0) {
                            filename = dateFormat.format(date) + "DownloadData3.csv";
                            sCSV = new SaveCSV(getContext(), filename);
                            ArrayList<String[]> downArray3 = sCSV.getDownload3(TsiList3);
                            result = sCSV.save(downArray3);
                            if (result > 0) {
                                msg = true;
                            } else {
                                msg = false;
                            }
                        } else
                            msg = false;
                    }
                    String massge = "";

                    if (msg) {
                        massge = "DOWNLOAD_OK";
                        sCSV.end();
                    } else {
                        massge = "DOWNLOAD_FAIL";
                    }

                    Si.getMassage(massge, pref.getString("LOGIN_LANGUAGE", ""), getContext(), Toast.LENGTH_SHORT);
                } else if ("UPGRADE".equals(Type)) {
                    new AdminUpgradePro(Act).execute(ArrayFname);
                } else if ("BACKDB".equals(Type)) {
                    Class c = null;

                    if ("D".equals(Menu)) {
                        c = DashBoardMainAct.class;
                    } else if ("H".equals(Menu)) {
                        c = TrainingHistoryAct.class;
                    } else if ("N".equals(Menu)) {
                        c = NewRegulationAct.class;
                    }
                    Si.deleteExternalStoragePrivateFileAll(Act);
                    MemberInfo Mi = (MemberInfo) obj;
                    /**수정추**/
                    Bundle m1 = new Bundle();
                    m1.putParcelable("MemberInfo", Mi);
                    intent = new Intent(Act, c); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                    if ("H".equals(Menu))
                        intent.putExtra("Type", "G");
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("MemberInfo", m1);
                    Act.startActivity(intent);//  go Intent
                    Act.finish();
                    if ("H".equals(Menu))
                        Act.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                    else
                        Act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                } else if ("GODB".equals(Type)) {
//                    intent = new Intent(Act, DashBoardMainAct.class);
                    intent = new Intent(Act, CrewMemberAct.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    Act.startActivity(intent);//  go Intent
                    Act.finish();
                    Act.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                } else if ("ALLDOWNLOAD".equals(Type)) {
//					Log.e("TwoButtonPopUpDialog","ALLDOWNLOAD");
                    Boolean msg = false;
                    int result = 0;
                    String filename = "";
                    SaveCSV sCSV = null;
                    TrainingDao Tdao = new TrainingDao(getContext());
                    TrainingsDao Tsdao = new TrainingsDao(getContext());
                    MemberDao Mdao = new MemberDao(getContext());
                    MemberInfo VslMi = Mdao.getVslMember("ADMIN", "Administration");
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "yyyyMMddhhmmss", Locale.getDefault());
                    Date date = new Date();
                    ArrayList<TrainingInfo> TiList = Tdao.getAllDownloadData(VslMi.getVsl_type());

//					Log.e("TwoButtonPopUpDialog","DOWNLOAD TiList : "+ TiList.size());
                    if (TiList.size() > 0) {
                        filename = dateFormat.format(date) + "AllDownloadData.csv";
                        sCSV = new SaveCSV(getContext(), filename);
                        ArrayList<String[]> downArray = sCSV.getDownload(TiList);
//					Log.e("TwoButtonPopUpDialog","DOWNLOAD downArray : "+ downArray.size());
                        result = sCSV.save(downArray);
//					Log.e("TwoButtonPopUpDialog","DOWNLOAD result : "+ result);
                        if (result > 0) {
                            filename = dateFormat.format(date) + "AllDownloadData2.csv";
                            sCSV = new SaveCSV(getContext(), filename);
                            ArrayList<TrainingInfo> TiList2 = new ArrayList<>();

                            for (int i = 0; i < TiList.size(); i++) {
                                List<TrainingInfo> TiRr = Tdao.getProblems(TiList.get(i).getIdx());
                                if (TiRr.size() > 0) {
                                    for (int j = 0; j < TiRr.size(); j++) {
                                        if (TiRr.get(j).getRelative_regulation().trim().length() > 0) {
                                            TrainingInfo Ti = new TrainingInfo();
//									Log.e("TwoButtonPopUpDialog","DOWNLOAD Relative_regulation1 : "+ TiRr.get(j).getRelative_regulation());
                                            Ti.setHistroy_idx(TiRr.get(j).getHistroy_idx());
                                            Ti.setRelative_regulation(TiRr.get(j).getRelative_regulation());
                                            TiList2.add(Ti);
//									Log.e("TwoButtonPopUpDialog","DOWNLOAD Relative_regulation2 : "+ Ti.getRelative_regulation());
//									Log.e("TwoButtonPopUpDialog","DOWNLOAD Relative_regulation3 : "+ TiList2.get(j).getRelative_regulation());
                                        }
                                    }
                                } else {
                                    TrainingInfo Ti = new TrainingInfo();
                                    Ti.setHistroy_idx(TiList.get(i).getIdx());
                                    Ti.setRelative_regulation("");
                                    TiList2.add(Ti);
                                }
                            }

                            ArrayList<String[]> downArray2 = sCSV.getDownload2(TiList2);

                            result = sCSV.save(downArray2);

                            if (result > 0) {
                                msg = true;
                            } else {
                                msg = false;
                            }
                        } else
                            msg = false;
                    }

                    ArrayList<TrainingsInfo> TsiList3 = Tsdao.getAllDownloadData(VslMi.getVsl_type());
                    if (TsiList3.size() > 0) {
                        filename = dateFormat.format(date) + "AllDownloadData4.csv";
                        sCSV = new SaveCSV(getContext(), filename);
                        ArrayList<TrainingsInfo> TsiList4 = new ArrayList<>();

                        for (int i = 0; i < TsiList3.size(); i++) {
                            List<TrainingsInfo> TsiRr = Tsdao.getHistoryMember(TsiList3.get(i).getIdx());
                            for (int j = 0; j < TsiRr.size(); j++) {
                                TrainingsInfo Tsi = new TrainingsInfo();
//									Log.e("TwoButtonPopUpDialog","DOWNLOAD Relative_regulation1 : "+ TiRr.get(j).getRelative_regulation());
                                Tsi.setHistroy_idx(TsiRr.get(j).getHistroy_idx());
//									Tsi.setMaster_idx(TsiRr.get(j).getMaster_idx());
                                Tsi.setType(TsiList3.get(i).getType());
//									Tsi.setVsl(TsiList3.get(i).getVsl());
//									Tsi.setVsl_type(TsiList3.get(i).getVsl_type());
//									Tsi.setTraining_course(TsiList3.get(i).getTraining_course());
//									Tsi.setDate(TsiList3.get(i).getDate());
                                Tsi.setRank(TsiRr.get(j).getRank());
                                Tsi.setSurName(TsiRr.get(j).getSurName());
                                Tsi.setFirstName(TsiRr.get(j).getFirstName());
                                TsiList4.add(Tsi);
                            }
                            TsiList3.get(i).setMaster_idx(TsiRr.get(0).getMaster_idx());
                        }
                        ArrayList<String[]> downArray4 = sCSV.getDownload4(TsiList4);

                        result = sCSV.save(downArray4);
                        if (result > 0) {
                            filename = dateFormat.format(date) + "AllDownloadData3.csv";
                            sCSV = new SaveCSV(getContext(), filename);
                            ArrayList<String[]> downArray3 = sCSV.getDownload3(TsiList3);
                            result = sCSV.save(downArray3);
                            if (result > 0) {
                                msg = true;
                            } else {
                                msg = false;
                            }
                        } else
                            msg = false;
                    }
                    String massge = "";

                    if (msg) {
                        massge = "DOWNLOAD_OK";
                        editor = pref.edit();
                        editor.putInt("ALLDOWNLOAD", 1);
                        editor.commit();
                        ((Button) obj).setVisibility(View.GONE);
                    } else {
                        massge = "DOWNLOAD_FAIL";
                        editor = pref.edit();
                        editor.putInt("ALLDOWNLOAD", 0);
                        editor.commit();
                    }

                    Si.getMassage(massge, pref.getString("LOGIN_LANGUAGE", ""), getContext(), Toast.LENGTH_SHORT);
                }

                dismiss();
                break;
        }
        if ("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
            new CommonUtil().setHideNavigationBar(Act.getWindow().getDecorView());
        }
    }
}
