package com.practical_developer.mergepdf;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.CoordinatesProvider;
import android.support.test.espresso.action.GeneralLocation;
import android.support.test.espresso.action.GeneralSwipeAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Swipe;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.woxthebox.draglistview.DragListView;

import org.hamcrest.Matcher;

import static org.hamcrest.Matchers.allOf;

/**
 * A custom action swaps position of two given file items with given file names
 */
public class MoveFileItemAction implements ViewAction {
    // Layout position of the source file item
    final int mSrcPosition;

    // Layout position of the destination file item
    final int mDstPosition;

    public MoveFileItemAction (int src, int dst) {
        mSrcPosition = src;
        mDstPosition = dst;
    }

    @Override
    public Matcher<View> getConstraints () {
        return allOf(
            ViewMatchers.isDisplayed(),
            ViewMatchers.isAssignableFrom(DragListView.class),
            FileItemMatcher.hasFileItemAt(mSrcPosition),
            FileItemMatcher.hasFileItemAt(mDstPosition)
        );
    }

    @Override
    public String getDescription() {
        return "Move a file item from and to given layout position";
    }

    @Override
    public void perform (UiController uiController, View view) {
        DragListView dlv = (DragListView) view;
        RecyclerView rv = dlv.getRecyclerView();
        final FileItemRecyclerViewAdapter.ViewHolder srcVH =
            (FileItemRecyclerViewAdapter.ViewHolder)
                rv.findViewHolderForLayoutPosition(mSrcPosition);
        final FileItemRecyclerViewAdapter.ViewHolder dstVH =
            (FileItemRecyclerViewAdapter.ViewHolder)
                rv.findViewHolderForLayoutPosition(mDstPosition);

        GeneralSwipeAction gsa = new GeneralSwipeAction(
            Swipe.FAST,
            new CoordinatesProvider() {
                @Override
                public float[] calculateCoordinates(View v) {
                    return GeneralLocation.CENTER.calculateCoordinates(
                        srcVH.mDraggable
                    );
                }
            },
            new CoordinatesProvider() {
                @Override
                public float[] calculateCoordinates(View v) {
                    return GeneralLocation.CENTER.calculateCoordinates(
                        dstVH.mDraggable
                    );
                }
            },
            Press.FINGER
        );
        gsa.perform(uiController, view);
    }
}
