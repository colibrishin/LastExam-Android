package com.test.lastexam_20151652;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.io.File;

public class PictureActivity extends AppCompatActivity {
    private ImageView imageView;
    private String curr_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        imageView = findViewById(R.id.imageView);

        Intent intent = getIntent();
        String strPath = intent.getStringExtra("src");
        curr_path = intent.getStringExtra("curr_path");

        Bitmap tmpBitmap = BitmapFactory.decodeFile(strPath);
        imageView.setImageBitmap(tmpBitmap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_picture, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_picture_back) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("curr_path", curr_path);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}