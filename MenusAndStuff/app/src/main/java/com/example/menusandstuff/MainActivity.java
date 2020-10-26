package com.example.menusandstuff;
// made by Zhongli Liang

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

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
    public void showDatePicker(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(),"datePicker");
    }
    public void processDatePickerResult(int year, int month, int day) {
        String month_string = Integer.toString(month+1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        String dateMessage = ("Date: "+ year_string+ "-" +month_string  + "-" +day_string );
        TextView Date_text = findViewById(R.id.date_text);
        Date_text.setText(dateMessage);
    }
    public void showTimePicker(View view) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(),
                getString(R.string.set_time));

    }
    /**
     * Process the time picker result into strings that can be displayed in
     * a Toast.
     *
     * @param hourOfDay Chosen hour
     * @param minute Chosen minute
     */
    public void processTimePickerResult(int hourOfDay, int minute) {
        // Convert time elements into strings.
        String hour_string = Integer.toString(hourOfDay);
        String minute_string = Integer.toString(minute);
        // Assign the concatenated strings to timeMessage.
        String timeMessage = ("Time: "+hour_string + ":" + minute_string);
        TextView Time_Text = findViewById(R.id.time_text);
        Time_Text.setText(timeMessage);
    }
    public void exitDialog(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Exit");
        builder.setMessage("Exit now?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i){finish();}
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                Toast toast = Toast.makeText(MainActivity.this, R.string.exit_text , Toast.LENGTH_SHORT);
                toast.show();
                dialogInterface.dismiss()
                ;}
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    }

