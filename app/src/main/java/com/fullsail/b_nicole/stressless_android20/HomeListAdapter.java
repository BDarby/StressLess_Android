package com.fullsail.b_nicole.stressless_android20;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;



public class HomeListAdapter extends BaseAdapter {

    Context context;
    ArrayList<MediaObject> source = new ArrayList<>();

    public HomeListAdapter(Context context) {
        this.context = context;
    }

    public void setSource(ArrayList<MediaObject> source) {
        this.source = source;
    }

    @Override
    public int getCount() {
        return source.size();
    }

    @Override
    public Object getItem(int i) {
        return source.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        MediaObject mediaObject = source.get(i);

        View viewHolder;

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.media_list_item, null);
        }

        viewHolder = view;

        ImageView imageView = viewHolder.findViewById(R.id.item_image_view);
        TextView textView = viewHolder.findViewById(R.id.item_title);

        imageView.setImageResource(mediaObject.getImageResource());

        String titleName = mediaObject.getSourceName();
        textView.setText(titleName);

        viewHolder.setTag(mediaObject);
        return viewHolder;
    }
}
