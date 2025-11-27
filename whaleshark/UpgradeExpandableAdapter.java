package com.togetherseatech.whaleshark;

import android.app.Activity;
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
import com.togetherseatech.whaleshark.Db.Problem.ProblemInfo;
import com.togetherseatech.whaleshark.util.SelectItems;
import com.togetherseatech.whaleshark.util.TextFontUtil;

import java.util.ArrayList;

/**
 * BaseExpandableAdapter UpgradeExpandableAdapter Class
 */
public class UpgradeExpandableAdapter extends BaseExpandableListAdapter{

	private ArrayList<String> groupList = null;
	private ArrayList<ArrayList<UpgradeInfo>> childList = null;
	private ArrayList<UpgradeInfo> childListContentQ = null;

	private LayoutInflater inflater = null;
	private ViewHolder viewHolder = null;
	private Activity Act;
	private SharedPreferences pref;
	private String data;
	private String LOGIN_LANGUAGE;
	private SelectItems Si;
	private TextFontUtil tf;
//	private String type;

	/**
	 * 데이터 클래스 생성자
	 * @param act Activity
	 * @param groupList ArrayList<String>
	 * @param childList ArrayList<ArrayList<UpgradeInfo>>
	 * @param childListContentQ ArrayList<UpgradeInfo>
	 */
	public UpgradeExpandableAdapter(Activity act, ArrayList<String> groupList, ArrayList<ArrayList<UpgradeInfo>> childList, ArrayList<UpgradeInfo> childListContentQ) {
      	super();
      	this.inflater = LayoutInflater.from(act);
      	this.groupList = groupList;
      	this.childList = childList;
      	this.childListContentQ = childListContentQ;
		this.Act = act;
		this.pref = act.getSharedPreferences("pref", Context.MODE_PRIVATE);
		LOGIN_LANGUAGE = pref.getString("LOGIN_LANGUAGE","");
		this.Si = new SelectItems();
	}

	/**
	 * 그룹 포지션을 반환한다.
	 */
	public String getGroup(int groupPosition) {
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
		View view;
		if(convertView == null) {
			viewHolder = new ViewHolder();
			view = inflater.inflate(R.layout.admin_upgrade_group_row, parent, false);
			view.setTag(viewHolder);
			viewHolder.rl_member = (RelativeLayout) view.findViewById(R.id.upgrade_row_rl);
			viewHolder.tv_title = (TextView) view.findViewById(R.id.upgrade_row_title_tv);
		} else {
			view = convertView;
			viewHolder = (ViewHolder)view.getTag();

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
			viewHolder.tv_title.setText(data);
			tf.setNanumSquareL(Act, viewHolder.tv_title);
       	}
    	   
       	return view;
	}
    
   	/**
	 * 차일드뷰를 반환한다.
	 */
 	public UpgradeInfo getChild(int groupPosition, int childPosition) {
       	return childList.get(groupPosition).get(childPosition);
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
	public View getChildView(final int groupPosition, int childPosition,
							 boolean isLastChild, View convertView, ViewGroup parent) {
//	   System.out.println("getChildView : "+groupPosition+" "+childPosition);
//	   System.out.println("isLastChild : "+isLastChild);
		View view;
		if(convertView == null) {
			System.out.println("v == null");
			viewHolder = new ViewHolder();
			view = inflater.inflate(R.layout.admin_upgrade_child_row, null);
			view.setTag(viewHolder);
			view.setId(0);
		} else {
			view = convertView;
			viewHolder = (ViewHolder)view.getTag();
		}


		viewHolder.child = (LinearLayout) view.findViewById(R.id.upgrade_child_ll);

		/*int count = Math.abs(childList_l.get(groupPosition).size() - childList_r.get(groupPosition).size());

		for(int i=0; i < count; i++){
			if(childList_l.get(groupPosition).size() > childList_r.get(groupPosition).size()) {
				childList_r.get(groupPosition).add("");
			}else{
				childList_l.get(groupPosition).add(new TrainingInfo(-1,""));
			}
		}*/
//        Log.e("getChildView", "childList : " + childList.size());

		if(childList.size() > 0) {
			int cnt = childList.get(groupPosition).size();
//			Log.e("getChildView", "childList : " + cnt);
			viewHolder.child.removeAllViews();
			viewHolder.tv_child_in_menu = new TextView[(cnt + 1)];
			viewHolder.tv_child_in_filename = new TextView[cnt + 1];
			viewHolder.tv_child_in_filesize = new TextView[cnt + 1];
			viewHolder.bt_child_in_download = new Button[cnt + 1];
			viewHolder.Vv = new View[cnt + 1];

			for (int i = 0; i < cnt; i++) {
//			if(!Si.hasObbPrivateFile(exContext, childList.get(groupPosition).get(i).getContent_kr())){
				viewHolder.Vv[i] = inflater.inflate(R.layout.admin_upgrade_child_in_row, null);
				viewHolder.tv_child_in_menu[i] = (TextView) viewHolder.Vv[i].findViewById(R.id.child_in_menu_tv);
				viewHolder.tv_child_in_filename[i] = (TextView) viewHolder.Vv[i].findViewById(R.id.child_in_filename_tv);
				viewHolder.tv_child_in_filesize[i] = (TextView) viewHolder.Vv[i].findViewById(R.id.child_in_filesize_tv);
				viewHolder.bt_child_in_download[i] = (Button) viewHolder.Vv[i].findViewById(R.id.child_in_download_bt);
				viewHolder.bt_child_in_download[i].setTag(i + "");
				viewHolder.bt_child_in_download[i].setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						int positoin = Integer.valueOf(v.getTag().toString());
//					Log.e("bt_child_in_download","bt_child_in_download : "+ childList.get(groupPosition).get(positoin).getTitle_kr());
						ArrayList<UpgradeInfo> arrayPi = new ArrayList<>();
						arrayPi.add(childList.get(groupPosition).get(positoin));
						new TwoButtonPopUpDialog(Act, arrayPi, "UPGRADE").show();
					}
				});
				viewHolder.tv_child_in_menu[i].setText(childList.get(groupPosition).get(i).getMenu());
				viewHolder.tv_child_in_filename[i].setText(LOGIN_LANGUAGE.equals("KR") ? childList.get(groupPosition).get(i).getFile_title_kr() : childList.get(groupPosition).get(i).getFile_title_eng());
				viewHolder.tv_child_in_filesize[i].setText(childList.get(groupPosition).get(i).getFile_size());
				tf.setNanumSquareL(Act, viewHolder.tv_child_in_menu[i]);
				tf.setNanumSquareL(Act, viewHolder.tv_child_in_filename[i]);
				tf.setNanumSquareL(Act, viewHolder.tv_child_in_filesize[i]);

				viewHolder.child.addView(viewHolder.Vv[i]);
//				Log.e("getChildView", "cnt : " + i);
//			}
			}

