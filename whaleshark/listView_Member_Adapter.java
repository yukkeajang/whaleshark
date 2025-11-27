package com.togetherseatech.whaleshark;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.togetherseatech.whaleshark.util.CommonUtil;
import com.togetherseatech.whaleshark.util.SelectItems;
import com.togetherseatech.whaleshark.util.TextFontUtil;

import java.util.List;


public class listView_Member_Adapter extends ArrayAdapter<MemberInfo>{

	private Activity mAct;
	private int LayoutResource;
	private List<MemberInfo> List;
	private LayoutInflater Inflater;
	private ViewHolder viewHolder = null;
	private SharedPreferences pref;
	private MemberInfo data;
	private String LOGIN_LANGUAGE;
	private SelectItems Si;

	/**
	 * ListViewCustomAdapter 생성자
	 *
	 * @param mAct Activity
	 * @param rowLayoutResource 레이아웃리소스(일반적으로는 텍스트뷰 리소스:리스트는 기본적으로 텍스트를 뿌리도록 되어있다.)
	 * @param objects 입력된 리스트
	 */
	public listView_Member_Adapter(Activity mAct, int rowLayoutResource, List<MemberInfo> objects)
	{
		super(mAct, rowLayoutResource, objects);
		this.mAct = mAct;
		this.LayoutResource = rowLayoutResource;
		this.List = objects;
		this.Inflater = (LayoutInflater)mAct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.pref = mAct.getSharedPreferences("pref", Context.MODE_PRIVATE);
		Si = new SelectItems();
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
	public View getView(int position, View convertView, final ViewGroup parent)
	{
//		System.out.println("position : "+position);
		// convertView(View)가 없는 경우 inflater로 뷰 객체를 생성한다.
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
		if(data != null)
		{
			// 어댑터에서의 findViewById() 메소드는 convertView 객체를 통해서 사용한다.
			// convertView를 통해 위젯을 레퍼런스 한다.
//			Log.e("getView","data.getRank() : " + data.getRank());
			viewHolder.iv_image = (ImageView) convertView.findViewById(R.id.member_row_iv);
			viewHolder.tv_rank = (TextView)convertView.findViewById(R.id.member_row_rank_tv);
			viewHolder.tv_name = (TextView)convertView.findViewById(R.id.member_row_name_tv);
			viewHolder.tv_birth = (TextView)convertView.findViewById(R.id.member_row_berth_tv);
			viewHolder.tv_national = (TextView) convertView.findViewById(R.id.member_row_national_tv);
			viewHolder.tv_signon = (TextView) convertView.findViewById(R.id.member_row_signon_tv);
//			viewHolder.tv_vsl = (TextView)convertView.findViewById(R.id.member_row_vsl_tv);
//			viewHolder.tv_vsl_type = (TextView)convertView.findViewById(R.id.member_row_vsltype_tv);
			viewHolder.bt_delete = (Button)convertView.findViewById(R.id.member_row_delete_bt);
			viewHolder.bt_delete.setTag(position);
			viewHolder.bt_pencil = (Button)convertView.findViewById(R.id.member_row_pencil_bt);
			viewHolder.bt_pencil.setTag(position);
			viewHolder.tv_signoff = (TextView)convertView.findViewById(R.id.member_row_signoff_tv);

			viewHolder.iv_image.setImageResource(Si.getMemberRankImg(data.getRank()));
			viewHolder.tv_rank.setText(Si.getMemberRank(data.getRank(), LOGIN_LANGUAGE));
//			viewHolder.tv_rank.setText(Si.getMemberRank(data.getRank(), LOGIN_LANGUAGE) + "/" + data.getMaster_idx());
			viewHolder.tv_name.setText(data.getSurName() + " " + data.getFirstName());
			viewHolder.tv_birth.setText(data.getBirth());
			viewHolder.tv_national.setText(Si.getMemberNational(data.getNational(), LOGIN_LANGUAGE));
			viewHolder.tv_signon.setText(data.getSign_on());
//			viewHolder.tv_vsl.setText(data.getVsl());
//			viewHolder.tv_vsl_type.setText(Si.getSelectMemberVslType(data.getVsl_type(), LOGIN_LANGUAGE));
			viewHolder.bt_delete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					new TwoButtonPopUpDialog(mAct, List.get((int)v.getTag()), "DEL").show();
				}
			});
			viewHolder.bt_delete.setVisibility(View.INVISIBLE);


			viewHolder.bt_pencil.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					new CommonUtil().setHideDialog(new AdminMemberPopUpDialog(mAct, List.get((int)v.getTag())));
				}
			});


			if(data.getSign_off() != null && !"".equals(data.getSign_off())){
				viewHolder.tv_signoff.setText(data.getSign_off());
				viewHolder.bt_delete.setVisibility(View.VISIBLE);
			}else{
				viewHolder.tv_signoff.setText(null);
				viewHolder.bt_delete.setVisibility(View.INVISIBLE);
			}

			tf.setNanumSquareL(mAct.getApplicationContext(), viewHolder.tv_national);
			tf.setNanumSquareL(mAct.getApplicationContext(), viewHolder.tv_signon);
			tf.setNanumSquareL(mAct.getApplicationContext(), viewHolder.tv_rank);
			tf.setNanumSquareL(mAct.getApplicationContext(), viewHolder.tv_name);
			tf.setNanumSquareL(mAct.getApplicationContext(), viewHolder.tv_birth);
//			tf.setNanumSquareL(mContext, viewHolder.tv_vsl);
//			tf.setNanumSquareL(mContext, viewHolder.tv_vsl_type);
			tf.setNanumSquareL(mAct.getApplicationContext(), viewHolder.tv_signoff);
		}
		
		// 만들어진 뷰(어댑터를 통해 아이템을 사용자에게 보여지도록 바꾼)를 반환한다.
		return convertView;
	}
	
	class ViewHolder {
		public ImageView iv_image;
		public TextView tv_rank;
//		public TextView tv_vsl;
		public TextView tv_vsl_type;
		public TextView tv_name;
		public TextView tv_birth;
		public TextView tv_national;
		public TextView tv_signon;
		public TextView tv_signoff;
		public Button bt_pencil;
		public Button bt_delete;

	}
}
