package com.togetherseatech.whaleshark;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
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
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.TextRenderer;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SubtitleView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.togetherseatech.whaleshark.Db.History.TrainingDao;
import com.togetherseatech.whaleshark.Db.History.TrainingInfo;
import com.togetherseatech.whaleshark.Db.Member.MemberInfo;
import com.togetherseatech.whaleshark.util.CommonUtil;
import com.togetherseatech.whaleshark.util.CustomPlayBackControlView;
import com.togetherseatech.whaleshark.util.CustomSimpleExoPlayerView;
import com.togetherseatech.whaleshark.util.FullDrawerLayout;
import com.togetherseatech.whaleshark.util.SelectItems;
import com.togetherseatech.whaleshark.util.TextFontUtil;
import com.togetherseatech.whaleshark.util.XmlSAXParser3;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.progress.ProgressMonitor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by seonghak on 2017. 11. 3..
 */

public class PersonalTrainingAct extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener{

    private static final String TAG = "PersonalTrainingAct";
    private Intent intent;
    private TrainingDao Tdao;
    private SharedPreferences pref;
    private TextFontUtil tf;
    private static MemberInfo Mi;
    private SelectItems Si;
    private String LOGIN_LANGUAGE;
    private int LOGIN_CARRIER;

    private Button DashBoard_Bt, Training_Bt, History_Bt, Help_Bt;
    private static FullDrawerLayout PT_DrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ImageView PT_Language_Iv;
    private TextView PT_Title_Tv, PT_Name_Tv;
    private CustomSimpleExoPlayerView PT_Vv;
    private CustomPlayBackControlView controlView;
    private SubtitleView subtitleView;
    private SimpleExoPlayer player;
    private static Boolean IsPlay = false;
    private String VideoName;
    // 액티비티에 보여지는 리스트뷰
    private ListView PT_Lv;
    // 리스트뷰에 사용할 어댑터
    private listView_Training_Adapter mAdapter;
    // 어댑터 및 리스트뷰에 사용할 데이터 리스트
    private List<SelectItems> MbList;
    private List<TrainingInfo> TiList;
    private static Activity Act;
    private AudioManager audioManager;
    private int volume = 15;

    private final String STATE_RESUME_WINDOW = "resumeWindow";
    private final String STATE_RESUME_POSITION = "resumePosition";
    private final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";

    private ImageButton mSubtitlesButton;
    private static boolean mExoPlayerFullscreen = false;
    private boolean mExoPlayerSubtitles = false;
    private static FrameLayout mFullScreenButton;
    private static ImageView mFullScreenIcon;
    private static Dialog mFullScreenDialog;
    private int mResumeWindow;
    private long mResumePosition;
    private static TextView tv;
    private TrackSelector trackSelector;
    private static int Position = 0;
    private ProgressMonitor progressMonitor;
    private MediaSource mVideoSource;
    private MergingMediaSource mergedSource;
    private DrmSessionManager<FrameworkMediaCrypto> drmSessionManager;
    private PlaybackParameters playbackParameters;
    private ProgressDialog eDialog;
    private File subTitleFile;
    private DefaultRenderersFactory rf;
    private WindowManager.LayoutParams lp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_training_act);

        lp = getWindow().getAttributes();
