package com.togetherseatech.whaleshark;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.ArrayRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.togetherseatech.whaleshark.util.CommonUtil;
import com.togetherseatech.whaleshark.util.SelectItems;
import com.togetherseatech.whaleshark.util.TextFontUtil;

import java.util.List;

/**
 * Created by seonghak on 2017. 11. 8..
 */

public class SpinnerArrayAdapter extends ArrayAdapter<SelectItems> {

    private Activity mContext;
    private int mLayoutResource;
    private int mDropDownResource;
    private List<SelectItems> mList;
    private String language;
    private LayoutInflater mInflater;
    private LayoutInflater mDropDownInflater;
    private SharedPreferences pref;

    public SpinnerArrayAdapter(@NonNull Activity context, int resource, @NonNull List<SelectItems> objects) {
        super(context, resource, objects);

        this.mContext = context;
        this.mLayoutResource = mDropDownResource= resource;
        this.mList = objects;
        this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public SelectItems getItem(int i) {
        return mList.get(i);
    }

    @Override
    public int getPosition(SelectItems item)
    {
        return mList.indexOf(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        Log.e("SpinnerArrayAdapter","getView");
        return createViewFromResource(mInflater, position, convertView, parent, mLayoutResource);
    }

    private View createViewFromResource(LayoutInflater inflater, int position, View convertView,
                                        ViewGroup parent, int resource) {
        View view;
        TextView text;
        TextFontUtil tf = new TextFontUtil();
        if (convertView == null) {
            view = inflater.inflate(resource, parent, false);
        } else {
            view = convertView;
        }

        text = (TextView) view;

        SelectItems item = getItem(position);

        language = pref.getString("LOGIN_LANGUAGE","");

        if("KR".equals(language)) {
            text.setText(item.getKr().toString());
        } else {
            text.setText(item.getEng().toString());
        }
        tf.setNanumSquareL(mContext, text);
        return view;
    }

    public void setDropDownViewResource(@LayoutRes int resource) {
        this.mDropDownResource = resource;
//        Log.e("SpinnerArrayAdapter","setDropDownViewResource");
    }

    /**
     * Sets the {@link Resources.Theme} against which drop-down views are
     * inflated.
     * <p>
     * By default, drop-down views are inflated against the theme of the
     * {@link Context} passed to the adapter's constructor.
     *
     * @param theme the theme against which to inflate drop-down views or
     *              {@code null} to use the theme from the adapter's context
     * @see #getDropDownView(int, View, ViewGroup)
     */
    @Override
    public void setDropDownViewTheme(Resources.Theme theme) {
//        Log.e("SpinnerArrayAdapter","setDropDownViewTheme");
        if (theme == null) {
            mDropDownInflater = null;
        } else if (theme == mInflater.getContext().getTheme()) {
            mDropDownInflater = mInflater;
        } else {
            final Context context = new ContextThemeWrapper(mContext, theme);
            mDropDownInflater = LayoutInflater.from(context);
        }
    }

    @Override
    public Resources.Theme getDropDownViewTheme() {
//        Log.e("SpinnerArrayAdapter","getDropDownViewTheme");
        return mDropDownInflater == null ? null : mDropDownInflater.getContext().getTheme();
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
//        Log.e("SpinnerArrayAdapter","getDropDownView");
        final LayoutInflater inflater = mDropDownInflater == null ? mInflater : mDropDownInflater;
        return createViewFromResource(inflater, position, convertView, parent, mDropDownResource);
    }

    /**
     * Creates a new ArrayAdapter from external resources. The content of the array is
     * obtained through {@link android.content.res.Resources#getTextArray(int)}.
     *
     * @param context The application's environment.
     * @param textArrayResId The identifier of the array to use as the data source.
     * @param textViewResId The identifier of the layout used to create views.
     *
     * @return An ArrayAdapter<CharSequence>.
     */
    public static ArrayAdapter<CharSequence> createFromResource(Context context, @ArrayRes int textArrayResId, @LayoutRes int textViewResId) {
//        Log.e("SpinnerArrayAdapter","createFromResource");
        CharSequence[] strings = context.getResources().getTextArray(textArrayResId);
        return new ArrayAdapter<CharSequence>(context, textViewResId, strings);
    }
}
