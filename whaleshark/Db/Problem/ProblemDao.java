package com.togetherseatech.whaleshark.Db.Problem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.togetherseatech.whaleshark.Db.DBContactHelper;
import com.togetherseatech.whaleshark.Vars;
import com.togetherseatech.whaleshark.util.LogWrapper;
import com.togetherseatech.whaleshark.util.SelectItems;

import java.sql.Wrapper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProblemDao extends DBContactHelper {

	public ProblemDao(Context context) {
		super(context);
	}

	// 새로운 DBContactInfo 함수 추가
	public void addProblems(ArrayList<ProblemInfo> arr) {
		SQLiteDatabase db = this.getWritableDatabase();

		String sql = "INSERT INTO "+ Vars.TABLE_PROBLEM +" VALUES (?,?,?,?,?,?,?,?,?,?,?,?);";
		Log.e("test","sql : " + sql);
//		Log.e("addProblems", "sql = "+sql);
		SQLiteStatement statement = db.compileStatement(sql);
		db.beginTransaction();
//		StringBuilder str = new StringBuilder();
		for (int i = 0; i < arr.size(); i++) {
			Log.e("test","arr.getLevel : " + arr.get(i).getLevel());
//			Log.e("addProblems", arr.get(i).getChapter()+"_"+arr.get(i).getLevel()+"_"+arr.get(i).getNo()+"_"+arr.get(i).getVsl_type());
//			Log.e("addProblems", i+" = "+arr.get(i).getIdx());
//			Log.e("addProblems", i+" = "+arr.get(i).getChapter());
//			Log.e("addProblems", i+" = "+arr.get(i).getLevel());
//			Log.e("addProblems", i+" = "+arr.get(i).getNo());
//			Log.e("addProblems", i+" = "+arr.get(i).getVsl_type());
//			Log.e("addProblems", i+" = "+arr.get(i).getTitle_kr());
//			Log.e("addProblems", i+" = "+arr.get(i).getTitle_eng());
//			Log.e("addProblems", i+" = "+arr.get(i).getAnswer());
//			Log.e("addProblems", i+" = "+arr.get(i).getRelative_regulation());
//			Log.e("addProblems", i+" = "+arr.get(i).getVoice_kr());
//			Log.e("addProblems", i+" = "+arr.get(i).getVoice_eng());
//			Log.e("addProblems", i+" = "+arr.get(i).getFlash_video());
			/*str.append(arr.get(i).getChapter()+"_"+arr.get(i).getLevel()+"_"+arr.get(i).getNo()+"_"+arr.get(i).getVsl_type());
			str.append("\r\n" + arr.get(i).getTitle_kr());
			str.append("\r\n" + arr.get(i).getTitle_eng());
			str.append("\r\n" + arr.get(i).getAnswer());
			str.append("\r\n" + arr.get(i).getRelative_regulation());
			str.append("\r\n" + arr.get(i).getVoice_kr());
			str.append("\r\n" + arr.get(i).getVoice_eng());
			str.append("\r\n" + arr.get(i).getFlash_video());
			str.append("\r\n");*/

//			Log.e("addProblems", i+" = "+arr.get(i).getExplanation_kr());
//			Log.e("addProblems", i+" = "+arr.get(i).getExplanation_eng());
			statement.clearBindings();
			statement.bindNull(1); //arr.get(i).getIdx()
			statement.bindLong(2, arr.get(i).getChapter());
			statement.bindLong(3, arr.get(i).getLevel());
			statement.bindLong(4, arr.get(i).getNo());
			statement.bindLong(5, arr.get(i).getVsl_type());
			statement.bindString(6, arr.get(i).getTitle_kr());
			statement.bindString(7, arr.get(i).getTitle_eng());
			statement.bindString(8, arr.get(i).getAnswer());
			statement.bindString(9, arr.get(i).getRelative_regulation() == null ? "" : arr.get(i).getRelative_regulation());
			statement.bindString(10, arr.get(i).getVoice_kr());
			statement.bindString(11, arr.get(i).getVoice_eng());
			statement.bindString(12, arr.get(i).getFlash_video() == null ? "" : arr.get(i).getFlash_video());
//			statement.bindString(10, arr.get(i).getExplanation_kr());
//			statement.bindString(11, arr.get(i).getExplanation_eng());



			statement.execute();
		}
		db.setTransactionSuccessful();
		db.endTransaction();
//		LogWrapper.appendLog(str.toString());
	}

	public void addSubProblems(ArrayList<ProblemInfo> arr) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "INSERT INTO "+ Vars.TABLE_SUB_PROBLEM +" VALUES (?,?,?,?,?,?,?);";
		SQLiteStatement statement = db.compileStatement(sql);
		db.beginTransaction();
//		StringBuilder str = new StringBuilder();
		for (int i = 0; i < arr.size(); i++) {
//			Log.e("addSubProblems", arr.get(i).getChapter()+"_"+arr.get(i).getLevel()+"_"+arr.get(i).getNo());
//			Log.e("addSubProblems", i+" = "+arr.get(i).getContent_kr());
//			Log.e("addSubProblems", i+" = "+arr.get(i).getContent_eng());
			/*str.append(arr.get(i).getChapter()+"_"+arr.get(i).getLevel()+"_"+arr.get(i).getNo());
			str.append("\r\n" + arr.get(i).getContent_kr());
			str.append("\r\n" + arr.get(i).getContent_eng());
			str.append("\r\n");*/
			statement.clearBindings();
			statement.bindNull(1);//arr.get(i).getIdx()
			statement.bindLong(2, arr.get(i).getChapter());
			statement.bindLong(3, arr.get(i).getLevel());
			statement.bindLong(4, arr.get(i).getNo());
			statement.bindString(5, arr.get(i).getProblem_answer() == null ? "" : arr.get(i).getProblem_answer());
			statement.bindString(6, arr.get(i).getContent_kr() == null ? "" : arr.get(i).getContent_kr());
			statement.bindString(7, arr.get(i).getContent_eng() == null ? "" : arr.get(i).getContent_eng());


			statement.execute();
		}
		db.setTransactionSuccessful();
		db.endTransaction();
//		LogWrapper.appendLog(str.toString());
	}

	public List<ProblemInfo> getSubProblem(ProblemInfo pi) {
		List<ProblemInfo> problemList = new ArrayList<ProblemInfo>();
		// Select All Query

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(Vars.TABLE_SUB_PROBLEM, null,  Vars.KEY_CHAPTER + " = ? AND "+Vars.KEY_LEVEL + " = ? AND "+ Vars.KEY_NO + " = ?",
				new String[] { String.valueOf(pi.getChapter()), String.valueOf(pi.getLevel()), String.valueOf(pi.getNo()) }, null, null, null, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				ProblemInfo problem = new ProblemInfo();
				problem.setIdx(cursor.getInt(0));
				problem.setChapter(cursor.getInt(1));
				problem.setLevel(cursor.getInt(2));
				problem.setNo(cursor.getInt(3));
				problem.setProblem_answer(cursor.getString(4));
				problem.setContent_kr(cursor.getString(5).replaceAll("\n","\r\n"));
				problem.setContent_eng(cursor.getString(6).replaceAll("\n","\r\n"));
				// Adding problem to list
				problemList.add(problem);
			} while (cursor.moveToNext());
		}

		// return problem list
		return problemList;
	}

	// 모든 Problem 정보 가져오기
	public List<ProblemInfo> getAllProblem() {
		List<ProblemInfo> problemList = new ArrayList<ProblemInfo>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + Vars.TABLE_PROBLEM;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				ProblemInfo problem = new ProblemInfo();
				problem.setIdx((int)cursor.getLong(0));
				problem.setChapter(cursor.getInt(1));
				problem.setLevel(cursor.getInt(2));
				problem.setNo(cursor.getInt(3));
				problem.setTitle_kr(cursor.getString(4));
				problem.setTitle_eng(cursor.getString(5));
				problem.setAnswer(cursor.getString(6));
				problem.setRelative_regulation(cursor.getString(7));
				problem.setVoice_kr(cursor.getString(8));
				problem.setVoice_eng(cursor.getString(9));
				problem.setFlash_video(cursor.getString(10));

//				problem.setExplanation_kr(cursor.getString(9));
//				problem.setExplanation_eng(cursor.getString(10));

				// Adding problem to list
				problemList.add(problem);
			} while (cursor.moveToNext());
		}

		// return problem list
		return problemList;
	}

	public ArrayList<Map<String, Object>> getAllProblemXml() {
		ArrayList<Map<String, Object>> problemList = new ArrayList<Map<String, Object>>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + Vars.TABLE_PROBLEM;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		SelectItems si = new SelectItems();
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Map<String, Object> map = new HashMap<>();
				map.put("MENU","Question");
				map.put("FILE_TITLE_KR",si.getChapter(cursor.getInt(1),"KR"));
				map.put("FILE_TITLE_ENG",si.getChapter(cursor.getInt(1),"ENG"));
				map.put("FILE_NAME",cursor.getString(9));
				map.put("FILE_SIZE","204MB");
				problemList.add(map);

				map = new HashMap<>();
				map.put("MENU","Question");
				map.put("FILE_TITLE_KR",si.getChapter(cursor.getInt(1),"KR"));
				map.put("FILE_TITLE_ENG",si.getChapter(cursor.getInt(1),"ENG"));
				map.put("FILE_NAME",cursor.getString(10));
				map.put("FILE_SIZE","204MB");

				problemList.add(map);

				if(!cursor.getString(11).equals("")) {
					map = new HashMap<>();
					map.put("MENU", "Question");
					map.put("FILE_TITLE_KR", si.getChapter(cursor.getInt(1), "KR"));
					map.put("FILE_TITLE_ENG", si.getChapter(cursor.getInt(1), "ENG"));
					map.put("FILE_NAME", cursor.getString(11));
					map.put("FILE_SIZE", "204MB");
					problemList.add(map);
				}

			} while (cursor.moveToNext());
		}

		// return problem list
		return problemList;
	}
	public List<ProblemInfo> getAllSubProblem() {
		List<ProblemInfo> problemList = new ArrayList<ProblemInfo>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + Vars.TABLE_SUB_PROBLEM;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				ProblemInfo problem = new ProblemInfo();
				problem.setIdx((int)cursor.getLong(0));
				problem.setChapter(cursor.getInt(1));
				problem.setLevel(cursor.getInt(2));
				problem.setNo(cursor.getInt(3));
				problem.setProblem_answer(cursor.getString(4));
				problem.setContent_kr(cursor.getString(5));
				problem.setContent_eng(cursor.getString(6));
				// Adding problem to list
				problemList.add(problem);
			} while (cursor.moveToNext());
		}

		// return problem list
		return problemList;
	}

	public ArrayList<ProblemInfo> getRankProblem(int rank, int chapter, String vsl_type){
//		Log.e("test","rank : " + rank);
//		Log.e("test","chapter : " + chapter);
//		Log.e("test","vsl_type : " + vsl_type);
//	public ArrayList<ProblemInfo> getRankProblem(int rank, int chapter){
//        Log.e("getChapter", "rank : "+ rank);
        int Low = 0, Middle = 0, High = 0, fix = 0;
        ArrayList<ProblemInfo> problemList = new ArrayList<>();
		SQLiteDatabase db = this.getReadableDatabase();


		switch(rank){
			case 1:     //선장
			case 2:     //일항사
				Log.e("test","선장 , 일항사");
				Low = 3;
				Middle = 6;
				High = 6;
				fix = 0;
				break;
			case 3:     //이항사
			case 4:     //삼항사
			case 7:     //이기사
			case 8:     //삼기사
				Log.e("test","이항사 , 삼항사 , 이기사 , 삼기사");
				Low = 4;
				Middle = 7;
				High = 4;
				fix = 0;
				break;
			case 5:     //기관장
			case 6:     //일기사
				Low = 3;
				Middle = 6;
				High = 6;
				fix = 0;
				break;
			case 9:     //갑판장
			case 10:    //갑판수
			case 11:    //갑판원
			case 12:    //조기장
			case 13:    //조기수
			case 14:    //조기원
			case 15:    //조리장
			case 16:    //조리수
			case 17:    //조리원
			case 18:    //실항사
			case 19:    //실기사
				Low = 8;
				Middle = 5;
				High = 2;
				fix = 0;
				break;
		}

		String selectQuery = "SELECT * FROM ("
						+ "		SELECT * FROM ("
						+ " 		SELECT IDX, CHAPTER, LEVEL, NO, TITLE_KR, TITLE_ENG, ANSWER, RELATIVE_REGULATION, VOICE_KR, VOICE_ENG, FLASH_VIDEO"
						+ "				FROM PROBLEM"
						+ "				WHERE 1=1";
		selectQuery += "				AND LEVEL = 0";
		selectQuery += "				AND CHAPTER = " + chapter;
		selectQuery += "				AND VSL_TYPE IN (" + vsl_type + ")";
		selectQuery	+= "				ORDER BY RANDOM() LIMIT "+ Low;
		selectQuery	+= "		)";
		selectQuery	+= "		UNION ALL";
		selectQuery	+= "		SELECT * FROM (";
		selectQuery	+= "			SELECT IDX, CHAPTER, LEVEL, NO, TITLE_KR, TITLE_ENG, ANSWER, RELATIVE_REGULATION, VOICE_KR, VOICE_ENG, FLASH_VIDEO";
		selectQuery	+= "				FROM PROBLEM";
		selectQuery	+= "				WHERE 1=1";
		selectQuery	+= "				AND LEVEL = 1";
		selectQuery += "				AND CHAPTER = " + chapter;
		selectQuery += "				AND VSL_TYPE IN (" + vsl_type + ")";
		selectQuery	+= "				ORDER BY RANDOM() LIMIT "+ Middle;
		selectQuery	+= "		)";
		selectQuery	+= "		UNION ALL";
		selectQuery	+= "		SELECT * FROM (";
		selectQuery	+= "			SELECT IDX, CHAPTER, LEVEL, NO, TITLE_KR, TITLE_ENG, ANSWER, RELATIVE_REGULATION, VOICE_KR, VOICE_ENG, FLASH_VIDEO";
		selectQuery	+= "				FROM PROBLEM";
		selectQuery	+= "				WHERE 1=1";
		selectQuery	+= "				AND LEVEL = 2";
		selectQuery += "				AND CHAPTER = " + chapter;
		selectQuery += "				AND VSL_TYPE IN (" + vsl_type + ")";
		selectQuery	+= "				ORDER BY RANDOM() LIMIT "+ High;
		selectQuery	+= "		)";
		selectQuery	+= "		UNION ALL";
		selectQuery	+= "		SELECT * FROM (";
		selectQuery	+= "			SELECT IDX, CHAPTER, LEVEL, NO, TITLE_KR, TITLE_ENG, ANSWER, RELATIVE_REGULATION, VOICE_KR, VOICE_ENG, FLASH_VIDEO";
		selectQuery	+= "				FROM PROBLEM";
		selectQuery	+= "				WHERE 1=1";
		selectQuery	+= "				AND LEVEL = 3";
		selectQuery += "				AND CHAPTER = " + chapter;
		selectQuery += "				AND VSL_TYPE IN (" + vsl_type + ")";
		selectQuery	+= "				ORDER BY RANDOM() LIMIT "+ fix;
		selectQuery	+= "		)";
		selectQuery	+= "	) A";
		selectQuery	+= "	ORDER BY A.LEVEL, A.NO";
		Log.e("test","selectQuery ::: " + selectQuery);
//		Log.e("getRankProblem", "selectQuery : "+selectQuery);
		Cursor cursor = db.rawQuery(selectQuery, null);

//		Log.e("getRankProblem", "cursor : "+cursor.getCount());

		if (cursor.moveToFirst()) {
			do {
//				Log.e("getRankProblem", cursor.getColumnName(0) + " : " + cursor.getInt(0)+"");
//				Log.e("getRankProblem", cursor.getColumnName(1) + " : " + cursor.getInt(1)+"");
//				Log.e("getRankProblem", cursor.getColumnName(2) + " : " + cursor.getInt(2)+"");
//				Log.e("getRankProblem", cursor.getColumnName(3) + " : " + cursor.getInt(3)+"");
//				Log.e("getRankProblem", cursor.getColumnName(4) + " : " + cursor.getString(4));
//				Log.e("getRankProblem", cursor.getColumnName(5) + " : " + cursor.getString(5));
//				Log.e("getRankProblem", cursor.getColumnName(6) + " : " + cursor.getString(6));
//				Log.e("getRankProblem", cursor.getColumnName(7) + " : " + cursor.getString(7));
//				Log.e("getRankProblem", cursor.getColumnName(8) + " : " + cursor.getString(8));
//				Log.e("getRankProblem", cursor.getColumnName(9) + " : " + cursor.getString(9));
//				Log.e("getRankProblem", cursor.getColumnName(10) + " : " + cursor.getString(10));
//				Log.e("getRankProblem", "///////////////////////");

				ProblemInfo problem = new ProblemInfo();
				problem.setIdx(cursor.getInt(0));
				problem.setChapter(cursor.getInt(1));
				problem.setLevel(cursor.getInt(2));
				problem.setNo(cursor.getInt(3));
				problem.setTitle_kr(cursor.getString(4));
				problem.setTitle_eng(cursor.getString(5));
				problem.setAnswer(cursor.getString(6));
				problem.setRelative_regulation(cursor.getString(7));
				problem.setVoice_kr(cursor.getString(8));
				problem.setVoice_eng(cursor.getString(9));
				problem.setFlash_video(cursor.getString(10));

				// Adding problem to list
				problemList.add(problem);

			Log.e("test","TABLE : " + cursor.getString(0));
			} while (cursor.moveToNext());
		}

        return problemList;
    }

	//Problem 정보 업데이트
	public int updateProblem(ProblemInfo problem) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(Vars.KEY_CHAPTER, problem.getChapter());
		values.put(Vars.KEY_LEVEL, problem.getLevel());
		values.put(Vars.KEY_NO, problem.getNo());
		values.put(Vars.KEY_TITLE_KR, problem.getTitle_kr());
		values.put(Vars.KEY_TITLE_ENG, problem.getTitle_eng());
		values.put(Vars.KEY_ANSWER, problem.getAnswer());
		values.put(Vars.KEY_RELATIVE_REGULATION, problem.getRelative_regulation());
		values.put(Vars.KEY_VOICE_KR, problem.getVoice_kr());
		values.put(Vars.KEY_VOICE_ENG, problem.getVoice_eng());
		values.put(Vars.KEY_FLASH_VIDEO, problem.getFlash_video());
