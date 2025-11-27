package com.togetherseatech.whaleshark.Db.History;

import android.os.Parcel;
import android.os.Parcelable;

import com.togetherseatech.whaleshark.Db.Member.MemberInfo;

/**
 * Created by seonghak on 2017. 10. 23..
 */

public class TrainingInfo implements Parcelable {


    int idx;
    int master_idx;

    // TRAINING_HISTORY Table Columns names
    int member_idx;
    int training_course;
    int training_course2;
    int year;
    int quarter;
    String date;
    int time;
    int score;
    String submit;

    //TRANING_PROBLEM Table Columns names
    int histroy_idx;
    int problem_idx;
    String relative_regulation;

    //MEMBER Table Columns names
    int rank;
    String fname;
    String sname;
    String birth;
    int national;
    String vsl;
    int vsl_type;
    String sign_on;
    String sign_off;
    String ing;
    String type;

    public TrainingInfo() {

    }

    // PROBLEM Table
    // TRAINING_HISTORY Table
    public TrainingInfo(int training_course, String date) {
        this.training_course = training_course;
        this.date = date;
    }

    public TrainingInfo(int member_idx, int training_course, int quarter, String date, int time, int score, String submit) {
        this.member_idx = member_idx;
        this.training_course = training_course;
        this.quarter = quarter;
        this.date = date;
        this.time = time;
        this.score = score;
        this.submit = submit;
    }

    public TrainingInfo(int idx, int member_idx, int training_course, int quarter, String date, int time, int score) {
        this.idx = idx;
        this.member_idx = member_idx;
        this.training_course = training_course;
        this.quarter = quarter;
        this.date = date;
        this.time = time;
        this.score = score;
    }

    public TrainingInfo(int rank, String fname, String sname, String birth, String sign_off, int training_course, String date, int score, String submit) {
        this.rank = rank;
        this.fname = fname;
        this.sname = sname;
        this.birth = birth;
        this.sign_off = sign_off;
        this.training_course = training_course;
        this.date = date;
        this.score = score;
        this.submit = submit;
    }

    public TrainingInfo(int master_idx, int rank, String fname, String sname, String vsl, int vsl_type, String birth, int national,
                        String sign_on, String sign_off, int training_course, int year, int quarter, String date, int time, int score) {
        this.master_idx = master_idx;
        this.rank = rank;
        this.fname = fname;
        this.sname = sname;
        this.vsl = vsl;
        this.vsl_type = vsl_type;
        this.birth = birth;
        this.national = national;
        this.sign_on = sign_on;
        this.sign_off = sign_off;
        this.training_course = training_course;
        this.year = year;
        this.quarter = quarter;
        this.date = date;
        this.time = time;
        this.score = score;
    }

    //TRANING_PROBLEM Table
    public TrainingInfo(int histroy_idx, int problem_idx, String relative_regulation) {
//        this.idx = idx;
        this.histroy_idx = histroy_idx;
        this.problem_idx = problem_idx;
        this.relative_regulation = relative_regulation;
    }

    public int getIdx() {
        return this.idx;
    }
    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getMaster_idx() {
        return master_idx;
    }

    public void setMaster_idx(int master_idx) {
        this.master_idx = master_idx;
    }

    // TRAINING_HISTORY Table Columns Get/Set
    public int getMember_idx() { return member_idx; }
    public void setMember_idx(int member_idx) { this.member_idx = member_idx; }

    public int getTraining_course() { return training_course; }
    public void setTraining_course(int training_course) { this.training_course = training_course; }

    public int getTraining_course2() { return training_course2; }
    public void setTraining_course2(int training_course2) { this.training_course2 = training_course2; }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date;    }

    public int getQuarter() {
        return quarter;
    }

    public void setQuarter(int quarter) {
        this.quarter = quarter;
    }

    public int getTime() { return time; }
    public void setTime(int time) { this.time = time; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    // TRAINING_PROBLEM Table Columns Get/Set
    public int getHistroy_idx() { return histroy_idx; }
    public void setHistroy_idx(int histroy_idx) { this.histroy_idx = histroy_idx; }

    public int getProblem_idx() { return problem_idx; }
    public void setProblem_idx(int problem_idx) { this.problem_idx = problem_idx; }

    public String getRelative_regulation() { return relative_regulation; }
    public void setRelative_regulation(String relative_regulation) { this.relative_regulation = relative_regulation; }

    public String getSubmit() {
        return submit;
    }

    public void setSubmit(String submit) {
        this.submit = submit;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getFirstName() {
        return this.fname;
    }
    public void setFirstName(String fname) {
        this.fname = fname;
    }

    public String getSurName() {
        return this.sname;
    }
    public void setSurName(String sname) {
        this.sname = sname;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public int getNational() {
        return national;
    }

    public void setNational(int national) {
        this.national = national;
    }

    public String getVsl() {
        return vsl;
    }

    public void setVsl(String vsl) {
        this.vsl = vsl;
    }

    public int getVsl_type() {
        return vsl_type;
    }

    public void setVsl_type(int vsl_type) {
        this.vsl_type = vsl_type;
    }

    public String getSign_on() {
        return sign_on;
    }

    public void setSign_on(String sign_on) {
        this.sign_on = sign_on;
    }

    public String getSign_off() {
        return sign_off;
    }

    public void setSign_off(String sign_off) {
        this.sign_off = sign_off;
    }

    public String getIng() {
        return ing;
    }

    public void setIng(String ing) {
        this.ing = ing;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TrainingInfo(Parcel source) {
        this.idx = source.readInt();
        this.member_idx = source.readInt();
        this.training_course = source.readInt();
        this.training_course2 = source.readInt();
        this.year = source.readInt();
        this.quarter = source.readInt();
        this.date = source.readString();
        this.time = source.readInt();
        this.score = source.readInt();

        this.rank = source.readInt();
        this.fname = source.readString();
        this.sname = source.readString();
        this.birth = source.readString();
        this.vsl = source.readString();
        this.vsl_type = source.readInt();
        this.sign_on = source.readString();
        this.sign_off = source.readString();

        this.ing = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int arg1) {
        dest.writeInt(idx);
        dest.writeInt(member_idx);
        dest.writeInt(training_course);
        dest.writeInt(training_course2);
        dest.writeInt(year);
        dest.writeInt(quarter);
        dest.writeString(date);
        dest.writeInt(time);
        dest.writeInt(score);

        dest.writeInt(rank);
        dest.writeString(fname);
        dest.writeString(sname);
        dest.writeString(birth);
        dest.writeString(vsl);
        dest.writeInt(vsl_type);
        dest.writeString(sign_on);
        dest.writeString(sign_off);

        dest.writeString(ing);

    }


    public static final Parcelable.Creator<TrainingInfo> CREATOR = new Parcelable.Creator<TrainingInfo>() {

        @Override
        public TrainingInfo createFromParcel(Parcel source) {
            return new TrainingInfo(source);
        }

        @Override
        public TrainingInfo[] newArray(int size) {
            return new TrainingInfo[size];
        }

    };
}
