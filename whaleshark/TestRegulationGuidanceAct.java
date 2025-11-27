package com.togetherseatech.whaleshark;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.togetherseatech.whaleshark.Db.History.TrainingDao;
import com.togetherseatech.whaleshark.Db.History.TrainingInfo;
import com.togetherseatech.whaleshark.Db.Member.MemberInfo;
import com.togetherseatech.whaleshark.Db.Problem.ProblemDao;
import com.togetherseatech.whaleshark.Db.Problem.ProblemInfo;
import com.togetherseatech.whaleshark.util.CommonUtil;
import com.togetherseatech.whaleshark.util.CustomPlayBackControlView;
import com.togetherseatech.whaleshark.util.CustomSimpleExoPlayerView;
import com.togetherseatech.whaleshark.util.SelectItems;
import com.togetherseatech.whaleshark.util.TextFontUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by seonghak on 2017. 11. 3..
 */

public class TestRegulationGuidanceAct extends Activity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private Button TRG_Next_Bt;
    private ImageView TRG_Language_Iv;
    private TextView TRG_Chapter_Tv;
    private TextView TRG_Name_Tv;
    public static TextView TRG_Time_Tv;
    public static TextView TRG_No_Tv;
    public static TextView TRG_Title_Tv;
//    public static TextView TRG_Level_Tv;
    public static RadioGroup TRG_Contents_Rg;
    public static RadioButton TRG_A, TRG_B, TRG_C, TRG_D, TRG_E, TRG_F;
    public static CustomSimpleExoPlayerView TRG_Vv;
    public static SimpleExoPlayer player;
    public static CustomPlayBackControlView controlView;
    public static View shutterView;
    public static Boolean shutter = false;

    private AudioManager audioManager;
    private int volume = 15;
    private static int PackageVer = 1;
    public static MediaPlayer mediaPlayer;
    private SoundPool mSoundPool;
    private int mSoundPoolID = 0;
    private Intent intent;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    public static MemberInfo Mi;
    public static TrainingInfo Ti;
    public static SelectItems Si;
    private static TextFontUtil tf;
    public static String LOGIN_LANGUAGE;
    private int LOGIN_CARRIER;
    private static String Voice;
    public static Timer T;
    public static Activity Act;
    public static Dialog mDialog;
    public static int count = 150;
    public static int problemCount = 0;
    public static List<ProblemInfo> PiList, SPiList;
    public static ArrayList<String> AnswerScoreList;
    public static ArrayList<Integer> AnswerTimeList;
    public static String Answer = null;
    public static ProblemDao PDao;
    public static boolean delict = false;
    public static boolean play = false;
    public static File readFile;
    public static final String SYSTEM_DIALOG_REASON_KEY = "reason";
    public static final String SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions";
    public static final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
    public static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 화면캡쳐 방지
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.test_regulation_guidance_act);

        pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
        LOGIN_CARRIER = pref.getInt("LOGIN_CARRIER", 0);
        LOGIN_LANGUAGE = pref.getString("LOGIN_LANGUAGE", "");
        /**수정추가**/
        Bundle M = getIntent().getBundleExtra("MemberInfo");
        Mi = M.getParcelable("MemberInfo");
//        Mi = (MemberInfo)getIntent().getExtras().get("MemberInfo");
        Ti = (TrainingInfo)getIntent().getExtras().get("TrainingInfo");
        Si = new SelectItems();
        tf = new TextFontUtil();
        AnswerScoreList = new ArrayList<>();
        AnswerTimeList = new ArrayList<>();
        Act = this;
        PackageManager manager = getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        PackageVer = info.versionCode;
        IntentFilter filter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(mBroadcastReceiver, filter);

        initSetting();
        initEventListener();

        PDao = new ProblemDao(this);
//        PiList = PDao.getRankProblem(Mi.getRank(), Ti.getTraining_course());
        PiList = PDao.getRankProblem(Mi.getRank(), Ti.getTraining_course(), Si.getProblemVslType(Mi.getVsl_type()));
        Log.e("test","TestRegulationGuidance Act :" + Mi.getRank());

        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

