package com.rburgos.griddytest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NoteDBOpenHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "fournote.db";
	private static final int DATABASE_VERSION = 1;
	public static final String NOTE_TABLE = "notes";
    public static final String KEY_ID = "_id";
	public static final String TITLE = "title";
	public static final String TEXT = "text";
    private static final String DB_CREATE =
            "create table " + NOTE_TABLE + "( " +
            KEY_ID + " integer primary key autoincrement, " +
		    TITLE + " text not null, " +
		            TEXT + " text);";
    private static final String DB_DROP = "drop table if it exists " +
            NOTE_TABLE;

    public NoteDBOpenHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(DB_DROP);
        onCreate(db);
    }
}
