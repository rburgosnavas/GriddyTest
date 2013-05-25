package com.rburgos.griddytest;

/**
 * Note class to hold notes from database
 */
public class Note
{
	private long id;
	private String noteText;
	private String noteTitle;

    public Note (long id, String noteTitle, String noteText)
    {
	    this.id = id;
	    this.noteTitle = noteTitle;
	    this.noteText = noteText;
    }

	public Note (String noteText, String noteTitle)
	{
		this(0, noteText, noteTitle);
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getNoteTitle()
	{
		return noteTitle;
	}

	public void setNoteTitle(String noteTitle)
	{
		this.noteTitle = noteTitle;
	}

	public String getNoteText()
	{
		return noteText;
	}

	public void setNoteText(String noteText)
	{
		this.noteText = noteText;
	}

	@Override
	public String toString()
	{
		return this.noteTitle;
	}
}
