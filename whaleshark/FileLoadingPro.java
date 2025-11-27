package com.togetherseatech.whaleshark;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.WindowManager;

import com.togetherseatech.whaleshark.util.CommonUtil;
import com.togetherseatech.whaleshark.util.SelectItems;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.progress.ProgressMonitor;

import java.io.File;

/**
 * Created by seonghak on 2018. 3. 23..
 */

// ***************************************************************************************** //
// AsyncTask FileLoadingPro Class
// ***************************************************************************************** //
public class FileLoadingPro extends AsyncTask<String, Integer, File> {

    private ProgressDialog eDialog;
    private Context mContext;
    private SelectItems Si;
    private String Type;


    public FileLoadingPro(Context con, String type) {
        mContext = con;
        Type = type;
        Si = new SelectItems();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//            eDialog = ProgressDialog.show(mContext, null, "Loading...", true, false);
        eDialog = new ProgressDialog(mContext);
        eDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        eDialog.setMessage("Loading FIle...");
        eDialog.setIndeterminate(false);
        eDialog.setCancelable(false);
        eDialog.setProgress(0);
        eDialog.setMax(100);
        eDialog.setSecondaryProgress(0);eDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
         if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
            new CommonUtil().setHideNavigationBar(eDialog.getWindow().getDecorView());
        }
        eDialog.show();
        eDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    @Override
    protected File doInBackground(String... str) {
        File readFile = null;
        SharedPreferences pref = mContext.getSharedPreferences("pref", Context.MODE_PRIVATE);
        String ZIP_PASSWORD = pref.getString("ZIP_PASSWORD", "");
        String PackageName = mContext.getPackageName();
        String fullFileName = str[0] + "." + str[1];

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.R || Environment.isExternalStorageManager()){
            Log.e("test","isExternalStorgedManager : " + Environment.isExternalStorageManager());
        }


        File root = Environment.getExternalStorageDirectory();


        Log.e("test","loadinpro  : " + Environment.getExternalStorageState());


        File expPath = new File(root.toString() + "/Android/obb/" + PackageName);
        String strMainPath = expPath + File.separator + str[0] +".obb";

        try{
//            Log.e("getZipFile","strMainPath : "+strMainPath);
            ZipFile zipFile = new ZipFile(strMainPath);
            zipFile.setRunInThread(true);

            if (zipFile.isEncrypted()) {
                zipFile.setPassword(ZIP_PASSWORD);
            }


            zipFile.extractFile(fullFileName, mContext.getExternalFilesDir(null).toString());
            ProgressMonitor progressMonitor = zipFile.getProgressMonitor();

            while (progressMonitor.getState() != ProgressMonitor.STATE_READY) {
//                    int percentDone = progressMonitor.getPercentDone();
//                    Log.e("getZipFile",progressMonitor.getState() + " " + percentDone);
//                Log.e("getZipFile",progressMonitor.getTotalWork() +"/"+progressMonitor.getPercentDone() );
//                publishProgress(progressMonitor.getPercentDone());
                eDialog.setProgress(progressMonitor.getPercentDone());
            }

//            Log.e("getZipFile","getExternalFilesDir : "+mContext.getExternalFilesDir(null));
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
        eDialog.setProgress(args[0]);
    }

    @Override
    protected void onPostExecute(File result) {
        super.onPostExecute(result);
        if (result != null) {


            eDialog.dismiss();
            if("P".equals(Type)){
                PersonalTrainingAct.MovePlay(result);
            }
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
//        eDialog.dismiss();
    }



}


