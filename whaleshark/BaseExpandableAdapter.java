package com.togetherseatech.whaleshark;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.togetherseatech.whaleshark.Db.History.TrainingInfo;
import com.togetherseatech.whaleshark.util.SelectItems;
import com.togetherseatech.whaleshark.util.TextFontUtil;

/**
 * BaseExpandableAdapter BaseExpandableListAdapter Class
 */
public class BaseExpandableAdapter extends BaseExpandableListAdapter{
    
	private ArrayList<TrainingInfo> groupList = null;
	private ArrayList<ArrayList<TrainingInfo>> childList_l = null;
	private ArrayList<ArrayList<String>> childList_r = null;
	private LayoutInflater inflater = null;
	private ViewHolder viewHolder = null;
	private Context exContext;
	private SharedPreferences pref;
	private TrainingInfo data;
	private String LOGIN_LANGUAGE;
	private SelectItems Si;
	private TextFontUtil tf;
//	private String type;

	/**
	 * 데이터 클래스 생성자
	 * @param c Context
	 * @param groupList ArrayList<String>
	 * @param childList_l ArrayList<ArrayList<String>>
	 * @param childList_r ArrayList<ArrayList<String>>
	 */
	public BaseExpandableAdapter(Context c, ArrayList<TrainingInfo> groupList,
          ArrayList<ArrayList<TrainingInfo>> childList_l, ArrayList<ArrayList<String>> childList_r) {
      	super();
      	this.inflater = LayoutInflater.from(c);
      	this.groupList = groupList;
      	this.childList_l = childList_l;
      	this.childList_r = childList_r;
		this.exContext = c;
		this.pref = c.getSharedPreferences("pref", Context.MODE_PRIVATE);
		this.Si = new SelectItems();
//		this.type = type;
	}
	

   
  
	/**
	 * 그룹 포지션을 반환한다.
	 */
	public TrainingInfo getGroup(int groupPosition) {
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
			viewHolder = new ViewHolder();
           	v = inflater.inflate(R.layout.training_status_group_row, parent, false);
			v.setTag(viewHolder);
			viewHolder.rl_member = (RelativeLayout) v.findViewById(R.id.status_row_rl);
			viewHolder.iv_image = (ImageView) v.findViewById(R.id.status_row_iv);
           	viewHolder.tv_rank = (TextView) v.findViewById(R.id.status_row_rank_tv);
           	viewHolder.tv_name = (TextView) v.findViewById(R.id.status_row_name_tv);
//           	viewHolder.tv_signon = (TextView) v.findViewById(R.id.status_row_signon_tv);
           	viewHolder.tv_date = (TextView) v.findViewById(R.id.status_row_lasttraining_tv);
			viewHolder.tv_ing = (TextView) v.findViewById(R.id.status_row_training_tv);

       	}else{
           viewHolder = (ViewHolder)v.getTag();
       	}
        
       	// 그룹을 펼칠때와 닫을때 아이콘을 변경해 준다.
       if(isExpanded){
		   RelativeLayout.LayoutParams plControl = (RelativeLayout.LayoutParams) viewHolder.rl_member.getLayoutParams();
		   plControl.bottomMargin = 0;
		   viewHolder.rl_member.setLayoutParams(plControl);
       }else{
		   RelativeLayout.LayoutParams plControl = (RelativeLayout.LayoutParams) viewHolder.rl_member.getLayoutParams();
		   plControl.bottomMargin = 10;
		   viewHolder.rl_member.setLayoutParams(plControl);
       }

		data = getGroup(groupPosition);
		tf = new TextFontUtil();
		LOGIN_LANGUAGE = pref.getString("LOGIN_LANGUAGE","");

