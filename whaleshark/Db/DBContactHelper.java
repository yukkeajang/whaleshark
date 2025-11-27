package com.togetherseatech.whaleshark.Db;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.togetherseatech.whaleshark.Vars;

/**
 * DBSqllte 엑티비티
 *
 */
public class DBContactHelper extends SQLiteOpenHelper {

	// All Static variables
	// Database Versionaa
	private static final int DATABASE_VERSION = 4; //4
//	private static int DATABASE_NEW_VERSION = 0; //0
	// Database Name
	private static final String DATABASE_NAME = "seatech";
	private SharedPreferences pref;
	private SharedPreferences.Editor editor;
	private Context con;

	public DBContactHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);  // 마지막 인자는 버전임
		con = context;
//		Log.e("DBContactHelper", "DATABASE_VERSION : " + DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
//		Log.e("DBContactHelper", "onCreate {"+DATABASE_NEW_VERSION+"}");

		// 쿼리구문 실행 dic이라는 테이블을 만듬
		String CREATE_PROBLEM = "CREATE TABLE IF NOT EXISTS PROBLEM ("
				+ Vars.KEY_IDX + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ Vars.KEY_CHAPTER + " INTEGER NOT NULL,"
				+ Vars.KEY_LEVEL + " INTEGER NOT NULL,"
				+ Vars.KEY_NO + " INTEGER NOT NULL,"
				+ Vars.KEY_VSL_TYPE + " INTEGER NOT NULL,"
				+ Vars.KEY_TITLE_KR + " TEXT NOT NULL,"
				+ Vars.KEY_TITLE_ENG + " TEXT NOT NULL,"
				+ Vars.KEY_ANSWER + " TEXT NOT NULL,"
				+ Vars.KEY_RELATIVE_REGULATION + " TEXT,"
				+ Vars.KEY_VOICE_KR + " TEXT,"
				+ Vars.KEY_VOICE_ENG + " TEXT,"
				+ Vars.KEY_FLASH_VIDEO + " TEXT )";
//				+ Vars.KEY_EXPLANATION_KR + " TEXT,"
//				+ Vars.KEY_EXPLANATION_ENG + " TEXT )";

		db.execSQL(CREATE_PROBLEM);

		String CREATE_SUB_PROBLEM = "CREATE TABLE IF NOT EXISTS SUB_PROBLEM ("
				+ Vars.KEY_IDX + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ Vars.KEY_CHAPTER + " INTEGER NOT NULL,"
				+ Vars.KEY_LEVEL + " INTEGER NOT NULL,"
				+ Vars.KEY_NO + " INTEGER NOT NULL,"
                + Vars.KEY_PROBLEM_ANSWER + " TEXT NOT NULL,"
				+ Vars.KEY_CONTENT_KR + " TEXT NOT NULL,"
				+ Vars.KEY_CONTENT_ENG + " TEXT NOT NULL )";
//				+ " FOREIGN KEY ("+KEY_PROBLEM_IDX+") REFERENCES PROBLEM ( "+KEY_IDX+"))";

		db.execSQL(CREATE_SUB_PROBLEM);

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

//		if(DATABASE_NEW_VERSION == 1) {

		String CREATE_KEY_LICENSE = "CREATE TABLE IF NOT EXISTS KEY_LICENSE ("
				+ Vars.KEY_IDX + " INTEGER NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT,"
				+ Vars.KEY_START + " TEXT,"
				+ Vars.KEY_END + " TEXT,"
				+ Vars.KEY_KEY + " TEXT NOT NULL )";
		db.execSQL(CREATE_KEY_LICENSE);

		String CREATE_VSL_MEMBER = "CREATE TABLE IF NOT EXISTS VSL_MEMBER ("
				+ Vars.KEY_IDX + " INTEGER NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT,"
				+ Vars.KEY_MASTER_IDX + " INTEGER NOT NULL,"
				+ Vars.KEY_AUTH + " TEXT NOT NULL,"
				+ Vars.KEY_ID + " TEXT NOT NULL,"
				+ Vars.KEY_PW + " TEXT NOT NULL,"
				+ Vars.KEY_VSL_NAME + " TEXT NOT NULL,"
				+ Vars.KEY_VSL_TYPE + " INTEGER NOT NULL )";
//					+ " FOREIGN KEY (" + Vars.KEY_MASTER_IDX + ")"
//					+ " REFERENCES VSL_MEMBER (" + Vars.KEY_IDX + ") ON DELETE CASCADE)";
		db.execSQL(CREATE_VSL_MEMBER);

		String CREATE_MEMBER = "CREATE TABLE IF NOT EXISTS MEMBER ("
				+ Vars.KEY_IDX + " INTEGER NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT,"
				+ Vars.KEY_MASTER_IDX + " INTEGER NOT NULL,"
				+ Vars.KEY_RANK + " INTEGER NOT NULL,"
				+ Vars.KEY_FNAME + " TEXT NOT NULL,"
				+ Vars.KEY_SNAME + " TEXT NOT NULL,"
				+ Vars.KEY_VSL_NAME + " TEXT NOT NULL,"
				+ Vars.KEY_VSL_TYPE + " INTEGER NOT NULL,"
				+ Vars.KEY_BIRTH + " TEXT NOT NULL,"
				+ Vars.KEY_NATIONAL + " INTEGER NOT NULL,"
				+ Vars.KEY_SIGN_ON + " TEXT NOT NULL,"
				+ Vars.KEY_SIGN_OFF + " TEXT,"
				+ Vars.KEY_DEL_YN + " TEXT )";
