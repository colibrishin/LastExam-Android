package com.test.lastexam_20151652;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MemoActivity extends AppCompatActivity {
    private DBManager DBMS;
    private SQLiteDatabase db;

    private String curr_path;

    boolean isExist = false;

    private EditText tvPath;
    private EditText tvDate;
    private EditText tvTitle;
    private EditText tvDesc;

    private String hash;
    private String path;
    private String date;
    private String title;
    private String description;

    @Override
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        tvPath = findViewById(R.id.Memo_filename);
        tvDate = findViewById(R.id.memo_date);
        tvTitle = findViewById(R.id.memo_title);
        tvDesc = findViewById(R.id.memo_description);

        DBMS = new DBManager(this);
        Intent intent = getIntent();

        path = intent.getStringExtra("src");
        curr_path = intent.getStringExtra("curr_path");

        try {
            hash = new Hash(path).getDigest();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        Cursor cursor;
        db = DBMS.getReadableDatabase();
        cursor = db.query(DBManager.TABLE_NAME, null, "hash=?", new String[]{hash},null,null,null,null);


        if(!cursor.moveToNext()) {
            isExist = false;
        } else {
            date = cursor.getString(cursor.getColumnIndex("date"));
            path = cursor.getString(cursor.getColumnIndex("path"));
            title = cursor.getString(cursor.getColumnIndex("title"));
            description = cursor.getString(cursor.getColumnIndex("description"));

            tvDate.setText(date);
            tvTitle.setText(title);
            tvDesc.setText(description);
            isExist = true;
        }

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int YEAR = c.get(Calendar.YEAR);
                int MONTH = c.get(Calendar.MONTH);
                int DAY = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MemoActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Date date = new Date(year - 1900, month, dayOfMonth);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
                        tvDate.setText(simpleDateFormat.format(date));
                    }
                }, YEAR, MONTH, DAY);
                datePickerDialog.show();
            }
        });

        tvPath.setText(path);
        cursor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_memo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.memo_memo_back) {
            db.close();
            DBMS.close();

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("curr_path", curr_path);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void modifyMemo(View v) {
        if(db.isOpen()) db.close();
        long isSuccess = 0;
        ContentValues contentValues = new ContentValues();
        date = tvDate.getText().toString();
        description = tvDesc.getText().toString();
        title = tvTitle.getText().toString();

        contentValues.put("hash", hash);
        contentValues.put("path", path);
        contentValues.put("date", date);
        contentValues.put("title", title);
        contentValues.put("description", description);

        db = DBMS.getWritableDatabase();
        if (isExist)
            isSuccess = db.update(DBManager.TABLE_NAME, contentValues, "hash=?", new String[]{hash});
        else
            isSuccess = db.insert(DBManager.TABLE_NAME, null, contentValues);

        if (isSuccess > 0) {
            Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();
            isExist = true;
        }
        else Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show();
    }

    public void deleteMemo(View v){
        if(!isExist) {
            Toast.makeText(getApplicationContext(), "Memo is not on the list.", Toast.LENGTH_LONG).show();
        } else {
            if(db.isOpen()) db.close();
            db = DBMS.getWritableDatabase();

            if(db.delete(DBManager.TABLE_NAME, "hash=?", new String[]{hash}) > 0) {
                Toast.makeText(getApplicationContext(), "Delete Successful.", Toast.LENGTH_LONG).show();
                tvDate.setText("Date");
                tvDesc.setText("Description");
                tvTitle.setText("Title");
            }
        }
    }
}