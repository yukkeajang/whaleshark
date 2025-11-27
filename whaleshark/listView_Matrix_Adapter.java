package com.togetherseatech.whaleshark;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.togetherseatech.whaleshark.Db.Member.MemberInfo;
import com.togetherseatech.whaleshark.Db.Training.TrainingsInfo;
import com.togetherseatech.whaleshark.util.SelectItems;
import com.togetherseatech.whaleshark.util.TextFontUtil;

import java.util.ArrayList;
import java.util.List;


public class listView_Matrix_Adapter extends ArrayAdapter<TrainingsInfo> {

	private Context mContext;
	private int LayoutResource;
	private List<TrainingsInfo> List;
	private LayoutInflater Inflater;
	private ViewHolder viewHolder = null;
	private SharedPreferences pref;
	private TrainingsInfo data;
	private String LOGIN_LANGUAGE;
	private SelectItems Si;
	private int p = 0;
	private boolean check = true;
	/**
	 * ListViewCustomAdapter 생성자
	 *
	 * @param context 컨텍스트
	 * @param rowLayoutResource 레이아웃리소스(일반적으로는 텍스트뷰 리소스:리스트는 기본적으로 텍스트를 뿌리도록 되어있다.)
	 * @param objects 입력된 리스트
	 */
	public listView_Matrix_Adapter(Context context, int rowLayoutResource, List<TrainingsInfo> objects)
	{
		super(context, rowLayoutResource, objects);
		this.mContext = context;
		this.LayoutResource = rowLayoutResource;
		this.List = objects;
		this.Inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
		Si = new SelectItems();
	}

	public void setList(List<TrainingsInfo> objects) {
		this.List = objects;
	}

	public List<TrainingsInfo> getList() {
		return this.List;
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
	public TrainingsInfo getItem(int position)
	{
		return List.get(position);
	}

	/**
	 * item(여기서는 Data)를 통해 리스트에서 해당 item의 position을 반환한다.
	 */
	@Override
	public int getPosition(TrainingsInfo item)
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
//		Log.e("getView","position : "+ position);
		if(convertView == null)
		{
			convertView = Inflater.inflate(LayoutResource, null);

			viewHolder = new ViewHolder();
			viewHolder.rl_row = (RelativeLayout) convertView.findViewById(R.id.matrix_trainings_row_rl);
			viewHolder.tv_chapter = (TextView)convertView.findViewById(R.id.matrix_trainings_row_chapter_tv);
			viewHolder.cb_ns = (CheckBox)convertView.findViewById(R.id.matrix_trainings_row_ns_cb);
			viewHolder.cb_nj = (CheckBox)convertView.findViewById(R.id.matrix_trainings_row_nj_cb);
			viewHolder.cb_nr = (CheckBox)convertView.findViewById(R.id.matrix_trainings_row_nr_cb);
			viewHolder.cb_es = (CheckBox)convertView.findViewById(R.id.matrix_trainings_row_es_cb);
			viewHolder.cb_ej = (CheckBox)convertView.findViewById(R.id.matrix_trainings_row_ej_cb);
			viewHolder.cb_er = (CheckBox)convertView.findViewById(R.id.matrix_trainings_row_er_cb);
			viewHolder.cb_or = (CheckBox)convertView.findViewById(R.id.matrix_trainings_row_or_cb);

			viewHolder.cb_ns.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.e("getView","cb_ns : "+ ((CheckBox)v).getTag()+"/"+((CheckBox)v).isChecked());
					check = ((CheckBox)v).isChecked();
					if(AdminMatrixAct.editCheck == false) {
						((CheckBox) v).setChecked(!check);
					} else {
						p = Integer.valueOf(((CheckBox)v).getTag().toString());
						List.get(p).setNavigation_senior(check == true ? 1 : 0);
					}
					Log.e("getView","cb_ns : "+ List.get(p).getNavigation_senior());
				}
			});

			viewHolder.cb_nj.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
//					Log.e("getView","cb_nj : "+ ((CheckBox)v).getTag()+"/"+((CheckBox)v).isChecked());
					check = ((CheckBox)v).isChecked();
					if(AdminMatrixAct.editCheck == false) {
						((CheckBox) v).setChecked(!check);
					} else {
                        p = Integer.valueOf(((CheckBox)v).getTag().toString());
						List.get(p).setNavigation_junior(check == true ? 1 : 0);
					}
//					Log.e("getView","cb_nj : "+ List.get(p).getNavigation_junior());
				}
			});

			viewHolder.cb_nr.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
//					Log.e("getView","cb_nr : "+ ((CheckBox)v).getTag()+"/"+((CheckBox)v).isChecked());
					check = ((CheckBox)v).isChecked();
					if(AdminMatrixAct.editCheck == false) {
						((CheckBox) v).setChecked(!check);
					} else {
                        p = Integer.valueOf(((CheckBox)v).getTag().toString());
						List.get(p).setNavigation_rating(check == true ? 1 : 0);
					}
//					Log.e("getView","cb_nr : "+ List.get(p).getNavigation_rating());
				}
			});

			viewHolder.cb_es.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
