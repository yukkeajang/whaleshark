package com.togetherseatech.whaleshark;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.togetherseatech.whaleshark.util.XmlSAXParser3;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Observable;
import java.util.Set;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import javax.xml.parsers.SAXParser;

/**
 * AutoUpdate Observable Class (자동업데이트)
 */
public class AutoUpdate extends Observable {

	/**
	 * 데이터 클래스 생성자
	 * @param ctx Context
	 */
	public AutoUpdate(Context ctx) {
		setupVariables(ctx);
	}

	/**
	 * set icon for notification popup (default = application icon)
	 * @param icon
	 */
	public static void setIcon(int icon) {
		appIcon = icon;
	}

	/**
	 * set name to display in notification popup (default = application label)
	 * @param name
	 */
	public static void setName(String name) {
		appName = name;
	}

	/**
	 * set Notification flags (default = Notification.FLAG_AUTO_CANCEL | Notification.FLAG_NO_CLEAR)
	 * @param flags
	 */
	public static void setNotificationFlags(int flags) {
		NOTIFICATION_FLAGS = flags;
	}

	/**
	 * set update interval (in milliseconds)
	 * there are nice constants in this file: MINUTES, HOURS, DAYS
	 * you may use them to specify update interval like: 5 * DAYS
	 * please, don't specify update interval below 1 hour, this might
	 * be considered annoying behaviour and result in service suspension
	 * @param interval
	 */
	public void setUpdateInterval(long interval) {
		if( interval > 60 * MINUTES ) {
			UPDATE_INTERVAL = interval;
		} else {
			Log_e(TAG, "update interval is too short (less than 1 hour)");
		}
	}

	/**
	 *  software updates will use WiFi/Ethernet only (default mode)
	 */
	public static void disableMobileUpdates() {
		mobile_updates = false;
	}

	/**
	 *  software updates will use any internet connection, including mobile
	 *  might be a good idea to have 'unlimited' plan on your 3.75G connection
	 */
	public static void enableMobileUpdates() {
		mobile_updates = true;
	}

	/**
	 * call this if you want to perform update on demand
	 * (checking for updates more often than once an hour is not recommended
	 * and polling server every few minutes might be a reason for suspension)
	 */
	public void checkUpdatesManually() {
		checkUpdates(true);		// force update check
	}
	
	public static final String UPDATE_CHECKING = "checking";
	public static final String UPDATE_NO_UPDATE = "no_update";
	public static final String UPDATE_GOT_UPDATE = "got_update";
	public static final String UPDATE_HAVE_UPDATE = "have_update";

	public void clearSchedule() {
		schedule.clear();
	}

	public void addSchedule(int start, int end) {
		schedule.add(new ScheduleEntry(start,end));
	}
//
// ---------- everything below this line is private and does not belong to the public API ----------
//
	protected final static String TAG = "AutoUpdate";
	private final static String ANDROID_PACKAGE = "application/vnd.android.package-archive";
	private final static String API_URL = "http://www.togetherseatech.com/UpdateApp.do";
//	private final static String API_URL = "http://192.168.0.8:8080/UpdateApp.do";
	public static Context context = null;
	protected static SharedPreferences preferences;
	private final static String LAST_UPDATE_KEY = "last_update";
	private static long last_update = 0;

	private static int appIcon = R.mipmap.ic_launcher;
	private static int versionCode = 0;		// as low as it gets
	private static String packageName;
	private static String appName;
	private static int device_id;

	public static final long MINUTES = 60 * 1000;
	public static final long HOURS = 60 * MINUTES;
	public static final long DAYS = 24 * HOURS;
	
	// 3-4 hours in dev.mode, 1-2 days for stable releases
//	private static long UPDATE_INTERVAL = 3 * HOURS;	// how often to check
	private static long UPDATE_INTERVAL = 1 * MINUTES;

	private static boolean mobile_updates = false;		// download updates over wifi only

	private final static Handler updateHandler = new Handler();
	protected final static String UPDATE_FILE = "update_file";
	protected final static String SILENT_FAILED = "silent_failed";
	private final static String MD5_TIME = "md5_time";
	private final static String MD5_KEY = "md5";

