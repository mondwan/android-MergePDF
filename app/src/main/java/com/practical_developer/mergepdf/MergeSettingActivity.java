package com.practical_developer.mergepdf;

import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.practical_developer.mergepdf.dummy.DummyContent;

import java.util.ArrayList;

public class MergeSettingActivity extends AppCompatActivity
        implements FileListFragment.FileListFragmentCallbacks {

    private ArrayList<Pair<Long, String>> mFileListSource;

    public ArrayList<Pair<Long, String>> getFileListSource() {
        return mFileListSource;
    }

    public boolean onDeleteFileItem(int pos) {
        boolean ret = true;

        try {
            mFileListSource.remove(pos);
        } catch (IndexOutOfBoundsException e) {
            ret = false;
        }

        return ret;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize mFileListSource before inflating activity below
        mFileListSource = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mFileListSource.add(new Pair<>(Long.valueOf(i), "Item " + i));
        }

        setContentView(R.layout.activity_merge_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.merge_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_source) {
            return true;
        }
        if (id == R.id.merge_now) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
