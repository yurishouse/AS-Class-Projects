package com.example.droidcafe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;

public class OrderActivity extends AppCompatActivity {
    // init two global vars to store the message and item
    String message_received;
    String item_received;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        // Get the intent and its data.
        Intent intent = getIntent();

        TextView textView = findViewById(R.id.order_textview);
        //extract from bundle
        Bundle extras = intent.getExtras();
        message_received = extras.getString("EXTRA_MESSAGE");
        item_received = extras.getString("EXTRA_ITEM");

        textView.setText(message_received);
    }
// https://stackoverflow.com/questions/9948373/android-share-plain-text-using-intent-to-all-messaging-apps
    public void share_message(View view) {
        String mimeType = "text/plain";
        ShareCompat.IntentBuilder
                .from(this)
                .setType(mimeType)
                .setChooserTitle("Share this text with: ")
                .setText(message_received)
                .startChooser();

    }

    public void search_item(View view) {
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            // preform a query search
            intent.putExtra(SearchManager.QUERY, item_received);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);

        }
    }
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private boolean image_captured = false;
    public void capturePhoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            TextView textView = findViewById(R.id.launch_camera);
            textView.setText("Already took a picture");
            // Do other work with full size photo saved in locationForPhotos
        }
    }
}
//    TextView textView = findViewById(R.id.launch_camera);
//    textView.setText("Already took a picture");