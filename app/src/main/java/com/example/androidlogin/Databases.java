package com.example.androidlogin;

import android.provider.BaseColumns;

public class Databases {

    public static final class CreateDB implements BaseColumns {
        public static final String AMPM = "ampm";
        public static final String HOUR = "hour";
        public static final String MINUTE = "minute";
        public static final String DRUGTEXT = "drugtext";
        public static final String ALARMTIME = "alarmtime";
        public static final String TABLE_NAME ="alarmtable";
        public static final String _CREATE0= "create table if not exists "
                + TABLE_NAME+ "("
                +_ID+" integer primary key autoincrement, "
                +AMPM+" text not null, "
                +HOUR+" integer not null , "
                +MINUTE+" integer not null , "
                +DRUGTEXT+" text not null ,"
                +ALARMTIME+" integer not null );";
        //테이블 내에서 구분하기 위해 ID값을 만들어서 행을 추가할때마다 1씩 증가하여 _ID에 입력되도록 한다.
        //HOUR와 MINNuTE은 integer, drugtext는 text로 데이터형식 설정한다.

    }
}