package com.example.chechboxes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    CheckBox[] arr = new CheckBox[5];

    String[] arr2 = new String[]{
      "Chocolate_syrup","sprinkles","Crushed_nuts","Cherries","Oreo_cookie_crumbles"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arr[0] = findViewById(R.id.Chocolate_syrup);
        arr[1] = findViewById(R.id.sprinkles);
        arr[2] = findViewById(R.id.Crushed_nuts);
        arr[3] = findViewById(R.id.Cherries);
        arr[4] = findViewById(R.id.Oreo_cookie_crumbles);

    }

    public void ShowToast(View view) {
        String topping = "Toppings:";
        for (int i = 0; i < 5; i++){
            if(arr[i].isChecked()){
                topping += arr[i].getText();
            }
        }
        Toast.makeText(this,topping,Toast.LENGTH_LONG).show();



    }
}