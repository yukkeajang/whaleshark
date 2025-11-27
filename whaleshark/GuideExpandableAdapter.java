package com.togetherseatech.whaleshark;

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
import android.widget.Toast;

import com.togetherseatech.whaleshark.Db.History.TrainingInfo;
import com.togetherseatech.whaleshark.util.SelectItems;
import com.togetherseatech.whaleshark.util.TextFontUtil;

import java.util.ArrayList;
import java.util.Map;

/**
 * BaseExpandableAdapter GuideExpandableAdapter Class
 */
public class GuideExpandableAdapter extends BaseExpandableListAdapter{

	private ArrayList<Map<String,String>> groupList = null;
	private ArrayList<ArrayList<Map<String,String>>> childList = null;
	private LayoutInflater inflater = null;
	private ViewHolder viewHolder = null;
	private Context exContext;
	private SharedPreferences pref;
	private Map<String,String> dataG , dataC;
	private SelectItems Si;
	private TextFontUtil tf;
//	private String type;

	/**
	 * 데이터 클래스 생성자
	 * @param c Context
	 * @param groupList ArrayList<Map<String,String>>
	 * @param childList ArrayList<ArrayList<Map<String,String>>>
	 */
	public GuideExpandableAdapter(Context c, ArrayList<Map<String,String>> groupList, ArrayList<ArrayList<Map<String,String>>> childList) {
      	super();
      	this.inflater = LayoutInflater.from(c);
      	this.groupList = groupList;
      	this.childList = childList;
		this.exContext = c;
		this.Si = new SelectItems();
		tf = new TextFontUtil();
//		this.type = type;
	}
	
	/**
	 * 그룹 포지션을 반환한다.
	 */
	public Map<String,String> getGroup(int groupPosition) {
		return groupList.get(groupPosition);
   	}

   	/**
	 * 그룹 사이즈를 반환한다.
	 */
   	public int getGroupCount() {
       return groupList.size();
   }

   	/**
	 * 그룹 ID를 반환한다.
	 */
   	public long getGroupId(int groupPosition) {
       return groupPosition;
   }

