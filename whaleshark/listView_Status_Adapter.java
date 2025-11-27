package com.togetherseatech.whaleshark;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.togetherseatech.whaleshark.Db.Member.MemberInfo;
import com.togetherseatech.whaleshark.util.SelectItems;
import com.togetherseatech.whaleshark.util.TextFontUtil;

import java.util.List;


public class listView_Status_Adapter extends ArrayAdapter<MemberInfo>{

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
	public listView_Status_Adapter(Context context, int rowLayoutResource, List<MemberInfo> objects)
	{
		super(context, rowLayoutResource, objects);
		this.mContext = context;
		this.LayoutResource = rowLayoutResource;
		this.List = objects;
		this.Inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
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
	public View getView(final int position, View convertView, final ViewGroup parent)
	{
//		System.out.println("position : "+position);
		// convertView(View)가 없는 경우 inflater로 뷰 객체를 생성한다.

		if(convertView == null)
		{
			convertView = Inflater.inflate(LayoutResource, null);

			viewHolder = new ViewHolder();
			viewHolder.rl_image = (RelativeLayout) convertView.findViewById(R.id.member_row_rl);
			viewHolder.iv_image = (ImageView) convertView.findViewById(R.id.member_row_iv);
			viewHolder.tv_rank = (TextView)convertView.findViewById(R.id.member_row_rank_tv);
			viewHolder.tv_name = (TextView)convertView.findViewById(R.id.member_row_name_tv);

			convertView.setTag(viewHolder);
		}// 캐시된 뷰가 있을 경우 저장된 뷰홀더를 사용한다
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
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

			if(data.getChecked())
				viewHolder.rl_image.setBackgroundResource(R.mipmap.training_status_crew_on);
			else
				viewHolder.rl_image.setBackgroundResource(R.mipmap.training_status_crew_off);

			viewHolder.iv_image.setImageResource(Si.getMemberRankImg(data.getRank()));
			viewHolder.tv_rank.setText(Si.getMemberRank(data.getRank(), LOGIN_LANGUAGE));
			viewHolder.tv_name.setText(data.getSurName() + " " + data.getFirstName());

			tf.setNanumSquareL(mContext, viewHolder.tv_rank);
			tf.setNanumSquareL(mContext, viewHolder.tv_name);

		}
		
		// 만들어진 뷰(어댑터를 통해 아이템을 사용자에게 보여지도록 바꾼)를 반환한다.
		return convertView;
	}
	
	class ViewHolder {
		public RelativeLayout rl_image;
		public ImageView iv_image;
		public TextView tv_rank;
		public TextView tv_name;

	}
}
