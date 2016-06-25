package com.maxcard.contact.sortlist;

import java.util.Comparator;

import com.maxcard.contact.model.CardModel;

import android.util.Log;


/**
 * @Description:拼音的比较器
 * @author http://blog.csdn.net/finddreams
 */ 
public class PinyinComparator implements Comparator<CardModel> {

	private final static String TAG = "PinyinComparator";
	public int compare(CardModel o1, CardModel o2) {
		if ((o1.getmSortModel().getSortLetters() != null && o1.getmSortModel().getSortLetters().equals("@"))
				|| (o2.getmSortModel().getSortLetters()  != null && o2.getmSortModel().getSortLetters().equals("#"))) {
			return -1;
		} else if ((o1.getmSortModel().getSortLetters()  != null && o1.getmSortModel().getSortLetters().equals("#")
				)|| (o2.getmSortModel().getSortLetters()  != null && o2.getmSortModel().getSortLetters().equals("@"))) {
			return 1;
		} else if(o1.getmSortModel().getSortLetters()  != null && o2.getmSortModel().getSortLetters()  != null){
			return o1.getmSortModel().getSortLetters().compareTo(o2.getmSortModel().getSortLetters());
		}else{
			return -1;
		}
	}

}
