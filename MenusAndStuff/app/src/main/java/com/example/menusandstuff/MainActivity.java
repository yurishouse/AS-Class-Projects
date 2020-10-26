package com.example.menusandstuff;
// made by Zhongli Liang

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        View Layout_main = findViewById(R.id.layout);
        setSupportActionBar(toolbar);
        // setting up the time for both view
        Calendar c = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        String date_formatted = date.format(c.getTime());
        SimpleDateFormat time = new SimpleDateFormat("HH:mm");
        String time_formatted = time.format(c.getTime());
        TextView date_view = findViewById(R.id.date_text);
        TextView time_view = findViewById(R.id.time_text);
        date_view.setText("Date: "+date_formatted);
        time_view.setText("Time: "+time_formatted);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }
    public void onRadioButtonClicked(View view) {
        View Layout_main = findViewById(R.id.main_layout);
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked.
        switch (view.getId()) {
            case R.id.setColor_cyan:
                if (checked)
                    Layout_main.setBackgroundColor(Color.parseColor("#66ffff"));


                break;
            case R.id.setColor_pink:
                if (checked)
                    Layout_main.setBackgroundColor(Color.parseColor("#FF80AB"));

                break;
            case R.id.setColor_red:
                if (checked)
                    Layout_main.setBackgroundColor(Color.parseColor("#ff5050"));
                break;

            case R.id.setColor_yellow:
                if (checked)
                    Layout_main.setBackgroundColor(Color.parseColor("#ffff66"));

                break;
            default:
                // Do nothing.
                break;
        }
    }
}

