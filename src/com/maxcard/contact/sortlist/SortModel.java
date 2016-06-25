package com.maxcard.contact.sortlist;

import android.os.Parcel;
import android.os.Parcelable;

public  class SortModel implements Parcelable{

	private String name;   //显示的数�?
	private String sortLetters;  //显示数据拼音的首字母
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public SortModel(){
		
	}
	public SortModel(SortModel mSortModel){
		this.name = mSortModel.getName();
		this.sortLetters = mSortModel.getSortLetters();
	}
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(name);
		dest.writeString(sortLetters);
	}
}
