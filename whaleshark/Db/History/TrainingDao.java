package com.togetherseatech.whaleshark.Db.History;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.togetherseatech.whaleshark.Db.DBContactHelper;

import com.togetherseatech.whaleshark.Db.Problem.ProblemInfo;
import com.togetherseatech.whaleshark.Vars;
import com.togetherseatech.whaleshark.util.SelectItems;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TrainingDao extends DBContactHelper {

	private SQLiteDatabase SqlDB;
	private SelectItems Si;

	public TrainingDao(Context context) {
		super(context);
	}

	// 새로운 DBContactInfo 함수 추가
	public int getHistoryIdx() {
		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT IFNULL(MAX(IDX),0) +1 AS IDX FROM " + Vars.TABLE_TRAINING_HISTORY;

		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null)
			cursor.moveToFirst();
		// return member
		return cursor.getInt(0);
	}

	public void addHistory(TrainingInfo training) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(Vars.KEY_IDX, training.getIdx());
		values.put(Vars.KEY_MEMBER_IDX, training.getMember_idx());
		values.put(Vars.KEY_TRAINING_COURSE, training.getTraining_course());
		values.put(Vars.KEY_YEAR, training.getYear());
		values.put(Vars.KEY_QUARTER, training.getQuarter());
		values.put(Vars.KEY_DATE, training.getDate());
		values.put(Vars.KEY_TIME, training.getTime());
		values.put(Vars.KEY_SCORE, training.getScore());
		values.put(Vars.KEY_SUBMIT, training.getSubmit());

		// Inserting Row
		db.insert(Vars.TABLE_TRAINING_HISTORY, null, values);
		db.close(); // Closing database connection
	}

	public void addHistorys(List<TrainingInfo> arr) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "INSERT INTO "+ Vars.TABLE_TRAINING_HISTORY +" VALUES (?,?,?,?,?,?,?,?,?);";
		SQLiteStatement statement = db.compileStatement(sql);
		db.beginTransaction();

		for (int i = 0; i < arr.size(); i++) {
			statement.clearBindings();
			statement.bindNull(1);
			statement.bindLong(2, arr.get(i).getMember_idx());
			statement.bindLong(3, arr.get(i).getTraining_course());
			statement.bindLong(4, arr.get(i).getYear());
			statement.bindLong(5, arr.get(i).getQuarter());
			statement.bindNull(6);
			statement.bindNull(7);
			statement.bindNull(8);
			statement.bindString(9, arr.get(i).getSubmit());
//			statement.bindString(3, arr.get(i).getDate());
//			statement.bindLong(4, arr.get(i).getTime());
//			statement.bindLong(5, arr.get(i).getScore());

			statement.execute();
		}
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	public void addHistoryProblems(List<TrainingInfo> arr) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "INSERT INTO "+ Vars.TABLE_TRANING_PROBLEM +" VALUES (?,?,?,?);";
		SQLiteStatement statement = db.compileStatement(sql);
		db.beginTransaction();

		for (int i = 0; i < arr.size(); i++) {
			statement.clearBindings();
			statement.bindNull(1);
			statement.bindLong(2, arr.get(i).getHistroy_idx());
			statement.bindLong(3, arr.get(i).getProblem_idx());
			statement.bindString(4, arr.get(i).getRelative_regulation());

			statement.execute();
		}
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	public TrainingInfo getHistory(int idx, int year, int quarter, int training_course) {
		TrainingInfo training = new TrainingInfo();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(Vars.TABLE_TRAINING_HISTORY, null,  Vars.KEY_MEMBER_IDX + " = ? AND " + Vars.KEY_YEAR + " = ? AND " + Vars.KEY_QUARTER + " = ? AND " + Vars.KEY_TRAINING_COURSE + " = ?"
				, new String[] { String.valueOf(idx), String.valueOf(year), String.valueOf(quarter), String.valueOf(training_course)}, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();

			Log.e("getHistory" , cursor.getColumnName(0) + " : "+cursor.getInt(0));
			Log.e("getHistory." , cursor.getColumnName(1) + " : "+cursor.getInt(1));
			Log.e("getHistory." , cursor.getColumnName(2) + " : "+cursor.getInt(2));
			Log.e("getHistory." , cursor.getColumnName(3) + " : "+cursor.getInt(3));
			Log.e("getHistory." , cursor.getColumnName(4) + " : "+cursor.getInt(4));
			Log.e("getHistory." , cursor.getColumnName(5) + " : "+cursor.getString(5));
			Log.e("getHistory." , cursor.getColumnName(6) + " : "+cursor.getInt(6));
			Log.e("getHistory." , cursor.getColumnName(7) + " : "+cursor.getInt(7));
			Log.e("getHistory." , cursor.getColumnName(8) + " : "+cursor.getString(8));
			Log.e("getHistory." , "//////////////////////////////////");

			training.setIdx(cursor.getInt(0));
			training.setMember_idx(cursor.getInt(1));
			training.setTraining_course(cursor.getInt(2));
			training.setYear(cursor.getInt(3));
			training.setQuarter(cursor.getInt(4));
			training.setDate(cursor.getString(5));
			training.setTime(cursor.getString(6) == null ? 0 : cursor.getInt(6));
			training.setScore(cursor.getString(7) == null ? 0 : cursor.getInt(7));
			training.setSubmit(cursor.getString(8));
		} else {
			training = null;
		}
		return training;
	}

	public ArrayList<TrainingInfo> getHistorys(int idx, int year, int quarter) {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<TrainingInfo> trainingList = new ArrayList<TrainingInfo>();
//		Log.e("getHistorys","idx : "+idx + " / quarter : "+ quarter);
		Cursor cursor = db.query(Vars.TABLE_TRAINING_HISTORY, null,  Vars.KEY_MEMBER_IDX + " = ? AND " + Vars.KEY_YEAR + " = ? AND " + Vars.KEY_QUARTER + " = ?"
				, new String[] { String.valueOf(idx), String.valueOf(year), String.valueOf(quarter)}, null, null, Vars.KEY_TRAINING_COURSE);
//		Cursor cursor = db.query(Vars.TABLE_TRAINING_HISTORY, null,  Vars.KEY_MEMBER_IDX + " = ? ", new String[] { String.valueOf(idx)}, null, null, null);
//		Log.e("getHistorys","cursor : "+cursor.getCount());
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
//				Log.e("getHistorys" , cursor.getColumnName(0) + " : "+cursor.getInt(0));
//				Log.e("getHistorys" , cursor.getColumnName(1) + " : "+cursor.getInt(1));
//				Log.e("getHistorys" , cursor.getColumnName(2) + " : "+cursor.getInt(2));
//				Log.e("getHistorys" , cursor.getColumnName(3) + " : "+cursor.getInt(3));
//				Log.e("getHistorys" , cursor.getColumnName(4) + " : "+cursor.getInt(4));
//				Log.e("getHistorys" , cursor.getColumnName(5) + " : "+cursor.getString(5));
//				Log.e("getHistorys" , cursor.getColumnName(6) + " : "+cursor.getInt(6));
//				Log.e("getHistorys" , cursor.getColumnName(7) + " : "+cursor.getInt(7));
//				Log.e("getHistorys" , cursor.getColumnName(8) + " : "+cursor.getString(8));
//				Log.e("getHistorys" , "//////////////////////////////////");

				TrainingInfo training = new TrainingInfo();
				training.setIdx(cursor.getInt(0));
				training.setMember_idx(cursor.getInt(1));
				training.setTraining_course(cursor.getInt(2));
				training.setYear(cursor.getInt(3));
				training.setQuarter(cursor.getInt(4));
				training.setDate(cursor.getString(5));
				training.setTime(cursor.getString(6) == null ? 0 : cursor.getInt(6));
				training.setScore(cursor.getString(7) == null ? 0 : cursor.getInt(7));
				training.setSubmit(cursor.getString(8));
				// Adding problem to list
				trainingList.add(training);
			} while (cursor.moveToNext());
		}

		// return training
		return trainingList;
	}

	public List<TrainingInfo> getUpdateData(int vsl_type) {

		String selectQuery = "SELECT * FROM("
								+ " SELECT A.RANK, A.FNAME, A.SNAME, A.BIRTH, A.SIGN_OFF, B.IDX, B.MEMBER_IDX, B.TRAINING_COURSE, B.DATE, B.SCORE, B.SUBMIT, '' TYPE, A.VSL_TYPE"
								+ " FROM " + Vars.TABLE_MEMBER + " A, " + Vars.TABLE_TRAINING_HISTORY + " B"
								+ " WHERE A.IDX = B.MEMBER_IDX"
								+ " UNION ALL"
								+ " SELECT 1 RANK, '' FNAME, '' SNAME, '' BIRTH, null SIGN_OFF, IDX, '' MEMBER_IDX, TRAINING_COURSE, DATE, SCORE, SUBMIT, TYPE, VSL_TYPE"
								+ " FROM " + Vars.TABLE_TRAININGS_HISTORY
								+ ") AA"
								+ " WHERE 1 = 1"
								+ " AND AA.VSL_TYPE = " + vsl_type
								+ " AND AA.SUBMIT = 'N'"
								+ " AND AA.DATE IS NOT NULL"
								+ " ORDER BY AA.DATE DESC";

		List<TrainingInfo> trainingList = new ArrayList<TrainingInfo>();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
//				Log.e("getUpdateData" , cursor.getColumnName(0) + " : "+cursor.getInt(0));
//				Log.e("getUpdateData" , cursor.getColumnName(1) + " : "+cursor.getString(1));
//				Log.e("getUpdateData" , cursor.getColumnName(2) + " : "+cursor.getString(2));
//				Log.e("getUpdateData" , cursor.getColumnName(3) + " : "+cursor.getString(3));
//				Log.e("getUpdateData" , cursor.getColumnName(4) + " : "+cursor.getString(4));
//				Log.e("getUpdateData" , cursor.getColumnName(5) + " : "+cursor.getInt(5));
//				Log.e("getUpdateData" , cursor.getColumnName(6) + " : "+cursor.getInt(6));
//				Log.e("getUpdateData" , cursor.getColumnName(7) + " : "+cursor.getString(7));
//				Log.e("getUpdateData" , cursor.getColumnName(8) + " : "+cursor.getString(8));
//				Log.e("getUpdateData" , cursor.getColumnName(9) + " : "+cursor.getInt(9));
//				Log.e("getUpdateData" , cursor.getColumnName(10) + " : "+cursor.getString(10));
//				Log.e("getUpdateData" , cursor.getColumnName(11) + " : "+cursor.getString(11));
//				Log.e("getUpdateData" , "//////////////////////////////////");
				String tc[] = cursor.getString(7).split("-");
//				Log.e("getUpdateData" , "tc[] : "+tc.length);

				TrainingInfo training = new TrainingInfo();
				training.setRank(cursor.getInt(0));
				training.setFirstName(cursor.getString(1));
				training.setSurName(cursor.getString(2));
				training.setBirth(cursor.getString(3));
				training.setSign_off(cursor.getString(4));
				training.setIdx(cursor.getInt(5));
				training.setMember_idx(cursor.getInt(6));
				if(tc.length == 1)
					training.setTraining_course(cursor.getInt(7));
				else {
					training.setTraining_course(Integer.valueOf(tc[0]));
					training.setTraining_course2(Integer.valueOf(tc[1]));
				}

				training.setDate(cursor.getString(8));
				training.setScore(cursor.getInt(9));
				training.setSubmit(cursor.getString(10));
				training.setType(cursor.getString(11).equals("") ? "T" :  cursor.getString(11));
				// Adding training to list
				trainingList.add(training);
			} while (cursor.moveToNext());
		}

		// return training list
		return trainingList;
	}

	public List<TrainingInfo> getAllUpdateData(int vsl_type) {

		String selectQuery = "SELECT * FROM("
				+ " SELECT A.RANK, A.FNAME, A.SNAME, A.BIRTH, A.SIGN_OFF, B.IDX, B.MEMBER_IDX, B.TRAINING_COURSE, B.DATE, B.SCORE, B.SUBMIT, '' TYPE, A.VSL_TYPE"
				+ " FROM " + Vars.TABLE_MEMBER + " A, " + Vars.TABLE_TRAINING_HISTORY + " B"
				+ " WHERE A.IDX = B.MEMBER_IDX"
				+ " UNION ALL"
				+ " SELECT 1 RANK, '' FNAME, '' SNAME, '' BIRTH, null SIGN_OFF, IDX, '' MEMBER_IDX, TRAINING_COURSE, DATE, SCORE, SUBMIT, TYPE, VSL_TYPE"
				+ " FROM " + Vars.TABLE_TRAININGS_HISTORY
				+ ") AA"
				+ " WHERE 1 = 1"
				+ " AND AA.VSL_TYPE = " + vsl_type
				+ " AND AA.SUBMIT = 'Y'"
				+ " AND AA.DATE IS NOT NULL"
				+ " ORDER BY AA.DATE DESC";

		List<TrainingInfo> trainingList = new ArrayList<TrainingInfo>();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
//				Log.e("getUpdateData" , cursor.getColumnName(0) + " : "+cursor.getInt(0));
//				Log.e("getUpdateData" , cursor.getColumnName(1) + " : "+cursor.getString(1));
//				Log.e("getUpdateData" , cursor.getColumnName(2) + " : "+cursor.getString(2));
//				Log.e("getUpdateData" , cursor.getColumnName(3) + " : "+cursor.getString(3));
//				Log.e("getUpdateData" , cursor.getColumnName(4) + " : "+cursor.getString(4));
//				Log.e("getUpdateData" , cursor.getColumnName(5) + " : "+cursor.getInt(5));
//				Log.e("getUpdateData" , cursor.getColumnName(6) + " : "+cursor.getInt(6));
//				Log.e("getUpdateData" , cursor.getColumnName(7) + " : "+cursor.getString(7));
//				Log.e("getUpdateData" , cursor.getColumnName(8) + " : "+cursor.getString(8));
//				Log.e("getUpdateData" , cursor.getColumnName(9) + " : "+cursor.getInt(9));
//				Log.e("getUpdateData" , cursor.getColumnName(10) + " : "+cursor.getString(10));
//				Log.e("getUpdateData" , cursor.getColumnName(11) + " : "+cursor.getString(11));
//				Log.e("getUpdateData" , "//////////////////////////////////");
				String tc[] = cursor.getString(7).split("-");
//				Log.e("getUpdateData" , "tc[] : "+tc.length);

				TrainingInfo training = new TrainingInfo();
				training.setRank(cursor.getInt(0));
				training.setFirstName(cursor.getString(1));
				training.setSurName(cursor.getString(2));
				training.setBirth(cursor.getString(3));
				training.setSign_off(cursor.getString(4));
				training.setIdx(cursor.getInt(5));
				training.setMember_idx(cursor.getInt(6));
				if(tc.length == 1)
					training.setTraining_course(cursor.getInt(7));
				else {
					training.setTraining_course(Integer.valueOf(tc[0]));
					training.setTraining_course2(Integer.valueOf(tc[1]));
				}

				training.setDate(cursor.getString(8));
				training.setScore(cursor.getInt(9));
				training.setSubmit(cursor.getString(10));
				training.setType(cursor.getString(11).equals("") ? "T" :  cursor.getString(11));
				// Adding training to list
				trainingList.add(training);
			} while (cursor.moveToNext());
		}

		// return training list
		return trainingList;
	}

	public int test() {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(Vars.KEY_SUBMIT, "N");

		// updating row
		return db.update(Vars.TABLE_TRAINING_HISTORY, values, Vars.KEY_SUBMIT + " = ? AND "+ Vars.KEY_DATE + " IS NOT NULL",
				new String[] { "Y"});
	}

	public int test2(int midx, int tc, int y, int q, String date) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(Vars.KEY_DATE, date);
		// updating row
		return db.update(Vars.TABLE_TRAINING_HISTORY, values, Vars.KEY_MEMBER_IDX + " = ? AND "+ Vars.KEY_TRAINING_COURSE + " = ? AND "+ Vars.KEY_YEAR + " = ? AND "+ Vars.KEY_QUARTER + " = ?",
				new String[] {String.valueOf(midx), String.valueOf(tc), String.valueOf(y), String.valueOf(q)});
	}

	public int updateData() {
		Si = new SelectItems();
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(Vars.KEY_SUBMIT, "Y");

		// updating row
		return db.update(Vars.TABLE_TRAINING_HISTORY, values, Vars.KEY_SUBMIT + " = ? AND "+ Vars.KEY_DATE + " IS NOT NULL",
				new String[] { "N"});
	}

	public ArrayList<TrainingInfo> getDownloadData(int vsl_type) {

		String selectQuery = "SELECT B.IDX, A.MASTER_IDX, A.RANK, A.FNAME, A.SNAME, A.VSL_NAME, A.VSL_TYPE, A.BIRTH, A.NATIONAL, A.SIGN_ON, A.SIGN_OFF"
								+ ", B.TRAINING_COURSE, B.YEAR, B.QUARTER, B.DATE, B.TIME, B.SCORE"
								+ " FROM " + Vars.TABLE_MEMBER + " A, " + Vars.TABLE_TRAINING_HISTORY + " B"
								+ " WHERE A.IDX = B.MEMBER_IDX"
								+ " AND A.VSL_TYPE = " + vsl_type
								+ " AND B.SUBMIT = 'N'"
								+ " AND B.DATE IS NOT NULL"
								+ " ORDER BY B.DATE DESC";

		ArrayList<TrainingInfo> trainingList = new ArrayList<TrainingInfo>();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
//				Log.e("getUpdateData" , cursor.getColumnName(0) + " : "+cursor.getInt(0));
//				Log.e("getUpdateData" , cursor.getColumnName(1) + " : "+cursor.getInt(1));
//				Log.e("getUpdateData" , cursor.getColumnName(2) + " : "+cursor.getInt(2));
//				Log.e("getUpdateData" , cursor.getColumnName(3) + " : "+cursor.getString(3));
//				Log.e("getUpdateData" , cursor.getColumnName(4) + " : "+cursor.getInt(4));
//				Log.e("getUpdateData" , cursor.getColumnName(5) + " : "+cursor.getString(5));
//				Log.e("getUpdateData" , cursor.getColumnName(6) + " : "+cursor.getInt(6));
//				Log.e("getUpdateData" , cursor.getColumnName(7) + " : "+cursor.getString(7));
//				Log.e("getUpdateData" , cursor.getColumnName(8) + " : "+cursor.getInt(8));
//				Log.e("getUpdateData" , cursor.getColumnName(9) + " : "+cursor.getString(9));
//				Log.e("getUpdateData" , cursor.getColumnName(10) + " : "+cursor.getString(10));
//				Log.e("getUpdateData" , cursor.getColumnName(11) + " : "+cursor.getInt(11));
//				Log.e("getUpdateData" , cursor.getColumnName(12) + " : "+cursor.getInt(12));
//				Log.e("getUpdateData" , cursor.getColumnName(13) + " : "+cursor.getInt(13));
//				Log.e("getUpdateData" , cursor.getColumnName(14) + " : "+cursor.getString(14));
//				Log.e("getUpdateData" , cursor.getColumnName(15) + " : "+cursor.getInt(15));
//				Log.e("getUpdateData" , cursor.getColumnName(16) + " : "+cursor.getInt(16));

//				Log.e("getUpdateData" , "//////////////////////////////////");
				TrainingInfo training = new TrainingInfo();
				training.setIdx(cursor.getInt(0));
				training.setMaster_idx(cursor.getInt(1));
				training.setRank(cursor.getInt(2));
				training.setFirstName(cursor.getString(3));
				training.setSurName(cursor.getString(4));
				training.setVsl(cursor.getString(5));
				training.setVsl_type(cursor.getInt(6));
				training.setBirth(cursor.getString(7));
				training.setNational(cursor.getInt(8));
				training.setSign_on(cursor.getString(9));
				training.setSign_off(cursor.getString(10));
				training.setTraining_course(cursor.getInt(11));
				training.setYear(cursor.getInt(12));
				training.setQuarter(cursor.getInt(13));
				training.setDate(cursor.getString(14));
				training.setTime(cursor.getInt(15));
				training.setScore(cursor.getInt(16));

				// Adding training to list
				trainingList.add(training);
			} while (cursor.moveToNext());
		}

		// return training list
		return trainingList;
	}

	public ArrayList<TrainingInfo> getAllDownloadData(int vsl_type) {

		String selectQuery = "SELECT B.IDX, A.MASTER_IDX, A.RANK, A.FNAME, A.SNAME, A.VSL_NAME, A.VSL_TYPE, A.BIRTH, A.NATIONAL, A.SIGN_ON, A.SIGN_OFF"
				+ ", B.TRAINING_COURSE, B.YEAR, B.QUARTER, B.DATE, B.TIME, B.SCORE"
				+ " FROM " + Vars.TABLE_MEMBER + " A, " + Vars.TABLE_TRAINING_HISTORY + " B"
				+ " WHERE A.IDX = B.MEMBER_IDX"
				+ " AND A.VSL_TYPE = " + vsl_type
				+ " AND B.SUBMIT = 'Y'"
				+ " AND B.DATE IS NOT NULL"
				+ " ORDER BY B.DATE DESC";
//		Log.e("getAllDownloadData" , "selectQuery : " + selectQuery);
		ArrayList<TrainingInfo> trainingList = new ArrayList<TrainingInfo>();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				/*Log.e("getAllDownloadData" , cursor.getColumnName(0) + " : "+cursor.getInt(0));
				Log.e("getAllDownloadData" , cursor.getColumnName(1) + " : "+cursor.getInt(1));
				Log.e("getAllDownloadData" , cursor.getColumnName(2) + " : "+cursor.getInt(2));
				Log.e("getAllDownloadData" , cursor.getColumnName(3) + " : "+cursor.getString(3));
				Log.e("getAllDownloadData" , cursor.getColumnName(4) + " : "+cursor.getInt(4));
				Log.e("getAllDownloadData" , cursor.getColumnName(5) + " : "+cursor.getString(5));
				Log.e("getAllDownloadData" , cursor.getColumnName(6) + " : "+cursor.getInt(6));
				Log.e("getAllDownloadData" , cursor.getColumnName(7) + " : "+cursor.getString(7));
				Log.e("getAllDownloadData" , cursor.getColumnName(8) + " : "+cursor.getInt(8));
				Log.e("getAllDownloadData" , cursor.getColumnName(9) + " : "+cursor.getString(9));
				Log.e("getAllDownloadData" , cursor.getColumnName(10) + " : "+cursor.getString(10));
				Log.e("getAllDownloadData" , cursor.getColumnName(11) + " : "+cursor.getInt(11));
				Log.e("getAllDownloadData" , cursor.getColumnName(12) + " : "+cursor.getInt(12));
				Log.e("getAllDownloadData" , cursor.getColumnName(13) + " : "+cursor.getInt(13));
				Log.e("getAllDownloadData" , cursor.getColumnName(14) + " : "+cursor.getString(14));
				Log.e("getAllDownloadData" , cursor.getColumnName(15) + " : "+cursor.getInt(15));
				Log.e("getAllDownloadData" , cursor.getColumnName(16) + " : "+cursor.getInt(16));

				Log.e("getAllDownloadData" , "//////////////////////////////////");*/
				TrainingInfo training = new TrainingInfo();
				training.setIdx(cursor.getInt(0));
				training.setMaster_idx(cursor.getInt(1));
				training.setRank(cursor.getInt(2));
				training.setFirstName(cursor.getString(3));
				training.setSurName(cursor.getString(4));
				training.setVsl(cursor.getString(5));
				training.setVsl_type(cursor.getInt(6));
				training.setBirth(cursor.getString(7));
				training.setNational(cursor.getInt(8));
				training.setSign_on(cursor.getString(9));
				training.setSign_off(cursor.getString(10));
				training.setTraining_course(cursor.getInt(11));
				training.setYear(cursor.getInt(12));
				training.setQuarter(cursor.getInt(13));
				training.setDate(cursor.getString(14));
				training.setTime(cursor.getInt(15));
				training.setScore(cursor.getInt(16));

				// Adding training to list
				trainingList.add(training);
			} while (cursor.moveToNext());
		}

		// return training list
		return trainingList;
	}

	public ArrayList<TrainingInfo> getHistoryData(int idx) {

		String selectQuery = "SELECT A.RANK, A.FNAME, A.SNAME, A.BIRTH, A.VSL_NAME, A.VSL_TYPE, A.SIGN_ON, A.SIGN_OFF, B.IDX, B.MEMBER_IDX, B.TRAINING_COURSE, B.DATE, B.TIME, B.SCORE, B.SUBMIT, B.YEAR, B.QUARTER"
				+ " FROM " + Vars.TABLE_MEMBER + " A, " + Vars.TABLE_TRAINING_HISTORY + " B"
				+ " WHERE A.IDX = B.MEMBER_IDX"
//				+ " AND B.SUBMIT = 'N'"
				+ " AND B.DATE IS NOT NULL"
				+ " AND A.IDX = '" + idx + "'"
				+ " ORDER BY B.DATE DESC";

		Log.e("getHistoryData" , "selectQuery : " + selectQuery);

		ArrayList<TrainingInfo> trainingList = new ArrayList<TrainingInfo>();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
//				Log.e("getHistoryData" , cursor.getColumnName(0) + " : "+cursor.getInt(0));
//				Log.e("getHistoryData" , cursor.getColumnName(1) + " : "+cursor.getString(1));
//				Log.e("getHistoryData" , cursor.getColumnName(2) + " : "+cursor.getString(2));
//				Log.e("getHistoryData" , cursor.getColumnName(3) + " : "+cursor.getString(3));
//				Log.e("getHistoryData" , cursor.getColumnName(4) + " : "+cursor.getString(4));
//				Log.e("getHistoryData" , cursor.getColumnName(5) + " : "+cursor.getInt(5));
//				Log.e("getHistoryData" , cursor.getColumnName(6) + " : "+cursor.getString(6));
//				Log.e("getHistoryData" , cursor.getColumnName(7) + " : "+cursor.getString(7));
//				Log.e("getHistoryData" , cursor.getColumnName(8) + " : "+cursor.getInt(8));
//				Log.e("getHistoryData" , cursor.getColumnName(9) + " : "+cursor.getInt(9));
//				Log.e("getHistoryData" , cursor.getColumnName(10) + " : "+cursor.getInt(10));
//				Log.e("getHistoryData" , cursor.getColumnName(11) + " : "+cursor.getString(11));
//				Log.e("getHistoryData" , cursor.getColumnName(12) + " : "+cursor.getInt(12));
//				Log.e("getHistoryData" , cursor.getColumnName(13) + " : "+cursor.getInt(13));
//				Log.e("getHistoryData" , cursor.getColumnName(14) + " : "+cursor.getString(14));
//				Log.e("getHistoryData" , cursor.getColumnName(15) + " : "+cursor.getInt(15));
//				Log.e("getHistoryData" , cursor.getColumnName(16) + " : "+cursor.getInt(16));
//				Log.e("getHistoryData" , "//////////////////////////////////");
				TrainingInfo training = new TrainingInfo();
				training.setRank(cursor.getInt(0));
				training.setFirstName(cursor.getString(1));
				training.setSurName(cursor.getString(2));
				training.setBirth(cursor.getString(3));
				training.setVsl(cursor.getString(4));
				training.setVsl_type(cursor.getInt(5));
				training.setSign_on(cursor.getString(6));
				training.setSign_off(cursor.getString(7));
				training.setIdx(cursor.getInt(8));
				training.setMember_idx(cursor.getInt(9));
				training.setTraining_course(cursor.getInt(10));
				training.setDate(cursor.getString(11));
				training.setTime(cursor.getInt(12));
				training.setScore(cursor.getInt(13));
				training.setSubmit(cursor.getString(14));

				// Adding training to list
				trainingList.add(training);
			} while (cursor.moveToNext());
		}

		// return training list
		return trainingList;
	}

	public ArrayList<TrainingInfo> getStatusDatas(int year, int quarter, int vsl_type) {

		String selectQuery = "SELECT IDX, RANK, FNAME, SNAME, SIGN_ON"
				+ " , CASE WHEN IDXC > 0 THEN ("
				+ "		CASE WHEN ING == IDXC THEN  'All Completed' "
				+ " 		ELSE CASE WHEN ING > 0 AND ING < IDXC THEN 'In Training···'"
				+ " 				ELSE 'Not Started' END"
				+ " 		END)"
				+ "  	ELSE  NULL END AS ING"
				+ " , LAST_DATE"
				+ " FROM ("
				+ " 	SELECT A.IDX, A.RANK, A.FNAME, A.SNAME, A.SIGN_ON, A.DEL_YN"
				+ " 		, (SELECT DATE "
				+ "					FROM TRAINING_HISTORY"
				+ "					WHERE A.IDX = MEMBER_IDX"
				+ " 				AND A.VSL_TYPE = " + vsl_type
				+ " 				AND QUARTER = " + quarter
				+ " 				AND YEAR = " + year
				+ " 				ORDER BY DATE DESC LIMIT 1) AS LAST_DATE"
				+ " 		, COUNT(A.IDX) AS IDXC"
				+ " 		, SUM(CASE WHEN B.DATE IS NULL THEN  0  ELSE 1 END )AS ING"
				+ " 		FROM MEMBER A, TRAINING_HISTORY B"
				+ " 		WHERE A.IDX = B.MEMBER_IDX"
				+ " 		AND A.VSL_TYPE = " + vsl_type
				+ " 		AND B.QUARTER = " + quarter
				+ " 		AND B.YEAR = " + year
				+ " 		AND A.DEL_YN = 'N'"
				+ " 		GROUP BY A.IDX, A.RANK, A.FNAME, A.SNAME, A.SIGN_ON, A.DEL_YN"
				+ " )"
				+ " GROUP BY IDX, RANK, FNAME, SNAME, ING"
				+ " ORDER BY RANK";

		ArrayList<TrainingInfo> trainingList = new ArrayList<TrainingInfo>();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
//				Log.e("getStatusData" , cursor.getColumnName(0) + " : "+cursor.getInt(0));
//				Log.e("getStatusData" , cursor.getColumnName(1) + " : "+cursor.getInt(1));
//				Log.e("getStatusData" , cursor.getColumnName(2) + " : "+cursor.getString(2));
//				Log.e("getStatusData" , cursor.getColumnName(3) + " : "+cursor.getString(3));
//				Log.e("getStatusData" , cursor.getColumnName(4) + " : "+cursor.getString(4));
//				Log.e("getStatusData" , cursor.getColumnName(5) + " : "+cursor.getString(5));
//				Log.e("getStatusData" , cursor.getColumnName(6) + " : "+cursor.getString(6));
//				Log.e("getStatusData" , "//////////////////////////////////");
				TrainingInfo training = new TrainingInfo();
				training.setIdx(cursor.getInt(0));
				training.setRank(cursor.getInt(1));
				training.setFirstName(cursor.getString(2));
				training.setSurName(cursor.getString(3));
				training.setSign_on(cursor.getString(4));
				training.setIng(cursor.getString(5));
				training.setDate(cursor.getString(6));

				// Adding training to list
				trainingList.add(training);
			} while (cursor.moveToNext());
		}

		// return training list
		return trainingList;
	}
	public void getStatusTest(int year, int quarter, int vsl_type) {
		String selectQuery = "SELECT A."+ Vars.KEY_MEMBER_IDX +", A."+ Vars.KEY_DATE +", A." + Vars.KEY_TRAINING_COURSE+", B." + Vars.KEY_DEL_YN+", B."+Vars.KEY_SNAME+", B."+Vars.KEY_FNAME
				+ "					FROM TRAINING_HISTORY A, MEMBER B"
				+ "					WHERE B."+ Vars.KEY_IDX +" = A."+ Vars.KEY_MEMBER_IDX
				+ "					AND A."+ Vars.KEY_YEAR +" = " + year
				+ "					AND A."+ Vars.KEY_QUARTER +" = " + quarter
				+ "					AND B."+ Vars.KEY_VSL_TYPE +" = " + vsl_type
				+ "					AND B."+ Vars.KEY_DEL_YN +" = 'N' ";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				Log.e("getStatusTest" , cursor.getColumnName(0) + " : "+cursor.getInt(0));
				Log.e("getStatusTest" , cursor.getColumnName(1) + " : "+cursor.getString(1));
				Log.e("getStatusTest" , cursor.getColumnName(2) + " : "+cursor.getInt(2));
				Log.e("getStatusTest" , cursor.getColumnName(3) + " : "+cursor.getString(3));
				Log.e("getStatusTest" , cursor.getColumnName(4) + " : "+cursor.getString(4));
				Log.e("getStatusTest" , cursor.getColumnName(5) + " : "+cursor.getString(5));
			} while (cursor.moveToNext());
		}
	}

	public String getStatusPRate(int year, int quarter, int vsl_type) {
		String selectQuery = "SELECT (MEMBER_S / MEMBER_C) AS MEMBER_AVG"	// MEMBER_C =  멤버별 이수한 과목수 총합 / MEMBER_S = 맴버별 RATE 총합 = 총 맴버 SCORE AVG
							+ "	FROM ("
							+ " SELECT COUNT(RATE) AS MEMBER_C, SUM(RATE) AS MEMBER_S"
							+ " 	FROM ("
							+ "		SELECT MEMBER_IDX, CHATER_C, NDATE_C, (CAST(NDATE_C AS float) / CAST(CHATER_C AS float)) * 100 AS RATE"	//(이수한 과목수 / 총과목수) * 100 = RATE
							+ " 		FROM ("
							+ "			SELECT "+ Vars.KEY_MEMBER_IDX
							+ "				, CHATER_C, SUM(CASE WHEN "+ Vars.KEY_DATE +" IS NULL THEN  0  ELSE 1 END) AS NDATE_C"
							+ "				FROM ("
							+ "				SELECT A."+ Vars.KEY_MEMBER_IDX +", B."+ Vars.KEY_RANK
							+ "					, CASE WHEN (B.RANK > 0 AND B.RANK < 5) OR (B.RANK > 8 AND B.RANK < 12) OR B.RANK = 18 THEN 11"
							+ "						ELSE CASE WHEN (B.RANK > 4 AND B.RANK < 9) OR (B.RANK > 11 AND B.RANK < 15) OR B.RANK = 19 THEN 9"
							+ "							ELSE CASE WHEN B.RANK > 14 AND B.RANK < 18 THEN 8 END END END AS CHATER_C"
							+ "					, A."+ Vars.KEY_DATE +", A." + Vars.KEY_TRAINING_COURSE+", B." + Vars.KEY_DEL_YN
							+ "					FROM TRAINING_HISTORY A, MEMBER B"
							+ "					WHERE B."+ Vars.KEY_IDX +" = A."+ Vars.KEY_MEMBER_IDX
							+ "					AND A."+ Vars.KEY_YEAR +" = " + year
							+ "					AND A."+ Vars.KEY_QUARTER +" = " + quarter
							+ "					AND B."+ Vars.KEY_VSL_TYPE +" = " + vsl_type
							+ "					AND B."+ Vars.KEY_DEL_YN +" = 'N' "
							+ "				) AA"
							+ "				GROUP BY "+ Vars.KEY_MEMBER_IDX
							+ "			) BB"
							+ "		) CC"
							+ " ) DD";
		Log.e("getStatusPRate", "selectQuery : " + selectQuery);
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor != null && cursor.getCount() > 0)
			cursor.moveToFirst();
