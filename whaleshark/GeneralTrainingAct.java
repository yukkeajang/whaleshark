package com.togetherseatech.whaleshark;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.media.PlaybackParams;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;

import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.ads.AdsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SubtitleView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.togetherseatech.whaleshark.Db.History.TrainingDao;
import com.togetherseatech.whaleshark.Db.Member.MemberInfo;
import com.togetherseatech.whaleshark.Db.Training.TrainingsDao;
import com.togetherseatech.whaleshark.Db.Training.TrainingsInfo;
import com.togetherseatech.whaleshark.util.CommonUtil;
import com.togetherseatech.whaleshark.util.CustomPlayBackControlView;
import com.togetherseatech.whaleshark.util.CustomSimpleExoPlayerView;
import com.togetherseatech.whaleshark.util.FullDrawerLayout;
import com.togetherseatech.whaleshark.util.SelectItems;
import com.togetherseatech.whaleshark.util.TextFontUtil;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.progress.ProgressMonitor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GeneralTrainingAct extends Activity implements View.OnClickListener {

    private static final String TAG = "GeneralTrainingAct";
    private Button DashBoard_Bt, Training_Bt, History_Bt, Help_Bt;
    private FullDrawerLayout GT_DrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ImageView GT_Language_Iv;
    private TextView GT_Title_Tv, GT_Name_Tv;
    private Intent intent;
    private TrainingsDao Tsdao;
    private SharedPreferences pref;
    private TextFontUtil tf;
    private SelectItems Si;
    private String LOGIN_LANGUAGE;
    private SimpleExoPlayer player;
    // 액티비티에 보여지는 리스트뷰
//    private ListView GT_Lv;
    // 리스트뷰에 사용할 어댑터
//    private listView_Training_Adapter mAdapter;

    // 액티비티에 보여지는 리스트뷰
    public static ExpandableListView GT_Lv;
    // 리스트뷰에 사용할 어댑터
    public static GeneralExpandableAdapter mAdapter;

    // 어댑터 및 리스트뷰에 사용할 데이터 리스트
//    private List<TrainingsInfo> TsiList;
    private boolean check = false;
    private static int tempW = 0;
    private static int tempH = 0;
    public static Activity Act;
    private AudioManager audioManager;
    private int volume = 15;
    private String VideoName;
    private Boolean IsPlay = false;
    private TrainingsInfo Tsi;
    private ArrayList<SelectItems> MbList, MbGroupList;
    private ArrayList<ArrayList<SelectItems>> MbChildList;
    private boolean FileLoading = true;


    private final String STATE_RESUME_WINDOW = "resumeWindow";
    private final String STATE_RESUME_POSITION = "resumePosition";
    private final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";

    private ImageButton mSubtitlesButton;
    private boolean mExoPlayerFullscreen = false,  mExoPlayerSubtitles = false;;
    private FrameLayout mFullScreenButton;
    private ImageView mFullScreenIcon;
    private ImageView mSubTitleIcon;
    private Dialog mFullScreenDialog;
    private CustomSimpleExoPlayerView GT_Vv;
    private CustomPlayBackControlView controlView;
    private SubtitleView subtitleView;
    private ProgressBar progressBar;
    private int mResumeWindow;
    private long mResumePosition;
    private MediaSource mVideoSource;
    private MergingMediaSource mergedSource;
    private TextView tv;
    private File readFile;
    private DrmSessionManager<FrameworkMediaCrypto> drmSessionManager;
    private PlaybackParameters playbackParameters;
    public static ProgressMonitor progressMonitor;
    private ProgressDialog eDialog;
    private File subTitleFile;
    private DefaultRenderersFactory rf;
    private WindowManager.LayoutParams lp;
    private MemberInfo Mi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_training_act);

        lp = getWindow().getAttributes();
//        lp.screenBrightness = (float) 125/255;
        lp.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;		//화면이 켜진상태 유지
        getWindow().setAttributes(lp);

        Act = this;
        pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
        LOGIN_LANGUAGE = pref.getString("LOGIN_LANGUAGE", "");
        Si = new SelectItems();
        tf = new TextFontUtil();
        Tsdao = new TrainingsDao(this);