//		values.put(Vars.KEY_EXPLANATION_KR, problem.getExplanation_kr());
//		values.put(Vars.KEY_EXPLANATION_ENG, problem.getExplanation_eng());

		// updating row
		return db.update(Vars.TABLE_PROBLEM, values, Vars.KEY_IDX + " = ?",
				new String[] { String.valueOf(problem.getIdx()) });
	}

	public int updateSubProblem(ProblemInfo problem) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(Vars.KEY_CHAPTER, problem.getChapter());
		values.put(Vars.KEY_LEVEL, problem.getLevel());
		values.put(Vars.KEY_NO, problem.getNo());
		values.put(Vars.KEY_PROBLEM_ANSWER, problem.getProblem_answer());
		values.put(Vars.KEY_CONTENT_KR, problem.getContent_kr());
		values.put(Vars.KEY_CONTENT_ENG, problem.getContent_eng());

		// updating row
		return db.update(Vars.TABLE_SUB_PROBLEM, values, Vars.KEY_IDX + " = ?",
				new String[] { String.valueOf(problem.getIdx()) });
	}

	// Problem 정보 숫자
	/*public int getProblemCount() {
		String countQuery = "SELECT  * FROM " + Vars.TABLE_PROBLEM;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		//cursor.close();

		// return count
		return cursor.getCount();
	}*/

	public int getProblemCount() {
		String countQuery = "SELECT COUNT(IDX) AS CNT FROM " + Vars.TABLE_PROBLEM;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		//cursor.close();
		if (cursor != null)
			cursor.moveToFirst();
		// return count
		return cursor.getInt(0);
	}

	public int getSubProblemCount() {
		String countQuery = "SELECT COUNT(IDX) AS CNT FROM " + Vars.TABLE_SUB_PROBLEM;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		//cursor.close();
		if (cursor != null)
			cursor.moveToFirst();
		// return count
		return cursor.getInt(0);
	}

	public void deleteAllProblem() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(Vars.TABLE_PROBLEM, null,null);
		db.delete(Vars.TABLE_SUB_PROBLEM, null,null);
		db.close();
	}
}