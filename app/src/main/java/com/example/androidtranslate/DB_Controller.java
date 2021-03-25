package com.example.androidtranslate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DB_Controller extends SQLiteOpenHelper {
    public DB_Controller(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "translate.DB", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE TB_TRANSLATE(ID INTEGER PRIMARY KEY AUTOINCREMENT, SRC_LANG TEXT, TRG_LANG TEXT, SRC_TXT TEXT, TRG_TXT TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS TB_TRANSLATE;");
        onCreate(sqLiteDatabase);
    }

    public void insert_student(String a, String b, String c, String d){
        ContentValues contentValues = new ContentValues();
        contentValues.put("SRC_LANG", a);
        contentValues.put("TRG_LANG", b);
        contentValues.put("SRC_TXT", c);
        contentValues.put("TRG_TXT", d);
        this.getWritableDatabase().insertOrThrow("TB_TRANSLATE","",contentValues);
    }

    public void delete_student(String txtSrc, String txtTrg){
        this.getWritableDatabase().delete("TB_TRANSLATE","SRC_TXT='"+txtSrc+"' && TRG_TXT='"+txtTrg+"'",null);
    }


    public ArrayList<String> list_all_students(){
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM TB_TRANSLATE", null);
        ArrayList<String> ListData = new ArrayList<>();
        /*
        textView.setText("");
        while (cursor.moveToNext()){
            textView.append(cursor.getString(1)+" "+cursor.getString(2)+"\n");
        }

         */

        cursor.moveToFirst();

        for(int x=0; x < cursor.getCount(); x++){

            cursor.moveToPosition(x);

            ListData.add(cursor.getString(1)+" -> "+cursor.getString(4)+"\n"+cursor.getString(2)+" -> "+cursor.getString(3));
            //Lalu Memasukan Semua Datanya kedalam ArrayList
        }

        return ListData;


    }
    

}
