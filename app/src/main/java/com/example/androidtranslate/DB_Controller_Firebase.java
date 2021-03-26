package com.example.androidtranslate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DB_Controller_Firebase extends SQLiteOpenHelper {
    public DB_Controller_Firebase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "firebase_msg.DB", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //jenis data di database blm diisi
        sqLiteDatabase.execSQL("CREATE TABLE TB_FIRE_MSG(ID INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, message TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS TB_FIRE_MSG;");
        onCreate(sqLiteDatabase);
    }

    public void insert_notif(String a, String b){
        //update isinya
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", a);
        contentValues.put("message", b);

        this.getWritableDatabase().insertOrThrow("TB_FIRE_MSG","",contentValues);
    }

    public void delete_notif(String a, String b){
        this.getWritableDatabase().delete("TB_FIRE_MSG","title='"+a+"'and message='"+b+"'",null);
    }

    public void delete_all(){
        this.getWritableDatabase().delete("TB_FIRE_MSG",null,null);
    }





    public ArrayList<String> list_all_students(){
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM TB_FIRE_MSG", null);
        ArrayList<String> ListData = new ArrayList<>();

        cursor.moveToFirst();

        for(int x=cursor.getCount()-1; x >=0 ; x--){

            cursor.moveToPosition(x);

            ListData.add(cursor.getString(1)+"\n"+cursor.getString(2));
            //Lalu Memasukan Semua Datanya kedalam ArrayList
        }

        return ListData;


    }

}

