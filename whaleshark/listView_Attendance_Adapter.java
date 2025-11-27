package com.togetherseatech.whaleshark;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.togetherseatech.whaleshark.Db.Member.MemberInfo;
import com.togetherseatech.whaleshark.util.SelectItems;
import com.togetherseatech.whaleshark.util.TextFontUtil;


public class listView_Attendance_Adapter extends ArrayAdapter<MemberInfo>{

	private Context mContext;
	private int LayoutResource;
	private List<MemberInfo> List;
	private LayoutInflater Inflater;
	private ViewHolder viewHolder = null;
	private String type = null;
	private SharedPreferences pref;
	private MemberInfo data;
	private String LOGIN_LANGUAGE;
	private SelectItems Si;
	/**
	 * ListViewCustomAdapter 생성자
	 * 
	 * @param context 컨텍스트
	 * @param rowLayoutResource 레이아웃리소스(일반적으로는 텍스트뷰 리소스:리스트는 기본적으로 텍스트를 뿌리도록 되어있다.)
	 * @param objects 입력된 리스트
	 */
	public listView_Attendance_Adapter(Context context, int rowLayoutResource, List<MemberInfo> objects) {
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
	public MemberInfo getItem(int position)
	{
		return List.get(position);
	}
	/**
	 * item(여기서는 Data)를 통해 리스트에서 해당 item의 position을 반환한다.
	 */
	@Override
	public int getPosition(MemberInfo item)
	{
		return List.indexOf(item);
	}
	/**
	 * 해당 position의 뷰를 만들어 반환한다.
	 */
	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {
//		Log.e("getView","position : "+position);
		viewHolder = new ViewHolder();
		if(convertView == null) {
			convertView = Inflater.inflate(LayoutResource, null);
		}
		// 리스트에서 포지션에 맞는 데이터를 받아온다.
		data = getItem(position);
		// 데이터가 있는 경우에만 처리한다.
		//Log.e("getView","data : "+ data);
		TextFontUtil tf = new TextFontUtil();
		LOGIN_LANGUAGE = pref.getString("LOGIN_LANGUAGE","");
		Si = new SelectItems();
		if(data != null) {
			viewHolder.iv_image = (ImageView) convertView.findViewById(R.id.member_row_iv);
			viewHolder.tv_rank = (TextView)convertView.findViewById(R.id.member_row_rank_tv);
			viewHolder.tv_name = (TextView)convertView.findViewById(R.id.member_row_name_tv);
			viewHolder.tv_birth = (TextView)convertView.findViewById(R.id.member_row_berth_tv);
			viewHolder.tv_national = (TextView) convertView.findViewById(R.id.member_row_national_tv);
			viewHolder.tv_signon = (TextView) convertView.findViewById(R.id.member_row_signon_tv);
			viewHolder.tb_bg = (ToggleButton) convertView.findViewById(R.id.trainings_row_tb);
			viewHolder.tb_bg.setTag(position);
			viewHolder.tb_bg.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
//					Log.e("CheckBox","Idx = " + v.getTag());
					if(!List.get((int)v.getTag()).getChecked()) {
						if(AttendanceAct.list.size() >= 10)
							Si.getMassage("ATTENDANCE10", LOGIN_LANGUAGE, mContext, Toast.LENGTH_SHORT);
						else {
							List.get((int) v.getTag()).setChecked(true);
							AttendanceAct.list.add(List.get((int)v.getTag()).getIdx()+"");
						}
					} else {
						List.get((int) v.getTag()).setChecked(false);
						AttendanceAct.list.remove(List.get((int)v.getTag()).getIdx()+"");
					}
					AttendanceAct.mAdapter.notifyDataSetChanged();
//					Log.e("CheckBox",AttendanceAct.list.size()+"");
//					for (int i = 0; i < AttendanceAct.list.size(); i++) {
//						Log.e("CheckBox", "IDX = " + AttendanceAct.list.get(i));
//					}
				}
			});
			if(data.getChecked())
				viewHolder.tb_bg.setBackgroundResource(R.mipmap.attendance_list_row_on);
			else
				viewHolder.tb_bg.setBackgroundResource(R.mipmap.attendance_list_row_off);
			// 어댑터에서의 findViewById() 메소드는 convertView 객체를 통해서 사용한다.
			// convertView를 통해 위젯을 레퍼런스 한다.
			viewHolder.iv_image.setImageResource(Si.getMemberRankImg(data.getRank()));
			viewHolder.tv_rank.setText(Si.getMemberRank(data.getRank(), LOGIN_LANGUAGE));
			viewHolder.tv_name.setText(data.getSurName() + " " + data.getFirstName());
			viewHolder.tv_birth.setText(data.getBirth());
			viewHolder.tv_national.setText(Si.getMemberNational(data.getNational(), LOGIN_LANGUAGE));
			viewHolder.tv_signon.setText(data.getSign_on());
			tf.setNanumSquareL(mContext, viewHolder.tv_national);
			tf.setNanumSquareL(mContext, viewHolder.tv_signon);
			tf.setNanumSquareL(mContext, viewHolder.tv_rank);
			tf.setNanumSquareL(mContext, viewHolder.tv_name);
			tf.setNanumSquareL(mContext, viewHolder.tv_birth);
		}
		// 만들어진 뷰(어댑터를 통해 아이템을 사용자에게 보여지도록 바꾼)를 반환한다.
		return convertView;
	}
	
	class ViewHolder {
		public ToggleButton tb_bg;
		public ImageView iv_image;
		public TextView tv_rank;
		public TextView tv_name;
		public TextView tv_birth;
		public TextView tv_national;
		public TextView tv_signon;
	}
}
