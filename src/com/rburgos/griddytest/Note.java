package com.rburgos.griddytest;

/**
 * Note class to hold notes from database
 */
public class Note
{
	private long id;
	private String title;
	private String text;

    public Note (long id, String title, String text)
    {
	    this.id = id;
	    this.title = title;
	    this.text = text;
    }

	public Note (String title, String text)
	{
		this(0, title, text);
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	@Override
	public String toString()
	{
		return this.title;
	}
}
