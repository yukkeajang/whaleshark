package com.togetherseatech.whaleshark;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.Toast;

import com.togetherseatech.whaleshark.Db.Problem.ProblemInfo;
import com.togetherseatech.whaleshark.util.CommonUtil;
import com.togetherseatech.whaleshark.util.SelectItems;
import com.togetherseatech.whaleshark.util.XmlSAXParser3;

import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by seonghak on 2018. 3. 23..
 */

// ***************************************************************************************** //
// AsyncTask AdminUpgradePro Class
// ***************************************************************************************** //
public class AdminUpgradePro extends AsyncTask<ArrayList<UpgradeInfo>,Integer,Boolean> {

    private String TAG = "PatchTask";
    private ProgressDialog mProgress;
    private String DownFileName = "";
    private String SaveFileName = "";
    private String SaveFileNameTemp = "";
    private Activity mAct;
    private SelectItems Si;
    private SharedPreferences pref;
    private ArrayList<UpgradeInfo> ArrayFname;

    public AdminUpgradePro(Activity act) {
        mAct = act;
        Si = new SelectItems();
        pref = act.getSharedPreferences("pref", Context.MODE_PRIVATE);
    }

    protected void onPreExecute()
    {
        // show progress bar or something
        mProgress = new ProgressDialog(mAct);
        mProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgress.setMessage("Downloading FIle...");
        mProgress.setIndeterminate(false);
        mProgress.setCancelable(false);
        mProgress.setProgress(0);
        mProgress.setMax(100);
        mProgress.setSecondaryProgress(0);
        mProgress.setButton("취소", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                cancel(true);
                isCancel(SaveFileName);
                XmlSAXParser3 xml = new XmlSAXParser3(mAct,"VersionFile");
                AdminUpgradeAct.Admin_GroupList = xml.getGroupList();
                AdminUpgradeAct.Admin_ChildList = xml.getChildList();
                AdminUpgradeAct.Admin_ChildListContentQ = xml.getChildListQ();
                AdminUpgradeAct.Admin_UpgradeExpandableAdapter = new UpgradeExpandableAdapter(mAct, xml.getGroupList(), xml.getChildList(), xml.getChildListQ());
                // 리스트뷰에 어댑터를 세팅한다.
                AdminUpgradeAct.Admin_ExpandableListView.setAdapter(AdminUpgradeAct.Admin_UpgradeExpandableAdapter);
                if(AdminUpgradeAct.Admin_GroupList.size() > 0)
                    AdminUpgradeAct.Admin_ExpandableListView.expandGroup(0,true);
            }
        });
        mProgress.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
         if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
            new CommonUtil().setHideNavigationBar(mProgress.getWindow().getDecorView());
        }
        mProgress.show();
        mProgress.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    protected Boolean doInBackground(ArrayList<UpgradeInfo>... str) {
        long start = System.currentTimeMillis();
        String fname = "";
        ArrayFname = str[0];
        Log.e("test","ArrayFname : " + ArrayFname);
        int strDataCnt = ArrayFname.size();
        try {
            String PackageName = mAct.getPackageName();
            HttpURLConnection conn;
            BufferedInputStream bis = null;

            for (int i = 0; i < strDataCnt; i++) {

                if(ArrayFname.get(i).getMod_ver() > 0){
                    String[] name = ArrayFname.get(i).getFile_name().split("-");
//                    Si.deleteobbPrivateFile(mAct,name[0] + ".obb");

                    fname = name[0];
                }else{
                    fname = ArrayFname.get(i).getFile_name();
                }

                if(ArrayFname.get(i).menu.indexOf("Subtitle") > 0) {
                    DownFileName = fname + ".srt";
                    SaveFileName = fname + ".srt";
                } else if(ArrayFname.get(i).menu.indexOf("PDF") > 0){
                    DownFileName = fname + ".pdf";
                    SaveFileName = fname + ".pdf";
                } else {
                    DownFileName = fname + ".zip";
                    SaveFileName = fname + ".obb";
                }

                Log.e(TAG, DownFileName + "/" + SaveFileName);
                URL url = new URL(Vars.KEY_WEB_SITE + File.separator + "obb" + File.separator + DownFileName);
                Log.e("test","url : " + url);

                conn = (HttpURLConnection) url.openConnection();
    //						conn.setRequestMethod("POST");
                conn.connect();

                int lenghtOfFile = conn.getContentLength();

                bis = new BufferedInputStream(url.openStream());

                FileOutputStream fos = new FileOutputStream(new File(mAct.getObbDir(), SaveFileName));
                long downloadedSize = 0;
                byte[] buffer = new byte[0xFFFF];
                int bufferLength = 0;
                while ((bufferLength = bis.read(buffer)) != -1) {
                    downloadedSize += bufferLength;
                    publishProgress((int) ((downloadedSize * 100) / lenghtOfFile), i+1, strDataCnt);
                    fos.write(buffer, 0, bufferLength);
                }
                fos.close();
                bis.close();
                conn.disconnect();

                if(ArrayFname.get(i).getMod_ver() > 0) {
                    try {
                        SaveFileNameTemp = ArrayFname.get(i).getFile_name();
//                        Log.e(TAG,"SaveFileNameTemp : "+ SaveFileNameTemp);
                        File file = new File(mAct.getObbDir(), SaveFileNameTemp + ".obb");
                        file.createNewFile();
                        FileOutputStream fOut = new FileOutputStream(file);
                        OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                        myOutWriter.append("");
                        myOutWriter.close();
                        fOut.flush();
                        fOut.close();
                    } catch (IOException e) {
                        e.printStackTrace();
//                        Log.e(TAG,"SaveFileNameTemp : "+  e);
                        isCancel(SaveFileNameTemp + ".obb");
                        return false;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("test","FileNotFoundException : "  + e);
//            Log.e(TAG, "IOException : " + e.getMessage());
            isCancel(SaveFileName);
            return false;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("test","MalformedURLException : "  + e);
//            Log.e(TAG, "IOException : " + e.getMessage());
            isCancel(SaveFileName);
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("test","IOException : "  + e);
//            Log.e(TAG, "IOException : " + e.getMessage());
            isCancel(SaveFileName);
            return false;
        } catch (Exception e){
            Log.e("test","Exception : "  + e);
//            Log.e(TAG, "Exception : " + e.getMessage());
            isCancel(SaveFileName);
            return false;
        } finally {
            long elapsed = System.currentTimeMillis() - start;
            Log.e(TAG, "download update finished in " + elapsed + "ms");
        }
        return true;
    }

    protected void onProgressUpdate(Integer... args)
    {
        mProgress.setProgress(args[0]);
//          mProgress.setMessage("Downloading...");
//			mProgress.setMessage(" " + ArrayFname.get(args[1]-1).getContent_kr() + "  " + args[1]+" / "+args[2]+"         진행율 : "+args[0]+" % ");
			mProgress.setMessage("총 " + args[2] + "  /  " + args[1] + "    파일 : " + ArrayFname.get(args[1]-1).getFile_name());

        if(args[0]==100)
        {
            //파일하나를 다 받았으면 디비에 저장한다.

        }
    }

    protected void onPostExecute(Boolean result) {
        String isDownload = "";
        Log.e(TAG, "result : " + result + " / " + SaveFileNameTemp);
        if(result){
            isDownload = "DOWNLOAD_OK";
        }else{
            isDownload = "DOWNLOAD_FAIL";
            Si.deleteobbPrivateFile(mAct, SaveFileNameTemp + ".obb");
        }

        Si.getMassage(isDownload, pref.getString("LOGIN_LANGUAGE",""), mAct, Toast.LENGTH_SHORT);
        mProgress.dismiss();

        mAct.runOnUiThread(new Runnable() {
            public void run() {
                new AdminUpgradeAct.AdminUpgrade(mAct).execute();
            }
        });
    }

    private void isCancel(String fileName){
        Si.deleteobbPrivateFile(mAct, fileName);
    }
}


