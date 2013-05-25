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

        intent = getIntent();
	    id = intent.getExtras().getLong("idParam");
	    titleParam = intent.getExtras().getString("titleParam");
	    textParam = intent.getExtras().getString("textParam");

	    dataSource = new NoteDataSource(this);
	    dataSource.open();

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
            case R.id.save_edit_btn:
	            titleParam = titleEt.getText().toString();
                textParam = noteEt.getText().toString();
	            intent.putExtra("titleParam", titleParam);
	            intent.putExtra("textParam", textParam);
	            if (id <= 0)
	            {
		            id = dataSource.addNote(titleParam, textParam);
		            Log.i(TAG, "New note added at ID " + id);
	            }
	            else
	            {
		            dataSource.updateNote(id, titleParam, textParam);
		            Log.i(TAG, "Note updated at ID " + id);
	            }
	            intent.putExtra("idParam", id);
                setResult(RESULT_OK, intent);
                finish();
                break;
            default:
                break;
        }
    }
}