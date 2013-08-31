package com.rburgos.griddytest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class NotesListActivity extends Activity implements
		AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener,
		ActionMode.Callback
{
	private static final String TAG = NotesListActivity.class.getSimpleName();
	private List<Note> notes;
	private ListView notesListView;
	private ArrayAdapter<Note> adapter;
	private Note note;
	private View tmpView;

	private ActionMode actionMode;

	private Intent intent;
	private NoteDataSource dataSource;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notes_list);

		intent = getIntent();

		dataSource = new NoteDataSource(this);
		dataSource.open();
		notes = dataSource.getAllNotes();

		notesListView = (ListView) findViewById(R.id.note_list_view);
		/* Using a standard ArrayAdapter...
		adapter = new ArrayAdapter<Note>(this, R.layout.note_list_item_layout,
				notes);
		... replaced with a custom implementation */
		adapter = new NotesArrayAdapter(this, notes);

		notesListView.setAdapter(adapter);
		notesListView.setOnItemClickListener(this);
		notesListView.setOnItemLongClickListener(this);
	}

	private void setNoteIntent(long id, String title, String text)
	{
		intent.putExtra(CellFragment.ID_ARG, id);
		intent.putExtra(CellFragment.TITLE_ARG, title);
		intent.putExtra(CellFragment.TEXT_ARG, text);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
	                        long id)
	{
		note = (Note) parent.getItemAtPosition(position);
		setNoteIntent(note.getId(), note.getTitle(), note.getText());
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, final View view,
	                               int position, long id)
	{
		Log.i(TAG, "onLongClick() pressed");
		Log.i(TAG, view.getClass().getSimpleName());
		tmpView = view;
		note = (Note) parent.getItemAtPosition(position);
		setNoteIntent(0, note.getTitle(), note.getText());
		setResult(RESULT_OK, intent);
		actionMode = startActionMode(this);
		return true;
	}

	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.note_list, menu);
		return true;
	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu)
	{
		return false;
	}

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.delete:
				dataSource.deleteNote(note);
				tmpView.animate().setDuration(500).alpha(0).
						withEndAction(new Runnable()
				{
					@Override
					public void run()
					{
						notes.remove(note);
						adapter.notifyDataSetChanged();
						tmpView.setAlpha(1);
						tmpView.animate().setDuration(250).alpha(1);
					}
				});
				mode.finish();
				return true;
			default:
				return false;
		}
	}

	@Override
	public void onDestroyActionMode(ActionMode mode)
	{
		actionMode = null;
	}
}