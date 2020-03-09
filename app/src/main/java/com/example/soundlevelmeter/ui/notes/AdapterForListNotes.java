package com.example.soundlevelmeter.ui.notes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.soundlevelmeter.R;

import java.util.List;


public class AdapterForListNotes extends ArrayAdapter<Note> {

    private int resourses;
    private LayoutInflater inflater;

    public AdapterForListNotes(@NonNull Context context, int resource, @NonNull List<Note> objects) {
        super(context, resource, objects);
        this.resourses = resource;
        this.inflater = LayoutInflater.from(context);

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(this.resourses, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Note note = getItem(position);

        viewHolder.textViewName.setText(note.getNameNote());
        viewHolder.textViewContent.setText(note.getContentNote());


        return convertView;
    }


    private class ViewHolder {
        final TextView textViewName;
        final TextView textViewContent;

        ViewHolder(View view) {
            textViewName = view.findViewById(R.id.text_view_name_item);
            textViewContent = view.findViewById(R.id.text_view_content_item);
        }
    }

}


