package com.practical_developer.mergepdf;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.woxthebox.draglistview.DragListView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

/**
 *  A custom matcher matches file item inside our file list
 */
public class FileItemMatcher {
    public static Matcher<View> hasFileItemAt(final int pos) {
        return new TypeSafeDiagnosingMatcher<View>() {
            @Override
            protected boolean matchesSafely (
                View item,
                Description mismatchDescription
            ) {
                if (!(item instanceof DragListView)) {
                    return false;
                }

                DragListView dlv = (DragListView) item;
                RecyclerView rv = dlv.getRecyclerView();
                RecyclerView.ViewHolder vh =
                    rv.findViewHolderForLayoutPosition(pos);

                return vh != null;
            }

            @Override
            public void describeTo (Description description) {
                description.appendText(
                    "has a file item at given position"
                );
            }
        };
    }
}
