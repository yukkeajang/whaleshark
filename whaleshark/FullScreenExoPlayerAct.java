package com.togetherseatech.whaleshark;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
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
import com.togetherseatech.whaleshark.Db.Member.MemberInfo;
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
import java.util.Locale;

/**
 * Created by seonghak on 2018. 05. 23..
 */

public class FullScreenExoPlayerAct extends Activity {

    private static final String TAG = "FullScreenExoPlayerAct";
    private Button FS_Back_Bt;
    private TextView FS_Title_Tv;
    private SelectItems Si;
    private TextFontUtil tf;
    private TrainingsInfo Tsi;
    private String FlashVideo;
    private CustomSimpleExoPlayerView FS_Vv;
    private CustomPlayBackControlView controlView;
    private SimpleExoPlayer player;
    private Boolean IsPlay = false;
    private AudioManager audioManager;
    private int volume = 15;

    private final String STATE_RESUME_WINDOW = "resumeWindow";
    private final String STATE_RESUME_POSITION = "resumePosition";
    private final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";

    private ImageButton mSubtitlesButton;
    private boolean mExoPlayerFullscreen = false,  mExoPlayerSubtitles = false;;
    private FrameLayout mFullScreenButton;
    private ImageView mFullScreenIcon;
    private ImageView mSubTitleIcon;
    private Dialog mFullScreenDialog;
    private SubtitleView subtitleView;
    private int mResumeWindow;
    private long mResumePosition;
    private TextView tv;
    private ProgressMonitor progressMonitor;
    private MediaSource mVideoSource;
    private MergingMediaSource mergedSource;
    private DrmSessionManager<FrameworkMediaCrypto> drmSessionManager;
    private PlaybackParameters playbackParameters;
    private ProgressDialog eDialog;
    private File subTitleFile;
    private DefaultRenderersFactory rf;
    private WindowManager.LayoutParams lp;
    private MemberInfo Mi;
    private Activity Act;
    private  int Rpos = 0;
    private Bundle M;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscreen_exoplayer);

        lp = getWindow().getAttributes();
