package com.practical_developer.mergepdf;

import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.practical_developer.mergepdf.file.FileItem;

import java.util.ArrayList;

public class MergeSettingActivity extends AppCompatActivity
        implements FileListFragment.FileListFragmentCallbacks {

    private ArrayList<Pair<Long, FileItem>> mFileListSource;
    private static final String MERGE_SETTING_TAG = "MergeSetting";

    public ArrayList<Pair<Long, FileItem>> getFileListSource() {
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

    public int onAddFileItem(FileItem item) {
        Long id = (long) 0;
        boolean hasFound = true;

        // Loop from 0 until there is a single ID does not used
        while (hasFound) {
            hasFound = false;
            for (Pair<Long, FileItem> p : mFileListSource) {
                Long _id = p.first;
                if (_id.longValue() == id.longValue()) {
                    id++;
                    hasFound = true;
                    break;
                }
            }
        }

        int index = mFileListSource.size();

        Log.d(
            MERGE_SETTING_TAG,
            String.format(
                "File |%s| type |%s| with id |%d| at |%d|",
                item.getFileName(),
                item.getFileType(),
                id,
                index
            )
        );

        mFileListSource.add(new Pair<>(id, item));

        return index;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize mFileListSource before inflating activity below
        mFileListSource = new ArrayList<>();

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
        if (id == R.id.merge_now) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
