package com.rburgos.griddytest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class NotesListActivity extends Activity implements AdapterView.OnItemClickListener
{
	List<Note> notes;
	ListView notesListView;
	ArrayAdapter<Note> adapter;

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
		adapter = new ArrayAdapter<Note>(this, R.layout.note_list_item_layout, notes);

		notesListView.setAdapter(adapter);
		notesListView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		Note note = (Note) parent.getItemAtPosition(position);
		intent.putExtra("idParam", note.getId());
		intent.putExtra("titleParam", note.getNoteTitle());
		intent.putExtra("textParam", note.getNoteText());
		setResult(RESULT_OK, intent);
		finish();
	}
}