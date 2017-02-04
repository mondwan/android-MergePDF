package com.practical_developer.mergepdf;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.woxthebox.draglistview.DragListView;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link FileListFragmentCallbacks}
 * interface.
 */
public class FileListFragment extends Fragment
    implements FileItemRecyclerViewAdapter.FileItemAdapterCallback {

    private FileListFragmentCallbacks mFileListFragmentCallback;
    private DragListView mDragListView;
    private FileItemRecyclerViewAdapter mFileItemRecyclerViewAdapter;

    private static final String FILE_LIST_TAG = "File List";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FileListFragment() {
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

        mFileItemRecyclerViewAdapter = new FileItemRecyclerViewAdapter(
            mFileListFragmentCallback.getFileListSource(),
            this
        );

        mDragListView = (DragListView) view.findViewById(R.id.file_list);
        mDragListView.getRecyclerView().setVerticalScrollBarEnabled(true);
        mDragListView.setLayoutManager(new LinearLayoutManager(getContext()));
        mDragListView.setAdapter(mFileItemRecyclerViewAdapter, true);
        mDragListView.setCanDragHorizontally(false);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(
            R.id.fab
        );
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(FILE_LIST_TAG, "Float button click");
                // TODO: Add a file source
                mFileListFragmentCallback.onAddFileItem();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FileListFragmentCallbacks) {
            mFileListFragmentCallback = (FileListFragmentCallbacks) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FileListFragmentCallbacks");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFileListFragmentCallback = null;
    }

    public void onClickDeleteIcon (int pos) {
        if (mFileListFragmentCallback.onDeleteFileItem(pos)) {
            mFileItemRecyclerViewAdapter.notifyItemRemoved(pos);
            mFileItemRecyclerViewAdapter.notifyItemChanged(
                pos,
                mFileListFragmentCallback.getFileListSource().size()
            );
        }
    }

    public interface FileListFragmentCallbacks {
        ArrayList<Pair<Long, String>> getFileListSource();
        /**
         * Define how to remove an item from the file list source
         * @param position Position of the item which is going to be deleted
         * @return True means deleted  successfully
         */
        boolean onDeleteFileItem(int position);

        /**
         * Define how to add  an item from the file list source
         * @return True means deleted  successfully
         */
        boolean onAddFileItem();
    }
}
