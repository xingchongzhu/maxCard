package com.maxcard.contact.database;


import com.maxcard.contact.model.CardModel;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SqliteHelper extends SQLiteOpenHelper{
    //用来保存UserID、Access Token、Access Secret的表名
	
    public static final String TB_NAME= "MaxCard";
    
    public SqliteHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    //创建表
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( "CREATE TABLE IF NOT EXISTS "+
                TB_NAME+ "("+
                CardModel.CONTACTID+ " varchar primary key ,"+
                CardModel.NAME + " varchar,"+
                CardModel.NUMBER + " varchar,"+
                CardModel.SORTLETTERS+ " varchar,"+
                CardModel.EMAIL+ " varchar,"+
                CardModel.ADDRESS+ " varchar,"+
                CardModel.Q_Q+" varchar,"+
                CardModel.TYPE+ " integer,"+
                CardModel.COMPANY+ " varchar,"+
                CardModel.POSITION+ " varchar,"+
                CardModel.WORDS+ " varchar,"+
                CardModel.BITMAP+ " blob,"+
                CardModel.FAX+ " varchar,"+
                CardModel.URL+ " varchar,"+
                CardModel.DEPARTMENT+ " varchar,"+
                CardModel.TEL+ " varchar,"+
                CardModel.IMAGE_PATH+ " varchar"+
                ")"
                );
        Log. e("Database" ,"onCreate create table "+TB_NAME );
    }
    
    //更新表
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "DROP TABLE IF EXISTS " + TB_NAME );
        onCreate(db);
        Log. e("Database" ,"onUpgrade table "+TB_NAME );
    }
    
    //更新列
    public void updateColumn(SQLiteDatabase db, String oldColumn, String newColumn, String typeColumn){
        try{
            db.execSQL( "ALTER TABLE " +
                    TB_NAME + " CHANGE " +
                    oldColumn + " "+ newColumn +
                    " " + typeColumn
            );
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
