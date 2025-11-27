package com.togetherseatech.whaleshark.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.util.Log;

import com.togetherseatech.whaleshark.Db.Member.MemberDao;
import com.togetherseatech.whaleshark.Db.Member.MemberInfo;
import com.togetherseatech.whaleshark.Db.Problem.ProblemDao;
import com.togetherseatech.whaleshark.Db.Problem.ProblemInfo;
import com.togetherseatech.whaleshark.UpdateAct;

import org.xml.sax.InputSource;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by seonghak on 2018. 2. 27..
 */

public class XmlSAXParser {
    private String currentElement;
    private int Count = 0, Count2 = 0;
    private String xml = null;
    private String str = "";
    private ProblemInfo Pi, Pi2;
    private MemberInfo Mi;
    private ArrayList<MemberInfo> MemberList;
    private ArrayList<ProblemInfo> ProblemList;
    private ArrayList<ProblemInfo> SubProblemList;
    private SelectItems Si;
    // Constructor
    public XmlSAXParser(Context context, String xml) {
        Si = new SelectItems();
        this.xml = xml;

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            if("VslMember".equals(xml)) {
                MemberList = new ArrayList<>();
            }else if("Problem".equals(xml)) {
                ProblemList = new ArrayList<>();
                SubProblemList = new ArrayList<>();
//                File readFile = Si.getZipFile(context, 1, xml+".xml");
//                saxParser.parse(readFile, new MyHandler());
            }

            AssetManager aManager = context.getAssets();
            saxParser.parse(aManager.open(xml+".xml"), new MyHandler());
        } catch (Exception e) {
            e.printStackTrace();
//            Log.e("XmlSAXParser", "Exception count : "+ Count);
        }
//        Log.e("XmlSAXParser", "XmlSAXParser End!!");
        Si.deleteExternalStoragePrivateFileAll(context);

        if("VslMember".equals(xml)){
            MemberDao Mdao = new MemberDao(context);
            Mdao.addVslMembers(MemberList);
        }else if("Problem".equals(xml)) {
            Log.e("test", "xml이름 같을때 추가");
            ProblemDao Pdao = new ProblemDao(context);
            Pdao.addProblems(ProblemList);
            Pdao.addSubProblems(SubProblemList);
        }


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
            if (currentElement.equals("ROW")) {
//                System.out.println("ROW START " + Count);
                if("VslMember".equals(xml))
                    Mi = new MemberInfo();
                else if("Problem".equals(xml))
                    Pi = new ProblemInfo();
            }else if (currentElement.equals("SROW")) {
//                System.out.println("SROW START " + Count2);
                Pi2 = new ProblemInfo();
            }
        }

        // Callback to handle element end tag
        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