//        TsiList = new ArrayList<>();
        FileLoading = false;
        if (savedInstanceState != null) {
            mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
            mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
            mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
        }
        /*** Bundle로 받는 부분 classException 에러 수정후 코드 **/
        Bundle M = getIntent().getBundleExtra("MemberInfo");
        Mi = M.getParcelable("MemberInfo");
        /*** Bundle로 받는 부분 classException 에러 수정후 코드 **/
        /*** Bundle로 받는 부분 classException 에러 수정전 코드 **/
        //Mi = (MemberInfo)getIntent().getExtras().get("MemberInfo");

        initSetting();
        initEventListener();

        MbList = Si.getGeneralTraining();
        List<TrainingsInfo> mList = Tsdao.getMatrixs("G", Si.getGTRankDept(Mi.getRank()));
//        MbChildList = Si.getGeneralTrainings();
        MbGroupList = new ArrayList<SelectItems>();
        MbChildList = new ArrayList<ArrayList<SelectItems>>();
        ArrayList<SelectItems> list = new ArrayList<>();
        Log.e("test","MbList Size = " + MbList.size());
        for(int i = 0; i < MbList.size(); i++) {
//            Log.e("test","(RankImg = " + MbList.get(i).getRankImg()
//                    +"(RankChild = " + MbList.get(i).getRankChild() + "(isChecked = " +
//                    MbList.get(i).getChecked() +"(isMCheked = " + MbList.get(i).getMChecked() +")");


//            TsiList.add(new TrainingsInfo(Si.getGeneralTraining(i, LOGIN_LANGUAGE),  Si.getGeneralTraining().get(i).getRankChild(), Si.getGeneralTraining().get(i).getRankImg()));
            for(TrainingsInfo tsi : mList) {


                if(MbList.get(i).getRankImg() == tsi.getTraining_course() && MbList.get(i).getRankChild() == tsi.getTraining_course2()) {


                        MbList.get(i).setMChecked(true);

                }


            }
            if(MbList.get(i).getRankChild() == 0) {

                MbGroupList.add(MbList.get(i));

            }else{
                list.add(MbList.get(i));
            }
        }
        for(int i = 0; i < MbGroupList.size(); i++) {
            ArrayList<SelectItems> temp = new ArrayList<>();
            for(int j = 0; j < list.size(); j++) {
                if(MbGroupList.get(i).getRankImg() == list.get(j).getRankImg())
                    temp.add(list.get(j));
            }
            MbChildList.add(temp);
        }
        // 커스텀 어댑터를 생성한다.
        mAdapter = new GeneralExpandableAdapter(this, MbGroupList, MbChildList);
        // 리스트뷰에 어댑터를 세팅한다.
        GT_Lv.setAdapter(mAdapter);
        // 처음 시작시 그룹 모두 열기 (expandGroup)
        int groupCount = (int) mAdapter.getGroupCount();
        for (int i = 0; i < groupCount; i++) {
            GT_Lv.expandGroup(i);
        }
        GT_Name_Tv.setText(Mi.getSurName() + " " + Mi.getFirstName());
        if("KR".equals(LOGIN_LANGUAGE)) {
            GT_Language_Iv.setImageResource(R.mipmap.language_kr);
            GT_Title_Tv.setText(MbList.get(0).getKr());
        }else if("ENG".equals(LOGIN_LANGUAGE)) {
            GT_Language_Iv.setImageResource(R.mipmap.language_eng);
            GT_Title_Tv.setText(MbList.get(0).getEng());
        }
        mDrawerToggle = new ActionBarDrawerToggle(this, GT_DrawerLayout, R.mipmap.back, R.string.open, R.string.close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        GT_DrawerLayout.setDrawerListener(mDrawerToggle);
        GT_DrawerLayout.openDrawer(GT_Lv);
        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        GT_Vv.setBackgroundResource(R.mipmap.standby);
    }

    private void initSetting() {
        DashBoard_Bt = (Button)findViewById(R.id.general_dashboard_bt);
        Training_Bt = (Button)findViewById(R.id.general_training_bt);
        Training_Bt.setBackgroundResource(R.mipmap.training_on);
        History_Bt = (Button)findViewById(R.id.general_history_bt);
        Help_Bt = (Button)findViewById(R.id.general_help_bt);
        GT_Language_Iv = (ImageView) findViewById(R.id.general_language_iv);
        GT_Name_Tv = (TextView) findViewById(R.id.general_name_tv);
        tf.setNanumSquareL(this, GT_Name_Tv);
        GT_Title_Tv = (TextView) findViewById(R.id.general_title_tv);
        tf.setNanumSquareEB(this, GT_Title_Tv);
        GT_DrawerLayout = (FullDrawerLayout) findViewById(R.id.drawer_layout);
        GT_Lv = (ExpandableListView)findViewById(R.id.general_lv);
//        GT_Vv = (ScreenResizeVideoView)findViewById(R.id.general_vv);
        GT_Vv = (CustomSimpleExoPlayerView)findViewById(R.id.general_vv);
        GT_Vv.setUseController(false);
    }

    private void initEventListener() {
        DashBoard_Bt.setOnClickListener(this);
        Training_Bt.setOnClickListener(this);
        History_Bt.setOnClickListener(this);
        Help_Bt.setOnClickListener(this);
//        GT_Lv.setOnItemClickListener(this);
        GT_Lv.setOnGroupClickListener(setOnGroupClickEvent);
        GT_Lv.setOnChildClickListener(setOnChildClickEvent);
    }

    private ExpandableListView.OnGroupClickListener setOnGroupClickEvent = new ExpandableListView.OnGroupClickListener() {
        int lastClickedPosition = 0;

        @Override
        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
            // 선택 한 groupPosition의 펼침/닫힘 상태 체크
//            Boolean isExpand = (!Admin_ExpandableListView.isGroupExpanded(groupPosition));
            // 이 전에 열려있던 group 닫기
//            Admin_ExpandableListView.collapseGroup(lastClickedPosition);
//            if(isExpand){
//                Admin_ExpandableListView.expandGroup(groupPosition);
//            }
//            lastClickedPosition = groupPosition;
            SelectItems si = MbGroupList.get(groupPosition);
            Log.e(TAG,"OnGroupClickListener SelectItems = "+groupPosition+"/"+si.getRankImg()+"/"+si.getRankChild());
            VideoName = Si.getGeneralTrainingRaw(si.getRankImg(),si.getRankChild(),LOGIN_LANGUAGE);
//            Tsi = TsiList.get(groupPosition);
            Log.e(TAG,"OnGroupClickListener VideoName = "+VideoName);
            if(!"".equals(VideoName)) {
                if(GT_Vv.getPlayer() != null)
                    GT_Vv.getPlayer().release();
    //        Log.e(TAG,"onItemClick position = "+position);
                Si.deleteExternalStoragePrivateFileAll(Act);
    //        GT_Vv.setBackgroundColor(Color.parseColor("#00000000"));
                Tsi = new TrainingsInfo(Si.getGeneralTraining(si,LOGIN_LANGUAGE), si.getRankImg(), si.getRankChild());
    //            ArrayList<SelectItems> MbList2 = Si.getGeneralTrainingRaw();
    //        Toast.makeText(this, "onItemClick : " + position +" / "+ si.getKr(), Toast.LENGTH_LONG).show();
                GT_Title_Tv.setText(Si.getGeneralTraining(si,LOGIN_LANGUAGE));
                /*for(int i=0; i < MbGroupList.size(); i++){
                    if(i != groupPosition) {
                        MbGroupList.get(i).setChecked(false);
                    }
                }*/

                if(!"".equals(VideoName))
                    MbGroupList.get(groupPosition).setChecked(true);

                for(int i=0; i < MbChildList.size(); i++){

                    if(i != groupPosition) {
                        MbGroupList.get(i).setChecked(false);
                    }
                    for(int j=0; j < MbChildList.get(i).size(); j++) {
                        MbChildList.get(i).get(j).setChecked(false);
                    }
                }
                mAdapter.notifyDataSetChanged();
                GT_DrawerLayout.closeDrawer(GT_Lv);
                new FileLoadingPro(Act).execute(VideoName,"mp4");
                Help_Bt.setVisibility(View.INVISIBLE);
            }
            return true;
        }
    };

    private ExpandableListView.OnChildClickListener setOnChildClickEvent = new ExpandableListView.OnChildClickListener() {
        int lastClickedPosition = 0;
        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            if(GT_Vv.getPlayer() != null)
                GT_Vv.getPlayer().release();
            Si.deleteExternalStoragePrivateFileAll(Act);
            SelectItems si = MbChildList.get(groupPosition).get(childPosition);
            Tsi = new TrainingsInfo(Si.getGeneralTraining(si,LOGIN_LANGUAGE), si.getRankImg(), si.getRankChild());
//            Log.e("GeneralTrainingAct","onChildClick " + Tsi.getTraining_course() + "/" + Tsi.getTraining_course2());
            GT_Title_Tv.setText(Si.getGeneralTraining(si,LOGIN_LANGUAGE));
            Log.e("test","GT_Title : " + GT_Title_Tv);
            VideoName = Si.getGeneralTrainingRaw(si.getRankImg(),si.getRankChild(),LOGIN_LANGUAGE);
//            Log.e(TAG,"OnChildClickListener VideoName = "+VideoName);
            MbChildList.get(groupPosition).get(childPosition).setChecked(true);
            for(int i=0; i < MbChildList.size(); i++){
                MbGroupList.get(i).setChecked(false);
                for(int j=0; j < MbChildList.get(i).size(); j++) {
                    if (!(i == groupPosition && j == childPosition)){
                        MbChildList.get(i).get(j).setChecked(false);
                    }
                }
            }
            mAdapter.notifyDataSetChanged();
            GT_DrawerLayout.closeDrawer(GT_Lv);
            new FileLoadingPro(Act).execute(VideoName,"mp4");
            Help_Bt.setVisibility(View.INVISIBLE);
            return true;
        }
    };

    private void initFullscreenDialog() {
        mFullScreenDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mExoPlayerFullscreen)
                    closeFullscreenDialog();
                super.onBackPressed();
            }
        };
        mFullScreenDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
         if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
            new CommonUtil().setHideNavigationBar(mFullScreenDialog.getWindow().getDecorView());
        }
    }

    private void openFullscreenDialog() {
        ((ViewGroup) GT_Vv.getParent()).removeView(GT_Vv);
        mFullScreenDialog.addContentView(GT_Vv, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(Act, R.mipmap.ic_fullscreen_skrink));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
        mFullScreenDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }


    private void closeFullscreenDialog() {
        ((ViewGroup) GT_Vv.getParent()).removeView(GT_Vv);
        ((FrameLayout) GT_DrawerLayout.findViewById(R.id.general_fl)).addView(GT_Vv);
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(Act, R.mipmap.ic_fullscreen_expand));
    }

    private void openSubtitles() {
        subtitleView.setVisibility(View.VISIBLE);
        mExoPlayerSubtitles = true;
        mSubtitlesButton.setImageDrawable(ContextCompat.getDrawable(Act, R.mipmap.ic_subtitle_on));
    }

    private  void closeSubtitles() {
        subtitleView.setVisibility(View.INVISIBLE);
        mExoPlayerSubtitles = false;
        mSubtitlesButton.setImageDrawable(ContextCompat.getDrawable(Act, R.mipmap.ic_subtitle_off));
    }

    private void initFullscreenButton() {
        controlView = (CustomPlayBackControlView) GT_Vv.findViewById(R.id.control);
        tv = (TextView)controlView.findViewById(R.id.playback_rate_control);
        mFullScreenIcon = (ImageView)controlView.findViewById(R.id.exo_fullscreen_icon);
        subtitleView = (SubtitleView) GT_Vv.findViewById(R.id.subtitles);
        subtitleView.setVisibility(View.INVISIBLE);
        mFullScreenButton = (FrameLayout) controlView.findViewById(R.id.exo_fullscreen_button);
        mFullScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mExoPlayerFullscreen)
                    openFullscreenDialog();
                else
                    closeFullscreenDialog();
            }
        });

        mSubtitlesButton = (ImageButton)controlView.findViewById(R.id.subtitle);
        mSubtitlesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mExoPlayerSubtitles)
                    openSubtitles();
                else
                    closeSubtitles();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.general_dashboard_bt :
                if(IsPlay)
                    new TwoButtonPopUpDialog(this, this,"BACKDB", "D",Mi).show();
                else {
                    Bundle m = new Bundle();
                    m.putParcelable("MemberInfo",Mi);
                    intent = new Intent(this, DashBoardMainAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("MemberInfo", m);
                    startActivity(intent);//  go Intent
                    finish();
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                }
                break;
            case R.id.general_history_bt :
                if(IsPlay)
                    new TwoButtonPopUpDialog(this, this,"BACKDB","H",Mi).show();
                else {
//                    intent = new Intent(this, CrewMemberAct.class);
                    Bundle m = new Bundle();
                    m.putParcelable("MemberInfo",Mi);
                    intent = new Intent(this, TrainingHistoryAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("MemberInfo", m);
                    intent.putExtra("Type", "G");
                    startActivity(intent);//  go Intent
                    finish();
                    overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                }
                break;
            case R.id.general_help_bt :
                intent = new Intent(GeneralTrainingAct.this, GuideAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);//  go Intent
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_in);
                break;
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mDrawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }


    class FileLoadingPro extends AsyncTask<String, Integer, File> {

        private String line = null;
        private Context mContext;
        private SelectItems Si;


        public FileLoadingPro(Context con) {
            mContext = con;
            Si = new SelectItems();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            eDialog = new ProgressDialog(mContext);
            eDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            eDialog.setMessage("Loading File...");
            eDialog.setIndeterminate(false);
            eDialog.setCancelable(false);
            eDialog.setProgress(0);
            eDialog.setMax(100);
            eDialog.setSecondaryProgress(0);
            eDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
             if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
                new CommonUtil().setHideNavigationBar(eDialog.getWindow().getDecorView());
            }
            eDialog.show();
            eDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
           /* eDialog = new ProgressDialog(mContext);
            eDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            eDialog.setMessage("Loading..");
            eDialog.show();*/
        }

        @Override
        protected File doInBackground(String... str) {
            subTitleFile = Si.getSubtitleFile(mContext,str[0]+".srt");
            readFile = null;
            SharedPreferences pref = mContext.getSharedPreferences("pref", Context.MODE_PRIVATE);
            String ZIP_PASSWORD = pref.getString("ZIP_PASSWORD", "");
            String PackageName = mContext.getPackageName();
            String fullFileName = str[0] + "." + str[1];
            File root = Environment.getExternalStorageDirectory();
            File expPath = new File(root.toString() + "/Android/obb/" + PackageName);
            String strMainPath = expPath + File.separator + str[0] +".obb";
            try{
                Log.e("test","strMainPath : "+strMainPath);
                Log.e("test","ZIP_PASSWORD : "+ZIP_PASSWORD);
                ZipFile zipFile = new ZipFile(strMainPath);
                zipFile.setRunInThread(true);
                progressMonitor = zipFile.getProgressMonitor();
                if (zipFile.isEncrypted()) {
                    zipFile.setPassword(ZIP_PASSWORD);
                }
                zipFile.extractFile(fullFileName, mContext.getExternalFilesDir(null).toString());
                /*while (progressMonitor.getPercentDone() < 1){
                }*/
                while (progressMonitor.getState() != ProgressMonitor.STATE_READY) {
                    eDialog.setProgress(progressMonitor.getPercentDone());
                }
                readFile = new File(mContext.getExternalFilesDir(null), fullFileName);
                int file_size = Integer.parseInt(String.valueOf(readFile.length()/1024));
                Log.e("test","file size : " + file_size);
            } catch (ZipException e) {
                e.printStackTrace();
                Log.e("test", "ZipException : "+e + "/" + e.getCode());
                return readFile;
            } catch (Exception e) {
                Log.e("test", "Exception : "+e);
                return readFile;
            }
            return readFile;
        }

        @Override
        protected void onProgressUpdate(Integer... args) {
            eDialog.setProgress(args[0]);
        }

        @Override
        protected void onPostExecute(File result) {
            super.onPostExecute(result);
            if (result != null) {
                initFullscreenDialog();
                initFullscreenButton();

                DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
                TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
                rf = new DefaultRenderersFactory(mContext, drmSessionManager, DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON);
                player = ExoPlayerFactory.newSimpleInstance(rf, trackSelector, new DefaultLoadControl());
//                player.setPlaybackParameters(playbackParameters);
                GT_Vv.setUseController(true);
                GT_Vv.requestFocus();
                GT_Vv.setPlayer(player);
                Uri videoUri = Uri.parse(result.getPath());
                DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mContext, Util.getUserAgent(mContext, getApplicationContext().getPackageName()));
                mVideoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(videoUri);
                Log.e("test","video uri : " + videoUri);
//                Log.e(TAG, "subTitleFile = " + subTitleFile.isFile() + "");
//                Log.e(TAG, "subTitleFile = " + subTitleFile + "");
                if (subTitleFile.isFile()) {
                    Format subtitleFormat = Format.createTextSampleFormat(null, MimeTypes.APPLICATION_SUBRIP, Format.NO_VALUE, "en", null);
                    MediaSource subtitleSource = new SingleSampleMediaSource.Factory(dataSourceFactory)
                            .createMediaSource(Uri.parse(subTitleFile.getPath()), subtitleFormat, C.TIME_UNSET);
//                    MediaSource subtitleSource = new SingleSampleMediaSource( Uri.parse(subTitleFile.getPath()), dataSourceFactory, subtitleFormat, C.TIME_UNSET);
                    mergedSource = new MergingMediaSource(mVideoSource, subtitleSource); // to be used later
                    player.prepare(mergedSource);
                }else{
                    player.prepare(mVideoSource);
                }
//                LoopingMediaSource loopingSource = new LoopingMediaSource(videoSource);
                player.setPlayWhenReady(true); //run file/link when ready to play.
                PlaybackParams params = new PlaybackParams();
                params.setSpeed(1.0f);
                player.setPlaybackParams(params);
                player.addListener(new ComponentListener());
                tv.setText("x 1.0");
                CustomPlayBackControlView.current_playbackrate = 1.0f;
            }else{
                Si.getToast(Act, "It will be updated in the future.", Toast.LENGTH_SHORT);
            }
            eDialog.dismiss();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
