package com.togetherseatech.whaleshark.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.view.WindowManager;
import android.widget.Toast;

import com.togetherseatech.whaleshark.Db.History.TrainingDao;
import com.togetherseatech.whaleshark.Db.History.TrainingInfo;
import com.togetherseatech.whaleshark.Db.Member.MemberDao;
import com.togetherseatech.whaleshark.Db.Member.MemberInfo;
import com.togetherseatech.whaleshark.Db.Training.TrainingsDao;
import com.togetherseatech.whaleshark.Db.Training.TrainingsInfo;
import com.togetherseatech.whaleshark.Vars;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

public class AllUpdate {
    Map item2;
    Activity Act;
    public AllUpdate(Activity Act) {
        this.Act = Act;
        SelectItems Si = new SelectItems();
        TrainingDao Tdao = new TrainingDao(Act);
        ArrayList<Map<String, Object>> updateArray = new ArrayList<>();
        ArrayList<Map<String, Object>> updateArray2 = new ArrayList<>();
        ArrayList<Map<String, Object>> updateArray3 = new ArrayList<>();
        ArrayList<Map<String, Object>> updateArray4 = new ArrayList<>();
        MemberDao Mdao = new MemberDao(Act);
        MemberInfo VslMi = Mdao.getVslMember("ADMIN", "Administration");

        ArrayList<TrainingInfo> Ti = Tdao.getAllDownloadData(VslMi.getVsl_type());

        if(Ti.size() > 0){
            for(int i = 0; i < Ti.size();i++){
                Map item = new HashMap();
                item.put(Vars.KEY_IDX, Ti.get(i).getIdx());
                item.put(Vars.KEY_MASTER_IDX, Ti.get(i).getMaster_idx());
                item.put(Vars.KEY_RANK, Ti.get(i).getRank());
                item.put(Vars.KEY_FNAME, Ti.get(i).getFirstName());
                item.put(Vars.KEY_SNAME, Ti.get(i).getSurName());
                item.put(Vars.KEY_VSL_NAME, Ti.get(i).getVsl());
                item.put(Vars.KEY_VSL_TYPE, Ti.get(i).getVsl_type());
                item.put(Vars.KEY_BIRTH, Ti.get(i).getBirth());
                item.put(Vars.KEY_NATIONAL, Ti.get(i).getNational());
                item.put(Vars.KEY_SIGN_ON, Ti.get(i).getSign_on());
                item.put(Vars.KEY_SIGN_OFF, Ti.get(i).getSign_off());
                item.put(Vars.KEY_TRAINING_COURSE, (Ti.get(i).getTraining_course()+1));
                item.put(Vars.KEY_YEAR, Ti.get(i).getYear());
                item.put(Vars.KEY_QUARTER, Ti.get(i).getQuarter());
                item.put(Vars.KEY_DATE, Ti.get(i).getDate());
                item.put(Vars.KEY_TIME, Ti.get(i).getTime());
                item.put(Vars.KEY_SCORE, Ti.get(i).getScore());
                updateArray.add(item);
                List<TrainingInfo> TiRr = Tdao.getProblems(Ti.get(i).getIdx());

                if(TiRr.size() > 0) {
                    for (int j = 0; j < TiRr.size(); j++) {
                        if(TiRr.get(j).getRelative_regulation().trim().length() > 0) {
                            item2 = new HashMap();
                            item2.put(Vars.KEY_HISTORY_IDX, TiRr.get(j).getHistroy_idx());
                            item2.put(Vars.KEY_RELATIVE_REGULATION, TiRr.get(j).getRelative_regulation().trim());

                            updateArray2.add(item2);
                        }
                    }
                }else{
                    item2 = new HashMap();
                    item2.put(Vars.KEY_HISTORY_IDX, item.get(Vars.KEY_IDX));
                    item2.put(Vars.KEY_RELATIVE_REGULATION, " ");
                    updateArray2.add(item2);
                }
            }
        }

        TrainingsDao Tsdao = new TrainingsDao(Act);
        ArrayList<TrainingsInfo> Tsi = Tsdao.getAllDownloadData(VslMi.getVsl_type());

        if(Tsi.size() > 0){
            for(int i = 0; i < Tsi.size();i++){

                List<TrainingsInfo> TsiRr = Tsdao.getHistoryMember(Tsi.get(i).getIdx());

                for (int j = 0; j < TsiRr.size(); j++) {
                    item2 = new HashMap();
                    item2.put(Vars.KEY_HISTORY_IDX, TsiRr.get(j).getHistroy_idx());
                    item2.put(Vars.KEY_TYPE, Tsi.get(i).getType());
                    item2.put(Vars.KEY_RANK, TsiRr.get(j).getRank());
                    item2.put(Vars.KEY_SNAME, TsiRr.get(j).getSurName());
                    item2.put(Vars.KEY_FNAME, TsiRr.get(j).getFirstName());
                    updateArray4.add(item2);
                }

                Map item = new HashMap();
                item.put(Vars.KEY_IDX, Tsi.get(i).getIdx());
                item.put(Vars.KEY_MASTER_IDX, TsiRr.get(0).getMaster_idx());
                item.put(Vars.KEY_TYPE, Tsi.get(i).getType());
                item.put(Vars.KEY_VSL_NAME, Tsi.get(i).getVsl());
                item.put(Vars.KEY_VSL_TYPE, Tsi.get(i).getVsl_type());
                if(Tsi.get(i).getType().equals("R"))
                    item.put(Vars.KEY_TRAINING_COURSE, (Si.getNewRegulation(Tsi.get(i).getTraining_course(), "ENG")));
                else if(Tsi.get(i).getType().equals("G"))
                    item.put(Vars.KEY_TRAINING_COURSE, (Si.getGeneralTraining(Tsi.get(i).getTraining_course(), Tsi.get(i).getTraining_course2(), "ENG")));
                item.put(Vars.KEY_DATE, Tsi.get(i).getDate());
                item.put(Vars.KEY_SCORE, Tsi.get(i).getScore());
                updateArray3.add(item);
            }
        }
        new AllUpdatePro(Act).execute(updateArray, updateArray2, updateArray3, updateArray4);
    }

