package com.test.lastexam_20151652;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.io.File;
import java.util.List;

public class FileAdapter extends ArrayAdapter<File> {
    public FileAdapter(@NonNull Context context, int resource, @NonNull List<File> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        File f = getItem(position);
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.main_item_layout, parent, false);

        TextView tvName = convertView.findViewById(R.id.tvFilename);
        tvName.setText(f.getName());

        return convertView;
    }
}
