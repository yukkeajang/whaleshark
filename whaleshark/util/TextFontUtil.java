package com.togetherseatech.whaleshark.util;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicBoolean;

public class TextFontUtil {

	/**
	 * 데이터 클래스 생성자
	 */
	public TextFontUtil(){
		
	}
	
	public void setNanumGothic(Context con, TextView text){
	    Typeface typeface = Typeface.createFromAsset( con.getAssets() , "font/NanumGothic.ttf" );
	    text.setTypeface(typeface);                                  //이부분은 Assets 에들어간 폰트 파일명을 적어주세요.
	}
	
	public void setNanumGothicBold(Context con, TextView text){
	    Typeface typeface = Typeface.createFromAsset( con.getAssets() , "font/NanumGothicBold.ttf" );
	    text.setTypeface(typeface);
	}
	
	public void setNanumGothicExtraBold(Context con, TextView text){
	    Typeface typeface = Typeface.createFromAsset( con.getAssets() , "font/NanumGothicExtraBold.ttf" );
	    text.setTypeface(typeface);
	}

	public void setNanumGothicLight(Context con, TextView text){
		Typeface typeface = Typeface.createFromAsset( con.getAssets() , "font/NanumGothicLight.ttf" );
		text.setTypeface(typeface);
	}

	public void setNanumSquareB(Context con, TextView text){
	    Typeface typeface = Typeface.createFromAsset( con.getAssets() , "font/NanumSquareB.ttf" );
	    text.setTypeface(typeface);
	}
	
	public void setNanumSquareEB(Context con, TextView text){
	    Typeface typeface = Typeface.createFromAsset( con.getAssets() , "font/NanumSquareEB.ttf" );
	    text.setTypeface(typeface);
	}
	
	public void setNanumSquareL(Context con, TextView text){
	    Typeface typeface = Typeface.createFromAsset( con.getAssets() , "font/NanumSquareL.ttf" );
	    text.setTypeface(typeface);
	}
	
	public void setNanumSquareR(Context con, TextView text){
	    Typeface typeface = Typeface.createFromAsset( con.getAssets() , "font/NanumSquareR.ttf" );
	    text.setTypeface(typeface);
	}
	
	public void setNanumSquareRoundB(Context con, TextView text){
	    Typeface typeface = Typeface.createFromAsset( con.getAssets() , "font/NanumSquareRoundB.ttf" );
	    text.setTypeface(typeface);
	}
	
	public void setNanumSquareRoundEB(Context con, TextView text){
	    Typeface typeface = Typeface.createFromAsset( con.getAssets() , "font/NanumSquareRoundEB.ttf" );
	    text.setTypeface(typeface);
	}
	
	public void setNanumSquareRoundL(Context con, TextView text){
	    Typeface typeface = Typeface.createFromAsset( con.getAssets() , "font/NanumSquareRoundL.ttf" );
	    text.setTypeface(typeface);
	}
	
	public void setNanumSquareRoundR(Context con, TextView text){
	    Typeface typeface = Typeface.createFromAsset( con.getAssets() , "font/NanumSquareRoundR.ttf" );
	    text.setTypeface(typeface);
	}

	public void setSourceSansPro_Semibold(Context con, TextView text){
		Typeface typeface = Typeface.createFromAsset( con.getAssets() , "font/SourceSansPro-Semibold.otf" );
		text.setTypeface(typeface);
	}

	public void setTimefont(Context con, TextView text){
		Typeface typeface = Typeface.createFromAsset( con.getAssets() , "font/timefont.ttf" );
		text.setTypeface(typeface);
	}

	public void setOdstemplik(Context con, TextView text){
		Typeface typeface = Typeface.createFromAsset( con.getAssets() , "font/odstemplik.otf" );
		text.setTypeface(typeface);
	}

	public void setOdstemplikBold(Context con, TextView text){
		Typeface typeface = Typeface.createFromAsset( con.getAssets() , "font/odstemplikBold.otf" );
		text.setTypeface(typeface);
	}

	public void setChampignon(Context con, TextView text){
		Typeface typeface = Typeface.createFromAsset( con.getAssets() , "font/Champignon.otf" );
		text.setTypeface(typeface);
	}

	public void setChampignonaltswash(Context con, TextView text){
		Typeface typeface = Typeface.createFromAsset( con.getAssets() , "font/champignonaltswash.ttf" );
		text.setTypeface(typeface);
	}

	public void setPrestigeSignatureSerif(Context con, TextView text){
		Typeface typeface = Typeface.createFromAsset( con.getAssets() , "font/PrestigeSignatureSerif.ttf" );
		text.setTypeface(typeface);
	}

	public void setOranienbaum(Context con, TextView text){
		Typeface typeface = Typeface.createFromAsset( con.getAssets() , "font/Oranienbaum.ttf" );
		text.setTypeface(typeface);
	}
	/**
	 * 키패드.
	 * */

	public void showKeypad(Context con, EditText editText){
		InputMethodManager manager = (InputMethodManager) con.getSystemService(Context.INPUT_METHOD_SERVICE);
		manager.showSoftInput(editText, InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	public void hideKeypad(Context con, EditText editText){
		InputMethodManager manager = (InputMethodManager)con.getSystemService(Context.INPUT_METHOD_SERVICE);
		manager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}
}
