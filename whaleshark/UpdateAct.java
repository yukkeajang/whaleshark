package com.togetherseatech.whaleshark;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.togetherseatech.whaleshark.Db.Member.MemberDao;
import com.togetherseatech.whaleshark.Db.Member.MemberInfo;
import com.togetherseatech.whaleshark.Db.Problem.ProblemDao;
import com.togetherseatech.whaleshark.Db.Problem.ProblemInfo;
import com.togetherseatech.whaleshark.Db.Training.TrainingsDao;
import com.togetherseatech.whaleshark.Db.Training.TrainingsInfo;
import com.togetherseatech.whaleshark.util.SelectItems;
import com.togetherseatech.whaleshark.util.XmlSAXParser;
import com.togetherseatech.whaleshark.util.XmlUtil;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by seonghak on 2017. 10. 30..
 */

public class UpdateAct extends Activity {

    private ProgressDialog eDialog;
    private SelectItems Si;
//    private SharedPreferences pref;
//    private SharedPreferences.Editor editor;
//    private Boolean ProblemUpdate = false;
    private String PackageName;
    private int PackageVer, cnt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_act);
//        Log.e("UpdateAct", "onCreate");

        Si = new SelectItems();
//        pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
        /*PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }*/

        PackageName = this.getPackageName();
//        PackageVer = info.versionCode;

        ProblemDao Pdao = new ProblemDao(this);
        cnt = Pdao.getProblemCount();
