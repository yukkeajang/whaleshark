package com.togetherseatech.whaleshark.util;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.expansion.zipfile.APKExpansionSupport;
import com.android.vending.expansion.zipfile.ZipResourceFile;
import com.togetherseatech.whaleshark.Db.Member.MemberDao;
import com.togetherseatech.whaleshark.Db.Member.MemberInfo;
import com.togetherseatech.whaleshark.Db.Training.TrainingsInfo;
import com.togetherseatech.whaleshark.R;
import com.togetherseatech.whaleshark.Vars;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.progress.ProgressMonitor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by seonghak on 2017. 11. 1..
 */

public class SelectItems implements Comparable {

    int idx;
    String kr;
    String eng;
    int rankImg;
    int rankChild;
    Boolean isChecked, isMChecked;
    private ProgressDialog eDialog;

    public SelectItems(){

    }

    public SelectItems(String kr, String eng){
        this.kr = kr;
        this.eng = eng;
    }

    public SelectItems(String kr, String eng, Boolean isMChecked){
        this.kr = kr;
        this.eng = eng;
        this.isMChecked = isMChecked;
    }

    public SelectItems(int idx, String kr, String eng){
        this.idx = idx;
        this.kr = kr;
        this.eng = eng;
    }

    public SelectItems(int idx, String kr, String eng, Boolean isChecked){
        this.idx = idx;
        this.kr = kr;
        this.eng = eng;
        this.isChecked = isChecked;
    }

    public SelectItems(String kr, String eng, int rankImg){
        this.kr = kr;
        this.eng = eng;
        this.rankImg = rankImg;
    }

    public SelectItems(int rankImg, int rankChild, String kr, String eng){
        this.kr = kr;
        this.eng = eng;
        this.rankImg = rankImg;
        this.rankChild = rankChild;

    }

    public SelectItems(String kr, String eng, int rankImg, Boolean isChecked){
        this.kr = kr;
        this.eng = eng;
        this.rankImg = rankImg;
        this.isChecked = isChecked;

    }

    public SelectItems(String kr, String eng, int rankImg, int rankChild, Boolean isChecked){
        this.kr = kr;
        this.eng = eng;
        this.rankImg = rankImg;
        this.rankChild = rankChild;
        this.isChecked = isChecked;

    }


