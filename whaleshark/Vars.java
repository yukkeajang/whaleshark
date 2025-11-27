package com.togetherseatech.whaleshark;

/**
 * Created by seonghak on 2017. 10. 27..
 */

public class Vars {

    public Vars(){};

    public static final String KEY_LICENSE_DATE = "2018-01-14";
    public static final int KEY_HALF_YEAR = 6;
    public static final int KEY_FULL_YEAR = 12;
    public static final String KEY_WEB_SITE = "http://www.togetherseatech.com";
    public static final String KEY_ZIP_PASSWORD = "seatech0801!";

    public static final String KEY_IDX = "idx";
    public static final String KEY_MASTER_IDX = "master_idx";

    // KEY_LICENSE Table & Columns names
    public static final String TABLE_KEY_LICENSE = "KEY_LICENSE";
    public static final String KEY_START = "START_DATE";
    public static final String KEY_END = "END_DATE";
    public static final String KEY_KEY = "KEY_LICENSE";

    // VSL_MEMBER Table & Columns names
    public static final String TABLE_VSL_MEMBER = "VSL_MEMBER";
    public static final String KEY_ID = "id";
    public static final String KEY_PW = "pw";
    public static final String KEY_AUTH = "auth";

    // MEMBER Table & Columns names
    public static final String TABLE_MEMBER = "MEMBER";
    public static final String KEY_VSL_IDX = "vsl_idx";
    public static final String KEY_FNAME = "fname";
    public static final String KEY_SNAME = "sname";
    public static final String KEY_RANK = "rank";
    public static final String KEY_VSL_NAME = "vsl_name";
    public static final String KEY_VSL_TYPE = "vsl_type";
    public static final String KEY_BIRTH = "birth";
    public static final String KEY_NATIONAL = "national";
    public static final String KEY_SIGN_ON = "sign_on";
    public static final String KEY_SIGN_OFF = "sign_off";
    public static final String KEY_DEL_YN = "del_yn";

    // PROBLEM Table & Columns names
    public static final String TABLE_PROBLEM = "PROBLEM";
//    public static final String KEY_LANGUAGE_TYPE = "language_type";
    public static final String KEY_NO = "no";
    public static final String KEY_CHAPTER = "chapter";
    public static final String KEY_LEVEL = "level";
    public static final String KEY_TITLE_KR = "title_kr";
    public static final String KEY_TITLE_ENG = "title_eng";
    public static final String KEY_ANSWER = "answer";
    public static final String KEY_RELATIVE_REGULATION = "relative_regulation";
    public static final String KEY_VOICE_KR = "voice_kr";
    public static final String KEY_VOICE_ENG = "voice_eng";
    public static final String KEY_FLASH_VIDEO = "flash_video";

//    public static final String KEY_EXPLANATION_KR = "explanation_kr";
//    public static final String KEY_EXPLANATION_ENG = "explanation_eng";

    // SUB_PROBLEM Table & Columns names
    public static final String TABLE_SUB_PROBLEM = "SUB_PROBLEM";
    public static final String KEY_PROBLEM_IDX = "problem_idx";
    public static final String KEY_PROBLEM_ANSWER = "problem_answer";
    public static final String KEY_CONTENT_KR = "content_kr";
    public static final String KEY_CONTENT_ENG = "content_eng";

    // TRAINING_HISTORY Table & Columns names
    public static final String TABLE_TRAINING_HISTORY = "TRAINING_HISTORY";
    public static final String KEY_MEMBER_IDX = "member_idx";
    public static final String KEY_TRAINING_COURSE = "training_course";
    public static final String KEY_TRAINING_COURSE2 = "training_course2";
    public static final String KEY_YEAR = "year";
    public static final String KEY_QUARTER = "quarter";
    public static final String KEY_DATE = "date";
    public static final String KEY_TIME = "time";
    public static final String KEY_SCORE = "score";
    public static final String KEY_SUBMIT = "submit";

    // TRANING_PROBLEM Table & Columns names
    public static final String TABLE_TRANING_PROBLEM = "TRANING_PROBLEM";
    public static final String KEY_HISTORY_IDX = "history_idx";
    //public static final String KEY_PROBLEM_IDX = "problem_idx";
    public static final String KEY_ANSWER_YN = "answer_yn";

    public static final String TABLE_TRAININGS_HISTORY = "TRAININGS_HISTORY";
    public static final String TABLE_TRAININGS_MEMBER = "TRAININGS_MEMBER";
    public static final String KEY_TYPE = "type";

    public static final String TABLE_TRAININGS_MATRIX = "TRAININGS_MATRIX";
    public static final String KEY_NS = "ns";
    public static final String KEY_NJ = "nj";
    public static final String KEY_NR = "nr";
    public static final String KEY_ES = "es";
    public static final String KEY_EJ = "ej";
    public static final String KEY_ER = "er";
    public static final String KEY_OSR = "osr";

    public static final int LOGOUT = R.layout.logout_popup;
    public static final int DEL = R.layout.admin_delete_popup;
    public static final int UPDATE = R.layout.admin_update_popup;
    public static final int START = R.layout.trainings_start_popup;
    public static final int NEXT = R.layout.trainings_next_popup;
    public static final int END = R.layout.trainings_end_popup;
    public static final int BACKDB = R.layout.popup;