//        MediaController mediaController = new MediaController(Act);
//        mediaController.setAnchorView(TRG_Vv);
//        TRG_Vv.setMediaController(mediaController);

        TRG_Chapter_Tv.setText(Si.getChapter(Ti.getTraining_course(),LOGIN_LANGUAGE));
        if("KR".equals(LOGIN_LANGUAGE)) {
            TRG_Language_Iv.setImageResource(R.mipmap.language_kr);
            TRG_Language_Iv.setTag(R.mipmap.language_kr);
        }else if("ENG".equals(LOGIN_LANGUAGE)) {
            TRG_Language_Iv.setImageResource(R.mipmap.language_eng);
            TRG_Language_Iv.setTag(R.mipmap.language_eng);
        }

        TRG_Name_Tv.setText(Mi.getSurName() + " " + Mi.getFirstName());
        if(PiList.size() > 0)
            setPageData();
    }

    private void initSetting() {
        TRG_Chapter_Tv = (TextView) findViewById(R.id.test_chapter_tv);
        tf.setNanumSquareEB(this, TRG_Chapter_Tv);
        TRG_Time_Tv = (TextView) findViewById(R.id.test_time_tv);
        tf.setTimefont(this, TRG_Time_Tv);
        TRG_No_Tv = (TextView) findViewById(R.id.test_no_tv);
        tf.setNanumSquareRoundL(this, TRG_No_Tv);
//        TRG_No_Tv.setText("Test_01");
        TRG_Name_Tv = (TextView)findViewById(R.id.test_name_tv);
        tf.setNanumSquareL(this, TRG_Name_Tv);
        TRG_Language_Iv = (ImageView) findViewById(R.id.test_language_iv);
//        TRG_Level_Tv = (TextView) findViewById(R.id.test_level_tv);
//        tf.setNanumSquareL(this, TRG_Level_Tv);
        TRG_Title_Tv = (TextView) findViewById(R.id.test_title_tv);
        tf.setNanumSquareL(this, TRG_Title_Tv);
        TRG_Contents_Rg = (RadioGroup) findViewById(R.id.test_con_rg);

        TRG_A = (RadioButton)findViewById(R.id.rb_a);
        tf.setNanumSquareL(this, TRG_A);
        TRG_B = (RadioButton)findViewById(R.id.rb_b);
        tf.setNanumSquareL(this, TRG_B);
        TRG_C = (RadioButton)findViewById(R.id.rb_c);
        tf.setNanumSquareL(this, TRG_C);
        TRG_D = (RadioButton)findViewById(R.id.rb_d);
        tf.setNanumSquareL(this, TRG_D);
        TRG_E = (RadioButton)findViewById(R.id.rb_e);
        tf.setNanumSquareL(this, TRG_E);
        TRG_F = (RadioButton)findViewById(R.id.rb_f);
        tf.setNanumSquareL(this, TRG_F);

        TRG_Next_Bt = (Button)findViewById(R.id.test_next_bt);
        TRG_Vv = (CustomSimpleExoPlayerView)findViewById(R.id.test_vv);
        TRG_Vv.setUseController(false);

        controlView = (CustomPlayBackControlView) TRG_Vv.findViewById(R.id.control);
        TextView tv = (TextView)controlView.findViewById(R.id.playback_rate_control);
        tv.setVisibility(View.INVISIBLE);
        ImageButton mMinus = (ImageButton)controlView.findViewById(R.id.minus);
        mMinus.setVisibility(View.INVISIBLE);
        ImageButton mPlus = (ImageButton)controlView.findViewById(R.id.plus);
        mPlus.setVisibility(View.INVISIBLE);
        FrameLayout mFullScreenButton = (FrameLayout) controlView.findViewById(R.id.exo_fullscreen_button);
        mFullScreenButton.setVisibility(View.INVISIBLE);

        ImageButton mSubtitlesButton = (ImageButton)controlView.findViewById(R.id.subtitle);
        mSubtitlesButton.setVisibility(View.INVISIBLE);
        shutterView = (View)TRG_Vv.findViewById(R.id.shutter);
        shutterView.setBackgroundResource(R.mipmap.standby);

    }

    private void initEventListener() {
//        TRG_Language_Iv.setOnClickListener(this);
        TRG_Next_Bt.setOnClickListener(this);
    }

    public static void reset(){
        TRG_Contents_Rg.clearCheck();

        if(T != null) {
            T.cancel();
            T = null;
        }
        play = false;
        if(player != null)
            player = null;
        controlView.hide();

//        TRG_Vv.setBackgroundColor(Color.parseColor("#757575"));
        TRG_Vv.setBackgroundResource(R.mipmap.standby);
        Answer = null;
        mDialog = null;
        count = 150;
        TRG_Time_Tv.setText("02:30");

        TRG_A.setVisibility(View.VISIBLE);
        TRG_B.setVisibility(View.VISIBLE);
        TRG_C.setVisibility(View.VISIBLE);
        TRG_D.setVisibility(View.VISIBLE);
        TRG_E.setVisibility(View.VISIBLE);
        TRG_F.setVisibility(View.VISIBLE);

        shutterView.setVisibility(View.VISIBLE);
        shutter = false;
    }

    public static void setPageData(){

        reset();

        TimeChack();

        TRG_No_Tv.setText("Ouestion "+(problemCount+1)+ " of "+ PiList.size());
//        Log.e("setPageData", "IDX : "+ PiList.get(problemCount).getIdx());
        SPiList = PDao.getSubProblem(PiList.get(problemCount));
//        Log.e("setPageData", "SPiList size : "+ SPiList.size());

        setProblemData(LOGIN_LANGUAGE);

        if("KR".equals(LOGIN_LANGUAGE)) {
            Voice = PiList.size() > 0 ? PiList.get(problemCount).getVoice_kr() : "";
        }else if("ENG".equals(LOGIN_LANGUAGE)) {
            Voice = PiList.size() > 0 ? PiList.get(problemCount).getVoice_eng() : "";
        }

//        Log.e("Voice",Voice);
//        Log.e("Flash_video",""+PiList.get(problemCount).getFlash_video());


        new FileLoadingPro(Act).execute();
//        if(Voice != null && !"".equals(Voice))
//            startMpPlay(Act, Si.getRaw(Voice));

    }

    public static void setProblemData(String lang){
//        String level = "[" + Si.getLevel(PiList.get(problemCount).getLevel(), LOGIN_LANGUAGE) + "/" + Si.getChapter(PiList.get(problemCount).getChapter(), LOGIN_LANGUAGE) + "]";
//        TRG_Level_Tv.setText(level);
        if("KR".equals(lang)) {
            TRG_Title_Tv.setText(PiList.get(problemCount).getTitle_kr());
            if(SPiList.size() == 2){
                TRG_A.setText(SPiList.get(0).getContent_kr());
                TRG_B.setText(SPiList.get(1).getContent_kr());
                TRG_C.setVisibility(View.GONE);
                TRG_D.setVisibility(View.GONE);
                TRG_E.setVisibility(View.GONE);
                TRG_F.setVisibility(View.GONE);
            }else if(SPiList.size() == 3){
                TRG_A.setText(SPiList.get(0).getContent_kr());
                TRG_B.setText(SPiList.get(1).getContent_kr());
                TRG_C.setText(SPiList.get(2).getContent_kr());
                TRG_D.setVisibility(View.GONE);
                TRG_E.setVisibility(View.GONE);
                TRG_F.setVisibility(View.GONE);
            }else if(SPiList.size() == 4){
                TRG_A.setText(SPiList.get(0).getContent_kr());
                TRG_B.setText(SPiList.get(1).getContent_kr());
                TRG_C.setText(SPiList.get(2).getContent_kr());
                TRG_D.setText(SPiList.get(3).getContent_kr());
                TRG_E.setVisibility(View.GONE);
                TRG_F.setVisibility(View.GONE);
            }else if(SPiList.size() == 5){
                TRG_A.setText(SPiList.get(0).getContent_kr());
                TRG_B.setText(SPiList.get(1).getContent_kr());
                TRG_C.setText(SPiList.get(2).getContent_kr());
                TRG_D.setText(SPiList.get(3).getContent_kr());
                TRG_E.setText(SPiList.get(4).getContent_kr());
                TRG_F.setVisibility(View.GONE);
            }else if(SPiList.size() == 6){
                TRG_A.setText(SPiList.get(0).getContent_kr());
                TRG_B.setText(SPiList.get(1).getContent_kr());
                TRG_C.setText(SPiList.get(2).getContent_kr());
                TRG_D.setText(SPiList.get(3).getContent_kr());
                TRG_E.setText(SPiList.get(4).getContent_kr());
                TRG_F.setText(SPiList.get(5).getContent_kr());
            }
        }else if("ENG".equals(lang)) {
            TRG_Title_Tv.setText(PiList.get(problemCount).getTitle_eng());
            if(SPiList.size() == 2){
                TRG_A.setText(SPiList.get(0).getContent_eng());
                TRG_B.setText(SPiList.get(1).getContent_eng());
                TRG_C.setVisibility(View.GONE);
                TRG_D.setVisibility(View.GONE);
                TRG_E.setVisibility(View.GONE);
                TRG_F.setVisibility(View.GONE);
            }else if(SPiList.size() == 3){
                TRG_A.setText(SPiList.get(0).getContent_eng());
                TRG_B.setText(SPiList.get(1).getContent_eng());
                TRG_C.setText(SPiList.get(2).getContent_eng());
                TRG_D.setVisibility(View.GONE);
                TRG_E.setVisibility(View.GONE);
                TRG_F.setVisibility(View.GONE);
            }else if(SPiList.size() == 4){
                TRG_A.setText(SPiList.get(0).getContent_eng());
                TRG_B.setText(SPiList.get(1).getContent_eng());
                TRG_C.setText(SPiList.get(2).getContent_eng());
                TRG_D.setText(SPiList.get(3).getContent_eng());
                TRG_E.setVisibility(View.GONE);
                TRG_F.setVisibility(View.GONE);
            }else if(SPiList.size() == 5){
                TRG_A.setText(SPiList.get(0).getContent_eng());
                TRG_B.setText(SPiList.get(1).getContent_eng());
                TRG_C.setText(SPiList.get(2).getContent_eng());
                TRG_D.setText(SPiList.get(3).getContent_eng());
                TRG_E.setText(SPiList.get(4).getContent_eng());
                TRG_F.setVisibility(View.GONE);

            }else if(SPiList.size() == 6){
                TRG_A.setText(SPiList.get(0).getContent_eng());
                TRG_B.setText(SPiList.get(1).getContent_eng());
                TRG_C.setText(SPiList.get(2).getContent_eng());
                TRG_D.setText(SPiList.get(3).getContent_eng());
                TRG_E.setText(SPiList.get(4).getContent_eng());
                TRG_F.setText(SPiList.get(5).getContent_eng());
            }
        }
    }
    public static void startMpPlay(Context context, File readFile){
//        mediaPlayer = new MediaPlayer();
//        mediaPlayer = MediaPlayer.create(context, raw);
        mediaPlayer = MediaPlayer.create(context, Uri.parse(readFile.getPath()));
//            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                play = true;
//                Log.e("startMpPlay", PiList.get(problemCount).getFlash_video());
                if(PiList.get(problemCount).getFlash_video() != null && !"".equals(PiList.get(problemCount).getFlash_video().trim())) {
                     if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
                        new CommonUtil().setHideNavigationBar(Act.getWindow().getDecorView());
                    }
                    TRG_Vv.getPlayer().setPlayWhenReady(true);
                    shutterView.setVisibility(View.GONE);
                }
            }
        });

        mediaPlayer.start();

    }

    public static void MovePlay(File readFile){

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        player = ExoPlayerFactory.newSimpleInstance(Act, trackSelector, new DefaultLoadControl());
        TRG_Vv.setUseController(true);
        TRG_Vv.requestFocus();
        TRG_Vv.setPlayer(player);
//        TRG_Vv.getVideoSurfaceView().setBackgroundResource(R.mipmap.standby);

        Uri videoUri = Uri.parse(readFile.getPath());

        //Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter bandwidthMeterA = new DefaultBandwidthMeter();
        //Produces DataSource instances through which media data is loaded.
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(Act, Util.getUserAgent(Act, Act.getApplicationContext().getPackageName()), bandwidthMeterA);
        //Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        MediaSource mVideoSource = new ExtractorMediaSource(videoUri, dataSourceFactory, extractorsFactory, null, null);
        player.prepare(mVideoSource);
//        player.setPlayWhenReady(true);
        player.addListener(new ExoPlayer.EventListener() {
            @Override
            public void onLoadingChanged(boolean isLoading) {
//                        Log.e(TAG, "onLoadingChanged");
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
//                        Log.e(TAG, "onPlayerStateChanged : "+ playbackState + "/" +playWhenReady);

                if(playbackState == Player.STATE_READY) {
                    if(!shutter) {
                        shutterView.setVisibility(View.VISIBLE);
                        shutter = true;
                    }
                }

                if(playbackState == Player.STATE_BUFFERING) {

                }

                if(playbackState == Player.STATE_ENDED) {
//                    controlView.hide();
                    shutterView.setVisibility(View.GONE);
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
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }

        });
    }

    public static void TimeChack(){
        T = new Timer();
        T.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Act.runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if(count != 0){
                            if(count < 150 && count >= 120 )
                                TRG_Time_Tv.setText("02:"+ (count-120 < 10 ? "0"+(count-120) :(count-120)));
                            else if(count < 120 && count >= 60 )
                                TRG_Time_Tv.setText("01:"+ (count-60 < 10 ? "0"+(count-60) :(count-60)));
                            else if( count < 60)
                                TRG_Time_Tv.setText("00:"+ (count < 10 ? "0"+count : count));
                            count--;
                        }else{

                            TRG_Time_Tv.setText("00:00");
//                            TRG_Vv.pause();
                            if(mDialog != null)
                                mDialog.dismiss();
                            if(Answer == null)
                                AnswerScoreList.add("N");
                            else if(Answer.equals(PiList.get(problemCount).getAnswer()))
                                AnswerScoreList.add("Y");
                            else
                                AnswerScoreList.add("N");

                            AnswerTimeList.add(150 - count);
                            Si.deleteExternalStoragePrivateFileAll(Act);
                            if(problemCount+1 == PiList.size()) {
                                T.cancel();
                                player.stop();
                                new setDataPro().execute();
                            }else{
                                problemCount += 1;
//                                Log.e("TRGAct","This Time Over Answer :"+ problemCount);
//                                Log.e("TRGAct","This Time Over Answer :"+ PiList.size());
                                setPageData();
                            }
                        }

                    }
                });
            }
        }, 1000, 1000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.test_next_bt :