//        lp.screenBrightness = (float) 125/255;
        lp.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;		//화면이 켜진상태 유지
        getWindow().setAttributes(lp);

        Act = this;
        pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
        LOGIN_LANGUAGE = pref.getString("LOGIN_LANGUAGE", "");
        LOGIN_CARRIER = pref.getInt("LOGIN_CARRIER", 0);
        Mi = (MemberInfo)getIntent().getExtras().get("MemberInfo");
        Si = new SelectItems();
        tf = new TextFontUtil();
        Tdao = new TrainingDao(this);
        MbList = new ArrayList<SelectItems>();

        if (savedInstanceState != null) {
            mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
            mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
            mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
        }

        initSetting();
        initEventListener();

        MbList = Si.getRankChapter(Mi.getRank(),Mi.getVsl_type());

        // 커스텀 어댑터를 생성한다.
        mAdapter = new listView_Training_Adapter(this, R.layout.training_row, MbList);

        // 리스트뷰에 어댑터를 세팅한다.
        PT_Lv.setAdapter(mAdapter);

        PT_Name_Tv.setText(Mi.getSurName() + " " + Mi.getFirstName());
        if("KR".equals(LOGIN_LANGUAGE)) {
            PT_Language_Iv.setImageResource(R.mipmap.language_kr);
            PT_Title_Tv.setText(MbList.get(0).getKr());
        }else if("ENG".equals(LOGIN_LANGUAGE)) {
            PT_Language_Iv.setImageResource(R.mipmap.language_eng);
            PT_Title_Tv.setText(MbList.get(0).getEng());
        }

        mDrawerToggle = new ActionBarDrawerToggle(this, PT_DrawerLayout, R.mipmap.back, R.string.open, R.string.close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        PT_DrawerLayout.setDrawerListener(mDrawerToggle);
        PT_DrawerLayout.openDrawer(PT_Lv);

        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
    }

    private void initSetting() {

        DashBoard_Bt = (Button)findViewById(R.id.training_dashboard_bt);
        Training_Bt = (Button)findViewById(R.id.training_bt);
        Training_Bt.setBackgroundResource(R.mipmap.training_on);
        History_Bt = (Button)findViewById(R.id.training_history_bt);
        History_Bt.setVisibility(View.INVISIBLE);
        Help_Bt = (Button)findViewById(R.id.training_help_bt);
        PT_Language_Iv = (ImageView) findViewById(R.id.training_language_iv);
        PT_Name_Tv = (TextView) findViewById(R.id.training_name_tv);
        tf.setNanumSquareL(this, PT_Name_Tv);
        PT_Title_Tv = (TextView) findViewById(R.id.training_title_tv);
        tf.setNanumSquareEB(this, PT_Title_Tv);

        PT_DrawerLayout = (FullDrawerLayout) findViewById(R.id.drawer_layout);
        PT_Lv = (ListView)findViewById(R.id.training_lv);
        PT_Vv = (CustomSimpleExoPlayerView)findViewById(R.id.training_vv);
        PT_Vv.setUseController(false);
    }

    private void initEventListener() {
        DashBoard_Bt.setOnClickListener(this);
        Training_Bt.setOnClickListener(this);
        Help_Bt.setOnClickListener(this);
        PT_Lv.setOnItemClickListener(this);
    }

    private void initFullscreenDialog() {

        mFullScreenDialog = new Dialog(Act, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
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
        ((ViewGroup) PT_Vv.getParent()).removeView(PT_Vv);
        mFullScreenDialog.addContentView(PT_Vv, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(Act, R.mipmap.ic_fullscreen_skrink));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
        mFullScreenDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }


    private void closeFullscreenDialog() {
        ((ViewGroup) PT_Vv.getParent()).removeView(PT_Vv);
        ((FrameLayout) PT_DrawerLayout.findViewById(R.id.training_fl)).addView(PT_Vv);
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(Act, R.mipmap.ic_fullscreen_expand));
    }

    private void openSubtitles() {
        subtitleView.setVisibility(View.VISIBLE);
        mExoPlayerSubtitles = true;
        /*MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
        TrackGroupArray textGroups = mappedTrackInfo.getTrackGroups(TRACK_TEXT); // list of captions
        int groupIndex = 1; // index of desired caption track within the textGroups array

        trackSelector.setRendererDisabled(TRACK_TEXT, false);
        MappingTrackSelector.SelectionOverride override =
                new MappingTrackSelector.SelectionOverride(fixedFactory, groupIndex, 0);
        trackSelector.setSelectionOverride(TRACK_TEXT, textGroups, override);*/
    }

    private  void closeSubtitles() {
        subtitleView.setVisibility(View.INVISIBLE);
        mExoPlayerSubtitles = false;
    }

    private void initFullscreenButton() {
        controlView = (CustomPlayBackControlView) PT_Vv.findViewById(R.id.control);
        subtitleView = (SubtitleView) PT_Vv.findViewById(R.id.subtitles);
        tv = (TextView)controlView.findViewById(R.id.playback_rate_control);
        mFullScreenIcon = (ImageView)controlView.findViewById(R.id.exo_fullscreen_icon);
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

        /*mSubtitlesButton = (ImageButton)controlView.findViewById(R.id.subtitles);
        mSubtitlesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mExoPlayerSubtitles)
                    openSubtitles();
                else
                    closeSubtitles();
            }
        });*/

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.training_dashboard_bt :
                if(IsPlay)
                    new CommonUtil().setHideDialog(new TwoButtonPopUpDialog(this, this,"BACKDB","D",Mi));
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
            case R.id.training_history_bt :
                Bundle m = new Bundle();
                m.putParcelable("MemberInfo",Mi);
                intent = new Intent(this, HistoryMainAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("MemberInfo", m);
                intent.putExtra("Type", "G");
                startActivity(intent);//  go Intent
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                break;
            case R.id.training_help_bt :
                intent = new Intent(PersonalTrainingAct.this, GuideAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);//  go Intent
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_in);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Position = position;
        if(PT_Vv.getPlayer() != null)
            PT_Vv.getPlayer().release();

        Si.deleteExternalStoragePrivateFileAll(Act);
        SelectItems si = (SelectItems)parent.getItemAtPosition(position);
//        Log.e("PersonalTrainingAct", LOGIN_CARRIER+"/"+Position);
        ArrayList<SelectItems> MbList2 = Si.getPersonalTrainingRaw(Mi.getRank(), LOGIN_CARRIER);
        if("KR".equals(LOGIN_LANGUAGE)) {
            PT_Title_Tv.setText(si.getKr());
            VideoName = MbList2.get(position).getKr();
        }else if("ENG".equals(LOGIN_LANGUAGE)) {
            PT_Title_Tv.setText(si.getEng());
            VideoName = MbList2.get(position).getEng();
        }

        /*ImageView iv = (ImageView)parent.getChildAt(position).findViewById(R.id.trainings_row_iv);
        ImageView siv = (ImageView)parent.getChildAt(position).findViewById(R.id.trainings_row_square_iv);
        iv.setBackgroundResource(R.mipmap.personal_row_on);
        siv.setBackgroundResource(R.mipmap.trainings_check_on);
        for(int i=0; i < parent.getChildCount(); i++){
            if(i != Integer.valueOf(iv.getTag().toString()))	{
                ImageView piv = (ImageView)parent.getChildAt(i).findViewById(R.id.trainings_row_iv);
                ImageView psiv = (ImageView)parent.getChildAt(i).findViewById(R.id.trainings_row_square_iv);
                piv.setBackgroundResource(R.mipmap.personal_row_off);
                psiv.setBackgroundResource(R.mipmap.trainings_check_off);
            }
        }*/
        MbList.get(position).setChecked(true);
        for(int i=0; i < MbList.size(); i++){
            if(i != position) {
                MbList.get(i).setChecked(false);
            }
        }
        mAdapter.notifyDataSetChanged();

        PT_DrawerLayout.closeDrawer(PT_Lv);

        new FileLoadingPro(this).execute(VideoName,"mp4");

        Help_Bt.setVisibility(View.INVISIBLE);

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

    public static void MovePlay(File file){

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
            eDialog.setMessage("Loading FIle...");
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

            /*eDialog = new ProgressDialog(mContext);
            eDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            eDialog.setMessage("Loading..");
            eDialog.show();*/
        }

        @Override
        protected File doInBackground(String... str) {
            File readFile = null;
            SharedPreferences pref = mContext.getSharedPreferences("pref", Context.MODE_PRIVATE);
            String ZIP_PASSWORD = pref.getString("ZIP_PASSWORD", "");
            String PackageName = mContext.getPackageName();
            String fullFileName = str[0] + "." + str[1];

            File root = Environment.getExternalStorageDirectory();
            File expPath = new File(root.toString() + "/Android/obb/" + PackageName);
            String strMainPath = expPath + File.separator + str[0] +".obb";

            try{
//                Log.e("getZipFile","strMainPath : "+strMainPath);
                ZipFile zipFile = new ZipFile(strMainPath);
                zipFile.setRunInThread(true);

                if (zipFile.isEncrypted()) {
                    zipFile.setPassword(ZIP_PASSWORD);
                }
                progressMonitor = zipFile.getProgressMonitor();
                zipFile.extractFile(fullFileName, mContext.getExternalFilesDir(null).toString());

                /*while (progressMonitor.getPercentDone() < 1){

                }*/
                while (progressMonitor.getState() != ProgressMonitor.STATE_READY) {
                    eDialog.setProgress(progressMonitor.getPercentDone());
                }

                readFile = new File(mContext.getExternalFilesDir(null), fullFileName);
            } catch (ZipException e) {
                e.printStackTrace();
                Log.e("getZipFile", "ZipException : "+e + "/" + e.getCode());
                return readFile;
            } catch (Exception e) {
                Log.e("getZipFile", "Exception : "+e);
                return readFile;
            }
            return readFile;
        }

        @Override
        protected void onProgressUpdate(Integer... args) {
            Log.e("onProgressUpdate",args[0]+"");
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
                PT_Vv.setUseController(true);
                PT_Vv.requestFocus();
                PT_Vv.setPlayer(player);
                Uri videoUri = Uri.parse(result.getPath());

                DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mContext, Util.getUserAgent(mContext, getApplicationContext().getPackageName()));
                mVideoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(videoUri);
                player.prepare(mVideoSource);

                player.setPlayWhenReady(true); //run file/link when ready to play.

                PlaybackParams params = new PlaybackParams();
                params.setSpeed(1.0f);
                player.setPlaybackParams(params);

                player.addListener(new ComponentListener());

                tv.setText("x 1.0");
                CustomPlayBackControlView.current_playbackrate = 1.0f;
            }else{
                //Si.getToast(this, "It will be updated in the future.", Toast.LENGTH_SHORT);
            }
            eDialog.dismiss();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
