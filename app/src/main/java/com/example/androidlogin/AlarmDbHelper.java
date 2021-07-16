package com.example.androidlogin;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class AlarmDbHelper extends SQLiteOpenHelper{
    private static AlarmDbHelper sInstance;
    public static SQLiteDatabase mDB;

    private static final int DB_VERSION = 1;
    //private static final String DB_NAME = "Alarm.db";
   /* private static final String SQL_CREATE_ENTRIES =
            String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s INTEGER, %sTEXT)",
                    Databases.CreateDB.TABLE_NAME,
                    Databases.CreateDB._ID,
                    Databases.CreateDB.HOUR,
                    Databases.CreateDB.MINUTE,
                    Databases.CreateDB.DRUGTEXT);*/




    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Databases.CreateDB.TABLE_NAME;

    public static AlarmDbHelper getInstance(Context context){
        if(sInstance ==null){
            sInstance = new AlarmDbHelper(context);
        }
        return sInstance;
    }

    public AlarmDbHelper(@Nullable Context context) {
        super(context, Databases.CreateDB.TABLE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Databases.CreateDB._CREATE0);
    }

    //app을 업데이트 할때
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    //입력한 id값을 가진 DB를 지우는 메소드
    public boolean deleteColumn(long id){
        return mDB.delete(Databases.CreateDB.TABLE_NAME,"_id="+id,null)>0;
    }
}