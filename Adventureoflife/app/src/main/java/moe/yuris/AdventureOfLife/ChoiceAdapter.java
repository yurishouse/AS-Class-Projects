package moe.yuris.AdventureOfLife;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

public class ChoiceAdapter extends RecyclerView.Adapter<ChoiceAdapter.ChoiceHolder> {

    public ChoiceAdapter(Context context, LinkedList<String> wordList) {
        mInflater = LayoutInflater.from(context);
        this.mChoiceList = wordList;
    }
    private LayoutInflater mInflater;

    private final LinkedList<String> mChoiceList;

    @NonNull
    @Override
    public ChoiceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.choice_items,
                parent, false);
        return new ChoiceHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ChoiceHolder holder, int position) {
        String mCurrent = mChoiceList.get(position);
        holder.ChoiceItemView.setText(mCurrent);

    }

    @Override
    public int getItemCount()  {
        return mChoiceList.size();
    }

    class ChoiceHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView ChoiceItemView;
        final ChoiceAdapter mAdapter;

        public ChoiceHolder(View itemView, ChoiceAdapter adapter) {
            super(itemView);
            ChoiceItemView = itemView.findViewById(R.id.Choices);
            this.mAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // Get the position of the item that was clicked.
            int mPosition = getLayoutPosition();
            // Use that to access the affected item in mWordList.
            String element = mChoiceList.get(mPosition);
            // Change the word in the mWordList.
            mChoiceList.set(mPosition, "Clicked! " + element);
            // Notify the adapter, that the data has changed so it can
            // update the RecyclerView to display the data.
            mAdapter.notifyDataSetChanged();
        }
    }
}
