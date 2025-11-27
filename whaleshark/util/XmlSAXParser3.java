package com.togetherseatech.whaleshark.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.togetherseatech.whaleshark.Db.Problem.ProblemInfo;
import com.togetherseatech.whaleshark.UpgradeInfo;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by seonghak on 2018. 2. 27..
 */

public class XmlSAXParser3 {
    private String currentElement;
    private int CHAPTER = 1, Count = 0, Count2 = 0;
    private String xml = null;
    private String str = "";
    private int VER = 1, MOD = 0;
    private String SIZE = "";
    private UpgradeInfo Ui;
    private ArrayList<String> Admin_GroupList;
    private ArrayList<UpgradeInfo> Admin_ChildListContent, Admin_ChildListContentQ;
    private ArrayList<ArrayList<UpgradeInfo>> Admin_ChildList;
    private SelectItems Si;
    private Context mContext;
    // Constructor
    public XmlSAXParser3(Context context, String xml) {
        Si = new SelectItems();
        this.xml = xml;
        this.mContext = context;
        Admin_GroupList = new ArrayList<>();
        Admin_ChildList = new ArrayList<>();
        Admin_ChildListContent = new ArrayList<>();
        Admin_ChildListContentQ = new ArrayList<>();

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            AssetManager aManager = context.getAssets();
            saxParser.parse(aManager.open(xml+".xml"), new MyHandler());
//            File readFile = Si.getZipFile(context, 1, xml+".xml");
//            saxParser.parse(readFile, new MyHandler());
        } catch (Exception e) {
            e.printStackTrace();
//            Log.e("XmlSAXParser", "Exception count : "+ Count);
        }
//        Log.e("XmlSAXParser", "XmlSAXParser End!!");
//        Si.deleteExternalStoragePrivateFileAll(context);
        /*for(int i = 0; i < ProblemList.size();i++){
            Log.e("XmlSAXParser","Chapter : "+ ProblemList.get(i).getChapter());
            Log.e("XmlSAXParser","Level : "+ ProblemList.get(i).getLevel());
            Log.e("XmlSAXParser","No : "+ ProblemList.get(i).getNo());
            Log.e("XmlSAXParser","Vsl_type : "+ ProblemList.get(i).getVsl_type());
            Log.e("XmlSAXParser","Title_kr : "+ ProblemList.get(i).getTitle_kr());
            Log.e("XmlSAXParser","Title_eng : "+ ProblemList.get(i).getTitle_eng());
            Log.e("XmlSAXParser","Answer : "+ ProblemList.get(i).getAnswer());
            Log.e("XmlSAXParser","Relative_regulation : "+ ProblemList.get(i).getRelative_regulation());
            Log.e("XmlSAXParser","Voice_kr : "+ ProblemList.get(i).getVoice_kr());
            Log.e("XmlSAXParser","Voice_eng : "+ ProblemList.get(i).getVoice_eng());
            Log.e("XmlSAXParser","Flash_video : "+ ProblemList.get(i).getFlash_video() == null ? "" : ProblemList.get(i).getFlash_video());
        }

        for(int i = 0; i < SubProblemList.size();i++){
            Log.e("XmlSAXParser","Chapter : "+ SubProblemList.get(i).getChapter());
            Log.e("XmlSAXParser","Level : "+ SubProblemList.get(i).getLevel());
            Log.e("XmlSAXParser","No : "+ SubProblemList.get(i).getNo());
            Log.e("XmlSAXParser","Problem_answer : "+ SubProblemList.get(i).getProblem_answer());
            Log.e("XmlSAXParser","Content_kr : "+ SubProblemList.get(i).getContent_kr());
            Log.e("XmlSAXParser","Content_eng : "+ SubProblemList.get(i).getContent_eng());
        }*/
    }

    public ArrayList<String> getGroupList(){
        return Admin_GroupList;
    }

    public ArrayList<ArrayList<UpgradeInfo>> getChildList(){
        return Admin_ChildList;
    }

    public ArrayList<UpgradeInfo> getChildListQ() {
        return Admin_ChildListContentQ;
    }
    /*
   * Inner class for the Callback Handlers.
   */
    class MyHandler extends DefaultHandler {
        // Callback to handle element start tag

        @Override
        public void startElement(String uri, String localName, String qName,
                                 Attributes attributes) throws SAXException {
            currentElement = qName;

//            Log.e("startElement","Element : "+ currentElement);
            if (currentElement.equals("VERSION_FILE")) {

                VER = Integer.valueOf(attributes.getValue("VER"));
//                Log.e("startElement","VERSION_FILE START " + VER);

            }

            if (currentElement.equals("ROW")) {
//                System.out.println("ROW START " + Count);
                MOD = Integer.valueOf(attributes.getValue("MOD"));
                Ui = new UpgradeInfo();
                Ui.setVer(VER);
                Ui.setMod_ver(MOD);
            }

            if (currentElement.equals("FILE_SIZE")) {
                /*if(Ui.getMenu().equals("Question")) {
                    SIZE = attributes.getValue("SIZE");
                    Ui.setTotal_file_size(SIZE);
                }
                System.out.println("FILE_SIZE START SIZE " + SIZE);*/

            }

        }

        // Callback to handle element end tag
        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
//            Log.e("endElement","Element : "+ qName);

            if (qName.equals("VERSION_FILE")) {
//                System.out.println("NEW_REGULATION END " + Count);
//                Count++;
                if(Admin_ChildListContent.size() > 0 || Admin_ChildListContentQ.size() > 0)
                    Admin_GroupList.add("Ver 1.0"+ VER);

                if(Admin_ChildListContent.size() > 0 ) {
                    Admin_ChildList.add(Admin_ChildListContent);
                }
            }

            if (qName.equals("ROW")) {
//                System.out.println("ROW END " + Count);
//                Log.e("XmlSAXParser3","ROW END = " + Si.hasObbPrivateFile(mContext, Ui.getFile_name(), Ui.getMenu().indexOf("Subtitle") > 0 ? "Subtitle" : ""));
                if(!Si.hasObbPrivateFile(mContext, Ui.getFile_name(), Ui.getMenu().indexOf("Subtitle") > 0 ? "Subtitle" : Ui.getMenu().indexOf("PDF") > 0 ? "PDF" : "")) {
//                    Log.e("XmlSAXParser3","ROW END = " + Ui.getMenu()+"/"+Ui.getFile_name());
                    if(!Ui.getMenu().equals("Question"))
                        Admin_ChildListContent.add(Ui);
                    else
                        Admin_ChildListContentQ.add(Ui);
                }/*else{
                    if(MOD > 0){
                        if(!Ui.getMenu().equals("Question"))
                            Admin_ChildListContent.add(Ui);
                        else
                            Admin_ChildListContentQ.add(Ui);
                    }
                }*/

                Count++;
                Count2 = 0;
            } else if (qName.equals("MENU")) {
                Ui.setMenu(str);
                str = "";
            } else if (qName.equals("FILE_TITLE_KR")) {
                Ui.setFile_title_kr(str);
                str = "";
            } else if (qName.equals("FILE_TITLE_ENG")) {
                Ui.setFile_title_eng(str);
                str = "";
            } else if (qName.equals("FILE_NAME")) {
//                System.out.println("FILE_NAME END " + str);
                String Str = "";
                if(MOD > 0)
                    Str = str + "-" + MOD;
                else
                    Str = str;
                Ui.setFile_name(Str);
                str = "";
            } else if (qName.equals("FILE_SIZE")) {
//                System.out.println("FILE_SIZE END " + str);
                Ui.setFile_size(str);
                str = "";
            }

            currentElement = "";
        }

        // Callback to handle the character text data inside an element
        @Override
        public void characters(char[] chars, int start, int length) throws SAXException {
//            Log.e("characters","Element : "+ currentElement);
            if (currentElement.equals("MENU")) {
//                    System.out.println("\tNO:\t" + new String(chars, start, length));
                str += new String(chars, start, length);
            } else if (currentElement.equals("FILE_TITLE_KR")) {
//                System.out.println("\tTITLE_KR:\t" + new String(chars, start, length));
                str += new String(chars, start, length);
            } else if (currentElement.equals("FILE_TITLE_ENG")) {
//                System.out.println("\tTITLE_KR:\t" + new String(chars, start, length));
                str += new String(chars, start, length);
            } else if (currentElement.equals("FILE_NAME")) {
//                System.out.println("\tTITLE_KR:\t" + new String(chars, start, length));
                str += new String(chars, start, length);
            } else if (currentElement.equals("FILE_SIZE")) {
//                System.out.println("\tTITLE_KR:\t" + new String(chars, start, length));
                str += new String(chars, start, length);
            }
        }
    }
}
