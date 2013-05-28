package com.rburgos.griddytest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class NotesListActivity extends Activity implements
		AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener
{
	List<Note> notes;
	ListView notesListView;
	ArrayAdapter<Note> adapter;
	Note note;
	View tmpView;

	private ActionMode actionMode;
	private ActionMode.Callback actionModeCallback;

	Intent intent;
	NoteDataSource dataSource;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notes_list);

		intent = getIntent();

		dataSource = new NoteDataSource(this);
		dataSource.open();
		notes = dataSource.getAllNotes();

		notesListView = (ListView) findViewById(R.id.note_list_view);
		adapter = new ArrayAdapter<Note>(this, R.layout.note_list_item_layout,
				notes);

		notesListView.setAdapter(adapter);
		notesListView.setOnItemClickListener(this);
		notesListView.setOnItemLongClickListener(this);

		actionModeCallback = new ActionMode.Callback()
		{
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
								dataSource.deleteNote(note);
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
		};
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
	                        long id)
	{
		note = (Note) parent.getItemAtPosition(position);
		intent.putExtra("idParam", note.getId());
		intent.putExtra("titleParam", note.getNoteTitle());
		intent.putExtra("textParam", note.getNoteText());
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, final View view,
	                               int position, long id)
	{
		Log.i("NotesListActivity", "onLongClick() pressed");
		Log.i("NotesListActivity", view.getClass().getSimpleName());
		tmpView = view;
		note = (Note) parent.getItemAtPosition(position);
		actionMode = startActionMode(actionModeCallback);
		return true;
	}
}