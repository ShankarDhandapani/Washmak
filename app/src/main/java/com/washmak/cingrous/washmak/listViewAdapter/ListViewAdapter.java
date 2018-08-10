package com.washmak.cingrous.washmak.listViewAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.washmak.cingrous.washmak.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ListViewAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private ArrayList<String> dataSet;
    Date date = new Date();

    public ListViewAdapter(Activity context, ArrayList<String> floorname) {
        super(context, R.layout.list_view_items, floorname);
        // TODO Auto-generated constructor stub
        this.context=context;
        this.dataSet=floorname;

    }

    @SuppressLint("SetTextI18n")
    @NonNull
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"}) View rowView = inflater.inflate(R.layout.list_view_items, null, true);

        TextView floor_name = (TextView) rowView.findViewById(R.id.floor_name_from_list_view_items);
        TextView time = (TextView) rowView.findViewById(R.id.floor_time_from_list_view_items);


        @SuppressLint("SimpleDateFormat") DateFormat timeFormat = new SimpleDateFormat("h:mm a");
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        final String formattedTime = timeFormat.format(date.getTime());
        final String formattedDate = dateFormat.format(date.getTime());
        floor_name.setText(dataSet.get(position));
        time.setText(formattedDate + "  " + formattedTime);

        return rowView;
    };

}