   	/**
	 * 그룹뷰 각각의 ROW
	 */
   	public View getGroupView(int groupPosition, boolean isExpanded,
		View convertView, ViewGroup parent) {
       	View v = convertView;

		if(v == null){
           	v = inflater.inflate(R.layout.guide_group_row, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.rl_group = (RelativeLayout) v.findViewById(R.id.guide_group_rl);
			viewHolder.tv_group_num = (TextView) v.findViewById(R.id.guide_group_num_tv);
			viewHolder.tv_group_kr = (TextView) v.findViewById(R.id.guide_group_kr_tv);
			viewHolder.tv_group_eng = (TextView) v.findViewById(R.id.guide_group_eng_tv);
			v.setTag(viewHolder);
			v.setId(groupPosition);
			if(groupPosition == 0)
				v.setBackgroundColor(Color.parseColor("#E4E7EA"));
       	}else{
           viewHolder = (ViewHolder)v.getTag();
       	}
        
       	// 그룹을 펼칠때와 닫을때 아이콘을 변경해 준다.
       /*if(isExpanded){
		   RelativeLayout.LayoutParams plControl = (RelativeLayout.LayoutParams) viewHolder.rl_member.getLayoutParams();
		   plControl.bottomMargin = 0;
		   viewHolder.rl_member.setLayoutParams(plControl);
       }else{
		   RelativeLayout.LayoutParams plControl = (RelativeLayout.LayoutParams) viewHolder.rl_member.getLayoutParams();
		   plControl.bottomMargin = 10;
		   viewHolder.rl_member.setLayoutParams(plControl);
       }*/

		dataG = getGroup(groupPosition);

		if(dataG != null){

			if("true".equals(dataG.get("check"))) {
				viewHolder.rl_group.setBackgroundColor(Color.parseColor("#E4E7EA"));
			} else {
				viewHolder.rl_group.setBackgroundColor(Color.parseColor("#FFFFFF"));
			}

			viewHolder.tv_group_num.setText(dataG.get("num"));
			viewHolder.tv_group_kr.setText(dataG.get("kr"));
			viewHolder.tv_group_eng.setText(dataG.get("eng"));

			tf.setNanumSquareB(exContext, viewHolder.tv_group_num);
			tf.setNanumSquareB(exContext, viewHolder.tv_group_kr);
			tf.setNanumSquareB(exContext, viewHolder.tv_group_eng);
       	}
    	   
       	return v;
	}
    
   	/**
	 * 차일드뷰를 반환한다.
	 */
 	public Map<String,String> getChild(int groupPosition, int childPosition) {
       	return childList.get(groupPosition).get(childPosition);
   	}
   
   	/**
	 * 차일드뷰 사이즈를 반환한다.
	 */
   	public int getChildrenCount(int groupPosition) {
//	   System.out.println("getChildrenCount : "+groupPosition);
//       return childList_up.get(groupPosition).size();
		return childList.get(groupPosition).size();
   	}

   	/**
	 * 차일드뷰 ID를 반환한다.
	 */
	public long getChildId(int groupPosition, int childPosition) {
       return childPosition;
   }

	/**
	 * 차일드뷰 각각의 ROW
	 */
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
//	   System.out.println("getChildView : "+groupPosition+" "+childPosition);
//	   System.out.println("isLastChild : "+isLastChild);
		View v = convertView;
		if(v == null){
    	   	System.out.println("v == null");
		   	v = inflater.inflate(R.layout.guide_child_row, null);
			viewHolder = new ViewHolder();
			viewHolder.rl_child = (RelativeLayout) v.findViewById(R.id.guide_child_rl);
			viewHolder.tv_child_num = (TextView) v.findViewById(R.id.guide_child_num_tv);
			viewHolder.tv_child_kr = (TextView) v.findViewById(R.id.guide_child_kr_tv);
			viewHolder.tv_child_eng = (TextView) v.findViewById(R.id.guide_child_eng_tv);
		   	v.setTag(viewHolder);
		   	v.setId(Integer.valueOf(""+groupPosition+childPosition));

		}else{
//    	   System.out.println("v != null");
			viewHolder = (ViewHolder)v.getTag();
		}
//		Log.e("getChildView","v.getId() : "+v.getId());
//        Log.e("getChildView",childList.get(groupPosition).get(childPosition) + "/" + groupPosition + "/" +childPosition);
		dataC = getChild(groupPosition, childPosition);
//		if(dataC != null)
//			Log.e("getChildView",dataC.get("num") + "/" + dataC.get("kr") + "/" + dataC.get("eng"));



		/*v.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				Log.e("getChildView","v.getId() : "+v.getId());
				if(Integer.valueOf(v.getId()) == 30)
					GuideAct.Guide_Iv.setBackgroundResource(R.mipmap.guid_4_1);
				else if(Integer.valueOf(v.getId()) == 31)
					GuideAct.Guide_Iv.setBackgroundResource(R.mipmap.guid_4_2);
				else if(Integer.valueOf(v.getId()) == 32)
					GuideAct.Guide_Iv.setBackgroundResource(R.mipmap.guid_4_3);
				else if(Integer.valueOf(v.getId()) == 33)
					GuideAct.Guide_Iv.setBackgroundResource(R.mipmap.guid_4_4);
				else if(Integer.valueOf(v.getId()) == 34)
					GuideAct.Guide_Iv.setBackgroundResource(R.mipmap.guid_4_5);
				else if(Integer.valueOf(v.getId()) == 35)
					GuideAct.Guide_Iv.setBackgroundResource(R.mipmap.guid_4_6);
				else if(Integer.valueOf(v.getId()) == 36)
					GuideAct.Guide_Iv.setBackgroundResource(R.mipmap.guid_4_7);
				else if(Integer.valueOf(v.getId()) == 40)
					GuideAct.Guide_Iv.setBackgroundResource(R.mipmap.guid_5_1);
				else if(Integer.valueOf(v.getId()) == 41)
					GuideAct.Guide_Iv.setBackgroundResource(R.mipmap.guid_5_2);
				else if(Integer.valueOf(v.getId()) == 42)
					GuideAct.Guide_Iv.setBackgroundResource(R.mipmap.guid_5_3);
				else if(Integer.valueOf(v.getId()) == 43)
					GuideAct.Guide_Iv.setBackgroundResource(R.mipmap.guid_5_4);
			}
		});*/



		if(dataC != null){

			if("true".equals(dataC.get("check"))) {
				viewHolder.rl_child.setBackgroundColor(Color.parseColor("#E4E7EA"));
			} else {
				viewHolder.rl_child.setBackgroundColor(Color.parseColor("#FFFFFF"));
			}

//			viewHolder.rl_child.setVisibility(View.INVISIBLE);
			viewHolder.tv_child_num.setText(dataC.get("num"));
			viewHolder.tv_child_kr.setText(dataC.get("kr"));
			viewHolder.tv_child_eng.setText(dataC.get("eng"));

			tf.setNanumSquareB(exContext, viewHolder.tv_child_num);
			tf.setNanumSquareB(exContext, viewHolder.tv_child_kr);
			tf.setNanumSquareB(exContext, viewHolder.tv_child_eng);
		}else{
			v.setVisibility(View.GONE);
		}

		return v;
   	}

	public boolean hasStableIds() { return true; }

   	public boolean isChildSelectable(int groupPosition, int childPosition) { return true; }

	public static class ViewHolder {
		public RelativeLayout rl_group;
		public TextView tv_group_num;
		public TextView tv_group_kr;
		public TextView tv_group_eng;
		public RelativeLayout rl_child;
		public TextView tv_child_num;
		public TextView tv_child_kr;
		public TextView tv_child_eng;
	}

}