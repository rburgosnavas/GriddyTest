package com.rburgos.fourtinynotes;

import android.content.Intent;

/**
 * Note class to hold notes from database
 */
public class Note {
	public static final String ID = "id";
	public static final String TITLE = "title";
	public static final String TEXT = "text";

	private long id;
	private String title;
	private String text;

    public Note (long id, String title, String text) {
	    this.id = id;
	    this.title = title;
	    this.text = text;
    }

	public Note (String title, String text) {
		this(0, title, text);
	}

	public long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getText() {
		return text;
	}

	@Override
	public String toString() {
		return this.title;
	}

	public static Note makeNote(Intent i) {
		return new Note(i.getExtras().getLong(ID),
				i.getExtras().getString(TITLE), i.getExtras().getString(TEXT));
	}

	public static Intent makeIntent(Note n) {
		return null;
	}
}
