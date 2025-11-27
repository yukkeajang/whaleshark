package com.togetherseatech.whaleshark.Db.Problem;

import android.os.Parcel;
import android.os.Parcelable;

import com.togetherseatech.whaleshark.Db.Member.MemberInfo;

/**
 * Created by seonghak on 2017. 10. 23..
 */

public class ProblemInfo implements Parcelable {

    // PROBLEM Table Columns names
    int idx;

//    String language_type;
    int no;
    int chapter;
    int level;
    int vsl_type;
    String title_kr;
    String title_eng;
    String answer;
    String relative_regulation;
    String voice_kr;
    String voice_eng;
    String flash_video;
//    String explanation_kr;
//    String explanation_eng;


    // SUB_PROBLEM Table Columns names
    String problem_answer;
    String content_kr;
    String content_eng;

    public ProblemInfo() {

    }

    // PROBLEM Table Columns Get/Set
    public int getIdx() {
        return this.idx;
    }
    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getChapter() {
        return chapter;
    }

    public void setChapter(int chapter) {
        this.chapter = chapter;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getVsl_type() {
        return vsl_type;
    }

    public void setVsl_type(int vsl_type) {
        this.vsl_type = vsl_type;
    }

    public String getTitle_kr() {
        return title_kr;
    }

    public void setTitle_kr(String title_kr) {
        this.title_kr = title_kr;
    }

    public String getTitle_eng() {
        return title_eng;
    }

    public void setTitle_eng(String title_eng) {
        this.title_eng = title_eng;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getRelative_regulation() {
        return relative_regulation;
    }

    public void setRelative_regulation(String relative_regulation) {
        this.relative_regulation = relative_regulation;
    }

    public String getVoice_kr() {
        return voice_kr;
    }

    public void setVoice_kr(String voice_kr) {
        this.voice_kr = voice_kr;
    }

    public String getVoice_eng() {
        return voice_eng;
    }

    public void setVoice_eng(String voice_eng) {
        this.voice_eng = voice_eng;
    }

    public String getFlash_video() {
        return flash_video;
    }

    public void setFlash_video(String flash_video) {
        this.flash_video = flash_video;
    }

/*public String getExplanation_kr() {
        return explanation_kr;
    }

    public void setExplanation_kr(String explanation_kr) {
        this.explanation_kr = explanation_kr;
    }

    public String getExplanation_eng() {
        return explanation_eng;
    }

    public void setExplanation_eng(String explanation_eng) {
        this.explanation_eng = explanation_eng;
    }*/

    // SUB_PROBLEM Table Columns Get/Set
    public String getProblem_answer() { return problem_answer; }
    public void setProblem_answer(String problem_answer) { this.problem_answer = problem_answer; }

    public String getContent_kr() {
        return content_kr;
    }

    public void setContent_kr(String content_kr) {
        this.content_kr = content_kr;
    }

    public String getContent_eng() {
        return content_eng;
    }

    public void setContent_eng(String content_eng) {
        this.content_eng = content_eng;
    }

    public ProblemInfo(Parcel source) {

        this.idx = source.readInt();
        this.no = source.readInt();
        this.chapter = source.readInt();
        this.level = source.readInt();
        this.title_kr = source.readString();
        this.title_eng = source.readString();
        this.answer = source.readString();
        this.relative_regulation = source.readString();
        this.voice_kr = source.readString();
        this.voice_eng = source.readString();
        this.flash_video = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int arg1) {
        dest.writeInt(idx);
        dest.writeInt(no);
        dest.writeInt(chapter);
        dest.writeInt(level);
        dest.writeString(title_kr);
        dest.writeString(title_eng);
        dest.writeString(answer);
        dest.writeString(relative_regulation);
        dest.writeString(voice_kr);
        dest.writeString(voice_eng);
        dest.writeString(flash_video);
    }


    public static final Parcelable.Creator<ProblemInfo> CREATOR = new Parcelable.Creator<ProblemInfo>() {

        @Override
        public ProblemInfo createFromParcel(Parcel source) {
            return new ProblemInfo(source);
        }

        @Override
        public ProblemInfo[] newArray(int size) {
            return new ProblemInfo[size];
        }

    };

}
