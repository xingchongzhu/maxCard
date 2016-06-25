package com.maxcard.contact.model;

import com.maxcard.contact.sortlist.SortModel;

import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

public class CardModel implements Parcelable{

	public final static String NAME = "_name";
	public final static String SORTLETTERS = "_iletter";
	public final static String CONTACTID = "_id";
	public final static String EMAIL = "_email";
	public final static String ADDRESS = "_address";
	public final static String Q_Q = "_qq";
	public final static String NUMBER = "_number";
	public final static String TYPE = "_type";
	public final static String BITMAP = "_bitmap";
	public final static String COMPANY = "_company";
	public final static String POSITION = "_position";
	public final static String WORDS = "_words";
	public final static String DRAWABLE = "_drawable";
	public final static String FAX = "_fax";
	public final static String URL = "_url";
	public final static String DEPARTMENT = "_department";
	public final static String TEL = "_tel";
	public final static String IMAGE_PATH = "_image_path";
	
	private String nameString;
	private SortModel mSortModel;
	private String contactId;
	private String email;  
	private String address;
	private String QQ;
	private String url;
	private String fax;
	private String number;
	private String company;
	private String position;
	private String words;
	private String department;
	private String tel;
	private String imagePath;
	private int type;
	private Drawable mDrawable;
	private Bitmap bgBitmap = null;
	private Bitmap personBitmap = null;
	
	public boolean isSelected = false;
	public CardModel(){
		mSortModel = null;
		contactId = "";
		email = "";
		address = "";
		QQ = "";
		number = "";
		company = "";
		position = "";
		words = "";
		type = 0;
		nameString = "";
		fax = "";
		url = "";
		mDrawable = null;
		department = "";
		tel = "";
		imagePath = "";
	}
	public CardModel(SortModel mSortModel,String contactId,
			String number,String email,String address,String QQ,String company,
			int type,String fax,String url,String words,String position,String department,String tel,String imagePath){
		this.mSortModel = mSortModel;
		this.nameString = mSortModel.getName();
		this.contactId = contactId;
		this.number = number;
		this.email = email;
		this.address = address;
		this.QQ = QQ;
		this.company = company;
		this.type = type;
		this.fax = fax;
		this.url = url;
		this.words = words;
		this.position = position;
		this.department = department;
		this.tel = tel;
		this.imagePath = imagePath;
	}
	public void setBgImage(Bitmap bgBitmap){
		this.bgBitmap = bgBitmap;
	}
	public Bitmap getBgImage(){
		return bgBitmap;
	}
	public CardModel(CardModel cardModel){
		this.mSortModel = new SortModel(cardModel.getmSortModel());
		this.contactId = cardModel.getContactId();
		this.nameString = cardModel.getName();
		this.number = cardModel.getNumber();
		this.email = cardModel.getEmail();
		this.address = cardModel.getAddress();
		this.QQ = cardModel.getQQ();
		this.company = cardModel.getCompany();
		this.type = cardModel.getType();
		this.words = cardModel.getWords();
		this.fax = cardModel.getFax();
		this.url = cardModel.getUrl();
		this.department = cardModel.getDepartment();
		this.tel = cardModel.getTel();
		this.imagePath = cardModel.getImagePath();
		this.bgBitmap = cardModel.getBgImage();
		this.personBitmap = cardModel.getPersonBitmap();
	}
	public void setImagePath(String imagePath){
		this.imagePath = imagePath;
	}
	public String getImagePath(){
		return imagePath;
	}
	public void setPersonBitmap(Bitmap personBitmap){
		this.personBitmap = personBitmap;
	}
	public Bitmap getPersonBitmap(){
		return personBitmap;
	}
	public void setTel(String tel){
		this.tel = tel;
	}
	public String getTel(){
		return tel;
	}
	public void setDepartment(String department){
		this.department = department;
	}
	public String getDepartment(){
		return department;
	}
	public void setFax(String fax){
		this.fax = fax;
	}
	public String getFax(){
		return fax;
	}
	public void setUrl(String url){
		this.url = url;
	}
	public String getUrl(){
		return url;
	}
	public void setWords(String words){
		this.words= words;
	}
	public String getName(){
		return this.nameString;
	}
	public void setName(String nameString){
		this.nameString= nameString;
	}
	public String getWords(){
		return this.words;
	}
	public void setPosition(String position){
		this.position = position;
	}
	public String getPosition(){
		return position;
	}
	public Drawable getDrawable(){
		return mDrawable;
	}
	public void setDrawable(Drawable mDrawable){
		this.mDrawable = mDrawable;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public SortModel getmSortModel() {
		return mSortModel;
	}
	public void setContactId(String contactId) {
		this.contactId = contactId;
	}
	public String getContactId() {
		return contactId;
	}
	public void setmSortModel(SortModel mSortModel) {
		this.mSortModel = mSortModel;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getQQ() {
		return QQ;
	}
	public void setQQ(String qQ) {
		QQ = qQ;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(this.contactId);
	    dest.writeString(this.number);
	    dest.writeString(this.email);
	    dest.writeString(this.address);
	    dest.writeString(this.QQ);
	    dest.writeString(this.company);
	    dest.writeInt(type);
	    dest.writeParcelable(mSortModel,0);
	    
	}
	
	
}
