package com.example.eqoram.alpha;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Stefan on 02.12.2016.
 */

public class MySimpleArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] name;
    private final String[] nachricht;
    private final String[] time;

    public MySimpleArrayAdapter(Context context, String[] name, String[] nachricht, String[] time) {
        super(context, R.layout.list_item_chat_overview, name);
        this.context = context;
        this.name = name;
        this.nachricht = nachricht;
        this.time = time;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item_chat_overview, parent, false);
        TextView textViewFirstLine = (TextView) rowView.findViewById(R.id.name);
        TextView textViewSecondLine = (TextView) rowView.findViewById(R.id.nachricht);
        TextView textViewTime = (TextView) rowView.findViewById(R.id.time);
        textViewFirstLine.setText(name[position]);
        textViewSecondLine.setText(nachricht[position]);
        textViewTime.setText(time[position]);
        return rowView;

    }
}
