package com.rburgos.griddytest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CellFragment extends Fragment implements OnClickListener,
		View.OnLongClickListener
{
    private static final String TAG = CellFragment.class.getSimpleName();
	public static final String ID_ARG = "id";
	public static final String TITLE_ARG = "title";
	public static final String TEXT_ARG = "text";

	private String idId, titleId, textId;

    private TextView tv;
    private Button refreshBtn, clearBtn, editBtn;
	private View v;

	private ActionMode actionMode;
	private ActionMode.Callback actionModeCallback;

    private String title, text;
	private long id;

    private final int shortAnimTime = 750;
	private boolean isEdited = false;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of this fragment using
     * the provided parameters.
     *
     * @param title The note's title
     * @param text The note's text
     * @return A new instance of fragment CellFragment.
     */
    public static CellFragment newInstance(String title, String text)
    {
        CellFragment fragment = new CellFragment();

        Bundle args = new Bundle();
	    args.putString(TITLE_ARG, title);
	    args.putString(TEXT_ARG, text);

        fragment.setArguments(args);
        return fragment;
    }

	public static CellFragment newInstance()
	{
		return newInstance("", "");
	}

    public CellFragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            title = getArguments().getString(TITLE_ARG);
	        text = getArguments().getString(TEXT_ARG);
        }
	    idId = TAG + getId() + "-id";
	    titleId = TAG + getId() + "-title";
	    textId = TAG + getId() + "-text";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_cell,
                container, false);

        refreshBtn = (Button)root.findViewById(R.id.load_btn);
        refreshBtn.setOnClickListener(this);

        clearBtn = (Button)root.findViewById(R.id.clear_btn);
        clearBtn.setOnClickListener(this);

        editBtn = (Button)root.findViewById(R.id.edit_btn);
        editBtn.setOnClickListener(this);

        tv = (TextView)root.findViewById(R.id.frag_cell_tv);

	    // TODO this is new for ActionBar test
	    tv.setOnLongClickListener(this);

        if (text != null)
        {
            tv.setText(text);
        }

	    v = root.findViewById(R.id.frag_cell_scroll);
	    // v.setOnLongClickListener(this);

	    actionModeCallback = new ActionMode.Callback()
	    {
		    @Override
		    public boolean onCreateActionMode(ActionMode mode, Menu menu)
		    {
			    MenuInflater inflater = getActivity().getMenuInflater();
			    inflater.inflate(R.menu.cell_frag, menu);
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
				    case R.id.toast:
					    Toast.makeText(getActivity(), "<***>",
							    Toast.LENGTH_LONG).show();
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

        return root;
    }

    public void setCrossfadeText(String text)
    {
	    this.tv.animate().
			    alpha(0f).
			    setDuration(shortAnimTime).
			    setListener(new AnimatorListenerAdapter()
			    {
				    @Override
				    public void onAnimationStart(Animator animation)
				    {
					    tv.setVisibility(View.VISIBLE);
				    }

				    @Override
				    public void onAnimationEnd(Animator animation)
				    {
					    tv.setVisibility(View.GONE);
				    }
			    });

	    tv.setText(text);

	    this.tv.setAlpha(0f);
	    this.tv.setVisibility(View.VISIBLE);
	    this.tv.animate().
			    alpha(1f).
			    setDuration(shortAnimTime).
			    setListener(null);
    }

    public void onButtonPressed()
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(
		            "onFragmentInteraction was called from CellFragment");
        }
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try
        {
            mListener = (OnFragmentInteractionListener) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1 && resultCode == Activity.RESULT_OK)
		{
			id = data.getExtras().getLong(CellFragment.ID_ARG);
			title = data.getExtras().getString(CellFragment.TITLE_ARG);
			text = data.getExtras().getString(CellFragment.TEXT_ARG);
			Log.i(TAG, id + ") " + title + ":" + text + " was passed.");
			setCrossfadeText(text);
			isEdited = true;
		}
	}

	@Override
	public void onResume()
	{
		super.onResume();
		if (!isEdited)
		{
			SharedPreferences prefs =
					getActivity().getPreferences(Context.MODE_PRIVATE);
			id = prefs.getLong(idId, 0);
			title = prefs.getString(titleId, "");
			text = prefs.getString(textId, "");
			tv.setText(text);
			isEdited = false;
		}
	}

	@Override
	public void onPause()
	{
		super.onPause();
		SharedPreferences prefs =
				getActivity().getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor= prefs.edit();
		editor.putLong(idId, id);
		editor.putString(titleId, title);
		editor.putString(textId, text);
		editor.commit();
	}

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.load_btn:
	            // Start the list activity to pick a note
	            Intent noteListIntent = new Intent(getActivity(),
			            NotesListActivity.class);
	            startActivityForResult(noteListIntent, 1);
                break;
	        case R.id.edit_btn:
		        // Start the edit note activity and pass title and note text
		        Intent editNoteIntent = new Intent(getActivity(),
				        EditNoteActivity.class);
		        editNoteIntent.putExtra(CellFragment.ID_ARG, id);
		        editNoteIntent.putExtra(CellFragment.TITLE_ARG, title);
		        editNoteIntent.putExtra(CellFragment.TEXT_ARG, text);
		        startActivityForResult(editNoteIntent, 1);
		        break;
	        case R.id.clear_btn:
		        // Clear the note
		        id = 0;
		        title = "";
		        text = "";
		        this.setCrossfadeText(text);
		        break;
            default:
                break;
        }
    }

	@Override
	public boolean onLongClick(View v)
	{
		actionMode = getActivity().startActionMode(actionModeCallback);
		tv.setSelected(true);
		return true;
	}

	public interface OnFragmentInteractionListener
    {
        public void onFragmentInteraction(String text);
    }
}