    public class AllUpdatePro extends AsyncTask<ArrayList<Map<String,Object>>, Integer, String> {

        private String url = "http://www.togetherseatech.com/Update.do";
        //    private String url = "http://192.168.0.8:8080/Update.do";
        private String line = null;
        private ProgressDialog eDialog;
        private Context mContext;
        private SharedPreferences pref;
        private SharedPreferences.Editor editor;
        private SelectItems Si;

        public AllUpdatePro(Context con) {
            mContext = con;
            pref = con.getSharedPreferences("pref", Context.MODE_PRIVATE);
            Si = new SelectItems();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            eDialog = new ProgressDialog(mContext);
            eDialog.setMessage("프로그램 최적화중...");
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
        protected String doInBackground(ArrayList<Map<String,Object>>... arrayLists) {
            try {
                String result = XmlUtil.string2Xml(arrayLists[0]);
                String result2 = XmlUtil.string2Xml(arrayLists[1]);
                String result3 = XmlUtil.string2Xml(arrayLists[2]);
                String result4 = XmlUtil.string2Xml(arrayLists[3]);
//            Log.e("doInBackground", result);
//            Log.e("doInBackground", result2);
//            Log.e("doInBackground", result3);
//            Log.e("doInBackground", result4);
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("DATA", result));
                nameValuePairs.add(new BasicNameValuePair("DATA2", result2));
                nameValuePairs.add(new BasicNameValuePair("DATA3", result3));
                nameValuePairs.add(new BasicNameValuePair("DATA4", result4));

                HttpClient httpClient = new DefaultHttpClient();
                HttpContext httpContext = new BasicHttpContext();
                HttpPost httpPost = new HttpPost(url);

                UrlEncodedFormEntity entityRequest = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");

                httpPost.setEntity(entityRequest);
                HttpResponse response = httpClient.execute(httpPost,httpContext);
                HttpEntity resEntity = response.getEntity();
                line =  EntityUtils.toString(resEntity, "UTF-8").trim();
            } catch (Exception e) {
//            Log.e("AdminUpdatePro", "Exception!!! = "+e);
                return line = null;
            }

            return line;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            int status = 0;
            if(result != null){
                final StringBuilder Data = new StringBuilder();
                Data.append(result);
                try {
                    Element root = XmlUtil.loadDocument(Data.toString());
                    status = Integer.valueOf(XmlUtil.getTagAttribute(root, "return", "status"));
                    String msg;
                    if(status == 1){
                        msg = "최적화가 완료되었습니다.";
                        editor = pref.edit();
                        editor.putInt("ALLDOWNLOAD", 1);
                        editor.commit();
                    }else{
                        msg = "최적화가 실패하였습니다. 인터넷이 접속되었는지 확인 바랍니다.";
                        editor = pref.edit();
                        editor.putInt("ALLDOWNLOAD", 0);
                        editor.commit();
                    }

                    Si.getToast(mContext,msg,Toast.LENGTH_SHORT);

                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
//                System.out.println("ParserConfigurationException :"+e.toString());
                } catch (SAXException e) {
                    e.printStackTrace();
//                System.out.println("SAXException :"+e.toString());
                } catch (IOException e) {
                    e.printStackTrace();
//                System.out.println("IOException :"+e.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                Si.getToast(mContext,"최적화가 실패하였습니다. 인터넷이 접속되었는지 확인 바랍니다.",Toast.LENGTH_SHORT);
            }
            eDialog.dismiss();
            if(status == 0)
                Act.finish();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            eDialog.dismiss();
        }
    }
}
