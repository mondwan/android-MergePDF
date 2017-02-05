package com.practical_developer.mergepdf;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.practical_developer.mergepdf.file.FileItem;
import com.woxthebox.draglistview.DragListView;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link FileListFragmentCallbacks}
 * interface.
 */
public class FileListFragment extends Fragment
    implements FileItemRecyclerViewAdapter.FileItemAdapterCallback {

    private FileListFragmentCallbacks mFileListFragmentCallback;
    private FileItemRecyclerViewAdapter mFileItemRecyclerViewAdapter;

    private static final String FILE_LIST_TAG = "FileList";
    private static final int FILE_SELECT_CODE = 0;

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

        DragListView mDragListView = (DragListView) view.findViewById(
            R.id.file_list
        );
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
                Log.d(
                    FILE_LIST_TAG,
                    "Prompt a file dialog for selecting source"
                );

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                try {
                    startActivityForResult(
                        Intent.createChooser(
                            intent,
                            "Select a PDF"
                        ),
                        FILE_SELECT_CODE
                    );
                } catch (android.content.ActivityNotFoundException ex) {
                    // Potentially direct the user to the Market with a Dialog
                    Toast.makeText(
                        view.getContext(),
                        "Please install a File Manager.",
                        Toast.LENGTH_SHORT
                    ).show();
                }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request it is that we're responding to
        Log.d(
            FILE_LIST_TAG,
            String.format(
                "OnActivityResult with req Code %d res Code %d",
                requestCode,
                resultCode
            )
        );

        if (requestCode == FILE_SELECT_CODE) {

            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                FileItem i = createFileItemFromURI(uri);
                mFileListFragmentCallback.onAddFileItem(i);
                mFileItemRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private FileItem createFileItemFromURI(Uri uri) {
        // Get meta information for a given file inside the uri
        Cursor c = getContext().getContentResolver().query(
            uri,
            null,
            null,
            null,
            null
        );

        // Reference to the final created file item
        FileItem ret = null;

        if (c != null) {
            // Get index of the file name from cursor
            int nameIndex = c.getColumnIndex(OpenableColumns.DISPLAY_NAME);

            // Reset the cursor such that we can fetch file name by index
            c.moveToFirst();

            // Get file name by using the given index
            String fileName = c.getString(nameIndex);

            // Get the file type from mime
            String mime = getContext().getContentResolver().getType(uri);
            String fileType = mime;
            if (mime != null) {
                int lastSlash = mime.lastIndexOf('/');
                fileType = mime.substring(lastSlash + 1);
            }

            // Close the cursor
            c.close();

            Log.d(
                FILE_LIST_TAG,
                String.format(
                    "File Name |%s| File Type |%s|",
                    fileName,
                    fileType
                )
            );

            ret = new FileItem(fileName, fileType, uri);
        } else {
            Log.e(
                FILE_LIST_TAG,
                String.format(
                    "Unable to get a cursor from given uri |%s|",
                    uri.toString()
                )
            );
        }

        return ret;
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
        ArrayList<Pair<Long, FileItem>> getFileListSource();
        /**
         * Define how to remove an item from the file list source
         * @param position Position of the item which is going to be deleted
         * @return True means deleted  successfully
         */
        boolean onDeleteFileItem(int position);

        /**
         * Define how to add  an item from the file list source
         * @param item File item selected by users
         * @return Position of the item in the array
         */
        int onAddFileItem(FileItem item);
    }
}
