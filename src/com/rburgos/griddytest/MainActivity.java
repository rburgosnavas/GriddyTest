package com.rburgos.griddytest;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.rburgos.griddytest.CellFragment.OnFragmentInteractionListener;

public class MainActivity extends FragmentActivity
        implements OnFragmentInteractionListener
{
    private final static String TAG = MainActivity.class.getSimpleName();

    private FragmentManager fragManager;
    private FragmentTransaction fragTransaction;
    private CellFragment cell1, cell2, cell3, cell4;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relative_main);

        if (savedInstanceState == null)
        {
            cell1 = CellFragment.newInstance();
            cell2 = CellFragment.newInstance();
            cell3 = CellFragment.newInstance();
            cell4 = CellFragment.newInstance();

            fragManager = getSupportFragmentManager();

            fragTransaction = fragManager.beginTransaction();
            fragTransaction.add(R.id.cell1, cell1);
            fragTransaction.add(R.id.cell2, cell2);
            fragTransaction.add(R.id.cell3, cell3);
            fragTransaction.add(R.id.cell4, cell4);
            fragTransaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item)
    {
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);

        /* I was testing orientation changes with this code...

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            Toast.makeText(this, "portrait @ " + this.getClass().toString(),
                    Toast.LENGTH_SHORT).show();
        }
        else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            Toast.makeText(this, "landscape @ " + this.getClass().toString(),
                    Toast.LENGTH_SHORT).show();
        }
        */
    }

	@Override
	protected void onPause()
	{
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}

	@Override
    public void onFragmentInteraction(String text)
    {
        Log.i(TAG, text);
    }
}
