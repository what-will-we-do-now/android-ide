package uk.ac.tees.v8036651.mode.FileViewer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import uk.ac.tees.v8036651.mode.R;

public class TextAdapter extends BaseAdapter {

    private List<String> data = new ArrayList<>();

    public void setData(List<String> data) {
        if(data != null){
            this.data.clear();
            if (data.size() > 0){
                this.data.addAll(data);
            }
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public String getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_viewer_item, parent, false);
            convertView.setTag(new ViewHolder((TextView) convertView.findViewById(R.id.fileViewerItem)));
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        final String item = getItem(position);
        holder.info.setText(item);
        return convertView;
    }


}
