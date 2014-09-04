package com.rburgos.fourtinynotes;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity
        implements CellFragment.OnFragmentInteractionListener {
    private final static String TAG = MainActivity.class.getSimpleName();

    private FragmentManager fragManager;
    private FragmentTransaction fragTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relative_main);

        if (savedInstanceState == null) {
            fragManager = getSupportFragmentManager();

            fragTransaction = fragManager.beginTransaction();
            fragTransaction.add(R.id.cell1, CellFragment.newInstance());
            fragTransaction.add(R.id.cell2, CellFragment.newInstance());
            fragTransaction.add(R.id.cell3, CellFragment.newInstance());
            fragTransaction.add(R.id.cell4, CellFragment.newInstance());
            fragTransaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onFragmentInteraction(String text) {
        Log.i(TAG, text);
    }
}
