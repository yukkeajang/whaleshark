package com.togetherseatech.whaleshark.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.togetherseatech.whaleshark.AdminUpdateAct;
import com.togetherseatech.whaleshark.Db.History.TrainingDao;
import com.togetherseatech.whaleshark.Db.History.TrainingInfo;
import com.togetherseatech.whaleshark.Db.Training.TrainingsDao;
import com.togetherseatech.whaleshark.Db.Training.TrainingsInfo;
import com.togetherseatech.whaleshark.Vars;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by seonghak on 2017. 12. 12..
 */

public class SaveCSV {

    private File file;
    private Context mContext;

    public SaveCSV(Context context, String filename){

        this.mContext = context;
        File folder = new File(Environment.getExternalStorageDirectory() + "/SeaTech");
        if (!folder.exists()) {
            //Toast.makeText(MainActivity.this, "Directory Does Not Exist, Create It", Toast.LENGTH_SHORT).show();
            folder.mkdir();
        }

        this.file = new File(folder, filename);
    }

    public void end(){
        TrainingDao Tdao = new TrainingDao(mContext);
        TrainingsDao Tsdao = new TrainingsDao(mContext);
        Tdao.updateData();
        Tsdao.updateData();
        AdminUpdateAct.mAdapter.clear();
        AdminUpdateAct.mAdapter.notifyDataSetChanged();
    }

    public int save(ArrayList<String[]> list){
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return 0;
//                throw new RuntimeException("Unable to create File " + e);
            }
        }
        try {
            FileWriter writer = new FileWriter(file, true);
            int startIndex = (fileExists && list.size() > 0) ? 1 : 0;
            for(int i = startIndex; i < list.size(); i++) {
                String[] row = list.get(i);
                for(int j = 0; j < row.length; j++) {
                    writer.write(row[j]);
//                    Log.e("save", j +" : " + row[j]);
                    if(j != (row.length - 1)) {
                        writer.write(',');
                    }
                    else {
                        writer.write('\n');
                    }
                }
            }
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
            return 0;
//            throw new RuntimeException("Unable to write to File " + e);
        }
        return 1;
    }

    public ArrayList<String[]> getDownload(ArrayList<TrainingInfo> array) {
        ArrayList<String[]> stringList = new ArrayList<String[]>();

        String[] Mrow = new String[]{Vars.KEY_IDX, Vars.KEY_MASTER_IDX, Vars.KEY_RANK, Vars.KEY_FNAME, Vars.KEY_SNAME, Vars.KEY_VSL_NAME,
                Vars.KEY_VSL_TYPE, Vars.KEY_BIRTH, Vars.KEY_NATIONAL, Vars.KEY_SIGN_ON, Vars.KEY_SIGN_OFF,
                Vars.KEY_TRAINING_COURSE, Vars.KEY_YEAR, Vars.KEY_QUARTER, Vars.KEY_DATE, Vars.KEY_TIME, Vars.KEY_SCORE};
        stringList.add(Mrow);

        for(int i = 0; i < array.size(); i++){
            String[] row = new String[]{String.valueOf(array.get(i).getIdx()), String.valueOf(array.get(i).getMaster_idx()),
                    String.valueOf(array.get(i).getRank()), array.get(i).getFirstName(), array.get(i).getSurName(), array.get(i).getVsl(),
                    String.valueOf(array.get(i).getVsl_type()), array.get(i).getBirth(), String.valueOf(array.get(i).getNational()),
                    array.get(i).getSign_on(), array.get(i).getSign_off() != null ? array.get(i).getSign_off() : "",
                    String.valueOf((array.get(i).getTraining_course()+1)), String.valueOf(array.get(i).getYear()), String.valueOf(array.get(i).getQuarter()),
                    array.get(i).getDate(), String.valueOf(array.get(i).getTime()), String.valueOf(array.get(i).getScore())};
            stringList.add(row);
        }
        return stringList;
    }

    public ArrayList<String[]> getDownload2(ArrayList<TrainingInfo> array) {
        ArrayList<String[]> stringList = new ArrayList<String[]>();

        String[] Mrow = new String[]{Vars.KEY_HISTORY_IDX, Vars.KEY_RELATIVE_REGULATION};
        stringList.add(Mrow);

        for(int i = 0; i < array.size(); i++) {
//            Log.e("","getDownload2 Relative_regulation : " + array.get(i).getRelative_regulation());
            String[] row = new String[]{String.valueOf(array.get(i).getHistroy_idx()), array.get(i).getRelative_regulation()};
            stringList.add(row);
        }
        return stringList;
    }

    public ArrayList<String[]> getDownload3(ArrayList<TrainingsInfo> array) {
        ArrayList<String[]> stringList = new ArrayList<String[]>();
        SelectItems Si = new SelectItems();
        String[] Mrow = new String[]{Vars.KEY_IDX, Vars.KEY_MASTER_IDX, Vars.KEY_TYPE, Vars.KEY_VSL_NAME, Vars.KEY_VSL_TYPE, Vars.KEY_TRAINING_COURSE, Vars.KEY_DATE, Vars.KEY_SCORE};
        stringList.add(Mrow);
        String TrainingCourese = "";
        for(int i = 0; i < array.size(); i++) {
            if(array.get(i).getType().equals("R"))
                TrainingCourese = Si.getNewRegulation(array.get(i).getTraining_course(), "ENG");
            else if(array.get(i).getType().equals("G"))
                TrainingCourese = Si.getGeneralTraining(array.get(i).getTraining_course(), array.get(i).getTraining_course2(), "ENG");
//            Log.e("","getDownload3 TrainingCourese : " + TrainingCourese);
            String[] row = new String[]{String.valueOf(array.get(i).getIdx()), String.valueOf(array.get(i).getMaster_idx()), array.get(i).getType(), array.get(i).getVsl(),
                    String.valueOf(array.get(i).getVsl_type()), TrainingCourese, array.get(i).getDate(), String.valueOf(array.get(i).getScore())};
            stringList.add(row);
        }
        return stringList;
    }

    public ArrayList<String[]> getDownload4(ArrayList<TrainingsInfo> array) {
        ArrayList<String[]> stringList = new ArrayList<String[]>();
        SelectItems Si = new SelectItems();
        String[] Mrow = new String[]{Vars.KEY_HISTORY_IDX, Vars.KEY_TYPE, Vars.KEY_RANK, Vars.KEY_SNAME, Vars.KEY_FNAME};
        stringList.add(Mrow);
        String TrainingCourese = "";
        for(int i = 0; i < array.size(); i++) {
//            Log.e("","getDownload3 Relative_regulation : " + array.get(i).getRelative_regulation());
            String[] row = new String[]{String.valueOf(array.get(i).getHistroy_idx()),  array.get(i).getType(),
                    String.valueOf(array.get(i).getRank()), array.get(i).getSurName(), array.get(i).getFirstName()};
            stringList.add(row);
        }
        return stringList;
    }
}

