package com.practical_developer.mergepdf;

import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.practical_developer.mergepdf.FileListFragment.OnListFragmentInteractionListener;
import com.practical_developer.mergepdf.dummy.DummyContent.DummyItem;
import com.woxthebox.draglistview.DragItemAdapter;

import java.util.ArrayList;
import java.util.Locale;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class FileItemRecyclerViewAdapter extends DragItemAdapter<Pair<Long, String>, FileItemRecyclerViewAdapter.ViewHolder> {
    public FileItemRecyclerViewAdapter(ArrayList<Pair<Long, String>> list) {
        setHasStableIds(true);
        setItemList(list);
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
        String text = mItemList.get(position).second;
        holder.mFileName.setText(text);
        holder.mFileType.setText("PDF");
        holder.itemView.setTag(text);
    }

    @Override
    public long getItemId(int position) {
        return mItemList.get(position).first;
    }

    public class ViewHolder extends DragItemAdapter.ViewHolder
        implements View.OnClickListener {
        public final View mView;
        public final ImageView mDraggable;
        public final TextView mFileName;
        public final TextView mFileType;
        public final ImageView mDeleteFile;
        private static final boolean mDragOnLongPress = false;
        private static final String POSITION_TAG = "ViewHolder Position";

        public ViewHolder(View view) {
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
            mItemList.remove(pos);
            notifyItemRemoved(pos);
            notifyItemChanged(pos, mItemList.size());
        }

        @Override
        public void onItemClicked(View view) {
        }
    }
}