//                Log.e("TRGAct", "Answer : " + PiList.get(problemCount).getAnswer());
                int checkedRadioButtonId = TRG_Contents_Rg.getCheckedRadioButtonId();
//                Log.e("TRGAct", "checkedRadioButtonId : " + checkedRadioButtonId);
                if (checkedRadioButtonId == -1) {
//                    Answer = "A";
                    Si.getMassage("NEXT",LOGIN_LANGUAGE,getApplicationContext(),Toast.LENGTH_SHORT);
                    return;
                } else {
                    switch (checkedRadioButtonId) {
                        case R.id.rb_a:
                            Answer = "A";
                            break;
                        case R.id.rb_b:
                            Answer = "B";
                            break;
                        case R.id.rb_c:
                            Answer = "C";
                            break;
                        case R.id.rb_d:
                            Answer = "D";
                            break;
                        case R.id.rb_e:
                            Answer = "E";
                            break;
                    }

                }
//                Log.e("TRGAct", "Answer : " + Answer);
                if(player != null)
                    player = null;

                if(!play) {
                    if(mediaPlayer != null)
                        mediaPlayer.stop();
                }
                Si.deleteExternalStoragePrivateFileAll(Act);

                if(Answer == null)
                    AnswerScoreList.add("N");
                else if(Answer.equals(PiList.get(problemCount).getAnswer() != null ? PiList.get(problemCount).getAnswer() : ""))
                    AnswerScoreList.add("Y");
                else
                    AnswerScoreList.add("N");

                AnswerTimeList.add(150 - count);

                if(problemCount+1 == PiList.size()) {
                    T.cancel();
                    new setDataPro().execute();
                }else {
//                    mDialog = new TwoButtonPopUpDialog(this, "NEXT");
//                    mDialog.show();
                    problemCount += 1;
//                    Log.e("TRGAct","problemCount : "+TestRegulationGuidanceAct.problemCount);
                    setPageData();
                }
                break;
            case R.id.test_language_iv :
                int lang = Integer.valueOf(TRG_Language_Iv.getTag().toString());
                editor = pref.edit();
                if(lang == R.mipmap.language_kr){
                    TRG_Language_Iv.setImageResource(R.mipmap.language_eng);
                    TRG_Language_Iv.setTag(R.mipmap.language_eng);
                    LOGIN_LANGUAGE = "ENG";
                    setProblemData(LOGIN_LANGUAGE);
                }else if(lang == R.mipmap.language_eng){

                    TRG_Language_Iv.setImageResource(R.mipmap.language_kr);
                    TRG_Language_Iv.setTag(R.mipmap.language_kr);
                    LOGIN_LANGUAGE = "KR";
                    setProblemData(LOGIN_LANGUAGE);
                }
                editor.putString("LOGIN_LANGUAGE", LOGIN_LANGUAGE);
                editor.commit();
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.rb_a :
                Answer = "A";
                break;
            case R.id.rb_b :
                Answer = "B";
                break;
            case R.id.rb_c :
                Answer = "C";
                break;
            case R.id.rb_d :
                Answer = "D";
                break;
            case R.id.rb_e :
                Answer = "E";
                break;
        }
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            Log.e("BroadcastReceiver",">>> Clcik Event");
            String action = intent.getAction();
