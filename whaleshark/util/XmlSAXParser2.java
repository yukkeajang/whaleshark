package com.togetherseatech.whaleshark.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.togetherseatech.whaleshark.Db.Member.MemberDao;
import com.togetherseatech.whaleshark.Db.Member.MemberInfo;
import com.togetherseatech.whaleshark.Db.Problem.ProblemDao;
import com.togetherseatech.whaleshark.Db.Problem.ProblemInfo;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by seonghak on 2018. 2. 27..
 */

public class XmlSAXParser2 {
    private String currentElement;
    private int CHAPTER = 1, CHILD = 0, Count = 0, Count2 = 0;
    private String xml = null;
    private String str = "";
    private int type = 0, type2 = 0;
    private ProblemInfo Pi, Pi2;
    private ArrayList<ProblemInfo> ProblemList;
    private ArrayList<ProblemInfo> SubProblemList;
    private ArrayList<ArrayList<ProblemInfo>> ASubProblemList;
    private SelectItems Si;

    // Constructor
    public XmlSAXParser2(Context context, String xml, int type, int type2) {
        Si = new SelectItems();
        this.xml = xml;
        if("NewRegulation".equals(xml))
            this.type = type + 1;
        else
            this.type = type;
        this.type2 = type2;

        Log.e("XmlSAXParser2","START " + this.type + "/" + this.type2);
        ProblemList = new ArrayList<>();
        SubProblemList = new ArrayList<>();
        ASubProblemList = new ArrayList<>();

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

    public ArrayList<ProblemInfo> getProblemList(){
        return ProblemList;
    }

    public ArrayList<ArrayList<ProblemInfo>> getASubProblemList(){
        return ASubProblemList;
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


            if (currentElement.equals("NEW_REGULATION")) {
                CHAPTER = Integer.valueOf(attributes.getValue("CHAPTER"));
                CHILD = Integer.valueOf(attributes.getValue("CHILD"));
                Log.e("XmlSAXParser2","NEW_REGULATION START " + CHAPTER + "/" + CHILD);
            }

            if (currentElement.equals("GENERAL_TRAINING")) {
                CHAPTER = Integer.valueOf(attributes.getValue("CHAPTER"));
                CHILD = Integer.valueOf(attributes.getValue("CHILD"));
                Log.e("XmlSAXParser2","GENERAL_TRAINING START " + CHAPTER + "/" + CHILD);
            }

            if(type == CHAPTER && type2 == CHILD){
                if (currentElement.equals("ROW")) {
//                System.out.println("ROW START " + Count);
                    Pi = new ProblemInfo();
                }else if (currentElement.equals("SUBPROBLEM")) {
//                System.out.println("SROW START " + Count2);
                    SubProblemList = new ArrayList<>();
                }else if (currentElement.equals("SROW")) {
//                System.out.println("SROW START " + Count2);
                    Pi2 = new ProblemInfo();
                }
            }

        }

        // Callback to handle element end tag
        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
//            Log.e("endElement","Element : "+ qName);

            if (qName.equals("NEW_REGULATION")) {
//                System.out.println("NEW_REGULATION END " + Count);
//                Count++;
            }

            if (qName.equals("GENERAL_TRAINING")) {
//                System.out.println("GENERAL_TRAINING END " + Count);
//                Count++;
            }

            if(type == CHAPTER && type2 == CHILD){
                if (qName.equals("ROW")) {
                System.out.println("ROW END " + Count);
                    ProblemList.add(Pi);
                    Count++;
                    Count2 = 0;
                }else if (qName.equals("SUBPROBLEM")) {
                    ASubProblemList.add(SubProblemList);
                }else if (qName.equals("NO")) {
                    System.out.println("NO END " + str);
                    Pi.setNo(Integer.valueOf(str));
                    str = "";
                } else if (qName.equals("TITLE_KR")) {
                    Pi.setTitle_kr(str);
                    str = "";
                } else if (qName.equals("TITLE_ENG")) {
                    Pi.setTitle_eng(str);
                    str = "";
                } else if (qName.equals("ANSWER")) {
                    Pi.setAnswer(str);
                    str = "";
                } else if (qName.equals("VOICE_KR")) {
                    Pi.setVoice_kr(str);
                    str = "";
                } else if (qName.equals("VOICE_ENG")) {
                    Pi.setVoice_eng(str);
                    str = "";
                } else if (qName.equals("FLASH_VIDEO")) {
                    Pi.setFlash_video(str);
                    str = "";
                }else if (qName.equals("SROW")) {
                    Pi2.setChapter(Pi.getChapter());
                    Pi2.setLevel(Pi.getLevel());
                    Pi2.setNo(Pi.getNo());
                    SubProblemList.add(Pi2);
                    Count2 ++;
                } else if (qName.equals("PROBLEM_ANSWER")) {
                    Pi2.setProblem_answer(str);
                    str = "";
                } else if (qName.equals("CONTENT_KR")) {
                    Pi2.setContent_kr(str);
                    str = "";
                } else if (qName.equals("CONTENT_ENG")) {
                    Pi2.setContent_eng(str);
                    str = "";
                }
            }
            currentElement = "";
        }

        // Callback to handle the character text data inside an element
        @Override
        public void characters(char[] chars, int start, int length) throws SAXException {
//            Log.e("characters","Element : "+ currentEleme nt);
            if(type == CHAPTER && type2 == CHILD){
                if (currentElement.equals("NO")) {
                System.out.println("\tNO:\t" + new String(chars, start, length));
                    str += new String(chars, start, length);
                } else if (currentElement.equals("TITLE_KR")) {
//                System.out.println("\tTITLE_KR:\t" + new String(chars, start, length));
                    str += new String(chars, start, length);
                } else if (currentElement.equals("TITLE_ENG")) {
//                System.out.println("\tTITLE_ENG:\t" + new String(chars, start, length));
                    str += new String(chars, start, length);
                } else if (currentElement.equals("ANSWER")) {
//                System.out.println("\tANSWER:\t" + new String(chars, start, length));
                    str += new String(chars, start, length);
                } else if (currentElement.equals("VOICE_KR")) {
//                System.out.println("\tVOICE_KR:\t" + new String(chars, start, length));
                    str += new String(chars, start, length);
                } else if (currentElement.equals("VOICE_ENG")) {
//                System.out.println("\tVOICE_ENG:\t" + new String(chars, start, length));
                    str += new String(chars, start, length);
                } else if (currentElement.equals("FLASH_VIDEO")) {
//                System.out.println("\tVOICE_ENG:\t" + new String(chars, start, length));
                    str += new String(chars, start, length);
                } else if (currentElement.equals("PROBLEM_ANSWER")) {
//                System.out.println("\tPROBLEM_ANSWER:\t" + new String(chars, start, length));
                    str += new String(chars, start, length);
                } else if (currentElement.equals("CONTENT_KR")) {
//                System.out.println("\tCONTENT_KR:\t" + new String(chars, start, length));
                    str += new String(chars, start, length);
                } else if (currentElement.equals("CONTENT_ENG")) {
//                System.out.println("\tCONTENT_ENG:\t" + new String(chars, start, length));
                    str += new String(chars, start, length);
                }
            }
        }
    }
}
