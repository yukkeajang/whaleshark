package com.togetherseatech.whaleshark.util;

import android.content.Context;
import android.util.Log;

import com.togetherseatech.whaleshark.Db.History.TrainingDao;
import com.togetherseatech.whaleshark.Db.History.TrainingInfo;
import com.togetherseatech.whaleshark.Db.Member.MemberDao;
import com.togetherseatech.whaleshark.Db.Member.MemberInfo;
import com.togetherseatech.whaleshark.Db.Training.TrainingsDao;
import com.togetherseatech.whaleshark.Db.Training.TrainingsInfo;

import java.util.ArrayList;
import java.util.List;

/**
+ * Exports pending training history to CSV on app startup so crews can append
+ * newly recorded sessions to the existing offline files without manual taps.
+ */

public class StartupCsvExporter {

    private static final String TAG = "StartupCsvExporter";

    private final Context context;

    public StartupCsvExporter(Context context) {
        this.context = context;
    }

    public void exportPendingCsv() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    export();
                } catch (Exception e) {
                    Log.e(TAG, "Failed to export CSV", e);
                }
            }
        }).start();
    }

    private void export() {
        TrainingDao trainingDao = new TrainingDao(context);
        TrainingsDao trainingsDao = new TrainingsDao(context);
        MemberDao memberDao = new MemberDao(context);
        MemberInfo admin = memberDao.getVslMember("ADMIN", "Administration");

        if (admin == null) {
            Log.w(TAG, "Admin account not found; skipping CSV export");
            return;
        }

        boolean exported = false;
        int vslType = admin.getVsl_type();

        ArrayList<TrainingInfo> trainingList = trainingDao.getAllDownloadData(vslType);
        if (!trainingList.isEmpty()) {
            SaveCSV csv = new SaveCSV(context, "AllDownloadData.csv");
            exported = csv.save(csv.getDownload(trainingList)) > 0 || exported;

            ArrayList<TrainingInfo> relativeRegulations = new ArrayList<>();
            for (int i = 0; i < trainingList.size(); i++) {
                List<TrainingInfo> trainingProblems = trainingDao.getProblems(trainingList.get(i).getIdx());
                if (!trainingProblems.isEmpty()) {
                    for (int j = 0; j < trainingProblems.size(); j++) {
                        if (trainingProblems.get(j).getRelative_regulation().trim().length() > 0) {
                            TrainingInfo info = new TrainingInfo();
                            info.setHistroy_idx(trainingProblems.get(j).getHistroy_idx());
                            info.setRelative_regulation(trainingProblems.get(j).getRelative_regulation());
                            relativeRegulations.add(info);
                        }
                    }
                } else {
                    TrainingInfo info = new TrainingInfo();
                    info.setHistroy_idx(trainingList.get(i).getIdx());
                    info.setRelative_regulation("");
                    relativeRegulations.add(info);
                }
            }

            if (!relativeRegulations.isEmpty()) {
                SaveCSV csv2 = new SaveCSV(context, "AllDownloadData2.csv");
                exported = csv2.save(csv2.getDownload2(relativeRegulations)) > 0 || exported;
            }
        }

        ArrayList<TrainingsInfo> trainingsList = trainingsDao.getAllDownloadData(vslType);
        if (!trainingsList.isEmpty()) {
            ArrayList<TrainingsInfo> trainingMembers = new ArrayList<>();

            for (int i = 0; i < trainingsList.size(); i++) {
                List<TrainingsInfo> historyMembers = trainingsDao.getHistoryMember(trainingsList.get(i).getIdx());
                for (int j = 0; j < historyMembers.size(); j++) {
                    TrainingsInfo info = new TrainingsInfo();
                    info.setHistroy_idx(historyMembers.get(j).getHistroy_idx());
                    info.setType(trainingsList.get(i).getType());
                    info.setRank(historyMembers.get(j).getRank());
                    info.setSurName(historyMembers.get(j).getSurName());
                    info.setFirstName(historyMembers.get(j).getFirstName());
                    trainingMembers.add(info);
                }
                trainingsList.get(i).setMaster_idx(historyMembers.get(0).getMaster_idx());
            }

            SaveCSV csv4 = new SaveCSV(context, "AllDownloadData4.csv");
            exported = csv4.save(csv4.getDownload4(trainingMembers)) > 0 || exported;

            SaveCSV csv3 = new SaveCSV(context, "AllDownloadData3.csv");
            exported = csv3.save(csv3.getDownload3(trainingsList)) > 0 || exported;
        }

        if (exported) {
            trainingDao.updateData();
            trainingsDao.updateData();
        }
    }
}
