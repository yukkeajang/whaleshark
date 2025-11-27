package com.togetherseatech.whaleshark;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.togetherseatech.whaleshark.util.SelectItems;
import com.togetherseatech.whaleshark.util.TextFontUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * BaseExpandableAdapter GeneralExpandableAdapter Class
 */
public class GeneralExpandableAdapter extends BaseExpandableListAdapter{
	private ArrayList<SelectItems> groupList = null;
	private ArrayList<ArrayList<SelectItems>> childList = null;
	private LayoutInflater inflater = null;
	private ViewHolder viewHolder = null;
	private Activity Act;
	private SharedPreferences pref;
	private SelectItems data, cdata;
	private String LOGIN_LANGUAGE;
	private SelectItems Si;
	private TextFontUtil tf;
//	private String type;
	/**
	 * 데이터 클래스 생성자
	 * @param act Activity
	 * @param groupList List<SelectItems>
	 * @param childList ArrayList<ArrayList<SelectItems>>
	 */
	public GeneralExpandableAdapter(Activity act, ArrayList<SelectItems> groupList, ArrayList<ArrayList<SelectItems>> childList) {
      	super();
      	this.inflater = LayoutInflater.from(act);
      	this.groupList = groupList;
      	this.childList = childList;
		this.Act = act;
		this.pref = act.getSharedPreferences("pref", Context.MODE_PRIVATE);
		this.LOGIN_LANGUAGE = pref.getString("LOGIN_LANGUAGE","");
		this.Si = new SelectItems();
		this.tf = new TextFontUtil();

	}
	/**
	 * 그룹 포지션을 반환한다.
	 */
	@Override
	public SelectItems getGroup(int groupPosition)
	{
		return groupList.get(groupPosition);
	}
	/**
	 * 그룹 사이즈를 반환한다.
	 */
	@Override
   	public int getGroupCount() {
       return groupList.size();
   }
   	/**
	 * 그룹 ID를 반환한다.
	 */
	@Override
   	public long getGroupId(int groupPosition) {
       return groupPosition;
   }
   	/**
	 * 그룹뷰 각각의 ROW
	 */
	@Override
   	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
//		Log.e("getGroupView", "groupPosition : " + groupPosition);
		View view;
		if(convertView == null) {
			viewHolder = new ViewHolder();
			view = inflater.inflate(R.layout.general_training_row, parent, false);
			view.setTag(viewHolder);
		} else {
			view = convertView;
			viewHolder = (ViewHolder)view.getTag();
		}
		viewHolder.iv_bg = (ImageView)view.findViewById(R.id.trainings_row_iv);
		viewHolder.iv_square = (ImageView) view.findViewById(R.id.trainings_row_square_iv);
		viewHolder.tv_chapter = (TextView)view.findViewById(R.id.trainings_row_chapter_tv);
		data = getGroup(groupPosition);

		if(data != null){
			if(data.getChecked()) {
				viewHolder.iv_bg.setBackgroundResource(R.mipmap.personal_row_on);
//				viewHolder.iv_square.setBackgroundResource(R.mipmap.trainings_check_on);
			} else {
				viewHolder.iv_bg.setBackgroundResource(R.mipmap.personal_row_off);
//				viewHolder.iv_square.setBackgroundResource(R.mipmap.trainings_check_off);
			}
			if(data.getMChecked()) {
//				viewHolder.tv_chapter.setTextColor(Color.parseColor("#FFFFFF"));
				viewHolder.tv_chapter.setTextColor(Color.parseColor("#54C0DD"));
			} else {
//				viewHolder.tv_chapter.setTextColor(Color.parseColor("#BD5366"));;
				viewHolder.tv_chapter.setTextColor(Color.parseColor("#4B5366"));;
			}
			if("KR".equals(LOGIN_LANGUAGE))
				viewHolder.tv_chapter.setText(data.getKr());
			else
				viewHolder.tv_chapter.setText(data.getEng());
//			viewHolder.tv_chapter.setVisibility(View.INVISIBLE);
			tf.setNanumGothicExtraBold(Act, viewHolder.tv_chapter);
       	}
       	return view;
	}
   	/**
	 * 차일드뷰를 반환한다.
	 */
	@Override
 	public SelectItems getChild(int groupPosition, int childPosition) {
       	return childList.get(groupPosition).get(childPosition);
   	}
   	/**
	 * 차일드뷰 사이즈를 반환한다.
	 */
	@Override
   	public int getChildrenCount(int groupPosition) {
		return childList.get(groupPosition).size();
   	}
   	/**
	 * 차일드뷰 ID를 반환한다.
	 */
	@Override
	public long getChildId(int groupPosition, int childPosition) {
       return childPosition;
   }
	/**
	 * 차일드뷰 각각의 ROW
	 */
	@Override
	public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
//		Log.e("getChildView", groupPosition + "/" + childPosition);
		View view;
		if(convertView == null) {
			viewHolder = new ViewHolder();
			view = inflater.inflate(R.layout.general_training_crow, null);
			view.setTag(viewHolder);
		} else {
			view = convertView;
			viewHolder = (ViewHolder)view.getTag();
		}
		viewHolder.child = (RelativeLayout) view.findViewById(R.id.trainings_crow_rl);
		viewHolder.iv_child_bg = (ImageView)view.findViewById(R.id.trainings_crow_iv);
		viewHolder.tv_child_chapter = (TextView)view.findViewById(R.id.trainings_crow_chapter_tv2);
		cdata = getChild(groupPosition,childPosition);
//		Log.e("getChildView", "cdata getChecked : "+ cdata.getChecked());
		if(cdata != null){
			if(cdata.getChecked())
				viewHolder.iv_child_bg.setBackgroundResource(R.mipmap.personal_row_on);
			else
				viewHolder.iv_child_bg.setBackgroundResource(R.mipmap.personal_row_off);

			if(cdata.getMChecked())
				viewHolder.tv_child_chapter.setTextColor(Color.parseColor("#54C0DD"));
			else
				viewHolder.tv_child_chapter.setTextColor(Color.parseColor("#4B5366"));;
			if("KR".equals(LOGIN_LANGUAGE))
				viewHolder.tv_child_chapter.setText(cdata.getKr());
			else
				viewHolder.tv_child_chapter.setText(cdata.getEng());
//			viewHolder.tv_chapter.setVisibility(View.INVISIBLE);
			tf.setNanumGothicExtraBold(Act, viewHolder.tv_child_chapter);
		}
		return view;
   	}

	@Override
	public boolean hasStableIds() { return true; }

	@Override
   	public boolean isChildSelectable(int groupPosition, int childPosition) { return true; }
    
   	class ViewHolder {
		public ImageView iv_bg;
		public ImageView iv_square;
		public TextView tv_chapter;
		public RelativeLayout child;
		public ImageView iv_child_bg;
		public TextView tv_child_chapter;
	}
}