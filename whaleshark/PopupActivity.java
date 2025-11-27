package com.togetherseatech.whaleshark;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.togetherseatech.whaleshark.util.CommonUtil;
import com.togetherseatech.whaleshark.util.TextFontUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * PopupActivity Activity
 * : Service 에서 AlertDialog뛰우기 위한 Activity
 */
public class PopupActivity extends Activity implements View.OnClickListener {

	private Button PopUpCanCel_Bt, PopUpOk_Bt;
	private TextView PopUpTitle_tv, PopUpCon_tv;
	private String LOGIN_LANGUAGE;
	private String UPDATE_FILE = "update_file";
	private String[] Update;
	private SharedPreferences pref;
	private TextFontUtil tf;
	private Context con;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Bundle bun = getIntent().getExtras();
		Update = bun.getStringArray("Update");
//		Log.e("PopupActivity", "Update : " + Update[0]);
		setContentView(R.layout.admin_update_popup);
		con = this;
		tf = new TextFontUtil();
		PopUpTitle_tv = (TextView) findViewById(R.id.title_tv);
		tf.setNanumSquareB(this, PopUpTitle_tv);
		PopUpCon_tv = (TextView) findViewById(R.id.con_tv);
		tf.setNanumSquareL(this, PopUpCon_tv);

		PopUpCanCel_Bt = (Button)findViewById(R.id.admin_member_popup_cancel_bt);
		PopUpCanCel_Bt.setVisibility(View.VISIBLE);
		PopUpOk_Bt = (Button)findViewById(R.id.admin_member_popup_ok_bt);

		PopUpCanCel_Bt.setOnClickListener(this);
		PopUpOk_Bt.setOnClickListener(this);

		pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
		LOGIN_LANGUAGE = pref.getString("LOGIN_LANGUAGE", "");

		/*if("KR".equals(LOGIN_LANGUAGE)) {
			PopUpTitle_tv.setText(R.string.update_download_title_kr);
//				PopUpTitle_tv.setTextScaleX(1f);
			PopUpCon_tv.setText(R.string.update_download_con_kr);
			PopUpCanCel_Bt.setBackgroundResource(R.drawable.popup_ucancel_kr_bt);
			PopUpOk_Bt.setBackgroundResource(R.drawable.popup_usend_kr_bt);
		}else {*/
			PopUpTitle_tv.setText(R.string.update_download_title_eng);
//				PopUpTitle_tv.setTextScaleX(0.8f);
			PopUpCon_tv.setText(R.string.update_download_con_eng);
			PopUpCanCel_Bt.setBackgroundResource(R.drawable.popup_ucancel_eng_bt);
			PopUpOk_Bt.setBackgroundResource(R.drawable.popup_usend_eng_bt);
//		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.admin_member_popup_cancel_bt:
				IntroAct.Act.finish();
				finish();
				break;
			case R.id.admin_member_popup_ok_bt:
				IntroAct.Act.finish();
				new UpdateTask().execute(Update);
				break;
		}
	}

	public class UpdateTask extends AsyncTask<String,Integer,String[]> {

		private String TAG = "UpdateTask";
		private ProgressDialog mProgress;
		private int mFileCount = 0;

		protected void onPreExecute()
		{
			// show progress bar or something
			mProgress = new ProgressDialog(con);
			mProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mProgress.setMessage("Downloading...");
			mProgress.setIndeterminate(false);
			mProgress.setCancelable(false);
			mProgress.setProgress(0);
			mProgress.setMax(100);
			mProgress.setSecondaryProgress(0);
			mProgress.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
					WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
			 if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
				new CommonUtil().setHideNavigationBar(mProgress.getWindow().getDecorView());
			}
			mProgress.setButton("취소", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int whichButton)
				{
					cancel(true);
				}
			});
			mProgress.show();
			mProgress.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
		}

		protected String[] doInBackground(String... str) {
//			int strDataCnt = update.length;
			int strDataCnt = 1;
			long start = System.currentTimeMillis();


			try {
				HttpURLConnection conn;
				BufferedInputStream bis = null;
				for (int i = 0; i < strDataCnt; i++) {
//					Log.e(TAG, "URL : "+ str[i+1].trim());
					URL url = new URL(str[i+1].trim());
					Log.e(TAG, "url : "+ url);
					conn = (HttpURLConnection) url.openConnection();
//						conn.setRequestMethod("POST");
					conn.connect();

					int lenghtOfFile = conn.getContentLength();

					bis = new BufferedInputStream(url.openStream());
					String fname = str[1].substring(str[1].lastIndexOf('/') + 1);
//					Log.e(TAG, "fname : "+ fname);
					FileOutputStream fos = con.openFileOutput(fname, Context.MODE_PRIVATE);
					long downloadedSize = 0;
					byte[] buffer = new byte[0xFFFF];
					int bufferLength = 0;
					while ((bufferLength = bis.read(buffer)) != -1) {
						downloadedSize += bufferLength;
						publishProgress((int) ((downloadedSize * 100) / lenghtOfFile), i + 1, strDataCnt);
						fos.write(buffer, 0, bufferLength);
					}
					fos.close();
					bis.close();
					conn.disconnect();
				}

//				setChanged();
//				notifyObservers(UPDATE_GOT_UPDATE);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (Exception e){
				Log.e(TAG, "error : " + e.getMessage());
			} finally {
				long elapsed = System.currentTimeMillis() - start;
				Log.e(TAG, "download update finished in " + elapsed + "ms");
			}
			return str;
		}

		protected void onProgressUpdate(Integer... args)
		{
			mProgress.setProgress(args[0]);
			mProgress.setMessage("Downloading...");
//			mProgress.setMessage(args[1]+"/"+args[2]+", 진행율:"+args[0]+"%");

			if(args[0]==100)
			{
//				파일하나를 다 받았으면 디비에 저장한다.
				mProgress.dismiss();
			}
		}

		protected void onPostExecute(String[] result) {
			/*String fname = result[1].substring(result[1].lastIndexOf('/')+1);
			Uri myUrl = Uri.parse("file://" + con.getFilesDir().getAbsolutePath() + "/" + fname);
			Intent mintent = new Intent(Intent.ACTION_VIEW).setDataAndType(myUrl,"application/vnd.android.package-archive");
			mintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			con.getApplicationContext().startActivity( mintent );*/
			String fname = result[1].substring(result[1].lastIndexOf('/')+1);
			File file = new File(con.getFilesDir().getAbsolutePath(), fname);
			Intent intent = new Intent(Intent.ACTION_VIEW);
//			Log.e("pathOpen", file.getPath());

			Uri myUrl = Uri.parse("file://" + con.getFilesDir().getAbsolutePath() + "/" + fname);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			Log.e("SDK_INT", Build.VERSION.SDK_INT+"/"+file);
			if (Build.VERSION.SDK_INT >= 24) {

				Uri apkURI = FileProvider.getUriForFile(con, con.getApplicationContext().getPackageName() + ".fileprovider", file);
				intent.setDataAndType(apkURI, "application/vnd.android.package-archive");
				intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

			} else if(Build.VERSION.SDK_INT >= 30){


			} else {

				intent.setDataAndType(myUrl, "application/vnd.android.package-archive");
			}
			con.getApplicationContext().startActivity(intent);
			finish();
		}
	}
}