//					Log.e("getView","cb_es : "+ ((CheckBox)v).getTag()+"/"+((CheckBox)v).isChecked());
					check = ((CheckBox)v).isChecked();
					if(AdminMatrixAct.editCheck == false) {
						((CheckBox) v).setChecked(!check);
					} else {
                        p = Integer.valueOf(((CheckBox)v).getTag().toString());
						List.get(p).setEngine_senior(check == true ? 1 : 0);
					}
//					Log.e("getView","cb_es : "+ List.get(p).getEngine_senior());
				}
			});

			viewHolder.cb_ej.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
//					Log.e("getView","cb_ej : "+ ((CheckBox)v).getTag()+"/"+((CheckBox)v).isChecked());
					check = ((CheckBox)v).isChecked();
					if(AdminMatrixAct.editCheck == false) {
						((CheckBox) v).setChecked(!check);
					} else {
                        p = Integer.valueOf(((CheckBox)v).getTag().toString());
						List.get(p).setEngine_junior(check == true ? 1 : 0);
					}
//					Log.e("getView","cb_ej : "+ List.get(p).getEngine_junior());
				}
			});

			viewHolder.cb_er.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
//					Log.e("getView","cb_er : "+ ((CheckBox)v).getTag()+"/"+((CheckBox)v).isChecked());
					check = ((CheckBox)v).isChecked();
					if(AdminMatrixAct.editCheck == false) {
						((CheckBox) v).setChecked(!check);
					} else {
                        p = Integer.valueOf(((CheckBox)v).getTag().toString());
						List.get(p).setEngine_rating(check == true ? 1 : 0);
					}
//					Log.e("getView","cb_er : "+ List.get(p).getEngine_rating());
				}
			});

			viewHolder.cb_or.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
//					Log.e("getView","cb_or : "+ ((CheckBox)v).getTag()+"/"+((CheckBox)v).isChecked());
					check = ((CheckBox)v).isChecked();
					if(AdminMatrixAct.editCheck == false) {
						((CheckBox) v).setChecked(!check);
					} else {
                        p = Integer.valueOf(((CheckBox)v).getTag().toString());
						List.get(p).setOthers_rating(check == true ? 1 : 0);
					}
//					Log.e("getView","cb_or : "+ List.get(p).getOthers_rating());
				}
			});
			convertView.setTag(viewHolder);
		}// 캐시된 뷰가 있을 경우 저장된 뷰홀더를 사용한다
		else
		{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
		// 리스트에서 포지션에 맞는 데이터를 받아온다.
		data = getItem(position);
		// 데이터가 있는 경우에만 처리한다.
//		Log.e("getView","data : "+ data);
		TextFontUtil tf = new TextFontUtil();
		LOGIN_LANGUAGE = pref.getString("LOGIN_LANGUAGE","");
		if(data != null)
		{
			// 어댑터에서의 findViewById() 메소드는 convertView 객체를 통해서 사용한다.
			// convertView를 통해 위젯을 레퍼런스 한다.
			viewHolder.tv_chapter.setText("G".equals(data.getType()) ?
					Si.getGeneralTraining(data.getTraining_course(),data.getTraining_course2(),LOGIN_LANGUAGE)
					: Si.getNewRegulation(data.getTraining_course() - 1,LOGIN_LANGUAGE));
			tf.setNanumSquareRoundL(mContext, viewHolder.tv_chapter);

			Log.e("test","chapter :" + Si.getGeneralTraining(data.getTraining_course(),data.getTraining_course2(),LOGIN_LANGUAGE));

			viewHolder.cb_ns.setChecked(data.getNavigation_senior() > 0 ? true : false);
			viewHolder.cb_nj.setChecked(data.getNavigation_junior() > 0 ? true : false);
			viewHolder.cb_nr.setChecked(data.getNavigation_rating() > 0 ? true : false);
			viewHolder.cb_es.setChecked(data.getEngine_senior() > 0 ? true : false);
			viewHolder.cb_ej.setChecked(data.getEngine_junior() > 0 ? true : false);
			viewHolder.cb_er.setChecked(data.getEngine_rating() > 0 ? true : false);
			viewHolder.cb_or.setChecked(data.getOthers_rating() > 0 ? true : false);

			viewHolder.cb_ns.setTag(position);
			viewHolder.cb_nj.setTag(position);
			viewHolder.cb_nr.setTag(position);
			viewHolder.cb_es.setTag(position);
			viewHolder.cb_ej.setTag(position);
			viewHolder.cb_er.setTag(position);
			viewHolder.cb_or.setTag(position);
//			Log.e("getView","data : "+ data.getNavigation_senior()+"/"+data.getNavigation_junior()+"/"+data.getNavigation_rating()+"/"+data.getEngine_senior()+"/"+data.getEngine_junior()+"/"+data.getEngine_rating()+"/"+data.getOthers_rating());
//			Log.e("getView","tv_chapter : "+ viewHolder.tv_chapter.getText());
		}
		
		// 만들어진 뷰(어댑터를 통해 아이템을 사용자에게 보여지도록 바꾼)를 반환한다.
		return convertView;
	}

	class ViewHolder {
		public RelativeLayout rl_row;
		public TextView tv_chapter;
		public CheckBox cb_ns;
		public CheckBox cb_nj;
		public CheckBox cb_nr;
		public CheckBox cb_es;
		public CheckBox cb_ej;
		public CheckBox cb_er;
		public CheckBox cb_or;
	}
}
