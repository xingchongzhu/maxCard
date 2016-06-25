package com.maxcard.contact.adapter;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import com.maxcard.contact.R;
import com.maxcard.contact.common.MaxCard;
import com.maxcard.contact.common.StaticMethod;
import com.maxcard.contact.model.CardModel;
import com.maxcard.contact.sortlist.SideBar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.text.GetChars;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;
/**
 * @Description:用来处理集合中数据的显示与排序
 * @author http://blog.csdn.net/finddreams
 */ 
@SuppressLint("DefaultLocale")
public class SortAdapter extends BaseAdapter implements SectionIndexer{
	private List<CardModel> list = null;
	private Context mContext;
	private ImageView maxdialog;
	Resources mResources;
	String context = "";
	@SuppressWarnings("unused")
	private final static String TAG = "SortAdapter";
	@SuppressWarnings("unused")
	private int srceenWide = 0;
	@SuppressWarnings("unused")
	private int srceenheight = 0;
	private int maxSize = 0;
	private boolean isShowBar = false;
	private boolean isShowLetter = false;
	private int letterCounter[];
	private ViewHolder viewHolder = null;
	private ViewHolder viewHolder1 = null;
	private ViewHolder viewHolder2 = null;
	private ViewHolder viewHolder3 = null;
	private ViewHolder viewHolder4 = null;
	private ViewHolder viewHolder5 = null;
	private ViewHolder viewHolder6 = null;
	private ViewHolder viewHolder7 = null;
	private ViewHolder viewHolder8 = null;
	private ViewHolder viewHolder9 = null;
	private ViewHolder viewHolder10 = null;
	private ViewHolder viewHolder11 = null;
	private List<String> arrlist = Arrays.asList(SideBar.b);
	public SortAdapter(Context mContext, List<CardModel> list,ImageView maxdialog,int srceenWide,int srceenheight) {
		this.mContext = mContext;
		this.list = list;
		this.maxdialog = maxdialog;
		this.srceenheight = srceenheight;
		this.srceenWide = srceenWide;
		if(srceenheight == 0 || srceenWide == 0){
			this.srceenheight = 400;
			this.srceenWide = 400;
		}
		maxSize = srceenWide/5*4;
		//Log.d("thismylog", "SortAdapter maxSize = "+maxSize);
		mResources = mContext.getResources(); 
	}
	
	public void setLetterCounter(int letterCounter[]){
		this.letterCounter = letterCounter;
	}
	public void setIsShowBar(boolean isShowBar){
		this.isShowBar = isShowBar;
	}
	public void setIsShowLetter(boolean isShowLetter){
		this.isShowLetter = isShowLetter;
	}
	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * @param list
	 */
	public void updateListView(List<CardModel> list){
		if(list != null){
			this.list = list;
			notifyDataSetChanged();
		}
	}
	public void updateListView(){
		notifyDataSetChanged();
	}

	public List<CardModel> getList(){
		return list;
	}
	public int getCount() {
		if(list != null){
			return this.list.size();
		}else {
			return 0;
		}
	}

	public Object getItem(int position) {
		if(list != null){
		    return list.get(position);
		}else {
			return null;
		}
	}

	public long getItemId(int position) {
		return position;
	}
	