//        Log.e("UpdateAct", "cnt = " + cnt);
//        int cnt2 = Pdao.getSubProblemCount();
//        Log.e("UpdateAct", "cnt2 = " + cnt2);
//        if(!Si.hasObbPrivateFile(this, "main")){
//            new PatchTask().execute();
//        }else{
            if (cnt <= 0) {
                new UpdatePro().execute("KeyLicense");
            }else{
                startActivity(new Intent(UpdateAct.this, MainAct.class));
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
//        }


    }


    // ***************************************************************************************** //
    // AsyncTask UpdatePro Class
    // ***************************************************************************************** //
    private class UpdatePro extends AsyncTask<String, Integer, String> {

        private String xml = null;
        private long start;
        private ArrayList<MemberInfo> MemberList;
        private MemberInfo Date;
        @Override
        protected void onPreExecute() {
//            Log.e("UpdateAct", "onPreExecute");
//            eDialog = ProgressDialog.show(UpdateAct.this, null, Si.getMassage("UPDATE_PROBLEM", "ENG"), true, false);
            eDialog = new ProgressDialog(UpdateAct.this);
            eDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            eDialog.setMessage(Si.getMassage("UPDATE_PROBLEM", "ENG"));
            eDialog.setIndeterminate(true);
            eDialog.setCancelable(false);
            eDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }


        @Override
        protected String doInBackground(String... str) {
            start = System.currentTimeMillis();
            String line = null;
            xml = str[0];
            try {
                AssetManager aManager = getAssets();
                InputStream iStream = aManager.open(str[0]+".xml");
//                File readFile = Si.getZipFile(UpdateAct.this, 1, str[0]+".xml");
//                InputStream iStream = new FileInputStream(readFile);
                int length = iStream.available();
                byte[] data = new byte[length];
                iStream.read(data);
                line = new String(data).toString();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return line;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(result != null){
                final StringBuilder Data = new StringBuilder();
                Data.append(result);
                try {
                    Element root = XmlUtil.loadDocument(Data.toString());
                    int cnt = XmlUtil.getTagCnt(root, "ROW");

                    MemberDao Mdao = new MemberDao(UpdateAct.this);
                    int lCnt = Mdao.getKeyLicenseCount();
//                    Log.e("UpdateAct", "lCnt = " + lCnt);
                    if (lCnt == 0) {
                        MemberList = new ArrayList<>();
                        for (int i = 0; i < cnt; i++) {
                            MemberInfo Mi = new MemberInfo();
                            Mi.setKey(XmlUtil.getTagTextContent(root, "ROW", i));
                            MemberList.add(Mi);
                        }
                        Mdao.addKeyLicense(MemberList);
                    }


                    int mCnt = Mdao.getVslMembersCount();
                    if (mCnt == 0)
                        new XmlSAXParser(UpdateAct.this, "VslMember");

                    new XmlSAXParser(UpdateAct.this, "Problem");

                    long elapsed = System.currentTimeMillis() - start;
                    Log.e("UpdateAct", "Question Updating finished in " + elapsed + "ms");


                    startActivity(new Intent(UpdateAct.this, MainAct.class));
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                    eDialog.dismiss();

                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
//                    System.out.println("ParserConfigurationException :"+e.toString());
                } catch (SAXException e) {
                    e.printStackTrace();
//                    System.out.println("SAXException :"+e.toString());
                } catch (IOException e) {
                    e.printStackTrace();
//                    System.out.println("IOException :"+e.toString());
                } catch (Exception e) {
                    e.printStackTrace();
//                    System.out.println("Exception :"+e.toString());
                }
            }
        }
    }

    /*// ***************************************************************************************** //
    // AsyncTask PatchTask Class
    // ***************************************************************************************** //
    public class PatchTask extends AsyncTask<Integer,Integer,Integer[]> {

        private String TAG = "PatchTask";
        private ProgressDialog mProgress;
        private int mFileCount = 0;
        private String DownFileName = "";
        private String SaveFileName = "";
        protected void onPreExecute()
        {
            // show progress bar or something
            mProgress = new ProgressDialog(UpdateAct.this);
            mProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgress.setMessage("Downloading Patch FIle..");
            mProgress.setIndeterminate(false);
            mProgress.setCancelable(false);
            mProgress.setProgress(0);
            mProgress.setMax(100);
            mProgress.setSecondaryProgress(0);
            *//*if(PackageVer > 1) {
                mProgress.setButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        cancel(true);
                        Si.deleteobbPrivateFile(UpdateAct.this, SaveFileName);
                        startActivity(new Intent(UpdateAct.this, MainAct.class));
                        finish();
                        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
                    }
                });
            }*//*
            mProgress.show();
        }

        protected Integer[] doInBackground(Integer... ver) {
            int strDataCnt = PackageVer;
            long start = System.currentTimeMillis();

            try {


                HttpURLConnection conn;
                BufferedInputStream bis = null;


                DownFileName = PackageName + ".main.zip";
                SaveFileName = PackageName + ".main.obb";

//                Log.e("MainAct", Vars.KEY_WEB_SITE + File.separator +"obb"+ File.separator +"main." + PackageVer + "." + PackageName + ".zip");
                URL url = new URL(Vars.KEY_WEB_SITE + File.separator + "obb" + File.separator + DownFileName);

                conn = (HttpURLConnection) url.openConnection();
//						conn.setRequestMethod("POST");
                conn.connect();

                int lenghtOfFile = conn.getContentLength();

                bis = new BufferedInputStream(url.openStream());

                FileOutputStream fos = new FileOutputStream(new File(UpdateAct.this.getObbDir(), SaveFileName));
                long downloadedSize = 0;
                byte[] buffer = new byte[0xFFFF];
                int bufferLength = 0;
                while ((bufferLength = bis.read(buffer)) != -1) {
                    downloadedSize += bufferLength;
                    publishProgress((int) ((downloadedSize * 100) / lenghtOfFile), strDataCnt);
                    fos.write(buffer, 0, bufferLength);
                }
                fos.close();
                bis.close();
                conn.disconnect();
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
            return ver;
        }

        protected void onProgressUpdate(Integer... args)
        {
            mProgress.setProgress(args[0]);
//          mProgress.setMessage("Downloading...");
//			mProgress.setMessage(args[1]+"/"+args[2]+", 진행율:"+args[0]+"%");

            if(args[0]==100)
            {
                //파일하나를 다 받았으면 디비에 저장한다.

            }
        }

        protected void onPostExecute(Integer[] result) {
            if (cnt <= 0) {
                new UpdatePro().execute("KeyLicense");
            }else {
                startActivity(new Intent(UpdateAct.this, MainAct.class));
                finish();
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
            }
            mProgress.dismiss();
        }
    }*/

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                break;
        }
        return true;
    }
}