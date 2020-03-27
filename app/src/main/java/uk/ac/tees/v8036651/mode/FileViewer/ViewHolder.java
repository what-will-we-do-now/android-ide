package uk.ac.tees.v8036651.mode.FileViewer;

import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder {
    TextView textInfo;
    ImageView imageInfo;

    ViewHolder(TextView info){
        this.textInfo = info;
    }


    ViewHolder(ImageView info) { this.imageInfo = info; }
}