		if(data != null){
			viewHolder.iv_image.setImageResource(Si.getMemberRankImg(data.getRank()));
			viewHolder.tv_rank.setText(Si.getMemberRank(data.getRank(), LOGIN_LANGUAGE));
			viewHolder.tv_name.setText(data.getSurName() + " " + data.getFirstName());
//			viewHolder.tv_signon.setText(data.getSign_on());
			viewHolder.tv_date.setText(data.getDate());
			viewHolder.tv_ing.setText(data.getIng());

			tf.setNanumSquareL(exContext, viewHolder.tv_rank);
			tf.setNanumSquareL(exContext, viewHolder.tv_name);
//			tf.setNanumSquareL(exContext, viewHolder.tv_signon);
			tf.setNanumSquareL(exContext, viewHolder.tv_date);
			tf.setNanumSquareL(exContext, viewHolder.tv_ing);
       	}
    	   
       	return v;
	}
    
   	/**
	 * 차일드뷰를 반환한다.
	 */
 	public String getChild(int groupPosition, int childPosition) {
       	return childList_r.get(groupPosition).get(childPosition);
   	}
   
   	/**
	 * 차일드뷰 사이즈를 반환한다.
	 */
   	public int getChildrenCount(int groupPosition) {
//	   System.out.println("getChildrenCount : "+groupPosition);
//       return childList_up.get(groupPosition).size();
		return 1;
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
	public View getChildView(int groupPosition, int childPosition,
		boolean isLastChild, View convertView, ViewGroup parent) {
//	   System.out.println("getChildView : "+groupPosition+" "+childPosition);
//	   System.out.println("isLastChild : "+isLastChild);
		View v = convertView;
		if(v == null){
    	   	System.out.println("v == null");
			viewHolder = new ViewHolder();
		   	v = inflater.inflate(R.layout.training_status_child_row, null);
		   	v.setTag(viewHolder);
		   	v.setId(0);

		   	viewHolder.child_History = (Button) v.findViewById(R.id.status_history_bt);
			viewHolder.child_History.setTag(groupPosition+"");
			viewHolder.child_History.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					AdminTrainingStatusAct_.goHistory(v.getTag().toString());
				}
			});
		   	viewHolder.child_LlLeft = (LinearLayout) v.findViewById(R.id.status_childl_ll);
			viewHolder.child_LlRight = (LinearLayout) v.findViewById(R.id.status_childr_ll);

			/*int count = Math.abs(childList_l.get(groupPosition).size() - childList_r.get(groupPosition).size());

			for(int i=0; i < count; i++){
				if(childList_l.get(groupPosition).size() > childList_r.get(groupPosition).size()) {
					childList_r.get(groupPosition).add("");
				}else{
					childList_l.get(groupPosition).add(new TrainingInfo(-1,""));
				}
			}*/
			viewHolder.iv_child_in_imageL = new ImageView[11];
			viewHolder.tv_child_in_chapterL = new TextView[11];
			viewHolder.tv_child_in_dateL = new TextView[11];
			viewHolder.Lv = new View[11];
			viewHolder.Rv = new View[11];

			for (int i = 0; i < 11; i++) {
				viewHolder.Lv[i] = inflater.inflate(R.layout.training_status_child_in_row, null);
				viewHolder.iv_child_in_imageL[i] = (ImageView) viewHolder.Lv[i].findViewById(R.id.child_in_iv);
				viewHolder.tv_child_in_chapterL[i] = (TextView) viewHolder.Lv[i].findViewById(R.id.child_in_chapter_tv);
				viewHolder.tv_child_in_dateL[i] = (TextView) viewHolder.Lv[i].findViewById(R.id.child_in_date_tv);
				tf.setNanumSquareL(exContext, viewHolder.tv_child_in_chapterL[i]);
				tf.setNanumSquareL(exContext, viewHolder.tv_child_in_dateL[i]);

				viewHolder.child_LlLeft.addView(viewHolder.Lv[i]);
			}

			viewHolder.tv_child_in_dateR = new TextView[11];

			for(int i=0; i < 11; i++) {
				viewHolder.Rv[i] = inflater.inflate(R.layout.training_status_child_in_row, null);
				viewHolder.iv_child_in_imageR = (ImageView) viewHolder.Rv[i].findViewById(R.id.child_in_iv);
				viewHolder.tv_child_in_chapterR = (TextView) viewHolder.Rv[i].findViewById(R.id.child_in_chapter_tv);
				viewHolder.tv_child_in_dateR[i] = (TextView) viewHolder.Rv[i].findViewById(R.id.child_in_date_tv);
				viewHolder.iv_child_in_imageR.setVisibility(View.INVISIBLE);
				viewHolder.tv_child_in_chapterR.setVisibility(View.GONE);
				tf.setNanumSquareL(exContext, viewHolder.tv_child_in_dateR[i]);
				viewHolder.child_LlRight.addView(viewHolder.Rv[i]);
			}

		}else{
//    	   System.out.println("v != null");
			viewHolder = (ViewHolder)v.getTag();
		}




