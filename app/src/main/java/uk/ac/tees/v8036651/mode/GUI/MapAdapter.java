package uk.ac.tees.v8036651.mode.GUI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class is based on android's implementation of ArrayAdapter
 */

public class MapAdapter extends BaseAdapter {

    private LinkedHashMap<String, String> data;
    private Context context;
    private int resource;


    public MapAdapter(Context context, int resource, Map<String, String> data){
        this(context, resource, new LinkedHashMap<>(data));
    }

    public MapAdapter(Context context, int resource, LinkedHashMap<String, String> data){
        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return (new ArrayList<>(data.keySet())).get(position);
    }

    @Override
    public long getItemId(int position) {
        //the position should be the same in rows and data
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return createViewFromResource(LayoutInflater.from(context), position, convertView, parent, resource);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(LayoutInflater.from(context), position, convertView, parent, resource);
    }

    private View createViewFromResource(LayoutInflater inflater, int position, View convertView, ViewGroup parent, int resource){
        View view;
        TextView text;

        if(convertView == null){
            view = inflater.inflate(resource, parent, false);
        }else{
            view = convertView;
        }

        text = (TextView) view;

        text.setText(data.get(getItem(position).toString()));

        return view;
    }
}