	private static int NOTIFICATION_ID = 0xDEADBEEF;
	private static int NOTIFICATION_FLAGS = Notification.FLAG_AUTO_CANCEL | Notification.FLAG_NO_CLEAR;
//	private static int NOTIFICATION_FLAGS = Notification.FLAG_AUTO_CANCEL;
	private static long WAKEUP_INTERVAL = 24 * HOURS;
	private NotificationManager mNotifyManager;
	private NotificationCompat.Builder mBuilder;
	public static String[] update;
	public static SAXParser saxParser;
	public static AssetManager aManager;

	private class ScheduleEntry {
		public int start;
		public int end;

		public ScheduleEntry(int start, int end) {
			this.start = start;
			this.end = end;
		}
	}

	private static ArrayList<ScheduleEntry> schedule = new ArrayList<ScheduleEntry>();

	private void setupVariables(Context ctx) {
//		Log.e("AutoUpdate", "setupVariables");
		context = ctx;
		packageName = context.getPackageName();
//		preferences = context.getSharedPreferences( packageName + "_" + TAG, Context.MODE_PRIVATE);
		preferences = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
		device_id = crc32(Secure.getString( context.getContentResolver(), Secure.ANDROID_ID));
		
		last_update = preferences.getLong("last_update", 0);
		
		NOTIFICATION_ID += crc32(packageName);
		ApplicationInfo appinfo = context.getApplicationInfo();
		if( appinfo.icon != 0 ) {
			appIcon = appinfo.icon;
		} else {
			Log_w(TAG, "unable to find application icon");
		}
		
		if( appinfo.labelRes != 0 ) {
			appName = context.getString(appinfo.labelRes);
		} else {
			Log_w(TAG, "unable to find application label");
		}
		if( new File(appinfo.sourceDir).lastModified() > preferences.getLong(MD5_TIME, 0) ) {
			preferences.edit().putString( MD5_KEY, MD5Hex(appinfo.sourceDir)).commit();
			preferences.edit().putLong( MD5_TIME, System.currentTimeMillis()).commit();

			String update_file = preferences.getString(UPDATE_FILE, "");
			
			if( update_file.length() > 0 ) {
				if( new File( context.getFilesDir().getAbsolutePath() + "/" + update_file ).delete() ) {
					preferences.edit().remove(UPDATE_FILE).remove(SILENT_FAILED).commit();
				}
			}
		}
		Log.e("AutoUpdate", "setupVariables = " + haveInternetPermissions());
		if( haveInternetPermissions() ) {
			Log.e("test","check :" + "?");
//			context.registerReceiver( connectivity_receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
//			updateHandler.postDelayed(periodicUpdate, WAKEUP_INTERVAL);
            checkUpdates(true);
		}
	}
	
	private boolean haveInternetPermissions() {
		Set<String> required_perms = new HashSet<String>();
		required_perms.add("android.permission.INTERNET");
		required_perms.add("android.permission.ACCESS_WIFI_STATE");
		required_perms.add("android.permission.ACCESS_NETWORK_STATE");

		PackageManager pm = context.getPackageManager();
		String packageName = context.getPackageName();
		int flags = PackageManager.GET_PERMISSIONS;
		PackageInfo packageInfo = null;

		try {
			
			packageInfo = pm.getPackageInfo(packageName, flags);
			versionCode = packageInfo.versionCode;
			
		} catch (NameNotFoundException e) {
			
			e.printStackTrace();
			Log_e(TAG, e.getMessage());
			
		}
		
		if( packageInfo.requestedPermissions != null ) {
			for( String p : packageInfo.requestedPermissions ) {
				//Log_v(TAG, "permission: " + p.toString());
				required_perms.remove(p);
			}
			if( required_perms.size() == 0 ) {
				return true;	// permissions are in order
			}
			// something is missing
			for( String p : required_perms ) {
				Log_e(TAG, "required permission missing: " + p);
			}
		}
		Log_e(TAG, "INTERNET/WIFI access required, but no permissions are found in Manifest.xml");
		return false;
	}

