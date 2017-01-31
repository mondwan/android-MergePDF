package com.practical_developer.mergepdf;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.practical_developer.mergepdf.dummy.DummyContent.DummyItem;
import com.woxthebox.draglistview.DragListView;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FileListFragment extends Fragment
    implements FileItemRecyclerViewAdapter.FileItemAdapterCallback {

    private OnListFragmentInteractionListener mListener;

    private DragListView mDragListView;
    private ArrayList<Pair<Long, String>> mItemArray;
    private static final String FILE_LIST_TAG = "File List";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FileListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FileListFragment newInstance(int columnCount) {
        FileListFragment fragment = new FileListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.file_item_list, container, false);

        mDragListView = (DragListView) view.findViewById(R.id.file_list);
        mDragListView.getRecyclerView().setVerticalScrollBarEnabled(true);

        mItemArray = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mItemArray.add(new Pair<>(Long.valueOf(i), "Item " + i));
        }

        mDragListView.setLayoutManager(new LinearLayoutManager(getContext()));
        FileItemRecyclerViewAdapter listAdapter = new FileItemRecyclerViewAdapter(
            mItemArray,
            this
        );
        mDragListView.setAdapter(listAdapter, true);
        mDragListView.setCanDragHorizontally(false);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public boolean onDeleteFileItem(int pos) {
        boolean ret = true;

        try {
            mItemArray.remove(pos);
        } catch (IndexOutOfBoundsException e) {
            ret = false;
        }

        return ret;
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }
}
