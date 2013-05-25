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
	private static final String ID_ARG= "id_arg";
	private static final String TITLE_ARG = "title_arg";
	private static final String TEXT_ARG = "text_arg";

    private TextView tv;
    private Button refreshBtn, clearBtn, editBtn;
	private View v;

	private ActionMode actionMode;
	private ActionMode.Callback actionModeCallback;

    private String titleParam, textParam;
	private long id;

    private final int shortAnimTime = 750;
	private boolean isEdited = false;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of this fragment using
     * the provided parameters.
     *
     * @param text
     *            Parameter 1.
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
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            titleParam = getArguments().getString(TITLE_ARG);
	        textParam = getArguments().getString(TEXT_ARG);
        }
        Log.i(TAG, "onCreate");
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

	    // TODO this is new for actionbar test
	    tv.setOnLongClickListener(this);

        if (textParam != null)
        {
            tv.setText(textParam);
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

        Log.i(TAG, "onCreateView");
	    Log.i(TAG, "fragment ID: " + getId());

        return root;
    }

    public void setCrossfadeText(String text)
    {
        if (tv != null)
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
        else
        {
            Log.i(TAG, TAG + "#TextView is null");
        }
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
        Log.i(TAG, "onAttach");
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
			id = data.getExtras().getLong("idParam");
			titleParam = data.getExtras().getString("titleParam");
			textParam = data.getExtras().getString("textParam");
			Log.i(TAG, id + ") " + titleParam + ":" + textParam + " was passed.");
			setCrossfadeText(textParam);
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
			id = prefs.getLong(TAG + getId() + "-id", 0);
			titleParam = prefs.getString(TAG + getId() + "-title", "");
			textParam = prefs.getString(TAG + getId() + "-text", "");
			Log.i(TAG, textParam + " was intersepted?");
			tv.setText(textParam);
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
		editor.putLong(TAG + getId() + "-id", id);
		editor.putString(TAG + getId() + "-title", titleParam);
		editor.putString(TAG + getId() + "-text", textParam);
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
		        editNoteIntent.putExtra("idParam", id);
		        editNoteIntent.putExtra("titleParam", titleParam);
		        editNoteIntent.putExtra("textParam", textParam);
		        startActivityForResult(editNoteIntent, 1);
		        break;
	        case R.id.clear_btn:
		        // Clear the note
		        id = 0;
		        titleParam = "";
		        textParam = "";
		        this.setCrossfadeText(textParam);
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
