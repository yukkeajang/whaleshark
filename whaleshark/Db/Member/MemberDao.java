package com.togetherseatech.whaleshark.Db.Member;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.togetherseatech.whaleshark.Db.DBContactHelper;
import com.togetherseatech.whaleshark.Db.Problem.ProblemInfo;
import com.togetherseatech.whaleshark.Db.Training.TrainingsInfo;
import com.togetherseatech.whaleshark.Vars;
import com.togetherseatech.whaleshark.util.LogWrapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MemberDao extends DBContactHelper {

	public MemberDao(Context context) {
		super(context);
	}


	// VslMembers 정보 숫자
	public int getKeyLicenseCount() {
		String countQuery = "SELECT COUNT(IDX) AS CNT FROM " + Vars.TABLE_KEY_LICENSE;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		//cursor.close();
		if (cursor != null)
			cursor.moveToFirst();
		// return count
		return cursor.getInt(0);
	}
	public void addKeyLicense(ArrayList<MemberInfo> arr) {
		SQLiteDatabase db = this.getWritableDatabase();

		String sql = "INSERT INTO "+ Vars.TABLE_KEY_LICENSE +" VALUES (?,?,?,?);";
//		Log.e("addKeyLicense", "sql = "+sql);
		SQLiteStatement statement = db.compileStatement(sql);
		db.beginTransaction();
		for (int i = 0; i < arr.size(); i++) {
//			Log.e("addKeyLicense", i+" = "+arr.get(i).getIdx());
//			Log.e("addKeyLicense", i+" = "+arr.get(i).getStart_date());
//			Log.e("addKeyLicense", i+" = "+arr.get(i).getEnd_date());
//			Log.e("addKeyLicense", i+" = "+arr.get(i).getKey());

			statement.clearBindings();
			statement.bindNull(1);
			statement.bindNull(2);
			statement.bindNull(3);
//			statement.bindString(2, arr.get(i).getStart_date());
//			statement.bindString(3, arr.get(i).getEnd_date());
			statement.bindString(4, arr.get(i).getKey());

			statement.execute();
		}
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	public void getKeyLicense() {
		String countQuery = "SELECT * FROM " + Vars.TABLE_KEY_LICENSE;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);

		if (cursor.moveToFirst()) {
			do {
				if(cursor.getString(1) != null) {
					LogWrapper.appendLog(cursor.getColumnName(0)+"/"+ cursor.getString(0));
					LogWrapper.appendLog(cursor.getColumnName(1)+"/"+ cursor.getString(1));
					LogWrapper.appendLog(cursor.getColumnName(2)+"/"+ cursor.getString(2));
					LogWrapper.appendLog(cursor.getColumnName(3)+"/"+ cursor.getString(3));
				}
//				Log.e("getKeyLicense", cursor.getColumnName(0) + " : " + cursor.getInt(0));
//				Log.e("getKeyLicense", cursor.getColumnName(1) + " : " + cursor.getString(1));
//				Log.e("getKeyLicense", cursor.getColumnName(2) + " : " + cursor.getString(2));
//				Log.e("getKeyLicense", cursor.getColumnName(3) + " : " + cursor.getString(3));
			} while (cursor.moveToNext());
		}
		cursor.close();
		// return count
	}

	public int updateKeyLicense(MemberInfo mi) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
//		Log.e("updateKeyLicense", mi.getStart_date());
//		Log.e("updateKeyLicense", mi.getEnd_date());
//		if("IWB4M3N986GOPWY5".equals(mi.getKey())) {
//			values.put(Vars.KEY_START, "2018-12-18");
//			values.put(Vars.KEY_END, "2019-06-18");
//		}else{
			values.put(Vars.KEY_START, mi.getStart_date());
			values.put(Vars.KEY_END, mi.getEnd_date());
//		}



		// updating row
		return db.update(Vars.TABLE_KEY_LICENSE, values, Vars.KEY_KEY + " = ?",
				new String[] { String.valueOf(mi.getKey()) });
	}

	public int getKeyLicense(String key) {
		Log.e("getKeyLicense" , "key : "+ key);
		getKeyLicense();

//		String countQuery = "SELECT  COUNT("+Vars.KEY_KEY+") AS CNT FROM " + Vars.TABLE_KEY_LICENSE + " WHERE "+ Vars.KEY_KEY+ " = '" + key + "' AND "+ Vars.KEY_END + " IS NULL";
		String countQuery = "SELECT "+ Vars.KEY_IDX + " FROM " + Vars.TABLE_KEY_LICENSE + " WHERE "+ Vars.KEY_KEY+ " = '" + key + "' AND "+ Vars.KEY_END + " IS NULL";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		if (cursor != null)
			cursor.moveToFirst();

		Log.e("getKeyLicense" , "cursor.getCount() : "+cursor.getCount());
		if(cursor.getCount() == 0)
			return 0;

//		Log.e("getKeyLicense" , cursor.getColumnName(0) + " : "+cursor.getInt(0));

		//cursor.close();
		// return count
		return cursor.getInt(0);
	}

	public MemberInfo getcloseLicense(String date) {
		Log.e("test","1번 close date :" + date);
		SQLiteDatabase db = this.getReadableDatabase();
		MemberInfo member = new MemberInfo();
		Cursor cursor = db.query(Vars.TABLE_KEY_LICENSE, null,  Vars.KEY_START + " <= ? AND " + Vars.KEY_END + " >= ? ",
				new String[] { date, date }, null, null, null, null);

		if(cursor.getCount() > 0){

			if (cursor != null)
				cursor.moveToFirst();

//			Log.e("getcloseLicense" , cursor.getColumnName(0) + " : "+cursor.getInt(0));
//			Log.e("getcloseLicense" , cursor.getColumnName(1) + " : "+cursor.getString(1));
//			Log.e("getcloseLicense" , cursor.getColumnName(2) + " : "+cursor.getString(2));
//			Log.e("getcloseLicense" , cursor.getColumnName(3) + " : "+cursor.getString(3));

			member.setIdx(cursor.getInt(0));
			member.setStart_date(cursor.getString(1));
			member.setEnd_date(cursor.getString(2));
			member.setKey(cursor.getString(3));
		}
		// return member
		return member;
	}

	public String getLicenseSDate(int idx) {
		Log.e("test","======= idx =====" + idx);
		SQLiteDatabase db = this.getReadableDatabase();
		MemberInfo member = new MemberInfo();
		Cursor cursor = db.query(Vars.TABLE_KEY_LICENSE, new String[] {Vars.KEY_END},  Vars.KEY_IDX + " = ?",
				new String[] { String.valueOf(idx) }, null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

//		Log.e("getLicenseSDate" , cursor.getColumnName(0) + " : "+cursor.getString(0));

		return cursor.getString(0);
	}

	public int getMemberIdx() {
		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT IFNULL(MAX(IDX),0) +1 AS IDX FROM " + Vars.TABLE_MEMBER;

		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null)
			cursor.moveToFirst();
		// return member
		return cursor.getInt(0);
	}

	// VslMembers 정보 숫자
	public int getVslMembersCount() {
		String countQuery = "SELECT COUNT(IDX) AS CNT FROM " + Vars.TABLE_VSL_MEMBER;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		//cursor.close();
		if (cursor != null)
			cursor.moveToFirst();
		// return count
		return cursor.getInt(0);
	}

	public void addVslMembers(ArrayList<MemberInfo> arr) {
		SQLiteDatabase db = this.getWritableDatabase();

		String sql = "INSERT INTO "+ Vars.TABLE_VSL_MEMBER +" VALUES (?,?,?,?,?,?,?);";
//		Log.e("addVslMembers", "sql = "+sql);
		SQLiteStatement statement = db.compileStatement(sql);
		db.beginTransaction();
		for (int i = 0; i < arr.size(); i++) {
//			Log.e("addProblems", i+" = "+arr.get(i).getIdx());
//			Log.e("addVslMembers", i+" = "+arr.get(i).getMaster_idx());
//			Log.e("addVslMembers", i+" = "+arr.get(i).getAuth());
//			Log.e("addVslMembers", i+" = "+arr.get(i).getId());
//			Log.e("addVslMembers", i+" = "+arr.get(i).getPw());
//			Log.e("addVslMembers", i+" = "+arr.get(i).getVsl());
//			Log.e("addVslMembers", i+" = "+arr.get(i).getVsl_type());

			statement.clearBindings();
			statement.bindNull(1);
			statement.bindLong(2, arr.get(i).getMaster_idx());
			statement.bindString(3, arr.get(i).getAuth());
			statement.bindString(4, arr.get(i).getId());
			statement.bindString(5, arr.get(i).getPw());
			statement.bindString(6, arr.get(i).getVsl());
			statement.bindLong(7, arr.get(i).getVsl_type());
			statement.execute();
		}
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	public int updateVslName(String vsl, int vsl_type) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(Vars.KEY_VSL_NAME, vsl);
		values.put(Vars.KEY_VSL_TYPE, vsl_type);

		// updating row
		return db.update(Vars.TABLE_VSL_MEMBER, values,null,null);
	}

	public String getVslName() {

		String VslName = "";
		String selectQuery = "SELECT VSL_NAME FROM " + Vars.TABLE_VSL_MEMBER;
		Log.e("test","selectQuery : " + selectQuery);

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor != null)
			cursor.moveToFirst();

		VslName = cursor.getString(0);
		Log.e("test","VslName : " + VslName);

		// return member list
		return VslName;
	}

	public void updateVslName(String vsl) {
		SQLiteDatabase db = this.getWritableDatabase();

//		Log.e("updateMember","KEY_SIGN_OFF = "+ "IDX = " + member.getIdx() + ("".equals(member.getSign_off()) ? "null" : member.getSign_off()));
		ContentValues values = new ContentValues();
//		values.put(Vars.KEY_MASTER_IDX, member.getMaster_idx());
		values.put(Vars.KEY_VSL_NAME, vsl);

		db.update(Vars.TABLE_VSL_MEMBER, values, null,null);
		db.update(Vars.TABLE_MEMBER, values, null,null);
		db.update(Vars.TABLE_TRAININGS_HISTORY, values, null,null);
	}

	public MemberInfo getVslMember(String id, String auth) {
		SQLiteDatabase db = this.getReadableDatabase();
		Log.e("test","first db = " + db);
		MemberInfo member = new MemberInfo();

		Cursor cursor = db.query(Vars.TABLE_VSL_MEMBER, null,  Vars.KEY_ID + " = ? AND " + Vars.KEY_AUTH + " = ? ",
				new String[] { id, auth }, null, null, null, null);

		if(cursor.getCount() > 0){

			if (cursor != null)
				cursor.moveToFirst();

//			Log.e("getVslMembers" , cursor.getColumnName(2) + " : "+cursor.getString(2));
//			Log.e("getVslMembers" , cursor.getColumnName(5) + " : "+cursor.getInt(5));
//			Log.e("getVslMembers" , cursor.getColumnName(6) + " : "+cursor.getInt(6));

			member.setIdx(cursor.getInt(0));
			member.setMaster_idx(cursor.getInt(1));
			member.setAuth(cursor.getString(2));
			member.setId(cursor.getString(3));
			member.setPw(cursor.getString(4));
			member.setVsl(cursor.getString(5));
			member.setVsl_type(cursor.getInt(6));
		}
		// return member
		return member;
	}


	// 새로운 DBmemberInfo 함수 추가
	public void addMember(MemberInfo member) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(Vars.KEY_IDX, member.getIdx());
		values.put(Vars.KEY_MASTER_IDX, member.getMaster_idx());
		values.put(Vars.KEY_RANK, member.getRank());
		values.put(Vars.KEY_FNAME, member.getFirstName());
		values.put(Vars.KEY_SNAME, member.getSurName());
		values.put(Vars.KEY_VSL_NAME, member.getVsl());
		values.put(Vars.KEY_VSL_TYPE, member.getVsl_type());
		values.put(Vars.KEY_BIRTH, member.getBirth());
		values.put(Vars.KEY_NATIONAL, member.getNational());
		values.put(Vars.KEY_SIGN_ON, member.getSign_on());
		values.put(Vars.KEY_SIGN_OFF, "".equals(member.getSign_off()) ? null : member.getSign_off());
		values.put(Vars.KEY_DEL_YN, member.getDel_yn());

		// Inserting Row
		db.insert(Vars.TABLE_MEMBER, null, values);
		db.close(); // Closing database connection
	}

	// id 에 해당하는 DBmemberInfo 객체 가져오기
	public MemberInfo getMember(int idx) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(Vars.TABLE_MEMBER, null,  Vars.KEY_IDX + " = ?",
				new String[] { String.valueOf(idx) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		MemberInfo member = new MemberInfo();
		member.setIdx(cursor.getInt(0));
		member.setMaster_idx(cursor.getInt(1));
		member.setRank(cursor.getInt(2));
		member.setFirstName(cursor.getString(3));
		member.setSurName(cursor.getString(4));
		member.setVsl(cursor.getString(5));
		member.setVsl_type(cursor.getInt(6));
		member.setBirth(cursor.getString(7));
		member.setNational(cursor.getInt(8));
		member.setSign_on(cursor.getString(9));
		member.setSign_off(cursor.getString(10));
		member.setDel_yn(cursor.getString(11));
		// return member
		return member;
	}

	// 모든 member 정보 가져오기
	public ArrayList<MemberInfo> getAllMembers() {
		ArrayList<MemberInfo> memberList = new ArrayList<MemberInfo>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + Vars.TABLE_MEMBER;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				MemberInfo member = new MemberInfo();
				member.setIdx(cursor.getInt(0));
				member.setMaster_idx(cursor.getInt(1));
				member.setRank(cursor.getInt(2));
				member.setFirstName(cursor.getString(3));
				member.setSurName(cursor.getString(4));
				member.setVsl(cursor.getString(5));
				member.setVsl_type(cursor.getInt(6));
				member.setBirth(cursor.getString(7));
				member.setNational(cursor.getInt(8));
				member.setSign_on(cursor.getString(9));
				member.setSign_off(cursor.getString(10));
				member.setDel_yn(cursor.getString(11));
				// Adding member to list
				memberList.add(member);
			} while (cursor.moveToNext());
		}

		// return member list
		return memberList;
	}

	public List<MemberInfo> getMembers(String query) {
		List<MemberInfo> memberList = new ArrayList<MemberInfo>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + Vars.TABLE_MEMBER + " WHERE 1=1 " + query + " ORDER BY RANK";
//		Log.e("getMembers", selectQuery);
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
//				Log.e("getMembers" , cursor.getColumnName(0) + " : "+cursor.getInt(0));
//				Log.e("getMembers" , cursor.getColumnName(3) + " : "+cursor.getString(3));
//				Log.e("getMembers" , cursor.getColumnName(4) + " : "+cursor.getString(4));
//				Log.e("getMembers" , cursor.getColumnName(10) + " : "+cursor.getString(10));
//				Log.e("getMembers" , cursor.getColumnName(11) + " : "+cursor.getString(11));
				MemberInfo member = new MemberInfo();
				member.setIdx(cursor.getInt(0));
				member.setMaster_idx(cursor.getInt(1));
				member.setRank(cursor.getInt(2));
				member.setFirstName(cursor.getString(3));
				member.setSurName(cursor.getString(4));
				member.setVsl(cursor.getString(5));
				member.setVsl_type(cursor.getInt(6));
				member.setBirth(cursor.getString(7));
				member.setNational(cursor.getInt(8));
				member.setSign_on(cursor.getString(9));
				member.setSign_off(cursor.getString(10));
				member.setDel_yn(cursor.getString(11));
				member.setChecked(false);
				// Adding member to list
				memberList.add(member);
			} while (cursor.moveToNext());
		}

		// return member list
		return memberList;
	}

	//member 정보 업데이트
	public int updateMember(MemberInfo member) {
		SQLiteDatabase db = this.getWritableDatabase();

//		Log.e("updateMember","KEY_SIGN_OFF = "+ "IDX = " + member.getIdx() + ("".equals(member.getSign_off()) ? "null" : member.getSign_off()));
		ContentValues values = new ContentValues();
//		values.put(Vars.KEY_MASTER_IDX, member.getMaster_idx());
		values.put(Vars.KEY_RANK, member.getRank());
		values.put(Vars.KEY_FNAME, member.getFirstName());
		values.put(Vars.KEY_SNAME, member.getSurName());
		values.put(Vars.KEY_VSL_NAME, member.getVsl());
		values.put(Vars.KEY_VSL_TYPE, member.getVsl_type());
		values.put(Vars.KEY_BIRTH, member.getBirth());
		values.put(Vars.KEY_NATIONAL, member.getNational());
		values.put(Vars.KEY_SIGN_ON, member.getSign_on());
		values.put(Vars.KEY_SIGN_OFF, "".equals(member.getSign_off()) ? null : member.getSign_off());

		// updating row
		return db.update(Vars.TABLE_MEMBER, values, Vars.KEY_IDX + " = ?",
				new String[] { String.valueOf(member.getIdx()) });
	}

	public int updateMember(int idx, String Sign_off) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(Vars.KEY_SIGN_OFF, "".equals(Sign_off) ? null : Sign_off);

		// updating row
		return db.update(Vars.TABLE_MEMBER, values, Vars.KEY_IDX + " = ?",
				new String[] { String.valueOf(idx) });
	}


	public int deleteMember(int idx) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(Vars.KEY_DEL_YN, "Y");

		// updating row
		return db.update(Vars.TABLE_MEMBER, values, Vars.KEY_IDX + " = ?",
				new String[] { String.valueOf(idx) });
	}

	public int test() {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(Vars.KEY_DEL_YN, "N");

		// updating row
		return db.update(Vars.TABLE_MEMBER, values, null,
				null);
	}

	// member 정보 삭제하기
	public void deleteMemberData(MemberInfo member) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(Vars.TABLE_MEMBER, Vars.KEY_IDX + " = ?",
				new String[] { String.valueOf(member.getIdx()) });
		db.close();
	}

	// member 정보 숫자
	public int getMembersCount(String query) {
		String countQuery = "SELECT COUNT(IDX) AS CNT FROM " + Vars.TABLE_MEMBER + " WHERE 1=1 " + query;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		//cursor.close();
		if (cursor != null)
			cursor.moveToFirst();
		// return count
		return cursor.getInt(0);
	}

	public void updateAll() {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(Vars.KEY_MASTER_IDX, 8);

		db.update(Vars.TABLE_MEMBER, values, Vars.KEY_MASTER_IDX + " = ?",
				new String[] { String.valueOf(7) });

		db.update(Vars.TABLE_VSL_MEMBER, values, Vars.KEY_MASTER_IDX + " = ?",
				new String[] { String.valueOf(7) });

		db.close();
	}
}