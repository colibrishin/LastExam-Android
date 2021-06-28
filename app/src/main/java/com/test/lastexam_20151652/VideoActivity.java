package com.test.lastexam_20151652;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {
    private String curr_path;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        videoView = findViewById(R.id.videoView);

        Intent intent = getIntent();
        curr_path = intent.getStringExtra("curr_path");
        String src = intent.getStringExtra("src");

        videoView.setVideoPath(src);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_video, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_video_back) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("curr_path", curr_path);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void videoPlay(View v){
        videoView.start();
    }
    public void videoPause(View v){
        videoView.pause();
    }
    public void videoStop(View v) {
        videoView.pause();
        videoView.seekTo(0);
    }
}