			if (childListContentQ != null & childListContentQ.size() > 0) {
				viewHolder.Vv[cnt] = inflater.inflate(R.layout.admin_upgrade_child_in_row, null);
				viewHolder.tv_child_in_menu[cnt] = (TextView) viewHolder.Vv[cnt].findViewById(R.id.child_in_menu_tv);
				viewHolder.tv_child_in_filename[cnt] = (TextView) viewHolder.Vv[cnt].findViewById(R.id.child_in_filename_tv);
				viewHolder.tv_child_in_filesize[cnt] = (TextView) viewHolder.Vv[cnt].findViewById(R.id.child_in_filesize_tv);
				viewHolder.bt_child_in_download[cnt] = (Button) viewHolder.Vv[cnt].findViewById(R.id.child_in_download_bt);
				viewHolder.bt_child_in_download[cnt].setTag(cnt + "");
				viewHolder.bt_child_in_download[cnt].setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						int positoin = Integer.valueOf(v.getTag().toString());
//					Log.e("bt_child_in_download","bt_child_in_download : "+ childList.get(groupPosition).get(positoin).getTitle_kr());
						new TwoButtonPopUpDialog(Act, childListContentQ, "UPGRADE").show();
					}
				});
				viewHolder.tv_child_in_menu[cnt].setText(childListContentQ.get(0).getMenu());
				viewHolder.tv_child_in_filename[cnt].setText("All Media Files");
				viewHolder.tv_child_in_filesize[cnt].setText(childListContentQ.get(0).getFile_size());
				tf.setNanumSquareL(Act, viewHolder.tv_child_in_menu[cnt]);
				tf.setNanumSquareL(Act, viewHolder.tv_child_in_filename[cnt]);
				tf.setNanumSquareL(Act, viewHolder.tv_child_in_filesize[cnt]);

				viewHolder.child.addView(viewHolder.Vv[cnt]);
			}

		} else {
			if (childListContentQ != null & childListContentQ.size() > 0) {
				viewHolder.child.removeAllViews();
				viewHolder.tv_child_in_menu = new TextView[1];
				viewHolder.tv_child_in_filename = new TextView[1];
				viewHolder.tv_child_in_filesize = new TextView[1];
				viewHolder.bt_child_in_download = new Button[1];
				viewHolder.Vv = new View[1];

				viewHolder.Vv[0] = inflater.inflate(R.layout.admin_upgrade_child_in_row, null);
				viewHolder.tv_child_in_menu[0] = (TextView) viewHolder.Vv[0].findViewById(R.id.child_in_menu_tv);
				viewHolder.tv_child_in_filename[0] = (TextView) viewHolder.Vv[0].findViewById(R.id.child_in_filename_tv);
				viewHolder.tv_child_in_filesize[0] = (TextView) viewHolder.Vv[0].findViewById(R.id.child_in_filesize_tv);
				viewHolder.bt_child_in_download[0] = (Button) viewHolder.Vv[0].findViewById(R.id.child_in_download_bt);
				viewHolder.bt_child_in_download[0].setTag(0 + "");
				viewHolder.bt_child_in_download[0].setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						int positoin = Integer.valueOf(v.getTag().toString());
//					Log.e("bt_child_in_download","bt_child_in_download : "+ childList.get(groupPosition).get(positoin).getTitle_kr());
						new TwoButtonPopUpDialog(Act, childListContentQ, "UPGRADE").show();
					}
				});
				viewHolder.tv_child_in_menu[0].setText(childListContentQ.get(0).getMenu());
				viewHolder.tv_child_in_filename[0].setText("All Media Files");
				viewHolder.tv_child_in_filesize[0].setText(childListContentQ.get(0).getFile_size());
				tf.setNanumSquareL(Act, viewHolder.tv_child_in_menu[0]);
				tf.setNanumSquareL(Act, viewHolder.tv_child_in_filename[0]);
				tf.setNanumSquareL(Act, viewHolder.tv_child_in_filesize[0]);

				viewHolder.child.addView(viewHolder.Vv[0]);
			}
		}
		return view;
   	}

	public boolean hasStableIds() { return true; }

   	public boolean isChildSelectable(int groupPosition, int childPosition) { return true; }
    
   	class ViewHolder {
		public View Vv[];
		public RelativeLayout rl_member;
		public TextView tv_title;
		public LinearLayout child;

		public TextView tv_child_in_menu[];
		public TextView tv_child_in_filename[];
		public TextView tv_child_in_filesize[];
		public Button bt_child_in_download[];

	}

}