//        eDialog.dismiss();
        }
    }

    class BufferingPro extends AsyncTask<Long, Integer, Long> {

        int per;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            eDialog = new ProgressDialog(PersonalTrainingAct.this);
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
        protected void onProgressUpdate(Integer... progress)
        {

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
//                Log.e(TAG, "STATE_BUFFERING END");
            }

            return position;
        }

        @Override
        protected void onPostExecute(Long result) {
            super.onPostExecute(result);


            eDialog.dismiss();
//            if(progressMonitor.getPercentDone() >= per)
             if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
                new CommonUtil().setHideNavigationBar(Act.getWindow().getDecorView());
            }
            initExoPlayer(result);
        }
    }

    private void initExoPlayer(Long position) {
//        Log.e(TAG, "initExoPlayer " + position + " / " + Float.valueOf(tv.getText().toString().substring(2)));
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        player = ExoPlayerFactory.newSimpleInstance(rf, trackSelector, new DefaultLoadControl());
        PT_Vv.setPlayer(player);
        PT_Vv.getPlayer().prepare(mVideoSource);

        PT_Vv.getPlayer().setPlayWhenReady(true);
        PT_Vv.getPlayer().seekTo(Math.max(0, position));
        PT_Vv.getPlayer().addListener(new ComponentListener());

        float speed = Float.valueOf(tv.getText().toString().substring(2));
        PlaybackParams params = new PlaybackParams();
        params.setSpeed(speed);
        PT_Vv.getPlayer().setPlaybackParams(params);

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
                if(IsPlay){
                    new CommonUtil().setHideDialog(new TrainingPopUpDialog(Act, Mi, Position));
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
        if (PT_Vv != null && PT_Vv.getPlayer() != null) {
            PT_Vv.getPlayer().release();
        }
        IsPlay = false;
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
        /*if (PT_Vv != null && PT_Vv.getPlayer() != null) {
            mResumeWindow = PT_Vv.getPlayer().getCurrentWindowIndex();
            mResumePosition = Math.max(0, PT_Vv.getPlayer().getCurrentPosition());

            PT_Vv.getPlayer().release();
        }

        if (mFullScreenDialog != null)
            mFullScreenDialog.dismiss();*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
         if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
            new CommonUtil().setHideNavigationBar(getWindow().getDecorView());
        }
    }

    @Override
    protected void onUserLeaveHint(){
        super.onUserLeaveHint();
        Log.e(TAG, "onUserLeaveHint");
        Si.deleteExternalStoragePrivateFileAll(this);

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