//        eDialog.dismiss();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_RESUME_WINDOW, mResumeWindow);
        outState.putLong(STATE_RESUME_POSITION, mResumePosition);
        outState.putBoolean(STATE_PLAYER_FULLSCREEN, mExoPlayerFullscreen);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.e(TAG, "onDestroy");
        if (GT_Vv != null && GT_Vv.getPlayer() != null) {
            GT_Vv.getPlayer().release();
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.e(TAG, "onStop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause");
        if (GT_Vv != null && GT_Vv.getPlayer() != null) {
            mResumeWindow = GT_Vv.getPlayer().getCurrentWindowIndex();
            mResumePosition = Math.max(0, GT_Vv.getPlayer().getCurrentPosition());
//            GT_Vv.getPlayer().release();
        }
        /*if (mFullScreenDialog != null)
            mFullScreenDialog.dismiss();*/
    }

    private void initExoPlayer(Long position) {
//        Log.e(TAG, "initExoPlayer " + position + " / " + Float.valueOf(tv.getText().toString().substring(2)));
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        player = ExoPlayerFactory.newSimpleInstance(rf, trackSelector, new DefaultLoadControl());
        GT_Vv.setPlayer(player);


        if (subTitleFile.isFile()) {
            GT_Vv.getPlayer().prepare(mergedSource);
        }else{
            GT_Vv.getPlayer().prepare(mVideoSource);
        }
//        GT_Vv.getPlayer().prepare(mVideoSource);
        GT_Vv.getPlayer().setPlayWhenReady(true);
        GT_Vv.getPlayer().seekTo(Math.max(0, position));
        GT_Vv.getPlayer().addListener(new ComponentListener());
        float speed = Float.valueOf(tv.getText().toString().substring(2));
        PlaybackParams params = new PlaybackParams();
        params.setSpeed(speed);
        GT_Vv.getPlayer().setPlaybackParams(params);
        CustomPlayBackControlView.current_playbackrate = speed;
    }

    private class ComponentListener implements Player.EventListener {
        @Override
        public void onLoadingChanged(boolean isLoading) {
//                        Log.e(TAG, "onLoadingChanged");
        }
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
//            Log.e(TAG, "onPlayerStateChanged : "+ playbackState + "/" +playWhenReady);
            if(playbackState == Player.STATE_READY){
//                Log.e(TAG, "STATE_READY" + "");
                IsPlay = true;
            }
            if(playbackState == Player.STATE_BUFFERING){
//                Log.e(TAG, "STATE_BUFFERING" + "");
                /*if(IsPlay) {
                    player.stop();
                    new BufferingPro().execute();
                }*/
            }
            if(playbackState == Player.STATE_ENDED){
//                            Si.deleteExternalStoragePrivateFile(mContext,VideoName+".mp4");
                if(IsPlay){
//                    Log.e("GeneralTrainingAct","START " + Tsi.getTraining_course() + "/" + Tsi.getTraining_course2());
                    if(!"intro".equals(VideoName) /*&& !"gt_4_k".equals(VideoName)*/) {
                        Bundle m = new Bundle();
                        m.putParcelable("MemberInfo",Mi);
                        Intent intent = new Intent(Act, AttendanceAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.putExtra("MemberInfo", m);
                        intent.putExtra("TrainingsInfo", Tsi);
                        intent.putExtra("Type", "G");
                        startActivity(intent);//  go Intent
                        finish();
                        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
                    }
                }
            }
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {
        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
        }

        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
//            Log.e(TAG, "onTimelineChanged :" + timeline);
        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
//                        Log.e(TAG, "onTracksChanged " + trackGroups.length +"/"+ trackSelections.length);
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
//                        Log.e(TAG, "onPlayerError");
            IsPlay = false;
        }

        @Override
        public void onPositionDiscontinuity(int reason) {
        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
//                        Log.e(TAG, "onPlaybackParametersChanged = " + playbackParameters);
        }

        @Override
        public void onSeekProcessed() {
        }
    }

    class BufferingPro extends AsyncTask<Long, Integer, Long> {
        int per;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            eDialog = new ProgressDialog(Act);
            eDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            eDialog.setMessage("Buffering..");
            eDialog.setIndeterminate(false);
            eDialog.setCancelable(false);
            eDialog.setProgress(0);
            eDialog.setMax(100);
            eDialog.setSecondaryProgress(0);
            eDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            eDialog.setProgress(progress[0]);
        }

        @Override
        protected Long doInBackground(Long... params) {
            long duration = player.getDuration();
            long position = player.getCurrentPosition();
//                            ((duration * progressBar.getProgress()) / 1000);
            per = player.getBufferedPercentage() + 15;

            if(per > 100) per = 98;
//            Log.e(TAG, "STATE_BUFFERING / "+progressMonitor.getPercentDone() +  " / " + progressMonitor.getTotalWork() + " / " + per + " / " + duration);
            int zero = progressMonitor.getPercentDone();
            if(progressMonitor.getState() != ProgressMonitor.STATE_READY) {
                while (progressMonitor.getPercentDone() < per) {
//                                    eDialog.setProgress(progressMonitor.getPercentDone());
//                    Log.e(TAG, progressMonitor.getPercentDone() + " / " + per);
//                    Log.e(TAG, position + " / " + player.getContentPosition());
                    int per2 = (int) (((double)(progressMonitor.getPercentDone() - zero) / (double)(per - zero)) * 100);
//                Log.e(TAG, "STATE_BUFFERING / "+per2);
                    eDialog.setProgress(per2);
                    if(progressMonitor.getState() == 0) break;
                }
                Log.e(TAG, "STATE_BUFFERING END");
            }
            return position;
        }

        @Override
        protected void onPostExecute(Long result) {
            super.onPostExecute(result);
            eDialog.dismiss();
//            if(progressMonitor.getPercentDone() >= per)
            initExoPlayer(result);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
        /*if(FileLoading)
            new FileLoadingPro(this).execute(VideoName+".mp4");*/
        /*if (GT_Vv == null) {
            GT_Vv = (CustomSimpleExoPlayerView)findViewById(R.id.general_vv);
            initFullscreenDialog();
            initFullscreenButton();
            Log.e(TAG, "onResume = " + readFile.getPath());
            Uri videoUri = Uri.parse(readFile.getPath());
            DefaultBandwidthMeter bandwidthMeterA = new DefaultBandwidthMeter();
            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, getApplicationContext().getPackageName()), bandwidthMeterA);
            mVideoSource = new HlsMediaSource(videoUri, dataSourceFactory, 1, null, null);
        }
        initExoPlayer();
        if (mExoPlayerFullscreen) {
            ((ViewGroup) GT_Vv.getParent()).removeView(GT_Vv);
            mFullScreenDialog.addContentView(GT_Vv, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.ic_fullscreen_skrink));
            mFullScreenDialog.show();
        }*/
         if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
            new CommonUtil().setHideNavigationBar(getWindow().getDecorView());
        }
    }

    @Override
    protected void onUserLeaveHint(){
        super.onUserLeaveHint();
        Log.e(TAG, "onUserLeaveHint");
        Si.deleteExternalStoragePrivateFileAll(this);
        FileLoading = true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.e(TAG, "onWindowFocusChanged : " + hasFocus);
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
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            //Do your thing  what you want...
            if(volume != 0)
                volume -= 1;
        } else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            if(volume != 15)
                volume += 1;
        }
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume,  AudioManager.FLAG_SHOW_UI);
        return true;
    }
}