	/*public BroadcastReceiver connectivity_receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(final Context context, Intent intent) {
			Log.e(TAG, "BroadcastReceiver : ");
			NetworkInfo currentNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
			// do application-specific task(s) based on the current network state, such 
			// as enabling queuing of HTTP requests when currentNetworkInfo is connected etc.
//			boolean WIFI = currentNetworkInfo.getTypeName().equalsIgnoreCase("WIFI") ? true : false;
//			String a= ""+WIFI+"/"+mobile_updates+"/"+currentNetworkInfo.getSubtypeName();
//			Log.d(TAG, a);
			Log.e(TAG, "isConnected : "+ currentNetworkInfo.isConnected());
			if(currentNetworkInfo.isConnected()) {
				checkUpdates(true);
			} else {
				*//*Handler aumHandler = new Handler();
				Runnable aumRunnable = new Runnable() {
					@Override
					public void run() {
						if( haveInternetPermissions() ) {
							context.registerReceiver( connectivity_receiver,
									new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
						}
					}
				};
				// 3초후 자동실행
				aumHandler.postDelayed(aumRunnable, UPDATE_INTERVAL);

		    	if(currentNetworkInfo.isConnected())
					aumHandler.removeCallbacks(aumRunnable);*//*
				setChanged();
				notifyObservers(UPDATE_NO_UPDATE);
				preferences.edit().putString("ZIP_PASSWORD", Vars.KEY_ZIP_PASSWORD).commit();
			}
		}
	};*/
	
	/*private Runnable periodicUpdate = new Runnable() {
		@Override
		public void run() {
			checkUpdates(true);
			updateHandler.removeCallbacks(periodicUpdate);	// remove whatever others may have posted
			updateHandler.postDelayed(this, WAKEUP_INTERVAL);
		}
	};*/
	
	private void checkUpdates(boolean forced) {
		Log.e("test","checkUpdates forced : " + forced);
		long now = System.currentTimeMillis();
		if( forced || (last_update + UPDATE_INTERVAL) < now && checkSchedule() ) {
			new checkUpdateTask().execute();
			last_update = System.currentTimeMillis();
			preferences.edit().putLong( LAST_UPDATE_KEY, last_update).commit();

			this.setChanged();
			this.notifyObservers(UPDATE_CHECKING);
		}
	}
	
	private boolean checkSchedule() {
		if( schedule.size() == 0 ) return true;	// empty schedule always fits

		int now = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		for( ScheduleEntry e : schedule ) {
			if( now >= e.start && now < e.end ) return true;
		}
		return false;
	}

	private class checkUpdateTask extends AsyncTask<Void,Void,String[]> {

		private DefaultHttpClient httpclient ; 
		private HttpPost post = new HttpPost(API_URL);
		private SchemeRegistry schemeRegistry = new SchemeRegistry();

		protected void onPreExecute()
		{
			// show progress bar or something
			Log_e(TAG, "checking if there's update on the server");
		}

		protected String[] doInBackground(Void... v) {
			
			schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			long start = System.currentTimeMillis();

			HttpParams httpParameters = new BasicHttpParams();
			
			int maxtotalconnection = 20;
			int matconnectionperroute = 20;
			int timeoutConnection = 5000;
			int timeoutSocket = 15000;
			
			ConnManagerParams.setMaxTotalConnections(httpParameters, maxtotalconnection);
			ConnManagerParams.setMaxConnectionsPerRoute(httpParameters, new ConnPerRouteBean(matconnectionperroute));
			// set the timeout in milliseconds until a connection is established
			// the default value is zero, that means the timeout is not used
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			// set the default socket timeout (SO_TIMEOUT) in milliseconds
			// which is the timeout for waiting for data
			
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			HttpConnectionParams.setTcpNoDelay(httpParameters, true);
			
			ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(httpParameters, schemeRegistry);
			
			
			httpclient = new DefaultHttpClient(cm, httpParameters);

			try {

				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

				nameValuePairs.add(new BasicNameValuePair("PACKAGE_NAME", packageName));
				nameValuePairs.add(new BasicNameValuePair("VERSION", versionCode+""));
				UrlEncodedFormEntity entityRequest =
						new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
				post.setHeader("Content-Type", "application/x-www-form-urlencoded");
				post.setEntity(entityRequest);

				Log.e("test","NameValue : " + nameValuePairs);
				HttpResponse httpResponse = httpclient.execute( post );
				Log.e("test","httpResponse :: " + httpResponse);

				String response = null;
				String[] result = null;
				
				if(httpResponse != null) {
					response = EntityUtils.toString( httpResponse.getEntity(), "UTF-8" );
					Log.e("test","respnse : " + response);
//					Log_e(TAG, "response : " + response);
					result = response.split(",");
				}
				Log.e("test","AutoUpdate Result : " + result);
				return result;

			} catch (Exception e){
				Log_e(TAG, "error : " + e.getMessage());
			} finally {
				httpclient.getConnectionManager().shutdown();
				long elapsed = System.currentTimeMillis() - start;
				Log_e(TAG, "update check finished in " + elapsed + "ms");
			}
			return null;
		}