//		Log.e("getChildView ","childList_l S : "+childList_l.get(groupPosition).size());
		if(childList_l.get(groupPosition).size() > 0) {
			for (int i = 0; i < childList_l.get(groupPosition).size(); i++) {
//				Log.e("TrainingStatusAct","Li : "+ i);
//				Log.e("getChildView LEFT",childList_l.get(groupPosition).get(i).getTraining_course()+"");
				if(childList_l.get(groupPosition).get(i).getTraining_course() > -1 ){
					viewHolder.iv_child_in_imageL[i].setVisibility(View.VISIBLE);
					viewHolder.tv_child_in_chapterL[i].setVisibility(View.VISIBLE);
					viewHolder.tv_child_in_dateL[i].setVisibility(View.VISIBLE);
					viewHolder.tv_child_in_chapterL[i].setText(Si.getChapter(childList_l.get(groupPosition).get(i).getTraining_course(), LOGIN_LANGUAGE));
					viewHolder.tv_child_in_dateL[i].setText(childList_l.get(groupPosition).get(i).getDate());
				}else{
					viewHolder.iv_child_in_imageL[i].setVisibility(View.INVISIBLE);
					viewHolder.tv_child_in_chapterL[i].setText("");
					viewHolder.tv_child_in_dateL[i].setText("");
				}
			}

			for(int i= childList_l.get(groupPosition).size(); i < 11; i++) {
//				Log.e("TrainingStatusAct","Li : "+ i);
				viewHolder.Lv[i].setVisibility(View.GONE);
			}
		}

//		Log.e("getChildView ","childList_r S : "+childList_r.get(groupPosition).size());
		if(childList_r.get(groupPosition).size() > 0) {
			for(int i=0; i < childList_r.get(groupPosition).size(); i++){
//				Log.e("TrainingStatusAct","Ri : "+ i);
//				Log.e("getChildView RIGHT",childList_r.get(groupPosition).get(i));
				if(!"".equals(childList_r.get(groupPosition).get(i).trim()))
					viewHolder.tv_child_in_dateR[i].setText(childList_r.get(groupPosition).get(i));
				else
					viewHolder.tv_child_in_dateR[i].setText("");
			}

			for(int i= childList_r.get(groupPosition).size(); i < 11; i++) {
//				Log.e("TrainingStatusAct","Ri : "+ i);
				viewHolder.Rv[i].setVisibility(View.GONE);
			}
		}

		return v;
   	}

	public boolean hasStableIds() { return true; }

   	public boolean isChildSelectable(int groupPosition, int childPosition) { return true; }
    
   	class ViewHolder {
		public View Lv[];
		public View Rv[];
		public RelativeLayout rl_member;
		public ImageView iv_image;

		public TextView tv_rank;
		public TextView tv_name;
//		public TextView tv_signon;
		public TextView tv_ing;
		public TextView tv_date;

		public LinearLayout child_LlLeft;
		public LinearLayout child_LlRight;
		public Button child_History;

		public ImageView iv_child_in_imageL[], iv_child_in_imageR;
		public TextView tv_child_in_chapterL[], tv_child_in_chapterR;
		public TextView tv_child_in_dateL[], tv_child_in_dateR[];

	}

}