//            Log.e("endElement","Element : "+ qName);
            if (qName.equals("ROW")) {
//                System.out.println("ROW END " + Count);
                if("VslMember".equals(xml)){
                    MemberList.add(Mi);
                } else if("Problem".equals(xml)){
                    ProblemList.add(Pi);
                    Count2 = 0;
                }
                Count++;
            }else if (qName.equals("CHAPTER")) {
                Pi.setChapter(Integer.valueOf(str));
                str = "";
            }else if (qName.equals("LEVEL")) {
               Pi.setLevel(Integer.valueOf(str));
                str = "";
            } else if (qName.equals("NO")) {
//                System.out.println("NO END " + str);
                Pi.setNo(Integer.valueOf(str));
                str = "";
            } else if (qName.equals("VSL_TYPE")) {
                if("VslMember".equals(xml))
                    Mi.setVsl_type(!"".equals(str.trim()) ? Integer.valueOf(str) : 0);
                else if("Problem".equals(xml))
                    Pi.setVsl_type(Integer.valueOf(str));
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
            } else if (qName.equals("RELATIVE_REGULATION")) {
                Pi.setRelative_regulation(str);
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
//                System.out.println("SROW END " + Count2);
                Pi2.setChapter(Pi.getChapter());
                Pi2.setLevel(Pi.getLevel());
                Pi2.setNo(Pi.getNo());
                SubProblemList.add(Pi2);
                Count2++;
            } else if (qName.equals("PROBLEM_ANSWER")) {
                Pi2.setProblem_answer(str);
                str = "";
            } else if (qName.equals("CONTENT_KR")) {
                Pi2.setContent_kr(str);
                str = "";
            } else if (qName.equals("CONTENT_ENG")) {
                Pi2.setContent_eng(str);
                str = "";
            } else if (qName.equals("MASTER_IDX")) {
                Mi.setMaster_idx(!"".equals(str.trim()) ? Integer.valueOf(str) : 0);
                str = "";
            } else if (qName.equals("AUTH")) {
                Mi.setAuth(!"".equals(str.trim()) ? str : "");
                str = "";
            } else if (qName.equals("ID")) {
                Mi.setId(!"".equals(str.trim()) ? str : "");
                str = "";
            } else if (qName.equals("PW")) {
                Mi.setPw(!"".equals(str.trim()) ? str : "");
                str = "";
            } else if (qName.equals("VSL")) {
                Mi.setVsl(!"".equals(str.trim()) ? str : "");
                str = "";
            }

            currentElement = "";
        }

        // Callback to handle the character text data inside an element
        @Override
        public void characters(char[] chars, int start, int length) throws SAXException {
//            Log.e("characters","Element : "+ currentElement);
            if (currentElement.equals("CHAPTER")) {
//                System.out.println("\tCHAPTER:\t" + new String(chars, start, length));
                str += new String(chars, start, length);
            } else if (currentElement.equals("LEVEL")) {
//                System.out.println("\tLEVEL:\t" + new String(chars, start, length));
                str += new String(chars, start, length);
            } else if (currentElement.equals("NO")) {
//                System.out.println("\tNO:\t" + new String(chars, start, length));
                str += new String(chars, start, length);
            } else if (currentElement.equals("VSL_TYPE")) {
//                System.out.println("\tVSL_TYPE:\t" + new String(chars, start, length));
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
            } else if (currentElement.equals("RELATIVE_REGULATION")) {
//                System.out.println("\tRELATIVE_REGULATION:\t" + new String(chars, start, length));
                str += new String(chars, start, length);
            } else if (currentElement.equals("VOICE_KR")) {
//                System.out.println("\tVOICE_KR:\t" + new String(chars, start, length));
                str += new String(chars, start, length);
            } else if (currentElement.equals("VOICE_ENG")) {
//                System.out.println("\tVOICE_ENG:\t" + new String(chars, start, length));
                str += new String(chars, start, length);
            } else if (currentElement.equals("FLASH_VIDEO")) {
//                System.out.println("\tFLASH_VIDEO:\t" + new String(chars, start, length));
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
            } else if (currentElement.equals("PROBLEM_ANSWER")) {
//                System.out.println("\tCONTENT_ENG:\t" + new String(chars, start, length));
                str += new String(chars, start, length);
            } else if (currentElement.equals("CONTENT_KR")) {
//                System.out.println("\tCONTENT_ENG:\t" + new String(chars, start, length));
                str += new String(chars, start, length);
            } else if (currentElement.equals("CONTENT_ENG")) {
//                System.out.println("\tCONTENT_ENG:\t" + new String(chars, start, length));
                str += new String(chars, start, length);
            } else if (currentElement.equals("MASTER_IDX")) {
//                System.out.println("\tCONTENT_ENG:\t" + new String(chars, start, length));
                str += new String(chars, start, length);
            } else if (currentElement.equals("AUTH")) {
//                System.out.println("\tCONTENT_ENG:\t" + new String(chars, start, length));
                str += new String(chars, start, length);
            } else if (currentElement.equals("ID")) {
//                System.out.println("\tCONTENT_ENG:\t" + new String(chars, start, length));
                str += new String(chars, start, length);
            } else if (currentElement.equals("PW")) {
//                System.out.println("\tCONTENT_ENG:\t" + new String(chars, start, length));
                str += new String(chars, start, length);
            } else if (currentElement.equals("VSL")) {
//                System.out.println("\tCONTENT_ENG:\t" + new String(chars, start, length));
                str += new String(chars, start, length);
            }
        }
    }
}