//            Log.e("BroadcastReceiver",">>> Event" + action);
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
//                Log.e("BroadcastReceiver",">>> "+reason);
                if (reason != null) {
                    if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
//                        Log.e("BroadcastReceiver",">>> Home Clcik Event");
                        delict = true;
                        Si.deleteExternalStoragePrivateFileAll(Act);
                    } else if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
//                        Log.e("BroadcastReceiver",">>> Home Long Press Event");
                        delict = true;
                        Si.deleteExternalStoragePrivateFileAll(Act);
                    } else if (reason.equals(SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS)) {
//                        Log.e("BroadcastReceiver", ">>> Home power Event");
                        delict = true;
                        Si.deleteExternalStoragePrivateFileAll(Act);
                    }
                }
            }
        }
    };


    static class FileLoadingPro extends AsyncTask<String, Integer, File> {

        private String line = null;
        private ProgressDialog eDialog;
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
            eDialog.setMessage("Loading...");
            eDialog.setIndeterminate(true);
            eDialog.setCancelable(false);
            eDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
             if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
                new CommonUtil().setHideNavigationBar(eDialog.getWindow().getDecorView());
            }
            eDialog.show();
            eDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        }

        @Override
        protected File doInBackground(String... str) {

            return null;
        }

        @Override
        protected void onPostExecute(File result) {
            super.onPostExecute(result);

            if(PiList.get(problemCount).getFlash_video() != null && !"".equals(PiList.get(problemCount).getFlash_video().trim())) {
                readFile = Si.getZipFile(Act, PiList.get(problemCount).getFlash_video(),"mp4");
                if(readFile != null)
                    MovePlay(readFile);
//            else
//                Si.getToast(Act, "There is no version "+ PackageVer +" media. Please download or copy to the specified folder.", Toast.LENGTH_SHORT);
            }else{
//                TRG_Vv.setBackgroundResource(R.mipmap.standby);
            }

            readFile = Si.getZipFile(Act,Voice,"mp3");
//        File readFile = Si.createExternalStoragePrivateFile(Act,Voice+".mp3");
            if(readFile != null)
                startMpPlay(Act, readFile);
            else{
//                Si.getToast(Act, "There is no version "+ PackageVer +" media. Please download or copy to the specified folder.", Toast.LENGTH_SHORT);
                Si.getToast(Act, "It will be updated in the future.", Toast.LENGTH_SHORT);
            }
            eDialog.dismiss();

             if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
                new CommonUtil().setHideNavigationBar(Act.getWindow().getDecorView());
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
//        eDialog.dismiss();
        }
    }

    // ***************************************************************************************** //
    // AsyncTask setDataPro Class
    // ***************************************************************************************** //
    public static class setDataPro extends AsyncTask<String, Integer, String> {

        String line = null;
        @Override
        protected String doInBackground(String... arg) {
//			System.out.println("doInBackground");
//            Log.e("setDataPro","doInBackground");
            int sumTime = 0;
            int sumScore = 0;
            for(Integer time : AnswerTimeList){
//                Log.e("TRGAct","AnswerTimeList : "+ time);
                sumTime += time;
            }

//            Log.e("TRGAct","sumTime : "+ sumTime);
            TrainingDao TDao = new TrainingDao(Act);
            if(Ti.getDate() != null)
                Ti.setIdx(TDao.getHistoryIdx());

            List<TrainingInfo> HistoryProblems = new ArrayList<>();
            for(int i=0; i < AnswerScoreList.size();i++){
//                Log.e("setDataPro","AnswerScoreList : "+ AnswerScoreList.get(i));

                if("N".equals(AnswerScoreList.get(i))) {
//                    Log.e("setDataPro","setHistroy_idx : "+ Ti.getIdx());
//                    Log.e("setDataPro","setProblem_idx : "+ PiList.get(i).getIdx());
//                    Log.e("setDataPro","setAnswer_yn : "+ PiList.get(i).getRelative_regulation());
//                    Log.e("setDataPro","///////////////////////");

                    HistoryProblems.add(new TrainingInfo(Ti.getIdx(), PiList.get(i).getIdx(), PiList.get(i).getRelative_regulation().trim()));
                }else
                    sumScore += 1;
            }
//            Log.e("setDataPro","sumScore : "+ sumScore);
            int Score = Math.round((float)sumScore / (float)AnswerScoreList.size() * 100);
//            Log.e("setDataPro","Score1 : "+ Math.round((float)sumScore / (float)AnswerScoreList.size() * 100));
//            Log.e("setDataPro","Score2 : "+ Score);
//            Log.e("TestAct","Ti.getDate() null: " + Ti.getDate() == null ? "true" : "false");
            Ti.setTime(sumTime);
            Ti.setScore(Score);
            if(Ti.getDate() == null) {
                TDao.updateHistory(Ti);
            } else {
                Ti.setYear(Si.getYear());
                Ti.setQuarter(Si.getQuarter());
                Ti.setDate(Si.getDateTime());

                Ti.setSubmit("N");
                TDao.addHistory(Ti);
            }

//            int result = TDao.updateHistory(Ti);
//            Log.e("setDataPro","result : "+ result);
            TDao.addHistoryProblems(HistoryProblems);
            Bundle m = new Bundle();
            m.putParcelable("MemberInfo",Mi);
            Intent intent = new Intent(Act, ReportCardAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("MemberInfo", m);
            intent.putExtra("TrainingInfo", Ti);
            intent.putExtra("Type", "TRG");
            Act.startActivity(intent);//  go Intent
            Act.finish();
            Act.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);

            return line;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result != null){

            }else{

            }
             if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
                new CommonUtil().setHideNavigationBar(Act.getWindow().getDecorView());
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
    // ***************************************************************************************** //
    // AsyncTask setDataPro Class End
    // ***************************************************************************************** //

    // back키 동작 금지
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            event.startTracking();
//            TRG_Vv.pause();
//            T.cancel();
            mDialog = new TwoButtonPopUpDialog(this, Mi, "END", Act );
            mDialog.show();
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

    @Override
    protected void onStart(){
        super.onStart();
        Log.e("TRGAct", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("TRGAct", "onResume");
        delict = false;
        if(play) {
            if (PiList.get(problemCount).getFlash_video() != null && !"".equals(PiList.get(problemCount).getFlash_video().trim())) {
                readFile = Si.getZipFile(Act,PiList.get(problemCount).getFlash_video(),"mp4");
                if (readFile != null)
                    MovePlay(readFile);
//            else
//                Si.getToast(Act, "There is no version "+ PackageVer +" media. Please download or copy to the specified folder.", Toast.LENGTH_SHORT);
            } else {
//                TRG_Vv.setBackgroundResource(R.mipmap.standby);
            }
        }
         if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
            new CommonUtil().setHideNavigationBar(getWindow().getDecorView());
        }
    }

    @Override
    protected void onUserLeaveHint(){
        super.onUserLeaveHint();
        Log.e("TRGAct", "onUserLeaveHint");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.e("TRGAct", "onWindowFocusChanged : " + hasFocus);
         if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
            new CommonUtil().setHideNavigationBar(getWindow().getDecorView());
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.e("TRGAct", "onPause");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("TRGAct", "onStop");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
//        Log.e("TRGAct", "onDestroy delict : "+delict);
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        if (TRG_Vv != null && TRG_Vv.getPlayer() != null) {
            TRG_Vv.getPlayer().release();
        }
        if(delict){
            T.cancel();
            AnswerScoreList.clear();
            AnswerTimeList.clear();
            new TRGBgService.setDelictPro(this, Ti).execute();
        }else{
            try {
                unregisterReceiver(mBroadcastReceiver);
            } catch (IllegalArgumentException e){
                e.getStackTrace();
            } catch (Exception e) {
                e.getStackTrace();
            }finally {
                problemCount = 0;
                AnswerScoreList.clear();
                AnswerTimeList.clear();
            }
        }

    }
}
