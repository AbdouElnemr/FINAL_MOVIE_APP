package com.example.elnemr.final_movie_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by elnemr on 5/5/16.
 */
public class Trailer extends BaseAdapter {

    Context mycontext;
    List<Pojo> objects;

    public Trailer(Context context, List<Pojo> objects) {
        this.mycontext = context;
        this.objects = objects;
    }

    public class ViewHolder {
        ImageView imageView;
        TextView trailer;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Pojo pojo = (Pojo) objects.get(position);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater)
                    mycontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.trailer, null);

            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.trailerImage);
            holder.trailer = (TextView) convertView.findViewById(R.id.trailerText);



            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }



        holder.trailer.setText(objects.get(position).getTrailerText());

        return convertView;

    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);

    }
}
