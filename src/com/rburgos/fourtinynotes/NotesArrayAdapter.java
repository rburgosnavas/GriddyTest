package com.rburgos.fourtinynotes;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class NotesArrayAdapter extends ArrayAdapter<Note>
{
	private List<Note> notes;

	public NotesArrayAdapter(Context context, List<Note> notes)
	{
		super(context, R.layout.notes_list_custom, R.id.notes_list_title, notes);
		this.notes = notes;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View row = super.getView(position, convertView, parent);
		ImageView icon = (ImageView) row.findViewById(R.id.list_icon);
		TextView title = (TextView) row.findViewById(R.id.notes_list_title);
		title.setText(notes.get(position).getTitle());
		return row;
	}
}
