package com.togetherseatech.whaleshark.Db.Member;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by seonghak on 2017. 10. 23..
 */

public class MemberInfo implements Parcelable {

    // MEMBER Table Columns names
    int idx;
    String auth;
    String id;
    String pw;
    int master_idx;
    int rank;
    String fname;
    String sname;
    String vsl;
    int vsl_type;
    String birth;
    int national;
    String sign_on;
    String sign_off;
    String del_yn;

    String start_date;
    String end_date;
    String key;
    Boolean isChecked;

    public MemberInfo() {

    }

    public MemberInfo(int idx, int rank, String fname, String sname, String birth, int national, String sign_on) {
        this.idx = idx;
        this.rank = rank;
        this.fname = fname;
        this.sname = sname;
        this.birth = birth;
        this.national = national;
        this.sign_on = sign_on;
    }

    public MemberInfo(int idx, int master_idx, int rank, String fname, String sname, String vsl, int vsl_type, String birth, int national, String sign_on, String sign_off, String del_yn) {
        this.idx = idx;
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
        this.del_yn = del_yn;
    }

    public MemberInfo(int idx, int rank, String fname, String sname, String vsl, int vsl_type, String birth, int national, String sign_on) {
        this.idx = idx;
        this.rank = rank;
        this.fname = fname;
        this.sname = sname;
        this.vsl = vsl;
        this.vsl_type = vsl_type;
        this.birth = birth;
        this.national = national;
        this.sign_on = sign_on;
    }

    public MemberInfo(int idx, int rank, String fname,  String sname, String vsl, int vsl_type, String birth, int national, String sign_on, String sign_off) {
        this.idx = idx;
        this.rank = rank;
        this.fname = fname;
        this.sname = sname;
        this.vsl = vsl;
        this.vsl_type = vsl_type;
        this.birth = birth;
        this.national = national;
        this.sign_on = sign_on;
        this.sign_off = sign_off;
        this.del_yn = del_yn;
    }

    public MemberInfo(int idx, String id, String pw, int rank, String fname, String sname, String vsl, int vsl_type, String birth, int national,  String sign_on, String sign_off) {
        this.idx = idx;
        this.id = id;
        this.pw = pw;
        this.rank = rank;
        this.fname = fname;
        this.sname = sname;
        this.vsl = vsl;
        this.vsl_type = vsl_type;
        this.birth = birth;
        this.national = national;
        this.sign_on = sign_on;
        this.sign_off = sign_off;
    }

    // MEMBER Table Columns Get/Set
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

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getPw() {
        return pw;
    }
    public void setPw(String pw) {
        this.pw = pw;
    }

    public int getRank() {
        return this.rank;
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

    public String getVsl() {
        return vsl;
    }
    public void setVsl(String vsl) {
        this.vsl = vsl;
    }

    public int getVsl_type() { return vsl_type; }
    public void setVsl_type(int vsl_type) {
        this.vsl_type = vsl_type;
    }

    public String getBirth() { return this.birth; }
    public void setBirth(String birth) {
        this.birth = birth;
    }

    public int getNational() { return this.national; }
    public void setNational(int national) {
        this.national = national;
    }

    public String getSign_on() { return sign_on; }
    public void setSign_on(String sign_on) { this.sign_on = sign_on; }

    public String getSign_off() { return sign_off; }
    public void setSign_off(String sign_off) { this.sign_off = sign_off; }

    public String getDel_yn() {
        return del_yn;
    }

    public void setDel_yn(String del_yn) {
        this.del_yn = del_yn;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public MemberInfo(Parcel source) {

        this.idx = source.readInt();
        this.rank = source.readInt();
        this.fname = source.readString();
        this.sname = source.readString();
        this.vsl = source.readString();
        this.vsl_type = source.readInt();
        this.birth = source.readString();
        this.national = source.readInt();
        this.sign_on = source.readString();
        this.sign_off = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int arg1) {
        dest.writeInt(idx);
        dest.writeInt(rank);
        dest.writeString(fname);
        dest.writeString(sname);
        dest.writeString(vsl);
        dest.writeInt(vsl_type);
        dest.writeString(birth);
        dest.writeInt(national);
        dest.writeString(sign_on);
        dest.writeString(sign_off);
    }


    public static final Parcelable.Creator<MemberInfo> CREATOR = new Parcelable.Creator<MemberInfo>() {

        @Override
        public MemberInfo createFromParcel(Parcel source) {
            return new MemberInfo(source);
        }

        @Override
        public MemberInfo[] newArray(int size) {
            return new MemberInfo[size];
        }

    };

}