    public static final String DEL_N = " AND DEL_YN = 'N' AND VSL_TYPE = ";
    public static final String SIGNOFF_N = " AND SIGN_OFF IS NULL ";

    //공통 Massage
    public static final String ENG_MASSAGE_KR = "특수기호는 사용하실 수 없습니다.";
    public static final String ENG_MASSAGE_ENG = "special symbols are not allowed.";

    public static final String ID_MASSAGE_KR = "아이디를 입력하세요.";
    public static final String ID_MASSAGE_ENG = "Please enter your ID.";

    public static final String PASSWORD_MASSAGE_KR = "비밀번호를 입력하세요.";
    public static final String PASSWORD_MASSAGE_ENG = "Please enter your password.";

    public static final String CHECK_MASSAGE_KR = "ID와 Password를 확인하세요.";
    public static final String CHECK_MASSAGE_ENG = "Please check your ID and Password.";

    public static final String NAME_MASSAGE_KR = "이름을 입력하세요.";
    public static final String NAME_MASSAGE_ENG = "Please enter your name.";

    public static final String RANK_MASSAGE_KR = "직책을 입력하세요.";
    public static final String RANK_MASSAGE_ENG = "Please enter your rank.";

    public static final String NATIONAL_MASSAGE_KR = "국적을 입력하세요.";
    public static final String NATIONAL_MASSAGE_ENG = "Please enter your nationality.";

    public static final String BIRTH_MASSAGE_KR = "생년월일을 입력하세요.";
    public static final String BIRTH_MASSAGE_ENG = "Please enter your date of birth.";

    public static final String VSL_NAME_MASSAGE_KR = "선박 이름을 입력하세요.";
    public static final String VSL_NAME_MASSAGE_ENG = "Please enter a ship’s name.";

    public static final String VSL_TYPE_MASSAGE_KR = "선박 종류를 입력하세요.";
    public static final String VSL_TYPE_MASSAGE_ENG = "Please enter a ship’s type.";

    public static final String SIGN_ON_MASSAGE_KR = "승선 일자를 입력하세요.";
    public static final String SIGN_ON_MASSAGE_ENG = "Please enter the boarding date.";

    public static final String ADD_MASSAGE_KR = "최대 인원을 초과하였습니다.";
    public static final String ADD_MASSAGE_ENG = "Maximum number of people has been exceeded.";

    public static final String NEXT_MASSAGE_KR = "문제를 계속 진행하기 전에 정답을 입력하세요.";
    public static final String NEXT_MASSAGE_ENG = "Please enter an answer before you continue.";

    public static final String UPDATE_NO_MASSAGE_KR = "전송할 데이터가 없습니다.";
    public static final String UPDATE_NO_MASSAGE_ENG = "There is no data to send.";

    public static final String UPDATE_SAVE_MASSAGE_KR = "저장할 데이터가 없습니다.";
    public static final String UPDATE_SAVE_MASSAGE_ENG = "There is no data to save.";

    public static final String UPDATE_OK_MASSAGE_KR = "데이터 전송이 완료되었습니다.";
    public static final String UPDATE_OK_MASSAGE_ENG = "Data transfer completed.";

    public static final String UPDATE_FAIL_MASSAGE_KR = "데이터 전송이 실패하였습니다. 인터넷 확인 후 다시 시도 바랍니다.";
    public static final String UPDATE_FAIL_MASSAGE_ENG = "Data transfer has failed. Please check the internet and try again.";

    public static final String DOWNLOAD_OK_MASSAGE_KR = "데이터 저장이 완료되었습니다.";
    public static final String DOWNLOAD_OK_MASSAGE_ENG = "Data saving is completed.";

    public static final String DOWNLOAD_FAIL_MASSAGE_KR = "데이터 저장이 실패하였습니다. 다시 시도 바랍니다.";
    public static final String DOWNLOAD_FAIL_MASSAGE_ENG = "Data saving has failed. Please try again.";

    public static final String UPDATE_MASSAGE_KR = "데이터 전송 중입니다.";
    public static final String UPDATE_MASSAGE_ENG = "Data is being transmitted.";

    public static final String UPDATE_PROBLEM_MASSAGE_KR = "테스트 문제 업데이트 중입니다.";
    public static final String UPDATE_PROBLEM_MASSAGE_ENG = "Questions are being Updated.";

    public static final String LICENSE_OK_MASSAGE_KR = "라이선스 키가 등록되었습니다.";
    public static final String LICENSE_OK_MASSAGE_ENG = "Your license key has been registered with the WhaleShark.";

    public static final String LICENSE_NO_MASSAGE_KR = "라이선스 키가 잘못 입력되었습니다.";
    public static final String LICENSE_NO_MASSAGE_ENG = "License key has been entered incorrectly.";

    public static final String ATTENDANCE_MASSAGE_KR = "테스트 할 참석자를 선택하십시오.";
    public static final String ATTENDANCE_MASSAGE_ENG = "Please select the seafarer to be tested.";

    public static final String ATTENDANCE10_MASSAGE_KR = "테스트 할 참석자는 10명을 초과할 수 없습니다.";
    public static final String ATTENDANCE10_MASSAGE_ENG = "Attendees can not exceed a maximum 10 people.";
}
