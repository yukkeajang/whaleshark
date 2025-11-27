package com.togetherseatech.whaleshark;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.togetherseatech.whaleshark.Db.History.TrainingInfo;
import com.togetherseatech.whaleshark.Db.Member.MemberInfo;
import com.togetherseatech.whaleshark.util.SelectItems;
import com.togetherseatech.whaleshark.util.TextFontUtil;

import java.util.List;

public class listView_Update_Adapter extends ArrayAdapter<TrainingInfo>{

	private Context mContext;
	private int LayoutResource;
	private List<TrainingInfo> List;
	private LayoutInflater Inflater;
	private ViewHolder viewHolder = null;
	private SharedPreferences pref;
	private TrainingInfo data;
	private String LOGIN_LANGUAGE;
	/**
	 * ListViewCustomAdapter 생성자
	 *
	 * @param context 컨텍스트
	 * @param rowLayoutResource 레이아웃리소스(일반적으로는 텍스트뷰 리소스:리스트는 기본적으로 텍스트를 뿌리도록 되어있다.)
	 * @param objects 입력된 리스트
	 */
	public listView_Update_Adapter(Context context, int rowLayoutResource, List<TrainingInfo> objects)
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
	public TrainingInfo getItem(int position)
	{
		return List.get(position);
	}

