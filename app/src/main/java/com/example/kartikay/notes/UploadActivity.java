package com.example.kartikay.notes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

public class UploadActivity extends AppCompatActivity {
    ImageView iv;
    Button browse,upload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        browse=(Button)findViewById(R.id.browse_button);
        upload=(Button)findViewById(R.id.upload_button);

    }

}
