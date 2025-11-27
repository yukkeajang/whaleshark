package com.togetherseatech.whaleshark.Db.Training;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.togetherseatech.whaleshark.Db.DBContactHelper;
import com.togetherseatech.whaleshark.Db.Member.MemberInfo;
import com.togetherseatech.whaleshark.Vars;
import com.togetherseatech.whaleshark.util.SelectItems;

import java.util.ArrayList;
import java.util.List;

public class TrainingsDao extends DBContactHelper {

	private SQLiteDatabase SqlDB;
	private SelectItems Si;

	public TrainingsDao(Context context) {
		super(context);
	}


	public int updateGt(String val) {
		String value = "";
		if("5".equals(val))
			value = "5-1";
		else if("6".equals(val))
			value = "5-2";
		else if("12".equals(val))
			value = "12-1";


		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(Vars.KEY_TRAINING_COURSE, value);

		// updating row
		return db.update(Vars.TABLE_TRAININGS_HISTORY, values, Vars.KEY_TRAINING_COURSE + " = ? AND "+ Vars.KEY_TYPE + " = ?",
				new String[] { val, "G"});
	}

	public int updateGt2() {
		String value = "";


		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(Vars.KEY_TRAINING_COURSE, "12");

		// updating row
		return db.update(Vars.TABLE_TRAININGS_HISTORY, values, Vars.KEY_TRAINING_COURSE + " = ? AND "+ Vars.KEY_TYPE + " = ?",
				new String[] { "12-1", "G"});
	}

	public List<Integer> getMatrixCount(String rank) {
		List<Integer> countList = new ArrayList<>();
		String countQuery = "SELECT TYPE, COUNT("+rank+") "+ " FROM " + Vars.TABLE_TRAININGS_MATRIX
							+ "	WHERE 1=1"
							+ "	AND "+ rank + " = 1"
							+ "	GROUP BY TYPE";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		if (cursor.moveToFirst()) {
			do {
//		countList.add(cursor.getInt(0));
		countList.add(cursor.getInt(1));
		Log.e("getMatrixCount" , cursor.getColumnName(0) + " : "+cursor.getInt(0));
		Log.e("getMatrixCount" , cursor.getColumnName(1) + " : "+cursor.getInt(1));
			} while (cursor.moveToNext());
		}

		return countList;
	}

	public void createMatrixtable() {
		SQLiteDatabase db = this.getWritableDatabase();
		/*String countQuery = "SELECT name FROM sqlite_master WHERE type='table'";
		Cursor cursor = db.rawQuery(countQuery, null);
		if(cursor.moveToFirst()) {
			for(;;) {
				Log.e("createMatrixtable" , cursor.getString(0));
				if(cursor.getString(0).equals("TRAININGS_MATRIX")) {

					break;
				}

				if(!cursor.moveToNext()) {
					break;
				}
			}
		}*/
		String CREATE_TRAININGS_MATRIX = "CREATE TABLE IF NOT EXISTS TRAININGS_MATRIX ("
				+ Vars.KEY_IDX + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ Vars.KEY_TYPE + " TEXT NOT NULL,"
				+ Vars.KEY_TRAINING_COURSE + " INTEGER NOT NULL,"
				+ Vars.KEY_TRAINING_COURSE2 + " INTEGER NOT NULL,"
				+ Vars.KEY_NS + " INTEGER NOT NULL,"
				+ Vars.KEY_NJ + " INTEGER NOT NULL,"
				+ Vars.KEY_NR + " INTEGER NOT NULL,"
				+ Vars.KEY_ES + " INTEGER NOT NULL,"
				+ Vars.KEY_EJ + " INTEGER NOT NULL,"
				+ Vars.KEY_ER + " INTEGER NOT NULL,"
				+ Vars.KEY_OSR + " INTEGER NOT NULL )";

		db.execSQL(CREATE_TRAININGS_MATRIX);
	}

	public int getMatrixCount(String type, int course, int course2) {
		String countQuery = "SELECT COUNT(IDX) AS CNT FROM " + Vars.TABLE_TRAININGS_MATRIX;
		if(!"".equals(type)) countQuery += "	WHERE "+ Vars.KEY_TYPE + " = '" + type + "' AND "+ Vars.KEY_TRAINING_COURSE + " = "+ course + " AND "+ Vars.KEY_TRAINING_COURSE2 + " = " + course2;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		//cursor.close();
		if (cursor != null)
			cursor.moveToFirst();
		// return count
		return cursor.getInt(0);
	}

