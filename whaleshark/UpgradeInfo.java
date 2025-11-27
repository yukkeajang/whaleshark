package com.togetherseatech.whaleshark;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by seonghak on 2018. 03. 29..
 */

public class UpgradeInfo implements Parcelable {

    int ver;
    String menu;
    String file_title_kr;
    String file_title_eng;
    String file_name;
    String file_size;
    String total_file_size;
    int mod_ver;

    public UpgradeInfo() {

    }

    public int getVer() {
        return ver;
    }

    public void setVer(int ver) {
        this.ver = ver;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getFile_title_kr() {
        return file_title_kr;
    }

    public void setFile_title_kr(String file_title_kr) {
        this.file_title_kr = file_title_kr;
    }

    public String getFile_title_eng() {
        return file_title_eng;
    }

    public void setFile_title_eng(String file_title_eng) {
        this.file_title_eng = file_title_eng;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_size() {
        return file_size;
    }

    public void setFile_size(String file_size) {
        this.file_size = file_size;
    }

    public String getTotal_file_size() {
        return total_file_size;
    }

    public void setTotal_file_size(String total_file_size) {
        this.total_file_size = total_file_size;
    }

    public int getMod_ver() {
        return mod_ver;
    }

    public void setMod_ver(int mod_ver) {
        this.mod_ver = mod_ver;
    }

    public static Creator<UpgradeInfo> getCREATOR() {
        return CREATOR;
    }

    public UpgradeInfo(Parcel source) {

        this.ver = source.readInt();
        this.menu = source.readString();
        this.file_title_kr = source.readString();
        this.file_title_eng = source.readString();
        this.file_name = source.readString();
        this.file_size = source.readString();
        this.total_file_size = source.readString();
        this.mod_ver = source.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int arg1) {
        dest.writeInt(ver);
        dest.writeString(menu);
        dest.writeString(file_title_kr);
        dest.writeString(file_title_eng);
        dest.writeString(file_name);
        dest.writeString(file_size);
        dest.writeString(total_file_size);
        dest.writeInt(mod_ver);
    }


    public static final Creator<UpgradeInfo> CREATOR = new Creator<UpgradeInfo>() {

        @Override
        public UpgradeInfo createFromParcel(Parcel source) {
            return new UpgradeInfo(source);
        }

        @Override
        public UpgradeInfo[] newArray(int size) {
            return new UpgradeInfo[size];
        }

    };

}
