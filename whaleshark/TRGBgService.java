package com.togetherseatech.whaleshark;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;

import android.os.SystemClock;
import android.util.Log;

import com.togetherseatech.whaleshark.Db.History.TrainingDao;
import com.togetherseatech.whaleshark.Db.History.TrainingInfo;
import com.togetherseatech.whaleshark.util.LogWrapper;
import com.togetherseatech.whaleshark.util.RestartService;

import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;


/**
 * Socket Background Service
 * 각종 메세지 관련 및 접속정보를 서버와 지속적인 소켓 통신 
 */

public class TRGBgService extends Service {

	@Override
    public void onCreate() {
//		Log.e("TRGBgService","onCreate TRGBgService");
		Thread.setDefaultUncaughtExceptionHandler(new LogWrapper());
//		unregisterRestartAlam();
//		AutoUpdate aua = new AutoUpdate(getApplicationContext());	// <-- don't forget to instantiate
//		aua.addObserver(this);	// see the remark below, next to update() method
		//setLicenseDateAlam();
		super.onCreate();
    }

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
    public void onDestroy() {
        super.onDestroy();
		Log.e("TRGBgService","onDestroy TRGBgService");
//		registerRestartAlam();
    }

	@Override
	public IBinder onBind(Intent intent) {
		// Service 객체와 (화면단 Activity 사이에서)
		// 통신(데이터를 주고받을) 때 사용하는 메서드
		// 데이터를 전달할 필요가 없으면 return null;
		return null;
	}

	/*public void setLicenseDateAlam(){
//		Log.e("TRGBgService","setLicenseDateAlam");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH,6);
		Intent intent = new Intent(TRGBgService.this, RestartService.class);
		intent.setAction("CloseLicense");
		PendingIntent sender = PendingIntent.getBroadcast(TRGBgService.this, 0, intent, 0);
		AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
//		am.set(AlarmManager.RTC, System.currentTimeMillis() + 60, sender);
		if(Build.VERSION.SDK_INT >= 23)
			am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
		else {
			if(Build.VERSION.SDK_INT >= 19)
				am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
			else
				am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
		}
	}*/

	/*public void registerRestartAlam(){
		Log.e("TRGBgService","registerRestartAlam");
		Intent intent = new Intent(TRGBgService.this, RestartService.class);
		intent.setAction("restart");
		PendingIntent sender = PendingIntent.getBroadcast(TRGBgService.this, 0, intent, 0);
		long firstTime = SystemClock.elapsedRealtime();
		firstTime += 1 * 1000; //10초
		AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
		am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, 10 * 1000, sender);
	}

	public void unregisterRestartAlam() {
		Log.e("TRGBgService","unregisterRestartAlam");
		Intent intent = new Intent(TRGBgService.this, RestartService.class);
		intent.setAction("restart");
		PendingIntent sender = PendingIntent.getBroadcast(TRGBgService.this, 0, intent, 0);
		AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
		am.cancel(sender);
	}*/



	// ***************************************************************************************** //
	// AsyncTask setDataPro Class
	// ***************************************************************************************** //
	public static class setDelictPro extends AsyncTask<String, Integer, String> {



		String line = null;
		Context mContext;
		TrainingInfo mTi;
		public setDelictPro(Context mContext, TrainingInfo mTi){
			this.mContext = mContext;
			this.mTi = mTi;
			Log.e("test","setDelictPro : ");
		}

		@Override
		protected String doInBackground(String... arg) {
//			System.out.println("doInBackground");
//			Log.e("setDataPro","doInBackground");
			int sumTime = 0;
			int sumScore = 0;

			TrainingDao TDao = new TrainingDao(mContext);

			mTi.setTime(sumTime);
			mTi.setScore(sumScore);
			int result = TDao.updateHistory(mTi);
			Log.e("setDataPro","result : "+ result);

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
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
	}
	// ***************************************************************************************** //
	// AsyncTask setDataPro Class End
	// ***************************************************************************************** //
}
