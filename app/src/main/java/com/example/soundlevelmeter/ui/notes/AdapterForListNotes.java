package com.example.soundlevelmeter.ui.notes;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.soundlevelmeter.R;

import java.util.List;


public class AdapterForListNotes extends ArrayAdapter<Note> {

    private int resourse;
    private LayoutInflater inflater;


    AdapterForListNotes(@NonNull Context context, int resource, @NonNull List<Note> objects) {
        super(context, resource, objects);
        this.resourse = resource;
        this.inflater = LayoutInflater.from(context);

    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(this.resourse, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Note note = getItem(position);

        viewHolder.textViewName.setText(note.getNameNote());
        viewHolder.textViewContent.setText(note.getContentNote());

        viewHolder.imageButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert(position);
            }
        });


        return convertView;
    }



    private class ViewHolder {
        final TextView textViewName;
        final TextView textViewContent;
        final ImageButton imageButtonDelete;

        ViewHolder(View view) {
            textViewName = view.findViewById(R.id.text_view_name_item);
            textViewContent = view.findViewById(R.id.text_view_content_item);
            imageButtonDelete = view.findViewById(R.id.button_delete_note_in_notes);
        }
    }


    private void showAlert(final int position) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setMessage(R.string.message_alert_delete_note);

        DialogInterface.OnClickListener clickListenerPositive = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                remove(getItem(position));
                notifyDataSetChanged();
            }
        };

        DialogInterface.OnClickListener clickListenerNegative = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        };

        alert.setPositiveButton(R.string.yes, clickListenerPositive);
        alert.setNegativeButton(R.string.no, clickListenerNegative);
        alert.setCancelable(true);
        alert.create();
        alert.show();


    }



}