//        lp.screenBrightness = (float) 125/255;
        lp.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;		//화면이 켜진상태 유지
        getWindow().setAttributes(lp);
        Act = this;
        Si = new SelectItems();
        tf = new TextFontUtil();
        M = new Bundle();
        M = getIntent().getBundleExtra("MemberInfo");
        Mi = M.getParcelable("MemberInfo");
        //Mi = (MemberInfo)getIntent().getExtras().get("MemberInfo");
        FlashVideo =  getIntent().getStringExtra("Flash_video");
        Tsi = (TrainingsInfo)getIntent().getExtras().get("TrainingsInfo");
        Rpos = getIntent().getIntExtra("pos",Rpos);
        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        if (savedInstanceState != null) {
            mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
            mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
            mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
        }

        initSetting();
        initEventListener();

        new FileLoadingPro(this).execute(FlashVideo,"mp4");
    }

    private void initSetting() {
        FS_Back_Bt = (Button)findViewById(R.id.full_back_bt);
        FS_Title_Tv = (TextView) findViewById(R.id.full_title_tv);
        tf.setNanumSquareEB(this, FS_Title_Tv);
        FS_Title_Tv.setText(Tsi.getAgreement());
        FS_Vv = (CustomSimpleExoPlayerView)findViewById(R.id.full_vv);
        FS_Vv.setUseController(false);
    }

    private void initEventListener() {

        FS_Back_Bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CommonUtil().setHideDialog(new TwoButtonPopUpDialog(FullScreenExoPlayerAct.this, FullScreenExoPlayerAct.this,"BACKDB","N",Mi));
            }
        });

    }

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
        ((ViewGroup) FS_Vv.getParent()).removeView(FS_Vv);
        mFullScreenDialog.addContentView(FS_Vv, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.ic_fullscreen_skrink));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
        mFullScreenDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }


    private void closeFullscreenDialog() {
        ((ViewGroup) FS_Vv.getParent()).removeView(FS_Vv);
        ((FrameLayout) findViewById(R.id.full_fl)).addView(FS_Vv);
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.ic_fullscreen_expand));
    }

    private void openSubtitles() {
        subtitleView.setVisibility(View.VISIBLE);
        mExoPlayerSubtitles = true;
        mSubtitlesButton.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.ic_subtitle_on));
    }

    private  void closeSubtitles() {
        subtitleView.setVisibility(View.INVISIBLE);
        mExoPlayerSubtitles = false;
        mSubtitlesButton.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.ic_subtitle_off));
    }

    private void initFullscreenButton() {
        controlView = (CustomPlayBackControlView) FS_Vv.findViewById(R.id.control);
        tv = (TextView)controlView.findViewById(R.id.playback_rate_control);
        mFullScreenIcon = (ImageView)controlView.findViewById(R.id.exo_fullscreen_icon);
        subtitleView = (SubtitleView) FS_Vv.findViewById(R.id.subtitles);
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

    class FileLoadingPro extends AsyncTask<String, Integer, File> {

        private String line = null;
        private Context mContext;
        private SelectItems Si;
        private File subTitleFile;

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

            subTitleFile = Si.getSubtitleFile(mContext,str[0]+".srt");
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
                FS_Vv.setUseController(true);
                FS_Vv.requestFocus();
                FS_Vv.setPlayer(player);
                Uri videoUri = Uri.parse(result.getPath());

                DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mContext, Util.getUserAgent(mContext, getApplicationContext().getPackageName()));
                mVideoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(videoUri);
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

                player.setPlayWhenReady(true); //run file/link when ready to play.

                PlaybackParams params = new PlaybackParams();
                params.setSpeed(1.0f);
                player.setPlaybackParams(params);

                player.addListener(new ComponentListener());

                tv.setText("x 1.0");
                CustomPlayBackControlView.current_playbackrate = 1.0f;
            }else{
                Si.getToast(mContext, "It will be updated in the future.", Toast.LENGTH_SHORT);
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
            eDialog = new ProgressDialog(FullScreenExoPlayerAct.this);
            eDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            eDialog.setMessage("Loading..");
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

    private void initExoPlayer(Long position) {
//        Log.e(TAG, "initExoPlayer " + position + " / " + Float.valueOf(tv.getText().toString().substring(2)));
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        player = ExoPlayerFactory.newSimpleInstance(rf, trackSelector, new DefaultLoadControl());
        FS_Vv.setPlayer(player);
        FS_Vv.getPlayer().prepare(mVideoSource);

//        GT_Vv.getPlayer().prepare(mVideoSource);

        FS_Vv.getPlayer().setPlayWhenReady(true);
        FS_Vv.getPlayer().seekTo(Math.max(0, position));
        FS_Vv.getPlayer().addListener(new ComponentListener());

        float speed = Float.valueOf(tv.getText().toString().substring(2));
        PlaybackParams params = new PlaybackParams();
        params.setSpeed(speed);
        FS_Vv.getPlayer().setPlaybackParams(params);

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
                Log.e(TAG, "STATE_READY" + "");
                IsPlay = true;
            }

            if(playbackState == Player.STATE_BUFFERING){
                Log.e(TAG, "STATE_BUFFERING" + "");

                /*if(IsPlay) {
                    player.stop();
                    new BufferingPro().execute();
                }*/
            }

            if(playbackState == Player.STATE_ENDED){
//                            Si.deleteExternalStoragePrivateFile(mContext,VideoName+".mp4");
                if(IsPlay){
                    Si.deleteExternalStoragePrivateFile(FullScreenExoPlayerAct.this,FlashVideo+".mp4");
                    NewRegulationAct.Act.finish();
                    Bundle m = new Bundle();
                    m.putParcelable("MemberInfo",Mi);
                    Intent intent = new Intent(FullScreenExoPlayerAct.this, AttendanceAct.class); // new Intent(현재보여지는액티비티, 불러올 액티비티)
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("MemberInfo", m);
                    intent.putExtra("TrainingsInfo", Tsi);
                    intent.putExtra("Type", "R");
                    intent.putExtra("pos",Rpos);
                    startActivity(intent);//  go Intent
                    finish();
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
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
        if (FS_Vv != null && FS_Vv.getPlayer() != null) {
            FS_Vv.getPlayer().release();
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
        /*if (FS_Vv != null && FS_Vv.getPlayer() != null) {
            mResumeWindow = FS_Vv.getPlayer().getCurrentWindowIndex();
            mResumePosition = Math.max(0, FS_Vv.getPlayer().getCurrentPosition());

            FS_Vv.getPlayer().release();
        }

        if (mFullScreenDialog != null)
            mFullScreenDialog.dismiss();*/
    }

    @Override
    protected void onResume() {
        super.onResume();
         if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
            new CommonUtil().setHideNavigationBar(getWindow().getDecorView());
        }
    }

    @Override
    protected void onUserLeaveHint(){
        super.onUserLeaveHint();
        Log.e(TAG, "onUserLeaveHint");
        Si.deleteExternalStoragePrivateFileAll(this);
        finish();
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