		protected void onPostExecute(String[] result) {
			// kill progress bar here
//			Log_e(TAG, "result : " + result);
			if( result != null ) {
				if( result[0].trim().equalsIgnoreCase("have update") ) {
					String fname = result[1].substring(result[1].lastIndexOf('/')+1);
					preferences.edit().putString(UPDATE_FILE, fname).commit();
//					preferences.edit().putString("ZIP_PASSWORD", result[2].trim()).commit();
					String update_file_path = context.getFilesDir().getAbsolutePath() + "/" + fname;
					Log.e("test","update_file_path : " + update_file_path);
					preferences.edit().putString( MD5_KEY, MD5Hex(update_file_path)).commit();
					preferences.edit().putLong( MD5_TIME, System.currentTimeMillis()).commit();
//					Log_e(TAG, "ZIP_PASSWORD " + result[2].trim());

					String update_file = preferences.getString(UPDATE_FILE, "");
//					Log_e(TAG, "update_file : " + update_file);
					if( update_file.length() > 0 ) {
						XmlSAXParser3 xml = new XmlSAXParser3(context,"VersionFile");
						Boolean isUpgrade = false;
						for(int i = 0; i < xml.getChildList().size(); i++){
							ArrayList<UpgradeInfo> ArrayPi = xml.getChildList().get(i);
							for(int j = 0; j < ArrayPi.size(); j++){
//								Log_e(TAG, "ArrayPi.get(j).getVer() : " + ArrayPi.get(j).getVer() +"/"+versionCode);
								if(ArrayPi.get(j).getVer() <= versionCode){
									isUpgrade = true;
									break;
								}
							}
						}

						for(int i = 0; i < xml.getChildListQ().size(); i++){
							UpgradeInfo ArrayPi = xml.getChildListQ().get(i);
//							Log_e(TAG, "ArrayPi.getVer() : " + ArrayPi.getVer() +"/"+versionCode);
							if(ArrayPi.getVer() <= versionCode){
								isUpgrade = true;
								break;
							}
						}

//						Log_e(TAG,"isUpgrade : " + isUpgrade);
						if(!isUpgrade){
							update = result;

							setChanged();
							notifyObservers(UPDATE_HAVE_UPDATE);

//					raise_notification();

						/*Bundle bun = new Bundle();
						bun.putStringArray("Update", update);
						Intent popupIntent = new Intent(context, PopupActivity.class);
						popupIntent.putExtras(bun);
						popupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						PendingIntent pie = PendingIntent.getActivity(context, 0, popupIntent, PendingIntent.FLAG_ONE_SHOT);
						try {
							pie.send();
						} catch (PendingIntent.CanceledException e) {
							Log_e(TAG, e.getMessage());
						}*/
						}else{
							setChanged();
							notifyObservers(UPDATE_NO_UPDATE);
						}
					}

				} else {
					setChanged();
					notifyObservers(UPDATE_NO_UPDATE);
//					preferences.edit().putString(UPDATE_FILE, "").commit();
//					preferences.edit().putString("ZIP_PASSWORD", result[1].trim()).commit();
//					Log_e(TAG, "ZIP_PASSWORD " + result[0].trim() + "/" +result[1].trim() + "/" + result.length);
				}

			} else {
				Log_e(TAG, "no reply from update server");
                setChanged();
                notifyObservers(UPDATE_NO_UPDATE);
//                preferences.edit().putString("ZIP_PASSWORD", Vars.KEY_ZIP_PASSWORD).commit();
			}

			preferences.edit().putString("ZIP_PASSWORD", Vars.KEY_ZIP_PASSWORD).commit();
		}
	}

