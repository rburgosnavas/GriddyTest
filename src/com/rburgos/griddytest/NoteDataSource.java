package com.rburgos.griddytest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class NoteDataSource
{
	private static final String TAG = NoteDataSource.class.getSimpleName();
	private SQLiteDatabase database;
	private NoteDBOpenHelper helper;
	private String[] columns = {
			NoteDBOpenHelper.KEY_ID,
			NoteDBOpenHelper.TITLE,
			NoteDBOpenHelper.TEXT
	};

	public NoteDataSource(Context con)
	{
		helper = new NoteDBOpenHelper(con);
	}

	public void open() throws SQLiteException
	{
		database = helper.getWritableDatabase();
		Log.i(TAG, "DB opened");
	}

	public void close()
	{
		helper.close();
		Log.i(TAG, "DB closed");
	}

	/**
	 * Inserts a new note and returns the ID of the new note
	 *
	 * @param title The title of the note
	 * @param text  The note's text
	 * @return The new note's ID from the database
	 */
	public long addNote(String title, String text)
	{
		ContentValues newNoteRow = new ContentValues();
		newNoteRow.put(NoteDBOpenHelper.TITLE, title);
		newNoteRow.put(NoteDBOpenHelper.TEXT, text);

		return database.insert(NoteDBOpenHelper.NOTE_TABLE, null,
				newNoteRow);
	}

	public boolean updateNote(long id, String title, String text)
	{
		ContentValues updateNoteRow = new ContentValues();
		updateNoteRow.put(NoteDBOpenHelper.TITLE, title);
		updateNoteRow.put(NoteDBOpenHelper.TEXT, text);

		return database.update(NoteDBOpenHelper.NOTE_TABLE, updateNoteRow,
				NoteDBOpenHelper.KEY_ID + " = " + id, null) > 0;
	}

	/**
	 * Removes a note based on it's ID. Return true if a note was deleted,
	 * false otherwise
	 *
	 * @param note The Note object to that holds the ID reference
	 * @return true if the note was deleted, false otherwise
	 */
	public boolean deleteNote(Note note)
	{
		return database.delete(NoteDBOpenHelper.NOTE_TABLE,
				NoteDBOpenHelper.KEY_ID + " = " + note.getId(),
				null) > 0;
	}

	public List<Note> getAllNotes()
	{
		List<Note> notes = new ArrayList<Note>();
		Cursor cursor = database.query(NoteDBOpenHelper.NOTE_TABLE, columns,
				null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast())
		{
			notes.add(new Note(cursor.getLong(0), cursor.getString(1),
					cursor.getString(2)));
			cursor.moveToNext();
		}

		cursor.close();
		return notes;
	}
}
