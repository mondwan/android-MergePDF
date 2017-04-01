package com.practical_developer.mergepdf;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.woxthebox.draglistview.DragListView;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * A custom view assertion asserts the position of file item with given file name
 */
public class FileItemPositionAssertion implements ViewAssertion {
    private final String mFileName;
    private final int mPosition;

    public FileItemPositionAssertion (String fileName, int position) {
        mFileName = fileName;
        mPosition = position;
    }

    @Override
    public void check (
        View view,
        NoMatchingViewException noViewFoundException
    ) {
        if (noViewFoundException != null) {
            throw noViewFoundException;
        }

        DragListView dlv = (DragListView) view;
        RecyclerView recyclerView = dlv.getRecyclerView();
        FileItemRecyclerViewAdapter.ViewHolder vh =
            (FileItemRecyclerViewAdapter.ViewHolder)
                recyclerView.findViewHolderForLayoutPosition(mPosition);

        assertThat(vh.mFileName.getText().toString(), is(mFileName));
    }
}
