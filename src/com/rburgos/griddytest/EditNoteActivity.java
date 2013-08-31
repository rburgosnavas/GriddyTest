package com.rburgos.griddytest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditNoteActivity extends Activity implements View.OnClickListener
{
    private static final String TAG = EditNoteActivity.class.getSimpleName();

	private Intent intent;
	private NoteDataSource dataSource;

    private Button saveEditBtn;
    private EditText titleEt, noteEt;

    private String titleParam, textParam;
	private long id;

	public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_note_layout);

	    // Get the Intent from MainActivity and initialize Note related fields
        intent = getIntent();
	    id = intent.getExtras().getLong(CellFragment.ID_ARG);
	    titleParam = intent.getExtras().getString(CellFragment.TITLE_ARG);
	    textParam = intent.getExtras().getString(CellFragment.TEXT_ARG);

	    // Start a Datasource
	    dataSource = new NoteDataSource(this);
	    dataSource.open();

	    // Widgets
        saveEditBtn = (Button)findViewById(R.id.save_edit_btn);
        saveEditBtn.setOnClickListener(this);

	    titleEt = (EditText)findViewById(R.id.note_title_et);
	    titleEt.setText(titleParam);

        noteEt = (EditText)findViewById(R.id.note_et);
        noteEt.setText(textParam);
    }

	@Override
	protected void onPause()
	{
		dataSource.close();
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		dataSource.open();
		super.onResume();
	}

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            // Save
	        case R.id.save_edit_btn:
	            titleParam = titleEt.getText().toString();
                textParam = noteEt.getText().toString();

		        // Get title and test from the edited note
	            intent.putExtra(CellFragment.TITLE_ARG, titleParam);
	            intent.putExtra(CellFragment.TEXT_ARG, textParam);

		        // If the ID is 0 or less then insert it to the db and get the
		        // ID back...
	            if (id <= 0)
	            {
		            id = dataSource.addNote(titleParam, textParam);
		            Log.i(TAG, "New note added at ID " + id);
	            }
	            // ... else just update the current note
	            else
	            {
		            dataSource.updateNote(id, titleParam, textParam);
		            Log.i(TAG, "Note updated at ID " + id);
	            }

		        // Push all this stuff back to the calling activity
	            intent.putExtra(CellFragment.ID_ARG, id);
                setResult(RESULT_OK, intent);
                finish();
                break;
            default:
                break;
        }
    }
}