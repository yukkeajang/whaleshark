package com.togetherseatech.whaleshark;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.togetherseatech.whaleshark.Db.Training.TrainingsInfo;
import com.togetherseatech.whaleshark.util.SelectItems;
import com.togetherseatech.whaleshark.util.TextFontUtil;

import java.util.List;

public class listView_Regulation_Adapter extends ArrayAdapter<TrainingsInfo>{

	private Context mContext;
	private int LayoutResource;
	private List<TrainingsInfo> List;
	private LayoutInflater Inflater;
	private ViewHolder viewHolder = null;
	private SharedPreferences pref;
	private TrainingsInfo data;
	private String LOGIN_LANGUAGE;
	private SelectItems Si;

	/**
	 * ListViewCustomAdapter 생성자
	 *
	 * @param context 컨텍스트
	 * @param rowLayoutResource 레이아웃리소스(일반적으로는 텍스트뷰 리소스:리스트는 기본적으로 텍스트를 뿌리도록 되어있다.)
	 * @param objects 입력된 리스트
	 */
	public listView_Regulation_Adapter(Context context, int rowLayoutResource, List<TrainingsInfo> objects)
	{
		super(context, rowLayoutResource, objects);
		this.mContext = context;
		this.LayoutResource = rowLayoutResource;
		this.List = objects;
		this.Inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
	}


	/**
	 * ListView에 사용되는 아이템의 숫자를 반환한다.
	 */
	@Override
	public int getCount()
	{
		return List.size();
	}

	/**
	 * position(index)를 통해 리스트에서 해당 position의 item을 반환한다.
	 */
	@Override
	public TrainingsInfo getItem(int position)
	{
		return List.get(position);
	}

	/**
	 * item(여기서는 Data)를 통해 리스트에서 해당 item의 position을 반환한다.
	 */
	@Override
	public int getPosition(TrainingsInfo item)
	{
		return List.indexOf(item);
	}

	/**
	 * 해당 position의 뷰를 만들어 반환한다.
	 */
	@Override
	public View getView(final int position, View convertView, final ViewGroup parent)
	{
//		System.out.println("position : "+position);
		// convertView(View)가 없는 경우 inflater로 뷰 객체를 생성한다.
		viewHolder = new ViewHolder();
		if(convertView == null)
		{
			convertView = Inflater.inflate(LayoutResource, null);
		}
		
		// 리스트에서 포지션에 맞는 데이터를 받아온다.
		data = getItem(position);
		// 데이터가 있는 경우에만 처리한다.
		//Log.e("getView","data : "+ data);
		TextFontUtil tf = new TextFontUtil();
		LOGIN_LANGUAGE = pref.getString("LOGIN_LANGUAGE","");
		Si = new SelectItems();
		if(data != null)
		{
			// 어댑터에서의 findViewById() 메소드는 convertView 객체를 통해서 사용한다.
			// convertView를 통해 위젯을 레퍼런스 한다.
			viewHolder.tv_year = (TextView)convertView.findViewById(R.id.regulation_row_year_tv);
			viewHolder.tv_agreement = (TextView)convertView.findViewById(R.id.regulation_row_agreement_tv);
			viewHolder.tv_title = (TextView)convertView.findViewById(R.id.regulation_row_title_tv);

			tf.setNanumSquareL(mContext, viewHolder.tv_year);
			tf.setNanumSquareL(mContext, viewHolder.tv_agreement);
			tf.setNanumSquareL(mContext, viewHolder.tv_title);

			viewHolder.tv_year.setText(data.getYear());
			viewHolder.tv_agreement.setText(data.getAgreement());
			viewHolder.tv_title.setText(data.getTitle());
//			Log.e("Regulation_Adapter","getView getMChecked: " + data.getMChecked());
			if(data.getMChecked()) {
				viewHolder.tv_year.setTextColor(Color.parseColor("#9E005D"));
				viewHolder.tv_agreement.setTextColor(Color.parseColor("#9E005D"));
				viewHolder.tv_title.setTextColor(Color.parseColor("#9E005D"));
			} else {
				viewHolder.tv_year.setTextColor(Color.parseColor("#4B5366"));
				viewHolder.tv_agreement.setTextColor(Color.parseColor("#4B5366"));
				viewHolder.tv_title.setTextColor(Color.parseColor("#4B5366"));
			}
		}else{

		}
		
		// 만들어진 뷰(어댑터를 통해 아이템을 사용자에게 보여지도록 바꾼)를 반환한다.
		return convertView;
	}
	
	class ViewHolder {
		public TextView tv_year;
		public TextView tv_agreement;
		public TextView tv_title;
	}
}
