package com.togetherseatech.whaleshark.Db.Training;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by seonghak on 2017. 10. 23..
 */

public class TrainingsInfo implements Parcelable {

    String title;
    String contents;
    String year;
    String agreement;
    String raw;

    int idx;
    int cidx;
    String type;
    String vsl;
    int vsl_type;
    int training_course;
    int training_course2;
    String date;
    int score;

    int histroy_idx;
    int member_idx;
    int master_idx;
    int rank;
    String fname;
    String sname;
    String submit;

    int navigation_senior;
    int navigation_junior;
    int navigation_rating;
    int engine_senior;
    int engine_junior;
    int engine_rating;
    int others_rating;
    boolean isMChecked;

    public TrainingsInfo() {

    }

    public TrainingsInfo(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

    public TrainingsInfo(String title, int training_course, int training_course2) {
        this.title = title;
        this.training_course = training_course;
        this.training_course2 = training_course2;
    }

    public TrainingsInfo(String title, String raw, int idx, int training_course) {
        this.title = title;
        this.raw = raw;
        this.idx = idx;
        this.training_course = training_course;
    }

    public TrainingsInfo(String year, String agreement, String title, String raw, int training_course, boolean isMChecked) {
        this.year = year;
        this.agreement = agreement;
        this.title = title;
        this.raw = raw;
        this.training_course = training_course;
        this.isMChecked = isMChecked;
    }

    public TrainingsInfo(String type, int training_course, int training_course2, int navigation_senior, int navigation_junior, int navigation_rating, int engine_senior, int engine_junior, int engine_rating, int others_rating) {
        this.type = type;
        this.training_course = training_course;
        this.training_course2 = training_course2;
        this.navigation_senior = navigation_senior;
        this.navigation_junior = navigation_junior;
        this.navigation_rating = navigation_rating;
        this.engine_senior = engine_senior;
        this.engine_junior = engine_junior;
        this.engine_rating = engine_rating;
        this.others_rating = others_rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getAgreement() {
        return agreement;
    }

    public void setAgreement(String agreement) {
        this.agreement = agreement;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public int getTraining_course() {
        return training_course;
    }

    public void setTraining_course(int training_course) {
        this.training_course = training_course;
    }

    public int getTraining_course2() {
        return training_course2;
    }

    public void setTraining_course2(int training_course2) {
        this.training_course2 = training_course2;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getHistroy_idx() {
        return histroy_idx;
    }

    public void setHistroy_idx(int histroy_idx) {
        this.histroy_idx = histroy_idx;
    }

    public int getMaster_idx() {
        return master_idx;
    }

    public void setMaster_idx(int master_idx) {
        this.master_idx = master_idx;
    }

    public int getMember_idx() {
        return member_idx;
    }

    public void setMember_idx(int member_idx) {
        this.member_idx = member_idx;
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

    public String getSubmit() {
        return submit;
    }

    public void setSubmit(String submit) {
        this.submit = submit;
    }

    public int getNavigation_senior() {
        return navigation_senior;
    }

    public void setNavigation_senior(int navigation_senior) {
        this.navigation_senior = navigation_senior;
    }

    public int getNavigation_junior() {
        return navigation_junior;
    }

    public void setNavigation_junior(int navigation_junior) {
        this.navigation_junior = navigation_junior;
    }

    public int getNavigation_rating() {
        return navigation_rating;
    }

    public void setNavigation_rating(int navigation_rating) {
        this.navigation_rating = navigation_rating;
    }

    public int getEngine_senior() {
        return engine_senior;
    }

    public void setEngine_senior(int engine_senior) {
        this.engine_senior = engine_senior;
    }

    public int getEngine_junior() {
        return engine_junior;
    }

    public void setEngine_junior(int engine_junior) {
        this.engine_junior = engine_junior;
    }

    public int getEngine_rating() {
        return engine_rating;
    }

    public void setEngine_rating(int engine_rating) {
        this.engine_rating = engine_rating;
    }

    public int getOthers_rating() {
        return others_rating;
    }

    public void setOthers_rating(int others_rating) {
        this.others_rating = others_rating;
    }

    public Boolean getMChecked() {
        return isMChecked;
    }

    public void setMChecked(Boolean checked) {
        isMChecked = checked;
    }

    public TrainingsInfo(Parcel source) {

        this.title = source.readString();
        this.contents = source.readString();
        this.year = source.readString();
        this.agreement = source.readString();
        this.raw = source.readString();

        this.idx = source.readInt();
        this.type = source.readString();
        this.vsl = source.readString();
        this.vsl_type = source.readInt();
        this.training_course = source.readInt();
        this.training_course2 = source.readInt();
        this.date = source.readString();
        this.score = source.readInt();

        this.histroy_idx = source.readInt();
        this.rank = source.readInt();
        this.fname = source.readString();
        this.sname = source.readString();
        this.submit = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int arg1) {

        dest.writeString(title);
        dest.writeString(contents);
        dest.writeString(year);
        dest.writeString(agreement);
        dest.writeString(raw);

        dest.writeInt(idx);
        dest.writeString(type);
        dest.writeString(vsl);
        dest.writeInt(vsl_type);
        dest.writeInt(training_course);
        dest.writeInt(training_course2);
        dest.writeString(date);
        dest.writeInt(score);

        dest.writeInt(histroy_idx);
        dest.writeInt(rank);
        dest.writeString(fname);
        dest.writeString(sname);
        dest.writeString(submit);
    }


    public static final Creator<TrainingsInfo> CREATOR = new Creator<TrainingsInfo>() {

        @Override
        public TrainingsInfo createFromParcel(Parcel source) {
            return new TrainingsInfo(source);
        }

        @Override
        public TrainingsInfo[] newArray(int size) {
            return new TrainingsInfo[size];
        }

    };

}
