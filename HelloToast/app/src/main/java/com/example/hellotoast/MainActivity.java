package com.example.hellotoast;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private int mCount;
    private TextView mShowCount;
    private boolean image = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mShowCount = (TextView) findViewById(R.id.show_count);
    }

    public void showToast(View view) {
        Toast toast = Toast.makeText(this, R.string.toast_message,
                Toast.LENGTH_SHORT);
        toast.show();
    }

    public void countUp(View view) {
        mCount++;
        if (mShowCount != null)
            mShowCount.setText(Integer.toString(mCount));
    }

    public void Double_count(View view) {
        mCount = mCount * 2;
        mShowCount.setText(Integer.toString(mCount));
    }

    public void Reset_count(View view) {
        mCount =0;
        mShowCount.setText("0");
    }

    public void Toggle_image(View view) {
        if (image){
        mShowCount.setBackground(getResources().getDrawable(R.drawable.android));
        mShowCount.invalidate();
        image = false;
        } else {
            mShowCount.setBackground(getResources().getDrawable(R.color.colorPrimary));
            mShowCount.invalidate();
            image = true;
        }
    }
}