//					+ " FOREIGN KEY (" + Vars.KEY_VSL_IDX + ")"
//					+ " REFERENCES VSL_MEMBER (" + Vars.KEY_IDX + ") ON DELETE CASCADE )";
		db.execSQL(CREATE_MEMBER);

		String CREATE_TRAINING_HISTORY = "CREATE TABLE IF NOT EXISTS TRAINING_HISTORY ("
				+ Vars.KEY_IDX + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ Vars.KEY_MEMBER_IDX + " INTEGER NOT NULL,"
				+ Vars.KEY_TRAINING_COURSE + " INTEGER NOT NULL,"
				+ Vars.KEY_YEAR+ " INTEGER NOT NULL,"
				+ Vars.KEY_QUARTER+ " INTEGER NOT NULL,"
				+ Vars.KEY_DATE + " TEXT ,"
				+ Vars.KEY_TIME + " INTEGER ,"
				+ Vars.KEY_SCORE + " INTEGER ,"
				+ Vars.KEY_SUBMIT + " TEXT ,"
				+ " FOREIGN KEY (" + Vars.KEY_MEMBER_IDX + ")"
				+ " REFERENCES MEMBER (" + Vars.KEY_IDX + ") ON DELETE CASCADE )";

		db.execSQL(CREATE_TRAINING_HISTORY);

		String CREATE_TRANING_PROBLEM = "CREATE TABLE IF NOT EXISTS TRANING_PROBLEM ("
				+ Vars.KEY_IDX + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ Vars.KEY_HISTORY_IDX + " INTEGER NOT NULL,"
				+ Vars.KEY_PROBLEM_IDX + " INTEGER NOT NULL,"
				+ Vars.KEY_RELATIVE_REGULATION + " TEXT NOT NULL ,"
				+ " FOREIGN KEY (" + Vars.KEY_HISTORY_IDX + ")"
				+ " REFERENCES TRAINING_HISTORY (" + Vars.KEY_IDX + ") ON DELETE CASCADE )";
		//				+ " FOREIGN KEY ("+KEY_HISTORY_IDX+") REFERENCES TRAINING_HISTORY ( "+KEY_IDX+"),"
		//				+ " FOREIGN KEY ("+KEY_PROBLEM_IDX+") REFERENCES PROBLEM ( "+KEY_IDX+"))";

		db.execSQL(CREATE_TRANING_PROBLEM);

		String CREATE_TRAININGS_HISTORY = "CREATE TABLE IF NOT EXISTS TRAININGS_HISTORY ("
				+ Vars.KEY_IDX + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ Vars.KEY_TYPE + " TEXT NOT NULL,"
				+ Vars.KEY_VSL_NAME + " TEXT NOT NULL,"
				+ Vars.KEY_VSL_TYPE + " INTEGER NOT NULL,"
				+ Vars.KEY_TRAINING_COURSE + " TEXT NOT NULL,"
				+ Vars.KEY_DATE + " TEXT NOT NULL,"
				+ Vars.KEY_SCORE + " INTEGER NOT NULL,"
				+ Vars.KEY_SUBMIT + " TEXT )";
//					+ " FOREIGN KEY (" + Vars.KEY_MEMBER_IDX + ")"
//					+ " REFERENCES MEMBER (" + Vars.KEY_IDX + ") ON DELETE CASCADE )";

		db.execSQL(CREATE_TRAININGS_HISTORY);

		String CREATE_TRAININGS_MEMBER = "CREATE TABLE IF NOT EXISTS TRAININGS_MEMBER ("
				+ Vars.KEY_IDX + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ Vars.KEY_HISTORY_IDX + " INTEGER NOT NULL,"
				+ Vars.KEY_MEMBER_IDX + " INTEGER NOT NULL,"
				+ " FOREIGN KEY (" + Vars.KEY_HISTORY_IDX + ")"
				+ " REFERENCES TRAININGS_HISTORY (" + Vars.KEY_IDX + ") ON DELETE CASCADE )";

		db.execSQL(CREATE_TRAININGS_MEMBER);
//		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		DATABASE_NEW_VERSION = newVersion;
		pref = con.getSharedPreferences("pref", Context.MODE_PRIVATE);
		editor = pref.edit();
		editor.putBoolean("PROBLEM_UPDATE", false).commit();
//		Log.e("DBContactHelper", "onUpgrade : "+ pref.getBoolean("PROBLEM_UPDATE", false));
		// 디비 버전이 바뀌었을 때 처리
		db.execSQL("DROP TABLE IF EXISTS PROBLEM");  // 기존 테이블 삭제
		db.execSQL("DROP TABLE IF EXISTS SUB_PROBLEM");
		onCreate(db);   // 데이터베이스 다시 만듬
	}
}