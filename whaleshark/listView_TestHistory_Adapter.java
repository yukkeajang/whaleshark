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

import com.togetherseatech.whaleshark.Db.History.TrainingInfo;
import com.togetherseatech.whaleshark.util.SelectItems;
import com.togetherseatech.whaleshark.util.TextFontUtil;

import java.util.List;

public class listView_TestHistory_Adapter extends ArrayAdapter<TrainingInfo>{

	private Context mContext;
	private int LayoutResource;
	private List<TrainingInfo> List;
	private LayoutInflater Inflater;
	private ViewHolder viewHolder = null;
	private SharedPreferences pref;
	private TrainingInfo data;
	private String LOGIN_LANGUAGE;
	private SelectItems Si;

	/**
	 * ListViewCustomAdapter 생성자
	 *
	 * @param context 컨텍스트
	 * @param rowLayoutResource 레이아웃리소스(일반적으로는 텍스트뷰 리소스:리스트는 기본적으로 텍스트를 뿌리도록 되어있다.)
	 * @param objects 입력된 리스트
	 */
	public listView_TestHistory_Adapter(Context context, int rowLayoutResource, List<TrainingInfo> objects)
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
		Si = new SelectItems();
		if(data != null)
		{
			// 어댑터에서의 findViewById() 메소드는 convertView 객체를 통해서 사용한다.
			// convertView를 통해 위젯을 레퍼런스 한다.
			viewHolder.rl_history = (RelativeLayout)convertView.findViewById(R.id.history_row_rl);
			viewHolder.iv_image = (ImageView) convertView.findViewById(R.id.history_row_iv);
			viewHolder.tv_rank = (TextView)convertView.findViewById(R.id.history_row_rank_tv);
			viewHolder.tv_name = (TextView)convertView.findViewById(R.id.history_row_name_tv);
//			viewHolder.tv_vsl = (TextView)convertView.findViewById(R.id.history_row_vsl_tv);
//			viewHolder.tv_vsl_type = (TextView)convertView.findViewById(R.id.history_row_vsltype_tv);
			viewHolder.tv_chapter = (TextView)convertView.findViewById(R.id.history_row_chapter_tv);
			viewHolder.tv_date = (TextView)convertView.findViewById(R.id.history_row_date_tv);
			viewHolder.tv_time = (TextView)convertView.findViewById(R.id.history_row_time_tv);
			viewHolder.tv_score = (TextView)convertView.findViewById(R.id.history_row_score_tv);
//			viewHolder.tv_signon = (TextView)convertView.findViewById(R.id.history_row_signon_tv);
//			viewHolder.tv_signoff = (TextView)convertView.findViewById(R.id.history_row_signoff_tv);
			viewHolder.bt_capture = (Button)convertView.findViewById(R.id.history_row_capture_bt);

			viewHolder.iv_image.setImageResource(Si.getMemberRankImg(data.getRank()));
			viewHolder.tv_rank.setText(Si.getMemberRank(data.getRank(), LOGIN_LANGUAGE));
			viewHolder.tv_name.setText(data.getSurName() + " " + data.getFirstName());
//			viewHolder.tv_vsl.setText(data.getVsl());
//			viewHolder.tv_vsl_type.setText(Si.getSelectMemberVslType(data.getVsl_type(), LOGIN_LANGUAGE));
//			viewHolder.tv_chapter.setText("Cargo Handing & Operation & Tank cleaning");
			viewHolder.tv_chapter.setText(Si.getChapter(data.getTraining_course(), LOGIN_LANGUAGE));
			viewHolder.tv_date.setText(data.getDate());
			viewHolder.tv_time.setText(data.getTime()+"s");
			viewHolder.tv_score.setText(data.getScore()+"");

//			viewHolder.tv_signon.setText(data.getSign_on());
			/*if(data.getSign_off() != null && !"".equals(data.getSign_off())){
				viewHolder.tv_signoff.setText(data.getSign_off());
			}else{
				viewHolder.tv_signoff.setText("-");
			}*/

			tf.setNanumSquareL(mContext, viewHolder.tv_rank);
			tf.setNanumSquareL(mContext, viewHolder.tv_name);
//			tf.setNanumSquareL(mContext, viewHolder.tv_vsl);
//			tf.setNanumSquareL(mContext, viewHolder.tv_vsl_type);
			tf.setNanumSquareL(mContext, viewHolder.tv_chapter);
			tf.setNanumSquareL(mContext, viewHolder.tv_date);
			tf.setNanumSquareL(mContext, viewHolder.tv_time);
			tf.setNanumSquareL(mContext, viewHolder.tv_score);
//			tf.setNanumSquareL(mContext, viewHolder.tv_signon);
//			tf.setNanumSquareL(mContext, viewHolder.tv_signoff);
			viewHolder.bt_capture.setTag(position+"");
			viewHolder.bt_capture.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					HistoryMainAct.getCapture(v.getTag().toString());
					Log.v("test","crew history : " + "adapter onclick");
				}
			});
		}else{

		}
		
		// 만들어진 뷰(어댑터를 통해 아이템을 사용자에게 보여지도록 바꾼)를 반환한다.
		return convertView;
	}
	
	class ViewHolder {
		public RelativeLayout rl_history;
		public ImageView iv_image;
		public TextView tv_rank;
		public TextView tv_name;
//		public TextView tv_vsl;
//		public TextView tv_vsl_type;
		public TextView tv_chapter;
		public TextView tv_date;
		public TextView tv_time;
		public TextView tv_score;
//		public TextView tv_signon;
//		public TextView tv_signoff;
		public Button bt_capture;
	}
}
