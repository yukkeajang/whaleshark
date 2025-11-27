package com.togetherseatech.whaleshark.util;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.format.Formatter;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.togetherseatech.whaleshark.Db.Member.MemberDao;
import com.togetherseatech.whaleshark.Vars;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class CommonUtil {
	public final static int INET4ADDRESS = 1;
	public final static int INET6ADDRESS = 2;

	public static String checkAvailableConnection(Context c) {
		ConnectivityManager connMgr = (ConnectivityManager)c.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		final NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//		Log.e("checkAvailableConnection", "wifi = "+wifi.isAvailable());
//		Log.e("checkAvailableConnection", "mobile = "+mobile.isAvailable());
		if(wifi.isAvailable()) {
			WifiManager wifiMgr = (WifiManager)c.getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
			int ipAddress = wifiInfo.getIpAddress();
//			Log.e("checkAvailableConnection", "ipAddress = "+ipAddress);
//			Log.e("checkAvailableConnection", "ipAddress2 = "+Formatter.formatIpAddress(ipAddress));
			return Formatter.formatIpAddress(ipAddress);
		} else if(mobile.isAvailable()){
			return getLocalIpAddress(1);
		} 
		
		return null;
	}
	
	private static String getLocalIpAddress(int type) {
		try {
			for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
			   NetworkInterface intf = ( NetworkInterface ) en.nextElement();
			   for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
				   InetAddress inetAddress = ( InetAddress ) enumIpAddr.nextElement();
				   if (!inetAddress.isLoopbackAddress()) {
					   switch (type) {
					   	case INET6ADDRESS:
					   		if (inetAddress instanceof Inet6Address) {
					   			return inetAddress.getHostAddress().toString();
					   		}
					   		break;
					   	case INET4ADDRESS:
					   		if (inetAddress instanceof Inet4Address) {
					   			return inetAddress.getHostAddress().toString();
					   		}
					   		break;
					   }
	     
				   }
			   }
			}
		} catch (SocketException ex) {
		}
		return null;
	}
	
	public static String getEncode(String data) {
		String Data = "";
		Data = Base64.encodeToString(data.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
		return Data;
	}
	
	public static String getDecode(String data) throws UnsupportedEncodingException {
		String Data = "";
		byte[] decodedByteArray = Base64.decode(data.trim(), Base64.DEFAULT);
		Data = new String(decodedByteArray, "UTF-8");
		return Data;
	}
	
	/**
    *
    * 입력된 스트링(s)을 HTML 형태로 변환한다.
    *
    * <pre>
    *
    * [사용 예제]
    *
    * java2Html("\r\n \r\n")
    * ===> <br>
    *			<br>
    *
    * </pre>
    *
    * @param s
    * @return java.lang.String
    */
   public static String java2Html(String s) {
       if (s == null)
           return "";

       StringBuffer buf = new StringBuffer();
       char[] c = s.toCharArray();
       int len = c.length;
       for (int i = 0; i < len; i++) {
           if (c[i] == '&')
               buf.append("&amp;");
           else if (c[i] == '<')
               buf.append("&lt;");
           else if (c[i] == '>')
               buf.append("&gt;");
           else if (c[i] == '"')
               buf.append("&quot;");
           else if (c[i] == '\'')
               buf.append("&#039;");
           else if (c[i] == '\n')
               buf.append("<br>");
           else
               buf.append(c[i]);
       }
       return buf.toString();
   }
   
   public static String Html2Java(String s) {
       String rtnStr = "";
       rtnStr = s.replaceAll("&amp;", "&");
       rtnStr = rtnStr.replaceAll("&lt;", "<");
       rtnStr = rtnStr.replaceAll("&gt;", ">");
       rtnStr = rtnStr.replaceAll("&quot;", "\"");
       rtnStr = rtnStr.replaceAll("&#039;", "'");
       rtnStr = rtnStr.replaceAll("<br>", "\n");
       rtnStr = rtnStr.replaceAll("script", "ＳＣＲＩＰＴ");
       rtnStr = rtnStr.replaceAll("href", "ＨＲＥＦ");
       
       return rtnStr;
   }

   /**
    *
    * 입력된 스트링(str)에서 carriage return 과 new line을 제거한 스트링을 반환한다.
    *
    * @param str
    * @return java.lang.String
    */
   public static String removeCRLF(String str) {
       return stringRemove(str, "\r\n\"");
   }

   public static String stringRemove(String strTarget, String elimination) {
   	if ((strTarget == null) || (strTarget.length() == 0) || (elimination == null))
   		return strTarget;
   	StringBuffer sb = new StringBuffer();
   	StringTokenizer st = new StringTokenizer(strTarget, elimination);
   	while (st.hasMoreTokens()) {
   		sb.append(st.nextToken());
   	}
   	return sb.toString();
   }

	public static String string2Xml(ArrayList<Map<String,Object>> obj)
			throws TransformerFactoryConfigurationError, TransformerException, ParserConfigurationException, DOMException, UnsupportedEncodingException {
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		Node root = document.createElement("SEATECH");
		document.appendChild(root);

//		Element Return = document.createElement("return");
//		Return.setAttribute("status", status);
//		root.appendChild(Return);

		if(obj != null){
			for (int i = 0; i < obj.size(); i++) {
				Element Row = document.createElement("ROW");
				root.appendChild(Row);
				Map<String,Object> item = obj.get(i);
				Set key = item.keySet();
				for (Iterator iterator = key.iterator(); iterator.hasNext();) {
					String keyName = (String) iterator.next();
//	    			String valueName = (String) item.get(keyName);
					String valueName = String.valueOf(item.get(keyName)) ;
//					System.out.println("string2Xml >>>>>>>>>> "+keyName +" = " +valueName);
					Element name = document.createElement(keyName);
					name.appendChild(document.createTextNode(valueName));
					Row.appendChild(name);
				}
			}
		}

		// Document 저장
		DOMSource xmlDOM = new DOMSource(document);

		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		StringWriter sw = new StringWriter();
		StreamResult result = new StreamResult(sw);
		transformer.transform(xmlDOM, result);

		return sw.toString();
	}

	public int getResID(Context con, String resid){
		String name = resid;
//        Log.e("getResID", name);
		int resID = con.getResources().getIdentifier(name, "id", "com.togetherseatech.beluga");
		return resID;
	}

	public void setHideDialog(Dialog d) {
		d.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		d.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
		 if("SM-T510".equals(Build.MODEL) || "SM-T515N".equals(Build.MODEL) || "SM-T500".equals(Build.MODEL) || "SM-T580".equals(Build.MODEL) || "SM-X200".equals(Build.MODEL)) {
			setHideNavigationBar(d.getWindow().getDecorView());
		}
		d.show();
		d.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
	}

	public void setHideNavigationBar(View v) {
		v.setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE
				| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
				| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_FULLSCREEN
				| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
				| View.SYSTEM_UI_FLAG_LOW_PROFILE
				| View.SYSTEM_UI_FLAG_IMMERSIVE
		);
	}

	public void showSystemUI(View v) {
		v.setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE
				| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
	}
}
