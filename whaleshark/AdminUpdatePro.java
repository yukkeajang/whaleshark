package com.togetherseatech.whaleshark;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.togetherseatech.whaleshark.Db.History.TrainingDao;
import com.togetherseatech.whaleshark.Db.History.TrainingInfo;
import com.togetherseatech.whaleshark.Db.Training.TrainingsDao;
import com.togetherseatech.whaleshark.util.CommonUtil;
import com.togetherseatech.whaleshark.util.SelectItems;
import com.togetherseatech.whaleshark.util.XmlUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by seonghak on 2017. 12. 28..
 */
public class AdminUpdatePro extends AsyncTask<ArrayList<Map<String,Object>>, Integer, String> {

    private String url = "http://www.togetherseatech.com/Update.do";
//    private String url = "http://192.168.0.8:8080/Update.do";
    private String line = null;
    private ProgressDialog eDialog;
    private Context mContext;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private SelectItems Si;

    public AdminUpdatePro(Context con) {
        mContext = con;
        pref = con.getSharedPreferences("pref", Context.MODE_PRIVATE);
        Si = new SelectItems();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        eDialog = new ProgressDialog(mContext);
        eDialog.setMessage(Si.getMassage("UPDATE", pref.getString("LOGIN_LANGUAGE","")));
        eDialog.setIndeterminate(true);
        eDialog.setCancelable(true);
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
            /*
            StringEntity se = new StringEntity( result, HTTP.UTF_8);
            se.setContentType("text/xml");*/
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

        if(result != null){
//            Log.e("AdminUpdatePro", "result : "+result);
            final StringBuilder Data = new StringBuilder();
            Data.append(result);
            try {
                Element root = XmlUtil.loadDocument(Data.toString());
                int status = Integer.valueOf(XmlUtil.getTagAttribute(root, "return", "status"));
                Log.e("AdminUpdatePro", "status : "+status);
                String msg;
                if(status == 1){
                    msg = "UPDATE_OK";
                    TrainingDao Tdao = new TrainingDao(mContext);
                    TrainingsDao Tsdao = new TrainingsDao(mContext);
                    Tdao.updateData();
                    Tsdao.updateData();
                    AdminUpdateAct.mAdapter.clear();
                    AdminUpdateAct.mAdapter.notifyDataSetChanged();
                }else{
                    msg = "UPDATE_FAIL";
                }
                Si.getMassage(msg, pref.getString("LOGIN_LANGUAGE",""), mContext, Toast.LENGTH_SHORT);
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
            Si.getMassage("UPDATE_FAIL", pref.getString("LOGIN_LANGUAGE",""), mContext, Toast.LENGTH_SHORT);
        }
        eDialog.dismiss();

    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        eDialog.dismiss();
    }
}
