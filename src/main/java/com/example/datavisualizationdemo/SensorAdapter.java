package com.example.datavisualizationdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

//build the fields and structure of the sensor list element
public class SensorAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    String[] sensors;
    String[] IDs;
    String[] descriptions;

    public SensorAdapter(Context c, String[] i, String[] p, String[] d){
        sensors = i;
        IDs = p;
        descriptions = d;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return sensors.length;
    }

    @Override
    public Object getItem(int i) {
        return sensors[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = mInflater.inflate(R.layout.sensor_listview_detail, null);
        TextView sensorTextView = (TextView) v.findViewById(R.id.sensor);
        TextView descriptionTextView = (TextView) v.findViewById(R.id.description);
        TextView IDTextView = (TextView) v.findViewById(R.id.ID);

        String sensor = sensors[i];
        String des = descriptions[i];
        String ID = IDs[i];

        sensorTextView.setText(sensor);
        descriptionTextView.setText(des);
        IDTextView.setText(ID);
        return v;
    }
}
