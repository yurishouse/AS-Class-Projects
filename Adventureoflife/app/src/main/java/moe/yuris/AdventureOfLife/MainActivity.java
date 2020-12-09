package moe.yuris.AdventureOfLife;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.file.FileSystemException;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ChoiceAdapter mAdapter;
    private TextView mMainTextView;
    private TextView mStatus;
    private Game game;

    private final LinkedList<String> mChoiceList = new LinkedList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Use IOStream to read from file
        try {
            InputStream is =  getResources().openRawResource(R.raw.game_data);
            Writer writer = new StringWriter();
            char[] buffer = new char[10240];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                is.close();
            }
            // save it to a String
            String jsonString = writer.toString();
            // pass it to game class, and also init it.
            game = new Game(jsonString);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_main);
        //Test code for recycler view
       // for (int i = 0; i < 20; i++) {
       //     mChoiceList.addLast("Word " + i);
       // }
        //locate main text view(s)
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

        // call the initial update
        onUpdatePage();


    }

    public void onUpdatePage() {
        // set main text to current story line
        mMainTextView.setText(game.getCurrentStageText());
        // clear recycler view
        mChoiceList.clear();
        // add choices back to recyclerview
        for (String s : game.getCurrentStageChoices()){
            mChoiceList.add(s);
        }
        // mRecyclerView.notify(); doesn't work
        // this works
        mAdapter.notifyDataSetChanged();
        // set Status text
        mStatus.setText(game.getCurrentStatusText());
    }

    public void choiceCallback(int index){
        // a callback method
        game.chooseAndAdvance(index);
        // call for update
        onUpdatePage();
    }

    public void Load(View view) {
        //TODO
    }

    public void Save(View view) {
        //TODO
    }

    public void ShowLog(View view) {
        //TODO
    }

    public void Exit(View view) {
        // Simple Exit Dialog, nothing fancy.
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