    public SelectItems(String kr, String eng, int rankImg, int rankChild, Boolean isChecked, Boolean isMChecked){
        this.kr = kr;
        this.eng = eng;
        this.rankImg = rankImg;
        this.rankChild = rankChild;
        this.isChecked = isChecked;
        this.isMChecked = isMChecked;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getKr() {
        return kr;
    }

    public void setKr(String kr) {
        this.kr = kr;
    }

    public String getEng() {
        return eng;
    }

    public void setEng(String eng) {
        this.eng = eng;
    }

    public int getRankImg() {
        return rankImg;
    }

    public void setRankImg(int rankImg) {
        this.rankImg = rankImg;
    }

    public int getRankChild() {
        return rankChild;
    }

    public void setRankChild(int rankChild) {
        this.rankChild = rankChild;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public Boolean getMChecked() {
        return isMChecked;
    }

    public void setMChecked(Boolean checked) {
        isMChecked = checked;
    }

    public ArrayList<SelectItems> getSelectMemberRank(){

        ArrayList<SelectItems> Mb = new ArrayList<SelectItems>();

        Mb.add(new SelectItems("- 선택하세요 -", "- Please select -"));
        Mb.add(new SelectItems("선장", "Capt", R.mipmap.rank1));
        Mb.add(new SelectItems("일항사", "C/O", R.mipmap.rank2 ));
        Mb.add(new SelectItems("이항사", "2/O", R.mipmap.rank3));
        Mb.add(new SelectItems("삼항사", "3/O", R.mipmap.rank4));
        Mb.add(new SelectItems("기관장", "C/E", R.mipmap.rank1));
        Mb.add(new SelectItems("일기사", "1/E", R.mipmap.rank2));
        Mb.add(new SelectItems("이기사", "2/E", R.mipmap.rank3));
        Mb.add(new SelectItems("삼기사", "3/E", R.mipmap.rank4));
        Mb.add(new SelectItems("갑판장", "BSN", R.mipmap.rank5));
        Mb.add(new SelectItems("갑판수", "Q/M", R.mipmap.rank5));
        Mb.add(new SelectItems("갑판원", "OS", R.mipmap.rank5));
        Mb.add(new SelectItems("조기장", "No.1 OLR", R.mipmap.rank5));
        Mb.add(new SelectItems("조기수", "OLR", R.mipmap.rank5));
        Mb.add(new SelectItems("조기원", "WR", R.mipmap.rank5));
        Mb.add(new SelectItems("조리장", "C/S", R.mipmap.rank5));
        Mb.add(new SelectItems("조리수", "Cook", R.mipmap.rank5));
        Mb.add(new SelectItems("조리원", "M/B", R.mipmap.rank5));
        Mb.add(new SelectItems("실항사", "A/O", R.mipmap.rank5));
        Mb.add(new SelectItems("실기사", "A/E", R.mipmap.rank5));

        return Mb;
    }

    public String getMemberRank(int idx, String type){

        String RankName = null;
        ArrayList<SelectItems> Mb = getSelectMemberRank();

        if("KR".equals(type))
            RankName = Mb.get(idx).getKr();
        else if("ENG".equals(type))
            RankName = Mb.get(idx).getEng();

        return RankName;
    }

    public int getMemberRankImg(int idx){

        ArrayList<SelectItems> Mb = getSelectMemberRank();

        return Mb.get(idx).getRankImg();
    }

    public ArrayList<SelectItems> getSelectMemberNational(){

        ArrayList<SelectItems> Mb = new ArrayList<SelectItems>();

        Mb.add(new SelectItems("- 선택하세요 -", "- Please select -"));
        Mb.add(new SelectItems("대한민국", "Korea"));
        Mb.add(new SelectItems("미얀마", "Myanmar"));
        Mb.add(new SelectItems("태국", "Thailand"));
        Mb.add(new SelectItems("중국", "China"));
        Mb.add(new SelectItems("필리핀", "Philippines"));
        Mb.add(new SelectItems("베트남", "Vietnam"));
        Mb.add(new SelectItems("방글라데시", "Bangladesh"));
        Mb.add(new SelectItems("일본", "Japan"));
        Mb.add(new SelectItems("인도", "India"));
        Mb.add(new SelectItems("캄보디아", "Cambodia"));
        Mb.add(new SelectItems("인도네시아", "Indonesia"));
        Mb.add(new SelectItems("말레이시아", "Malaysia"));
        Mb.add(new SelectItems("싱가폴", "Singapore"));
        Mb.add(new SelectItems("대만", "Taiwan"));
        Mb.add(new SelectItems("러시아", "Russia"));

        return Mb;
    }

    public String getMemberNational(int idx, String type){

        String NationalName = null;
        ArrayList<SelectItems> Mb = getSelectMemberNational();

        if("KR".equals(type))
            NationalName = Mb.get(idx).getKr();
        else if("ENG".equals(type))
            NationalName = Mb.get(idx).getEng();

        return NationalName;
    }

    public ArrayList<SelectItems> getSelectMemberVslType(){
        ArrayList<SelectItems> Mb = new ArrayList<SelectItems>();
        Mb.add(new SelectItems("", ""));
        Mb.add(new SelectItems("석유", "Petroleum"));
        Mb.add(new SelectItems("석유 및 화학", "Oil & Chemical"));
        Mb.add(new SelectItems("가스", "Gas"));
        // 2305 일반선박 추가
        Mb.add(new SelectItems("일반", "General"));

        return Mb;
    }

    public ArrayList<String> getSelectMemberVslType2(){
        ArrayList<String> Mb = new ArrayList<String>();
        Mb.add("");
        Mb.add("Petroleum");
        Mb.add("Oil & Chemical");
        Mb.add("Gas");
        // 2305 일반선박 추가
        Mb.add("General");


        return Mb;
    }

    public ArrayList<String> getSelectMemberVslType3(){
        ArrayList<String> Mb = new ArrayList<String>();
        Mb.add("- Please select -");
        Mb.add("Petroleum");
        Mb.add("Oil & Chemical");
        Mb.add("Gas");
        // 2305 일반선박 추가
        Mb.add("General");

        return Mb;
    }
    /**
     * Ex
     * 1. Petroleum
     * 2. Oil & Chemical
     * 3. Gas
     * 4. Petroleum, Oil & Chemical
     * 5. Petroleum, Gas
     * 6. Oil & Chemical, Gas
     * 7. Petroleum, Oil & Chemical, Gas
     * @param vsl_type
     * @return
     */

    public String getProblemVslType(int vsl_type){
        String vsl = null;

        switch (vsl_type){
            case 1:
                vsl = "1, 4, 5, 7";
                break;
            case 2:
                vsl = "2, 4, 6, 7";
                break;
            case 3:
                vsl = "3, 5, 6, 7";
                break;
            // 2305 일반선박 추가
            case 4:
                vsl = "4, 5, 6, 7";
                break;
        }

        return vsl;
    }

    public String getSelectMemberVslType(int idx, String type){

        String VslTypeName = null;
        ArrayList<SelectItems> Mb = getSelectMemberVslType();

        if("KR".equals(type))
            VslTypeName = Mb.get(idx).getKr();
        else if("ENG".equals(type))
            VslTypeName = Mb.get(idx).getEng();

        return VslTypeName;
    }

    public ArrayList<SelectItems> getChapter(){
        ArrayList<SelectItems> Mb = new ArrayList<SelectItems>();
        Mb.add(new SelectItems("비상대응 및 훈련", "Emergency Respons and Drill"));
        Mb.add(new SelectItems("항해", "Navigation"));
//                Mb.add(new SelectItems("통신", "Communication"));
        Mb.add(new SelectItems("화물 취급 및 작업 그리고 탱크 세정", "Cargo Handing & Operation & Tank cleaning"));
        Mb.add(new SelectItems("계류작업", "Mooring"));
//        Mb.add(new SelectItems("기름", "Petroleum"));
//        Mb.add(new SelectItems("화학제품", "Chemical"));
//        Mb.add(new SelectItems("가스", "Gas"));
        Mb.add(new SelectItems("기관실", "Machiery Space"));
        Mb.add(new SelectItems("소화 및 구명", "Fire fighting & Life saving"));
        Mb.add(new SelectItems("위험성 평가 및 사고조사", "Risk assessment & Investigation"));
        Mb.add(new SelectItems("환경", "Envirnmental"));
        Mb.add(new SelectItems("보안", "Security"));
        Mb.add(new SelectItems("에너지 보존", "Energy conservation"));
        Mb.add(new SelectItems("ISM (서류 및 관리)", "ISM (Document & Control)"));
        Mb.add(new SelectItems("위생", "Hygiene"));
        return Mb;
    }
    public String getChapter(int idx, String type){
        String ChapterName = null;
        ArrayList<SelectItems> Mb = getChapter();
        if("KR".equals(type))
            ChapterName = Mb.get(idx).getKr();
        else if("ENG".equals(type))
            ChapterName = Mb.get(idx).getEng();
        return ChapterName;
    }
    public ArrayList<SelectItems> getRankChapter(int rank, int type){
//        Log.e("getChapter", "rank : "+ rank);
        ArrayList<SelectItems> Mb = new ArrayList<SelectItems>();
        Mb.add(new SelectItems(0,"비상대응 및 훈련", "Emergency Respons and Drill", false));
        Mb.add(new SelectItems(5,"소화 및 구명", "Fire fighting & Life saving", false));
        Mb.add(new SelectItems(6,"위험성 평가 및 사고조사", "Risk assessment & Investigation", false));
        Mb.add(new SelectItems(7,"환경", "Environment", false));
        Mb.add(new SelectItems(8,"보안", "Security", false));
        Mb.add(new SelectItems(9,"에너지 보존", "Energy conservation", false));
        Mb.add(new SelectItems(10,"ISM (서류 및 관리)", "ISM (Document & Control)", false));
        Mb.add(new SelectItems(11,"위생", "Hygiene", false));
        switch(rank){
            case 1:     //선장
            case 2:     //일항사
            case 3:     //이항사
            case 4:     //삼항사
            case 9:     //갑판장
            case 10:    //갑판수
            case 11:    //갑판원
            case 18:    //실항사
                Mb.add(new SelectItems(1,"항해", "Navigation", false));
//                Mb.add(new SelectItems("통신", "Communication"));
                if(type != 3)
                    Mb.add(new SelectItems(2,"화물 취급 및 작업 그리고 탱크 세정", "Cargo Handing & Operation & Tank cleaning", false));
                else
                    Mb.add(new SelectItems(2,"화물 취급 및 작업", "Cargo Handing & Operation", false));
                Mb.add(new SelectItems(3,"계류작업", "Mooring", false));
//        Mb.add(new SelectItems("기름", "Petroleum"));
//        Mb.add(new SelectItems("화학제품", "Chemical"));
//        Mb.add(new SelectItems("가스", "Gas"));
                break;
            case 5:     //기관장
            case 6:     //일기사
            case 7:     //이기사
            case 8:     //삼기사
            case 12:    //조기장
            case 13:    //조기수
            case 14:    //조기원
            case 19:    //실기사
                Mb.add(new SelectItems(4,"기관실", "Machiery Space", false));
                break;
            case 15:    //조리장
            case 16:    //조리수
            case 17:    //조리원
                break;
        }
        /*for(SelectItems str: Mb){
            System.out.println(str.getIdx()+"/"+str.getKr());
        }
        System.out.println("//////////////////////////////");*/
        Collections.sort(Mb);

        /*for(SelectItems str: Mb){
            System.out.println(str.getIdx()+"/"+str.getKr());
        }*/
        return Mb;
    }

    @Override
    public int compareTo(@NonNull Object another) {
        int compareage = ((SelectItems)another).getIdx();
        return this.idx - compareage;
    }

    public ArrayList<SelectItems> getLevel(){
        ArrayList<SelectItems> Mb = new ArrayList<SelectItems>();
        Mb.add(new SelectItems("낮음", "Low"));
        Mb.add(new SelectItems("중간", "Middle"));
        Mb.add(new SelectItems("높음", "High"));
        Mb.add(new SelectItems("전체", "FIX"));
        return Mb;
    }

    public String getLevel(int idx, String type){

        String LevelName = null;
        ArrayList<SelectItems> Mb = getLevel();

        if("KR".equals(type))
            LevelName = Mb.get(idx).getKr();
        else if("ENG".equals(type))
            LevelName = Mb.get(idx).getEng();

        return LevelName;
    }
    public int getScoreGraph(int score){
       int graph = 0;
        switch(score){
            case 0 :
                graph = R.mipmap.graph0;
                break;
            case 7 :
                graph = R.mipmap.graph1;
                break;
            case 13 :
            case 14 :
                graph = R.mipmap.graph2;
                break;
            case 20 :
            case 21 :
            case 27 :
                graph = R.mipmap.graph3;
                break;
            case 33 :
                graph = R.mipmap.graph4;
                break;
            case 40 :
            case 47 :
                graph = R.mipmap.graph5;
                break;
            case 53 :
                graph = R.mipmap.graph6;
                break;
            case 60 :
            case 67 :
                graph = R.mipmap.graph7;
                break;
            case 73 :
                graph = R.mipmap.graph8;
                break;
            case 80 :
            case 87 :
                graph = R.mipmap.graph9;
                break;
            case 93 :
            case 100 :
                graph = R.mipmap.graph10;
                break;
        }
        return graph;
    }
    public ArrayList<SelectItems> getPersonalTrainingRaw(int rank, int type){
        ArrayList<SelectItems> Mb = new ArrayList<SelectItems>();
        Mb.add(new SelectItems(0,"pt_0_k", "pt_0_e"));
        Mb.add(new SelectItems(5,"pt_5_k", "pt_5_e"));
        Mb.add(new SelectItems(6,"pt_6_k", "pt_6_e"));
        Mb.add(new SelectItems(7,"pt_7_k", "pt_7_e"));
        Mb.add(new SelectItems(8,"pt_8_k", "pt_8_e"));
        Mb.add(new SelectItems(9,"pt_9_k", "pt_9_e"));
        Mb.add(new SelectItems(10,"pt_10_k", "pt_10_e"));
        Mb.add(new SelectItems(11,"pt_11_k", "pt_11_e"));
        switch(rank){
            case 1:     //선장
            case 2:     //일항사
            case 3:     //이항사
            case 4:     //삼항사
            case 9:     //갑판장
            case 10:    //갑판수
            case 11:    //갑판원
            case 18:    //실항사
                Mb.add(new SelectItems(1,"pt_1_k", "pt_1_e"));
//                Mb.add(new SelectItems("통신", "Communication"));
                if(type == 1)
                    Mb.add(new SelectItems(2,"", ""));
                else if(type == 2)
                    Mb.add(new SelectItems(2,"pt_2_k", "pt_2_e"));
                else if(type == 3)
                    Mb.add(new SelectItems(2,"pt_12_k", "pt_12_e"));

                Mb.add(new SelectItems(3,"pt_3_k", "pt_3_e"));
//        Mb.add(new SelectItems("기름", "Petroleum"));
//        Mb.add(new SelectItems("화학제품", "Chemical"));
//        Mb.add(new SelectItems("가스", "Gas"));
                break;
            case 5:     //기관장
            case 6:     //일기사
            case 7:     //이기사
            case 8:     //삼기사
            case 12:    //조기장
            case 13:    //조기수
            case 14:    //조기원
            case 19:    //실기사
                Mb.add(new SelectItems(4,"pt_4_k", "pt_4_e"));
                break;
            case 15:    //조리장
            case 16:    //조리수
            case 17:    //조리원
                break;
        }
        /*for(SelectItems str: Mb){
            System.out.println(str.getIdx()+"/"+str.getKr());
        }
        System.out.println("//////////////////////////////");*/
        Collections.sort(Mb);

        return Mb;
    }

    public ArrayList<SelectItems> getGeneralTraining(){
        ArrayList<SelectItems> Mb = new ArrayList<SelectItems>();
        Mb.add(new SelectItems("소개", "Intro", 0,0,false,false));
        Mb.add(new SelectItems("화물의 상호반응성", "Cargo Compatibility and Reactivity", 1,0,false,false));
        Mb.add(new SelectItems("탱크 출입을 위한 가이드", "Guidelines on tank entry for tankers", 2,0,false,false));
        Mb.add(new SelectItems("화물창 및 밀폐구역으로부터의 구조", "Rescue from cargo tanks and other enclosed space", 3,0,false,false));
        Mb.add(new SelectItems("해상 사이버 보안", "Maritime Cyber Security", 4,0,false,false));

        Mb.add(new SelectItems("OIL RECORD BOOK PART I에서의 작업 기록 지침", "Guidance for The recording of operation in the oil record book part I", 5,0,false,false));
        Mb.add(new SelectItems("상편", "First Vol", 5,1,false,false));
        Mb.add(new SelectItems("하편", "Second Vol", 5,2,false,false));

        Mb.add(new SelectItems("계류 작업 MEG 4 & VIQ 7 통합", "Mooring including MEG 4 & VIQ 7", 7,0,false,false));
        Mb.add(new SelectItems("자가반응", "SELF REACTION", 8,0,false,false));
        Mb.add(new SelectItems("카고 호스에 대한 점검 요구사항", "Inspection requirements for cargo hoses", 9,0,false,false));
        Mb.add(new SelectItems("VIQ 7에 대한 개정사항", "Amendments to VIQ 7", 10,0,false,false));
        Mb.add(new SelectItems("고온작업", "Hot Work", 11,0,false,false));

        Mb.add(new SelectItems("사고조사", "Incident Investigation", 12,0,false,false));
        Mb.add(new SelectItems("기본개념", "Basic", 12,1,false,false));
        Mb.add(new SelectItems("조사착수", "Commencement of Investigation", 12,2,false,false));
        Mb.add(new SelectItems("데이터 수집 및 보존(1/2)", "Data collection and conservation(1/2)", 12,3,false,false));
        Mb.add(new SelectItems("데이터 수집 및 보존(2/2)", "Data collection and conservation(2/2)", 12,4,false,false));
//        Mb.add(new SelectItems("데이터 분석", "Data analysis", 12,5,false,false));

        Mb.add(new SelectItems("선박보안인식", "Ship Security Awareness", 13,0,false,false));
        Mb.add(new SelectItems("기름배출감시장비(ODME)", "Oil Discharging Monitoring Equipment(ODME)", 14,0,false,false));
        Mb.add(new SelectItems("H2S의 인식 및 중요성", "H2S awareness", 15,0,false,false));
        Mb.add(new SelectItems("리프팅 및 슬링 - 운용 및 장비", "Lifting and Sling - Operation and Equipment", 16,0,false,false));
        Mb.add(new SelectItems("준사고", "Near miss incident", 17,0,false,false));
        Mb.add(new SelectItems("전자해도 시스템", "ECDIS ", 18,0,false,false));
        Mb.add(new SelectItems("항해 계획", "Passage Plan", 18,1,false,false));
        Mb.add(new SelectItems("항해 당직", "Navigational Watch", 18,2,false,false));

        Mb.add(new SelectItems("비상대응 및 훈련", "Emergency response and drill", 19,0,false,false));
        Mb.add(new SelectItems("항해", "Navigation", 20,0,false,false));
        Mb.add(new SelectItems("케미칼 화물 작업", "Chemical Cargo operation", 21,0,false,false));
        Mb.add(new SelectItems("가스 화물 작업", "Gas Cargo operation", 22,0,false,false));
        Mb.add(new SelectItems("계류작업", "Mooring", 23,0,false,false));
        Mb.add(new SelectItems("기관실", "Machinery space", 24,0,false,false));
        Mb.add(new SelectItems("소화구명설비", "Fire ﬁghting & Life saving", 25,0,false,false));
        Mb.add(new SelectItems("위험성 평가 및 사고조사", "risk assessment & investigation", 26,0,false,false));
        Mb.add(new SelectItems("환경", "Environmental", 27,0,false,false));
        Mb.add(new SelectItems("보안", "Security", 28,0,false,false));
        Mb.add(new SelectItems("에너지 보존", "Energy conservation", 29,0,false,false));
        Mb.add(new SelectItems("서류 및 관리", "ISM", 30,0,false,false));
        Mb.add(new SelectItems("위생", "Hygiene", 31,0,false,false));
        Mb.add(new SelectItems("전용 구조정의 진수 및 회수 절차", "Launching & Recovery procedures for dedicated rescue boat", 32,0,false,false));
        Mb.add(new SelectItems("PSC 검사", "PSC Inspection", 33,0,false,false));
        Mb.add(new SelectItems("평형수 관리", "Ballast water management", 34,0,false,false));
        Mb.add(new SelectItems("육 해상 간 안전점검표", "Ship Shore Safety Checklist", 35,0,false,false));

        Mb.add(new SelectItems("가스화물 직무 교육", "Gas Cargo job training", 36,0,false,false));
        Mb.add(new SelectItems("기본", "-Basic", 36,1,false,false));
        Mb.add(new SelectItems("가스화물 취급 장비", "Gas cargo handling equipment", 36,2,false,false));
        Mb.add(new SelectItems("가스화물 취급", "Gas cargo handling", 36,3,false,false));

        Mb.add(new SelectItems("유류수급", "Bunkering", 37,0,false,false));
        Mb.add(new SelectItems("위험 에너지 관리", "Control of Hazardous Energy", 38,0,false,false));
        Mb.add(new SelectItems("동시작업", "SIMOPS", 39,0,false,false));
        Mb.add(new SelectItems("선상 문화", "Culture on board", 40,0,false,false));
        Mb.add(new SelectItems("선상 문화 (1/3)", "Culture on board (1/3)", 40,1,false,false));
        Mb.add(new SelectItems("선상 문화 (2/3)", "Culture on board (2/3)", 40,2,false,false));
        Mb.add(new SelectItems("선상 문화 (3/3)", "Culture on board (3/3)", 40,3,false,false));
        Mb.add(new SelectItems("인적자원관리", "Human Resource Management", 41,0,false,false));
        return Mb;


    }

    public ArrayList<SelectItems> getGeneralTrainingRaw(){
        ArrayList<SelectItems> Mb = new ArrayList<SelectItems>();
        Mb.add(new SelectItems(0,0,"intro", "intro"));
        Mb.add(new SelectItems(1,0,"gt_1_k", "gt_1_e"));
        Mb.add(new SelectItems(2,0,"gt_2_k", "gt_2_e"));
        Mb.add(new SelectItems(3,0,"gt_3_k", "gt_3_e"));
        Mb.add(new SelectItems(4,0,"gt_4_k", "gt_4_e"));

        Mb.add(new SelectItems(5,0,"", ""));
        Mb.add(new SelectItems(5,1,"gt_5_k", "gt_5_e"));
        Mb.add(new SelectItems(5,2,"gt_6_k", "gt_6_e"));

        Mb.add(new SelectItems(7,0,"gt_7_k", "gt_7_e"));
        Mb.add(new SelectItems(8,0,"gt_8_k", "gt_8_e"));
        Mb.add(new SelectItems(9,0,"gt_9_k", "gt_9_e"));
        Mb.add(new SelectItems(10,0,"gt_10_k", "gt_10_e"));
        Mb.add(new SelectItems(11,0,"gt_11_k", "gt_11_e"));

        Mb.add(new SelectItems(12,0,"", ""));
        Mb.add(new SelectItems(12,1,"gt_12_k", "gt_12_e"));
        Mb.add(new SelectItems(12,2,"gt_12_2_k", "gt_12_2_e"));
        Mb.add(new SelectItems(12,3,"gt_12_3_k", "gt_12_3_e"));
        Mb.add(new SelectItems(12,4,"gt_12_4_k", "gt_12_4_e"));
//        Mb.add(new SelectItems(12,5,"gt_12_5_k", "gt_12_5_e"));

        Mb.add(new SelectItems(13,0,"gt_13_k", "gt_13_e"));
        Mb.add(new SelectItems(14,0,"gt_14_k", "gt_14_e"));
        Mb.add(new SelectItems(15,0,"gt_15_k", "gt_15_e"));
        Mb.add(new SelectItems(16,0,"gt_16_k", "gt_16_e"));
        Mb.add(new SelectItems(17,0,"gt_17_k", "gt_17_e"));
        Mb.add(new SelectItems(18,0,"", ""));
        Mb.add(new SelectItems(18,1,"gt_18_1_k", "gt_18_1_e"));
        Mb.add(new SelectItems(18,2,"gt_18_2_k", "gt_18_2_e"));
        Mb.add(new SelectItems(19,0,"pt_0_k", "pt_0_e"));
        Mb.add(new SelectItems(20,0,"pt_1_k", "pt_1_e"));
        Mb.add(new SelectItems(21,0,"pt_2_k", "pt_2_e"));
        Mb.add(new SelectItems(22,0,"pt_12_k", "pt_12_e"));
        Mb.add(new SelectItems(23,0,"pt_3_k", "pt_3_e"));
        Mb.add(new SelectItems(24,0,"pt_4_k", "pt_4_e"));
        Mb.add(new SelectItems(25,0,"pt_5_k", "pt_5_e"));
        Mb.add(new SelectItems(26,0,"pt_6_k", "pt_6_e"));
        Mb.add(new SelectItems(27,0,"pt_7_k", "pt_7_e"));
        Mb.add(new SelectItems(28,0,"pt_8_k", "pt_8_e"));
        Mb.add(new SelectItems(29,0,"pt_9_k", "pt_9_e"));
        Mb.add(new SelectItems(30,0,"pt_10_k", "pt_10_e"));
        Mb.add(new SelectItems(31,0,"pt_11_k", "pt_11_e"));
        Mb.add(new SelectItems(32,0,"gt_32_k", "gt_32_e"));
        Mb.add(new SelectItems(33,0,"gt_33_k", "gt_33_e"));
        Mb.add(new SelectItems(34,0,"gt_34_k", "gt_34_e"));
        Mb.add(new SelectItems(35,0,"gt_35_k", "gt_35_e"));
        Mb.add(new SelectItems(36,0,"", ""));
        Mb.add(new SelectItems(36,1,"gt_36_k", "gt_36_e"));
        Mb.add(new SelectItems(36,2,"gt_36_2_k", "gt_36_2_e"));
        Mb.add(new SelectItems(36,3,"gt_36_3_k", "gt_36_3_e"));
        Mb.add(new SelectItems(37,0,"gt_37_k", "gt_37_e"));
        Mb.add(new SelectItems(38,0,"gt_38_k", "gt_38_e"));
        Mb.add(new SelectItems(39,0,"gt_39_k", "gt_39_e"));
        Mb.add(new SelectItems(40,0,"", ""));
        Mb.add(new SelectItems(40,1,"gt_40_1_k", "gt_40_1_e"));
        Mb.add(new SelectItems(40,2,"gt_40_2_k", "gt_40_2_e"));
        Mb.add(new SelectItems(40,3,"gt_40_3_k", "gt_40_3_e"));
        Mb.add(new SelectItems(41,0,"gt_41_k", "gt_41_e"));


        return Mb;
    }

    public int getGeneralTrainingSize(){
        ArrayList<SelectItems> Mb = getGeneralTrainingRaw();
        int cnt = 0;
        for(int i = 0; i < Mb.size(); i++) {
            if(!"".equals(Mb.get(i).getKr())) {
                cnt++;
            }
        }

        return cnt;
    }

    public String getGeneralTrainingRaw(int main, int sub, String type){
        String raw = null;
        ArrayList<SelectItems> getRaw = getGeneralTrainingRaw();
        for(int i = 0; i < getRaw.size(); i++) {

            if(getRaw.get(i).getRankImg() == main && getRaw.get(i).getRankChild() == sub) {
                if("KR".equals(type))

                    raw = getRaw.get(i).getKr();

                else if("ENG".equals(type))
                    raw = getRaw.get(i).getEng();
            }

        }
        return raw;
    }

    public String getGeneralTraining(int group, int sub, String type){
        int Group = group;
        int Sub = sub;

//        Log.e("getGeneralTrainings", "Group : "+ Group +"/ Sub : " + Sub);
        if(Group == 5 && Sub == 0) {

            Group = 5;
            Sub = 1;
        } else if(Group == 6 && Sub == 0) {

            Group = 5;
            Sub = 2;
        }else if(Group == 12 && Sub == 0) {

            Group = 12;
            Sub = 1;
        }

        String LevelName = null;
        ArrayList<SelectItems> Mb = getGeneralTraining();
        String temp = "";
        for(int i = 0; i < Mb.size(); i++) {

            if(Mb.get(i).getRankChild() == 0)
                temp = "KR".equals(type) ? Mb.get(i).getKr() : Mb.get(i).getEng();

            if(Mb.get(i).getRankImg() == Group && Mb.get(i).getRankChild() == Sub) {
                if("KR".equals(type)) {
                    LevelName = Sub > 0 ? temp + "(" + Mb.get(i).getKr() + ")" : Mb.get(i).getKr();

                } else if("ENG".equals(type)) {
                    LevelName = Sub > 0 ? temp + "(" + Mb.get(i).getEng() + ")" : Mb.get(i).getEng();
                }
            }
        }

        return LevelName;
    }

    public String getGeneralTraining(SelectItems si, String type){

        String LevelName = null;

        if("KR".equals(type))
            LevelName = si.getKr();
        else if("ENG".equals(type))
            LevelName = si.getEng();

        return LevelName;
    }

    public ArrayList<SelectItems> getNewRegulation(){
        ArrayList<SelectItems> Mb = new ArrayList<SelectItems>();
        Mb.add(new SelectItems("HME 물질 및 쓰레기 기록부의 형태", "HME substances and Form of Garbage Record Book", false));
        Mb.add(new SelectItems("선박 연료소모량 데이터 수집 시스템", "Data collection system for fuel oil consumption of ships", false));
        Mb.add(new SelectItems("IOPP 증서추록 Form B의 개정안", "Form B of the Supplement to the International Oil Pollution Prevention Certificate", false));
        Mb.add(new SelectItems("소방원의 통신", "Fire-fighter's communication", false));
        Mb.add(new SelectItems("Bunker Delivery Note 개정안", "Amendments to Bunker Delivery Note", false));
        Mb.add(new SelectItems("홍콩수역에서의 대기오염 규제", "Air Pollution Control in Hong Kong Waters", false));
        Mb.add(new SelectItems("중국 ECA 지역에서의 저유황 연료유 사용 규제", "Regulation on the use of low-sulfur fuel oil in China ECA area", false));
        Mb.add(new SelectItems("대만 지역에서의 저유황 연료유 사용 규제", "Regulation on the use of low-sulfur fuel oil in Taiwan waters", false));
        Mb.add(new SelectItems("2020 NEW Regulation : 상반기", "2020 NEW Regulation : The first half", false));
        Mb.add(new SelectItems("THE GUIDELINES ON CYBER SECURITY ONBOARD SHIPS \n Version 4", "THE GUIDELINES ON CYBER SECURITY ONBOARD SHIPS / Version 4", false));
        Mb.add(new SelectItems("잔류성 부유물질의 탱크세정 및 화물 잔류물", "Cargo residues and tank washings of persistent floating products", false));
        Mb.add(new SelectItems("IBC code 개정(1장,15장,16장,17장,18장,19장 및 21장)", "IBC code Amendment(Ch 1,15,16,17,18,19 and 21)", false));
        Mb.add(new SelectItems("BCH code 개정(벌크 액체 용 황화수소 (H2S) 감시 장비)", "BCH code Amendment(H2S detection equipment for bulk liquids)", false));
        Mb.add(new SelectItems("ISM code 개정(사이버 리스크 관리)", "Amendment of ISM code(Management of Cyber risk)", false));
        Mb.add(new SelectItems("IBC code 개정 (벌크 액체 용 황화수소 감시 장비)", "IBC code Amendment (H2S detection equipment for bulk liquids)", false));
        Mb.add(new SelectItems("BCH code 개정(벌크 액체 용 황화수소 감시 장비)", "BCH code Amendment(H2S detection equipment for bulk liquids)", false));

        return Mb;
    }

    public ArrayList<SelectItems> getNewRegulationRaw(){
        ArrayList<SelectItems> Mb = new ArrayList<SelectItems>();
        Mb.add(new SelectItems("nr_2018_1_k", "nr_2018_1_e"));
        Mb.add(new SelectItems("nr_2018_2_k", "nr_2018_2_e"));
        Mb.add(new SelectItems("nr_2018_3_k", "nr_2018_3_e"));
        Mb.add(new SelectItems("nr_2014_4_k", "nr_2014_4_e"));
        Mb.add(new SelectItems("nr_2019_1_k", "nr_2019_1_e"));
        Mb.add(new SelectItems("nr_2019_6_k", "nr_2019_6_e"));
        Mb.add(new SelectItems("nr_2019_7_k", "nr_2019_7_e"));
        Mb.add(new SelectItems("nr_2019_8_k", "nr_2019_8_e"));
        Mb.add(new SelectItems("nr_2020_1_k", "nr_2020_1_e"));
        Mb.add(new SelectItems("2021-Cyber-Security-Guidelines", "2021-Cyber-Security-Guidelines"));
        Mb.add(new SelectItems("MEPC.315(74)-K", "MEPC.315(74)-E"));
        Mb.add(new SelectItems("MEPC.318(74)-K", "MEPC.318(74)-E"));
        Mb.add(new SelectItems("MEPC.319(74)-K", "MEPC.319(74)-E"));
        Mb.add(new SelectItems("MSC.428(98)-K", "MSC.428(98)-E"));
        Mb.add(new SelectItems("MSC.460(101)-K", "MSC.460(101)-E"));
        Mb.add(new SelectItems("MSC.463(101)-K", "MSC.463(101)-E"));

        return Mb;
    }

    public String getNewRegulation(int idx, String type){

        String Name = null;
        ArrayList<SelectItems> Mb = getNewRegulation();
        Log.e("test","type : " + type);
        Log.e("test","Mb : " + Mb.size());
        if("KR".equals(type))
            Name = Mb.get(idx).getKr();
        else if("ENG".equals(type))
            Name = Mb.get(idx).getEng();

        return Name;
    }

    public List<TrainingsInfo> getGTsubject(List<TrainingsInfo> tsinfo, String type){
        ArrayList<String> Mb = new ArrayList<String>();

        for(int i=0; i<tsinfo.size(); i++) {
//            String tc[] = count.get(i).split("-");
//            Log.e("getGTsubject" , "tc[] : "+tc.length);
            if("G".equals(type)) {
//                if(tc.length > 1)
                    tsinfo.get(i).setTitle(getGeneralTraining(tsinfo.get(i).getTraining_course(),tsinfo.get(i).getTraining_course2(),"ENG"));
//                else
//                    Mb.add(getGeneralTraining(Integer.valueOf(tc[0]),0,"ENG"));
            }else if("R".equals(type)) {
                tsinfo.get(i).setTitle(getNewRegulation(tsinfo.get(i).getTraining_course() - 1,"ENG"));
            }
        }

        return tsinfo;
    }

    public String getGTRankDept(int rank) {
        String Rank = "";
        switch(rank){
            case 1:     //선장
            case 2:     //일항사
                Rank = "ns";
                break;
            case 3:     //이항사
            case 4:     //삼항사
                Rank = "nj";
                break;
            case 9:     //갑판장
            case 10:    //갑판수
            case 11:    //갑판원
            case 18:    //실항사
                Rank = "nr";
                break;
            case 5:     //기관장
            case 6:     //일기사
                Rank = "es";
                break;
            case 7:     //이기사
            case 8:     //삼기사
                Rank = "ej";
                break;
            case 12:    //조기장
            case 13:    //조기수
            case 14:    //조기원
            case 19:    //실기사
                Rank = "er";
                break;
            case 15:    //조리장
            case 16:    //조리수
            case 17:    //조리원
                Rank = "osr";
                break;
        }

        return Rank;
    }

    public List<TrainingsInfo> getGTMatrix(String Type) {
        List<TrainingsInfo> tsList = new ArrayList<>();
        if("G".equals(Type)) {
            Log.e("test","G Matrix ");
            tsList.add(new TrainingsInfo("G",1,0,1,1,1,0,0,0,1));
            tsList.add(new TrainingsInfo("G",2,0,1,1,1,1,1,1,1));
            tsList.add(new TrainingsInfo("G",3,0,1,1,1,1,1,1,1));
            tsList.add(new TrainingsInfo("G",4,0,1,1,1,1,1,1,1));
            tsList.add(new TrainingsInfo("G",5,1,0,0,0,1,1,0,0));
            tsList.add(new TrainingsInfo("G",5,2,0,0,0,1,1,0,0));
            tsList.add(new TrainingsInfo("G",7,0,1,1,1,0,0,0,1));
            tsList.add(new TrainingsInfo("G",8,0,1,1,1,1,0,0,0));
            tsList.add(new TrainingsInfo("G",9,0,1,1,1,1,0,0,0));
            tsList.add(new TrainingsInfo("G",10,0,1,1,0,1,1,0,0));
            tsList.add(new TrainingsInfo("G",11,0,1,1,1,1,1,1,0));
            tsList.add(new TrainingsInfo("G",12,1,1,1,0,1,1,0,0));
            tsList.add(new TrainingsInfo("G",12,2,1,1,0,1,1,0,0));
            tsList.add(new TrainingsInfo("G",12,3,1,1,0,1,1,0,0));
            tsList.add(new TrainingsInfo("G",12,4,1,1,0,1,1,0,0));
//            tsList.add(new TrainingsInfo("G",12,5,1,1,0,1,1,0,0));
            tsList.add(new TrainingsInfo("G",13,0,1,1,1,1,1,1,1));
            tsList.add(new TrainingsInfo("G",14,0,1,1,1,0,0,0,0));
            tsList.add(new TrainingsInfo("G",15,0,1,1,1,1,1,1,1));
            tsList.add(new TrainingsInfo("G",16,0,1,1,1,1,1,1,0));
            tsList.add(new TrainingsInfo("G",17,0,1,1,1,1,1,1,1));
            tsList.add(new TrainingsInfo("G",18,1,1,1,1,0,0,0,0));
            tsList.add(new TrainingsInfo("G",18,2,1,1,1,0,0,0,0));
            tsList.add(new TrainingsInfo("G",19,0,1,1,1,1,1,1,1));
            tsList.add(new TrainingsInfo("G",20,0,1,1,1,0,0,0,0));
            tsList.add(new TrainingsInfo("G",21,0,1,1,1,0,0,0,0));
            tsList.add(new TrainingsInfo("G",22,0,1,1,1,0,0,0,0));
            tsList.add(new TrainingsInfo("G",23,0,1,1,1,0,0,0,0));
            tsList.add(new TrainingsInfo("G",24,0,0,0,0,1,1,1,0));
            tsList.add(new TrainingsInfo("G",25,0,1,1,1,1,1,1,0));
            tsList.add(new TrainingsInfo("G",26,0,1,1,1,1,1,1,1));
            tsList.add(new TrainingsInfo("G",27,0,1,1,1,1,1,1,1));
            tsList.add(new TrainingsInfo("G",28,0,1,1,1,1,1,1,1));
            tsList.add(new TrainingsInfo("G",29,0,1,1,1,1,1,1,1));
            tsList.add(new TrainingsInfo("G",30,0,1,1,1,1,1,1,1));
            tsList.add(new TrainingsInfo("G",31,0,1,1,1,1,1,1,1));
            tsList.add(new TrainingsInfo("G",32,0,1,1,1,1,1,1,1));
            tsList.add(new TrainingsInfo("G",33,0,1,1,0,1,1,0,0));
            tsList.add(new TrainingsInfo("G",34,0,1,1,1,1,0,0,0));
            tsList.add(new TrainingsInfo("G",35,0,1,1,0,0,0,0,0));
            tsList.add(new TrainingsInfo("G",36,1,1,1,1,1,1,1,1));
            tsList.add(new TrainingsInfo("G",36,2,1,1,0,0,0,0,0));
            tsList.add(new TrainingsInfo("G",36,3,1,1,0,0,0,0,0));
            tsList.add(new TrainingsInfo("G",37,0,0,0,0,1,1,1,0));
            tsList.add(new TrainingsInfo("G",38,0,1,1,1,1,1,1,1));
            tsList.add(new TrainingsInfo("G",39,0,1,1,1,1,1,1,1));
            tsList.add(new TrainingsInfo("G",40,1,1,1,1,1,1,1,1));
            tsList.add(new TrainingsInfo("G",40,2,1,1,1,1,1,1,1));
            tsList.add(new TrainingsInfo("G",40,3,1,1,1,1,1,1,1));
            tsList.add(new TrainingsInfo("G",41,0,1,1,1,1,1,1,1));
        } else if("R".equals(Type)) {
            Log.e("test","R Matrix ");
            tsList.add(new TrainingsInfo("R",1,0,1,1,1,1,1,1,1));
            tsList.add(new TrainingsInfo("R",2,0,1,0,0,1,1,0,0));
            tsList.add(new TrainingsInfo("R",3,0,1,0,0,1,0,0,0));
            tsList.add(new TrainingsInfo("R",4,0,1,1,1,1,1,1,1));
            tsList.add(new TrainingsInfo("R",5,0,1,0,0,1,1,0,0));
            tsList.add(new TrainingsInfo("R",6,0,1,1,1,1,1,1,0));
            tsList.add(new TrainingsInfo("R",7,0,1,1,1,1,1,1,0));
            tsList.add(new TrainingsInfo("R",8,0,1,1,1,1,1,1,0));
            tsList.add(new TrainingsInfo("R",9,0,1,1,0,1,1,1,0));
            tsList.add(new TrainingsInfo("R",10,0,1,1,1,1,1,1,1));
            tsList.add(new TrainingsInfo("R",11,0,1,0,0,1,0,0,0));
            tsList.add(new TrainingsInfo("R",12,0,1,0,0,1,0,0,0));
            tsList.add(new TrainingsInfo("R",13,0,1,0,0,1,0,0,0));
            tsList.add(new TrainingsInfo("R",14,0,1,0,0,1,0,0,0));
            tsList.add(new TrainingsInfo("R",15,0,1,0,0,1,0,0,0));
            tsList.add(new TrainingsInfo("R",16,0,1,0,0,1,0,0,0));
            //test
            tsList.add(new TrainingsInfo("R",17,0,1,1,1,1,1,1,1));

        }
        return tsList;
    }
    /*public int getRaw(String name) {
        int MovieNo = 0;

        try {
            MovieNo = (int) R.raw.class.getDeclaredField(name).get(R.raw.class);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return MovieNo;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return MovieNo;
    }*/

    public static AssetFileDescriptor getAssetFileDescriptor(Context con, String filePath) {
        PackageManager manager = con.getPackageManager();
        PackageInfo info;
        try {
            info = manager.getPackageInfo(con.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        String thePackageName = con.getPackageName();
        int thePackageVer = info.versionCode;

        String zipFilePath = Environment.getExternalStorageDirectory() + "/Android/obb/" + thePackageName + "/main."
                + thePackageVer + "." + thePackageName + ".obb";
        ZipResourceFile expansionFile = null;
        AssetFileDescriptor afd = null;
        try {
//            expansionFile = new ZipResourceFile(zipFilePath);
            expansionFile = APKExpansionSupport.getAPKExpansionZipFile(con, 1, 1);
            afd = expansionFile.getAssetFileDescriptor(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return afd;
    }

    public InputStream getAssetFileInputStream(Context con, String filename){
        PackageManager manager = con.getPackageManager();
        PackageInfo info;
        InputStream fileStream = null;
        try{
            info = manager.getPackageInfo(con.getPackageName(),0);
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
            return (null);
        }

        String PackageName = con.getPackageName();
        int PackageVer = info.versionCode;
        String contentPath = "content://";
        File root = Environment.getExternalStorageDirectory();
        File expPath = new File(root.toString() + "/Android/obb/" + PackageName);

        String strMainPath = expPath + File.separator + "main." + PackageVer + "." + PackageName + ".obb";
//        String strMainPath = con.getObbDir() + "/main." + PackageVer + "." + PackageName + ".obb";
        File main = new File(strMainPath);
        if ( main.isFile() ) {
            Log.e("getAssetFileDescriptor","isFile : "+true);
        }

        ZipResourceFile expansionFile = null;

        try{
            expansionFile = APKExpansionSupport.getAPKExpansionZipFile(con, 1, 0);

            fileStream = expansionFile.getInputStream(filename);
        }catch (IOException e){
            e.printStackTrace();
        }
        return fileStream;
    }

    public File getZipFile(Context con, String filename, String filetype){
        SharedPreferences pref = con.getSharedPreferences("pref", Context.MODE_PRIVATE);
        String ZIP_PASSWORD = pref.getString("ZIP_PASSWORD", "");
//        Log.e("getZipFile","ZIP_PASSWORD : "+ZIP_PASSWORD);
        PackageManager manager = con.getPackageManager();
        PackageInfo info;
        File file = null;
        String fullFileName = filename + "." + filetype;
        try{
            info = manager.getPackageInfo(con.getPackageName(),0);
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
            return (null);
        }

        String PackageName = con.getPackageName();
//        int PackageVer = info.versionCode;
        File root = Environment.getExternalStorageDirectory();
        File expPath = new File(root.toString() + "/Android/obb/" + PackageName);
        String strMainPath = expPath + File.separator + filename +".obb";
//        File main = new File(strMainPath);
//        if ( main.isFile() ) {
//            Log.e("getZipFile","isFile : "+true);
//        }
        try{
//            Log.e("getZipFile","strMainPath : "+strMainPath);
            ZipFile zipFile = new ZipFile(strMainPath);
            zipFile.setRunInThread(true);
            ProgressMonitor progressMonitor = zipFile.getProgressMonitor();
            if (zipFile.isEncrypted()) {
                zipFile.setPassword(ZIP_PASSWORD);
            }
            zipFile.extractFile(fullFileName, con.getExternalFilesDir(null).toString());

            while (progressMonitor.getState() != ProgressMonitor.STATE_READY) {
            }

            file = new File(con.getExternalFilesDir(null), fullFileName);
        } catch (ZipException e) {
            e.printStackTrace();

            Log.e("getZipFile", "ZipException : "+e + "/" + e.getCode());
            return file;
        } catch (Exception e) {
            Log.e("getZipFile", "Exception : "+e);
            return file;
        }
        return file;
    }

    public File createExternalStoragePrivateFile(Context con, String filePath) {
        // Create a path where we will place our private file on external
        // storage.
        File file = new File(con.getExternalFilesDir(null), filePath);

        try {
            // Very simple code to copy a picture from the application's
            // resource into the external file.  Note that this code does
            // no error checking, and assumes the picture is small (does not
            // try to copy it in chunks).  Note that if external storage is
            // not currently mounted this will silently fail.
            InputStream is = getAssetFileInputStream(con, filePath);
            OutputStream os = new FileOutputStream(file);
            byte[] data = new byte[is.available()];
            is.read(data);
            os.write(data);
            is.close();
            os.close();
        } catch (IOException e) {
            // Unable to create file, likely because external storage is
            // not currently mounted.
            Log.w("ExternalStorage", "Error writing " + file, e);
        }
        return file;
    }

    public void deleteExternalStoragePrivateFile(Context con, String filePath) {
        // Get path for the file on external storage.  If external
        // storage is not currently mounted this will fail.
        File file = new File(con.getExternalFilesDir(null), filePath);
        if (file != null) {
            file.delete();
        }
    }
    public void deleteobbPrivateFile(Context con, String filePath) {
        // Get path for the file on external storage.  If external
        // storage is not currently mounted this will fail.
        File file = new File(con.getObbDir(), filePath);
        if (file != null) {
            file.delete();
        }
    }

    public void deleteExternalStoragePrivateFileAll(Context con) {
        // Get path for the file on external storage.  If external
        // storage is not currently mounted this will fail.
        File file = new File(con.getExternalFilesDir(null).toString());
        if (file != null) {
            // 하위 디렉토리들을 배열에 담는다.
            File[] innerFiles= file.listFiles();

            // 하위 디렉토리 삭제
            for(int i=0; i<innerFiles.length; i++) {
                innerFiles[i].delete();
            }
        }
    }

    public boolean hasExternalStoragePrivateFile(Context con, String filePath) {
        // Get path for the file on external storage.  If external
        // storage is not currently mounted this will fail.
        File file = new File(con.getExternalFilesDir(null), filePath);
        if (file != null) {
            return file.exists();
        }
        return false;
    }

    public boolean hasObbPrivateFile(Context con, String str, String type) {
        PackageManager manager = con.getPackageManager();
        PackageInfo info;
        File file = null;
        try{
            info = manager.getPackageInfo(con.getPackageName(),0);
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
            return false;
        }

        String PackageName = con.getPackageName();
        int PackageVer = info.versionCode;

        File root = Environment.getExternalStorageDirectory();
        File expPath = new File(root.toString() + "/Android/obb/" + PackageName);

        String fileName = "";


        if("Subtitle".equals(type))
            fileName = str +".srt";
        else if("PDF".equals(type))
            fileName = str +".pdf";
        else
            fileName = str +".obb";

        String strMainPath = expPath + File.separator + fileName;
        Log.e("hasObbPrivateFile","strMainPath : "+ strMainPath);
        File main = new File(strMainPath);

        /*if(PackageVer <=  1){
            return true;
        }*/

        if( main.exists() ) {
            return true;
        }

        return false;
    }


    /**수정중 코드 **/

   /* public boolean hasObbPrivateFile(Context con, String str, String type) {

        Log.e("test","isFileGranted : " + isFileGranted(con));

        if((Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) && !isFileGranted(con)){

            // 권한이 부여되지 않은 상태
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            Uri uri = Uri.fromParts("package",con.getPackageName(), null);
            intent.setData(uri);
            con.startActivity(intent);

        }else {
            Log.e("test","isFileGranted true 일경우 : " );

            PackageManager manager = con.getPackageManager();
            PackageInfo info;
            File file = null;
            File expPath = null;
            try {
                info = manager.getPackageInfo(con.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            String PackageName = con.getPackageName();
            int PackageVer = info.versionCode;


            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                Log.e("test", "VER : " + Build.VERSION.SDK_INT);
                File root = Environment.getExternalStorageDirectory();
                Log.e("test", "root : " + root);
//        File root = Environment.getExternalStorageDirectory();
                expPath = new File(root.toString() + "/Android/obb/" + PackageName);
                //File expPath = new File("/storage/emulated/0/Android/obb/" + PackageName);
            } else {
                String path = con.getExternalFilesDir(null).getPath();
                Log.e("test", "paht : " + path);
                String subPath = "";
                subPath = path.substring(0, 19);
                Log.e("test", "subPath : " + subPath);

//        File root = Environment.getExternalStorageDirectory();
                expPath = new File(subPath + "/Android/obb/" + PackageName);
                //File expPath = new File("/storage/emulated/0/Android/obb/" + PackageName);
            }
            //File root = Environment.getDataDirectory();


            String fileName = "";


            if ("Subtitle".equals(type))
                fileName = str + ".srt";
            else if ("PDF".equals(type))
                fileName = str + ".pdf";
            else
                fileName = str + ".obb";

            String strMainPath = expPath + File.separator + fileName;
            Log.e("hasObbPrivateFile", "strMainPath : " + strMainPath);
            File main = new File(strMainPath);

        *//*if(PackageVer <=  1){
            return true;
        }*//*


            if (main.exists()) {
                return true;
            }


        }
        return false;
    }*/

    public File getSubtitleFile(Context con, String str) {
        String PackageName = con.getPackageName();
        File root = Environment.getExternalStorageDirectory();
        File expPath = new File(root.toString() + "/Android/obb/" + PackageName);
        File Subtitle = null;
        String strMainPath = expPath + File.separator + str;
        Log.e("hasObbPrivateFile","strMainPath : "+ strMainPath);
        Subtitle = new File(strMainPath);

        return Subtitle;
    }

    public void getMassage(String type, String language, Context mContext, int duration) {
        String Massage = null;
        Massage = type+"_MASSAGE_"+language;
        try {
            Vars var = new Vars();
            Field field = var.getClass().getDeclaredField(Massage);
            field.setAccessible(true);
//            Massage = field.get(Vars.class).toString();
            Massage = (String)field.get(var);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        getToast(mContext, Massage, duration);
    }

    public String getMassage(String type, String language) {
        String Massage = null;
        Massage = type+"_MASSAGE_"+language;
        Vars var = new Vars();
        try {
            Massage = (String)var.getClass().getDeclaredField(Massage).get(var);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return Massage;
    }

    public void getToast(Context mContext, String msg, int duration) {
        TextFontUtil tf = new TextFontUtil();
        Toast toast = Toast.makeText(mContext, msg, duration);
        /**android11 대응 **/
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R){


        ViewGroup group = (ViewGroup) toast.getView();
        Log.e("test","view group : " + group);
        group.setBackgroundColor(Color.parseColor("#00000000"));
//        group.setBackgroundResource(R.mipmap.warning);
        TextView messageTextView = (TextView) group.getChildAt(0);
        messageTextView.setTextColor(Color.parseColor("#AAAAAA"));
//        messageTextView.setTextColor(Color.parseColor("#FFFFFF"));

        tf.setNanumSquareL(mContext, messageTextView);
        messageTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 27);
        messageTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        messageTextView.setBackgroundResource(R.mipmap.warning);
        messageTextView.setGravity(Gravity.CENTER);
        toast.setGravity(Gravity.CENTER, 0, 0);

        }else{

            if(Build.VERSION.SDK_INT <Build.VERSION_CODES.R) {
                ViewGroup group = (ViewGroup) toast.getView();
                Log.e("test","view group : " + group);
                group.setBackgroundColor(Color.parseColor("#00000000"));
//        group.setBackgroundResource(R.mipmap.warning);
                TextView messageTextView = (TextView) group.getChildAt(0);
                messageTextView.setTextColor(Color.parseColor("#AAAAAA"));
//        messageTextView.setTextColor(Color.parseColor("#FFFFFF"));

                tf.setNanumSquareL(mContext, messageTextView);
                messageTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 27);
                messageTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                messageTextView.setBackgroundResource(R.mipmap.warning);
                messageTextView.setGravity(Gravity.CENTER);
                toast.setGravity(Gravity.CENTER, 0, 0);
            }

        }

        toast.show();

    }

    public String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy.MM.dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public String getQuarterLastday(int year, int quarter) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
        int month = 3;
//        quarter == 1 ? month = 3 : quarter == 2 ? month = 6 : quarter == 3 ? month = 9 : month = 12;
        if(quarter == 2)
            month = 6;
        else if(quarter == 3)
            month = 9;
        else if(quarter == 4)
            month = 12;

        Calendar cal = Calendar.getInstance();
//        int year = cal.get(Calendar.YEAR);
//        cal.set(quarter == 1 ? year-1 : year , month-1, 1); //월은 -1해줘야 해당월로 인식
        cal.set(year , month-1, 1); //월은 -1해줘야 해당월로 인식
//        System.out.println(cal.get(Calendar.MONTH)+1+"월");
//        System.out.println("마지막 일:"+cal.getMaximum(Calendar.DAY_OF_MONTH));
//        Log.e("getQuarterLastday",dateFormat.format(cal.getTime()));
        return dateFormat.format(cal.getTime());
    }

    public String getDateTime2() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        Log.e("test","date :: " + dateFormat.format(date));
        return dateFormat.format(date);
    }

    public MemberInfo getDateTime(String todate) {
        Log.e("test","왜 Null?todate : " + todate);

        Date endDate = null;
        Calendar calendar = Calendar.getInstance();
        MemberInfo Date = new MemberInfo();
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            calendar.setTime (formatter.parse(todate));

            calendar.add(Calendar.MONTH, Vars.KEY_HALF_YEAR);
//            calendar.add(Calendar.MONTH, 3);
//            calendar.add(Calendar.DAY_OF_WEEK, 1);
            endDate = calendar.getTime();

            Date.setStart_date(todate);
            Date.setEnd_date(formatter.format(endDate).trim());
            /*calendar.add(Calendar.MONTH, (1 * (cnt - 1)));
            beginDate = calendar.getTime();
            Date.setStart_date(formatter.format(beginDate).trim());

            calendar.add(Calendar.MONTH, (1));
            endDate = calendar.getTime();
            Date.setEnd_date(formatter.format(endDate).trim());*/


//            Log.e("CbtApplication getTime", "!!!!start!!!! "+ Date.getStart_date() );
//            Log.e("CbtApplication getTime", "!!!!end!!!!!! "+ Date.getEnd_date() + "\n\n");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return Date;
    }
    public int getYear() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy", Locale.getDefault());
        Date date = new Date();
        return Integer.valueOf(dateFormat.format(date));
    }

    public int getQuarter() {
        Calendar c = Calendar.getInstance();
        
        return (int) Math.ceil((c.get(Calendar.MONTH) + 1) / 3.0);
    }

    public Boolean CloseLicense(Context con) {
        Date endDate = null;
        Date toDate = null;
        MemberDao Mdio = new MemberDao(con);
        MemberInfo Mi = Mdio.getcloseLicense(getDateTime2());
        /*Log.e("CloseLicense", "!!!!idx!!!! "+ Mi.getIdx());*/
//        Log.e("CloseLicense", "!!!!start!! "+ Mi.getStart_date());
//        Log.e("CloseLicense", "!!!!end!!!! "+ Mi.getEnd_date());
//        Log.e("CloseLicense", "!!!!key!!!! "+ Mi.getKey());

        if(Mi.getStart_date() != null && Mi.getEnd_date() != null) {
            try {

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                toDate = formatter.parse(getDateTime2());
                endDate = formatter.parse(Mi.getEnd_date());

//                Log.e("CbtApplication getTime", "!!!!today!!!! " + toDate);
                //Log.e("CbtApplication getTime", "!!!!start!!!! " + Mi.getStart_date());
//                Log.e("CbtApplication getTime", "!!!!end!!!! " + endDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            int compare = endDate.compareTo(toDate);
//            Log.e("CbtApplication getTime", "!!!!compare!!!! " + compare);
            /*if (compare > 0)
                Log.e("CbtApplication getTime", "false!!!!!!!! endDate > today");
            else if (compare < 0) {
                Log.e("CbtApplication getTime", "true!!!!!!!! endDate < today");
                return true;
            } else {
                Log.e("CbtApplication getTime", "false!!!!!!!! endDate = today");
            }*/
            if (compare < 0)
                return true;
        }else {
            return true;
        }

        return false;
    }

    /** 안드로이드 11 대응 권한여부 상태값저장 메서드 (true/false) **/
    @TargetApi(Build.VERSION_CODES.R)
    private static boolean isFileGranted(Context mContext){
        boolean granted = false; // 권한 부여 상태값 저장
        try {
            //파일 접근 권한이 허용된 경우
            if(Environment.isExternalStorageManager() == true){
                granted = true;
            }else{
                granted = false;
            }

        }catch (Throwable why){

        }
        return granted;
    }

    /** 안드로이드 11 대응 퍼미션 체크 메서드 **/

    public static void checkFilePermission(Context mContext){

    }
}