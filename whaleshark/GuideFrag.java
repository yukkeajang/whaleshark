package com.togetherseatech.whaleshark;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@SuppressLint("ValidFragment")
public class GuideFrag extends Fragment {

    private String PAGE = "GuideFrag";
    private ViewGroup mContainer;
    private Context mContext, pContext;
    private int LayoutId;

    public GuideFrag(Activity Act, int Position) {
        this.LayoutId = Act.getResources().getIdentifier("guide_frag_"+(Position+1), "layout", "com.togetherseatech.whaleshark");
//        Log.e(PAGE, "Position : " + Position);
//        Log.e(PAGE, "LayoutId : " + LayoutId);

    }

    public GuideFrag(Activity Act, int GPosition, int CPosition) {
        this.LayoutId = Act.getResources().getIdentifier("guide_frag_"+(GPosition+1)+"_"+(CPosition+1), "layout", "com.togetherseatech.whaleshark");
//        Log.e(PAGE, "Position : " + Position);
//        Log.e(PAGE, "LayoutId : " + LayoutId);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mContainer = container;
        this.mContext = getContext();
        this.pContext = new ContextThemeWrapper(getContext(), android.R.style.Theme_Holo_Light_Dialog);
//        Log.e(PAGE, R.layout.guide_frag_1+"");
        return inflater.inflate(LayoutId, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initSetting();
        initEventListener();
//        ImageView iv = (ImageView) mContainer.findViewById(R.id.imageView);
//        iv.measure(0,0);
//        Log.e(PAGE, "getWidth = " + iv.getMeasuredWidth());
    }

    private void initSetting() {

    }

    private void initEventListener() {

    }

}