	protected void raise_notification() {
		
		mNotifyManager =
		        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mBuilder = new NotificationCompat.Builder(context);
		mBuilder.setContentTitle("Update CBT Download")
		    .setContentText("Download in progress")
		    .setSmallIcon(R.mipmap.ic_launcher);
		// Start a lengthy operation in a background thread
		new Thread(
		    new Runnable() {
		        @Override
		        public void run() {
		            int incr;
		            // Do the "lengthy" operation 20 times
		            for (incr = 0; incr <= 100; incr+=5) {
		                    // Sets the progress indicator to a max value, the
		                    // current completion percentage, and "determinate"
		                    // state
		                    mBuilder.setProgress(100, incr, false);
		                    // Displays the progress bar for the first time.
		                    mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());
		                        // Sleeps the thread, simulating an operation
		                        // that takes time
		                        try {
		                            // Sleep for 5 seconds
		                            Thread.sleep(5*100);
		                        } catch (InterruptedException e) {
		                            Log.d(TAG, "sleep failure");
		                        }
		            }
		            mNotifyManager.cancel(NOTIFICATION_ID);
		            // When the loop is finished, updates the notification
		            mBuilder.setContentText("Download complete").setProgress(0,0,false);          // Removes the progress bar
		  
		                    
		            mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());
		            
		           
		        }
		    }
		// Starts the thread by calling the run() method in its Runnable
		).start();
		
		String update_file = preferences.getString(UPDATE_FILE, "");