//		Log.e("getStatusPRate" , cursor.getColumnName(0) + " : "+cursor.getDouble(0));
		String Rate = String.format("%.1f",cursor.getDouble(0));
		// return training list
		return Rate;
	}

	public ArrayList<String> getStatusPData(int year, int quarter, int vsl_type, int idx) {
		ArrayList<String> trainingList = new ArrayList<String>();
		String selectQuery = "SELECT (CAST(NDATE_C AS float) / CAST(CHATER_C AS float)) * 100 AS RATE"	//(이수한 과목수 / 총과목수) * 100 = RATE
							//+ "	, (CAST(SCORE_C AS float) / CAST(CHATER_C AS float)) AS AVG"			//스코어 총합 / 총과목수 = SCORE AVG
							+ "	, (CAST(SCORE_C AS float) / CAST(NDATE_C AS float)) AS AVG"			//이수한 과목 스코어 총합 / 이수한 과목 수 = SCORE AVG
							+ "	, (SELECT "+ Vars.KEY_DATE
							+ "		FROM TRAINING_HISTORY A, MEMBER B"
							+ "		WHERE B."+ Vars.KEY_IDX +" = A."+ Vars.KEY_MEMBER_IDX
							+ "		AND A."+ Vars.KEY_YEAR +" = " + year
							+ "		AND A."+ Vars.KEY_QUARTER +" = " + quarter
							+ "		AND B."+ Vars.KEY_VSL_TYPE +" = " + vsl_type
							+ "		AND A."+ Vars.KEY_MEMBER_IDX +" = " + idx
							+ "		ORDER BY "+ Vars.KEY_DATE +" DESC LIMIT 1) AS LAST_DATE"
							+ " FROM ("
							+ "	SELECT "+ Vars.KEY_MEMBER_IDX +", CHATER_C"
							+ "		, SUM("+ Vars.KEY_SCORE +") AS SCORE_C, SUM(CASE WHEN "+ Vars.KEY_DATE +" IS NULL THEN  0  ELSE 1 END) AS NDATE_C"
							+ "		FROM ("
							+ "		SELECT "+ Vars.KEY_MEMBER_IDX +", "+ Vars.KEY_SCORE +", "+ Vars.KEY_DATE +", "+ Vars.KEY_TRAINING_COURSE
							+ "				, CASE WHEN (B.RANK > 0 AND B.RANK < 5) OR (B.RANK > 8 AND B.RANK < 12) OR B.RANK = 18 THEN 11"
							+ "					ELSE CASE WHEN (B.RANK > 4 AND B.RANK < 9) OR (B.RANK > 11 AND B.RANK < 15) OR B.RANK = 19 THEN 9"
							+ "						ELSE CASE WHEN B.RANK > 14 AND B.RANK < 18 THEN 8 END END END AS CHATER_C"
							+ "			FROM TRAINING_HISTORY A, MEMBER B"
							+ "			WHERE B."+ Vars.KEY_IDX +" = A."+ Vars.KEY_MEMBER_IDX
							+ "			AND A."+ Vars.KEY_YEAR +" = " + year
							+ "			AND A."+ Vars.KEY_QUARTER +" = " + quarter
							+ "			AND B."+ Vars.KEY_VSL_TYPE +" = " + vsl_type
							+ "			AND A."+ Vars.KEY_MEMBER_IDX +" = " + idx
							+ "		) A"
							+ "		GROUP BY "+ Vars.KEY_MEMBER_IDX
							+ "	) B";
//		Log.e("getStatusPData", "selectQuery : " + selectQuery);
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
//		Log.e("getStatusPData", "cursor.getCount() : " + cursor.getCount());

		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();

//			Log.e("getStatusPData", cursor.getColumnName(0) + " : " + cursor.getDouble(0));
//			Log.e("getStatusPData", cursor.getColumnName(1) + " : " + cursor.getDouble(1));
//			Log.e("getStatusPData", cursor.getColumnName(2) + " : " + cursor.getString(2));
			trainingList.add(String.format("%.1f",cursor.getDouble(0)));
			trainingList.add(String.format("%.1f",cursor.getDouble(1)));
			trainingList.add(cursor.getString(2) != null ? cursor.getString(2) : "-");
		} else {
			trainingList.add("0.0");
			trainingList.add("0.0");
			trainingList.add("-");
		}
		// return training list
		return trainingList;
	}

	public List<TrainingInfo> getProblems(int idx) {
		Log.e("getProblems" , "idx : "+ idx);
		List<TrainingInfo> trainingList = new ArrayList<>();
		// Select All Query

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(Vars.TABLE_TRANING_PROBLEM, null,  Vars.KEY_HISTORY_IDX + " = ?",
				new String[] { String.valueOf(idx) }, null, null, null, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Log.e("getProblems" , cursor.getColumnName(0) + " : "+cursor.getInt(0));
				Log.e("getProblems" , cursor.getColumnName(1) + " : "+cursor.getInt(1));
				Log.e("getProblems" , cursor.getColumnName(2) + " : "+cursor.getInt(2));
				Log.e("getProblems" , cursor.getColumnName(3) + " : "+cursor.getString(3));
				Log.e("getProblems" , "//////////////////////////////////////");
				TrainingInfo training = new TrainingInfo();
				training.setIdx(cursor.getInt(0));
				training.setHistroy_idx(cursor.getInt(1));
				training.setProblem_idx(cursor.getInt(2));
				training.setRelative_regulation(cursor.getString(3));
				// Adding problem to list
				trainingList.add(training);
			} while (cursor.moveToNext());
		}

		// return training
		return trainingList;
	}

	//Problem 정보 업데이트
	public int updateEndHistory(int idx, int year, int quarter ) {
//		Log.e("updateEndHistory",idx+"/"+year+"/"+quarter);
		String Date = "";
//		if(quarter == 1)

		Si = new SelectItems();
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
//		values.put(Vars.KEY_MEMBER_IDX, training.getMember_idx());
//		values.put(Vars.KEY_TRAINING_COURSE, training.getTraining_course());
//		values.put(Vars.KEY_QUARTER, training.getQuarter());
		values.put(Vars.KEY_DATE, Si.getQuarterLastday(year, quarter));
		values.put(Vars.KEY_TIME, 0);
		values.put(Vars.KEY_SCORE, 0);

		// updating row
		return db.update(Vars.TABLE_TRAINING_HISTORY, values, Vars.KEY_MEMBER_IDX + " = ? AND " + Vars.KEY_YEAR + " = ? AND " + Vars.KEY_QUARTER + " = ? AND " + Vars.KEY_DATE + " IS NULL",
				new String[] { String.valueOf(idx), String.valueOf(year), String.valueOf(quarter)});
	}

	public int updateHistory(TrainingInfo training) {
		Si = new SelectItems();
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(Vars.KEY_DATE, Si.getDateTime());
		values.put(Vars.KEY_TIME, training.getTime());
		values.put(Vars.KEY_SCORE, training.getScore());

		// updating row
		return db.update(Vars.TABLE_TRAINING_HISTORY, values, Vars.KEY_IDX + " = ?",
				new String[] { String.valueOf(training.getIdx()) });
	}

	// Problem 정보 삭제하기
	public void deleteHistory(TrainingInfo training) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(Vars.TABLE_TRAINING_HISTORY, Vars.KEY_IDX + " = ?",
				new String[] { String.valueOf(training.getIdx()) });
		db.close();
	}

	public void deleteProblem(TrainingInfo training) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(Vars.TABLE_TRANING_PROBLEM, Vars.KEY_IDX + " = ?",
				new String[] { String.valueOf(training.getIdx()) });
		db.close();
	}

	// History 정보 숫자
	public int getHistoryCount() {
		String countQuery = "SELECT  * FROM " + Vars.TABLE_TRAINING_HISTORY;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

	public int getProblemCount() {
		String countQuery = "SELECT  * FROM " + Vars.TABLE_TRANING_PROBLEM;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

}