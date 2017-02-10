package com.practical_developer.mergepdf;

import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.practical_developer.mergepdf.FileListFragment.FileListFragmentCallbacks;
import com.practical_developer.mergepdf.file.FileItem;
import com.woxthebox.draglistview.DragItemAdapter;

import java.util.ArrayList;
import java.util.Locale;

/**
 * {@link RecyclerView.Adapter} that can display a {@link FileItem} and makes a call to the
 * specified {@link FileListFragmentCallbacks}.
 */
@SuppressWarnings("CanBeFinal")
public class FileItemRecyclerViewAdapter extends DragItemAdapter<
    Pair<Long, FileItem>,
    FileItemRecyclerViewAdapter.ViewHolder>
{
    @SuppressWarnings("CanBeFinal")
    private FileItemAdapterCallback mFileItemAdapterCallback;

    public FileItemRecyclerViewAdapter(
        ArrayList<Pair<Long, FileItem>> list,
        FileItemAdapterCallback cb
    ) {
        setHasStableIds(true);
        setItemList(list);
        mFileItemAdapterCallback = cb;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
            R.layout.file_item,
            parent,
            false
        );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        FileItem f = mItemList.get(position).second;
        holder.mFileName.setText(f.getFileName());
        holder.mFileType.setText(f.getFileType());
    }

    @Override
    public long getItemId(int position) {
        return mItemList.get(position).first;
    }

    class ViewHolder extends DragItemAdapter.ViewHolder
        implements View.OnClickListener {
        @SuppressWarnings("unused")
        final View mView;
        @SuppressWarnings("unused")
        final ImageView mDraggable;
        final TextView mFileName;
        final TextView mFileType;
        final ImageView mDeleteFile;
        private static final boolean mDragOnLongPress = false;
        private static final String POSITION_TAG = "ViewHolderPosition";

        ViewHolder(View view) {
            super(view, R.id.draggable_file_item, mDragOnLongPress);

            mView = view;
            mDraggable = (ImageView) view.findViewById(R.id.draggable_file_item);
            mFileName = (TextView) view.findViewById(R.id.file_item_name);
            mFileType = (TextView) view.findViewById(R.id.file_item_type);
            mDeleteFile = (ImageView) view.findViewById(R.id.remove_file_item);

            mDeleteFile.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = this.getAdapterPosition();
            Log.d(
                    POSITION_TAG,
                    String.format(
                        Locale.US,
                        "click at AP%d LP%d",
                        pos,
                        this.getLayoutPosition()
                    )
            );

            mFileItemAdapterCallback.onClickDeleteIcon(pos);
        }
    }

    public interface FileItemAdapterCallback {
        /**
         * Click handler when users click the delete icon
         * @param position Position of the item which is going to be deleted
         */
        void onClickDeleteIcon(int position);
    }
}