//        Log.d("test","file://" + context.getFilesDir().getAbsolutePath() + "/" + update_file);
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager nm = (NotificationManager) context.getSystemService(ns);

		if( update_file.length() > 0 ) {
			setChanged();
			notifyObservers(UPDATE_HAVE_UPDATE);
			
			CharSequence contentTitle = appName + " update available";
			CharSequence contentText = "Select to install";
			Intent notificationIntent = new Intent(Intent.ACTION_VIEW);
			notificationIntent.setDataAndType(
					Uri.parse("file://" + context.getFilesDir().getAbsolutePath() + "/" + update_file),
					ANDROID_PACKAGE);
			PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
			Notification notification = new Notification.Builder(context)
			.setContentTitle(contentTitle) 
			.setContentText(contentText)         
			.setSmallIcon(appIcon)
			.setContentIntent(contentIntent)
			//.setLargeIcon()
			.build();
			notification.flags |= NOTIFICATION_FLAGS;
			// raise notification


			nm.notify( NOTIFICATION_ID, notification);
		} else {
			nm.cancel( NOTIFICATION_ID );
		}
	}

	/*protected void raise_notification() {
		
		String update_file = preferences.getString(UPDATE_FILE, "");
		Log_e(TAG, "raise_notification==="+context.getFilesDir().getAbsolutePath()+ "/" + update_file);
		boolean silent_update_failed = preferences.getBoolean(SILENT_FAILED, false);
		if( update_file.length() > 0 && !silent_update_failed ) {
			final String libs = "LD_LIBRARY_PATH=/vendor/lib:/system/lib ";
			final String[] commands = {
					libs + "pm install -r " + context.getFilesDir().getAbsolutePath() + "/" + update_file,
					libs + "am start -n " + context.getPackageName() + "/" + get_main_activity()
			};
			execute_as_root(commands);	// not supposed to return if successful
			preferences.edit().putBoolean(SILENT_FAILED, true).commit();	// avoid silent update loop
		}
	}

	// this is not guaranteed to work 100%, should be rewritten.
	//
	// if your application fails to restart after silent upgrade,
	// you may try to replace this function with a simple statement:
	//
	//		return ".YourMainActivity";
	//
	private String get_main_activity() {
		Log_e(TAG, "get_main_activity");
		PackageManager pm = context.getPackageManager();
		String packageName = context.getPackageName();

		try {
			final int flags = PackageManager.GET_ACTIVITIES;
			PackageInfo packageInfo = pm.getPackageInfo(packageName, flags);
			for( ActivityInfo ai : packageInfo.activities ) {
				if( ai.exported ) {
					return ai.name;
				}
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		Log_e(TAG, "get_main_activity() failed");
		return "";
	}

	private void execute_as_root( String[] commands ) {
		Log_e(TAG, "execute_as_root");
		try {
			// Do the magic
			//Log_e(TAG, "execute_as_root="+Runtime.getRuntime().exec("su"));
			Process p = Runtime.getRuntime().exec("sh\n");
			InputStream es = p.getErrorStream();
			DataOutputStream os = new DataOutputStream(p.getOutputStream());
			Log_i(TAG,"command:"+commands);
			for( String command : commands ) {
				Log_e(TAG, "command===="+command);
				os.writeBytes(command + "\n");
			}
			os.writeBytes("exit\n");
			os.flush();

			int read;
			byte[] buffer = new byte[4096];
			String output = new String();
			while ((read = es.read(buffer)) > 0) {
			    output += new String(buffer, 0, read);
			}
			os.close();
			p.waitFor();
			
			Log_e(TAG, output.trim() + " /(" + p.exitValue() + ")");
		} catch (IOException e) {
			Log_e(TAG, e.getMessage());
		} catch (InterruptedException e) {
			Log_e(TAG, e.getMessage());
		}
	}
	*/
	private String MD5Hex( String filename )
	{
		final int BUFFER_SIZE = 8192;
		byte[] buf = new byte[BUFFER_SIZE];
		int length;
		try {
			FileInputStream fis = new FileInputStream( filename );
			BufferedInputStream bis = new BufferedInputStream(fis);
			MessageDigest md = MessageDigest.getInstance("MD5");
			while( (length = bis.read(buf)) != -1 ) {
				md.update(buf, 0, length);
			}

			byte[] array = md.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
			}
//			Log_v(TAG, "md5sum: " + sb.toString());
			return sb.toString();
		} catch (Exception e) {
//			e.printStackTrace();
			Log_e(TAG, e.getMessage());
		}
		return "md5bad";
	}

	private static int crc32(String str) {
//		android.util.Log.e(TAG, "crc32 ===S==: " + str);
		byte bytes[] = str.getBytes();
		Checksum checksum = new CRC32();
		checksum.update(bytes,0,bytes.length);
//		android.util.Log.e(TAG, "crc32 ===E=return==: " + checksum.getValue());
		return (int) checksum.getValue();
	}

	// logging facilities to enable easy overriding. thanks, Dan!
	//
	protected void Log_v(String tag, String message) {Log_v(tag, message, null);}
	protected void Log_v(String tag, String message, Throwable e) {log("v", tag, message, e);}
	protected void Log_d(String tag, String message) {Log_d(tag, message, null);}
	protected void Log_d(String tag, String message, Throwable e) {log("d", tag, message, e);}
	protected void Log_i(String tag, String message) {Log_d(tag, message, null);}
	protected void Log_i(String tag, String message, Throwable e) {log("i", tag, message, e);}
	protected void Log_w(String tag, String message) {Log_w(tag, message, null);}
	protected void Log_w(String tag, String message, Throwable e) {log("w", tag, message, e);}
	protected static void Log_e(String tag, String message) {Log_e(tag, message, null);}
	protected static void Log_e(String tag, String message, Throwable e) {log("e", tag, message, e);}

	protected static void log(String level, String tag, String message, Throwable e) {
		if(level.equalsIgnoreCase("v")) {
			if(e == null) Log.v(tag, message);
			else Log.v(tag, message, e);
		} else if(level.equalsIgnoreCase("d")) {
			if(e == null) Log.d(tag, message);
			else Log.d(tag, message, e);
		} else if(level.equalsIgnoreCase("i")) {
			if(e == null) Log.i(tag, message);
			else Log.i(tag, message, e);
		} else if(level.equalsIgnoreCase("w")) {
			if(e == null) Log.w(tag, message);
			else Log.w(tag, message, e);
		} else {
			if(e == null) Log.e(tag, message);
			else Log.e(tag, message, e);
		}
	}

}
