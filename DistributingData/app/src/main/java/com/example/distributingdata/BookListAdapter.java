package com.example.distributingdata;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.WordViewHolder> {

    public BookListAdapter(Context context, LinkedList<String> wordList) {
        mInflater = LayoutInflater.from(context);
        this.mWordList = wordList;
    }

    private LayoutInflater mInflater;

    private final LinkedList<String> mWordList;

    class WordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView BookItemView;
        final BookListAdapter mAdapter;
        public WordViewHolder(View itemView, BookListAdapter adapter) {
            super(itemView);
            BookItemView = itemView.findViewById(R.id.BookView);
            this.mAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // Get the position of the item that was clicked.
            int mPosition = getLayoutPosition();
            // Use that to access the affected item in mWordList.
            String element = mWordList.get(mPosition);
            // Change the word in the mWordList.
            mWordList.set(mPosition, "Clicked! " + element);
            // Notify the adapter, that the data has changed so it can
            // update the RecyclerView to display the data.
            mAdapter.notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
        View mItemView = mInflater.inflate(R.layout.booklist_item,
                parent, false);
        return new WordViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        String mCurrent = mWordList.get(position);
        holder.BookItemView.setText(mCurrent);
    }

    @Override
    public int getItemCount() {
        return mWordList.size();
    }
}