	/**
	 * item(여기서는 Data)를 통해 리스트에서 해당 item의 position을 반환한다.
	 */
	@Override
	public int getPosition(TrainingInfo item)
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
		SelectItems Si = new SelectItems();
		if(data != null)
		{
			// 어댑터에서의 findViewById() 메소드는 convertView 객체를 통해서 사용한다.
			// convertView를 통해 위젯을 레퍼런스 한다.

			/*viewHolder.tv_nodata = (TextView)convertView.findViewById(R.id.nodata_tv);
			viewHolder.tv_nodata.setVisibility(View.INVISIBLE);
//			viewHolder.rl_member = (RelativeLayout)convertView.findViewById(R.id.member_row_rl);
			viewHolder.rl_member.setVisibility(View.VISIBLE);*/
			viewHolder.rl_member = (RelativeLayout)convertView.findViewById(R.id.member_row_rl);
//			Log.e("getSign_off","getSign_off : "+data.getSign_off());
			if(getItem(position).getSign_off() != null)
				viewHolder.rl_member.setBackgroundResource(R.mipmap.list_row_lrbg_off);
			else
				viewHolder.rl_member.setBackgroundResource(R.mipmap.list_row_lbg_off);

			viewHolder.iv_image = (ImageView) convertView.findViewById(R.id.member_row_iv);
			viewHolder.tv_rank = (TextView)convertView.findViewById(R.id.member_row_rank_tv);
			viewHolder.tv_name = (TextView)convertView.findViewById(R.id.member_row_name_tv);
//			viewHolder.tv_birth = (TextView)convertView.findViewById(R.id.member_row_birth_tv);

//			Log.e("listView_Update_Adapter","tv_rank : "+data.getRank());
//			Log.e("listView_Update_Adapter","tv_name : "+data.getSurName() + " " + data.getFirstName());
//			Log.e("listView_Update_Adapter","tv_birth : "+data.getBirth());

			Log.e("test","Udate User : " + "Rank : " + data.getRank() + ")" +
					"Name : " + data.getFirstName()+data.getSurName() +
					"+" + "TRAINING COURSE :" + data.getTraining_course() + ")" + "COMPLETED DATE : " + data.getDate() + ")" +"SCORE : " + data.getScore());
			viewHolder.iv_image.setImageResource(Si.getMemberRankImg(data.getRank()));
			viewHolder.tv_rank.setText(Si.getMemberRank(data.getRank(), LOGIN_LANGUAGE));
			viewHolder.tv_name.setText(data.getSurName() + " " + data.getFirstName());
//			viewHolder.tv_birth.setText(data.getBirth());

			tf.setNanumSquareL(mContext, viewHolder.tv_rank);
			tf.setNanumSquareL(mContext, viewHolder.tv_name);
//			tf.setNanumSquareL(mContext, viewHolder.tv_birth);

			if(!"T".equals(data.getType())) {
				viewHolder.iv_image.setVisibility(View.INVISIBLE);
				viewHolder.tv_rank.setVisibility(View.INVISIBLE);
			}else{
				viewHolder.iv_image.setVisibility(View.VISIBLE);
				viewHolder.tv_rank.setVisibility(View.VISIBLE);
			}

			/*viewHolder.tv_vsl = (TextView)convertView.findViewById(R.id.member_row_vsl_tv);
			viewHolder.tv_vsl_type = (TextView)convertView.findViewById(R.id.member_row_vsltype_tv);
			viewHolder.tv_vsl.setText(data.getVsl());
			viewHolder.tv_vsl_type.setText(Si.getSelectMemberVslType(data.getVsl_type(), LOGIN_LANGUAGE));
			tf.setNanumSquareL(mContext, viewHolder.tv_vsl);
			tf.setNanumSquareL(mContext, viewHolder.tv_vsl_type);
			viewHolder.tv_signoff = (TextView)convertView.findViewById(R.id.member_row_signoff_tv);
			if(data.getSign_off() != null && !"".equals(data.getSign_off())){
				viewHolder.tv_signoff.setText(data.getSign_off());
			}else{
				viewHolder.tv_signoff.setText(null);
			}
			tf.setNanumSquareL(mContext, viewHolder.tv_signoff);
			viewHolder.tb_member = (ToggleButton)convertView.findViewById(R.id.member_row_tb);
			viewHolder.tb_member.setTag(position+"");
			viewHolder.tb_member.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					Log.e("tb_member","Sign_off : "+getItem(position).getSign_off());
					if(isChecked){
						AdminUpdateAct.position = position;
						buttonView.setBackgroundResource(R.mipmap.list_row_lbg_on);
						bt = (Button)parent.getChildAt(position).findViewById(R.id.member_row_done_bt);
						bt.setVisibility(View.VISIBLE);
						for(int i=0; i < parent.getChildCount(); i++){
							if(i != Integer.valueOf(buttonView.getTag().toString()))	{
								parent.getChildAt(i).findViewById(R.id.member_row_tb).setBackgroundResource(R.mipmap.list_row_lbg_off);
								bt = (Button)parent.getChildAt(i).findViewById(R.id.member_row_done_bt);
								bt.setVisibility(View.INVISIBLE);
								tb = (ToggleButton)parent.getChildAt(i).findViewById(R.id.member_row_tb);
								tb.setChecked(false);
							}
						}
					}else{
						for(int i=0; i < parent.getChildCount(); i++){
							tb = (ToggleButton)parent.getChildAt(i).findViewById(R.id.member_row_tb);
							if(tb.isChecked()){
								AdminUpdateAct.position = i;
								return;
							}else{
								AdminUpdateAct.position = -1;
							}
						}
						buttonView.setBackgroundResource(R.mipmap.list_row_lbg_off);
						bt = (Button)parent.getChildAt(position).findViewById(R.id.member_row_done_bt);
						bt.setVisibility(View.INVISIBLE);
					}
				}
			});*/
			viewHolder.tv_chapter = (TextView)convertView.findViewById(R.id.member_row_chapter_tv);
			viewHolder.tv_date = (TextView)convertView.findViewById(R.id.member_row_date_tv);
			viewHolder.tv_score = (TextView)convertView.findViewById(R.id.member_row_score_tv);

			tf.setNanumSquareL(mContext, viewHolder.tv_chapter);
			tf.setNanumSquareL(mContext, viewHolder.tv_date);
			tf.setNanumSquareL(mContext, viewHolder.tv_score);
//            Log.e("listView_Update_Adapter","getType : "+data.getType());
			if("R".equals(data.getType())) {
				viewHolder.tv_chapter.setText(Si.getNewRegulation(data.getTraining_course(), LOGIN_LANGUAGE));
			}else if("G".equals(data.getType()))
				viewHolder.tv_chapter.setText(Si.getGeneralTraining(data.getTraining_course(),data.getTraining_course2(),LOGIN_LANGUAGE));
			else
				viewHolder.tv_chapter.setText(Si.getChapter(data.getTraining_course(), LOGIN_LANGUAGE));

			viewHolder.tv_date.setText(data.getDate());
//			Log.e("tv_score","tv_score : "+data.getScore());
			viewHolder.tv_score.setText(data.getScore()+"");


		}else{
			/*viewHolder.rl_member = (RelativeLayout)convertView.findViewById(R.id.member_row_rl);
			viewHolder.rl_member.setVisibility(View.INVISIBLE);
			viewHolder.tv_nodata = (TextView)convertView.findViewById(R.id.nodata_tv);
			tf.setNanumSquareL(mContext, viewHolder.tv_nodata);
			viewHolder.tv_nodata.setVisibility(View.VISIBLE);*/
		}
		
		// 만들어진 뷰(어댑터를 통해 아이템을 사용자에게 보여지도록 바꾼)를 반환한다.
		return convertView;
	}
	
	class ViewHolder {
		public RelativeLayout rl_member;
		public ImageView iv_image;
//		public ToggleButton tb_member;
		public TextView tv_rank;
		public TextView tv_vsl;
		public TextView tv_vsl_type;
		public TextView tv_name;
//		public TextView tv_birth;
		public TextView tv_national;
		public TextView tv_signon;
		public TextView tv_signoff;
		public Button bt_pencil;
		public Button bt_delete;
		public TextView tv_nodata;
		public TextView tv_chapter;
		public TextView tv_date;
		public TextView tv_score;
	}
}