	public List<TrainingsInfo> getMatrixs(String type, String rank) {
//		Log.e("getMatrixs" , "rank : "+rank);
		List<TrainingsInfo> trainingsList = new ArrayList<>();
		String countQuery = "SELECT * FROM " + Vars.TABLE_TRAININGS_MATRIX
							+ "	WHERE "+ Vars.KEY_TYPE + " = '" + type +"'";
		if(!"".equals(rank)) countQuery += "	AND "+ rank + " = 1";
//		Log.e("getMatrixs" , "countQuery : "+countQuery);
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
//		Cursor cursor = db.query(Vars.TABLE_TRAININGS_MATRIX, null,  Vars.KEY_TYPE + " = ?", new String[] {type}, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
//				Log.e("getHistorys" , cursor.getColumnName(0) + " : "+cursor.getInt(0));
//				Log.e("getMatrixs" , cursor.getColumnName(1) + " : "+cursor.getString(1));
//				Log.e("getMatrixs" , cursor.getColumnName(2) + " : "+cursor.getInt(2));
//				Log.e("getMatrixs" , cursor.getColumnName(3) + " : "+cursor.getInt(3));
//				Log.e("getMatrixs" , cursor.getColumnName(4) + " : "+cursor.getInt(4));
//				Log.e("getMatrixs" , cursor.getColumnName(5) + " : "+cursor.getInt(5));
//				Log.e("getMatrixs" , cursor.getColumnName(6) + " : "+cursor.getInt(6));
//				Log.e("getMatrixs" , cursor.getColumnName(7) + " : "+cursor.getInt(7));
//				Log.e("getMatrixs" , cursor.getColumnName(8) + " : "+cursor.getInt(8));
//				Log.e("getMatrixs" , cursor.getColumnName(9) + " : "+cursor.getInt(9));
//				Log.e("getMatrixs" , cursor.getColumnName(10) + " : "+cursor.getInt(10));
//				Log.e("getMatrixs" , "//////////////////////////////////");

				TrainingsInfo trainings = new TrainingsInfo();
				trainings.setIdx(cursor.getInt(0));
				trainings.setType(cursor.getString(1));
				trainings.setTraining_course(cursor.getInt(2));
				trainings.setTraining_course2(cursor.getInt(3));
				trainings.setNavigation_senior(cursor.getInt(4));
				trainings.setNavigation_junior(cursor.getInt(5));
				trainings.setNavigation_rating(cursor.getInt(6));
				trainings.setEngine_senior(cursor.getInt(7));
				trainings.setEngine_junior(cursor.getInt(8));
				trainings.setEngine_rating(cursor.getInt(9));
				trainings.setOthers_rating(cursor.getInt(10));
				// Adding problem to list
				trainingsList.add(trainings);
			} while (cursor.moveToNext());
		}
		// return count
		return trainingsList;
	}

	public void addMatrix(TrainingsInfo trainings) {

//		Log.e("addMatrix" , "KEY_TRAINING_COURSE : "+trainings.getTraining_course() +"-"+ trainings.getTraining_course2());

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(Vars.KEY_TYPE, trainings.getType());
		values.put(Vars.KEY_TRAINING_COURSE, trainings.getTraining_course());
		values.put(Vars.KEY_TRAINING_COURSE2, trainings.getTraining_course2());
		values.put(Vars.KEY_NS, trainings.getNavigation_senior());
		values.put(Vars.KEY_NJ, trainings.getNavigation_junior());
		values.put(Vars.KEY_NR, trainings.getNavigation_rating());
		values.put(Vars.KEY_ES, trainings.getEngine_senior());
		values.put(Vars.KEY_EJ, trainings.getEngine_junior());
		values.put(Vars.KEY_ER, trainings.getEngine_rating());
		values.put(Vars.KEY_OSR, trainings.getOthers_rating());

		Log.e("test","values : " + values);
		// Inserting Row
		db.insert(Vars.TABLE_TRAININGS_MATRIX, null, values);
		db.close(); // Closing database connection
	}

	public int updateMatrix(TrainingsInfo trainings) {

//		Log.e("updateMatrix" , "KEY_TRAINING_COURSE : "+trainings.getTraining_course() +"-"+ trainings.getTraining_course2());
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(Vars.KEY_NS, trainings.getNavigation_senior());
		values.put(Vars.KEY_NJ, trainings.getNavigation_junior());
		values.put(Vars.KEY_NR, trainings.getNavigation_rating());
		values.put(Vars.KEY_ES, trainings.getEngine_senior());
		values.put(Vars.KEY_EJ, trainings.getEngine_junior());
		values.put(Vars.KEY_ER, trainings.getEngine_rating());
		values.put(Vars.KEY_OSR, trainings.getOthers_rating());

		// updating row
		return db.update(Vars.TABLE_TRAININGS_MATRIX, values, Vars.KEY_TYPE + " = '"+ trainings.getType()+"' AND "+ Vars.KEY_TRAINING_COURSE + " = ? AND "+ Vars.KEY_TRAINING_COURSE2 + " = ?",
				new String[] {String.valueOf(trainings.getTraining_course()),String.valueOf(trainings.getTraining_course2())});
	}

	public int deleteMatrix(String type, int Training_course, int Training_course2) {
		SQLiteDatabase db = this.getWritableDatabase();

		// delete row
		return db.delete(Vars.TABLE_TRAININGS_MATRIX, Vars.KEY_TYPE + " = '"+ type+"' AND "+ Vars.KEY_TRAINING_COURSE + " = ? AND "+ Vars.KEY_TRAINING_COURSE2 + " = ?",
				new String[] {String.valueOf(Training_course),String.valueOf(Training_course2)});
	}

	public int allDeleteMatrix() {
		SQLiteDatabase db = this.getWritableDatabase();
		// delete row
		return db.delete(Vars.TABLE_TRAININGS_MATRIX,null,null);
	}

	public int getHistoryIdx() {
		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT IFNULL(MAX(IDX),0) +1 AS IDX FROM " + Vars.TABLE_TRAININGS_HISTORY;

		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null)
			cursor.moveToFirst();

		// return member
		return cursor.getInt(0);
	}

	// 새로운 DBContactInfo 함수 추가
	public void addHistory(TrainingsInfo trainings) {

//		Log.e("addHistory" , "KEY_TRAINING_COURSE : "+trainings.getTraining_course() +"-"+ trainings.getTraining_course2());

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(Vars.KEY_IDX, trainings.getIdx());
		values.put(Vars.KEY_TYPE, trainings.getType());
		values.put(Vars.KEY_VSL_NAME, trainings.getVsl());
		values.put(Vars.KEY_VSL_TYPE, trainings.getVsl_type());
		if(trainings.getTraining_course2() == 0)
			values.put(Vars.KEY_TRAINING_COURSE, trainings.getTraining_course());
		else
			values.put(Vars.KEY_TRAINING_COURSE, trainings.getTraining_course() +"-"+ trainings.getTraining_course2());
		values.put(Vars.KEY_DATE, trainings.getDate());
		values.put(Vars.KEY_SCORE, trainings.getScore());
		values.put(Vars.KEY_SUBMIT, trainings.getSubmit());

		// Inserting Row
		db.insert(Vars.TABLE_TRAININGS_HISTORY, null, values);
		db.close(); // Closing database connection
	}

	public void addHistoryMember(List<TrainingsInfo> arr) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "INSERT INTO "+ Vars.TABLE_TRAININGS_MEMBER +" VALUES (?,?,?);";
		SQLiteStatement statement = db.compileStatement(sql);
		db.beginTransaction();

		for (int i = 0; i < arr.size(); i++) {
			statement.clearBindings();
			statement.bindNull(1);
			statement.bindLong(2, arr.get(i).getHistroy_idx());
			statement.bindLong(3, arr.get(i).getMember_idx());
			/*statement.bindString(4, arr.get(i).getFirstName());
			statement.bindString(5, arr.get(i).getSurName());*/

			statement.execute();
		}
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	public ArrayList<TrainingsInfo> getHistory(String type, int vsltype, MemberInfo mi) {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<TrainingsInfo> trainingsList = new ArrayList<>();
//		Log.e("getHistorys","type : "+type);
		String selectQuery = "SELECT B.*"
					+ " 		FROM (SELECT " + Vars.KEY_HISTORY_IDX
					+ " 				FROM " + Vars.TABLE_TRAININGS_MEMBER
					+ " 				WHERE 1 = 1"
					+ " 				AND " +  Vars.KEY_MEMBER_IDX + " = " + mi.getIdx()
					+ " 				GROUP BY " + Vars.KEY_HISTORY_IDX
					+ " 		) A, " + Vars.TABLE_TRAININGS_HISTORY +" B"
					+ " 		WHERE A." + Vars.KEY_HISTORY_IDX + " = B." + Vars.KEY_IDX
					+ " 		AND B." + Vars.KEY_TYPE +" = '" + type + "'"
					+ " 		AND B." + Vars.KEY_VSL_TYPE +" = "+ vsltype
					+ " 		ORDER BY B.DATE DESC";
//		Log.e("getHistorys","selectQuery : "+selectQuery);
		Cursor cursor = db.rawQuery(selectQuery, null);
//		Cursor cursor = db.query(Vars.TABLE_TRAININGS_HISTORY, null,  Vars.KEY_TYPE + " = ? AND " + Vars.KEY_VSL_TYPE + " = ?"
//				, new String[] { type, String.valueOf(vsltype) }, null, null, Vars.KEY_DATE);
//		Log.e("getHistorys","cursor : "+cursor.getCount());
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
//				Log.e("getHistorys" , cursor.getColumnName(0) + " : "+cursor.getInt(0));
//				Log.e("getHistorys" , cursor.getColumnName(1) + " : "+cursor.getString(1));
//				Log.e("getHistorys" , cursor.getColumnName(2) + " : "+cursor.getString(2));
//				Log.e("getHistorys" , cursor.getColumnName(3) + " : "+cursor.getInt(3));
//				Log.e("getHistorys" , cursor.getColumnName(4) + " : "+cursor.getString(4));
//				Log.e("getHistorys" , cursor.getColumnName(5) + " : "+cursor.getInt(5));
//				Log.e("getHistorys" , cursor.getColumnName(6) + " : "+cursor.getString(6));
//				Log.e("getHistorys" , "//////////////////////////////////");
				String tc[] = cursor.getString(4).split("-");
//				Log.e("getHistorys" , "tc[] : "+tc.length);
				TrainingsInfo trainings = new TrainingsInfo();
				trainings.setIdx(cursor.getInt(0));
				trainings.setType(cursor.getString(1));
				trainings.setVsl(cursor.getString(2));
				trainings.setVsl_type(cursor.getInt(3));
				if(tc.length == 1)
					trainings.setTraining_course(cursor.getInt(4));
				else {
					trainings.setTraining_course(Integer.valueOf(tc[0]));
					trainings.setTraining_course2(Integer.valueOf(tc[1]));
				}
				trainings.setDate(cursor.getString(5));
				trainings.setScore(cursor.getString(6) == null ? 0 : cursor.getInt(6));
				trainings.setSubmit(cursor.getString(7));
				// Adding problem to list
				trainingsList.add(trainings);
			} while (cursor.moveToNext());
		}

		// return training
		return trainingsList;
	}

	public ArrayList<ArrayList<String>> getStatusGTRate(int vsl_type) {
		ArrayList<ArrayList<String>> trainingList = new ArrayList<>();
		String selectQuery = "SELECT TYPE, (MEMBER_S / MEMBER_C) AS MEMBER_AVG"		// 맴버별 RATE 총합 / "G" & "R"별 총합 = 총 맴버 SCORE AVG
				+ " FROM ("
				+ "	SELECT TYPE, COUNT(TYPE) AS MEMBER_C, SUM(RATE) AS MEMBER_S"	// MEMBER_C = "G" & "R"별 총합 , MEMBER_S = 맴버별 "G" & "R"별 RATE 총합
				+ "		FROM ("
				+ "			SELECT "+ Vars.KEY_MEMBER_IDX +", "+ Vars.KEY_TYPE +", CHATER_C, (CAST(T_C AS float) / CAST(CHATER_C AS float)) * 100 AS RATE" //(이수한 과목수 / 총과목수) * 100 = RATE
				+ "				FROM ("
				+ "					SELECT "+ Vars.KEY_MEMBER_IDX +", "+ Vars.KEY_TYPE +", COUNT(TRAINING_COURSE) AS T_C, CHATER_C"
				+ "						FROM ("
				+ "							SELECT "+ Vars.KEY_MEMBER_IDX +", "+ Vars.KEY_TYPE +", "+ Vars.KEY_TRAINING_COURSE +", CHATER_C"
				+ "								FROM ("
				+ "									SELECT A."+ Vars.KEY_MEMBER_IDX +", A."+ Vars.KEY_HISTORY_IDX +", B."+ Vars.KEY_TYPE +", B."+ Vars.KEY_TRAINING_COURSE + ", C."+ Vars.KEY_DEL_YN
				+ "										, (SELECT COUNT(CASE WHEN C.RANK > 0 AND C.RANK < 3 THEN " + Vars.KEY_NS
				+ "														ELSE CASE WHEN C.RANK > 2 AND C.RANK < 5 THEN " + Vars.KEY_NJ
				+ "														ELSE CASE WHEN C.RANK > 4 AND C.RANK < 7 THEN " + Vars.KEY_ES
				+ "														ELSE CASE WHEN C.RANK > 6 AND C.RANK < 9 THEN " + Vars.KEY_EJ
				+ "														ELSE CASE WHEN C.RANK > 8 AND C.RANK < 12 THEN " + Vars.KEY_NR
				+ "														ELSE CASE WHEN C.RANK > 11 AND C.RANK < 15 THEN " + Vars.KEY_ER
				+ "														ELSE CASE WHEN C.RANK > 14 AND C.RANK < 18 THEN " + Vars.KEY_OSR
				+ "														ELSE CASE WHEN C.RANK = 18 THEN " + Vars.KEY_NR
				+ "														ELSE CASE WHEN C.RANK = 19 THEN " + Vars.KEY_ER
				+ "														END END END END END END END END END) "
				+ "											FROM TRAININGS_MATRIX"
				+ "											WHERE TYPE = B.TYPE"
				+ "											AND CASE WHEN C.RANK > 0 AND C.RANK < 3 THEN " + Vars.KEY_NS
				+ "													ELSE CASE WHEN C.RANK > 2 AND C.RANK < 5 THEN " + Vars.KEY_NJ
				+ "													ELSE CASE WHEN C.RANK > 4 AND C.RANK < 7 THEN " + Vars.KEY_ES
				+ "													ELSE CASE WHEN C.RANK > 6 AND C.RANK < 9 THEN " + Vars.KEY_EJ
				+ "													ELSE CASE WHEN C.RANK > 8 AND C.RANK < 12 THEN " + Vars.KEY_NR
				+ "													ELSE CASE WHEN C.RANK > 11 AND C.RANK < 15 THEN " + Vars.KEY_ER
				+ "													ELSE CASE WHEN C.RANK > 14 AND C.RANK < 18 THEN " + Vars.KEY_OSR
				+ "													ELSE CASE WHEN C.RANK = 18 THEN " + Vars.KEY_NR
				+ "													ELSE CASE WHEN C.RANK = 19 THEN " + Vars.KEY_ER
				+ "													END END END END END END END END END = 1) AS CHATER_C"
				+ "										FROM TRAININGS_MEMBER A, TRAININGS_HISTORY B, MEMBER C"
				+ "										WHERE A."+ Vars.KEY_HISTORY_IDX +" = B."+ Vars.KEY_IDX
				+ "										AND A."+ Vars.KEY_MEMBER_IDX +" =  C."+ Vars.KEY_IDX
				+ "										AND B."+ Vars.KEY_VSL_TYPE +" = "+ vsl_type
				+ "										AND C."+ Vars.KEY_DEL_YN +" = 'N'"
				+ "								) C"
				+ "								GROUP BY "+ Vars.KEY_MEMBER_IDX +", "+ Vars.KEY_TYPE+ ", "+ Vars.KEY_TRAINING_COURSE +", CHATER_C"
				+ "						) D"
				+ "						GROUP BY "+ Vars.KEY_MEMBER_IDX +", "+ Vars.KEY_TYPE +", CHATER_C"
				+ "				) E"
				+ "				GROUP BY "+ Vars.KEY_MEMBER_IDX +", "+ Vars.KEY_TYPE +", CHATER_C, T_C"
				+ "		) F"
				+ "		GROUP BY "+ Vars.KEY_TYPE
				+ "	) G";
//		Log.e("getStatusGTRate" , selectQuery);
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

//		Log.e("getStatusGTRate" , cursor.getCount()+"");
		if (cursor != null && cursor.getCount() > 0) {
			if (cursor.moveToFirst()) {
				do {
					ArrayList<String> trainingInfo = new ArrayList<String>();
//					Log.e("getStatusGTRate" , cursor.getColumnName(0) + " : "+cursor.getString(0));
//					Log.e("getStatusGTRate" , cursor.getColumnName(1) + " : "+cursor.getDouble(1));
					trainingInfo.add(cursor.getString(0));
					trainingInfo.add(String.format("%.1f",cursor.getDouble(1)));

					trainingList.add(trainingInfo);
				} while (cursor.moveToNext());
				if(trainingList.size() < 2) {
					ArrayList<String> trainingInfo = new ArrayList<String>();
					if("G".equals(trainingList.get(0).get(0)))
						trainingInfo.add("R");
					else
						trainingInfo.add("G");
					trainingInfo.add("0.0");

					trainingList.add(trainingInfo);
				}
			}
		} else {
			ArrayList<String> trainingInfo = new ArrayList<String>();
			trainingInfo.add("G");
			trainingInfo.add("0.0");
			trainingList.add(trainingInfo);
			trainingInfo.add("R");
			trainingInfo.add("0.0");
			trainingList.add(trainingInfo);
		}

		// return training list
		return trainingList;
	}

	public ArrayList<ArrayList<String>> getStatusGTData(int gSize, int nrSize, int idx, int vsltype) {
		ArrayList<ArrayList<String>> trainingList = new ArrayList<ArrayList<String>>();
		String selectQuery = "SELECT " + Vars.KEY_TYPE
				+ " , (CAST(T_C AS float) / CAST(CHATER_C AS float)) * 100 AS RATE"	//(이수한 과목수 / 총과목수) * 100 = RATE
				//+ "	, (CAST(SCORE_C AS float) / CAST(CHATER_C AS float)) AS AVG" 					//스코어 총합 / 총과목수 = SCORE AVG
				+ "	, (CAST(SCORE_C AS float) / CAST(T_C AS float)) AS AVG" 					//이수한 과목 스코어 총합 / 이수한 과목 수 = SCORE AVG
				+ "	, (SELECT "+ Vars.KEY_DATE
				+ "		FROM TRAININGS_MEMBER A, TRAININGS_HISTORY B"
				+ "		WHERE A."+ Vars.KEY_HISTORY_IDX +" = B."+ Vars.KEY_IDX
				+ "		AND B."+ Vars.KEY_TYPE +" = D."+ Vars.KEY_TYPE
				+ "		AND A."+ Vars.KEY_MEMBER_IDX +" = " + idx
				+ "		AND B."+ Vars.KEY_VSL_TYPE +" = " + vsltype
				+ "		ORDER BY DATE DESC LIMIT 1) AS LAST_DATE"
				+ "	FROM ("
				+ "	SELECT "+ Vars.KEY_MEMBER_IDX +", "+ Vars.KEY_TYPE +", COUNT("+ Vars.KEY_TRAINING_COURSE +") AS T_C"
				+ "		, SUM("+ Vars.KEY_SCORE +") AS SCORE_C"
				+ "		, CASE WHEN "+ Vars.KEY_TYPE +" = 'G' THEN "+ gSize +" ELSE "+ nrSize +" END AS CHATER_C"
				+ "		FROM ("
				+ "		SELECT "+ Vars.KEY_MEMBER_IDX +", "+ Vars.KEY_TYPE +", "+ Vars.KEY_TRAINING_COURSE +", "+ Vars.KEY_SCORE
				+ "			FROM ("
				+ "				SELECT A."+ Vars.KEY_MEMBER_IDX +", A."+ Vars.KEY_HISTORY_IDX + ", B."+ Vars.KEY_TYPE
				+					", B."+ Vars.KEY_TRAINING_COURSE +", B."+ Vars.KEY_SCORE
				+ "					FROM TRAININGS_MEMBER A, TRAININGS_HISTORY B"
				+ "					WHERE A."+ Vars.KEY_HISTORY_IDX +" = B."+ Vars.KEY_IDX
				+ "					AND A."+ Vars.KEY_MEMBER_IDX +" = " + idx
				+ "					AND B."+ Vars.KEY_VSL_TYPE +" = " + vsltype
				+ "			)"
				+ "			GROUP BY "+ Vars.KEY_MEMBER_IDX +", "+ Vars.KEY_TYPE +", "+ Vars.KEY_TRAINING_COURSE
				+ "		) C"
				+ "		GROUP BY "+ Vars.KEY_MEMBER_IDX +", "+ Vars.KEY_TYPE
				+ "	) D"
				+ "	GROUP BY "+ Vars.KEY_MEMBER_IDX +", "+ Vars.KEY_TYPE +", T_C, CHATER_C";

//		Log.e("getStatusGTData" , "selectQuery : "+selectQuery);
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null && cursor.getCount() > 0) {
			if (cursor.moveToFirst()) {
				do {
					Log.e("getStatusGTData" , cursor.getColumnName(0) + " : "+cursor.getString(0));
					Log.e("getStatusGTData" , cursor.getColumnName(1) + " : "+cursor.getDouble(1));
					Log.e("getStatusGTData" , cursor.getColumnName(2) + " : "+cursor.getDouble(2));
					Log.e("getStatusGTData" , cursor.getColumnName(3) + " : "+cursor.getString(3));
					ArrayList<String> trainingInfo = new ArrayList<String>();
					trainingInfo.add(cursor.getString(0));
					trainingInfo.add(String.format("%.1f",cursor.getDouble(1)));
					trainingInfo.add(String.format("%.1f",cursor.getDouble(2)));
					trainingInfo.add(cursor.getString(3));

					trainingList.add(trainingInfo);
				} while (cursor.moveToNext());
				if(trainingList.size() < 2) {
					ArrayList<String> trainingInfo = new ArrayList<String>();
					if("G".equals(trainingList.get(0).get(0)))
						trainingInfo.add("R");
					else
						trainingInfo.add("G");
					trainingInfo.add("0.0");
					trainingInfo.add("0.0");
					trainingInfo.add("-");

					trainingList.add(trainingInfo);
				}

			}
		} else {
			ArrayList<String> trainingInfo = new ArrayList<String>();
			trainingInfo.add("G");
			trainingInfo.add("0.0");
			trainingInfo.add("0.0");
			trainingInfo.add("-");

			trainingList.add(trainingInfo);

			trainingInfo = new ArrayList<String>();
			trainingInfo.add("R");
			trainingInfo.add("0.0");
			trainingInfo.add("0.0");
			trainingInfo.add("-");

			trainingList.add(trainingInfo);
		}

		// return training list
		return trainingList;
	}

	public ArrayList<TrainingsInfo> getStatusHistory2(List<String> count, ArrayList<String> subject, String type, int vsltype, int idx) {
		ArrayList<TrainingsInfo> trainingsList = new ArrayList<TrainingsInfo>();
		String selectQuery = "SELECT * FROM (";
		for(int i=0; i<subject.size(); i++){
			selectQuery += "SELECT '"+ count.get(i) +"' AS "+ Vars.KEY_IDX +", '"+ subject.get(i).replace("'","''") + "' AS "+ Vars.KEY_TRAINING_COURSE;

			if(subject.size()-1 != i)
				selectQuery += " UNION ALL ";
			else
				selectQuery += " ) AA ";
		}

		selectQuery += " LEFT OUTER JOIN"
				+ "	(SELECT B."+ Vars.KEY_DATE +", B."+ Vars.KEY_TRAINING_COURSE
				+ "			FROM TRAININGS_MEMBER A, TRAININGS_HISTORY B"
				+ "			WHERE A."+ Vars.KEY_HISTORY_IDX +" = B."+ Vars.KEY_IDX
				+ "			AND A."+ Vars.KEY_MEMBER_IDX +" = " + idx
				+ "			AND B."+ Vars.KEY_VSL_TYPE +" = " + vsltype
				+ "			AND B."+ Vars.KEY_TYPE +" = '"+ type +"'"
				+ "			) BB ON AA."+ Vars.KEY_IDX +" = BB."+ Vars.KEY_TRAINING_COURSE;
//		Log.e("getStatusHistory2" , "selectQuery : "+selectQuery);
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
//				Log.e("getStatusHistory" , cursor.getColumnName(0) + " : "+cursor.getInt(0));
//				Log.e("getStatusHistory" , cursor.getColumnName(0) + " : "+cursor.getString(0));
//				Log.e("getStatusHistory" , cursor.getColumnName(1) + " : "+cursor.getString(1));
//				Log.e("getStatusHistory" , cursor.getColumnName(2) + " : "+cursor.getString(2));
//				Log.e("getStatusHistory" , cursor.getColumnName(3) + " : "+cursor.getInt(3));
				TrainingsInfo trainings = new TrainingsInfo();
				trainings.setTraining_course(cursor.getInt(0));
				trainings.setTitle(cursor.getString(1));
				trainings.setDate(cursor.getString(2));
				trainingsList.add(trainings);
			} while (cursor.moveToNext());
		}
		// return training list
		return trainingsList;
	}

	public ArrayList<TrainingsInfo> getStatusHistory(List<TrainingsInfo> tsilist, String type, int vsltype, int idx) {
		ArrayList<TrainingsInfo> trainingsList = new ArrayList<TrainingsInfo>();
		String gidx = "";
		String selectQuery = "SELECT * FROM (";
		for(int i=0; i<tsilist.size(); i++){
			if("G".equals(type)) {
				if(tsilist.get(i).getTraining_course2() > 0)
					gidx = tsilist.get(i).getTraining_course() + "-" + tsilist.get(i).getTraining_course2();
				else
					gidx = String.valueOf(tsilist.get(i).getTraining_course());
			} else
				gidx = String.valueOf(tsilist.get(i).getTraining_course() - 1);

			selectQuery += "SELECT '"+ gidx +"' AS "+ Vars.KEY_IDX +", '"+ tsilist.get(i).getTitle().replace("'","''") + "' AS "+ Vars.KEY_TRAINING_COURSE;

			if(tsilist.size()-1 != i)
				selectQuery += " UNION ALL ";
			else
				selectQuery += " ) AA ";
		}

		selectQuery += " LEFT OUTER JOIN"
					+ "	(SELECT B."+ Vars.KEY_DATE +", B."+ Vars.KEY_TRAINING_COURSE
					+ "			FROM TRAININGS_MEMBER A, TRAININGS_HISTORY B"
					+ "			WHERE A."+ Vars.KEY_HISTORY_IDX +" = B."+ Vars.KEY_IDX
					+ "			AND A."+ Vars.KEY_MEMBER_IDX +" = " + idx
					+ "			AND B."+ Vars.KEY_VSL_TYPE +" = " + vsltype
					+ "			AND B."+ Vars.KEY_TYPE +" = '"+ type +"'"
					+ "			) BB ON AA."+ Vars.KEY_IDX +" = BB."+ Vars.KEY_TRAINING_COURSE;

		/*String selectQuery = "SELECT B."+ Vars.KEY_DATE +", B."+ Vars.KEY_TRAINING_COURSE
							+ "	FROM TRAININGS_MEMBER A, TRAININGS_HISTORY B"
							+ "	WHERE A."+ Vars.KEY_HISTORY_IDX +" = B."+ Vars.KEY_IDX
							+ "	AND A."+ Vars.KEY_MEMBER_IDX +" = " + idx
							+ "	AND B."+ Vars.KEY_VSL_TYPE +" = " + vsltype
							+ "	AND B."+ Vars.KEY_TYPE +" = '"+ type +"'";*/
		Log.e("getStatusHistory" , "selectQuery : "+selectQuery);
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
//				Log.e("getStatusHistory" , cursor.getColumnName(0) + " : "+cursor.getInt(0));
				Log.e("getStatusHistory" , cursor.getColumnName(0) + " : "+cursor.getString(0));
				Log.e("getStatusHistory" , cursor.getColumnName(1) + " : "+cursor.getString(1));
				Log.e("getStatusHistory" , cursor.getColumnName(2) + " : "+cursor.getString(2));
				Log.e("getStatusHistory" , cursor.getColumnName(3) + " : "+cursor.getInt(3));
				TrainingsInfo trainings = new TrainingsInfo();
				trainings.setTraining_course(cursor.getInt(0));
				trainings.setTitle(cursor.getString(1));
				trainings.setDate(cursor.getString(2));
				trainingsList.add(trainings);
			} while (cursor.moveToNext());
		}
		// return training list
		return trainingsList;
	}

	public List<TrainingsInfo> getHistoryMember(int idx) {
//		Log.e("getHistoryMember" , "idx : "+ idx);
		List<TrainingsInfo> trainingsList = new ArrayList<>();
		// Select All Query
		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT A.IDX, A.HISTORY_IDX, B.MASTER_IDX, B.RANK, B.FNAME, B.SNAME FROM "
				+ Vars.TABLE_TRAININGS_MEMBER + " A," + Vars.TABLE_MEMBER + " B"
				+ " WHERE A." + Vars.KEY_MEMBER_IDX + " = B." + Vars.KEY_IDX
				+ " AND A.HISTORY_IDX = " + idx;

		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
//				Log.e("getHistoryMember" , cursor.getColumnName(0) + " : "+cursor.getInt(0));
//				Log.e("getHistoryMember" , cursor.getColumnName(1) + " : "+cursor.getInt(1));
//				Log.e("getHistoryMember" , cursor.getColumnName(2) + " : "+cursor.getInt(2));
//				Log.e("getHistoryMember" , cursor.getColumnName(3) + " : "+cursor.getString(3));
//				Log.e("getHistoryMember" , cursor.getColumnName(4) + " : "+cursor.getString(4));
//				Log.e("getHistoryMember" , cursor.getColumnName(5) + " : "+cursor.getString(5));
//				Log.e("getHistoryMember" , "//////////////////////////////////////");
				TrainingsInfo trainings = new TrainingsInfo();
				trainings.setIdx(cursor.getInt(0));
				trainings.setHistroy_idx(cursor.getInt(1));
				trainings.setMaster_idx(cursor.getInt(2));
				trainings.setRank(cursor.getInt(3));
				trainings.setFirstName(cursor.getString(4));
				trainings.setSurName(cursor.getString(5));
				// Adding problem to list
				trainingsList.add(trainings);
			} while (cursor.moveToNext());
		}
		// return training
		return trainingsList;
	}

	public int test() {
		Si = new SelectItems();
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(Vars.KEY_SUBMIT, "N");

		// updating row
		return db.update(Vars.TABLE_TRAININGS_HISTORY, values, Vars.KEY_SUBMIT + " = ? AND "+ Vars.KEY_DATE + " IS NOT NULL",
				new String[] { "Y"});
	}

	public int updateData() {
		Si = new SelectItems();
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(Vars.KEY_SUBMIT, "Y");

		// updating row
		return db.update(Vars.TABLE_TRAININGS_HISTORY, values, Vars.KEY_SUBMIT + " = ? AND "+ Vars.KEY_DATE + " IS NOT NULL",
				new String[] { "N"});
	}

	public ArrayList<TrainingsInfo> getDownloadData(int vsl_type) {

		String selectQuery = "SELECT * FROM " + Vars.TABLE_TRAININGS_HISTORY
				+ " WHERE VSL_TYPE = " + vsl_type
				+ " AND SUBMIT = 'N'"
				+ " AND DATE IS NOT NULL"
				+ " ORDER BY DATE DESC";

		ArrayList<TrainingsInfo> trainingsList = new ArrayList<TrainingsInfo>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {

				String tc[] = cursor.getString(4).split("-");
//				Log.e("getDownloadData" , "tc[] : "+tc.length);

				TrainingsInfo trainings = new TrainingsInfo();
				trainings.setIdx(cursor.getInt(0));
				trainings.setType(cursor.getString(1));
				trainings.setVsl(cursor.getString(2));
				trainings.setVsl_type(cursor.getInt(3));

				if(tc.length == 1)
					trainings.setTraining_course(cursor.getInt(4));
				else {
					trainings.setTraining_course(Integer.valueOf(tc[0]));
					trainings.setTraining_course2(Integer.valueOf(tc[1]));
				}

				trainings.setDate(cursor.getString(5));
				trainings.setScore(cursor.getInt(6));

				// Adding training to list
				trainingsList.add(trainings);
			} while (cursor.moveToNext());
		}

		// return training list
		return trainingsList;
	}

	public ArrayList<TrainingsInfo> getAllDownloadData(int vsl_type) {

		String selectQuery = "SELECT * FROM " + Vars.TABLE_TRAININGS_HISTORY
				+ " WHERE VSL_TYPE = " + vsl_type
				+ " AND SUBMIT = 'Y'"
				+ " AND DATE IS NOT NULL"
				+ " ORDER BY DATE DESC";

		ArrayList<TrainingsInfo> trainingsList = new ArrayList<TrainingsInfo>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {

				String tc[] = cursor.getString(4).split("-");
//				Log.e("getDownloadData" , "tc[] : "+tc.length);

				TrainingsInfo trainings = new TrainingsInfo();
				trainings.setIdx(cursor.getInt(0));
				trainings.setType(cursor.getString(1));
				trainings.setVsl(cursor.getString(2));
				trainings.setVsl_type(cursor.getInt(3));

				if(tc.length == 1)
					trainings.setTraining_course(cursor.getInt(4));
				else {
					trainings.setTraining_course(Integer.valueOf(tc[0]));
					trainings.setTraining_course2(Integer.valueOf(tc[1]));
				}

				trainings.setDate(cursor.getString(5));
				trainings.setScore(cursor.getInt(6));

				// Adding training to list
				trainingsList.add(trainings);
			} while (cursor.moveToNext());
		}

		// return training list
		return trainingsList;
	}
}