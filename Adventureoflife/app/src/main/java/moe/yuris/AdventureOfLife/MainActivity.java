package moe.yuris.AdventureOfLife;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ChoiceAdapter mAdapter;
    private TextView mMainTextView;
    private TextView mStatus;

    private final LinkedList<String> mChoiceList = new LinkedList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 0; i < 20; i++) {
            mChoiceList.addLast("Word " + i);
        }
        //locate main text view
        mMainTextView = findViewById(R.id.MainTextView);
        mStatus = findViewById(R.id.Status);

        // Get a handle to the RecyclerView.
        mRecyclerView = findViewById(R.id.ChoiceView);
        // Create an adapter and supply the data to be displayed.
        mAdapter = new ChoiceAdapter(this, mChoiceList);
        // Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    public void Load(View view) {
    }

    public void Save(View view) {
    }

    public void ShowLog(View view) {
    }

    public void Exit(View view) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing Game")
                .setMessage("Are you sure you want to close the Game?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

}