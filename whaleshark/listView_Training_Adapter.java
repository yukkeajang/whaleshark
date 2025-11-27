package com.togetherseatech.whaleshark;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.togetherseatech.whaleshark.Db.History.TrainingInfo;
import com.togetherseatech.whaleshark.util.SelectItems;
import com.togetherseatech.whaleshark.util.TextFontUtil;

import java.util.List;

public class listView_Training_Adapter extends ArrayAdapter<SelectItems>{

	private Context mContext;
	private int LayoutResource;
	private List<SelectItems> SiList;
	private LayoutInflater Inflater;
	private ViewHolder viewHolder = null;
	private SharedPreferences pref;
	private ToggleButton tb;
	private SelectItems data;
	private String LOGIN_LANGUAGE;
	/**
	 * ListViewCustomAdapter 생성자
	 *
	 * @param context 컨텍스트
	 * @param rowLayoutResource 레이아웃리소스(일반적으로는 텍스트뷰 리소스:리스트는 기본적으로 텍스트를 뿌리도록 되어있다.)
	 * @param SiList 입력된 리스트
	 */
	public listView_Training_Adapter(Context context, int rowLayoutResource, List<SelectItems> SiList)
	{
		super(context, rowLayoutResource, SiList);
		this.mContext = context;
		this.LayoutResource = rowLayoutResource;
		this.SiList = SiList;
		this.Inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
	}

	/**
	 * ListView에 사용되는 아이템의 숫자를 반환한다.
	 */
	@Override
	public int getCount()
	{
		return SiList.size();
	}

	/**
	 * position(index)를 통해 리스트에서 해당 position의 item을 반환한다.
	 */
	@Override
	public SelectItems getItem(int position)
	{
		return SiList.get(position);
	}

	/**
	 * item(여기서는 Data)를 통해 리스트에서 해당 item의 position을 반환한다.
	 */
	@Override
	public int getPosition(SelectItems item)
	{
		return SiList.indexOf(item);
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
		if(data != null)
		{
			viewHolder.iv_bg = (ImageView)convertView.findViewById(R.id.trainings_row_iv);
			viewHolder.iv_bg.setTag(position+"");
//			viewHolder.rl_bg.setPadding(0,0,20,0);
			/*RelativeLayout.LayoutParams relativeParams = (RelativeLayout.LayoutParams)viewHolder.rl_bg.getLayoutParams();
			relativeParams.setMargins(0, 0, 80, 0);  // left, top, right, bottom
			viewHolder.rl_bg.setLayoutParams(relativeParams);*/
			viewHolder.iv_square = (ImageView) convertView.findViewById(R.id.trainings_row_square_iv);
			viewHolder.tv_chapter = (TextView)convertView.findViewById(R.id.trainings_row_chapter_tv);
			/*if(position == 0) {
				viewHolder.rl_bg.setBackgroundResource(R.mipmap.personal_row_on);
				viewHolder.iv_square.setBackgroundResource(R.mipmap.trainings_check_on);
			}*/
//			Log.e("listViewTrainingAdapter","position = "+position);
//			Log.e("listViewTrainingAdapter","data.getChecked() = "+data.getChecked());
			if(data.getChecked()) {
				viewHolder.iv_bg.setBackgroundResource(R.mipmap.personal_row_on);
				viewHolder.iv_square.setBackgroundResource(R.mipmap.trainings_check_on);
			} else {
				viewHolder.iv_bg.setBackgroundResource(R.mipmap.personal_row_off);
				viewHolder.iv_square.setBackgroundResource(R.mipmap.trainings_check_off);
			}

			if("KR".equals(LOGIN_LANGUAGE))
				viewHolder.tv_chapter.setText(data.getKr());
			else
				viewHolder.tv_chapter.setText(data.getEng());
//			viewHolder.tv_chapter.setVisibility(View.INVISIBLE);
			tf.setNanumGothicExtraBold(mContext, viewHolder.tv_chapter);
		}
		
		// 만들어진 뷰(어댑터를 통해 아이템을 사용자에게 보여지도록 바꾼)를 반환한다.
		return convertView;
	}
	
	class ViewHolder {
		public ImageView iv_bg;
		public ImageView iv_square;
		public TextView tv_chapter;
	}
}
