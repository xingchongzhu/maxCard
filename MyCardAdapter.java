package com.maxcard.contact.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.maxcard.contact.R;
import com.maxcard.contact.common.StaticSetting;
import com.maxcard.contact.dataManager.StateManager;
import com.maxcard.contact.fragment.AddContactFragment;
import com.maxcard.contact.model.CardModel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

/**
 * @Description:用来处理集合中数据的显示与排序
 * @author http://blog.csdn.net/finddreams
 */
@SuppressLint("DefaultLocale")
public class MyCardAdapter extends BaseAdapter implements SectionIndexer {

	//private Drawable icon = null;
	@SuppressWarnings("rawtypes")
	//private Map map = new HashMap();
	private String sortList[] = {"BGLOGO","NAME","NUMBER",
			"QQ","EMAIL","COMPANY","DEPARTMENT","POSITION","FAX","TEL","URL","ADDRESS"};
	private int titleId [] ={R.string.delete,R.string.name,R.string.number,R.string.qq,R.string.email,R.string.company,R.string.department
			,R.string.position,R.string.fax,R.string.tle,R.string.url,R.string.address};
	private List<String> titleList = new ArrayList<String>();
	private Context mContext;
	private CardModel myinfo;
	public MyCardAdapter(Context mContext, CardModel myinfo) {
		this.mContext = mContext;
		this.myinfo = myinfo;
		initData(myinfo);
	}
	private void initData(CardModel myinfo){
		sortList[1] = myinfo.getmSortModel().getName();
		sortList[2] = myinfo.getNumber();
		sortList[3] = myinfo.getQQ();
		sortList[4] = myinfo.getEmail();
		sortList[5] = myinfo.getCompany();
		sortList[6] = myinfo.getDepartment();
		sortList[7] = myinfo.getPosition();
		sortList[8] = myinfo.getFax();
		sortList[9] = myinfo.getTel();
		sortList[10] = myinfo.getUrl();
		sortList[11] = myinfo.getAddress();
		for(int i = 0;i < titleId.length;i++){
			titleList.add(mContext.getResources().getString(titleId[i]));
		}
	}
	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * 
	 * @param list
	 */
	public void updateInfoView(CardModel myinfo) {
		if (myinfo != null) {
			notifyDataSetChanged();
		}
	}

	public int getCount() {
		if (myinfo != null) {
			return sortList.length;
		} else {
			return 0;
		}
	}

	public CardModel getItem(String key) {
		if (myinfo != null) {
			return myinfo;
		} else {
			return null;
		}
	}

	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("null")
	@SuppressLint("InflateParams")
	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolderTitle viewHolder = null;
		ViewHolderContext mViewHolderContex = null;

		if (view == null) {
			if(position == 0 ){
				viewHolder = new ViewHolderTitle();
				view = LayoutInflater.from(mContext).inflate(R.layout.my_card_top_title, null);
				viewHolder.bg = (LinearLayout)view.findViewById(R.id.bg);
				viewHolder.words = (TextView)view.findViewById(R.id.word);
				viewHolder.icon = (ImageView)view.findViewById(R.id.icon);
				viewHolder.edit = (ImageView)view.findViewById(R.id.edit);
				view.setTag(viewHolder);
			}else {
				mViewHolderContex = new ViewHolderContext();
				view = LayoutInflater.from(mContext).inflate(R.layout.list_item, null);
				mViewHolderContex.title = (TextView)view.findViewById(R.id.title);
				mViewHolderContex.context = (TextView)view.findViewById(R.id.context);
				view.setTag(mViewHolderContex);
			}
		}else {
			if(position == 0 ){
				viewHolder = (ViewHolderTitle)view.getTag();
			}else{
				mViewHolderContex = (ViewHolderContext)view.getTag();
			}
		}
		if(position == 0){
			if(myinfo.getBgImage() != null){
				viewHolder.bg.setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), myinfo.getBgImage()));
			}
			if(myinfo.getPersonBitmap() != null){
				viewHolder.icon.setImageBitmap(toRoundBitmap(myinfo.getPersonBitmap()));
			}else{
				viewHolder.icon.setImageBitmap(toRoundBitmap(drawableToBitamp(mContext.getResources().getDrawable(R.drawable.icon))));
			}
			if(myinfo.getWords() != null && !myinfo.getWords().equals("")){
				viewHolder.words.setText(myinfo.getWords());
			}else{
				viewHolder.words.setText("你的力量，超乎你的想象。。。");
			}
			viewHolder.edit.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					AddContactFragment edit = new AddContactFragment();
					Bundle args = new Bundle();
					args.putBoolean("change", true);
					args.putString(StaticSetting.INTENT_CONTACT_ID_FLAG,
							myinfo.getContactId());
					edit.setArguments(args); // FragmentActivity将点击的菜单列表标题传递给Fragment
					StateManager.getInstance().startStateForResult(edit);
				}
				
			});
			
		}else{
			mViewHolderContex.title.setText(titleList.get(position));
			mViewHolderContex.context.setText(sortList[position]);
		}
		return view;
	}

	private Bitmap drawableToBitamp(Drawable drawable) {
		Bitmap bitmap = null;
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565;
		bitmap = Bitmap.createBitmap(w, h, config);
		// 注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		drawable.draw(canvas);
		return bitmap;
	}
	
	final static class ViewHolderTitle {
		LinearLayout bg;
		TextView words;
		ImageView icon;
		ImageView edit;
	}
	
	final static class ViewHolderContext {
		TextView title;
		TextView context;
	}

	/**
	 * 转换图片成圆形
	 * 
	 * @param bitmap
	 *            传入Bitmap对象
	 * @return
	 */
	public Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2 - 5;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2 - 5;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst_left + 15, dst_top + 15,
				dst_right - 20, dst_bottom - 20);

		paint.setAntiAlias(true);

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);

		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}

	@Override
	public Object[] getSections() {
		return null;
	}

	@Override
	public int getSectionForPosition(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPositionForSection(int sectionIndex) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}
	
}