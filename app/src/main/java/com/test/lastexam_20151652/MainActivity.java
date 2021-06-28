package com.test.lastexam_20151652;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private String path;
    private String root;
    private File[] files;
    private ListView listView;
    private FileAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }

        Intent intent = getIntent();
        String intent_path = intent.getStringExtra("curr_path");
        root = Environment.getExternalStorageDirectory().toString();

        if (intent_path == null)
            path = root;
        else
            path = intent_path;

        listView = findViewById(R.id.list_file);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File f = (File) adapter.getItem(position);
                if (f.isDirectory()) {
                    goChildDirectory(f.getName());
                    refreshList();
                }
                else if (f.isFile()) {
                    String file_path = f.getPath();
                    Intent intent = null;
                    String url = null;

                    try {
                        url = URLEncoder.encode(file_path, "UTF-8").replace("+", "%20");
                    } catch (UnsupportedEncodingException e) {
                        url = file_path;
                    }
                    String fileExtension = MimeTypeMap.getFileExtensionFromUrl(url).toLowerCase();
                    String MIME = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);

                    if (MIME == null){
                        Toast.makeText(getApplicationContext(), "Unknown Error", Toast.LENGTH_LONG).show();
                    } else {
                        MIME = MIME.split("/")[0];

                        if (MIME.equals("image"))
                            intent = new Intent(getApplicationContext(), PictureActivity.class);
                        else if (MIME.equals("video"))
                            intent = new Intent(getApplicationContext(), VideoActivity.class);
                        else {
                            Toast.makeText(getApplicationContext(), "Not Supported File", Toast.LENGTH_LONG).show();
                            return;
                        }

                        intent.putExtra("src", file_path);
                        intent.putExtra("curr_path", path);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent_long = new Intent(getApplicationContext(), MemoActivity.class);

                File f = adapter.getItem(position);
                if(f.isFile()) {
                    intent_long.putExtra("src", f.getPath());
                    intent_long.putExtra("curr_path", path);
                    startActivity(intent_long);
                    finish();
                }
                else
                    Toast.makeText(MainActivity.this, "Only Adding Memo on file is allowed", Toast.LENGTH_LONG).show();
                return true;
            }
        });

        refreshList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_main_back) {
            int last_divider = root.lastIndexOf('/');
            if (path.equals(root) || path.equals(root.substring(0, last_divider)) ) {
                Toast.makeText(this, "This is root directory", Toast.LENGTH_LONG).show();
            } else {
                goParentDirectory();
                refreshList();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    protected void refreshList(){
        getDirectory();
        listDirectory();
    }

    protected void getDirectory() {
        File dir = new File(path);
        files = dir.listFiles();
    }

    protected void goParentDirectory(){
        int last_divider = path.lastIndexOf('/');
        path = path.substring(0, last_divider);

        getDirectory();
    }

    protected void goChildDirectory(final String dir_name){
        path = path + "/" + dir_name;
    }

    protected void listDirectory(){
        if(files == null) {
            Toast.makeText(this, "Access Denied", Toast.LENGTH_LONG).show();
            goParentDirectory();
            getDirectory();
        }
        adapter = new FileAdapter(this, R.layout.activity_main, Arrays.asList(files));
        listView.setAdapter(adapter);
    }
}