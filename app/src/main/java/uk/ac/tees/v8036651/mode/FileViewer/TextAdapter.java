package uk.ac.tees.v8036651.mode.FileViewer;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.ac.tees.v8036651.mode.R;

public class TextAdapter extends BaseAdapter {

    private List<File> data = new ArrayList<>();

    public void setData(List<File> data) {
        if(data != null){
            this.data.clear();
            if (data.size() > 0){
                this.data.addAll(data);
            }
            Collections.sort(data);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public File getItem(int position) {
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
            convertView.setTag(R.id.fileViewerItem, new ViewHolder((TextView) convertView.findViewById(R.id.fileViewerItem)));
            convertView.setTag(R.id.icon, new ViewHolder((ImageView) convertView.findViewById(R.id.icon)));
        }

        final File item = getItem(position);

        ViewHolder holder = (ViewHolder) convertView.getTag(R.id.fileViewerItem);
        holder.textInfo.setText(item.getName().substring(item.getName().lastIndexOf('/') + 1));

        if(selection != null) {
            if (selection[position]) {
                TypedValue typedValue = new TypedValue();
                parent.getContext().getTheme().resolveAttribute(R.attr.backgroundSelected, typedValue, true);
                int color = typedValue.data;
                ((LinearLayout) holder.textInfo.getParent()).setBackgroundColor(color);
            } else {
                TypedValue typedValue = new TypedValue();
                parent.getContext().getTheme().resolveAttribute(android.R.attr.colorBackground, typedValue, true);
                int color = typedValue.data;
                ((LinearLayout) holder.textInfo.getParent()).setBackgroundColor(color);
            }
        }

        holder = (ViewHolder) convertView.getTag(R.id.icon);

        if (item.isDirectory()){
            holder.imageInfo.setImageResource(R.drawable.ic_folder_black_24dp);
        }
        else {
            holder.imageInfo.setImageResource(R.drawable.ic_file_black_24dp);
        }

        return convertView;
    }

    private boolean[] selection;

    void setSelection(boolean[] selection){
        if(selection != null){
            this.selection = new boolean[selection.length];

            for(int i = 0; i < selection.length; i++){
                this.selection[i] = selection[i];
            }
            notifyDataSetChanged();
        }
    }
}