	@SuppressWarnings("null")
	@SuppressLint("InflateParams")
	public View getView(final int position, View view, ViewGroup arg2) {
		
		if(list != null && list.size() > 0){
			final CardModel mContent = list.get(position);
			//Log.d(TAG, "getView mContent.getType() = "+mContent.getType());
			// Log.d(TAG, "getView name = "+mContent.getmSortModel().getName()+" type = "+mContent.getType());
			if(view == null){
				// 按当前所需的样式，确定new的布局 
	            switch (mContent.getType()) { 
	            case 0:
	            	viewHolder = new ViewHolder();
					view = LayoutInflater.from(mContext).inflate(R.layout.phone_constacts_item, null);
//					if(!isShowBar){
//						viewHolder.mLinearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
//						viewHolder.mLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(srceenWide/2, srceenWide/3));
//					}
					viewHolder.name = (TextView) view.findViewById(R.id.name);
					viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
					viewHolder.phone_number = (TextView) view.findViewById(R.id.phone_number);
					viewHolder.email_address = (TextView) view.findViewById(R.id.email_address);
					viewHolder.qq_number = (TextView) view.findViewById(R.id.qq_number);
					viewHolder.icon = (ImageView) view.findViewById(R.id.icon);
					viewHolder.select = (ImageView) view.findViewById(R.id.select);
					view.setTag(viewHolder);
	            	break;
	            case 1:
	            	viewHolder1 = new ViewHolder();
					view = LayoutInflater.from(mContext).inflate(R.layout.phone_constacts_item01, null);
//					if(!isShowBar){
//						viewHolder1.mLinearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
//						viewHolder1.mLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(srceenWide/2, srceenWide/3));
//					}
					viewHolder1.name = (TextView) view.findViewById(R.id.name);
					viewHolder1.tvLetter = (TextView) view.findViewById(R.id.catalog);
					viewHolder1.phone_number = (TextView) view.findViewById(R.id.phone_number);
					viewHolder1.email_address = (TextView) view.findViewById(R.id.email_address);
					viewHolder1.qq_number = (TextView) view.findViewById(R.id.qq_number);
					viewHolder1.icon = (ImageView) view.findViewById(R.id.icon);
					viewHolder1.select = (ImageView) view.findViewById(R.id.select);
					view.setTag(viewHolder1);
	            	break;
	            case 2:
	            	viewHolder2 = new ViewHolder();
					view = LayoutInflater.from(mContext).inflate(R.layout.phone_constacts_item02, null);
//					if(!isShowBar){
//						viewHolder2.mLinearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
//						viewHolder2.mLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(srceenWide/2, srceenWide/3));
//					}
					viewHolder2.name = (TextView) view.findViewById(R.id.name);
					viewHolder2.tvLetter = (TextView) view.findViewById(R.id.catalog);
					viewHolder2.phone_number = (TextView) view.findViewById(R.id.phone_number);
					viewHolder2.email_address = (TextView) view.findViewById(R.id.email_address);
					viewHolder2.qq_number = (TextView) view.findViewById(R.id.qq_number);
					viewHolder2.icon = (ImageView) view.findViewById(R.id.icon);
					viewHolder2.select = (ImageView) view.findViewById(R.id.select);
					view.setTag(viewHolder2);
	            	break;
	            case 3:
	            	viewHolder3 = new ViewHolder();
					view = LayoutInflater.from(mContext).inflate(R.layout.phone_constacts_item03, null);
//					if(!isShowBar){
//						viewHolder3.mLinearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
//						viewHolder3.mLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(srceenWide/2, srceenWide/3));
//					}
					viewHolder3.name = (TextView) view.findViewById(R.id.name);
					viewHolder3.tvLetter = (TextView) view.findViewById(R.id.catalog);
					viewHolder3.phone_number = (TextView) view.findViewById(R.id.phone_number);
					viewHolder3.email_address = (TextView) view.findViewById(R.id.email_address);
					viewHolder3.qq_number = (TextView) view.findViewById(R.id.qq_number);
					viewHolder3.icon = (ImageView) view.findViewById(R.id.icon);
					viewHolder3.select = (ImageView) view.findViewById(R.id.select);
					view.setTag(viewHolder3);
	            	break;
	            case 4:
	            	viewHolder4 = new ViewHolder();
					view = LayoutInflater.from(mContext).inflate(R.layout.phone_constacts_item04, null);
//					if(!isShowBar){
//						viewHolder4.mLinearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
//						viewHolder4.mLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(srceenWide/2, srceenWide/3));
//					}
					viewHolder4.name = (TextView) view.findViewById(R.id.name);
					viewHolder4.tvLetter = (TextView) view.findViewById(R.id.catalog);
					viewHolder4.phone_number = (TextView) view.findViewById(R.id.phone_number);
					viewHolder4.email_address = (TextView) view.findViewById(R.id.email_address);
					viewHolder4.qq_number = (TextView) view.findViewById(R.id.qq_number);
					viewHolder4.icon = (ImageView) view.findViewById(R.id.icon);
					viewHolder4.select = (ImageView) view.findViewById(R.id.select);
					view.setTag(viewHolder4);
	            	break;
	            case 5:
	            	viewHolder5 = new ViewHolder();
					view = LayoutInflater.from(mContext).inflate(R.layout.phone_constacts_item05, null);
//					if(!isShowBar){
//						viewHolder5.mLinearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
//						viewHolder5.mLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(srceenWide/2, srceenWide/3));
//					}
					viewHolder5.name = (TextView) view.findViewById(R.id.name);
					viewHolder5.tvLetter = (TextView) view.findViewById(R.id.catalog);
					viewHolder5.phone_number = (TextView) view.findViewById(R.id.phone_number);
					viewHolder5.email_address = (TextView) view.findViewById(R.id.email_address);
					viewHolder5.qq_number = (TextView) view.findViewById(R.id.qq_number);
					viewHolder5.icon = (ImageView) view.findViewById(R.id.icon);
					viewHolder5.select = (ImageView) view.findViewById(R.id.select);
					view.setTag(viewHolder5);
	            	break;
	            case 6:
	            	viewHolder6 = new ViewHolder();
					view = LayoutInflater.from(mContext).inflate(R.layout.phone_constacts_item06, null);
//					if(!isShowBar){
//						viewHolder6.mLinearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
//						viewHolder6.mLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(srceenWide/2, srceenWide/3));
//					}
					viewHolder6.name = (TextView) view.findViewById(R.id.name);
					viewHolder6.tvLetter = (TextView) view.findViewById(R.id.catalog);
					viewHolder6.phone_number = (TextView) view.findViewById(R.id.phone_number);
					viewHolder6.email_address = (TextView) view.findViewById(R.id.email_address);
					viewHolder6.qq_number = (TextView) view.findViewById(R.id.qq_number);
					viewHolder6.icon = (ImageView) view.findViewById(R.id.icon);
					viewHolder6.select = (ImageView) view.findViewById(R.id.select);
					view.setTag(viewHolder6);
	            	break;
	            case 7:
	            	viewHolder7 = new ViewHolder();
					view = LayoutInflater.from(mContext).inflate(R.layout.phone_constacts_item07, null);
//					if(!isShowBar){
//						viewHolder7.mLinearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
//						viewHolder7.mLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(srceenWide/2, srceenWide/3));
//					}
					viewHolder7.name = (TextView) view.findViewById(R.id.name);
					viewHolder7.tvLetter = (TextView) view.findViewById(R.id.catalog);
					viewHolder7.phone_number = (TextView) view.findViewById(R.id.phone_number);
					viewHolder7.email_address = (TextView) view.findViewById(R.id.email_address);
					viewHolder7.qq_number = (TextView) view.findViewById(R.id.qq_number);
					viewHolder7.icon = (ImageView) view.findViewById(R.id.icon);
					viewHolder7.select = (ImageView) view.findViewById(R.id.select);
					view.setTag(viewHolder7);
	            	break;
	            case 8:
	            	viewHolder8 = new ViewHolder();
					view = LayoutInflater.from(mContext).inflate(R.layout.phone_constacts_item08, null);
//					if(!isShowBar){
//						viewHolder8.mLinearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
//						viewHolder8.mLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(srceenWide/2, srceenWide/3));
//					}
					viewHolder8.name = (TextView) view.findViewById(R.id.name);
					viewHolder8.tvLetter = (TextView) view.findViewById(R.id.catalog);
					viewHolder8.phone_number = (TextView) view.findViewById(R.id.phone_number);
					viewHolder8.email_address = (TextView) view.findViewById(R.id.email_address);
					viewHolder8.qq_number = (TextView) view.findViewById(R.id.qq_number);
					viewHolder8.icon = (ImageView) view.findViewById(R.id.icon);
					viewHolder8.select = (ImageView) view.findViewById(R.id.select);
					view.setTag(viewHolder8);
	            	break;
	            case 9:
	            	viewHolder9 = new ViewHolder();
					view = LayoutInflater.from(mContext).inflate(R.layout.phone_constacts_item09, null);
//					if(!isShowBar){
//						viewHolder9.mLinearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
//						viewHolder9.mLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(srceenWide/2, srceenWide/3));
//					}
					viewHolder9.name = (TextView) view.findViewById(R.id.name);
					viewHolder9.tvLetter = (TextView) view.findViewById(R.id.catalog);
					viewHolder9.phone_number = (TextView) view.findViewById(R.id.phone_number);
					viewHolder9.email_address = (TextView) view.findViewById(R.id.email_address);
					viewHolder9.qq_number = (TextView) view.findViewById(R.id.qq_number);
					viewHolder9.icon = (ImageView) view.findViewById(R.id.icon);
					viewHolder9.select = (ImageView) view.findViewById(R.id.select);
					view.setTag(viewHolder9);
	            	break;
	            case 10:
	            	viewHolder10 = new ViewHolder();
					view = LayoutInflater.from(mContext).inflate(R.layout.phone_constacts_item10, null);
//					if(!isShowBar){
//						viewHolder10.mLinearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
//						viewHolder10.mLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(srceenWide/2, srceenWide/3));
//					}
					viewHolder10.name = (TextView) view.findViewById(R.id.name);
					viewHolder10.tvLetter = (TextView) view.findViewById(R.id.catalog);
					viewHolder10.phone_number = (TextView) view.findViewById(R.id.phone_number);
					viewHolder10.email_address = (TextView) view.findViewById(R.id.email_address);
					viewHolder10.qq_number = (TextView) view.findViewById(R.id.qq_number);
					viewHolder10.icon = (ImageView) view.findViewById(R.id.icon);
					viewHolder10.select = (ImageView) view.findViewById(R.id.select);
					view.setTag(viewHolder10);
	            	break;
	            default:
	            	viewHolder11 = new ViewHolder();
					view = LayoutInflater.from(mContext).inflate(R.layout.phone_constacts_item11, null);
//					if(!isShowBar){
//						viewHolder11.mLinearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
//						viewHolder11.mLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(srceenWide/2, srceenWide/3));
//					}
					viewHolder11.name = (TextView) view.findViewById(R.id.name);
					viewHolder11.tvLetter = (TextView) view.findViewById(R.id.catalog);
					viewHolder11.phone_number = (TextView) view.findViewById(R.id.phone_number);
					viewHolder11.email_address = (TextView) view.findViewById(R.id.email_address);
					viewHolder11.qq_number = (TextView) view.findViewById(R.id.qq_number);
					viewHolder11.icon = (ImageView) view.findViewById(R.id.icon);
					viewHolder11.select = (ImageView) view.findViewById(R.id.select);
					view.setTag(viewHolder11);
	            	break;
	            }
			}else{
				switch (mContent.getType()) { 
	            case 0:
	            	viewHolder = (ViewHolder) view.getTag();
	            	break;
	            case 1:
	            	viewHolder1 = (ViewHolder) view.getTag();
	            	break;
	            case 2:
	            	viewHolder2 = (ViewHolder) view.getTag();
	            	break;
	            case 3:
	            	viewHolder3 = (ViewHolder) view.getTag();
	            	break;
	            case 4:
	            	viewHolder4 = (ViewHolder) view.getTag();
	            	break;
	            case 5:
	            	viewHolder5 = (ViewHolder) view.getTag();
	            	break;
	            case 6:
	            	viewHolder6 = (ViewHolder) view.getTag();
	            	break;
	            case 7:
	            	viewHolder7 = (ViewHolder) view.getTag();
	            	break;
	            case 8:
	            	viewHolder8 = (ViewHolder) view.getTag();
	            	break;
	            case 9:
	            	viewHolder9 = (ViewHolder) view.getTag();
	            	break;
	            case 10:
	            	viewHolder10 = (ViewHolder) view.getTag();
	            	break;
	            default:
	            	viewHolder11 = (ViewHolder) view.getTag();
	            	break;
				}
			}
			
			//根据position获取分类的首字母的Char ascii值
			//int section = getSectionForPosition(position);
			context = StaticMethod.codeMaxCard(mContent);
			//context = mContent.getmSortModel().getName()+":"+mContent.getNumber()+":"+mContent.getEmail()+":"+mContent.getQQ()+":"+mContent.getCompany();
			switch (mContent.getType()) { 
            case 0:
            	//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            	if(!isShowBar){
            		viewHolder.tvLetter.setVisibility(View.GONE);
            	}else if(isShowBar && !isShowLetter){
            		viewHolder.tvLetter.setText("");
            	}else if(isShowBar && isShowLetter){
            		int num = 0;
            		if(letterCounter != null && letterCounter.length > arrlist.indexOf(mContent.getmSortModel().getSortLetters())){
            			num = letterCounter[arrlist.indexOf(mContent.getmSortModel().getSortLetters())];
            		}
    				viewHolder.tvLetter.setText(mContent.getmSortModel().getSortLetters()+"     "+num);
    			}
            	viewHolder.name.setText(this.list.get(position).getmSortModel().getName());
    			viewHolder.phone_number.setText(this.list.get(position).getNumber());
    			viewHolder.email_address.setText(this.list.get(position).getEmail());
    			viewHolder.qq_number.setText(this.list.get(position).getQQ());
    			viewHolder.context = context;
    			if(this.list.get(position).isSelected){
    				viewHolder.select.setVisibility(View.VISIBLE);
    			}else{
    				viewHolder.select.setVisibility(View.GONE);
    			}
    			try {
					//Log.d("thismylog", "bitmap = "+);
					viewHolder.icon.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(maxdialog != null &&maxdialog.getVisibility() == View.VISIBLE){
								maxdialog.setVisibility(View.GONE);
							}else if(maxdialog != null){
								maxdialog.setVisibility(View.VISIBLE);
								try {
									maxdialog.setImageBitmap(MaxCard.createQRImage(viewHolder.context, maxSize, maxSize, null));
								} catch (FileNotFoundException
										| NotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					});
					viewHolder.icon.setImageBitmap(MaxCard.createQRImage(context, (int)mResources.getDimension(R.dimen.icon_weight), (int)mResources.getDimension(R.dimen.icon_height), null));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

            	break;
            case 1:
            	//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            	if(!isShowBar){
            		viewHolder1.tvLetter.setVisibility(View.GONE);
            	}else if(isShowBar && !isShowLetter){
            		viewHolder1.tvLetter.setText("");
            	}else if(isShowBar && isShowLetter){
            		int num = 0;
            		if(letterCounter != null && letterCounter.length > arrlist.indexOf(mContent.getmSortModel().getSortLetters())){
            			num = letterCounter[arrlist.indexOf(mContent.getmSortModel().getSortLetters())];
            		}
    				viewHolder1.tvLetter.setText(mContent.getmSortModel().getSortLetters()+"     "+num);
    			}
            	viewHolder1.name.setText(this.list.get(position).getmSortModel().getName());
    			viewHolder1.phone_number.setText(this.list.get(position).getNumber());
    			viewHolder1.email_address.setText(this.list.get(position).getEmail());
    			viewHolder1.qq_number.setText(this.list.get(position).getQQ());
    			viewHolder1.context = context;
    			if(this.list.get(position).isSelected){
    				viewHolder1.select.setVisibility(View.VISIBLE);
    			}else{
    				viewHolder1.select.setVisibility(View.GONE);
    			}
    			try {
					//Log.d("thismylog", "bitmap = "+);
					viewHolder1.icon.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(maxdialog != null && maxdialog.getVisibility() == View.VISIBLE){
								maxdialog.setVisibility(View.GONE);
							}else if(maxdialog != null){
								maxdialog.setVisibility(View.VISIBLE);
								try {
									maxdialog.setImageBitmap(MaxCard.createQRImage(viewHolder1.context, maxSize, maxSize, null));
								} catch (FileNotFoundException
										| NotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					});
					viewHolder1.icon.setImageBitmap(MaxCard.createQRImage(context, (int)mResources.getDimension(R.dimen.icon_weight), (int)mResources.getDimension(R.dimen.icon_height), null));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}            	break;
            case 2:
            	//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            	if(!isShowBar){
            		viewHolder2.tvLetter.setVisibility(View.GONE);
            	}else if(isShowBar && !isShowLetter){
            		viewHolder2.tvLetter.setText("");
            	}else if(isShowBar && isShowLetter){
            		int num = 0;
            		if(letterCounter != null && letterCounter.length > arrlist.indexOf(mContent.getmSortModel().getSortLetters())){
            			num = letterCounter[arrlist.indexOf(mContent.getmSortModel().getSortLetters())];
            		}
    				viewHolder2.tvLetter.setText(mContent.getmSortModel().getSortLetters()+"     "+num);
    			}
            	viewHolder2.name.setText(this.list.get(position).getmSortModel().getName());
    			viewHolder2.phone_number.setText(this.list.get(position).getNumber());
    			viewHolder2.email_address.setText(this.list.get(position).getEmail());
    			viewHolder2.qq_number.setText(this.list.get(position).getQQ());
    			viewHolder2.context = context;
    			if(this.list.get(position).isSelected){
    				viewHolder2.select.setVisibility(View.VISIBLE);
    			}else{
    				viewHolder2.select.setVisibility(View.GONE);
    			}
    			try {
					//Log.d("thismylog", "bitmap = "+);
					viewHolder2.icon.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(maxdialog.getVisibility() == View.VISIBLE){
								maxdialog.setVisibility(View.GONE);
							}else{
								maxdialog.setVisibility(View.VISIBLE);
								try {
									maxdialog.setImageBitmap(MaxCard.createQRImage(viewHolder2.context, maxSize, maxSize, null));
								} catch (FileNotFoundException
										| NotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					});
					viewHolder2.icon.setImageBitmap(MaxCard.createQRImage(context, (int)mResources.getDimension(R.dimen.icon_weight), (int)mResources.getDimension(R.dimen.icon_height), null));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	break;
            case 3:
            	//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            	if(!isShowBar){
            		viewHolder3.tvLetter.setVisibility(View.GONE);
            	}else if(isShowBar && !isShowLetter){
            		viewHolder3.tvLetter.setText("");
            	}else if(isShowBar && isShowLetter){
            		int num = 0;
            		if(letterCounter != null && letterCounter.length > arrlist.indexOf(mContent.getmSortModel().getSortLetters())){
            			num = letterCounter[arrlist.indexOf(mContent.getmSortModel().getSortLetters())];
            		}
    				viewHolder3.tvLetter.setText(mContent.getmSortModel().getSortLetters()+"     "+num);
    			}
            	viewHolder3.name.setText(this.list.get(position).getmSortModel().getName());
    			viewHolder3.phone_number.setText(this.list.get(position).getNumber());
    			viewHolder3.email_address.setText(this.list.get(position).getEmail());
    			viewHolder3.qq_number.setText(this.list.get(position).getQQ());
    			viewHolder3.context = context;
    			if(this.list.get(position).isSelected){
    				viewHolder3.select.setVisibility(View.VISIBLE);
    			}else{
    				viewHolder3.select.setVisibility(View.GONE);
    			}
    			try {
					//Log.d("thismylog", "bitmap = "+);
					viewHolder3.icon.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(maxdialog != null &&maxdialog.getVisibility() == View.VISIBLE){
								maxdialog.setVisibility(View.GONE);
							}else if(maxdialog != null){
								maxdialog.setVisibility(View.VISIBLE);
								try {
									maxdialog.setImageBitmap(MaxCard.createQRImage(viewHolder3.context, maxSize, maxSize, null));
								} catch (FileNotFoundException
										| NotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					});
					viewHolder3.icon.setImageBitmap(MaxCard.createQRImage(context, (int)mResources.getDimension(R.dimen.icon_weight), (int)mResources.getDimension(R.dimen.icon_height), null));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	break;
            case 4:
            	//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            	if(!isShowBar){
            		viewHolder4.tvLetter.setVisibility(View.GONE);
            	}else if(isShowBar && !isShowLetter){
            		viewHolder4.tvLetter.setText("");
            	}else if(isShowBar && isShowLetter){
            		int num = 0;
            		if(letterCounter != null && letterCounter.length > arrlist.indexOf(mContent.getmSortModel().getSortLetters())){
            			num = letterCounter[arrlist.indexOf(mContent.getmSortModel().getSortLetters())];
            		}
    				viewHolder4.tvLetter.setText(mContent.getmSortModel().getSortLetters()+"     "+num);
    			}
            	viewHolder4.name.setText(this.list.get(position).getmSortModel().getName());
    			viewHolder4.phone_number.setText(this.list.get(position).getNumber());
    			viewHolder4.email_address.setText(this.list.get(position).getEmail());
    			viewHolder4.qq_number.setText(this.list.get(position).getQQ());
    			viewHolder4.context = context;
    			if(this.list.get(position).isSelected){
    				viewHolder4.select.setVisibility(View.VISIBLE);
    			}else{
    				viewHolder4.select.setVisibility(View.GONE);
    			}
    			try {
					//Log.d("thismylog", "bitmap = "+);
					viewHolder4.icon.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(maxdialog != null && maxdialog.getVisibility() == View.VISIBLE){
								maxdialog.setVisibility(View.GONE);
							}else if(maxdialog != null){
								maxdialog.setVisibility(View.VISIBLE);
								try {
									maxdialog.setImageBitmap(MaxCard.createQRImage(viewHolder4.context, maxSize, maxSize, null));
								} catch (FileNotFoundException
										| NotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					});
					viewHolder4.icon.setImageBitmap(MaxCard.createQRImage(context, (int)mResources.getDimension(R.dimen.icon_weight), (int)mResources.getDimension(R.dimen.icon_height), null));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	break;
            case 5:
            	//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            	if(!isShowBar){
            		viewHolder5.tvLetter.setVisibility(View.GONE);
            	}else if(isShowBar && !isShowLetter){
            		viewHolder5.tvLetter.setText("");
            	}else if(isShowBar && isShowLetter){
            		int num = 0;
            		if(letterCounter != null && letterCounter.length > arrlist.indexOf(mContent.getmSortModel().getSortLetters())){
            			num = letterCounter[arrlist.indexOf(mContent.getmSortModel().getSortLetters())];
            		}
    				viewHolder5.tvLetter.setText(mContent.getmSortModel().getSortLetters()+"     "+num);
    			}
            	viewHolder5.name.setText(this.list.get(position).getmSortModel().getName());
    			viewHolder5.phone_number.setText(this.list.get(position).getNumber());
    			viewHolder5.email_address.setText(this.list.get(position).getEmail());
    			viewHolder5.qq_number.setText(this.list.get(position).getQQ());
    			viewHolder5.context = context;
    			if(this.list.get(position).isSelected){
    				viewHolder5.select.setVisibility(View.VISIBLE);
    			}else{
    				viewHolder5.select.setVisibility(View.GONE);
    			}
    			try {
					//Log.d("thismylog", "bitmap = "+);
					viewHolder5.icon.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(maxdialog != null  && maxdialog.getVisibility() == View.VISIBLE){
								maxdialog.setVisibility(View.GONE);
							}else if(maxdialog != null ){
								maxdialog.setVisibility(View.VISIBLE);
								try {
									maxdialog.setImageBitmap(MaxCard.createQRImage(viewHolder5.context, maxSize, maxSize, null));
								} catch (FileNotFoundException
										| NotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					});
					viewHolder5.icon.setImageBitmap(MaxCard.createQRImage(context, (int)mResources.getDimension(R.dimen.icon_weight), (int)mResources.getDimension(R.dimen.icon_height), null));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	break;
            case 6:
            	//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            	if(!isShowBar){
            		viewHolder6.tvLetter.setVisibility(View.GONE);
            	}else if(isShowBar && !isShowLetter){
            		viewHolder6.tvLetter.setText("");
            	}else if(isShowBar && isShowLetter){
            		int num = 0;
            		if(letterCounter != null && letterCounter.length > arrlist.indexOf(mContent.getmSortModel().getSortLetters())){
            			num = letterCounter[arrlist.indexOf(mContent.getmSortModel().getSortLetters())];
            		}
    				viewHolder6.tvLetter.setText(mContent.getmSortModel().getSortLetters()+"     "+num);
    			}
            	viewHolder6.name.setText(this.list.get(position).getmSortModel().getName());
    			viewHolder6.phone_number.setText(this.list.get(position).getNumber());
    			viewHolder6.email_address.setText(this.list.get(position).getEmail());
    			viewHolder6.qq_number.setText(this.list.get(position).getQQ());
    			viewHolder6.context = context;
    			if(this.list.get(position).isSelected){
    				viewHolder6.select.setVisibility(View.VISIBLE);
    			}else{
    				viewHolder6.select.setVisibility(View.GONE);
    			}
    			try {
					//Log.d("thismylog", "bitmap = "+);
					viewHolder6.icon.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(maxdialog != null && maxdialog.getVisibility() == View.VISIBLE){
								maxdialog.setVisibility(View.GONE);
							}else if(maxdialog != null ){
								maxdialog.setVisibility(View.VISIBLE);
								try {
									maxdialog.setImageBitmap(MaxCard.createQRImage(viewHolder6.context, maxSize, maxSize, null));
								} catch (FileNotFoundException
										| NotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					});
					viewHolder6.icon.setImageBitmap(MaxCard.createQRImage(context, (int)mResources.getDimension(R.dimen.icon_weight), (int)mResources.getDimension(R.dimen.icon_height), null));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	break;
            case 7:
            	//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            	if(!isShowBar){
            		viewHolder7.tvLetter.setVisibility(View.GONE);
            	}else if(isShowBar && !isShowLetter){
            		viewHolder7.tvLetter.setText("");
            	}else if(isShowBar && isShowLetter ){
            		int num = 0;
            		if(letterCounter != null && letterCounter.length > arrlist.indexOf(mContent.getmSortModel().getSortLetters())){
            			num = letterCounter[arrlist.indexOf(mContent.getmSortModel().getSortLetters())];
            		}
    				viewHolder7.tvLetter.setText(mContent.getmSortModel().getSortLetters()+"     "+num);
    			}
            	viewHolder7.name.setText(this.list.get(position).getmSortModel().getName());
    			viewHolder7.phone_number.setText(this.list.get(position).getNumber());
    			viewHolder7.email_address.setText(this.list.get(position).getEmail());
    			viewHolder7.qq_number.setText(this.list.get(position).getQQ());
    			viewHolder7.context = context;
    			if(this.list.get(position).isSelected){
    				viewHolder7.select.setVisibility(View.VISIBLE);
    			}else{
    				viewHolder7.select.setVisibility(View.GONE);
    			}
    			try {
					//Log.d("thismylog", "bitmap = "+);
					viewHolder7.icon.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(maxdialog != null && maxdialog.getVisibility() == View.VISIBLE){
								maxdialog.setVisibility(View.GONE);
							}else if(maxdialog != null ){
								maxdialog.setVisibility(View.VISIBLE);
								try {
									maxdialog.setImageBitmap(MaxCard.createQRImage(viewHolder7.context, maxSize, maxSize, null));
								} catch (FileNotFoundException
										| NotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					});
					viewHolder7.icon.setImageBitmap(MaxCard.createQRImage(context, (int)mResources.getDimension(R.dimen.icon_weight), (int)mResources.getDimension(R.dimen.icon_height), null));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	break;
            case 8:
            	//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            	if(!isShowBar){
            		viewHolder8.tvLetter.setVisibility(View.GONE);
            	}else if(isShowBar && !isShowLetter){
            		viewHolder8.tvLetter.setText("");
            	}else if(isShowBar && isShowLetter){
            		int num = 0;
            		if(letterCounter != null && letterCounter.length > arrlist.indexOf(mContent.getmSortModel().getSortLetters())){
            			num = letterCounter[arrlist.indexOf(mContent.getmSortModel().getSortLetters())];
            		}
    				viewHolder8.tvLetter.setText(mContent.getmSortModel().getSortLetters()+"     "+num);
    			}
            	viewHolder8.name.setText(this.list.get(position).getmSortModel().getName());
    			viewHolder8.phone_number.setText(this.list.get(position).getNumber());
    			viewHolder8.email_address.setText(this.list.get(position).getEmail());
    			viewHolder8.qq_number.setText(this.list.get(position).getQQ());
    			viewHolder8.context = context;
    			if(this.list.get(position).isSelected){
    				viewHolder8.select.setVisibility(View.VISIBLE);
    			}else{
    				viewHolder8.select.setVisibility(View.GONE);
    			}
    			try {
					//Log.d("thismylog", "bitmap = "+);
					viewHolder8.icon.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(maxdialog != null && maxdialog.getVisibility() == View.VISIBLE){
								maxdialog.setVisibility(View.GONE);
							}else if(maxdialog != null){
								maxdialog.setVisibility(View.VISIBLE);
								try {
									maxdialog.setImageBitmap(MaxCard.createQRImage(viewHolder8.context, maxSize, maxSize, null));
								} catch (FileNotFoundException
										| NotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					});
					viewHolder8.icon.setImageBitmap(MaxCard.createQRImage(context, (int)mResources.getDimension(R.dimen.icon_weight), (int)mResources.getDimension(R.dimen.icon_height), null));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	break;
            case 9:
            	//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            	if(!isShowBar){
            		viewHolder9.tvLetter.setVisibility(View.GONE);
            	}else if(isShowBar && !isShowLetter){
            		viewHolder9.tvLetter.setText("");
            	}else if(isShowBar && isShowLetter){
            		int num = 0;
            		if(letterCounter != null && letterCounter.length > arrlist.indexOf(mContent.getmSortModel().getSortLetters())){
            			num = letterCounter[arrlist.indexOf(mContent.getmSortModel().getSortLetters())];
            		}
    				viewHolder9.tvLetter.setText(mContent.getmSortModel().getSortLetters()+"     "+num);
    			}
            	viewHolder9.name.setText(this.list.get(position).getmSortModel().getName());
    			viewHolder9.phone_number.setText(this.list.get(position).getNumber());
    			viewHolder9.email_address.setText(this.list.get(position).getEmail());
    			viewHolder9.qq_number.setText(this.list.get(position).getQQ());
    			viewHolder9.context = context;
    			if(this.list.get(position).isSelected){
    				viewHolder9.select.setVisibility(View.VISIBLE);
    			}else{
    				viewHolder9.select.setVisibility(View.GONE);
    			}
    			try {
					//Log.d("thismylog", "bitmap = "+);
					viewHolder9.icon.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(maxdialog != null && maxdialog.getVisibility() == View.VISIBLE){
								maxdialog.setVisibility(View.GONE);
							}else if(maxdialog != null){
								maxdialog.setVisibility(View.VISIBLE);
								try {
									maxdialog.setImageBitmap(MaxCard.createQRImage(viewHolder9.context, maxSize, maxSize, null));
								} catch (FileNotFoundException
										| NotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					});
					viewHolder9.icon.setImageBitmap(MaxCard.createQRImage(context, (int)mResources.getDimension(R.dimen.icon_weight), (int)mResources.getDimension(R.dimen.icon_height), null));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	break;
            case 10:
            	//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            	if(!isShowBar){
            		viewHolder10.tvLetter.setVisibility(View.GONE);
            	}else if(isShowBar && !isShowLetter){
            		viewHolder10.tvLetter.setText("");
            	}else if(isShowBar && isShowLetter){
            		int num = 0;
            		if(letterCounter != null && letterCounter.length > arrlist.indexOf(mContent.getmSortModel().getSortLetters())){
            			num = letterCounter[arrlist.indexOf(mContent.getmSortModel().getSortLetters())];
            		}
    				viewHolder10.tvLetter.setText(mContent.getmSortModel().getSortLetters()+"     "+num);
    			}
    			viewHolder10.name.setText(this.list.get(position).getmSortModel().getName());
    			viewHolder10.phone_number.setText(this.list.get(position).getNumber());
    			viewHolder10.email_address.setText(this.list.get(position).getEmail());
    			viewHolder10.qq_number.setText(this.list.get(position).getQQ());
    			viewHolder10.context = context;
    			if(this.list.get(position).isSelected){
    				viewHolder10.select.setVisibility(View.VISIBLE);
    			}else{
    				viewHolder10.select.setVisibility(View.GONE);
    			}
    			try {
					//Log.d("thismylog", "bitmap = "+);
    				viewHolder10.icon.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(maxdialog != null && maxdialog.getVisibility() == View.VISIBLE){
								maxdialog.setVisibility(View.GONE);
							}else if(maxdialog != null ){
								maxdialog.setVisibility(View.VISIBLE);
								try {
									maxdialog.setImageBitmap(MaxCard.createQRImage(viewHolder10.context, maxSize, maxSize, null));
								} catch (FileNotFoundException
										| NotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					});
    				viewHolder10.icon.setImageBitmap(MaxCard.createQRImage(context, (int)mResources.getDimension(R.dimen.icon_weight), (int)mResources.getDimension(R.dimen.icon_height), null));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	break;
            default:
            	//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            	if(!isShowBar){
            		viewHolder11.tvLetter.setVisibility(View.GONE);
            	}else if(isShowBar && !isShowLetter){
            		viewHolder11.tvLetter.setText("");
            	}else if(isShowBar && isShowLetter){
            		int num = 0;
            		if(letterCounter != null && letterCounter.length > arrlist.indexOf(mContent.getmSortModel().getSortLetters())){
            			num = letterCounter[arrlist.indexOf(mContent.getmSortModel().getSortLetters())];
            		}
    				viewHolder11.tvLetter.setText(mContent.getmSortModel().getSortLetters()+"     "+num);
    			}
    			viewHolder11.name.setText(this.list.get(position).getmSortModel().getName());
    			viewHolder11.phone_number.setText(this.list.get(position).getNumber());
    			viewHolder11.email_address.setText(this.list.get(position).getEmail());
    			viewHolder11.qq_number.setText(this.list.get(position).getQQ());
    			viewHolder11.context = context;
    			if(this.list.get(position).isSelected){
    				viewHolder11.select.setVisibility(View.VISIBLE);
    			}else{
    				viewHolder11.select.setVisibility(View.GONE);
    			}
    			try {
					//Log.d("thismylog", "bitmap = "+);
    				viewHolder11.icon.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(maxdialog != null && maxdialog.getVisibility() == View.VISIBLE){
								maxdialog.setVisibility(View.GONE);
							}else if(maxdialog != null){
								maxdialog.setVisibility(View.VISIBLE);
								try {
									maxdialog.setImageBitmap(MaxCard.createQRImage(viewHolder11.context, maxSize, maxSize, null));
								} catch (FileNotFoundException
										| NotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					});
    				viewHolder11.icon.setImageBitmap(MaxCard.createQRImage(context, (int)mResources.getDimension(R.dimen.icon_weight), (int)mResources.getDimension(R.dimen.icon_height), null));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	break;
			}
		}
		return view;

	}

	final static class ViewHolder {
		LinearLayout mLinearLayout;
		TextView tvLetter;
		TextView phone_number;
		TextView email_address;
		TextView qq_number;
		TextView name;
		ImageView icon;
		ImageView select ;
		String context;
	}

	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		if(list.get(position).getmSortModel().getSortLetters() == null)
			return -1;
		return list.get(position).getmSortModel().getSortLetters().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	@SuppressLint("DefaultLocale")
	public int getPositionForSection(int section) {
		if(section == -1)
			return -1;
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getmSortModel().getSortLetters();
			char firstChar = '0';
			if(sortStr != null && sortStr.toUpperCase() != null)
			    firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * 提取英文的首字母，非英文字母用#代替。
	 * 
	 * @param str
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	private String getAlpha(String str) {
		String  sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}